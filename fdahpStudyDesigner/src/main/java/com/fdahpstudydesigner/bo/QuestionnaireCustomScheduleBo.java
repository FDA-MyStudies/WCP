package com.fdahpstudydesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="questionnaires_custom_frequencies")
public class QuestionnaireCustomScheduleBo implements Serializable{

	private static final long serialVersionUID = 1935609268959765482L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="questionnaires_id")
	private Integer questionnairesId;
	
	@Column(name="frequency_start_date")
	private String frequencyStartDate;

	@Column(name="frequency_end_date")
	private String frequencyEndDate;
	
	@Column(name="frequency_time")
	private String frequencyTime;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestionnairesId() {
		return questionnairesId;
	}

	public void setQuestionnairesId(Integer questionnairesId) {
		this.questionnairesId = questionnairesId;
	}

	public String getFrequencyStartDate() {
		return frequencyStartDate;
	}

	public void setFrequencyStartDate(String frequencyStartDate) {
		this.frequencyStartDate = frequencyStartDate;
	}

	public String getFrequencyEndDate() {
		return frequencyEndDate;
	}

	public void setFrequencyEndDate(String frequencyEndDate) {
		this.frequencyEndDate = frequencyEndDate;
	}

	public String getFrequencyTime() {
		return frequencyTime;
	}

	public void setFrequencyTime(String frequencyTime) {
		this.frequencyTime = frequencyTime;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}
	
}