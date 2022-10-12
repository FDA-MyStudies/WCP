package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bean.FormulaInfoBean;
import com.fdahpstudydesigner.bean.GroupMappingStepBean;
import com.fdahpstudydesigner.bean.GroupsBean;
import com.fdahpstudydesigner.bean.PreLoadLogicBean;
import com.fdahpstudydesigner.bean.QuestionnaireStepBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.dao.AuditLogDAO;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.dao.StudyQuestionnaireDAO;
import com.fdahpstudydesigner.util.EmailNotification;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/** @author BTC */
@Service
public class StudyQuestionnaireServiceImpl implements StudyQuestionnaireService {
  private static Logger logger = LogManager.getLogger(StudyQuestionnaireServiceImpl.class);

  @Autowired private AuditLogDAO auditLogDAO;

  @Autowired private StudyQuestionnaireDAO studyQuestionnaireDAO;

  @Autowired private StudyDAO studyDAO;

  @Autowired private StudyService studyService;

  /**
   * From step have a one or more question.Each question have the short title field this will be
   * created the as column in response server so its should be unique across all the
   * steps.Validateing the Unique of question short title inside form step
   *
   * @author BTC
   * @param Integer , questionnaireId {@link QuestionnairesStepsBo}
   * @param String , shortTitle {@link QuestionsBo}
   * @param String , questionnaireShortTitle {@link QuestionnaireBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String Success/Failure
   */
  @Override
  public String checkFromQuestionShortTitle(
      Integer questionnaireId,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - checkFromQuestionShortTitle - Starts");
    return studyQuestionnaireDAO.checkFromQuestionShortTitle(
        questionnaireId, shortTitle, questionnaireShortTitle, customStudyId);
  }

  /*------------------------------------Added By Vivek End---------------------------------------------------*/

  /**
   * This method is used to validate the questionnaire have response type text scale while changing
   * the platform in study settings page
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String SUCCESS or FAILURE
   */
  @Override
  public String checkQuestionnaireResponseTypeValidation(Integer studyId, String customStudyId) {
    logger.info(
        "StudyQuestionnaireServiceImpl - checkQuestionnaireResponseTypeValidation - Starts");
    return studyQuestionnaireDAO.checkQuestionnaireResponseTypeValidation(studyId, customStudyId);
  }

