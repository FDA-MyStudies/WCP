package com.fdahpstudydesigner.bo;

import com.fdahpstudydesigner.bean.PreLoadLogicBean;
import com.fdahpstudydesigner.bean.QuestionnaireStepBean;
import java.io.Serializable;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the questionnaires_steps database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "questionnaires_steps")
@NamedQueries({
  @NamedQuery(
      name = "getQuestionnaireStepSequenceNo",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnairesId and QSBO.active=1 order by QSBO.sequenceNo DESC"),
  @NamedQuery(
      name = "getQuestionnaireStep",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.instructionFormId=:instructionFormId and QSBO.stepType=:stepType and QSBO.active=1"),
  @NamedQuery(
      name = "getQuestionnaireStepList",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId and QSBO.active=1 order by QSBO.sequenceNo"),
  @NamedQuery(
      name = "checkQuestionnaireStepShortTitle",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnaireId and QSBO.stepShortTitle=:shortTitle"),
  @NamedQuery(
      name = "getForwardQuestionnaireSteps",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnairesId and QSBO.sequenceNo >:sequenceNo and QSBO.active=1 order by QSBO.sequenceNo ASC"),
  @NamedQuery(
      name = "getQuestionnaireStepsByType",
      query =
          "From QuestionnairesStepsBo QSBO where QSBO.questionnairesId=:questionnairesId and QSBO.stepType=:stepType and QSBO.active=1"),
})
public class QuestionnairesStepsBo implements Serializable {

  private static final long serialVersionUID = -7908951701723989954L;

  @Column(name = "active")
  private Boolean active;

  @Column(name = "created_by")
  private Integer createdBy;

  @Column(name = "created_on")
  private String createdOn;

  @Column(name = "destination_step")
  private Integer destinationStep;

  @Transient private SortedMap<Integer, QuestionnaireStepBean> formQuestionMap = new TreeMap<>();

  @Column(name = "instruction_form_id")
  private Integer instructionFormId;

  @Transient private Integer isShorTitleDuplicate = 0;

  @Column(name = "modified_by")
  private Integer modifiedBy;

  @Column(name = "modified_on")
  private String modifiedOn;

  @Transient private List<QuestionConditionBranchBo> questionConditionBranchBoList;

  @Column(name = "questionnaires_id")
  private Integer questionnairesId;

  @Transient private QuestionReponseTypeBo questionReponseTypeBo;

  @Transient private List<QuestionResponseSubTypeBo> questionResponseSubTypeList;

  @Transient private QuestionsBo questionsBo;

   @Transient private GroupsBo groupBo;
  @Column(name = "repeatable")
  private String repeatable = "No";

  @Column(name = "repeatable_text")
  private String repeatableText;

  @Column(name = "sequence_no")
  private Integer sequenceNo;

  @Column(name = "skiappable")
  private String skiappable;

  @Column(name = "status")
  private Boolean status;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "step_id")
  private Integer stepId;

  @Column(name = "step_short_title")
  private String stepShortTitle;

  @Column(name = "step_type")
  private String stepType;

  @Column(name = "default_visibility")
  private Boolean defaultVisibility;

  @Column(name = "destination_true_as_group")
  private Integer destinationTrueAsGroup;

  @Column(name = "is_piping")
  private Integer isPiping;

  @Column(name = "piping_snippet")
  private String pipingSnippet;

  @Column(name = "piping_source_question_key")
  private String pipingSourceQuestionKey;

  @Transient private String type;

  @Transient private List<PreLoadLogicBean> preLoadLogicBeans;

  @Transient private String groupDefaultVisibility;

  public Boolean getActive() {
    return active;
  }

  public Integer getCreatedBy() {
    return createdBy;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public Integer getDestinationStep() {
    return destinationStep;
  }

  public SortedMap<Integer, QuestionnaireStepBean> getFormQuestionMap() {
    return formQuestionMap;
  }

  public Integer getInstructionFormId() {
    return instructionFormId;
  }

  public Integer getIsShorTitleDuplicate() {
    return isShorTitleDuplicate;
  }

  public Integer getModifiedBy() {
    return modifiedBy;
  }

  public String getModifiedOn() {
    return modifiedOn;
  }

  public List<QuestionConditionBranchBo> getQuestionConditionBranchBoList() {
    return questionConditionBranchBoList;
  }

  public Integer getQuestionnairesId() {
    return questionnairesId;
  }

  public QuestionReponseTypeBo getQuestionReponseTypeBo() {
    return questionReponseTypeBo;
  }

  public List<QuestionResponseSubTypeBo> getQuestionResponseSubTypeList() {
    return questionResponseSubTypeList;
  }

  public QuestionsBo getQuestionsBo() {
    return questionsBo;
  }

  public String getRepeatable() {
    return repeatable;
  }

  public String getRepeatableText() {
    return repeatableText;
  }

  public Integer getSequenceNo() {
    return sequenceNo;
  }

  public String getSkiappable() {
    return skiappable;
  }

  public Boolean getStatus() {
    return status;
  }

  public Integer getStepId() {
    return stepId;
  }

  public String getStepShortTitle() {
    return stepShortTitle;
  }

  public String getStepType() {
    return stepType;
  }

  public String getType() {
    return type;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public void setCreatedBy(Integer createdBy) {
    this.createdBy = createdBy;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public void setDestinationStep(Integer destinationStep) {
    this.destinationStep = destinationStep;
  }

  public void setFormQuestionMap(SortedMap<Integer, QuestionnaireStepBean> formQuestionMap) {
    this.formQuestionMap = formQuestionMap;
  }

  public void setInstructionFormId(Integer instructionFormId) {
    this.instructionFormId = instructionFormId;
  }

  public void setIsShorTitleDuplicate(Integer isShorTitleDuplicate) {
    this.isShorTitleDuplicate = isShorTitleDuplicate;
  }

  public void setModifiedBy(Integer modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public void setModifiedOn(String modifiedOn) {
    this.modifiedOn = modifiedOn;
  }

  public void setQuestionConditionBranchBoList(
      List<QuestionConditionBranchBo> questionConditionBranchBoList) {
    this.questionConditionBranchBoList = questionConditionBranchBoList;
  }

  public void setQuestionnairesId(Integer questionnairesId) {
    this.questionnairesId = questionnairesId;
  }

  public void setQuestionReponseTypeBo(QuestionReponseTypeBo questionReponseTypeBo) {
    this.questionReponseTypeBo = questionReponseTypeBo;
  }

  public void setQuestionResponseSubTypeList(
      List<QuestionResponseSubTypeBo> questionResponseSubTypeList) {
    this.questionResponseSubTypeList = questionResponseSubTypeList;
  }

  public void setQuestionsBo(QuestionsBo questionsBo) {
    this.questionsBo = questionsBo;
  }

  public void setRepeatable(String repeatable) {
    this.repeatable = repeatable;
  }

  public void setRepeatableText(String repeatableText) {
    this.repeatableText = repeatableText;
  }

  public void setSequenceNo(Integer sequenceNo) {
    this.sequenceNo = sequenceNo;
  }

  public void setSkiappable(String skiappable) {
    this.skiappable = skiappable;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public void setStepId(Integer stepId) {
    this.stepId = stepId;
  }

  public void setStepShortTitle(String stepShortTitle) {
    this.stepShortTitle = stepShortTitle;
  }

  public void setStepType(String stepType) {
    this.stepType = stepType;
  }

  public void setType(String type) {
    this.type = type;
  }

  public GroupsBo getGroupBo() {
    return groupBo;
  }

  public void setGroupBo(GroupsBo groupBo) {
    this.groupBo = groupBo;
  }

  public Boolean getDefaultVisibility() {
    return defaultVisibility;
  }

  public void setDefaultVisibility(Boolean defaultVisibility) {
    this.defaultVisibility = defaultVisibility;
  }

  public Integer getDestinationTrueAsGroup() {
    return destinationTrueAsGroup;
  }

  public void setDestinationTrueAsGroup(Integer destinationTrueAsGroup) {
    this.destinationTrueAsGroup = destinationTrueAsGroup;
  }

  public Integer getIsPiping() {
    return isPiping;
  }

  public void setIsPiping(Integer isPiping) {
    this.isPiping = isPiping;
  }

  public String getPipingSnippet() {
    return pipingSnippet;
  }

  public void setPipingSnippet(String pipingSnippet) {
    this.pipingSnippet = pipingSnippet;
  }

  public String getPipingSourceQuestionKey() {
    return pipingSourceQuestionKey;
  }

  public void setPipingSourceQuestionKey(String pipingSourceQuestionKey) {
    this.pipingSourceQuestionKey = pipingSourceQuestionKey;
  }

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
}
