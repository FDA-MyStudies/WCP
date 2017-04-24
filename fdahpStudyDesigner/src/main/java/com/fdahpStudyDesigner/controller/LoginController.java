package com.fdahpstudydesigner.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fdahpstudydesigner.bo.UserBO;
import com.fdahpstudydesigner.service.LoginServiceImpl;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;

/**
 * @author Ronalin
 *
 */
@Controller
public class LoginController {
	
	private static Logger logger = Logger.getLogger(LoginController.class.getName());
	
	private LoginServiceImpl loginService;
	/* Setter Injection */
	@Autowired
	public void setLoginService(LoginServiceImpl loginService) {
		this.loginService = loginService;
	}
	
	/**
	 * Navigate to login page
	 * @author Ronalin
	 *  
	 * @param error , the error message from Spring security 
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , View login page 
	 * @throws Exception 
	 */
	@RequestMapping(value ="/login.do")
	public ModelAndView loginPage(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
		String sucMsg = "";
		String errMsg = "";
		ModelMap map = new ModelMap();
		/*if (error != null && (error.equalsIgnoreCase("timeOut") || error.equalsIgnoreCase("multiUser"))) {
			map.addAttribute("errMsg", propMap.get("user.session.timeout"));
		} else if (error != null) {
			map.addAttribute("errMsg", FdahpStudyDesignerUtil.getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}*/
		if(null != request.getSession().getAttribute("sucMsg")){
			sucMsg = (String) request.getSession().getAttribute("sucMsg");
			map.addAttribute("sucMsg", sucMsg);
			request.getSession().removeAttribute("sucMsg");
		}
		if(null != request.getSession().getAttribute("errMsg")){
			errMsg = (String) request.getSession().getAttribute("errMsg");
			map.addAttribute("errMsg", errMsg);
			request.getSession().removeAttribute("errMsg");
		}
		return new ModelAndView("loginPage", map);
	}
	
