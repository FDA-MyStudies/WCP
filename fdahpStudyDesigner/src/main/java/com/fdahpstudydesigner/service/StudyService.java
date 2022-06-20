package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bean.StudyIdBean;
import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bean.StudyPageBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.List;
import java.util.Map;

public interface StudyService {

  public String checkActiveTaskTypeValidation(Integer studyId);

  public int comprehensionTestQuestionOrder(Integer studyId);

  public int consentInfoOrder(Integer studyId);

  public boolean copyliveStudyByCustomStudyId(String customStudyId, SessionObject sesObj);

  public String deleteComprehensionTestQuestion(
      Integer questionId, Integer studyId, SessionObject sessionObject);

  public String deleteConsentInfo(
      Integer consentInfoId, Integer studyId, SessionObject sessionObject, String customStudyId);

  public String deleteEligibilityTestQusAnsById(
      Integer eligibilityTestId,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId);

  public String deleteOverviewStudyPageById(String studyId, String pageId);

  public String deleteResourceInfo(
      Integer resourceInfoId, SessionObject sesObj, String customStudyId, int studyId);

  public boolean deleteStudyByCustomStudyId(String customStudyId);

  public List<UserBO> getActiveNonAddedUserList(Integer studyId, Integer userId);

  public List<StudyPermissionBO> getAddedUserListToStudy(Integer studyId, Integer userId);

  public List<StudyBo> getAllStudyList();

  List<VersionInfo> getVersionInfoList();

  public Checklist getchecklistInfo(Integer studyId);

  public ComprehensionTestQuestionBo getComprehensionTestQuestionById(Integer questionId);

  public List<ComprehensionTestQuestionBo> getComprehensionTestQuestionList(Integer studyId);

  List<ComprehensionQuestionLangBO> getComprehensionTestQuestionLangList(
      List<ComprehensionTestQuestionBo> questionBoList, String language);

  public List<ComprehensionTestResponseBo> getComprehensionTestResponseList(
      Integer comprehensionQuestionId);

  public ConsentBo getConsentDetailsByStudyId(String studyId);

  public ConsentInfoBo getConsentInfoById(Integer consentInfoId);

  public List<ConsentInfoBo> getConsentInfoDetailsListByStudyId(String studyId);

  public List<ConsentInfoBo> getConsentInfoList(Integer studyId);

  public List<ConsentMasterInfoBo> getConsentMasterInfoList();

  public StudyIdBean getLiveVersion(String customStudyId);

  public List<StudyPageBo> getOverviewStudyPagesById(String studyId, Integer userId);

  public Map<String, List<ReferenceTablesBo>> getreferenceListByCategory();

  public ResourceBO getResourceInfo(Integer resourceInfoId);

  public List<ResourceBO> getResourceList(Integer studyId);

  List<ResourcesLangBO> syncAndGetResourceLangList(
      List<ResourceBO> resourceBOList, Integer studyId, String language);

  public List<NotificationBO> getSavedNotification(Integer studyId);

  public StudyBo getStudyById(String studyId, Integer userId);

  public EligibilityBo getStudyEligibiltyByStudyId(String studyId);

  public List<StudyListBean> getStudyList(Integer userId);

  public List<StudyListBean> getStudyListByUserId(Integer userId);

  public StudyBo getStudyLiveStatusByCustomId(String customStudyId);

  public ResourceBO getStudyProtocol(Integer studyId);

  public String markAsCompleted(
      int studyId, String markCompleted, Boolean flag, SessionObject sesObj, String customStudyId);

  public String markAsCompleted(
      int studyId,
      String markCompleted,
      SessionObject sesObj,
      String customStudyId,
      String language);

  public String reOrderComprehensionTestQuestion(
      Integer studyId, int oldOrderNumber, int newOrderNumber);

  public String reOrderConsentInfoList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  public String reorderEligibilityTestQusAns(
      Integer eligibilityId, int oldOrderNumber, int newOrderNumber, Integer studyId);

  public String reOrderResourceList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  public boolean resetDraftStudyByCustomStudyId(String customStudyId);

  public int resourceOrder(Integer studyId);

  public List<ResourceBO> resourcesSaved(Integer studyId);

  public List<ResourceBO> resourcesWithAnchorDate(Integer studyId);

  public ConsentBo saveOrCompleteConsentReviewDetails(
      ConsentBo consentBo, SessionObject sesObj, String customStudyId, String language);

