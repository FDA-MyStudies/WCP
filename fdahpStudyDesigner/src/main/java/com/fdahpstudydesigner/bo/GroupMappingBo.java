package com.fdahpstudydesigner.bo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "group_mapping")
public class GroupMappingBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer Id = 0;

    @Column(name = "grp_id")
    private Integer grpId = 0;

    @Column(name = "step_id")
    private String stepId = null;

    @Column(name = "status")
    private Boolean status = false;

    @Column(name = "quetstionnaire_stepId")
    private Integer questionnaireStepId;

}
