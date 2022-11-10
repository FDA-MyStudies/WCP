package com.fdahpstudydesigner.bean;

import javax.persistence.*;
import java.util.List;

public class GroupsBean {
    private Integer id;
    private String groupId;
    private String groupName;
    private String createdOn = "";
    private Integer createdBy;
    private Integer modifiedBy;
    private String modifiedOn;
    private Boolean groupDeleted = false;
    private String userIds;
    private String userName;
    private Integer studyId;
    private Integer questionnaireId;
	private boolean action;
	private String type;
	private String buttonText;
    private Boolean defaultVisibility;
    private Integer destinationTrueAsGroup;
    private Integer isPublished;
    private String stepOrGroup;

    @Transient private String groupDefaultVisibility;

    @Transient private List<PreLoadLogicBean> preLoadLogicBeans;

    public List<PreLoadLogicBean> getPreLoadLogicBeans() {
        return preLoadLogicBeans;
    }

    public void setPreLoadLogicBeans(List<PreLoadLogicBean> preLoadLogicBeans) {
        this.preLoadLogicBeans = preLoadLogicBeans;
    }

    public String getGroupDefaultVisibility() {
        return groupDefaultVisibility;
    }

    public void setGroupDefaultVisibility(String groupDefaultVisibility) {
        this.groupDefaultVisibility = groupDefaultVisibility;
    }

    public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

    public boolean isAction() {
		return action;
	}

	public void setAction(boolean action) {
		this.action = action;
	}

     public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public Integer getDestinationTrueAsGroup() {
        return destinationTrueAsGroup;
    }

    public void setDestinationTrueAsGroup(Integer destinationTrueAsGroup) {
        this.destinationTrueAsGroup = destinationTrueAsGroup;
    }

    public Boolean getDefaultVisibility() {
        return defaultVisibility;
    }

    public void setDefaultVisibility(Boolean defaultVisibility) {
        this.defaultVisibility = defaultVisibility;
    }
   

    public Integer getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public void setGroupDeleted(Boolean groupDeleted) {
        this.groupDeleted = groupDeleted;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStudyId(Integer studyId) {
        this.studyId = studyId;
    }

    public void setQuestionnaireId(Integer questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public Boolean getGroupDeleted() {
        return groupDeleted;
    }

    public String getUserIds() {
        return userIds;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public Integer getQuestionnaireId() {
        return questionnaireId;
    }

	public Integer getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(Integer isPublished) {
		this.isPublished = isPublished;
	}

    public String getStepOrGroup() {
        return stepOrGroup;
    }

    public void setStepOrGroup(String stepOrGroup) {
        this.stepOrGroup = stepOrGroup;
    }
}