  public Integer saveOrDoneChecklist(
      Checklist checklist, String actionBut, SessionObject sesObj, String customStudyId);

  public ComprehensionTestQuestionBo saveOrUpdateComprehensionTestQuestion(
      ComprehensionTestQuestionBo comprehensionTestQuestionBo, String language);

  public ConsentInfoBo saveOrUpdateConsentInfo(
      ConsentInfoBo consentInfoBo,
      SessionObject sessionObject,
      String customStudyId,
      String language);

  public Integer saveOrUpdateEligibilityTestQusAns(
      EligibilityTestBo eligibilityTestBo,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId,
      String language);

  public String saveOrUpdateOverviewStudyPages(
      StudyPageBean studyPageBean, SessionObject sesObj, String language);

  public Integer saveOrUpdateResource(ResourceBO resourceBO, SessionObject sesObj, String language);

  public String saveOrUpdateStudy(
      StudyBo studyBo, Integer userId, SessionObject sessionObject, String language);

  public ParticipantPropertiesBO saveOrUpdateParticipantProperties(
      ParticipantPropertiesBO participantPropertiesBO);

  public List<ParticipantPropertiesBO> getParticipantProperties(String customStudyId);

  public ParticipantPropertiesBO getParticipantProperty(
      String participantPropertyId, String customStudyId);

  public String deactivateParticipantProperty(int participantPropertyId, int userId);

  public String deleteParticipantProperty(int participantPropertyId, int userId);

  public String checkParticipantPropertyShortTitle(String shortTitle, String customStudyId);

  public String saveOrUpdateStudyEligibilty(
      EligibilityBo eligibilityBo, SessionObject sesObj, String customStudyId, String language);

  public String saveOrUpdateStudySettings(
      StudyBo studyBo,
      SessionObject sesObj,
      String userIds,
      String permissions,
      String projectLead,
      String newLanguages,
      String deletedLanguages,
      String currLang);

  String removeExistingLanguageAndData(
      String studyId,
      SessionObject sesObj,
      String langToBeDeleted,
      String newLanguages,
      String mlFlag);

  public String updateStudyActionOnAction(String studyId, String buttonText, SessionObject sesObj);

  public String switchStudyToLiveMode(String studyId);

  public String validateActivityComplete(String studyId, String action);

  public String validateParticipantPropertyComplete(String customStudyId);

  public String validateEligibilityTestKey(
      Integer eligibilityTestId, String shortTitle, Integer eligibilityId);

  public String validateStudyAction(String studyId, String buttonText);

  public boolean validateStudyId(String studyId);

  public List<EligibilityTestBo> viewEligibilityTestQusAnsByEligibilityId(Integer eligibilityId);

  public EligibilityTestBo viewEligibilityTestQusAnsById(Integer eligibilityTestId);

  public Boolean isAnchorDateExistForEnrollment(Integer studyId, String customStudyId);

  public Boolean isAnchorDateExistForEnrollmentDraftStudy(Integer studyId, String customStudyId);

  public boolean validateAppId(String customStudyId, String appId, String studyType);

  public StudyPermissionBO findStudyPermissionBO(int studyId, int userId);

  public StudyLanguageBO getStudyLanguageById(int studyId, String language);

  StudySequenceLangBO getStudySequenceById(int studyId, String language);

  public List<ConsentInfoLangBO> syncConsentDataInLanguageTable(
      List<ConsentInfoBo> consentInfoList, String language);

  ConsentInfoLangBO getConsentInfoLangById(int consentInfoId, String language);

  List<ConsentInfoLangBO> getConsentInfoLangByStudyId(int studyId, String language);

  ComprehensionQuestionLangBO getComprehensionQuestionLangById(int questionId, String language);

  String syncQuestionDataInLanguageTables(
      ComprehensionTestQuestionBo comprehensionTestQuestionBo, String language);

  List<EligibilityTestLangBo> syncEligibilityTestDataInLanguageTable(
      List<EligibilityTestBo> eligibilityTestList, String language, String studyId);

  EligibilityTestLangBo getEligibilityTestLangById(int eligibilityId, String language);

  List<StudyPageLanguageBO> getOverviewStudyPagesLangById(
      List<StudyPageBo> studyPageBos, String language);

  ResourcesLangBO getResourceLangInfo(String resourceInfoId, String language);

  Map<String, Boolean> isLanguageDeletable(String customStudyId);

  String forceUpgradeApp(String appId, String androidUpdateType, String iosUpdateType);
}
