package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "instructions_lang")
public class InstructionsLangBO implements Serializable {

  @EmbeddedId private InstructionLangPK instructionLangPK;

  @Column(name = "instruction_title", length = 250)
  private String instructionTitle;

  @Column(name = "instruction_text", length = 2500)
  private String instructionText;

  @Column(name = "piping_snippet", length = 2500)
  private String pipingSnippet;

  @Column(name = "questionnaire_id")
  private Integer questionnaireId;

  @Column(name = "active")
  private Boolean active = true;

  @Column(name = "status")
  private Boolean status = false;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private String createdOn;

  @Column(name = "modified_by")
  private Integer modifiedBy;

  @Column(name = "modified_on")
  private String modifiedOn;

  public InstructionLangPK getInstructionLangPK() {
    return instructionLangPK;
  }

  public void setInstructionLangPK(InstructionLangPK instructionLangPK) {
    this.instructionLangPK = instructionLangPK;
  }

  public String getInstructionTitle() {
    return instructionTitle;
  }

  public void setInstructionTitle(String instructionTitle) {
    this.instructionTitle = instructionTitle;
  }

  public Integer getQuestionnaireId() {
    return questionnaireId;
  }

  public void setQuestionnaireId(Integer questionnaireId) {
    this.questionnaireId = questionnaireId;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
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

  public String getInstructionText() {
    return instructionText;
  }

  public void setInstructionText(String instructionText) {
    this.instructionText = instructionText;
  }

  public String getPipingSnippet() {
    return pipingSnippet;
  }

  public void setPipingSnippet(String pipingSnippet) {
    this.pipingSnippet = pipingSnippet;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
