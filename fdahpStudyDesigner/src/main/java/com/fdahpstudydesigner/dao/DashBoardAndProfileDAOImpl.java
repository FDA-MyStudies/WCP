package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bo.MasterDataBO;
import com.fdahpstudydesigner.bo.UserBO;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/** @author BTC */
@Repository
public class DashBoardAndProfileDAOImpl implements DashBoardAndProfileDAO {

  private static Logger logger = LogManager.getLogger(DashBoardAndProfileDAOImpl.class);
  HibernateTemplate hibernateTemplate;

  private Transaction transaction = null;

  @Override
  public MasterDataBO getMasterData(String type) {
    logger.info("DashBoardAndProfileDAOImpl - getMasterData() - Starts");
    Session session = null;
    MasterDataBO masterDataBO = null;
    Query query = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .createQuery("select MDBO from MasterDataBO MDBO where MDBO.type =:type")
              .setString("type", type);
      masterDataBO = (MasterDataBO) query.uniqueResult();
    } catch (Exception e) {
      logger.error("DashBoardAndProfileDAOImpl - getMasterData() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("DashBoardAndProfileDAOImpl - getMasterData() - Ends");
    return masterDataBO;
  }

  /**
   * Validating whether userEmail already existing in DB
   *
   * @param email
   * @return message, Success/Failure
   * @author BTC
   */
  @Override
  public String isEmailValid(String email) {
    logger.info("DashBoardAndProfileDAOImpl - isEmailValid() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    String queryString = null;
    Query query = null;
    UserBO user = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      queryString = "FROM UserBO where userEmail = :email";
      query = session.createQuery(queryString);
      query.setString("email", email);
      user = (UserBO) query.uniqueResult();
      if (null != user) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("DashBoardAndProfileDAOImpl - isEmailValid() - ERROR " + e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("DashBoardAndProfileDAOImpl - isEmailValid() - Ends");
    return message;
  }

  @Autowired
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.hibernateTemplate = new HibernateTemplate(sessionFactory);
  }

  /**
   * Updating User Details
   *
   * @param userId
   * @param userBO , {@link UserBO}
   * @author BTC
   */
  @Override
  public String updateProfileDetails(UserBO userBO, int userId) {
    logger.info("DashBoardAndProfileDAOImpl - updateProfileDetails() - Starts");
    Session session = null;
    Query query = null;
    String queryString = "";
    String message = FdahpStudyDesignerConstants.FAILURE;
    UserBO updatedUserBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      /*-------------------------Update FDA Admin-----------------------*/
      queryString = "from UserBO UBO where UBO.userId = :userId";
      query = session.createQuery(queryString);
      query.setInteger("userId", userId);
      updatedUserBo = (UserBO) query.uniqueResult();
      if (updatedUserBo != null) {
        updatedUserBo.setFirstName(
            null != userBO.getFirstName() ? userBO.getFirstName().trim() : "");
        updatedUserBo.setLastName(null != userBO.getLastName() ? userBO.getLastName().trim() : "");
        updatedUserBo.setUserEmail(
            null != userBO.getUserEmail() ? userBO.getUserEmail().trim() : "");
        updatedUserBo.setPhoneNumber(
            null != userBO.getPhoneNumber() ? userBO.getPhoneNumber().trim() : "");
        updatedUserBo.setModifiedBy(null != userBO.getModifiedBy() ? userBO.getModifiedBy() : 0);
        updatedUserBo.setModifiedOn(null != userBO.getModifiedOn() ? userBO.getModifiedOn() : "");
        session.update(updatedUserBo);
      }
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("DashBoardAndProfileDAOImpl - updateProfileDetails - ERROR", e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("DashBoardAndProfileDAOImpl - updateProfileDetails - Ends");
    return message;
  }
}
