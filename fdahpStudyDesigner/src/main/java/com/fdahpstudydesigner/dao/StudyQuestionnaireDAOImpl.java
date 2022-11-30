/** */
package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bean.GroupMappingStepBean;
import com.fdahpstudydesigner.bean.QuestionnaireStepBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

/** @author BTC */
@Repository
public class StudyQuestionnaireDAOImpl implements StudyQuestionnaireDAO {
  private static Logger logger = LogManager.getLogger(StudyQuestionnaireDAOImpl.class.getName());
  @Autowired private AuditLogDAO auditLogDAO;
  HibernateTemplate hibernateTemplate;
  private Query query = null;
  String queryString = "";
  private Transaction transaction = null;

  /**
   * From step have a one or more question.Each question have the short title field this will be
   * created the as column in response server so its should be unique across all the
   * steps.Validating the Unique of question short title inside form step
   *
   * @author BTC
   * @param String , shortTitle in {@link QuestionsBo}
   * @param Integer , questionnaireId in {@link QuestionnairesStepsBo}
   * @param String , questionnaireShortTitle in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return String SUCCESS or FAILUE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String checkFromQuestionShortTitle(
      Integer questionnaireId,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<QuestionnairesStepsBo> questionnairesStepsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (questionnaireShortTitle != null && !questionnaireShortTitle.isEmpty()) {
        query =
            session
                .createQuery(
                    "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN"
                        + "(select QBO.id From QuestionnaireBo QBO where QBO.shortTitle=:questionnaireShortTitle and"
                        + " QBO.studyId in(select id From StudyBo SBO WHERE customStudyId=:customStudyId)) and"
                        + " QSBO.stepShortTitle=:shortTitle")
                .setString("questionnaireShortTitle", questionnaireShortTitle)
                .setString("customStudyId", customStudyId)
                .setParameter("shortTitle", shortTitle);
        questionnairesStepsBo = query.list();
        if (questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          String searchQuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + " and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.short_title=:questionnaireShortTitle "
                  + " and Q.study_id in(select id From studies SBO WHERE custom_study_id=:customStudyId "
                  + " ) and QSBO.step_type='Form' and QBO.short_title=:shortTitle ";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setString("customStudyId", customStudyId)
                      .setString("questionnaireShortTitle", questionnaireShortTitle)
                      .setString("shortTitle", shortTitle)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      } else {
        query =
            session
                .getNamedQuery("checkQuestionnaireStepShortTitle")
                .setInteger("questionnaireId", questionnaireId)
                .setString("shortTitle", shortTitle);
        questionnairesStepsBo = query.list();
        if (questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          String searchQuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + "and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.id=:questionnaireId "
                  + " and QSBO.step_type='Form' and QBO.short_title=:shortTitle ";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setInteger("questionnaireId", questionnaireId)
                      .setString("shortTitle", shortTitle)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      }

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - Ends");
    return message;
  }

  /**
   * This method is used to validate the questionnaire have response type text scale while changing
   * the platform in study settings page
   *
   * @author BTC
   * @param Integer , studyId in {@link StudyBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return String, SUCCESS or FAILURE
   */
  @Override
  public String checkQuestionnaireResponseTypeValidation(Integer studyId, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireResponseTypeValidation() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    BigInteger questionCount = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      // checking of all the question step of questionnaire
      String searchQuery =
          "select count(*) from questions QBO,questionnaires_steps QSBO,questionnaires Q where QBO.id=QSBO.instruction_form_id"
              + " and QSBO.questionnaires_id=Q.id and Q.study_id=:studyId and Q.active=1 and QSBO.step_type= :questionStep"
              + " and QSBO.active=1 and QBO.active=1 and QBO.response_type=3";
      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setInteger("studyId", studyId)
                  .setString("questionStep", FdahpStudyDesignerConstants.QUESTION_STEP)
                  .uniqueResult();
      if (count != null && count.intValue() > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      } else {
        // checking of all question of form step of questionnaire
        String searchQuuery =
            "select count(*) from questions q,form_mapping f,questionnaires_steps qs,questionnaires qq where q.id=f.question_id"
                + " and f.form_id=qs.instruction_form_id and qs.questionnaires_id=qq.id and qq.study_id= :studyId"
                + " and qq.active=1 and qs.step_type='Form' and qs.active=1 and f.active=1 and q.response_type=3 and q.active=1";
        questionCount =
            (BigInteger)
                session.createSQLQuery(searchQuuery).setInteger("studyId", studyId).uniqueResult();
        if (questionCount != null && questionCount.intValue() > 0) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error(
          "StudyQuestionnaireDAOImpl - checkQuestionnaireResponseTypeValidation() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireResponseTypeValidation() - Ends");
    return message;
  }

  /**
   * Questionnaire contains the content,schedule as two tabs.Each questionnaire contains the short
   * title on the content tab this will be created as the column for the questionnaire response in
   * response server for this we are doing the unique title validation for each questionnaire in
   * study level
   *
   * @author BTC
   * @param Integer , studyId in {@link StudyBo}
   * @param String , shortTitle in {@link QuestionnairesStepsBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return String : SUCCESS or FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String checkQuestionnaireShortTitle(
      Integer studyId, String shortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireShortTitle() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<QuestionnaireBo> questionnaireBo = null;
    List<ActiveTaskBo> taskBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (customStudyId != null && !customStudyId.isEmpty()) {
        // checking in the live version questionnaire
        query =
            session.createQuery(
                "From QuestionnaireBo QBO where QBO.studyId IN(select id From StudyBo SBO WHERE customStudyId=:customStudyId ) and QBO.shortTitle=:shortTitle ");
        questionnaireBo =
            query
                .setString("customStudyId", customStudyId)
                .setString("shortTitle", shortTitle)
                .list();
        if (questionnaireBo != null && !questionnaireBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          queryString =
              "from ActiveTaskBo where studyId IN(select id From StudyBo SBO WHERE customStudyId=:customStudyId )  and shortTitle=:shortTitle ";
          taskBo =
              session
                  .createQuery(queryString)
                  .setString("customStudyId", customStudyId)
                  .setString("shortTitle", shortTitle)
                  .list();
          if (taskBo != null && !taskBo.isEmpty()) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      } else {
        // checking in the draft version questionnaire
        query =
            session
                .getNamedQuery("checkQuestionnaireShortTitle")
                .setInteger("studyId", studyId)
                .setString("shortTitle", shortTitle);
        questionnaireBo = query.list();

        if (questionnaireBo != null && !questionnaireBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          queryString = "from ActiveTaskBo where studyId=:studyId and shortTitle=:shortTitle";
          taskBo =
              session
                  .createQuery(queryString)
                  .setInteger("studyId", studyId)
                  .setString("shortTitle", shortTitle)
                  .list();
          if (taskBo != null && !taskBo.isEmpty()) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkQuestionnaireShortTitle() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireShortTitle() - Ends");
    return message;
  }

  /**
   * A questionnaire is an ordered set of one or more steps.Each step contains the step short title
   * field. Which will be response column for the step in response server.so it should be the
   * unique.Here validating the unique for step short title
   *
   * @author BTC
   * @param Integer , questionnaireId in {@link QuestionnairesStepsBo}
   * @param String , stepType in {@link QuestionnairesStepsBo}
   * @param String , shortTitle in {@link QuestionnairesStepsBo}
   * @param String , questionnaireShortTitle in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return String : Success/Failure
   */
  @SuppressWarnings("unchecked")
  @Override
  public String checkQuestionnaireStepShortTitle(
      Integer questionnaireId,
      String stepType,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<QuestionnairesStepsBo> questionnairesStepsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (questionnaireShortTitle != null && !questionnaireShortTitle.isEmpty()) {
        query =
            session.createQuery(
                "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select QBO.id From QuestionnaireBo QBO where QBO.shortTitle=:questionnaireShortTitle "
                    + " and QBO.studyId in(select id From StudyBo SBO WHERE customStudyId=:customStudyId "
                    + " )) and QSBO.stepShortTitle=:shortTitle ");
        questionnairesStepsBo =
            query
                .setString("questionnaireShortTitle", questionnaireShortTitle)
                .setString("customStudyId", customStudyId)
                .setString("shortTitle", shortTitle)
                .list();
        if (questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          String searchQuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + " and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.short_title=:questionnaireShortTitle "
                  + " and Q.study_id in(select id From studies SBO WHERE custom_study_id=:customStudyId "
                  + " ) and QSBO.step_type='Form' and QBO.short_title=:shortTitle ";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setString("questionnaireShortTitle", questionnaireShortTitle)
                      .setString("customStudyId", customStudyId)
                      .setString("shortTitle", shortTitle)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      } else {
        query =
            session
                .getNamedQuery("checkQuestionnaireStepShortTitle")
                .setInteger("questionnaireId", questionnaireId)
                .setString("shortTitle", shortTitle);
        questionnairesStepsBo = query.list();
        if (questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          String searchQuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + " and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.id=:questionnaireId "
                  + " and QSBO.step_type='Form' and QBO.short_title=:shortTitle ";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setInteger("questionnaireId", questionnaireId)
                      .setString("shortTitle", shortTitle)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - Ends");
    return message;
  }

  /**
   * The admin can choose to add a response data element to the study dashboard in the form of line
   * charts or statistics.Adding a statistic to the dashboard needs the admin to specify the short
   * name should be unique across all the state in the study So validating the unique validation for
   * short name in states.
   *
   * @author BTC
   * @param Integer , StudyId in {@link StudyBo}
   * @param String , shortTitle in {@link QuestionsBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return String SUCCESS or FAILUE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String checkStatShortTitle(Integer studyId, String shortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - checkQuestionnaireStepShortTitle() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<QuestionsBo> questionsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      // checking with custom study in live version and draft version
      if (customStudyId != null && !customStudyId.isEmpty()) {
        // checking in the question step stastic data
        String serachQuery =
            "select count(*) from questions qbo,questionnaires_steps qsbo,questionnaires q where qbo.id=qsbo.instruction_form_id and qsbo.questionnaires_id=q.id and q.study_id in(select id From studies SBO WHERE custom_study_id=:customStudyId "
                + ") and qsbo.step_type=:stepType "
                + " and qbo.stat_short_name=:shortTitle";
        BigInteger count =
            (BigInteger)
                session
                    .createSQLQuery(serachQuery)
                    .setString("customStudyId", customStudyId)
                    .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP)
                    .setString("shortTitle", shortTitle)
                    .uniqueResult();
        if (count != null && count.intValue() > 0) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          // checking in the form step questions stastic data
          String searchQuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + "and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.study_id IN(select id From studies SBO WHERE custom_study_id= :customStudyId"
                  + " ) and QSBO.step_type='Form' and QBO.stat_short_name=:shortTitle ";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(searchQuery)
                      .setString("customStudyId", customStudyId)
                      .setString("shortTitle", shortTitle)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          } else {
            // checking in the active task stastic data
            String taskQuery =
                "from ActiveTaskAtrributeValuesBo where activeTaskId in(select id from ActiveTaskBo where studyId IN(select id From StudyBo SBO WHERE customStudyId=:customStudyId "
                    + " ) ) and identifierNameStat=:shortTitle ";
            List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos =
                session
                    .createQuery(taskQuery)
                    .setString("customStudyId", customStudyId)
                    .setString("shortTitle", shortTitle)
                    .list();
            if (activeTaskAtrributeValuesBos != null && !activeTaskAtrributeValuesBos.isEmpty()) {
              message = FdahpStudyDesignerConstants.SUCCESS;
            }
          }
        }
      } else {
        // checking with study if custom study id is not available
        query =
            session.createQuery(
                "From QuestionsBo QBO where QBO.id IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select id from QuestionnaireBo Q where Q.studyId=:studyId "
                    + " ) and QSBO.stepType=:stepType "
                    + " and QSBO.active=1) and QBO.statShortName=:shortTitle ");
        questionsBo =
            query
                .setInteger("studyId", studyId)
                .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP)
                .setString("shortTitle", shortTitle)
                .list();
        if (questionsBo != null && !questionsBo.isEmpty()) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        } else {
          String searchQuuery =
              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id "
                  + "and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.study_id=:studyId"
                  + " and QSBO.step_type='Form' and QBO.stat_short_name=:shortTitle";
          questionsBo =
              session
                  .createQuery(searchQuuery)
                  .setInteger("studyId", studyId)
                  .setString("shortTitle", shortTitle)
                  .list();
          if (questionsBo != null && !questionsBo.isEmpty()) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          } else {
            String taskQuery =
                "from ActiveTaskAtrributeValuesBo where activeTaskId in(select id from ActiveTaskBo where studyId=:studyId "
                    + ") and identifierNameStat=:shortTitle ";
            List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos =
                session
                    .createQuery(taskQuery)
                    .setInteger("studyId", studyId)
                    .setString("shortTitle", shortTitle)
                    .list();
            if (activeTaskAtrributeValuesBos != null && !activeTaskAtrributeValuesBos.isEmpty()) {
              message = FdahpStudyDesignerConstants.SUCCESS;
            }
          }
        }
      }

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkStatShortTitle() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkStatShortTitle() - Ends");
    return message;
  }

  /**
   * Admin want copy the already existed question into the same study admin has to click the copy
   * icon in the questionnaire list.It will copy the existed questionnaire into the study with out
   * questionnaire short title because the short title will be unique across the study
   *
   * @author BTC
   * @param questionnaireId , {@link QuestionnaireBo}
   * @param customStudyId , {@link StudyBo}
   * @param sessionObject {@link SessionObject}
   * @return {@link QuestionnaireBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public QuestionnaireBo copyStudyQuestionnaireBo(
      Integer questionnaireId, String customStudyId, SessionObject sessionObject) {
    logger.info("StudyQuestionnaireDAOImpl - copyStudyQuestionnaireBo() - Starts");
    QuestionnaireBo questionnaireBo = null;
    QuestionnaireBo newQuestionnaireBo = null;
    Session session = null;
    QuestionReponseTypeBo questionReponseTypeBo = null;
    int count=0;
    try {
      // Questionarries
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      query = session.createQuery(" From QuestionnaireBo QBO WHERE QBO.id=:questionnaireId");
      questionnaireBo =
          (QuestionnaireBo) query.setInteger("questionnaireId", questionnaireId).uniqueResult();
      if (questionnaireBo != null) {
        String searchQuery = null;
        newQuestionnaireBo = SerializationUtils.clone(questionnaireBo);
        newQuestionnaireBo.setId(null);
        newQuestionnaireBo.setLive(0);
        newQuestionnaireBo.setCreatedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
        newQuestionnaireBo.setCreatedBy(sessionObject.getUserId());
        newQuestionnaireBo.setModifiedBy(null);
        newQuestionnaireBo.setModifiedDate(null);
        newQuestionnaireBo.setShortTitle(null);
        newQuestionnaireBo.setStatus(false);
        newQuestionnaireBo.setVersion(0f);
        session.save(newQuestionnaireBo);

        query = session.createQuery(" From QuestionnaireLangBO WHERE questionnaireLangPK.id=:questionnaireId");
        List<QuestionnaireLangBO> questionnaireLangBOList = query.setParameter("questionnaireId", questionnaireId).list();
        if (questionnaireLangBOList != null) {
          for (QuestionnaireLangBO questionnaireLangBO : questionnaireLangBOList) {
            QuestionnaireLangBO newQuestionnaireLang = SerializationUtils.clone(questionnaireLangBO);
            newQuestionnaireLang.getQuestionnaireLangPK().setId(newQuestionnaireBo.getId());
            newQuestionnaireLang.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            newQuestionnaireLang.setCreatedBy(sessionObject.getUserId());
            newQuestionnaireLang.setModifiedBy(null);
            newQuestionnaireLang.setModifiedOn(null);
            newQuestionnaireLang.setStatus(false);
            session.save(newQuestionnaireLang);
          }
        }


        List<StudySequenceLangBO> studySequenceLangBOS =
            session
                .createQuery("from StudySequenceLangBO where studySequenceLangPK.studyId=:id")
                .setInteger("id", questionnaireBo.getStudyId())
                .list();
        if (studySequenceLangBOS != null && studySequenceLangBOS.size() > 0) {
          for (StudySequenceLangBO studySequenceLangBO : studySequenceLangBOS) {
            studySequenceLangBO.setStudyExcQuestionnaries(false);
            session.update(studySequenceLangBO);
          }
        }

        /** Questionnaire Schedule Purpose copying Start * */
        if (StringUtils.isNotEmpty(questionnaireBo.getFrequency())) {
          if (questionnaireBo
              .getFrequency()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
            searchQuery =
                "From QuestionnaireCustomScheduleBo QCSBO where QCSBO.questionnairesId=:questionnairesId";
            List<QuestionnaireCustomScheduleBo> questionnaireCustomScheduleList =
                session
                    .createQuery(searchQuery)
                    .setInteger("questionnairesId", questionnaireBo.getId())
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
                "From QuestionnairesFrequenciesBo QFBO where QFBO.questionnairesId=:questionnairesId";
            List<QuestionnairesFrequenciesBo> questionnairesFrequenciesList =
                session
                    .createQuery(searchQuery)
                    .setInteger("questionnairesId", questionnaireBo.getId())
                    .list();
            if (questionnairesFrequenciesList != null && !questionnairesFrequenciesList.isEmpty()) {
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
        /** Questionnaire Schedule Purpose copying End * */

        /** Questionnaire Content purpose copying Start * */
        List<Integer> destinationList = new ArrayList<>();
        Map<Integer, Integer> destionationMapList = new HashMap<>();

        List<QuestionnairesStepsBo> existedQuestionnairesStepsBoList = null;
        List<QuestionnairesStepsBo> newQuestionnairesStepsBoList = new ArrayList<>();
        List<QuestionResponseSubTypeBo> existingQuestionResponseSubTypeList = new ArrayList<>();
        List<QuestionResponseSubTypeBo> newQuestionResponseSubTypeList = new ArrayList<>();

        List<QuestionReponseTypeBo> existingQuestionResponseTypeList = new ArrayList<>();
        List<QuestionReponseTypeBo> newQuestionResponseTypeList = new ArrayList<>();

        query =
            session
                .getNamedQuery("getQuestionnaireStepList")
                .setInteger("questionnaireId", questionnaireBo.getId());
        existedQuestionnairesStepsBoList = query.list();
        // copying the questionnaire steps
        Map<Integer, Integer> oldNewDestMap = new HashMap<>();
        Map<Integer, Integer> oldNewSourceMap = new HashMap<>();
        Map<Integer,Integer> groupIdMap = new HashMap<>();
        List<Integer> preLoadDestList = new ArrayList<>();
        List<Integer> pipingSrcList = new ArrayList<>();
        if (existedQuestionnairesStepsBoList != null
            && !existedQuestionnairesStepsBoList.isEmpty()) {
          for (QuestionnairesStepsBo questionnairesStepsBo : existedQuestionnairesStepsBoList) {
            if (questionnairesStepsBo.getDestinationTrueAsGroup() != null) {
              preLoadDestList.add(questionnairesStepsBo.getDestinationTrueAsGroup());
            }
            if (questionnairesStepsBo.getPipingSourceQuestionKey() != null) {
              pipingSrcList.add(questionnairesStepsBo.getPipingSourceQuestionKey());
            }
            Integer destionStep = questionnairesStepsBo.getDestinationStep();
            if (destionStep.equals(0)) {
              destinationList.add(-1);
            } else {
              for (int i = 0; i < existedQuestionnairesStepsBoList.size(); i++) {
                if (existedQuestionnairesStepsBoList.get(i).getStepId() != null
                    && destionStep.equals(existedQuestionnairesStepsBoList.get(i).getStepId())) {
                  destinationList.add(i);
                  break;
                }
              }
            }
            destionationMapList.put(
                questionnairesStepsBo.getSequenceNo(), questionnairesStepsBo.getStepId());
          }
          Map<Integer,Integer> stepIdMap = new HashMap<>();
          Map<Integer, Integer> oldNewInstructionFormIdMap = new HashMap<>();
          for (QuestionnairesStepsBo questionnairesStepsBo : existedQuestionnairesStepsBoList) {
            if (StringUtils.isNotEmpty(questionnairesStepsBo.getStepType())) {
              QuestionnairesStepsBo newQuestionnairesStepsBo =
                  SerializationUtils.clone(questionnairesStepsBo);
              newQuestionnairesStepsBo.setQuestionnairesId(newQuestionnaireBo.getId());
              newQuestionnairesStepsBo.setStepId(null);
              newQuestionnairesStepsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              newQuestionnairesStepsBo.setCreatedBy(sessionObject.getUserId());
              newQuestionnairesStepsBo.setModifiedBy(null);
              newQuestionnairesStepsBo.setModifiedOn(null);
              session.save(newQuestionnairesStepsBo);
              stepIdMap.put(questionnairesStepsBo.getStepId(), newQuestionnairesStepsBo.getStepId());

              if (!preLoadDestList.isEmpty() && preLoadDestList.contains(questionnairesStepsBo.getStepId())) {
                for (Integer oldDestId : preLoadDestList) {
                  if (oldDestId.equals(questionnairesStepsBo.getStepId())) {
                    oldNewDestMap.put(oldDestId, newQuestionnairesStepsBo.getStepId());
                  }
                }
              }

              if (!pipingSrcList.isEmpty() && pipingSrcList.contains(questionnairesStepsBo.getStepId())) {
                for (Integer oldSrcId : pipingSrcList) {
                  if (oldSrcId.equals(questionnairesStepsBo.getStepId())) {
                    oldNewSourceMap.put(oldSrcId, newQuestionnairesStepsBo.getStepId());
                  }
                }
              }

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
                // copying the instruction step
                InstructionsBo instructionsBo =
                    (InstructionsBo)
                        session
                            .getNamedQuery("getInstructionStep")
                            .setInteger("id", questionnairesStepsBo.getInstructionFormId())
                            .uniqueResult();
                if (instructionsBo != null) {
                  InstructionsBo newInstructionsBo = SerializationUtils.clone(instructionsBo);
                  newInstructionsBo.setId(null);
                  newInstructionsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                  newInstructionsBo.setCreatedBy(sessionObject.getUserId());
                  newInstructionsBo.setModifiedBy(null);
                  newInstructionsBo.setModifiedOn(null);
                  session.save(newInstructionsBo);

                  // updating new InstructionId
                  newQuestionnairesStepsBo.setInstructionFormId(newInstructionsBo.getId());

                  // for other languages
                  List<InstructionsLangBO> instructionsLangBOList = session
                                          .createQuery("from InstructionsLangBO where instructionLangPK.id = :id and active = true")
                                          .setParameter("id", questionnairesStepsBo.getInstructionFormId())
                                          .list();

                  if (instructionsLangBOList != null) {
                    for (InstructionsLangBO instructionsLangBO : instructionsLangBOList) {
                      InstructionsLangBO newInstructionsLang = SerializationUtils.clone(instructionsLangBO);
                      newInstructionsLang.getInstructionLangPK().setId(newInstructionsBo.getId());
                      newInstructionsLang.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                      newInstructionsLang.setCreatedBy(sessionObject.getUserId());
                      newInstructionsLang.setModifiedBy(null);
                      newInstructionsLang.setModifiedOn(null);
                      session.save(newInstructionsLang);
                    }
                  }
                  oldNewInstructionFormIdMap.put(questionnairesStepsBo.getInstructionFormId(), newInstructionsBo.getId());
                }
              } else if (questionnairesStepsBo
                  .getStepType()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
                // copying the question step
                QuestionsBo questionsBo =
                    (QuestionsBo)
                        session
                            .getNamedQuery("getQuestionStep")
                            .setInteger("stepId", questionnairesStepsBo.getInstructionFormId())
                            .uniqueResult();
                if (questionsBo != null) {

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
                  newQuestionsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                  newQuestionsBo.setCreatedBy(sessionObject.getUserId());
                  newQuestionsBo.setModifiedBy(null);
                  newQuestionsBo.setModifiedOn(null);
                  newQuestionsBo.setAnchorDateId(null);
                  if (questionsBo
                      .getUseStasticData()
                      .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
                    newQuestionsBo.setStatShortName(null);
                    newQuestionsBo.setStatus(false);
                    newQuestionnairesStepsBo.setStatus(false);
                  }
                  session.save(newQuestionsBo);

                  List<QuestionLangBO> questionLangBOList = session
                          .createQuery("from QuestionLangBO where questionLangPK.id = :id and active = true")
                          .setParameter("id", questionnairesStepsBo.getInstructionFormId())
                          .list();

                  // Question response Type
                  if (questionReponseTypeBo != null) {
                    QuestionReponseTypeBo newQuestionReponseTypeBo =
                        SerializationUtils.clone(questionReponseTypeBo);
                    newQuestionReponseTypeBo.setResponseTypeId(null);
                    newQuestionReponseTypeBo.setQuestionsResponseTypeId(newQuestionsBo.getId());
                    newQuestionReponseTypeBo.setOtherDestinationStepId(null);
                    session.save(newQuestionReponseTypeBo);
                    if (questionReponseTypeBo.getOtherType() != null
                        && StringUtils.isNotEmpty(questionReponseTypeBo.getOtherType())
                        && questionReponseTypeBo.getOtherType().equals("on")) {
                      existingQuestionResponseTypeList.add(questionReponseTypeBo);
                      newQuestionResponseTypeList.add(newQuestionReponseTypeBo);
                    }
                  }

                  // Question Condition branching logic
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

                      QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                          SerializationUtils.clone(questionResponseSubTypeBo);
                      newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                      newQuestionResponseSubTypeBo.setResponseTypeId(newQuestionsBo.getId());
                      newQuestionResponseSubTypeBo.setDestinationStepId(null);
                      session.save(newQuestionResponseSubTypeBo);
                      newQuestionResponseSubTypeList.add(newQuestionResponseSubTypeBo);
                    }
                  }

                  // updating new InstructionId
                  newQuestionnairesStepsBo.setInstructionFormId(newQuestionsBo.getId());

                  if (questionLangBOList != null) {
                    for (QuestionLangBO questionLangBO : questionLangBOList) {
                      QuestionLangBO newQuestionsLang = SerializationUtils.clone(questionLangBO);
                      newQuestionsLang.getQuestionLangPK().setId(newQuestionsBo.getId());
                      newQuestionsLang.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                      newQuestionsLang.setCreatedBy(sessionObject.getUserId());
                      newQuestionsLang.setModifiedBy(null);
                      newQuestionsLang.setModifiedOn(null);
                      session.save(newQuestionsLang);
                    }
                  }
                  oldNewInstructionFormIdMap.put(questionnairesStepsBo.getInstructionFormId(), newQuestionnairesStepsBo.getInstructionFormId());
                }
              } else if (questionnairesStepsBo
                  .getStepType()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
                // copying the form step
                FormBo formBo =
                    (FormBo)
                        session
                            .getNamedQuery("getFormBoStep")
                            .setInteger("stepId", questionnairesStepsBo.getInstructionFormId())
                            .uniqueResult();
                if (formBo != null) {

                  FormBo newFormBo = SerializationUtils.clone(formBo);
                  newFormBo.setFormId(null);
                  session.save(newFormBo);

                  List<FormLangBO> formLangBOList = session.createQuery("from FormLangBO where formLangPK.formId = :id and active = true")
                          .setParameter("id", questionnairesStepsBo.getInstructionFormId())
                          .list();

                  if (formLangBOList != null) {
                    for (FormLangBO formLangBO : formLangBOList) {
                      FormLangBO newFormLangBo = SerializationUtils.clone(formLangBO);
                      newFormLangBo.getFormLangPK().setFormId(newFormBo.getFormId());
                      session.save(newFormLangBo);
                    }
                  }

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

                        // Question response subType
                        List<QuestionResponseSubTypeBo> questionResponseSubTypeList =
                            session
                                .getNamedQuery("getQuestionSubResponse")
                                .setInteger("responseTypeId", questionsBo.getId())
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

                        newQuestionsBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                        newQuestionsBo.setCreatedBy(sessionObject.getUserId());
                        newQuestionsBo.setModifiedBy(null);
                        newQuestionsBo.setModifiedOn(null);
                        newQuestionsBo.setAnchorDateId(null);
                        if (questionsBo
                            .getUseStasticData()
                            .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
                          newQuestionsBo.setStatShortName(null);
                          newQuestionsBo.setStatus(false);
                          newQuestionnairesStepsBo.setStatus(false);
                        }

                        session.save(newQuestionsBo);

                        // Question response Type
                        if (questionReponseTypeBo != null) {
                          QuestionReponseTypeBo newQuestionReponseTypeBo =
                              SerializationUtils.clone(questionReponseTypeBo);
                          newQuestionReponseTypeBo.setResponseTypeId(null);
                          newQuestionReponseTypeBo.setQuestionsResponseTypeId(
                              newQuestionsBo.getId());
                          session.save(newQuestionReponseTypeBo);
                        }

                        // Question response subType
                        if (questionResponseSubTypeList != null
                            && !questionResponseSubTypeList.isEmpty()) {
                          for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                              questionResponseSubTypeList) {
                            QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                                SerializationUtils.clone(questionResponseSubTypeBo);
                            newQuestionResponseSubTypeBo.setResponseSubTypeValueId(null);
                            newQuestionResponseSubTypeBo.setResponseTypeId(newQuestionsBo.getId());
                            session.save(newQuestionResponseSubTypeBo);
                          }
                        }

                        // adding questionId
                        newMappingBo.setQuestionId(newQuestionsBo.getId());
                        session.save(newMappingBo);

                        List<QuestionLangBO> questionLangBOList =
                                        session.createQuery("from QuestionLangBO where questionLangPK.id = :id and active = true")
                                                .setParameter("id", questionsBo.getId())
                                                .list();

                        if (questionLangBOList != null) {
                          for (QuestionLangBO questionLangBO : questionLangBOList) {
                            QuestionLangBO newQuestionsLang = SerializationUtils.clone(questionLangBO);
                            newQuestionsLang.getQuestionLangPK().setId(newQuestionsBo.getId());
                            newQuestionsLang.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                            newQuestionsLang.setCreatedBy(sessionObject.getUserId());
                            newQuestionsLang.setModifiedBy(null);
                            newQuestionsLang.setModifiedOn(null);
                            session.save(newQuestionsLang);
                          }
                        }
                      }
                    }
                  }
                  // updating new formId
                  oldNewInstructionFormIdMap.put(questionnairesStepsBo.getInstructionFormId(), newFormBo.getFormId());
                  newQuestionnairesStepsBo.setInstructionFormId(newFormBo.getFormId());
                }
              }
              session.update(newQuestionnairesStepsBo);
              newQuestionnairesStepsBoList.add(newQuestionnairesStepsBo);
            }
          }

          //copy groups
          List<GroupsBo> groupsBoList = session.createQuery("From GroupsBo GBO WHERE  GBO.questionnaireId=:questionnaireId")
                  .setParameter("questionnaireId", questionnaireId)
                  .getResultList();
          if (groupsBoList != null) {
            List<GroupsBo> newGroupsList = new ArrayList<>();
            for (GroupsBo groupsBo : groupsBoList) {
              if (groupsBo != null) {
                GroupsBo newGroupsBo = SerializationUtils.clone(groupsBo);
                newGroupsBo.setId(null);
                newGroupsBo.setStudyId(newQuestionnaireBo.getStudyId());
                newGroupsBo.setQuestionnaireId(newQuestionnaireBo.getId());
                session.saveOrUpdate(newGroupsBo);
                newGroupsList.add(newGroupsBo);
                groupIdMap.put(groupsBo.getId(), newGroupsBo.getId());

                List<PreLoadLogicBo> preLoadLogicBoGroupsList = session.createQuery("from PreLoadLogicBo where stepGroupId=:id and stepOrGroup=:group")
                        .setParameter("id", groupsBo.getId())
                        .setParameter(FdahpStudyDesignerConstants.GROUP, FdahpStudyDesignerConstants.GROUP)
                        .list();

                for (PreLoadLogicBo preLoadLogicBo : preLoadLogicBoGroupsList) {
                  PreLoadLogicBo newPreLoadLogicBo = SerializationUtils.clone(preLoadLogicBo);
                  newPreLoadLogicBo.setId(null);
                  newPreLoadLogicBo.setStepGroupId(newGroupsBo.getId());
                  session.save(newPreLoadLogicBo);
                }

                List<GroupMappingBo> mappingBos = session
                        .createQuery("from GroupMappingBo where status=true and grpId=:groupId")
                        .setParameter("groupId", groupsBo.getId())
                        .list();
                if (mappingBos != null && !mappingBos.isEmpty()) {
                  for (GroupMappingBo mappingBo : mappingBos) {
                    GroupMappingBo newMappingBo = SerializationUtils.clone(mappingBo);
                    newMappingBo.setId(null);
                    newMappingBo.setGrpId(newGroupsBo.getId());
                    newMappingBo.setStepId(oldNewInstructionFormIdMap.containsKey(Integer.parseInt(mappingBo.getStepId()))
                            ? String.valueOf(oldNewInstructionFormIdMap.get(Integer.parseInt(mappingBo.getStepId())))
                            : mappingBo.getStepId());
                    newMappingBo.setQuestionnaireStepId(stepIdMap.containsKey(mappingBo.getQuestionnaireStepId())
                            ? stepIdMap.get(mappingBo.getQuestionnaireStepId())
                            : mappingBo.getQuestionnaireStepId());
                    session.save(newMappingBo);
                  }
                }
              }
            }

            for (GroupsBo groupsBo : newGroupsList) {
              if (groupsBo.getDestinationTrueAsGroup() != null && !groupsBo.getDestinationTrueAsGroup().equals(0)) {
                if (FdahpStudyDesignerConstants.GROUP.equals(groupsBo.getStepOrGroup())) {
                  groupsBo.setDestinationTrueAsGroup(groupIdMap.get(groupsBo.getDestinationTrueAsGroup()));
                } else {
                	for(QuestionnairesStepsBo questionnairesStepsBo : existedQuestionnairesStepsBoList) {
                		if(questionnairesStepsBo.getStepId().equals(groupsBo.getDestinationTrueAsGroup())) {
                			int seqNumber=questionnairesStepsBo.getSequenceNo();
                			for(QuestionnairesStepsBo questionnairesSteps : newQuestionnairesStepsBoList) {
                				if(questionnairesSteps.getSequenceNo().equals(seqNumber)) {
                					groupsBo.setDestinationTrueAsGroup(questionnairesSteps.getStepId());
                				}
                			}
                		}
                	}
                  
                }
                session.update(groupsBo);
              }
            }
          }
        }

