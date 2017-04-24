package com.fdahpstudydesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Ronalin
 *
 */
@Entity
@Table(name = "eligibility_test_response")
public class EligibilityTestResponseBo implements Serializable{
	
	private static final long serialVersionUID = -6967340852884815498L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="response_id")
	private Integer responseId;
	
	@Column(name = "eligibility_test_id")
	private Integer eligibilityTestId;
	
	@Column(name = "response_option")
	private String responseOption;
	
	@Column(name = "pass_fail")
	private String passFail;
	
	@Column(name = "destination_question")
	private Integer destinationQuestion;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;

	public Integer getResponseId() {
		return responseId;
	}

	public void setResponseId(Integer responseId) {
		this.responseId = responseId;
	}

	public Integer getEligibilityTestId() {
		return eligibilityTestId;
	}

	public void setEligibilityTestId(Integer eligibilityTestId) {
		this.eligibilityTestId = eligibilityTestId;
	}

	public String getResponseOption() {
		return responseOption;
	}

	public void setResponseOption(String responseOption) {
		this.responseOption = responseOption;
	}

	public String getPassFail() {
		return passFail;
	}

	public void setPassFail(String passFail) {
		this.passFail = passFail;
	}

	public Integer getDestinationQuestion() {
		return destinationQuestion;
	}

	public void setDestinationQuestion(Integer destinationQuestion) {
		this.destinationQuestion = destinationQuestion;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}
	
}