package com.fdahpstudydesigner.dao;

import com.fdahpstudydesigner.bean.StudyIdBean;
import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bean.StudyPageBean;
import com.fdahpstudydesigner.bo.*;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.HashMap;
/** @author BTC */
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.Transaction;

public interface StudyDAO {

  String checkActiveTaskTypeValidation(Integer studyId);

  int comprehensionTestQuestionOrder(Integer studyId);

  int consentInfoOrder(Integer studyId);

  String deleteComprehensionTestQuestion(
      Integer questionId, Integer studyId, SessionObject sessionObject);

  String deleteConsentInfo(
      Integer consentInfoId, Integer studyId, SessionObject sessionObject, String customStudyId);

  String deleteEligibilityTestQusAnsById(
      Integer eligibilityTestId,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId);

  boolean deleteLiveStudy(String customStudyId);

  String deleteOverviewStudyPageById(String studyId, String pageId);

  String deleteResourceInfo(Integer resourceInfoId, boolean resourceVisibility, int studyId);

  boolean deleteStudyByCustomStudyId(String customStudyId);

  String deleteStudyByIdOrCustomstudyId(
      Session session, Transaction transaction, String studyId, String customStudyId);

  int eligibilityTestOrderCount(Integer eligibilityId);

  List<UserBO> getActiveNonAddedUserList(Integer studyId, Integer userId);

  List<StudyPermissionBO> getAddedUserListToStudy(Integer studyId, Integer userId);

  List<StudyBo> getAllStudyList();

  List<VersionInfo> getVersionInfoList();

  Checklist getchecklistInfo(Integer studyId);

  ComprehensionTestQuestionBo getComprehensionTestQuestionById(Integer questionId);

  List<ComprehensionTestQuestionBo> getComprehensionTestQuestionList(Integer studyId);

  List<ComprehensionQuestionLangBO> getComprehensionTestQuestionLangList(
      Integer studyId, String language);

  List<ComprehensionTestResponseBo> getComprehensionTestResponseList(
      Integer comprehensionQuestionId);

  ConsentBo getConsentDetailsByStudyId(String studyId);

  ConsentInfoBo getConsentInfoById(Integer consentInfoId);

  List<ConsentInfoBo> getConsentInfoDetailsListByStudyId(String studyId);

  List<ConsentInfoBo> getConsentInfoList(Integer studyId);

  List<ConsentMasterInfoBo> getConsentMasterInfoList();

  StudyIdBean getLiveVersion(String customStudyId);

  NotificationBO getNotificationByResourceId(Integer resourseId);

  List<StudyPageBo> getOverviewStudyPagesById(String studyId, Integer userId);

  HashMap<String, List<ReferenceTablesBo>> getreferenceListByCategory();

  ResourceBO getResourceInfo(Integer resourceInfoId);

  List<ResourceBO> getResourceList(Integer studyId);

  List<NotificationBO> getSavedNotification(Integer studyId);

  StudyBo getStudyById(String studyId, Integer userId);

  EligibilityBo getStudyEligibiltyByStudyId(String studyId);

  List<StudyListBean> getStudyList(Integer userId);

  List<StudyListBean> getStudyListByUserId(Integer userId);

  StudyBo getStudyLiveStatusByCustomId(String customStudyId);

  ResourceBO getStudyProtocol(Integer studyId);

  String markAsCompleted(
      int studyId, String markCompleted, boolean flag, SessionObject sesObj, String customStudyId);

  String reOrderComprehensionTestQuestion(Integer studyId, int oldOrderNumber, int newOrderNumber);

  String reOrderConsentInfoList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  String reorderEligibilityTestQusAns(
      Integer eligibilityId, int oldOrderNumber, int newOrderNumber, Integer studyId);

  String reOrderResourceList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  boolean resetDraftStudyByCustomStudyId(String customStudyId, String action, SessionObject sesObj);

  int resourceOrder(Integer studyId);

  List<ResourceBO> resourcesSaved(Integer studyId);

  List<ResourceBO> resourcesWithAnchorDate(Integer studyId);

  ConsentBo saveOrCompleteConsentReviewDetails(
      ConsentBo consentBo, SessionObject sesObj, String customStudyId);

  Integer saveOrDoneChecklist(Checklist checklist);

  ComprehensionTestQuestionBo saveOrUpdateComprehensionTestQuestion(
      ComprehensionTestQuestionBo comprehensionTestQuestionBo);

  ConsentInfoBo saveOrUpdateConsentInfo(
      ConsentInfoBo consentInfoBo, SessionObject sesObj, String customStudyId);

  Integer saveOrUpdateEligibilityTestQusAns(
      EligibilityTestBo eligibilityTestBo,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId);

  String saveOrUpdateOverviewStudyPages(StudyPageBean studyPageBean, SessionObject sesObj);

  Integer saveOrUpdateResource(ResourceBO resourceBO);

  String saveOrUpdateStudy(StudyBo studyBo, SessionObject sessionObject);

