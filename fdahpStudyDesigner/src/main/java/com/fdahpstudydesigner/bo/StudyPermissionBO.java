package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


/**
 * The persistent class for the study_permission database table.
 *
 * @author Pradyumn
 */
@Entity
@Table(name = "study_permission")
@NamedQueries({
  @NamedQuery(
      name = "getStudyPermissionById",
      query = " from StudyPermissionBO where studyId=:studyId and userId=:userId"),
})
public class StudyPermissionBO implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  @Column(name = "delFlag")
  private Integer delFlag;

  @Column(name = "project_lead")
  private Integer projectLead;

  @Column(name = "study_id")
  private Integer studyId;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer studyPermissionId;

  @Transient private String userFullName;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "view_permission", length = 1)
  private boolean viewPermission;

  public Integer getDelFlag() {
    return delFlag;
  }

  public Integer getProjectLead() {
    return projectLead;
  }

  public Integer getStudyId() {
    return studyId;
  }

  public Integer getStudyPermissionId() {
    return studyPermissionId;
  }

  public String getUserFullName() {
    return userFullName;
  }

  public Integer getUserId() {
    return userId;
  }

  public boolean isViewPermission() {
    return viewPermission;
  }

  public void setDelFlag(Integer delFlag) {
    this.delFlag = delFlag;
  }

  public void setProjectLead(Integer projectLead) {
    this.projectLead = projectLead;
  }

  public void setStudyId(Integer studyId) {
    this.studyId = studyId;
  }

  public void setStudyPermissionId(Integer studyPermissionId) {
    this.studyPermissionId = studyPermissionId;
  }

  public void setUserFullName(String userFullName) {
    this.userFullName = userFullName;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public void setViewPermission(boolean viewPermission) {
    this.viewPermission = viewPermission;
  }
}
