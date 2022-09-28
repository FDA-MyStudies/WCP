package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bean.GroupsBean;
import com.fdahpstudydesigner.bean.QuestionnaireStepBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.List;
import java.util.SortedMap;
import org.hibernate.Session;
import org.hibernate.Transaction;

public interface StudyQuestionnaireDAO {
  public String checkFromQuestionShortTitle(
      Integer questionnaireId,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId);

  public String checkQuestionnaireResponseTypeValidation(Integer studyId, String customStudyId);

  public String checkQuestionnaireShortTitle(
      Integer studyId, String shortTitle, String customStudyId);

  public String checkQuestionnaireStepShortTitle(
      Integer questionnaireId,
      String stepType,
      String shortTitle,
      String questionnaireShortTitle,
      String customStudyId);

  public String checkStatShortTitle(Integer studyId, String shortTitle, String customStudyId);

  public QuestionnaireBo copyStudyQuestionnaireBo(
      Integer questionnaireId, String customStudyId, SessionObject sessionObject);

  public String deleteFromStepQuestion(
      Integer formId, Integer questionId, SessionObject sessionObject, String customStudyId);

  public String deleteQuestionnaireStep(
      Integer stepId,
      Integer questionnaireId,
      String stepType,
      SessionObject sessionObject,
      String customStudyId);

  public String deleteQuestuionnaireInfo(
      Integer studyId, Integer questionnaireId, SessionObject sessionObject, String customStudyId);

  public List<HealthKitKeysInfo> getHeanlthKitKeyInfoList();

  public InstructionsBo getInstructionsBo(
      Integer instructionId,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId);

  public List<QuestionConditionBranchBo> getQuestionConditionalBranchingLogic(
      Session session, Integer questionId);

  public QuestionnaireBo getQuestionnaireById(Integer questionnaireId, String customStudyId);

  public List<QuestionnairesStepsBo> getQuestionnairesStepsList(
      Integer questionnaireId, Integer sequenceNo);

  public QuestionnairesStepsBo getQuestionnaireStep(
      Integer stepId,
      String stepType,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId);

  public SortedMap<Integer, QuestionnaireStepBean> getQuestionnaireStepList(
      Integer questionnaireId);

  public List<QuestionResponseTypeMasterInfoBo> getQuestionReponseTypeList();

  public QuestionsBo getQuestionsById(
      Integer questionId, String questionnaireShortTitle, String customStudyId);

  public List<QuestionnaireBo> getStudyQuestionnairesByStudyId(String studyId, Boolean isLive);

  public Boolean isAnchorDateExistsForStudy(Integer studyId, String customStudyId);

  public Boolean isQuestionnairesCompleted(Integer studyId);

  public String reOrderFormStepQuestions(Integer formId, int oldOrderNumber, int newOrderNumber);

  public String reOrderQuestionnaireSteps(
      Integer questionnaireId, int oldOrderNumber, int newOrderNumber);

  public QuestionnairesStepsBo saveOrUpdateFromQuestionnaireStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sesObj,
      String customStudyId,
      String studyId);

  public InstructionsBo saveOrUpdateInstructionsBo(
      InstructionsBo instructionsBo, SessionObject sessionObject, String customStudyId);

  public QuestionsBo saveOrUpdateQuestion(QuestionsBo questionsBo);

  public QuestionnaireBo saveORUpdateQuestionnaire(
      QuestionnaireBo questionnaireBo, SessionObject sessionObject, String customStudyId);

  public QuestionnairesStepsBo saveOrUpdateQuestionStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sessionObject,
      String customStudyId);

  public String validateLineChartSchedule(Integer questionnaireId, String frequency);

  public String validateRepetableFormQuestionStats(Integer formId);

  public String checkUniqueAnchorDateName(
      String anchordateText, String customStudyId, String anchorDateId);

  public Integer getStudyIdByCustomStudy(Session session, String customStudyId);

  public List<AnchorDateTypeBo> getAnchorTypesByStudyId(String customStudyId);

  public boolean isAnchorDateExistByQuestionnaire(Integer questionnaireId);

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
      boolean isChange);

  void saveOrUpdateObject(Object object);

  InstructionsLangBO getInstructionLangBo(int instructionId, String language);

  FormLangBO getFormLangBo(int formId, String language);

  QuestionLangBO getQuestionLangBo(int id, String language);

  QuestionnaireLangBO getQuestionnaireLangById(Integer questionnaireId, String language);

  List<QuestionnaireLangBO> getQuestionnaireLangByStudyId(Integer studyId, String language);

  List<QuestionnaireLangBO> getQuestionnaireLangByQuestionnaireId(int studyId);

  List<QuestionLangBO> getQuestionLangByQuestionnaireId(Integer questionnaireId, String language);

  List<QuestionLangBO> getQuestionLangByFormId(int questionnaireId, int formId, String language);

  List<FormLangBO> getFormLangByQuestionnaireId(Integer questionnaireId, String language);

  List<InstructionsLangBO> getInstructionLangByQuestionnaireId(
      Integer questionnaireId, String language);

  void deleteQuestionStep(int id, String language);
  
public List<GroupsBo> getGroupsByStudyId(String studyId,String questionnaireId);

  public GroupsBo getGroupsDetails(int id);

  public String addOrUpdateGroupsDetails(
          GroupsBo groupsBo);

  public GroupsBo getGroupById(int id);
  
  public String deleteGroup(String id,SessionObject sessionObject);

  List<PreLoadLogicBo> getPreLoadLogicByStep(Integer stepId);

  List<Integer> getPreLoadIds(Integer stepId);
  
  public String checkGroupId(String questionnaireId, String groupId, String studyId);


  PreLoadLogicBo getPreLoadLogicById(Integer id);

  void deleteFormula(List<Integer> ids);
  
  public String checkGroupName(String questionnaireId, String groupName, String studyId);

  String saveOrUpdateGroup(GroupsBo groupsBO);

  List<GroupMappingBo> assignQuestionSteps(List<String> arr, Integer grpId, String questionnaireId);

  List<QuestionnairesStepsBo> getSameSurveySourceKeys(int queId, int seq);

  List<QuestionnaireBo> getQuestionnairesForPiping(String queId, String studyId);

  void saveOrUpdatePipingData(QuestionnaireStepBean questionnaireStepBean);
}