  String saveOrUpdateStudyForOtherLanguages(
      StudyBo studyBo, StudyLanguageBO studyLanguageBO, int userId, String language);

  ParticipantPropertiesBO saveOrUpdateParticipantProperties(
      ParticipantPropertiesBO participantPropertiesBO);

  Integer addAnchorDate(int studyId, String customStudyId, String anchorDateName, Session session);

  void updateParticipantPropertyAsAnchorDate(
      Integer anchorDateId, String anchorDateName, Session session);

  void deleteParticipantPropertyAsAnchorDate(Integer anchorDateId, Session session);

  List<ParticipantPropertiesBO> getParticipantProperties(String customStudyId);

  ParticipantPropertiesBO getParticipantProperty(
      String participantPropertyId, String customStudyId);

  String deactivateParticipantProperty(int participantPropertyId, int userId);

  String deleteParticipantProperty(int participantPropertyId, int userId);

  String checkParticipantPropertyShortTitle(String shortTitle, String customStudyId);

  String saveOrUpdateStudyEligibilty(
      EligibilityBo eligibilityBo, SessionObject sesObj, String customStudyId);

  String saveOrUpdateStudySettings(
      StudyBo studyBo,
      SessionObject sesObj,
      String userIds,
      String permissions,
      String projectLead,
      String newLanguages,
      String deletedLanguages);

  String saveOrUpdateStudySettingsForOtherLanguages(StudyBo studyBo, String currLang);

  String saveResourceNotification(NotificationBO notificationBO, boolean notiFlag);

  String updateStudyActionOnAction(String studyId, String buttonText, SessionObject sesObj);

  String switchStudyToLiveMode(String studyId);

  void updateStudyId(StudyBo studyBo, String newStudyId, Session session);

  void updateAppId(String customStudyId, String appId, String orgId, Session session);

  String validateActivityComplete(String studyId, String action);

  String validateParticipantPropertyComplete(String customStudyId);

  String validateEligibilityTestKey(
      Integer eligibilityTestId, String shortTitle, Integer eligibilityId);

  String validateStudyAction(String studyId, String buttonText);

  boolean validateStudyId(String studyId);

  List<EligibilityTestBo> viewEligibilityTestQusAnsByEligibilityId(Integer eligibilityId);

  EligibilityTestBo viewEligibilityTestQusAnsById(Integer eligibilityTestId);

  Boolean isAnchorDateExistForEnrollment(Integer studyId, String customStudyId);

  Boolean isAnchorDateExistForEnrollmentDraftStudy(Integer studyId, String customStudyId);

  String updateAnchordateForEnrollmentDate(
      StudyBo oldStudyBo, StudyBo updatedStudyBo, Session session, Transaction transaction);

  boolean validateAppId(String customStudyId, String appId, String studyType);

  StudyPermissionBO getStudyPermissionBO(int studyId, int userId);

  StudyLanguageBO getStudyLanguageById(int studyId, String language);

  ConsentInfoLangBO getConsentLanguageDataById(int id, String language);

  void saveOrUpdateConsentInfoLanguageData(ConsentInfoLangBO consentInfoLangBO);

  List<ConsentInfoLangBO> getConsentLangInfoByStudyId(int studyId, String language);

  ComprehensionQuestionLangBO getComprehensionQuestionLangById(int questionId, String language);

  void saveOrUpdateComprehensionQuestionLanguageData(
      ComprehensionQuestionLangBO comprehensionQuestionLangBO, boolean deleteExisting);

  void saveOrUpdateObject(Object object);

  String saveOrUpdateStudyEligibiltyForOtherLanguages(
      EligibilityBo eligibilityBo, StudyLanguageBO studyLanguageBO, String language);

  Integer saveOrUpdateStudyEligibiltyTestQusForOtherLanguages(
      EligibilityTestLangBo eligibilityTestLangBo);

  EligibilityTestLangBo getEligibilityTestLanguageDataById(int id, String language);

  List<EligibilityTestLangBo> getEligibilityTestLangByStudyId(int studyId, String language);

  String saveOrUpdateOverviewLanguageStudyPages(
      StudyPageBean studyPageBean, SessionObject sesObj, String language);

  List<StudyPageLanguageBO> getOverviewStudyPagesLangDataById(int studyId, String language);

  StudyPageLanguageBO getStudyPageLanguageById(int pageId, String language);

  StudySequenceLangBO getStudySequenceLangBO(int studyId, String language);

  List<StudySequenceLangBO> getStudySequenceByStudyId(int studyId);

  ResourcesLangBO getResourceLangBO(int id, String language);

  String deleteAllLanguageData(int id, String language);

  StudyBo getStudyBoById(String studyId);

  List<ResourcesLangBO> getResourcesLangList(int studyId, String language);

  Map<String, Boolean> isLanguageDeletable(String customStudyId);

  String updateDraftStatusInStudyBo(int userId, int studyId);

  String forceUpgradeApp(String appId, String androidUpdateType, String iosUpdateType);
}
