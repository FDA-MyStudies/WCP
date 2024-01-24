package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import org.hibernate.annotations.Type;
import org.hibernate.type.YesNoConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "study_sequence_lang")
public class StudySequenceLangBO implements Serializable {

  @EmbeddedId private StudySequenceLangPK studySequenceLangPK;

  @Column(name = "actions")
  @Convert(converter = YesNoConverter.class)
  private boolean actions = false;

  @Column(name = "basic_info")
  @Convert(converter = YesNoConverter.class)
  private boolean basicInfo = false;

  @Column(name = "check_list")
  @Convert(converter = YesNoConverter.class)
  private boolean checkList = false;

  @Column(name = "comprehension_test")
  @Convert(converter = YesNoConverter.class)
  private boolean comprehensionTest = false;

  @Column(name = "consent_edu_info")
  @Convert(converter = YesNoConverter.class)
  private boolean consentEduInfo = false;

  @Column(name = "e_consent")
  @Convert(converter = YesNoConverter.class)
  private boolean eConsent = false;

  @Column(name = "eligibility")
  @Convert(converter = YesNoConverter.class)
  private boolean eligibility = false;

  @Column(name = "miscellaneous_branding")
  @Convert(converter = YesNoConverter.class)
  private boolean miscellaneousBranding = false;

  @Column(name = "miscellaneous_notification")
  @Convert(converter = YesNoConverter.class)
  private boolean miscellaneousNotification = false;

  @Column(name = "miscellaneous_resources")
  @Convert(converter = YesNoConverter.class)
  private boolean miscellaneousResources = false;

  @Column(name = "over_view")
  @Convert(converter = YesNoConverter.class)
  private boolean overView = false;

  @Column(name = "setting_admins")
  @Convert(converter = YesNoConverter.class)
  private boolean settingAdmins = false;

  @Column(name = "study_dashboard_chart")
  @Convert(converter = YesNoConverter.class)
  private boolean studyDashboardChart = false;

  @Column(name = "study_dashboard_stats")
  @Convert(converter = YesNoConverter.class)
  private boolean studyDashboardStats = false;

  @Column(name = "study_exc_active_task")
  @Convert(converter = YesNoConverter.class)
  private boolean studyExcActiveTask = false;

  @Column(name = "study_exc_questionnaries")
  @Convert(converter = YesNoConverter.class)
  private boolean studyExcQuestionnaries = false;

  @Column(name = "participant_properties")
  @Convert(converter = YesNoConverter.class)
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
