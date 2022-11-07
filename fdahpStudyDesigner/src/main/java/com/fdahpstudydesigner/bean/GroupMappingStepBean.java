package com.fdahpstudydesigner.bean;

import java.util.List;

public class GroupMappingStepBean {
	
	  private Integer stepId = null;
	  
	  private String stepType = "";
	  
	  private List<String> description;
	  
	
	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer integer) {
		this.stepId = integer;
	}

	public String getStepType() {
		return stepType;
	}

	public void setStepType(String stepType) {
		this.stepType = stepType;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

}
