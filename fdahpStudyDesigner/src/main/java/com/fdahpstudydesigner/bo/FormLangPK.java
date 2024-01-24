package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FormLangPK implements Serializable {

  @Column(name = "form_id")
  private Integer formId;

  @Column(name = "lang_code")
  private String langCode;

  public FormLangPK(Integer formId, String langCode) {
    this.formId = formId;
    this.langCode = langCode;
  }

  public FormLangPK() {}

  public Integer getFormId() {
    return formId;
  }

  public void setFormId(Integer formId) {
    this.formId = formId;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }
}
