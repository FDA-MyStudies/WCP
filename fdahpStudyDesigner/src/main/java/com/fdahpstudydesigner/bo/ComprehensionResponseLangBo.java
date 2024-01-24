package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "comprehension_test_response_lang")
public class ComprehensionResponseLangBo implements Serializable {

  @EmbeddedId private ComprehensionResponseLangPK comprehensionResponseLangPK;

  @Column(name = "question_id")
  private Integer questionId;

  @Column(name = "response_option")
  private String responseOption;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
        name = "question_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false),
    @JoinColumn(
        name = "lang_code",
        referencedColumnName = "lang_code",
        insertable = false,
        updatable = false)
  })
  private ComprehensionQuestionLangBO comprehensionQuestionLangBO;

  public ComprehensionQuestionLangBO getComprehensionQuestionLangBO() {
    return comprehensionQuestionLangBO;
  }

  public void setComprehensionQuestionLangBO(
      ComprehensionQuestionLangBO comprehensionQuestionLangBO) {
    this.comprehensionQuestionLangBO = comprehensionQuestionLangBO;
  }

  public ComprehensionResponseLangPK getComprehensionResponseLangPK() {
    return comprehensionResponseLangPK;
  }

  public void setComprehensionResponseLangPK(
      ComprehensionResponseLangPK comprehensionResponseLangPK) {
    this.comprehensionResponseLangPK = comprehensionResponseLangPK;
  }

  public Integer getQuestionId() {
    return questionId;
  }

  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  public String getResponseOption() {
    return responseOption;
  }

  public void setResponseOption(String responseOption) {
    this.responseOption = responseOption;
  }
}