        if (!oldNewDestMap.isEmpty()) {
          for (QuestionnairesStepsBo questionnairesStepsBo : newQuestionnairesStepsBoList) {
            if (FdahpStudyDesignerConstants.STEP.equals(questionnairesStepsBo.getStepOrGroup())) {
              for (Integer oldDestId : oldNewDestMap.keySet()) {
                if (oldDestId.equals(questionnairesStepsBo.getDestinationTrueAsGroup())) {
                  questionnairesStepsBo.setDestinationTrueAsGroup(oldNewDestMap.get(oldDestId));
                  break;
                }
              }
            }
          }
        }

        if (!groupIdMap.isEmpty()) {
          for (QuestionnairesStepsBo questionnairesStepsBo : newQuestionnairesStepsBoList) {
            if (FdahpStudyDesignerConstants.GROUP.equals(questionnairesStepsBo.getStepOrGroup())) {
              for (Integer oldDestId : groupIdMap.keySet()) {
                if (oldDestId.equals(questionnairesStepsBo.getDestinationTrueAsGroup())) {
                  questionnairesStepsBo.setDestinationTrueAsGroup(groupIdMap.get(oldDestId));
                  break;
                }
              }
            }
          }
        }

        if (!oldNewSourceMap.isEmpty()) {
          for (QuestionnairesStepsBo questionnairesStepsBo : newQuestionnairesStepsBoList) {
            for (Integer oldSrcId : oldNewSourceMap.keySet()) {
              if (oldSrcId.equals(questionnairesStepsBo.getPipingSourceQuestionKey())) {
                questionnairesStepsBo.setPipingSourceQuestionKey(oldNewSourceMap.get(oldSrcId));
                break;
              }
            }
          }
        }

