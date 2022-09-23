package com.fdahpstudydesigner.bo;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Data
@Entity
@Table(name = "grouppp")
public class GroupsBo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "groupId")
    private String groupId;
    
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "created_on")
    private String createdOn = "";

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_on")
    private String modifiedOn;

    @Column(name = "is_group_deleted", length = 1)
    private Boolean isgroupDelted = false;
    
    @Transient
    private String userId;
    
    @Transient private String buttonText;

    @Transient private String type;
    
    @Column(name = "action", length = 1)
    private boolean action;

    @Transient
    private String userName;
    
    @Column(name = "study_id")
    private Integer studyId;
    
    @Column(name="questionnaire_id")
    private Integer questionnaireId;

    public String getUserIds() {
        return userId;
    }

    public void setUserIds(String userIds) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
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

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public boolean isGroupDeleted() {
        return isgroupDelted;
    }

    public void setGroupDeleted(boolean groupDeleted) {
        this.isgroupDelted = groupDeleted;
    }

}
