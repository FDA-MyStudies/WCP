package com.fdahpstudydesigner.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StepDropdownBean {
    String questionnaireId;
    String studyId;
    Integer seqNo;
    Integer stepId;
    Integer instructionFormId;
    String caller;
    Boolean isDifferentSurveyPreload;
    String preLoadQuestionnaireId;
    Boolean isDifferentSurveyPiping;
}