        // updating the copied destination steps for questionnaire steps
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
        // getting the list of all copied choice based destinations
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
          // updating the choice based destination steps
          for (int i = 0; i < destinationResList.size(); i++) {
            newQuestionResponseSubTypeList.get(i).setDestinationStepId(destinationResList.get(i));
            session.update(newQuestionResponseSubTypeList.get(i));
          }
        }

        // for other type , update the destination in questionresponsetype table
        /** start * */
        List<Integer> sequenceTypeList = new ArrayList<>();
        List<Integer> destinationResTypeList = new ArrayList<>();
        if (existingQuestionResponseTypeList != null
            && !existingQuestionResponseTypeList.isEmpty()) {
          for (QuestionReponseTypeBo questionResponseTypeBo : existingQuestionResponseTypeList) {
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
              for (QuestionnairesStepsBo questionnairesStepsBo : newQuestionnairesStepsBoList) {
                if (sequenceTypeList.get(i).equals(questionnairesStepsBo.getSequenceNo())) {
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
    logger.info("StudyQuestionnaireDAOImpl - copyStudyQuestionnaireBo() - Ends");
    return newQuestionnaireBo;
  }

  /**
   * This method is used to delete the question inside the form step of an questionnaire
   *
   * @author BTC
   * @param Integer , formId in {@link FormMappingBo}
   * @param Integer , questionId in {@link QuestionsBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return String Success/Failure
   */
  @Override
  public String deleteFromStepQuestion(
      Integer formId, Integer questionId, SessionObject sessionObject, String customStudyId) {
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    logger.info("StudyQuestionnaireDAOImpl - deleteFromStepQuestion() - Starts");
    FormMappingBo formMappingBo = null;
    String activitydetails = "";
    String activity = "";
    StudyVersionBo studyVersionBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      query =
          session
              .getNamedQuery("getStudyByCustomStudyId")
              .setMaxResults(1)
              .setString("customStudyId", customStudyId);
      query.setMaxResults(1);
      studyVersionBo = (StudyVersionBo) query.uniqueResult();
      query =
          session
              .getNamedQuery("getFormQuestion")
              .setInteger("formId", formId)
              .setInteger("questionId", questionId);
      formMappingBo = (FormMappingBo) query.uniqueResult();
      if (formMappingBo != null) {
        String updateQuery =
            "update FormMappingBo FMBO set FMBO.sequenceNo=FMBO.sequenceNo-1 where FMBO.formId=:formId "
                + " and FMBO.active=1 and FMBO.sequenceNo >=:sequenceNo ";
        query =
            session
                .createQuery(updateQuery)
                .setInteger("formId", formMappingBo.getFormId())
                .setInteger("sequenceNo", formMappingBo.getSequenceNo());
        query.executeUpdate();
        // delete anchordate start
        StudyBo studyBo =
            (StudyBo)
                session
                    .createQuery("from StudyBo where customStudyId=:customStudyId and live=0")
                    .setString("customStudyId", customStudyId)
                    .uniqueResult();
        if (studyBo != null) {
          boolean isChange = true;
          message =
              updateAnchordateInQuestionnaire(
                  session,
                  transaction,
                  studyVersionBo,
                  null,
                  sessionObject,
                  studyBo.getId(),
                  null,
                  questionId,
                  "",
                  isChange);
          if (!message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            return message;
          }
        }
        // delete anchordate end
        if (studyVersionBo != null) {
          // doing the soft delete after study is launched
          formMappingBo.setActive(false);
          session.saveOrUpdate(formMappingBo);
          String deleteQuery =
              "Update QuestionsBo QBO set QBO.active=0,QBO.modifiedBy=:userId "
                  + ",QBO.modifiedOn=:currentDateAndTime "
                  + " where QBO.id=:questionId ";
          query =
              session
                  .createQuery(deleteQuery)
                  .setInteger("userId", sessionObject.getUserId())
                  .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
                  .setInteger("questionId", questionId);
          query.executeUpdate();
          String deleteResponse =
              "Update QuestionReponseTypeBo QRBO set QRBO.active=0 where QRBO.questionsResponseTypeId=:questionId ";
          query = session.createQuery(deleteResponse).setInteger("questionId", questionId);
          query.executeUpdate();

          String deleteSubResponse =
              "Update QuestionResponseSubTypeBo QRSBO set QRSBO.active=0 where QRSBO.responseTypeId=:questionId ";
          query = session.createQuery(deleteSubResponse).setInteger("questionId", questionId);
          query.executeUpdate();
        } else {
          // doing the hard delete before study launched
          String deleteQuery = "delete QuestionsBo QBO where QBO.id=:questionId ";
          query = session.createQuery(deleteQuery).setInteger("questionId", questionId);
          query.executeUpdate();

          String deleteResponse =
              "delete QuestionReponseTypeBo QRBO where QRBO.questionsResponseTypeId=:questionId ";
          query = session.createQuery(deleteResponse).setInteger("questionId", questionId);
          query.executeUpdate();

          String deleteSubResponse =
              "delete QuestionResponseSubTypeBo QRSBO  where QRSBO.responseTypeId=:questionId ";
          query = session.createQuery(deleteSubResponse).setInteger("questionId", questionId);
          query.executeUpdate();

          session.delete(formMappingBo);
        }
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
      activity = "Question of form step was deleted.";
      activitydetails = "Question of form step was deleted. (Study ID = " + customStudyId + ")";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - deleteFromStepQuestion");
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteFromStepQuestion() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteFromStepQuestion() - Ends");
    return message;
  }

  /**
   * Delete of an questionnaire step(Instruction,Question,Form) which are listed in questionnaire.
   *
   * @author BTC
   * @param stepId in {@link QuestionnairesStepsBo}
   * @param questionnaireId in {@link QuestionnaireBo}
   * @param stepType in {@link QuestionnairesStepsBo}
   * @param sessionObject {@link SessionObject}
   * @param customStudyId in {@link StudyBo}
   * @return String SUCCESS or FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String deleteQuestionnaireStep(
      Integer stepId,
      Integer questionnaireId,
      String stepType,
      SessionObject sessionObject,
      String customStudyId,
      Integer deletionId) {
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireStep() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    QuestionnairesStepsBo questionnairesStepsBo = null;
    List<QuestionnairesStepsBo> questionnaireStepList = null;
    String activitydetails = "";
    String activity = "";
    StudyVersionBo studyVersionBo = null;
    String searchQuery = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      query =
          session
              .getNamedQuery("getStudyByCustomStudyId")
              .setMaxResults(1)
              .setString("customStudyId", customStudyId);
      query.setMaxResults(1);
      studyVersionBo = (StudyVersionBo) query.uniqueResult();

      // Anchordate delete based on stepId start
      if (!stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.INSTRUCTION_STEP)) {
        StudyBo studyBo =
            (StudyBo)
                session
                    .createQuery("from StudyBo where customStudyId=:customStudyId and live=0 ")
                    .setParameter("customStudyId", customStudyId)
                    .setMaxResults(1)
                    .uniqueResult();
        if (studyBo != null) {
          boolean isChange = true;
          message =
              updateAnchordateInQuestionnaire(
                  session,
                  transaction,
                  studyVersionBo,
                  questionnaireId,
                  sessionObject,
                  studyBo.getId(),
                  stepId,
                  null,
                  stepType,
                  isChange);
          if (!message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            return message;
          }
        }
      }
      // Anchordate delete based on stepId end

      // set status to false for other steps if used in preload logic
      this.validatePreLoadLogicForOtherSteps(session, deletionId);
      this.validatePipingLogicForOtherSteps(session, deletionId);

      if (studyVersionBo != null) {
        // doing the soft delete after study launch
        searchQuery =
            "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:stepId "
                + " and QSBO.questionnairesId=:questionnaireId "
                + " and QSBO.stepType=:stepType ";
        questionnairesStepsBo =
            (QuestionnairesStepsBo)
                session
                    .createQuery(searchQuery)
                    .setParameter("stepId", stepId)
                    .setParameter("questionnaireId", questionnaireId)
                    .setParameter("stepType", stepType)
                    .uniqueResult();
        if (questionnairesStepsBo != null) {

          questionnairesStepsBo.setActive(false);
          session.saveOrUpdate(questionnairesStepsBo);

          query =
              session
                  .createSQLQuery(
                      "CALL deleteQuestionnaireStep(:questionnaireId,:modifiedOn,:modifiedBy,:sequenceNo,:stepId,:steptype)")
                  .setParameter("questionnaireId", questionnaireId)
                  .setParameter("modifiedOn", FdahpStudyDesignerUtil.getCurrentDateTime())
                  .setParameter("modifiedBy", sessionObject.getUserId())
                  .setParameter("sequenceNo", 2)
                  .setParameter("stepId", stepId)
                  .setParameter("steptype", stepType);
          query.executeUpdate();

          if (questionnairesStepsBo
              .getStepType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.INSTRUCTION_STEP)) {
            activity = FdahpStudyDesignerConstants.INSTRUCTION_ACTIVITY + " was deleted.";
            activitydetails = "Instruction step was deleted. (Study ID = " + customStudyId + ")";
          } else if (questionnairesStepsBo
              .getStepType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
            activity = FdahpStudyDesignerConstants.QUESTIONSTEP_ACTIVITY + " was deleted.";
            activitydetails = "Question step was deleted. (Study ID = " + customStudyId + ")";
          } else if (questionnairesStepsBo
              .getStepType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
            activity = FdahpStudyDesignerConstants.FORMSTEP_ACTIVITY + " was deleted.";
            activitydetails = "Form step was deleted .(Study ID = " + customStudyId + ")";
          }
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      } else {
        // doing the hard delete before study launch
        message =
            deleteQuestionnaireStep(
                stepId,
                questionnaireId,
                stepType,
                customStudyId,
                sessionObject,
                session,
                transaction);
      }
      // Reset destination steps in Questionnaire Starts
      searchQuery =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 order by QSBO.sequenceNo ASC";

      questionnaireStepList =
          session.createQuery(searchQuery).setInteger("questionnaireId", questionnaireId).list();
      if (null != questionnaireStepList && !questionnaireStepList.isEmpty()) {
        if (questionnaireStepList.size() == 1) {
          questionnaireStepList.get(0).setDestinationStep(0);
          questionnaireStepList.get(0).setSequenceNo(1);
          session.update(questionnaireStepList.get(0));
        } else {
          int i;
          for (i = 0; i < questionnaireStepList.size() - 1; i++) {
            questionnaireStepList
                .get(i)
                .setDestinationStep(questionnaireStepList.get(i + 1).getStepId());
            questionnaireStepList.get(i).setSequenceNo(i + 1);
            session.update(questionnaireStepList.get(i));
          }
          questionnaireStepList.get(i).setDestinationStep(0);
          questionnaireStepList.get(i).setSequenceNo(i + 1);
          session.update(questionnaireStepList.get(i));
        }
      }

      String questionResponseQuery =
          "update response_sub_type_value rs,questionnaires_steps q set rs.destination_step_id = NULL "
              + "where rs.response_type_id=q.instruction_form_id and q.step_type=:stepType "
              + " and q.questionnaires_id=:questionnaireId "
              + " and rs.active=1 and q.active=1";
      query =
          session
              .createSQLQuery(questionResponseQuery)
              .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP)
              .setInteger("questionnaireId", questionnaireId);
      query.executeUpdate();

      String questionConditionResponseQuery =
          "update questions qs,questionnaires_steps q,response_type_value rs  set qs.status = 0 where"
              + " rs.questions_response_type_id=q.instruction_form_id and q.step_type=:stepType "
              + " and q.questionnaires_id=:questionnaireId "
              + " and qs.id=q.instruction_form_id and qs.active=1 and rs.active=1 and q.active=1 and rs.formula_based_logic='Yes'";

      query =
          session
              .createSQLQuery(questionConditionResponseQuery)
              .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP)
              .setInteger("questionnaireId", questionnaireId);
      query.executeUpdate();

      // Reset destination steps in Questionnaire Ends
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - deleteQuestionnaireStep");
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteQuestionnaireStep() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireStep() - Ends");
    return message;
  }

  @SuppressWarnings("unchecked")
  private void validatePreLoadLogicForOtherSteps(Session session, int deletionId) {
    List<QuestionnairesStepsBo> stepsBos = session.createQuery("from QuestionnairesStepsBo where active=true and destinationTrueAsGroup=:stepId and stepOrGroup='step'")
            .setParameter("stepId", deletionId)
            .list();
    for (QuestionnairesStepsBo stepsBo : stepsBos) {
      stepsBo.setStatus(false);
      session.update(stepsBo);
      QuestionnaireBo questionnaireBo = session.get(QuestionnaireBo.class, stepsBo.getQuestionnairesId());
      if (questionnaireBo != null) {
        questionnaireBo.setStatus(false);
        session.update(questionnaireBo);
      }
      String stepType = stepsBo.getStepType();
      int id = stepsBo.getInstructionFormId();
      if (FdahpStudyDesignerConstants.QUESTION_STEP.equals(stepType)) {
        QuestionsBo questionsBo = session.get(QuestionsBo.class, id);
        if (questionsBo != null) {
          questionsBo.setStatus(false);
          session.update(questionsBo);
        }
      } else if (FdahpStudyDesignerConstants.INSTRUCTION_STEP.equals(stepType)) {
        InstructionsBo instructionsBo = session.get(InstructionsBo.class, id);
        if (instructionsBo != null) {
          instructionsBo.setStatus(false);
          session.update(instructionsBo);
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void validatePipingLogicForOtherSteps(Session session, int deletionId) {
    List<QuestionnairesStepsBo> stepsBos = session.createQuery("from QuestionnairesStepsBo where active=true and pipingSourceQuestionKey=:stepId")
            .setParameter("stepId", deletionId)
            .list();
    for (QuestionnairesStepsBo stepsBo : stepsBos) {
      stepsBo.setStatus(false);
      session.update(stepsBo);
      QuestionnaireBo questionnaireBo = session.get(QuestionnaireBo.class, stepsBo.getQuestionnairesId());
      if (questionnaireBo != null) {
        questionnaireBo.setStatus(false);
        session.update(questionnaireBo);
      }
      String stepType = stepsBo.getStepType();
      int id = stepsBo.getInstructionFormId();
      if (FdahpStudyDesignerConstants.QUESTION_STEP.equals(stepType)) {
        QuestionsBo questionsBo = session.get(QuestionsBo.class, id);
        if (questionsBo != null) {
          questionsBo.setStatus(false);
          session.update(questionsBo);
        }
      } else if (FdahpStudyDesignerConstants.INSTRUCTION_STEP.equals(stepType)) {
        InstructionsBo instructionsBo = session.get(InstructionsBo.class, id);
        if (instructionsBo != null) {
          instructionsBo.setStatus(false);
          session.update(instructionsBo);
        }
      }
    }
  }

  /**
   * Delete questionnaire step inside questionnaire of an study before publish
   *
   * @author BTC
   * @param stepId , {@link QuestionnairesStepsBo}
   * @param questionnaireId , {@link QuestionnaireBo}
   * @param stepType , {@link QuestionnairesStepsBo}
   * @param customStudyId , {@link StudyBo}
   * @param sessionObject , {@link SessionObject}
   * @param session , {@link Session}
   * @param transaction , {@link Transaction}
   * @return String Success/Failure
   */
  public String deleteQuestionnaireStep(
      Integer stepId,
      Integer questionnaireId,
      String stepType,
      String customStudyId,
      SessionObject sessionObject,
      Session session,
      Transaction transaction) {
    String message = FdahpStudyDesignerConstants.FAILURE;
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireStep(session,transction) - starts");
    String activitydetails = "";
    String activity = "";
    String searchQuery = null;
    QuestionnairesStepsBo questionnairesStepsBo = null;
    try {
      searchQuery =
          "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:stepId "
              + " and QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.stepType=:stepType ";
      questionnairesStepsBo =
          (QuestionnairesStepsBo)
              session
                  .createQuery(searchQuery)
                  .setInteger("stepId", stepId)
                  .setInteger("questionnaireId", questionnaireId)
                  .setString("stepType", stepType)
                  .uniqueResult();
      if (questionnairesStepsBo != null) {
        String updateQuery =
            "update QuestionnairesStepsBo QSBO set QSBO.sequenceNo=QSBO.sequenceNo-1,QSBO.modifiedBy=:userId "
                + ",QSBO.modifiedOn=:currentDateAndTime "
                + " where QSBO.questionnairesId=:questionnairesId "
                + " and QSBO.active=1 and QSBO.sequenceNo >=:sequenceNo ";
        query =
            session
                .createQuery(updateQuery)
                .setInteger("userId", sessionObject.getUserId())
                .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
                .setInteger("questionnairesId", questionnairesStepsBo.getQuestionnairesId())
                .setInteger("sequenceNo", questionnairesStepsBo.getSequenceNo());
        query.executeUpdate();

        if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.INSTRUCTION_STEP)) {
          String deleteQuery = "delete InstructionsBo IBO where IBO.id=:stepId ";
          query = session.createQuery(deleteQuery).setInteger("stepId", stepId);
          query.executeUpdate();
          activity = FdahpStudyDesignerConstants.INSTRUCTION_ACTIVITY + " was deleted.";
          activitydetails = "Instruction Step was deleted.(Study ID =" + customStudyId + ")";
          auditLogDAO.saveToAuditLog(
              session,
              transaction,
              sessionObject,
              activity,
              activitydetails,
              "StudyQuestionnaireDAOImpl - deleteQuestionnaireStep");

        } else if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
          String deleteQuery = "delete QuestionsBo QBO where QBO.id=:stepId ";
          query = session.createQuery(deleteQuery).setInteger("stepId", stepId);
          query.executeUpdate();
          activity = FdahpStudyDesignerConstants.QUESTIONSTEP_ACTIVITY + " was deleted.";
          activitydetails = "Question Step was deleted.(Study ID =" + customStudyId + ")";
          auditLogDAO.saveToAuditLog(
              session,
              transaction,
              sessionObject,
              activity,
              activitydetails,
              "StudyQuestionnaireDAOImpl - deleteQuestionnaireStep");

          String deleteResponse =
              "delete QuestionReponseTypeBo QRBO where QRBO.questionsResponseTypeId=:stepId ";
          query = session.createQuery(deleteResponse).setInteger("stepId", stepId);
          query.executeUpdate();

          String deleteSubResponse =
              "delete QuestionResponseSubTypeBo QRSBO  where QRSBO.responseTypeId=:stepId ";
          query = session.createQuery(deleteSubResponse).setInteger("stepId", stepId);
          query.executeUpdate();

        } else if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
          String subQuery =
              "select FMBO.questionId from FormMappingBo FMBO where FMBO.formId=:stepId ";
          query = session.createQuery(subQuery).setInteger("stepId", stepId);
          if (query.list() != null && !query.list().isEmpty()) {
            String deleteQuery = "delete QuestionsBo QBO where QBO.id IN (" + subQuery + ")";
            query = session.createQuery(deleteQuery).setInteger("stepId", stepId);
            query.executeUpdate();

            String deleteResponse =
                "delete QuestionReponseTypeBo QRBO where QRBO.questionsResponseTypeId IN ("
                    + subQuery
                    + ")";
            query = session.createQuery(deleteResponse).setInteger("stepId", stepId);
            query.executeUpdate();

            String deleteSubResponse =
                "delete QuestionResponseSubTypeBo QRSBO  where QRSBO.responseTypeId IN ("
                    + subQuery
                    + ")";
            query = session.createQuery(deleteSubResponse).setInteger("stepId", stepId);
            query.executeUpdate();
          }

          String formMappingDelete = "delete FormMappingBo FMBO where FMBO.formId=:stepId ";
          query = session.createQuery(formMappingDelete).setInteger("stepId", stepId);
          query.executeUpdate();

          String formDelete = "delete FormBo FBO where FBO.formId=:stepId ";
          query = session.createQuery(formDelete).setInteger("stepId", stepId);
          query.executeUpdate();
          activity = FdahpStudyDesignerConstants.FORMSTEP_ACTIVITY + " was deleted.";
          activitydetails = "Form Step was deleted.(Study ID =" + customStudyId + ")";
          auditLogDAO.saveToAuditLog(
              session,
              transaction,
              sessionObject,
              activity,
              activitydetails,
              "StudyQuestionnaireDAOImpl - deleteQuestionnaireStep");
        }
        session.delete(questionnairesStepsBo);
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error(
          "StudyQuestionnaireDAOImpl - deleteQuestionnaireStep(session,transction) - ERROR ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireStep(session,transction) - Ends");
    return message;
  }

  /**
   * Delete the Questionnaire in Study
   *
   * @author BTC
   * @param Integer , studyId in {@link StudyBo}
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return String : SUCCESS or FAILURE
   */
  @Override
  public String deleteQuestuionnaireInfo(
      Integer studyId, Integer questionnaireId, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String activitydetails = "";
    String activity = "";
    StudyVersionBo studyVersionBo = null;
    QuestionnaireBo questionnaireBo = null;
    AnchorDateTypeBo anchorDateTypeBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      query =
          session
              .getNamedQuery("getStudyByCustomStudyId")
              .setMaxResults(1)
              .setString("customStudyId", customStudyId);
      query.setMaxResults(1);
      studyVersionBo = (StudyVersionBo) query.uniqueResult();

      questionnaireBo = (QuestionnaireBo) session.get(QuestionnaireBo.class, questionnaireId);
      if (null != questionnaireBo && null != questionnaireBo.getAnchorDateId()) {
        anchorDateTypeBo =
            (AnchorDateTypeBo)
                session.get(AnchorDateTypeBo.class, questionnaireBo.getAnchorDateId());
      }

      // deleting from language table
      session
          .createQuery("delete from QuestionnaireLangBO where questionnaireLangPK.id=:id")
          .setInteger("id", questionnaireId)
          .executeUpdate();

      // delete anchordate from question start
      /** * ------------------ * */
      boolean isChange = true;
      message =
          updateAnchordateInQuestionnaire(
              session,
              transaction,
              studyVersionBo,
              questionnaireId,
              sessionObject,
              studyId,
              null,
              null,
              "",
              isChange);
      if (!message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
        return message;
      }
      /** **** ----------------------- * */
      // delete anchordate from question end

      if (studyVersionBo != null) {
        // doing the soft delete after study launch
        query =
            session
                .createSQLQuery(
                    "CALL deleteQuestionnaire(:questionnaireId,:modifiedOn,:modifiedBy,:studyId)")
                .setInteger("questionnaireId", questionnaireId)
                .setString("modifiedOn", FdahpStudyDesignerUtil.getCurrentDateTime())
                .setInteger("modifiedBy", sessionObject.getUserId())
                .setInteger("studyId", studyId);
        query.executeUpdate();
        message = FdahpStudyDesignerConstants.SUCCESS;
      } else {
        // doing the hard delete before study launch
        message =
            deleteQuestuionnaireInfo(studyId, questionnaireId, customStudyId, session, transaction);
      }
      if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {

        /*
         * if (null != questionnaireBo && null != questionnaireBo.getAnchorDateId()) {
         * anchorDateTypeBo = (AnchorDateTypeBo) session.get(AnchorDateTypeBo.class,
         * questionnaireBo.getAnchorDateId());
         */
        if (null != anchorDateTypeBo
            && null != anchorDateTypeBo.getParticipantProperty()
            && anchorDateTypeBo.getParticipantProperty()) {
          query =
              session.createQuery(
                  "select count(*) from QuestionnaireBo QBO  where QBO.studyId=:studyId and QBO.anchorDateId=:anchorDateId and QBO.active=1");
          query.setInteger("studyId", studyId);
          query.setInteger("anchorDateId", questionnaireBo.getAnchorDateId());
          Long count = (Long) query.uniqueResult();
          if (count < 1) {
            query =
                session.createQuery(
                    "Update ParticipantPropertiesBO PBO SET PBO.isUsedInQuestionnaire = 0 where PBO.anchorDateId=:anchorDateId");
            query.setInteger("anchorDateId", questionnaireBo.getAnchorDateId());
            query.executeUpdate();
          }
        }
        /* } */
      }
      activity = FdahpStudyDesignerConstants.QUESTIONNAIRE_ACTIVITY + " was deleted.";
      activitydetails =
          FdahpStudyDesignerConstants.QUESTIONNAIRE_ACTIVITY
              + " was deleted. (Study ID = "
              + customStudyId
              + ")";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo");

      queryString =
          "DELETE From NotificationBO where questionnarieId=:questionnaireId AND notificationSent=false";
      session
          .createQuery(queryString)
          .setInteger("questionnaireId", questionnaireId)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo() - Ends");
    return message;
  }

  /**
   * Delete the Questionnaire in study before publish
   *
   * @author BTC
   * @param studyId in {@link StudyBo}
   * @param questionnaireId in {@link QuestionnaireBo}
   * @param customStudyId in {@link StudyBo}
   * @param {@link Session}
   * @param {@link Transaction}
   * @return String Success/Failure
   */
  public String deleteQuestuionnaireInfo(
      Integer studyId,
      Integer questionnaireId,
      String customStudyId,
      Session session,
      Transaction transaction) {
    logger.info(
        "StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo(session,transction) - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    int count = 0;
    try {
      String deleteInsQuery =
          "delete InstructionsBo IBO where IBO.id IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType) ";
      query =
          session
              .createQuery(deleteInsQuery)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.INSTRUCTION_STEP);
      query.executeUpdate();

      String deleteQuesQuery =
          "delete QuestionsBo QBO where QBO.id IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType) ";
      query =
          session
              .createQuery(deleteQuesQuery)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP);
      query.executeUpdate();

      String deleteResponse =
          "delete QuestionReponseTypeBo QRBO where QRBO.questionsResponseTypeId IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType) ";
      query =
          session
              .createQuery(deleteResponse)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP);
      query.executeUpdate();

      String deleteSubResponse =
          "delete QuestionResponseSubTypeBo QRSBO  where QRSBO.responseTypeId IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType ) ";
      query =
          session
              .createQuery(deleteSubResponse)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP);
      query.executeUpdate();

      String subQuery =
          "select FMBO.questionId from FormMappingBo FMBO where FMBO.formId IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType ) ";
      query =
          session
              .createQuery(subQuery)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
      if (query.list() != null && !query.list().isEmpty()) {

        String deleteFormResponse =
            "delete QuestionReponseTypeBo QRBO where QRBO.questionsResponseTypeId IN ("
                + subQuery
                + ")";
        query =
            session
                .createQuery(deleteFormResponse)
                .setInteger("questionnaireId", questionnaireId)
                .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
        query.executeUpdate();

        String deleteFormSubResponse =
            "delete QuestionResponseSubTypeBo QRSBO  where QRSBO.responseTypeId IN ("
                + subQuery
                + ")";
        query =
            session
                .createQuery(deleteFormSubResponse)
                .setInteger("questionnaireId", questionnaireId)
                .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
        query.executeUpdate();

        String deleteFormQuery = "delete QuestionsBo QBO where QBO.id IN (" + subQuery + ")";
        query =
            session
                .createQuery(deleteFormQuery)
                .setInteger("questionnaireId", questionnaireId)
                .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
        query.executeUpdate();
      }

      String formMappingDelete =
          "delete FormMappingBo FMBO where FMBO.formId IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType) ";
      query =
          session
              .createQuery(formMappingDelete)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
      query.executeUpdate();

      String formDelete =
          "delete FormBo FBO where FBO.formId IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
              + " and QSBO.active=1 and QSBO.stepType=:stepType) ";
      query =
          session
              .createQuery(formDelete)
              .setInteger("questionnaireId", questionnaireId)
              .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
      query.executeUpdate();

      String searchQuery =
          "delete QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId";
      query = session.createQuery(searchQuery).setInteger("questionnaireId", questionnaireId);
      query.executeUpdate();

      String deletecustomFreQuery =
          "delete from questionnaires_custom_frequencies where questionnaires_id=:questionnaireId ";
      query =
          session
              .createSQLQuery(deletecustomFreQuery)
              .setInteger("questionnaireId", questionnaireId);
      query.executeUpdate();

      String deleteFreQuery =
          "delete from questionnaires_frequencies where questionnaires_id=:questionnaireId ";
      query = session.createSQLQuery(deleteFreQuery).setInteger("questionnaireId", questionnaireId);
      query.executeUpdate();

      String deleteQuery =
          "delete QuestionnaireBo QBO where QBO.studyId=:studyId "
              + " and QBO.id=:questionnaireId ";
      query =
          session
              .createQuery(deleteQuery)
              .setInteger("studyId", studyId)
              .setInteger("questionnaireId", questionnaireId);
      count = query.executeUpdate();

      if (count > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }

    } catch (Exception e) {
      transaction.rollback();
      logger.error(
          "StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo(session,transction) - ERROR ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestuionnaireInfo(session,transction) - Ends");
    return message;
  }

  /**
   * For QA of response type that results in the data type 'double',the admin can also choose to
   * give the user a provision to allow the app to read the response from HealthKit this method is
   * used to get the pre-defined list of HealthKit quantity data types
   *
   * @author BTC
   * @return List of HealthKityKeyInfo
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<HealthKitKeysInfo> getHeanlthKitKeyInfoList() {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - Starts");
    Session session = null;
    List<HealthKitKeysInfo> healthKitKeysInfoList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query = session.getNamedQuery("getHealthKitKeyInfo");
      healthKitKeysInfoList = query.list();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - Ends");
    return healthKitKeysInfoList;
  }

  /**
   * Instruction step page in questionnaire.Lays down instructions for the user in mobile app.Which
   * contains the short title instruction title and text
   *
   * @author BTC
   * @param Integer , instructionId in {@link InstructionsBo}
   * @param String , questionnaireShortTitle in {@link QuestionnairesStepsBo}
   * @param String , customStudyId in {@link StudyBo}
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @return {@link InstructionsBo}
   */
  @Override
  public InstructionsBo getInstructionsBo(
      Integer instructionId,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - getInstructionsBo - Starts");
    Session session = null;
    InstructionsBo instructionsBo = null;
    QuestionnairesStepsBo questionnairesStepsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      instructionsBo = (InstructionsBo) session.get(InstructionsBo.class, instructionId);
      if (instructionsBo != null) {
        if (questionnaireId != null) {
          query =
              session
                  .createQuery(
                      "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:instructionFormId "
                          + " and QSBO.stepType=:stepType "
                          + " and QSBO.active=1 and QSBO.questionnairesId=:questionnaireId ")
                  .setInteger("instructionFormId", instructionsBo.getId())
                  .setInteger("questionnaireId", questionnaireId)
                  .setString("stepType", FdahpStudyDesignerConstants.INSTRUCTION_STEP);
        } else {
          query =
              session
                  .getNamedQuery("getQuestionnaireStep")
                  .setInteger("instructionFormId", instructionsBo.getId())
                  .setString("stepType", FdahpStudyDesignerConstants.INSTRUCTION_STEP);
        }
        questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();

        if (StringUtils.isNotEmpty(questionnaireShortTitle)) {
          // Duplicate ShortTitle per QuestionnaireStepBo Start
          BigInteger shortTitleCount =
              (BigInteger)
                  session
                      .createSQLQuery(
                          "select count(*) from questionnaires_steps qs where qs.questionnaires_id  "
                              + " in(select q.id from questionnaires q where q.short_title=:questionnaireShortTitle "
                              + " and q.active=1 and q.is_live=1 and q.custom_study_id=:customStudyId ) "
                              + "and qs.step_short_title = :shortTitle "
                              + " and qs.active=1")
                      .setString("questionnaireShortTitle", questionnaireShortTitle)
                      .setString("customStudyId", customStudyId)
                      .setString("shortTitle", questionnairesStepsBo.getStepShortTitle())
                      .uniqueResult();
          if (shortTitleCount != null && shortTitleCount.intValue() > 0)
            questionnairesStepsBo.setIsShorTitleDuplicate(shortTitleCount.intValue());
          else questionnairesStepsBo.setIsShorTitleDuplicate(0);
        } else {
          questionnairesStepsBo.setIsShorTitleDuplicate(0);
        }
        // Duplicate ShortTitle per QuestionnaireStepBo End
        instructionsBo.setQuestionnairesStepsBo(questionnairesStepsBo);
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getInstructionsBo() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getInstructionsBo - Ends");
    return instructionsBo;
  }

  /**
   * Get the condition(formula) of question which are created in the response level attributes of
   * question in formula based destination enabled section
   *
   * @author BTC
   * @param session {@link Session}
   * @param questionId in {@link QuestionsBo}
   * @return List of {@link QuestionConditionBranchBo}
   */
  @Override
  @SuppressWarnings({"unchecked"})
  public List<QuestionConditionBranchBo> getQuestionConditionalBranchingLogic(
      Session session, Integer questionId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionConditionalBranchingLogic() - Starts");
    List<QuestionConditionBranchBo> questionConditionBranchList = null;
    List<QuestionConditionBranchBo> newQuestionConditionBranchList = null;
    Session newSession = null;
    try {
      if (session == null) {
        newSession = hibernateTemplate.getSessionFactory().openSession();
      }
      String searchQuery =
          "From QuestionConditionBranchBo QCBO where QCBO.questionId=:questionId "
              + " order by QCBO.sequenceNo ASC";
      if (newSession != null) {
        query = newSession.createQuery(searchQuery).setInteger("questionId", questionId);
      } else {
        query = session.createQuery(searchQuery).setInteger("questionId", questionId);
      }
      questionConditionBranchList = query.list();
      if (session == null) {
        newQuestionConditionBranchList = new ArrayList<>();
        newQuestionConditionBranchList = questionConditionBranchList;
      } else {
        if (questionConditionBranchList != null && !questionConditionBranchList.isEmpty()) {
          newQuestionConditionBranchList = new ArrayList<>();
          for (QuestionConditionBranchBo questionConditionBranchBo : questionConditionBranchList) {
            if (questionConditionBranchBo.getInputType() != null
                && (questionConditionBranchBo.getInputType().equalsIgnoreCase("MF")
                    || questionConditionBranchBo.getInputType().equalsIgnoreCase("F"))) {
              List<QuestionConditionBranchBo> conditionBranchList = new ArrayList<>();
              for (QuestionConditionBranchBo conditionBranchBo : questionConditionBranchList) {
                if (questionConditionBranchBo
                    .getSequenceNo()
                    .equals(conditionBranchBo.getParentSequenceNo())) {
                  conditionBranchList.add(conditionBranchBo);
                }
              }
              questionConditionBranchBo.setQuestionConditionBranchBos(conditionBranchList);
              newQuestionConditionBranchList.add(questionConditionBranchBo);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error(
          "StudyQuestionnaireDAOImpl - getQuestionConditionalBranchingLogic() - ERROR ", e);
    } finally {
      if (null != newSession) {
        newSession.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionConditionalBranchingLogic() - Ends");
    return newQuestionConditionBranchList;
  }

  /**
   * Load the questionnaire of study with all the steps(instruction,question,form) with schedule
   * information. Each step corresponds to one screen on the mobile app.There can be multiple types
   * of QA in a questionnaire depending on the type of response format selected per QA.
   *
   * @author BTC
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return QuestionnaireBo {@link QuestionnaireBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public QuestionnaireBo getQuestionnaireById(Integer questionnaireId, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireById() - Starts");
    Session session = null;
    QuestionnaireBo questionnaireBo = null;

    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      questionnaireBo = (QuestionnaireBo) session.get(QuestionnaireBo.class, questionnaireId);
      if (null != questionnaireBo) {
        if (StringUtils.isNotEmpty(customStudyId)) {
          // Duplicate ShortTitle per QuestionnaireBo Start
          BigInteger shortTitleCount =
              (BigInteger)
                  session
                      .createSQLQuery(
                          "select count(*) from questionnaires "
                              + "where short_title=:shortTilte "
                              + " and custom_study_id =:customStudyId"
                              + " and active=1 and is_live=1")
                      .setString("shortTilte", questionnaireBo.getShortTitle())
                      .setString("customStudyId", customStudyId)
                      .uniqueResult();
          if (shortTitleCount != null
              && shortTitleCount.intValue() > 0
              && questionnaireBo.getScheduleType().equals("AnchorDate")) {
            questionnaireBo.setShortTitleDuplicate(shortTitleCount.intValue());
          } else if (shortTitleCount != null
              && shortTitleCount.intValue() > 0
              && questionnaireBo.getScheduleType().equals("Regular")) {
            if (questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_DAILY)
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MONTHLY)
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_WEEKLY)
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONGOING)) {
              questionnaireBo.setShortTitleDuplicate(shortTitleCount.intValue());
            } else {
              questionnaireBo.setShortTitleDuplicate(0);
            }
          } else {
            questionnaireBo.setShortTitleDuplicate(0);
          }

        } else {
          questionnaireBo.setShortTitleDuplicate(0);
        }
        // Duplicate ShortTitle per QuestionnaireBo End
        String searchQuery = "";
        if (null != questionnaireBo.getFrequency() && !questionnaireBo.getFrequency().isEmpty()) {
          if (questionnaireBo
              .getFrequency()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)) {
            searchQuery =
                "From QuestionnaireCustomScheduleBo QCSBO where QCSBO.questionnairesId=:questionnairesId ";
            query =
                session
                    .createQuery(searchQuery)
                    .setInteger("questionnairesId", questionnaireBo.getId());
            List<QuestionnaireCustomScheduleBo> questionnaireCustomScheduleList = query.list();
            questionnaireBo.setQuestionnaireCustomScheduleBo(questionnaireCustomScheduleList);
          } else {
            searchQuery =
                "From QuestionnairesFrequenciesBo QFBO where QFBO.questionnairesId=:questionnairesId ";
            query =
                session
                    .createQuery(searchQuery)
                    .setInteger("questionnairesId", questionnaireBo.getId());
            if (questionnaireBo
                .getFrequency()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_DAILY)) {
              List<QuestionnairesFrequenciesBo> questionnairesFrequenciesList = query.list();
              questionnaireBo.setQuestionnairesFrequenciesList(questionnairesFrequenciesList);
            } else {
              QuestionnairesFrequenciesBo questionnairesFrequenciesBo =
                  (QuestionnairesFrequenciesBo) query.uniqueResult();
              questionnaireBo.setQuestionnairesFrequenciesBo(questionnairesFrequenciesBo);
            }
          }
        }
        if (questionnaireBo.getVersion() != null) {
          questionnaireBo.setQuestionnarieVersion(" (V" + questionnaireBo.getVersion() + ")");
        }
      }

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireById() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireById() - Ends");
    return questionnaireBo;
  }

  /**
   * This method is used to get the forward question step of an questionnaire based on sequence
   * no.Thease questions are populated in the destination step drop down in the step level
   * attributes of question step,from step and instruction step to select the destination step if
   * branching is enabled for that questionnaire
   *
   * @author BTC
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @param Integer , sequenceNo in {@link QuestionnairesStepsBo}
   * @return List : {@link QuestionnairesStepsBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<QuestionnairesStepsBo> getQuestionnairesStepsList(
      Integer questionnaireId, Integer sequenceNo) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireStepList() - Starts");
    Session session = null;
    List<QuestionnairesStepsBo> questionnairesStepsList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .getNamedQuery("getForwardQuestionnaireSteps")
              .setInteger("questionnairesId", questionnaireId)
              .setInteger("sequenceNo", sequenceNo);
      questionnairesStepsList = query.list();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - deleteFromStepQuestion() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    return questionnairesStepsList;
  }

  /**
   * Load the Question step page in questionnaire which contains the question and answer. Which
   * Carries one QA per screen in Mobile app
   *
   * @author BTC
   * @param Integer , stepId in {@link QuestionnairesStepsBo}
   * @param String , stepType in {@link QuestionnairesStepsBo}
   * @param String , questionnaireShortTitle in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public QuestionnairesStepsBo getQuestionnaireStep(
      Integer stepId,
      String stepType,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireStep() - Starts");
    Session session = null;
    QuestionnairesStepsBo questionnairesStepsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (questionnaireId != null) {
        query =
            session
                .createQuery(
                    "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:instructionFormId "
                        + " and QSBO.stepType=:stepType "
                        + " and QSBO.active=1 and QSBO.questionnairesId=:questionnaireId ")
                .setInteger("instructionFormId", stepId)
                .setString("stepType", stepType)
                .setInteger("questionnaireId", questionnaireId);
      } else {
        query =
            session
                .getNamedQuery("getQuestionnaireStep")
                .setInteger("instructionFormId", stepId)
                .setString("stepType", stepType);
      }
      questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
      if (null != questionnairesStepsBo && questionnairesStepsBo.getStepType() != null) {
        if (StringUtils.isNotEmpty(questionnaireShortTitle)) {
          // Duplicate ShortTitle per QuestionnaireStepBo Start
          BigInteger shortTitleCount =
              (BigInteger)
                  session
                      .createSQLQuery(
                          "select count(*) from questionnaires_steps qs where qs.questionnaires_id  "
                              + " in(select q.id from questionnaires q where q.short_title=:questionnaireShortTitle "
                              + " and q.active=1 and q.is_live=1 and q.custom_study_id=:customStudyId )"
                              + " and qs.step_short_title=:shortTitle ")
                      .setString("questionnaireShortTitle", questionnaireShortTitle)
                      .setString("customStudyId", customStudyId)
                      .setString("shortTitle", questionnairesStepsBo.getStepShortTitle())
                      .uniqueResult();
          if (shortTitleCount != null && shortTitleCount.intValue() > 0)
            questionnairesStepsBo.setIsShorTitleDuplicate(shortTitleCount.intValue());
          else questionnairesStepsBo.setIsShorTitleDuplicate(0);
        } else {
          questionnairesStepsBo.setIsShorTitleDuplicate(0);
        }
        // Duplicate ShortTitle per QuestionnaireStepBo End

        if (questionnairesStepsBo
            .getStepType()
            .equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
          // get the one question step of questionnaire
          QuestionsBo questionsBo = null;
          query = session.getNamedQuery("getQuestionStep").setInteger("stepId", stepId);
          questionsBo = (QuestionsBo) query.uniqueResult();
          if (questionsBo != null && questionsBo.getId() != null) {
            if (StringUtils.isNotEmpty(questionnaireShortTitle)) {
              // Duplicate statShortTitle per questionsBo Start
              if (StringUtils.isNotEmpty(questionsBo.getStatShortName())) {
                BigInteger quesionStatshortTitleCount =
                    (BigInteger)
                        session
                            .createSQLQuery(
                                "select count(*) From questions QBO,questionnaires_steps QSBO,questionnaires Q where QBO.id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.short_title=:questionnaireShortTitle "
                                    + " and Q.active=1 and Q.is_live=1 and Q.custom_study_id=:customStudyId "
                                    + " and QSBO.step_type='Question' and QBO.stat_short_name=:shortName "
                                    + " and QBO.active=1")
                            .setString("questionnaireShortTitle", questionnaireShortTitle)
                            .setString("customStudyId", customStudyId)
                            .setString("shortName", questionsBo.getStatShortName())
                            .uniqueResult();
                if (quesionStatshortTitleCount != null && quesionStatshortTitleCount.intValue() > 0)
                  questionsBo.setIsStatShortNameDuplicate(quesionStatshortTitleCount.intValue());
                else questionsBo.setIsStatShortNameDuplicate(0);
              }
            } else {
              questionsBo.setIsStatShortNameDuplicate(0);
            }
            // Duplicate statShortTitle per questionsBo End

            // get the response level attributes values of an
            // questions
            QuestionReponseTypeBo questionReponseTypeBo = null;
            logger.info(
                "StudyQuestionnaireDAOImpl - getQuestionnaireStep() - questionsResponseTypeId:"
                    + questionsBo.getId());
            query =
                session
                    .getNamedQuery("getQuestionResponse")
                    .setInteger("questionsResponseTypeId", questionsBo.getId());
            query.setMaxResults(1);
            questionReponseTypeBo = (QuestionReponseTypeBo) query.uniqueResult();
            if (questionReponseTypeBo != null
                && questionReponseTypeBo.getStyle() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getStyle())) {
              questionReponseTypeBo.setStyle(questionReponseTypeBo.getStyle());
              if ((FdahpStudyDesignerConstants.DATE)
                  .equalsIgnoreCase(questionReponseTypeBo.getStyle())) {
                if (questionReponseTypeBo.getMinDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getMinDate())) {
                  questionReponseTypeBo.setMinDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getMinDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE,
                          FdahpStudyDesignerConstants.UI_SDF_DATE));
                }
                if (questionReponseTypeBo.getMaxDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getMaxDate())) {
                  questionReponseTypeBo.setMaxDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getMaxDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE,
                          FdahpStudyDesignerConstants.UI_SDF_DATE));
                }
                if (questionReponseTypeBo.getDefaultDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getDefaultDate())) {
                  questionReponseTypeBo.setDefaultDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getDefaultDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE,
                          FdahpStudyDesignerConstants.UI_SDF_DATE));
                }
              } else if ((FdahpStudyDesignerConstants.DATE_TIME)
                  .equalsIgnoreCase(questionReponseTypeBo.getStyle())) {
                if (questionReponseTypeBo.getMinDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getMinDate())) {
                  questionReponseTypeBo.setMinDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getMinDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                          FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
                }
                if (questionReponseTypeBo.getMaxDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getMaxDate())) {
                  questionReponseTypeBo.setMaxDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getMaxDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                          FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
                }
                if (questionReponseTypeBo.getDefaultDate() != null
                    && StringUtils.isNotEmpty(questionReponseTypeBo.getDefaultDate())) {
                  questionReponseTypeBo.setDefaultDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionReponseTypeBo.getDefaultDate(),
                          FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                          FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
                }
              }
            }
            if (questionReponseTypeBo != null
                && questionReponseTypeBo.getFormulaBasedLogic() != null
                && questionReponseTypeBo
                    .getFormulaBasedLogic()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              List<QuestionConditionBranchBo> questionConditionBranchList =
                  getQuestionConditionalBranchingLogic(session, questionsBo.getId());
              questionnairesStepsBo.setQuestionConditionBranchBoList(questionConditionBranchList);
            }
            questionnairesStepsBo.setQuestionReponseTypeBo(questionReponseTypeBo);

            List<QuestionResponseSubTypeBo> questionResponseSubTypeList = null;
            query =
                session
                    .getNamedQuery("getQuestionSubResponse")
                    .setInteger("responseTypeId", questionsBo.getId());
            questionResponseSubTypeList = query.list();
            // appending the current date time to the image url
            if (null != questionResponseSubTypeList && !questionResponseSubTypeList.isEmpty()) {
              for (QuestionResponseSubTypeBo s : questionResponseSubTypeList) {
                if (FdahpStudyDesignerUtil.isNotEmpty(s.getImage())) {
                  if (s.getImage().contains("?v=")) {
                    String imagePathArr[] = s.getImage().split("\\?");
                    s.setImage(imagePathArr[0] + "?v=" + new Date().getTime());
                  } else {
                    s.setImage(s.getImage() + "?v=" + new Date().getTime());
                  }
                }
                if (FdahpStudyDesignerUtil.isNotEmpty(s.getSelectedImage())) {
                  if (s.getSelectedImage().contains("?v=")) {
                    String imagePathArr[] = s.getSelectedImage().split("\\?");
                    s.setSelectedImage(imagePathArr[0] + "?v=" + new Date().getTime());
                  } else {
                    s.setSelectedImage(s.getSelectedImage() + "?v=" + new Date().getTime());
                  }
                }
              }
            }
            questionnairesStepsBo.setQuestionResponseSubTypeList(questionResponseSubTypeList);

            // Phase 2a ancordate start
            if (questionsBo.getAnchorDateId() != null) {
              String name =
                  (String)
                      session
                          .createSQLQuery("select name from anchordate_type where id=:anchorDateId")
                          .setInteger("anchorDateId", questionsBo.getAnchorDateId())
                          .uniqueResult();
              questionsBo.setAnchorDateName(name);
            }
            // phase 2a anchordate end

          }
          questionnairesStepsBo.setQuestionsBo(questionsBo);

        } else if (questionnairesStepsBo
            .getStepType()
            .equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
          // get the one from step of an questionnaire
          String fromQuery =
              "select f.form_id,f.question_id,f.sequence_no, q.id, q.question,q.response_type,q.add_line_chart,q.use_stastic_data,q.status,q.use_anchor_date from questions q, form_mapping f where q.id=f.question_id and f.form_id=:stepId "
                  + " and f.active=1 order by f.form_id";
          Iterator iterator =
              session.createSQLQuery(fromQuery).setInteger("stepId", stepId).list().iterator();
          TreeMap<Integer, QuestionnaireStepBean> formQuestionMap = new TreeMap<>();
          boolean isDone = true;
          while (iterator.hasNext()) {
            Object[] objects = (Object[]) iterator.next();
            Integer formId = (Integer) objects[0];
            Integer sequenceNo = (Integer) objects[2];
            Integer questionId = (Integer) objects[3];
            String questionText = (String) objects[4];
            Integer responseType = (Integer) objects[5];
            String lineChart = (String) objects[6];
            String statData = (String) objects[7];
            Boolean status = (Boolean) objects[8];
            Boolean useAnchorDate = (Boolean) objects[9];
            QuestionnaireStepBean questionnaireStepBean = new QuestionnaireStepBean();
            questionnaireStepBean.setStepId(formId);
            questionnaireStepBean.setQuestionInstructionId(questionId);
            questionnaireStepBean.setTitle(questionText);
            questionnaireStepBean.setSequenceNo(sequenceNo);
            questionnaireStepBean.setStepType(FdahpStudyDesignerConstants.FORM_STEP);
            questionnaireStepBean.setResponseType(responseType);
            questionnaireStepBean.setLineChart(lineChart);
            questionnaireStepBean.setStatData(statData);
            questionnaireStepBean.setStatus(status);
            questionnaireStepBean.setUseAnchorDate(useAnchorDate);
            formQuestionMap.put(sequenceNo, questionnaireStepBean);
            if (!status) {
              isDone = false;
            }
          }
          questionnairesStepsBo.setStatus(isDone);
          questionnairesStepsBo.setFormQuestionMap(formQuestionMap);
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireStep() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireStep() - Ends");
    return questionnairesStepsBo;
  }

  /**
   * Load the questionnaires of study with all the steps(instruction,question,form) with schedule
   * information. Each step corresponds to one screen on the mobile app.There can be multiple types
   * of QA in a questionnaire depending on the type of response format selected per QA.
   *
   * @author BTC
   * @param Integer , questionnaireId uin {@link QuestionnaireBo}
   * @return Map : SortedMap<Integer, QuestionnaireStepBean>
   */
  @SuppressWarnings("unchecked")
  @Override
  public SortedMap<Integer, QuestionnaireStepBean> getQuestionnaireStepList(
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireStepList() - Ends");
    Session session = null;
    List<QuestionnairesStepsBo> questionnairesStepsList = null;
    Map<String, Integer> sequenceNoMap = new HashMap<>();
    SortedMap<Integer, QuestionnaireStepBean> qTreeMap = new TreeMap<>();
    Map<Integer, String> destinationText = new HashMap<>();
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query =
          session
              .getNamedQuery("getQuestionnaireStepList")
              .setInteger("questionnaireId", questionnaireId);
      questionnairesStepsList = query.list();
      List<Integer> instructionIdList = new ArrayList<>();
      List<Integer> questionIdList = new ArrayList<>();
      List<Integer> formIdList = new ArrayList<>();
      Map<String, Integer> destinationMap = new HashMap<>();
      Map<Integer, Boolean> formStatusMap = new HashMap<>();
      destinationText.put(0, "Completion Step");
      // setting the sequenceNo and destination steps to the map based on
      // the individual steps later using this map to set the destination
      // step name
      for (QuestionnairesStepsBo questionaireSteps : questionnairesStepsList) {
        destinationText.put(
            questionaireSteps.getStepId(),
            questionaireSteps.getSequenceNo() + ":" + questionaireSteps.getStepShortTitle());
        switch (questionaireSteps.getStepType()) {
          case FdahpStudyDesignerConstants.INSTRUCTION_STEP:
            instructionIdList.add(questionaireSteps.getInstructionFormId());
            sequenceNoMap.put(
                    questionaireSteps.getInstructionFormId()
                    + FdahpStudyDesignerConstants.INSTRUCTION_STEP,
                questionaireSteps.getSequenceNo());
            Integer defaultInstructionStep = null;
            if (questionaireSteps.getDestinationStep() != null) {
            	defaultInstructionStep = questionaireSteps.getDestinationStep();
              } else {
                long count = (long) session.createQuery("select count(*) from QuestionnairesStepsBo where questionnairesId=:queId")
                        .setParameter("queId", questionaireSteps.getQuestionnairesId())
                        .uniqueResult();
                if (count == questionaireSteps.getSequenceNo()) {
                	defaultInstructionStep = 0;
                } else {
                  QuestionnairesStepsBo defaultStepsDto = (QuestionnairesStepsBo) session.createQuery(
                                  "from QuestionnairesStepsBo where questionnairesId=:queId and sequenceNo=:seqNo")
                          .setParameter("queId", questionaireSteps.getQuestionnairesId())
                          .setParameter("seqNo", questionaireSteps.getSequenceNo()+1)
                          .uniqueResult();
                  if (defaultStepsDto != null) {
                	  defaultInstructionStep = defaultStepsDto.getStepId();
                  }
                }
              }
              destinationMap.put(questionaireSteps.getInstructionFormId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP, defaultInstructionStep);

            break;
          	case FdahpStudyDesignerConstants.QUESTION_STEP:
            questionIdList.add(questionaireSteps.getInstructionFormId());
            sequenceNoMap.put(
                    questionaireSteps.getInstructionFormId()
                    + FdahpStudyDesignerConstants.QUESTION_STEP,
                questionaireSteps.getSequenceNo());
            Integer defaultStep = null;
            if (questionaireSteps.getDestinationStep() != null) {
              defaultStep = questionaireSteps.getDestinationStep();
            } else {
              long count = (long) session.createQuery("select count(*) from QuestionnairesStepsBo where questionnairesId=:queId")
                      .setParameter("queId", questionaireSteps.getQuestionnairesId())
                      .uniqueResult();
              if (count == questionaireSteps.getSequenceNo()) {
                defaultStep = 0;
              } else {
                QuestionnairesStepsBo defaultStepsDto = (QuestionnairesStepsBo) session.createQuery(
                                "from QuestionnairesStepsBo where questionnairesId=:queId and sequenceNo=:seqNo")
                        .setParameter("queId", questionaireSteps.getQuestionnairesId())
                        .setParameter("seqNo", questionaireSteps.getSequenceNo()+1)
                        .uniqueResult();
                if (defaultStepsDto != null) {
                  defaultStep = defaultStepsDto.getStepId();
                }
              }
            }
            destinationMap.put(questionaireSteps.getInstructionFormId() + FdahpStudyDesignerConstants.QUESTION_STEP, defaultStep);

            break;
          	case FdahpStudyDesignerConstants.FORM_STEP:
            formIdList.add(questionaireSteps.getInstructionFormId());
            sequenceNoMap.put(
                    questionaireSteps.getInstructionFormId()
                    + FdahpStudyDesignerConstants.FORM_STEP,
                questionaireSteps.getSequenceNo());
            Integer defaultFormStep = null;
			if (questionaireSteps.getDestinationStep() != null) {
				defaultFormStep = questionaireSteps.getDestinationStep();
			} else {
				long count = (long) session
						.createQuery("select count(*) from QuestionnairesStepsBo where questionnairesId=:queId")
						.setParameter("queId", questionaireSteps.getQuestionnairesId()).uniqueResult();
				if (count == questionaireSteps.getSequenceNo()) {
					defaultFormStep = 0;
				} else {
					QuestionnairesStepsBo defaultStepsDto = (QuestionnairesStepsBo) session
							.createQuery(
									"from QuestionnairesStepsBo where questionnairesId=:queId and sequenceNo=:seqNo")
							.setParameter("queId", questionaireSteps.getQuestionnairesId())
							.setParameter("seqNo", questionaireSteps.getSequenceNo() + 1).uniqueResult();
					if (defaultStepsDto != null) {
						defaultFormStep = defaultStepsDto.getStepId();
					}
				}
			}
			destinationMap.put(questionaireSteps.getInstructionFormId() + FdahpStudyDesignerConstants.FORM_STEP, defaultFormStep);            
            formStatusMap.put(
                questionaireSteps.getInstructionFormId(), questionaireSteps.getStatus());
            break;
          	default:
            break;
        }
      }
      // get the list of instruction step in questionnaire
      if (!instructionIdList.isEmpty()) {
        List<InstructionsBo> instructionsList = null;
        query =
            session.createQuery(
                " from InstructionsBo IBO where IBO.active=1 and IBO.id in ( :instructionIdList )");
        instructionsList = query.setParameterList("instructionIdList", instructionIdList).list();
        if (instructionsList != null && !instructionsList.isEmpty()) {
          for (InstructionsBo instructionsBo : instructionsList) {
            query =
                    session
                            .getNamedQuery("getQuestionnaireGroupStep")
                            .setInteger("instructionFormId", instructionsBo.getId())
                            .setInteger("questionnairesId", questionnaireId);
            QuestionnairesStepsBo questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
            QuestionnaireStepBean questionnaireStepBean = new QuestionnaireStepBean();
            questionnaireStepBean.setStepId(instructionsBo.getId());
            if (questionnairesStepsBo.getGroupFlag() != null && questionnairesStepsBo.getGroupFlag()) {
              GroupsBo groupsBo = this.getGroupIdByStepId(session, questionnairesStepsBo.getStepId());
              if (groupsBo != null) {
                questionnaireStepBean.setGroupId(groupsBo.getId());
                questionnaireStepBean.setGroupName(groupsBo.getGroupName());
              }
            }
            questionnaireStepBean.setDeletionId(questionnairesStepsBo.getStepId());
            questionnaireStepBean.setStepType(FdahpStudyDesignerConstants.INSTRUCTION_STEP);
            questionnaireStepBean.setSequenceNo(
                sequenceNoMap.get(
                    instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP));
            questionnaireStepBean.setTitle(instructionsBo.getInstructionTitle());
            questionnaireStepBean.setStatus(instructionsBo.getStatus());
            questionnaireStepBean.setGroupFlag(questionnairesStepsBo.getGroupFlag());
            questionnaireStepBean.setDestinationStep(
                    destinationMap.get(
                            instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP));
            String stepOrGroup = questionnairesStepsBo.getStepOrGroupPostLoad();
            if(stepOrGroup != null) {
              //setting destinationText if the selected destination step is STEP
              if (FdahpStudyDesignerConstants.STEP.equalsIgnoreCase(stepOrGroup)) {
                questionnaireStepBean.setDestinationText(
                        destinationText.get(
                                destinationMap.get(
                                        instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP)));
              }
              //setting destinationText if the selected destinationstep is GROUP
              else {
                //getting groupautogeneratedId that is stored in destinationStep in order to get the groupName in the groupsBo table
                Integer groupId = destinationMap.get(
                        instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP);
                //get the groupname by passing above id
                String groupName = null;
                query =
                        session.createQuery(
                                " select groupName from GroupsBo GBO where GBO.id =: groupId ");
                groupName = String.valueOf(query.setParameter("groupId", groupId).uniqueResult());
                questionnaireStepBean.setDestinationText("Group:" + groupName);
              }
            } else {
              questionnaireStepBean.setDestinationText(
                      destinationText.get(
                              destinationMap.get(
                                      instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP)));

            }
            qTreeMap.put(
                sequenceNoMap.get(
                    instructionsBo.getId() + FdahpStudyDesignerConstants.INSTRUCTION_STEP),
                questionnaireStepBean);
          }
        }
      }
      // get the list of question step inside the questionnaire
      if (!questionIdList.isEmpty()) {
        List<QuestionsBo> questionsList = null;
        query =
            session.createQuery(
                " from QuestionsBo QBO where QBO.active=1 and QBO.id in ( :questionIdList )");
        questionsList = query.setParameterList("questionIdList", questionIdList).list();
        if (questionsList != null && !questionsList.isEmpty()) {
          for (QuestionsBo questionsBo : questionsList) {
            query =
                    session
                            .getNamedQuery("getQuestionnaireGroupStep")
                            .setInteger("instructionFormId", questionsBo.getId())
                            .setInteger("questionnairesId", questionnaireId);
            QuestionnairesStepsBo questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
            QuestionnaireStepBean questionnaireStepBean = new QuestionnaireStepBean();
            questionnaireStepBean.setStepId(questionsBo.getId());
            questionnaireStepBean.setDeletionId(questionnairesStepsBo.getStepId());
            questionnaireStepBean.setStepType(FdahpStudyDesignerConstants.QUESTION_STEP);
            questionnaireStepBean.setSequenceNo(
                sequenceNoMap.get(questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP));
            if (questionnairesStepsBo.getGroupFlag() != null && questionnairesStepsBo.getGroupFlag()) {
              GroupsBo groupsBo = this.getGroupIdByStepId(session, questionnairesStepsBo.getStepId());
              if (groupsBo != null) {
                questionnaireStepBean.setGroupId(groupsBo.getId());
                questionnaireStepBean.setGroupName(groupsBo.getGroupName());
              }
            }
            questionnaireStepBean.setTitle(questionsBo.getQuestion());
            questionnaireStepBean.setResponseType(questionsBo.getResponseType());
            questionnaireStepBean.setLineChart(questionsBo.getAddLineChart());
            questionnaireStepBean.setStatData(questionsBo.getUseStasticData());
            questionnaireStepBean.setStatus(questionsBo.getStatus());
            questionnaireStepBean.setGroupFlag(questionnairesStepsBo.getGroupFlag());
            questionnaireStepBean.setDestinationStep(
                destinationMap.get(
                    questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP));
            questionnaireStepBean.setUseAnchorDate(questionsBo.getUseAnchorDate());
            String stepOrGroup = questionnairesStepsBo.getStepOrGroupPostLoad();
            if(stepOrGroup != null) {
              //setting destinationText if the selected destination step is STEP
              if (FdahpStudyDesignerConstants.STEP.equalsIgnoreCase(stepOrGroup)) {
                questionnaireStepBean.setDestinationText(
                        destinationText.get(
                                destinationMap.get(
                                        questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP)));
              }
              //setting destinationText if the selected destinationstep is GROUP
              else {
                //getting groupautogeneratedId that is stored in destinationStep in order to get the groupName in the groupsBo table
                Integer groupId = destinationMap.get(
                        questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP);
                //get the groupname by passing above id
                String groupName = null;
                query =
                        session.createQuery(
                                " select groupName from GroupsBo GBO where GBO.id =: groupId ");
                groupName = String.valueOf(query.setParameter("groupId", groupId).uniqueResult());
                questionnaireStepBean.setDestinationText("Group:" + groupName);
              }
            } else {
              questionnaireStepBean.setDestinationText(
                      destinationText.get(
                              destinationMap.get(
                                      questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP)));
            }
            qTreeMap.put(
                sequenceNoMap.get(questionsBo.getId() + FdahpStudyDesignerConstants.QUESTION_STEP),
                questionnaireStepBean);
          }
        }
      }
      // get the list of form step which contains the multiple questions
      // of questionnaire
      if (!formIdList.isEmpty()) {
        String fromQuery =
            "select f.form_id, f.question_id, f.sequence_no, q.id, q.question, q.response_type, q.add_line_chart, " +
                    "q.use_stastic_data, q.status, q.use_anchor_date, qs.step_short_title from questionnaires_steps qs, " +
                    "questions q, form_mapping f where qs.instruction_form_id=f.form_id and q.id=f.question_id and " +
                    "q.active=1 and f.form_id IN (:formIdList ) and f.active=1 order by f.form_id";
        List<?> result =
            session.createSQLQuery(fromQuery).setParameterList("formIdList", formIdList).list();
        for (int i = 0; i < formIdList.size(); i++) {
          QuestionnaireStepBean fQuestionnaireStepBean = new QuestionnaireStepBean();
          TreeMap<Integer, QuestionnaireStepBean> formQuestionMap = new TreeMap<>();
          for (int j = 0; j < result.size(); j++) {
            Object[] objects = (Object[]) result.get(j);
            Integer formId = (Integer) objects[0];
            Integer sequenceNo = (Integer) objects[2];
            Integer questionId = (Integer) objects[3];
            String questionText = (String) objects[4];
            Integer responseType = (Integer) objects[5];
            String lineChart = (String) objects[6];
            String statData = (String) objects[7];
            Boolean status = (Boolean) objects[8];
            Boolean useAnchorDate = (Boolean) objects[9];
            String stepShortTitle = (String) objects[10];
            if (formIdList.get(i).equals(formId)) {
              QuestionnaireStepBean questionnaireStepBean = new QuestionnaireStepBean();
              questionnaireStepBean.setStepId(formId);
              questionnaireStepBean.setQuestionInstructionId(questionId);
              questionnaireStepBean.setTitle(questionText);
              questionnaireStepBean.setSequenceNo(sequenceNo);
              questionnaireStepBean.setStepType(FdahpStudyDesignerConstants.FORM_STEP);
              questionnaireStepBean.setResponseType(responseType);
              questionnaireStepBean.setLineChart(lineChart);
              questionnaireStepBean.setStatData(statData);
              questionnaireStepBean.setStatus(status);
              questionnaireStepBean.setUseAnchorDate(useAnchorDate);
              questionnaireStepBean.setStepShortTitle(stepShortTitle);
              formQuestionMap.put(sequenceNo, questionnaireStepBean);
            }
          }
          query =
                  session
                          .getNamedQuery("getQuestionnaireGroupStep")
                          .setInteger("instructionFormId", formIdList.get(i))
                          .setInteger("questionnairesId", questionnaireId);

          QuestionnairesStepsBo questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
          fQuestionnaireStepBean.setStepId(formIdList.get(i));
          if (questionnairesStepsBo.getGroupFlag() != null && questionnairesStepsBo.getGroupFlag()) {
            GroupsBo groupsBo = this.getGroupIdByStepId(session, questionnairesStepsBo.getStepId());
            if (groupsBo != null) {
              fQuestionnaireStepBean.setGroupId(groupsBo.getId());
              fQuestionnaireStepBean.setGroupName(groupsBo.getGroupName());
            }
          }
          fQuestionnaireStepBean.setDeletionId(questionnairesStepsBo.getStepId());
          fQuestionnaireStepBean.setStepType(FdahpStudyDesignerConstants.FORM_STEP);
          fQuestionnaireStepBean.setSequenceNo(
              sequenceNoMap.get(formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP));
          fQuestionnaireStepBean.setFromMap(formQuestionMap);
          fQuestionnaireStepBean.setStatus(formStatusMap.get(formIdList.get(i)));
          fQuestionnaireStepBean.setGroupFlag(questionnairesStepsBo.getGroupFlag());
          fQuestionnaireStepBean.setQuestionInstructionId(formIdList.get(i));
          fQuestionnaireStepBean.setDestinationStep(
              destinationMap.get(formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP));
          String stepOrGroup = questionnairesStepsBo.getStepOrGroupPostLoad();
          if(stepOrGroup != null) {
            //setting destinationText if the selected destination step is STEP
            if (FdahpStudyDesignerConstants.STEP.equalsIgnoreCase(stepOrGroup)) {
              fQuestionnaireStepBean.setDestinationText(
                      destinationText.get(
                              destinationMap.get(formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP)));
            }
            //setting destinationText if the selected destinationstep is GROUP
            else {
              //getting groupautogeneratedId that is stored in destinationStep in order to get the groupName in the groupsBo table
              Integer groupId = destinationMap.get(
                      formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP);
              //get the groupname by passing above id
              String groupName = null;
              query =
                      session.createQuery(
                              " select groupName from GroupsBo GBO where GBO.id =: groupId ");
              groupName = String.valueOf(query.setParameter("groupId", groupId).uniqueResult());
              fQuestionnaireStepBean.setDestinationText("Group:" + groupName);
            }
          }else {
            fQuestionnaireStepBean.setDestinationText(
                    destinationText.get(
                            destinationMap.get(formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP)));
          }
          fQuestionnaireStepBean.setStepShortTitle(questionnairesStepsBo.getStepShortTitle());
          qTreeMap.put(
              sequenceNoMap.get(formIdList.get(i) + FdahpStudyDesignerConstants.FORM_STEP),
              fQuestionnaireStepBean);
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireStepList() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireStepList() - Ends");
    return qTreeMap;
  }

  private GroupsBo getGroupIdByStepId(Session session, Integer stepId) {
    if (stepId != null) {
      return (GroupsBo) session.createQuery("from GroupsBo where id = (select grpId from GroupMappingBo where questionnaireStepId=:stepId)")
              .setParameter("stepId", stepId)
              .uniqueResult();
    }
    return null;
  }

  /**
   * This method is used to get the Response Type Master information which research kit and research
   * stack supports
   *
   * @author BTC
   * @return List Object {@link QuestionResponseTypeMasterInfoBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<QuestionResponseTypeMasterInfoBo> getQuestionReponseTypeList() {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - Starts");
    Session session = null;
    List<QuestionResponseTypeMasterInfoBo> questionResponseTypeMasterInfoBos = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      query = session.getNamedQuery("getResponseTypes");
      questionResponseTypeMasterInfoBos = query.list();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionReponseTypeList() - Ends");
    return questionResponseTypeMasterInfoBos;
  }

  /**
   * Load the question of form step inside questionnaire.Question contains the question level
   * attributes and response level attributes
   *
   * @author BTC
   * @param Integer , questionId in {@link QuestionnaireBo}
   * @param String , questionnaireShortTitle in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return {@link QuestionsBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public QuestionsBo getQuestionsById(
      Integer questionId, String questionnaireShortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionsById() - Starts");
    Session session = null;
    QuestionsBo questionsBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      questionsBo = (QuestionsBo) session.get(QuestionsBo.class, questionId);
      if (questionsBo != null) {
        try {
          if (StringUtils.isNotEmpty(questionnaireShortTitle)) {
            // Duplicate ShortTitle per questionsBo Start
            BigInteger quesionshortTitleCount =
                (BigInteger)
                    session
                        .createSQLQuery(
                            "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.short_title=:questionnaireShortTitle "
                                + " and Q.active=1 and Q.is_live=1 and Q.custom_study_id=:customStudyId "
                                + " and QSBO.step_type='Form' and QBO.short_title=:shortTitle "
                                + " and QBO.active=1")
                        .setString("questionnaireShortTitle", questionnaireShortTitle)
                        .setString("customStudyId", customStudyId)
                        .setString("shortTitle", questionsBo.getShortTitle())
                        .uniqueResult();
            if (quesionshortTitleCount != null && quesionshortTitleCount.intValue() > 0)
              questionsBo.setIsShorTitleDuplicate(quesionshortTitleCount.intValue());
            else questionsBo.setIsShorTitleDuplicate(0);
            // Duplicate ShortTitle per questionsBo End

            // Duplicate statShortTitle per questionsBo Start
            if (StringUtils.isNotEmpty(questionsBo.getStatShortName())) {
              BigInteger quesionStatshortTitleCount =
                  (BigInteger)
                      session
                          .createSQLQuery(
                              "select count(*) From questions QBO,form_mapping f,questionnaires_steps QSBO,questionnaires Q where QBO.id=f.question_id and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=Q.id and Q.short_title=:questionnaireShortTitle "
                                  + " and Q.active=1 and Q.is_live=1 and Q.custom_study_id=:customStudyId "
                                  + " and QSBO.step_type='Form' and QBO.stat_short_name=:shortTitle "
                                  + " and QBO.active=1")
                          .setString("questionnaireShortTitle", questionnaireShortTitle)
                          .setString("customStudyId", customStudyId)
                          .setString("shortTitle", questionsBo.getStatShortName())
                          .uniqueResult();
              if (quesionStatshortTitleCount != null && quesionStatshortTitleCount.intValue() > 0)
                questionsBo.setIsStatShortNameDuplicate(quesionStatshortTitleCount.intValue());
              else questionsBo.setIsStatShortNameDuplicate(0);
            }

            // Duplicate statShortTitle per questionsBo Ends
          } else {
            questionsBo.setIsStatShortNameDuplicate(0);
            questionsBo.setIsShorTitleDuplicate(0);
          }
        } catch (Exception e) {
          logger.error("StudyQuestionnaireDAOImpl - getQuestionsById() - SUB  ERROR ", e);
        }
        QuestionReponseTypeBo questionReponseTypeBo = null;
        logger.info(
            "StudyQuestionnaireDAOImpl - getQuestionnaireStep() - questionsResponseTypeId:"
                + questionsBo.getId());
        query =
            session
                .getNamedQuery("getQuestionResponse")
                .setInteger("questionsResponseTypeId", questionsBo.getId());
        query.setMaxResults(1);
        questionReponseTypeBo = (QuestionReponseTypeBo) query.uniqueResult();
        if (questionReponseTypeBo != null
            && questionReponseTypeBo.getStyle() != null
            && StringUtils.isNotEmpty(questionReponseTypeBo.getStyle())) {
          questionReponseTypeBo.setStyle(questionReponseTypeBo.getStyle());
          // changing the date format to database date format
          if ((FdahpStudyDesignerConstants.DATE)
              .equalsIgnoreCase(questionReponseTypeBo.getStyle())) {
            if (questionReponseTypeBo.getMinDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getMinDate())) {
              questionReponseTypeBo.setMinDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getMinDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
            if (questionReponseTypeBo.getMaxDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getMaxDate())) {
              questionReponseTypeBo.setMaxDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getMaxDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
            if (questionReponseTypeBo.getDefaultDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getDefaultDate())) {
              questionReponseTypeBo.setDefaultDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getDefaultDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
          } else if ((FdahpStudyDesignerConstants.DATE_TIME)
              .equalsIgnoreCase(questionReponseTypeBo.getStyle())) {
            if (questionReponseTypeBo.getMinDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getMinDate())) {
              questionReponseTypeBo.setMinDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getMinDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
            }
            if (questionReponseTypeBo.getMaxDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getMaxDate())) {
              questionReponseTypeBo.setMaxDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getMaxDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
            }
            if (questionReponseTypeBo.getDefaultDate() != null
                && StringUtils.isNotEmpty(questionReponseTypeBo.getDefaultDate())) {
              questionReponseTypeBo.setDefaultDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionReponseTypeBo.getDefaultDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME));
            }
          }
        }
        questionsBo.setQuestionReponseTypeBo(questionReponseTypeBo);

        List<QuestionResponseSubTypeBo> questionResponseSubTypeList = null;
        query =
            session
                .getNamedQuery("getQuestionSubResponse")
                .setInteger("responseTypeId", questionsBo.getId());
        questionResponseSubTypeList = query.list();
        // appending the current date and time for image urls
        if (null != questionResponseSubTypeList && !questionResponseSubTypeList.isEmpty()) {
          for (QuestionResponseSubTypeBo s : questionResponseSubTypeList) {
            if (FdahpStudyDesignerUtil.isNotEmpty(s.getImage())) {
              if (s.getImage().contains("?v=")) {
                String imagePathArr[] = s.getImage().split("\\?");
                s.setImage(imagePathArr[0] + "?v=" + new Date().getTime());
              } else {
                s.setImage(s.getImage() + "?v=" + new Date().getTime());
              }
            }
            if (FdahpStudyDesignerUtil.isNotEmpty(s.getSelectedImage())) {
              if (s.getSelectedImage().contains("?v=")) {
                String imagePathArr[] = s.getSelectedImage().split("\\?");
                s.setSelectedImage(imagePathArr[0] + "?v=" + new Date().getTime());
              } else {
                s.setSelectedImage(s.getSelectedImage() + "?v=" + new Date().getTime());
              }
            }
          }
        }
        questionsBo.setQuestionResponseSubTypeList(questionResponseSubTypeList);

        // Phase 2a ancordate start
        if (questionsBo.getAnchorDateId() != null) {
          String name =
              (String)
                  session
                      .createSQLQuery("select name from anchordate_type where id=:anchorDateId")
                      .setInteger("anchorDateId", questionsBo.getAnchorDateId())
                      .uniqueResult();
          questionsBo.setAnchorDateName(name);
        }
        // phase 2a anchordate end
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionsById() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionsById() - Ends");
    return questionsBo;
  }

  /**
   * Return the QuestionReponsetype based on the response type id which already exists for
   * question.These values are listed in response level attributes
   *
   * @author BTC
   * @param Object : {@link QuestionReponseTypeBo}
   * @param session : {@link Session}
   * @return Object : {@link QuestionReponseTypeBo}
   */
  public QuestionReponseTypeBo getQuestionsResponseTypeBo(
      QuestionReponseTypeBo questionsResponseTypeBo, Session session) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionsResponseTypeBo() - Starts");
    QuestionReponseTypeBo addOrUpdateQuestionsResponseTypeBo = null;
    try {
      if (questionsResponseTypeBo != null && session != null) {
        if (questionsResponseTypeBo.getResponseTypeId() != null) {
          addOrUpdateQuestionsResponseTypeBo =
              (QuestionReponseTypeBo)
                  session.get(
                      QuestionReponseTypeBo.class, questionsResponseTypeBo.getResponseTypeId());
        } else {
          addOrUpdateQuestionsResponseTypeBo = new QuestionReponseTypeBo();
          addOrUpdateQuestionsResponseTypeBo.setActive(true);
        }
        if (questionsResponseTypeBo.getQuestionsResponseTypeId() != null) {
          addOrUpdateQuestionsResponseTypeBo.setQuestionsResponseTypeId(
              questionsResponseTypeBo.getQuestionsResponseTypeId());
        }
        if (questionsResponseTypeBo.getMinValue() != null) {
          addOrUpdateQuestionsResponseTypeBo.setMinValue(questionsResponseTypeBo.getMinValue());
        }
        if (questionsResponseTypeBo.getMaxValue() != null) {
          addOrUpdateQuestionsResponseTypeBo.setMaxValue(questionsResponseTypeBo.getMaxValue());
        }
        if (questionsResponseTypeBo.getDefaultValue() != null) {
          addOrUpdateQuestionsResponseTypeBo.setDefaultValue(
              questionsResponseTypeBo.getDefaultValue());
        }
        if (questionsResponseTypeBo.getStep() != null) {
          addOrUpdateQuestionsResponseTypeBo.setStep(questionsResponseTypeBo.getStep());
        }
        if (questionsResponseTypeBo.getVertical() != null) {
          addOrUpdateQuestionsResponseTypeBo.setVertical(questionsResponseTypeBo.getVertical());
        }
        addOrUpdateQuestionsResponseTypeBo.setMinDescription(
            questionsResponseTypeBo.getMinDescription());
        addOrUpdateQuestionsResponseTypeBo.setMaxDescription(
            questionsResponseTypeBo.getMaxDescription());
        if (questionsResponseTypeBo.getMaxFractionDigits() != null) {
          addOrUpdateQuestionsResponseTypeBo.setMaxFractionDigits(
              questionsResponseTypeBo.getMaxFractionDigits());
        }
        if (questionsResponseTypeBo.getTextChoices() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getTextChoices())) {
          addOrUpdateQuestionsResponseTypeBo.setTextChoices(
              questionsResponseTypeBo.getTextChoices());
        }
        if (questionsResponseTypeBo.getSelectionStyle() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getSelectionStyle())) {
          addOrUpdateQuestionsResponseTypeBo.setSelectionStyle(
              questionsResponseTypeBo.getSelectionStyle());
        }
        if (questionsResponseTypeBo.getImageSize() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getImageSize())) {
          addOrUpdateQuestionsResponseTypeBo.setImageSize(questionsResponseTypeBo.getImageSize());
        }
        if (questionsResponseTypeBo.getStyle() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getStyle())) {
          addOrUpdateQuestionsResponseTypeBo.setStyle(questionsResponseTypeBo.getStyle());
          if ((FdahpStudyDesignerConstants.DATE)
              .equalsIgnoreCase(questionsResponseTypeBo.getStyle())) {
            if (questionsResponseTypeBo.getMinDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getMinDate())) {
              addOrUpdateQuestionsResponseTypeBo.setMinDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getMinDate(),
                      FdahpStudyDesignerConstants.UI_SDF_DATE,
                      FdahpStudyDesignerConstants.DB_SDF_DATE));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMinDate(null);
            }
            if (questionsResponseTypeBo.getMaxDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getMaxDate())) {
              addOrUpdateQuestionsResponseTypeBo.setMaxDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getMaxDate(),
                      FdahpStudyDesignerConstants.UI_SDF_DATE,
                      FdahpStudyDesignerConstants.DB_SDF_DATE));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMaxDate(null);
            }
            if (questionsResponseTypeBo.getDefaultDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getDefaultDate())) {
              addOrUpdateQuestionsResponseTypeBo.setDefaultDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getDefaultDate(),
                      FdahpStudyDesignerConstants.UI_SDF_DATE,
                      FdahpStudyDesignerConstants.DB_SDF_DATE));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setDefaultDate(null);
            }
          } else if ((FdahpStudyDesignerConstants.DATE_TIME)
              .equalsIgnoreCase(questionsResponseTypeBo.getStyle())) {
            if (questionsResponseTypeBo.getMinDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getMinDate())) {
              addOrUpdateQuestionsResponseTypeBo.setMinDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getMinDate(),
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME,
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMinDate(null);
            }
            if (questionsResponseTypeBo.getMaxDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getMaxDate())) {
              addOrUpdateQuestionsResponseTypeBo.setMaxDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getMaxDate(),
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME,
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMaxDate(null);
            }
            if (questionsResponseTypeBo.getDefaultDate() != null
                && StringUtils.isNotEmpty(questionsResponseTypeBo.getDefaultDate())) {
              addOrUpdateQuestionsResponseTypeBo.setDefaultDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionsResponseTypeBo.getDefaultDate(),
                      FdahpStudyDesignerConstants.REQUIRED_DATE_TIME,
                      FdahpStudyDesignerConstants.DB_SDF_DATE_TIME));
            } else {
              addOrUpdateQuestionsResponseTypeBo.setDefaultDate(null);
            }
          }
        }
        addOrUpdateQuestionsResponseTypeBo.setPlaceholder(questionsResponseTypeBo.getPlaceholder());
        addOrUpdateQuestionsResponseTypeBo.setUnit(questionsResponseTypeBo.getUnit());
        addOrUpdateQuestionsResponseTypeBo.setMaxLength(questionsResponseTypeBo.getMaxLength());
        if (questionsResponseTypeBo.getValidationRegex() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getValidationRegex())) {
          addOrUpdateQuestionsResponseTypeBo.setValidationRegex(
              questionsResponseTypeBo.getValidationRegex());
        }
        addOrUpdateQuestionsResponseTypeBo.setInvalidMessage(
            questionsResponseTypeBo.getInvalidMessage());
        if (questionsResponseTypeBo.getMultipleLines() != null) {
          addOrUpdateQuestionsResponseTypeBo.setMultipleLines(
              questionsResponseTypeBo.getMultipleLines());
        }
        if (questionsResponseTypeBo.getMeasurementSystem() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getMeasurementSystem())) {
          addOrUpdateQuestionsResponseTypeBo.setMeasurementSystem(
              questionsResponseTypeBo.getMeasurementSystem());
        }
        if (questionsResponseTypeBo.getUseCurrentLocation() != null) {
          addOrUpdateQuestionsResponseTypeBo.setUseCurrentLocation(
              questionsResponseTypeBo.getUseCurrentLocation());
        }
        addOrUpdateQuestionsResponseTypeBo.setValidationCondition(
            questionsResponseTypeBo.getValidationCondition());
        addOrUpdateQuestionsResponseTypeBo.setValidationCharacters(
            questionsResponseTypeBo.getValidationCharacters());
        addOrUpdateQuestionsResponseTypeBo.setValidationExceptText(
            questionsResponseTypeBo.getValidationExceptText());

        addOrUpdateQuestionsResponseTypeBo.setValidationRegex(
            FdahpStudyDesignerUtil.getRegExpression(
                questionsResponseTypeBo.getValidationCondition(),
                questionsResponseTypeBo.getValidationCharacters(),
                questionsResponseTypeBo.getValidationExceptText()));

        String fileName;
        if (questionsResponseTypeBo.getMinImageFile() != null) {
          if (questionsResponseTypeBo.getMinImage() != null
              && StringUtils.isNotEmpty(questionsResponseTypeBo.getMinImage())) {
            addOrUpdateQuestionsResponseTypeBo.setMinImage(questionsResponseTypeBo.getMinImage());
          } else {
            if (questionsResponseTypeBo.getMinImageFile().getOriginalFilename() != null
                && StringUtils.isNotEmpty(
                    questionsResponseTypeBo.getMinImageFile().getOriginalFilename())) {
              fileName =
                  FdahpStudyDesignerUtil.getStandardFileName(
                      FdahpStudyDesignerConstants.QUESTION_STEP_IMAGE + 0,
                      questionsResponseTypeBo.getMinImageFile().getOriginalFilename(),
                      String.valueOf(questionsResponseTypeBo.getQuestionsResponseTypeId()));
              String imagePath =
                  FdahpStudyDesignerUtil.uploadImageFile(
                      questionsResponseTypeBo.getMinImageFile(),
                      fileName,
                      FdahpStudyDesignerConstants.QUESTIONNAIRE);
              addOrUpdateQuestionsResponseTypeBo.setMinImage(imagePath);
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMinImage(null);
            }
          }
        } else {
          if (StringUtils.isEmpty(questionsResponseTypeBo.getMinImage())) {
            addOrUpdateQuestionsResponseTypeBo.setMinImage(null);
          }
        }
        if (questionsResponseTypeBo.getMaxImageFile() != null) {
          if (questionsResponseTypeBo.getMaxImage() != null
              && StringUtils.isNotEmpty(questionsResponseTypeBo.getMaxImage())) {
            addOrUpdateQuestionsResponseTypeBo.setMaxImage(questionsResponseTypeBo.getMaxImage());
          } else {
            if (questionsResponseTypeBo.getMaxImageFile().getOriginalFilename() != null
                && StringUtils.isNotEmpty(
                    questionsResponseTypeBo.getMaxImageFile().getOriginalFilename())) {
              fileName =
                  FdahpStudyDesignerUtil.getStandardFileName(
                      FdahpStudyDesignerConstants.QUESTION_STEP_IMAGE + 1,
                      questionsResponseTypeBo.getMaxImageFile().getOriginalFilename(),
                      String.valueOf(questionsResponseTypeBo.getQuestionsResponseTypeId()));
              String imagePath =
                  FdahpStudyDesignerUtil.uploadImageFile(
                      questionsResponseTypeBo.getMaxImageFile(),
                      fileName,
                      FdahpStudyDesignerConstants.QUESTIONNAIRE);
              addOrUpdateQuestionsResponseTypeBo.setMaxImage(imagePath);
            } else {
              addOrUpdateQuestionsResponseTypeBo.setMaxImage(null);
            }
          }
        } else {
          if (StringUtils.isEmpty(questionsResponseTypeBo.getMaxImage())) {
            addOrUpdateQuestionsResponseTypeBo.setMaxImage(null);
          }
        }
        if (questionsResponseTypeBo.getDefaultTime() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getDefaultTime())) {
          addOrUpdateQuestionsResponseTypeBo.setDefaultTime(
              questionsResponseTypeBo.getDefaultTime());
        }
        if (questionsResponseTypeBo.getFormulaBasedLogic() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getFormulaBasedLogic())) {
          addOrUpdateQuestionsResponseTypeBo.setFormulaBasedLogic(
              questionsResponseTypeBo.getFormulaBasedLogic());
        }
        if (questionsResponseTypeBo.getConditionFormula() != null
            && StringUtils.isNotEmpty(questionsResponseTypeBo.getConditionFormula())) {
          addOrUpdateQuestionsResponseTypeBo.setConditionFormula(
              questionsResponseTypeBo.getConditionFormula());
        }

        /** Other type set addded by ronalin * */
        if (StringUtils.isNotEmpty(questionsResponseTypeBo.getOtherType())
            && questionsResponseTypeBo.getOtherType().equals("on")) {
          addOrUpdateQuestionsResponseTypeBo.setOtherType(questionsResponseTypeBo.getOtherType());
          addOrUpdateQuestionsResponseTypeBo.setOtherText(questionsResponseTypeBo.getOtherText());
          addOrUpdateQuestionsResponseTypeBo.setOtherValue(questionsResponseTypeBo.getOtherValue());
          addOrUpdateQuestionsResponseTypeBo.setOtherExclusive(
              questionsResponseTypeBo.getOtherExclusive());
          addOrUpdateQuestionsResponseTypeBo.setOtherDestinationStepId(
              questionsResponseTypeBo.getOtherDestinationStepId());
          addOrUpdateQuestionsResponseTypeBo.setOtherDescription(
              questionsResponseTypeBo.getOtherDescription());
          addOrUpdateQuestionsResponseTypeBo.setOtherIncludeText(
              questionsResponseTypeBo.getOtherIncludeText());
          if (StringUtils.isNotEmpty(questionsResponseTypeBo.getOtherIncludeText())
              && questionsResponseTypeBo.getOtherIncludeText().equals("Yes")) {
            addOrUpdateQuestionsResponseTypeBo.setOtherPlaceholderText(
                questionsResponseTypeBo.getOtherPlaceholderText());
            addOrUpdateQuestionsResponseTypeBo.setOtherParticipantFill(
                questionsResponseTypeBo.getOtherParticipantFill());
          } else {
            addOrUpdateQuestionsResponseTypeBo.setOtherPlaceholderText(null);
            addOrUpdateQuestionsResponseTypeBo.setOtherParticipantFill(null);
          }
        } else {
          addOrUpdateQuestionsResponseTypeBo.setOtherType(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherText(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherValue(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherExclusive(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherDestinationStepId(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherDescription(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherIncludeText(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherPlaceholderText(null);
          addOrUpdateQuestionsResponseTypeBo.setOtherParticipantFill(null);
        }
        /** Other type set addded by ronalin * */
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionsResponseTypeBo() - Error", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionsResponseTypeBo() - Ends");
    return addOrUpdateQuestionsResponseTypeBo;
  }

  /**
   * return List of all the Questionnaires of an study.A Study can have 0 or more Questionnaires and
   * admin can manage a list of questionnaires for the study Questionnaires based on user's Study Id
   *
   * @author BTC
   * @param studyId , studyId of the {@link StudyBo}
   * @param boolean : isLive
   * @return List of {@link QuestionnaireBo}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<QuestionnaireBo> getStudyQuestionnairesByStudyId(String studyId, Boolean isLive) {
    logger.info("StudyQuestionnaireDAOImpl - getStudyQuestionnairesByStudyId() - Starts");
    Session session = null;
    List<QuestionnaireBo> questionnaires = null;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotEmpty(studyId)) {
        if (isLive) {
          searchQuery =
              "From QuestionnaireBo QBO WHERE QBO.customStudyId =:studyId "
                  + " and QBO.active=1 and QBO.live=1 order by QBO.createdDate DESC";
          query = session.createQuery(searchQuery).setString("studyId", studyId);
        } else {
          query =
              session
                  .getNamedQuery("getQuestionariesByStudyId")
                  .setInteger("studyId", Integer.parseInt(studyId));
        }
        questionnaires = query.list();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getStudyQuestionnairesByStudyId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getStudyQuestionnairesByStudyId() - Ends");
    return questionnaires;
  }

  /**
   * In Questionnaire for question step and question in form step for date response type we can
   * chose those question as anchor date. The anchor date question is unique across the study so
   * here we are validating for anchor date is checked or not for any other question while create or
   * updating the new question in a study
   *
   * @author BTC
   * @param Integer , studyId in {@link StudyBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return Boolean : true/false
   */
  @Override
  public Boolean isAnchorDateExistsForStudy(Integer studyId, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - starts");
    boolean isExists = false;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (customStudyId != null && StringUtils.isNotEmpty(customStudyId)) {
        // checking in the question step anchor date is selected or not
        String searchQuery =
            "select count(q.use_anchor_date) from questions q,questionnaires_steps qsq,questionnaires qq  where q.id=qsq.instruction_form_id and qsq.step_type='Question' "
                + "and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.study_id in(select s.id from studies s where s.custom_study_id=:customStudyId "
                + " and s.is_live=0) and qq.active=1 and q.active=1;";
        BigInteger count =
            (BigInteger)
                session
                    .createSQLQuery(searchQuery)
                    .setString("customStudyId", customStudyId)
                    .uniqueResult();
        if (count.intValue() > 0) {
          isExists = true;
        } else {
          // checking in the form step question anchor date is
          // selected or not
          String subQuery =
              "select count(q.use_anchor_date) from questions q,form_mapping fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and f.form_id=fm.form_id and f.active=1 "
                  + "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and qsf.questionnaires_id=qq.id and study_id in (select s.id from studies s where s.custom_study_id=:customStudyId "
                  + " and s.is_live=0) and q.active=1";
          BigInteger subCount =
              (BigInteger)
                  session
                      .createSQLQuery(subQuery)
                      .setString("customStudyId", customStudyId)
                      .uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            isExists = true;
          }
        }
      } else {
        // checking in the question step anchor date is selected or not
        String searchQuery =
            "select count(q.use_anchor_date) from questions q,questionnaires_steps qsq,questionnaires qq  where q.id=qsq.instruction_form_id and qsq.step_type='Question' "
                + "and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.study_id=:studyId "
                + " and qq.active=1 and q.active=1;";
        BigInteger count =
            (BigInteger)
                session.createSQLQuery(searchQuery).setInteger("studyId", studyId).uniqueResult();
        if (count.intValue() > 0) {
          isExists = true;
        } else {
          // checking in the form step question anchor date is
          // selected or not
          String subQuery =
              "select count(q.use_anchor_date) from questions q,form_mapping fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and f.form_id=fm.form_id and f.active=1 "
                  + "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and qsf.questionnaires_id=qq.id and study_id=:studyId "
                  + " and q.active=1";
          BigInteger subCount =
              (BigInteger)
                  session.createSQLQuery(subQuery).setInteger("studyId", studyId).uniqueResult();
          if (subCount != null && subCount.intValue() > 0) {
            isExists = true;
          }
        }
      }
      if (!isExists) {
        char isEnrollAnchorExist =
            (char)
                session
                    .createSQLQuery(
                        "select s.enrollmentdate_as_anchordate from studies s where s.id=:studyId")
                    .setInteger("studyId", studyId)
                    .uniqueResult();
        if (isEnrollAnchorExist != ' ' && isEnrollAnchorExist == 'Y') isExists = true;
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - Ends");
    return isExists;
  }

  /**
   * Checking the Questionnaire creation is completed or not
   *
   * @author BTC
   * @param Integer , studyId in {@link StudyBo}
   * @return Boolean true r false
   */
  @Override
  public Boolean isQuestionnairesCompleted(Integer studyId) {
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - starts");
    boolean isExists = true;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      String searchQuery =
          "select sum(q.status = 0) as no from questionnaires_steps q where q.questionnaires_id in (select id from questionnaires where study_id=:studyId "
              + " and active=1) and q.active=1";
      BigDecimal count =
          (BigDecimal)
              session.createSQLQuery(searchQuery).setInteger("studyId", studyId).uniqueResult();
      if (count != null && count.intValue() > 0) {
        isExists = false;
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistsForStudy() - Ends");
    return isExists;
  }

  /**
   * From step contains the list of questions with default admin created master order.Admin can
   * manage these orders by reordering the question on drag and drop of a questions in the list
   *
   * @author BTC
   * @param Integer , formId in {@link FormBo}
   * @param Integer , oldOrderNumber
   * @param Integer , newOrderNumber
   * @return String : Success/Failure
   */
  @Override
  public String reOrderFormStepQuestions(Integer formId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyQuestionnaireDAOImpl - reOrderFormStepQuestions() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    FormMappingBo formMappingBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      String updateQuery = "";
      query =
          session
              .getNamedQuery("getFromByIdAndSequenceNo")
              .setInteger("formId", formId)
              .setInteger("oldOrderNumber", oldOrderNumber);
      formMappingBo = (FormMappingBo) query.uniqueResult();
      if (formMappingBo != null) {
        if (oldOrderNumber < newOrderNumber) {
          updateQuery =
              "update FormMappingBo FMBO set FMBO.sequenceNo=FMBO.sequenceNo-1 where FMBO.formId=:formId "
                  + " and FMBO.sequenceNo <=:newOrderNumber "
                  + " and FMBO.sequenceNo >:oldOrderNumber "
                  + " and FMBO.active=1";
          query =
              session
                  .createQuery(updateQuery)
                  .setInteger("formId", formId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber);
          count = query.executeUpdate();
          if (count > 0) {
            query =
                session
                    .getNamedQuery("updateFromQuestionSequenceNo")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("id", formMappingBo.getId());
            count = query.executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else if (oldOrderNumber > newOrderNumber) {
          updateQuery =
              "update FormMappingBo FMBO set FMBO.sequenceNo=FMBO.sequenceNo+1 where FMBO.formId=:formId "
                  + " and FMBO.sequenceNo >=:newOrderNumber "
                  + " and FMBO.sequenceNo <:oldOrderNumber "
                  + " and FMBO.active=1";
          query =
              session
                  .createQuery(updateQuery)
                  .setInteger("formId", formId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber);
          count = query.executeUpdate();
          if (count > 0) {
            query =
                session
                    .getNamedQuery("updateFromQuestionSequenceNo")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("id", formMappingBo.getId());
            count = query.executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - reOrderFormStepQuestions() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - reOrderFormStepQuestions() - Ends");
    return message;
  }

  /**
   * A questionnaire is an ordered set of one or more steps (screens on the mobile app).The
   * questionnaire by default follows the master order of steps admin can manage the order of an
   * step.Here we can do the reordering of an questionnaire steps(Instruction,Question,Form) which
   * are listed on questionnaire content page.
   *
   * @author BTC
   * @param Integer questionnaireId in {@link QuestionnairesStepsBo}
   * @param int oldOrderNumber
   * @param int newOrderNumber
   * @return String SUCCESS or FAILURE
   */
  @SuppressWarnings("unchecked")
  @Override
  public String reOrderQuestionnaireSteps(
      Integer questionnaireId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyQuestionnaireDAOImpl - reOrderQuestionnaireSteps() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    int count = 0;
    QuestionnairesStepsBo questionnairesStepsBo = null;
    List<QuestionnairesStepsBo> questionnaireStepList = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      String updateQuery = "";
      query =
          session
              .createQuery(
                  "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
                      + " and QSBO.sequenceNo =:oldOrderNumber "
                      + " and QSBO.active=1")
              .setInteger("questionnaireId", questionnaireId)
              .setInteger("oldOrderNumber", oldOrderNumber);
      questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
      if (questionnairesStepsBo != null) {

        if (oldOrderNumber < newOrderNumber) {
          updateQuery =
              "update QuestionnairesStepsBo QSBO set QSBO.sequenceNo=QSBO.sequenceNo-1 where QSBO.questionnairesId=:questionnaireId "
                  + " and QSBO.sequenceNo <=:newOrderNumber "
                  + " and QSBO.sequenceNo >:oldOrderNumber "
                  + " and QSBO.active=1";
          query =
              session
                  .createQuery(updateQuery)
                  .setInteger("questionnaireId", questionnaireId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber);
          count = query.executeUpdate();
          if (count > 0) {
            query =
                session
                    .createQuery(
                        "update QuestionnairesStepsBo q set q.sequenceNo=:newOrderNumber "
                            + " where q.stepId=:stepId "
                            + " and q.active=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("stepId", questionnairesStepsBo.getStepId());
            count = query.executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else if (oldOrderNumber > newOrderNumber) {
          updateQuery =
              "update QuestionnairesStepsBo QSBO set QSBO.sequenceNo=QSBO.sequenceNo+1 where QSBO.questionnairesId=:questionnaireId "
                  + " and QSBO.sequenceNo >=:newOrderNumber "
                  + " and QSBO.sequenceNo <:oldOrderNumber "
                  + " and QSBO.active=1";
          query =
              session
                  .createQuery(updateQuery)
                  .setInteger("questionnaireId", questionnaireId)
                  .setInteger("newOrderNumber", newOrderNumber)
                  .setInteger("oldOrderNumber", oldOrderNumber);
          count = query.executeUpdate();
          if (count > 0) {
            query =
                session
                    .createQuery(
                        "update QuestionnairesStepsBo Q set Q.sequenceNo=:newOrderNumber "
                            + " where Q.stepId=:stepId "
                            + " and Q.active=1")
                    .setInteger("newOrderNumber", newOrderNumber)
                    .setInteger("stepId", questionnairesStepsBo.getStepId());
            query.executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }

        // Reset destination steps in Questionnaire Starts
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          String searchQuery =
              "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId "
                  + "and QSBO.active=1 order by QSBO.sequenceNo ASC";
          questionnaireStepList = session.createQuery(searchQuery).setInteger("questionnaireId", questionnaireId).list();
          if (null != questionnaireStepList && !questionnaireStepList.isEmpty()) {
            if (questionnaireStepList.size() == 1) {
              questionnaireStepList.get(0).setDestinationStep(0);
              questionnaireStepList.get(0).setSequenceNo(1);
              session.update(questionnaireStepList.get(0));
            } else {
              int i;
              for (i = 0; i < questionnaireStepList.size() - 1; i++) {
                questionnaireStepList
                    .get(i)
                    .setDestinationStep(questionnaireStepList.get(i + 1).getStepId());
                questionnaireStepList.get(i).setSequenceNo(i + 1);
                session.update(questionnaireStepList.get(i));
              }
              questionnaireStepList.get(i).setDestinationStep(0);
              questionnaireStepList.get(i).setSequenceNo(i + 1);
              session.update(questionnaireStepList.get(i));
            }
          }
          String questionResponseQuery =
              "update response_sub_type_value rs,questionnaires_steps q set rs.destination_step_id = NULL "
                  + "where rs.response_type_id=q.instruction_form_id and q.step_type=:type"
                  + " and q.questionnaires_id=:questionnaireId "
                  + " and rs.active=1 and q.active=1";
          query =
              session
                  .createSQLQuery(questionResponseQuery)
                  .setParameter("type", FdahpStudyDesignerConstants.QUESTION_STEP)
                  .setInteger("questionnaireId", questionnaireId);
          query.executeUpdate();

          String questionConditionResponseQuery =
              "update questions qs,questionnaires_steps q,response_type_value rs  set qs.status = 0 where"
                  + " rs.questions_response_type_id=q.instruction_form_id and q.step_type=:type"
                  + " and q.questionnaires_id=:questionnaireId "
                  + " and qs.id=q.instruction_form_id and qs.active=1 and rs.active=1 and q.active=1 and rs.formula_based_logic='Yes'";
          query =
              session
                  .createSQLQuery(questionConditionResponseQuery)
                  .setParameter("type", FdahpStudyDesignerConstants.QUESTION_STEP)
                  .setInteger("questionnaireId", questionnaireId);
          query.executeUpdate();
        }
        // Reset destination steps in Questionnaire Ends
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - reOrderQuestionnaireSteps() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - reOrderQuestionnaireSteps() - Ends");
    return message;
  }

  /**
   * Here admin will add the from step to the questionnaire which contains the two sets of
   * attributes. which are step level attribute,form level attribute.Admin has fill the required
   * fields and click on done it save the info here.
   *
   * @author BTC
   * @param Object , questionnaireStepsBo {@link QuestionnairesStepsBo}
   * @param Object , sesObj {@link SessionObject}
   * @param String : customStudyId in {@link StudyBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @Override
  @SuppressWarnings("unchecked")
  public QuestionnairesStepsBo saveOrUpdateFromQuestionnaireStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sesObj,
      String customStudyId,
      String studyId) {
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateFromQuestionnaireStep() - Starts");
    Session session = null;
    QuestionnairesStepsBo addOrUpdateQuestionnairesStepsBo = null;
    String activitydetails = "";
    String activity = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (questionnairesStepsBo != null) {
        if (questionnairesStepsBo.getStepId() != null) {
          addOrUpdateQuestionnairesStepsBo =
              (QuestionnairesStepsBo)
                  session.get(QuestionnairesStepsBo.class, questionnairesStepsBo.getStepId());
        } else {
          addOrUpdateQuestionnairesStepsBo = new QuestionnairesStepsBo();
          addOrUpdateQuestionnairesStepsBo.setActive(true);
          addOrUpdateQuestionnairesStepsBo.setDestinationStep(0);

          List<StudySequenceLangBO> studySequenceLangBOList =
              session
                  .createQuery("from StudySequenceLangBO where studySequenceLangPK.studyId=:id")
                  .setInteger("id", Integer.parseInt(studyId))
                  .list();
          if (studySequenceLangBOList != null && studySequenceLangBOList.size() > 0) {
            for (StudySequenceLangBO studySequenceLangBO : studySequenceLangBOList) {
              studySequenceLangBO.setStudyExcQuestionnaries(false);
              session.update(studySequenceLangBO);
            }
          }

          List<QuestionnaireLangBO> questionnaireLangBOList =
              session
                  .createQuery("from QuestionnaireLangBO where questionnaireLangPK.id=:id")
                  .setInteger("id", questionnairesStepsBo.getQuestionnairesId())
                  .list();
          if (questionnaireLangBOList != null && questionnaireLangBOList.size() > 0) {
            for (QuestionnaireLangBO questionnaireLangBO : questionnaireLangBOList) {
              questionnaireLangBO.setStatus(false);
              session.update(questionnaireLangBO);
            }
          }
        }
        if (questionnairesStepsBo.getStepShortTitle() != null
            && !questionnairesStepsBo.getStepShortTitle().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setStepShortTitle(
              questionnairesStepsBo.getStepShortTitle());
        }
        if (questionnairesStepsBo.getSkiappable() != null
            && !questionnairesStepsBo.getSkiappable().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setSkiappable(questionnairesStepsBo.getSkiappable());
        }
        if (questionnairesStepsBo.getRepeatable() != null
            && !questionnairesStepsBo.getRepeatable().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setRepeatable(questionnairesStepsBo.getRepeatable());
        }
        addOrUpdateQuestionnairesStepsBo.setRepeatableText(
            questionnairesStepsBo.getRepeatableText());
        if (questionnairesStepsBo.getDestinationStep() != null) {
          addOrUpdateQuestionnairesStepsBo.setDestinationStep(
              questionnairesStepsBo.getDestinationStep());
          addOrUpdateQuestionnairesStepsBo.setStepOrGroupPostLoad(
                  questionnairesStepsBo.getStepOrGroupPostLoad());
        }
        if (questionnairesStepsBo.getQuestionnairesId() != null) {
          addOrUpdateQuestionnairesStepsBo.setQuestionnairesId(
              questionnairesStepsBo.getQuestionnairesId());
        }
        if (questionnairesStepsBo.getInstructionFormId() != null) {
          addOrUpdateQuestionnairesStepsBo.setInstructionFormId(
              questionnairesStepsBo.getInstructionFormId());
        }
        if (questionnairesStepsBo.getStepType() != null) {
          addOrUpdateQuestionnairesStepsBo.setStepType(questionnairesStepsBo.getStepType());
        }
        if (questionnairesStepsBo.getCreatedOn() != null) {
          addOrUpdateQuestionnairesStepsBo.setCreatedOn(questionnairesStepsBo.getCreatedOn());
        }
        if (questionnairesStepsBo.getCreatedBy() != null) {
          addOrUpdateQuestionnairesStepsBo.setCreatedBy(questionnairesStepsBo.getCreatedBy());
        }
        if (questionnairesStepsBo.getModifiedOn() != null) {
          addOrUpdateQuestionnairesStepsBo.setModifiedOn(questionnairesStepsBo.getModifiedOn());
        }
        if (questionnairesStepsBo.getModifiedBy() != null) {
          addOrUpdateQuestionnairesStepsBo.setModifiedBy(questionnairesStepsBo.getModifiedBy());
        }
        if (questionnairesStepsBo.getType() != null) {
          if (questionnairesStepsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
            addOrUpdateQuestionnairesStepsBo.setStatus(false);
            activity = "Content saved for Form Step.";
            activitydetails =
                "Content saved for Form Step. (Question Key = "
                    + questionnairesStepsBo.getStepShortTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ")";
            query =
                session
                    .createSQLQuery(
                        "update questionnaires q set q.status=0 where q.id=:questionnaireId ")
                    .setInteger(
                        "questionnaireId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
            query.executeUpdate();
          } else if (questionnairesStepsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
            addOrUpdateQuestionnairesStepsBo.setStatus(true);
            activity =
                "Question of form step succesfully checked for minimum content completeness.";
            activitydetails =
                "Question of form step succesfully checked for minimum content completeness and marked 'Done'.(Question Key = "
                    + questionnairesStepsBo.getStepShortTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ")";
          }
        }
        int count = 0;
        // adding the form step to questionnaire for the first time form
        // step creation
        if (addOrUpdateQuestionnairesStepsBo.getQuestionnairesId() != null
            && addOrUpdateQuestionnairesStepsBo.getStepId() == null) {
          FormBo formBo = new FormBo();
          formBo.setActive(true);
          formBo.setCreatedOn(addOrUpdateQuestionnairesStepsBo.getCreatedOn());
          formBo.setCreatedBy(addOrUpdateQuestionnairesStepsBo.getCreatedBy());
          session.saveOrUpdate(formBo);
          addOrUpdateQuestionnairesStepsBo.setQuestionnairesId(
              addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
          addOrUpdateQuestionnairesStepsBo.setInstructionFormId(formBo.getFormId());
          addOrUpdateQuestionnairesStepsBo.setStepType(FdahpStudyDesignerConstants.FORM_STEP);
          QuestionnairesStepsBo existedQuestionnairesStepsBo = null;
          query =
              session
                  .getNamedQuery("getQuestionnaireStepSequenceNo")
                  .setInteger(
                      "questionnairesId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
          query.setMaxResults(1);
          existedQuestionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
          if (existedQuestionnairesStepsBo != null) {
            count = existedQuestionnairesStepsBo.getSequenceNo() + 1;
          } else {
            count = count + 1;
          }
          addOrUpdateQuestionnairesStepsBo.setSequenceNo(count);
        }
        session.saveOrUpdate(addOrUpdateQuestionnairesStepsBo);
        if (addOrUpdateQuestionnairesStepsBo != null && count > 0) {
          String updateQuery =
              "update QuestionnairesStepsBo QSBO set QSBO.destinationStep=:stepId"
                  + " where "
                  + "QSBO.destinationStep=0 and QSBO.sequenceNo=:sequenceNo"
                  + " and QSBO.questionnairesId=:questionnairesId ";
          session
              .createQuery(updateQuery)
              .setInteger("sequenceNo", (count - 1))
              .setInteger(
                  "questionnairesId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId())
              .setInteger("stepId", addOrUpdateQuestionnairesStepsBo.getStepId())
              .executeUpdate();
        }
      }
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sesObj,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - saveOrUpdateFromQuestionnaireStep");

      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveOrUpdateFromQuestionnaireStep() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateFromQuestionnaireStep() - Ends");
    return addOrUpdateQuestionnairesStepsBo;
  }

  /**
   * Create the instruction step in Questionnaire which lays the instruction to user in mobile
   * app.Admin would needs to fill the short title instruction title and instruction text.
   *
   * @author BTC
   * @param Object , instructionBo {@link InstructionsBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String : customStudyId in {@link StudyBo}
   * @return {@link InstructionsBo}
   */
  @Override
  public InstructionsBo saveOrUpdateInstructionsBo(
      InstructionsBo instructionsBo, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateInstructionsBo() - Starts");
    Session session = null;
    QuestionnairesStepsBo existedQuestionnairesStepsBo = null;
    String activitydetails = "";
    String activity = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session.saveOrUpdate(instructionsBo);
      QuestionnairesStepsBo questionnairesStepsBo = null;
      if (instructionsBo != null
          && instructionsBo.getId() != null
          && instructionsBo.getQuestionnairesStepsBo() != null) {
        if (instructionsBo.getQuestionnairesStepsBo().getStepId() != null) {
          questionnairesStepsBo =
              (QuestionnairesStepsBo)
                  session.get(
                      QuestionnairesStepsBo.class,
                      instructionsBo.getQuestionnairesStepsBo().getStepId());
        } else {
          questionnairesStepsBo = new QuestionnairesStepsBo();
          questionnairesStepsBo.setActive(true);
          questionnairesStepsBo.setDestinationStep(0);
        }
        questionnairesStepsBo.setQuestionnairesId(instructionsBo.getQuestionnaireId());
        questionnairesStepsBo.setInstructionFormId(instructionsBo.getId());
        questionnairesStepsBo.setStepType(FdahpStudyDesignerConstants.INSTRUCTION_STEP);
        if (instructionsBo.getQuestionnairesStepsBo().getStepShortTitle() != null
            && !instructionsBo.getQuestionnairesStepsBo().getStepShortTitle().isEmpty()) {
          questionnairesStepsBo.setStepShortTitle(
              instructionsBo.getQuestionnairesStepsBo().getStepShortTitle());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getSkiappable() != null
            && !instructionsBo.getQuestionnairesStepsBo().getSkiappable().isEmpty()) {
          questionnairesStepsBo.setSkiappable(
              instructionsBo.getQuestionnairesStepsBo().getSkiappable());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getRepeatable() != null
            && !instructionsBo.getQuestionnairesStepsBo().getRepeatable().isEmpty()) {
          questionnairesStepsBo.setRepeatable(
              instructionsBo.getQuestionnairesStepsBo().getRepeatable());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getRepeatableText() != null
            && !instructionsBo.getQuestionnairesStepsBo().getRepeatableText().isEmpty()) {
          questionnairesStepsBo.setRepeatableText(
              instructionsBo.getQuestionnairesStepsBo().getRepeatableText());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getDestinationStep() != null) {
          questionnairesStepsBo.setDestinationStep(
              instructionsBo.getQuestionnairesStepsBo().getDestinationStep());
          questionnairesStepsBo.setStepOrGroupPostLoad(
                  instructionsBo.getQuestionnairesStepsBo().getStepOrGroupPostLoad());
          }
        if (instructionsBo.getQuestionnairesStepsBo().getCreatedOn() != null) {
          questionnairesStepsBo.setCreatedOn(
              instructionsBo.getQuestionnairesStepsBo().getCreatedOn());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getCreatedBy() != null) {
          questionnairesStepsBo.setCreatedBy(
              instructionsBo.getQuestionnairesStepsBo().getCreatedBy());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getModifiedOn() != null) {
          questionnairesStepsBo.setModifiedOn(
              instructionsBo.getQuestionnairesStepsBo().getModifiedOn());
        }
        if (instructionsBo.getQuestionnairesStepsBo().getModifiedBy() != null) {
          questionnairesStepsBo.setModifiedBy(
              instructionsBo.getQuestionnairesStepsBo().getModifiedBy());
        }
        if (instructionsBo.getType() != null) {
          if (instructionsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
            questionnairesStepsBo.setStatus(false);
            activity = FdahpStudyDesignerConstants.INSTRUCTION_ACTIVITY + " saved.";
            activitydetails =
                "Content saved for  instruction Step. (Step Key  = "
                    + instructionsBo.getInstructionTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ")";
            query =
                session
                    .createSQLQuery(
                        "update questionnaires q set q.status=0 where q.id=:questionnairesId ")
                    .setInteger("questionnairesId", questionnairesStepsBo.getQuestionnairesId());
            query.executeUpdate();
          } else if (instructionsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
            questionnairesStepsBo.setStatus(true);
            activity = FdahpStudyDesignerConstants.INSTRUCTION_ACTIVITY + " marked Done.";
            activitydetails =
                "Instruction step succesfully checked for minimum content completeness and marked 'Done'. (Step Key  = "
                    + instructionsBo.getInstructionTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ")";
          }
        }
        int count = 0;
        if (instructionsBo.getQuestionnaireId() != null
            && questionnairesStepsBo.getStepId() == null) {
          query =
              session
                  .getNamedQuery("getQuestionnaireStepSequenceNo")
                  .setInteger("questionnairesId", instructionsBo.getQuestionnaireId());
          query.setMaxResults(1);
          existedQuestionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
          if (existedQuestionnairesStepsBo != null) {
            count = existedQuestionnairesStepsBo.getSequenceNo() + 1;
          } else {
            count = count + 1;
          }
          questionnairesStepsBo.setSequenceNo(count);
        }
        session.saveOrUpdate(questionnairesStepsBo);
        instructionsBo.setQuestionnairesStepsBo(questionnairesStepsBo);
        if (questionnairesStepsBo != null && count > 0) {
          String updateQuery =
              "update QuestionnairesStepsBo QSBO set QSBO.destinationStep=:stepId "
                  + " where "
                  + "QSBO.destinationStep=0 and QSBO.sequenceNo=:sequenceNo"
                  + " and QSBO.questionnairesId=:questionnairesId ";
          session
              .createQuery(updateQuery)
              .setInteger("stepId", questionnairesStepsBo.getStepId())
              .setInteger("questionnairesId", instructionsBo.getQuestionnaireId())
              .setInteger("sequenceNo", (count - 1))
              .executeUpdate();
        }
      }
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - saveOrUpdateInstructionsBo");
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveOrUpdateInstructionsBo() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateInstructionsBo() - Ends");
    return instructionsBo;
  }

  /**
   * Question of a form step contains the two attributes .Question-level attributes-these are the
   * same set of attributes as that for question step with the exception of the skippable property
   * and branching logic based on participant choice of response or the conditional logic based
   * branching Response-level attributes (same as that for Question Step).Here we can save or update
   * the form questions.
   *
   * @author BTC
   * @param Object , questionBo{@link QuestionsBo}
   * @return {@link QuestionsBo}
   */
  @Override
  public QuestionsBo saveOrUpdateQuestion(QuestionsBo questionsBo) {
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateQuestion() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      // Ancrodate text start
      if (questionsBo.getUseAnchorDate() != null && questionsBo.getUseAnchorDate()) {
        if (StringUtils.isNotEmpty(questionsBo.getAnchorDateName())) {
          Integer studyId = this.getStudyIdByCustomStudy(session, questionsBo.getCustomStudyId());
          AnchorDateTypeBo anchorDateTypeBo = new AnchorDateTypeBo();
          anchorDateTypeBo.setId(questionsBo.getAnchorDateId());
          anchorDateTypeBo.setCustomStudyId(questionsBo.getCustomStudyId());
          anchorDateTypeBo.setStudyId(studyId);
          anchorDateTypeBo.setName(questionsBo.getAnchorDateName());
          anchorDateTypeBo.setHasAnchortypeDraft(1);
          session.saveOrUpdate(anchorDateTypeBo);
          if (anchorDateTypeBo.getId() != null) {
            questionsBo.setAnchorDateId(anchorDateTypeBo.getId());
          }
        }
      } else {
        if (questionsBo.getAnchorDateId() != null && questionsBo.getId() != null) {
          query =
              session
                  .getNamedQuery("getStudyByCustomStudyId")
                  .setMaxResults(1)
                  .setString("customStudyId", questionsBo.getCustomStudyId());
          query.setMaxResults(1);
          StudyVersionBo studyVersionBo = (StudyVersionBo) query.uniqueResult();
          Integer studyId = this.getStudyIdByCustomStudy(session, questionsBo.getCustomStudyId());
          SessionObject sessionObject = new SessionObject();
          sessionObject.setUserId(questionsBo.getModifiedBy());
          boolean isChange = false;
          if (questionsBo.getIsShorTitleDuplicate() != null
              && questionsBo.getIsShorTitleDuplicate() > 0) {
            isChange = true;
          }
          updateAnchordateInQuestionnaire(
              session,
              transaction,
              studyVersionBo,
              null,
              sessionObject,
              studyId,
              null,
              questionsBo.getId(),
              "",
              isChange);
          questionsBo.setAnchorDateId(null);
        }
      }
      // Anchordate Text end
      session.saveOrUpdate(questionsBo);
      if (questionsBo != null && questionsBo.getId() != null && questionsBo.getFromId() != null) {
        QuestionReponseTypeBo addQuestionReponseTypeBo =
            getQuestionsResponseTypeBo(questionsBo.getQuestionReponseTypeBo(), session);
        if (addQuestionReponseTypeBo != null) {
          if (addQuestionReponseTypeBo.getQuestionsResponseTypeId() == null) {
            addQuestionReponseTypeBo.setQuestionsResponseTypeId(questionsBo.getId());
          }
          session.saveOrUpdate(addQuestionReponseTypeBo);
        }
        questionsBo.setQuestionReponseTypeBo(addQuestionReponseTypeBo);
        if (questionsBo.getQuestionResponseSubTypeList() != null
            && !questionsBo.getQuestionResponseSubTypeList().isEmpty()) {
          String deletQuesry =
              "Delete From QuestionResponseSubTypeBo QRSTBO where QRSTBO.responseTypeId=:responseTypeId ";
          session
              .createQuery(deletQuesry)
              .setInteger("responseTypeId", questionsBo.getId())
              .executeUpdate();
          if (questionsBo.getResponseType() == 4
              || questionsBo.getResponseType() == 3
              || questionsBo.getResponseType() == 6
              || questionsBo.getResponseType() == 5) {
            int i = 0;
            // uploading the images for ImageChoice response type
            // questions
            for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                questionsBo.getQuestionResponseSubTypeList()) {
              if ((questionResponseSubTypeBo.getText() != null
                      && !questionResponseSubTypeBo.getText().isEmpty())
                  && (questionResponseSubTypeBo.getValue() != null
                      && !questionResponseSubTypeBo.getValue().isEmpty())) {
                String fileName;
                if (questionResponseSubTypeBo.getImageFile() != null) {
                  if (questionResponseSubTypeBo.getImage() != null
                      && !questionResponseSubTypeBo.getImage().isEmpty()) {
                    questionResponseSubTypeBo.setImage(questionResponseSubTypeBo.getImage());
                  } else {
                    fileName =
                        FdahpStudyDesignerUtil.getStandardFileName(
                            FdahpStudyDesignerConstants.FORM_STEP_IMAGE + i,
                            questionResponseSubTypeBo.getImageFile().getOriginalFilename(),
                            String.valueOf(questionsBo.getId()));
                    String imagePath =
                        FdahpStudyDesignerUtil.uploadImageFile(
                            questionResponseSubTypeBo.getImageFile(),
                            fileName,
                            FdahpStudyDesignerConstants.QUESTIONNAIRE);
                    questionResponseSubTypeBo.setImage(imagePath);
                  }
                }
                if (questionResponseSubTypeBo.getSelectImageFile() != null) {
                  if (questionResponseSubTypeBo.getSelectedImage() != null
                      && !questionResponseSubTypeBo.getSelectedImage().isEmpty()) {
                    questionResponseSubTypeBo.setSelectedImage(
                        questionResponseSubTypeBo.getSelectedImage());
                  } else {
                    fileName =
                        FdahpStudyDesignerUtil.getStandardFileName(
                            FdahpStudyDesignerConstants.FORM_STEP_SELECTEDIMAGE + i,
                            questionResponseSubTypeBo.getSelectImageFile().getOriginalFilename(),
                            String.valueOf(questionsBo.getId()));
                    String imagePath =
                        FdahpStudyDesignerUtil.uploadImageFile(
                            questionResponseSubTypeBo.getSelectImageFile(),
                            fileName,
                            FdahpStudyDesignerConstants.QUESTIONNAIRE);
                    questionResponseSubTypeBo.setSelectedImage(imagePath);
                  }
                }
                questionResponseSubTypeBo.setResponseTypeId(questionsBo.getId());
                questionResponseSubTypeBo.setActive(true);
                session.save(questionResponseSubTypeBo);
              }
              i = i + 1;
            }
          } else {
            for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                questionsBo.getQuestionResponseSubTypeList()) {
              questionResponseSubTypeBo.setResponseTypeId(questionsBo.getId());
              questionResponseSubTypeBo.setActive(true);
              session.save(questionResponseSubTypeBo);
            }
          }
        }
        // updating the questionnaire and questionnaire step status to
        // incomplete because the admin saving the content question not
        // mark as completed
        if (!questionsBo.getStatus()) {
          if (questionsBo.getQuestionnaireId() != null) {
            query =
                session
                    .createQuery(
                        "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:fromId "
                            + " and QSBO.stepType=:stepType "
                            + " and QSBO.active=1 and QSBO.questionnairesId=:questionnairesId ")
                    .setInteger("fromId", questionsBo.getFromId())
                    .setInteger("questionnairesId", questionsBo.getQuestionnaireId())
                    .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
          } else {
            query =
                session
                    .getNamedQuery("getQuestionnaireStep")
                    .setInteger("instructionFormId", questionsBo.getFromId())
                    .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
          }

          QuestionnairesStepsBo questionnairesStepsBo =
              (QuestionnairesStepsBo) query.uniqueResult();
          if (questionnairesStepsBo != null && questionnairesStepsBo.getStatus()) {
            questionnairesStepsBo.setStatus(false);
            session.saveOrUpdate(questionnairesStepsBo);
            query =
                session
                    .createSQLQuery(
                        "update questionnaires q set q.status=0 where q.id=:questionnairesId ")
                    .setInteger("questionnairesId", questionnairesStepsBo.getQuestionnairesId());
            query.executeUpdate();
          }
        }
        query =
            session.getNamedQuery("getFormMappingBO").setInteger("questionId", questionsBo.getId());
        FormMappingBo formMappingBo = (FormMappingBo) query.uniqueResult();
        if (formMappingBo == null) {
          formMappingBo = new FormMappingBo();
          formMappingBo.setFormId(questionsBo.getFromId());
          formMappingBo.setQuestionId(questionsBo.getId());
          formMappingBo.setActive(true);
          int sequenceNo = 0;
          query =
              session
                  .createQuery(
                      "From FormMappingBo FMBO where FMBO.formId=:formId "
                          + " and FMBO.active=1 order by FMBO.sequenceNo DESC ")
                  .setInteger("formId", questionsBo.getFromId());
          query.setMaxResults(1);
          FormMappingBo existedFormMappingBo = (FormMappingBo) query.uniqueResult();
          if (existedFormMappingBo != null) {
            sequenceNo = existedFormMappingBo.getSequenceNo() + 1;
          } else {
            sequenceNo = sequenceNo + 1;
          }
          formMappingBo.setSequenceNo(sequenceNo);
          session.save(formMappingBo);
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveOrUpdateQuestion() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateQuestion() - Ends");
    return questionsBo;
  }

  /**
   * Create or update of questionnaire in study which contains content and scheduling which can be
   * managed by the admin.The questionnaire schedule frequency can be One
   * time,Daily,Weekly,Monthly,Custom and admin has to select any one frequency.
   *
   * @author BTC
   * @param Object , questionnaireBo {@link QuestionnaireBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return {@link QuestionnaireBo}
   */
  @Override
  public QuestionnaireBo saveORUpdateQuestionnaire(
      QuestionnaireBo questionnaireBo, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - saveORUpdateQuestionnaire() - Starts");
    Session session = null;
    String activitydetails = "";
    String activity = "";
    AnchorDateTypeBo anchorDateTypeBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (null != questionnaireBo && null != questionnaireBo.getAnchorDateId()) {
        anchorDateTypeBo =
            (AnchorDateTypeBo)
                session.get(AnchorDateTypeBo.class, questionnaireBo.getAnchorDateId());
      }
      if (null != anchorDateTypeBo
          && null != anchorDateTypeBo.getParticipantProperty()
          && anchorDateTypeBo.getParticipantProperty()) {

        query =
            session.createQuery(
                "select count(*) from QuestionnaireBo QBO  where QBO.studyId=:studyId and QBO.anchorDateId=:anchorDateId and QBO.active=1");
        query.setInteger("studyId", questionnaireBo.getStudyId());
        query.setInteger("anchorDateId", questionnaireBo.getAnchorDateId());
        Long count = (Long) query.uniqueResult();
        if (count < 1) {
          query =
              session.createQuery(
                  "Update ParticipantPropertiesBO PBO SET PBO.isUsedInQuestionnaire = 1 where PBO.anchorDateId=:anchorDateId");
          query.setInteger("anchorDateId", questionnaireBo.getAnchorDateId());
          query.executeUpdate();
        }
      }
      session.saveOrUpdate(questionnaireBo);
      if (questionnaireBo.getType().equalsIgnoreCase(FdahpStudyDesignerConstants.SCHEDULE)) {
        if (questionnaireBo != null && questionnaireBo.getId() != null) {
          if (questionnaireBo.getQuestionnairesFrequenciesList() != null
              && !questionnaireBo.getQuestionnairesFrequenciesList().isEmpty()) {
            query =
                session
                    .createSQLQuery("CALL deleteQuestionnaireFrequencies(:questionnaireId)")
                    .setInteger("questionnaireId", questionnaireBo.getId());
            query.executeUpdate();
            for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
                questionnaireBo.getQuestionnairesFrequenciesList()) {
              if (questionnairesFrequenciesBo.getFrequencyTime() != null) {
                questionnairesFrequenciesBo.setFrequencyTime(
                    FdahpStudyDesignerUtil.getFormattedDate(
                        questionnairesFrequenciesBo.getFrequencyTime(),
                        FdahpStudyDesignerConstants.SDF_TIME,
                        FdahpStudyDesignerConstants.UI_SDF_TIME));
                if (questionnairesFrequenciesBo.getQuestionnairesId() == null) {
                  questionnairesFrequenciesBo.setId(null);
                  questionnairesFrequenciesBo.setQuestionnairesId(questionnaireBo.getId());
                }
                session.saveOrUpdate(questionnairesFrequenciesBo);
              }
            }
          }
          if (questionnaireBo.getQuestionnairesFrequenciesBo() != null) {
            QuestionnairesFrequenciesBo questionnairesFrequenciesBo =
                questionnaireBo.getQuestionnairesFrequenciesBo();
            if (!questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_DAILY)
                && !questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(questionnaireBo.getPreviousFrequency())) {
              query =
                  session
                      .createSQLQuery("CALL deleteQuestionnaireFrequencies(:questionnaireId)")
                      .setInteger("questionnaireId", questionnaireBo.getId());
              query.executeUpdate();
            }
            if (questionnairesFrequenciesBo.getFrequencyDate() != null
                || questionnairesFrequenciesBo.getFrequencyTime() != null
                || questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)) {
              if (questionnairesFrequenciesBo.getQuestionnairesId() == null) {
                questionnairesFrequenciesBo.setQuestionnairesId(questionnaireBo.getId());
              }
              if (questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyDate() != null
                  && !questionnaireBo
                      .getQuestionnairesFrequenciesBo()
                      .getFrequencyDate()
                      .isEmpty()) {
                questionnairesFrequenciesBo.setFrequencyDate(
                    FdahpStudyDesignerUtil.getFormattedDate(
                        questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyDate(),
                        FdahpStudyDesignerConstants.UI_SDF_DATE,
                        FdahpStudyDesignerConstants.SD_DATE_FORMAT));
              }
              if (questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyTime() != null
                  && !questionnaireBo
                      .getQuestionnairesFrequenciesBo()
                      .getFrequencyTime()
                      .isEmpty()) {
                questionnaireBo
                    .getQuestionnairesFrequenciesBo()
                    .setFrequencyTime(
                        FdahpStudyDesignerUtil.getFormattedDate(
                            questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyTime(),
                            FdahpStudyDesignerConstants.SDF_TIME,
                            FdahpStudyDesignerConstants.UI_SDF_TIME));
              }
              session.saveOrUpdate(questionnairesFrequenciesBo);
            }
          }
          if (questionnaireBo.getQuestionnaireCustomScheduleBo() != null
              && !questionnaireBo.getQuestionnaireCustomScheduleBo().isEmpty()) {
            query =
                session
                    .createSQLQuery("CALL deleteQuestionnaireFrequencies(:questionnaireId)")
                    .setInteger("questionnaireId", questionnaireBo.getId());
            query.executeUpdate();
            for (QuestionnaireCustomScheduleBo questionnaireCustomScheduleBo :
                questionnaireBo.getQuestionnaireCustomScheduleBo()) {
              if (questionnaireCustomScheduleBo.getFrequencyTime() != null) {
                if (questionnaireCustomScheduleBo.getQuestionnairesId() == null) {
                  questionnaireCustomScheduleBo.setQuestionnairesId(questionnaireBo.getId());
                }
                if (questionnaireCustomScheduleBo.getFrequencyEndDate() != null
                    && !questionnaireCustomScheduleBo.getFrequencyEndDate().isEmpty()) {
                  questionnaireCustomScheduleBo.setFrequencyEndDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionnaireCustomScheduleBo.getFrequencyEndDate(),
                          FdahpStudyDesignerConstants.UI_SDF_DATE,
                          FdahpStudyDesignerConstants.SD_DATE_FORMAT));
                }
                if (questionnaireCustomScheduleBo.getFrequencyStartDate() != null
                    && !questionnaireCustomScheduleBo.getFrequencyStartDate().isEmpty()) {
                  questionnaireCustomScheduleBo.setFrequencyStartDate(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionnaireCustomScheduleBo.getFrequencyStartDate(),
                          FdahpStudyDesignerConstants.UI_SDF_DATE,
                          FdahpStudyDesignerConstants.SD_DATE_FORMAT));
                }
                if (questionnaireCustomScheduleBo.getFrequencyTime() != null
                    && !questionnaireCustomScheduleBo.getFrequencyTime().isEmpty()) {
                  questionnaireCustomScheduleBo.setFrequencyTime(
                      FdahpStudyDesignerUtil.getFormattedDate(
                          questionnaireCustomScheduleBo.getFrequencyTime(),
                          FdahpStudyDesignerConstants.SDF_TIME,
                          FdahpStudyDesignerConstants.UI_SDF_TIME));
                }
                questionnaireCustomScheduleBo.setxDaysSign(
                    questionnaireCustomScheduleBo.isxDaysSign());
                if (questionnaireCustomScheduleBo.getTimePeriodFromDays() != null) {
                  questionnaireCustomScheduleBo.setTimePeriodFromDays(
                      questionnaireCustomScheduleBo.getTimePeriodFromDays());
                }
                questionnaireCustomScheduleBo.setyDaysSign(
                    questionnaireCustomScheduleBo.isyDaysSign());
                if (questionnaireCustomScheduleBo.getTimePeriodToDays() != null) {
                  questionnaireCustomScheduleBo.setTimePeriodToDays(
                      questionnaireCustomScheduleBo.getTimePeriodToDays());
                }
                session.saveOrUpdate(questionnaireCustomScheduleBo);
              }
            }
          }
        }
      }
      // updating the anchor date of an study while change the
      // questionnaire frequency in schedule part
      if (!questionnaireBo
          .getFrequency()
          .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)) {
        String updateFromQuery =
            "update questions QBO,form_mapping f,questionnaires_steps QSBO SET QBO.use_anchor_date = 0 where "
                + " QBO.id=f.question_id and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=:questionnaireId "
                + " and QSBO.active=1 "
                + " and QSBO.step_type=:stepType "
                + " and QBO.active=1";
        query =
            session
                .createSQLQuery(updateFromQuery)
                .setInteger("questionnaireId", questionnaireBo.getId())
                .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP);
        query.executeUpdate();

        String updateQuestionSteps =
            "Update questions QBO,questionnaires_steps QSBO SET QBO.use_anchor_date = 0 where QBO.id=QSBO.instruction_form_id"
                + " and QSBO.questionnaires_id=:questionnaireId "
                + " and QSBO.active=1 and "
                + " QSBO.step_type=:stepType "
                + "  and QBO.active=1";
        query =
            session
                .createSQLQuery(updateQuestionSteps)
                .setInteger("questionnaireId", questionnaireBo.getId())
                .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP);
        query.executeUpdate();
      }
      // updating the stastic option of dashboard while change the
      // questionnaire frequency
      if (questionnaireBo.getFrequency() != null
          && questionnaireBo
              .getFrequency()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_DAILY)) {
        if (questionnaireBo.getCurrentFrequency() != null
            && questionnaireBo.getPreviousFrequency() != null
            && !questionnaireBo
                .getCurrentFrequency()
                .equalsIgnoreCase(questionnaireBo.getPreviousFrequency())) {
          updateLineChartSchedule(
              questionnaireBo.getId(),
              questionnaireBo.getCurrentFrequency(),
              sessionObject,
              session,
              transaction,
              customStudyId);
        }
      } else if (questionnaireBo.getPreviousFrequency() != null
          && !questionnaireBo
              .getFrequency()
              .equalsIgnoreCase(questionnaireBo.getPreviousFrequency())) {
        updateLineChartSchedule(
            questionnaireBo.getId(),
            questionnaireBo.getFrequency(),
            sessionObject,
            session,
            transaction,
            customStudyId);
      }

      activity = "Content saved for questionnaire.";
      activitydetails = "Content saved for questionnaire. (Study ID = " + customStudyId + ").";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - saveORUpdateQuestionnaire");

      if (questionnaireBo != null && questionnaireBo.getStatus()) {
        auditLogDAO.updateDraftToEditedStatus(
            session,
            transaction,
            sessionObject.getUserId(),
            FdahpStudyDesignerConstants.DRAFT_QUESTIONNAIRE,
            questionnaireBo.getStudyId());
        // Notification Purpose needed Started
        queryString = " From StudyBo where customStudyId=:customStudyId and live=1";
        StudyBo studyBo =
            (StudyBo)
                session
                    .createQuery(queryString)
                    .setString("customStudyId", customStudyId)
                    .uniqueResult();
        if (studyBo != null) {
          queryString = " From StudyBo where id=:studyId";
          StudyBo draftStudyBo =
              (StudyBo)
                  session
                      .createQuery(queryString)
                      .setInteger("studyId", questionnaireBo.getStudyId())
                      .uniqueResult();
          NotificationBO notificationBO = null;
          queryString =
              "From NotificationBO where questionnarieId=:questionnarieId "
                  + "and studyId=:studyId ";
          notificationBO =
              (NotificationBO)
                  session
                      .createQuery(queryString)
                      .setMaxResults(1)
                      .setInteger("studyId", questionnaireBo.getStudyId())
                      .setInteger("questionnarieId", questionnaireBo.getId())
                      .uniqueResult();
          if (!questionnaireBo.getScheduleType().equalsIgnoreCase("AnchorDate")) {
            if (notificationBO == null) {
              notificationBO = new NotificationBO();
              notificationBO.setStudyId(questionnaireBo.getStudyId());
              notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
              if (StringUtils.isNotEmpty(studyBo.getAppId()))
                notificationBO.setAppId(studyBo.getAppId());
              notificationBO.setNotificationType(FdahpStudyDesignerConstants.NOTIFICATION_ST);
              notificationBO.setNotificationSubType(
                  FdahpStudyDesignerConstants.NOTIFICATION_SUBTYPE_ACTIVITY);
              notificationBO.setNotificationScheduleType(
                  FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
              notificationBO.setQuestionnarieId(questionnaireBo.getId());
              notificationBO.setNotificationStatus(false);
              notificationBO.setCreatedBy(sessionObject.getUserId());
              notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              notificationBO.setNotificationSent(false);
            } else {
              notificationBO.setModifiedBy(sessionObject.getUserId());
              notificationBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            }
            notificationBO.setNotificationText(
                FdahpStudyDesignerConstants.NOTIFICATION_ACTIVETASK_TEXT
                    .replace("$shortTitle", questionnaireBo.getTitle())
                    .replace("$customId", draftStudyBo.getName()));
            if (!notificationBO.isNotificationSent()) session.saveOrUpdate(notificationBO);
          }
        }
        // Notification Purpose needed End
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveORUpdateQuestionnaire() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveORUpdateQuestionnaire() - Ends");
    return questionnaireBo;
  }

  /**
   * Admin can add the question step to questionnaire here which contains the 3 subsections admin
   * has to fill the sub section such as step level attribute,question level attribute,response
   * level attributes.Questions can be various types as defined by the response format. Depending on
   * the response format, the attributes of the QA would vary Here we can create or update the
   * question step in questionnaire
   *
   * @author BTC
   * @param Object , questionnaireStepsBo {@link QuestionnairesStepsBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @Override
  public QuestionnairesStepsBo saveOrUpdateQuestionStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sessionObject,
      String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateQuestionStep() - Starts");
    Session session = null;
    QuestionnairesStepsBo addOrUpdateQuestionnairesStepsBo = null;
    String activitydetails = "";
    String activity = "";
    boolean isChange = false;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (questionnairesStepsBo != null) {
        if (questionnairesStepsBo.getStepId() != null) {
          addOrUpdateQuestionnairesStepsBo =
              (QuestionnairesStepsBo)
                  session.get(QuestionnairesStepsBo.class, questionnairesStepsBo.getStepId());
        } else {
          addOrUpdateQuestionnairesStepsBo = new QuestionnairesStepsBo();
          addOrUpdateQuestionnairesStepsBo.setActive(true);
          addOrUpdateQuestionnairesStepsBo.setDestinationStep(0);
        }
        if (questionnairesStepsBo.getStepShortTitle() != null
            && !questionnairesStepsBo.getStepShortTitle().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setStepShortTitle(
              questionnairesStepsBo.getStepShortTitle());
        }
        if (questionnairesStepsBo.getSkiappable() != null
            && !questionnairesStepsBo.getSkiappable().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setSkiappable(questionnairesStepsBo.getSkiappable());
        }
        if (questionnairesStepsBo.getRepeatable() != null
            && !questionnairesStepsBo.getRepeatable().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setRepeatable(questionnairesStepsBo.getRepeatable());
        }
        if (questionnairesStepsBo.getRepeatableText() != null
            && !questionnairesStepsBo.getRepeatableText().isEmpty()) {
          addOrUpdateQuestionnairesStepsBo.setRepeatableText(
              questionnairesStepsBo.getRepeatableText());
        }
        if (questionnairesStepsBo.getDestinationStep() != null) {
          addOrUpdateQuestionnairesStepsBo.setDestinationStep(
              questionnairesStepsBo.getDestinationStep());
          addOrUpdateQuestionnairesStepsBo.setStepOrGroupPostLoad(
                  questionnairesStepsBo.getStepOrGroupPostLoad());
        }
        if (questionnairesStepsBo.getQuestionnairesId() != null) {
          addOrUpdateQuestionnairesStepsBo.setQuestionnairesId(
              questionnairesStepsBo.getQuestionnairesId());
        }
        if (questionnairesStepsBo.getInstructionFormId() != null) {
          addOrUpdateQuestionnairesStepsBo.setInstructionFormId(
              questionnairesStepsBo.getInstructionFormId());
        }
        if (questionnairesStepsBo.getStepType() != null) {
          addOrUpdateQuestionnairesStepsBo.setStepType(questionnairesStepsBo.getStepType());
        }
        if (questionnairesStepsBo.getType() != null) {
          if (questionnairesStepsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
            addOrUpdateQuestionnairesStepsBo.setStatus(false);
            activity = "Question step saved.";
            activitydetails =
                "Content saved for question step. (Step Key  = "
                    + questionnairesStepsBo.getStepShortTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ") ";
            query =
                session
                    .createSQLQuery(
                        "update questionnaires q set q.status=0 where q.id=:questionnairesId ")
                    .setInteger(
                        "questionnairesId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
            query.executeUpdate();
          } else if (questionnairesStepsBo
              .getType()
              .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
            addOrUpdateQuestionnairesStepsBo.setStatus(true);
            activity = "Question step successfully checked for minimum content completeness.";
            activitydetails =
                "Question step successfully checked for minimum content completeness and marked 'Done'. (Step Key  = "
                    + questionnairesStepsBo.getStepShortTitle()
                    + ", Study ID = "
                    + customStudyId
                    + ") ";
          }
        }
        int count = 0;
        if (questionnairesStepsBo.getQuestionsBo() != null) {
          addOrUpdateQuestionnairesStepsBo.setQuestionnairesId(
              addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
          QuestionsBo questionsBo = questionnairesStepsBo.getQuestionsBo();
          // Ancrodate text start
          if (questionnairesStepsBo.getQuestionsBo().getUseAnchorDate() != null
              && questionnairesStepsBo.getQuestionsBo().getUseAnchorDate()) {
            if (StringUtils.isNotEmpty(
                questionnairesStepsBo.getQuestionsBo().getAnchorDateName())) {
              Integer studyId = this.getStudyIdByCustomStudy(session, customStudyId);
              AnchorDateTypeBo anchorDateTypeBo = new AnchorDateTypeBo();
              anchorDateTypeBo.setId(questionnairesStepsBo.getQuestionsBo().getAnchorDateId());
              anchorDateTypeBo.setCustomStudyId(customStudyId);
              anchorDateTypeBo.setStudyId(studyId);
              anchorDateTypeBo.setName(questionnairesStepsBo.getQuestionsBo().getAnchorDateName());
              anchorDateTypeBo.setHasAnchortypeDraft(1);
              session.saveOrUpdate(anchorDateTypeBo);
              if (anchorDateTypeBo.getId() != null) {
                questionsBo.setAnchorDateId(anchorDateTypeBo.getId());
              }
            }
          } else {
            if (questionnairesStepsBo.getIsShorTitleDuplicate() != null
                && questionnairesStepsBo.getIsShorTitleDuplicate() > 0) {
              isChange = true;
            }
            if (StringUtils.isEmpty(customStudyId)) {
              customStudyId = questionsBo.getCustomStudyId();
            }
            if (questionnairesStepsBo.getQuestionsBo().getAnchorDateId() != null
                && questionsBo != null
                && questionsBo.getId() != null) {
              query =
                  session
                      .getNamedQuery("getStudyByCustomStudyId")
                      .setMaxResults(1)
                      .setString("customStudyId", customStudyId);
              query.setMaxResults(1);
              StudyVersionBo studyVersionBo = (StudyVersionBo) query.uniqueResult();
              Integer studyId = this.getStudyIdByCustomStudy(session, customStudyId);
              updateAnchordateInQuestionnaire(
                  session,
                  transaction,
                  studyVersionBo,
                  null,
                  sessionObject,
                  studyId,
                  null,
                  questionsBo.getId(),
                  "",
                  isChange);
            }
            // session.createQuery("delete from AnchorDateTypeBo where
            // id="+questionnairesStepsBo.getQuestionsBo().getAnchorDateId()).executeUpdate();
            questionsBo.setAnchorDateId(null);
          }
          // Anchordate Text end
          session.saveOrUpdate(questionsBo);
          addOrUpdateQuestionnairesStepsBo.setQuestionsBo(questionsBo);
          // adding or updating the response level attributes
          if (questionsBo != null
              && questionsBo.getId() != null
              && questionnairesStepsBo.getQuestionReponseTypeBo() != null) {
            QuestionReponseTypeBo questionResponseTypeBo =
                getQuestionsResponseTypeBo(
                    questionnairesStepsBo.getQuestionReponseTypeBo(), session);
            if (questionResponseTypeBo != null) {
              if (questionResponseTypeBo.getQuestionsResponseTypeId() == null) {
                questionResponseTypeBo.setQuestionsResponseTypeId(questionsBo.getId());
              }
              session.saveOrUpdate(questionResponseTypeBo);
            }
            addOrUpdateQuestionnairesStepsBo.setQuestionReponseTypeBo(questionResponseTypeBo);
            if (questionnairesStepsBo.getQuestionResponseSubTypeList() != null
                && !questionnairesStepsBo.getQuestionResponseSubTypeList().isEmpty()) {
              String deletQuesry =
                  "Delete From QuestionResponseSubTypeBo QRSTBO where QRSTBO.responseTypeId=:responseTypeId ";
              session
                  .createQuery(deletQuesry)
                  .setInteger("responseTypeId", questionsBo.getId())
                  .executeUpdate();
              // upload the images in response level
              if (questionnairesStepsBo.getQuestionsBo().getResponseType() == 4
                  || questionnairesStepsBo.getQuestionsBo().getResponseType() == 3
                  || questionnairesStepsBo.getQuestionsBo().getResponseType() == 6
                  || questionnairesStepsBo.getQuestionsBo().getResponseType() == 5) {
                int j = 0;
                for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                    questionnairesStepsBo.getQuestionResponseSubTypeList()) {
                  if ((questionResponseSubTypeBo.getText() != null
                          && !questionResponseSubTypeBo.getText().isEmpty())
                      && (questionResponseSubTypeBo.getValue() != null
                          && !questionResponseSubTypeBo.getValue().isEmpty())) {
                    String fileName;
                    if (questionResponseSubTypeBo.getImageFile() != null) {
                      if (questionResponseSubTypeBo.getImage() != null
                          && !questionResponseSubTypeBo.getImage().isEmpty()) {
                        questionResponseSubTypeBo.setImage(questionResponseSubTypeBo.getImage());
                      } else {
                        fileName =
                            FdahpStudyDesignerUtil.getStandardFileName(
                                FdahpStudyDesignerConstants.QUESTION_STEP_IMAGE + j,
                                questionResponseSubTypeBo.getImageFile().getOriginalFilename(),
                                String.valueOf(questionnairesStepsBo.getQuestionsBo().getId()));
                        String imagePath =
                            FdahpStudyDesignerUtil.uploadImageFile(
                                questionResponseSubTypeBo.getImageFile(),
                                fileName,
                                FdahpStudyDesignerConstants.QUESTIONNAIRE);
                        questionResponseSubTypeBo.setImage(imagePath);
                      }
                    }
                    if (questionResponseSubTypeBo.getSelectImageFile() != null) {
                      if (questionResponseSubTypeBo.getSelectedImage() != null
                          && !questionResponseSubTypeBo.getSelectedImage().isEmpty()) {
                        questionResponseSubTypeBo.setSelectedImage(
                            questionResponseSubTypeBo.getSelectedImage());
                      } else {
                        fileName =
                            FdahpStudyDesignerUtil.getStandardFileName(
                                FdahpStudyDesignerConstants.QUESTION_STEP_SELECTEDIMAGE + j,
                                questionResponseSubTypeBo
                                    .getSelectImageFile()
                                    .getOriginalFilename(),
                                String.valueOf(questionnairesStepsBo.getQuestionsBo().getId()));
                        String imagePath =
                            FdahpStudyDesignerUtil.uploadImageFile(
                                questionResponseSubTypeBo.getSelectImageFile(),
                                fileName,
                                FdahpStudyDesignerConstants.QUESTIONNAIRE);
                        questionResponseSubTypeBo.setSelectedImage(imagePath);
                      }
                    }
                    questionResponseSubTypeBo.setResponseTypeId(questionsBo.getId());
                    questionResponseSubTypeBo.setActive(true);
                    session.save(questionResponseSubTypeBo);
                  }
                  j = j + 1;
                }
              } else {
                for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                    questionnairesStepsBo.getQuestionResponseSubTypeList()) {
                  questionResponseSubTypeBo.setResponseTypeId(questionsBo.getId());
                  questionResponseSubTypeBo.setActive(true);
                  session.save(questionResponseSubTypeBo);
                }
              }
            } else {
              String deletQuesry =
                  "Delete From QuestionResponseSubTypeBo QRSTBO where QRSTBO.responseTypeId=:questionId";
              session
                  .createQuery(deletQuesry)
                  .setInteger("questionId", questionsBo.getId())
                  .executeUpdate();
            }
            // condition branching adding for the response type that
            // results in the data type 'double' and checked as
            // formula based branching is yes
            if (questionResponseTypeBo != null
                && questionResponseTypeBo
                    .getFormulaBasedLogic()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              if (questionnairesStepsBo.getQuestionConditionBranchBoList() != null
                  && !questionnairesStepsBo.getQuestionConditionBranchBoList().isEmpty()) {
                String deleteQuery =
                    "delete from question_condtion_branching where question_id=:questionId";
                session
                    .createSQLQuery(deleteQuery)
                    .setInteger("questionId", questionsBo.getId())
                    .executeUpdate();
                for (QuestionConditionBranchBo questionConditionBranchBo :
                    questionnairesStepsBo.getQuestionConditionBranchBoList()) {
                  if (questionConditionBranchBo.getQuestionId() == null) {
                    questionConditionBranchBo.setQuestionId(questionsBo.getId());
                  }
                  if (questionConditionBranchBo.getInputType() != null
                      && questionConditionBranchBo.getInputType().equalsIgnoreCase("MF")) {
                    session.save(questionConditionBranchBo);
                  }
                  if (questionConditionBranchBo.getQuestionConditionBranchBos() != null
                      && !questionConditionBranchBo.getQuestionConditionBranchBos().isEmpty()) {
                    for (QuestionConditionBranchBo conditionBranchBo :
                        questionConditionBranchBo.getQuestionConditionBranchBos()) {
                      if (conditionBranchBo.getInputType() != null
                          && conditionBranchBo.getInputTypeValue() != null) {
                        if (conditionBranchBo.getQuestionId() == null) {
                          conditionBranchBo.setQuestionId(questionsBo.getId());
                        }
                        session.save(conditionBranchBo);
                      }
                    }
                  }
                }
              }
            } else {
              String deleteQuery =
                  "delete from question_condtion_branching where question_id=:questionId";
              session
                  .createSQLQuery(deleteQuery)
                  .setInteger("questionId", questionsBo.getId())
                  .executeUpdate();
            }
          }

          if (questionsBo != null) {
            addOrUpdateQuestionnairesStepsBo.setInstructionFormId(questionsBo.getId());
          }
          // updating the sequence no of step based on the previous
          // sequence no
          if (addOrUpdateQuestionnairesStepsBo.getQuestionnairesId() != null
              && addOrUpdateQuestionnairesStepsBo.getStepId() == null) {
            QuestionnairesStepsBo existedQuestionnairesStepsBo = null;
            query =
                session
                    .getNamedQuery("getQuestionnaireStepSequenceNo")
                    .setInteger(
                        "questionnairesId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId());
            query.setMaxResults(1);
            existedQuestionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
            if (existedQuestionnairesStepsBo != null) {
              count = existedQuestionnairesStepsBo.getSequenceNo() + 1;
            } else {
              count = count + 1;
            }
            addOrUpdateQuestionnairesStepsBo.setSequenceNo(count);
          }
        }
        session.saveOrUpdate(addOrUpdateQuestionnairesStepsBo);
        // updating the destination step for previous step
        if (addOrUpdateQuestionnairesStepsBo != null && count > 0) {
          String updateQuery =
              "update QuestionnairesStepsBo QSBO set QSBO.destinationStep=:stepId "
                  + " where "
                  + "QSBO.destinationStep=0 and QSBO.sequenceNo=:sequenceNo"
                  + " and QSBO.questionnairesId=:questionnairesId ";
          session
              .createQuery(updateQuery)
              .setInteger("sequenceNo", (count - 1))
              .setInteger("stepId", addOrUpdateQuestionnairesStepsBo.getStepId())
              .setInteger(
                  "questionnairesId", addOrUpdateQuestionnairesStepsBo.getQuestionnairesId())
              .executeUpdate();
        }
      }

      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - saveOrUpdateInstructionsBo");

      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveOrUpdateQuestionStep() - Error", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateQuestionStep() - Ends");
    return addOrUpdateQuestionnairesStepsBo;
  }

  @Autowired
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.hibernateTemplate = new HibernateTemplate(sessionFactory);
  }

  /**
   * Update the questions status to draft when changing the frequency of questionnaire if the line
   * chart is enabled for that question in user for statistics section
   *
   * @author BTC
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @param String , frequency in {@link QuestionnaireBo}
   * @param Object , {@link SessionObject}
   * @param {@link Session}
   * @param {@link Transaction}
   * @param String , customStudyId in {@link StudyBo}
   * @return Success/Failure
   */
  public String updateLineChartSchedule(
      Integer questionnaireId,
      String frequency,
      SessionObject sessionObject,
      Session session,
      Transaction transaction,
      String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - updateLineChartSchedule() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    String timeRange = "";
    Session newSession = null;
    String activity = "";
    String activitydetails = "";
    try {
      if (session == null) {
        newSession = hibernateTemplate.getSessionFactory().openSession();
        transaction = newSession.beginTransaction();
      }
      timeRange = FdahpStudyDesignerUtil.getTimeRangeString(frequency);
      // updating the question steps
      String searchQuery =
          " update questions QBO,questionnaires_steps QSBO set QBO.status=0, QBO.modified_by=:userId "
              + ",QBO.modified_on=:currentDateAndTime "
              + " where QBO.id=QSBO.instruction_form_id and QSBO.questionnaires_id=:questionnaireId "
              + " and QSBO.step_type='Question' and QSBO.active=1 and QBO.active=1 and QBO.add_line_chart='Yes' and QBO.line_chart_timerange not in ("
              + " :timeRange )";
      if (newSession != null) {
        newSession
            .createSQLQuery(searchQuery)
            .setInteger("userId", sessionObject.getUserId())
            .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
            .setInteger("questionnaireId", questionnaireId)
            .setParameterList("timeRange", Arrays.asList(timeRange))
            .executeUpdate();
      } else {
        session
            .createSQLQuery(searchQuery)
            .setInteger("userId", sessionObject.getUserId())
            .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
            .setInteger("questionnaireId", questionnaireId)
            .setParameterList("timeRange", Arrays.asList(timeRange))
            .executeUpdate();
      }
      // updating the form step questions
      String formQuery =
          "update questionnaires_steps qs,form_mapping f, questions QBO  set qs.status=0,qs.modified_by=:userId "
              + ",qs.modified_on=:currentDateAndTime "
              + ",QBO.status=0,QBO.modified_by=:userId "
              + ",QBO.modified_on=:currentDateAndTime"
              + " where qs.step_type = 'Form' and qs.instruction_form_id= f.form_id"
              + " and f.question_id = QBO.id and f.active=1 and QBO.active=1 and QBO.add_line_chart='Yes' "
              + " and QBO.line_chart_timerange not in (:timeRange "
              + " ) and qs.questionnaires_id=:questionnaireId "
              + " and qs.active=1";
      if (newSession != null) {
        newSession
            .createSQLQuery(formQuery)
            .setInteger("userId", sessionObject.getUserId())
            .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
            .setInteger("questionnaireId", questionnaireId)
            .setParameterList("timeRange", Arrays.asList(timeRange))
            .executeUpdate();
      } else {
        session
            .createSQLQuery(formQuery)
            .setInteger("userId", sessionObject.getUserId())
            .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
            .setInteger("questionnaireId", questionnaireId)
            .setParameterList("timeRange", Arrays.asList(timeRange))
            .executeUpdate();
      }
      activity = FdahpStudyDesignerConstants.FORMSTEP_ACTIVITY + " saved.";
      activitydetails = "Content saved for Form Step. (Study ID = " + customStudyId + ")";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - updateLineChartSchedule()");

      activity = FdahpStudyDesignerConstants.QUESTIONSTEP_ACTIVITY + " saved.";
      activitydetails = "Content saved for Question Step. (Study ID = " + customStudyId + ")";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - updateLineChartSchedule()");

      activity = FdahpStudyDesignerConstants.FORMSTEP_QUESTION_ACTIVITY + " saved.";
      activitydetails =
          "Content saved for Question of Form Step. (Study ID = " + customStudyId + ")";
      auditLogDAO.saveToAuditLog(
          session,
          transaction,
          sessionObject,
          activity,
          activitydetails,
          "StudyQuestionnaireDAOImpl - updateLineChartSchedule()");
      if (session == null) transaction.commit();
    } catch (Exception e) {
      if (session == null && null != transaction) {
        transaction.rollback();
      }
      logger.error("StudyQuestionnaireDAOImpl - updateLineChartSchedule() - ERROR ", e);
    } finally {
      if (null != newSession) {
        newSession.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - updateLineChartSchedule() - Ends");
    return message;
  }

  /**
   * The admin can choose to add a response data element to the study dashboard in the form of line
   * charts or statistics.Adding a line chart to the dashboard needs the admin to specify The
   * options time range for the chart which depend on the scheduling frequency set for the
   * activity.when admin change the frequency in questionnaire schedule its validate the options in
   * the time range for chart options.
   *
   * @author BTC
   * @param Integer , questionnaireId
   * @param String , frequency
   * @param String Success or failure
   */
  @Override
  public String validateLineChartSchedule(Integer questionnaireId, String frequency) {
    logger.info("StudyQuestionnaireDAOImpl - validateLineChartSchedule() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    String timeRange = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      timeRange = FdahpStudyDesignerUtil.getTimeRangeString(frequency);
      // checking in the question step
      String searchQuery =
          "select count(*) from questions QBO,questionnaires_steps QSBO where QBO.id=QSBO.instruction_form_id and QSBO.questionnaires_id=:questionnaireId "
              + " and QSBO.active=1 and QSBO.step_type=:stepType "
              + " and QBO.active=1 and QBO.add_line_chart='Yes' and QBO.line_chart_timerange not in ( :timeRange ) ";

      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setString("stepType", FdahpStudyDesignerConstants.QUESTION_STEP)
                  .setInteger("questionnaireId", questionnaireId)
                  .setParameterList("timeRange", Arrays.asList(timeRange))
                  .uniqueResult();
      if (count != null && count.intValue() > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      } else {
        // checking in the form step questions
        String searchSubQuery =
            "select count(*) from questions QBO,form_mapping f,questionnaires_steps QSBO where QBO.id=f.question_id and f.form_id=QSBO.instruction_form_id and QSBO.questionnaires_id=:questionnaireId "
                + " and QSBO.active=1 and QSBO.step_type=:stepType "
                + " and QBO.active=1 and QBO.add_line_chart = 'Yes' and QBO.line_chart_timerange not in (:timeRange) ";
        BigInteger subCount =
            (BigInteger)
                session
                    .createSQLQuery(searchSubQuery)
                    .setString("stepType", FdahpStudyDesignerConstants.FORM_STEP)
                    .setInteger("questionnaireId", questionnaireId)
                    .setParameterList("timeRange", Arrays.asList(timeRange))
                    .uniqueResult();
        if (subCount != null && subCount.intValue() > 0) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - validateLineChartSchedule() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - validateLineChartSchedule() - Ends");
    return message;
  }

  /**
   * In Questionnaire form step carries the multiple question and Answers .In form level attributes
   * we can make form form as repeatable if the form is repeatable we can not add the line chart and
   * states data to the dashbord.here we are validating the added line chart and statistics data
   * before updating the form as repeatable.
   *
   * @author BTC
   * @param Integer , formId
   * @param String , Success or failure
   */
  @Override
  public String validateRepetableFormQuestionStats(Integer formId) {
    logger.info("StudyQuestionnaireDAOImpl - validateRepetableFormQuestionStats() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      String searchQuery =
          "select count(*) from questions q,form_mapping f where q.id=f.question_id and q.active=1 and f.active=1 and f.form_id=:formId "
              + " and (q.add_line_chart = 'Yes' or q.use_stastic_data='Yes' or q.use_anchor_date=true)";
      BigInteger questionCount =
          (BigInteger)
              session.createSQLQuery(searchQuery).setInteger("formId", formId).uniqueResult();
      if (questionCount != null && questionCount.intValue() > 0) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - validateRepetableFormQuestionStats() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - validateRepetableFormQuestionStats() - Ends");
    return message;
  }

  /**
   * In Questionnaire form step carries the multiple question and Answers .In form level attributes
   * we can make form form as repeatable if the form is repeatable we can not add the line chart and
   * states data to the dashbord.here we are validating the added line chart and statistics data
   * before updating the form as repeatable.
   *
   * @author BTC
   * @param Integer , formId
   * @param String , Success or failure
   */
  @Override
  public String checkUniqueAnchorDateName(
      String anchordateText, String customStudyId, String anchorDateId) {
    logger.info("StudyQuestionnaireDAOImpl - checkUniqueAnchorDateName() - starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    Integer dbAnchorId = 0;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();

      if (StringUtils.isNotEmpty(anchorDateId)) {
        dbAnchorId =
            (Integer)
                session
                    .createSQLQuery("select q.id from anchordate_type q where q.id=:anchorDateId")
                    .setString("anchorDateId", anchorDateId)
                    .uniqueResult();
        if (!dbAnchorId.equals(Integer.parseInt(anchorDateId))) dbAnchorId = 0;
      }
      if (dbAnchorId == 0) {
        String searchQuery =
            "select count(*) from anchordate_type a"
                + " where a.name=:anchordateText "
                + " and a.custom_study_id=:customStudyId ";
        BigInteger questionCount =
            (BigInteger)
                session
                    .createSQLQuery(searchQuery)
                    .setString("anchordateText", anchordateText)
                    .setString("customStudyId", customStudyId)
                    .uniqueResult();
        if (questionCount != null && questionCount.intValue() > 0) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkUniqueAnchorDateName() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - checkUniqueAnchorDateName() - Ends");
    return message;
  }

  @Override
  public Integer getStudyIdByCustomStudy(Session session, String customStudyId) {
    logger.info(
        "INFO: ActivityMetaDataDao - getQuestionnaireFrequencyAncorDetailsForManuallySchedule() :: Starts");
    Integer studyId = null;
    try {
      String searchQuery =
          "select id from studies where custom_study_id=:customStudyId and is_live=0";
      studyId =
          (Integer)
              session
                  .createSQLQuery(searchQuery)
                  .setString("customStudyId", customStudyId)
                  .uniqueResult();

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - checkUniqueAnchorDateName() - ERROR ", e);
    }
    return studyId;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AnchorDateTypeBo> getAnchorTypesByStudyId(String customStudyId) {
    logger.info("StudyQuestionnaireDAOImpl - getAnchorTypesByStudyId - Starts");
    Session session = null;
    List<AnchorDateTypeBo> anchorDateTypeBos = null;
    String queryString = "";
    String subQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      StudyBo studyBo =
          (StudyBo)
              session
                  .createQuery("from StudyBo where customStudyId=:customStudyId and live=0")
                  .setString("customStudyId", customStudyId)
                  .uniqueResult();
      if (studyBo != null) {
        if (!studyBo.isEnrollmentdateAsAnchordate()) {
          subQuery = "and name != '" + FdahpStudyDesignerConstants.ANCHOR_TYPE_ENROLLMENTDATE + "'";
        }
      }
      // queryString = "From AnchorDateTypeBo where customStudyId='"+customStudyId+ "'
      // and hasAnchortypeDraft=1"+subQuery;
      // Added by sweta
      queryString =
          "From AnchorDateTypeBo where customStudyId=:customStudyId and hasAnchortypeDraft=1";
      anchorDateTypeBos =
          session.createQuery(queryString).setString("customStudyId", customStudyId).list();

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getAnchorTypesByStudyId - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getAnchorTypesByStudyId - Ends");
    return anchorDateTypeBos;
  }

  @Override
  public boolean isAnchorDateExistByQuestionnaire(Integer questionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistByQuestionnaire - Starts");
    Session session = null;
    Boolean isExist = false;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      // checking in the question step anchor date is selected or not
      String searchQuery =
          "select count(q.anchor_date_id) from questions q,questionnaires_steps qsq,questionnaires qq  where q.id=qsq.instruction_form_id and qsq.step_type='Question' "
              + "and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.id=:questionnaireId "
              + " and qq.active=1 and q.active=1;";
      BigInteger count =
          (BigInteger)
              session
                  .createSQLQuery(searchQuery)
                  .setInteger("questionnaireId", questionnaireId)
                  .uniqueResult();
      if (count.intValue() > 0) {
        isExist = true;
      } else {
        // checking in the form step question anchor date is
        // selected or not
        String subQuery =
            "select count(q.anchor_date_id) from questions q,form_mapping fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and f.form_id=fm.form_id and f.active=1 "
                + "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and qsf.questionnaires_id=qq.id and qq.id=:questionnaireId "
                + " and q.active=1";
        BigInteger subCount =
            (BigInteger)
                session
                    .createSQLQuery(subQuery)
                    .setInteger("questionnaireId", questionnaireId)
                    .uniqueResult();
        if (subCount != null && subCount.intValue() > 0) {
          isExist = true;
        }
      }

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isAnchorDateExistByQuestionnaire - Error");
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isAnchorDateExistByQuestionnaire - Ends");
    return isExist;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String updateAnchordateInQuestionnaire(
      Session session,
      Transaction transaction,
      StudyVersionBo studyVersionBo,
      Integer questionnaireId,
      SessionObject sessionObject,
      Integer studyId,
      Integer stepId,
      Integer questionId,
      String stepType,
      boolean isChange) {
    logger.info("StudyQuestionnaireDAOImpl - updateAnchordateInQuestionnaire - Starts");
    List<Integer> anchorIds = new ArrayList<Integer>();
    List<Integer> anchorExistIds = new ArrayList<Integer>();
    Boolean isAnchorUsed = false;
    String searchQuery = "";
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      if (!stepType.isEmpty()) {
        if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.QUESTION_STEP)) {
          searchQuery =
              "select q.anchor_date_id from questions q,questionnaires_steps qsq,questionnaires qq where q.id=qsq.instruction_form_id and qsq.step_type='Question' "
                  + "and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.id=:questionnaireId "
                  + " and q.id=:stepId "
                  + " and qq.active=1 and q.active=1"
                  + " and q.anchor_date_id IS NOT NULL;";
          List<Integer> aIds =
              session
                  .createSQLQuery(searchQuery)
                  .setInteger("stepId", stepId)
                  .setInteger("questionnaireId", questionnaireId)
                  .list();
          if (aIds != null && aIds.size() > 0) anchorIds.addAll(aIds);
        } else if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)) {
          String subQuery =
              "select q.anchor_date_id from questions q,form_mapping fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and f.form_id=fm.form_id and f.active=1 "
                  + "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and qsf.questionnaires_id=qq.id and qq.id=:questionnaireId "
                  + " and f.form_id=:stepId "
                  + " and q.active=1"
                  + " and q.anchor_date_id IS NOT NULL;";
          List<Integer> aaIds =
              session
                  .createSQLQuery(subQuery)
                  .setInteger("stepId", stepId)
                  .setInteger("questionnaireId", questionnaireId)
                  .list();
          if (aaIds != null && aaIds.size() > 0) anchorIds.addAll(aaIds);
        }
      }
      // Question level deletion
      if (questionId != null) {
        searchQuery =
            "select q.anchor_date_id from questions q where q.active=1 and q.id=:questionId"
                + " and q.anchor_date_id IS NOT NULL;";
        List<Integer> aIds =
            session.createSQLQuery(searchQuery).setInteger("questionId", questionId).list();
        if (aIds != null && aIds.size() > 0) anchorIds.addAll(aIds);
      }
      // Questionnaire level deletion
      if (stepId == null && questionnaireId != null) {
        // checking in the question step anchor date is selected or not
        searchQuery =
            "select q.anchor_date_id from questions q,questionnaires_steps qsq,questionnaires qq where q.id=qsq.instruction_form_id and qsq.step_type='Question'"
                + " and qsq.active=1 and qsq.questionnaires_id=qq.id and qq.id=:questionnaireId "
                + " and qq.active=1 and q.active=1"
                + " and q.anchor_date_id IS NOT NULL;";
        List<Integer> aIds =
            session
                .createSQLQuery(searchQuery)
                .setInteger("questionnaireId", questionnaireId)
                .list();
        if (aIds != null && aIds.size() > 0) anchorIds.addAll(aIds);
        // checking in the form step question anchor date is
        // selected or not
        String subQuery =
            "select q.anchor_date_id from questions q,form_mapping fm,form f,questionnaires_steps qsf,questionnaires qq where q.id=fm.question_id and f.form_id=fm.form_id and f.active=1 "
                + "and f.form_id=qsf.instruction_form_id and qsf.step_type='Form' and qsf.questionnaires_id=qq.id and qq.id=:questionnaireId"
                + " and q.active=1"
                + " and q.anchor_date_id IS NOT NULL;";
        List<Integer> aaIds =
            session.createSQLQuery(subQuery).setInteger("questionnaireId", questionnaireId).list();
        if (aaIds != null && aaIds.size() > 0) anchorIds.addAll(aaIds);
      }
      if (!anchorIds.isEmpty() && anchorIds.size() > 0) {
        searchQuery =
            "select q.id from questionnaires q where q.schedule_type=:type"
                + " and q.anchor_date_id in( :anchorIds )";
        anchorExistIds =
            session
                .createSQLQuery(searchQuery)
                .setString("type", FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)
                .setParameterList("anchorIds", anchorIds)
                .list();
        if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
          isAnchorUsed = true;
        } else {
          searchQuery =
              "select q.id from active_task q where q.schedule_type=:type"
                  + " and q.anchor_date_id in( :anchorIds )";
          anchorExistIds =
              session
                  .createSQLQuery(searchQuery)
                  .setString("type", FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)
                  .setParameterList("anchorIds", anchorIds)
                  .list();
          if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
            isAnchorUsed = true;
          } else {
            searchQuery = "select q.id from resources q where q.anchor_date_id in( :anchorIds )";
            anchorExistIds =
                session.createSQLQuery(searchQuery).setParameterList("anchorIds", anchorIds).list();
            if (!anchorExistIds.isEmpty() && anchorExistIds.size() > 0) {
              isAnchorUsed = true;
            }
          }
        }
        if (studyVersionBo != null && isChange) {
          if (isAnchorUsed) {
            message = FdahpStudyDesignerConstants.FAILURE + "anchorused";
            return message;
          } else {
            String deleteAncQuery = "delete from anchordate_type where id IN( :anchorIds )";
            query = session.createSQLQuery(deleteAncQuery).setParameterList("anchorIds", anchorIds);
            query.executeUpdate();
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else {
          if (isAnchorUsed) {
            StudySequenceBo studySequence =
                (StudySequenceBo)
                    session
                        .getNamedQuery("getStudySequenceByStudyId")
                        .setInteger("studyId", studyId)
                        .uniqueResult();
            if (studySequence != null) {
              int count1 =
                  session
                      .createSQLQuery(
                          "update questionnaires set status=0,anchor_date_id=null,"
                              + "modified_by=:userId "
                              + ",modified_date=:currentDateAndTime"
                              + " where active=1 and anchor_date_id in( :anchorIds ) ")
                      .setInteger("userId", sessionObject.getUserId())
                      .setParameterList("anchorIds", anchorIds)
                      .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
                      .executeUpdate();
              if (count1 > 0) {
                studySequence.setStudyExcQuestionnaries(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    sessionObject.getUserId(),
                    FdahpStudyDesignerConstants.DRAFT_QUESTIONNAIRE,
                    studyId);
              }
              int count2 =
                  session
                      .createSQLQuery(
                          "update active_task set action=0 ,anchor_date_id=null, modified_by=:userId "
                              + ",modified_date=:currentDateAndTime"
                              + " where active=1 and anchor_date_id in( :anchorIds )")
                      .setInteger("userId", sessionObject.getUserId())
                      .setParameterList("anchorIds", anchorIds)
                      .setString("currentDateAndTime", FdahpStudyDesignerUtil.getCurrentDateTime())
                      .executeUpdate();
              if (count2 > 0) {
                studySequence.setStudyExcActiveTask(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    sessionObject.getUserId(),
                    FdahpStudyDesignerConstants.DRAFT_ACTIVETASK,
                    studyId);
              }
              int count3 =
                  session
                      .createSQLQuery(
                          "update resources set action=0,anchor_date_id=null "
                              + "where status=1 and anchor_date_id in( :anchorIds )")
                      .setParameterList("anchorIds", anchorIds)
                      .executeUpdate();
              if (count3 > 0) {
                studySequence.setMiscellaneousResources(false);
                auditLogDAO.updateDraftToEditedStatus(
                    session,
                    transaction,
                    sessionObject.getUserId(),
                    FdahpStudyDesignerConstants.DRAFT_STUDY,
                    studyId);
              }
              session.saveOrUpdate(studySequence);
            }
          }
          String deleteAncQuery = "delete from anchordate_type where id IN( :anchorIds )";
          query = session.createSQLQuery(deleteAncQuery).setParameterList("anchorIds", anchorIds);
          query.executeUpdate();
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      } else {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - updateAnchordateInQuestionnaire - ERROR ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - updateAnchordateInQuestionnaire - Ends");
    return message;
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

  @Override
  public InstructionsLangBO getInstructionLangBo(int instructionId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getInstructionLangBo - Starts");
    InstructionsLangBO instructionsLangBO = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      instructionsLangBO =
          (InstructionsLangBO)
              session
                  .createQuery(
                      "from InstructionsLangBO where instructionLangPK.langCode=:language and instructionLangPK.id=:id")
                  .setString("language", language)
                  .setInteger("id", instructionId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getInstructionLangBo - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getInstructionLangBo - Ends");
    return instructionsLangBO;
  }

  @Override
  public FormLangBO getFormLangBo(int formId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getFormLangBo - Starts");
    FormLangBO formLangBO = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      formLangBO =
          (FormLangBO)
              session
                  .createQuery(
                      "from FormLangBO where formLangPK.langCode=:language and formLangPK.formId=:id")
                  .setString("language", language)
                  .setInteger("id", formId)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getFormLangBo - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getFormLangBo - Ends");
    return formLangBO;
  }

  @Override
  public QuestionLangBO getQuestionLangBo(int id, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangBo - Starts");
    QuestionLangBO questionLangBO = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      questionLangBO =
          (QuestionLangBO)
              session
                  .createQuery(
                      "from QuestionLangBO where questionLangPK.langCode=:language and questionLangPK.id=:id")
                  .setString("language", language)
                  .setInteger("id", id)
                  .uniqueResult();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionLangBo - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangBo - Ends");
    return questionLangBO;
  }

  @Override
  public QuestionnaireLangBO getQuestionnaireLangById(Integer questionnaireId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangById - Starts");
    QuestionnaireLangBO questionnaireLangBO = null;
    Session session = null;
    try {
      if (questionnaireId != null) {
        session = hibernateTemplate.getSessionFactory().openSession();
        questionnaireLangBO =
            (QuestionnaireLangBO)
                session
                    .createQuery(
                        "from QuestionnaireLangBO where questionnaireLangPK.langCode=:language and questionnaireLangPK.id=:id and active=true")
                    .setString("language", language)
                    .setInteger("id", questionnaireId)
                    .uniqueResult();
      } else return null;
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireLangById - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangById - Ends");
    return questionnaireLangBO;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnaireLangBO> getQuestionnaireLangByStudyId(Integer studyId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Starts");
    List<QuestionnaireLangBO> questionnaireLangBOList = null;
    Session session = null;
    try {
      if (studyId != null) {
        session = hibernateTemplate.getSessionFactory().openSession();
        questionnaireLangBOList =
            session
                .createQuery(
                    "from QuestionnaireLangBO where questionnaireLangPK.langCode=:language and studyId=:id")
                .setString("language", language)
                .setInteger("id", studyId)
                .list();
      } else return null;
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Ends");
    return questionnaireLangBOList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnaireLangBO> getQuestionnaireLangByQuestionnaireId(int questionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Starts");
    List<QuestionnaireLangBO> questionnaireLangBOList = null;
    Session session = null;
    try {
      if (questionnaireId != 0) {
        session = hibernateTemplate.getSessionFactory().openSession();
        questionnaireLangBOList =
            session
                .createQuery("from QuestionnaireLangBO where questionnaireLangPK.id=:id")
                .setInteger("id", questionnaireId)
                .list();
      } else return null;
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Error : ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnaireLangByStudyId - Ends");
    return questionnaireLangBOList;
  }

  @Override
  public void deleteQuestionStep(int id, String language) {
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireLang - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      session
          .createQuery(
              "delete from QuestionnaireLangBO where questionnaireLangPK.id=:id and questionnaireLangPK.langCode=:language")
          .setInteger("id", id)
          .setString("language", language)
          .executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - deleteQuestionnaireLang - Error : ", e);
      transaction.rollback();
    } finally {
      session.close();
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteQuestionnaireLang - Ends");
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionLangBO> getQuestionLangByQuestionnaireId(
      Integer questionnaireId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Starts");
    List<QuestionLangBO> questionLangBOList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      questionLangBOList =
          session
              .createQuery(
                  "from QuestionLangBO where questionnaireId=:id and questionLangPK.langCode=:language and active=true")
              .setInteger("id", questionnaireId)
              .setString("language", language)
              .list();
      session.close();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Error : ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Ends");
    return questionLangBOList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionLangBO> getQuestionLangByFormId(
      int questionnaireId, int formId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Starts");
    List<QuestionLangBO> questionLangBOList = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      questionLangBOList =
          session
              .createQuery(
                  "from QuestionLangBO where questionnaireId=:id and questionLangPK.langCode=:language and active=true and formId=:formId")
              .setInteger("id", questionnaireId)
              .setString("language", language)
              .setInteger("formId", formId)
              .list();
      session.close();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Error : ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionLangByQuestionnaireId - Ends");
    return questionLangBOList;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<FormLangBO> getFormLangByQuestionnaireId(Integer questionnaireId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getFormLangByQuestionnaireId - Starts");
    List<FormLangBO> formLangBOS = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      formLangBOS =
          session
              .createQuery(
                  "from FormLangBO where questionnaireId=:id and formLangPK.langCode=:language and active=true")
              .setInteger("id", questionnaireId)
              .setString("language", language)
              .list();
      session.close();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getFormLangByQuestionnaireId - Error : ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - getFormLangByQuestionnaireId - Ends");
    return formLangBOS;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<InstructionsLangBO> getInstructionLangByQuestionnaireId(
      Integer questionnaireId, String language) {
    logger.info("StudyQuestionnaireDAOImpl - getInstructionLangByQuestionnaireId - Starts");
    List<InstructionsLangBO> instructionsLangBOS = null;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      instructionsLangBOS =
          session
              .createQuery(
                  "from InstructionsLangBO where questionnaireId=:id and instructionLangPK.langCode=:language and active=true")
              .setInteger("id", questionnaireId)
              .setString("language", language)
              .list();
      session.close();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getInstructionLangByQuestionnaireId - Error : ", e);
    }
    logger.info("StudyQuestionnaireDAOImpl - getInstructionLangByQuestionnaireId - Ends");
    return instructionsLangBOS;
  }
  
  @Override
  public List<GroupsBo> getGroupsByStudyId(String studyId, String questionnaireId) {
  	logger.entry("begin getGroupsByStudyId()");
      Session session = null;
      List<GroupsBo> groups = null;
      String searchQuery = "";
      try {
      	
        session = hibernateTemplate.getSessionFactory().openSession();
        if (StringUtils.isNotBlank(studyId)) {
          query = session.createQuery("From GroupsBo GBO WHERE GBO.studyId =:studyId and GBO.questionnaireId=:questionnaireId order by GBO.id desc")
                  .setParameter("studyId", Integer.parseInt(studyId))
                  .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
        } else {
          query = session.createQuery("From GroupsBo GBO WHERE GBO.questionnaireId=:questionnaireId order by GBO.id desc")
                  .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
        }
        groups = query.list();
      } catch (Exception e) {
        logger.error("StudyQuestionnaireDAOImpl - getGroupsByStudyId() - ERROR ", e);
      } finally {
        if (session != null) {
          session.close();
        }
      }
      logger.exit("getGroupsByStudyId() - Ends");
      return groups;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<GroupsBo> getGroupsByStudyIdAndStepId(String studyId, String questionnaireId, int stepId) {
    logger.info("begin getGroupsByStudyId()");
    Session session = null;
    List<GroupsBo> groups = null;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery =
              "from GroupsBo WHERE studyId =:studyId and questionnaireId=:questionnaireId and " +
                      "id not in (select grpId from GroupMappingBo where stepId=:stepId) order by id desc";
      query = session.createQuery(searchQuery)
              .setParameter("studyId", Integer.parseInt(studyId))
              .setParameter("stepId", String.valueOf(stepId))
              .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
      groups = query.list();

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupsByStudyId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getGroupsByStudyId() - Ends");
    return groups;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<GroupsBo> getGroupsForPreloadAndPostLoad(String questionnaireId, String queIdForGroups, Integer stepId, boolean isPreload) {
    logger.info("begin getGroupsByStudyId()");
    Session session = null;
    List<GroupsBo> groups = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (isPreload) {
        if (stepId != null) {
          if (StringUtils.isNotBlank(queIdForGroups) && !queIdForGroups.equals(questionnaireId)) {
            query = session.createQuery("from GroupsBo WHERE questionnaireId=:queIdForGroups and " +
                            "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where instructionFormId <> :stepIdInt and questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                            "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step')) " +
                            "order by id")
                    .setParameter("queIdForGroups", Integer.parseInt(queIdForGroups))
                    .setParameter("stepIdInt", stepId)
                    .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
          } else {
            query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and " +
                            "id not in (select grpId from GroupMappingBo where stepId=:stepId) and " +
                            "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where instructionFormId <> :stepId and questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                            "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step')) and " +
                            "id not in (select destinationTrueAsGroup from GroupsBo where questionnaireId=:questionnaireId and stepOrGroup='group') and " +
                            "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationTrueAsGroup from GroupsBo where questionnaireId=:questionnaireId and stepOrGroup='step'))" +
                            "order by id")
                    .setParameter("stepId", String.valueOf(stepId))
                    .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
          }
        } else {
          if (StringUtils.isNotBlank(queIdForGroups) && !queIdForGroups.equals(questionnaireId)) {
            query = session.createQuery("from GroupsBo WHERE questionnaireId=:queIdForGroups and " +
                            "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                            "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step')) " +
                            "order by id")
                    .setParameter("queIdForGroups", Integer.parseInt(queIdForGroups))
                    .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
          } else {
            query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and " +
                            "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                            "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step')) " +
                            "order by id")
                    .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
          }
        }
      } else {
        if (stepId != null) {
          query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and " +
                          "id not in (select grpId from GroupMappingBo where stepId=:stepId) and " +
                          "id not in (select destinationStep from QuestionnairesStepsBo where instructionFormId <> :stepId and questionnairesId=:questionnaireId and stepOrGroupPostLoad='group') and " +
                          "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationStep from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroupPostLoad='step')) " +
                          "order by id")
                  .setParameter("stepId", String.valueOf(stepId))
                  .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
        } else {
          query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and " +
                          "id not in (select destinationStep from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroupPostLoad='group') and " +
                          "id not in (select grpId from GroupMappingBo where questionnaireStepId in (select destinationStep from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroupPostLoad='step')) " +
                          "order by id")
                  .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
        }
      }
      groups = query.list();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupsByStudyId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getGroupsByStudyId() - Ends");
    return groups;
  }

  @Override
  public List<GroupsBo> getGroupsListForGroupsPage(String questionnaireId, int groupId) {
    logger.info("begin getGroupsByStudyId()");
    Session session = null;
    List<GroupsBo> groups = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (groupId != 0) {
        query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and id <> :groupId and " +
                        "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                        "id not in (select destinationTrueAsGroup from GroupsBo where questionnaireId=:questionnaireId and stepOrGroup='group' and id <> :groupId) and " +
                        "id not in (select grpId from GroupMappingBo where questionnaireStepId in " +
                        "(select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step') " +
                        "or questionnaireStepId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'step' and questionnaireId = :questionnaireId and id <> :groupId)) " +
                        "order by id")
                .setParameter("groupId", groupId)
                .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
      } else {
        query = session.createQuery("from GroupsBo WHERE questionnaireId=:questionnaireId and " +
                        "id not in (select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='group') and " +
                        "id not in (select destinationTrueAsGroup from GroupsBo where questionnaireId=:questionnaireId and stepOrGroup='group') and " +
                        "id not in (select grpId from GroupMappingBo where questionnaireStepId in " +
                        "(select destinationTrueAsGroup from QuestionnairesStepsBo where questionnairesId=:questionnaireId and stepOrGroup='step') " +
                        "or questionnaireStepId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'step' and questionnaireId = :questionnaireId)) " +
                        "order by id")
                .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
      }
      groups = query.list();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupsByStudyId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getGroupsByStudyId() - Ends");
    return groups;
  }

  @Override
  public GroupsBo getGroupsDetails(int id) {
    logger.info("StudyDAOImpl - getGroupDetails() - Starts");
    GroupsBo groupsBo = null;
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " FROM  GroupsBo GBO WHERE GBO.id = " + id;
      query = session.createQuery(queryString);
      groupsBo = (GroupsBo) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getGroupDetails() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getGroupDetails() - Ends");
    return groupsBo;
  }

  @Override
  public String addOrUpdateGroupsDetails(GroupsBo groupsBo) {
      logger.info("StudyDAOImpl - addOrUpdateGroupsDetails() - Starts");
      Session session = null;
      String groupId = "";
      String msg = FdahpStudyDesignerConstants.FAILURE;
      Query query;
       GroupsBo groupsBo2 = null;
       boolean updateFlag = false;
      try {
        session = hibernateTemplate.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        if (null == groupsBo.getId()) {
          groupId = (String) session.save(groupsBo);
        } else {
          session.update(groupsBo);
          groupId = groupsBo.getGroupId();
          updateFlag = true;
        }
        query = session.createQuery(" FROM GroupsBo GBO where GBO.groupId = :groupId");
        query.setString("groupId", groupId);
        groupsBo2 = (GroupsBo) query.uniqueResult();
        session.update(groupsBo2);
        transaction.commit();
        msg = FdahpStudyDesignerConstants.SUCCESS;
      } catch (Exception e) {
        transaction.rollback();
        logger.error("studyDAOImpl - addOrUpdateGroupsDetails() - ERROR", e);
      } finally {
        if (null != session) {
          session.close();
        }
      }
      logger.info("studyDAOImpl - addOrUpdateGroupsDetails() - Ends");
      return msg;
  }

  @Override
  public GroupsBo getGroupById(int id) {
    logger.entry("begin getGroupsById()");
    Session session = null;
    GroupsBo groupsBo = null;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery =
              "From GroupsBo GBO WHERE GBO.id=:id";
      groupsBo = (GroupsBo) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupsById() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.exit("getGroupsById() - Ends");
    return groupsBo;
  }

@Override
public String deleteGroup(String id, SessionObject sessionObject) {
	logger.info("StudyQuestionnaireDAOImpl - deleteGroupId() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int count = 0;
    String queryString;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (StringUtils.isNotEmpty(id)) {
    	  queryString  =
                  "delete GroupsBo GBO where GBO.id=:id ";
        query = session.createQuery(queryString);

        count = query.setString("id", id).executeUpdate();
        if (count > 0) message = FdahpStudyDesignerConstants.SUCCESS;
      }
      int gpId = Integer.parseInt(id);
      this.validatePreLoadLogicForGroups(session, gpId);
      if (StringUtils.isNotEmpty(id)){
        count = session.createQuery("delete PreLoadLogicBo PBO where PBO.stepGroupId=:gpId and stepOrGroup=:group")
                .setParameter("gpId", gpId)
                .setParameter("group", "group")
                .executeUpdate();
        if (count > 0) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteGroupId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteGroupId() - Ends");
    return message;
}

@SuppressWarnings("unchecked")
@Override
public String checkGroupId(String questionnaireId, String groupId, String studyId) {
  logger.info("StudyQuestionnaireDAOImpl - checkGroupId() - Starts");
  String message = FdahpStudyDesignerConstants.FAILURE;
  Session session = null;
  List<GroupsBo> groupsBo = null;
  try {
    session = hibernateTemplate.getSessionFactory().openSession();
    if (StringUtils.isNotEmpty(questionnaireId) && StringUtils.isNotEmpty(groupId)) {
    	groupsBo =
          session
              .createQuery(
                  "From GroupsBo GBO WHERE  GBO.groupId= :groupId and GBO.questionnaireId= :questionnaireId and GBO.studyId = :studyId")
              .setString("questionnaireId", questionnaireId)
              .setString("groupId", groupId)
                  .setString("studyId" , (studyId))
              .list();
      if (groupsBo != null && !groupsBo.isEmpty()) {
        message = FdahpStudyDesignerConstants.SUCCESS;
      }
    }
  } catch (Exception e) {
    logger.error("StudyQuestionnaireDAOImpl - checkGroupId() - ERROR ", e);
  } finally {
    if (session != null) {
      session.close();
    }
  }
  logger.info("StudyQuestionnaireDAOImpl - checkGroupId() - Ends");
  return message;
}

@Override
public String saveOrUpdateGroup(GroupsBo groupsBO) {
	logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateGroup() - Starts");
    Session session = null;
    String groupId = "";
    String msg = FdahpStudyDesignerConstants.FAILURE;
    AnchorDateTypeBo anchorDateTypeBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      
      if (null == groupsBO.getId()) {
    	  if (StringUtils.isNotEmpty(groupsBO.getGroupId())) {
    		  
    		  GroupsBo groupsBo1 = 
    				  (GroupsBo) session.getSession().createQuery(" from GroupsBo GBO where GBO.groupId=:groupId and GBO.questionnaireId= :questionnaireId and GBO.studyId = :studyId")
			           .setString("groupId", groupsBO.getGroupId())
			           .setInteger("questionnaireId",(groupsBO.getQuestionnaireId()))
                              .setInteger("studyId" , (groupsBO.getStudyId()))
			           .uniqueResult();
    		  
    	    if(null != groupsBo1 && null != groupsBo1.getGroupId() && groupsBo1.getGroupId().equalsIgnoreCase(groupsBO.getGroupId()) )
    	    {
    	    	groupsBo1.setGroupId(groupsBO.getGroupId());
    	    	groupsBo1.setGroupName(groupsBO.getGroupName());
    	    	groupsBo1.setAction(groupsBO.getAction());
    	    	session.saveOrUpdate(groupsBo1);
    	    }
    	    else {
    	    	session.saveOrUpdate(groupsBO);
    	    	msg = FdahpStudyDesignerConstants.SUC_MSG;
    	 }
    	  }
      }
    	    else {
    	  groupsBO.setGroupId(groupsBO.getGroupId());
    	  groupsBO.setGroupName(groupsBO.getGroupName());
    	  groupsBO.setAction(groupsBO.getAction());
        session.saveOrUpdate(groupsBO);
 	   msg = FdahpStudyDesignerConstants.SUC_MSG;
      }
      QuestionnaireBo questionnaireBo = session.get(QuestionnaireBo.class,
              groupsBO.getQuestionnaireId());
      if (questionnaireBo != null) {
        if (Boolean.FALSE.equals(groupsBO.getAction())) {
          questionnaireBo.setStatus(false);
          session.update(questionnaireBo);
        }
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - saveOrUpdateGroup() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - saveOrUpdateGroup() - Ends");
    return msg;

}

@Override
public String checkGroupName(String questionnaireId, String groupName, String studyId) {
	 logger.info("StudyQuestionnaireDAOImpl - checkGroupName() - Starts");
	  String message = FdahpStudyDesignerConstants.FAILURE;
	  Session session = null;
	  List<GroupsBo> groupsBo = null;
	  try {
	    session = hibernateTemplate.getSessionFactory().openSession();
	    if (StringUtils.isNotEmpty(questionnaireId) && StringUtils.isNotEmpty(groupName)) {
	    	groupsBo =
	          session
	              .createQuery(
	                  "From GroupsBo GBO WHERE  GBO.groupName= :groupName and GBO.questionnaireId= :questionnaireId and GBO.studyId = :studyId")
	              .setString("questionnaireId", questionnaireId)
	              .setString("groupName", groupName)
                      .setString("studyId" , (studyId))
	              .list();
	      if (groupsBo != null && !groupsBo.isEmpty()) {
	        message = FdahpStudyDesignerConstants.SUCCESS;
	      }
	    }
	  } catch (Exception e) {
	    logger.error("StudyQuestionnaireDAOImpl - checkGroupName() - ERROR ", e);
	  } finally {
	    if (session != null) {
	      session.close();
	    }
	  }
	  logger.info("StudyQuestionnaireDAOImpl - checkGroupName() - Ends");
	  return message;
}


  @Override
  @SuppressWarnings("unchecked")
  public List<PreLoadLogicBo> getPreLoadLogicByStep(Integer stepGroupId) {
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadLogicByStep() - Starts");
    Session session = null;
    List<PreLoadLogicBo> preLoadLogicBos = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (stepGroupId != null) {
        String queryString  = "from PreLoadLogicBo where stepGroupId=:stepGroupId";
        preLoadLogicBos = session.createQuery(queryString).setParameter("stepGroupId", stepGroupId).list();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getPreLoadLogicByStep() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadLogicByStep() - Ends");
    return preLoadLogicBos;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Integer> getPreLoadIds(Integer stepId) {
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadIds() - Starts");
    Session session = null;
    List<Integer> preLoadIds = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (stepId != null) {
        String queryString  = "select id from PreLoadLogicBo where stepGroupId=:stepGroupId";
        preLoadIds = session.createQuery(queryString).setParameter("stepGroupId", stepId).list();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getPreLoadIds() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadIds() - Ends");
    return preLoadIds;
  }

  @Override
  public PreLoadLogicBo getPreLoadLogicByIdAndType(Integer id, String type) {
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadLogicById() - Starts");
    Session session = null;
    PreLoadLogicBo preLoadLogicBo = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      if (id != null) {
        String queryString  = "from PreLoadLogicBo where id=:id and stepOrGroup=:type";
        preLoadLogicBo = (PreLoadLogicBo) session.createQuery(queryString)
                .setParameter("id", id)
                .setParameter("type", type)
                .uniqueResult();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getPreLoadLogicById() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getPreLoadLogicById() - Ends");
    return preLoadLogicBo;
  }

  @Override
  public void deleteFormula(List<Integer> ids) {
    logger.info("StudyQuestionnaireDAOImpl - deleteFormula - Starts");
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      if (ids != null && ids.size()>0) {
        session.createQuery(
                        "delete from PreLoadLogicBo where id in (:id)")
                .setParameterList("id", ids)
                .executeUpdate();
        transaction.commit();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - deleteFormula - Error : ", e);
      transaction.rollback();
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteFormula - Ends");
  }

  @Override
  public List<GroupMappingBo> assignQuestionSteps(List<String> arr, Integer grpId, String questionnaireId) {
    logger.info("StudyDAOImpl - assignQuestionSteps() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Session session = null;
    List<GroupMappingBo> listGroupMappingBo = new ArrayList<>();
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      int size = arr.size();
      int i = 1;
      for(String s : arr) {
        String stepId = s.replaceAll("^\"|\"$", "");
         //get stepId by sending stepId[instruction_formId] from questionnaire_step table
        Integer questionnaireStepId =  getquestionnaireStepId(stepId, questionnaireId);
        GroupMappingBo groupMappingBo = new GroupMappingBo();
        groupMappingBo.setGrpId(grpId);
        groupMappingBo.setStepId(s.replaceAll("^\"|\"$", ""));
        groupMappingBo.setStatus(true);
        groupMappingBo.setQuestionnaireStepId(questionnaireStepId);
        session.saveOrUpdate(groupMappingBo);
        query =
                session
                        .getNamedQuery("getQuestionnaireGroupStep")
                        .setInteger("instructionFormId", Integer.parseInt(stepId))
                        .setInteger("questionnairesId", Integer.parseInt(questionnaireId));

        QuestionnairesStepsBo questionnairesStepsBo = (QuestionnairesStepsBo) query.uniqueResult();
        questionnairesStepsBo.setGroupFlag(true);
        if (i == size && FdahpStudyDesignerConstants.FORM_STEP.equals(questionnairesStepsBo.getStepType())) {
          Integer groupId = this.getGroupIdByStep(session, questionnaireStepId);
          if (groupId != null) {
            GroupsBo groupsBo = session.get(GroupsBo.class, groupId);
            if (groupsBo != null
                    && groupsBo.getDefaultVisibility() != null
                    && !groupsBo.getDefaultVisibility()
                    && groupsBo.getDestinationTrueAsGroup() != null) {
              questionnairesStepsBo.setRepeatable("No");
              questionnairesStepsBo.setRepeatableText(null);
            }
          }
        }
        session.saveOrUpdate(questionnairesStepsBo);
        listGroupMappingBo.add(groupMappingBo);
        i++;
      }
      //groupMappingBo = FdahpStudyDesignerConstants.SUCCESS;
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - assignQuestionSteps() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyDAOImpl - assignQuestionSteps() - Ends");
    return listGroupMappingBo;
  }

  private Integer getquestionnaireStepId(String stepId, String questionnaireId) {
    logger.info("begin getquestionnaireStepId()");
    Session session = null;
    Integer questionnairesStepsBo = null;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery =
              "select stepId from QuestionnairesStepsBo WHERE questionnairesId =:questionnaireId and instructionFormId=:stepId";
      query = session.createQuery(searchQuery)
              .setString("stepId", String.valueOf(stepId))
              .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
      questionnairesStepsBo = (Integer) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getquestionnaireStepId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getGroupsByStudyId() - Ends");
    return questionnairesStepsBo;

  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnairesStepsBo> getSameSurveySourceKeys(int queId, int seq, String caller, Integer currStepId, Integer currQuestionnaireId) {
    logger.info("StudyQuestionnaireDAOImpl - getSameSurveySourceKeys() - Starts");
    Session session = null;
    List<QuestionnairesStepsBo> list = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (queId != 0) {
        org.hibernate.query.Query<QuestionnairesStepsBo> query;
        if ("preload".equals(caller)) {
          List<Integer> destList;
          List<Integer> assignedStepsList;
          if (currStepId != null) {
            if (currQuestionnaireId != null && currQuestionnaireId.equals(queId)) {
              destList = session.createQuery("select destinationTrueAsGroup from QuestionnairesStepsBo where active = true and questionnairesId=:queId and stepId <> :stepId")
                      .setParameter("queId", queId)
                      .setParameter("stepId", currStepId)
                      .list();
              assignedStepsList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                              "(select questionnaireStepId from GroupMappingBo where grpId in " +
                              "(select destinationTrueAsGroup from QuestionnairesStepsBo where stepOrGroup = 'group' and questionnairesId = :queId and stepId <> :stepId) or " +
                              "grpId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'group' and questionnaireId = :queId)) or " +
                              "stepId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'step' and questionnaireId = :queId)")
                      .setParameter("queId", queId)
                      .setParameter("stepId", currStepId)
                      .list();
            } else {
              destList = session.createQuery("select destinationTrueAsGroup from QuestionnairesStepsBo where active = true and " +
                              "questionnairesId=:queId and stepId <> :stepId and stepOrGroup='step'")
                      .setParameter("queId", currQuestionnaireId)
                      .setParameter("stepId", currStepId)
                      .list();
              assignedStepsList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                              "(select questionnaireStepId from GroupMappingBo where grpId in " +
                              "(select destinationTrueAsGroup from QuestionnairesStepsBo where stepOrGroup = 'group' and questionnairesId = :queId and stepId <> :stepId))")
                      .setParameter("queId", currQuestionnaireId)
                      .setParameter("stepId", currStepId)
                      .list();
            }
          } else {
            if (currQuestionnaireId != null && currQuestionnaireId.equals(queId)) {
              destList = session.createQuery("select destinationTrueAsGroup from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                      .setParameter("queId", queId)
                      .list();
              assignedStepsList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                              "(select questionnaireStepId from GroupMappingBo where grpId in " +
                              "(select destinationTrueAsGroup from QuestionnairesStepsBo where stepOrGroup = 'group' and questionnairesId = :queId))")
                      .setParameter("queId", queId)
                      .list();
            } else {
              destList = session.createQuery("select destinationTrueAsGroup from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                      .setParameter("queId", currQuestionnaireId)
                      .list();
              assignedStepsList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                              "(select questionnaireStepId from GroupMappingBo where grpId in " +
                              "(select destinationTrueAsGroup from QuestionnairesStepsBo where stepOrGroup = 'group' and questionnairesId = :queId))")
                      .setParameter("queId", currQuestionnaireId)
                      .list();
            }
          }
          destList.addAll(assignedStepsList);
          destList.removeAll(Collections.singleton(null));
          if (seq != -1) {
            if (!destList.isEmpty()) {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and sequenceNo > :seq and stepId not in (:destList)")
                      .setParameter("queId", queId)
                      .setParameter("destList", destList)
                      .setParameter("seq", seq);
            } else {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and sequenceNo > :seq")
                      .setParameter("queId", queId)
                      .setParameter("seq", seq);
            }
          } else {
            if (!destList.isEmpty()) {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and stepId not in (:destList)")
                      .setParameter("destList", destList)
                      .setParameter("queId", queId);
            } else {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                      .setParameter("queId", queId);
            }
          }
          list = query.list();
        } else {
          if (seq != -1) {
            query = session.createQuery("select qs from QuestionnairesStepsBo qs, QuestionsBo qb where qs.active = true " +
                            "and qs.questionnairesId=:queId and qs.sequenceNo < :seq and qb.responseType in (:responseList) " +
                            "and qb.id = qs.instructionFormId")
                    .setParameter("queId", queId)
                    .setParameter("seq", seq)
                    .setParameterList("responseList", Arrays.asList(3, 4, 6, 11));
          } else {
            query = session.createQuery("select qs from QuestionnairesStepsBo qs, QuestionsBo qb where qs.active = true " +
                            "and qs.questionnairesId=:queId and qb.responseType in (:responseList) " +
                            "and qb.id = qs.instructionFormId")
                    .setParameter("queId", queId)
                    .setParameterList("responseList", Arrays.asList(3, 4, 6, 11));
          }
          List<QuestionnairesStepsBo> rawList = query.list();
          list = new ArrayList<>();
          for (QuestionnairesStepsBo stepsBo : rawList) {
            if (!isQuestionMultiSelect(stepsBo.getInstructionFormId())) {
              list.add(stepsBo);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getSameSurveySourceKeys() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getSameSurveySourceKeys() - Ends");
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnairesStepsBo> getStepsForGroups(int queId, int groupId) {
    logger.info("StudyQuestionnaireDAOImpl - getStepsForGroups() - Starts");
    Session session = null;
    List<QuestionnairesStepsBo> list = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (queId != 0) {
        org.hibernate.query.Query<QuestionnairesStepsBo> query;
        List<Integer> assignedSteps = new ArrayList<>();

        List<Integer> destList = session.createQuery("select destinationTrueAsGroup from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                .setParameter("queId", queId)
                .list();
        List<Integer> assignedStepsList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                        "(select questionnaireStepId from GroupMappingBo where grpId in " +
                        "(select destinationTrueAsGroup from QuestionnairesStepsBo where stepOrGroup = 'group' and questionnairesId = :queId)" +
                        "or grpId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'group' and questionnaireId = :queId and id <> :grpId)) " +
                        "or stepId in (select destinationTrueAsGroup from GroupsBo where stepOrGroup = 'step' and questionnaireId = :queId and id <> :grpId)")
                .setParameter("queId", queId)
                .setParameter("grpId", groupId)
                .list();
        if (groupId != 0) {
          assignedSteps = session.createQuery("select questionnaireStepId from GroupMappingBo where grpId=:grpId")
                  .setParameter("grpId", groupId)
                  .list();
        }
        destList.addAll(assignedStepsList);
        destList.addAll(assignedSteps);
        destList.removeAll(Collections.singleton(null));

        if (!destList.isEmpty()) {
          query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and stepId not in (:destList)")
                  .setParameter("destList", destList)
                  .setParameter("queId", queId);
        } else {
          query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                  .setParameter("queId", queId);
        }
        list = query.list();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getStepsForGroups() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getStepsForGroups() - Ends");
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnairesStepsBo> getPostLoadDestinationKeys(Integer queId, int seq, Integer currStepId) {
    logger.info("StudyQuestionnaireDAOImpl - getPostLoadDestinationKeys() - Starts");
    Session session = null;
    List<QuestionnairesStepsBo> list = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (queId != null) {
        org.hibernate.query.Query<QuestionnairesStepsBo> query;
          List<Integer> destList;
          if (currStepId != null) {
            destList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                            "(select questionnaireStepId from GroupMappingBo where grpId in " +
                            "(select destinationStep from QuestionnairesStepsBo where stepOrGroupPostLoad = 'group' and questionnairesId = :queId and stepId <> :stepId))")
                    .setParameter("queId", queId)
                    .setParameter("stepId", currStepId)
                    .list();
          } else {
            destList = session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                            "(select questionnaireStepId from GroupMappingBo where grpId in " +
                            "(select destinationStep from QuestionnairesStepsBo where stepOrGroupPostLoad = 'group' and questionnairesId = :queId))")
                    .setParameter("queId", queId)
                    .list();
          }
          destList.removeAll(Collections.singleton(null));
          if (seq != -1) {
            if (!destList.isEmpty()) {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and sequenceNo > :seq and stepId not in (:destList)")
                      .setParameter("queId", queId)
                      .setParameter("destList", destList)
                      .setParameter("seq", seq);
            } else {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and sequenceNo > :seq")
                      .setParameter("queId", queId)
                      .setParameter("seq", seq);
            }
          } else {
            if (!destList.isEmpty()) {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId and stepId not in (:destList)")
                      .setParameter("destList", destList)
                      .setParameter("queId", queId);
            } else {
              query = session.createQuery("from QuestionnairesStepsBo where active = true and questionnairesId=:queId")
                      .setParameter("queId", queId);
            }
          }
          list = query.list();
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getPostLoadDestinationKeys() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getPostLoadDestinationKeys() - Ends");
    return list;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<QuestionnaireBo> getQuestionnairesForPiping(String queId, String studyId, boolean isLive) {
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnairesForPiping() - Starts");
    Session session = null;
    List<QuestionnaireBo> list = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNoneBlank(queId, studyId)) {
        if (isLive) {
          list = session.createQuery("from QuestionnaireBo where id <> :id and customStudyId = :studyId and live=:live and active = :active order by createdDate")
                  .setParameter("id", Integer.parseInt(queId))
                  .setParameter("studyId", studyId)
                  .setParameter("live", 1)
                  .setParameter("active", true)
                  .list();
        } else {
          list = session.createQuery("from QuestionnaireBo where id <> :id and studyId = :studyId and active = :active order by createdDate")
                  .setParameter("id", Integer.parseInt(queId))
                  .setParameter("studyId", Integer.parseInt(studyId))
                  .setParameter("active", true)
                  .list();
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getQuestionnairesForPiping() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - getQuestionnairesForPiping() - Ends");
    return list;
  }

  @Override
  public void saveOrUpdatePipingData(QuestionnaireStepBean bean) {
    logger.info("StudyDAOImpl - saveOrUpdatePipingData() - Starts");
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();
      QuestionnairesStepsBo questionnairesStepsBo = session.get(QuestionnairesStepsBo.class, bean.getStepId());
      boolean isMlStudy = false;
      List<String> languages = null;
      if (questionnairesStepsBo != null) {
        QuestionnaireBo questionnaireBo = session.get(QuestionnaireBo.class, questionnairesStepsBo.getQuestionnairesId());
        if (questionnaireBo != null) {
          StudyBo studyBo = session.get(StudyBo.class, questionnaireBo.getStudyId());
          if (studyBo != null && Boolean.TRUE.equals(studyBo.getMultiLanguageFlag())) {
            isMlStudy = true;
            if (StringUtils.isNotBlank(studyBo.getSelectedLanguages())) {
              languages = Arrays.asList(studyBo.getSelectedLanguages().split(","));
            }
          }
        }

        String stepType = questionnairesStepsBo.getStepType();
        String language = bean.getLanguage();
        if (StringUtils.isNotBlank(language) && !"en".equals(language)) {
          if ("Question".equals(stepType)) {
            QuestionLangBO questionLangBO = session.get(QuestionLangBO.class, new QuestionLangPK(questionnairesStepsBo.getInstructionFormId(), language));
            if (questionLangBO != null) {
              questionLangBO.setPipingSnippet(bean.getPipingSnippet());
              session.saveOrUpdate(questionLangBO);
            }
          } else if ("Instruction".equals(stepType)) {
            InstructionsLangBO instructionsLangBO = session.get(InstructionsLangBO.class, new InstructionLangPK(questionnairesStepsBo.getInstructionFormId(), language));
            if (instructionsLangBO != null) {
              instructionsLangBO.setPipingSnippet(bean.getPipingSnippet());
              session.saveOrUpdate(instructionsLangBO);
            }
          }
        } else {
          questionnairesStepsBo.setIsPiping(true);
          questionnairesStepsBo.setPipingSnippet(bean.getPipingSnippet());
          questionnairesStepsBo.setDifferentSurvey(bean.getDifferentSurvey());
          questionnairesStepsBo.setPipingSurveyId(bean.getPipingSurveyId());
          questionnairesStepsBo.setPipingSourceQuestionKey(bean.getPipingSourceQuestionKey());
          session.saveOrUpdate(questionnairesStepsBo);

          if (isMlStudy && languages != null) {
            for (String lang : languages) {
              QuestionnaireLangBO questionnaireLangBO = session.get(QuestionnaireLangBO.class, new QuestionnaireLangPK(questionnairesStepsBo.getQuestionnairesId(), lang));
              if (questionnaireLangBO != null) {
                questionnaireLangBO.setStatus(false);
              }
              if ("Question".equals(stepType)) {
                QuestionLangBO questionLangBO = session.get(QuestionLangBO.class, new QuestionLangPK(questionnairesStepsBo.getInstructionFormId(), lang));
                if (questionLangBO != null) {
                  questionLangBO.setStatus(false);
                  session.saveOrUpdate(questionLangBO);
                }
              } else if ("Instruction".equals(stepType)) {
                InstructionsLangBO instructionsLangBO = session.get(InstructionsLangBO.class, new InstructionLangPK(questionnairesStepsBo.getInstructionFormId(), lang));
                if (instructionsLangBO != null) {
                  instructionsLangBO.setStatus(false);
                  session.saveOrUpdate(instructionsLangBO);
                }
              }
              StudySequenceLangBO studySequenceLangBO = session.get(StudySequenceLangBO.class, new StudySequenceLangPK(questionnaireBo.getStudyId(), lang));
              if (studySequenceLangBO != null) {
                studySequenceLangBO.setStudyExcQuestionnaries(false);
                session.saveOrUpdate(studySequenceLangBO);
              }
            }
          }
        }
      }
      transaction.commit();
      } catch (Exception e) {
        transaction.rollback();
        logger.error("StudyDAOImpl - saveOrUpdatePipingData() - ERROR ", e);
      } finally {
        if (null != session && session.isOpen()) {
          session.close();
        }
      }
      logger.info("StudyDAOImpl - saveOrUpdatePipingData() - Ends");
  }

  @Override
  public List<GroupMappingBo> getStepId(String id, String questionnaireId) {
    logger.info("StudyDAOImpl - getStepId() - Starts");
    List<GroupMappingBo> stepId = new ArrayList<>();
    Session session = null;
    Query query;
    String queryString;
    try{
    	if (StringUtils.isNotBlank(id) && (null != id)) {
    	      session = hibernateTemplate.getSessionFactory().openSession();
    	      queryString = "FROM  GroupMappingBo GBO WHERE GBO.grpId =:id";
    	      query = session.createQuery(queryString).setParameter("id", Integer.parseInt(id));  
    	      stepId = query.list();
    	}
    }
    catch(Exception e){
      logger.error("StudyDAOImpl - getStepId() - ERROR",e);
    }finally{
      if(session != null){
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getStepId() - Ends");
    return stepId;
  }

  @Override
  public String groupFlagDisable(String id,List<GroupMappingBo> groupMappingBo, String questionnaireId) {
    logger.info("StudyDAOImpl - groupFlagDisable() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Query query;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      if(!groupMappingBo.isEmpty()) {
      for (GroupMappingBo el : groupMappingBo) {
        String stepId = el.getStepId();
        query = session
                .createQuery(
                        "update QuestionnairesStepsBo Q set Q.groupFlag=0"
                                + " where Q.instructionFormId=:stepId and Q.questionnairesId=:questionnaireId");
        query.setParameter("stepId", Integer.parseInt(stepId));
        query.setParameter("questionnaireId", Integer.parseInt(questionnaireId));
        query.executeUpdate();
      }
    }
    	query = session
                .createQuery(
                        "update QuestionnairesStepsBo Q set Q.destinationTrueAsGroup=null"
                                + " where Q.destinationTrueAsGroup=:id and Q.stepOrGroup='group'");
        query.setParameter("id", Integer.parseInt(id));
        query.executeUpdate();
        
        query = session
                .createQuery(
                        "update QuestionnairesStepsBo Q set Q.destinationStep=null,Q.stepOrGroupPostLoad=null"
                                + " where Q.destinationStep=:id and Q.stepOrGroupPostLoad='group'");
        query.setParameter("id", Integer.parseInt(id));
        query.executeUpdate();
    
      message = FdahpStudyDesignerConstants.SUCCESS;
      if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
        transaction.commit();
      }
      // transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyDAOImpl - groupFlagDisable() - ERROR", e);
    }
    logger.info("StudyDAOImpl - groupFlagDisable() - Ends");
    return message;
  }


  @Override
   public List<GroupMappingStepBean> getGroupsList(Integer grpId) {
    Session session = null;
    List<GroupMappingStepBean> groupMappingBeanss = new ArrayList<GroupMappingStepBean>();
    try {
    	session = hibernateTemplate.getSessionFactory().openSession();   
    	transaction = session.beginTransaction();
    	List<QuestionnairesStepsBo> questionnairesStepsBos = (List<QuestionnairesStepsBo>) session
                .createQuery("from QuestionnairesStepsBo where stepId IN (select questionnaireStepId from GroupMappingBo where grpId=:grpId) order by sequenceNo asc")
                .setParameter("grpId", grpId)
    			.getResultList();
    	for (QuestionnairesStepsBo stepsBo : questionnairesStepsBos) {
    		GroupMappingStepBean groupMappingStepBean1 = new GroupMappingStepBean();
    		List<String> question = new ArrayList<>();
    	  if (FdahpStudyDesignerConstants.QUESTION_STEP.equals(stepsBo.getStepType())) {
    	    groupMappingStepBean1.setStepId(stepsBo.getInstructionFormId());
    	    question.add((String) session.createQuery("select question from QuestionsBo where id=:stepId")
    	    		.setParameter("stepId",stepsBo.getInstructionFormId())
    	    		.uniqueResult());
    	    groupMappingStepBean1.setDescription(question);
    	    groupMappingStepBean1.setStepType(FdahpStudyDesignerConstants.QUESTION_STEP);
    	    groupMappingBeanss.add(groupMappingStepBean1);
    	  } else if (FdahpStudyDesignerConstants.INSTRUCTION_STEP.equals(stepsBo.getStepType())) {
    	    groupMappingStepBean1.setStepId(stepsBo.getInstructionFormId());
    	    question.add((String) session.createQuery("select instructionTitle From InstructionsBo  WHERE id=: stepId")
    	    		.setParameter("stepId",stepsBo.getInstructionFormId())
    	    		.uniqueResult());
    	    groupMappingStepBean1.setDescription(question);
    	    groupMappingStepBean1.setStepType(FdahpStudyDesignerConstants.INSTRUCTION_STEP);
    	    groupMappingBeanss.add(groupMappingStepBean1);
    	  } else {
    	     
    	      List<String> result =
    	              session.createSQLQuery("select q.question from questions q, form_mapping f where q.id=f.question_id and q.active=1 and f.form_id IN ("
    	                      + " :formIdList ) and f.active=1 order by f.form_id").setParameter("formIdList", stepsBo.getInstructionFormId()).list();
    	      for (int j = 0; j < result.size(); j++) {
    	        question.add(result.get(j));
    	      }
    	    groupMappingStepBean1.setStepId(stepsBo.getInstructionFormId());
    	    groupMappingStepBean1.setDescription(question);
    	    groupMappingStepBean1.setStepType(FdahpStudyDesignerConstants.FORM_STEP);
    	    groupMappingBeanss.add(groupMappingStepBean1);
    	  }
    	}
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupsList() - ERROR ", e);
    } finally {
      if (session != null && session.isOpen()) {
        session.close();
      }
    }
    return groupMappingBeanss;
  }

  @Override
  public String deleteGroupMaprecords(String id) {
    logger.info("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int count = 0;
    String queryString = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (StringUtils.isNotEmpty(id)) {
        //delete the qroup based on id
        queryString  =
                "delete GroupMappingBo GBO where GBO.grpId=:id ";
        query = session.createQuery(queryString);
        count = query.setString("id", id).executeUpdate();
        if (count > 0) message = FdahpStudyDesignerConstants.SUCCESS;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - Ends");
    return message;
  }

@Override
public GroupMappingBo getStepDetails(String id, String questionnaireId) {
	logger.info("StudyQuestionnaireDAOImpl - getStepDetails() - Starts");
    GroupMappingBo stepDetails = null;
  Query query = null;
  String queryString = "";
  try (Session session = hibernateTemplate.getSessionFactory().openSession()) {

    queryString = "FROM  GroupMappingBo GBO WHERE GBO.stepId = " + id;
    query = session.createQuery(queryString);
    stepDetails = (GroupMappingBo) query.uniqueResult();
  } catch (Exception e) {
    logger.error("StudyQuestionnaireDAOImpl - getStepDetails() - ERROR", e);
  }
    logger.info("StudyQuestionnaireDAOImpl - getStepDetails() - Ends");
    return stepDetails;
}

@Override
public String stepFlagDisable(GroupMappingBo groupMappingBo, String questionnaireId) {
	logger.info("StudyQuestionnaireDAOImpl - groupFlagDisable() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
  Query query = null;
  try  {
    Session session = hibernateTemplate.getSessionFactory().openSession();
    transaction = session.beginTransaction();
    int id = groupMappingBo.getGrpId();
    Integer stepId = Integer.valueOf(groupMappingBo.getStepId());
    Integer seqNum = (Integer) session.createQuery("select sequenceNo from QuestionnairesStepsBo where instructionFormId=: stepId").setParameter("stepId",stepId)
            .setMaxResults(1).uniqueResult();
    List<Integer> stepList = session.createQuery("select questionnaireStepId from GroupMappingBo where grpId=:id").setParameter("id",id).getResultList();
    String updateQuery = "update QuestionnairesStepsBo set groupFlag=false where questionnairesId=:questionnaireId and sequenceNo>=(:seq) and stepId in (:stepList)";
    query = session.createQuery(updateQuery)
            .setString("questionnaireId", questionnaireId)
            .setParameter("seq",seqNum)
            .setParameterList("stepList",stepList);
    query.executeUpdate();
    message = FdahpStudyDesignerConstants.SUCCESS;
    if (transaction.getStatus().equals(TransactionStatus.ACTIVE)) {
      transaction.commit();
    }
    // transaction.commit();
  } catch (Exception e) {
    transaction.rollback();
    logger.error("StudyQuestionnaireDAOImpl - groupFlagDisable() - ERROR", e);
  }
    logger.info("StudyQuestionnaireDAOImpl - groupFlagDisable() - Ends");
    return message;
}

@Override
public String deleteStepMaprecords(String id,String questionnaireId) {
  logger.info("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - Starts");
  Session session = null;
  String message = FdahpStudyDesignerConstants.FAILURE;
  int count = 0;
  String queryString;
  try {
    session = hibernateTemplate.getSessionFactory().openSession();
    transaction = session.beginTransaction();

    if (StringUtils.isNotEmpty(id)) {
      //delete the qroup based on id
      queryString =
              "delete GroupMappingBo GBO where GBO.stepId in "
      +"(select instructionFormId from QuestionnairesStepsBo where questionnairesId=:questionnaireId and sequenceNo>="
      +"(select sequenceNo from QuestionnairesStepsBo where instructionFormId=:id))";
      query = session.createQuery(queryString);
      count = query.setString("id", id).setString("questionnaireId", questionnaireId).executeUpdate();
      if (count > 0) message = FdahpStudyDesignerConstants.SUCCESS;
    }
    transaction.commit();
  } catch (Exception e) {
    transaction.rollback();
    logger.error("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - ERROR ", e);
  } finally {
    if (null != session && session.isOpen()) {
      session.close();
    }
  }
  logger.info("StudyQuestionnaireDAOImpl - deleteGroupMaprecords() - Ends");
  return message;
}

  @Override
  public Boolean isPreloadLogicAndPipingEnabled(Integer queId) {
    logger.info("StudyQuestionnaireDAOImpl - isPreloadLogicAndPipingEnabled() - Starts");
    Session session = null;
    boolean allowReorder = true;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (queId != null) {
        BigInteger count = (BigInteger) session
                .createSQLQuery("select count(*) from questionnaires_steps where questionnaires_id=:queId " +
                        "and active=:active and ((default_visibility=:dv and destination_true_as_group is not null and is_different_survey_pre_load is not true) " +
                        "or (is_piping = :isPiping and is_different_survey is not true) or (group_flag is true))")
                .setParameter("queId", queId)
                .setParameter("active", true)
                .setParameter("dv", false)
                .setParameter("isPiping", true)
                .uniqueResult();
        if (count.intValue() > 0) {
          allowReorder = false;
        }

        if (allowReorder) {
          BigInteger groupCount = (BigInteger) session
                  .createSQLQuery("select count(*) from grouppp where questionnaire_id=:queId "
                          + "and ((default_visibility=:dv and destination_true_as_group is not null))")
                  .setParameter("queId", queId)
                  .setParameter("dv", false)
                  .uniqueResult();
          if (groupCount.intValue() > 0) {
            allowReorder = false;
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isPreloadLogicAndPipingEnabled() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isPreloadLogicAndPipingEnabled() - Ends");
    return allowReorder;
  }

  @Override
  public String deleteStepBasedOnStepId(String stepId) {
    logger.info("StudyQuestionnaireDAOImpl - deleteStepBasedOnStepId() - Starts");
    Session session = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    int count = 0;
    String queryString = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      transaction = session.beginTransaction();

      if (StringUtils.isNotEmpty(stepId)) {
        //delete the qroup based on stepId
        queryString =
                "delete GroupMappingBo GBO where GBO.stepId=:stepId ";
        query = session.createQuery(queryString);
        count = query.setString("stepId", stepId).executeUpdate();
        if (count > 0) message = FdahpStudyDesignerConstants.SUCCESS;
      }
      transaction.commit();
    } catch (Exception e) {
      transaction.rollback();
      logger.error("StudyQuestionnaireDAOImpl - deleteStepBasedOnStepId() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - deleteStepBasedOnStepId() - Ends");
    return message;
  }

  @Override
  public boolean isQuestionMultiSelect(int queId) {
    logger.info("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - Starts");
    boolean isMultiSelect = false;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      QuestionReponseTypeBo responseTypeBo = (QuestionReponseTypeBo) session.createQuery("from QuestionReponseTypeBo where questionsResponseTypeId=:id")
              .setParameter("id", queId)
              .setMaxResults(1)
              .uniqueResult();
      if (responseTypeBo != null && StringUtils.isNotBlank(responseTypeBo.getSelectionStyle())
              && "Multiple".equals(responseTypeBo.getSelectionStyle())) {
        isMultiSelect = true;
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - Ends");
    return isMultiSelect;
  }

  @Override
  public List<PreLoadLogicBo> getPreLoadLogicDetails(Integer id) {
    logger.info("StudyDAOImpl - getPreLoadLogicDetails() - Starts");
    List<PreLoadLogicBo> preLoadLogicBo = null;
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " FROM PreLoadLogicBo PBO WHERE PBO.stepGroupId=:id and stepOrGroup=:group";
      query = session.createQuery(queryString).setParameter("id", id).setParameter("group", "group");
      preLoadLogicBo = (List<PreLoadLogicBo>) query.getResultList();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getPreLoadLogicDetails() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getPreLoadLogicDetails() - Ends");
    return preLoadLogicBo;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean isLastFormQuestion(String formId, String questionId) {
    logger.info("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - Starts");
    boolean isLastFormQuestion = false;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      if (StringUtils.isNotBlank(formId)) {
        List<Integer> questionsIds = session.createQuery("select id from QuestionsBo where active=true and " +
                "id in (select questionId from FormMappingBo where formId=:formId and active=true) order by id desc")
                .setParameter("formId", Integer.parseInt(formId))
                .list();
        if (questionsIds != null && !questionsIds.isEmpty() &&
                (Integer.parseInt(questionId) == questionsIds.get(0))) {
          isLastFormQuestion = true;
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isQuestionMultiSelect() - Ends");
    return isLastFormQuestion;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean isLastGroupQuestion(int stepId) {
    logger.info("StudyQuestionnaireDAOImpl - isLastGroupQuestion() - Starts");
    boolean isLastGroupQuestion = false;
    Session session = null;
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      Integer groupId = this.getGroupIdByStep(session, stepId);
      if (groupId != null) {
        GroupsBo groupsBo = session.get(GroupsBo.class, groupId);
        if (groupsBo != null
                && groupsBo.getDefaultVisibility() != null
                && !groupsBo.getDefaultVisibility()
                && groupsBo.getDestinationTrueAsGroup() != null) {
          int lastStep = (int) session.createQuery("select stepId from QuestionnairesStepsBo where stepId in " +
                          "(select questionnaireStepId from GroupMappingBo where grpId = :grpId) " +
                          "order by sequenceNo desc")
                  .setParameter("grpId", groupId)
                  .setMaxResults(1)
                  .uniqueResult();
          if (lastStep == stepId) {
            isLastGroupQuestion = true;
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - isLastGroupQuestion() - ERROR ", e);
    } finally {
      if (null != session && session.isOpen()) {
        session.close();
      }
    }
    logger.info("StudyQuestionnaireDAOImpl - isLastGroupQuestion() - Ends");
    return isLastGroupQuestion;
  }

  public Integer getGroupIdByStep(Session session, int stepId) {
    try {
      return (Integer) session.createQuery("select grpId from GroupMappingBo where questionnaireStepId=:stepId")
              .setParameter("stepId", stepId)
              .uniqueResult();
    } catch (Exception e) {
      logger.error("Exception : ", e);
    }
    return null;
  }

  @Override
  public Integer getGroupId(String stepId) {
    logger.info("StudyDAOImpl - getGroupId() - Starts");
 Session session = null;
    Integer groupId = 0;
    Query query;
 String queryString;
 try{
 session = hibernateTemplate.getSessionFactory().openSession();
 queryString = "select grpId FROM GroupMappingBo GBO WHERE GBO.stepId=:stepId";
 query = session.createQuery(queryString).setParameter("stepId", stepId);
   groupId = (Integer) query.uniqueResult();
 }
catch(Exception e){
 logger.error("StudyDAOImpl - getGroupId() - ERROR",e);
 }finally{
 if(session != null){
 session.close();
 }
 }
logger.info("StudyDAOImpl - getGroupId() - Ends");
 return groupId;

  }

  @Override
  public GroupsBo getGroupIdBySendingQuestionStepId(Integer questionStepId) {
    logger.info("StudyDAOImpl - getGroupIdBySendingquestionstepId() - Starts");
    Session session = null;
    Integer groupId = null;
    Query query;
    GroupsBo groupsBo = null;
    String queryString;
    try{
      session = hibernateTemplate.getSessionFactory().openSession();
      //queryString = "select grpId FROM GroupMappingBo GBO WHERE GBO.questionnaireStepId =:questionStepId";
      groupId = (Integer) session.createQuery("select grpId FROM GroupMappingBo GBO WHERE GBO.questionnaireStepId =:questionStepId")
              .setParameter("questionStepId", questionStepId)
              .setMaxResults(1)
              .uniqueResult();
      if (groupId != null) {
        groupsBo = (GroupsBo) session.createQuery("from GroupsBo where id=:id").setParameter("id", groupId).uniqueResult();
      }
    }
    catch(Exception e){
      logger.error("StudyDAOImpl - getGroupIdBySendingquestionstepId() - ERROR",e);
    }finally{
      if(session != null){
        session.close();
      }
    }
    logger.info("StudyDAOImpl - getGroupIdBySendingquestionstepId() - Ends");
    return groupsBo;
  }

  private void validatePreLoadLogicForGroups(Session session, int gpId) {
    List<QuestionnairesStepsBo> stepsBos = session.createQuery("from QuestionnairesStepsBo where active=true and destinationTrueAsGroup=:groupId and stepOrGroup <> 'step'")
            .setParameter("groupId", gpId)
            .list();
    for (QuestionnairesStepsBo stepsBo : stepsBos) {
      stepsBo.setStatus(false);
      session.update(stepsBo);
      QuestionnaireBo questionnaireBo = session.get(QuestionnaireBo.class, stepsBo.getQuestionnairesId());
      if (questionnaireBo != null) {
        questionnaireBo.setStatus(false);
        session.update(questionnaireBo);
      }
      String stepType = stepsBo.getStepType();
      int id = stepsBo.getInstructionFormId();
      if (FdahpStudyDesignerConstants.QUESTION_STEP.equals(stepType)) {
        QuestionsBo questionsBo = session.get(QuestionsBo.class, id);
        if (questionsBo != null) {
          questionsBo.setStatus(false);
          session.update(questionsBo);
        }
      } else if (FdahpStudyDesignerConstants.INSTRUCTION_STEP.equals(stepType)) {
        InstructionsBo instructionsBo = session.get(InstructionsBo.class, id);
        if (instructionsBo != null) {
          instructionsBo.setStatus(false);
          session.update(instructionsBo);
        }
      }
    }
  }

  @Override
  public String getStepType(Integer questionStepId, Integer stepId) {
    logger.info("StudyDAOImpl - getStepType() - Starts");
    String stepType = null;
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " select stepType from QuestionnairesStepsBo QSB where QSB.instructionFormId =:stepId and QSB.stepId =:questionStepId";
      query = session.createQuery(queryString).setParameter("questionStepId", questionStepId).setParameter("stepId", stepId);
      stepType = (String) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getStepType() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getStepType() - Ends");
    return stepType;
  }

  @Override
  public Integer getResponseType(Integer questionStepId) {
    logger.info("StudyDAOImpl - getResponseType() - Starts");
    Integer responseType = null;
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " select responseType from QuestionsBo QS where QS.id = (select instructionFormId from QuestionnairesStepsBo QSB where QSB.stepId =:questionStepId)";
      query = session.createQuery(queryString).setParameter("questionStepId", questionStepId);
      responseType = (Integer) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResponseType() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getResponseType() - Ends");
    return responseType;

  }

  @Override
  public List<Integer> getQuestionIdList(Integer questionStepId) {
    logger.info("StudyDAOImpl - getResponseType() - Starts");
    List<Integer> questionIdList = new ArrayList<>();
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " select questionId from FormMappingBo FMB where FMB.formId = (select instructionFormId from QuestionnairesStepsBo QSB where QSB.stepId =:questionStepId) order by FMB.sequenceNo desc";
      query = session.createQuery(queryString).setParameter("questionStepId", questionStepId);
      questionIdList = query.getResultList();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResponseType() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getResponseType() - Ends");
    return questionIdList;

  }

  @Override
  public Integer getResponseTypeForFormStep(Integer lastQuestinObjectValue) {
    logger.info("StudyDAOImpl - getResponseTypeForFormStep() - Starts");
    Integer responseType = null;
    Query query;
    String queryString;
    try (Session session = hibernateTemplate.getSessionFactory().openSession()) {
      queryString = " select responseType from QuestionsBo QS where QS.id =:lastQuestinObjectValue";
      query = session.createQuery(queryString).setParameter("lastQuestinObjectValue", lastQuestinObjectValue);
      responseType = (Integer) query.uniqueResult();
    } catch (Exception e) {
      logger.error("StudyDAOImpl - getResponseTypeForFormStep() - ERROR", e);
    }
    logger.info("StudyDAOImpl - getResponseTypeForFormStep() - Ends");
    return responseType;

  }

  @Override
  public List<GroupsBo> getGroupListBySendingQuestionStepId(String studyId, String questionnaireId, Integer questionStepId) {
    logger.info("begin getGroupsByStudyId()");
    Session session = null;
    List<GroupsBo> groups = null;
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery =
              "from GroupsBo WHERE studyId =:studyId and questionnaireId=:questionnaireId and " +
                      "id not in (select grpId from GroupMappingBo where questionnaireStepId=:questionStepId) order by id desc";
      query = session.createQuery(searchQuery)
              .setParameter("studyId", Integer.parseInt(studyId))
              .setParameter("questionStepId", questionStepId)
              .setParameter("questionnaireId", Integer.parseInt(questionnaireId));
      groups = query.list();

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getGroupListBySendingQuestionStepId() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getGroupListBySendingQuestionStepId() - Ends");
    return groups;
  }

  @Override
  public List<FormMappingBo> getListOfQuestions(Integer formId) {
    logger.info("begin getListOfQuestions()");
    Session session = null;
    List<FormMappingBo> questionList = new ArrayList<>();
    String searchQuery = "";
    try {
      session = hibernateTemplate.getSessionFactory().openSession();
      searchQuery =
              "from FormMappingBo WHERE formId =:formId";
      query = session.createQuery(searchQuery)
              .setParameter("formId", formId);
      questionList = query.list();

    } catch (Exception e) {
      logger.error("StudyQuestionnaireDAOImpl - getListOfQuestions() - ERROR ", e);
    } finally {
      if (session != null) {
        session.close();
      }
    }
    logger.info("getListOfQuestions() - Ends");
    return questionList;
  }

  @Override
  public String getTextChoiceSelectionStyle(Integer questionStepId) {
     logger.info("begin getTextChoiceSelectionStyle()");
      Session session = null;
      String selectionStyle = null;
      String searchQuery = "";
        try {
             session = hibernateTemplate.getSessionFactory().openSession();
               searchQuery =
                       "select selectionStyle from QuestionReponseTypeBo WHERE questionsResponseTypeId =:questionStepId";
                       query = session.createQuery(searchQuery)
                       .setParameter("questionStepId", questionStepId);
                        selectionStyle = (String) query.uniqueResult();

            } catch (Exception e) {
             logger.error("StudyQuestionnaireDAOImpl - getTextChoiceSelectionStyle() - ERROR ", e);
              } finally {
              if (session != null) {
               session.close();
               }
              }
               logger.info("getTextChoiceSelectionStyle() - Ends");
         return selectionStyle;
  }

}
