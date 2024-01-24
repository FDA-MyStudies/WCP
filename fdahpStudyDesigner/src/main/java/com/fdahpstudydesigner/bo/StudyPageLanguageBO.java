package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "study_page_lang")
public class StudyPageLanguageBO implements Serializable {

  @EmbeddedId private StudyPageLanguagePK studyPageLanguagePK;

  @Column(name = "study_id")
  private Integer studyId;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private String createdOn;

  @Column(name = "modified_by")
  private Integer modifiedBy;

  @Column(name = "modified_on")
  private String modifiedOn;

  public Integer getStudyId() {
    return studyId;
  }

  public void setStudyId(Integer studyId) {
    this.studyId = studyId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public StudyPageLanguagePK getStudyPageLanguagePK() {
    return studyPageLanguagePK;
  }

  public void setStudyPageLanguagePK(StudyPageLanguagePK studyPageLanguagePK) {
    this.studyPageLanguagePK = studyPageLanguagePK;
  }
}
