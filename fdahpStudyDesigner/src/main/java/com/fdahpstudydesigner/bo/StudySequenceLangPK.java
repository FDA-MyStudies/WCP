package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StudySequenceLangPK implements Serializable {

  @Column(name = "study_id")
  private Integer studyId;

  @Column(name = "langCode")
  private String langCode;

  public StudySequenceLangPK(Integer studyId, String langCode) {
    this.studyId = studyId;
    this.langCode = langCode;
  }

  public StudySequenceLangPK() {}

  public Integer getStudyId() {
    return studyId;
  }

  public void setStudyId(Integer studyId) {
    this.studyId = studyId;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }
}
