package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class InstructionLangPK implements Serializable {

  @Column(name = "id")
  private Integer id;

  @Column(name = "lang_code")
  private String langCode;

  public InstructionLangPK(Integer id, String langCode) {
    this.id = id;
    this.langCode = langCode;
  }

  public InstructionLangPK() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLangCode() {
    return langCode;
  }

  public void setLangCode(String langCode) {
    this.langCode = langCode;
  }
}
