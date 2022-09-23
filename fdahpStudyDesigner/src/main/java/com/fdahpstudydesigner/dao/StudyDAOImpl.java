package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bean.DynamicBean;
import com.fdahpstudydesigner.bean.DynamicFrequencyBean;
import com.fdahpstudydesigner.bean.StudyIdBean;
import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bean.StudyPageBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;
import com.fdahpstudydesigner.util.SpanishLangConstants;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/** @author BTC */
@Repository
public class StudyDAOImpl implements StudyDAO {

  private static Logger logger = LogManager.getLogger(StudyDAOImpl.class.getName());
  @Autowired private AuditLogDAO auditLogDAO;
  HibernateTemplate hibernateTemplate;
  private Query query = null;
  String queryString = "";
  private Transaction transaction = null;

  public StudyDAOImpl() {
    // Unused
  }

  /**
   * This method is used to validate the activetaskType for android platform
   *
   * @author BTC
   * @param Integer , studyId
   * @return String, SUCCESS or FAILURE
   */
  @Override
  public String checkActiveTaskTypeValidation(Integer studyId) {
    logger.info("StudyDAOImpl - checkActiveTaskTypeValidation() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      String searchQuery =
          "select count(*) from active_task a where a.study_id= :studyId and "
              + "a.task_type_id in(select c.active_task_list_id from active_task_list c "
              + "where a.active=1 and c.task_name in(:taskNameList));";
      List<String> taskNameList =
          Arrays.asList(
              FdahpStudyDesignerConstants.TOWER_OF_HANOI,
              FdahpStudyDesignerConstants.SPATIAL_SPAN_MEMORY);
      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setInteger("studyId", studyId)
                  .setParameterList("taskNameList", taskNameList)
                  .uniqueResult();
      if (count != null && count.intValue() > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - checkActiveTaskTypeValidation() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - checkActiveTaskTypeValidation() - Ends");
    return message;
  }

  /**
   * Describes the order of an comprehension question while creating the new comprehension question
   *
   * @author BTC
   * @param studyId {@link StudyBo}
   * @return int count
   */
  @Override
  public int comprehensionTestQuestionOrder(Integer studyId) {
    logger.info("StudyDAOImpl - comprehensionTestQuestionOrder() - Starts");
    Session session = null;
    int count = 0;
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createQuery(
              "From ComprehensionTestQuestionBo CTQBO where CTQBO.studyId= :studyId"
                  + " and CTQBO.active=1 order by CTQBO.sequenceNo desc");
      query.setInteger("studyId", studyId).setMaxResults(1);
      comprehensionTestQuestionBo = (ComprehensionTestQuestionBo) query.uniqueResult();
      if (comprehensionTestQuestionBo != null) {
        count = comprehensionTestQuestionBo.getSequenceNo() + 1;
      } else {
        count = count + 1;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - comprehensionTestQuestionOrder() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - comprehensionTestQuestionOrder() - Ends");
    return count;
  }

  /**
   * Describes the order of an consent item while creating the new consent item in the study consent
   * section
   *
   * @author BTC
   * @param studyId {@link StudyBo}
   * @return int count
   */
  @Override
  public int consentInfoOrder(Integer studyId) {
    logger.info("StudyDAOImpl - consentInfoOrder() - Starts");
    Session session = null;
    int count = 1;
    ConsentInfoBo consentInfoBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createQuery(
              "From ConsentInfoBo CIB where CIB.studyId= :studyId"
                  + " and CIB.active=1 order by CIB.sequenceNo DESC");
      query.setInteger("studyId", studyId).setMaxResults(1);
      consentInfoBo = ((ConsentInfoBo) query.uniqueResult());
      if (consentInfoBo != null) {
        count = consentInfoBo.getSequenceNo() + 1;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - consentInfoOrder() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - consentInfoOrder() - Ends");
    return count;
  }

  /**
   * Describes the delete of an comprehension test question from the list of test question in the
   * study consent section
   *
   * @author BTC
   * @param Integer , questionId {@link ComprehensionTestQuestionBo}
   * @return String : SUCCESS or FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteComprehensionTestQuestion(
      Integer questionId, Integer studyId, SessionObject sessionObject) {
    logger.info("StudyDAOImpl - deleteComprehensionTestQuestion() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    String searchQuery = "";
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    StudySequenceBo studySequence = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = null;
      searchQuery =
          "From ComprehensionTestQuestionBo CTQBO where CTQBO.studyId= :studyId"
              + " and CTQBO.active=1 order by CTQBO.sequenceNo asc";
      comprehensionTestQuestionList =
          session.createQuery(searchQuery).setInteger("studyId", studyId).list();
      if (comprehensionTestQuestionList != null && !comprehensionTestQuestionList.isEmpty()) {
        boolean isValue = false;
        for (ComprehensionTestQuestionBo comprehensionTestQuestion :
            comprehensionTestQuestionList) {
          if (comprehensionTestQuestion.getId().equals(questionId)) {
            isValue = true;
          }
          if (isValue && !comprehensionTestQuestion.getId().equals(questionId)) {
            comprehensionTestQuestion.setSequenceNo(comprehensionTestQuestion.getSequenceNo() - 1);
            session.update(comprehensionTestQuestion);
          }
        }
      }
      comprehensionTestQuestionBo =
          (ComprehensionTestQuestionBo) session.get(ComprehensionTestQuestionBo.class, questionId);
      if (comprehensionTestQuestionBo != null) {
        comprehensionTestQuestionBo.setActive(false);
        comprehensionTestQuestionBo.setModifiedBy(sessionObject.getUserId());
        comprehensionTestQuestionBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
        session.saveOrUpdate(comprehensionTestQuestionBo);

        List<ComprehensionQuestionLangBO> comprehensionQuestionLangBOList =
            session
                .createQuery(
                    "from ComprehensionQuestionLangBO where comprehensionQuestionLangPK.id=:id")
                .setInteger("id", questionId)
                .list();
        if (comprehensionQuestionLangBOList != null && comprehensionQuestionLangBOList.size() > 0) {
          for (ComprehensionQuestionLangBO comprehensionQuestionLangBO :
              comprehensionQuestionLangBOList) {
            comprehensionQuestionLangBO.setActive(false);
            session.update(comprehensionQuestionLangBO);
          }
        }

        message = FdahpStudyDesignerConstants.SUCCESS;
        if (comprehensionTestQuestionBo.getStudyId() != null) {
          studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(
                          FdahpStudyDesignerConstants.STUDY_ID,
                          comprehensionTestQuestionBo.getStudyId())
                      .uniqueResult();
          if (studySequence != null) {
            if (studySequence.isComprehensionTest()) {
              studySequence.setComprehensionTest(false);
            }
            session.saveOrUpdate(studySequence);
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteComprehensionTestQuestion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteComprehensionTestQuestion() - Ends");
    return message;
  }

  /**
   * Describes the delete of an consent item from the list of consent items in the study consent
   * section
   *
   * @author BTC
   * @param Integer , consentInfoId {@link ConsentInfoBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return String :SUCCESS or FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteConsentInfo(
      Integer consentInfoId, Integer studyId, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyDAOImpl - deleteConsentInfo() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    ConsentInfoBo consentInfo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      List<ConsentInfoBo> consentInfoList = null;
      String searchQuery =
          "From ConsentInfoBo CIB where CIB.studyId= :studyId and CIB.active=1 order by CIB.sequenceNo asc";
      consentInfoList = session.createQuery(searchQuery).setInteger("studyId", studyId).list();
      if (consentInfoList != null && !consentInfoList.isEmpty()) {
        boolean isValue = false;
        for (ConsentInfoBo consentInfoBo : consentInfoList) {
          if (consentInfoBo.getId().equals(consentInfoId)) {
            isValue = true;
          }
          if (isValue && !consentInfoBo.getId().equals(consentInfoId)) {
            consentInfoBo.setSequenceNo(consentInfoBo.getSequenceNo() - 1);
            consentInfoBo.setModifiedBy(sessionObject.getUserId());
            consentInfoBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            session.update(consentInfoBo);
          }
        }
        StudySequenceBo studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                    .uniqueResult();
        if (studySequence != null) {
          if (consentInfoList.size() == 1) {
            studySequence.setConsentEduInfo(false);
          }
          if (studySequence.iseConsent()) {
            studySequence.seteConsent(false);
          }
          session.saveOrUpdate(studySequence);
        }
      }
      String deleteQuery =
          "Update ConsentInfoBo CIB set CIB.active=0,CIB.modifiedBy= :modifiedBy"
              + ",CIB.modifiedOn= :modifiedOn where CIB.id= :consentInfoId";
      query =
          session
              .createQuery(deleteQuery)
              .setInteger("modifiedBy", sessionObject.getUserId())
              .setString("modifiedOn", FdahpStudyDesignerUtil.getCurrentDateTime())
              .setInteger("consentInfoId", consentInfoId);
      count = query.executeUpdate();

      List<ConsentInfoLangBO> consentInfoLangBOList =
          session
              .createQuery("from ConsentInfoLangBO where consentInfoLangPK.id= :consentInfoId")
              .setInteger("consentInfoId", consentInfoId)
              .list();
      for (ConsentInfoLangBO consentInfoLangBO : consentInfoLangBOList) {
        consentInfoLangBO.setActive(false);
        consentInfoLangBO.setModifiedBy(sessionObject.getUserId());
        consentInfoLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
        session.update(consentInfoLangBO);
      }

      if (count > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
      if (consentInfoId != null) {
        consentInfo = getConsentInfoById(consentInfoId);
        if (consentInfo != null) {
          auditLogDAO.saveToAuditLog(
              session,
              transaction,
              sessionObject,
              "ConsentInfo Deleted.",
              "Consent Section deleted. (Display name = "
                  + consentInfo.getDisplayTitle()
                  + ", Study ID = "
                  + customStudyId
                  + ") ",
              "StudyDAOImpl - deleteConsentInfo");
        }
      }

      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteConsentInfo() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteConsentInfo() - Ends");
    return message;
  }

  /**
   * Delete eligibility test question answer by id
   *
   * @author BTC
   * @param eligibilityTestId , Id of {@link EligibilityTestBo}
   * @param studyId , Id of {@link StudyBo}
   * @param sessionObject , Object of {@link SessionObject}
   * @param customStudyId , custom study id of {@link StudyBo}
   * @return {@link FdahpStudyDesignerConstants.SUCCESS} or {@link
   *     FdahpStudyDesignerConstants.FAILURE} , reorder status
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteEligibilityTestQusAnsById(
      Integer eligibilityTestId,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId) {
    logger.info("StudyDAOImpl - deleteEligibilityTestQusAnsById - Starts");
    Session session = null;
    int eligibilityDeleteResult = 0;
    Transaction trans = null;
    String result = FdahpStudyDesignerConstants.FAILURE;
    String reorderQuery;
    EligibilityTestBo eligibilityTestBo;
    List<EligibilityTestBo> eligibilityTestBos;
    StudyBo studyBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      trans = session.beginTransaction();
      eligibilityTestBo =
          (EligibilityTestBo)
              session
                  .getNamedQuery("EligibilityTestBo.findById")
                  .setInteger("eligibilityTestId", eligibilityTestId)
                  .uniqueResult();

      List<EligibilityTestLangBo> eligibilityTestLangBoList =
          session
              .createQuery(
                  "SELECT ETB FROM EligibilityTestLangBo ETB WHERE ETB.active = true AND ETB.eligibilityTestLangPK.id=:eligibilityTestId ORDER BY ETB.sequenceNo")
              .setInteger("eligibilityTestId", eligibilityTestId)
              .list();
      studyBo = this.getStudyById(String.valueOf(studyId), sessionObject.getUserId());

      if (null != studyBo
          && studyBo.getStatus().contains(FdahpStudyDesignerConstants.STUDY_PRE_LAUNCH)) {
        session.delete(eligibilityTestBo);
        if (eligibilityTestLangBoList != null && eligibilityTestLangBoList.size() > 0) {
          for (EligibilityTestLangBo eligibilityTestLangBo : eligibilityTestLangBoList) {
            if (eligibilityTestLangBo != null) {
              session.delete(eligibilityTestLangBo);
            }
          }
        }
        eligibilityDeleteResult = 1;
      } else {
        eligibilityDeleteResult =
            session
                .getNamedQuery("EligibilityTestBo.deleteById")
                .setInteger("eligibilityTestId", eligibilityTestId)
                .executeUpdate();

        if (eligibilityTestLangBoList != null && eligibilityTestLangBoList.size() > 0) {
          for (EligibilityTestLangBo eligibilityTestLangBo : eligibilityTestLangBoList) {
            eligibilityTestLangBo.setActive(false);
            session.update(eligibilityTestLangBo);
          }
        }
      }

      eligibilityTestBos =
          session
              .createQuery(
                  "select id FROM EligibilityTestBo  WHERE sequenceNo > :sequenceNo AND active = true AND eligibilityId = :eligibilityId")
              .setInteger("sequenceNo", eligibilityTestBo.getSequenceNo())
              .setInteger("eligibilityId", eligibilityTestBo.getEligibilityId())
              .list();

      if (eligibilityDeleteResult > 0 && !eligibilityTestBos.isEmpty()) {
        reorderQuery =
            "update EligibilityTestBo  set sequenceNo=sequenceNo-1 where id in (:eligibilityTestBoList)";
        eligibilityDeleteResult =
            session
                .createQuery(reorderQuery)
                .setParameterList("eligibilityTestBoList", eligibilityTestBos)
                .executeUpdate();
      }
      if (eligibilityDeleteResult > 0) {
        result = FdahpStudyDesignerConstants.SUCCESS;
      }
      if (eligibilityTestId > 0) {
        StudySequenceBo studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                    .uniqueResult();
        if (studySequence != null && studySequence.isEligibility()) {
          studySequence.setEligibility(false);
          session.update(studySequence);
        }
      }
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          "EligibilityQus",
          customStudyId + " -- EligibilityQus deleted",
          "StudyDAOImpl - deleteEligibilityTestQusAnsById");
      trans.commit();
    } catch (Exception e) {
      if (null != trans) trans.rollback();
      logger.error("StudyDAOImpl - deleteEligibilityTestQusAnsById - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteEligibilityTestQusAnsById - Ends");
    return result;
  }

  /**
   * delete live study by customStudyId
   *
   * @author BTC
   * @param String , customStudyId
   * @return boolean
   */
  @Override
  public boolean deleteLiveStudy(String customStudyId) {
    logger.info("StudyDAOImpl - deleteLiveStudy() - Starts");
    boolean flag = false;
    Session session = null;
    StudyBo liveStudyBo = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      liveStudyBo =
          (StudyBo)
              session
                  .getNamedQuery("getStudyLiveVersion")
                  .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId)
                  .uniqueResult();
      if (liveStudyBo != null) {
        // deleting the live study
        message =
            deleteStudyByIdOrCustomstudyId(
                session, transaction, liveStudyBo.getId().toString(), "");

        // once live study deleted successfully, reseting the new study
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          session
              .createSQLQuery("DELETE FROM study_version WHERE custom_study_id= :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery(
                  "DELETE FROM study_activity_version WHERE custom_study_id= :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery(
                  "UPDATE active_task set is_live=0 WHERE study_id in (SELECT id FROM studies WHERE custom_study_id= :customStudyId)")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery(
                  "UPDATE questionnaires set is_live=0 WHERE study_id in (SELECT id FROM studies WHERE custom_study_id= :customStudyId)")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery(
                  "UPDATE consent set is_live=0 WHERE study_id in (SELECT id FROM studies WHERE custom_study_id= :customStudyId)")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery(
                  "UPDATE consent_info set is_live=0 WHERE study_id in (SELECT id FROM studies WHERE custom_study_id= :customStudyId)")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery("UPDATE studies set is_live=0 WHERE custom_study_id = :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          flag = true;
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteLiveStudy() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteLiveStudy() - Ends");
    return flag;
  }

  /**
   * delete the Study Overview Page By Page Id
   *
   * @author BTC
   * @param String , studyId
   * @param String , pageId
   * @return {@link String}
   */
  @Override
  public String deleteOverviewStudyPageById(String studyId, String pageId) {
    logger.info("StudyDAOImpl - deleteOverviewStudyPageById() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int count = 0;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(studyId) && StringUtils.isNotEmpty(pageId)) {
        query =
            session.createQuery(
                "delete from StudyPageBo where studyId= :studyId and pageId= :pageId");
        count = query.setString("studyId", studyId).setString("pageId", pageId).executeUpdate();
        if (count > 0) message = FdahpStudyDesignerConstants.SUCCESS;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteOverviewStudyPageById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteOverviewStudyPageById() - Ends");
    return message;
  }

  /**
   * This method is used to delete the resource
   *
   * @author BTC
   * @param resourceInfoId
   * @param resourceVisibility
   * @param studyId
   * @return message, Success/Failure message
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteResourceInfo(
      Integer resourceInfoId, boolean resourceVisibility, int studyId) {
    logger.info("StudyDAOImpl - deleteResourceInfo() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int resourceCount = 0;
    Query notificationQuery = null;
    List<ResourceBO> resourceBOList = null;
    ResourceBO resource = null;
    AnchorDateTypeBo anchorDateTypeBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      resourceBOList =
          session
              .createQuery(
                  "From ResourceBO RBO where RBO.studyId= :studyId and RBO.status=1 order by RBO.sequenceNo asc")
              .setInteger("studyId", studyId)
              .list();
      if (resourceBOList != null && !resourceBOList.isEmpty()) {
        boolean isValue = false;
        for (ResourceBO resourceBO : resourceBOList) {
          if (resourceBO.getId().equals(resourceInfoId)) {
            isValue = true;
          }
          if (isValue && !resourceBO.getId().equals(resourceInfoId)) {
            resourceBO.setSequenceNo(resourceBO.getSequenceNo() - 1);
            session.update(resourceBO);
          }
        }
      }

      resourceCount =
          session
              .createQuery("UPDATE ResourceBO RBO SET status = false WHERE id = :resourceInfoId")
              .setInteger("resourceInfoId", resourceInfoId)
              .executeUpdate();

      List<ResourcesLangBO> resourcesLangBOList =
          session
              .createQuery(
                  "From ResourcesLangBO RBO where RBO.resourcesLangPK.id= :id and RBO.status=true order by RBO.sequenceNo asc")
              .setInteger("id", resourceInfoId)
              .list();

      if (resourcesLangBOList != null && resourcesLangBOList.size() > 0) {
        for (ResourcesLangBO resourcesLangBO : resourcesLangBOList) {
          resourcesLangBO.setStatus(false);
          resourcesLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          session.update(resourcesLangBO);
        }
      }

      if (!resourceVisibility && resourceCount > 0) {

        resource = (ResourceBO) session.get(ResourceBO.class, resourceInfoId);
        if (null != resource && null != resource.getAnchorDateId()) {
          anchorDateTypeBo =
              (AnchorDateTypeBo) session.get(AnchorDateTypeBo.class, resource.getAnchorDateId());
          if (null != anchorDateTypeBo
              && null != anchorDateTypeBo.getParticipantProperty()
              && anchorDateTypeBo.getParticipantProperty()) {
            query =
                session.createQuery(
                    "select count(*) from ResourceBO RBO  where RBO.studyId=:studyId and RBO.anchorDateId=:anchorDateId and RBO.status=1");
            query.setInteger("studyId", resource.getStudyId());
            query.setInteger("anchorDateId", resource.getAnchorDateId());
            Long count = (Long) query.uniqueResult();
            if (count < 1) {
              query =
                  session.createQuery(
                      "Update ParticipantPropertiesBO PBO SET PBO.isUsedInResource = 0 where PBO.anchorDateId=:anchorDateId");
              query.setInteger("anchorDateId", resource.getAnchorDateId());
              query.executeUpdate();
            }
          }
        }

        notificationQuery =
            session
                .createQuery(
                    "UPDATE NotificationBO NBO set NBO.notificationStatus = 1 WHERE NBO.resourceId = :resourceInfoId")
                .setInteger("resourceInfoId", resourceInfoId);
        notificationQuery.executeUpdate();
      }
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteResourceInfo() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteResourceInfo() - Ends");
    return message;
  }

  /**
   * This method is used to delete Study
   *
   * @author BTC
   * @param String , customStudyId
   * @return boolean,{true/false}
   */
  public boolean deleteStudyByCustomStudyId(String customStudyId) {
    logger.info("StudyDAOImpl - deleteStudyByCustomStudyId() - Starts");
    Session session = null;
    boolean falg = false;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      message = deleteStudyByIdOrCustomstudyId(session, transaction, "", customStudyId);
      if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
        falg = true;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteStudyByCustomStudyId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteStudyByCustomStudyId() - Ends");
    return falg;
  }

  /**
   * This method is used to delete Study
   *
   * @author BTC
   * @param String , customStudyId
   * @return boolean,{true/false}
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteStudyByIdOrCustomstudyId(
      Session session, Transaction transaction, String studyId, String customStudyId) {
    logger.info("StudyDAOImpl - deleteStudyByIdOrCustomstudyId() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    List<StudyBo> studyBOList = null;
    String subQuery = "";
    String studyCustomQuery = "";
    List<Integer> idList = null;
    try {
      if (StringUtils.isNotEmpty(customStudyId)) {
        studyCustomQuery = " FROM StudyBo SBO WHERE SBO.customStudyId ='" + customStudyId + "'";
        subQuery = "(SELECT id FROM studies WHERE custom_study_id='" + customStudyId + "')";
      } else {
        studyCustomQuery = " FROM StudyBo SBO WHERE SBO.id in(" + studyId + ")";
        subQuery = "(" + studyId + ")";
      }
      query = session.createQuery(studyCustomQuery);
      studyBOList = query.list();
      if (studyBOList != null && !studyBOList.isEmpty()) {

        queryString =
            "SELECT page_id FROM study_page WHERE page_id is not null and study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM study_page WHERE page_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "select id from eligibility_test e where e.eligibility_id in(select id from eligibility where study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM eligibility_test WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM eligibility WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM eligibility WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM consent WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM consent WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "select r.id from comprehension_test_response r where r.comprehension_test_question_id in(select id from comprehension_test_question c where c.study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM comprehension_test_response WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM comprehension_test_question WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM comprehension_test_question WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM consent_info WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM consent_info WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT active_task_id FROM active_task_attrtibutes_values WHERE active_task_id IN(SELECT id FROM active_task WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery(
                  "DELETE FROM active_task_attrtibutes_values WHERE active_task_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT active_task_id FROM active_task_frequencies WHERE active_task_id IN (SELECT id FROM active_task WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery(
                  "DELETE FROM active_task_frequencies WHERE active_task_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT active_task_id FROM active_task_custom_frequencies WHERE active_task_id IN(SELECT id FROM active_task WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery(
                  "DELETE FROM active_task_custom_frequencies WHERE active_task_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM active_task WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM active_task WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        /** Questionnarie Part Start * */
        // Form Step Start .....

        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM questions WHERE id IN(SELECT question_id FROM form_mapping WHERE form_id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Form' AND questionnaires_id IN (SELECT id FROM questionnaires q WHERE study_id in"
                + subQuery
                + ")))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questions WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT response_sub_type_value_id FROM response_sub_type_value WHERE response_type_id IN(SELECT question_id FROM form_mapping WHERE form_id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Form' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + ")))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery(
                  "DELETE FROM response_sub_type_value WHERE response_sub_type_value_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT response_type_id FROM response_type_value WHERE questions_response_type_id IN(SELECT question_id FROM form_mapping WHERE form_id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Form' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + ")))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM response_type_value WHERE response_type_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        // form_mapping deletion
        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM form_mapping WHERE form_id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Form' AND questionnaires_id IN (SELECT id FROM questionnaires q WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM form_mapping WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        // form deletion
        queryString =
            "SELECT form_id FROM form WHERE form_id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Form' AND questionnaires_id IN (SELECT id FROM questionnaires q WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM form WHERE form_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        // Form Step End......

        // Instruction Step Start .....

        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM instructions WHERE id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Instruction' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM instructions WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }
        // Instruction Step End......

        // Question Step End......

        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM questions WHERE id IN (SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Question' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questions WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT response_sub_type_value_id FROM response_sub_type_value WHERE response_type_id IN(SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Question' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery(
                  "DELETE FROM response_sub_type_value WHERE response_sub_type_value_id in(:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT response_type_id FROM response_type_value WHERE questions_response_type_id IN(SELECT instruction_form_id FROM questionnaires_steps WHERE step_type='Question' AND questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + "))";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM response_type_value WHERE response_type_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        // Question Step End......
        idList = null;
        queryString = "";
        queryString =
            "SELECT step_id FROM questionnaires_steps WHERE questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questionnaires_steps WHERE step_id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM questionnaires_frequencies WHERE questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questionnaires_frequencies WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString =
            "SELECT id FROM questionnaires_custom_frequencies WHERE questionnaires_id IN (SELECT id FROM questionnaires WHERE study_id in"
                + subQuery
                + ")";
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questionnaires_custom_frequencies WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        idList = null;
        queryString = "";
        queryString = "SELECT id FROM questionnaires WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM questionnaires WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        /** Questionnarie Part End **** */
        idList = null;
        queryString = "";
        queryString = "SELECT id FROM resources WHERE study_id in" + subQuery;
        idList = session.createSQLQuery(queryString).list();
        if (idList != null && !idList.isEmpty()) {
          session
              .createSQLQuery("DELETE FROM resources WHERE id in (:idList)")
              .setParameterList("idList", idList)
              .executeUpdate();
        }

        session
            .createSQLQuery(
                "DELETE FROM notification_history WHERE notification_id in(SELECT notification_id FROM notification WHERE study_id in "
                    + subQuery
                    + ")")
            .executeUpdate();

        session
            .createSQLQuery("DELETE FROM notification WHERE study_id in" + subQuery)
            .executeUpdate();

        session
            .createSQLQuery("DELETE FROM study_checklist WHERE study_id in" + subQuery)
            .executeUpdate();

        session
            .createSQLQuery("DELETE FROM study_permission WHERE study_id in" + subQuery)
            .executeUpdate();

        session
            .createSQLQuery("DELETE FROM study_sequence WHERE study_id in" + subQuery)
            .executeUpdate();
        if (StringUtils.isNotEmpty(customStudyId)) {
          session
              .createSQLQuery("DELETE FROM study_version WHERE custom_study_id= :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery("DELETE FROM studies WHERE custom_study_id= :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
          session
              .createSQLQuery("DELETE FROM anchordate_type WHERE custom_study_id= :customStudyId")
              .setString("customStudyId", customStudyId)
              .executeUpdate();
        } else {
          session
              .createSQLQuery("DELETE FROM studies WHERE id in (:studyId)")
              .setParameterList("studyId", Arrays.asList(studyId))
              .executeUpdate();
        }
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteStudyByIdOrCustomstudyId() - ERROR ", e);
    }
    logger.info("StudyDAOImpl - deleteStudyByIdOrCustomstudyId() - Ends");
    return message;
  }

  /**
   * The last order count of questions of a {@link EligibilityBo}
   *
   * @author BTC
   * @param eligibilityId
   * @return count , the last order count of the {@link EligibilityTestBo}
   */
  @Override
  public int eligibilityTestOrderCount(Integer eligibilityId) {
    logger.info("StudyDAOImpl - eligibilityTestOrderCount - Starts");
    Session session = null;
    int count = 1;
    EligibilityTestBo eligibilityTestBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      eligibilityTestBo =
          (EligibilityTestBo)
              session
                  .createQuery(
                      "From EligibilityTestBo ETB where ETB.eligibilityId= :eligibilityId and ETB.active=1 order by ETB.sequenceNo DESC")
                  .setInteger("eligibilityId", eligibilityId)
                  .setMaxResults(1)
                  .uniqueResult();
      if (eligibilityTestBo != null) {
        count = eligibilityTestBo.getSequenceNo() + 1;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - eligibilityTestOrderCount - Error", e);
    }
    logger.info("StudyDAOImpl - eligibilityTestOrderCount - Ends");
    return count;
  }

  /**
   * This method is used to get the active user list whom are not yet added to the particular study
   *
   * @author BTC
   * @param studyId
   * @param userId
   * @return List of {@link UserBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<UserBO> getActiveNonAddedUserList(Integer studyId, Integer userId) {
    logger.info("StudyDAOImpl - getActiveNonAddedUserList() - Starts");
    Session session = null;
    List<UserBO> userList = null;
    List<Object[]> objList = null;
    Query query = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createSQLQuery(
              " SELECT u.user_id,u.first_name,u.last_name,u.email,r.role_name "
                  + "FROM users u,roles r WHERE r.role_id = u.role_id AND u.status = 1 "
                  + "AND u.user_id NOT IN (SELECT upm.user_id FROM user_permission_mapping upm "
                  + "WHERE upm.permission_id = (SELECT up.permission_id FROM user_permissions up WHERE up.permissions ='ROLE_SUPERADMIN')) ");
      objList = query.list();
      if (null != objList && !objList.isEmpty()) {
        userList = new ArrayList<>();
        for (Object[] obj : objList) {
          UserBO userBO = new UserBO();
          userBO.setUserId(null != obj[0] ? (Integer) obj[0] : 0);
          userBO.setFirstName(null != obj[1] ? String.valueOf(obj[1]) : "");
          userBO.setLastName(null != obj[2] ? String.valueOf(obj[2]) : "");
          userBO.setUserEmail(null != obj[3] ? String.valueOf(obj[3]) : "");
          userBO.setRoleName(null != obj[4] ? String.valueOf(obj[4]) : "");
          userBO.setUserFullName(userBO.getFirstName() + " " + userBO.getLastName());
          userList.add(userBO);
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getActiveNonAddedUserList() - ERROR", e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getActiveNonAddedUserList() - Ends");
    return userList;
  }

  /**
   * This method is used to get the users whom are already added to the particular study
   *
   * @author BTC
   * @param studyId
   * @param userId
   * @return List of {@link StudyPermissionBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StudyPermissionBO> getAddedUserListToStudy(Integer studyId, Integer userId) {
    logger.info("StudyDAOImpl - getAddedUserListToStudy() - Starts");
    Session session = null;
    List<Object[]> objList = null;
    Query query = null;
    List<StudyPermissionBO> studyPermissionList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createSQLQuery(
              " SELECT sp.user_id,u.first_name,u.last_name,sp.view_permission,sp.project_lead "
                  + "FROM users u,study_permission sp "
                  + "WHERE u.user_id = sp.user_id AND u.status = 1 AND sp.study_id = :studyId"
                  + " AND u.user_id NOT IN (SELECT upm.user_id FROM user_permission_mapping upm "
                  + "WHERE upm.permission_id = (SELECT up.permission_id FROM user_permissions up WHERE up.permissions ='ROLE_SUPERADMIN')) ");
      objList = query.setInteger("studyId", studyId).list();
      if (null != objList && !objList.isEmpty()) {
        studyPermissionList = new ArrayList<>();
        for (Object[] obj : objList) {
          StudyPermissionBO studyPermissionBO = new StudyPermissionBO();
          studyPermissionBO.setUserId(null != obj[0] ? (Integer) obj[0] : 0);
          studyPermissionBO.setUserFullName(
              (null != obj[1] ? String.valueOf(obj[1]) : "")
                  + " "
                  + (null != obj[2] ? String.valueOf(obj[2]) : ""));
          studyPermissionBO.setViewPermission((boolean) obj[3] ? true : false);
          studyPermissionBO.setProjectLead(
              null != obj[4] ? Integer.parseInt(obj[4].toString()) : 0);
          studyPermissionList.add(studyPermissionBO);
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getAddedUserListToStudy() - ERROR", e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getAddedUserListToStudy() - Ends");
    return studyPermissionList;
  }

  /**
   * return all active study List based
   *
   * @author BTC
   * @return the Study list
   * @exception Exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StudyBo> getAllStudyList() {
    logger.info("StudyDAOImpl - getAllStudyList() - Starts");
    Session session = null;
    List<StudyBo> studyBOList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createQuery(
              " FROM StudyBo SBO WHERE SBO.version = 0 AND SBO.status <> :deActivateStatus");
      studyBOList =
          query.setString("deActivateStatus", FdahpStudyDesignerConstants.STUDY_DEACTIVATED).list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getAllStudyList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getAllStudyList() - Ends");
    return studyBOList;
  }

  /**
   * return all active app versions
   *
   * @author BTC
   * @return the app list
   * @exception Exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<VersionInfo> getVersionInfoList() {
    logger.info("StudyDAOImpl - getVersionInfoList() - Starts");
    Session session = null;
    List<VersionInfo> versionInfos = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      versionInfos =
              session.createQuery(
                      " FROM VersionInfo WHERE appId is not null").list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getVersionInfoList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getVersionInfoList() - Ends");
    return versionInfos;
  }

  /**
   * This method is used to get the checklist info
   *
   * @author BTC
   * @param studyId
   * @return {@link Checklist}
   */
  @Override
  public Checklist getchecklistInfo(Integer studyId) {
    logger.info("StudyDAOImpl - getchecklistInfo() - Starts");
    Checklist checklist = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .getNamedQuery("getchecklistInfo")
              .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId);
      checklist = (Checklist) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getchecklistInfo() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getchecklistInfo() - Ends");
    return checklist;
  }

  /**
   * Study Consent have the 0 or more comprehension test question.Each question contains the
   * question text and answers and make correct response to the question as being ANY or ALL of the
   * 'correct' answer options.
   *
   * @author BTC
   * @param Integer , questionId {@link ComprehensionTestQuestionBo}
   * @return {@link ComprehensionTestQuestionBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ComprehensionTestQuestionBo getComprehensionTestQuestionById(Integer questionId) {
    logger.info("StudyDAOImpl - getComprehensionTestQuestionById() - Starts");
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    Session session = null;
    List<ComprehensionTestResponseBo> comprehensionTestResponsList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      comprehensionTestQuestionBo =
          (ComprehensionTestQuestionBo) session.get(ComprehensionTestQuestionBo.class, questionId);
      if (null != comprehensionTestQuestionBo) {
        comprehensionTestResponsList =
            session
                .createQuery(
                    "From ComprehensionTestResponseBo CRBO where CRBO.comprehensionTestQuestionId= :comprehensionTestQuestionId")
                .setInteger("comprehensionTestQuestionId", comprehensionTestQuestionBo.getId())
                .list();
        comprehensionTestQuestionBo.setResponseList(comprehensionTestResponsList);
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getComprehensionTestQuestionById() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getComprehensionTestQuestionById() - Ends");
    return comprehensionTestQuestionBo;
  }

  /**
   * Study consent have the 0 or more comprehension test question to check the understanding of the
   * consent to a participant here will show the list comprehension test question of consent of a
   * study
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @return List : {@link ComprehensionTestQuestionBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ComprehensionTestQuestionBo> getComprehensionTestQuestionList(Integer studyId) {
    logger.info("StudyDAOImpl - getComprehensionTestQuestionList() - Starts");
    Session session = null;
    List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      comprehensionTestQuestionList =
          session
              .createQuery(
                  "From ComprehensionTestQuestionBo CTQBO where CTQBO.studyId= :studyId and CTQBO.active=1 order by CTQBO.sequenceNo asc")
              .setInteger("studyId", studyId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getComprehensionTestQuestionList() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getComprehensionTestQuestionList() - Ends");
    return comprehensionTestQuestionList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ComprehensionQuestionLangBO> getComprehensionTestQuestionLangList(
      Integer studyId, String language) {
    logger.info("StudyDAOImpl - getComprehensionTestQuestionLangList() - Starts");
    Session session = null;
    List<ComprehensionQuestionLangBO> list = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      list =
          session
              .createQuery(
                  "From ComprehensionQuestionLangBO CTQBO where CTQBO.studyId= :studyId and "
                      + "CTQBO.comprehensionQuestionLangPK.langCode=:language order by CTQBO.sequenceNo asc")
              .setInteger("studyId", studyId)
              .setString("language", language)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getComprehensionTestQuestionLangList() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getComprehensionTestQuestionLangList() - Ends");
    return list;
  }

  /**
   * Describes the get the comprehension test question response
   *
   * @author BTC
   * @param Integer , comprehensionQuestionId {@link ComprehensionTestQuestionBo}
   * @return List : {@link ComprehensionTestResponseBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ComprehensionTestResponseBo> getComprehensionTestResponseList(
      Integer comprehensionQuestionId) {
    logger.info("StudyDAOImpl - deleteComprehensionTestQuestion() - Starts");
    Session session = null;
    List<ComprehensionTestResponseBo> comprehensionTestResponseList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      comprehensionTestResponseList =
          session
              .createQuery(
                  "From ComprehensionTestResponseBo CTRBO where CTRBO.comprehensionTestQuestionId= :comprehensionQuestionId")
              .setInteger("comprehensionQuestionId", comprehensionQuestionId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - deleteComprehensionTestQuestion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteComprehensionTestQuestion() - Ends");
    return comprehensionTestResponseList;
  }

  /**
   * Describes the get consent of an study which contains the shara data permission and e-consent
   * from details
   *
   * @author BTC
   * @param String : studyId in {@link StudyBo}
   * @return {@link ConsentBo}
   */
  @Override
  public ConsentBo getConsentDetailsByStudyId(String studyId) {
    logger.info("INFO: StudyDAOImpl - getConsentDetailsByStudyId() :: Starts");
    ConsentBo consentBo = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      consentBo =
          (ConsentBo)
              session
                  .createQuery(
                      "from ConsentBo CBO where CBO.studyId= :studyId order by CBO.createdOn desc")
                  .setString("studyId", studyId)
                  .setMaxResults(1)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentDetailsByStudyId() :: ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("INFO: StudyDAOImpl - getConsentDetailsByStudyId() :: Ends");
    return consentBo;
  }

