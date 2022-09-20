package com.fdahpstudydesigner.bo;

import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "group_mapping")
public class GroupMappingBo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer Id = 0;

    @Column(name = "group_id")
    private Integer groupId = 0;

    @Column(name = "step_id")
    private List<Integer> stepId = null;

}
