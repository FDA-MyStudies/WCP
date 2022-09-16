package com.fdahpstudydesigner.bean;

import javax.persistence.*;

public class GroupsBean {
    private Integer id;
    private Integer groupId;
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

    public Integer getId() {
        return id;
    }

    public Integer getGroupId() {
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

    public void setGroupId(Integer groupId) {
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
}