  /**
   * Describes the get the consent of item from the list consent items of an study in the consent
   * section
   *
   * @author BTC
   * @param Integer , consentInfoId {@link ConsentInfoBo}
   * @return {@link ConsentInfoBo}
   */
  @Override
  public ConsentInfoBo getConsentInfoById(Integer consentInfoId) {
    logger.info("StudyDAOImpl - getConsentInfoById() - Starts");
    ConsentInfoBo consentInfoBo = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      consentInfoBo = (ConsentInfoBo) session.get(ConsentInfoBo.class, consentInfoId);
      if (consentInfoBo != null) {
        consentInfoBo.setDisplayTitle(
            StringUtils.isEmpty(consentInfoBo.getDisplayTitle())
                ? ""
                : consentInfoBo
                    .getDisplayTitle()
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "\'"));
        consentInfoBo.setBriefSummary(
            StringUtils.isEmpty(consentInfoBo.getBriefSummary())
                ? ""
                : consentInfoBo
                    .getBriefSummary()
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "\'"));
        consentInfoBo.setElaborated(
            StringUtils.isEmpty(consentInfoBo.getElaborated())
                ? ""
                : consentInfoBo
                    .getElaborated()
                    .replaceAll("&#34;", "\"")
                    .replaceAll("&#39;", "\'"));
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentInfoById() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getConsentInfoById() - Ends");
    return consentInfoBo;
  }

  /**
   * Describes to get the list of consent item which are added in the study consent section
   *
   * @author BTC
   * @param studyId {@link StudyBo}
   * @return List {@link ConsentInfoBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ConsentInfoBo> getConsentInfoDetailsListByStudyId(String studyId) {
    logger.info("INFO: StudyDAOImpl - getConsentInfoDetailsListByStudyId() :: Starts");
    Session session = null;
    List<ConsentInfoBo> consentInfoBoList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      consentInfoBoList =
          session
              .createQuery(
                  "from ConsentInfoBo CIBO where CIBO.studyId= :studyId and CIBO.active=1 ORDER BY CIBO.sequenceNo ")
              .setString("studyId", studyId)
              .list();
      if (null != consentInfoBoList && consentInfoBoList.size() > 0) {
        for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
          consentInfoBo.setDisplayTitle(
              consentInfoBo
                  .getDisplayTitle()
                  .replaceAll("<", "&#60;")
                  .replaceAll(">", "&#62;")
                  .replaceAll("/", "&#47;")
                  .replaceAll("'", "&#39;")
                  .replaceAll("\"", "&#34;"));
          consentInfoBo.setElaborated(
              consentInfoBo
                  .getElaborated()
                  .replaceAll("&#39;", "'")
                  .replaceAll("&#34;", "&quot;")
                  .replaceAll("em>", "i>")
                  .replaceAll(
                      "<a", "<a target='_blank' style='text-decoration:underline;color:blue;'"));
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentInfoDetailsListByStudyId() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("INFO: StudyDAOImpl - getConsentInfoDetailsListByStudyId() :: Ends");
    return consentInfoBoList;
  }

  /**
   * Describes to get the list of consent item which are added in the study consent section
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @return List {@link ConsentInfoBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ConsentInfoBo> getConsentInfoList(Integer studyId) {
    logger.info("StudyDAOImpl - getConsentInfoList() - Starts");
    List<ConsentInfoBo> consentInfoList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      consentInfoList =
          session
              .createQuery(
                  "From ConsentInfoBo CIB where CIB.studyId= :studyId and CIB.active=1 order by CIB.sequenceNo asc")
              .setInteger("studyId", studyId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentInfoList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getConsentInfoList() - Ends");
    return consentInfoList;
  }

  /**
   * ResearchKit already provides a few Consent Items screens by default.Here describes the getting
   * the list of research kit consents
   *
   * @author BTC
   * @return List : {@link ConsentMasterInfoBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ConsentMasterInfoBo> getConsentMasterInfoList() {
    logger.info("StudyDAOImpl - getConsentMasterInfoList() - Starts");
    Session session = null;
    List<ConsentMasterInfoBo> consentMasterInfoList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query = session.createQuery("From ConsentMasterInfoBo CMIB");
      consentMasterInfoList = query.list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentMasterInfoList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getConsentMasterInfoList() - Ends");
    return consentMasterInfoList;
  }

  /**
   * validate all study section completed or not before do Publish (as Upcoming
   * Study)/Launch/publish update of the study
   *
   * @author BTC
   * @param String , studyId
   * @param String , buttonText
   * @return {@link String}
   */
  public String getErrorBasedonAction(StudySequenceBo studySequenceBo) {
    String message = FdahpStudyDesignerConstants.SUCCESS;
    if (studySequenceBo != null) {
      if (!studySequenceBo.isBasicInfo()) {
        message = FdahpStudyDesignerConstants.BASICINFO_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isSettingAdmins()) {
        message = FdahpStudyDesignerConstants.SETTING_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isOverView()) {
        message = FdahpStudyDesignerConstants.OVERVIEW_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isEligibility()) {
        message = FdahpStudyDesignerConstants.ELIGIBILITY_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isConsentEduInfo()) {
        message = FdahpStudyDesignerConstants.CONSENTEDUINFO_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isComprehensionTest()) {
        message = FdahpStudyDesignerConstants.COMPREHENSIONTEST_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.iseConsent()) {
        message = FdahpStudyDesignerConstants.ECONSENT_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isStudyExcQuestionnaries()) {
        message = FdahpStudyDesignerConstants.STUDYEXCQUESTIONNARIES_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isStudyExcActiveTask()) {
        message = FdahpStudyDesignerConstants.STUDYEXCACTIVETASK_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isMiscellaneousResources()) {
        message = FdahpStudyDesignerConstants.RESOURCES_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.isCheckList()) {
        message = FdahpStudyDesignerConstants.CHECKLIST_ERROR_MSG;
        return message;
      } else if (!studySequenceBo.getParticipantProperties()) {
        message = FdahpStudyDesignerConstants.PARTICIPANT_PROPERTIES_ERROR_MSG;
        return message;
      }
    }
    return message;
  }

  /*------------------------------------Added By Vivek Start---------------------------------------------------*/

  /**
   * return Study version on customStudyid
   *
   * @author BTC
   * @param String , customStudyId
   * @return {@link StudyIdBean}
   */
  @Override
  public StudyIdBean getLiveVersion(String customStudyId) {
    logger.info("StudyDAOImpl - getLiveVersion() - Starts");
    Session session = null;
    StudyVersionBo studyVersionBo = null;
    Integer consentStudyId = null;
    StudyIdBean studyIdBean = new StudyIdBean();
    Integer activetaskStudyId = null;
    Integer questionnarieStudyId = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(customStudyId)) {
        query =
            session
                .getNamedQuery("getStudyByCustomStudyId")
                .setMaxResults(1)
                .setString("customStudyId", customStudyId);
        query.setMaxResults(1);
        studyVersionBo = (StudyVersionBo) query.uniqueResult();
        if (studyVersionBo != null) {
          activetaskStudyId =
              (Integer)
                  session
                      .createSQLQuery(
                          "SELECT s.study_id FROM active_task s where s.custom_study_id= :customStudyId and is_live =1")
                      .setString("customStudyId", customStudyId)
                      .setMaxResults(1)
                      .uniqueResult();

          questionnarieStudyId =
              (Integer)
                  session
                      .createSQLQuery(
                          "SELECT s.study_id FROM questionnaires s where s.custom_study_id= :customStudyId and is_live =1")
                      .setString("customStudyId", customStudyId)
                      .setMaxResults(1)
                      .uniqueResult();

          consentStudyId =
              (Integer)
                  session
                      .createSQLQuery(
                          "SELECT s.study_id FROM consent s where s.custom_study_id= :customStudyId and round(s.version, 1) = :version and is_live=:isLive")
                      .setString("customStudyId", customStudyId)
                      .setFloat("version", studyVersionBo.getConsentVersion())
                      .setInteger("isLive", 1)
                      .setMaxResults(1)
                      .uniqueResult();

          if (consentStudyId == null) {
            consentStudyId =
                (Integer)
                    session
                        .createSQLQuery(
                            "SELECT s.study_id FROM consent_info s where s.custom_study_id= :customStudyId and round(s.version, 1) = :version")
                        .setString("customStudyId", customStudyId)
                        .setFloat("version", studyVersionBo.getConsentVersion())
                        .setMaxResults(1)
                        .uniqueResult();
          }

          studyIdBean.setActivetaskStudyId(activetaskStudyId);
          studyIdBean.setQuestionnarieStudyId(questionnarieStudyId);
          studyIdBean.setConsentStudyId(consentStudyId);
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getLiveVersion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getLiveVersion() - Ends");
    return studyIdBean;
  }

  /**
   * This method is used to get the notification data
   *
   * @author BTC
   * @param resourseId
   * @return {@link NotificationBO}
   */
  @Override
  public NotificationBO getNotificationByResourceId(Integer resourseId) {
    logger.info("StudyDAOImpl - getNotificationByResourceId() - Starts");
    Session session = null;
    NotificationBO notificationBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      notificationBO =
          (NotificationBO)
              session
                  .createQuery("FROM NotificationBO NBO WHERE NBO.resourceId = :resourseId")
                  .setInteger("resourseId", resourseId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getNotificationByResourceId - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getNotificationByResourceId - Ends");
    return notificationBO;
  }

  /**
   * This method return content(A title,Description,Image) of Overview pages of the Study
   *
   * @author BTC
   * @param String , studyId in {@link StudyBo}
   * @return {@link List<StudyPageBo>}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StudyPageBo> getOverviewStudyPagesById(String studyId, Integer userId) {
    logger.info("StudyDAOImpl - getOverviewStudyPagesById() - Starts");
    Session session = null;
    List<StudyPageBo> studyPageBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(studyId)) {
        studyPageBo =
            session
                .createQuery("from StudyPageBo where studyId= :studyId")
                .setString("studyId", studyId)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getOverviewStudyPagesById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getOverviewStudyPagesById() - Ends");
    return studyPageBo;
  }

  /**
   * return reference List based on category
   *
   * @author BTC
   * @return {@link HashMap<String, List<ReferenceTablesBo>>}
   */
  @SuppressWarnings({"unchecked"})
  @Override
  public HashMap<String, List<ReferenceTablesBo>> getreferenceListByCategory() {
    logger.info("StudyDAOImpl - getreferenceListByCategory() - Starts");
    Session session = null;
    List<ReferenceTablesBo> allReferenceList = null;
    List<ReferenceTablesBo> categoryList = new ArrayList<>();
    List<ReferenceTablesBo> researchSponserList = new ArrayList<>();
    List<ReferenceTablesBo> dataPartnerList = new ArrayList<>();
    HashMap<String, List<ReferenceTablesBo>> referenceMap = new HashMap<>();
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query = session.createQuery("from ReferenceTablesBo order by category asc,id asc");
      allReferenceList = query.list();
      if (allReferenceList != null && !allReferenceList.isEmpty()) {
        for (ReferenceTablesBo referenceTablesBo : allReferenceList) {
          if (StringUtils.isNotEmpty(referenceTablesBo.getCategory())) {
            switch (referenceTablesBo.getCategory()) {
              case FdahpStudyDesignerConstants.REFERENCE_TYPE_CATEGORIES:
                categoryList.add(referenceTablesBo);
                break;
              case FdahpStudyDesignerConstants.REFERENCE_TYPE_RESEARCH_SPONSORS:
                researchSponserList.add(referenceTablesBo);
                break;
              case FdahpStudyDesignerConstants.REFERENCE_TYPE_DATA_PARTNER:
                dataPartnerList.add(referenceTablesBo);
                break;

              default:
                break;
            }
          }
        }
        referenceMap = new HashMap<>();
        if (!categoryList.isEmpty())
          referenceMap.put(FdahpStudyDesignerConstants.REFERENCE_TYPE_CATEGORIES, categoryList);
        if (!researchSponserList.isEmpty())
          referenceMap.put(
              FdahpStudyDesignerConstants.REFERENCE_TYPE_RESEARCH_SPONSORS, researchSponserList);
        if (!dataPartnerList.isEmpty())
          referenceMap.put(
              FdahpStudyDesignerConstants.REFERENCE_TYPE_DATA_PARTNER, dataPartnerList);
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getreferenceListByCategory() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getreferenceListByCategory() - Ends");
    return referenceMap;
  }

  /**
   * This method is used to get the resource information
   *
   * @author BTC
   * @param resourceInfoId
   * @return {@link ResourceBO}
   */
  @Override
  public ResourceBO getResourceInfo(Integer resourceInfoId) {
    logger.info("StudyDAOImpl - getResourceInfo() - Starts");
    ResourceBO resourceBO = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query = session.getNamedQuery("getResourceInfo").setInteger("resourceInfoId", resourceInfoId);
      resourceBO = (ResourceBO) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResourceInfo() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getResourceInfo() - Ends");
    return resourceBO;
  }

  /**
   * This method is used to get the list of resources
   *
   * @author BTC
   * @param studyId
   * @return List of {@link ResourceBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ResourceBO> getResourceList(Integer studyId) {
    logger.info("StudyDAOImpl - getResourceList() - Starts");
    List<ResourceBO> resourceBOList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      resourceBOList =
          session
              .createQuery(
                  "FROM ResourceBO RBO WHERE RBO.studyId= :studyId AND RBO.status = 1 AND RBO.studyProtocol = false ORDER BY RBO.createdOn ASC")
              .setInteger("studyId", studyId)
              .list();

      if (resourceBOList != null && !resourceBOList.isEmpty()) {
        int sequenceNo = 1;
        for (ResourceBO rBO : resourceBOList) {
          if (rBO.getSequenceNo() == null || rBO.getSequenceNo().equals(0)) {
            rBO.setSequenceNo(sequenceNo);
            session.update(rBO);
            sequenceNo++;
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - getResourceList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getResourceList() - Ends");
    return resourceBOList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<NotificationBO> getSavedNotification(Integer studyId) {
    logger.info("StudyDAOImpl - getSavedNotification() - Starts");
    List<NotificationBO> notificationSavedList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      notificationSavedList =
          session
              .createQuery(
                  "FROM NotificationBO NBO WHERE NBO.studyId= :studyId AND NBO.notificationAction = 0 AND NBO.notificationType='ST' AND NBO.notificationSubType='Announcement'")
              .setInteger("studyId", studyId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getSavedNotification() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getSavedNotification() - Ends");
    return notificationSavedList;
  }

  /**
   * get study details
   *
   * @author BTC
   * @param String , studyId
   * @param Integer , userId
   * @return studyBo, {@link StudyBo}
   */
  @Override
  public StudyBo getStudyById(String studyId, Integer userId) {
    logger.info("StudyDAOImpl - getStudyById() - Starts");
    Session session = null;
    StudyBo studyBo = null;
    StudySequenceBo studySequenceBo = null;
    StudyPermissionBO permissionBO = null;
    StudyVersionBo studyVersionBo = null;
    StudyBo liveStudyBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(studyId)) {
        studyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", Integer.parseInt(studyId))
                    .uniqueResult();
        studySequenceBo =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, Integer.parseInt(studyId))
                    .uniqueResult();
        permissionBO =
            (StudyPermissionBO)
                session
                    .getNamedQuery("getStudyPermissionById")
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, Integer.parseInt(studyId))
                    .setInteger("userId", userId)
                    .uniqueResult();
        if (studySequenceBo != null) studyBo.setStudySequenceBo(studySequenceBo);
        if (permissionBO != null) studyBo.setViewPermission(permissionBO.isViewPermission());
        if (studyBo != null) {
          // To get latest version of study, consent version ,
          // activity version
          query =
              session
                  .getNamedQuery("getStudyByCustomStudyId")
                  .setMaxResults(1)
                  .setString("customStudyId", studyBo.getCustomStudyId());
          query.setMaxResults(1);
          studyVersionBo = (StudyVersionBo) query.uniqueResult();
          if (studyVersionBo != null) {
            studyVersionBo.setStudyLVersion(" V" + studyVersionBo.getStudyVersion());
            studyVersionBo.setConsentLVersion(" (V" + studyVersionBo.getConsentVersion() + ")");
            studyVersionBo.setActivityLVersion(" (V" + studyVersionBo.getActivityVersion() + ")");
            studyBo.setStudyVersionBo(studyVersionBo);
          }
          // To get the live version of study by passing customstudyId
          liveStudyBo =
              (StudyBo)
                  session
                      .createQuery("FROM StudyBo where customStudyId= :customStudyId and live=1")
                      .setString("customStudyId", studyBo.getCustomStudyId())
                      .uniqueResult();
          if (liveStudyBo != null) {
            studyBo.setLiveStudyBo(liveStudyBo);
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyById() - Ends");
    return studyBo;
  }

  @Override
  public StudyBo getStudyBoById(String studyId) {
    logger.info("StudyDAOImpl - getStudyBoById() - Starts");
    Session session = null;
    StudyBo studyBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(studyId)) {
        studyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", Integer.parseInt(studyId))
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyBoById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyBoById() - Ends");
    return studyBo;
  }
  /**
   * return eligibility based on user's Study Id
   *
   * @author BTC
   * @param studyId , studyId of the {@link StudyBo}
   * @return {@link EligibilityBo}
   * @exception Exception
   */
  @Override
  public EligibilityBo getStudyEligibiltyByStudyId(String studyId) {
    logger.info("StudyDAOImpl - getStudyEligibiltyByStudyId() - Starts");
    Session session = null;
    EligibilityBo eligibilityBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(studyId)) {
        query =
            session
                .getNamedQuery("getEligibiltyByStudyId")
                .setInteger(FdahpStudyDesignerConstants.STUDY_ID, Integer.parseInt(studyId));
        eligibilityBo = (EligibilityBo) query.uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyEligibiltyByStudyId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyEligibiltyByStudyId() - Ends");
    return eligibilityBo;
  }

  /**
   * ********************************** Added By BTC Start
   * ***********************************************
   */
  /**
   * return study List based on user
   *
   * @author BTc
   * @param Integer , userId in {@link UserBO}
   * @return {@link List<StudyListBean>}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StudyListBean> getStudyList(Integer userId) {
    logger.info("StudyDAOImpl - getStudyList() - Starts");
    Session session = null;
    List<StudyListBean> studyListBeans = null;
    String name = "";
    List<ReferenceTablesBo> referenceTablesBos = null;
    StudyBo liveStudy = null;
    StudyBo studyBo = null;
    try {

      session = hibernateTemplate.getSessionFactory().openSession();
      if (userId != null && userId != 0) {
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.StudyListBean(s.id,s.customStudyId,s.name,s.category,s.researchSponsor,user.firstName, user.lastName,p.viewPermission,s.status,s.createdOn)"
                    + " from StudyBo s,StudyPermissionBO p, UserBO user where s.id=p.studyId"
                    + " and user.userId = s.createdBy and s.version=0 and p.userId=:impValue"
                    + " order by s.createdOn desc");
        query.setParameter(FdahpStudyDesignerConstants.IMP_VALUE, userId);
        studyListBeans = query.list();

        if (studyListBeans != null && !studyListBeans.isEmpty()) {
          for (StudyListBean bean : studyListBeans) {
            if (StringUtils.isNotEmpty(name)) bean.setProjectLeadName(name);
            if (StringUtils.isNotEmpty(bean.getCategory())
                && StringUtils.isNotEmpty(bean.getResearchSponsor())) {
              // get the Category, Research Sponsor name of the
              // study from categoryIds
              referenceTablesBos =
                  session
                      .createQuery("from ReferenceTablesBo where id in(:category)")
                      .setInteger("category", Integer.valueOf(bean.getCategory()))
                      .list();
              if (referenceTablesBos != null && !referenceTablesBos.isEmpty()) {
                bean.setCategory(referenceTablesBos.get(0).getValue());
              }
            }
            if (StringUtils.isNotEmpty(bean.getCustomStudyId())) {
              liveStudy =
                  (StudyBo)
                      session
                          .createQuery(
                              "from StudyBo where customStudyId= :customStudyId and live=1")
                          .setString("customStudyId", bean.getCustomStudyId())
                          .uniqueResult();
              if (liveStudy != null) {
                bean.setLiveStudyId(liveStudy.getId());
              } else {
                bean.setLiveStudyId(null);
              }
            }
            // if is there any change in study then edit with dot
            // will come
            if (bean.getId() != null && bean.getLiveStudyId() != null) {
              studyBo =
                  (StudyBo)
                      session
                          .createQuery("from StudyBo where id= :studyId")
                          .setInteger("studyId", bean.getId())
                          .uniqueResult();
              if (studyBo.getHasStudyDraft() == 1) bean.setFlag(true);
            }
            // if is there any team lead in that study
            if (bean.getId() != null) {
              String userInfo =
                  (String)
                      session
                          .createQuery(
                              "SELECT u.firstName from StudyPermissionBO s , UserBO u where s.studyId= :studyId and s.userId=u.userId and s.projectLead=1")
                          .setInteger("studyId", bean.getId())
                          .setMaxResults(1)
                          .uniqueResult();
              if (StringUtils.isNotEmpty(userInfo)) {
                bean.setProjectLeadName(userInfo);
              } else {
                bean.setProjectLeadName("None");
              }
            }
          }
        }
      }

    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyList() - Ends");
    return studyListBeans;
  }

  /**
   * This method is used to return study List based on user
   *
   * @author BTC
   * @param Integer , userId
   * @return {@link StudyListBean}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<StudyListBean> getStudyListByUserId(Integer userId) {
    logger.info("StudyDAOImpl - getStudyListByUserId() - Starts");
    Session session = null;
    List<StudyListBean> studyListBeans = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (userId != null && userId != 0) {
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.StudyListBean(s.id,s.customStudyId,s.name,p.viewPermission)"
                    + " from StudyBo s,StudyPermissionBO p where s.id=p.studyId and s.version = 0"
                    + " and p.userId=:impValue");
        query.setParameter("impValue", userId);
        studyListBeans = query.list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyListByUserId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyListByUserId() - Ends");
    return studyListBeans;
  }

  /**
   * This method is to get live Study details
   *
   * @author BTC
   * @param String , customStudyId
   * @return {@link StudyBo}
   */
  @Override
  public StudyBo getStudyLiveStatusByCustomId(String customStudyId) {
    logger.info("StudyDAOImpl - getStudyLiveStatusByCustomId() - Starts");
    StudyBo studyLive = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      studyLive =
          (StudyBo)
              session
                  .createQuery(
                      "FROM StudyBo SBO WHERE SBO.customStudyId = :customStudyId AND SBO.live = 1")
                  .setString("customStudyId", customStudyId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyLiveStatusByCustomId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyLiveStatusByCustomId() - Ends");
    return studyLive;
  }

  /**
   * This method is used to get the a special resource called study protocol
   *
   * @author BTC
   * @param studyId
   * @return {@link ResourceBO}
   */
  @Override
  public ResourceBO getStudyProtocol(Integer studyId) {
    logger.info("StudyDAOImpl - getStudyProtocol() - Starts");
    ResourceBO studyprotocol = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      studyprotocol =
          (ResourceBO)
              session
                  .createQuery(
                      "FROM ResourceBO RBO WHERE RBO.studyId= :studyId AND RBO.studyProtocol = true")
                  .setInteger("studyId", studyId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyProtocol() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyProtocol() - Ends");
    return studyprotocol;
  }

  /**
   * This method is used to get the super admins user Ids
   *
   * @author BTC
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Integer> getSuperAdminUserIds() {
    logger.info("StudyDAOImpl - getSuperAdminUserIds() - Starts");
    Session session = null;
    List<Integer> superAdminUserIds = null;
    Query query = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session.createSQLQuery(
              "SELECT u.user_id FROM users u WHERE u.user_id in (SELECT upm.user_id FROM user_permission_mapping upm WHERE upm.permission_id = (SELECT up.permission_id FROM user_permissions up WHERE up.permissions = 'ROLE_SUPERADMIN'))");
      superAdminUserIds = query.list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getSuperAdminUserIds() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getSuperAdminUserIds() - Ends");
    return superAdminUserIds;
  }

  /**
   * mark as completed of study
   *
   * @author BTC
   * @param Integer , studyId
   * @param String , markCompleted
   * @param sesObj , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return String, SUCCES/FAILURE
   */
  @Override
  public String markAsCompleted(
      int studyId, String markCompleted, boolean flag, SessionObject sesObj, String customStudyId) {
    logger.info("StudyDAOImpl - markAsCompleted() - Starts");
    String msg = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    String activity = "";
    String activityDetails = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      /**
       * match the markCompleted flag and complete the resource/notification/consent/consent review
       * /checkList/activeTaskList/questionnaire/comprehenstionTest section of study before launch
       */
      if (markCompleted.equals(FdahpStudyDesignerConstants.NOTIFICATION)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET miscellaneousNotification = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity =
              "Study Notifications section successfully  checked for minimum content completeness.";
          activityDetails =
              "Study Notifications section successfully  checked for minimum content completeness and marked 'Completed'. (Study ID = "
                  + customStudyId
                  + ")";
        }
      } else if (markCompleted.equals(FdahpStudyDesignerConstants.RESOURCE)) {
        if (flag) {
          activity = "Resources section successsfully checked for minimum content completeness.";
          activityDetails =
              "Resources section successsfully checked for minimum content completeness and marked 'Completed'. (Study ID = "
                  + customStudyId
                  + ")";
          auditLogDAO.updateDraftToEditedStatus(
              session,
              transaction,
              sesObj.getUserId(),
              FdahpStudyDesignerConstants.DRAFT_STUDY,
              studyId);
        }
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET miscellaneousResources = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.CONESENT)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET consentEduInfo = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity =
              "Study Consent section successsfully checked for minimum content completeness.";
          activityDetails =
              "Consent Sections successfully checked for minimum content completeness and this ection of the Study is marked 'Completed'.  (Study ID = "
                  + customStudyId
                  + ")";
        }
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_CONSCENT,
            studyId);
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.CONESENT_REVIEW)) {
        count =
            session
                .createQuery("UPDATE StudySequenceBo SET eConsent = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_CONSCENT,
            studyId);
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.CHECK_LIST)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET checkList = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTIVETASK_LIST)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET studyExcActiveTask = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity = "Active Tasks section successfully checked for minimum Content Completeness.";
          activityDetails =
              "Active Tasks section successfully checked for minimum Content Completeness and marked 'Completed'. (Study ID = "
                  + customStudyId
                  + ")";
        }
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_ACTIVETASK,
            studyId);
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTIONNAIRE)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET studyExcQuestionnaries = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity = "Questionnaire succesfully checked for minimum content completeness.";
          activityDetails =
              "Questionnaire succesfully checked for minimum content completeness and marked 'Done'. (Study ID = "
                  + customStudyId
                  + ")";
        }
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_QUESTIONNAIRE,
            studyId);
      } else if (markCompleted.equalsIgnoreCase(FdahpStudyDesignerConstants.COMPREHENSION_TEST)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET comprehensionTest = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity = "comprehension test";
          activityDetails =
              customStudyId
                  + " -- "
                  + FdahpStudyDesignerConstants.QUESTIONNAIRELIST_MARKED_AS_COMPLETED;
        }
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_CONSCENT,
            studyId);
      } else if (markCompleted.equalsIgnoreCase(
          FdahpStudyDesignerConstants.PARTICIPANT_PROPERTIES)) {
        count =
            session
                .createQuery(
                    "UPDATE StudySequenceBo SET participantProperties = :flag WHERE studyId = :studyId")
                .setBoolean("flag", flag)
                .setInteger("studyId", studyId)
                .executeUpdate();
        if (flag) {
          activity = "Participant Properties succesfully checked for minimum content completeness.";
          activityDetails =
              "Participant Properties succesfully checked for minimum content completeness and marked 'Done'. (Study ID = "
                  + customStudyId
                  + ")";
        }
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sesObj.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_PARTICIPANT_PROPERTIES,
            studyId);
      }
      if (count > 0) {
        msg = FdahpStudyDesignerConstants.SUCCESS;
      }
      if (sesObj != null
          && FdahpStudyDesignerUtil.isNotEmpty(activity)
          && FdahpStudyDesignerUtil.isNotEmpty(activityDetails)) {
        auditLogDAO.saveToAuditLog(
            session,
            transaction,
            sesObj,
            activity,
            activityDetails,
            "StudyDAOImpl - markAsCompleted");
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - markAsCompleted() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - markAsCompleted() - Ends");
    return msg;
  }

  /**
   * Comprehension question will be created by default in the master order.Admin can manage the
   * order by drag and drop questions in the list
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param int , oldOrderNumber
   * @param int , newOrderNumber
   * @return String SUCCESS or FAILURE
   */
  @Override
  public String reOrderComprehensionTestQuestion(
      Integer studyId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyDAOImpl - reOrderComprehensionTestQuestion() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    StudySequenceBo studySequence = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      comprehensionTestQuestionBo =
          (ComprehensionTestQuestionBo)
              session
                  .createQuery(
                      "From ComprehensionTestQuestionBo CTB where CTB.studyId= :studyId and CTB.sequenceNo = :sequenceNo and CTB.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("sequenceNo", oldOrderNumber)
                  .uniqueResult();
      if (comprehensionTestQuestionBo != null) {
        if (oldOrderNumber < newOrderNumber) {
          count =
              session
                  .createQuery(
                      "update ComprehensionTestQuestionBo CTB set CTB.sequenceNo=CTB.sequenceNo-1 where "
                          + "CTB.studyId= :studyId and CTB.sequenceNo <= :newSequenceNo and CTB.sequenceNo > :oldSequenceNo and CTB.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newSequenceNo", newOrderNumber)
                  .setInteger("oldSequenceNo", oldOrderNumber)
                  .executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ComprehensionTestQuestionBo CTB set CTB.sequenceNo= :newSequenceNo where "
                            + "CTB.id= :comprehensionTestQuestionId and CTB.active=1")
                    .setInteger("newSequenceNo", newOrderNumber)
                    .setInteger("comprehensionTestQuestionId", comprehensionTestQuestionBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else if (oldOrderNumber > newOrderNumber) {
          count =
              session
                  .createQuery(
                      "update ComprehensionTestQuestionBo CTB set CTB.sequenceNo=CTB.sequenceNo+1 where CTB.studyId= :studyId"
                          + " and CTB.sequenceNo >= :newSequenceNo and CTB.sequenceNo < :oldSequenceNo and CTB.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newSequenceNo", newOrderNumber)
                  .setInteger("oldSequenceNo", oldOrderNumber)
                  .executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ComprehensionTestQuestionBo CTB set CTB.sequenceNo= :newSequenceNo where CTB.id= :comprehensionTestQuestionId and CTB.active=1")
                    .setInteger("newSequenceNo", newOrderNumber)
                    .setInteger("comprehensionTestQuestionId", comprehensionTestQuestionBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
        if (comprehensionTestQuestionBo.getStudyId() != null) {
          studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(
                          FdahpStudyDesignerConstants.STUDY_ID,
                          comprehensionTestQuestionBo.getStudyId())
                      .uniqueResult();
          if (studySequence != null) {
            if (studySequence.isComprehensionTest()) {
              studySequence.setComprehensionTest(false);
            }
            session.saveOrUpdate(studySequence);
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - reOrderComprehensionTestQuestion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - reOrderComprehensionTestQuestion() - Ends");
    return message;
  }

  /**
   * Consent items will be created by default in master order.Admin can manage the order by
   * reordering the consents with in a list to make reorder admin can drag and drop the consent with
   * in a list
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param int , oldOrderNumber
   * @param int , newOrderNumber
   * @return String SUCCESS or FAILURE
   */
  @Override
  public String reOrderConsentInfoList(Integer studyId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyDAOImpl - reOrderConsentInfoList() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    Query query = null;
    int count = 0;
    ConsentInfoBo consentInfoBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      consentInfoBo =
          (ConsentInfoBo)
              session
                  .createQuery(
                      "From ConsentInfoBo CIB where CIB.studyId= :studyId and CIB.sequenceNo = :oldOrderNumber and CIB.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .uniqueResult();
      if (consentInfoBo != null) {
        if (oldOrderNumber < newOrderNumber) {
          count =
              session
                  .createQuery(
                      "update ConsentInfoBo CIBO set CIBO.sequenceNo=CIBO.sequenceNo-1 where CIBO.studyId= :studyId"
                          + " and CIBO.sequenceNo <= :newOrderNumber and CIBO.sequenceNo > :oldOrderNumber and CIBO.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ConsentInfoBo C set C.sequenceNo= :newOrderNumber where C.id= :consentInfo and C.active=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("consentInfo", consentInfoBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else if (oldOrderNumber > newOrderNumber) {
          query =
              session
                  .createQuery(
                      "update ConsentInfoBo CIBO set CIBO.sequenceNo=CIBO.sequenceNo+1 where CIBO.studyId= :studyId"
                          + " and CIBO.sequenceNo >= :newOrderNumber and CIBO.sequenceNo < :oldOrderNumber and CIBO.active=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber);
          count = query.executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ConsentInfoBo C set C.sequenceNo= :newOrderNumber where C.id= :consentInfoId and C.active=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("consentInfoId", consentInfoBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          StudySequenceBo studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                      .uniqueResult();
          if (studySequence != null) {
            studySequence.setConsentEduInfo(false);
            if (studySequence.iseConsent()) {
              studySequence.seteConsent(false);
            }
            session.saveOrUpdate(studySequence);
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - reOrderConsentInfoList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - reOrderConsentInfoList() - Ends");
    return message;
  }

  /**
   * Reorder the eligibility test questions
   *
   * @author BTC
   * @param eligibilityId , Eligibility Id of the study
   * @param oldOrderNumber , Old order
   * @param newOrderNumber , New order
   * @param studyId , Id of the study
   * @return {@link FdahpStudyDesignerConstants.SUCCESS} or {@link
   *     FdahpStudyDesignerConstants.FAILURE} , reorder status
   */
  @Override
  public String reorderEligibilityTestQusAns(
      Integer eligibilityId, int oldOrderNumber, int newOrderNumber, Integer studyId) {
    logger.info("StudyDAOImpl - reorderEligibilityTestQusAns - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    Query hibQuery = null;
    int count = 0;
    EligibilityTestBo eligibilityTest = null;
    Transaction trans = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      trans = session.beginTransaction();
      hibQuery =
          session
              .getNamedQuery("EligibilityTestBo.findByEligibilityIdAndSequenceNo")
              .setInteger("eligibilityId", eligibilityId)
              .setInteger("sequenceNo", oldOrderNumber);
      eligibilityTest = (EligibilityTestBo) hibQuery.uniqueResult();
      if (eligibilityTest != null) {
        if (oldOrderNumber < newOrderNumber) {
          count =
              session
                  .createQuery(
                      "UPDATE EligibilityTestBo ETB set ETB.sequenceNo=ETB.sequenceNo-1 WHERE ETB.eligibilityId= :eligibilityId AND "
                          + "ETB.sequenceNo <= :newOrderNumber AND ETB.sequenceNo > :oldOrderNumber AND ETB.active = true")
                  .setInteger("eligibilityId", eligibilityId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .executeUpdate();
        } else if (oldOrderNumber > newOrderNumber) {
          count =
              session
                  .createQuery(
                      "UPDATE EligibilityTestBo ETB set ETB.sequenceNo=ETB.sequenceNo+1 WHERE ETB.eligibilityId= :eligibilityId"
                          + " AND ETB.sequenceNo >= :newOrderNumber AND ETB.sequenceNo < :oldOrderNumber AND ETB.active = true")
                  .setInteger("eligibilityId", eligibilityId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .executeUpdate();
        }
        if (count > 0) {
          count =
              session
                  .createQuery(
                      "UPDATE EligibilityTestBo ETB set ETB.sequenceNo= :newOrderNumber WHERE ETB.id= :eligibilityTestId")
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("eligibilityTestId", eligibilityTest.getId())
                  .executeUpdate();
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          StudySequenceBo studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                      .uniqueResult();
          if (studySequence != null && studySequence.isEligibility()) {
            studySequence.setEligibility(false);
            session.update(studySequence);
          }
        }
      }
      trans.commit();
    } catch (Exception e) {
      if (null != trans) trans.rollback();
      logger.error("StudyDAOImpl - reorderEligibilityTestQusAns - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - reorderEligibilityTestQusAns - Ends");
    return message;
  }

  @Override
  public String reOrderResourceList(Integer studyId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyDAOImpl - reOrderResourceList() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    ResourceBO resourceBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      resourceBo =
          (ResourceBO)
              session
                  .createQuery(
                      "From ResourceBO RBO where RBO.studyId= :studyId and RBO.sequenceNo = :oldOrderNumber and RBO.status=1")
                  .setInteger("studyId", studyId)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .uniqueResult();
      if (resourceBo != null) {
        if (oldOrderNumber < newOrderNumber) {
          count =
              session
                  .createQuery(
                      "update ResourceBO RBO set RBO.sequenceNo=RBO.sequenceNo-1 where RBO.studyId= :studyId"
                          + " and RBO.sequenceNo <= :newOrderNumber and RBO.sequenceNo > :oldOrderNumber and RBO.status=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ResourceBO RBO set RBO.sequenceNo= :newOrderNumber where RBO.id= :resourceId and RBO.status=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("resourceId", resourceBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else if (oldOrderNumber > newOrderNumber) {
          count =
              session
                  .createQuery(
                      "update ResourceBO RBO set RBO.sequenceNo=RBO.sequenceNo+1 where RBO.studyId= :studyId"
                          + " and RBO.sequenceNo >= :newOrderNumber and RBO.sequenceNo < :oldOrderNumber and RBO.status=1")
                  .setInteger("studyId", studyId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber)
                  .executeUpdate();
          if (count > 0) {
            count =
                session
                    .createQuery(
                        "update ResourceBO RBO set RBO.sequenceNo= :newOrderNumber where RBO.id= :resourceId and RBO.status=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("resourceId", resourceBo.getId())
                    .executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          StudySequenceBo studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                      .uniqueResult();
          if (studySequence != null) {
            studySequence.setMiscellaneousResources(false);
            session.saveOrUpdate(studySequence);
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - reOrderResourceList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - reOrderResourceList() - Ends");
    return message;
  }

  /**
   * reset study by customStudyId
   *
   * @author BTC
   * @param String , customStudyId
   * @return boolean
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public boolean resetDraftStudyByCustomStudyId(
      String customStudyId, String action, SessionObject sesObj) {
    logger.info("StudyDAOImpl - resetDraftStudyByCustomStudyId() - Starts");
    Session session = null;
    boolean flag = false;
    StudyBo liveStudyBo = null;
    List<StudyBo> draftDatas = null;
    List<StudyPageBo> studyPageBo = null;
    EligibilityBo eligibilityBo = null;
    List<ResourceBO> resourceBOList = null;
    List<QuestionnaireBo> questionnaires = null;
    QuestionReponseTypeBo questionReponseTypeBo = null;
    List<Integer> studyIdList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      liveStudyBo =
          (StudyBo)
              session
                  .getNamedQuery("getStudyLiveVersion")
                  .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId)
                  .uniqueResult();
      if (liveStudyBo != null) {
        // create new Study and made it draft
        StudyBo studyDreaftBo = SerializationUtils.clone(liveStudyBo);
        studyDreaftBo.setVersion(0f);
        studyDreaftBo.setId(null);
        if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY)) {
          studyDreaftBo.setLive(5);
          studyDreaftBo.setThumbnailImage(null);
        } else {
          studyDreaftBo.setCustomStudyId(null);
          studyDreaftBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          studyDreaftBo.setCreatedBy(sesObj.getUserId());
          studyDreaftBo.setModifiedOn(null);
          studyDreaftBo.setModifiedBy(null);
          studyDreaftBo.setLive(0);
        }
        studyDreaftBo.setHasActivetaskDraft(0);
        studyDreaftBo.setHasQuestionnaireDraft(0);
        studyDreaftBo.setHasConsentDraft(0);
        studyDreaftBo.setHasStudyDraft(0);
        studyDreaftBo.setStatus(FdahpStudyDesignerConstants.STUDY_PRE_LAUNCH);
        session.save(studyDreaftBo);

        // Study Permission
        String permissionQuery =
            "select s.user_id,s.view_permission"
                + " from study_permission s, user_permission_mapping u, user_permissions p"
                + " where s.study_id= :id"
                + " and s.user_id=u.user_id"
                + " and u.permission_id = p.permission_id"
                + " and p.permissions='ROLE_SUPERADMIN'";
        Iterator iterator =
            session
                .createSQLQuery(permissionQuery)
                .setInteger("id", liveStudyBo.getId())
                .list()
                .iterator();
        while (iterator.hasNext()) {
          Object[] objects = (Object[]) iterator.next();
          if (objects != null) {
            Integer superadminId = (Integer) objects[0];
            StudyPermissionBO studyPermissionBO = new StudyPermissionBO();
            studyPermissionBO.setUserId((Integer) objects[0]);
            studyPermissionBO.setViewPermission((Boolean) objects[1]);
            studyPermissionBO.setStudyId(studyDreaftBo.getId());
            studyPermissionBO.setStudyPermissionId(null);
            session.save(studyPermissionBO);
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)
                && !superadminId.equals(sesObj.getUserId())) {
              studyPermissionBO = new StudyPermissionBO();
              studyPermissionBO.setUserId(sesObj.getUserId());
              studyPermissionBO.setViewPermission((Boolean) objects[1]);
              studyPermissionBO.setStudyId(studyDreaftBo.getId());
              studyPermissionBO.setStudyPermissionId(null);
              session.save(studyPermissionBO);
            }
          }
        }
        // studySequence
        StudySequenceBo studySequenceBo = new StudySequenceBo();
        studySequenceBo.setStudyId(studyDreaftBo.getId());
        session.save(studySequenceBo);

        // Over View
        query = session.createQuery("from StudyPageBo where studyId=:id ");
        query.setInteger("id", liveStudyBo.getId());
        studyPageBo = query.list();
        if (studyPageBo != null && !studyPageBo.isEmpty()) {
          for (StudyPageBo pageBo : studyPageBo) {
            StudyPageBo subPageBo = SerializationUtils.clone(pageBo);
            subPageBo.setStudyId(studyDreaftBo.getId());
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY)) {
              subPageBo.setImagePath(null);
            }
            subPageBo.setPageId(null);
            session.save(subPageBo);
          }
        }

        // Eligibility
        query =
            session
                .getNamedQuery("getEligibiltyByStudyId")
                .setInteger(FdahpStudyDesignerConstants.STUDY_ID, liveStudyBo.getId());
        eligibilityBo = (EligibilityBo) query.uniqueResult();
        if (eligibilityBo != null) {
          EligibilityBo bo = SerializationUtils.clone(eligibilityBo);
          bo.setStudyId(studyDreaftBo.getId());
          bo.setId(null);
          session.save(bo);
          List<EligibilityTestBo> eligibilityTestList = null;
          eligibilityTestList =
              session
                  .getNamedQuery("EligibilityTestBo.findByEligibilityId")
                  .setInteger(FdahpStudyDesignerConstants.ELIGIBILITY_ID, eligibilityBo.getId())
                  .list();
          if (eligibilityTestList != null && !eligibilityTestList.isEmpty()) {
            for (EligibilityTestBo eligibilityTestBo : eligibilityTestList) {
              EligibilityTestBo newEligibilityTestBo = SerializationUtils.clone(eligibilityTestBo);
              newEligibilityTestBo.setId(null);
              newEligibilityTestBo.setEligibilityId(bo.getId());
              newEligibilityTestBo.setUsed(false);
              if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                newEligibilityTestBo.setStatus(false);
              }
              session.save(newEligibilityTestBo);
            }
          }
        }

        // resources
        String searchQuery =
            "FROM ResourceBO RBO WHERE RBO.studyId=:id AND RBO.status = 1 ORDER BY RBO.createdOn";
        query = session.createQuery(searchQuery);
        query.setParameter("id", liveStudyBo.getId());
        resourceBOList = query.list();
        if (resourceBOList != null && !resourceBOList.isEmpty()) {
          for (ResourceBO bo : resourceBOList) {
            ResourceBO resourceBO = SerializationUtils.clone(bo);
            resourceBO.setStudyId(studyDreaftBo.getId());
            if (StringUtils.isNotEmpty(bo.getPdfUrl()) && StringUtils.isNotEmpty(bo.getPdfName())) {
              resourceBO.setAction(false);
            }
            resourceBO.setId(null);
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
              resourceBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              resourceBO.setCreatedBy(sesObj.getUserId());
              resourceBO.setModifiedBy(null);
              resourceBO.setModifiedOn(null);
            } else {
              resourceBO.setPdfName(null);
              resourceBO.setPdfUrl(null);
            }
            session.save(resourceBO);
          }
        }
        // Questionarries
        query =
            session.createQuery(
                " From QuestionnaireBo QBO WHERE QBO.live=1 and QBO.active=1 and QBO.customStudyId= :customStudyId order by QBO.createdDate");
        query.setParameter("customStudyId", customStudyId);
        questionnaires = query.list();
        if (questionnaires != null && !questionnaires.isEmpty()) {
          for (QuestionnaireBo questionnaireBo : questionnaires) {
            boolean draftFlag = false;
            QuestionnaireBo newQuestionnaireBo = SerializationUtils.clone(questionnaireBo);
            newQuestionnaireBo.setId(null);
            newQuestionnaireBo.setStudyId(studyDreaftBo.getId());
            newQuestionnaireBo.setVersion(questionnaireBo.getVersion());
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY))
              newQuestionnaireBo.setLive(5);
            else {
              newQuestionnaireBo.setCustomStudyId(null);
              newQuestionnaireBo.setLive(0);
              newQuestionnaireBo.setCreatedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
              newQuestionnaireBo.setCreatedBy(sesObj.getUserId());
              newQuestionnaireBo.setModifiedBy(null);
              newQuestionnaireBo.setModifiedDate(null);
              // newQuestionnaireBo.setShortTitle(null);
              newQuestionnaireBo.setStatus(false);
            }
            newQuestionnaireBo.setVersion(0f);
            session.save(newQuestionnaireBo);
            /** Schedule Purpose creating draft Start * */
            if (StringUtils.isNotEmpty(questionnaireBo.getFrequency())) {
              if (questionnaireBo
                  .getFrequency()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
                searchQuery =
                    "From QuestionnaireCustomScheduleBo QCSBO where QCSBO.questionnairesId=:id";
                List<QuestionnaireCustomScheduleBo> questionnaireCustomScheduleList =
                    session
                        .createQuery(searchQuery)
                        .setInteger("id", questionnaireBo.getId())
                        .list();
                if (questionnaireCustomScheduleList != null
                    && !questionnaireCustomScheduleList.isEmpty()) {
                  for (QuestionnaireCustomScheduleBo customScheduleBo :
                      questionnaireCustomScheduleList) {
                    QuestionnaireCustomScheduleBo newCustomScheduleBo =
                        SerializationUtils.clone(customScheduleBo);
                    newCustomScheduleBo.setQuestionnairesId(newQuestionnaireBo.getId());
                    newCustomScheduleBo.setId(null);
                    newCustomScheduleBo.setUsed(false);
                    session.save(newCustomScheduleBo);
                  }
                }
              } else {
                searchQuery =
                    "From QuestionnairesFrequenciesBo QFBO where QFBO.questionnairesId=:id";
                List<QuestionnairesFrequenciesBo> questionnairesFrequenciesList =
                    session
                        .createQuery(searchQuery)
                        .setInteger("id", questionnaireBo.getId())
                        .list();
                if (questionnairesFrequenciesList != null
                    && !questionnairesFrequenciesList.isEmpty()) {
                  for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
                      questionnairesFrequenciesList) {
                    QuestionnairesFrequenciesBo newQuestionnairesFrequenciesBo =
                        SerializationUtils.clone(questionnairesFrequenciesBo);
                    newQuestionnairesFrequenciesBo.setQuestionnairesId(newQuestionnaireBo.getId());
                    newQuestionnairesFrequenciesBo.setId(null);
                    session.save(newQuestionnairesFrequenciesBo);
                  }
                }
              }
            }
            /** Schedule Purpose creating draft End * */

            /** Content purpose creating draft Start * */
            List<Integer> destinationList = new ArrayList<>();
            Map<Integer, Integer> destionationMapList = new HashMap<>();

            List<QuestionnairesStepsBo> existedQuestionnairesStepsBoList = null;
            List<QuestionnairesStepsBo> newQuestionnairesStepsBoList = new ArrayList<>();
            List<QuestionResponseSubTypeBo> existingQuestionResponseSubTypeList = new ArrayList<>();
            List<QuestionResponseSubTypeBo> newQuestionResponseSubTypeList = new ArrayList<>();
            query =
                session
                    .getNamedQuery("getQuestionnaireStepList")
                    .setInteger("questionnaireId", questionnaireBo.getId());
            existedQuestionnairesStepsBoList = query.list();
            if (existedQuestionnairesStepsBoList != null
                && !existedQuestionnairesStepsBoList.isEmpty()) {
              for (QuestionnairesStepsBo questionnairesStepsBo : existedQuestionnairesStepsBoList) {
                Integer destionStep = questionnairesStepsBo.getDestinationStep();
                if (destionStep.equals(0)) {
                  destinationList.add(-1);
                } else {
                  for (int i = 0; i < existedQuestionnairesStepsBoList.size(); i++) {
                    if (existedQuestionnairesStepsBoList.get(i).getStepId() != null
                        && destionStep.equals(
                            existedQuestionnairesStepsBoList.get(i).getStepId())) {
                      destinationList.add(i);
                      break;
                    }
                  }
                }
                destionationMapList.put(
                    questionnairesStepsBo.getSequenceNo(), questionnairesStepsBo.getStepId());
              }
              for (QuestionnairesStepsBo questionnairesStepsBo : existedQuestionnairesStepsBoList) {
                if (StringUtils.isNotEmpty(questionnairesStepsBo.getStepType())) {
                  QuestionnairesStepsBo newQuestionnairesStepsBo =
                      SerializationUtils.clone(questionnairesStepsBo);
                  newQuestionnairesStepsBo.setQuestionnairesId(newQuestionnaireBo.getId());
                  newQuestionnairesStepsBo.setStepId(null);
                  if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                    newQuestionnairesStepsBo.setCreatedOn(
                        FdahpStudyDesignerUtil.getCurrentDateTime());
                    newQuestionnairesStepsBo.setCreatedBy(sesObj.getUserId());
                    newQuestionnairesStepsBo.setModifiedBy(null);
                    newQuestionnairesStepsBo.setModifiedOn(null);
                    newQuestionnairesStepsBo.setStatus(false);
                    // newQuestionnairesStepsBo.setStepShortTitle(null);
                  }
                  session.save(newQuestionnairesStepsBo);
                  if (questionnairesStepsBo
                      .getStepType()
                      .equalsIgnoreCase(FdahpStudyDesignerConstants.INSTRUCTION_STEP)) {
                    InstructionsBo instructionsBo =
                        (InstructionsBo)
                            session
                                .getNamedQuery("getInstructionStep")
                                .setInteger("id", questionnairesStepsBo.getInstructionFormId())
                                .uniqueResult();
                    if (instructionsBo != null) {
                      InstructionsBo newInstructionsBo = SerializationUtils.clone(instructionsBo);
                      newInstructionsBo.setId(null);
                      if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                        newInstructionsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                        newInstructionsBo.setCreatedBy(sesObj.getUserId());
                        newInstructionsBo.setModifiedBy(null);
                        newInstructionsBo.setModifiedOn(null);
                        newInstructionsBo.setStatus(false);
                      }
                      session.save(newInstructionsBo);

                      // updating new InstructionId
                      newQuestionnairesStepsBo.setInstructionFormId(newInstructionsBo.getId());
                    }
                  } else if (questionnairesStepsBo
                      .getStepType()
                      .equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
                    QuestionsBo questionsBo =
                        (QuestionsBo)
                            session
                                .getNamedQuery("getQuestionStep")
                                .setInteger("stepId", questionnairesStepsBo.getInstructionFormId())
                                .uniqueResult();
                    if (questionsBo != null) {
                      boolean questionDraftFlag = false;
                      // Question response subType
                      List<QuestionResponseSubTypeBo> questionResponseSubTypeList =
                          session
                              .getNamedQuery("getQuestionSubResponse")
                              .setInteger("responseTypeId", questionsBo.getId())
                              .list();

                      List<QuestionConditionBranchBo> questionConditionBranchList =
                          session
                              .getNamedQuery("getQuestionConditionBranchList")
                              .setInteger("questionId", questionsBo.getId())
                              .list();

                      // Question response Type
                      questionReponseTypeBo =
                          (QuestionReponseTypeBo)
                              session
                                  .getNamedQuery("getQuestionResponse")
                                  .setInteger("questionsResponseTypeId", questionsBo.getId())
                                  .uniqueResult();

                      QuestionsBo newQuestionsBo = SerializationUtils.clone(questionsBo);
                      newQuestionsBo.setId(null);
                      if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                        newQuestionsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                        newQuestionsBo.setCreatedBy(sesObj.getUserId());
                        newQuestionsBo.setModifiedBy(null);
                        newQuestionsBo.setModifiedOn(null);
                        // newQuestionsBo.setShortTitle(null);
                        // newQuestionsBo.setStatShortName(null);
                        newQuestionsBo.setStatus(false);
                      }
                      session.save(newQuestionsBo);

                      // Question response Type
                      if (questionReponseTypeBo != null) {
                        QuestionReponseTypeBo newQuestionReponseTypeBo =
                            SerializationUtils.clone(questionReponseTypeBo);
                        newQuestionReponseTypeBo.setResponseTypeId(null);
                        newQuestionReponseTypeBo.setQuestionsResponseTypeId(newQuestionsBo.getId());
                        session.save(newQuestionReponseTypeBo);
                      }

                      // Question Condition branching
                      // logic
                      if (questionConditionBranchList != null
                          && !questionConditionBranchList.isEmpty()) {
                        for (QuestionConditionBranchBo questionConditionBranchBo :
                            questionConditionBranchList) {
                          QuestionConditionBranchBo newQuestionConditionBranchBo =
                              SerializationUtils.clone(questionConditionBranchBo);
                          newQuestionConditionBranchBo.setConditionId(null);
                          newQuestionConditionBranchBo.setQuestionId(newQuestionsBo.getId());
                          session.save(newQuestionConditionBranchBo);
                        }
                      }

                      // Question response subType
                      if (questionResponseSubTypeList != null
                          && !questionResponseSubTypeList.isEmpty()) {
                        existingQuestionResponseSubTypeList.addAll(questionResponseSubTypeList);

                        for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                            questionResponseSubTypeList) {
                          if (StringUtils.isNotEmpty(questionResponseSubTypeBo.getImage())
                              && StringUtils.isNotEmpty(
                                  questionResponseSubTypeBo.getSelectedImage())) {
                            draftFlag = true;
                            questionDraftFlag = true;
                          }
                          QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                              SerializationUtils.clone(questionResponseSubTypeBo);
                          newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                          newQuestionResponseSubTypeBo.setResponseTypeId(newQuestionsBo.getId());
                          if (!action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                            newQuestionResponseSubTypeBo.setImage(null);
                            newQuestionResponseSubTypeBo.setSelectedImage(null);
                          }
                          newQuestionResponseSubTypeBo.setDestinationStepId(null);
                          session.save(newQuestionResponseSubTypeBo);
                          newQuestionResponseSubTypeList.add(newQuestionResponseSubTypeBo);
                        }
                      }
                      if (questionDraftFlag) {
                        newQuestionnairesStepsBo.setStatus(false);
                        newQuestionsBo.setStatus(false);
                        session.update(newQuestionsBo);
                      }
                      // updating new InstructionId
                      newQuestionnairesStepsBo.setInstructionFormId(newQuestionsBo.getId());
                    }
                  } else if (questionnairesStepsBo
                      .getStepType()
                      .equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
                    FormBo formBo =
                        (FormBo)
                            session
                                .getNamedQuery("getFormBoStep")
                                .setInteger("stepId", questionnairesStepsBo.getInstructionFormId())
                                .uniqueResult();
                    if (formBo != null) {
                      boolean formDraftFlag = false;
                      FormBo newFormBo = SerializationUtils.clone(formBo);
                      newFormBo.setFormId(null);
                      session.save(newFormBo);

                      List<FormMappingBo> formMappingBoList =
                          session
                              .getNamedQuery("getFormByFormId")
                              .setInteger("formId", formBo.getFormId())
                              .list();
                      if (formMappingBoList != null && !formMappingBoList.isEmpty()) {
                        for (FormMappingBo formMappingBo : formMappingBoList) {
                          FormMappingBo newMappingBo = SerializationUtils.clone(formMappingBo);
                          newMappingBo.setFormId(newFormBo.getFormId());
                          newMappingBo.setId(null);

                          QuestionsBo questionsBo =
                              (QuestionsBo)
                                  session
                                      .getNamedQuery("getQuestionByFormId")
                                      .setInteger("formId", formMappingBo.getQuestionId())
                                      .uniqueResult();
                          if (questionsBo != null) {
                            boolean questionFlag = false;
                            // Question response
                            // subType
                            List<QuestionResponseSubTypeBo> questionResponseSubTypeList =
                                session
                                    .getNamedQuery("getQuestionSubResponse")
                                    .setInteger("responseTypeId", questionsBo.getId())
                                    .list();

                            // Question response
                            // Type
                            questionReponseTypeBo =
                                (QuestionReponseTypeBo)
                                    session
                                        .getNamedQuery("getQuestionResponse")
                                        .setInteger("questionsResponseTypeId", questionsBo.getId())
                                        .uniqueResult();

                            QuestionsBo newQuestionsBo = SerializationUtils.clone(questionsBo);
                            newQuestionsBo.setId(null);
                            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.COPY_STUDY)) {
                              newQuestionsBo.setCreatedOn(
                                  FdahpStudyDesignerUtil.getCurrentDateTime());
                              newQuestionsBo.setCreatedBy(sesObj.getUserId());
                              newQuestionsBo.setModifiedBy(null);
                              newQuestionsBo.setModifiedOn(null);
                              // newQuestionsBo.setShortTitle(null);
                              // newQuestionsBo.setStatShortName(null);
                              newQuestionsBo.setStatus(false);
                            }
                            session.save(newQuestionsBo);

                            // Question response
                            // Type
                            if (questionReponseTypeBo != null) {
                              QuestionReponseTypeBo newQuestionReponseTypeBo =
                                  SerializationUtils.clone(questionReponseTypeBo);
                              newQuestionReponseTypeBo.setResponseTypeId(null);
                              newQuestionReponseTypeBo.setQuestionsResponseTypeId(
                                  newQuestionsBo.getId());
                              session.save(newQuestionReponseTypeBo);
                            }

                            // Question response
                            // subType
                            if (questionResponseSubTypeList != null
                                && !questionResponseSubTypeList.isEmpty()) {
                              // existingQuestionResponseSubTypeList.addAll(questionResponseSubTypeList);
                              for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                                  questionResponseSubTypeList) {
                                if (StringUtils.isNotEmpty(questionResponseSubTypeBo.getImage())
                                    && StringUtils.isNotEmpty(
                                        questionResponseSubTypeBo.getSelectedImage())) {
                                  draftFlag = true;
                                  formDraftFlag = true;
                                  questionFlag = true;
                                }
                                QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                                    SerializationUtils.clone(questionResponseSubTypeBo);
                                newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                                newQuestionResponseSubTypeBo.setResponseTypeId(
                                    newQuestionsBo.getId());
                                if (!action.equalsIgnoreCase(
                                    FdahpStudyDesignerConstants.COPY_STUDY)) {
                                  newQuestionResponseSubTypeBo.setImage(null);
                                  newQuestionResponseSubTypeBo.setSelectedImage(null);
                                }
                                session.save(newQuestionResponseSubTypeBo);
                                // newQuestionResponseSubTypeList.add(newQuestionResponseSubTypeBo);
                              }
                            }

                            // adding questionId
                            newMappingBo.setQuestionId(newQuestionsBo.getId());
                            session.save(newMappingBo);
                            if (questionFlag) {
                              newQuestionsBo.setStatus(false);
                              session.update(newQuestionsBo);
                            }
                          }
                        }
                      }
                      // updating new formId
                      if (formDraftFlag) {
                        newQuestionnairesStepsBo.setStatus(false);
                      }
                      newQuestionnairesStepsBo.setInstructionFormId(newFormBo.getFormId());
                    }
                  }
                  session.update(newQuestionnairesStepsBo);
                  newQuestionnairesStepsBoList.add(newQuestionnairesStepsBo);
                }
              }
            }
            if (destinationList != null && !destinationList.isEmpty()) {
              for (int i = 0; i < destinationList.size(); i++) {
                int desId = 0;
                if (destinationList.get(i) != -1) {
                  desId = newQuestionnairesStepsBoList.get(destinationList.get(i)).getStepId();
                }
                newQuestionnairesStepsBoList.get(i).setDestinationStep(desId);
                session.update(newQuestionnairesStepsBoList.get(i));
              }
            }
            List<Integer> sequenceSubTypeList = new ArrayList<>();
            List<Integer> destinationResList = new ArrayList<>();
            if (existingQuestionResponseSubTypeList != null
                && !existingQuestionResponseSubTypeList.isEmpty()) {
              for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                  existingQuestionResponseSubTypeList) {
                if (questionResponseSubTypeBo.getDestinationStepId() == null) {
                  sequenceSubTypeList.add(null);
                } else if (questionResponseSubTypeBo.getDestinationStepId() != null
                    && questionResponseSubTypeBo.getDestinationStepId().equals(0)) {
                  sequenceSubTypeList.add(-1);
                } else {
                  if (existedQuestionnairesStepsBoList != null
                      && !existedQuestionnairesStepsBoList.isEmpty()) {
                    for (QuestionnairesStepsBo questionnairesStepsBo :
                        existedQuestionnairesStepsBoList) {
                      if (questionResponseSubTypeBo.getDestinationStepId() != null
                          && questionResponseSubTypeBo
                              .getDestinationStepId()
                              .equals(questionnairesStepsBo.getStepId())) {
                        sequenceSubTypeList.add(questionnairesStepsBo.getSequenceNo());
                        break;
                      }
                    }
                  }
                }
              }
            }
            if (sequenceSubTypeList != null && !sequenceSubTypeList.isEmpty()) {
              for (int i = 0; i < sequenceSubTypeList.size(); i++) {
                Integer desId = null;
                if (sequenceSubTypeList.get(i) == null) {
                  desId = null;
                } else if (sequenceSubTypeList.get(i).equals(-1)) {
                  desId = 0;
                } else {
                  for (QuestionnairesStepsBo questionnairesStepsBo : newQuestionnairesStepsBoList) {
                    if (sequenceSubTypeList.get(i).equals(questionnairesStepsBo.getSequenceNo())) {
                      desId = questionnairesStepsBo.getStepId();
                      break;
                    }
                  }
                }
                destinationResList.add(desId);
              }
              for (int i = 0; i < destinationResList.size(); i++) {
                newQuestionResponseSubTypeList
                    .get(i)
                    .setDestinationStepId(destinationResList.get(i));
                session.update(newQuestionResponseSubTypeList.get(i));
              }
            }
            if (draftFlag) {
              newQuestionnaireBo.setStatus(false);
              session.update(newQuestionnaireBo);
            }
            /** Content purpose creating draft End * */
          }
        } // If Questionarries updated flag -1 then update End
        // ActiveTasks
        List<ActiveTaskBo> activeTasks = null;
        query =
            session.createQuery(
                "SELECT ATB FROM ActiveTaskBo ATB where ATB.active IS NOT NULL and ATB.active=1 and ATB.live =1 and customStudyId=:customStudyId order by id");
        query.setString("customStudyId", customStudyId);
        activeTasks = query.list();
        if (activeTasks != null && !activeTasks.isEmpty()) {
          for (ActiveTaskBo activeTaskBo : activeTasks) {
            ActiveTaskBo newActiveTaskBo = SerializationUtils.clone(activeTaskBo);
            newActiveTaskBo.setId(null);
            newActiveTaskBo.setStudyId(studyDreaftBo.getId());
            newActiveTaskBo.setVersion(activeTaskBo.getVersion());
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY))
              newActiveTaskBo.setLive(5);
            else {
              newActiveTaskBo.setCustomStudyId(null);
              newActiveTaskBo.setLive(0);
              newActiveTaskBo.setCreatedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
              newActiveTaskBo.setCreatedBy(sesObj.getUserId());
              newActiveTaskBo.setModifiedBy(null);
              newActiveTaskBo.setModifiedDate(null);
              // newActiveTaskBo.setShortTitle(null);
              newActiveTaskBo.setAction(false);
            }
            newActiveTaskBo.setVersion(0f);
            session.save(newActiveTaskBo);
            /** Schedule Purpose creating draft Start * */
            if (StringUtils.isNotEmpty(activeTaskBo.getFrequency())) {
              if (activeTaskBo
                  .getFrequency()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
                searchQuery = "From ActiveTaskCustomScheduleBo QCSBO where QCSBO.activeTaskId=:id";
                List<ActiveTaskCustomScheduleBo> activeTaskCustomScheduleList =
                    session.createQuery(searchQuery).setInteger("id", activeTaskBo.getId()).list();
                if (activeTaskCustomScheduleList != null
                    && !activeTaskCustomScheduleList.isEmpty()) {
                  for (ActiveTaskCustomScheduleBo customScheduleBo : activeTaskCustomScheduleList) {
                    ActiveTaskCustomScheduleBo newCustomScheduleBo =
                        SerializationUtils.clone(customScheduleBo);
                    newCustomScheduleBo.setActiveTaskId(newActiveTaskBo.getId());
                    newCustomScheduleBo.setUsed(false);
                    newCustomScheduleBo.setId(null);
                    session.save(newCustomScheduleBo);
                  }
                }
              } else {
                searchQuery = "From ActiveTaskFrequencyBo QFBO where QFBO.activeTaskId=:id";
                List<ActiveTaskFrequencyBo> activeTaskFrequenciesList =
                    session.createQuery(searchQuery).setInteger("id", activeTaskBo.getId()).list();
                if (activeTaskFrequenciesList != null && !activeTaskFrequenciesList.isEmpty()) {
                  for (ActiveTaskFrequencyBo activeTaskFrequenciesBo : activeTaskFrequenciesList) {
                    ActiveTaskFrequencyBo newFrequenciesBo =
                        SerializationUtils.clone(activeTaskFrequenciesBo);
                    newFrequenciesBo.setActiveTaskId(newActiveTaskBo.getId());
                    newFrequenciesBo.setId(null);
                    session.save(newFrequenciesBo);
                  }
                }
              }
            }
            /** Schedule Purpose creating draft End * */

            /** Content Purpose creating draft Start * */
            query =
                session
                    .getNamedQuery("getAttributeListByActiveTAskId")
                    .setInteger("activeTaskId", activeTaskBo.getId());
            List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBoList = query.list();
            if (activeTaskAtrributeValuesBoList != null
                && !activeTaskAtrributeValuesBoList.isEmpty()) {
              for (ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo :
                  activeTaskAtrributeValuesBoList) {
                ActiveTaskAtrributeValuesBo newActiveTaskAtrributeValuesBo =
                    SerializationUtils.clone(activeTaskAtrributeValuesBo);
                newActiveTaskAtrributeValuesBo.setActiveTaskId(newActiveTaskBo.getId());
                newActiveTaskAtrributeValuesBo.setAttributeValueId(null);
                /*
                 * if(action.equalsIgnoreCase( FdahpStudyDesignerConstants.COPY_STUDY)){
                 * newActiveTaskAtrributeValuesBo .setIdentifierNameStat(null); }
                 */
                session.save(newActiveTaskAtrributeValuesBo);
              }
            }
            /** Content Purpose creating draft End * */
          }
        } // Active TAsk End

        // Consent updated update Start
        query =
            session.createQuery(
                "From ConsentBo CBO WHERE CBO.live =1 and customStudyId=:customStudyId order by CBO.createdOn DESC");
        query.setString("customStudyId", customStudyId);
        ConsentBo consentBo = (ConsentBo) query.uniqueResult();
        if (consentBo != null) {
          ConsentBo newConsentBo = SerializationUtils.clone(consentBo);
          newConsentBo.setId(null);
          newConsentBo.setStudyId(studyDreaftBo.getId());
          newConsentBo.setVersion(0f);
          if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY))
            newConsentBo.setLive(5);
          else {
            newConsentBo.setCustomStudyId(null);
            newConsentBo.setLive(0);
            newConsentBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            newConsentBo.setCreatedBy(sesObj.getUserId());
            newConsentBo.setModifiedBy(null);
            newConsentBo.setModifiedOn(null);
          }
          newConsentBo.setVersion(0f);
          session.save(newConsentBo);

          // Comprehension test Start
          if (StringUtils.isNotEmpty(consentBo.getNeedComprehensionTest())
              && consentBo
                  .getNeedComprehensionTest()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
            List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = null;
            List<Integer> comprehensionIds = new ArrayList<>();
            List<ComprehensionTestResponseBo> comprehensionTestResponseList = null;
            query =
                session.createQuery(
                    "From ComprehensionTestQuestionBo CTQBO where CTQBO.studyId=:studyId and CTQBO.active=1 order by CTQBO.sequenceNo asc");
            query.setInteger("studyId", consentBo.getStudyId());
            comprehensionTestQuestionList = query.list();
            if (comprehensionTestQuestionList != null && !comprehensionTestQuestionList.isEmpty()) {
              for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
                  comprehensionTestQuestionList) {
                comprehensionIds.add(comprehensionTestQuestionBo.getId());
              }
              comprehensionTestResponseList =
                  session
                      .createQuery(
                          "From ComprehensionTestResponseBo CTRBO "
                              + "where CTRBO.comprehensionTestQuestionId IN (:idList) "
                              + "order by comprehensionTestQuestionId")
                      .setParameterList("idList", comprehensionIds)
                      .list();
              for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
                  comprehensionTestQuestionList) {
                ComprehensionTestQuestionBo newComprehensionTestQuestionBo =
                    SerializationUtils.clone(comprehensionTestQuestionBo);
                newComprehensionTestQuestionBo.setStudyId(studyDreaftBo.getId());
                newComprehensionTestQuestionBo.setId(null);
                newComprehensionTestQuestionBo.setCreatedOn(
                    FdahpStudyDesignerUtil.getCurrentDateTime());
                newComprehensionTestQuestionBo.setCreatedBy(sesObj.getUserId());
                newComprehensionTestQuestionBo.setModifiedBy(null);
                newComprehensionTestQuestionBo.setModifiedOn(null);
                session.save(newComprehensionTestQuestionBo);
                if (comprehensionTestResponseList != null
                    && !comprehensionTestResponseList.isEmpty()) {
                  for (ComprehensionTestResponseBo comprehensionTestResponseBo :
                      comprehensionTestResponseList) {
                    if (comprehensionTestQuestionBo.getId().intValue()
                        == comprehensionTestResponseBo
                            .getComprehensionTestQuestionId()
                            .intValue()) {
                      ComprehensionTestResponseBo newComprehensionTestResponseBo =
                          SerializationUtils.clone(comprehensionTestResponseBo);
                      newComprehensionTestResponseBo.setId(null);
                      newComprehensionTestResponseBo.setComprehensionTestQuestionId(
                          newComprehensionTestQuestionBo.getId());
                      session.save(newComprehensionTestResponseBo);
                    }
                  }
                }
              }
            }
          }
          // Comprehension test End
        }

        query =
            session.createQuery(
                " From ConsentInfoBo CBO WHERE CBO.live =1 and CBO.active=1 and customStudyId=:customStudyId order by CBO.sequenceNo DESC");
        query.setString("customStudyId", customStudyId);
        List<ConsentInfoBo> consentInfoBoList = query.list();
        if (consentInfoBoList != null && !consentInfoBoList.isEmpty()) {
          for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
            ConsentInfoBo newConsentInfoBo = SerializationUtils.clone(consentInfoBo);
            newConsentInfoBo.setId(null);
            newConsentInfoBo.setStudyId(studyDreaftBo.getId());
            newConsentInfoBo.setVersion(0f);
            // newConsentInfoBo.setLive(5);
            if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY))
              newConsentInfoBo.setLive(5);
            else {
              newConsentInfoBo.setLive(0);
              newConsentInfoBo.setCustomStudyId(null);
              newConsentInfoBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              newConsentInfoBo.setCreatedBy(sesObj.getUserId());
              newConsentInfoBo.setModifiedBy(null);
              newConsentInfoBo.setModifiedOn(null);
              newConsentInfoBo.setStatus(false);
            }
            newConsentInfoBo.setVersion(0f);
            session.save(newConsentInfoBo);
          }
        }
        // Consent updated update End
        // checklist save
        Checklist checklist = new Checklist();
        checklist.setStudyId(studyDreaftBo.getId());
        checklist.setCustomStudyId(
            action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY)
                ? customStudyId
                : null);
        session.save(checklist);
        flag = true;
        if (flag && action.equalsIgnoreCase(FdahpStudyDesignerConstants.RESET_STUDY)) {
          draftDatas =
              session
                  .getNamedQuery("getStudyDraftVersion")
                  .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId)
                  .list();
          if (draftDatas != null && !draftDatas.isEmpty()) {
            studyIdList = new ArrayList<>();
            for (StudyBo study : draftDatas) {
              studyIdList.add(study.getId());
            }
            deleteStudyByIdOrCustomstudyId(
                session, transaction, StringUtils.join(studyIdList, ","), "");
          }
        }
        // }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - resetDraftStudyByCustomStudyId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - resetDraftStudyByCustomStudyId() - Ends");
    return flag;
  }

  /**
   * This method is used to get the sequence number to set to the resource
   *
   * @author BTC
   * @param studyId
   * @return sequence number
   */
  @Override
  public int resourceOrder(Integer studyId) {
    logger.info("StudyDAOImpl - resourceOrder() - Starts");
    Session session = null;
    int count = 1;
    ResourceBO resourceBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      resourceBo =
          (ResourceBO)
              session
                  .createQuery(
                      "From ResourceBO RBO where RBO.studyId= :studyId and RBO.studyProtocol = false and RBO.status=1 order by RBO.sequenceNo DESC")
                  .setInteger("studyId", studyId)
                  .setMaxResults(1)
                  .uniqueResult();
      if (resourceBo != null) {
        count = resourceBo.getSequenceNo() + 1;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - resourceOrder() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - resourceOrder() - Ends");
    return count;
  }

  /**
   * This method is used to get the saved resource list
   *
   * @author BTC
   * @param studyId
   * @return List of {@link ResourceBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ResourceBO> resourcesSaved(Integer studyId) {
    logger.info("StudyDAOImpl - resourcesSaved() - Starts");
    List<ResourceBO> resourceBOList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      resourceBOList =
          session
              .createQuery(
                  "FROM ResourceBO RBO WHERE RBO.studyId= :studyId AND RBO.action = 0 AND RBO.status = 1 AND RBO.studyProtocol = 0")
              .setInteger("studyId", studyId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - resourcesSaved() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - resourcesSaved() - Ends");
    return resourceBOList;
  }

  /**
   * This method is used to get the list of resources having anchor date
   *
   * @author BTC
   * @param studyId
   * @return List of {@link ResourceBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<ResourceBO> resourcesWithAnchorDate(Integer studyId) {
    logger.info("StudyDAOImpl - resourcesWithAnchorDate() - Starts");
    List<ResourceBO> resourceList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      resourceList =
          session
              .createQuery(
                  "FROM ResourceBO RBO WHERE RBO.studyId= :studyId AND RBO.resourceType = 1 AND RBO.status = 1")
              .setInteger("studyId", studyId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - resourcesWithAnchorDate() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - resourcesWithAnchorDate() - Ends");
    return resourceList;
  }

  /**
   * Study consent section have the consent review sub section which contain the share data
   * permission allows the admin to specify if as part of the Consent process, participants need to
   * be asked to provide permission for their response data to be shared with 3rd parties and
   * consent document review section is section is meant for the admin to confirm the content seen
   * by users in the Review Terms (Consent Document) screen on the mobile app In e-consent section
   * The admin sees the elements of the e-consent form as provided to the user in the mobile app
   *
   * @author BTC
   * @param Object , {@link ConsentBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return {@link ConsentBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ConsentBo saveOrCompleteConsentReviewDetails(
      ConsentBo consentBo, SessionObject sesObj, String customStudyId) {
    logger.info("INFO: StudyDAOImpl - saveOrCompleteConsentReviewDetails() :: Starts");
    Session session = null;
    StudySequenceBo studySequence = null;
    List<ConsentInfoBo> consentInfoList = null;
    String content = "";
    String activitydetails = "";
    String activity = "";
    StudyVersionBo studyVersionBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      // check whether the consentinfo is saved for this study or not, if
      // not update
      if (consentBo.getId() != null) {
        consentBo.setModifiedOn(
            FdahpStudyDesignerUtil.getFormattedDate(
                FdahpStudyDesignerUtil.getCurrentDateTime(),
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss"));
        consentBo.setModifiedBy(sesObj.getUserId());
      } else {
        consentBo.setCreatedOn(
            FdahpStudyDesignerUtil.getFormattedDate(
                FdahpStudyDesignerUtil.getCurrentDateTime(),
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm:ss"));
        consentBo.setCreatedBy(sesObj.getUserId());
      }

      // get the review content based on the version, studyId and visual
      // step
      if (consentBo != null
          && StringUtils.isNotEmpty(consentBo.getConsentDocType())
          && consentBo.getConsentDocType().equalsIgnoreCase("Auto")) {
        consentInfoList =
            session
                .createQuery(
                    "from ConsentInfoBo CIBO where CIBO.studyId= :studyId and CIBO.active=1")
                .setInteger("studyId", consentBo.getStudyId())
                .list();
        if (consentInfoList != null && consentInfoList.size() > 0) {
          for (ConsentInfoBo consentInfo : consentInfoList) {
            content +=
                "<span style=&#34;font-size:20px;&#34;><strong>"
                    + consentInfo.getDisplayTitle()
                    + "</strong></span><br/>"
                    + "<span style=&#34;display: block; overflow-wrap: break-word; width: 100%;&#34;>"
                    + consentInfo.getElaborated()
                    + "</span><br/>";
          }
          consentBo.setConsentDocContent(content);
        }
      }

      if (consentBo.getType() != null
          && consentBo.getType().equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
        studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, consentBo.getStudyId())
                    .uniqueResult();
        if (studySequence != null) {
          studySequence.seteConsent(false);
        } else {
          studySequence = new StudySequenceBo();
          studySequence.seteConsent(false);
          studySequence.setStudyId(consentBo.getStudyId());
        }
        session.saveOrUpdate(studySequence);
      }
      session.saveOrUpdate(consentBo);
      if (customStudyId != null && !customStudyId.isEmpty()) {
        if (consentBo.getType() != null) {
          if (consentBo.getType().equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
            activity = "Study consentReview saved.";
            activitydetails =
                "Content saved for Consent Review Section. (Study ID = " + customStudyId + ")";
          } else {
            query =
                session
                    .getNamedQuery("getStudyByCustomStudyId")
                    .setMaxResults(1)
                    .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId);
            query.setMaxResults(1);
            studyVersionBo = (StudyVersionBo) query.uniqueResult();
            if (studyVersionBo != null) {
              activity = "Study consentReview marked as completed.";
              activitydetails =
                  "Consent Review section successfully checked for minimum content completeness and marked 'Completed'.  (Study ID = "
                      + customStudyId
                      + ", Consent Document Version = "
                      + studyVersionBo.getConsentVersion()
                      + ")";
            }
          }
        } else if (consentBo.getComprehensionTest() != null) {
          if (consentBo
              .getComprehensionTest()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
            activity = "Study comprehension test saved.";
            activitydetails =
                "Content saved for Comprehension test Section. (Study ID = " + customStudyId + ")";
          } else {
            query =
                session
                    .getNamedQuery("getStudyByCustomStudyId")
                    .setMaxResults(1)
                    .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId);
            query.setMaxResults(1);
            studyVersionBo = (StudyVersionBo) query.uniqueResult();
            if (studyVersionBo != null) {
              activity = "Study comprehension test marked as completed.";
              activitydetails =
                  "Comprehension Test section successfully checked for minimum content completeness and marked 'Completed'.  (Study ID = "
                      + customStudyId
                      + ")";
            }
          }
        }
      }
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sesObj,
          activity,
          activitydetails,
          "StudyDAOImpl - saveOrCompleteConsentReviewDetails");
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrCompleteConsentReviewDetails() :: ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("INFO: StudyDAOImpl - saveOrCompleteConsentReviewDetails() :: Ends");
    return consentBo;
  }

  /**
   * This method is used to Save or Done Checklist
   *
   * @author BTC
   * @param checklist , {@link Checklist}
   * @return checklist Id
   */
  @Override
  public Integer saveOrDoneChecklist(Checklist checklist) {
    logger.info("StudyDAOImpl - saveOrDoneChecklist() - Starts");
    Session session = null;
    Integer checklistId = 0;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (checklist.getChecklistId() == null) {
        checklistId = (Integer) session.save(checklist);
      } else {
        session.update(checklist);
        checklistId = checklist.getChecklistId();
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrDoneChecklist() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrDoneChecklist() - Ends");
    return checklistId;
  }

  /**
   * Study consent can have 0 or more comprehension test questions.Admin can mark whether or not
   * this is required for the study if required admin will need to add question text,answer options
   * and correct option and minimum score
   *
   * @author BTC
   * @param Object , {@link ComprehensionTestQuestionBo}
   * @return Object , {@link ComprehensionTestQuestionBo}
   */
  @Override
  public ComprehensionTestQuestionBo saveOrUpdateComprehensionTestQuestion(
      ComprehensionTestQuestionBo comprehensionTestQuestionBo) {
    logger.info("StudyDAOImpl - saveOrUpdateComprehensionTestQuestion() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(comprehensionTestQuestionBo);
      if (comprehensionTestQuestionBo.getId() != null
          && comprehensionTestQuestionBo.getResponseList() != null
          && !comprehensionTestQuestionBo.getResponseList().isEmpty()) {
        session
            .createSQLQuery(
                "delete from comprehension_test_response where comprehension_test_question_id= :comprehensionTestQuestionId")
            .setInteger("comprehensionTestQuestionId", comprehensionTestQuestionBo.getId())
            .executeUpdate();
        for (ComprehensionTestResponseBo comprehensionTestResponseBo :
            comprehensionTestQuestionBo.getResponseList()) {
          if (comprehensionTestResponseBo.getResponseOption() != null
              && !comprehensionTestResponseBo.getResponseOption().isEmpty()) {
            if (comprehensionTestResponseBo.getComprehensionTestQuestionId() == null) {
              comprehensionTestResponseBo.setComprehensionTestQuestionId(
                  comprehensionTestQuestionBo.getId());
            }
            session.save(comprehensionTestResponseBo);
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateComprehensionTestQuestion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateComprehensionTestQuestion() - Ends");
    return comprehensionTestQuestionBo;
  }

  /**
   * Study consent section is mandatory in mobile section admin can add one or more consent items
   * here.The consent item are two types which are research kit provided consents and custom defined
   * consent items admin can create the custom consent items which are not available in research kit
   *
   * @author BTC
   * @param Object : {@link ConsentInfoBo}
   * @param Object : {@link SessionObject}
   * @param String : customStudyId in {@link StudyBo}
   * @return {@link ConsentInfoBo}
   */
  @Override
  public ConsentInfoBo saveOrUpdateConsentInfo(
      ConsentInfoBo consentInfoBo, SessionObject sesObj, String customStudyId) {
    logger.info("StudyDAOImpl - saveOrUpdateConsentInfo() - Starts");
    Session session = null;
    StudySequenceBo studySequence = null;
    String activitydetails = "";
    String activity = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (consentInfoBo.getType() != null) {
        studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, consentInfoBo.getStudyId())
                    .uniqueResult();
        if (consentInfoBo
            .getType()
            .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
          consentInfoBo.setStatus(false);
          if (studySequence != null) {
            studySequence.setConsentEduInfo(false);
            if (studySequence.iseConsent()) {
              studySequence.seteConsent(false);
            }
            if (studySequence.isComprehensionTest()) {
              studySequence.setComprehensionTest(false);
            }
          } else {
            studySequence = new StudySequenceBo();
            studySequence.setConsentEduInfo(false);
            studySequence.setStudyId(consentInfoBo.getStudyId());
          }
        } else if (consentInfoBo
            .getType()
            .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
          consentInfoBo.setStatus(true);
          if (studySequence.iseConsent()) {
            studySequence.seteConsent(false);
          }
          if (studySequence.isComprehensionTest()) {
            studySequence.setComprehensionTest(false);
          }
        }
        session.saveOrUpdate(studySequence);
      }
      session.saveOrUpdate(consentInfoBo);
      if (consentInfoBo
          .getType()
          .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
        activity = "Consent section successfully checked for minimum content completeness.";
        // activitydetails =
        // "Consent section updated successfully (Study ID = "+customStudyId+").";
        activitydetails =
            "Consent section successfully checked for minimum content completeness and marked 'Done'. (Display Name = "
                + consentInfoBo.getDisplayTitle()
                + ", Study ID = "
                + customStudyId
                + ")";
      } else {
        activity = "Consent section saved as draft.";
        activitydetails =
            "Content saved for Consent Section. (Display Name = "
                + consentInfoBo.getDisplayTitle()
                + ", Study ID = "
                + customStudyId
                + ")";
      }
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sesObj,
          activity,
          activitydetails,
          "StudyDAOImpl - saveOrUpdateConsentInfo");
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateConsentInfo() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateConsentInfo() - Ends");
    return consentInfoBo;
  }

  /**
   * Save or update eligibility test question and answer
   *
   * @param eligibilityTestBo , object of {@link EligibilityTestBo}
   * @param studyId , id of the {@link StudyBo}
   * @param sesObj , current {@link SessionObject} object
   * @param customStudyId , custom study id of {@link StudyBo}
   * @return eligibilityTestId, Id of {@link EligibilityTestBo}
   */
  @Override
  public Integer saveOrUpdateEligibilityTestQusAns(
      EligibilityTestBo eligibilityTestBo,
      Integer studyId,
      SessionObject sesObj,
      String customStudyId) {
    logger.info("StudyDAOImpl - saveOrUpdateEligibilityTestQusAns - Starts");
    Session session = null;
    Integer eligibilityTestId = 0;
    Transaction trans = null;
    String activity;
    String activitydetails;
    EligibilityTestBo saveEligibilityTestBo;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      trans = session.beginTransaction();
      StudySequenceBo studySequence =
          (StudySequenceBo)
              session
                  .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                  .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyId)
                  .uniqueResult();
      if (studySequence != null && studySequence.isEligibility()) {
        studySequence.setEligibility(false);
        session.update(studySequence);
      }
      if (eligibilityTestBo
          .getType()
          .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
        activity = "EligibilityQus section done";
        activitydetails =
            customStudyId
                + " -- EligibilityQus section done and eligible for mark as completed action";
        eligibilityTestBo.setStatus(true);
      } else {
        activity = "EligibilityQus section saved";
        activitydetails =
            customStudyId
                + " -- EligibilityQus section saved but not eligible for mark as completed action untill unless it is DONE";
      }
      if (null != eligibilityTestBo.getId()) {
        saveEligibilityTestBo =
            (EligibilityTestBo) session.get(EligibilityTestBo.class, eligibilityTestBo.getId());
        session.evict(saveEligibilityTestBo);
        eligibilityTestBo.setUsed(saveEligibilityTestBo.isUsed());
      }
      session.saveOrUpdate(eligibilityTestBo);
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sesObj,
          activity,
          activitydetails,
          "StudyDAOImpl - saveOrUpdateEligibilityTestQusAns");
      eligibilityTestId = eligibilityTestBo.getId();
      trans.commit();
    } catch (Exception e) {
      if (null != trans) trans.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateEligibilityTestQusAns - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateEligibilityTestQusAns - Ends");
    return eligibilityTestId;
  }

  /**
   * save or update content(title,description,image) for the Overview pages of the Study those pages
   * will reflect on mobile overview screen
   *
   * @author BTC
   * @param studyPageBean , {@link StudyPageBean}
   * @return {@link String}
   */
  @Override
  public String saveOrUpdateOverviewStudyPages(StudyPageBean studyPageBean, SessionObject sesObj) {
    logger.info("StudyDAOImpl - saveOrUpdateOverviewStudyPages() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int titleLength = 0;
    StudySequenceBo studySequence = null;
    StudyBo studyBo = null;
    String activitydetails = "";
    String activity = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(studyPageBean.getStudyId())) {
        studyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", Integer.parseInt(studyPageBean.getStudyId()))
                    .uniqueResult();
        if (studyBo != null) {
          studyBo.setMediaLink(studyPageBean.getMediaLink());
          session.update(studyBo);
        }
        // fileArray based on pageId will save/update into particular
        // location
        titleLength = studyPageBean.getTitle().length;
        if (titleLength > 0) {
          // delete the pages which are deleted from front end
          List<Integer> pageIdList = new ArrayList<>();
          for (int j = 0; j < studyPageBean.getPageId().length; j++) {
            if (FdahpStudyDesignerUtil.isNotEmpty(studyPageBean.getPageId()[j])) {
              pageIdList.add(Integer.valueOf(studyPageBean.getPageId()[j]));
            }
          }
          if (!pageIdList.isEmpty()) {
            session
                .createQuery(
                    "delete from StudyPageBo where studyId= :studyId and pageId not in(:pageIdList)")
                .setString("studyId", studyPageBean.getStudyId())
                .setParameterList("pageIdList", pageIdList)
                .executeUpdate();
            session
                .createQuery(
                    "delete from StudyPageLanguageBO spl where spl.studyId= :studyId and spl.studyPageLanguagePK.pageId not in(:pageIdList)")
                .setString("studyId", studyPageBean.getStudyId())
                .setParameterList("pageIdList", pageIdList)
                .executeUpdate();
          } else {
            session
                .createQuery("delete from StudyPageBo where studyId= :studyId")
                .setString("studyId", studyPageBean.getStudyId())
                .executeUpdate();
            session
                .createQuery("delete from StudyPageLanguageBO where studyId= :studyId")
                .setString("studyId", studyPageBean.getStudyId())
                .executeUpdate();
          }
          for (int i = 0; i < titleLength; i++) {
            StudyPageBo studyPageBo = null;
            if (FdahpStudyDesignerUtil.isNotEmpty(studyPageBean.getPageId()[i]))
              studyPageBo =
                  (StudyPageBo)
                      session
                          .createQuery("from StudyPageBo SPB where SPB.pageId= :pageId")
                          .setString("pageId", studyPageBean.getPageId()[i])
                          .uniqueResult();

            if (studyPageBo == null) studyPageBo = new StudyPageBo();

            if (FdahpStudyDesignerUtil.isNotEmpty(studyPageBean.getPageId()[i])) {
              studyPageBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              studyPageBo.setModifiedBy(studyPageBean.getUserId());
            } else {
              studyPageBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              studyPageBo.setCreatedBy(studyPageBean.getUserId());
            }
            studyPageBo.setStudyId(
                FdahpStudyDesignerUtil.isEmpty(studyPageBean.getStudyId())
                    ? 0
                    : Integer.parseInt(studyPageBean.getStudyId()));
            studyPageBo.setTitle(
                FdahpStudyDesignerUtil.isEmpty(studyPageBean.getTitle()[i])
                    ? null
                    : studyPageBean.getTitle()[i]);
            studyPageBo.setDescription(
                FdahpStudyDesignerUtil.isEmpty(studyPageBean.getDescription()[i])
                    ? null
                    : studyPageBean.getDescription()[i]);
            studyPageBo.setImagePath(
                FdahpStudyDesignerUtil.isEmpty(studyPageBean.getImagePath()[i])
                    ? null
                    : studyPageBean.getImagePath()[i]);
            session.saveOrUpdate(studyPageBo);
          }
          studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(
                          FdahpStudyDesignerConstants.STUDY_ID,
                          Integer.parseInt(studyPageBean.getStudyId()))
                      .uniqueResult();
          if (studySequence != null) {
            if (studyPageBean.getActionType() != null
                && studyPageBean
                    .getActionType()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)
                && !studySequence.isOverView()) {
              studySequence.setOverView(true);
            } else if (studyPageBean.getActionType() != null
                && studyPageBean
                    .getActionType()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.SAVE_BUTTON)) {
              studySequence.setOverView(false);
            }
            session.update(studySequence);
          }
          message =
              auditLogDAO.updateDraftToEditedStatus(
                  session,
                  transaction,
                  studyPageBean.getUserId(),
                  FdahpStudyDesignerConstants.DRAFT_STUDY,
                  Integer.parseInt(studyPageBean.getStudyId()));
          if (studyPageBean
              .getActionType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
            activity = "Study overview marked as Completed.";
            activitydetails =
                "Section validated for minimum completion required and marked as Completed. (Study ID = "
                    + studyBo.getCustomStudyId()
                    + ")";
          } else {
            activity = "Study overview content saved as draft.";
            activitydetails =
                "Study overview content saved as draft. (Study ID = "
                    + studyBo.getCustomStudyId()
                    + ")";
          }
          auditLogDAO.saveToAuditLog(
              session,
              transaction,
              sesObj,
              activity,
              activitydetails,
              "StudyDAOImpl - saveOrUpdateOverviewStudyPages");
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateOverviewStudyPages() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateOverviewStudyPages() - Ends");
    return message;
  }

  /**
   * This method is used to save or update the Study Resource
   *
   * @author BTC
   * @param resourceBO , {@link ResourceBO}
   * @return resource Id
   */
  @Override
  public Integer saveOrUpdateResource(ResourceBO resourceBO) {
    logger.info("StudyDAOImpl - saveOrUpdateResource() - Starts");
    Session session = null;
    Integer resourceId = 0;
    AnchorDateTypeBo anchorDateTypeBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (null != resourceBO && null != resourceBO.getAnchorDateId()) {
        anchorDateTypeBo =
            (AnchorDateTypeBo) session.get(AnchorDateTypeBo.class, resourceBO.getAnchorDateId());
      }
      if (null != anchorDateTypeBo
          && null != anchorDateTypeBo.getParticipantProperty()
          && anchorDateTypeBo.getParticipantProperty()) {
        query =
            session.createQuery(
                "select count(*) from ResourceBO RBO  where RBO.studyId=:studyId and RBO.anchorDateId=:anchorDateId and RBO.status=1");
        query.setInteger("studyId", resourceBO.getStudyId());
        query.setInteger("anchorDateId", resourceBO.getAnchorDateId());
        Long count = (Long) query.uniqueResult();
        if (count < 1) {
          query =
              session.createQuery(
                  "Update ParticipantPropertiesBO PBO SET PBO.isUsedInResource = 1 where PBO.anchorDateId=:anchorDateId");
          query.setInteger("anchorDateId", resourceBO.getAnchorDateId());
          query.executeUpdate();
        }
      }

      if (null == resourceBO.getId()) {
        resourceId = (Integer) session.save(resourceBO);
      } else {
        session.update(resourceBO);
        resourceId = resourceBO.getId();
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateResource() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateResource() - Ends");
    return resourceId;
  }

  /**
   * This method captures basic information about the study basic info like Study ID, Study name,
   * Study full name, Study Category, Research Sponsor,Data Partner, Estimated Duration in
   * weeks/months/years, Study Tagline, Study Description, Study website, Study Type, giving
   * permissions to all super admin
   *
   * @author BTC
   * @param StudyBo , {@link StudyBo}
   * @return {@link String}
   */
  @SuppressWarnings("unchecked")
  @Override
  public String saveOrUpdateStudy(StudyBo studyBo, SessionObject sessionObject) {
    logger.info("StudyDAOImpl - saveOrUpdateStudy() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.SUCCESS;
    StudyPermissionBO studyPermissionBO = null;
    Integer studyId = null;
    Integer userId = null;
    StudySequenceBo studySequenceBo = null;
    StudyBo dbStudyBo = null;
    String activitydetails = "";
    String activity = "";
    List<Integer> userSuperAdminList = null;
    try {
      userId = studyBo.getUserId();
      String appId = "";
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (studyBo.getId() == null) {
        studyBo.setCreatedBy(studyBo.getUserId());
        appId = studyBo.getAppId().toUpperCase();
        studyBo.setAppId(appId);
        studyBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
        studyBo.setTestModeStudyId(studyBo.getCustomStudyId());
        studyBo.setTestModeAppId(studyBo.getAppId());
        studyId = (Integer) session.save(studyBo);

        studyPermissionBO = new StudyPermissionBO();
        studyPermissionBO.setUserId(userId);
        studyPermissionBO.setStudyId(studyId);
        studyPermissionBO.setViewPermission(true);
        session.save(studyPermissionBO);

        // give permission to all super admin Start
        userSuperAdminList =
            session
                .createSQLQuery(
                    "Select upm.user_id from user_permission_mapping upm where upm.permission_id = :permissionId")
                .setInteger("permissionId", FdahpStudyDesignerConstants.ROLE_SUPERADMIN)
                .list();
        if (userSuperAdminList != null && !userSuperAdminList.isEmpty()) {
          for (Integer superAdminId : userSuperAdminList) {
            if (null != userId && !userId.equals(superAdminId)) {
              studyPermissionBO = new StudyPermissionBO();
              studyPermissionBO.setUserId(superAdminId);
              studyPermissionBO.setStudyId(studyId);
              studyPermissionBO.setViewPermission(true);
              session.save(studyPermissionBO);
            }
          }
        }
        // give permission to all super admin End

        // creating table to keep track of each section of study
        // completed or not
        studySequenceBo = new StudySequenceBo();
        studySequenceBo.setStudyId(studyId);
        session.save(studySequenceBo);

        // Phase2a code Start
        // create one record in anchordate_type table to give user to use enrollmentdate
        AnchorDateTypeBo anchorDateTypeBo = new AnchorDateTypeBo();
        anchorDateTypeBo.setCustomStudyId(studyBo.getCustomStudyId());
        anchorDateTypeBo.setStudyId(studyId);
        anchorDateTypeBo.setName(FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE);
        anchorDateTypeBo.setHasAnchortypeDraft(1);
        session.save(anchorDateTypeBo);
        // Phase2a code End
      } else {
        dbStudyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", studyBo.getId())
                    .uniqueResult();
        if (dbStudyBo != null) {
          if (dbStudyBo.getStudyMode() != null
              && dbStudyBo
                  .getStudyMode()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.STUDY_MODE_LIVE)) {
            if (dbStudyBo.getTestModeStudyId().equalsIgnoreCase(studyBo.getCustomStudyId())
                || dbStudyBo.getTestModeAppId().equalsIgnoreCase(studyBo.getAppId())) {
              throw new Exception(FdahpStudyDesignerConstants.STUDY_ID_APP_ID_ERR_MSG);
            }
          }

          if (dbStudyBo.getSwitchVal() != null && dbStudyBo.getSwitchVal() == 1) {
            dbStudyBo.setSwitchVal(2);
          }

          if (dbStudyBo.getStudyMode() != null
              && dbStudyBo
                  .getStudyMode()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.STUDY_MODE_TEST)) {
            dbStudyBo.setTestModeStudyId(studyBo.getCustomStudyId());
            dbStudyBo.setTestModeAppId(studyBo.getAppId());
          }

          if (!StringUtils.equals(dbStudyBo.getCustomStudyId(), studyBo.getCustomStudyId())) {
            // update CustomStudyId in AnchorDateTypeBo if new CustomStudyId is there
            query =
                session.createQuery(
                    "Update AnchorDateTypeBo ABO SET ABO.customStudyId=:newCustomStudyId where ABO.customStudyId=:customStudyId AND ABO.studyId=:studyId");
            query.setString("newCustomStudyId", studyBo.getCustomStudyId());
            query.setString("customStudyId", dbStudyBo.getCustomStudyId());
            query.setInteger("studyId", dbStudyBo.getId());
            query.executeUpdate();

            // update CustomStudyId of ParticipantPropertiesBO if new CustomStudyId is there

            query =
                session.createQuery(
                    "UPDATE ParticipantPropertiesBO PPBO set PPBO.customStudyId=:newStudyId,PPBO.appId=:appId,PPBO.orgId=:orgId where PPBO.customStudyId=:studyId");
            query.setString("newStudyId", studyBo.getCustomStudyId());
            query.setString("appId", studyBo.getAppId());
            query.setString("orgId", studyBo.getOrgId());
            query.setString("studyId", dbStudyBo.getCustomStudyId());
            query.executeUpdate();
          }

          if (StringUtils.equals(dbStudyBo.getCustomStudyId(), studyBo.getCustomStudyId())
              && !StringUtils.equals(dbStudyBo.getAppId(), studyBo.getAppId())) {

            query =
                session.createQuery(
                    "UPDATE ParticipantPropertiesBO PPBO set PPBO.appId=:appId,PPBO.orgId=:orgId where PPBO.customStudyId=:studyId");
            query.setString("appId", studyBo.getAppId());
            query.setString("orgId", studyBo.getOrgId());
            query.setString("studyId", studyBo.getCustomStudyId());
            query.executeUpdate();
          }

          dbStudyBo.setStudyLanguage(studyBo.getStudyLanguage());
          dbStudyBo.setCustomStudyId(studyBo.getCustomStudyId());
          dbStudyBo.setName(studyBo.getName());
          dbStudyBo.setFullName(studyBo.getFullName());
          dbStudyBo.setCategory(studyBo.getCategory());
          dbStudyBo.setResearchSponsor(studyBo.getResearchSponsor());
          dbStudyBo.setDataPartner(studyBo.getDataPartner());
          dbStudyBo.setTentativeDuration(studyBo.getTentativeDuration());
          dbStudyBo.setTentativeDurationWeekmonth(studyBo.getTentativeDurationWeekmonth());
          dbStudyBo.setDescription(studyBo.getDescription());
          dbStudyBo.setStudyTagLine(studyBo.getStudyTagLine());
          dbStudyBo.setStudyWebsite(studyBo.getStudyWebsite());
          dbStudyBo.setInboxEmailAddress(studyBo.getInboxEmailAddress());
          dbStudyBo.setType(studyBo.getType());
          dbStudyBo.setThumbnailImage(studyBo.getThumbnailImage());
          dbStudyBo.setModifiedBy(studyBo.getUserId());
          dbStudyBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          dbStudyBo.setAppId(studyBo.getAppId());
          dbStudyBo.setOrgId(studyBo.getOrgId());
          session.update(dbStudyBo);
        }
      }
      studySequenceBo =
          (StudySequenceBo)
              session
                  .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                  .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId())
                  .uniqueResult();
      if (studySequenceBo != null) {
        if (!studySequenceBo.isBasicInfo()
            && StringUtils.isNotEmpty(studyBo.getButtonText())
            && studyBo
                .getButtonText()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
          studySequenceBo.setBasicInfo(true);
          activity = "Study marked as completed.";
          activitydetails =
              "Study basic info section successsfully checked for minimum content completeness and marked 'Completed'. (Study ID = "
                  + studyBo.getCustomStudyId()
                  + ")";
        } else if (StringUtils.isNotEmpty(studyBo.getButtonText())
            && studyBo.getButtonText().equalsIgnoreCase(FdahpStudyDesignerConstants.SAVE_BUTTON)) {
          activity = "Content saved as draft.";
          activitydetails =
              "Study basic info content saved as draft. (Study ID = "
                  + studyBo.getCustomStudyId()
                  + ")";
          studySequenceBo.setBasicInfo(false);
        }
        session.update(studySequenceBo);
      }
      message =
          auditLogDAO.updateDraftToEditedStatus(
              session,
              transaction,
              studyBo.getUserId(),
              FdahpStudyDesignerConstants.DRAFT_STUDY,
              studyBo.getId());
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyDAOImpl - saveOrUpdateSubAdmin");

      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateSubAdmin() - ERROR", e);
      message = e.getMessage();
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateSubAdmin() - Ends");
    return message;
  }

  @Override
  public String saveOrUpdateStudyForOtherLanguages(
      StudyBo studyBo, StudyLanguageBO studyLanguageBO, int userId, String language) {
    logger.info("StudyDAOImpl - saveOrUpdateStudyForOtherLanguages() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      studyLanguageBO.setName(studyBo.getName());
      studyLanguageBO.setFullName(studyBo.getFullName());
      studyLanguageBO.setStudyTagline(studyBo.getStudyTagLine());
      studyLanguageBO.setDescription(studyBo.getDescription());
      studyLanguageBO.setResearchSponsor(studyBo.getResearchSponsor());
      session.saveOrUpdate(studyLanguageBO);
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateStudyForOtherLanguages() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateStudyForOtherLanguages() - Ends");
    return message;
  }

  @Override
  public ParticipantPropertiesBO saveOrUpdateParticipantProperties(
      ParticipantPropertiesBO participantPropertiesBO) {
    logger.info("StudyDAOImpl - saveOrUpdateParticipantProperties() - Starts");
    Session session = null;
    ParticipantPropertiesBO participantProperties = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (participantPropertiesBO.getId() == null) {
        if (participantPropertiesBO.getUseAsAnchorDate()) {
          Integer anchorDateId =
              addAnchorDate(
                  Integer.valueOf(participantPropertiesBO.getStudyId()),
                  participantPropertiesBO.getCustomStudyId(),
                  participantPropertiesBO.getShortTitle(),
                  session);
          participantPropertiesBO.setAnchorDateId(anchorDateId);
        }
        participantPropertiesBO.setActive(true);
        participantPropertiesBO.setStatus(true);
        participantPropertiesBO.setVersion(0f);
        participantPropertiesBO.setLive(0);
        session.save(participantPropertiesBO);
      } else {
        Query query = null;
        participantProperties =
            (ParticipantPropertiesBO)
                session.get(ParticipantPropertiesBO.class, participantPropertiesBO.getId());
        if (participantProperties.getAnchorDateId() != null) {
          if (!participantPropertiesBO.getUseAsAnchorDate()) {
            deleteParticipantPropertyAsAnchorDate(participantProperties.getAnchorDateId(), session);
            StudySequenceBo studySequence =
                (StudySequenceBo)
                    session
                        .getNamedQuery("getStudySequenceByStudyId")
                        .setInteger("studyId", participantProperties.getStudyId())
                        .uniqueResult();
            if (participantProperties.getIsUsedInQuestionnaire()) {
              query =
                  session.createQuery(
                      "UPDATE QuestionnaireBo QBO SET QBO.status=0,QBO.anchorDateId=:newAnchorDateId WHERE QBO.anchorDateId=:anchorDateId AND QBO.active=1");
              query.setParameter("newAnchorDateId", null);
              query.setInteger("anchorDateId", participantProperties.getAnchorDateId());
              query.executeUpdate();
              studySequence.setStudyExcQuestionnaries(false);
            }
            if (participantProperties.getIsUsedInActiveTask()) {
              query =
                  session.createQuery(
                      "UPDATE ActiveTaskBo ABO SET ABO.action=0,ABO.anchorDateId=:newAnchorDateId WHERE ABO.anchorDateId=:anchorDateId AND ABO.active=1");
              query.setParameter("newAnchorDateId", null);
              query.setInteger("anchorDateId", participantProperties.getAnchorDateId());
              query.executeUpdate();
              studySequence.setStudyExcActiveTask(false);
            }
            if (participantProperties.getIsUsedInResource()) {
              query =
                  session.createQuery(
                      "UPDATE ResourceBO RBO SET RBO.action=0,RBO.anchorDateId=:newAnchorDateId WHERE RBO.anchorDateId=:anchorDateId AND RBO.status=1");
              query.setParameter("newAnchorDateId", null);
              query.setInteger("anchorDateId", participantProperties.getAnchorDateId());
              query.executeUpdate();
              studySequence.setMiscellaneousResources(false);
            }
            session.saveOrUpdate(studySequence);
          } else {
            updateParticipantPropertyAsAnchorDate(
                participantProperties.getAnchorDateId(),
                participantPropertiesBO.getShortTitle(),
                session);
          }
        } else {
          if (participantPropertiesBO.getUseAsAnchorDate()) {
            Integer anchorDateId =
                addAnchorDate(
                    Integer.valueOf(participantPropertiesBO.getStudyId()),
                    participantPropertiesBO.getCustomStudyId(),
                    participantPropertiesBO.getShortTitle(),
                    session);
            participantPropertiesBO.setAnchorDateId(anchorDateId);
          }
        }
        participantPropertiesBO.setCreatedBy(participantProperties.getCreatedBy());
        participantPropertiesBO.setCreatedDate(participantProperties.getCreatedDate());
        participantPropertiesBO.setActive(participantProperties.getActive());
        participantPropertiesBO.setStatus(participantProperties.getStatus());
        participantPropertiesBO.setVersion(participantProperties.getVersion());
        participantPropertiesBO.setLive(participantProperties.getLive());
        session.merge(participantPropertiesBO);
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateParticipantProperties() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateParticipantProperties() - Ends");
    return participantPropertiesBO;
  }

  @Override
  public Integer addAnchorDate(
      int studyId, String customStudyId, String anchorDateName, Session session) {
    logger.info("StudyDAOImpl - addAnchorDate() - Starts");
    AnchorDateTypeBo anchorDateTypeBo = null;
    Integer anchorDateId = null;
    try {
      anchorDateTypeBo = new AnchorDateTypeBo();
      anchorDateTypeBo.setCustomStudyId(customStudyId);
      anchorDateTypeBo.setStudyId(studyId);
      anchorDateTypeBo.setName(anchorDateName);
      anchorDateTypeBo.setHasAnchortypeDraft(1);
      anchorDateTypeBo.setParticipantProperty(true);
      anchorDateId = (Integer) session.save(anchorDateTypeBo);
    } catch (Exception e) {
      logger.error("StudyDAOImpl - addAnchorDate() - ERROR", e);
    }
    logger.info("StudyDAOImpl - addAnchorDate() - Ends");
    return anchorDateId;
  }

  @Override
  public void deleteParticipantPropertyAsAnchorDate(Integer anchorDateId, Session session) {
    logger.info("StudyDAOImpl - deleteParticipantPropertyAsAnchorDate() - Starts");
    try {
      if (anchorDateId != null) {
        session
            .createQuery("UPDATE AnchorDateTypeBo set hasAnchortypeDraft=0 where id= :anchorDateId")
            .setInteger("anchorDateId", anchorDateId)
            .executeUpdate();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - deleteParticipantPropertyAsAnchorDate() - ERROR", e);
    }
    logger.info("StudyDAOImpl - deleteParticipantPropertyAsAnchorDate() - Ends");
  }

  @Override
  public void updateParticipantPropertyAsAnchorDate(
      Integer anchorDateId, String anchorDateName, Session session) {
    logger.info("StudyDAOImpl - updateParticipantPropertyAsAnchorDate() - Starts");
    try {
      if (anchorDateId != null) {
        session
            .createQuery(
                "UPDATE AnchorDateTypeBo set hasAnchortypeDraft=1,name= :anchorDateName where id= :anchorDateId")
            .setString("anchorDateName", anchorDateName)
            .setInteger("anchorDateId", anchorDateId)
            .executeUpdate();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - updateParticipantPropertyAsAnchorDate() - ERROR", e);
    }
    logger.info("StudyDAOImpl - updateParticipantPropertyAsAnchorDate() - Ends");
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ParticipantPropertiesBO> getParticipantProperties(String customStudyId) {
    logger.info("StudyDAOImpl - getParticipantProperties() - Starts");
    Session session = null;
    List<ParticipantPropertiesBO> participantPropertiesBoList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(customStudyId)) {
        participantPropertiesBoList =
            session
                .createQuery(
                    "From ParticipantPropertiesBO PPBO WHERE PPBO.customStudyId = :customStudyId and PPBO.active=1 order by PPBO.createdDate DESC")
                .setString("customStudyId", customStudyId)
                .list();
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - getParticipantProperties() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getParticipantProperties() - Ends");
    return participantPropertiesBoList;
  }

  @Override
  public ParticipantPropertiesBO getParticipantProperty(
      String participantPropertyId, String customStudyId) {
    logger.info("StudyDAOImpl - getParticipantProperty() - Starts");
    Session session = null;
    ParticipantPropertiesBO participantPropertiesBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(participantPropertyId) && StringUtils.isNotEmpty(customStudyId)) {
        participantPropertiesBO =
            (ParticipantPropertiesBO)
                session
                    .createQuery(
                        "From ParticipantPropertiesBO PPBO WHERE PPBO.customStudyId = :customStudyId and PPBO.id = :participantPropertyId and PPBO.active=1")
                    .setString("customStudyId", customStudyId)
                    .setInteger("participantPropertyId", Integer.valueOf(participantPropertyId))
                    .uniqueResult();
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - getParticipantProperty() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getParticipantProperty() - Ends");
    return participantPropertiesBO;
  }

  @Override
  public String deactivateParticipantProperty(int participantPropertyId, int userId) {
    logger.info("StudyDAOImpl - deactivateParticipantProperty() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    Query query = null;
    ParticipantPropertiesBO participantPropertiesBO = null;
    Integer anchorDateId = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      participantPropertiesBO =
          (ParticipantPropertiesBO)
              session.get(ParticipantPropertiesBO.class, participantPropertyId);
      anchorDateId = participantPropertiesBO.getAnchorDateId();
      count =
          session
              .createQuery(
                  "UPDATE ParticipantPropertiesBO set status=0,completed=1,isChange=1, modifiedBy= :userId"
                      + ", modifiedDate= :modifiedDate where id= :participantPropertyId")
              .setInteger("userId", userId)
              .setString("modifiedDate", FdahpStudyDesignerUtil.getCurrentDateTime())
              .setInteger("participantPropertyId", participantPropertyId)
              .executeUpdate();
      if (count > 0) {
        if (count > 0) {
          deleteParticipantPropertyAsAnchorDate(anchorDateId, session);
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deactivateParticipantProperty() - ERROR", e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deactivateParticipantProperty() - Ends");
    return message;
  }

  @Override
  public String deleteParticipantProperty(int participantPropertyId, int userId) {
    logger.info("StudyDAOImpl - deleteParticipantProperty() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    ParticipantPropertiesBO participantPropertiesBO = null;
    Integer anchorDateId = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      participantPropertiesBO =
          (ParticipantPropertiesBO)
              session.get(ParticipantPropertiesBO.class, participantPropertyId);
      anchorDateId = participantPropertiesBO.getAnchorDateId();
      count =
          session
              .createQuery(
                  "UPDATE ParticipantPropertiesBO set active=0, modifiedBy= :userId, modifiedDate= :modifiedDate where id= :participantPropertyId")
              .setInteger("userId", userId)
              .setString("modifiedDate", FdahpStudyDesignerUtil.getCurrentDateTime())
              .setInteger("participantPropertyId", participantPropertyId)
              .executeUpdate();
      if (count > 0) {
        deleteParticipantPropertyAsAnchorDate(anchorDateId, session);
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteParticipantProperty() - ERROR", e);
    } finally {
      if (null != session) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteParticipantProperty() - Ends");
    return message;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String checkParticipantPropertyShortTitle(String shortTitle, String customStudyId) {
    logger.info("StudyDAOImpl - checkParticipantPropertyShortTitle() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<ParticipantPropertiesBO> participantPropertiesBOs = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(customStudyId) && StringUtils.isNotEmpty(shortTitle)) {
        participantPropertiesBOs =
            session
                .createQuery(
                    "From ParticipantPropertiesBO PPBO WHERE PPBO.customStudyId = :customStudyId and PPBO.shortTitle= :shortTitle")
                .setString("customStudyId", customStudyId)
                .setString("shortTitle", shortTitle)
                .list();
        if (participantPropertiesBOs != null && !participantPropertiesBOs.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - checkParticipantPropertyShortTitle() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - checkParticipantPropertyShortTitle() - Ends");
    return message;
  }

  /**
   * Save or update eligibility of study
   *
   * @author BTC
   * @param eligibilityBo , {@link EligibilityBo}
   * @return {@link String} , the status FdahpStudyDesignerConstants.SUCCESS or
   *     FdahpStudyDesignerConstants.FAILURE
   * @exception Exception
   */
  @Override
  public String saveOrUpdateStudyEligibilty(
      EligibilityBo eligibilityBo, SessionObject sesObj, String customStudyId) {
    logger.info("StudyDAOImpl - saveOrUpdateStudyEligibilty() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    StudySequenceBo studySequence = null;
    EligibilityBo eligibilityBoUpdate = null;
    Boolean updateFlag = false;
    String activitydetails = "";
    String activity = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (null != eligibilityBo) {
        if (eligibilityBo.getId() != null) {
          eligibilityBoUpdate =
              (EligibilityBo)
                  session
                      .createQuery(" From EligibilityBo EBO WHERE EBO.id =:id")
                      .setInteger("id", eligibilityBo.getId())
                      .uniqueResult();
          eligibilityBoUpdate.setEligibilityMechanism(eligibilityBo.getEligibilityMechanism());
          eligibilityBoUpdate.setInstructionalText(eligibilityBo.getInstructionalText());
          eligibilityBoUpdate.setModifiedOn(eligibilityBo.getModifiedOn());
          eligibilityBoUpdate.setModifiedBy(eligibilityBo.getModifiedBy());
          updateFlag = true;
        } else {
          eligibilityBoUpdate = eligibilityBo;
        }

        studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, eligibilityBo.getStudyId())
                    .uniqueResult();
        if (studySequence != null) {
          if (eligibilityBo.getActionType() != null
              && ("mark").equals(eligibilityBo.getActionType())
              && !studySequence.isEligibility()) {
            studySequence.setEligibility(true);
          } else if (eligibilityBo.getActionType() != null
              && !("mark").equals(eligibilityBo.getActionType())) {
            studySequence.setEligibility(false);
          }
          session.saveOrUpdate(eligibilityBoUpdate);
        }
        if (("mark").equals(eligibilityBo.getActionType())) {
          activity = "Study eligibility marked as Completed.";
          activitydetails =
              "Section validated for minimum completion required and marked as Completed. (Study ID = "
                  + customStudyId
                  + ")";
        } else {
          activity = "Study eligibility content saved as draft.";
          activitydetails =
              "Study eligibility content saved as draft. (Study ID = " + customStudyId + ")";
        }
        auditLogDAO.saveToAuditLog(
            session,
            transaction,
            sesObj,
            activity,
            activitydetails,
            "StudyDAOImpl - saveOrUpdateStudyEligibilty");
        result =
            auditLogDAO.updateDraftToEditedStatus(
                session,
                transaction,
                (updateFlag ? eligibilityBo.getModifiedBy() : eligibilityBo.getCreatedBy()),
                FdahpStudyDesignerConstants.DRAFT_STUDY,
                eligibilityBo.getStudyId());
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateStudyEligibilty() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateStudyEligibilty() - Ends");
    return result;
  }

  /**
   * Save or update settings and admins of study
   *
   * @author BTC
   * @param studyBo , {@link StudyBo}
   * @param sesObj , {@link SessionObject}
   * @param userIds
   * @param permissions
   * @param projectLead
   * @return {@link String} , SUCCESS or FAILURE
   * @exception Exception
   */
  @SuppressWarnings({"unchecked"})
  @Override
  public String saveOrUpdateStudySettings(
      StudyBo studyBo,
      SessionObject sesObj,
      String userIds,
      String permissions,
      String projectLead,
      String newLanguages,
      String deletedLanguages) {
    logger.info("StudyDAOImpl - saveOrUpdateStudySettings() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    StudySequenceBo studySequence = null;
    StudyBo study = null;
    String activitydetails = "";
    String activity = "";
    String[] userId = null;
    String[] viewPermission = null;
    StudyPermissionBO studyPermissionBO = null;
    List<Integer> superAdminUserIds = null;
    String deleteExceptIds = "";
    String forceLogoutUserIds = "";
    List<Integer> deletingUserIds = new ArrayList<Integer>();
    List<Integer> deletingUserIdsWithoutLoginUser = new ArrayList<Integer>();
    boolean ownUserForceLogout = false;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (null != studyBo) {
        if (studyBo.getId() != null) {
          study =
              (StudyBo)
                  session
                      .createQuery("from StudyBo where id=:id")
                      .setInteger("id", studyBo.getId())
                      .uniqueResult();
          studySequence =
              (StudySequenceBo)
                  session
                      .createQuery("from StudySequenceBo where studyId=:id")
                      .setInteger("id", studyBo.getId())
                      .uniqueResult();
          if (study != null && studySequence != null) {
            // validation of anchor date
            updateAnchordateForEnrollmentDate(study, studyBo, session, transaction);
            // saving new Languages
            String languages =
                FdahpStudyDesignerUtil.isNotEmpty(study.getSelectedLanguages())
                    ? study.getSelectedLanguages().concat(newLanguages)
                    : newLanguages;
            if (FdahpStudyDesignerUtil.isNotEmpty(newLanguages)) {
              study.setSelectedLanguages(languages);
              saveSelectedLanguages(newLanguages, study, sesObj.getUserId(), session, transaction);
            }
            // removing existing language
            if (FdahpStudyDesignerUtil.isNotEmpty(deletedLanguages)) {
              List<String> existingLanguages =
                  new LinkedList<>(Arrays.asList(languages.split(",")));
              String[] langToBeDeleted = deletedLanguages.split(",");
              for (String lang : langToBeDeleted) {
                if (FdahpStudyDesignerUtil.isNotEmpty(lang)) {
                  existingLanguages.remove(lang);
                }
              }
              StringBuilder selectedLanguages = new StringBuilder();
              for (String lang : existingLanguages) {
                if (FdahpStudyDesignerUtil.isNotEmpty(lang)) {
                  selectedLanguages.append(lang).append(",");
                }
              }
              study.setSelectedLanguages(selectedLanguages.toString());
              deleteExistingLanguages(study.getId(), deletedLanguages, session, transaction);
            }
            study.setMultiLanguageFlag(studyBo.getMultiLanguageFlag());
            if (!studyBo.getMultiLanguageFlag().equals(true)) {
              study.setSelectedLanguages(null);
            }
            study.setPlatform(studyBo.getPlatform());
            study.setAllowRejoin(studyBo.getAllowRejoin());
            study.setEnrollingParticipants(studyBo.getEnrollingParticipants());
            study.setRetainParticipant(studyBo.getRetainParticipant());
            study.setAllowRejoin(studyBo.getAllowRejoin());
            study.setAllowRejoinText(studyBo.getAllowRejoinText());
            study.setModifiedBy(studyBo.getUserId());
            study.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            // Phase2a code Start(adding enrollment date as anchor date(yes/no))
            study.setEnrollmentdateAsAnchordate(studyBo.isEnrollmentdateAsAnchordate());
            // Phase2a code end
            session.saveOrUpdate(study);

            // setting true to setting admins
            // setting and admins section of Study completed or not
            // updated in study sequence table(to keep track of each
            // section of study completed or not)
            if (StringUtils.isNotEmpty(studyBo.getButtonText())
                && studyBo
                    .getButtonText()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)
                && !studySequence.isSettingAdmins()) {
              studySequence.setSettingAdmins(true);
            } else if (StringUtils.isNotEmpty(studyBo.getButtonText())
                && studyBo
                    .getButtonText()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.SAVE_BUTTON)) {
              studySequence.setSettingAdmins(false);
            }
            session.update(studySequence);

            // Phase2a code Start(adding enrollment date as anchor date(yes/no))
            if (studyBo.isEnrollmentdateAsAnchordate()) {
              session
                  .createSQLQuery(
                      "UPDATE anchordate_type set has_anchortype_draft=1 where study_id=:id"
                          + " and has_anchortype_draft=0 and name=:name")
                  .setInteger("id", study.getId())
                  .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                  .executeUpdate();
            } else {
              session
                  .createSQLQuery(
                      "UPDATE anchordate_type set has_anchortype_draft=0 where study_id=:id"
                          + " and has_anchortype_draft=1 and name=:name")
                  .setInteger("id", study.getId())
                  .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                  .executeUpdate();
            }
            // Phase2a code end
          }
        }

        /* admin section starts */
        deleteExceptIds = userIds;
        superAdminUserIds = getSuperAdminUserIds();
        if (superAdminUserIds != null) {
          for (Integer id : superAdminUserIds) {
            if (deleteExceptIds == "") {
              deleteExceptIds = String.valueOf(id);
            } else {
              deleteExceptIds += "," + id;
            }
          }
        }
        //				query = session.createSQLQuery(" SELECT sp.user_id FROM study_permission sp WHERE
        // sp.user_id NOT IN ("
        //						+ deleteExceptIds + ") AND sp.study_id=:id");
        query =
            session.createSQLQuery(
                " SELECT sp.user_id FROM study_permission sp WHERE sp.user_id NOT IN (:deleteExceptIds) AND sp.study_id=:id");
        deletingUserIds =
            query
                .setInteger("id", studyBo.getId())
                .setParameterList("deleteExceptIds", deleteExceptIds.split(","))
                .list();
        deletingUserIdsWithoutLoginUser.addAll(deletingUserIds);
        if (deletingUserIds.contains(sesObj.getUserId())) {
          ownUserForceLogout = true;
          deletingUserIdsWithoutLoginUser.remove(sesObj.getUserId());
        }

        if (null != deletingUserIdsWithoutLoginUser && !deletingUserIdsWithoutLoginUser.isEmpty()) {
          for (Integer id : deletingUserIdsWithoutLoginUser) {
            if (forceLogoutUserIds == "") {
              forceLogoutUserIds = String.valueOf(id);
            } else {
              forceLogoutUserIds += "," + id;
            }
          }
        }

        if (!"".equals(userIds) && !"".equals(permissions)) {
          userId = userIds.split(",");
          viewPermission = permissions.split(",");

          if (null != deletingUserIds && !deletingUserIds.isEmpty()) {
            //						query = session.createSQLQuery(" DELETE FROM study_permission WHERE user_id NOT
            // IN ("
            //								+ deleteExceptIds + ") AND study_id =:id");
            query =
                session.createSQLQuery(
                    " DELETE FROM study_permission WHERE user_id NOT IN (:deleteExceptIds) AND study_id =:id");
            query
                .setInteger("id", studyBo.getId())
                .setParameterList("deleteExceptIds", deleteExceptIds.split(","))
                .executeUpdate();
          }

          for (int i = 0; i < userId.length; i++) {
            query =
                session.createQuery(
                    "FROM StudyPermissionBO UBO WHERE UBO.userId = :userId AND studyId =:id");
            studyPermissionBO =
                (StudyPermissionBO)
                    query
                        .setString("userId", userId[i])
                        .setInteger("id", studyBo.getId())
                        .uniqueResult();
            if (null != studyPermissionBO) {
              Boolean flag = false;
              if (studyPermissionBO.isViewPermission() != "1".equals(viewPermission[i])
                  ? true
                  : false) {
                studyPermissionBO.setViewPermission("1".equals(viewPermission[i]) ? true : false);
                flag = true;
              }
              if ((studyPermissionBO.getProjectLead() != null
                      ? studyPermissionBO.getProjectLead()
                      : 0)
                  != (projectLead.equals(userId[i]) ? 1 : 0)) {
                studyPermissionBO.setProjectLead(projectLead.equals(userId[i]) ? 1 : 0);
                flag = true;
              }
              if (flag) {
                session.update(studyPermissionBO);
                if (sesObj.getUserId().equals(Integer.parseInt(userId[i]))) {
                  ownUserForceLogout = true;
                } else {
                  if (forceLogoutUserIds == "") {
                    forceLogoutUserIds = userId[i];
                  } else {
                    forceLogoutUserIds += "," + userId[i];
                  }
                }
              }
            } else {
              studyPermissionBO = new StudyPermissionBO();
              studyPermissionBO.setStudyId(studyBo.getId());
              studyPermissionBO.setUserId(Integer.parseInt(userId[i]));
              studyPermissionBO.setViewPermission("1".equals(viewPermission[i]) ? true : false);
              studyPermissionBO.setProjectLead(projectLead.equals(userId[i]) ? 1 : 0);
              session.save(studyPermissionBO);

              UserBO user = null;
              boolean present = false;
              Set<UserPermissions> permissionSet = null;
              query = session.createQuery(" FROM UserBO UBO where UBO.userId = :userId");
              user = (UserBO) query.setString("userId", userId[i]).uniqueResult();
              if (user != null) {
                Set<String> oldPermissions = new HashSet<>();
                for (UserPermissions temp : user.getPermissions()) {
                  oldPermissions.add(temp.getPermissions());
                  if (temp.getPermissions().equals("ROLE_MANAGE_STUDIES")) {
                    present = true;
                  }
                }

                if (!present) {
                  permissionSet =
                      new HashSet<UserPermissions>(
                          session
                              .createQuery(
                                  "FROM UserPermissions UPBO WHERE UPBO.permissions IN (:permissions)")
                              .setParameterList("permissions", oldPermissions)
                              .list());
                  user.setPermissionList(permissionSet);
                  user.setModifiedBy(study.getUserId());
                  user.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                  session.update(user);
                }
              }

              if (sesObj.getUserId().equals(Integer.parseInt(userId[i]))) {
                ownUserForceLogout = true;
              } else {
                if (forceLogoutUserIds == "") {
                  forceLogoutUserIds = userId[i];
                } else {
                  forceLogoutUserIds += "," + userId[i];
                }
              }
            }
          }
        } else {
          if (null != deletingUserIds && !deletingUserIds.isEmpty()) {
            //						query = session.createSQLQuery(" DELETE FROM study_permission WHERE user_id NOT
            // IN ("
            //								+ deleteExceptIds + ") AND study_id =" + studyBo.getId());
            query =
                session.createSQLQuery(
                    " DELETE FROM study_permission WHERE user_id NOT IN (:deleteExceptIds) AND study_id =:id");
            query
                .setParameterList("deleteExceptIds", deleteExceptIds.split(","))
                .setInteger("id", studyBo.getId())
                .executeUpdate();
          }
        }

        if (forceLogoutUserIds != "") {
          //					query = session.createSQLQuery(
          //							" UPDATE users SET force_logout = 'Y' WHERE user_id IN (" + forceLogoutUserIds +
          // ")");
          query =
              session.createSQLQuery(
                  " UPDATE users SET force_logout = 'Y' WHERE user_id IN (:forceLogoutUserIds) ");
          query
              .setParameterList("forceLogoutUserIds", forceLogoutUserIds.split(","))
              .executeUpdate();
        }

        /* admin section ends */

        result =
            auditLogDAO.updateDraftToEditedStatus(
                session,
                transaction,
                studyBo.getUserId(),
                FdahpStudyDesignerConstants.DRAFT_STUDY,
                studyBo.getId());

        if (result.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS) && ownUserForceLogout) {
          result = FdahpStudyDesignerConstants.WARNING;
        }

        if (study != null) {
          if (studyBo
              .getButtonText()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
            activity = "Study settings marked as Completed.";
            activitydetails =
                "Section validated for minimum completion required and marked as Completed. (Study ID = "
                    + study.getCustomStudyId()
                    + ")";
          } else {
            activity = "Study settings content saved as draft.";
            activitydetails =
                "Study settings content saved as draft. (Study ID = "
                    + study.getCustomStudyId()
                    + ")";
          }
        }
        auditLogDAO.saveToAuditLog(
            session,
            transaction,
            sesObj,
            activity,
            activitydetails,
            "StudyDAOImpl - saveOrUpdateStudySettings");
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateStudySettings() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateStudySettings() - Ends");
    return result;
  }

  @Override
  public String saveOrUpdateStudySettingsForOtherLanguages(StudyBo studyBo, String currLang) {
    logger.info("StudyDAOImpl - saveOrUpdateStudySettingsForOtherLanguages() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session
          .createSQLQuery(
              "update studies_lang set allow_rejoin_text=:alertText where study_id=:studyId and lang_code=:language")
          .setString("alertText", studyBo.getAllowRejoinText())
          .setInteger("studyId", studyBo.getId())
          .setString("language", currLang)
          .executeUpdate();
      transaction.commit();
      result = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateStudySettingsForOtherLanguages() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateStudySettingsForOtherLanguages() - Ends");
    return result;
  }

  /**
   * save the Study Overview Page By PageId
   *
   * @author BTC
   * @param String , studyId
   * @return {@link Integer}
   */
  public Integer saveOverviewStudyPageById(String studyId) {
    Integer pageId = 0;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(studyId)) {
        StudyPageBo studyPageBo = new StudyPageBo();
        studyPageBo.setStudyId(Integer.parseInt(studyId));
        pageId = (Integer) session.save(studyPageBo);
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - deleteOverviewStudyPageById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteOverviewStudyPageById() - Ends");

    return pageId;
  }

  /**
   * This method is used to save the resource notification
   *
   * @author BTC
   * @param notificationBO , {@link NotificationBO}
   * @return message, Success/Failure message.
   */
  @Override
  public String saveResourceNotification(NotificationBO notificationBO, boolean notiFlag) {
    logger.info("StudyDAOImpl - saveResourceNotification() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (!notiFlag) {
        session.save(notificationBO);
      } else {
        session.update(notificationBO);
      }
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveResourceNotification() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveResourceNotification() - Ends");
    return message;
  }

  @Autowired
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.hibernateTemplate = new HibernateTemplate(sessionFactory);
  }

  /**
   * create live study from existing study
   *
   * @author BTC
   * @param studyBo , {@link StudyBo}
   * @param session , {@link Session}
   * @return {@link String}
   */
  @SuppressWarnings("unchecked")
  public String studyDraftCreation(StudyBo studyBo, Session session) {
    logger.info("StudyDAOImpl - studyDraftCreation() - Starts");
    List<StudyPageBo> studyPageBo = null;
    List<StudyPermissionBO> studyPermissionList = null;
    EligibilityBo eligibilityBo = null;
    List<ResourceBO> resourceBOList = null;
    StudyVersionBo studyVersionBo = null;
    StudyVersionBo newstudyVersionBo = null;
    boolean flag = true;
    String message = FdahpStudyDesignerConstants.FAILURE;
    List<QuestionnaireBo> questionnaires = null;
    List<ActiveTaskBo> activeTasks = null;
    String searchQuery = "";
    QuestionReponseTypeBo questionReponseTypeBo = null;
    List<String> objectList = null;
    List<String> questionnarieShorttitleList = null;
    List<AnchorDateTypeBo> anchorDateTypeList = null;
    Map<Integer, Integer> anchordateoldnewMapList = new HashMap<>();
    List<ParticipantPropertiesBO> participantPropertiesBOs = null;
    try {
      if (studyBo != null) {
        logger.info("StudyDAOImpl - studyDraftCreation() getStudyByCustomStudyId- Starts");
        // if already launch and study hasStudyDraft()==1 , then update
        // and create draft version , otherwise not
        query =
            session
                .getNamedQuery("getStudyByCustomStudyId")
                .setMaxResults(1)
                .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
        query.setMaxResults(1);
        studyVersionBo = (StudyVersionBo) query.uniqueResult();
        logger.info("StudyDAOImpl - studyDraftCreation() getStudyByCustomStudyId- Ends");

        boolean mlFlag =
            studyBo.getMultiLanguageFlag() != null ? studyBo.getMultiLanguageFlag() : false;
        if (studyVersionBo != null && (studyBo.getHasStudyDraft().equals(0)) && !mlFlag) {
          flag = false;
        }
        if (flag) {
          // version update in study_version table
          if (studyVersionBo != null) {
            logger.info("StudyDAOImpl - studyDraftCreation() updateStudyVersion- Starts");
            // update all studies to archive (live as 2)
            // pass customstudyId and making all study status belongs to same customstudyId
            // as 2(archive)
            query =
                session
                    .getNamedQuery("updateStudyVersion")
                    .setString(
                        FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
            query.executeUpdate();
            logger.info("StudyDAOImpl - studyDraftCreation() updateStudyVersion- Ends");

            if (mlFlag) {
              query =
                  session
                      .createQuery(
                          "UPDATE StudyLanguageBO SET live=2 WHERE studyLanguagePK.study_id=:studyId and live=1")
                      .setInteger("studyId", studyBo.getId());
              query.executeUpdate();
            }

            newstudyVersionBo = SerializationUtils.clone(studyVersionBo);
            newstudyVersionBo.setStudyVersion(studyVersionBo.getStudyVersion() + 0.1f);
            if (studyBo.getHasConsentDraft().equals(1)) {
              newstudyVersionBo.setConsentVersion(studyVersionBo.getConsentVersion() + 0.1f);
            }
            newstudyVersionBo.setVersionId(null);
            session.save(newstudyVersionBo);
          } else {
            newstudyVersionBo = new StudyVersionBo();
            newstudyVersionBo.setCustomStudyId(studyBo.getCustomStudyId());
            newstudyVersionBo.setActivityVersion(1.0f);
            newstudyVersionBo.setConsentVersion(1.0f);
            newstudyVersionBo.setStudyVersion(1.0f);
            session.save(newstudyVersionBo);
          }

          // create new Study and made it draft study
          StudyBo studyDreaftBo = SerializationUtils.clone(studyBo);
          if (newstudyVersionBo != null) {
            studyDreaftBo.setVersion(newstudyVersionBo.getStudyVersion());
            studyDreaftBo.setLive(1);
          }
          studyDreaftBo.setId(null);
          session.save(studyDreaftBo);

          // change id in language tables
          List<StudyLanguageBO> studyLanguageBOList =
              session
                  .createQuery("from StudyLanguageBO where studyLanguagePK.study_id=:studyId")
                  .setInteger("studyId", studyBo.getId())
                  .list();
          for (StudyLanguageBO studyLanguageBO : studyLanguageBOList) {
            studyLanguageBO.setConsentCompleted(true);
            session.update(studyLanguageBO);
            StudyLanguageBO studyLanguageBO1 = SerializationUtils.clone(studyLanguageBO);
            studyLanguageBO1.getStudyLanguagePK().setStudy_id(studyDreaftBo.getId());
            studyLanguageBO1.setLive(1);
            session.save(studyLanguageBO1);
          }

          // clone of Study Permission

          studyPermissionList =
              session
                  .createQuery("from StudyPermissionBO where studyId=:id")
                  .setInteger("id", studyBo.getId())
                  .list();
          if (studyPermissionList != null) {
            logger.info("StudyDAOImpl - studyDraftCreation() StudyPermissionBO- Starts");
            for (StudyPermissionBO permissionBO : studyPermissionList) {
              StudyPermissionBO studyPermissionBO = SerializationUtils.clone(permissionBO);
              studyPermissionBO.setStudyId(studyDreaftBo.getId());
              studyPermissionBO.setStudyPermissionId(null);
              session.save(studyPermissionBO);
            }
            logger.info("StudyDAOImpl - studyDraftCreation() StudyPermissionBO- Ends");
          }

          // clone of Study Sequence
          StudySequenceBo studySequence =
              (StudySequenceBo)
                  session
                      .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                      .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId())
                      .uniqueResult();
          StudySequenceBo newStudySequenceBo = SerializationUtils.clone(studySequence);
          newStudySequenceBo.setStudyId(studyDreaftBo.getId());
          newStudySequenceBo.setStudySequenceId(null);
          session.save(newStudySequenceBo);

          // clone of Study Sequence Lang
          List<StudySequenceLangBO> studySequenceLangBOS =
              session
                  .createQuery(
                      "from StudySequenceLangBO where studySequenceLangPK.studyId=:studyId")
                  .setInteger("studyId", studyBo.getId())
                  .list();
          for (StudySequenceLangBO sequenceLangBO : studySequenceLangBOS) {
            StudySequenceLangBO sequenceLangBO1 = SerializationUtils.clone(sequenceLangBO);
            sequenceLangBO1.getStudySequenceLangPK().setStudyId(studyDreaftBo.getId());
            session.save(sequenceLangBO1);
          }

          // clone of Over View section
          query = session.createQuery("from StudyPageBo where studyId=:studyId");
          studyPageBo = query.setInteger("studyId", studyBo.getId()).list();
          if (studyPageBo != null && !studyPageBo.isEmpty()) {
            for (StudyPageBo pageBo : studyPageBo) {
              StudyPageBo subPageBo = SerializationUtils.clone(pageBo);
              subPageBo.setStudyId(studyDreaftBo.getId());
              subPageBo.setPageId(null);
              session.save(subPageBo);
              // clone of Over View Multi Lang section
              List<StudyPageLanguageBO> studyPageLanguageBOS =
                  session
                      .createQuery(
                          "from StudyPageLanguageBO where studyPageLanguagePK.pageId=:pageId")
                      .setInteger("pageId", pageBo.getPageId())
                      .list();
              for (StudyPageLanguageBO studyPageLanguageBO : studyPageLanguageBOS) {
                StudyPageLanguageBO studyPageLanguageBO1 =
                    SerializationUtils.clone(studyPageLanguageBO);
                studyPageLanguageBO1.getStudyPageLanguagePK().setPageId(subPageBo.getPageId());
                studyPageLanguageBO1.setStudyId(studyDreaftBo.getId());
                session.save(studyPageLanguageBO1);
              }
            }
          }

          // clone of Eligibility
          query =
              session
                  .getNamedQuery("getEligibiltyByStudyId")
                  .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId());
          eligibilityBo = (EligibilityBo) query.uniqueResult();
          if (eligibilityBo != null) {
            EligibilityBo bo = SerializationUtils.clone(eligibilityBo);
            bo.setStudyId(studyDreaftBo.getId());
            bo.setId(null);
            session.save(bo);
            List<EligibilityTestBo> eligibilityTestList = null;
            eligibilityTestList =
                session
                    .getNamedQuery("EligibilityTestBo.findByEligibilityId")
                    .setInteger(FdahpStudyDesignerConstants.ELIGIBILITY_ID, eligibilityBo.getId())
                    .list();
            if (eligibilityTestList != null && !eligibilityTestList.isEmpty()) {
              List<Integer> eligibilityTestIds = new ArrayList<>();
              for (EligibilityTestBo eligibilityTestBo : eligibilityTestList) {
                eligibilityTestIds.add(eligibilityTestBo.getId());
                EligibilityTestBo newEligibilityTestBo =
                    SerializationUtils.clone(eligibilityTestBo);
                newEligibilityTestBo.setId(null);
                newEligibilityTestBo.setEligibilityId(bo.getId());
                newEligibilityTestBo.setUsed(true);
                session.save(newEligibilityTestBo);

                List<EligibilityTestLangBo> eligibilityTestLangBos =
                    session
                        .createQuery(
                            "from EligibilityTestLangBo where eligibilityTestLangPK.id=:id")
                        .setInteger("id", eligibilityTestBo.getId())
                        .list();
                for (EligibilityTestLangBo eligibilityTestLangBo : eligibilityTestLangBos) {
                  EligibilityTestLangBo eligibilityTestLangBo1 =
                      SerializationUtils.clone(eligibilityTestLangBo);
                  eligibilityTestLangBo1
                      .getEligibilityTestLangPK()
                      .setId(newEligibilityTestBo.getId());
                  eligibilityTestLangBo1.setStudyId(studyDreaftBo.getId());
                  session.save(eligibilityTestLangBo1);
                }
              }
              if (!eligibilityTestIds.isEmpty())
                session
                    .createSQLQuery("UPDATE eligibility_test set is_used='Y' where id in (:ids)")
                    .setParameterList("ids", eligibilityTestIds)
                    .executeUpdate();
            }
          }

          // clone anchor date data
          /*
           * searchQuery = " FROM AnchorDateTypeBo RBO WHERE RBO.studyId=" +
           * studyBo.getId() + " AND RBO.hasAnchortypeDraft = 1 ORDER BY RBO.id DESC";
           * query = session.createQuery(searchQuery); anchorDateTypeList = query.list();
           * if(anchorDateTypeList!=null && !anchorDateTypeList.isEmpty()) {
           * logger.info("StudyDAOImpl - studyDraftCreation() AnchorDateTypeBo- Starts");
           * Integer oldAnchorId = null; for (AnchorDateTypeBo bo : anchorDateTypeList) {
           * oldAnchorId = bo.getId(); AnchorDateTypeBo anchorDateTypeBo =
           * SerializationUtils .clone(bo);
           * anchorDateTypeBo.setStudyId(studyDreaftBo.getId());
           * anchorDateTypeBo.setId(null);
           * anchorDateTypeBo.setVersion(studyDreaftBo.getVersion());
           * session.save(anchorDateTypeBo); anchordateoldnewMapList.put(oldAnchorId,
           * anchorDateTypeBo.getId()); }
           * logger.info("StudyDAOImpl - studyDraftCreation() AnchorDateTypeBo- Ends"); }
           */

          // clone of resources
          searchQuery =
              " FROM ResourceBO RBO WHERE RBO.studyId=:id AND RBO.status = 1 ORDER BY RBO.createdOn DESC ";
          query = session.createQuery(searchQuery);
          resourceBOList = query.setInteger("id", studyBo.getId()).list();
          if (resourceBOList != null && !resourceBOList.isEmpty()) {
            logger.info("StudyDAOImpl - studyDraftCreation() ResourceBO- Starts");
            for (ResourceBO bo : resourceBOList) {
              ResourceBO resourceBO = SerializationUtils.clone(bo);
              resourceBO.setStudyId(studyDreaftBo.getId());
              resourceBO.setId(null);
              // If resource is a target anchor date or not,yes then update new anchordate Id
              //							if(resourceBO.getAnchorDateId()!=null) {
              //
              //	resourceBO.setAnchorDateId(anchordateoldnewMapList.get(bo.getAnchorDateId()));
              //						     }
              // If resource is a target anchor date or not,yes then update anchordate Id
              session.save(resourceBO);

              List<ResourcesLangBO> resourcesLangBOList =
                  session
                      .createQuery("from ResourcesLangBO where resourcesLangPK.id=:id")
                      .setInteger("id", bo.getId())
                      .list();
              for (ResourcesLangBO resourcesLangBO : resourcesLangBOList) {
                ResourcesLangBO resourcesLangBO1 = SerializationUtils.clone(resourcesLangBO);
                resourcesLangBO1.getResourcesLangPK().setId(resourceBO.getId());
                resourcesLangBO1.setStudyId(studyDreaftBo.getId());
                session.save(resourcesLangBO1);
              }
            }
            logger.info("StudyDAOImpl - studyDraftCreation() ResourceBO- Ends");
          }

          query =
              session.createQuery(
                  "FROM ParticipantPropertiesBO PBO WHERE PBO.studyId=:id AND PBO.active = 1 ORDER BY PBO.createdDate DESC");
          participantPropertiesBOs = query.setInteger("id", studyBo.getId()).list();
          for (ParticipantPropertiesBO pbo : participantPropertiesBOs) {

            if (null != pbo.getLive() && pbo.getLive() != 0) {
              if (pbo.getIsChange()) {
                pbo.setVersion(pbo.getVersion() + 0.1f);
                // pbo.setStudyVersion(studyDreaftBo.getVersion());
              }
            } else {
              pbo.setLive(1);
              pbo.setVersion(1f);
              // pbo.setStudyVersion(studyDreaftBo.getVersion());
            }
            pbo.setIsChange(false);
            session.update(pbo);
          }

          query =
              session.createQuery(
                  "FROM ParticipantPropertiesBO PBO WHERE PBO.studyId=:id and PBO.active=1 ORDER BY PBO.createdDate DESC");
          participantPropertiesBOs = query.setInteger("id", studyBo.getId()).list();
          if (participantPropertiesBOs != null && !participantPropertiesBOs.isEmpty()) {
            for (ParticipantPropertiesBO bo : participantPropertiesBOs) {
              ParticipantPropertiesDraftBO pBO = new ParticipantPropertiesDraftBO();
              BeanUtils.copyProperties(pBO, bo);
              pBO.setStudyId(studyDreaftBo.getId());
              pBO.setStudyVersion(studyDreaftBo.getVersion());
              pBO.setId(null);
              session.save(pBO);
            }
          }

          query =
              session.createQuery(
                  "select shortTitle from ParticipantPropertiesBO where status=0 and studyId=:studyId and shortTitle is NOT NULL");
          query.setInteger("studyId", studyBo.getId());
          objectList = query.list();

          if (objectList != null && !objectList.isEmpty()) {
            try {
              String currentDateTime = FdahpStudyDesignerUtil.getCurrentDateTime();
              String subQuery =
                  "update participant_properties_draft SET modified_date=:currentTime, "
                      + " status=0 where short_title IN(:objectList) and status=1 and custom_study_id=:custStudyId";
              query = session.createSQLQuery(subQuery);
              query.setParameter("currentTime", currentDateTime);
              query.setParameterList("objectList", objectList);
              query.setParameter("custStudyId", studyBo.getCustomStudyId());
              query.executeUpdate();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          // If Questionnaire updated flag -1 then update(clone)
          if (studyVersionBo == null
              || (studyBo.getHasQuestionnaireDraft() != null
                  && studyBo.getHasQuestionnaireDraft().equals(1))
              || mlFlag) {
            // Questionarries
            query =
                session
                    .getNamedQuery("getQuestionariesByStudyId")
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId());
            questionnaires = query.list();
            if (questionnaires != null && !questionnaires.isEmpty()) {
              // short title taking updating to archived which
              // have change start
              questionnarieShorttitleList = new ArrayList<>();
              for (QuestionnaireBo questionnaireBo : questionnaires) {
                if (questionnaireBo.getIsChange() != null
                    && questionnaireBo.getIsChange().equals(1)) {
                  questionnarieShorttitleList.add("'" + questionnaireBo.getShortTitle() + "'");
                }
              }
              if (questionnarieShorttitleList != null && !questionnarieShorttitleList.isEmpty()) {
                logger.info(
                    "StudyDAOImpl - studyDraftCreation() Questionnarie update is_live=2- Starts");
                queryString =
                    "update questionnaires SET is_live=2 where short_title IN (:shortTitleList) and is_live=1 and custom_study_id=:id";
                query = session.createSQLQuery(queryString);
                query
                    .setParameterList("shortTitleList", questionnarieShorttitleList)
                    .setString("id", studyBo.getCustomStudyId())
                    .executeUpdate();
                logger.info(
                    "StudyDAOImpl - studyDraftCreation() Questionnarie update is_live=2- Ends");
              }

              // setting live=-1 in all questionnaires as new records will be created for each row
              // with live=1
              session
                  .getNamedQuery("updateStudyQuestionnaireVersion")
                  .setString("customStudyId", studyBo.getCustomStudyId())
                  .executeUpdate();
              // short title taking updating to archived which
              // have change end
              for (QuestionnaireBo questionnaireBo : questionnaires) {
                logger.info("StudyDAOImpl - studyDraftCreation() Questionnarie creation- Starts");
                // creating in study Activity version
                StudyActivityVersionBo studyActivityVersionBo = new StudyActivityVersionBo();
                studyActivityVersionBo.setCustomStudyId(studyBo.getCustomStudyId());
                studyActivityVersionBo.setStudyVersion(newstudyVersionBo.getStudyVersion());
                studyActivityVersionBo.setActivityType("Q");
                studyActivityVersionBo.setShortTitle(questionnaireBo.getShortTitle());
                // is there any change in questionnarie
//                if (questionnaireBo.getIsChange() != null && questionnaireBo.getIsChange().equals(1)
//                    || mlFlag) {
                // commenting this condition as we are already updating live flags to -1 earlier

                  Float questionnarieversion = questionnaireBo.getVersion();
                  QuestionnaireBo newQuestionnaireBo = SerializationUtils.clone(questionnaireBo);
                  newQuestionnaireBo.setId(null);
                  newQuestionnaireBo.setStudyId(studyDreaftBo.getId());
                  newQuestionnaireBo.setCreatedBy(0);
                  newQuestionnaireBo.setModifiedBy(0);
                  newQuestionnaireBo.setModifiedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
                  if (studyVersionBo == null) {
                    newQuestionnaireBo.setVersion(1.0f);
                    questionnaireBo.setVersion(1.0f);
                  } else {
                    if (questionnarieversion.equals(0f)) {
                      questionnaireBo.setVersion(1.0f);
                      newQuestionnaireBo.setVersion(1.0f);
                    } else {
                      newQuestionnaireBo.setVersion(questionnaireBo.getVersion() + 0.1f);
                      questionnaireBo.setVersion(questionnaireBo.getVersion() + 0.1f);
                    }
                  }
                  newQuestionnaireBo.setLive(1);
                  newQuestionnaireBo.setCustomStudyId(studyBo.getCustomStudyId());
                  // If questionnaire is a target anchor date or not,yes then update new
                  // anchordate Id
                  //									if(questionnaireBo.getScheduleType()!=null &&
                  // questionnaireBo.getScheduleType().equals(FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)) {
                  //										if(questionnaireBo.getAnchorDateId()!=null) {
                  //
                  //	newQuestionnaireBo.setAnchorDateId(anchordateoldnewMapList.get(questionnaireBo.getAnchorDateId()));
                  //										}
                  //									}
                  // If questionnaire is a target anchor date or not,yes then update anchordate Id
                  session.save(newQuestionnaireBo);
                  questionnaireBo.setIsChange(0);
                  questionnaireBo.setLive(0);
                  session.update(questionnaireBo);

                  List<QuestionnaireLangBO> questionnaireLangBOS =
                      session
                          .createQuery("from QuestionnaireLangBO where questionnaireLangPK.id=:id")
                          .setInteger("id", questionnaireBo.getId())
                          .list();
                  for (QuestionnaireLangBO questionnaireLangBO : questionnaireLangBOS) {
                    QuestionnaireLangBO questionnaireLangBO1 =
                        SerializationUtils.clone(questionnaireLangBO);
                    questionnaireLangBO1.getQuestionnaireLangPK().setId(newQuestionnaireBo.getId());
                    questionnaireLangBO1.setStudyId(studyDreaftBo.getId());
                    session.save(questionnaireLangBO1);
                  }

                  /** Schedule Purpose creating draft Start * */
                  if (StringUtils.isNotEmpty(questionnaireBo.getFrequency())) {
                    if (questionnaireBo
                        .getFrequency()
                        .equalsIgnoreCase(
                            FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
                      searchQuery =
                          "From QuestionnaireCustomScheduleBo QCSBO where QCSBO.questionnairesId=:id";
                      List<QuestionnaireCustomScheduleBo> questionnaireCustomScheduleList =
                          session
                              .createQuery(searchQuery)
                              .setInteger("id", questionnaireBo.getId())
                              .list();
                      if (questionnaireCustomScheduleList != null
                          && !questionnaireCustomScheduleList.isEmpty()) {
                        logger.info(
                            "StudyDAOImpl - studyDraftCreation() Questionnarie manual schedule update - Starts");
                        for (QuestionnaireCustomScheduleBo customScheduleBo :
                            questionnaireCustomScheduleList) {
                          QuestionnaireCustomScheduleBo newCustomScheduleBo =
                              SerializationUtils.clone(customScheduleBo);
                          newCustomScheduleBo.setQuestionnairesId(newQuestionnaireBo.getId());
                          newCustomScheduleBo.setId(null);
                          session.save(newCustomScheduleBo);
                        }
                        // updating draft version of
                        // schecule to Yes
                        session
                            .createQuery(
                                "UPDATE QuestionnaireCustomScheduleBo set used=true where questionnairesId=:id")
                            .setInteger("id", questionnaireBo.getId())
                            .executeUpdate();
                        logger.info(
                            "StudyDAOImpl - studyDraftCreation() Questionnarie manual schedule update - Ends");
                      }
                    } else {
                      searchQuery =
                          "From QuestionnairesFrequenciesBo QFBO where QFBO.questionnairesId=:id";
                      List<QuestionnairesFrequenciesBo> questionnairesFrequenciesList =
                          session
                              .createQuery(searchQuery)
                              .setInteger("id", questionnaireBo.getId())
                              .list();
                      if (questionnairesFrequenciesList != null
                          && !questionnairesFrequenciesList.isEmpty()) {
                        logger.info(
                            "StudyDAOImpl - studyDraftCreation() Questionnarie except manual schedule other update - Starts");
                        for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
                            questionnairesFrequenciesList) {
                          QuestionnairesFrequenciesBo newQuestionnairesFrequenciesBo =
                              SerializationUtils.clone(questionnairesFrequenciesBo);
                          newQuestionnairesFrequenciesBo.setQuestionnairesId(
                              newQuestionnaireBo.getId());
                          newQuestionnairesFrequenciesBo.setId(null);
                          session.save(newQuestionnairesFrequenciesBo);
                        }
                        logger.info(
                            "StudyDAOImpl - studyDraftCreation() Questionnarie except manual schedule other update - Ends");
                      }
                    }
                  }
                  /** Schedule Purpose creating draft End * */
                  /** Content purpose creating draft Start * */
                  List<Integer> destinationList = new ArrayList<>();
                  Map<Integer, Integer> destionationMapList = new HashMap<>();

                  List<QuestionnairesStepsBo> existedQuestionnairesStepsBoList = null;
                  List<QuestionnairesStepsBo> newQuestionnairesStepsBoList = new ArrayList<>();
                  List<QuestionResponseSubTypeBo> existingQuestionResponseSubTypeList =
                      new ArrayList<>();
                  List<QuestionResponseSubTypeBo> newQuestionResponseSubTypeList =
                      new ArrayList<>();

                  List<QuestionReponseTypeBo> existingQuestionResponseTypeList = new ArrayList<>();
                  List<QuestionReponseTypeBo> newQuestionResponseTypeList = new ArrayList<>();

                  query =
                      session
                          .getNamedQuery("getQuestionnaireStepSequenceNo")
                          .setInteger("questionnairesId", questionnaireBo.getId());
                  existedQuestionnairesStepsBoList = query.list();
                  if (existedQuestionnairesStepsBoList != null
                      && !existedQuestionnairesStepsBoList.isEmpty()) {
                    for (QuestionnairesStepsBo questionnairesStepsBo :
                        existedQuestionnairesStepsBoList) {
                      Integer destionStep = questionnairesStepsBo.getDestinationStep();
                      if (destionStep.equals(0)) {
                        destinationList.add(-1);
                      } else {
                        for (int i = 0; i < existedQuestionnairesStepsBoList.size(); i++) {
                          if (existedQuestionnairesStepsBoList.get(i).getStepId() != null
                              && destionStep.equals(
                                  existedQuestionnairesStepsBoList.get(i).getStepId())) {
                            destinationList.add(i);
                            break;
                          }
                        }
                      }
                      destionationMapList.put(
                          questionnairesStepsBo.getSequenceNo(), questionnairesStepsBo.getStepId());
                    }
                    for (QuestionnairesStepsBo questionnairesStepsBo :
                        existedQuestionnairesStepsBoList) {
                      if (StringUtils.isNotEmpty(questionnairesStepsBo.getStepType())) {
                        QuestionnairesStepsBo newQuestionnairesStepsBo =
                            SerializationUtils.clone(questionnairesStepsBo);
                        newQuestionnairesStepsBo.setQuestionnairesId(newQuestionnaireBo.getId());
                        newQuestionnairesStepsBo.setStepId(null);
                        session.save(newQuestionnairesStepsBo);

                        List<PreLoadLogicBo> preLoadLogicBoList = session.createQuery("from PreLoadLogicBo where stepGroupId=:stepId and stepOrGroup=:step")
                                .setParameter("stepId", questionnairesStepsBo.getStepId())
                                .setParameter(FdahpStudyDesignerConstants.STEP, FdahpStudyDesignerConstants.STEP)
                                .list();

                        for (PreLoadLogicBo preLoadLogicBo : preLoadLogicBoList) {
                          PreLoadLogicBo newPreLoadLogicBo = SerializationUtils.clone(preLoadLogicBo);
                          newPreLoadLogicBo.setId(null);
                          newPreLoadLogicBo.setStepGroupId(newQuestionnairesStepsBo.getStepId());
                          session.save(newPreLoadLogicBo);
                        }

                        if (questionnairesStepsBo
                            .getStepType()
                            .equalsIgnoreCase(FdahpStudyDesignerConstants.INSTRUCTION_STEP)) {
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie instruction step - Starts");
                          InstructionsBo instructionsBo =
                              (InstructionsBo)
                                  session
                                      .getNamedQuery("getInstructionStep")
                                      .setInteger(
                                          "id", questionnairesStepsBo.getInstructionFormId())
                                      .uniqueResult();
                          if (instructionsBo != null) {
                            InstructionsBo newInstructionsBo =
                                SerializationUtils.clone(instructionsBo);
                            newInstructionsBo.setId(null);
                            session.save(newInstructionsBo);

                            List<InstructionsLangBO> instructionsLangBOS =
                                session
                                    .createQuery(
                                        "from InstructionsLangBO where instructionLangPK.id=:id")
                                    .setInteger("id", instructionsBo.getId())
                                    .list();
                            for (InstructionsLangBO instructionsLangBO : instructionsLangBOS) {
                              InstructionsLangBO instructionsLangBO1 =
                                  SerializationUtils.clone(instructionsLangBO);
                              instructionsLangBO1
                                  .getInstructionLangPK()
                                  .setId(newInstructionsBo.getId());
                              instructionsLangBO1.setQuestionnaireId(newQuestionnaireBo.getId());
                              session.save(instructionsLangBO1);
                            }

                            // updating new
                            // InstructionId
                            newQuestionnairesStepsBo.setInstructionFormId(
                                newInstructionsBo.getId());
                          }
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie instruction step - Ends");
                        } else if (questionnairesStepsBo
                            .getStepType()
                            .equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie Qustion step - Starts");
                          QuestionsBo questionsBo =
                              (QuestionsBo)
                                  session
                                      .getNamedQuery("getQuestionStep")
                                      .setInteger(
                                          "stepId", questionnairesStepsBo.getInstructionFormId())
                                      .uniqueResult();
                          if (questionsBo != null) {
                            // Question response
                            // subType
                            List<QuestionResponseSubTypeBo> questionResponseSubTypeList =
                                session
                                    .getNamedQuery("getQuestionSubResponse")
                                    .setInteger("responseTypeId", questionsBo.getId())
                                    .list();
                            List<QuestionConditionBranchBo> questionConditionBranchList =
                                session
                                    .getNamedQuery("getQuestionConditionBranchList")
                                    .setInteger("questionId", questionsBo.getId())
                                    .list();

                            // Question response
                            // Type
                            questionReponseTypeBo =
                                (QuestionReponseTypeBo)
                                    session
                                        .getNamedQuery("getQuestionResponse")
                                        .setInteger("questionsResponseTypeId", questionsBo.getId())
                                        .setMaxResults(1)
                                        .uniqueResult();

                            QuestionsBo newQuestionsBo = SerializationUtils.clone(questionsBo);
                            newQuestionsBo.setId(null);
                            // update source anchordate id start
                            // if(questionsBo.getUseAnchorDate() &&
                            // questionsBo.getAnchorDateId()!=null)
                            // newQuestionsBo.setAnchorDateId(anchordateoldnewMapList.get(questionsBo.getAnchorDateId()));
                            // update source anchor date id end
                            session.save(newQuestionsBo);

                            if (mlFlag) {
                              List<QuestionLangBO> questionLangBOS =
                                  session
                                      .createQuery(
                                          "from QuestionLangBO where questionLangPK.id=:id")
                                      .setInteger("id", questionsBo.getId())
                                      .list();
                              for (QuestionLangBO questionLangBO : questionLangBOS) {
                                QuestionLangBO questionLangBO1 =
                                    SerializationUtils.clone(questionLangBO);
                                questionLangBO1.getQuestionLangPK().setId(newQuestionsBo.getId());
                                questionLangBO1.setQuestionnaireId(newQuestionnaireBo.getId());
                                session.save(questionLangBO1);
                              }
                            }

                            // Question response
                            // Type
                            if (questionReponseTypeBo != null) {

                              QuestionReponseTypeBo newQuestionReponseTypeBo =
                                  SerializationUtils.clone(questionReponseTypeBo);
                              newQuestionReponseTypeBo.setResponseTypeId(null);
                              newQuestionReponseTypeBo.setQuestionsResponseTypeId(
                                  newQuestionsBo.getId());
                              newQuestionReponseTypeBo.setOtherDestinationStepId(null);
                              session.save(newQuestionReponseTypeBo);

                              if (questionReponseTypeBo.getOtherType() != null
                                  && StringUtils.isNotEmpty(questionReponseTypeBo.getOtherType())
                                  && questionReponseTypeBo.getOtherType().equals("on")) {
                                existingQuestionResponseTypeList.add(questionReponseTypeBo);
                                newQuestionResponseTypeList.add(newQuestionReponseTypeBo);
                              }
                            }

                            // Question Condition
                            // branching logic
                            if (questionConditionBranchList != null
                                && !questionConditionBranchList.isEmpty()) {
                              for (QuestionConditionBranchBo questionConditionBranchBo :
                                  questionConditionBranchList) {
                                QuestionConditionBranchBo newQuestionConditionBranchBo =
                                    SerializationUtils.clone(questionConditionBranchBo);
                                newQuestionConditionBranchBo.setConditionId(null);
                                newQuestionConditionBranchBo.setQuestionId(newQuestionsBo.getId());
                                session.save(newQuestionConditionBranchBo);
                              }
                            }

                            // Question response
                            // subType
                            if (questionResponseSubTypeList != null
                                && !questionResponseSubTypeList.isEmpty()) {
                              existingQuestionResponseSubTypeList.addAll(
                                  questionResponseSubTypeList);

                              for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                                  questionResponseSubTypeList) {
                                QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                                    SerializationUtils.clone(questionResponseSubTypeBo);
                                newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                                newQuestionResponseSubTypeBo.setResponseTypeId(
                                    newQuestionsBo.getId());
                                newQuestionResponseSubTypeBo.setDestinationStepId(null);
                                session.save(newQuestionResponseSubTypeBo);
                                newQuestionResponseSubTypeList.add(newQuestionResponseSubTypeBo);
                              }
                            }

                            // updating new
                            // InstructionId
                            newQuestionnairesStepsBo.setInstructionFormId(newQuestionsBo.getId());
                          }
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie Qustion step - Ends");
                        } else if (questionnairesStepsBo
                            .getStepType()
                            .equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie Form step - Starts");
                          FormBo formBo =
                              (FormBo)
                                  session
                                      .getNamedQuery("getFormBoStep")
                                      .setInteger(
                                          "stepId", questionnairesStepsBo.getInstructionFormId())
                                      .uniqueResult();
                          if (formBo != null) {
                            FormBo newFormBo = SerializationUtils.clone(formBo);
                            newFormBo.setFormId(null);
                            session.save(newFormBo);

                            if (mlFlag) {
                              List<FormLangBO> formLangBOS =
                                  session
                                      .createQuery("from FormLangBO where formLangPK.formId=:id")
                                      .setInteger("id", formBo.getFormId())
                                      .list();
                              for (FormLangBO formLangBO : formLangBOS) {
                                FormLangBO formLangBO1 = SerializationUtils.clone(formLangBO);
                                formLangBO1.getFormLangPK().setFormId(newFormBo.getFormId());
                                formLangBO1.setQuestionnaireId(newQuestionnaireBo.getId());
                                session.save(formLangBO1);
                              }
                            }

                            List<FormMappingBo> formMappingBoList =
                                session
                                    .getNamedQuery("getFormByFormId")
                                    .setInteger("formId", formBo.getFormId())
                                    .list();
                            if (formMappingBoList != null && !formMappingBoList.isEmpty()) {
                              for (FormMappingBo formMappingBo : formMappingBoList) {
                                FormMappingBo newMappingBo =
                                    SerializationUtils.clone(formMappingBo);
                                newMappingBo.setFormId(newFormBo.getFormId());
                                newMappingBo.setId(null);

                                QuestionsBo questionsBo =
                                    (QuestionsBo)
                                        session
                                            .getNamedQuery("getQuestionByFormId")
                                            .setInteger("formId", formMappingBo.getQuestionId())
                                            .uniqueResult();
                                if (questionsBo != null) {
                                  // Question
                                  // response
                                  // subType
                                  List<QuestionResponseSubTypeBo> questionResponseSubTypeList =
                                      session
                                          .getNamedQuery("getQuestionSubResponse")
                                          .setInteger("responseTypeId", questionsBo.getId())
                                          .list();

                                  // Question
                                  // response
                                  // Type
                                  questionReponseTypeBo =
                                      (QuestionReponseTypeBo)
                                          session
                                              .getNamedQuery("getQuestionResponse")
                                              .setInteger(
                                                  "questionsResponseTypeId", questionsBo.getId())
                                              .setMaxResults(1)
                                              .uniqueResult();

                                  QuestionsBo newQuestionsBo =
                                      SerializationUtils.clone(questionsBo);
                                  newQuestionsBo.setId(null);
                                  // update source anchordate id start
                                  // if(questionsBo.getUseAnchorDate() &&
                                  // questionsBo.getAnchorDateId()!=null)
                                  // newQuestionsBo.setAnchorDateId(anchordateoldnewMapList.get(questionsBo.getAnchorDateId()));
                                  // update source anchor date id end
                                  session.save(newQuestionsBo);

                                  if (mlFlag) {
                                    List<QuestionLangBO> questionLangBOS =
                                        session
                                            .createQuery(
                                                "from QuestionLangBO where questionLangPK.id=:id")
                                            .setInteger("id", questionsBo.getId())
                                            .list();
                                    for (QuestionLangBO questionLangBO : questionLangBOS) {
                                      QuestionLangBO questionLangBO1 =
                                          SerializationUtils.clone(questionLangBO);
                                      questionLangBO1
                                          .getQuestionLangPK()
                                          .setId(newQuestionsBo.getId());
                                      questionLangBO1.setFormId(newFormBo.getFormId());
                                      questionLangBO1.setQuestionnaireId(
                                          newQuestionnaireBo.getId());
                                      session.save(questionLangBO1);
                                    }
                                  }

                                  // Question
                                  // response
                                  // Type
                                  if (questionReponseTypeBo != null) {
                                    QuestionReponseTypeBo newQuestionReponseTypeBo =
                                        SerializationUtils.clone(questionReponseTypeBo);
                                    newQuestionReponseTypeBo.setResponseTypeId(null);
                                    newQuestionReponseTypeBo.setQuestionsResponseTypeId(
                                        newQuestionsBo.getId());
                                    session.save(newQuestionReponseTypeBo);
                                  }

                                  // Question
                                  // response
                                  // subType
                                  if (questionResponseSubTypeList != null
                                      && !questionResponseSubTypeList.isEmpty()) {
                                    for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                                        questionResponseSubTypeList) {
                                      QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                                          SerializationUtils.clone(questionResponseSubTypeBo);
                                      newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                                      newQuestionResponseSubTypeBo.setResponseTypeId(
                                          newQuestionsBo.getId());
                                      session.save(newQuestionResponseSubTypeBo);
                                    }
                                  }

                                  // adding
                                  // questionId
                                  newMappingBo.setQuestionId(newQuestionsBo.getId());
                                  session.save(newMappingBo);
                                }
                              }
                            }
                            // updating new formId
                            newQuestionnairesStepsBo.setInstructionFormId(newFormBo.getFormId());
                          }
                          logger.info(
                              "StudyDAOImpl - studyDraftCreation() Questionnarie Form step - Ends");
                        }
                        session.update(newQuestionnairesStepsBo);
                        newQuestionnairesStepsBoList.add(newQuestionnairesStepsBo);
                      }
                    }
                  }
                  if (destinationList != null && !destinationList.isEmpty()) {
                    for (int i = 0; i < destinationList.size(); i++) {
                      int desId = 0;
                      if (destinationList.get(i) != -1) {
                        desId =
                            newQuestionnairesStepsBoList.get(destinationList.get(i)).getStepId();
                      }
                      newQuestionnairesStepsBoList.get(i).setDestinationStep(desId);
                      session.update(newQuestionnairesStepsBoList.get(i));
                    }
                  }
                  List<Integer> sequenceSubTypeList = new ArrayList<>();
                  List<Integer> destinationResList = new ArrayList<>();
                  if (existingQuestionResponseSubTypeList != null
                      && !existingQuestionResponseSubTypeList.isEmpty()) {
                    for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                        existingQuestionResponseSubTypeList) {
                      if (questionResponseSubTypeBo.getDestinationStepId() == null) {
                        sequenceSubTypeList.add(null);
                      } else if (questionResponseSubTypeBo.getDestinationStepId() != null
                          && questionResponseSubTypeBo.getDestinationStepId().equals(0)) {
                        sequenceSubTypeList.add(-1);
                      } else {
                        if (existedQuestionnairesStepsBoList != null
                            && !existedQuestionnairesStepsBoList.isEmpty()) {
                          for (QuestionnairesStepsBo questionnairesStepsBo :
                              existedQuestionnairesStepsBoList) {
                            if (questionResponseSubTypeBo.getDestinationStepId() != null
                                && questionResponseSubTypeBo
                                    .getDestinationStepId()
                                    .equals(questionnairesStepsBo.getStepId())) {
                              sequenceSubTypeList.add(questionnairesStepsBo.getSequenceNo());
                              break;
                            }
                          }
                        }
                      }
                    }
                  }
                  if (sequenceSubTypeList != null && !sequenceSubTypeList.isEmpty()) {
                    for (int i = 0; i < sequenceSubTypeList.size(); i++) {
                      Integer desId = null;
                      if (sequenceSubTypeList.get(i) == null) {
                        desId = null;
                      } else if (sequenceSubTypeList.get(i).equals(-1)) {
                        desId = 0;
                      } else {
                        for (QuestionnairesStepsBo questionnairesStepsBo :
                            newQuestionnairesStepsBoList) {
                          if (sequenceSubTypeList
                              .get(i)
                              .equals(questionnairesStepsBo.getSequenceNo())) {
                            desId = questionnairesStepsBo.getStepId();
                            break;
                          }
                        }
                      }
                      destinationResList.add(desId);
                    }
                    for (int i = 0; i < destinationResList.size(); i++) {
                      newQuestionResponseSubTypeList
                          .get(i)
                          .setDestinationStepId(destinationResList.get(i));
                      session.update(newQuestionResponseSubTypeList.get(i));
                    }
                  }

                  // for other type , update the destination in questionresponsetype table
                  /** start * */
                  List<Integer> sequenceTypeList = new ArrayList<>();
                  List<Integer> destinationResTypeList = new ArrayList<>();
                  if (existingQuestionResponseTypeList != null
                      && !existingQuestionResponseTypeList.isEmpty()) {
                    for (QuestionReponseTypeBo questionResponseTypeBo :
                        existingQuestionResponseTypeList) {
                      if (questionResponseTypeBo.getOtherDestinationStepId() == null) {
                        sequenceTypeList.add(null);
                      } else if (questionResponseTypeBo.getOtherDestinationStepId() != null
                          && questionResponseTypeBo.getOtherDestinationStepId().equals(0)) {
                        sequenceTypeList.add(-1);
                      } else {
                        if (existedQuestionnairesStepsBoList != null
                            && !existedQuestionnairesStepsBoList.isEmpty()) {
                          for (QuestionnairesStepsBo questionnairesStepsBo :
                              existedQuestionnairesStepsBoList) {
                            if (questionResponseTypeBo.getOtherDestinationStepId() != null
                                && questionResponseTypeBo
                                    .getOtherDestinationStepId()
                                    .equals(questionnairesStepsBo.getStepId())) {
                              sequenceTypeList.add(questionnairesStepsBo.getSequenceNo());
                              break;
                            }
                          }
                        }
                      }
                    }
                  }
                  if (sequenceTypeList != null && !sequenceTypeList.isEmpty()) {
                    for (int i = 0; i < sequenceTypeList.size(); i++) {
                      Integer desId = null;
                      if (sequenceTypeList.get(i) == null) {
                        desId = null;
                      } else if (sequenceTypeList.get(i).equals(-1)) {
                        desId = 0;
                      } else {
                        for (QuestionnairesStepsBo questionnairesStepsBo :
                            newQuestionnairesStepsBoList) {
                          if (sequenceTypeList
                              .get(i)
                              .equals(questionnairesStepsBo.getSequenceNo())) {
                            desId = questionnairesStepsBo.getStepId();
                            break;
                          }
                        }
                      }
                      destinationResTypeList.add(desId);
                    }
                    for (int i = 0; i < destinationResTypeList.size(); i++) {
                      newQuestionResponseTypeList
                          .get(i)
                          .setOtherDestinationStepId(destinationResTypeList.get(i));
                      session.update(newQuestionResponseTypeList.get(i));
                    }
                  }
                  /** * end ** */
                  studyActivityVersionBo.setActivityVersion(newQuestionnaireBo.getVersion());
                  /** Content purpose creating draft End * */
//                } else {
//                  studyActivityVersionBo.setActivityVersion(questionnaireBo.getVersion());
//                }
                session.save(studyActivityVersionBo);
                logger.info("StudyDAOImpl - studyDraftCreation() Questionnarie creation- Ends");
              }
              // Executing draft version to 0
            } // If Questionarries updated flag -1 then update End
          } // In Questionnarie change or not

          // which are already in live those are deleted in draft to
          // making update those questionnarie to archived and make it
          // inactive(status=0)
          /*
           * StringBuilder subString = new StringBuilder();
           * subString.append("select CONCAT('"); subString.append('"');
           * subString.append("',shortTitle,'"); subString.append('"');
           * subString.append("') from QuestionnaireBo where active=0 and studyId=" +
           * studyBo.getId() + " and shortTitle is NOT NULL");
           */
          query =
              session.createQuery(
                  "select CONCAT('',shortTitle,'') from QuestionnaireBo where active=0 and studyId=:id"
                      + " and shortTitle is NOT NULL");
          objectList = query.setInteger("id", studyBo.getId()).list();

          /*
           * if (objectList != null && !objectList.isEmpty()) { String subQuery =
           * "update questionnaires SET is_live=2,modified_date='" +
           * FdahpStudyDesignerUtil.getCurrentDateTime() +
           * "', active=0 where short_title IN(" + StringUtils.join(objectList,
           * ",").replaceAll(":", "':'") + ") and is_live=1 and custom_study_id='" +
           * studyBo.getCustomStudyId() + "'"; query = session.createSQLQuery(subQuery);
           * query.executeUpdate(); }
           */

          if (objectList != null && !objectList.isEmpty()) {
            try {
              String currentDateTime = FdahpStudyDesignerUtil.getCurrentDateTime();
              String subQuery =
                  "update questionnaires SET is_live=2,modified_date=:currentTime, "
                      + " active=0 where short_title IN(:objectList) and is_live=1 and custom_study_id=:custStudyId";
              query = session.createSQLQuery(subQuery);
              query.setParameter("currentTime", currentDateTime);
              query.setParameterList("objectList", objectList);
              query.setParameter("custStudyId", studyBo.getCustomStudyId());
              query.executeUpdate();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          // In ActiveTask change or not Start
          // is there any change doing clone of active task
          if (studyVersionBo == null
              || (studyBo.getHasActivetaskDraft() != null
                  && studyBo.getHasActivetaskDraft().equals(1))
              || mlFlag) {
            // update all ActiveTasks to archive (live as 2)
            query =
                session
                    .getNamedQuery("updateStudyActiveTaskVersion")
                    .setString(
                        FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
            query.executeUpdate();

            // ActiveTasks
            query =
                session
                    .getNamedQuery("ActiveTaskBo.getActiveTasksByByStudyId")
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId());
            activeTasks = query.list();
            if (activeTasks != null && !activeTasks.isEmpty()) {
              for (ActiveTaskBo activeTaskBo : activeTasks) {
                Float activeTaskversion = activeTaskBo.getVersion();
                ActiveTaskBo newActiveTaskBo = SerializationUtils.clone(activeTaskBo);
                newActiveTaskBo.setId(null);
                newActiveTaskBo.setStudyId(studyDreaftBo.getId());
                if (studyVersionBo == null) {
                  newActiveTaskBo.setVersion(1.0f);
                  activeTaskBo.setVersion(1.0f);
                } else if (activeTaskBo.getIsChange() != null
                    && activeTaskBo.getIsChange().equals(1)) {
                  if (activeTaskversion.equals(0f)) {
                    activeTaskBo.setVersion(1.0f);
                    newActiveTaskBo.setVersion(1.0f);
                  } else {
                    newActiveTaskBo.setVersion(activeTaskBo.getVersion() + 0.1f);
                    activeTaskBo.setVersion(activeTaskBo.getVersion() + 0.1f);
                  }
                } else {
                  newActiveTaskBo.setVersion(activeTaskBo.getVersion());
                  activeTaskBo.setVersion(activeTaskBo.getVersion());
                }
                newActiveTaskBo.setLive(1);
                newActiveTaskBo.setCustomStudyId(studyBo.getCustomStudyId());
                // If activeTask is a target anchor date or not,yes then update new anchordate
                // Id
                //								if(activeTaskBo.getScheduleType()!=null &&
                // activeTaskBo.getScheduleType().equals(FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)) {
                //									if(activeTaskBo.getAnchorDateId()!=null) {
                //
                //	newActiveTaskBo.setAnchorDateId(anchordateoldnewMapList.get(activeTaskBo.getAnchorDateId()));
                //									}
                //								}
                // If activeTask is a target anchor date or not,yes then update anchordate Id
                session.save(newActiveTaskBo);

                List<ActiveTaskLangBO> activeTaskLangBOS =
                    session
                        .createQuery("from ActiveTaskLangBO where activeTaskLangPK.id=:id")
                        .setInteger("id", activeTaskBo.getId())
                        .list();
                for (ActiveTaskLangBO activeTaskLangBO : activeTaskLangBOS) {
                  ActiveTaskLangBO activeTaskLangBO1 = SerializationUtils.clone(activeTaskLangBO);
                  activeTaskLangBO1.getActiveTaskLangPK().setId(newActiveTaskBo.getId());
                  activeTaskLangBO1.setStudyId(studyDreaftBo.getId());
                  session.save(activeTaskLangBO1);
                }

                /** Schedule Purpose creating draft Start * */
                if (StringUtils.isNotEmpty(activeTaskBo.getFrequency())) {
                  if (activeTaskBo
                      .getFrequency()
                      .equalsIgnoreCase(
                          FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
                    searchQuery =
                        "From ActiveTaskCustomScheduleBo QCSBO where QCSBO.activeTaskId=:id";
                    List<ActiveTaskCustomScheduleBo> activeTaskCustomScheduleList =
                        session
                            .createQuery(searchQuery)
                            .setInteger("id", activeTaskBo.getId())
                            .list();
                    if (activeTaskCustomScheduleList != null
                        && !activeTaskCustomScheduleList.isEmpty()) {
                      for (ActiveTaskCustomScheduleBo customScheduleBo :
                          activeTaskCustomScheduleList) {
                        ActiveTaskCustomScheduleBo newCustomScheduleBo =
                            SerializationUtils.clone(customScheduleBo);
                        newCustomScheduleBo.setActiveTaskId(newActiveTaskBo.getId());
                        newCustomScheduleBo.setId(null);
                        session.save(newCustomScheduleBo);
                        session.update(customScheduleBo);
                      }
                      // updating draft version of
                      // schedule to Yes
                      session
                          .createQuery(
                              "UPDATE ActiveTaskCustomScheduleBo set used=true where activeTaskId=:id")
                          .setInteger("id", activeTaskBo.getId())
                          .executeUpdate();
                    }
                  } else {
                    searchQuery = "From ActiveTaskFrequencyBo QFBO where QFBO.activeTaskId=:id";
                    List<ActiveTaskFrequencyBo> activeTaskFrequenciesList =
                        session
                            .createQuery(searchQuery)
                            .setInteger("id", activeTaskBo.getId())
                            .list();
                    if (activeTaskFrequenciesList != null && !activeTaskFrequenciesList.isEmpty()) {
                      for (ActiveTaskFrequencyBo activeTaskFrequenciesBo :
                          activeTaskFrequenciesList) {
                        ActiveTaskFrequencyBo newFrequenciesBo =
                            SerializationUtils.clone(activeTaskFrequenciesBo);
                        newFrequenciesBo.setActiveTaskId(newActiveTaskBo.getId());
                        newFrequenciesBo.setId(null);
                        session.save(newFrequenciesBo);
                      }
                    }
                  }
                }
                /** Schedule Purpose creating draft End * */

                /** Content Purpose creating draft Start * */
                query =
                    session
                        .getNamedQuery("getAttributeListByActiveTAskId")
                        .setInteger("activeTaskId", activeTaskBo.getId());
                List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBoList = query.list();
                if (activeTaskAtrributeValuesBoList != null
                    && !activeTaskAtrributeValuesBoList.isEmpty()) {
                  for (ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo :
                      activeTaskAtrributeValuesBoList) {
                    ActiveTaskAtrributeValuesBo newActiveTaskAtrributeValuesBo =
                        SerializationUtils.clone(activeTaskAtrributeValuesBo);
                    newActiveTaskAtrributeValuesBo.setActiveTaskId(newActiveTaskBo.getId());
                    newActiveTaskAtrributeValuesBo.setAttributeValueId(null);
                    session.save(newActiveTaskAtrributeValuesBo);
                  }
                }
                /** Content Purpose creating draft End * */
              }
              // Executing draft version to 0
              session
                  .createQuery("UPDATE ActiveTaskBo set live=0, isChange = 0 where studyId=:id")
                  .setInteger("id", studyBo.getId())
                  .executeUpdate();
            } // Active TAsk End
          } // In ActiveTask change or not
          // Activities End

          // If Consent updated flag -1 then update
          query =
              session.createQuery(
                  "from ConsentBo CBO where CBO.studyId=:id ORDER BY CBO.createdOn DESC");
          ConsentBo consentBo =
              (ConsentBo) query.setInteger("id", studyBo.getId()).setMaxResults(1).uniqueResult();

          if (studyVersionBo == null || studyBo.getHasConsentDraft().equals(1) || mlFlag) {
            // update all consentBo to archive (live as 2)
            query =
                session
                    .getNamedQuery("updateStudyConsentVersion")
                    .setString(
                        FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
            query.executeUpdate();

            // update all consentInfoBo to archive (live as 2)
            query =
                session
                    .getNamedQuery("updateStudyConsentInfoVersion")
                    .setString(
                        FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
            query.executeUpdate();

            // If Consent updated flag -1 then update
            if (consentBo != null) {
              ConsentBo newConsentBo = SerializationUtils.clone(consentBo);
              newConsentBo.setId(null);
              newConsentBo.setStudyId(studyDreaftBo.getId());
              newConsentBo.setVersion(newstudyVersionBo.getConsentVersion());
              newConsentBo.setLive(1);
              newConsentBo.setCustomStudyId(studyBo.getCustomStudyId());
              session.save(newConsentBo);
            }
            query =
                session
                    .getNamedQuery("getConsentInfoByStudyId")
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId());
            List<ConsentInfoBo> consentInfoBoList = query.list();
            if (consentInfoBoList != null && !consentInfoBoList.isEmpty()) {
              for (ConsentInfoBo consentInfoBo : consentInfoBoList) {
                ConsentInfoBo newConsentInfoBo = SerializationUtils.clone(consentInfoBo);
                newConsentInfoBo.setId(null);
                newConsentInfoBo.setStudyId(studyDreaftBo.getId());
                newConsentInfoBo.setVersion(newstudyVersionBo.getConsentVersion());
                newConsentInfoBo.setCustomStudyId(studyBo.getCustomStudyId());
                newConsentInfoBo.setLive(1);
                session.save(newConsentInfoBo);

                List<ConsentInfoLangBO> consentInfoLangBOS =
                    session
                        .createQuery("from ConsentInfoLangBO where consentInfoLangPK.id=:id")
                        .setInteger("id", consentInfoBo.getId())
                        .list();
                for (ConsentInfoLangBO consentInfoLangBO : consentInfoLangBOS) {
                  ConsentInfoLangBO consentInfoLangBO1 =
                      SerializationUtils.clone(consentInfoLangBO);
                  consentInfoLangBO1.getConsentInfoLangPK().setId(newConsentInfoBo.getId());
                  consentInfoLangBO1.setStudyId(studyDreaftBo.getId());
                  session.save(consentInfoLangBO1);
                }
              }
            }
          }

          if (consentBo != null) {
            // Comprehension test Start
            if (StringUtils.isNotEmpty(consentBo.getNeedComprehensionTest())
                && consentBo
                    .getNeedComprehensionTest()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = null;
              List<Integer> comprehensionIds = new ArrayList<>();
              List<ComprehensionTestResponseBo> comprehensionTestResponseList = null;
              query =
                  session.createQuery(
                      "From ComprehensionTestQuestionBo CTQBO where CTQBO.studyId=:id "
                          + "and CTQBO.active=1 order by CTQBO.sequenceNo asc");
              comprehensionTestQuestionList = query.setInteger("id", studyBo.getId()).list();
              if (comprehensionTestQuestionList != null
                  && !comprehensionTestQuestionList.isEmpty()) {
                for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
                    comprehensionTestQuestionList) {
                  comprehensionIds.add(comprehensionTestQuestionBo.getId());
                }
                comprehensionTestResponseList =
                    session
                        .createQuery(
                            "From ComprehensionTestResponseBo CTRBO "
                                + "where CTRBO.comprehensionTestQuestionId IN(:comprehensionIds) order by comprehensionTestQuestionId")
                        .setParameterList("comprehensionIds", comprehensionIds)
                        .list();
                for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
                    comprehensionTestQuestionList) {
                  ComprehensionTestQuestionBo newComprehensionTestQuestionBo =
                      SerializationUtils.clone(comprehensionTestQuestionBo);
                  newComprehensionTestQuestionBo.setStudyId(studyDreaftBo.getId());
                  newComprehensionTestQuestionBo.setId(null);
                  session.save(newComprehensionTestQuestionBo);

                  if (mlFlag) {
                    List<ComprehensionQuestionLangBO> comprehensionQuestionLangBOS =
                        session
                            .createQuery(
                                "from ComprehensionQuestionLangBO where comprehensionQuestionLangPK.id=:id")
                            .setInteger("id", comprehensionTestQuestionBo.getId())
                            .list();
                    for (ComprehensionQuestionLangBO comprehensionQuestionLangBO :
                        comprehensionQuestionLangBOS) {
                      ComprehensionQuestionLangBO comprehensionQuestionLangBO1 =
                          SerializationUtils.clone(comprehensionQuestionLangBO);
                      comprehensionQuestionLangBO1
                          .getComprehensionQuestionLangPK()
                          .setId(newComprehensionTestQuestionBo.getId());
                      comprehensionQuestionLangBO1.setStudyId(studyDreaftBo.getId());
                      session.save(comprehensionQuestionLangBO1);
                    }
                  }

                  if (comprehensionTestResponseList != null
                      && !comprehensionTestResponseList.isEmpty()) {
                    for (ComprehensionTestResponseBo comprehensionTestResponseBo :
                        comprehensionTestResponseList) {
                      if (comprehensionTestQuestionBo.getId().intValue()
                          == comprehensionTestResponseBo
                              .getComprehensionTestQuestionId()
                              .intValue()) {
                        ComprehensionTestResponseBo newComprehensionTestResponseBo =
                            SerializationUtils.clone(comprehensionTestResponseBo);
                        newComprehensionTestResponseBo.setId(null);
                        newComprehensionTestResponseBo.setComprehensionTestQuestionId(
                            newComprehensionTestQuestionBo.getId());
                        session.save(newComprehensionTestResponseBo);

                        if (mlFlag) {
                          List<ComprehensionResponseLangBo> comprehensionResponseLangBoList =
                              session
                                  .createQuery(
                                      "from ComprehensionResponseLangBo where comprehensionResponseLangPK.id=:id")
                                  .setInteger("id", comprehensionTestResponseBo.getId())
                                  .list();
                          for (ComprehensionResponseLangBo comprehensionResponseLangBo :
                              comprehensionResponseLangBoList) {
                            ComprehensionResponseLangBo comprehensionResponseLangBo1 =
                                SerializationUtils.clone(comprehensionResponseLangBo);
                            comprehensionResponseLangBo1
                                .getComprehensionResponseLangPK()
                                .setId(newComprehensionTestResponseBo.getId());
                            comprehensionResponseLangBo1.setQuestionId(
                                newComprehensionTestQuestionBo.getId());
                            session.save(comprehensionResponseLangBo1);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            // Comprehension test End
          }

          // updating the edited study to draft
          if (studyDreaftBo != null && studyDreaftBo.getId() != null) {
            studyBo.setVersion(0f);
            studyBo.setHasActivetaskDraft(0);
            studyBo.setHasQuestionnaireDraft(0);
            studyBo.setHasConsentDraft(0);
            studyBo.setHasStudyDraft(0);
            studyBo.setLive(0);
            session.update(studyBo);

            // Updating Notification and Resources
            session
                .createQuery(
                    "UPDATE NotificationBO set customStudyId=:customStudyId, "
                        + "appId =:appId where studyId=:id")
                .setString("customStudyId", studyBo.getCustomStudyId())
                .setString("appId", studyBo.getAppId())
                .setInteger("id", studyBo.getId())
                .executeUpdate();
            session
                .createQuery(
                    "UPDATE Checklist set customStudyId=:customStudyId where studyId=:studyId")
                .setString("customStudyId", studyBo.getCustomStudyId())
                .setInteger("studyId", studyBo.getId())
                .executeUpdate();
          }
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - studyDraftCreation() - ERROR ", e);
      e.printStackTrace();
    }
    logger.info("StudyDAOImpl - studyDraftCreation() - Ends");

    return message;
  }

  /**
   * This method is to update status of Study
   *
   * @author BTC
   * @param String , studyId
   * @param String , buttonText
   * @param sesObj , {@link SessionObject}
   * @return String, SUCCESS/FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String updateStudyActionOnAction(String studyId, String buttonText, SessionObject sesObj) {
    logger.info("StudyDAOImpl - updateStudyActionOnAction() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    StudyBo studyBo = null;
    List<Integer> objectList = null;
    String activitydetails = "";
    String activity = "";
    StudyBo liveStudy = null;
    StudyVersionBo studyVersionBo = null;
    NotificationBO notificationBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(studyId) && StringUtils.isNotEmpty(buttonText)) {
        studyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", Integer.parseInt(studyId))
                    .uniqueResult();
        if (studyBo != null) {
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PUBLISH)) {
            studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_PRE_PUBLISH);
            studyBo.setStudyPreActiveFlag(true);
            session.update(studyBo);
            message = FdahpStudyDesignerConstants.SUCCESS;
            activity = "Study Published as Upcoming.";
            activitydetails =
                "Study Published as Upcoming Study. (Study ID = "
                    + studyBo.getCustomStudyId()
                    + ", Status = Pre-launch(Published))";
            // notification sent to gateway
            notificationBO = new NotificationBO();
            notificationBO.setStudyId(studyBo.getId());
            notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
            if (StringUtils.isNotEmpty(studyBo.getAppId()))
              notificationBO.setAppId(studyBo.getAppId());
            notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_GT);
            notificationBO.setNotificationSubType(FdahpStudyDesignerConstants.STUDY_EVENT);
            notificationBO.setNotificationScheduleType(
                FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
            notificationBO.setNotificationStatus(false);
            notificationBO.setCreatedBy(sesObj.getUserId());

            if (StringUtils.equalsIgnoreCase(
                FdahpStudyDesignerConstants.STUDY_LANGUAGE_SPANISH, studyBo.getStudyLanguage())) {
              notificationBO.setNotificationText(
                  SpanishLangConstants.NOTIFICATION_UPCOMING_OR_ACTIVE_TEXT);
            } else {
              notificationBO.setNotificationText(
                  FdahpStudyDesignerConstants.NOTIFICATION_UPCOMING_OR_ACTIVE_TEXT);
            }

            notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
            notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
            notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            notificationBO.setNotificationDone(true);
            session.save(notificationBO);
          } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UNPUBLISH)) {
            studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_PRE_LAUNCH);
            studyBo.setStudyPreActiveFlag(false);
            session.update(studyBo);
            message = FdahpStudyDesignerConstants.SUCCESS;
            activity = "Study Unpublished.";
            activitydetails =
                "Study Unpublished as Upcoming Study.(Study ID = "
                    + studyBo.getCustomStudyId()
                    + ", Status = Unpublished)";
          } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)
              || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {
            studyBo.setStudyPreActiveFlag(false);
            studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_ACTIVE);
            studyBo.setStudylunchDate(FdahpStudyDesignerUtil.getCurrentDateTime());
            session.update(studyBo);
            // getting Questionnaries based on StudyId
            query =
                session.createQuery(
                    "select ab.id"
                        + " from QuestionnairesFrequenciesBo a,QuestionnaireBo ab"
                        + " where a.questionnairesId=ab.id and ab.studyId=:impValue"
                        + " and ab.frequency=:frequency and a.isLaunchStudy=1 and active=1"
                        + " and ab.shortTitle NOT IN(SELECT shortTitle from QuestionnaireBo WHERE active=1 AND live=1 AND customStudyId=:id)");
            query
                .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, Integer.parseInt(studyId))
                .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)
                .setString("id", studyBo.getCustomStudyId());
            objectList = query.list();
            if (objectList != null && !objectList.isEmpty()) {
              query =
                  session.createSQLQuery(
                      "update questionnaires ab SET ab.study_lifetime_start=:date"
                          + " where id IN(:objectList)");
              query
                  .setString("date", studyBo.getStudylunchDate())
                  .setParameterList("objectList", objectList)
                  .executeUpdate();
            }
            // getting activeTasks based on StudyId
            query =
                session.createQuery(
                    "select ab.id from ActiveTaskFrequencyBo a,ActiveTaskBo ab"
                        + " where a.activeTaskId=ab.id and ab.studyId=:impValue and ab.frequency=:frequency"
                        + " and a.isLaunchStudy=1 and active=1 and ab.shortTitle NOT IN(SELECT shortTitle"
                        + " from ActiveTaskBo WHERE active=1 AND live=1 AND customStudyId=:id)");
            query
                .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, Integer.parseInt(studyId))
                .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)
                .setString("id", studyBo.getCustomStudyId());
            objectList = query.list();
            if (objectList != null && !objectList.isEmpty()) {
              query =
                  session.createSQLQuery(
                      "update active_task ab SET ab.active_task_lifetime_start=:date where id IN(:objectList)");
              query
                  .setString("date", studyBo.getStudylunchDate())
                  .setParameterList("objectList", objectList)
                  .executeUpdate();
            }

            message = FdahpStudyDesignerConstants.SUCCESS;
            // StudyDraft version creation
            message = studyDraftCreation(studyBo, session);
            if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
              if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)) {
                // notification text --
                activity = "Study Launched.";
                activitydetails =
                    "Study successfully launched. (Study ID = "
                        + studyBo.getCustomStudyId()
                        + ", Status = Launched)";
                // notification sent to gateway
                notificationBO = new NotificationBO();
                notificationBO.setStudyId(studyBo.getId());
                notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
                if (StringUtils.isNotEmpty(studyBo.getAppId()))
                  notificationBO.setAppId(studyBo.getAppId());
                notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_GT);
                notificationBO.setNotificationSubType(FdahpStudyDesignerConstants.STUDY_EVENT);
                notificationBO.setNotificationScheduleType(
                    FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
                notificationBO.setNotificationStatus(false);
                notificationBO.setCreatedBy(sesObj.getUserId());

                if (StringUtils.equalsIgnoreCase(
                    FdahpStudyDesignerConstants.STUDY_LANGUAGE_SPANISH,
                    studyBo.getStudyLanguage())) {
                  notificationBO.setNotificationText(
                      SpanishLangConstants.NOTIFICATION_UPCOMING_OR_ACTIVE_TEXT);
                } else {
                  notificationBO.setNotificationText(
                      FdahpStudyDesignerConstants.NOTIFICATION_UPCOMING_OR_ACTIVE_TEXT);
                }

                notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
                notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
                notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                notificationBO.setNotificationDone(true);
                session.save(notificationBO);
              } else {
                // notification text --
                activity = "Study Updates Published.";
                activitydetails =
                    "Study version updated successfully. (Study ID = "
                        + studyBo.getCustomStudyId()
                        + ", Status = version updates)";
              }
              // Update resource startdate and time based on
              // customStudyId
              session
                  .createQuery(
                      "UPDATE NotificationBO set scheduleDate=:scheduleDate, scheduleTime =:scheduleTime, "
                          + "appId =:appId where customStudyId=:customStudyId and scheduleDate IS NULL and scheduleTime IS NULL "
                          + "and notificationType=:notificationType and notificationSubType=:notificationSubType and "
                          + "notificationScheduleType=:notificationScheduleType")
                  .setString("scheduleDate", FdahpStudyDesignerUtil.getCurrentDate())
                  .setString("scheduleTime", FdahpStudyDesignerUtil.getCurrentTime())
                  .setString("appId", studyBo.getAppId())
                  .setString("customStudyId", studyBo.getCustomStudyId())
                  .setString("notificationType", FdahpStudyDesignerConstants.NOTIFICATION_ST)
                  .setString(
                      "notificationSubType",
                      FdahpStudyDesignerConstants.NOTIFICATION_SUBTYPE_RESOURCE)
                  .setString(
                      "notificationScheduleType",
                      FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE)
                  .executeUpdate();

              // Update activity startdate and time based on
              // customStudyId
              session
                  .createQuery(
                      "UPDATE NotificationBO set scheduleDate=:scheduleDate,"
                          + " scheduleTime =:scheduleTime, appId =:appId where customStudyId=:customStudyId"
                          + " and scheduleDate IS NULL and scheduleTime IS NULL and notificationType=:notificationType"
                          + " and notificationSubType=:notificationSubType and notificationScheduleType=:notificationScheduleType")
                  .setString("scheduleDate", FdahpStudyDesignerUtil.getCurrentDate())
                  .setString("scheduleTime", FdahpStudyDesignerUtil.getCurrentTime())
                  .setString("appId", studyBo.getAppId())
                  .setString("customStudyId", studyBo.getCustomStudyId())
                  .setString("notificationType", FdahpStudyDesignerConstants.NOTIFICATION_ST)
                  .setString(
                      "notificationSubType",
                      FdahpStudyDesignerConstants.NOTIFICATION_SUBTYPE_ACTIVITY)
                  .setString(
                      "notificationScheduleType",
                      FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE)
                  .executeUpdate();

            } else {
              throw new IllegalStateException("Error in creating in Study draft");
            }
          } else {
            liveStudy =
                (StudyBo)
                    session
                        .getNamedQuery("getStudyLiveVersion")
                        .setString(
                            FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId())
                        .uniqueResult();
            if (liveStudy != null) {
              liveStudy.setStudyPreActiveFlag(false);
              query =
                  session
                      .getNamedQuery("getStudyByCustomStudyId")
                      .setMaxResults(1)
                      .setString(
                          FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, studyBo.getCustomStudyId());
              query.setMaxResults(1);
              studyVersionBo = (StudyVersionBo) query.uniqueResult();
              if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PAUSE)) {
                // notification text --
                if (studyVersionBo != null) {
                  activity = "Study paused.";
                  activitydetails =
                      "Study Paused. (Study ID = "
                          + studyBo.getCustomStudyId()
                          + ", Study Version = "
                          + studyVersionBo.getStudyVersion()
                          + ", Status = Paused)";
                }
                notificationBO = new NotificationBO();
                notificationBO.setStudyId(liveStudy.getId());
                notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
                if (StringUtils.isNotEmpty(studyBo.getAppId()))
                  notificationBO.setAppId(studyBo.getAppId());
                notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_ST);
                notificationBO.setNotificationSubType(FdahpStudyDesignerConstants.STUDY_EVENT);
                notificationBO.setNotificationScheduleType(
                    FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
                notificationBO.setNotificationStatus(false);
                notificationBO.setCreatedBy(sesObj.getUserId());

                if (StringUtils.equalsIgnoreCase(
                    FdahpStudyDesignerConstants.STUDY_LANGUAGE_SPANISH,
                    studyBo.getStudyLanguage())) {
                  notificationBO.setNotificationText(
                      SpanishLangConstants.NOTIFICATION_PAUSE_TEXT.replace(
                          "$customId", studyBo.getName()));
                } else {
                  notificationBO.setNotificationText(
                      FdahpStudyDesignerConstants.NOTIFICATION_PAUSE_TEXT.replace(
                          "$customId", studyBo.getName()));
                }

                notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
                notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
                notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                notificationBO.setNotificationDone(true);
                session.save(notificationBO);

                liveStudy.setStatus(FdahpStudyDesignerConstants.STUDY_PAUSED);
                studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_PAUSED);
                studyBo.setStudyPreActiveFlag(false);
              } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_RESUME)) {
                // notification text --
                activity = "Study Resumed.";
                activitydetails =
                    "Study Resumed. (Study ID = "
                        + studyBo.getCustomStudyId()
                        + ", Study Version = "
                        + studyVersionBo.getStudyVersion()
                        + ", Status = Active)";
                notificationBO = new NotificationBO();
                notificationBO.setStudyId(liveStudy.getId());
                notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
                if (StringUtils.isNotEmpty(studyBo.getAppId()))
                  notificationBO.setAppId(studyBo.getAppId());
                notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_ST);
                notificationBO.setNotificationSubType(FdahpStudyDesignerConstants.STUDY_EVENT);
                notificationBO.setNotificationScheduleType(
                    FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
                notificationBO.setNotificationStatus(false);
                notificationBO.setCreatedBy(sesObj.getUserId());

                if (StringUtils.equalsIgnoreCase(
                    FdahpStudyDesignerConstants.STUDY_LANGUAGE_SPANISH,
                    studyBo.getStudyLanguage())) {
                  notificationBO.setNotificationText(
                      SpanishLangConstants.NOTIFICATION_RESUME_TEXT.replace(
                          "$customId", studyBo.getName()));
                } else {
                  notificationBO.setNotificationText(
                      FdahpStudyDesignerConstants.NOTIFICATION_RESUME_TEXT.replace(
                          "$customId", studyBo.getName()));
                }

                notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
                notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
                notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                notificationBO.setNotificationDone(true);
                session.save(notificationBO);
                liveStudy.setStatus(FdahpStudyDesignerConstants.STUDY_ACTIVE);
                studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_ACTIVE);
                studyBo.setStudyPreActiveFlag(false);
              } else if (buttonText.equalsIgnoreCase(
                  FdahpStudyDesignerConstants.ACTION_DEACTIVATE)) {
                // notification text --
                liveStudy.setStatus(FdahpStudyDesignerConstants.STUDY_DEACTIVATED);
                activity = "Study Deactivated.";
                activitydetails =
                    "Study Deactivated. (Study ID = "
                        + studyBo.getCustomStudyId()
                        + ", Last Version = "
                        + studyVersionBo.getStudyVersion()
                        + " ,Status = Deactivated)";
                notificationBO = new NotificationBO();
                notificationBO.setStudyId(liveStudy.getId());
                notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
                if (StringUtils.isNotEmpty(studyBo.getAppId()))
                  notificationBO.setAppId(studyBo.getAppId());
                notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_ST);
                notificationBO.setNotificationSubType(FdahpStudyDesignerConstants.STUDY_EVENT);
                notificationBO.setNotificationScheduleType(
                    FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
                notificationBO.setNotificationStatus(false);
                notificationBO.setCreatedBy(sesObj.getUserId());

                if (StringUtils.equalsIgnoreCase(
                    FdahpStudyDesignerConstants.STUDY_LANGUAGE_SPANISH,
                    studyBo.getStudyLanguage())) {
                  notificationBO.setNotificationText(
                      SpanishLangConstants.NOTIFICATION_DEACTIVATE_TEXT.replace(
                          "$customId", studyBo.getName()));
                } else {
                  notificationBO.setNotificationText(
                      FdahpStudyDesignerConstants.NOTIFICATION_DEACTIVATE_TEXT.replace(
                          "$customId", studyBo.getName()));
                }

                notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
                notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
                notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                notificationBO.setNotificationDone(true);
                session.save(notificationBO);
                studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_DEACTIVATED);
                studyBo.setStudyPreActiveFlag(false);
                session.update(studyBo);
              }
              session.update(liveStudy);
              message = FdahpStudyDesignerConstants.SUCCESS;
            }
          }
          if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS))
            session.update(studyBo);
        }
        auditLogDAO.saveToAuditLog(
            session,
            transaction,
            sesObj,
            activity,
            activitydetails,
            "StudyDAOImpl - updateStudyActionOnAction");
      }
      transaction.commit();
    } catch (Exception e) {
      message = FdahpStudyDesignerConstants.FAILURE;
      transaction.rollback();
      logger.error("StudyDAOImpl - updateStudyActionOnAction() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - updateStudyActionOnAction() - Ends");
    return message;
  }

  @Override
  public String switchStudyToLiveMode(String studyId) {
    logger.info("StudyDAOImpl - switchStudyToLiveMode() - Starts");
    Transaction transaction = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      Query query = null;
      Session session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      StudyBo studyBo =
          (StudyBo)
              session
                  .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                  .setInteger("id", Integer.parseInt(studyId))
                  .uniqueResult();
      // reset all study and consent versions
      query =
          session.createQuery("DELETE FROM StudyVersionBo SVBO where SVBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of study to -1 and archive all
      query =
          session.createQuery(
              "UPDATE StudyBo SBO set SBO.live=2,SBO.version=-1 where SBO.customStudyId=:studyId AND SBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update study to Pre-Lunch stat
      studyBo.setStatus("Pre-launch");
      studyBo.setStudyPreActiveFlag(false);
      studyBo.setHasConsentDraft(1);
      studyBo.setHasStudyDraft(1);
      studyBo.setHasActivetaskDraft(1);
      studyBo.setHasQuestionnaireDraft(1);
      studyBo.setStudyMode(FdahpStudyDesignerConstants.STUDY_MODE_LIVE);
      studyBo.setSwitchVal(1);
      session.update(studyBo);

      StudySequenceBo studySequence =
          (StudySequenceBo)
              session
                  .getNamedQuery("getStudySequenceByStudyId")
                  .setInteger("studyId", Integer.parseInt(studyId))
                  .uniqueResult();

      // make basic info as not mark as completed
      studySequence.setBasicInfo(false);
      session.update(studySequence);

      // update all published versions of consentinfo to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ConsentInfoBo CBO set CBO.live=2,CBO.version=-1 where CBO.customStudyId=:studyId AND CBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of consent to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ConsentBo CBO set CBO.live=2,CBO.version=-1 where CBO.customStudyId=:studyId AND CBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of Questionnaire to -1 and archive all
      query =
          session.createQuery(
              "UPDATE QuestionnaireBo QBO set QBO.live=2,QBO.version=-1 where QBO.customStudyId=:studyId AND QBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update Questionnaire base version to 0
      query =
          session.createQuery(
              "UPDATE QuestionnaireBo QBO set QBO.version=0,QBO.isChange=1 where QBO.studyId=:studyId AND QBO.live=0");
      query.setInteger("studyId", studyBo.getId());
      query.executeUpdate();

      // reset all study activity versions
      query =
          session.createQuery(
              "DELETE FROM StudyActivityVersionBo SAVBO where SAVBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of activities to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ActiveTaskBo ABO set ABO.live=2,ABO.version=-1 where ABO.customStudyId=:studyId AND ABO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update activities base version to 0
      query =
          session.createQuery(
              "UPDATE ActiveTaskBo ABO set ABO.version=0 where ABO.studyId=:studyId AND ABO.live=0");
      query.setInteger("studyId", studyBo.getId());
      query.executeUpdate();

      // update all participant properties version to 0
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesBO PPBO set PPBO.version=0,PPBO.live=0 where PPBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all draft participant properties study version to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesDraftBO PPDBO set PPDBO.version=-1,PPDBO.studyVersion =-1 where PPDBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      query =
          session.createQuery(
              "SELECT notificationId from NotificationBO where studyId=:studyId AND notificationType='GT'");
      query.setInteger("studyId", Integer.parseInt(studyId));
      List<Integer> notificationIdList = query.list();

      if (notificationIdList != null && notificationIdList.size() > 0) {
        // clear previous sent notifications history
        query =
            session.createQuery(
                "DELETE from NotificationHistoryBO where notificationId IN(:idList)");
        query.setParameterList("idList", notificationIdList);
        query.executeUpdate();

        // clear previous notifications
        query = session.createQuery("DELETE from NotificationBO where notificationId IN(:idList)");
        query.setParameterList("idList", notificationIdList);
        query.executeUpdate();
      }

      message = FdahpStudyDesignerConstants.SUCCESS;
      transaction.commit();

    } catch (Exception e) {
      logger.error("StudyDAOImpl - switchStudyToLiveMode() - ERROR ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      message = FdahpStudyDesignerConstants.FAILURE;
    }
    logger.info("StudyDAOImpl - switchStudyToLiveMode() - Ends");
    return message;
  }

  @Override
  public void updateStudyId(StudyBo studyBo, String newStudyId, Session session) {
    logger.info("StudyDAOImpl - updateStudyId() - Starts");
    try {
      Query query = null;
      // reset all study and consent versions
      query =
          session.createQuery("DELETE FROM StudyVersionBo SVBO where SVBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of study to -1 and archive all
      query =
          session.createQuery(
              "UPDATE StudyBo SBO set SBO.live=2,SBO.version=-1 where SBO.customStudyId=:studyId AND SBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update study
      studyBo.setStatus("Pre-launch");
      studyBo.setStudyPreActiveFlag(false);
      studyBo.setHasConsentDraft(1);
      studyBo.setHasStudyDraft(1);
      studyBo.setHasActivetaskDraft(1);
      studyBo.setHasQuestionnaireDraft(1);
      session.update(studyBo);

      // update all published versions of consentinfo to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ConsentInfoBo CBO set CBO.live=2,CBO.version=-1 where CBO.customStudyId=:studyId AND CBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of consent to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ConsentBo CBO set CBO.live=2,CBO.version=-1 where CBO.customStudyId=:studyId AND CBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of Questionnaire to -1 and archive all
      query =
          session.createQuery(
              "UPDATE QuestionnaireBo QBO set QBO.live=2,QBO.version=-1 where QBO.customStudyId=:studyId AND QBO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update Questionnaire base version to 0
      query =
          session.createQuery(
              "UPDATE QuestionnaireBo QBO set QBO.version=0,QBO.isChange=1 where QBO.studyId=:studyId AND QBO.live=0");
      query.setInteger("studyId", studyBo.getId());
      query.executeUpdate();

      // reset all study activity versions
      query =
          session.createQuery(
              "DELETE FROM StudyActivityVersionBo SAVBO where SAVBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all published versions of activities to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ActiveTaskBo ABO set ABO.live=2,ABO.version=-1 where ABO.customStudyId=:studyId AND ABO.live IN(1,2)");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update activities base version to 0
      query =
          session.createQuery(
              "UPDATE ActiveTaskBo ABO set ABO.version=0 where ABO.studyId=:studyId AND ABO.live=0");
      query.setInteger("studyId", studyBo.getId());
      query.executeUpdate();

      // update all participant properties to new customStudyId and version to 1
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesBO PPBO set PPBO.version=1,PPBO.customStudyId=:newStudyId where PPBO.customStudyId=:studyId");
      query.setString("newStudyId", newStudyId);
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      // update all draft participant properties study version to -1 and archive all
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesDraftBO PPDBO set PPDBO.version=-1,PPDBO.studyVersion =-1 where PPDBO.customStudyId=:studyId");
      query.setString("studyId", studyBo.getCustomStudyId());
      query.executeUpdate();

      query =
          session.createQuery(
              "SELECT notificationId from NotificationBO where studyId=:studyId AND notificationType='GT'");
      query.setInteger("studyId", studyBo.getId());
      List<Integer> notificationIdList = query.list();

      if (notificationIdList != null && notificationIdList.size() > 0) {
        // clear previous sent notifications history
        query =
            session.createQuery(
                "DELETE from NotificationHistoryBO where notificationId IN(:idList)");
        query.setParameterList("idList", notificationIdList);
        query.executeUpdate();

        // clear previous notifications
        query = session.createQuery("DELETE from NotificationBO where notificationId IN(:idList)");
        query.setParameterList("idList", notificationIdList);
        query.executeUpdate();
      }

    } catch (Exception e) {
      logger.error("StudyDAOImpl - updateStudyId() - ERROR ", e);
      throw e;
    }
    logger.info("StudyDAOImpl - updateStudyId() - Ends");
  }

  @Override
  public void updateAppId(String customStudyId, String appId, String orgId, Session session) {
    logger.info("StudyDAOImpl - updateAppId() - Starts");
    try {
      Query query = null;

      // update appid and orgid in study table
      query =
          session.createQuery(
              "UPDATE StudyBo SBO set SBO.appId=:appId,SBO.orgId=:orgId where SBO.customStudyId=:studyId");
      query.setString("appId", appId);
      query.setString("orgId", orgId);
      query.setString("studyId", customStudyId);
      query.executeUpdate();

      // update appid and orgid in participant_properties table
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesBO PPBO set PPBO.appId=:appId,PPBO.orgId=:orgId where PPBO.customStudyId=:studyId");
      query.setString("appId", appId);
      query.setString("orgId", orgId);
      query.setString("studyId", customStudyId);
      query.executeUpdate();

      // update appid and orgid in participant_properties_draft table
      query =
          session.createQuery(
              "UPDATE ParticipantPropertiesDraftBO PPDBO set PPDBO.appId=:appId,PPDBO.orgId=:orgId where PPDBO.customStudyId=:studyId");
      query.setString("appId", appId);
      query.setString("orgId", orgId);
      query.setString("studyId", customStudyId);
      query.executeUpdate();

      // update appid in notification table
      query =
          session.createQuery(
              "UPDATE NotificationBO NBO set NBO.appId=:appId where NBO.customStudyId=:studyId");
      query.setString("appId", appId);
      query.setString("studyId", customStudyId);
      query.executeUpdate();

    } catch (Exception e) {
      logger.error("StudyDAOImpl - updateAppId() - ERROR ", e);
      throw e;
    }
    logger.info("StudyDAOImpl - updateAppId() - Ends");
  }

  /**
   * This method is validate the activity(Active task/Questionnaire) done or not
   *
   * @author BTC
   * @param String , studyId
   * @param String , action
   * @return String, {SUCCESS/FAILURE}
   */
  @SuppressWarnings("unchecked")
  @Override
  public String validateActivityComplete(String studyId, String action) {
    logger.info("StudyDAOImpl - validateActivityComplete() - Starts");
    Session session = null;
    boolean questionnarieFlag = true;
    boolean activeTaskEmpty = false;
    boolean questionnarieEmpty = false;
    boolean activeTaskFlag = true;
    List<ActiveTaskBo> completedactiveTasks = null;
    List<QuestionnaireBo> completedquestionnaires = null;
    StudySequenceBo studySequence = null;
    String message = FdahpStudyDesignerConstants.SUCCESS;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(action)) {
        // For checking active task or questionnaire done or not
        query =
            session
                .getNamedQuery("ActiveTaskBo.getActiveTasksByByStudyIdDone")
                .setInteger(FdahpStudyDesignerConstants.STUDY_ID, Integer.parseInt(studyId));
        completedactiveTasks = query.list();
        query =
            session
                .getNamedQuery("getQuestionariesByStudyIdDone")
                .setInteger(FdahpStudyDesignerConstants.STUDY_ID, Integer.parseInt(studyId));
        completedquestionnaires = query.list();
        studySequence =
            (StudySequenceBo)
                session
                    .getNamedQuery("getStudySequenceByStudyId")
                    .setInteger("studyId", Integer.parseInt(studyId))
                    .uniqueResult();
        if ((completedactiveTasks != null && !completedactiveTasks.isEmpty())) {
          for (ActiveTaskBo activeTaskBo : completedactiveTasks) {
            if (!activeTaskBo.isAction()) {
              activeTaskFlag = false;
              break;
            }
          }
        } else {
          activeTaskEmpty = true;
        }
        if (completedquestionnaires != null && !completedquestionnaires.isEmpty()) {
          for (QuestionnaireBo questionnaireBo : completedquestionnaires) {
            if (questionnaireBo.getStatus() != null && !questionnaireBo.getStatus()) {
              questionnarieFlag = false;
              break;
            }
          }
        } else {
          questionnarieEmpty = true;
        }
        // questionnarieFlag, activeTaskFlag will be true, then only
        // will allow to mark as complete
        if (action.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTIVITY_TYPE_QUESTIONNAIRE)) {
          if (!questionnarieFlag) {
            message = FdahpStudyDesignerConstants.MARK_AS_COMPLETE_DONE_ERROR_MSG;
          } else if (studySequence.isStudyExcActiveTask()
              && (questionnarieEmpty && activeTaskEmpty)) {
            message = FdahpStudyDesignerConstants.ACTIVEANDQUESSIONAIREEMPTY_ERROR_MSG;
          }
        } else {
          if (!activeTaskFlag) {
            message = FdahpStudyDesignerConstants.MARK_AS_COMPLETE_DONE_ERROR_MSG;
          } else if (studySequence.isStudyExcQuestionnaries()
              && (questionnarieEmpty && activeTaskEmpty)) {
            message = FdahpStudyDesignerConstants.ACTIVEANDQUESSIONAIREEMPTY_ERROR_MSG;
          }
        }
      } else {
        message = FdahpStudyDesignerConstants.ACTIVEANDQUESSIONAIREEMPTY_ERROR_MSG;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateActivityComplete() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateActivityComplete() - Ends");
    return message;
  }

  @Override
  public String validateParticipantPropertyComplete(String customStudyId) {
    logger.info("StudyDAOImpl - validateParticipantPropertyComplete() - Starts");
    Session session = null;
    boolean participantPropertiesFlag = true;
    List<ParticipantPropertiesBO> completedParticipantProperties = null;
    // StudySequenceBo studySequence = null;
    String message = FdahpStudyDesignerConstants.SUCCESS;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .createQuery(
                  "From ParticipantPropertiesBO PBO WHERE PBO.customStudyId =:customStudyId and PBO.active=1 order by PBO.createdDate DESC")
              .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId);
      completedParticipantProperties = query.list();
      /*
       * studySequence = (StudySequenceBo)
       * session.getNamedQuery("getStudySequenceByStudyId") .setInteger("studyId",
       * Integer.parseInt(studyId)).uniqueResult();
       */

      if (completedParticipantProperties != null && !completedParticipantProperties.isEmpty()) {
        for (ParticipantPropertiesBO participantPropertiesBO : completedParticipantProperties) {
          if (participantPropertiesBO.getCompleted() != null
              && !participantPropertiesBO.getCompleted()) {
            participantPropertiesFlag = false;
            break;
          }
        }
      } /*
         * else { participantPropertiesEmpty = true; }
         */
      // questionnarieFlag, activeTaskFlag will be true, then only
      // will allow to mark as complete
      if (!participantPropertiesFlag) {
        message = FdahpStudyDesignerConstants.MARK_AS_COMPLETE_DONE_ERROR_MSG;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateParticipantPropertyComplete() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateParticipantPropertyComplete() - Ends");
    return message;
  }

  /**
   * validate the date related to Activity and notification before do Publish (as Upcoming
   * Study)/Launch/publish update of the study
   *
   * @author BTC
   * @param studyBo , {@link StudyBo}
   * @param String , buttonText
   * @return {@link String}
   */
  @SuppressWarnings("unchecked")
  public String validateDateForStudyAction(StudyBo studyBo, String buttonText) {
    boolean resourceFlag = true;
    boolean resourceAnchorFlag = true;
    boolean activitiesFalg = true;
    boolean questionarriesFlag = true;
    boolean notificationFlag = true;
    List<DynamicBean> dynamicList = null;
    List<DynamicFrequencyBean> dynamicFrequencyList = null;
    List<NotificationBO> notificationBOs = null;
    List<ResourceBO> resourceBOList = null;
    String searchQuery = "";
    Session session = null;
    String message = FdahpStudyDesignerConstants.SUCCESS;
    boolean isExists = false;
    List<Integer> anchorDateTypeIds = null;
    BigInteger anchorCount = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();

      if (!buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {
        // getting based on custom start date resource list
        searchQuery =
            " FROM ResourceBO RBO WHERE RBO.studyId=:id AND RBO.status = 1 AND RBO.startDate IS NOT NULL ORDER BY RBO.createdOn DESC ";
        query = session.createQuery(searchQuery);
        resourceBOList = query.setInteger("id", studyBo.getId()).list();
        if (resourceBOList != null && !resourceBOList.isEmpty()) {
          for (ResourceBO resourceBO : resourceBOList) {
            boolean flag = false;
            String currentDate = FdahpStudyDesignerUtil.getCurrentDate();
            if (currentDate.equalsIgnoreCase(resourceBO.getStartDate())) {
              flag = true;
            } else {
              flag =
                  FdahpStudyDesignerUtil.compareDateWithCurrentDateResource(
                      resourceBO.getStartDate(), "yyyy-MM-dd");
            }
            if (!flag) {
              resourceFlag = false;
              break;
            }
          }
        }
      }
      if (!resourceFlag) {
        message = FdahpStudyDesignerConstants.RESOURCE_DATE_ERROR_MSG;
        return message;
      } else {
        // Ancordate Checking

        //				searchQuery = " FROM ResourceBO RBO WHERE RBO.studyId="
        //						+ studyBo.getId()
        //						+ " AND RBO.resourceType = 1 AND RBO.status = 1 ";
        //				query = session.createQuery(searchQuery);
        //				resourceBOList = query.list();
        // if resources are available, checking anchor date in
        // questionnaires/form questionnaires
        // else default making true
        //				if (resourceBOList != null && !resourceBOList.isEmpty()) {
        //					// checking anchordate in questionnaires
        //					searchQuery = "select count(q.use_anchor_date) from questions q,questionnaires_steps
        // qsq,questionnaires qq  where q.id=qsq.instruction_form_id and qsq.step_type='Question' "
        //							+ "and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.study_id in(select s.id
        // from studies s where s.custom_study_id='"
        //							+ studyBo.getCustomStudyId()
        //							+ "' and s.is_live=0) and qq.active=1 and q.use_anchor_date=1 and q.active=1;";
        //					BigInteger count = (BigInteger) session.createSQLQuery(
        //							searchQuery).uniqueResult();
        //					if (count.intValue() > 0) {
        //						isExists = true;
        //					} else {
        //						// checking anchordate in form questionnaires
        //						String subQuery = "select count(q.use_anchor_date) from questions q,form_mapping
        // fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and
        // f.form_id=fm.form_id and f.active=1 "
        //								+ "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and
        // qsf.questionnaires_id=qq.id and study_id in (select s.id from studies s where
        // s.custom_study_id='"
        //								+ studyBo.getCustomStudyId()
        //								+ "' and s.is_live=0) and q.use_anchor_date=1 and q.active=1";
        //						BigInteger subCount = (BigInteger) session
        //								.createSQLQuery(subQuery).uniqueResult();
        //						if (subCount != null && subCount.intValue() > 0) {
        //							isExists = true;
        //						}
        //					}
        //				} else {
        //					isExists = true;
        //				}
        /** anchor date test * */
        //				String subQuery = "";
        //				if(!studyBo.isEnrollmentdateAsAnchordate())
        //					subQuery = " AND
        // a.name!='"+FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE+"'";
        //				query = session
        //						.createSQLQuery("SELECT a.id FROM anchordate_type a WHERE a.study_id
        // ="+studyBo.getId()+" AND a.has_anchortype_draft=1"+subQuery);
        //				anchorDateTypeIds = query.list();
        //				if(anchorDateTypeIds!=null && !anchorDateTypeIds.isEmpty()) {
        //					int count = 0;
        //					Boolean isAnchorExist = false;
        //					for(Integer anchorId: anchorDateTypeIds) {
        //					   searchQuery = "select count(*) from questionnaires q where
        // q.schedule_type='"+FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE+"' and
        // q.anchor_date_id="+anchorId;
        //					   anchorCount = (BigInteger) session.createSQLQuery(searchQuery).uniqueResult();
        //						if (anchorCount == null || anchorCount.intValue()==0) {
        //							searchQuery = "select count(*) from active_task a where
        // a.schedule_type='"+FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE+"' and
        // a.anchor_date_id="+anchorId;
        //							anchorCount = (BigInteger) session.createSQLQuery(searchQuery).uniqueResult();
        //							if(anchorCount == null || anchorCount.intValue()==0) {
        //								searchQuery = "select count(*) from resources r where r.anchor_date_id="+anchorId;
        //								anchorCount = (BigInteger) session.createSQLQuery(searchQuery).uniqueResult();
        //								if(anchorCount != null && anchorCount.intValue()>0)
        //									isAnchorExist =true;
        //							}else {
        //								isAnchorExist = true;
        //							}
        //						}else {
        //							isAnchorExist = true;
        //						}
        //						if(isAnchorExist) {
        //							count++;
        //							isAnchorExist = false;
        //						}
        //					}
        //					if(anchorDateTypeIds.size() == count)
        //						isExists = true;
        //				}else {
        //					isExists = true;
        //				}
        //				if (!isExists)
        //					resourceAnchorFlag = false;
        //
        //			}
        // Anchor date Checking
        //			if (!resourceAnchorFlag) {
        //				//message = FdahpStudyDesignerConstants.RESOURCE_ANCHOR_ERROR_MSG;
        //				message = FdahpStudyDesignerConstants.ANCHOR_ERROR_MSG;
        //				return message;
        //			} else {
        // getting activeTasks based on StudyId
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicBean(a.frequencyDate, a.frequencyTime)"
                    + " from ActiveTaskFrequencyBo a,ActiveTaskBo ab where a.activeTaskId=ab.id"
                    + " and ab.active IS NOT NULL and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency=:frequency and a.isLaunchStudy=false and"
                    + " a.frequencyDate IS NOT NULL"
                    + " and a.frequencyTime IS NOT NULL"
                    + " and ab.shortTitle NOT IN(SELECT shortTitle from ActiveTaskBo WHERE active=1 AND live=1 AND customStudyId=:id)");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)
            .setString("id", studyBo.getCustomStudyId());
        dynamicList = query.list();
        if (dynamicList != null && !dynamicList.isEmpty()) {
          // checking active task which have scheduled for One time
          // expired or not
          for (DynamicBean obj : dynamicList) {
            if (obj.getDateTime() != null
                && !FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                    obj.getDateTime() + " " + obj.getTime(), "yyyy-MM-dd HH:mm:ss")) {
              activitiesFalg = false;
              break;
            }
          }
        }

        List<String> list =
            Arrays.asList(
                FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME,
                FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE);
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicBean(ab.activeTaskLifetimeStart, a.frequencyTime)"
                    + " from ActiveTaskFrequencyBo a,ActiveTaskBo ab where a.activeTaskId=ab.id"
                    + " and ab.active IS NOT NULL and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency not in (:freqList)"
                    + " and ab.activeTaskLifetimeStart IS NOT NULL and a.frequencyTime IS NOT NULL"
                    + " and ab.shortTitle NOT IN(SELECT shortTitle from ActiveTaskBo WHERE active=1 AND live=1 AND customStudyId=:id)");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setParameterList("freqList", list)
            .setString("id", studyBo.getCustomStudyId());
        dynamicList = query.list();
        if (dynamicList != null && !dynamicList.isEmpty()) {
          // checking active task which have scheduled not in One
          // time,Manually Schedule expired or not
          for (DynamicBean obj : dynamicList) {
            if (obj.getDateTime() != null
                && !FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                    obj.getDateTime() + " " + obj.getTime(), "yyyy-MM-dd HH:mm:ss")) {
              activitiesFalg = false;
              break;
            }
          }
        }

        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicFrequencyBean(a.frequencyStartDate, a.frequencyTime)"
                    + " from ActiveTaskCustomScheduleBo a,ActiveTaskBo ab where a.activeTaskId=ab.id"
                    + " and ab.active IS NOT NULL and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency=:frequency and a.frequencyStartDate IS NOT NULL and a.frequencyTime IS NOT NULL"
                    + " and a.used=false");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE);
        dynamicFrequencyList = query.list();
        if (dynamicFrequencyList != null && !dynamicFrequencyList.isEmpty()) {
          for (DynamicFrequencyBean obj : dynamicFrequencyList) {
            // checking active task which have scheduled for
            // "Manually Schedule" expired or not
            if (obj.getStartDate() != null && obj.getTime() != null) {
              String dateTime = obj.getStartDate() + " " + obj.getTime();
              if (!FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                  dateTime, "yyyy-MM-dd HH:mm:ss")) {
                activitiesFalg = false;
                break;
              }
            }
          }
        }
      }
      if (!activitiesFalg) {
        message = FdahpStudyDesignerConstants.ACTIVETASK_DATE_ERROR_MSG;
        return message;
      } else {
        // getting Questionnaires based on StudyId
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicBean(a.frequencyDate, a.frequencyTime)"
                    + " from QuestionnairesFrequenciesBo a,QuestionnaireBo ab"
                    + " where a.questionnairesId=ab.id and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency=:frequency and a.frequencyDate IS NOT NULL and a.frequencyTime IS NOT NULL"
                    + " and ab.shortTitle NOT IN(SELECT shortTitle from QuestionnaireBo WHERE active=1 AND live=1 AND customStudyId=:id)");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)
            .setString("id", studyBo.getCustomStudyId());
        dynamicList = query.list();
        if (dynamicList != null && !dynamicList.isEmpty()) {
          for (DynamicBean obj : dynamicList) {
            // checking Questionnaires which have scheduled for one
            // time expired or not
            if (obj.getDateTime() != null
                && !FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                    obj.getDateTime() + " " + obj.getTime(), "yyyy-MM-dd HH:mm:ss")) {
              questionarriesFlag = false;
              break;
            }
          }
        }

        List<String> freqList =
            Arrays.asList(
                FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME,
                FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE);
        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicBean(ab.studyLifetimeStart, a.frequencyTime)"
                    + " from QuestionnairesFrequenciesBo a,QuestionnaireBo ab"
                    + " where a.questionnairesId=ab.id and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency not in(:freqList)"
                    + " and ab.studyLifetimeStart IS NOT NULL and a.frequencyTime IS NOT NULL"
                    + " and ab.shortTitle NOT IN(SELECT shortTitle from QuestionnaireBo WHERE active=1 AND live=1 AND customStudyId=:id)");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setParameterList("freqList", freqList)
            .setString("id", studyBo.getCustomStudyId());
        dynamicList = query.list();
        if (dynamicList != null && !dynamicList.isEmpty()) {
          // checking Questionnaires which have scheduled not in one
          // time,manually schedule expired or not
          for (DynamicBean obj : dynamicList) {
            if (obj.getDateTime() != null
                && !FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                    obj.getDateTime() + " " + obj.getTime(), "yyyy-MM-dd HH:mm:ss")) {
              questionarriesFlag = false;
              break;
            }
          }
        }

        query =
            session.createQuery(
                "select new com.fdahpstudydesigner.bean.DynamicFrequencyBean(a.frequencyStartDate, a.frequencyTime)"
                    + " from QuestionnaireCustomScheduleBo a,QuestionnaireBo ab"
                    + " where a.questionnairesId=ab.id and ab.active=1 and ab.studyId=:impValue"
                    + " and ab.frequency=:frequency and a.frequencyStartDate IS NOT NULL and a.frequencyTime IS NOT NULL"
                    + " and a.used=false");
        query
            .setInteger(FdahpStudyDesignerConstants.IMP_VALUE, studyBo.getId())
            .setString("frequency", FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE);
        dynamicFrequencyList = query.list();
        if (dynamicFrequencyList != null && !dynamicFrequencyList.isEmpty()) {
          // checking Questionnaires which have scheduled
          // "manually schedule" expired or not
          for (DynamicFrequencyBean obj : dynamicFrequencyList) {
            if (obj.getStartDate() != null && obj.getTime() != null) {
              String dateTime = obj.getStartDate() + " " + obj.getTime();
              if (!FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                  dateTime, "yyyy-MM-dd HH:mm:ss")) {
                questionarriesFlag = false;
                break;
              }
            }
          }
        }
      }
      if (!questionarriesFlag) {
        message = FdahpStudyDesignerConstants.QUESTIONNARIES_ERROR_MSG;
        return message;
      } else {
        // getting based on start date notification list
        searchQuery =
            " FROM NotificationBO RBO WHERE RBO.studyId=:id"
                + " AND RBO.scheduleDate IS NOT NULL AND RBO.scheduleTime IS NOT NULL"
                + " AND RBO.notificationType='ST' AND RBO.notificationSubType='Announcement' AND RBO.notificationScheduleType='notImmediate' "
                + " AND RBO.notificationSent=0 AND RBO.notificationStatus=0 ";
        query = session.createQuery(searchQuery).setInteger("id", studyBo.getId());
        notificationBOs = query.list();
        if (notificationBOs != null && !notificationBOs.isEmpty()) {
          // checking notification expired or not
          for (NotificationBO notificationBO : notificationBOs) {
            if (!FdahpStudyDesignerUtil.compareDateWithCurrentDateTime(
                notificationBO.getScheduleDate() + " " + notificationBO.getScheduleTime(),
                "yyyy-MM-dd HH:mm:ss")) {
              notificationFlag = false;
              break;
            }
          }
        }
      }
      if (!notificationFlag) {
        message = FdahpStudyDesignerConstants.NOTIFICATION_ERROR_MSG;

        return message;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - updateStudyActionOnAction() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }

    return message;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String validateEligibilityTestKey(
      Integer eligibilityTestId, String shortTitle, Integer eligibilityId) {
    logger.info("StudyDAOImpl - getStudyVersionInfo() - Starts");
    Session session = null;
    List<EligibilityTestBo> eligibilityTestBos;
    String result = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .getNamedQuery("EligibilityTestBo.validateShortTitle")
              .setString("shortTitle", shortTitle)
              .setInteger("eligibilityTestId", eligibilityTestId)
              .setInteger("eligibilityId", eligibilityId);
      eligibilityTestBos = query.list();
      if (eligibilityTestBos.isEmpty()) {
        result = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyVersionInfo() - ERROR ", e);
    }
    logger.info("StudyDAOImpl - getStudyVersionInfo() - Ends");
    return result;
  }

  /**
   * validate the below items before do Publish (as Upcoming Study)/Launch/publish update of the
   * study
   *
   * @author BTC
   * @param String , studyId
   * @param String , buttonText
   * @return {@link String}
   */
  @Override
  public String validateStudyAction(String studyId, String buttonText) {
    logger.info("StudyDAOImpl - validateStudyAction() - Ends");
    String message = FdahpStudyDesignerConstants.SUCCESS;
    Session session = null;
    boolean enrollementFlag = false;
    boolean studyActivityFlag = false;
    StudySequenceBo studySequenceBo = null;
    StudyBo studyBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(buttonText) && StringUtils.isNotEmpty(studyId)) {

        studyBo =
            (StudyBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_LIST_BY_ID)
                    .setInteger("id", Integer.parseInt(studyId))
                    .uniqueResult();
        studySequenceBo =
            (StudySequenceBo)
                session
                    .getNamedQuery(FdahpStudyDesignerConstants.STUDY_SEQUENCE_BY_ID)
                    .setInteger(FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId())
                    .uniqueResult();

        if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)
            || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {

          // 1-all validation mark as completed
          if (studySequenceBo != null) {
            String studyActivity = "";
            studyActivity = getErrorBasedonAction(studySequenceBo);
            if (StringUtils.isNotEmpty(studyActivity)
                && !(FdahpStudyDesignerConstants.SUCCESS).equalsIgnoreCase(studyActivity))
              return studyActivity;
            else studyActivityFlag = true;
          }

          // validating mark as completed for multiple languages
          if (studyBo.getMultiLanguageFlag() != null && studyBo.getMultiLanguageFlag()) {
            message = validateCompletionStatusFromLangTables(Integer.parseInt(studyId), false);
            if (!FdahpStudyDesignerConstants.SUCCESS.equals(message)) {
              return message;
            }
          }

          // 2-enrollment validation
          if (studyActivityFlag
              && StringUtils.isNotEmpty(studyBo.getEnrollingParticipants())
              && studyBo
                  .getEnrollingParticipants()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
            enrollementFlag = true;
          }
          // 3-The study must have at least one 'activity' added. This
          // could be a questionnaire or active task.
          if (!enrollementFlag) {
            if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH))
              message = FdahpStudyDesignerConstants.LUNCH_ENROLLMENT_ERROR_MSG;
            else message = FdahpStudyDesignerConstants.PUBLISH_ENROLLMENT_ERROR_MSG;
            return message;
          } else {
            // 4-Date validation
            message = validateDateForStudyAction(studyBo, buttonText);
            return message;
          }
        } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PUBLISH)) {
          if (studySequenceBo != null) {
            if (!studySequenceBo.isBasicInfo()) {
              message = FdahpStudyDesignerConstants.BASICINFO_ERROR_MSG;
              return message;
            } else if (!studySequenceBo.isSettingAdmins()) {
              message = FdahpStudyDesignerConstants.SETTING_ERROR_MSG;
              return message;
            } else if (!studySequenceBo.isOverView()) {
              message = FdahpStudyDesignerConstants.OVERVIEW_ERROR_MSG;
              return message;
            } else if (!studySequenceBo.isConsentEduInfo()) {
              message = FdahpStudyDesignerConstants.CONSENTEDUINFO_ERROR_MSG;
              return message;
            } else if (!studySequenceBo.iseConsent()) {
              message = FdahpStudyDesignerConstants.ECONSENT_ERROR_MSG;
              return message;
            } else if (StringUtils.isNotEmpty(studyBo.getEnrollingParticipants())
                && studyBo
                    .getEnrollingParticipants()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              message = FdahpStudyDesignerConstants.PRE_PUBLISH_ENROLLMENT_ERROR_MSG;
              return message;
            }
          }
          // validating mark as completed for multiple languages
          if (studyBo.getMultiLanguageFlag() != null && studyBo.getMultiLanguageFlag()) {
            message = validateCompletionStatusFromLangTables(Integer.parseInt(studyId), true);
            if (!FdahpStudyDesignerConstants.SUCCESS.equals(message)) {
              return message;
            }
          }
        }
      } else {
        message = "Action is missing";
      }

    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateStudyAction() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateStudyAction() - Ends");
    return message;
  }

  @SuppressWarnings("unchecked")
  private String validateCompletionStatusFromLangTables(int studyId, boolean publishFlag) {
    logger.info("StudyDAOImpl - validateCompletionStatusFromLangTables() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      List<StudySequenceLangBO> langBOList =
          session
              .createQuery("from StudySequenceLangBO  where studySequenceLangPK.studyId=:id")
              .setInteger("id", studyId)
              .list();
      for (StudySequenceLangBO studySequenceLangBO : langBOList) {
        if (!studySequenceLangBO.isBasicInfo()) {
          message = FdahpStudyDesignerConstants.BASICINFO_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isSettingAdmins()) {
          message = FdahpStudyDesignerConstants.SETTING_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isOverView()) {
          message = FdahpStudyDesignerConstants.OVERVIEW_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isEligibility() && !publishFlag) {
          message = FdahpStudyDesignerConstants.ELIGIBILITY_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isConsentEduInfo()) {
          message = FdahpStudyDesignerConstants.CONSENTEDUINFO_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isComprehensionTest() && !publishFlag) {
          message = FdahpStudyDesignerConstants.COMPREHENSIONTEST_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.iseConsent() && !publishFlag) {
          message = FdahpStudyDesignerConstants.ECONSENT_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isStudyExcQuestionnaries() && !publishFlag) {
          message = FdahpStudyDesignerConstants.STUDYEXCQUESTIONNARIES_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isStudyExcActiveTask() && !publishFlag) {
          message = FdahpStudyDesignerConstants.STUDYEXCACTIVETASK_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.isMiscellaneousResources() && !publishFlag) {
          message = FdahpStudyDesignerConstants.RESOURCES_ERROR_MSG;
          return message;
        } else if (!studySequenceLangBO.getParticipantProperties() && !publishFlag) {
          message = FdahpStudyDesignerConstants.PARTICIPANT_PROPERTIES_ERROR_MSG;
          return message;
        }
      }
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateCompletionStatusFromLangTables() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateCompletionStatusFromLangTables() - Ends");
    return message;
  }

  /**
   * validating study Custom id
   *
   * @author BTC
   * @return boolean
   * @exception Exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean validateStudyId(String customStudyId) {
    logger.info("StudyDAOImpl - validateStudyId() - Starts");
    boolean flag = false;
    Session session = null;
    List<StudyBo> studyBos = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      studyBos =
          session
              .getNamedQuery("StudyBo.getStudyBycustomStudyId")
              .setString(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID, customStudyId)
              .list();
      if (studyBos != null && !studyBos.isEmpty()) flag = true;
    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateStudyId() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateStudyId() - Starts");
    return flag;
  }

  /**
   * View eligibility test question answer by eligibility id
   *
   * @author BTC
   * @param eligibilityId , Id of {@link EligibilityBo}
   * @return eligibilityTestList , {@link List} of {@link EligibilityTestBo} object
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<EligibilityTestBo> viewEligibilityTestQusAnsByEligibilityId(Integer eligibilityId) {
    logger.info("StudyDAOImpl - viewEligibilityTestQusAnsByEligibilityId - Starts");
    Session session = null;
    List<EligibilityTestBo> eligibilityTestList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      eligibilityTestList =
          session
              .getNamedQuery("EligibilityTestBo.findByEligibilityId")
              .setInteger("eligibilityId", eligibilityId)
              .list();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - viewEligibilityTestQusAnsByEligibilityId - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - viewEligibilityTestQusAnsByEligibilityId - Ends");
    return eligibilityTestList;
  }

  /**
   * View eligibility test question answer by id
   *
   * @author BTC
   * @param eligibilityTestId , Id of {@link EligibilityTestBo}
   * @return eligibilityTest , Object of {@link EligibilityTestBo}
   */
  @Override
  public EligibilityTestBo viewEligibilityTestQusAnsById(Integer eligibilityTestId) {
    logger.info("StudyDAOImpl - viewEligibilityTestQusAnsById - Starts");
    Session session = null;
    EligibilityTestBo eligibilityTest = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      eligibilityTest =
          (EligibilityTestBo)
              session
                  .getNamedQuery("EligibilityTestBo.findById")
                  .setInteger("eligibilityTestId", eligibilityTestId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - viewEligibilityTestQusAnsById - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - viewEligibilityTestQusAnsById - Ends");
    return eligibilityTest;
  }

  @Override
  public Boolean isAnchorDateExistForEnrollment(Integer studyId, String customStudyId) {
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollment - Starts");
    Session session = null;
    Boolean isExist = false;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      // checking in the questionnaire step anchor date is selected or not
      searchQuery =
          "select count(*) from questionnaires qr where qr.anchor_date_id IS NOT NULL "
              + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name and "
              + "t.custom_study_id=:id) and qr.custom_study_id=:id and qr.active=1";
      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                  .setString("id", customStudyId)
                  .uniqueResult();
      if (count.intValue() > 0) {
        isExist = true;
      } else {
        // activetask target anchordate
        searchQuery =
            "select count(*) from active_task qr where qr.anchor_date_id IS NOT NULL "
                + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name "
                + "and t.custom_study_id=:id) and qr.custom_study_id=:id and qr.active=1";
        BigInteger subCount =
            (BigInteger)
                session
                    .createSQLQuery(searchQuery)
                    .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                    .setString("id", customStudyId)
                    .uniqueResult();
        if (subCount != null && subCount.intValue() > 0) {
          isExist = true;
        } else {
          searchQuery =
              "select count(*) from resources qr where qr.anchor_date_id IS NOT NULL "
                  + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name and "
                  + "t.custom_study_id=:id) and qr.custom_study_id=:id and qr.status=1";
          BigInteger sub1Count =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                      .setString("id", customStudyId)
                      .uniqueResult();
          if (sub1Count != null && sub1Count.intValue() > 0) {
            isExist = true;
          }
        }
      }
    } catch (Exception e) {
      logger.error("EXCEPTION : ", e);
    }
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollment - Ends");
    return isExist;
  }

  @Override
  public Boolean isAnchorDateExistForEnrollmentDraftStudy(Integer studyId, String customStudyId) {
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Starts");
    Session session = null;
    boolean isExist = false;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      // checking in the questionnaire step anchor date is selected or not
      searchQuery =
          "select count(*) from questionnaires qr where qr.anchor_date_id IS NOT NULL "
              + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name and t.custom_study_id=:customStudyId) "
              + "and qr.study_id=:id and qr.active=1";
      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                  .setString("customStudyId", customStudyId)
                  .setInteger("id", studyId)
                  .uniqueResult();
      if (count.intValue() > 0) {
        isExist = true;
      } else {
        // activetask target anchordate
        searchQuery =
            "select count(*) from active_task qr where qr.anchor_date_id IS NOT NULL "
                + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name and t.custom_study_id=:customStudyId) "
                + "and qr.study_id=:id and qr.active=1";
        BigInteger subCount =
            (BigInteger)
                session
                    .createSQLQuery(searchQuery)
                    .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                    .setString("customStudyId", customStudyId)
                    .setInteger("id", studyId)
                    .uniqueResult();
        if (subCount != null && subCount.intValue() > 0) {
          isExist = true;
        } else {
          searchQuery =
              "select count(*) from resources qr where qr.anchor_date_id IS NOT NULL "
                  + "and qr.anchor_date_id=(select t.id from anchordate_type t where t.name=:name and t.custom_study_id=:customStudyId) "
                  + "and qr.study_id=:id and qr.status=1";
          BigInteger sub1Count =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                      .setString("customStudyId", customStudyId)
                      .setInteger("id", studyId)
                      .uniqueResult();
          if (sub1Count != null && sub1Count.intValue() > 0) {
            isExist = true;
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - ERROR ", e);
    }
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Ends");
    return isExist;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String updateAnchordateForEnrollmentDate(
      StudyBo oldStudy, StudyBo updatedStudy, Session session, Transaction transaction) {
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Starts");
    Boolean isAnchorUsed = false;
    String searchQuery = "";
    String message = FdahpStudyDesignerConstants.FAILURE;
    List<Integer> anchorIds = new ArrayList<Integer>();
    List<Integer> anchorExistIds = new ArrayList<Integer>();

    try {
      if (oldStudy.isEnrollmentdateAsAnchordate() && !updatedStudy.isEnrollmentdateAsAnchordate()) {

        anchorIds =
            session
                .createSQLQuery(
                    "select t.id from anchordate_type t where t.name=:name and t.custom_study_id=:id")
                .setString("name", FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE)
                .setString("id", oldStudy.getCustomStudyId())
                .list();
        if (!anchorIds.isEmpty() && anchorIds.size() > 0) {

          searchQuery =
              "select q.id from questionnaires q where q.schedule_type=:schedule_type and q.anchor_date_id in(:anchorIds)";
          anchorExistIds =
              session
                  .createSQLQuery(searchQuery)
                  .setParameterList("anchorIds", anchorIds)
                  .setString("schedule_type", FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)
                  .list();
          if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
            isAnchorUsed = true;
          } else {
            searchQuery =
                "select q.id from active_task q where q.schedule_type=:schedule_type and q.anchor_date_id in(:anchorIds)";
            anchorExistIds =
                session
                    .createSQLQuery(searchQuery)
                    .setParameterList("anchorIds", anchorIds)
                    .setString("schedule_type", FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)
                    .list();
            if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
              isAnchorUsed = true;
            } else {
              searchQuery = "select q.id from resources q where q.anchor_date_id in(:anchorIds)";
              anchorExistIds =
                  session
                      .createSQLQuery(searchQuery)
                      .setParameterList("anchorIds", anchorIds)
                      .list();
              if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
                isAnchorUsed = true;
              }
            }
          }
          if (isAnchorUsed) {

            StudySequenceBo studySequence =
                (StudySequenceBo)
                    session
                        .getNamedQuery("getStudySequenceByStudyId")
                        .setInteger("studyId", oldStudy.getId())
                        .uniqueResult();
            if (studySequence != null) {
              int count1 =
                  session
                      .createSQLQuery(
                          "update questionnaires set status=0,anchor_date_id=null,"
                              + "modified_by=:modified_by, modified_date=:modified_date "
                              + "where active=1 and anchor_date_id in(:anchorIds)")
                      .setInteger("modified_by", updatedStudy.getModifiedBy())
                      .setString("modified_date", FdahpStudyDesignerUtil.getCurrentDateTime())
                      .setParameterList("anchorIds", anchorIds)
                      .executeUpdate();
              if (count1 > 0) {
                studySequence.setStudyExcQuestionnaries(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    updatedStudy.getModifiedBy(),
                    FdahpStudyDesignerConstants.DRAFT_QUESTIONNAIRE,
                    oldStudy.getId());
              }
              int count2 =
                  session
                      .createSQLQuery(
                          "update active_task set action=0 ,anchor_date_id=null, modified_by=:modified_by, "
                              + "modified_date=:modified_date where active=1 and anchor_date_id in(:anchorIds)")
                      .setInteger("modified_by", updatedStudy.getModifiedBy())
                      .setString("modified_date", FdahpStudyDesignerUtil.getCurrentDateTime())
                      .setParameterList("anchorIds", anchorIds)
                      .executeUpdate();
              if (count2 > 0) {
                studySequence.setStudyExcActiveTask(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    updatedStudy.getModifiedBy(),
                    FdahpStudyDesignerConstants.DRAFT_ACTIVETASK,
                    oldStudy.getId());
              }
              int count3 =
                  session
                      .createSQLQuery(
                          "update resources set action=0,anchor_date_id=null "
                              + "where status=1 and anchor_date_id in(:anchorIds)")
                      .setParameterList("anchorIds", anchorIds)
                      .executeUpdate();

              if (count3 > 0) {
                studySequence.setMiscellaneousResources(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    updatedStudy.getModifiedBy(),
                    FdahpStudyDesignerConstants.DRAFT_STUDY,
                    oldStudy.getId());
              }
              session.saveOrUpdate(studySequence);
              message = FdahpStudyDesignerConstants.SUCCESS;
            }
          }
        }
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - ERROR ", e);
    }
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Ends");
    return message;
  }

  public void saveSelectedLanguages(
      String newLanguages, StudyBo studyBo, int userId, Session session, Transaction transaction) {
    logger.info("StudyDAOImpl - saveSelectedLanguages - Starts");
    try {
      String[] langArray = newLanguages.split(",");
      if (langArray.length > 0) {
        for (String lang : langArray) {
          if (FdahpStudyDesignerUtil.isNotEmpty(lang)) {
            StudyLanguageBO studyLanguageBO = new StudyLanguageBO();
            studyLanguageBO.setStudyLanguagePK(new StudyLanguagePK(studyBo.getId(), lang));
            studyLanguageBO.setCreatedBy(userId);
            studyLanguageBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            session.saveOrUpdate(studyLanguageBO);
          }
        }
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveSelectedLanguages - ERROR ", e);
    }
    logger.info("StudyDAOImpl - saveSelectedLanguages - ends");
  }

  public void deleteExistingLanguages(
      int studyId, String deletedLanguage, Session session, Transaction transaction) {
    logger.info("StudyDAOImpl - saveSelectedLanguages - Starts");
    try {
      String[] langArray = deletedLanguage.split(",");
      if (langArray.length > 0) {
        for (String lang : langArray) {
          if (FdahpStudyDesignerUtil.isNotEmpty(lang)) {
            StudyLanguageBO studyLanguageBO =
                (StudyLanguageBO)
                    session
                        .createQuery(
                            "from StudyLanguageBO where studyLanguagePK.langCode=:language and studyLanguagePK.study_id=:studyId")
                        .setString("language", lang)
                        .setInteger("studyId", studyId)
                        .uniqueResult();
            if (studyLanguageBO != null) {
              session
                  .createSQLQuery(
                      "DELETE FROM studies_lang WHERE lang_code=:language and study_id=:studyId")
                  .setString("language", lang)
                  .setInteger("studyId", studyId)
                  .executeUpdate();
            }
          }
        }
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveSelectedLanguages - ERROR ", e);
    }
    logger.info("StudyDAOImpl - saveSelectedLanguages - ends");
  }

  /**
   * validating study app id
   *
   * @author BTC
   * @return boolean
   * @exception Exception
   */
  @SuppressWarnings("unchecked")
  @Override
  public boolean validateAppId(String customStudyId, String appId, String studyType) {
    logger.info("StudyDAOImpl - validateAppId() - Starts");
    boolean flag = false;
    Session session = null;
    List<StudyBo> studyBos = null;
    String searchQuery = "";
    String subQry = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (!studyType.isEmpty() && !appId.isEmpty()) {
        if (StringUtils.isNotEmpty(customStudyId)) {
          subQry = " and customStudyId!=:customStudyId";
        }
        if (studyType.equalsIgnoreCase(FdahpStudyDesignerConstants.STUDY_TYPE_GT)) {
          searchQuery =
              "From StudyBo WHERE appId=:appId and type='"
                  + FdahpStudyDesignerConstants.STUDY_TYPE_SD
                  + "'"
                  + subQry;
        } else {
          searchQuery = "From StudyBo WHERE appId=:appId" + subQry;
        }
        studyBos =
            session
                .createQuery(searchQuery)
                .setString("customStudyId", customStudyId)
                .setString("appId", appId)
                .list();
      }

      if (studyBos != null && !studyBos.isEmpty()) flag = true;
    } catch (Exception e) {
      logger.error("StudyDAOImpl - validateAppId() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - validateAppId() - Ends");
    return flag;
  }

  @Override
  public StudyPermissionBO getStudyPermissionBO(int studyId, int userId) {
    logger.info("StudyDAOImpl - getStudyPermissionBO() - Starts");
    Session session = null;
    StudyPermissionBO studyPermissionBO = null;
    String searchQuery = "";
    Query query = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery = "From StudyPermissionBO WHERE studyId=:studyId and userId=:userId";
      query =
          session
              .createQuery(searchQuery)
              .setInteger("studyId", studyId)
              .setInteger("userId", userId);
      studyPermissionBO = (StudyPermissionBO) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyPermissionBO() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyPermissionBO() - Ends");
    return studyPermissionBO;
  }

  /**
   * get study details
   *
   * @author BTC
   * @param studyId
   * @param language
   * @return studyBo, {@link StudyLanguageBO}
   */
  @Override
  public StudyLanguageBO getStudyLanguageById(int studyId, String language) {
    logger.info("StudyDAOImpl - getStudyLanguageById() - Starts");
    Session session = null;
    StudyLanguageBO studyBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        studyBo =
            (StudyLanguageBO)
                session
                    .createQuery(
                        "from StudyLanguageBO where studyLanguagePK.langCode=:language and studyLanguagePK.study_id=:studyId")
                    .setString("language", language)
                    .setInteger("studyId", studyId)
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyLanguageById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyLanguageById() - Ends");
    return studyBo;
  }

  @Override
  public ConsentInfoLangBO getConsentLanguageDataById(int id, String language) {
    logger.info("StudyDAOImpl - getConsentLanguageDataById() - Starts");
    Session session = null;
    ConsentInfoLangBO consentInfoLangBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (id != 0) {
        consentInfoLangBO =
            (ConsentInfoLangBO)
                session
                    .createQuery(
                        "from ConsentInfoLangBO where active=true and consentInfoLangPK.langCode=:language and consentInfoLangPK.id=:id")
                    .setString("language", language)
                    .setInteger("id", id)
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentLanguageDataById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getConsentLanguageDataById() - Ends");
    return consentInfoLangBO;
  }

  @Override
  public void saveOrUpdateConsentInfoLanguageData(ConsentInfoLangBO consentInfoLangBO) {
    logger.info("StudyDAOImpl - saveConsentInfoLanguageData() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(consentInfoLangBO);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveConsentInfoLanguageData() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveConsentInfoLanguageData() - Ends");
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ConsentInfoLangBO> getConsentLangInfoByStudyId(int studyId, String language) {
    logger.info("StudyDAOImpl - getConsentLangInfoByStudyId() - Starts");
    Session session = null;
    List<ConsentInfoLangBO> dataList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        dataList =
            session
                .createQuery(
                    "from ConsentInfoLangBO cil where cil.active=true and cil.consentInfoLangPK.langCode=:language and studyId=:id")
                .setString("language", language)
                .setInteger("id", studyId)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getConsentLangInfoByStudyId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getConsentLangInfoByStudyId() - Ends");
    return dataList;
  }

  /**
   * Study Consent have the 0 or more comprehension test question.Each question contains the
   * question text and answers and make correct response to the question as being ANY or ALL of the
   * 'correct' answer options.
   *
   * @author BTC
   * @param questionId {@link ComprehensionTestQuestionBo}
   * @return {@link ComprehensionQuestionLangBO}
   */
  @SuppressWarnings("unchecked")
  @Override
  public ComprehensionQuestionLangBO getComprehensionQuestionLangById(
      int questionId, String language) {
    logger.info("StudyDAOImpl - getComprehensionQuestionLangById() - Starts");
    ComprehensionQuestionLangBO comprehensionQuestionLangBO = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      Criteria criteria = session.createCriteria(ComprehensionQuestionLangBO.class);
      criteria
          .add(Restrictions.eq("comprehensionQuestionLangPK.id", questionId))
          .add(Restrictions.eq("comprehensionQuestionLangPK.langCode", language))
          .add(Restrictions.eq("active", true));
      criteria.setFetchMode("comprehensionResponseLangBoList", FetchMode.JOIN);
      comprehensionQuestionLangBO = (ComprehensionQuestionLangBO) criteria.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getComprehensionQuestionLangById() - Error", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getComprehensionQuestionLangById() - Ends");
    return comprehensionQuestionLangBO;
  }

  @Override
  public void saveOrUpdateComprehensionQuestionLanguageData(
      ComprehensionQuestionLangBO questionLangBO, boolean deleteExisting) {
    logger.info("StudyDAOImpl - saveOrUpdateComprehensionQuestionLanguageData() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (deleteExisting) {
        session
            .createSQLQuery(
                "delete from comprehension_test_response_lang where question_id= :question_id")
            .setInteger("question_id", questionLangBO.getComprehensionQuestionLangPK().getId())
            .executeUpdate();
      } else {
        session.saveOrUpdate(questionLangBO);
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateComprehensionQuestionLanguageData() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateComprehensionQuestionLanguageData() - Ends");
  }

  @Override
  public void saveOrUpdateObject(Object object) {
    logger.info("StudyDAOImpl - saveOrUpdateObject() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(object);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateObject() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateObject() - Ends");
  }

  public String saveOrUpdateStudyEligibiltyForOtherLanguages(
      EligibilityBo eligibilityBo, StudyLanguageBO studyLanguageBO, String language) {
    logger.info("StudyDAOImpl - saveOrUpdateStudyEligibiltyForOtherLanguages() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      studyLanguageBO.setInstructionalText(eligibilityBo.getInstructionalText());
      session.update(studyLanguageBO);
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - saveOrUpdateStudyEligibiltyForOtherLanguages() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveConsentInfoLanguageData() - Ends");
    return message;
  }

  @Override
  public Integer saveOrUpdateStudyEligibiltyTestQusForOtherLanguages(
      EligibilityTestLangBo eligibilityTestLangBo) {
    logger.info("StudyDAOImpl - saveOrUpdateStudyEligibiltyTestQusForOtherLanguages() - Starts");
    Session session = null;
    Integer id = 0;
    EligibilityTestLangBo saveEligibilityTestBo;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(eligibilityTestLangBo);
      id = eligibilityTestLangBo.getEligibilityTestLangPK().getId();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error(
          "StudyDAOImpl - saveOrUpdateStudyEligibiltyTestQusForOtherLanguages() - ERROR", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateStudyEligibiltyTestQusForOtherLanguages() - Ends");
    return id;
  }

  @Override
  public EligibilityTestLangBo getEligibilityTestLanguageDataById(
      int eligibilityTestId, String language) {
    logger.info("StudyDAOImpl - getEligibilityTestLanguageDataById() - Starts");
    Session session = null;
    EligibilityTestLangBo eligibilityTestLangBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();

      eligibilityTestLangBo =
          (EligibilityTestLangBo)
              session
                  .createQuery(
                      "SELECT ETB FROM EligibilityTestLangBo ETB WHERE ETB.active = true AND ETB.eligibilityTestLangPK.id=:eligibilityTestId AND ETB.eligibilityTestLangPK.langCode=:language ORDER BY ETB.sequenceNo")
                  .setString("language", language)
                  .setInteger("eligibilityTestId", eligibilityTestId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getEligibilityTestLanguageDataById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getEligibilityTestLanguageDataById() - Ends");
    return eligibilityTestLangBo;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<EligibilityTestLangBo> getEligibilityTestLangByStudyId(int studyId, String language) {
    logger.info("StudyDAOImpl - getEligibilityTestLangByEligibilityId() - Starts");
    Session session = null;
    List<EligibilityTestLangBo> dataList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        dataList =
            session
                .createQuery(
                    "from EligibilityTestLangBo etlb where etlb.active=true and etlb.eligibilityTestLangPK.langCode=:language and etlb.studyId=:id")
                .setString("language", language)
                .setInteger("id", studyId)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getEligibilityTestLangByEligibilityId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getEligibilityTestLangByEligibilityId() - Ends");
    return dataList;
  }

  @Override
  public String saveOrUpdateOverviewLanguageStudyPages(
      StudyPageBean studyPageBean, SessionObject sesObj, String language) {
    logger.info("StudyDAOImpl - saveOrUpdateOverviewLanguageStudyPages() - Starts");
    Session session = null;
    StudyLanguageBO studyLanguageBO = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int titleLength = 0;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(studyPageBean.getStudyId())) {
        studyLanguageBO =
            (StudyLanguageBO)
                session
                    .createQuery(
                        "From StudyLanguageBO SBO WHERE SBO.studyLanguagePK.study_id=:study_id AND SBO.studyLanguagePK.langCode=:lang_code")
                    .setInteger("study_id", Integer.parseInt(studyPageBean.getStudyId()))
                    .setString("lang_code", language)
                    .setMaxResults(1)
                    .uniqueResult();
        if (studyLanguageBO != null) {
          studyLanguageBO.setMediaLink(studyPageBean.getMediaLink());
          session.update(studyLanguageBO);
        }

        titleLength = studyPageBean.getTitle().length;
        for (int i = 0; i < titleLength; i++) {
          StudyPageLanguageBO studyPageLanguageBO = null;
          if (FdahpStudyDesignerUtil.isNotEmpty(studyPageBean.getPageId()[i]))
            studyPageLanguageBO =
                (StudyPageLanguageBO)
                    session
                        .createQuery(
                            "from StudyPageLanguageBO SPB where SPB.studyPageLanguagePK.pageId=:page_id "
                                + "and SPB.studyPageLanguagePK.langCode =:lang_code")
                        .setString("page_id", studyPageBean.getPageId()[i])
                        .setString("lang_code", language)
                        .uniqueResult();

          if (studyPageLanguageBO == null) studyPageLanguageBO = new StudyPageLanguageBO();
          if (FdahpStudyDesignerUtil.isNotEmpty(studyPageBean.getPageId()[i])) {
            studyPageLanguageBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            studyPageLanguageBO.setModifiedBy(studyPageBean.getUserId());
          } else {
            studyPageLanguageBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            studyPageLanguageBO.setCreatedBy(studyPageBean.getUserId());
          }
          studyPageLanguageBO.setStudyPageLanguagePK(
              new StudyPageLanguagePK(Integer.parseInt(studyPageBean.getPageId()[i]), language));
          studyPageLanguageBO.setStudyId(
              FdahpStudyDesignerUtil.isEmpty(studyPageBean.getStudyId())
                  ? 0
                  : Integer.parseInt(studyPageBean.getStudyId()));
          studyPageLanguageBO.setTitle(
              FdahpStudyDesignerUtil.isEmpty(studyPageBean.getTitle()[i])
                  ? null
                  : studyPageBean.getTitle()[i]);
          studyPageLanguageBO.setDescription(
              FdahpStudyDesignerUtil.isEmpty(studyPageBean.getDescription()[i])
                  ? null
                  : studyPageBean.getDescription()[i]);
          session.saveOrUpdate(studyPageLanguageBO);
        }
      }
      transaction.commit();
      message = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      logger.error("StudyDAOImpl - saveOrUpdateOverviewLanguageStudyPages() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - saveOrUpdateOverviewLanguageStudyPages() - Ends");
    return message;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<StudyPageLanguageBO> getOverviewStudyPagesLangDataById(int studyId, String language) {
    logger.info("StudyDAOImpl - getOverviewStudyPagesLangDataById() - Starts");
    Session session = null;
    List<StudyPageLanguageBO> studyPageLanguageBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        studyPageLanguageBO =
            session
                .createQuery(
                    "from StudyPageLanguageBO where studyId= :studyId and studyPageLanguagePK.langCode=:lang")
                .setInteger("studyId", studyId)
                .setString("lang", language)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getOverviewStudyPagesLangDataById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getOverviewStudyPagesLangDataById() - Ends");
    return studyPageLanguageBO;
  }

  @Override
  public StudyPageLanguageBO getStudyPageLanguageById(int pageId, String language) {
    logger.info("StudyDAOImpl - getStudyPageLanguageById() - Starts");
    Session session = null;
    StudyPageLanguageBO pageLanguageBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (pageId != 0) {
        pageLanguageBO =
            (StudyPageLanguageBO)
                session
                    .createQuery(
                        "from StudyPageLanguageBO where studyPageLanguagePK.langCode=:language and studyPageLanguagePK.pageId=:pageId")
                    .setString("language", language)
                    .setInteger("pageId", pageId)
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudyPageLanguageById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudyPageLanguageById() - Ends");
    return pageLanguageBO;
  }

  @Override
  public StudySequenceLangBO getStudySequenceLangBO(int studyId, String language) {
    logger.info("StudyDAOImpl - getStudySequenceLangBO() - Starts");
    Session session = null;
    StudySequenceLangBO studySequenceLangBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        studySequenceLangBO =
            (StudySequenceLangBO)
                session
                    .createQuery(
                        "from StudySequenceLangBO where studySequenceLangPK.langCode=:language and studySequenceLangPK.studyId=:id")
                    .setString("language", language)
                    .setInteger("id", studyId)
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudySequenceLangBO() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudySequenceLangBO() - Ends");
    return studySequenceLangBO;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<StudySequenceLangBO> getStudySequenceByStudyId(int studyId) {
    logger.info("StudyDAOImpl - getStudySequenceByStudyId() - Starts");
    Session session = null;
    List<StudySequenceLangBO> studySequenceLangBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        studySequenceLangBO =
            session
                .createQuery("from StudySequenceLangBO where studySequenceLangPK.studyId=:id")
                .setInteger("id", studyId)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStudySequenceByStudyId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStudySequenceByStudyId() - Ends");
    return studySequenceLangBO;
  }

  @Override
  public ResourcesLangBO getResourceLangBO(int id, String language) {
    logger.info("StudyDAOImpl - getResourceLangBO() - Starts");
    Session session = null;
    ResourcesLangBO resourcesLangBO = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (id != 0) {
        resourcesLangBO =
            (ResourcesLangBO)
                session
                    .createQuery(
                        "from ResourcesLangBO where resourcesLangPK.langCode=:language and resourcesLangPK.id=:id")
                    .setString("language", language)
                    .setInteger("id", id)
                    .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResourceLangBO() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getResourceLangBO() - Ends");
    return resourcesLangBO;
  }

  @Override
  @SuppressWarnings("unchecked")
  public String deleteAllLanguageData(int studyId, String language) {
    logger.info("StudyDAOImpl - deleteAllLanguageData() - Starts");
    Session session = null;
    String result = FdahpStudyDesignerConstants.FAILURE;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (studyId != 0) {

        session
            .createQuery(
                "delete from ActiveTaskLangBO where activeTaskLangPK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        List<ComprehensionQuestionLangBO> questionList =
            session
                .createQuery(
                    "from ComprehensionQuestionLangBO where comprehensionQuestionLangPK.langCode=:lang and studyId=:id")
                .setString("lang", language)
                .setInteger("id", studyId)
                .list();

        List<Integer> questionIdList = new ArrayList<>();
        for (ComprehensionQuestionLangBO questionLangBO : questionList) {
          questionIdList.add(questionLangBO.getComprehensionQuestionLangPK().getId());
        }

        if (questionIdList.size() > 0) {
          session
              .createQuery(
                  "delete from ComprehensionResponseLangBo where comprehensionResponseLangPK.langCode=:lang and questionId in (:questionIdList)")
              .setString("lang", language)
              .setParameterList("questionIdList", questionIdList)
              .executeUpdate();
        }

        session
            .createQuery(
                "delete from ComprehensionQuestionLangBO where comprehensionQuestionLangPK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        session
            .createQuery(
                "delete from ConsentInfoLangBO where consentInfoLangPK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        session
            .createQuery(
                "delete from EligibilityTestLangBo where eligibilityTestLangPK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        List<Integer> questionnaireIdList =
            session
                .createSQLQuery(
                    "select id from questionnaires_lang "
                        + "where study_id=:id and lang_code=:lang")
                .setInteger("id", studyId)
                .setString("lang", language)
                .list();

        session
            .createQuery(
                "delete from QuestionnaireLangBO where questionnaireLangPK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        if (questionnaireIdList != null && questionnaireIdList.size() > 0) {
          for (Integer questionnaireId : questionnaireIdList) {
            session
                .createQuery(
                    "delete from InstructionsLangBO where instructionLangPK.langCode=:lang and questionnaireId=:id")
                .setString("lang", language)
                .setInteger("id", questionnaireId)
                .executeUpdate();

            session
                .createQuery(
                    "delete from FormLangBO where formLangPK.langCode=:lang and questionnaireId=:id")
                .setString("lang", language)
                .setInteger("id", questionnaireId)
                .executeUpdate();

            session
                .createQuery(
                    "delete from QuestionLangBO where questionLangPK.langCode=:lang and questionnaireId=:id")
                .setString("lang", language)
                .setInteger("id", questionnaireId)
                .executeUpdate();
          }
        }

        session
            .createQuery(
                "delete from StudyLanguageBO where studyLanguagePK.langCode=:lang and studyLanguagePK.study_id=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        session
            .createQuery(
                "delete from StudyPageLanguageBO where studyPageLanguagePK.langCode=:lang and studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        session
            .createQuery(
                "delete from StudySequenceLangBO where studySequenceLangPK.langCode=:lang and studySequenceLangPK.studyId=:id")
            .setString("lang", language)
            .setInteger("id", studyId)
            .executeUpdate();

        transaction.commit();
        result = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - deleteAllLanguageData() - ERROR ", e);
      transaction.rollback();
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - deleteAllLanguageData() - Ends");
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<ResourcesLangBO> getResourcesLangList(int studyId, String language) {
    logger.info("StudyDAOImpl - getResourcesLangList() - Starts");
    Session session = null;
    List<ResourcesLangBO> resourcesLangBOList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (studyId != 0) {
        resourcesLangBOList =
            session
                .createQuery(
                    "from ResourcesLangBO where studyId= :studyId and resourcesLangPK.langCode=:lang and status=true")
                .setInteger("studyId", studyId)
                .setString("lang", language)
                .list();
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResourcesLangList() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getResourcesLangList() - Ends");
    return resourcesLangBOList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Boolean> isLanguageDeletable(String customStudyId) {
    logger.info("StudyDAOImpl - isLanguageDeletable() - Starts");
    Session session = null;
    boolean isDeletable = true;
    Map<String, Boolean> map = new HashMap<>();
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      List<Integer> ids =
          session
              .createQuery("select id from StudyBo where customStudyId=:customStudyId")
              .setString("customStudyId", customStudyId)
              .list();

      List<StudyLanguageBO> studyLanguageBOList =
          session
              .createQuery(
                  "from StudyLanguageBO where studyLanguagePK.study_id in (:ids) and live=1")
              .setParameterList("ids", ids)
              .list();

      if (studyLanguageBOList != null && studyLanguageBOList.size() > 0) {
        for (StudyLanguageBO studyLanguageBO : studyLanguageBOList) {
          map.put(studyLanguageBO.getStudyLanguagePK().getLangCode(), false);
        }
      }

    } catch (Exception e) {
      logger.error("StudyDAOImpl - isLanguageDeletable() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - isLanguageDeletable() - Ends");
    return map;
  }

  @Override
  public String updateDraftStatusInStudyBo(int userId, int studyId) {
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      Session session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      message =
          auditLogDAO.updateDraftToEditedStatus(
              session, transaction, userId, FdahpStudyDesignerConstants.DRAFT_STUDY, studyId);
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - ERROR ", e);
    }
    logger.info("StudyDAOImpl - isAnchorDateExistForEnrollmentDraftStudy - Ends");
    return message;
  }

  @Override
  public String forceUpgradeApp(String appId, String androidUpdateType, String iosUpdateType) {
    logger.info("StudyDAOImpl - forceUpgradeApp() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      Session session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      VersionInfo versionInfo = (VersionInfo) session.createQuery("from VersionInfo where appId=:appId")
              .setParameter("appId", appId)
              .uniqueResult();
      if (versionInfo != null) {
        if (StringUtils.isNotBlank(androidUpdateType)) {
          versionInfo.setAndroidForceUpgrade("AUTO".equalsIgnoreCase(androidUpdateType));
        } else {
          versionInfo.setAndroidForceUpgrade(null);
        }
        if (StringUtils.isNotBlank(iosUpdateType)) {
          versionInfo.setIosForceUpgrade("AUTO".equalsIgnoreCase(iosUpdateType));
        } else {
          versionInfo.setIosForceUpgrade(null);
        }
        session.update(versionInfo);
        auditLogDAO.saveToAuditLog(
                session,
                transaction,
                null,
                "App Metadata Updated.",
                "App Metadata Updated. (app id = "
                        + appId
                        + ", app name = "
                        + versionInfo.getAppName()
                        + ") ",
                "StudyDAOImpl - forceUpgradeApp");
        transaction.commit();
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("StudyDAOImpl - forceUpgradeApp() - ERROR ", e);
      transaction.commit();
    }
    logger.info("StudyDAOImpl - forceUpgradeApp() - Ends");
    return message;
  }
}
