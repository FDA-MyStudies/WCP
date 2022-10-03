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

    @Column(name = "group_id")
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

    @Transient
    private String userId;
    
    @Transient private String buttonText;

    @Transient private String type;
    
    @Column(name = "action", length = 1)
    private Boolean action;

    @Transient
    private String userName;
    
    @Column(name = "study_id")
    private Integer studyId;
    
    @Column(name="questionnaire_id")
    private Integer questionnaireId;

}
