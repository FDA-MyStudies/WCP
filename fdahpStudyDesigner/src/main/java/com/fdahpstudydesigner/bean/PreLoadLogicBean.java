package com.fdahpstudydesigner.bean;

public class PreLoadLogicBean {

    private Integer id;
    private String operator;
    private String inputValue;
    private String conditionOperator;
    private Integer stepGroupId;
    private String stepOrGroup;
    private String deleteIds;

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

    public String getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(String deleteIds) {
        this.deleteIds = deleteIds;
    }
}
