package com.fdahpstudydesigner.bo;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pre_load_logic")
public class PreLoadLogicBo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "operator")
    private String operator;

    @Column(name = "input_value")
    private String inputValue;

    @Column(name = "condition_operator")
    private String conditionOperator;

    @Column(name = "step_group_id")
    private Integer stepGroupId;

    @Column(name = "step_or_group")
    private String stepOrGroup;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getConditionOperator() {
        return conditionOperator;
    }

    public void setConditionOperator(String conditionOperator) {
        this.conditionOperator = conditionOperator;
    }

    public Integer getStepGroupId() {
        return stepGroupId;
    }

    public void setStepGroupId(Integer stepGroupId) {
        this.stepGroupId = stepGroupId;
    }

    public String getStepOrGroup() {
        return stepOrGroup;
    }

    public void setStepOrGroup(String stepOrGroup) {
        this.stepOrGroup = stepOrGroup;
    }
}
