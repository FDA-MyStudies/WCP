package com.fdahpstudydesigner.bean;

import lombok.Data;

import java.util.Map;

@Data
public class QuestionnaireStepBean {

  private Integer destinationStep;
  private String destinationText;
  Map<Integer, QuestionnaireStepBean> fromMap;
  private String lineChart;
  private Integer questionInstructionId;
  private Integer responseType;
  private String responseTypeText;
  private Integer sequenceNo;
  private String statData;
  private Boolean status;

  private  Boolean groupFlag;
  private Integer stepId;
  private Integer groupId;
  private Integer deletionId;
  private String stepType;

  private String stepShortTitle;
  private String title;

  private Boolean useAnchorDate;
  private Boolean defaultVisibility;
  private Boolean destinationTrueAsGroup;
  private Boolean isPiping;
  private String pipingSnippet;
  private Integer pipingSourceQuestionKey;
  private Boolean differentSurvey;
  private Integer pipingSurveyId;
  private String language;
}
