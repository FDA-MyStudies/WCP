/** */
package com.fdahpstudydesigner.util;

import com.fdahpstudydesigner.bo.UserAttemptsBo;
import com.fdahpstudydesigner.bo.UserBO;
import com.fdahpstudydesigner.dao.AuditLogDAO;
import com.fdahpstudydesigner.dao.LoginDAOImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * An {@link AuthenticationProvider} implementation that retrieves user details from a {@link
 * UserDetailsService} and count the user fail login.
 *
 * @author BTC
 */
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

  private static Logger logger = LogManager.getLogger(LimitLoginAuthenticationProvider.class.getName());

  @Autowired private AuditLogDAO auditLogDAO;

  private LoginDAOImpl loginDAO;

  Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();

  /**
   * @see org.springframework.security.authentication.dao. AbstractUserDetailsAuthenticationProvider
   *     #authenticate(org.springframework.security.core.Authentication)
   */
  @Override
  public Authentication authenticate(Authentication authentication) {
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    final Integer MAX_ATTEMPTS = Integer.valueOf(propMap.get("max.login.attempts"));
    final Integer USER_LOCK_DURATION =
        Integer.valueOf(propMap.get("user.lock.duration.in.minutes"));
    final String lockMsg = propMap.get("user.lock.msg");
    try {
      ServletRequestAttributes attributes =
          (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      attributes.getRequest();
      String username = (String) authentication.getPrincipal();
      UserBO userBO =
          StringUtils.isNotBlank(username) ? loginDAO.getValidUserByEmail(username) : null;
      if (userBO == null) {
        auditLogDAO.saveToAuditLog(
            null,
            null,
            null,
            FdahpStudyDesignerConstants.USER_EMAIL_FAIL_ACTIVITY_MESSAGE,
            FdahpStudyDesignerConstants.USER_EMAIL_FAIL_ACTIVITY_DEATILS_MESSAGE,
            "LimitLoginAuthenticationProvider - authenticate()");
      }
      UserAttemptsBo userAttempts = loginDAO.getUserAttempts(authentication.getName());

      // Restricting the user to login for specified minutes if the user
      // has max fails attempts
      try {
        if (userAttempts != null
            && userAttempts.getAttempts() >= MAX_ATTEMPTS
            && new SimpleDateFormat(FdahpStudyDesignerConstants.DB_SDF_DATE_TIME)
                .parse(
                    FdahpStudyDesignerUtil.addMinutes(
                        userAttempts.getLastModified(), USER_LOCK_DURATION))
                .after(
                    new SimpleDateFormat(FdahpStudyDesignerConstants.DB_SDF_DATE_TIME)
                        .parse(FdahpStudyDesignerUtil.getCurrentDateTime()))) {
          throw new LockedException(lockMsg);
        }
      } catch (ParseException e) {
        logger.error("LimitLoginAuthenticationProvider - authenticate - ERROR", e);
      }

      // if reach here, means login success, else an exception will be
      // thrown
      // reset the user_attempts
      if (userBO != null) {
        if (StringUtils.isNotBlank(userBO.getSalt())) {
          if (StringUtils.equals(
              userBO.getUserPassword(),
              FdahpStudyDesignerUtil.getHashedPassword(
                  authentication.getCredentials().toString(), userBO.getSalt()))) {
            if (!userBO.isEnabled()) {
              throw new DisabledException(propMap.get("user.inactive.msg"));
            }
            if (!userBO.isCredentialsNonExpired()) {
              throw new CredentialsExpiredException(propMap.get("user.admin.forcepassword.msg"));
            }
            Authentication auth =
                new UsernamePasswordAuthenticationToken(
                    authentication.getPrincipal(),
                    null,
                    FdahpStudyDesignerUtil.buildUserAuthority(userBO.getPermissions()));
            if (userBO != null) {
              userBO.setUserLastLoginDateTime(FdahpStudyDesignerUtil.getCurrentDateTime());
              loginDAO.updateUser(userBO);
            }
            return auth;
          } else {
            throw new BadCredentialsException(propMap.get("user.login.failure"));
          }

        } else {
          Authentication auth =
              super.authenticate(
                  new UsernamePasswordAuthenticationToken(
                      authentication.getPrincipal(),
                      authentication.getCredentials(),
                      new ArrayList<GrantedAuthority>()));

          String rawSalt = FdahpStudyDesignerUtil.getUUID(RandomStringUtils.randomAlphanumeric(20));
          String hashedPassword =
              FdahpStudyDesignerUtil.getHashedPassword(
                  authentication.getCredentials().toString(), rawSalt);
          userBO.setUserPassword(hashedPassword);
          userBO.setSalt(rawSalt);
          String message = loginDAO.updateUser(userBO);
          if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            loginDAO.updateToHashedPassword(userBO.getUserId(), hashedPassword, rawSalt);
          }
          if (userBO != null) {
            userBO.setUserLastLoginDateTime(FdahpStudyDesignerUtil.getCurrentDateTime());
            loginDAO.updateUser(userBO);
          }
          return auth;
        }
      } else {
        throw new BadCredentialsException(propMap.get("user.login.failure"));
      }
    } catch (BadCredentialsException e) {

      // invalid login, update to user_attempts
      loginDAO.updateFailAttempts(authentication.getName());
      throw e;

    } catch (LockedException e) {

      logger.error(
          "LimitLoginAuthenticationProvider - authenticate - ERROR - this user is locked! ", e);
      String error;
      UserAttemptsBo userAttempts = loginDAO.getUserAttempts(authentication.getName());

      if (userAttempts != null) {
        error = lockMsg;
      } else {
        error = e.getMessage();
      }

      throw new LockedException(error);
    } catch (DisabledException e) {
      throw e;
    } catch (CredentialsExpiredException e) {
      throw e;
    }
  }

  @Autowired
  public void setLoginDAO(LoginDAOImpl loginDAO) {
    this.loginDAO = loginDAO;
  }
}
