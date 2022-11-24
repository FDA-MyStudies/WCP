/** */
package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bean.FormulaInfoBean;
import com.fdahpstudydesigner.bean.GroupMappingStepBean;
import com.fdahpstudydesigner.bean.GroupsBean;
import com.fdahpstudydesigner.bean.QuestionnaireStepBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.SessionObject;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/** @author BTC */
public interface StudyQuestionnaireService {
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
      String customStudyId,
      Integer deletionId);

  public String deletQuestionnaire(
      Integer studyId, Integer questionnaireId, SessionObject sessionObject, String customStudyId);

  public List<HealthKitKeysInfo> getHeanlthKitKeyInfoList();

  public InstructionsBo getInstructionsBo(
      Integer instructionId,
      String questionnaireShortTitle,
      String customStudyId,
      Integer questionnaireId);

  public QuestionnaireBo getQuestionnaireById(Integer questionnaireId, String customStudyId);

  QuestionnaireLangBO getQuestionnaireLangById(int questionnaireId, String language);

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

  public QuestionnairesStepsBo saveOrUpdateFromStepQuestionnaire(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sesObj,
      String customStudyId,
      String studyId);

  public InstructionsBo saveOrUpdateInstructionsBo(
      InstructionsBo instructionsBo,
      SessionObject sessionObject,
      String customStudyId,
      String language,
      String studyId,
      QuestionnaireStepBean pipingBean);

  public QuestionsBo saveOrUpdateQuestion(
      QuestionsBo questionsBo,
      SessionObject sesObj,
      String customStudyId,
      String language,
      String studyId);

  public QuestionnaireBo saveOrUpdateQuestionnaire(
      QuestionnaireBo questionnaireBo,
      SessionObject sessionObject,
      String customStudyId,
      String language);

  public QuestionnaireBo saveOrUpdateQuestionnaireSchedule(
      QuestionnaireBo questionnaireBo, SessionObject sessionObject, String customStudyId);

  public QuestionnairesStepsBo saveOrUpdateQuestionStep(
      QuestionnairesStepsBo questionnairesStepsBo,
      SessionObject sessionObject,
      String customStudyId,
      String language,
      String studyId,
      QuestionnaireStepBean pipingBean);

  public String validateLineChartSchedule(Integer questionnaireId, String frequency);

  public FormulaInfoBean validateQuestionConditionalBranchingLogic(
      String lhs, String rhs, String operator, String input);

  public String validateRepetableFormQuestionStats(Integer formId);

  public String checkUniqueAnchorDateName(
      String anchordateText, String customStudyId, String anchorDateId);

  public List<AnchorDateTypeBo> getAnchorTypesByStudyId(String customStudyId);

  public boolean isAnchorDateExistByQuestionnaire(Integer questionnaireId);

  InstructionsLangBO getInstructionLangBO(int instructionId, String language);

  FormLangBO getFormLangBO(int formId, String language);

  QuestionLangBO getQuestionLangBO(int id, String language);

  String saveOrUpdateFormStepForOtherLanguages(
      QuestionnairesStepsBo questionnairesStepsBo, String language, String studyId);

  List<String> syncAndGetLangData(
      Map<Integer, QuestionnaireStepBean> qTreeMap,
      int questionnaireId,
      String language,
      int userId);

  List<QuestionnaireLangBO> syncAndGetQuestionnaireLangList(
      List<QuestionnaireBo> boList, Integer studyId, String language);

  List<QuestionLangBO> getQuestionLangByQuestionnaireId(int questionnaireId, String language);

  List<QuestionLangBO> syncAndGetQuestionLangByFormId(
      QuestionnairesStepsBo questionnairesStepsBo,
      int questionnaireId,
      int formId,
      String language);

  List<FormLangBO> getFormLangByQuestionnaireId(int questionnaireId, String language);

  List<InstructionsLangBO> getInstructionLangByQuestionnaireId(
      int questionnaireId, String language);
  
  List<GroupsBo> getGroupsByStudyId(String studyId,String questionnaireId, boolean isStep, Integer stepId);

  List<GroupsBo> getGroupsForPreloadAndPostLoad(String questionnaireId, String queIdForGroups, Integer stepId, boolean isPreload);

  List<GroupsBo> getGroupsListForGroupsPage(String questionnaireId, int groupId);

  public GroupsBo getGroupsDetails(Integer id);

  public String addOrUpdateGroupsDetails(
          GroupsBean groupsBean, SessionObject userSession);
  
  public String deleteGroup(String id,SessionObject sessionObject);

String checkGroupId(String questionnaireId, String groupId, String studyId);

String checkGroupName(String questionnaireId, String groupName, String studyId);

  public List<GroupMappingBo> assignQuestionSteps(List<String> arr, Integer grpId, String questionnaireId);

  List<QuestionnairesStepsBo> getSameSurveySourceKeys(int queId, int seq, String caller, Integer stepId, Integer currQuestionnaireId);

  List<QuestionnairesStepsBo> getStepsForGroups(int queId, int groupId);

  List<QuestionnairesStepsBo> getPostLoadDestinationKeys(Integer currQuestionnaireId, int seq, Integer stepId);

  List<QuestionnaireBo> getQuestionnairesForPiping(String queId, String studyId, boolean isLive);

  void saveOrUpdatePipingData(QuestionnaireStepBean questionnaireStepBean);

  public List<GroupMappingBo> getStepId(String id, String questionnaireId);

  public String groupFlagDisable(List<GroupMappingBo> groupMappingBo, String questionnaireId);

  public List<GroupMappingStepBean> getGroupsAssignedList(Integer grpId);

  public String deleteGroupMaprecords(String id);

GroupMappingBo getStepDetails(String id, String questionnaireId);

public String stepFlagDisable(GroupMappingBo groupMappingBo, String questionnaireId);

public String deleteStepMaprecords(String id,String questionnaireId);

  List<PreLoadLogicBo> getPreLoadLogicDetails(Integer id);

  Boolean isPreloadLogicAndPipingEnabled(Integer queId);

  String deleteStepBasedOnStepId(String stepId);

  boolean isQuestionMultiSelect(int queId);

  Integer getGroupId(String stepId);

  boolean isLastFormQuestion(String formId, String questionId);

  GroupsBo getGroupIdBySendingQuestionStepId(Integer questionStepId);

  String getStepType(Integer questionStepId, Integer stepId);

  Integer getResponseType(Integer questionStepId);

  List<Integer> getQuestionIdList(Integer questionStepId);

  Integer getResponseTypeForFormStep(Integer lastQuestinObjectValue);

  List<GroupsBo> getGroupListBySendingQuestionStepId(String studyId, String questionnaireId, Integer questionStepId);

  List<FormMappingBo> getListOfQuestions(Integer formId);

  public String getTextChoiceSelectionStyle(Integer questionStepId);

}
