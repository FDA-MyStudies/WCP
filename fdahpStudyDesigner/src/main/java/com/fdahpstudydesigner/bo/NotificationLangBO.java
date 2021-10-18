package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notification_lang")
public class NotificationLangBO implements Serializable {

  @EmbeddedId private NotificationLangPK notificationLangPK;

  @Column(name = "notification_text")
  private String notificationText;

  @Column(name = "study_id")
  private Integer studyId;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private String createdOn;

  @Column(name = "modified_by")
  private Integer modifiedBy;

  @Column(name = "modified_on")
  private String modifiedOn;

  @Column(name = "notification_status", length = 1)
  private Boolean notificationStatus = false;

  @Column(name = "is_active")
  private Boolean isActive = true;

  @Column(name = "notification_action", length = 1)
  private Boolean notificationAction;

  @Column(name = "notification_sent", length = 1)
  private Boolean notificationSent = false;

  public NotificationLangPK getNotificationLangPK() {
    return notificationLangPK;
  }

  public void setNotificationLangPK(NotificationLangPK notificationLangPK) {
    this.notificationLangPK = notificationLangPK;
  }

  public String getNotificationText() {
    return notificationText;
  }

  public void setNotificationText(String notificationText) {
    this.notificationText = notificationText;
  }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public Integer getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public String getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(String modifiedOn) {
    this.modifiedOn = modifiedOn;
  }

  public Integer getStudyId() {
    return studyId;
  }

  public void setStudyId(Integer studyId) {
    this.studyId = studyId;
  }

  public Boolean getNotificationStatus() {
    return notificationStatus;
  }

  public void setNotificationStatus(Boolean notificationStatus) {
    this.notificationStatus = notificationStatus;
  }

  public Boolean getActive() {
    return isActive;
  }

  public void setActive(Boolean active) {
    isActive = active;
  }

  public boolean isNotificationAction() {
    return notificationAction;
  }

  public void setNotificationAction(boolean notificationAction) {
    this.notificationAction = notificationAction;
  }

  public Boolean getNotificationSent() {
    return notificationSent;
  }

  public void setNotificationSent(Boolean notificationSent) {
    this.notificationSent = notificationSent;
  }
}