	/**
	 * Navigate to login page
	 * @author Ronalin
	 *  
	 * @param error , the error message from Spring security 
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , View login page 
	 * @throws Exception 
	 */
	@RequestMapping(value ="/errorRedirect.do")
	public ModelAndView errorRedirect(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = FdahpStudyDesignerUtil.configMap;
		if (error != null && (error.equalsIgnoreCase("timeOut") || error.equalsIgnoreCase("multiUser"))) {
			request.getSession().setAttribute("errMsg", propMap.get("user.session.timeout"));
		} else if (error != null) {
			request.getSession().setAttribute("errMsg", FdahpStudyDesignerUtil.getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}
		return new ModelAndView("redirect:login.do");
	}
	/**
	 * Initiate  the forget password process
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , View redirect to login page 
	 */
	@RequestMapping(value ="/forgotPassword.do")
	public ModelAndView forgotPassword(HttpServletRequest request)  {
		logger.info("LoginController - forgotPassword() - Starts");
		ModelAndView mav = new ModelAndView("redirect:login.do");
		String message = FdahpStudyDesignerConstants.FAILURE;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = FdahpStudyDesignerUtil.configMap;
		try{
			String email = (null != request.getParameter("email") && !"".equals(request.getParameter("email"))) ? request.getParameter("email") : "";
			message   = loginService.sendPasswordResetLinkToMail(request, email, "");
			if(FdahpStudyDesignerConstants.SUCCESS.equals(message)){
				request.getSession().setAttribute("sucMsg", propMap.get("user.forgot.success.msg"));
			} else {
				request.getSession().setAttribute("errMsg",propMap.get("user.forgot.error.msg"));
			}
		}catch (Exception e) {
			logger.error("LoginController - forgotPassword() - ERROR " , e);
		}
		logger.info("LoginController - forgotPassword() - Ends");
		return mav;
	}
	
	/**
	 * Initiate  the change password process
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @param response , {@link HttpServletResponse} 
	 * @return {@link ModelAndView} , login page 
	 */
	@RequestMapping(value = "/changePassword.do")
	public ModelAndView changePassword(HttpServletRequest request, HttpServletResponse response){
		logger.info("LoginController - changePassword() - Starts");
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = FdahpStudyDesignerUtil.configMap;
		String message = FdahpStudyDesignerConstants.FAILURE;
		int userId = 0;
		ModelAndView mv = new ModelAndView("redirect:login.do");
		SessionObject sesObj = null;
		HttpSession session = null;
		try{
			session = request.getSession(false);
			sesObj = (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
			userId =  sesObj.getUserId();
			String newPassword = null != request.getParameter("newPassword") && !"".equals(request.getParameter("newPassword")) ? request.getParameter("newPassword"):"";
			String oldPassword = null != request.getParameter("oldPassword") && !"".equals(request.getParameter("oldPassword")) ? request.getParameter("oldPassword"):"";
			message = loginService.changePassword(userId, newPassword, oldPassword, sesObj);
			if(FdahpStudyDesignerConstants.SUCCESS.equals(message)){
				sesObj.setPasswordExpairdedDateTime(FdahpStudyDesignerUtil.getCurrentDateTime());
				mv = new ModelAndView("redirect:sessionOut.do?sucMsg="+propMap.get("user.force.logout.success"));
				request.getSession().setAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT, sesObj);
			} else {
				request.getSession(false).setAttribute("errMsg", message);
				mv = new ModelAndView("redirect:/profile/changeExpiredPassword.do");
			}
		}catch (Exception e) {
			logger.error("LoginController - changePassword() - ERROR " , e);
		}
		logger.info("LoginController - changePassword() - Ends");
		return mv;
	}
	
	/**
	 * Navigate to  the force change password view
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @param response , {@link HttpServletResponse}
	 * @return {@link ModelAndView} , forceChangePassword page view
	 */
	@RequestMapping(value = "/profile/changeExpiredPassword.do")
	public ModelAndView changeExpiredPassword(HttpServletRequest request, HttpServletResponse response){
		logger.info("LoginController - changeExpiredPassword() - Starts");
		ModelAndView mv = new ModelAndView("loginPage");
		String errMsg = null;
		String sucMsg = null;
		ModelMap map = null;
		try{
			map = new ModelMap();
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			mv = new ModelAndView("changeExpiredPassword", map);
		}catch (Exception e) {
			logger.error("LoginController - changeExpiredPassword() - ERROR " , e);
		}
		logger.info("LoginController - changeExpiredPassword() - Ends");
		return mv;
	}
	
	/**
	 * Prevent the user to view unauthorized view
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , View unauthorized error page 
	 */
	@RequestMapping(value ="/unauthorized.do")
	public ModelAndView unauthorized(HttpServletRequest request) {
		logger.info("LoginController - unauthorized()");
		return new ModelAndView("unauthorized");
	}
	
	
	/**
	 * Remove User from the Session
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @param response, {@link HttpServletResponse}
	 * @return {@link ModelAndView} , View login page
	 */
	@RequestMapping("/sessionOut.do")
	public ModelAndView sessionOut(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "msg", required = false) String msg, @RequestParam(value = "sucMsg", required = false) String sucMsg){
		logger.info("LoginController - sessionOut() - Starts");
		try {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null){
	            new SecurityContextLogoutHandler().logout(request, response, auth);
	        }
	        	 request.getSession(true).setAttribute("errMsg",msg);
	        	 request.getSession(true).setAttribute("sucMsg",sucMsg);
		} catch (Exception e) {
			logger.error("LoginController - sessionOut() - ERROR " , e);
		}
		logger.info("LoginController - sessionOut() - Ends");
		return new ModelAndView("redirect:login.do");
	}
	
	/**
	 * Validate the Security Token and navigate to change password page
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} 
	 */
	@RequestMapping("/createPassword.do")
	public ModelAndView validateSecurityToken(HttpServletRequest request){
		ModelMap map = new ModelMap();
		logger.info("LoginController - createPassword() - Starts");
		String securityToken = null;
		boolean  checkSecurityToken = false;
		UserBO userBO = null;
		ModelAndView mv = new ModelAndView("redirect:login.do");
		try {
			if(null != request.getSession(false).getAttribute("sucMsg")){
				map.addAttribute("sucMsg", request.getSession(false).getAttribute("sucMsg"));
				request.getSession(false).removeAttribute("sucMsg");
			}
			if(null != request.getSession(false).getAttribute("errMsg")){
				map.addAttribute("errMsg", request.getSession(false).getAttribute("errMsg"));
				request.getSession(false).removeAttribute("errMsg");
			}
			securityToken = FdahpStudyDesignerUtil.isNotEmpty(request.getParameter("securityToken")) ? request.getParameter("securityToken") :"";
			userBO = loginService.checkSecurityToken(securityToken);
			map.addAttribute("securityToken", securityToken);
			if(userBO != null){
				checkSecurityToken = true;
			}
			map.addAttribute("isValidToken", checkSecurityToken);
			if(userBO != null && StringUtils.isEmpty(userBO.getUserPassword())){
				map.addAttribute("userBO", userBO);
				mv = new ModelAndView("signUpPage", map);
			} else {
				mv = new ModelAndView("userPasswordReset", map);
			}
		} catch (Exception e) {
			logger.error("LoginController - createPassword() - ERROR " , e);
		}
		logger.info("LoginController - createPassword() - Ends");
		return mv;
	}
	
	/**
	 * Validate access code and add new password
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} 
	 */
	@RequestMapping("/addPassword.do")
	public ModelAndView addPassword(HttpServletRequest request,UserBO userBO){
		logger.info("LoginController - addPassword() - Starts");
		String securityToken = null;
		String accessCode = null;
		String password = null;
		String  errorMsg = FdahpStudyDesignerConstants.FAILURE;
		ModelAndView mv = new ModelAndView("redirect:login.do");
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = FdahpStudyDesignerUtil.configMap;
		SessionObject sesObj = null;
		HttpSession session = null;
		try {
			session = request.getSession(false);
			sesObj = (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
			accessCode = FdahpStudyDesignerUtil.isNotEmpty(request.getParameter("accessCode")) ? request.getParameter("accessCode") :"";
			password = FdahpStudyDesignerUtil.isNotEmpty(request.getParameter("password")) ? request.getParameter("password") :"";
			securityToken = FdahpStudyDesignerUtil.isNotEmpty(request.getParameter("securityToken")) ? request.getParameter("securityToken") :"";
			errorMsg = loginService.authAndAddPassword(securityToken, accessCode, password, userBO,sesObj);
			if(!errorMsg.equals(FdahpStudyDesignerConstants.SUCCESS)){
				request.getSession(false).setAttribute("errMsg", errorMsg);
				/*if(userBO != null && StringUtils.isNotEmpty(userBO.getFirstName())) {
					mv = new ModelAndView("redirect:signUp.do?securityToken="+securityToken);
				} else {*/
					mv = new ModelAndView("redirect:createPassword.do?securityToken="+securityToken);
				/*}*///
			} else {
				if(userBO != null && StringUtils.isNotEmpty(userBO.getFirstName())){
					request.getSession(false).setAttribute("sucMsg", propMap.get("user.newaccount.success.msg"));
				}else{
					request.getSession(false).setAttribute("sucMsg", propMap.get("user.newpassword.success.msg"));
				}
			}
		} catch (Exception e) {
			logger.error("LoginController - addPassword() - ERROR " , e);
		}
		logger.info("LoginController - addPassword() - Ends");
		return mv;
	}
	
	/**
	 * Navigate to privacy policy Page
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , termsAndCondition page
	 */
	@RequestMapping("/privacyPolicy.do")
	public ModelAndView privacyPolicy(HttpServletRequest request) {
		logger.info("LoginController - privacyPolicy() - Starts");
		ModelMap map = new ModelMap();
		logger.info("LoginController - privacyPolicy() - Ends");
		return new ModelAndView("privacypolicy", map);
	}
	
	/**
	 * Navigate to terms and condition Page
	 * @author Ronalin
	 *  
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView} , termsAndCondition page
	 */
	@RequestMapping("/termsAndCondition.do")
	public ModelAndView termsAndCondition(HttpServletRequest request) {
		logger.info("LoginController - termsAndCondition() - Starts");
		ModelMap map = new ModelMap();
		logger.info("LoginController - termsAndCondition() - Ends");
		return new ModelAndView("termsAndCondition", map);
	}
}
