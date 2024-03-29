package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QuestionnaireLangPK implements Serializable {

  @Column(name = "id")
  private Integer id;

  @Column(name = "lang_code")
  private String langCode;

  public QuestionnaireLangPK(Integer id, String langCode) {
    this.id = id;
    this.langCode = langCode;
  }

  public QuestionnaireLangPK() {}

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
