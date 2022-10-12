package com.fdahpstudydesigner.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StepDropdownBean {
    String questionnaireId;
    Integer seqNo;
    String caller;
    Boolean isDifferentSurveyPreload;
    Boolean isDifferentSurveyPiping;
}