  /**
   * Questionnaire contains the content,schedule as two tabs.Each questionnaire contains the short
   * title on the content tab this will be created as the column for the questionnaire response in
   * response server for this we are doing the unique title validation for each questionnaire in
   * study level
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param String , shortTitle {@link StudyBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String : SUCCESS or FAILURE
   */
  @Override
  public String checkQuestionnaireShortTitle(
      Integer studyId, String shortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - checkQuestionnaireShortTitle() - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.checkQuestionnaireShortTitle(studyId, shortTitle, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnaireStepList - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - checkQuestionnaireShortTitle() - Ends");
    return message;
  }

  /**
   * A questionnaire is an ordered set of one or more steps.Each step contains the step short title
   * field. Which will be response column for the step in response server.so it should be the
   * unique.Here validating the unique for step short title
   *
   * @author BTC
   * @param Integer , QuestionnaireId {@link QuestionnairesStepsBo}
   * @param String , stepType {@link QuestionnairesStepsBo}
   * @param String , shortTitle {@link QuestionnairesStepsBo}
   * @param String , questionnaireShortTitle {@link QuestionnaireBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String, Success or Failure
   */
  @Override
  public String checkQuestionnaireStepShortTitle(
      Integer questionnaireId,
      String stepType,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - checkQuestionnaireStepShortTitle - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.checkQuestionnaireStepShortTitle(
              questionnaireId, stepType, shortTitle, questionnaireShortTitle, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - checkQuestionnaireStepShortTitle - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - checkQuestionnaireStepShortTitle - Ends");
    return message;
  }

  /**
   * The admin can choose to add a response data element to the study dashboard in the form of line
   * charts or statistics.Adding a statistic to the dashboard needs the admin to specify the short
   * name should be unique across all the state in the study So validating the unique validation for
   * short name in states.
   *
   * @author BTC
   * @param Integer , StudyId {@link StudyBo}
   * @param String , shortTitle {@link QuestionsBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String , SUCCESS or FAILUE
   */
  @Override
  public String checkStatShortTitle(Integer studyId, String shortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - checkStatShortTitle - Starts");
    return studyQuestionnaireDAO.checkStatShortTitle(studyId, shortTitle, customStudyId);
  }

  /**
   * Admin want copy the already existed question into the same study admin has to click the copy
   * icon in the questionnaire list.It will copy the existed questionnaire into the study with out
   * questionnaire short title because the short title will be unique across the study
   *
   * @author BTC
   * @param Integer , questionnaireId
   * @param String , customStudyId {@link StudyBo}
   * @param sessionObject , {@link SessionObject}
   * @return {@link QuestionnaireBo}
   */
  @Override
  public QuestionnaireBo copyStudyQuestionnaireBo(
      Integer questionnaireId, String customStudyId, SessionObject sessionObject) {
    logger.info("StudyQuestionnaireServiceImpl - copyStudyQuestionnaireBo - Starts");
    return studyQuestionnaireDAO.copyStudyQuestionnaireBo(
        questionnaireId, customStudyId, sessionObject);
  }

  /**
   * This method is used to delete the question inside the form step of an questionnaire
   *
   * @author BTC
   * @param Integer , formId {@link FormBo}
   * @param Integer , questionId {@link QuestionsBo}
   * @param sessionObject , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return String SUCESS or FAILURE
   */
  @Override
  public String deleteFromStepQuestion(
      Integer formId, Integer questionId, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - deleteFromStepQuestion - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.deleteFromStepQuestion(
              formId, questionId, sessionObject, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - deleteFromStepQuestion - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - deleteFromStepQuestion - Ends");
    return message;
  }

  /**
   * Delete of an questionnaire step(Instruction,Question,Form) which are listed in questionnaire.
   *
   * @author BTC
   * @param Integer , stepId {@link QuestionnairesStepsBo}
   * @param Integer , questionnaireId {@link QuestionnairesStepsBo}
   * @param String ,stepType {@link QuestionnairesStepsBo}
   * @param sessionObject , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return String SUCCESS or FAILURE
   */
  @Override
  public String deleteQuestionnaireStep(
      Integer stepId,
      Integer questionnaireId,
      String stepType,
      SessionObject sessionObject,
      String customStudyId,
      Integer deletionId) {
    logger.info("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.deleteQuestionnaireStep(
              stepId, questionnaireId, stepType, sessionObject, customStudyId, deletionId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Ends");
    return message;
  }

  /**
   * Deleting of an Questionnaire in Study
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param Integer , questionnaireId {@link QuestionnaireBo}
   * @param Object , sessionObject {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return String : SUCCESS or FAILURE
   */
  @Override
  public String deletQuestionnaire(
      Integer studyId, Integer questionnaireId, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - deletQuestionnaire - Starts");
    return studyQuestionnaireDAO.deleteQuestuionnaireInfo(
        studyId, questionnaireId, sessionObject, customStudyId);
  }

  /**
   * For QA of response type that results in the data type 'double',the admin can also choose to
   * give the user a provision to allow the app to read the response from HealthKit this method is
   * used to get the pre-defined list of HealthKit quantity data types
   *
   * @author BTC
   * @return List of {@link HealthKitKeysInfo}
   */
  @Override
  public List<HealthKitKeysInfo> getHeanlthKitKeyInfoList() {
    logger.info("StudyQuestionnaireServiceImpl - getHeanlthKitKeyInfoList - Starts");
    return studyQuestionnaireDAO.getHeanlthKitKeyInfoList();
  }

  /**
   * Instruction step page in questionnaire.Lays down instructions for the user in mobile app. Which
   * contains the short title instruction title and text
   *
   * @author BTC
   * @param Integer , instructionId in {@link InstructionsBo}
   * @param String , questionnaireShortTitle in {@link QuestionnairesBo}
   * @param String , customStudyId in {@link StudyBo}
   * @param Integer , questionnaireId in {@link QuestionnairesStepsBo}
   * @return {@link InstructionsBo}
   */
  @Override
  public InstructionsBo getInstructionsBo(
      Integer instructionId,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - getInstructionsBo - Starts");
    InstructionsBo instructionsBo = null;
    try {
      instructionsBo =
          studyQuestionnaireDAO.getInstructionsBo(
              instructionId, questionnaireShortTitle, customStudyId, questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getInstructionsBo - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getInstructionsBo - Ends");
    return instructionsBo;
  }

  /**
   * Load the questionnaire of study with all the steps(instruction,question,form) with schedule
   * information. Each step corresponds to one screen on the mobile app.There can be multiple types
   * of QA in a questionnaire depending on the type of response format selected per QA.
   *
   * @author BTC
   * @param Integer , questionnaireId in {@link QuestionnaireBo}
   * @param String , customStudyId in {@link StudyBo}
   * @return {@link QuestionnaireBo}
   */
  @Override
  public QuestionnaireBo getQuestionnaireById(Integer questionnaireId, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireById - Starts");
    QuestionnaireBo questionnaireBo = null;
    try {
      questionnaireBo = studyQuestionnaireDAO.getQuestionnaireById(questionnaireId, customStudyId);
      if (null != questionnaireBo) {
        if (questionnaireBo.getStudyLifetimeStart() != null
            && !questionnaireBo.getStudyLifetimeStart().isEmpty()) {
          questionnaireBo.setStudyLifetimeStart(
              FdahpStudyDesignerUtil.getFormattedDate(
                  questionnaireBo.getStudyLifetimeStart(),
                  FdahpStudyDesignerConstants.DB_SDF_DATE,
                  FdahpStudyDesignerConstants.UI_SDF_DATE));
        }
        if (questionnaireBo.getStudyLifetimeEnd() != null
            && !questionnaireBo.getStudyLifetimeEnd().isEmpty()) {
          questionnaireBo.setStudyLifetimeEnd(
              FdahpStudyDesignerUtil.getFormattedDate(
                  questionnaireBo.getStudyLifetimeEnd(),
                  FdahpStudyDesignerConstants.DB_SDF_DATE,
                  FdahpStudyDesignerConstants.UI_SDF_DATE));
        }
        if (questionnaireBo.getQuestionnairesFrequenciesBo() != null
            && questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyDate() != null) {

          questionnaireBo
              .getQuestionnairesFrequenciesBo()
              .setFrequencyDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
        }
        if (questionnaireBo.getQuestionnairesFrequenciesBo() != null
            && StringUtils.isNotBlank(
                questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyTime())) {
          questionnaireBo
              .getQuestionnairesFrequenciesBo()
              .setFrequencyTime(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireBo.getQuestionnairesFrequenciesBo().getFrequencyTime(),
                      FdahpStudyDesignerConstants.UI_SDF_TIME,
                      FdahpStudyDesignerConstants.SDF_TIME));
        }
        if (questionnaireBo.getQuestionnairesFrequenciesList() != null
            && !questionnaireBo.getQuestionnairesFrequenciesList().isEmpty()) {
          for (QuestionnairesFrequenciesBo questionnairesFrequenciesBo :
              questionnaireBo.getQuestionnairesFrequenciesList()) {
            if (questionnairesFrequenciesBo.getFrequencyDate() != null) {
              questionnairesFrequenciesBo.setFrequencyDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnairesFrequenciesBo.getFrequencyDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
            if (StringUtils.isNotBlank(questionnairesFrequenciesBo.getFrequencyTime())) {
              questionnairesFrequenciesBo.setFrequencyTime(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnairesFrequenciesBo.getFrequencyTime(),
                      FdahpStudyDesignerConstants.UI_SDF_TIME,
                      FdahpStudyDesignerConstants.SDF_TIME));
            }
          }
        }
        if (questionnaireBo.getQuestionnaireCustomScheduleBo() != null
            && !questionnaireBo.getQuestionnaireCustomScheduleBo().isEmpty()) {
          for (QuestionnaireCustomScheduleBo questionnaireCustomScheduleBo :
              questionnaireBo.getQuestionnaireCustomScheduleBo()) {
            if (questionnaireCustomScheduleBo.getFrequencyStartDate() != null) {
              questionnaireCustomScheduleBo.setFrequencyStartDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireCustomScheduleBo.getFrequencyStartDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
            if (questionnaireCustomScheduleBo.getFrequencyEndDate() != null) {
              questionnaireCustomScheduleBo.setFrequencyEndDate(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireCustomScheduleBo.getFrequencyEndDate(),
                      FdahpStudyDesignerConstants.DB_SDF_DATE,
                      FdahpStudyDesignerConstants.UI_SDF_DATE));
            }
            if (StringUtils.isNotBlank(questionnaireCustomScheduleBo.getFrequencyTime())) {
              questionnaireCustomScheduleBo.setFrequencyTime(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireCustomScheduleBo.getFrequencyTime(),
                      FdahpStudyDesignerConstants.UI_SDF_TIME,
                      FdahpStudyDesignerConstants.SDF_TIME));
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnaireById - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireById - Ends");
    return questionnaireBo;
  }

  @Override
  public QuestionnaireLangBO getQuestionnaireLangById(int questionnaireId, String language) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireLangById - Starts");
    QuestionnaireLangBO questionnaireBo = null;
    try {
      questionnaireBo = studyQuestionnaireDAO.getQuestionnaireLangById(questionnaireId, language);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnaireLangById - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireLangById - Ends");
    return questionnaireBo;
  }

  /**
   * This method is used to get the forward question step of an questionnaire based on sequence
   * no.Thease questions are populated in the destination step drop down in the step level
   * attributes of question step,from step and instruction step to select the destination step if
   * branching is enabled for that questionnaire
   *
   * @author BTC
   * @param Integer , questionnaireId
   * @param Integer , sequenceNo
   * @return List {@link QuestionnairesStepsBo}
   */
  @Override
  public List<QuestionnairesStepsBo> getQuestionnairesStepsList(
      Integer questionnaireId, Integer sequenceNo) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnairesStepsList - Starts");
    List<QuestionnairesStepsBo> questionnairesStepsList = null;
    try {
      questionnairesStepsList =
          studyQuestionnaireDAO.getQuestionnairesStepsList(questionnaireId, sequenceNo);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnairesStepsList - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnairesStepsList - Starts");
    return questionnairesStepsList;
  }

  /**
   * Load the Question step page in questionnaire which contains the question and answer. Which
   * Carries one QA per screen in Mobile app
   *
   * @author BTC
   * @param Integer , stepId {@link QuestionnairesStepsBo}
   * @param String , stepType {@link QuestionnairesStepsBo}
   * @param String , questionnaireShortTitle {@link QuestionnaireBo}
   * @param String , customStudyId {@link StudyBo}
   * @param Integer , questionnaireId {@link QuestionnaireBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @Override
  public QuestionnairesStepsBo getQuestionnaireStep(
      Integer stepId,
      String stepType,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireStep - Starts");
    QuestionnairesStepsBo questionnairesStepsBo = null;
    try {
      questionnairesStepsBo =
          studyQuestionnaireDAO.getQuestionnaireStep(
              stepId, stepType, questionnaireShortTitle, customStudyId, questionnaireId);

      if (questionnairesStepsBo != null) {
        List<PreLoadLogicBo> preLoadLogicBos = studyQuestionnaireDAO.getPreLoadLogicByStep(questionnairesStepsBo.getStepId());
        List<PreLoadLogicBean> preLoadLogicBeans = new ArrayList<>();
        for (PreLoadLogicBo bo : preLoadLogicBos) {
          PreLoadLogicBean bean = new PreLoadLogicBean();
          BeanUtils.copyProperties(bo, bean);
          preLoadLogicBeans.add(bean);
        }
        questionnairesStepsBo.setPreLoadLogicBeans(preLoadLogicBeans);
        if (stepType.equalsIgnoreCase(FdahpStudyDesignerConstants.FORM_STEP)
                && questionnairesStepsBo.getFormQuestionMap() != null) {
          List<QuestionResponseTypeMasterInfoBo> questionResponseTypeMasterInfoList =
                  studyQuestionnaireDAO.getQuestionReponseTypeList();
          if (questionResponseTypeMasterInfoList != null
                  && !questionResponseTypeMasterInfoList.isEmpty()) {
            for (QuestionResponseTypeMasterInfoBo questionResponseTypeMasterInfoBo :
                    questionResponseTypeMasterInfoList) {
              for (Entry<Integer, QuestionnaireStepBean> entry :
                      questionnairesStepsBo.getFormQuestionMap().entrySet()) {
                QuestionnaireStepBean questionnaireStepBean = entry.getValue();
                if (questionnaireStepBean.getResponseType() != null
                        && questionnaireStepBean
                        .getResponseType()
                        .equals(questionResponseTypeMasterInfoBo.getId())) {
                  if (FdahpStudyDesignerConstants.DATE.equalsIgnoreCase(
                          questionResponseTypeMasterInfoBo.getResponseType())) {
                    questionnaireStepBean.setResponseTypeText(
                            questionResponseTypeMasterInfoBo.getResponseType());
                  } else {
                    questionnaireStepBean.setResponseTypeText(
                            questionResponseTypeMasterInfoBo.getDataType());
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnaireStep - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireStep - Ends");
    return questionnairesStepsBo;
  }

  /**
   * Load the questionnaires of study with all the steps(instruction,question,form) with schedule
   * information. Each step corresponds to one screen on the mobile app.There can be multiple types
   * of QA in a questionnaire depending on the type of response format selected per QA.
   *
   * @author BTC
   * @param Integer , questionnaireId {@link QuestionnaireBo}
   * @return Map : TreeMap<Integer, QuestionnaireStepBean>
   */
  @Override
  public SortedMap<Integer, QuestionnaireStepBean> getQuestionnaireStepList(
      Integer questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireStepList() - Starts");
    SortedMap<Integer, QuestionnaireStepBean> questionnaireStepMap = null;
    try {
      questionnaireStepMap = studyQuestionnaireDAO.getQuestionnaireStepList(questionnaireId);
      if (questionnaireStepMap != null) {
        List<QuestionResponseTypeMasterInfoBo> questionResponseTypeMasterInfoList =
            studyQuestionnaireDAO.getQuestionReponseTypeList();
        if (questionResponseTypeMasterInfoList != null
            && !questionResponseTypeMasterInfoList.isEmpty()) {
          for (QuestionResponseTypeMasterInfoBo questionResponseTypeMasterInfoBo :
              questionResponseTypeMasterInfoList) {
            for (Entry<Integer, QuestionnaireStepBean> entry : questionnaireStepMap.entrySet()) {
              QuestionnaireStepBean questionnaireStepBean = entry.getValue();
              if (questionResponseTypeMasterInfoBo
                  .getId()
                  .equals(questionnaireStepBean.getResponseType())) {
                if (FdahpStudyDesignerConstants.DATE.equalsIgnoreCase(
                    questionResponseTypeMasterInfoBo.getResponseType())) {
                  questionnaireStepBean.setResponseTypeText(
                      questionResponseTypeMasterInfoBo.getResponseType());
                } else {
                  questionnaireStepBean.setResponseTypeText(
                      questionResponseTypeMasterInfoBo.getDataType());
                }
              }
              if (entry.getValue().getFromMap() != null) {
                for (Entry<Integer, QuestionnaireStepBean> entryKey :
                    entry.getValue().getFromMap().entrySet()) {
                  if (questionResponseTypeMasterInfoBo
                      .getId()
                      .equals(entryKey.getValue().getResponseType())) {
                    if (FdahpStudyDesignerConstants.DATE.equalsIgnoreCase(
                        questionResponseTypeMasterInfoBo.getResponseType())) {
                      questionnaireStepBean.setResponseTypeText(
                          questionResponseTypeMasterInfoBo.getResponseType());
                    } else {
                      questionnaireStepBean.setResponseTypeText(
                          questionResponseTypeMasterInfoBo.getDataType());
                    }
                  }
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionnaireStepList - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionnaireStepList() - Ends");
    return questionnaireStepMap;
  }

  /**
   * This method is used to get the Response Type Master information which research kit and research
   * stack supports
   *
   * @author BTC
   * @return List : {@link QuestionResponseTypeMasterInfoBo}
   */
  @Override
  public List<QuestionResponseTypeMasterInfoBo> getQuestionReponseTypeList() {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionReponseTypeList - Starts");
    List<QuestionResponseTypeMasterInfoBo> questionResponseTypeMasterInfoList = null;
    try {
      questionResponseTypeMasterInfoList = studyQuestionnaireDAO.getQuestionReponseTypeList();
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionReponseTypeList - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionReponseTypeList - Ends");
    return questionResponseTypeMasterInfoList;
  }

  /**
   * Load the question of form step inside questionnaire.Question contains the question level
   * attributes and response level attributes
   *
   * @author BTC
   * @param Integer , questionId {@link QuestionsBo}
   * @param String , questionnaireShortTitle {@link QuestionnaireBo}
   * @param String , customStudyId {@link StudyBo}
   * @return {@link QuestionsBo}
   */
  @Override
  public QuestionsBo getQuestionsById(
      Integer questionId, String questionnaireShortTitle, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionsById - Starts");
    QuestionsBo questionsBo = null;
    try {
      questionsBo =
          studyQuestionnaireDAO.getQuestionsById(
              questionId, questionnaireShortTitle, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionsById - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionsById - Ends");
    return questionsBo;
  }

  /*------------------------------------Added By Vivek Start---------------------------------------------------*/
  /**
   * return List of all the Questionnaires of an study.A Study can have 0 or more Questionnaires and
   * admin can manage a list of questionnaires for the study Questionnaires based on user's Study Id
   *
   * @author BTC
   * @param studyId , studyId of the {@link StudyBo}
   * @return List of {@link QuestionnaireBo}
   */
  @Override
  public List<QuestionnaireBo> getStudyQuestionnairesByStudyId(String studyId, Boolean isLive) {
    logger.info("StudyQuestionnaireServiceImpl - getStudyQuestionnairesByStudyId() - Starts");
    List<QuestionnaireBo> questionnaires = null;
    try {
      questionnaires = studyQuestionnaireDAO.getStudyQuestionnairesByStudyId(studyId, isLive);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getStudyQuestionnairesByStudyId() - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getStudyQuestionnairesByStudyId() - Ends");
    return questionnaires;
  }

  /**
   * In Questionnaire for question step and question in form step for date response type we can
   * chose those question as anchor date. The anchor date question is unique across the study so
   * here we are validating for anchor date is checked or not for any other question while create or
   * updating the new question in a study
   *
   * @author BTC
   * @param Integer , studyId {@link StudyBo}
   * @param String , customStudyId {@link StudyBo}
   * @return Boolean true or false
   */
  @Override
  public Boolean isAnchorDateExistsForStudy(Integer studyId, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - isAnchorDateExistsForStudy - Starts");
    return studyQuestionnaireDAO.isAnchorDateExistsForStudy(studyId, customStudyId);
  }

  /**
   * Checking for questionnaire creation is completed or not
   *
   * @author BTC
   * @param Integer : studyId {@link StudyBo}
   * @return Boolean true r false
   */
  @Override
  public Boolean isQuestionnairesCompleted(Integer studyId) {
    logger.info("StudyQuestionnaireServiceImpl - isAnchorDateExistsForStudy - Starts");
    return studyQuestionnaireDAO.isQuestionnairesCompleted(studyId);
  }

  /**
   * From step contains the list of questions with default admin created master order.Admin can
   * manage these orders by reordering the question on drag and drop of a questions in the list
   *
   * @author BTC
   * @param Integer , fromId {@link FormBo}
   * @param int , oldOrderNumber
   * @param int ,: newOrderNumber
   * @return String : SUCCESS or FAILURE
   */
  @Override
  public String reOrderFormStepQuestions(Integer formId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyQuestionnaireServiceImpl - reOrderFormStepQuestions - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.reOrderFormStepQuestions(formId, oldOrderNumber, newOrderNumber);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - reOrderFormStepQuestions - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - reOrderFormStepQuestions - Starts");
    return message;
  }

  /**
   * A questionnaire is an ordered set of one or more steps (screens on the mobile app).The
   * questionnaire by default follows the master order of steps admin can manage the order of an
   * step.Here we can do the reordering of an questionnaire steps(Instruction,Question,Form) which
   * are listed on questionnaire content page.
   *
   * @author BTC
   * @param Integer , questionnaireId {@link QuestionnairesStepsBo}
   * @param int, oldOrderNumber
   * @param int, newOrderNumber
   * @return String SUCCESS or FAILURE
   */
  @Override
  public String reOrderQuestionnaireSteps(
      Integer questionnaireId, int oldOrderNumber, int newOrderNumber) {
    logger.info("StudyQuestionnaireServiceImpl - reOrderQuestionnaireSteps - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.reOrderQuestionnaireSteps(
              questionnaireId, oldOrderNumber, newOrderNumber);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - reOrderQuestionnaireSteps - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - reOrderQuestionnaireSteps - Starts");
    return message;
  }

  /**
   * Here admin will add the from step to the questionnaire which contains the two sets of
   * attributes. which are step level attribute,form level attribute.Admin has fill the required
   * fields and click on done it save the info here.
   *
   * @author BTC
   * @param Object , {@link QuestionnairesStepsBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @Override
  public QuestionnairesStepsBo saveOrUpdateFromStepQuestionnaire(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sesObj,
      String customStudyId,
      String studyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateFromStepQuestionnaire - Starts");
    QuestionnairesStepsBo addOrUpdateQuestionnairesStepsBo = null;
    try {
      addOrUpdateQuestionnairesStepsBo =
          studyQuestionnaireDAO.saveOrUpdateFromQuestionnaireStep(
              questionnairesStepsBo, sesObj, customStudyId, studyId);
      if (questionnairesStepsBo.getInstructionFormId() == null) {
        questionnairesStepsBo.setInstructionFormId(addOrUpdateQuestionnairesStepsBo.getInstructionFormId());
        questionnairesStepsBo.setStepId(addOrUpdateQuestionnairesStepsBo.getStepId());
      }
      this.doPreLoadLogic(questionnairesStepsBo, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveOrUpdateFromStepQuestionnaire - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateFromStepQuestionnaire - Starts");
    return addOrUpdateQuestionnairesStepsBo;
  }

  private void doPreLoadLogic(QuestionnairesStepsBo questionnairesStepsBo, String customStudyId) throws Exception{
    try {
      List<PreLoadLogicBean> preLoadLogicBeans = questionnairesStepsBo.getPreLoadLogicBeans();
      if (preLoadLogicBeans != null && !preLoadLogicBeans.isEmpty()) {
        List<Integer> preLoadIds = studyQuestionnaireDAO.getPreLoadIds(questionnairesStepsBo.getStepId());
        for (PreLoadLogicBean logicBean : preLoadLogicBeans) {
          PreLoadLogicBo preLoadLogicBo = studyQuestionnaireDAO.getPreLoadLogicById(logicBean.getId());
          if (StringUtils.isNotBlank(logicBean.getOperator()) && StringUtils.isNotBlank(logicBean.getInputValue())) {
            if (preLoadLogicBo == null) {
              preLoadLogicBo = new PreLoadLogicBo();
              preLoadLogicBo.setStepOrGroup(FdahpStudyDesignerConstants.STEP);
              preLoadLogicBo.setStepGroupId(questionnairesStepsBo.getStepId());
            } else {
              preLoadIds.remove(preLoadLogicBo.getId());
            }
            preLoadLogicBo.setOperator(StringUtils.isBlank(logicBean.getOperator())
                    ? "&&" : logicBean.getOperator());
            preLoadLogicBo.setInputValue(logicBean.getInputValue());
            preLoadLogicBo.setConditionOperator(logicBean.getConditionOperator());
            studyQuestionnaireDAO.saveOrUpdateObject(preLoadLogicBo);
          }
        }
        studyQuestionnaireDAO.deleteFormula(preLoadIds);
        QuestionnairesStepsBo persistentStepsBo =
                studyQuestionnaireDAO.getQuestionnaireStep(
                        questionnairesStepsBo.getInstructionFormId(), questionnairesStepsBo.getStepType(), null,
                        customStudyId, questionnairesStepsBo.getQuestionnairesId());
        if (persistentStepsBo != null) {
          persistentStepsBo.setDefaultVisibility("true".equals(questionnairesStepsBo.getGroupDefaultVisibility()));
          persistentStepsBo.setDestinationTrueAsGroup(questionnairesStepsBo.getDestinationTrueAsGroup());
          persistentStepsBo.setDifferentSurveyPreLoad(questionnairesStepsBo.getDifferentSurveyPreLoad());
          persistentStepsBo.setPreLoadSurveyId(questionnairesStepsBo.getPreLoadSurveyId());
          studyQuestionnaireDAO.saveOrUpdateObject(persistentStepsBo);
        }
      }
    } catch (Exception e) {
      logger.error("Exception : ", e);
      throw e;
    }
  }

  private void doGroupPreLoadLogic(GroupsBean groupsBo) throws Exception{
    try {
      List<PreLoadLogicBean> preLoadLogicBeans = groupsBo.getPreLoadLogicBeans();
      if (preLoadLogicBeans != null && !preLoadLogicBeans.isEmpty()) {
        List<Integer> preLoadIds = studyQuestionnaireDAO.getPreLoadIds(groupsBo.getId());
        for (PreLoadLogicBean logicBean : preLoadLogicBeans) {
          PreLoadLogicBo preLoadLogicBo = studyQuestionnaireDAO.getPreLoadLogicById(logicBean.getId());
          if (StringUtils.isNotBlank(logicBean.getOperator()) && StringUtils.isNotBlank(logicBean.getInputValue())) {
            if (preLoadLogicBo == null) {
              preLoadLogicBo = new PreLoadLogicBo();
              preLoadLogicBo.setStepOrGroup(FdahpStudyDesignerConstants.GROUP);
              preLoadLogicBo.setStepGroupId(groupsBo.getId());
            } else {
              preLoadIds.remove(preLoadLogicBo.getId());
            }
            preLoadLogicBo.setOperator(StringUtils.isBlank(logicBean.getOperator())
                    ? "&&" : logicBean.getOperator());
            preLoadLogicBo.setInputValue(logicBean.getInputValue());
            preLoadLogicBo.setConditionOperator(logicBean.getConditionOperator());
            studyQuestionnaireDAO.saveOrUpdateObject(preLoadLogicBo);
          }
        }
        studyQuestionnaireDAO.deleteFormula(preLoadIds);
        GroupsBo persistentStepsBo = null;
        if(groupsBo.getId()!=null){
          persistentStepsBo = studyQuestionnaireDAO.getGroupsDetails(groupsBo.getId());
        }
        if (persistentStepsBo != null) {
          persistentStepsBo.setDefaultVisibility("true".equals(groupsBo.getDefaultVisibility()));
          persistentStepsBo.setDestinationTrueAsGroup(groupsBo.getDestinationTrueAsGroup());
          studyQuestionnaireDAO.saveOrUpdateObject(persistentStepsBo);
        }
      }
    } catch (Exception e) {
      logger.error("Exception : ", e);
      throw e;
    }
  }

  /**
   * Create the instruction step in Questionnaire which lays the instruction to user in mobile
   * app.Admin would needs to fill the short title instruction title and instruction text.
   *
   * @author BTC
   * @param Object , {@link InstructionsBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId in {@link StudyBo}
   * @return Object , {@link InstructionsBo}
   */
  @Override
  public InstructionsBo saveOrUpdateInstructionsBo(
      InstructionsBo instructionsBo,
      SessionObject sessionObject,
      String customStudyId,
      String language,
      String studyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateInstructionsBo - Starts");
    InstructionsBo addOrUpdateInstructionsBo = null;
    InstructionsLangBO instructionsLangBO = null;
    try {
      if (null != instructionsBo) {
        if (instructionsBo.getId() != null) {
          addOrUpdateInstructionsBo =
              studyQuestionnaireDAO.getInstructionsBo(
                  instructionsBo.getId(), "", customStudyId, instructionsBo.getQuestionnaireId());
        } else {
          addOrUpdateInstructionsBo = new InstructionsBo();
          addOrUpdateInstructionsBo.setActive(true);
          this.updateStudyLangSequence(
              instructionsBo.getQuestionnaireId(), Integer.parseInt(studyId));
        }

        if (FdahpStudyDesignerUtil.isNotEmpty(language)
            && !"en".equals(language)
            && !"undefined".equals(language)) {
          if (instructionsBo.getId() != null) {
            instructionsLangBO =
                studyQuestionnaireDAO.getInstructionLangBo(instructionsBo.getId(), language);
          }
          if (instructionsLangBO == null) {
            instructionsLangBO = new InstructionsLangBO();
            instructionsLangBO.setInstructionLangPK(
                new InstructionLangPK(instructionsBo.getId(), language));
            instructionsLangBO.setCreatedBy(instructionsBo.getCreatedBy());
            instructionsLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          } else {
            instructionsLangBO.setModifiedBy(instructionsBo.getModifiedBy());
            instructionsLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          instructionsLangBO.setStatus(
              FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(instructionsBo.getType()));
          instructionsLangBO.setInstructionTitle(instructionsBo.getInstructionTitle());
          instructionsLangBO.setInstructionText(instructionsBo.getInstructionText());
          if (StringUtils.isNotBlank(studyId)) {
            StudySequenceLangBO studySequenceLangBO =
                studyService.getStudySequenceById(Integer.parseInt(studyId), language);
            if (studySequenceLangBO != null) {
              studySequenceLangBO.setStudyExcQuestionnaries(false);
              studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
          studyQuestionnaireDAO.saveOrUpdateObject(instructionsLangBO);
          studyDAO.updateDraftStatusInStudyBo(sessionObject.getUserId(), Integer.parseInt(studyId));
        } else {
          if (instructionsBo.getInstructionText() != null
              && !instructionsBo.getInstructionText().isEmpty()) {
            addOrUpdateInstructionsBo.setInstructionText(instructionsBo.getInstructionText());
          }
          if (instructionsBo.getInstructionTitle() != null
              && !instructionsBo.getInstructionTitle().isEmpty()) {
            addOrUpdateInstructionsBo.setInstructionTitle(instructionsBo.getInstructionTitle());
          }
          if (instructionsBo.getCreatedOn() != null && !instructionsBo.getCreatedOn().isEmpty()) {
            addOrUpdateInstructionsBo.setCreatedOn(instructionsBo.getCreatedOn());
          }
          if (instructionsBo.getCreatedBy() != null) {
            addOrUpdateInstructionsBo.setCreatedBy(instructionsBo.getCreatedBy());
          }
          if (instructionsBo.getModifiedOn() != null && !instructionsBo.getModifiedOn().isEmpty()) {
            addOrUpdateInstructionsBo.setModifiedOn(instructionsBo.getModifiedOn());
          }
          if (instructionsBo.getModifiedBy() != null) {
            addOrUpdateInstructionsBo.setModifiedBy(instructionsBo.getModifiedBy());
          }
          if (instructionsBo.getQuestionnaireId() != null) {
            addOrUpdateInstructionsBo.setQuestionnaireId(instructionsBo.getQuestionnaireId());
          }
          if (instructionsBo.getQuestionnairesStepsBo() != null) {
            addOrUpdateInstructionsBo.setQuestionnairesStepsBo(
                instructionsBo.getQuestionnairesStepsBo());
          }
          if (instructionsBo.getType() != null && !instructionsBo.getType().isEmpty()) {
            addOrUpdateInstructionsBo.setType(instructionsBo.getType());
            if (instructionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
              addOrUpdateInstructionsBo.setStatus(false);
            } else if (instructionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
              addOrUpdateInstructionsBo.setStatus(true);
            }
          }
          addOrUpdateInstructionsBo =
              studyQuestionnaireDAO.saveOrUpdateInstructionsBo(
                  addOrUpdateInstructionsBo, sessionObject, customStudyId);
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveOrUpdateInstructionsBo - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateInstructionsBo - Ends");
    return addOrUpdateInstructionsBo;
  }

  /**
   * Question of a form step contains the two attributes.Question-level attributes-these are the
   * same set of attributes as that for question step with the exception of the skippable property
   * and branching logic based on participant choice of response or the conditional logic based
   * branching Response-level attributes (same as that for Question Step).Here we can save or update
   * the form questions.
   *
   * @author BTC
   * @param Object , {@link QuestionsBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return Object , {@link QuestionsBo}
   */
  @Override
  public QuestionsBo saveOrUpdateQuestion(
      QuestionsBo questionsBo,
      SessionObject sesObj,
      String customStudyId,
      String language,
      String studyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestion - Starts");
    QuestionsBo addQuestionsBo = null;
    String activitydetails = "";
    String activity = "";
    QuestionLangBO questionLangBO = null;
    try {
      if (null != questionsBo) {

        if (questionsBo.getId() != null) {
          addQuestionsBo =
                  studyQuestionnaireDAO.getQuestionsById(questionsBo.getId(), null, customStudyId);
        } else {
          addQuestionsBo = new QuestionsBo();
          addQuestionsBo.setActive(true);
          this.updateStudyLangSequence(questionsBo.getQuestionnaireId(), Integer.parseInt(studyId));
        }

        Integer id = questionsBo.getId();
        if (id != null && (StringUtils.isEmpty(language) || "en".equals(language))) {
          questionLangBO = studyQuestionnaireDAO.getQuestionLangBo(id, "es");
          if (questionLangBO != null) {
            StringBuilder dispText = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            if (questionsBo.getQuestionResponseSubTypeList() != null
                    && !questionsBo.getQuestionResponseSubTypeList().isEmpty()) {
              List<QuestionResponseSubTypeBo> subTypeBoList = questionsBo.getQuestionResponseSubTypeList();
              int size = subTypeBoList.size();
              int i = 0;
              if (FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(questionsBo.getType())) {
                Collections.sort(subTypeBoList, new Comparator<QuestionResponseSubTypeBo>() {
                  public int compare(final QuestionResponseSubTypeBo object1, final QuestionResponseSubTypeBo object2) {
                    return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                  }
                });
              }
              for (QuestionResponseSubTypeBo subTypeBo : subTypeBoList) {
                // for spanish
                String dispLang = StringUtils.isNotBlank(subTypeBo.getDisplayTextLang())
                        ? subTypeBo.getDisplayTextLang()
                        : "";
                String descLang = StringUtils.isNotBlank(subTypeBo.getDescriptionLang())
                        ? subTypeBo.getDescriptionLang()
                        : "";
                if (i == size - 1) {
                  dispText.append(dispLang);
                  desc.append(descLang);
                } else {
                  dispText.append(dispLang).append("|");
                  desc.append(descLang).append("|");
                }
                i++;
              }
              questionLangBO.setDisplayText(dispText.toString());
              questionLangBO.setTextChoiceDescription(desc.toString());
              studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
            }
          }
        }

        if (FdahpStudyDesignerUtil.isNotEmpty(language)
            && !"en".equals(language)
            && !"undefined".equals(language)) {
          if (id != null) {
            questionLangBO = studyQuestionnaireDAO.getQuestionLangBo(id, language);
          }
          if (questionLangBO == null) {
            questionLangBO = new QuestionLangBO();
            questionLangBO.setQuestionLangPK(new QuestionLangPK(id, language));
            questionLangBO.setQuestionnaireId(questionsBo.getQuestionnaireId());
            questionLangBO.setCreatedBy(questionsBo.getCreatedBy());
            questionLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          } else {
            questionLangBO.setModifiedBy(questionLangBO.getCreatedBy());
            questionLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          questionLangBO.setFormId(questionsBo.getFromId());
          questionLangBO.setQuestion(questionsBo.getQuestion());
          questionLangBO.setDescription(questionsBo.getDescription());
          questionLangBO.setStatDisplayName(questionsBo.getStatDisplayName());
          questionLangBO.setStatDisplayUnits(questionsBo.getStatDisplayUnits());
          questionLangBO.setChartTitle(questionsBo.getChartTitle());
          questionLangBO.setResponseTypeId(questionsBo.getResponseType());

          QuestionReponseTypeBo questionReponseTypeBo = questionsBo.getQuestionReponseTypeBo();
          if (questionReponseTypeBo != null) {
            questionLangBO.setMaxDescription(questionReponseTypeBo.getMaxDescription());
            questionLangBO.setMinDescription(questionReponseTypeBo.getMinDescription());
            questionLangBO.setPlaceholderText(questionReponseTypeBo.getPlaceholder());
            questionLangBO.setUnit(questionReponseTypeBo.getUnit());
            questionLangBO.setInvalidMessage(questionReponseTypeBo.getInvalidMessage());
            questionLangBO.setExceptText(questionReponseTypeBo.getValidationExceptText());
            questionLangBO.setOtherText(questionReponseTypeBo.getOtherText());
          }

          StringBuilder displayText = new StringBuilder();
          StringBuilder description = new StringBuilder();
          List<QuestionResponseSubTypeBo> subTypeBoList = questionsBo.getQuestionResponseSubTypeList();
          if (subTypeBoList != null && !subTypeBoList.isEmpty()
                  && FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(questionsBo.getType())) {
            Collections.sort(subTypeBoList, new Comparator<QuestionResponseSubTypeBo>() {
              public int compare(final QuestionResponseSubTypeBo object1, final QuestionResponseSubTypeBo object2) {
                if (StringUtils.isNoneBlank(object1.getSequenceNumber(), object2.getSequenceNumber())) {
                  return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                }
                return 0;
              }
            });
          }
          if (subTypeBoList != null && !subTypeBoList.isEmpty()) {
            int size = questionsBo.getQuestionResponseSubTypeList().size();
            int i = 0;
            for (QuestionResponseSubTypeBo subTypeBo : subTypeBoList) {
              if (i == size - 1) {
                displayText.append(subTypeBo.getText());
                description.append(subTypeBo.getDescription());
              } else {
                displayText.append(subTypeBo.getText()).append("|");
                description.append(subTypeBo.getDescription()).append("|");
              }
              i++;
            }
          }
          questionLangBO.setStatus(
              FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(questionsBo.getType()));
          questionLangBO.setDisplayText(displayText.toString());
          questionLangBO.setTextChoiceDescription(description.toString());
          studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
          if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
            StudySequenceLangBO studySequenceLangBO =
                studyService.getStudySequenceById(Integer.parseInt(studyId), language);
            if (studySequenceLangBO != null) {
              studySequenceLangBO.setStudyExcQuestionnaries(false);
              studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
          studyDAO.updateDraftStatusInStudyBo(sesObj.getUserId(), Integer.parseInt(studyId));
        } else {
          if (questionsBo.getShortTitle() != null) {
            addQuestionsBo.setShortTitle(questionsBo.getShortTitle());
          }
          if (questionsBo.getQuestion() != null) {
            addQuestionsBo.setQuestion(questionsBo.getQuestion());
          }
          addQuestionsBo.setDescription(questionsBo.getDescription());
          if (questionsBo.getSkippable() != null) {
            addQuestionsBo.setSkippable(questionsBo.getSkippable());
          }
          if (questionsBo.getAddLineChart() != null) {
            addQuestionsBo.setAddLineChart(questionsBo.getAddLineChart());
          }
          if (questionsBo.getLineChartTimeRange() != null) {
            addQuestionsBo.setLineChartTimeRange(questionsBo.getLineChartTimeRange());
          }
          if (questionsBo.getAllowRollbackChart() != null) {
            addQuestionsBo.setAllowRollbackChart(questionsBo.getAllowRollbackChart());
          }
          if (questionsBo.getChartTitle() != null) {
            addQuestionsBo.setChartTitle(questionsBo.getChartTitle());
          }
          if (questionsBo.getUseStasticData() != null) {
            addQuestionsBo.setUseStasticData(questionsBo.getUseStasticData());
          }
          if (questionsBo.getStatShortName() != null) {
            addQuestionsBo.setStatShortName(questionsBo.getStatShortName());
          }
          if (questionsBo.getStatDisplayName() != null) {
            addQuestionsBo.setStatDisplayName(questionsBo.getStatDisplayName());
          }
          if (questionsBo.getStatDisplayUnits() != null) {
            addQuestionsBo.setStatDisplayUnits(questionsBo.getStatDisplayUnits());
          }
          if (questionsBo.getStatType() != null) {
            addQuestionsBo.setStatType(questionsBo.getStatType());
          }
          if (questionsBo.getStatFormula() != null) {
            addQuestionsBo.setStatFormula(questionsBo.getStatFormula());
          }
          if (questionsBo.getResponseType() != null) {
            addQuestionsBo.setResponseType(questionsBo.getResponseType());
          }
          if (questionsBo.getCreatedOn() != null) {
            addQuestionsBo.setCreatedOn(questionsBo.getCreatedOn());
          }
          if (questionsBo.getCreatedBy() != null) {
            addQuestionsBo.setCreatedBy(questionsBo.getCreatedBy());
          }
          if (questionsBo.getModifiedOn() != null) {
            addQuestionsBo.setModifiedOn(questionsBo.getModifiedOn());
          }
          if (questionsBo.getModifiedBy() != null) {
            addQuestionsBo.setModifiedBy(questionsBo.getModifiedBy());
          }
          if (questionsBo.getQuestionReponseTypeBo() != null) {
            addQuestionsBo.setQuestionReponseTypeBo(questionsBo.getQuestionReponseTypeBo());
          }
          if (questionsBo.getQuestionResponseSubTypeList() != null) {
            List<QuestionResponseSubTypeBo> questionResponseSubTypeBos = new ArrayList<>();
            for (QuestionResponseSubTypeBo questionResponseSubTypeBo :
                questionsBo.getQuestionResponseSubTypeList()) {
              if (questionResponseSubTypeBo.getResponseSubTypeValueId() == null) {
                QuestionResponseSubTypeBo newQuestionResponseSubTypeBo =
                    new QuestionResponseSubTypeBo();
                BeanUtils.copyProperties(questionResponseSubTypeBo, newQuestionResponseSubTypeBo);
                questionResponseSubTypeBos.add(newQuestionResponseSubTypeBo);
              } else {
                questionResponseSubTypeBos.add(questionResponseSubTypeBo);
              }
            }
            addQuestionsBo.setQuestionResponseSubTypeList(questionResponseSubTypeBos);
          }
          if (questionsBo.getFromId() != null) {
            addQuestionsBo.setFromId(questionsBo.getFromId());
          }
          if (questionsBo.getUseAnchorDate() != null) {
            addQuestionsBo.setUseAnchorDate(questionsBo.getUseAnchorDate());
            addQuestionsBo.setAnchorDateName(questionsBo.getAnchorDateName());
            if (questionsBo.getAnchorDateId() != null)
              addQuestionsBo.setAnchorDateId(questionsBo.getAnchorDateId());
          }
          if (questionsBo.getQuestionnaireId() != null) {
            addQuestionsBo.setQuestionnaireId(questionsBo.getQuestionnaireId());
          }
          if (questionsBo.getAllowHealthKit() != null) {
            addQuestionsBo.setAllowHealthKit(questionsBo.getAllowHealthKit());
          }
          if (questionsBo.getHealthkitDatatype() != null) {
            addQuestionsBo.setHealthkitDatatype(questionsBo.getHealthkitDatatype());
          }
          if (questionsBo.getType() != null) {
            if (questionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
              addQuestionsBo.setStatus(false);
            } else if (questionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
              addQuestionsBo.setStatus(true);
            }
          }
          if (questionsBo.getIsShorTitleDuplicate() != null
              && questionsBo.getIsShorTitleDuplicate() > 0)
            addQuestionsBo.setIsShorTitleDuplicate(questionsBo.getIsShorTitleDuplicate());

          addQuestionsBo.setCustomStudyId(customStudyId);
          addQuestionsBo = studyQuestionnaireDAO.saveOrUpdateQuestion(addQuestionsBo);
          if (null != addQuestionsBo && questionsBo.getType() != null) {
            if (questionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
              activity = "Question of form step saved.";
              activitydetails =
                  "Content saved for question of form step. (Question ID = "
                      + addQuestionsBo.getId()
                      + ", Study ID = "
                      + customStudyId
                      + ")";
            } else if (questionsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
              activity = "Question of form step checked for minimum content completeness.";
              activitydetails =
                  "Question  succesfully checked for minimum content completeness and marked 'Done'. (Question ID = "
                      + addQuestionsBo.getId()
                      + ", Study ID = "
                      + customStudyId
                      + ")";
            }
            auditLogDAO.saveToAuditLog(
                null,
                null,
                sesObj,
                activity,
                activitydetails,
                "StudyQuestionnaireServiceImpl - saveOrUpdateQuestion");
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveOrUpdateQuestion - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestion - Ends");
    return addQuestionsBo;
  }

  /**
   * Create or update of questionnaire in study which contains content and scheduling which can be
   * managed by the admin.The questionnaire schedule frequency can be One
   * time,Daily,Weekly,Monthly,Custom and admin has to select any one frequency.
   *
   * @author BTC
   * @param Object , {@link QuestionnaireBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return {@link QuestionnaireBo}
   */
  @Override
  public QuestionnaireBo saveOrUpdateQuestionnaire(
      QuestionnaireBo questionnaireBo,
      SessionObject sessionObject,
      String customStudyId,
      String language) {
    logger.info("StudyQuestionnaireServiceImpl - saveORUpdateQuestionnaire - Starts");
    QuestionnaireBo addQuestionnaireBo = null;
    try {
      if (null != questionnaireBo) {
        if (questionnaireBo.getId() != null) {
          addQuestionnaireBo =
              studyQuestionnaireDAO.getQuestionnaireById(questionnaireBo.getId(), customStudyId);
        } else {
          addQuestionnaireBo = new QuestionnaireBo();
          addQuestionnaireBo.setActive(true);

          List<StudySequenceLangBO> studySequenceLangBOS =
              studyDAO.getStudySequenceByStudyId(questionnaireBo.getStudyId());
          for (StudySequenceLangBO studySequenceLangBO : studySequenceLangBOS) {
            studySequenceLangBO.setStudyExcQuestionnaries(false);
            studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
          }
        }

        if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
          QuestionnaireLangBO questionnaireLangBO =
              studyQuestionnaireDAO.getQuestionnaireLangById(questionnaireBo.getId(), language);

          if (questionnaireLangBO == null) {
            questionnaireLangBO = new QuestionnaireLangBO();
            questionnaireLangBO.setQuestionnaireLangPK(
                new QuestionnaireLangPK(addQuestionnaireBo.getId(), language));
            questionnaireLangBO.setCreatedBy(questionnaireBo.getCreatedBy());
            questionnaireLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            questionnaireLangBO.setActive(true);
            questionnaireLangBO.setStudyId(questionnaireBo.getStudyId());
          } else {
            questionnaireLangBO.setModifiedBy(questionnaireBo.getCreatedBy());
            questionnaireLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          questionnaireLangBO.setStatus(questionnaireBo.getStatus());
          questionnaireLangBO.setTitle(questionnaireBo.getTitle());
          studyQuestionnaireDAO.saveOrUpdateObject(questionnaireLangBO);

          StudySequenceLangBO studySequenceLangBO =
              studyService.getStudySequenceById(questionnaireBo.getStudyId(), language);
          if (studySequenceLangBO != null) {
            if (questionnaireBo.getStatus()) {
              studySequenceLangBO.setStudyExcQuestionnaries(false);
              studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
          studyDAO.updateDraftStatusInStudyBo(
              sessionObject.getUserId(), questionnaireBo.getStudyId());
        } else {
          if (questionnaireBo.getStudyId() != null) {
            addQuestionnaireBo.setStudyId(questionnaireBo.getStudyId());
          }
          if (questionnaireBo.getFrequency() != null) {
            addQuestionnaireBo.setFrequency(questionnaireBo.getFrequency());
          }
          if (questionnaireBo.getScheduleType() != null) {
            addQuestionnaireBo.setScheduleType(questionnaireBo.getScheduleType());
          }
          if (StringUtils.equals(
                  questionnaireBo.getScheduleType(),
                  FdahpStudyDesignerConstants.SCHEDULETYPE_ANCHORDATE)
              && questionnaireBo.getAnchorDateId() != null) {
            addQuestionnaireBo.setAnchorDateId(questionnaireBo.getAnchorDateId());
          } else {
            addQuestionnaireBo.setAnchorDateId(null);
          }
          if (questionnaireBo.getFrequency() != null
              && !questionnaireBo
                  .getFrequency()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)) {
            if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeStart())
                && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeStart())
                && !questionnaireBo.getStudyLifetimeStart().isEmpty()) {
              addQuestionnaireBo.setStudyLifetimeStart(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireBo.getStudyLifetimeStart(),
                      FdahpStudyDesignerConstants.UI_SDF_DATE,
                      FdahpStudyDesignerConstants.DB_SDF_DATE));
              if (questionnaireBo.getAnchorDateId() != null) {
                addQuestionnaireBo.setAnchorDateId(questionnaireBo.getAnchorDateId());
              }
            } else {
              addQuestionnaireBo.setStudyLifetimeStart(null);
            }
            if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeEnd())
                && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeEnd())) {
              addQuestionnaireBo.setStudyLifetimeEnd(
                  FdahpStudyDesignerUtil.getFormattedDate(
                      questionnaireBo.getStudyLifetimeEnd(),
                      FdahpStudyDesignerConstants.UI_SDF_DATE,
                      FdahpStudyDesignerConstants.DB_SDF_DATE));
            } else {
              addQuestionnaireBo.setStudyLifetimeEnd(null);
            }
          }
          if (questionnaireBo.getTitle() != null) {
            addQuestionnaireBo.setTitle(questionnaireBo.getTitle());
          }
          if (questionnaireBo.getShortTitle() != null) {
            addQuestionnaireBo.setShortTitle(questionnaireBo.getShortTitle());
          }
          if (questionnaireBo.getCreatedDate() != null) {
            addQuestionnaireBo.setCreatedDate(questionnaireBo.getCreatedDate());
          }
          if (questionnaireBo.getCreatedBy() != null) {
            addQuestionnaireBo.setCreatedBy(questionnaireBo.getCreatedBy());
          }
          if (questionnaireBo.getModifiedDate() != null) {
            addQuestionnaireBo.setModifiedDate(questionnaireBo.getModifiedDate());
          }
          if (questionnaireBo.getModifiedBy() != null) {
            addQuestionnaireBo.setModifiedBy(questionnaireBo.getModifiedBy());
          }
          addQuestionnaireBo.setRepeatQuestionnaire(questionnaireBo.getRepeatQuestionnaire());
          if (questionnaireBo.getDayOfTheWeek() != null) {
            addQuestionnaireBo.setDayOfTheWeek(questionnaireBo.getDayOfTheWeek());
          }
          if (questionnaireBo.getType() != null) {
            addQuestionnaireBo.setType(questionnaireBo.getType());
          }
          if (questionnaireBo.getBranching() != null) {
            addQuestionnaireBo.setBranching(questionnaireBo.getBranching());
          }
          addQuestionnaireBo.setAddNewRuns(questionnaireBo.getAddNewRuns());
          if (questionnaireBo.getStatus() != null) {
            addQuestionnaireBo.setStatus(questionnaireBo.getStatus());
            if (questionnaireBo.getStatus()) {
              questionnaireBo.setIsChange(1);
            } else {
              questionnaireBo.setIsChange(0);
            }
          }
          if (questionnaireBo.getFrequency() != null) {
            QuestionnairesFrequenciesBo questionnairesFrequenciesBo =
                questionnaireBo.getQuestionnairesFrequenciesBo() != null
                    ? questionnaireBo.getQuestionnairesFrequenciesBo()
                    : null;
            if (!questionnaireBo
                .getFrequency()
                .equalsIgnoreCase(questionnaireBo.getPreviousFrequency())) {
              addQuestionnaireBo.setQuestionnaireCustomScheduleBo(
                  questionnaireBo.getQuestionnaireCustomScheduleBo());
              addQuestionnaireBo.setQuestionnairesFrequenciesList(
                  questionnaireBo.getQuestionnairesFrequenciesList());
              if (questionnaireBo
                  .getFrequency()
                  .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)) {
                if (questionnaireBo.getQuestionnairesFrequenciesBo() != null) {
                  if (!questionnaireBo.getQuestionnairesFrequenciesBo().getIsLaunchStudy()) {
                    if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeStart())
                        && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeStart())
                        && !questionnaireBo.getStudyLifetimeStart().isEmpty()) {
                      addQuestionnaireBo.setStudyLifetimeStart(
                          FdahpStudyDesignerUtil.getFormattedDate(
                              questionnaireBo.getStudyLifetimeStart(),
                              FdahpStudyDesignerConstants.UI_SDF_DATE,
                              FdahpStudyDesignerConstants.DB_SDF_DATE));
                    }
                  } else {
                    questionnairesFrequenciesBo.setFrequencyTime(null);
                  }
                  if (!questionnaireBo.getQuestionnairesFrequenciesBo().getIsStudyLifeTime()) {
                    if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeEnd())
                        && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeEnd())) {
                      addQuestionnaireBo.setStudyLifetimeEnd(
                          FdahpStudyDesignerUtil.getFormattedDate(
                              questionnaireBo.getStudyLifetimeEnd(),
                              FdahpStudyDesignerConstants.UI_SDF_DATE,
                              FdahpStudyDesignerConstants.DB_SDF_DATE));
                    }
                  } else {
                    addQuestionnaireBo.setStudyLifetimeEnd(null);
                  }
                }
              }
              addQuestionnaireBo.setQuestionnairesFrequenciesBo(questionnairesFrequenciesBo);
            } else {
              if (questionnaireBo.getQuestionnaireCustomScheduleBo() != null
                  && !questionnaireBo.getQuestionnaireCustomScheduleBo().isEmpty()) {
                addQuestionnaireBo.setQuestionnaireCustomScheduleBo(
                    questionnaireBo.getQuestionnaireCustomScheduleBo());
              }
              if (questionnaireBo.getQuestionnairesFrequenciesList() != null
                  && !questionnaireBo.getQuestionnairesFrequenciesList().isEmpty()) {
                addQuestionnaireBo.setQuestionnairesFrequenciesList(
                    questionnaireBo.getQuestionnairesFrequenciesList());
              }
              if (questionnaireBo.getQuestionnairesFrequenciesBo() != null) {
                if (questionnaireBo
                    .getFrequency()
                    .equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)) {
                  if (!questionnaireBo.getQuestionnairesFrequenciesBo().getIsLaunchStudy()) {
                    if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeStart())
                        && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeStart())
                        && !questionnaireBo.getStudyLifetimeStart().isEmpty()) {
                      addQuestionnaireBo.setStudyLifetimeStart(
                          FdahpStudyDesignerUtil.getFormattedDate(
                              questionnaireBo.getStudyLifetimeStart(),
                              FdahpStudyDesignerConstants.UI_SDF_DATE,
                              FdahpStudyDesignerConstants.DB_SDF_DATE));
                    }
                  } else {
                    questionnairesFrequenciesBo.setFrequencyTime(null);
                  }
                  if (!questionnaireBo.getQuestionnairesFrequenciesBo().getIsStudyLifeTime()) {
                    if (StringUtils.isNotBlank(questionnaireBo.getStudyLifetimeEnd())
                        && !("NA").equalsIgnoreCase(questionnaireBo.getStudyLifetimeEnd())) {
                      addQuestionnaireBo.setStudyLifetimeEnd(
                          FdahpStudyDesignerUtil.getFormattedDate(
                              questionnaireBo.getStudyLifetimeEnd(),
                              FdahpStudyDesignerConstants.UI_SDF_DATE,
                              FdahpStudyDesignerConstants.DB_SDF_DATE));
                    }
                  } else {
                    addQuestionnaireBo.setStudyLifetimeEnd(null);
                  }
                }
                addQuestionnaireBo.setQuestionnairesFrequenciesBo(questionnairesFrequenciesBo);
              }
            }
          }
          if (questionnaireBo.getPreviousFrequency() != null) {
            addQuestionnaireBo.setPreviousFrequency(questionnaireBo.getPreviousFrequency());
          }
          if (questionnaireBo.getCurrentFrequency() != null) {
            addQuestionnaireBo.setCurrentFrequency(questionnaireBo.getCurrentFrequency());
          }
          addQuestionnaireBo.setIsChange(questionnaireBo.getIsChange());
          addQuestionnaireBo =
              studyQuestionnaireDAO.saveORUpdateQuestionnaire(
                  addQuestionnaireBo, sessionObject, customStudyId);
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveORUpdateQuestionnaire - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveORUpdateQuestionnaire - Ends");
    return addQuestionnaireBo;
  }

  /**
   * This method is used to save the questionnaire schedule information of an study
   *
   * @author BTC
   * @param Object , {@link QuestionnaireBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return {@link QuestionnaireBo}
   */
  @Override
  public QuestionnaireBo saveOrUpdateQuestionnaireSchedule(
      QuestionnaireBo questionnaireBo, SessionObject sessionObject, String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionnaireSchedule - Starts");
    QuestionnaireBo addQuestionnaireBo = null;
    try {
      addQuestionnaireBo =
          studyQuestionnaireDAO.saveORUpdateQuestionnaire(
              questionnaireBo, sessionObject, customStudyId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionnaireSchedule - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionnaireSchedule - Ends");
    return addQuestionnaireBo;
  }

  /**
   * Admin can add the question step to questionnaire here which contains the 3 subsections admin
   * has to fill the sub section such as step level attribute,question level attribute,response
   * level attributes.Questions can be various types as defined by the response format. Depending on
   * the response format, the attributes of the QA would vary Here we can create or update the
   * question step in questionnaire
   *
   * @author BTC
   * @param Object , {@link QuestionnairesStepsBo}
   * @param Object , {@link SessionObject}
   * @param String , customStudyId {@link StudyBo}
   * @return {@link QuestionnairesStepsBo}
   */
  @Override
  public QuestionnairesStepsBo saveOrUpdateQuestionStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sessionObject,
      String customStudyId,
      String language,
      String studyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionStep - Starts");
    QuestionnairesStepsBo addOrUpdateQuestionnairesStepsBo = null;
    QuestionLangBO questionLangBO = null;
    try {
      QuestionsBo addQuestionsBo = null;
      if (questionnairesStepsBo != null && questionnairesStepsBo.getQuestionsBo() != null) {
        Integer id = questionnairesStepsBo.getQuestionsBo().getId();

        if (id != null) {
          addQuestionsBo = studyQuestionnaireDAO.getQuestionsById(id, null, customStudyId);
          if (questionnairesStepsBo.getModifiedOn() != null) {
            addQuestionsBo.setModifiedOn(questionnairesStepsBo.getModifiedOn());
          }
          if (questionnairesStepsBo.getModifiedBy() != null) {
            addQuestionsBo.setModifiedBy(questionnairesStepsBo.getModifiedBy());
          }
        } else {
          addQuestionsBo = new QuestionsBo();
          if (questionnairesStepsBo.getCreatedOn() != null) {
            addQuestionsBo.setCreatedOn(questionnairesStepsBo.getCreatedOn());
          }
          if (questionnairesStepsBo.getCreatedBy() != null) {
            addQuestionsBo.setCreatedBy(questionnairesStepsBo.getCreatedBy());
          }
          addQuestionsBo.setActive(true);
          this.updateStudyLangSequence(
              questionnairesStepsBo.getQuestionnairesId(), Integer.parseInt(studyId));
        }
        if (id != null && (StringUtils.isEmpty(language) || "en".equals(language))) {
          questionLangBO = studyQuestionnaireDAO.getQuestionLangBo(id, "es");
          if (questionLangBO != null) {
            StringBuilder dispText = new StringBuilder();
            StringBuilder desc = new StringBuilder();
            List<QuestionResponseSubTypeBo> subTypeBoList = questionnairesStepsBo.getQuestionResponseSubTypeList();
            if (subTypeBoList != null && !subTypeBoList.isEmpty()) {
              int size = subTypeBoList.size();
              int i = 0;
              if (FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(questionnairesStepsBo.getType())) {
                Collections.sort(subTypeBoList, new Comparator<QuestionResponseSubTypeBo>() {
                  public int compare(final QuestionResponseSubTypeBo object1, final QuestionResponseSubTypeBo object2) {
                    if (object1.getSequenceNumber() != null)  {
                      return object1.getSequenceNumber().compareTo(object2.getSequenceNumber());
                    }
                    return 0;
                  }
                });
              }
              for (QuestionResponseSubTypeBo subTypeBo : subTypeBoList) {
                // for spanish
                String dispLang = StringUtils.isNotBlank(subTypeBo.getDisplayTextLang())
                        ? subTypeBo.getDisplayTextLang()
                        : "";
                String descLang = StringUtils.isNotBlank(subTypeBo.getDescriptionLang())
                        ? subTypeBo.getDescriptionLang()
                        : "";
                if (i == size - 1) {
                  dispText.append(dispLang);
                  desc.append(descLang);
                } else {
                  dispText.append(dispLang).append("|");
                  desc.append(descLang).append("|");
                }
                i++;
              }
              questionLangBO.setDisplayText(dispText.toString());
              questionLangBO.setTextChoiceDescription(desc.toString());
              studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
            }
          }
        }

        if (FdahpStudyDesignerUtil.isNotEmpty(language)
            && !"en".equals(language)
            && !"undefined".equals(language)) {
          if (id != null) {
            questionLangBO = studyQuestionnaireDAO.getQuestionLangBo(id, language);
          }
          if (questionLangBO == null) {
            questionLangBO = new QuestionLangBO();
            questionLangBO.setQuestionLangPK(new QuestionLangPK(id, language));
            questionLangBO.setQuestionnaireId(questionnairesStepsBo.getQuestionnairesId());
            questionLangBO.setCreatedBy(questionnairesStepsBo.getCreatedBy());
            questionLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          } else {
            questionLangBO.setModifiedBy(questionLangBO.getCreatedBy());
            questionLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }

          QuestionsBo questionsBo = questionnairesStepsBo.getQuestionsBo();
          if (questionsBo != null) {
            questionLangBO.setQuestion(questionsBo.getQuestion());
            questionLangBO.setDescription(questionsBo.getDescription());
            questionLangBO.setStatDisplayName(questionsBo.getStatDisplayName());
            questionLangBO.setStatDisplayUnits(questionsBo.getStatDisplayUnits());
            questionLangBO.setChartTitle(questionsBo.getChartTitle());
            questionLangBO.setResponseTypeId(questionsBo.getResponseType());
          }

          QuestionReponseTypeBo questionReponseTypeBo =
              questionnairesStepsBo.getQuestionReponseTypeBo();
          if (questionReponseTypeBo != null) {
            questionLangBO.setMaxDescription(questionReponseTypeBo.getMaxDescription());
            questionLangBO.setMinDescription(questionReponseTypeBo.getMinDescription());
            questionLangBO.setPlaceholderText(questionReponseTypeBo.getPlaceholder());
            questionLangBO.setUnit(questionReponseTypeBo.getUnit());
            questionLangBO.setInvalidMessage(questionReponseTypeBo.getInvalidMessage());
            questionLangBO.setExceptText(questionReponseTypeBo.getValidationExceptText());
            questionLangBO.setOtherText(questionReponseTypeBo.getOtherText());
            questionLangBO.setOtherPlaceholderText(questionReponseTypeBo.getOtherPlaceholderText());
            questionLangBO.setOtherDescription(questionReponseTypeBo.getOtherDescription());
          }

          StringBuilder displayText = new StringBuilder();
          StringBuilder description = new StringBuilder();
          if (questionnairesStepsBo.getQuestionResponseSubTypeList() != null
              && questionnairesStepsBo.getQuestionResponseSubTypeList().size() > 0) {
            int size = questionnairesStepsBo.getQuestionResponseSubTypeList().size();
            int i = 0;
            for (QuestionResponseSubTypeBo subTypeBo :
                questionnairesStepsBo.getQuestionResponseSubTypeList()) {
              if (i == size - 1) {
                displayText.append(subTypeBo.getText());
                description.append(subTypeBo.getDescription());
              } else {
                displayText.append(subTypeBo.getText()).append("|");
                description.append(subTypeBo.getDescription()).append("|");
              }
              i++;
            }
          }
          questionLangBO.setDisplayText(displayText.toString());
          questionLangBO.setTextChoiceDescription(description.toString());
          questionLangBO.setStatus(
              FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(
                  questionnairesStepsBo.getType()));
          studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);

          if (StringUtils.isNotBlank(studyId)) {
            StudySequenceLangBO studySequenceLangBO =
                studyService.getStudySequenceById(Integer.parseInt(studyId), language);
            if (studySequenceLangBO != null) {
              studySequenceLangBO.setStudyExcQuestionnaries(false);
              studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
          studyDAO.updateDraftStatusInStudyBo(sessionObject.getUserId(), Integer.parseInt(studyId));
        } else {
          if (questionnairesStepsBo.getQuestionsBo().getQuestion() != null) {
            addQuestionsBo.setQuestion(questionnairesStepsBo.getQuestionsBo().getQuestion());
          }
          addQuestionsBo.setDescription(questionnairesStepsBo.getQuestionsBo().getDescription());
          if (questionnairesStepsBo.getQuestionsBo().getSkippable() != null) {
            addQuestionsBo.setSkippable(questionnairesStepsBo.getQuestionsBo().getSkippable());
          }
          if (questionnairesStepsBo.getQuestionsBo().getAddLineChart() != null) {
            addQuestionsBo.setAddLineChart(
                questionnairesStepsBo.getQuestionsBo().getAddLineChart());
          }
          if (questionnairesStepsBo.getQuestionsBo().getLineChartTimeRange() != null) {
            addQuestionsBo.setLineChartTimeRange(
                questionnairesStepsBo.getQuestionsBo().getLineChartTimeRange());
          }
          if (questionnairesStepsBo.getQuestionsBo().getAllowRollbackChart() != null) {
            addQuestionsBo.setAllowRollbackChart(
                questionnairesStepsBo.getQuestionsBo().getAllowRollbackChart());
          }
          if (questionnairesStepsBo.getQuestionsBo().getChartTitle() != null) {
            addQuestionsBo.setChartTitle(questionnairesStepsBo.getQuestionsBo().getChartTitle());
          }
          if (questionnairesStepsBo.getQuestionsBo().getUseStasticData() != null) {
            addQuestionsBo.setUseStasticData(
                questionnairesStepsBo.getQuestionsBo().getUseStasticData());
          }
          if (questionnairesStepsBo.getQuestionsBo().getStatShortName() != null) {
            addQuestionsBo.setStatShortName(
                questionnairesStepsBo.getQuestionsBo().getStatShortName());
          }
          if (questionnairesStepsBo.getQuestionsBo().getStatDisplayName() != null) {
            addQuestionsBo.setStatDisplayName(
                questionnairesStepsBo.getQuestionsBo().getStatDisplayName());
          }
          if (questionnairesStepsBo.getQuestionsBo().getStatDisplayUnits() != null) {
            addQuestionsBo.setStatDisplayUnits(
                questionnairesStepsBo.getQuestionsBo().getStatDisplayUnits());
          }
          if (questionnairesStepsBo.getQuestionsBo().getStatType() != null) {
            addQuestionsBo.setStatType(questionnairesStepsBo.getQuestionsBo().getStatType());
          }
          if (questionnairesStepsBo.getQuestionsBo().getStatFormula() != null) {
            addQuestionsBo.setStatFormula(questionnairesStepsBo.getQuestionsBo().getStatFormula());
          }
          if (questionnairesStepsBo.getQuestionsBo().getResponseType() != null) {
            addQuestionsBo.setResponseType(
                questionnairesStepsBo.getQuestionsBo().getResponseType());
          }
          if (questionnairesStepsBo.getQuestionsBo().getUseAnchorDate() != null) {
            addQuestionsBo.setUseAnchorDate(
                questionnairesStepsBo.getQuestionsBo().getUseAnchorDate());
            if (StringUtils.isNotEmpty(questionnairesStepsBo.getQuestionsBo().getAnchorDateName()))
              addQuestionsBo.setAnchorDateName(
                  questionnairesStepsBo.getQuestionsBo().getAnchorDateName());
            if (questionnairesStepsBo.getQuestionsBo().getAnchorDateId() != null)
              addQuestionsBo.setAnchorDateId(
                  questionnairesStepsBo.getQuestionsBo().getAnchorDateId());
          }
          if (questionnairesStepsBo.getQuestionsBo().getAllowHealthKit() != null) {
            addQuestionsBo.setAllowHealthKit(
                questionnairesStepsBo.getQuestionsBo().getAllowHealthKit());
          }
          if (questionnairesStepsBo.getQuestionsBo().getHealthkitDatatype() != null) {
            addQuestionsBo.setHealthkitDatatype(
                questionnairesStepsBo.getQuestionsBo().getHealthkitDatatype());
          }
          if (questionnairesStepsBo.getType() != null) {
            if (questionnairesStepsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)) {
              addQuestionsBo.setStatus(false);
            } else if (questionnairesStepsBo
                .getType()
                .equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE)) {
              addQuestionsBo.setStatus(true);
            }
          }
          questionnairesStepsBo.setQuestionsBo(addQuestionsBo);
          addOrUpdateQuestionnairesStepsBo =
              studyQuestionnaireDAO.saveOrUpdateQuestionStep(
                  questionnairesStepsBo, sessionObject, customStudyId);
          if (questionnairesStepsBo.getInstructionFormId() == null) {
            questionnairesStepsBo.setInstructionFormId(addOrUpdateQuestionnairesStepsBo.getInstructionFormId());
            questionnairesStepsBo.setStepId(addOrUpdateQuestionnairesStepsBo.getStepId());
          }
          this.doPreLoadLogic(questionnairesStepsBo, customStudyId);
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionStep - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateQuestionStep - Ends");
    return addOrUpdateQuestionnairesStepsBo;
  }

  /**
   * The admin can choose to add a response data element to the study dashboard in the form of line
   * charts or statistics.Adding a line chart to the dashboard needs the admin to specify The
   * options time range for the chart which depend on the scheduling frequency set for the
   * activity.when admin change the frequency in questionnaire schedule its validate the options in
   * the time range for chart options.
   *
   * @author BTC
   * @param Integer , questionnaireId {@link QuestionnaireBo}
   * @param String , frequency {@link QuestionnaireBo}
   * @return String , Success or Failure
   */
  @Override
  public String validateLineChartSchedule(Integer questionnaireId, String frequency) {
    logger.info(
        "StudyQuestionnaireServiceImpl - checkQuestionnaireResponseTypeValidation - Starts");
    return studyQuestionnaireDAO.validateLineChartSchedule(questionnaireId, frequency);
  }

  /**
   * For QA of response type that results in the data type 'double',the admin can define conditional
   * logic (formula-based) to evaluate with user response as the input. A condition or formula is to
   * be defined along with a destination step to navigate to if the result of evaluation is TRUE and
   * an alternative destination step if FALSE.Admin can check the condition is valid or not here.
   *
   * @author BTC
   * @param String , lhs
   * @param String , rhs
   * @param String , operator
   * @param String , input
   * @return List of {@link QuestionConditionBranchBo}
   */
  @Override
  public FormulaInfoBean validateQuestionConditionalBranchingLogic(
      String lhs, String rhs, String operator, String input) {
    logger.info(
        "StudyQuestionnaireServiceImpl - validateQuestionConditionalBranchingLogic - Starts");
    FormulaInfoBean formulaInfoBean = new FormulaInfoBean();
    if (StringUtils.isNotEmpty(lhs)
        && StringUtils.isNotEmpty(rhs)
        && StringUtils.isNotEmpty(operator)) {
      formulaInfoBean =
          FdahpStudyDesignerUtil.getConditionalFormulaResult(lhs, rhs, operator, input);
    }
    logger.info("StudyQuestionnaireServiceImpl - validateQuestionConditionalBranchingLogic - Ends");
    return formulaInfoBean;
  }

  /**
   * In Questionnaire form step carries the multiple question and Answers .In form level attributes
   * we can make form form as repeatable if the form is repeatable we can not add the line chart and
   * states data to the dashbord.here we are validating the added line chart and statistics data
   * before updating the form as repeatable.
   *
   * @author BTC
   * @param Integer , formId {@link FormBo}
   * @param String , Success or failure
   */
  @Override
  public String validateRepetableFormQuestionStats(Integer formId) {
    logger.info("StudyQuestionnaireServiceImpl - validateRepetableFormQuestionStats - Starts");
    return studyQuestionnaireDAO.validateRepetableFormQuestionStats(formId);
  }

  /**
   * A questionnaire is an ordered set of one or more steps.Each step contains the step short title
   * field. Which will be response column for the step in response server.so it should be the
   * unique.Here validating the unique for step short title
   *
   * @author BTC
   * @param String , anchordateText {@link QuestionnairesStepsBo}
   * @param String , customStudyId {@link StudyBo}
   * @return String, Success or Failure
   */
  @Override
  public String checkUniqueAnchorDateName(
      String anchordateText, String customStudyId, String anchorDateId) {
    logger.info("StudyQuestionnaireServiceImpl - checkUniqueAnchorDateName - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.checkUniqueAnchorDateName(
              anchordateText, customStudyId, anchorDateId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - checkUniqueAnchorDateName - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - checkUniqueAnchorDateName - Ends");
    return message;
  }

  @Override
  public List<AnchorDateTypeBo> getAnchorTypesByStudyId(String customStudyId) {
    logger.info("StudyQuestionnaireServiceImpl - getAnchorTypesByStudyId - Starts");
    List<AnchorDateTypeBo> anchorDateTypeBos = null;
    HashMap<String, AnchorDateTypeBo> anchorMap = new HashMap<>();
    try {
      anchorDateTypeBos = studyQuestionnaireDAO.getAnchorTypesByStudyId(customStudyId);
      for (AnchorDateTypeBo anchorDateTypeBo : anchorDateTypeBos) {
        anchorMap.put(anchorDateTypeBo.getName(), anchorDateTypeBo);
      }
      anchorDateTypeBos = new ArrayList<>(anchorMap.values());
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getAnchorTypesByStudyId - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getAnchorTypesByStudyId - Ends");
    return anchorDateTypeBos;
  }

  @Override
  public boolean isAnchorDateExistByQuestionnaire(Integer questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - isAnchorDateExistByQuestionnaire - Starts");
    return studyQuestionnaireDAO.isAnchorDateExistByQuestionnaire(questionnaireId);
  }

  @Override
  public InstructionsLangBO getInstructionLangBO(int instructionId, String language) {
    logger.info("StudyQuestionnaireServiceImpl - getInstructionLangBO - Starts");
    InstructionsLangBO instructionsBo = null;
    try {
      instructionsBo = studyQuestionnaireDAO.getInstructionLangBo(instructionId, language);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getInstructionLangBO - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getInstructionLangBO - Ends");
    return instructionsBo;
  }

  @Override
  public FormLangBO getFormLangBO(int formId, String language) {
    logger.info("StudyQuestionnaireServiceImpl - getFormLangBO - Starts");
    FormLangBO formLangBO = null;
    try {
      formLangBO = studyQuestionnaireDAO.getFormLangBo(formId, language);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getFormLangBO - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getFormLangBO - Ends");
    return formLangBO;
  }

  @Override
  public QuestionLangBO getQuestionLangBO(int id, String language) {
    logger.info("StudyQuestionnaireServiceImpl - getQuestionLangBO - Starts");
    QuestionLangBO questionLangBO = null;
    try {
      questionLangBO = studyQuestionnaireDAO.getQuestionLangBo(id, language);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getQuestionLangBO - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - getQuestionLangBO - Ends");
    return questionLangBO;
  }

  @Override
  public String saveOrUpdateFormStepForOtherLanguages(
      QuestionnairesStepsBo questionnairesStepsBo, String language, String studyId) {
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateFormStepForOtherLanguages - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    try {
      FormLangBO formLangBO =
          studyQuestionnaireDAO.getFormLangBo(
              questionnairesStepsBo.getInstructionFormId(), language);
      if (formLangBO == null) {
        formLangBO = new FormLangBO();
        formLangBO.setFormLangPK(
            new FormLangPK(questionnairesStepsBo.getInstructionFormId(), language));
        formLangBO.setQuestionnaireId(questionnairesStepsBo.getQuestionnairesId());
        formLangBO.setCreatedBy(questionnairesStepsBo.getCreatedBy());
        formLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
      } else {
        formLangBO.setModifiedBy(questionnairesStepsBo.getCreatedBy());
        formLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
      }
      formLangBO.setRepeatableText(questionnairesStepsBo.getRepeatableText());
      formLangBO.setStatus(
          FdahpStudyDesignerConstants.ACTION_TYPE_COMPLETE.equals(questionnairesStepsBo.getType()));
      studyQuestionnaireDAO.saveOrUpdateObject(formLangBO);

      if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
        StudySequenceLangBO studySequenceLangBO =
            studyService.getStudySequenceById(Integer.parseInt(studyId), language);
        if (studySequenceLangBO != null) {
          studySequenceLangBO.setStudyExcQuestionnaries(false);
          studyQuestionnaireDAO.saveOrUpdateObject(studySequenceLangBO);
        }
      }
      studyDAO.updateDraftStatusInStudyBo(
          questionnairesStepsBo.getCreatedBy(), Integer.parseInt(studyId));
      result = FdahpStudyDesignerConstants.SUCCESS;
    } catch (Exception e) {
      logger.error(
          "StudyQuestionnaireServiceImpl - saveOrUpdateFormStepForOtherLanguages - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - saveOrUpdateFormStepForOtherLanguages - Ends");
    return result;
  }

  @Override
  public List<String> syncAndGetLangData(
      Map<Integer, QuestionnaireStepBean> qTreeMap,
      int questionnaireId,
      String language,
      int userId) {
    logger.info("StudyQuestionnaireServiceImpl - syncAndGetLangData - Starts");
    List<String> textList = new ArrayList<>();
    try {
      if (qTreeMap.values().size() > 0) {
        for (QuestionnaireStepBean stepBean : qTreeMap.values()) {
          if (FdahpStudyDesignerConstants.INSTRUCTION_STEP.equals(stepBean.getStepType())) {
            InstructionsLangBO instructionsLangBO =
                studyQuestionnaireDAO.getInstructionLangBo(stepBean.getStepId(), language);
            if (instructionsLangBO == null) {
              instructionsLangBO = new InstructionsLangBO();
              instructionsLangBO.setInstructionLangPK(
                  new InstructionLangPK(stepBean.getStepId(), language));
              instructionsLangBO.setQuestionnaireId(questionnaireId);
              instructionsLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              instructionsLangBO.setCreatedBy(userId);
              studyQuestionnaireDAO.saveOrUpdateObject(instructionsLangBO);
            }
          } else if (FdahpStudyDesignerConstants.QUESTION_STEP.equals(stepBean.getStepType())) {
            QuestionLangBO questionLangBO =
                studyQuestionnaireDAO.getQuestionLangBo(stepBean.getStepId(), language);
            if (questionLangBO == null) {
              questionLangBO = new QuestionLangBO();
              questionLangBO.setQuestionLangPK(new QuestionLangPK(stepBean.getStepId(), language));
              questionLangBO.setQuestionnaireId(questionnaireId);
              questionLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              questionLangBO.setCreatedBy(userId);
              studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
            }
          } else if (FdahpStudyDesignerConstants.FORM_STEP.equals(stepBean.getStepType())) {
            FormLangBO formLangBO =
                studyQuestionnaireDAO.getFormLangBo(stepBean.getQuestionInstructionId(), language);
            if (formLangBO == null) {
              formLangBO = new FormLangBO();
              formLangBO.setFormLangPK(
                  new FormLangPK(stepBean.getQuestionInstructionId(), language));
              formLangBO.setQuestionnaireId(questionnaireId);
              formLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              formLangBO.setCreatedBy(userId);
              studyQuestionnaireDAO.saveOrUpdateObject(formLangBO);
            }

            if (stepBean.getFromMap() != null) {
              for (QuestionnaireStepBean questionnaireStepBean : stepBean.getFromMap().values()) {
                QuestionLangBO questionLangBO =
                    studyQuestionnaireDAO.getQuestionLangBo(
                        questionnaireStepBean.getQuestionInstructionId(), language);
                if (questionLangBO == null) {
                  questionLangBO = new QuestionLangBO();
                  questionLangBO.setQuestionLangPK(
                      new QuestionLangPK(
                          questionnaireStepBean.getQuestionInstructionId(), language));
                  questionLangBO.setQuestionnaireId(questionnaireId);
                  questionLangBO.setFormId(questionnaireStepBean.getStepId());
                  questionLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
                  questionLangBO.setCreatedBy(userId);
                  studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - syncAndGetLangData - ERROR ", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - syncAndGetLangData - Ends");
    return textList;
  }

  @Override
  public List<QuestionnaireLangBO> syncAndGetQuestionnaireLangList(
      List<QuestionnaireBo> boList, Integer studyId, String language) {
    logger.info("StudyServiceImpl - syncAndGetQuestionnaireLangList() - Starts");
    List<QuestionnaireLangBO> questionnaireLangBOS = null;
    try {
      if (boList != null && boList.size() > 0) {
        for (QuestionnaireBo questionnaireBo : boList) {
          QuestionnaireLangBO questionnaireLangBO =
              studyQuestionnaireDAO.getQuestionnaireLangById(questionnaireBo.getId(), language);
          if (questionnaireLangBO == null) {
            questionnaireLangBO = new QuestionnaireLangBO();
            questionnaireLangBO.setQuestionnaireLangPK(
                new QuestionnaireLangPK(questionnaireBo.getId(), language));
            questionnaireLangBO.setStudyId(questionnaireBo.getStudyId());
            questionnaireLangBO.setActive(true);
            questionnaireLangBO.setStatus(false);
            questionnaireLangBO.setCreatedBy(questionnaireBo.getCreatedBy());
            questionnaireLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            studyQuestionnaireDAO.saveOrUpdateObject(questionnaireLangBO);
          }
        }
      }
      questionnaireLangBOS = studyQuestionnaireDAO.getQuestionnaireLangByStudyId(studyId, language);
    } catch (Exception e) {
      logger.error("StudyServiceImpl - syncAndGetQuestionnaireLangList() - Error", e);
    }
    logger.info("StudyServiceImpl - syncAndGetQuestionnaireLangList() - Ends");
    return questionnaireLangBOS;
  }

  @Override
  public List<QuestionLangBO> getQuestionLangByQuestionnaireId(
      int questionnaireId, String language) {
    logger.info("StudyServiceImpl - getQuestionLangByQuestionnaireId() - Starts");
    List<QuestionLangBO> questionLangBOS = null;
    try {
      questionLangBOS =
          studyQuestionnaireDAO.getQuestionLangByQuestionnaireId(questionnaireId, language);
    } catch (Exception e) {
      logger.error("StudyServiceImpl - getQuestionLangByQuestionnaireId() - Error", e);
    }
    logger.info("StudyServiceImpl - getQuestionLangByQuestionnaireId() - Ends");
    return questionLangBOS;
  }

  @Override
  public List<QuestionLangBO> syncAndGetQuestionLangByFormId(
      QuestionnairesStepsBo questionnairesStepsBo,
      int questionnaireId,
      int formId,
      String language) {
    logger.info("StudyServiceImpl - getQuestionLangByFormId() - Starts");
    List<QuestionLangBO> questionLangBOS = null;
    try {

      FormLangBO formLangBO =
          studyQuestionnaireDAO.getFormLangBo(
              questionnairesStepsBo.getInstructionFormId(), language);
      if (formLangBO == null) {
        formLangBO = new FormLangBO();
        formLangBO.setFormLangPK(
            new FormLangPK(questionnairesStepsBo.getInstructionFormId(), language));
        formLangBO.setQuestionnaireId(questionnaireId);
        formLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
        formLangBO.setCreatedBy(questionnairesStepsBo.getCreatedBy());
        studyQuestionnaireDAO.saveOrUpdateObject(formLangBO);
      }

      if (questionnairesStepsBo.getFormQuestionMap() != null) {
        for (QuestionnaireStepBean questionnaireStepBean :
            questionnairesStepsBo.getFormQuestionMap().values()) {
          QuestionLangBO questionLangBO =
              studyQuestionnaireDAO.getQuestionLangBo(
                  questionnaireStepBean.getQuestionInstructionId(), language);
          if (questionLangBO == null) {
            questionLangBO = new QuestionLangBO();
            questionLangBO.setQuestionLangPK(
                new QuestionLangPK(questionnaireStepBean.getQuestionInstructionId(), language));
            questionLangBO.setQuestionnaireId(questionnaireId);
            questionLangBO.setFormId(questionnaireStepBean.getStepId());
            questionLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            questionLangBO.setCreatedBy(questionnairesStepsBo.getCreatedBy());
            studyQuestionnaireDAO.saveOrUpdateObject(questionLangBO);
          }
        }
      }
      questionLangBOS =
          studyQuestionnaireDAO.getQuestionLangByFormId(questionnaireId, formId, language);
    } catch (Exception e) {
      logger.error("StudyServiceImpl - getQuestionLangByFormId() - Error", e);
    }
    logger.info("StudyServiceImpl - getQuestionLangByFormId() - Ends");
    return questionLangBOS;
  }

  @Override
  public List<FormLangBO> getFormLangByQuestionnaireId(int questionnaireId, String language) {
    logger.info("StudyServiceImpl - getFormLangByQuestionnaireId() - Starts");
    List<FormLangBO> formLangBOS = null;
    try {
      formLangBOS = studyQuestionnaireDAO.getFormLangByQuestionnaireId(questionnaireId, language);
    } catch (Exception e) {
      logger.error("StudyServiceImpl - getFormLangByQuestionnaireId() - Error", e);
    }
    logger.info("StudyServiceImpl - getFormLangByQuestionnaireId() - Ends");
    return formLangBOS;
  }

  @Override
  public List<InstructionsLangBO> getInstructionLangByQuestionnaireId(
      int questionnaireId, String language) {
    logger.info("StudyServiceImpl - getInstructionLangByQuestionnaireId() - Starts");
    List<InstructionsLangBO> instructionsLangBOS = null;
    try {
      instructionsLangBOS =
          studyQuestionnaireDAO.getInstructionLangByQuestionnaireId(questionnaireId, language);
    } catch (Exception e) {
      logger.error("StudyServiceImpl - getInstructionLangByQuestionnaireId() - Error", e);
    }
    logger.info("StudyServiceImpl - getInstructionLangByQuestionnaireId() - Ends");
    return instructionsLangBOS;
  }

  public void updateStudyLangSequence(int questionnaireId, int studyId) {
    logger.info("StudyServiceImpl - updateStudyLangSequence() - Starts");
    try {
      List<StudySequenceLangBO> studySequenceLangBOList =
          studyDAO.getStudySequenceByStudyId(studyId);
      if (studySequenceLangBOList != null && studySequenceLangBOList.size() > 0) {
        for (StudySequenceLangBO studySequenceLangBO : studySequenceLangBOList) {
          studySequenceLangBO.setStudyExcQuestionnaries(false);
          studyDAO.saveOrUpdateObject(studySequenceLangBO);
        }
      }
      List<QuestionnaireLangBO> questionnaireLangBOList =
          studyQuestionnaireDAO.getQuestionnaireLangByQuestionnaireId(questionnaireId);
      for (QuestionnaireLangBO questionnaireLangBO : questionnaireLangBOList) {
        questionnaireLangBO.setStatus(false);
        studyDAO.saveOrUpdateObject(questionnaireLangBO);
      }
    } catch (Exception e) {
      logger.error("StudyServiceImpl - updateStudyLangSequence() - Error", e);
    }
    logger.info("StudyServiceImpl - updateStudyLangSequence() - Ends");
  }

 
@Override
public List<GroupsBo> getGroupsByStudyId(String studyId,String questionnaireId) {
	logger.info("StudyQuestionnaireServiceImpl - getGroupsByStudyId - Starts");
	List<GroupsBo> groups = null;
    
    try {
      groups = studyQuestionnaireDAO.getGroupsByStudyId(studyId,questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getGroupsByStudyId() - ERROR ", e);
    }
    logger.exit("getGroupsByStudyId() - Ends");
    return groups;
	
}

  @Override
  public GroupsBo getGroupsDetails(Integer id) {
    logger.info("UsersServiceImpl - getGroupsDetails() - Starts");
    GroupsBo groupsBo = null;
    try {
      groupsBo = studyQuestionnaireDAO.getGroupsDetails(id);
    } catch (Exception e) {
      logger.error("UsersServiceImpl - getGroupsDetails() - ERROR", e);
    }
    logger.info("UsersServiceImpl - getGroupsDetails() - Ends");
    return groupsBo;
  }

  @Override
  public String addOrUpdateGroupsDetails(GroupsBean groupsBean, SessionObject userSession) {
    logger.info("UsersServiceImpl - addOrUpdateGroupsDetails() - Starts");
    String msg = FdahpStudyDesignerConstants.FAILURE;
    boolean addFlag = false;
    String activity = "";
    String activitydetails = "";

    try {
      GroupsBo groupsBo = null;
      if(groupsBean.getId() != null ){
          groupsBo = studyQuestionnaireDAO.getGroupsDetails(groupsBean.getId());

    	  if(groupsBean.isAction() == true)
    	  {
    		  groupsBo.setAction(groupsBean.isAction());
    	  }
    	  else
    	  {
    		  groupsBo.setAction(groupsBean.isAction());

    	  }
      }
      
      if (groupsBo == null) {
        addFlag = true;
        groupsBo = new GroupsBo();
        groupsBo.setCreatedBy(userSession.getUserId());
        groupsBo.setCreatedOn(userSession.getCreatedDate());
        groupsBo.setAction(groupsBean.isAction());
        groupsBo.setQuestionnaireId(groupsBean.getQuestionnaireId());
        groupsBo.setStudyId(groupsBean.getStudyId());
        groupsBo.setDefaultVisibility(groupsBean.getDefaultVisibility());
        groupsBo.setGroupDefaultVisibility(Boolean.valueOf(groupsBean.getGroupDefaultVisibility()));
        groupsBo.setDestinationTrueAsGroup(groupsBean.getDestinationTrueAsGroup());
      } else {
          groupsBo.setModifiedBy(userSession.getUserId());
          groupsBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
      }
      groupsBo.setGroupName(null != groupsBean.getGroupName() ? groupsBean.getGroupName().trim() : "");
      groupsBo.setGroupId(groupsBean.getGroupId());
      studyQuestionnaireDAO.saveOrUpdateGroup(groupsBo);
      this.doGroupPreLoadLogic(groupsBean);
      if (addFlag) {
          activity = FdahpStudyDesignerConstants.SAVE_GROUP_SUCCESS_MESSAGE;
          activitydetails =
                  " groups saved";
        }
        if (!addFlag) {
          activity = FdahpStudyDesignerConstants.UPDATE_GROUP_SUCCESS_MESSAGE;
          activitydetails =
                  "updated group";
        }
      msg=auditLogDAO.saveToAuditLog(
              null,
              null,
              userSession,
              activity,
              activitydetails,
              "StudyQuestionnaireServiceImpl - addOrUpdateGroupsDetails");

    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - addOrUpdateGroupDetails() - ERROR", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - addOrUpdateGroupDetails() - Ends");
    return msg;
  }

@Override
public String deleteGroup(String id, SessionObject sessionObject) {
logger.info("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Starts");
	
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          studyQuestionnaireDAO.deleteGroup( id,sessionObject);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - deleteQuestionnaireStep - Ends");
    return message;
}


@Override
public String checkGroupId(String questionnaireId, String groupId, String studyId) {
  logger.info("StudyQuestionnaireServiceImpl - checkGroupId() - Starts");
  String message = FdahpStudyDesignerConstants.FAILURE;
  try {
    message = studyQuestionnaireDAO.checkGroupId(questionnaireId, groupId,studyId);
  } catch (Exception e) {
    logger.error("StudyQuestionnaireServiceImpl - checkGroupId() - Error", e);
  }
  logger.info("StudyQuestionnaireServiceImpl - checkGroupId() - Ends");
  return message;
}

@Override
public String checkGroupName(String questionnaireId, String groupName, String studyId) {
	logger.info("StudyQuestionnaireServiceImpl - checkGroupName() - Starts");
	  String message = FdahpStudyDesignerConstants.FAILURE;
	  try {
	    message = studyQuestionnaireDAO.checkGroupName(questionnaireId, groupName,studyId);
	  } catch (Exception e) {
	    logger.error("StudyQuestionnaireServiceImpl - checkGroupName() - Error", e);
	  }
	  logger.info("StudyQuestionnaireServiceImpl - checkGroupName() - Ends");
	  return message;
}

  @Override
  public List<GroupMappingBo> assignQuestionSteps(List<String> arr, Integer grpId, String questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - assignQuestionsteps - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    List<GroupMappingBo> groupMappingBo = new ArrayList<>();;
    try {
      groupMappingBo = studyQuestionnaireDAO.assignQuestionSteps(arr,grpId,questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - assignQuestionSteps() - ERROR ", e);
    }
    logger.exit("assignQuestionSteps() - Ends");
    return groupMappingBo;
  }

  @Override
  public List<QuestionnairesStepsBo> getSameSurveySourceKeys(int queId, int seq, String caller) {
    return studyQuestionnaireDAO.getSameSurveySourceKeys(queId, seq, caller);
  }

  @Override
  public List<QuestionnaireBo> getQuestionnairesForPiping(String queId, String studyId, boolean isLive){
    return studyQuestionnaireDAO.getQuestionnairesForPiping(queId, studyId, isLive);
  }

  @Override
  public void saveOrUpdatePipingData(QuestionnaireStepBean questionnaireStepBean) {
    studyQuestionnaireDAO.saveOrUpdatePipingData(questionnaireStepBean);
  }

  @Override
  public List<GroupMappingBo> getStepId(String id, String questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - getStepId() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    List<GroupMappingBo> stepId = new ArrayList<>();;
    try {
      stepId = studyQuestionnaireDAO.getStepId(id, questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getStepId() - ERROR ", e);
    }
    logger.exit("getStepId() - Ends");
    return stepId;
  }

  @Override
  public GroupMappingBo getStepDetails(String id, String questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - getStepId() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    GroupMappingBo stepId = null;
    try {
      stepId = studyQuestionnaireDAO.getStepDetails(id, questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - getStepId() - ERROR ", e);
    }
    logger.exit("getStepId() - Ends");
    return stepId;
  }

  @Override
  public String groupFlagDisable(List<GroupMappingBo> groupMappingBo, String questionnaireId) {
    logger.info("StudyQuestionnaireServiceImpl - groupFlagDisable() - Starts");
    String result = FdahpStudyDesignerConstants.FAILURE;
    try {
      result = studyQuestionnaireDAO.groupFlagDisable(groupMappingBo, questionnaireId);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - groupFlagDisable() - ERROR ", e);
    }
    logger.exit("groupFlagDisable() - Ends");
    return result;  }

  @Override
  public  List<GroupMappingStepBean> getGroupsAssignedList(Integer grpId,List<GroupMappingBo> groupMappingBo,List<GroupMappingStepBean> groupMappingBeans) {
  	List<GroupMappingStepBean> groupMappingBeanss = null;
  	groupMappingBeanss = studyQuestionnaireDAO.getGroupsList(grpId,groupMappingBo, groupMappingBeans);
  	return groupMappingBeanss;
  }

  @Override
  public List<GroupMappingBo> getAssignSteps(int grpId) {
      logger.info("StudyQuestionnaireServiceImpl - getAssignSteps - Starts");
      List<GroupMappingBo> getAssignSteps = studyQuestionnaireDAO.getAssignSteps(grpId);

  	return getAssignSteps;
  }


  @Override
  public String deleteGroupMaprecords(String id) {
    logger.info("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Starts");

    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
              studyQuestionnaireDAO.deleteGroupMaprecords(id);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Ends");
    return message;
  }

@Override
public String stepFlagDisable(GroupMappingBo groupMappingBo, String questionnaireId) {
	 logger.info("StudyQuestionnaireServiceImpl - groupFlagDisable() - Starts");
	    String result = FdahpStudyDesignerConstants.FAILURE;
	    try {
	      result = studyQuestionnaireDAO.stepFlagDisable(groupMappingBo, questionnaireId);
	    } catch (Exception e) {
	      logger.error("StudyQuestionnaireServiceImpl - groupFlagDisable() - ERROR ", e);
	    }
	    logger.exit("groupFlagDisable() - Ends");
	    return result;
}

@Override
public String deleteStepMaprecords(String id) {
	logger.info("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Starts");

    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
              studyQuestionnaireDAO.deleteStepMaprecords(id);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Error", e);
    }
    logger.info("StudyQuestionnaireServiceImpl - deleteGroupMaprecords() - Ends");
    return message;
}

  @Override
  public Boolean isPreloadLogicAndPipingEnabled(Integer queId) {
    return studyQuestionnaireDAO.isPreloadLogicAndPipingEnabled(queId);
  }
}
