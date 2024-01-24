package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StudyPageLanguagePK implements Serializable {

  @Column(name = "page_id")
  private Integer pageId;

  @Column(name = "lang_code")
  private String langCode;

  public StudyPageLanguagePK(Integer pageId, String langCode) {
    this.pageId = pageId;
    this.langCode = langCode;
  }

  public StudyPageLanguagePK() {}

  public Integer getPageId() {
    return pageId;
  }

  public void setPageId(Integer pageId) {
    this.pageId = pageId;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }
}
