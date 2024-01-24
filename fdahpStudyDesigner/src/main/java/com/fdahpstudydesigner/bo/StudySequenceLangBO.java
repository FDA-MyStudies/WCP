package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "study_sequence_lang")
public class StudySequenceLangBO implements Serializable {

  @EmbeddedId private StudySequenceLangPK studySequenceLangPK;

  @Column(name = "actions")
  @Type(type = "yes_no")
  private boolean actions = false;

  @Column(name = "basic_info")
  @Type(type = "yes_no")
  private boolean basicInfo = false;

  @Column(name = "check_list")
  @Type(type = "yes_no")
  private boolean checkList = false;

  @Column(name = "comprehension_test")
  @Type(type = "yes_no")
  private boolean comprehensionTest = false;

  @Column(name = "consent_edu_info")
  @Type(type = "yes_no")
  private boolean consentEduInfo = false;

  @Column(name = "e_consent")
  @Type(type = "yes_no")
  private boolean eConsent = false;

  @Column(name = "eligibility")
  @Type(type = "yes_no")
  private boolean eligibility = false;

  @Column(name = "miscellaneous_branding")
  @Type(type = "yes_no")
  private boolean miscellaneousBranding = false;

  @Column(name = "miscellaneous_notification")
  @Type(type = "yes_no")
  private boolean miscellaneousNotification = false;

  @Column(name = "miscellaneous_resources")
  @Type(type = "yes_no")
  private boolean miscellaneousResources = false;

  @Column(name = "over_view")
  @Type(type = "yes_no")
  private boolean overView = false;

  @Column(name = "setting_admins")
  @Type(type = "yes_no")
  private boolean settingAdmins = false;

  @Column(name = "study_dashboard_chart")
  @Type(type = "yes_no")
  private boolean studyDashboardChart = false;

  @Column(name = "study_dashboard_stats")
  @Type(type = "yes_no")
  private boolean studyDashboardStats = false;

  @Column(name = "study_exc_active_task")
  @Type(type = "yes_no")
  private boolean studyExcActiveTask = false;

  @Column(name = "study_exc_questionnaries")
  @Type(type = "yes_no")
  private boolean studyExcQuestionnaries = false;

  @Column(name = "participant_properties")
  @Type(type = "yes_no")
  private Boolean participantProperties = false;

  public StudySequenceLangPK getStudySequenceLangPK() {
    return studySequenceLangPK;
  }

  public void setStudySequenceLangPK(StudySequenceLangPK studySequenceLangPK) {
    this.studySequenceLangPK = studySequenceLangPK;
  }

  public boolean isActions() {
    return actions;
  }

  public void setActions(boolean actions) {
    this.actions = actions;
  }

  public boolean isBasicInfo() {
    return basicInfo;
  }

  public void setBasicInfo(boolean basicInfo) {
    this.basicInfo = basicInfo;
  }

  public boolean isCheckList() {
    return checkList;
  }

  public void setCheckList(boolean checkList) {
    this.checkList = checkList;
  }

  public boolean isComprehensionTest() {
    return comprehensionTest;
  }

  public void setComprehensionTest(boolean comprehensionTest) {
    this.comprehensionTest = comprehensionTest;
  }

  public boolean isConsentEduInfo() {
    return consentEduInfo;
  }

  public void setConsentEduInfo(boolean consentEduInfo) {
    this.consentEduInfo = consentEduInfo;
  }

  public boolean iseConsent() {
    return eConsent;
  }

  public void seteConsent(boolean eConsent) {
    this.eConsent = eConsent;
  }

  public boolean isEligibility() {
    return eligibility;
  }

  public void setEligibility(boolean eligibility) {
    this.eligibility = eligibility;
  }

  public boolean isMiscellaneousBranding() {
    return miscellaneousBranding;
  }

  public void setMiscellaneousBranding(boolean miscellaneousBranding) {
    this.miscellaneousBranding = miscellaneousBranding;
  }

  public boolean isMiscellaneousNotification() {
    return miscellaneousNotification;
  }

  public void setMiscellaneousNotification(boolean miscellaneousNotification) {
    this.miscellaneousNotification = miscellaneousNotification;
  }

  public boolean isMiscellaneousResources() {
    return miscellaneousResources;
  }

  public void setMiscellaneousResources(boolean miscellaneousResources) {
    this.miscellaneousResources = miscellaneousResources;
  }

  public boolean isOverView() {
    return overView;
  }

  public void setOverView(boolean overView) {
    this.overView = overView;
  }

  public boolean isSettingAdmins() {
    return settingAdmins;
  }

  public void setSettingAdmins(boolean settingAdmins) {
    this.settingAdmins = settingAdmins;
  }

  public boolean isStudyDashboardChart() {
    return studyDashboardChart;
  }

  public void setStudyDashboardChart(boolean studyDashboardChart) {
    this.studyDashboardChart = studyDashboardChart;
  }

  public boolean isStudyDashboardStats() {
    return studyDashboardStats;
  }

  public void setStudyDashboardStats(boolean studyDashboardStats) {
    this.studyDashboardStats = studyDashboardStats;
  }

  public boolean isStudyExcActiveTask() {
    return studyExcActiveTask;
  }

  public void setStudyExcActiveTask(boolean studyExcActiveTask) {
    this.studyExcActiveTask = studyExcActiveTask;
  }

  public boolean isStudyExcQuestionnaries() {
    return studyExcQuestionnaries;
  }

  public void setStudyExcQuestionnaries(boolean studyExcQuestionnaries) {
    this.studyExcQuestionnaries = studyExcQuestionnaries;
  }

  public Boolean getParticipantProperties() {
    return participantProperties;
  }

  public void setParticipantProperties(Boolean participantProperties) {
    this.participantProperties = participantProperties;
  }
}
