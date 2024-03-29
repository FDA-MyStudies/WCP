package com.fdahpstudydesigner.bo;

import com.fdahpstudydesigner.bean.PreLoadLogicBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
    
    @Column(name = "is_published")
    private Integer isPublished=0;

    @Transient
    private String userId;
    
    @Transient private String buttonText;

    @Transient private String type;

    @Transient private List<PreLoadLogicBean> preLoadLogicBeans;
    
    @Column(name = "action", length = 1)
    private Boolean action;

    @Transient
    private String userName;
    
    @Column(name = "study_id")
    private Integer studyId;
    
    @Column(name="questionnaire_id")
    private Integer questionnaireId;

    @Column(name = "destination_true_as_group")
    private Integer destinationTrueAsGroup;

    @Column(name = "default_visibility")
    private Boolean defaultVisibility;

    @Column(name = "step_or_group")
    private String stepOrGroup;
}
