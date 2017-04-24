package com.fdahpstudydesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="instructions")
@NamedQueries({
	@NamedQuery(name="getInstructionStep", query="from InstructionsBo IBO where IBO.id=:stepId"),
})
public class InstructionsBo implements Serializable {
	
	private static final long serialVersionUID = 1389506581768527442L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name="instruction_title",length=250)
	private String instructionTitle;
	
	@Column(name="instruction_text",length=2500)
	private String instructionText;
	
	@Column(name = "created_on")
	private String createdOn;
	
	@Column(name = "modified_on")
	private String modifiedOn;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;
	
	@Column(name="active")
	private Boolean active;
	
	@Column(name="status")
	private Boolean status;
	
	@Transient
	private String type;
	
	@Transient
	private Integer questionnaireId;
	
	@Transient
	private QuestionnairesStepsBo questionnairesStepsBo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstructionTitle() {
		return instructionTitle;
	}

	public void setInstructionTitle(String instructionTitle) {
		this.instructionTitle = instructionTitle;
	}

	public String getInstructionText() {
		return instructionText;
	}

	public void setInstructionText(String instructionText) {
		this.instructionText = instructionText;
	}

	public QuestionnairesStepsBo getQuestionnairesStepsBo() {
		return questionnairesStepsBo;
	}

	public void setQuestionnairesStepsBo(QuestionnairesStepsBo questionnairesStepsBo) {
		this.questionnairesStepsBo = questionnairesStepsBo;
	}

	public Integer getQuestionnaireId() {
		return questionnaireId;
	}

	public void setQuestionnaireId(Integer questionnaireId) {
		this.questionnaireId = questionnaireId;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}