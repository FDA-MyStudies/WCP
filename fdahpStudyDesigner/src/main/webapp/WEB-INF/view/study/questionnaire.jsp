<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date"/>
<c:set var="tz" value="America/Los_Angeles"/>
<head>
    <meta charset="UTF-8">
    <style>

      .cursonMove {
        cursor: move !important;
      }

      .sepimgClass {
        position: relative;
      }

      .time-opts .addBtnDis {
        display: none;
      }

      .time-opts:last-child .addBtnDis {
        display: inline-block;
      }

      .manually-option .addBtnDis {
        display: none;
      }

      .manually-option:last-child .addBtnDis {
        display: inline-block;
      }

      .manually-anchor-option .addBtnDis {
        display: none;
      }

      .manually-anchor-option:last-child .addBtnDis {
        display: inline-block;
      }

      .tool-tip {
        display: inline-block;
      }

      .tool-tip [disabled] {
        pointer-events: none;
      }

      .yourTableClass {
        border-collapse: separate;
        border-spacing: 10px;
        *border-collapse: expression('separate', cellSpacing = '10px');
      }

      .dis_inlinetop {
        display: inline-block;
        vertical-align: top;
      }

      .sorting_disabled {
        pointer-events: none;
        cursor: not-allowed;
      }

      .static-width {
        width:20% !important;
      }

      /* .delete{
	background-position: -113px -63px ;
	width: 17px;
	height: 22px;
    display: inline-block !important;
    cursor: pointer;
    vertical-align:middle;
} */

      /* error box css start here  */
      .help-block ul {
        width: 160px;
      / / font-size: 10 px !important;
      }

      /* error box css end here  */
      .display__none {
        display: none !important;
      }

      .display__inline-block {
        display: inline-block !important;
      }
      
      .langSpecific{
    	position: relative;
  	  }

  	  .langSpecific > button::before{
    	content: '';
    	display: block;
    	background-image: url("../images/global_icon.png");
    	width: 16px;
    	height: 14px;
    	position: absolute;
    	top: 9px;
    	left: 9px;
    	background-repeat: no-repeat;
  	  }

  	  .langSpecific > button{
        padding-left: 30px;
  	  }

  	  #autoSavedMessage{
      width:257px;
      }

      #myAutoModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
      position:relative !important;
      right:-14px !important;
      margin-top:6% !important;
      }

      #timeOutModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
        position:relative !important;
        right:-14px !important;
        margin-top:6% !important;
        }

      .flr_modal{
      float:right !important;
      }

      .grey_txt{
      color:grey;
      font-size:15px;
      font-weight:500;
      }

      .blue_text{
      color:#007CBA !important;
      font-size:15px;
      font-weight:500;
      }

      .timerPos{
      position:relative;
      top:-2px;
      right:2px !important;
      }

      .bold_txt{
      font-weight:900 !important;
      color:#007cba !important;
      font-size:15px;
       }

.close{
background:none;
}

.dailyTimeDiv .align-span-center {
    position: relative;
    top: 0px;
}

.manuallyContainer .align-span-center {
    position: relative;
    top: -9px;
}
.form-control{
margin-bottom:5px !important;
}
  .sign-box{
    height:40px !important;
    }
    
.form-control {
    height: 33px;
}
    </style>
</head>


<script type="text/javascript">
  function isNumber(evt, thisAttr) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if ((charCode < 48 && charCode > 57) || (charCode >= 65 && charCode <= 90) || (charCode >= 97
        && charCode <= 122)) {
      return false;
    }
    if ((!$(thisAttr).val()) && charCode == 48) {
      return false;
    }
    return true;
  }
</script>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <form:form action="/fdahpStudyDesigner/sessionOut.do" id="backToLoginPage" name="backToLoginPage" method="post"></form:form>
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);"><img
                        src="../images/icons/back-b.png" class="pr-md"/></span>
                <c:if test="${actionType eq 'add'}">Add Questionnaire</c:if>
                <c:if test="${actionType eq 'edit'}">Edit Questionnaire</c:if>
                <c:if test="${actionType eq 'view'}">View Questionnaire <c:set
                        var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span> ':''} ${not empty  sessionScope[isLive]?questionnaireBo.questionnarieVersion:''}
                </c:if>
            </div>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionType != 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <select
                            class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
                            id="studyLanguage" name="studyLanguage" title="Select">
                        <option value="en" ${((currLanguage eq null) or (currLanguage eq '') or (currLanguage eq 'undefined') or (currLanguage eq 'en')) ?'selected':''}>
                            English
                        </option>
                        <c:forEach items="${languageList}" var="language">
                            <option value="${language.key}"
                                ${currLanguage eq language.key ?'selected':''}>${language.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionType == 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
                          data-placement="bottom"
                          title="Language selection is available in edit screen only">
						<select class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
                                title="Select" disabled>
                        <option selected>English</option>
                    </select>
					</span>
                </div>
            </c:if>

            <div class="dis-line form-group mb-none mr-sm">
                <button type="button" class="btn btn-default gray-btn"
                        onclick="goToBackPage(this);">Cancel
                </button>
            </div>
            <c:if test="${actionType ne 'view'}">
                <%-- <c:if test="${empty permission}"> --%>
                <div class="dis-line form-group mb-none mr-sm">
                    <c:choose>
                        <c:when test="${not empty questionnaireBo.id}">
                            <button type="button" class="btn btn-default gray-btn"
                                    id="saveId">Save
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-default gray-btn"
                                    id="saveId">Next
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="dis-line form-group mb-none">
					<span class="tool-tip" data-toggle="tooltip"
                          data-placement="bottom" id="helpNote"
                            <c:if test="${empty questionnaireBo.id}"> title="Please click on Next to continue." </c:if>
						<c:if test="${fn:length(qTreeMap) eq 0 }"> title="Please ensure you add one or more Steps to this questionnaire before attempting to mark this section as Complete." </c:if>
						<c:if test="${!isDone }"> title="Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete." </c:if>>
						<button type="button" class="btn btn-primary blue-btn" id="doneId"
                                <c:if test="${fn:length(qTreeMap) eq 0 || !isDone }">disabled</c:if>>Done</button>
					</span>
                </div>
                <%-- /c:if> --%>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->
    <!--  Start body tab section -->

    <div class="right-content-body pt-none pl-none" id="rootContainer">

    <ul class="nav nav-tabs customTabs" id="tabContainer" role="tablist">
      <li class="nav-item contentqusClass" role="presentation">
        <button class="nav-link active" id="home-tab" data-toggle="tab" data-target="#contentTab" type="button" role="tab" aria-controls="content" aria-selected="true">Content</button>
      </li>
      <li class="nav-item scheduleQusClass" role="presentation">
        <button class="nav-link" id="profile-tab" data-toggle="tab" data-target="#schedule" type="button" role="tab" aria-controls="schedule" aria-selected="false">Schedule</button>
      </li>

    </ul>
    <div class="tab-content pl-xlg pr-xlg mt-lg">
      <div class="tab-pane fade show active" id="contentTab" role="tabpanel" aria-labelledby="home-tab">

        <form:form
                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                              name="contentFormId" id="contentFormId" method="post"
                              data-toggle="validator" role="form">
                          <input type="hidden" name="${csrf.parameterName}"
                                 value="${csrf.token}">
                          <input type="hidden" id="currentLanguage" name="language"
                                 value="${currLanguage}">
                          <input type="hidden" name="type" id="type" value="content">
                          <input type="hidden" name="id" id="id"
                                 value="${questionnaireBo.id}">
                          <input type="hidden" id="mlTitle" value="${questionnaireLangBo.title}">
                          <input type="hidden" name="status" id="status" value="true">
                          <input type="hidden" name="questionnaireId" id="questionnaireId"
                                 value="${questionnaireBo.id}">
                          <input type="hidden" name="studyId" id="studyId"
                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                          <input type="hidden" id="customStudyId"
                                 value="${studyBo.customStudyId}">
                          <input type="hidden" name="instructionId" id="instructionId"
                                 value="">
                          <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
                          <input type="hidden" id="customStudyName"
                                 value="${fn:escapeXml(studyBo.name)}"/>
                          <input type="hidden" name="formId" id="formId" value="">
                          <input type="hidden" name="questionId" id="questionId" value="">
                           <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
                          <!-- <input type="hidden" id="actionType" name="actionType"> -->
                          <input type="hidden" id="actionTypeForQuestionPage"
                                 name="actionTypeForQuestionPage">

                          <select id="questionLangBOList" style="display: none">
                              <c:forEach items="${questionLangBOList}" var="questionLang">
                                  <option id='${questionLang.questionLangPK.id}' status="${questionLang.status}"
                                          formId="${questionLang.formId}" value="${questionLang.question}">${questionLang.question}</option>
                              </c:forEach>
                          </select>

                          <select id="formLangList" style="display: none">
                              <c:forEach items="${formLangList}" var="formLang">
                                  <option id='${formLang.formLangPK.formId}' status="${formLang.status}"></option>
                              </c:forEach>
                          </select>

                          <select id="instructionsLangBOList" style="display: none">
                              <c:forEach items="${instructionsLangBOList}" var="instructionLang">
                                  <option id='${instructionLang.instructionLangPK.id}' status="${instructionLang.status}"
                                          value="${instructionLang.instructionTitle}">${instructionLang.instructionTitle}</option>
                              </c:forEach>
                          </select>

                          <div class="gray-xs-f mb-xs">
                              Activity Short Title or Key (1 to 50 characters)<span
                                  class="requiredStar">*</span><span
                                  class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                  title="A human readable step identifier and must be unique across all activities of the study.Note that this field cannot be edited once the study is Launched."></span>
                          </div>
                          <div class="form-group col-md-5 p-none">
                              <input autofocus="autofocus" type="text" custAttType="cust"
                                     class="form-control" name="shortTitle" id="shortTitleId"
                                     value="${fn:escapeXml(questionnaireBo.shortTitle)}"
                                      <c:if test="${not empty questionnaireBo.shortTitleDuplicate && (questionnaireBo.shortTitleDuplicate gt 0)}"> disabled</c:if>
                                     required="required" maxlength="50"/>
                              <div class="help-block with-errors red-txt"></div>
                              <input type="hidden" id="preShortTitleId"
                                     value="${fn:escapeXml(questionnaireBo.shortTitle)}"/>
                              <!-- ///^[ A-Za-z0-9*()_+|:.-]*$/ -->
                          </div>
                          <div class="clearfix"></div>
                          <div class="gray-xs-f mb-xs">
                              Title (1 to 300 characters)<span class="requiredStar">*</span>
                          </div>
                          <div class="form-group">
                              <input type="text" class="form-control" name="title" id="titleId"
                                     value="${fn:escapeXml(questionnaireBo.title)}" maxlength="300"
                                     required="required"/>
                              <div class="help-block with-errors red-txt"></div>
                          </div>
                          <div class="mt-lg" id="stepContainer">
                              <div
                                      class="add-steps-btn blue-bg <c:if test="${actionType eq 'view' || empty questionnaireBo.id}"> cursor-none </c:if>"
                                      onclick="getQuestionnaireStep('Instruction');">Add
                                  Instruction Step
                              </div>
                              <div
                                      class="add-steps-btn green-bg <c:if test="${actionType eq 'view' || empty questionnaireBo.id}"> cursor-none </c:if>"
                                      onclick="getQuestionnaireStep('Question');">Add Question
                                  Step
                              </div>
                              <div
                                      class="add-steps-btn skyblue-bg <c:if test="${actionType eq 'view' || empty questionnaireBo.id}"> cursor-none </c:if>"
                                      onclick="getQuestionnaireStep('Form');">Add Form Step
                              </div>
                              <span class="sprites_v3 info" id="infoIconId"></span>
                              <div class="pull-right mt-xs">
      							<span class="checkbox checkbox-inline"> <input
                                          type="checkbox" id="branchingId" value="true" name="branching"
                                      ${questionnaireBo.branching ? 'checked':''}> <label
                                          for="branchingId"> Apply Branching </label>
      							</span>
                              </div>
                          </div>
                      </form:form>
                      <div class="mt-md">
                          <table id="content" class="display" cellspacing="0" width="100%"
                                 style="border-color: #ffffff;">
                              <thead style="display: none;"></thead>
                              <tbody>
                              <c:forEach items="${qTreeMap}" var="entry">
                                  <tr id="row_${entry.value.stepId}" type="${entry.value.stepType}" status="${entry.value.status}">
                                      <c:choose>
                                          <c:when test="${entry.value.stepType eq 'Instruction'}">
                                              <td><span id="${entry.key}" data="round blue-round"
                                                        class="round blue-round">${entry.key}</span></td>
                                          </c:when>
                                          <c:when test="${entry.value.stepType eq 'Question'}">
                                              <td><span id="${entry.key}" data="round green-round"
                                                        class="round green-round">${entry.key}</span></td>
                                          </c:when>
                                          <c:otherwise>
                                              <td><span id="${entry.key}" data="round teal-round"
                                                        class="round teal-round">${entry.key}</span></td>
                                              <%-- <c:forEach begin="0" end="${fn:length(entry.value.fromMap)-1}">
      								    <div>&nbsp;</div>
      							 </c:forEach> --%>
                                          </c:otherwise>
                                      </c:choose>
                                      <td class="title"><c:choose>
                                          <c:when test="${entry.value.stepType eq 'Form'}">
                                              <c:forEach items="${entry.value.fromMap}" var="subentry">
                                                  <div class="dis-ellipsis" id="div_${fn:escapeXml(subentry.value.questionInstructionId)}"
                                                       title="${fn:escapeXml(subentry.value.title)}">${subentry.value.title}</div>
                                                  <div class="clearfix"></div>
                                              </c:forEach>
                                          </c:when>
                                          <c:otherwise>
                                              <div class="dis-ellipsis"
                                                   title="${fn:escapeXml(entry.value.title)}">${entry.value.title}</div>
                                          </c:otherwise>
                                      </c:choose></td>
                                      <td>
                                          <div class="destinationStep questionnaireStepClass"
                                               style="display: none;">${entry.value.destinationText}</div>
                                      </td>
                                      <td>
                                          <div>
                                              <div class="text-right pos-relative">
                                                  <c:if test="${entry.value.stepType ne 'Instruction'}">
                                                      <c:choose>
                                                          <c:when
                                                                  test="${entry.value.responseTypeText eq 'Double'  && (entry.value.lineChart eq 'Yes' || entry.value.statData eq 'Yes')}">
                                                              <span class="sprites_v3 status-blue mr-md"></span>
                                                          </c:when>
                                                          <c:when
                                                                  test="${entry.value.responseTypeText eq 'Double' && (entry.value.lineChart eq 'No' && entry.value.statData eq 'No')}">
                                                              <span class="sprites_v3 status-gray mr-md"></span>
                                                          </c:when>
                                                          <c:when
                                                                  test="${entry.value.responseTypeText eq 'Date' && entry.value.useAnchorDate}">
                                                              <span class="sprites_v3 calender-blue mr-md"></span>
                                                          </c:when>
                                                          <c:when
                                                                  test="${entry.value.responseTypeText eq 'Date' && !entry.value.useAnchorDate}">
                                                              <span class="sprites_v3 calender-gray mr-md"></span>
                                                          </c:when>
                                                      </c:choose>
                                                  </c:if>

                                                  <span class="ellipse"
                                                        onmouseenter="ellipseHover(this);"></span>
                                                  <div class="ellipse-hover-icon"
                                                       onmouseleave="ellipseUnHover(this);">
      													<span class="sprites_icon preview-g mr-sm"
                                                                onclick="viewStep(${entry.value.stepId},'${entry.value.stepType}')"></span>
                                                      <span
                                                              class="${entry.value.status?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-sm <c:if test="${actionType eq 'view'}"> cursor-none-without-event </c:if>"
                                                              <c:if test="${actionType ne 'view'}">onclick="editStep(${entry.value.stepId},'${entry.value.stepType}')"</c:if>></span>
                                                      <span
                                                              class="sprites_icon delete deleteStepButton <c:if test="${actionType eq 'view'}"> cursor-none-without-event </c:if>"
                                                              <c:if test="${actionType ne 'view'}">onclick="deletStep(${entry.value.stepId},'${entry.value.stepType}')"</c:if>></span>
                                                  </div>
                                              </div>
                                              <c:if test="${entry.value.stepType eq 'Form'}">
                                                  <c:if test="${fn:length(entry.value.fromMap) gt 0}">
                                                      <c:forEach begin="0"
                                                                 end="${fn:length(entry.value.fromMap)-1}">
                                                          <div>&nbsp;</div>
                                                      </c:forEach>
                                                  </c:if>
                                              </c:if>
                                          </div>
                                      </td>
                                  </tr>
                              </c:forEach>
                              </tbody>
                          </table>
                      </div>


      </div>
      <div class="tab-pane fade" id="schedule" role="tabpanel" aria-labelledby="profile-tab">
         <div class="gray-xs-f mb-sm">Questionnaire Schedule Type</div>
                    <div class="pb-lg ">
      					<span class="radio radio-info radio-inline p-40 pl-1"> <input
                                  type="radio" id="schedule1" class="typeofschedule"
                                  scheduletype="Regular" value="Regular" name="scheduleType"
                              ${empty questionnaireBo.scheduleType  || questionnaireBo.scheduleType=='Regular' ?'checked':''}
                              ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                              <c:if test="${empty anchorTypeList || fn:length(anchorTypeList) le 1}">'disabled'</c:if>>
      						<label for="schedule1">Regular</label>
      					</span> <span id="anchorspanId" class="tool-tip" data-toggle="tooltip"
                                        data-html="true" data-placement="top"
                              <c:if test="${isAnchorQuestionnaire}">
                                  title="This option has been disabled, since this questionnaire has 1 or more Anchor Dates defined in it."
                              </c:if>>
      						<span class="radio radio-inline p-40"> <input type="radio"
                                                                            id="schedule2"
                                                                            class="typeofschedule"
                                                                            scheduletype="AnchorDate"
                                                                            value="AnchorDate"
                                                                            name="scheduleType"
                              ${isAnchorQuestionnaire?'disabled':''}
                              ${questionnaireBo.scheduleType=='AnchorDate' ?'checked':''}
                              ${questionnaireBo.shortTitleDuplicate > 0?'disabled' : ''}
                                                                            <c:if test="${empty anchorTypeList}">disabled</c:if>> <label
                                      for="schedule2">Anchor-Date-based</label>
      					</span>
      					</span>
                      </div>
                       <!-- Anchor date type -->
                                      <form:form action="" name="anchorFormId" id="anchorFormId"
                                                 method="post" role="form" data-toggle="validator">
                                          <div class="anchortypeclass" style="display: none;">
                                              <c:if test="${fn:length(anchorTypeList) gt 0}">
                                                  <div class="gray-xs-f mb-sm">Select Anchor Date Type</div>
                                                  <div class="clearfix"></div>
                                                  <div class="col-md-5 col-lg-5 p-none">
                                                      <div class="form-group">
                                                          <select id="anchorDateId"
                                                                  class="selectpicker ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                  required name="anchorDateId">
                                                              <option value=''>Select</option>
                                                              <c:forEach items="${anchorTypeList}" var="anchorTypeInfo">
                                                                  <option data-id="${anchorTypeInfo.participantProperty}"
                                                                          value="${anchorTypeInfo.id}"
                                                                      ${questionnaireBo.anchorDateId eq anchorTypeInfo.id ? 'selected' : ''}>${anchorTypeInfo.name}</option>
                                                              </c:forEach>
                                                          </select>
                                                          <div class="help-block with-errors red-txt"></div>
                                                      </div>
                                                  </div>
                                                  <div class="clearfix"></div>
                                              </c:if>
                                          </div>
                                      </form:form>
                                      <!-- Ancor date type -->
                                      <div class="gray-xs-f mb-sm">Questionnaire Scheduling Options</div>
                                      <div class="pb-lg b-bor">
                      					<span class="radio radio-info radio-inline p-40 pl-1"> <input
                                                  type="radio" id="inlineRadio1" class="schedule"
                                                  frequencytype="oneTime" value="One time" name="frequency"
                                          ${empty questionnaireBo.frequency  || questionnaireBo.frequency=='One time' ?'checked':''}
                                          ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}>
                      						<label for="inlineRadio1">One Time</label>
                      					</span> <span class="radio radio-inline p-40"> <input type="radio"
                                                                                                id="inlineRadio2"
                                                                                                class="schedule"
                                                                                                frequencytype="daily"
                                                                                                value="Daily"
                                                                                                name="frequency"
                                      ${questionnaireBo.frequency=='Daily' ?'checked':''}
                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                                      ${isAnchorQuestionnaire?'disabled':''}> <label
                                              for="inlineRadio2">Daily</label>
                      					</span> <span class="radio radio-inline p-40"> <input type="radio"
                                                                                                id="inlineRadio3"
                                                                                                class="schedule"
                                                                                                frequencytype="week"
                                                                                                value="Weekly"
                                                                                                name="frequency"
                                      ${questionnaireBo.frequency=='Weekly' ?'checked':''}
                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                                      ${isAnchorQuestionnaire?'disabled':''}> <label
                                              for="inlineRadio3">Weekly</label>
                      					</span> <span class="radio radio-inline p-40"> <input type="radio"
                                                                                                id="inlineRadio4"
                                                                                                class="schedule"
                                                                                                frequencytype="month"
                                                                                                value="Monthly"
                                                                                                name="frequency"
                                      ${questionnaireBo.frequency=='Monthly' ?'checked':''}
                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                                      ${isAnchorQuestionnaire?'disabled':''}> <label
                                              for="inlineRadio4">Monthly</label>
                      					</span> <span class="radio radio-inline p-40"> <input type="radio"
                                                                                                id="inlineRadio5"
                                                                                                class="schedule"
                                                                                                frequencytype="manually"
                                                                                                value="Manually Schedule"
                                                                                                name="frequency"
                                      ${questionnaireBo.frequency=='Manually Schedule' ?'checked':''}
                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                                      ${isAnchorQuestionnaire?'disabled':''}> <label
                                              for="inlineRadio5">Custom Schedule</label>
                      					</span> <span class="radio radio-inline p-40"> <input type="radio"
                                                                                                id="inlineRadio6"
                                                                                                class="schedule"
                                                                                                frequencytype="ongoing"
                                                                                                value="Ongoing"
                                                                                                name="frequency"
                                      ${questionnaireBo.frequency=='Ongoing' ?'checked':''}
                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''}
                                      ${isAnchorQuestionnaire?'disabled':''}> <label
                                              for="inlineRadio6">Ongoing</label>
                      					</span>
                                      </div>
                                      <!-- One Time Section-->
                                      <form:form
                                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                                              name="oneTimeFormId" id="oneTimeFormId" method="post" role="form"
                                              data-toggle="validator">
                                          <input type="hidden" name="frequency" id="frequencyId"
                                                 value="${questionnaireBo.frequency}">
                                          <c:choose>
                                              <c:when test="${questionnaireBo.frequency eq 'Daily'}">
                                                  <c:if
                                                          test="${fn:length(questionnaireBo.questionnairesFrequenciesList) gt 1}">
                                                      <input type="hidden" name="previousFrequency"
                                                             id="previousFrequency" value="${questionnaireBo.frequency}">
                                                  </c:if>
                                                  <c:if
                                                          test="${empty questionnaireBo.questionnairesFrequenciesList || fn:length(questionnaireBo.questionnairesFrequenciesList) le 1}">
                                                      <input type="hidden" name="previousFrequency"
                                                             id="previousFrequency" value="One time">
                                                  </c:if>
                                              </c:when>
                                              <c:otherwise>
                                                  <input type="hidden" name="previousFrequency"
                                                         id="previousFrequency" value="${questionnaireBo.frequency}">
                                              </c:otherwise>
                                          </c:choose>
                                          <input type="hidden" name="id" id="id"
                                                 value="${questionnaireBo.id}">
                                          <input type="hidden" name="type" id="type" value="schedule">
                                          <input type="hidden" name="studyId" id="studyId"
                                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                                          <div class="oneTime all mt-lg">
                                              <div class="gray-xs-f mb-sm">
                                                  Date/Time of launch (pick one) <span class="requiredStar">*</span>
                                              </div>
                                              <div class="mt-sm">
                      							<span class="checkbox checkbox-inline pl-1"> <input
                                                          type="hidden" name="questionnairesFrequenciesBo.id"
                                                          id="oneTimeFreId"
                                                          value="${questionnaireBo.questionnairesFrequenciesBo.id}">
                      								<input type="checkbox" id="isLaunchStudy"
                                                             name="questionnairesFrequenciesBo.isLaunchStudy" value="true"
                                                          ${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy ?'checked':''}
                                                             required ${(questionnaireBo.shortTitleDuplicate>
                                                              0)?'disabled' : ''}> <label for="isLaunchStudy"> Launch
                      									with study</label>
                      							</span>
                                                  <div class="onetimeanchorClass mt-sm" style="display: none">
                                                      <!-- Anchordate start -->
                                                      <div class="opacity06">OR</div>
                                                      <!-- Anchordate start-->
                                                      <div class="mt-none resetDate">
                                                          <div>
                                                              <span class="pr-md">Anchor Date</span> <span> <select
                                                                  class="signDropDown selectpicker sign-box ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                  name="questionnairesFrequenciesBo.xDaysSign"
                                                                  id="onetimeXSign">
                      												<option value="0"
                                                                          ${studyBo.enrollmentdateAsAnchordate?'selected':''}
                                                                          ${not questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>+</option>
                      												<option value="1"
                                                                          ${questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>-</option>
                      										</select>
                      										</span>
                                                              <!--  selectpicker -->
                                                              <span
                                                                      class="form-group m-none dis-inline vertical-align-middle">
                      											<c:choose>
                                                                      <c:when
                                                                              test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy}">
                      													<input id="onetimexdaysId" type="text"
                                                                                 class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                 placeholder="X"
                                                                                 name="questionnairesFrequenciesBo.timePeriodFromDays"
                                                                                 value=""
                                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy }"> disabled </c:if>
                                                                                 maxlength="3" pattern="[0-9]+"
                                                                                 data-pattern-error="Please enter valid number."/>
                                                                      </c:when>
                                                                      <c:otherwise>
                      													<input id="onetimexdaysId" type="text"
                                                                                 class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                 placeholder="X"
                                                                                 name="questionnairesFrequenciesBo.timePeriodFromDays"
                                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.timePeriodFromDays}"
                                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy }"> disabled </c:if>
                                                                                 maxlength="3" pattern="[0-9]+"
                                                                                 data-pattern-error="Please enter valid number."/>
                                                                      </c:otherwise>
                                                                  </c:choose> <span
                                                                      class="help-block with-errors red-txt"></span>
                      										</span> <span class="mb-sm pr-md"> <span
                                                                  class="light-txt opacity06"> days</span>
                      										</span> <span
                                                                  class="form-group m-none dis-inline vertical-align-middle pr-md">
                      											<input id="selectTime" type="text"
                                                                         class=" form-control clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                         name="questionnairesFrequenciesBo.frequencyTime"
                                                                         value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"
                                                                          <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy}"> disabled </c:if>
                                                                         placeholder="Select Time"/> <span
                                                                  class='help-block with-errors red-txt'></span>
                      										</span>
                                                          </div>
                                                      </div>
                                                      <!-- Anchordate End -->
                                                  </div>


                                                  <div class="mt-md form-group regularClass">
                      								<span
                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                      									<input id="chooseDate" type="text"
                                                                 class="mt-sm form-control calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                 name="questionnairesFrequenciesBo.frequencyDate"
                                                                 placeholder="Choose Date"
                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.frequencyDate}"
                                                                 required
                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy}"> disabled </c:if> />
                      									<span class='help-block with-errors red-txt'></span>
                      								</span> <span
                                                          class="form-group m-none dis-inline vertical-align-middle pr-md">
                      									<input id="selectTime1" type="text"
                                                                 class="mt-sm form-control clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                 name="questionnairesFrequenciesBo.frequencyTime"
                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"
                                                                 required
                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isLaunchStudy}"> disabled </c:if>
                                                                 placeholder="Select Time"/> <span
                                                          class='help-block with-errors red-txt'></span>
                      								</span>
                                                  </div>
                                              </div>
                                              <!-- <div class="gray-xs-f mb-sm mt-md">Lifetime of the run and of the questionnaire (pick one)<span class="requiredStar">*</span></div> -->
                                              <div class="gray-xs-f mb-sm mt-md">
                                                  Lifetime of the run/questionnaire (choose between Study Lifetime
                                                  and custom end date)<span class="requiredStar">*</span>
                                              </div>
                                              <div class="mt-sm">
                      							<span class="checkbox checkbox-inline pl-1"> <input
                                                          type="checkbox" id="isStudyLifeTime"
                                                          name="questionnairesFrequenciesBo.isStudyLifeTime" value="true"
                                                      ${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime ?'checked':''}
                                                          required ${(questionnaireBo.shortTitleDuplicate>
                                                          0)?'disabled' : ''}> <label for="isStudyLifeTime"> Study
                      									Lifetime </label>
                      							</span>
                                                  <div class="mt-md form-group regularClass">
                      								<span
                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                      									<c:choose>
                                                              <c:when
                                                                      test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime}">
                      											<input id="chooseEndDate" type="text"
                                                                         class="form-control calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                         name="studyLifetimeEnd"
                                                                         placeholder="Choose End Date"
                                                                         required
                                                                          <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime }"> disabled </c:if>
                                                                         value=""/>
                                                              </c:when>
                                                              <c:otherwise>
                      											<input id="chooseEndDate" type="text"
                                                                         class="form-control calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                         name="studyLifetimeEnd"
                                                                         placeholder="Choose End Date"
                                                                         required
                                                                          <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime }"> disabled </c:if>
                                                                         value="${questionnaireBo.studyLifetimeEnd}"/>
                                                              </c:otherwise>
                                                          </c:choose> <span class='help-block with-errors red-txt'></span>
                      								</span>
                                                  </div>
                                                  <div class="onetimeanchorClass mt-sm" style="display: none">
                                                      <div class="opacity06">OR</div>
                                                      <!-- Anchordate start-->
                                                      <div class="mt-none resetDate">
                                                          <div>
                                                              <span class="pr-md">Anchor Date</span> <span> <select
                                                                  class="signDropDown selectpicker sign-box ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                  title="Select"
                                                                  name="questionnairesFrequenciesBo.yDaysSign"
                                                                  id="onetimeYSign">
                      												<option value="0"
                                                                          ${not questionnaireBo.questionnairesFrequenciesBo.yDaysSign ?'selected':''}>+</option>
                      												<option value="1"
                                                                          ${questionnaireBo.questionnairesFrequenciesBo.yDaysSign ?'selected':''}>-</option>
                      										</select>
                      										</span>
                                                              <!--  selectpicker -->
                                                              <span
                                                                      class="form-group m-none dis-inline vertical-align-middle">
                      											<c:choose>
                                                                      <c:when
                                                                              test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime}">
                      													<input id="onetimeydaysId" type="text"
                                                                                 class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                 placeholder="Y"
                                                                                 name="questionnairesFrequenciesBo.timePeriodToDays"
                                                                                 value=""
                                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime }"> disabled </c:if>
                                                                                 maxlength="3" pattern="[0-9]+"
                                                                                 data-pattern-error="Please enter valid number."/>
                                                                      </c:when>
                                                                      <c:otherwise>
                      													<input id="onetimeydaysId" type="text"
                                                                                 class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask  ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                 placeholder="Y"
                                                                                 name="questionnairesFrequenciesBo.timePeriodToDays"
                                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.timePeriodToDays}"
                                                                                  <c:if test="${questionnaireBo.questionnairesFrequenciesBo.isStudyLifeTime}"> disabled </c:if>
                                                                                 maxlength="3" pattern="[0-9]+"
                                                                                 data-pattern-error="Please enter valid number."/>
                                                                      </c:otherwise>
                                                                  </c:choose> <span
                                                                      class="help-block with-errors red-txt"></span>
                      										</span> <span class="mb-sm pr-md"> <span
                                                                  class="light-txt opacity06"> days</span>
                      										</span>
                                                          </div>
                                                      </div>
                                                  </div>
                                                  <!-- Anchordate End -->
                                              </div>
                                          </div>
                                      </form:form>
                                       <!-- Daily Section-->
                                                      <form:form
                                                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                                                              name="dailyFormId" id="dailyFormId" method="post" role="form"
                                                              data-toggle="validator">
                                                          <input type="hidden" name="frequency" id="dailyFrequencyId"
                                                                 value="${questionnaireBo.frequency}">
                                                          <input type="hidden" name="previousFrequency"
                                                                 id="previousFrequency" value="${questionnaireBo.frequency}">
                                                          <input type="hidden" name="id" id="id"
                                                                 value="${questionnaireBo.id}">
                                                          <input type="hidden" name="studyId" id="studyId"
                                                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                                                          <input type="hidden" name="type" id="type" value="schedule">
                                                          <div class="daily all mt-lg dis-none">
                                                              <div class="gray-xs-f mb-sm">
                                                                  Time(s) of the day for daily occurrence <span
                                                                      class="requiredStar">*</span>
                                                              </div>
                                                              <div class="dailyContainer">
                                                                  <c:if
                                                                          test="${fn:length(questionnaireBo.questionnairesFrequenciesList) eq 0}">
                                                                      <div class="time-opts mt-md dailyTimeDiv" id="0">
                                      									<span
                                                                                  class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      										<input id="time0" type="text"
                                                                                     name="questionnairesFrequenciesList[0].frequencyTime"
                                                                                     required
                                                                                     class="form-control clock dailyClock"
                                                                                     placeholder="Time"
                                                                                     onclick='timep(this.id);'/> <span
                                                                                  class='help-block with-errors red-txt'></span>
                                      									</span> <span class="addBtnDis addbtn mr-sm align-span-center"
                                                                                        onclick='addTime();'>+</span>
                                                                          <!-- <span
                                      										class="delete vertical-align-middle remBtnDis hide pl-md align-span-center"
                                      										onclick='removeTime(this);'></span> -->
                                                                      </div>
                                                                  </c:if>
                                                                  <c:if
                                                                          test="${fn:length(questionnaireBo.questionnairesFrequenciesList) gt 0}">
                                                                      <c:forEach
                                                                              items="${questionnaireBo.questionnairesFrequenciesList}"
                                                                              var="questionnairesFrequencies" varStatus="frequeincesVar">
                                                                          <div class="time-opts mt-md dailyTimeDiv"
                                                                               id="${frequeincesVar.index}">
                                                                              <input type="hidden"
                                                                                     name="questionnairesFrequenciesList[${frequeincesVar.index}].id"
                                                                                     value="${questionnairesFrequencies.id}"> <span
                                                                                  class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      											<input id="time${frequeincesVar.index}" type="text"
                                                                                         name="questionnairesFrequenciesList[${frequeincesVar.index}].frequencyTime"
                                                                                         required
                                                                                         class="form-control clock dailyClock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                         placeholder="Time" onclick='timep(this.id);'
                                                                                         value="${questionnairesFrequencies.frequencyTime}"
                                                                                      ${(questionnaireBo.shortTitleDuplicate > 0)?'disabled' : ''} />
                                      											<span class='help-block with-errors red-txt'></span>
                                      										</span> <span
                                                                                  class="addBtnDis addbtn mr-sm align-span-center ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                  onclick='addTime();'>+</span> <span
                                                                                  class="delete vertical-align-middle remBtnDis hide pl-md align-span-center ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                  onclick='removeTime(this);'></span>
                                                                          </div>
                                                                      </c:forEach>
                                                                  </c:if>
                                                              </div>
                                                              <div class="mt-md row">
                                                                  <div class="dailyStartCls col-md-3 pl-none">
                                      								<span
                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      									<span class="gray-xs-f">Start date (pick a date) <span
                                                                                  class="requiredStar">*</span></span><br/> <input
                                                                              id="startDate"
                                                                              type="text"
                                                                              class="form-control mt-sm calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                              placeholder="Choose Date" name="studyLifetimeStart"
                                                                              value="${questionnaireBo.studyLifetimeStart}"/> <span
                                                                              class='help-block with-errors red-txt'></span>
                                      								</span>
                                                                  </div>
                                                                  <!-- Anchordate start-->
                                                                  <div class="dailyanchorDiv col-md-4 pl-none"
                                                                       style="display: none;">
                                                                      <div class=" resetDate">
                                                                          <div>
                                      										<span
                                                                                      class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      											<span class="gray-xs-f">Start date (pick a date) <span
                                                                                          class="requiredStar">*</span></span>
                                                                                             <div class="mt-sm">
                                                                                           <span
                                                                                      class="pr-md">Anchor
                                      												Date</span> <span> <select
                                                                                      class="signDropDown selectpicker sign-box ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                      title="Select"
                                                                                      name="questionnairesFrequenciesList[0].xDaysSign"
                                                                                      id="dailyXSign">
                                      													<option value="0"
                                                                                              ${(fn:length(questionnaireBo.questionnairesFrequenciesList) gt 0) && not questionnaireBo.questionnairesFrequenciesList[0].xDaysSign ?'selected':''}>+</option>
                                      													<option value="1"
                                                                                              ${(fn:length(questionnaireBo.questionnairesFrequenciesList) gt 0) && questionnaireBo.questionnairesFrequenciesList[0].xDaysSign ?'selected':''}>-</option>
                                      											</select>
                                      										</span> <span
                                                                                      class="form-group m-none dis-inline vertical-align-middle">
                                      												<input id="dailyxdaysId" type="text"
                                                                                             class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask  ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                             placeholder="X"
                                                                                             name="questionnairesFrequenciesList[0].timePeriodFromDays"
                                                                                             value="${(fn:length(questionnaireBo.questionnairesFrequenciesList) gt 0)?questionnaireBo.questionnairesFrequenciesList[0].timePeriodFromDays:''}"
                                                                                             maxlength="3" pattern="[0-9]+"
                                                                                             data-pattern-error="Please enter valid number."/> <span
                                                                                      class="help-block with-errors red-txt"></span>
                                      										</span> <span class="mb-sm pr-md"> <span
                                                                                      class="light-txt opacity06"> days</span>
                                      										</span>
                                      										</div>
                                      										</span>
                                                                          </div>
                                                                      </div>
                                                                  </div>
                                                                  <!-- Anchordate End -->
                                                                  <div class="col-md-6 pr-none">
                                      								<span
                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      									<span class="gray-xs-f">No. of times to repeat the
                                      										questionnaire <span class="requiredStar">*</span>
                                      								</span> <div class=""> <input id="days" type="text"
                                                                                          class="form-control mt-sm numChk ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                          name="repeatQuestionnaire"
                                                                                          placeholder="No of Times" required
                                                                                          value="${questionnaireBo.repeatQuestionnaire}"
                                                                                          onkeypress="return isNumber(event, this)"
                                                                                          pattern="^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$"
                                                                                          data-pattern-error="Please enter valid number."
                                                                                          maxlength="3"/>
                                      									<span class='help-block with-errors red-txt'></span>
                                      								</span>
                                      								</div>
                                                                  </div>
                                                                  <div class="clearfix"></div>
                                                              </div>
                                                              <div class="mt-md">
                                                                  <div class="gray-xs-f mb-xs">End Date</div>
                                                                  <div class="black-xs-f"
                                                                       id="endDateId">${not empty questionnaireBo.studyLifetimeEnd ? questionnaireBo.studyLifetimeEnd :'NA'}</div>
                                                                  <input type="hidden" name="studyLifetimeEnd"
                                                                         id="studyDailyLifetimeEnd"
                                                                         value="${questionnaireBo.studyLifetimeEnd}">
                                                              </div>
                                                              <div class="mt-lg">
                                                                  <div class="gray-xs-f mb-xs">Lifetime of each run</div>
                                                                  <div class="black-xs-f">Until the next run comes up</div>
                                                              </div>
                                                              <div class="mt-lg">
                                                                  <div class="gray-xs-f mb-xs">Lifetime of the questionnaire
                                                                  </div>
                                                                  <div class="black-xs-f"
                                                                       id="lifeTimeId">${questionnaireBo.studyLifetimeStart}
                                                                      - ${questionnaireBo.studyLifetimeEnd}</div>
                                                              </div>
                                                          </div>
                                                      </form:form>
                                                      <!-- Weekly Section-->
                                                      <form:form
                                                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                                                              name="weeklyFormId" id="weeklyFormId" method="post" role="form"
                                                              data-toggle="validator">
                                                          <input type="hidden" name="frequency" id="weeklyfrequencyId">
                                                          <input type="hidden" name="previousFrequency"
                                                                 id="previousFrequency" value="${questionnaireBo.frequency}">
                                                          <input type="hidden" name="id" id="id"
                                                                 value="${questionnaireBo.id}">
                                                          <input type="hidden" name="studyId" id="studyId"
                                                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                                                          <input type="hidden" name="questionnairesFrequenciesBo.id"
                                                                 id="weeklyFreId"
                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.id}">
                                                          <input type="hidden" name="type" id="type" value="schedule">
                                                          <div class="week all mt-lg dis-none">
                                                              <div id="weekDaysId" class="weeklyCls">
                                      							<span class="gray-xs-f">Day/Time (of the week) <span
                                                                          class="requiredStar">*</span><br/> <span
                                                                          class=" form-group m-none dis-inline vertical-align-middle pr-lg">
                                      									<span class=""> <select id="startDateWeekly"
                                                                                                  class="form-control mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''} weeklyCls"
                                                                                                  name="dayOfTheWeek" required>
                                      											<option value=''>Select</option>
                                      											<option value='Sunday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Sunday' ? 'selected':''}>Sunday</option>
                                      											<option value='Monday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Monday' ?'selected':''}>Monday</option>
                                      											<option value='Tuesday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Tuesday' ?'selected':''}>Tuesday</option>
                                      											<option value='Wednesday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Wednesday' ?'selected':''}>Wednesday</option>
                                      											<option value='Thursday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Thursday' ?'selected':''}>Thursday</option>
                                      											<option value='Friday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Friday' ?'selected':''}>Friday
                                      											</option>
                                      											<option value='Saturday'
                                                                                      ${questionnaireBo.dayOfTheWeek eq 'Saturday' ?'selected':''}>Saturday</option>
                                      									</select> <span class='help-block with-errors red-txt'></span>
                                      								</span>
                                      							</span>
                                      							</span> <span
                                                                      class="form-group m-none dis-inline vertical-align-middle pr-md pl-lg">
                                      								<!-- <span class="gray-xs-f">&nbsp;</span><br/> --> <input
                                                                      id="selectWeeklyTime" type="text"
                                                                      class="form-control mt-sm clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''} weeklyCls"
                                                                      required onclick="timep(this.id)" placeholder="Time"
                                                                      name="questionnairesFrequenciesBo.frequencyTime"
                                                                      value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"/>
                                      								<span class='help-block with-errors red-txt'></span>
                                      							</span>
                                                              </div>
                                                              <div class="mt-md row">
                                                                  <div class="weeklyStartCls col-md-3 pl-none">
                                      								<span
                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      									<span class="gray-xs-f">Start date (pick a date) <span
                                                                                  class="requiredStar">*</span></span><br/> <input
                                                                              id="startWeeklyDate" type="text"
                                                                              class="form-control mt-sm calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                              required name="studyLifetimeStart" placeholder="Choose Date"
                                                                              value="${questionnaireBo.studyLifetimeStart}"
                                                                              readonly="readonly"/> <span
                                                                              class='help-block with-errors red-txt'></span>
                                      								</span>
                                                                  </div>
                                                                  <!-- Anchordate start-->
                                                                  <div class="weeklyanchorDiv col-md-12 pl-none"
                                                                       style="display: none;">
                                                                      <div class=" resetDate dis_inlinetop p-none">
                                                                          <div>
                                      										<span
                                                                                      class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      											<span class="gray-xs-f">Start date (pick a date) <span
                                                                                          class="requiredStar">*</span></span><br/> <span
                                                                                      class="pr-md">Anchor
                                      												Date</span> <span> <select
                                                                                      class="signDropDown selectpicker sign-box mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                      title="Select"
                                                                                      name="questionnairesFrequenciesBo.xDaysSign"
                                                                                      id="weeklyXSign">
                                      													<option value="0"
                                                                                              ${not questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>+</option>
                                      													<option value="1"
                                                                                              ${questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>-</option>
                                      											</select>
                                      										</span> <span
                                                                                      class="form-group m-none dis-inline vertical-align-middle">
                                      												<input id="weeklyxdaysId" type="text"
                                                                                             class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                             placeholder="X"
                                                                                             name="questionnairesFrequenciesBo.timePeriodFromDays"
                                                                                             value="${questionnaireBo.questionnairesFrequenciesBo.timePeriodFromDays}"
                                                                                             maxlength="3" pattern="[0-9]+"
                                                                                             data-pattern-error="Please enter valid number."/> <span
                                                                                      class="help-block with-errors red-txt"></span>
                                      										</span> <span class="mb-sm pr-md"> <span
                                                                                      class="light-txt opacity06"> days</span>
                                      										</span>
                                      										</span>
                                                                          </div>
                                                                      </div>
                                                                      <div class="dis_inlinetop p-none">
                                                                          <span class="gray-xs-f">Time <span class="requiredStar">*</span><br/></span>
                                                                          <span
                                                                                  class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      										<input id="selectWeeklyTimeAnchor" type="text"
                                                                                     class="form-control mt-sm clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                     required onclick="timep(this.id)" placeholder="Time"
                                                                                     name="questionnairesFrequenciesBo.frequencyTime"
                                                                                     value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"/>
                                      										<span class='help-block with-errors red-txt'></span>
                                      									</span>
                                                                      </div>

                                                                      <div class="dis_inlinetop">
                                      									<span
                                                                                  class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      										<span class="gray-xs-f">No. of times to repeat the
                                      											questionnaire <span class="requiredStar">*</span>
                                      									</span><br/> <input id="weeksAnchor" type="text"
                                                                                              class="form-control mt-sm numChk ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                              name="repeatQuestionnaire"
                                                                                              placeholder="No of Times"
                                                                                              value="${questionnaireBo.repeatQuestionnaire}"
                                                                                              required
                                                                                              onkeypress="return isNumber(event, this)"
                                                                                              pattern="^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$"
                                                                                              data-pattern-error="Please enter valid number."
                                                                                              maxlength="3"/>
                                      										<span class='help-block with-errors red-txt'></span>
                                      									</span>
                                                                      </div>

                                                                  </div>

                                                                  <!-- Anchordate End -->
                                                                  <div class="col-md-5 p-none weeklyRegular">
                                      								<span
                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                      									<span class="gray-xs-f">No. of times to repeat the
                                      										questionnaire <span class="requiredStar">*</span>
                                      								</span><br/> <input id="weeks" type="text"
                                                                                          class="form-control mt-sm numChk ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                          name="repeatQuestionnaire"
                                                                                          placeholder="No of Times"
                                                                                          value="${questionnaireBo.repeatQuestionnaire}"
                                                                                          required
                                                                                          onkeypress="return isNumber(event, this)"
                                                                                          pattern="^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$"
                                                                                          data-pattern-error="Please enter valid number."
                                                                                          maxlength="3"/>
                                      									<span class='help-block with-errors red-txt'></span>
                                      								</span>
                                                                  </div>
                                                                  <div class="clearfix"></div>
                                                              </div>
                                                              <div class="mt-md">
                                                                  <div class="gray-xs-f mb-xs">End Date</div>
                                                                  <div class="black-xs-f"
                                                                       id="weekEndDate">${not empty questionnaireBo.studyLifetimeEnd ? questionnaireBo.studyLifetimeEnd :'NA'}</div>
                                                                  <input type="hidden" name="studyLifetimeEnd"
                                                                         id="studyWeeklyLifetimeEnd"
                                                                         value="${questionnaireBo.studyLifetimeEnd}">
                                                              </div>
                                                              <div class="mt-lg">
                                                                  <div class="gray-xs-f mb-xs">Lifetime of each run</div>
                                                                  <div class="black-xs-f">Until the next run comes up</div>
                                                              </div>
                                                              <div class="mt-lg">
                                                                  <div class="gray-xs-f mb-xs">Lifetime of the questionnaire
                                                                  </div>
                                                                  <div class="black-xs-f"
                                                                       id="weekLifeTimeEnd">${questionnaireBo.studyLifetimeStart}
                                                                      - ${questionnaireBo.studyLifetimeEnd}</div>
                                                              </div>
                                                          </div>
                                                      </form:form>
                                                        <!-- Monthly Section-->
                                                                      <form:form
                                                                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                                                                              name="monthlyFormId" id="monthlyFormId" method="post" role="form"
                                                                              data-toggle="validator">
                                                                          <input type="hidden" name="frequency" id="monthlyfrequencyId"
                                                                                 value="${questionnaireBo.frequency}">
                                                                          <input type="hidden" name="previousFrequency"
                                                                                 id="previousFrequency" value="${questionnaireBo.frequency}">
                                                                          <input type="hidden" name="id" id="id"
                                                                                 value="${questionnaireBo.id}">
                                                                          <input type="hidden" name="studyId" id="studyId"
                                                                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                                                                          <input type="hidden" name="questionnairesFrequenciesBo.id"
                                                                                 id="monthFreId"
                                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.id}">
                                                                          <input type="hidden" name="type" id="type" value="schedule">
                                                                          <div class="month all mt-lg dis-none">
                                                                              <div id="monthlyDateId">
                                                      							<span class="gray-xs-f">Select Date/Time (of the month) <span
                                                                                          class="requiredStar">*</span></span><br/> <span
                                                                                      class="monthlyStartCls form-group m-none dis-inline vertical-align-middle pr-md">
                                                      								<span class=""> <input id="startDateMonthly" type="text"
                                                                                                             class="form-control mt-sm calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                             required placeholder="Choose Date"
                                                                                                             name="questionnairesFrequenciesBo.frequencyDate"
                                                                                                             value="${questionnaireBo.questionnairesFrequenciesBo.frequencyDate}"/>
                                                      									<span class='help-block with-errors red-txt'></span>
                                                      							</span>
                                                      							</span> <span
                                                                                      class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      								<!-- 	                  <span class="gray-xs-f">&nbsp;</span><br/> -->
                                                      								<input id="selectMonthlyTime" type="text"
                                                                                             class="form-control mt-sm clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                             required onclick="timep(this.id)" placeholder="Time"
                                                                                             name="questionnairesFrequenciesBo.frequencyTime"
                                                                                             value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"/>
                                                      								<span class='help-block with-errors red-txt'></span>
                                                      							</span>
                                                                                  <div
                                                                                          class="gray-xs-f mt-sm mb-lg italic-txt text-weight-light monthlyStartCls">
                                                                                      If
                                                                                      the selected date is not available in a month, the last day of
                                                                                      the month will be used instead
                                                                                  </div>
                                                                                  <div class="monthlyStartCls dis_inlinetop p-none">
                                                      								<span
                                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      									<span class="gray-xs-f">Start date (pick a date) <span
                                                                                                  class="requiredStar">*</span></span><br/> <input
                                                                                              id="pickStartDate"
                                                                                              type="text"
                                                                                              class="form-control mt-sm calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                              placeholder="Choose Start Date" required
                                                                                              name="studyLifetimeStart"
                                                                                              value="${questionnaireBo.studyLifetimeStart}"
                                                                                              readonly="readonly"/> <span
                                                                                              class='help-block with-errors red-txt'></span>
                                                      								</span>
                                                                                  </div>
                                                                                  <div class="dis_inlinetop p-none monthlyRegular">
                                                      								<span
                                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      									<span class="gray-xs-f">No. of times to repeat the
                                                      										questionnaire <span class="requiredStar">*</span>
                                                      								</span><br/> <input id="months" type="text"
                                                                                                          class="form-control mt-sm numChk ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                          name="repeatQuestionnaire"
                                                                                                          placeholder="No of Times" required
                                                                                                          value="${questionnaireBo.repeatQuestionnaire}"
                                                                                                          onkeypress="return isNumber(event, this)"
                                                                                                          pattern="^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$"
                                                                                                          data-pattern-error="Please enter valid number."
                                                                                                          maxlength="3"/>
                                                      									<span class='help-block with-errors red-txt'></span>
                                                      								</span>
                                                                                  </div>
                                                                              </div>
                                                                              <!-- Anchordate start-->
                                                                              <!-- <div class="mt-lg"> -->
                                                                                  <%-- <div class="monthlyStartCls dis_inlinetop p-none">
                                                      								<span
                                                      									class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      									<span class="gray-xs-f">Start date (pick a date) <span
                                                      										class="requiredStar">*</span></span><br /> <input id="pickStartDate"
                                                      									type="text"
                                                      									class="form-control mt-sm calendar ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                      									placeholder="Choose Start Date" required
                                                      									name="studyLifetimeStart"
                                                      									value="${questionnaireBo.studyLifetimeStart}"
                                                      									readonly="readonly" /> <span
                                                      									class='help-block with-errors red-txt'></span>
                                                      								</span>
                                                      							</div> --%>
                                                                              <div class="monthlyanchorDiv" style="display: none;">
                                                                                  <div class="dis_inlinetop p-none">
                                                                                      <div class=" resetDate dis_inlinetop p-none">
                                                                                          <div>
                                                      										<span
                                                                                                      class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      											<span class="gray-xs-f">Start date (pick a date) <span
                                                                                                          class="requiredStar">*</span></span><br/> <span
                                                                                                      class="pr-md">Anchor
                                                      												Date</span> <span> <select
                                                                                                      class="signDropDown selectpicker sign-box  mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                      title="Select"
                                                                                                      name="questionnairesFrequenciesBo.xDaysSign"
                                                                                                      id="monthlyXSign">
                                                      													<option value="0"
                                                                                                              ${not questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>+</option>
                                                      													<option value="1"
                                                                                                              ${questionnaireBo.questionnairesFrequenciesBo.xDaysSign ?'selected':''}>-</option>
                                                      											</select>
                                                      										</span> <span
                                                                                                      class="form-group m-none dis-inline vertical-align-middle">
                                                      												<input id="monthlyxdaysId" type="text"
                                                                                                             class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave daysMask mt-sm ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                             placeholder="X"
                                                                                                             name="questionnairesFrequenciesBo.timePeriodFromDays"
                                                                                                             value="${questionnaireBo.questionnairesFrequenciesBo.timePeriodFromDays}"
                                                                                                             maxlength="3" pattern="[0-9]+"
                                                                                                             data-pattern-error="Please enter valid number."/> <span
                                                                                                      class="help-block with-errors red-txt"></span>
                                                      										</span> <span class="mb-sm pr-md"> <span
                                                                                                      class="light-txt opacity06">days</span>
                                                      										</span>
                                                      										</span>
                                                                                          </div>
                                                                                      </div>
                                                                                  </div>


                                                                                  <div class="dis_inlinetop p-none">
                                                                                      <span class="gray-xs-f">Time <span
                                                                                              class="requiredStar">*</span></span><br/>
                                                                                      <span
                                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      									<input id="selectMonthlyTimeAnchor" type="text"
                                                                                                 class="form-control mt-sm clock ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                 required onclick="timep(this.id)" placeholder="Time"
                                                                                                 name="questionnairesFrequenciesBo.frequencyTime"
                                                                                                 value="${questionnaireBo.questionnairesFrequenciesBo.frequencyTime}"/>
                                                      									<span class='help-block with-errors red-txt'></span>
                                                      								</span>
                                                                                  </div>
                                                                                  <div class="dis_inlinetop">
                                                      								<span
                                                                                              class="form-group m-none dis-inline vertical-align-middle pr-md">
                                                      									<span class="gray-xs-f">No. of times to repeat the
                                                      										questionnaire <span class="requiredStar">*</span>
                                                      								</span><br/> <input id="monthsAnchor" type="text"
                                                                                                          class="form-control mt-sm numChk ${(questionnaireBo.shortTitleDuplicate > 0)?'cursor-none' : ''}"
                                                                                                          name="repeatQuestionnaire"
                                                                                                          placeholder="No of Times" required
                                                                                                          value="${questionnaireBo.repeatQuestionnaire}"
                                                                                                          onkeypress="return isNumber(event, this)"
                                                                                                          pattern="^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$"
                                                                                                          data-pattern-error="Please enter valid number."
                                                                                                          maxlength="3"/>
                                                      									<span class='help-block with-errors red-txt'></span>
                                                      								</span>
                                                                                  </div>
                                                                              </div>
                                                                              <!-- </div> -->
                                                                              <!-- Anchordate End -->

                                                                              <div class="mt-md col-md-12 p-none">
                                                                                  <div class="gray-xs-f mb-xs">End Date</div>
                                                                                  <div class="black-xs-f"
                                                                                       id="monthEndDate">${not empty questionnaireBo.studyLifetimeEnd ? questionnaireBo.studyLifetimeEnd :'NA'}</div>
                                                                                  <input type="hidden" name="studyLifetimeEnd"
                                                                                         id="studyMonthlyLifetimeEnd"
                                                                                         value="${questionnaireBo.studyLifetimeEnd}">
                                                                              </div>
                                                                              <div class="mt-lg col-md-12 p-none">
                                                                                  <div class="gray-xs-f mb-xs">Lifetime of each run</div>
                                                                                  <div class="black-xs-f">Until the next run comes up</div>
                                                                              </div>
                                                                              <div class="mt-lg col-md-12 p-none">
                                                                                  <div class="gray-xs-f mb-xs">Lifetime of the questionnaire
                                                                                  </div>
                                                                                  <div class="black-xs-f"
                                                                                       id="monthLifeTimeDate">${questionnaireBo.studyLifetimeStart}
                                                                                      - ${questionnaireBo.studyLifetimeEnd}</div>
                                                                              </div>
                                                                          </div>
                                                                      </form:form>
                                                                      <!-- Manually Section-->
                                                                      <form:form
                                                                              action="/fdahpStudyDesigner/adminStudies/saveorUpdateQuestionnaireSchedule.do?_S=${param._S}"
                                                                              name="customFormId" id="customFormId" method="post" role="form"
                                                                              data-toggle="validator">
                                                                          <input type="hidden" name="id" id="id"
                                                                                 value="${questionnaireBo.id}">
                                                                          <input type="hidden" name="studyId" id="studyId"
                                                                                 value="${not empty questionnaireBo.studyId ? questionnaireBo.studyId : studyBo.id}">
                                                                          <input type="hidden" name="frequency" id="customfrequencyId"
                                                                                 value="${questionnaireBo.frequency}">
                                                                          <input type="hidden" name="previousFrequency"
                                                                                 id="previousFrequency" value="${questionnaireBo.frequency}">
                                                                          <input type="hidden" name="type" id="type" value="schedule">
                                                                          <div class="manually all mt-lg dis-none">
                                                                              <div class="gray-xs-f mb-sm">
                                                                                  Select time period <span class="requiredStar">*</span>
                                                                              </div>
                                                                              <div class="manuallyContainer">
                                                                                  <c:if
                                                                                          test="${fn:length(questionnaireBo.questionnaireCustomScheduleBo) eq 0}">
                                                                                      <div class="manually-option mb-md form-group" id="0">
                                                                                          <input type="hidden"
                                                                                                 name="questionnaireCustomScheduleBo[0].questionnairesId"
                                                                                                 id="questionnairesId" value="${questionnaireBo.id}">
                                                                                          <span
                                                                                                  class="form-group  dis-inline vertical-align-middle pr-md">
                                                      										<input id="StartDate0" type="text" count='0'
                                                                                                     class="form-control calendar customCalnder cusStrDate"
                                                                                                     name="questionnaireCustomScheduleBo[0].frequencyStartDate"
                                                                                                     value="" placeholder="Start Date"
                                                                                                     onclick='customStartDate(this.id,0);'
                                                                                                     required/> <span
                                                                                                  class='help-block with-errors red-txt'></span>
                                                      									</span> <span class="gray-xs-f mb-sm pr-md align-span-center">
                                                      										to </span> <span
                                                                                              class="form-group dis-inline vertical-align-middle pr-md">
                                                      										<input id="EndDate0" type="text" count='0'
                                                                                                     class="form-control calendar customCalnder cusEndDate"
                                                                                                     name="questionnaireCustomScheduleBo[0].frequencyEndDate"
                                                                                                     placeholder="End Date"
                                                                                                     onclick='customEndDate(this.id,0);'
                                                                                                     required/> <span
                                                                                              class='help-block with-errors red-txt'></span>
                                                      									</span> <span
                                                                                              class="form-group  dis-inline vertical-align-middle pr-md">
                                                      										<input id="customTime0" type="text" count='0'
                                                                                                     class="form-control clock cusTime"
                                                                                                     name="questionnaireCustomScheduleBo[0].frequencyTime"
                                                                                                     placeholder="Time" onclick='timep(this.id);' disabled
                                                                                                     required/>
                                                      										<span class='help-block with-errors red-txt'></span>
                                                      									</span> <span class="addbtn addBtnDis align-span-center mr-md"
                                                                                                        onclick="addDate();">+</span> <span id="delete"
                                                                                                                                            class="sprites_icon delete vertical-align-middle remBtnDis hide align-span-center"
                                                                                                                                            onclick="removeDate(this);"></span>
                                                                                      </div>
                                                                                  </c:if>
                                                                                  <c:if
                                                                                          test="${fn:length(questionnaireBo.questionnaireCustomScheduleBo) gt 0}">
                                                                                      <c:forEach
                                                                                              items="${questionnaireBo.questionnaireCustomScheduleBo}"
                                                                                              var="questionnaireCustomScheduleBo" varStatus="customVar">
                                                                                          <div class="manually-option mb-md form-group"
                                                                                               id="${customVar.index}">
                                                                                              <input type="hidden"
                                                                                                     name="questionnaireCustomScheduleBo[${customVar.index}].id"
                                                                                                     id="id" value="${questionnaireCustomScheduleBo.id}">
                                                                                              <input type="hidden"
                                                                                                     name="activeTaskCustomScheduleBo[${customVar.index}].used"
                                                                                                     id="isUsed${customVar.index}"
                                                                                                     value="${questionnaireCustomScheduleBo.used}"> <input
                                                                                                  type="hidden"
                                                                                                  name="questionnaireCustomScheduleBo[${customVar.index}].questionnairesId"
                                                                                                  id="questionnairesId"
                                                                                                  value="${questionnaireCustomScheduleBo.questionnairesId}">
                                                                                              <span
                                                                                                      class="form-group dis-inline vertical-align-middle pr-md">
                                                      											<input id="StartDate${customVar.index}" type="text"
                                                                                                         count='${customVar.index}'
                                                                                                         class="form-control calendar cusStrDate ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''} "
                                                                                                         name="questionnaireCustomScheduleBo[${customVar.index}].frequencyStartDate"
                                                                                                         value="${questionnaireCustomScheduleBo.frequencyStartDate}"
                                                                                                         placeholder="Start Date"
                                                                                                         onclick='customStartDate(this.id,${customVar.index});'
                                                                                                         required/> <span
                                                                                                      class='help-block with-errors red-txt'></span>
                                                      										</span> <span
                                                                                                  class="gray-xs-f mb-sm pr-md align-span-center">
                                                      											to </span> <span
                                                                                                  class="form-group dis-inline vertical-align-middle pr-md">
                                                      											<input id="EndDate${customVar.index}" type="text"
                                                                                                         count='${customVar.index}'
                                                                                                         class="form-control calendar cusEndDate ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''} cursor-display"
                                                                                                         name="questionnaireCustomScheduleBo[${customVar.index}].frequencyEndDate"
                                                                                                         value="${questionnaireCustomScheduleBo.frequencyEndDate}"
                                                                                                         placeholder="End Date"
                                                                                                         onclick='customEndDate(this.id,${customVar.index});'
                                                                                                         required/>
                                                      											<span class='help-block with-errors red-txt'></span>
                                                      										</span> <span
                                                                                                  class="form-group  dis-inline vertical-align-middle pr-md">
                                                      											<input id="customTime${customVar.index}" type="text"
                                                                                                         count='${customVar.index}'
                                                                                                         class="form-control clock cusTime ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''} cursor-display"
                                                                                                         name="questionnaireCustomScheduleBo[${customVar.index}].frequencyTime"
                                                                                                         value="${questionnaireCustomScheduleBo.frequencyTime}"
                                                                                                         placeholder="Time" onclick='timep(this.id);'
                                                                                                         required/> <span
                                                                                                  class='help-block with-errors red-txt'></span>
                                                      										</span> <span
                                                                                                  class="addbtn addBtnDis align-span-center mr-md cursor-display"
                                                                                                  onclick="addDate();">+</span> <span id="delete"
                                                                                                                                      class="sprites_icon delete vertical-align-middle remBtnDis hide align-span-center ${questionnaireCustomScheduleBo.used ?'cursor-none' : ''} cursor-display"
                                                                                                                                      onclick="removeDate(this);"></span>
                                                                                          </div>
                                                                                      </c:forEach>
                                                                                  </c:if>
                                                                              </div>
                                                                              <div class="manuallyAnchorContainer" style="display: none;">
                                                                                  <!-- anchordate Start -->
                                                                                  <c:if
                                                                                          test="${fn:length(questionnaireBo.questionnaireCustomScheduleBo) eq 0}">
                                                                                      <div class="manually-anchor-option mb-md form-group" id="0">
                                                                                          <input type="hidden"
                                                                                                 name="questionnaireCustomScheduleBo[0].questionnairesId"
                                                                                                 id="questionnairesId" value="${questionnaireBo.id}">
                                                                                          <span
                                                                                                  class="mb-sm pr-md"> <span class="light-txt opacity06">
                                                      											Anchor Date </span>
                                                      									</span> <span> <select
                                                                                              class="signDropDown selectpicker sign-box mt-sm selectXSign"
                                                                                              count='0' title="Select"
                                                                                              name="questionnaireCustomScheduleBo[0].xDaysSign"
                                                                                              id="xSign0">
                                                      											<option value="0"
                                                                                                      ${not questionnaireCustomScheduleBo.xDaysSign ?'selected':''}>+</option>
                                                      											<option value="1"
                                                                                                      ${questionnaireCustomScheduleBo.xDaysSign ?'selected':''}>-</option>
                                                      									</select>
                                                      									</span> <span
                                                                                              class="form-group m-none dis-inline vertical-align-middle">
                                                      										<input id="xdays0" type="text"
                                                                                                     class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave xdays daysMask mt-sm resetAncDate"
                                                                                                     count='0' placeholder="X"
                                                                                                     name="questionnaireCustomScheduleBo[0].timePeriodFromDays"
                                                                                                     value="${questionnaireCustomScheduleBo.timePeriodFromDays}"
                                                                                                     maxlength="3" required pattern="[0-9]+"
                                                                                                     data-pattern-error="Please enter valid number."/> <span
                                                                                              class="help-block with-errors red-txt"></span>
                                                      									</span> <span class="mb-sm pr-md"> <span
                                                                                              class="light-txt opacity06"> days <span
                                                                                              style="padding-right: 5px; padding-left: 5px">to </span>
                                                      											Anchor Date
                                                      									</span>
                                                      									</span> <span> <select
                                                                                              class="signDropDown selectpicker sign-box mt-sm selectYSign"
                                                                                              count='0' title="Select"
                                                                                              name="questionnaireCustomScheduleBo[0].yDaysSign"
                                                                                              id="ySign0">
                                                      											<option value="0"
                                                                                                      ${not questionnaireCustomScheduleBo.yDaysSign ?'selected':''}>+</option>
                                                      											<option value="1"
                                                                                                      ${questionnaireCustomScheduleBo.yDaysSign ?'selected':''}>-</option>
                                                      									</select>
                                                      									</span> <span
                                                                                              class="form-group m-none dis-inline vertical-align-middle">
                                                      										<input id="ydays0" type="text"
                                                                                                     class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave ydays daysMask mt-sm resetAncDate"
                                                                                                     count='0' placeholder="Y"
                                                                                                     name="questionnaireCustomScheduleBo[0].timePeriodToDays"
                                                                                                     value="${questionnaireCustomScheduleBo.timePeriodToDays}"
                                                                                                     maxlength="3" pattern="[0-9]+"
                                                                                                     data-pattern-error="Please enter valid number."
                                                                                                     required/> <span
                                                                                              class="help-block with-errors red-txt"></span>
                                                      									</span> <span class="mb-sm pr-md"> <span
                                                                                              class="light-txt opacity06"> days </span>
                                                      									</span> <span
                                                                                              class="form-group  dis-inline vertical-align-middle pr-md mt-sm"
                                                                                              style="margin-bottom: 0px"> <input id="manualTime0"
                                                                                                                                   type="text"
                                                                                                                                   class="form-control clock"
                                                                                                                                   name="questionnaireCustomScheduleBo[0].frequencyTime"
                                                                                                                                   value="${questionnaireCustomScheduleBo.frequencyTime}"
                                                                                                                                   placeholder="Time"
                                                                                                                                   required/> <span
                                                                                              class='help-block with-errors red-txt'></span>
                                                      									</span> <span id="addbtn0"
                                                                                                        class="addbtn addBtnDis addbtnZero dis-inline vertical-align-middle mr-sm"
                                                                                                        onclick="addDateAnchor();">+</span>
                                                                                          <!-- <span id="deleteAncchor0"
                                                      										class="sprites_icon delete vertical-align-middle remBtnDis hide align-span-center"
                                                      										onclick="removeDateAnchor(this);"></span> -->
                                                                                      </div>
                                                                                  </c:if>
                                                                                  <c:if
                                                                                          test="${fn:length(questionnaireBo.questionnaireCustomScheduleBo) gt 0}">
                                                                                      <c:forEach
                                                                                              items="${questionnaireBo.questionnaireCustomScheduleBo}"
                                                                                              var="questionnaireCustomScheduleBo" varStatus="customVar">
                                                                                          <div class="manually-anchor-option mb-md form-group"
                                                                                               id="${customVar.index}">
                                                                                              <input type="hidden"
                                                                                                     name="questionnaireCustomScheduleBo[${customVar.index}].id"
                                                                                                     id="id" value="${questionnaireCustomScheduleBo.id}">
                                                                                              <input type="hidden"
                                                                                                     name="activeTaskCustomScheduleBo[${customVar.index}].used"
                                                                                                     id="isUsed${customVar.index}"
                                                                                                     value="${questionnaireCustomScheduleBo.used}"> <input
                                                                                                  type="hidden"
                                                                                                  name="questionnaireCustomScheduleBo[${customVar.index}].questionnairesId"
                                                                                                  id="questionnairesId"
                                                                                                  value="${questionnaireCustomScheduleBo.questionnairesId}">
                                                                                              <span class="mb-sm pr-md"> <span
                                                                                                      class="light-txt opacity06"> Anchor Date </span>
                                                      										</span> <span> <select
                                                                                                  class="signDropDown selectpicker sign-box ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''}"
                                                                                                  count='${customVar.index}' title="Select"
                                                                                                  name="questionnaireCustomScheduleBo[${customVar.index}].xDaysSign"
                                                                                                  id="xSign${customVar.index}">
                                                      												<option value="0"
                                                                                                          ${not questionnaireCustomScheduleBo.xDaysSign ?'selected':''}>+</option>
                                                      												<option value="1"
                                                                                                          ${questionnaireCustomScheduleBo.xDaysSign ?'selected':''}>-</option>
                                                      										</select>
                                                      										</span> <span
                                                                                                  class="form-group m-none dis-inline vertical-align-middle">
                                                      											<input id="xdays${customVar.index}" type="text"
                                                                                                         class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave xdays daysMask resetAncDate ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''}"
                                                                                                         count='${customVar.index}' placeholder="X"
                                                                                                         name="questionnaireCustomScheduleBo[${customVar.index}].timePeriodFromDays"
                                                                                                         value="${questionnaireCustomScheduleBo.timePeriodFromDays}"
                                                                                                         maxlength="3" required pattern="[0-9]+"
                                                                                                         data-pattern-error="Please enter valid number."/> <span
                                                                                                  class="help-block with-errors red-txt"></span>
                                                      										</span> <span class="mb-sm pr-md"> <span
                                                                                                  class="light-txt opacity06"> days <span
                                                                                                  style="padding-right: 5px; padding-left: 5px">to </span>
                                                      												Anchor Date
                                                      										</span>
                                                      										</span> <span> <select
                                                                                                  class="signDropDown selectpicker sign-box selectYSign ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''}"
                                                                                                  count='${customVar.index}' title="Select"
                                                                                                  name="questionnaireCustomScheduleBo[${customVar.index}].yDaysSign"
                                                                                                  id="ySign${customVar.index}">
                                                      												<option value="0"
                                                                                                          ${not questionnaireCustomScheduleBo.yDaysSign ?'selected':''}>+</option>
                                                      												<option value="1"
                                                                                                          ${questionnaireCustomScheduleBo.yDaysSign ?'selected':''}>-</option>
                                                      										</select>
                                                      										</span> <span
                                                                                                  class="form-group m-none dis-inline vertical-align-middle">
                                                      											<input id="ydays${customVar.index}" type="text"
                                                                                                         class="form-control wid70 disRadBtn1 disBtn1 remReqOnSave ydays daysMask resetAncDate ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''}"
                                                                                                         count='${customVar.index}' placeholder="Y"
                                                                                                         name="questionnaireCustomScheduleBo[${customVar.index}].timePeriodToDays"
                                                                                                         value="${questionnaireCustomScheduleBo.timePeriodToDays}"
                                                                                                         maxlength="3" pattern="[0-9]+"
                                                                                                         data-pattern-error="Please enter valid number."
                                                                                                         required/>
                                                      											<span class="help-block with-errors red-txt"></span>
                                                      										</span> <span class="mb-sm pr-md"> <span
                                                                                                  class="light-txt opacity06"> days </span>
                                                      										</span> <span
                                                                                                  class="form-group  dis-inline vertical-align-middle pr-md"
                                                                                                  style="margin-bottom: 0px"> <input
                                                                                                  id="manualTime${customVar.index}" type="text"
                                                                                                  class="form-control clock ${questionnaireCustomScheduleBo.used && studyBo.status ne 'Pre-launch' ?'cursor-none' : ''}"
                                                                                                  name="questionnaireCustomScheduleBo[${customVar.index}].frequencyTime"
                                                                                                  value="${questionnaireCustomScheduleBo.frequencyTime}"
                                                                                                  placeholder="Time" required/> <span
                                                                                                  class='help-block with-errors red-txt'></span>
                                                      										</span> <span id="addbtn${customVar.index}"
                                                                                                            class="addbtn addBtnDis addbtnZero align-span-center mr-sm cursor-display"
                                                                                                            onclick="addDateAnchor();">+</span> <span
                                                                                                  id="deleteAncchor${customVar.index}"
                                                                                                  class="sprites_icon delete vertical-align-middle remBtnDis remBtnZero hide align-span-center ${questionnaireCustomScheduleBo.used ?'cursor-none' : ''} cursor-display"
                                                                                                  onclick="removeDateAnchor(this);"></span>
                                                                                          </div>
                                                                                      </c:forEach>
                                                                                  </c:if>
                                                                              </div>
                                                                              <!-- anchordate end -->
                                                                              <div class="mt-md">
                                                                                  <div class="gray-xs-f mb-xs">Default Lifetime of each run</div>
                                                                                  <div class="black-xs-f">As defined by the start and end
                                                                                      times selected above
                                                                                  </div>
                                                                              </div>
                                                                              <div class="col-md-4 col-lg-3 mt-xlg mb-lg addNewRuns"
                                                                                   style="display: none;">
                                                      							<span class="checkbox checkbox-inline addNewRuns"> <input
                                                                                          type="checkbox" id="addNewRunsId"
                                                                                          value="${questionnaireBo.addNewRuns}"
                                                                                          <c:if test="${questionnaireBo.addNewRuns}">checked</c:if>>
                                                      								<label for="addNewRunsId"> Add Runs </label>
                                                      							</span>
                                                                              </div>
                                                                          </div>
                                                                      </form:form>
                                                                      <div id="ongoingNoteId" class="note-center" style="display: none">Note:
                                                                          Activities scheduled as ongoing will always be available for
                                                                          participants to take any number of times, during the lifetime of
                                                                          the study
                                                                      </div>




      </div>
    </div>

       <!-- <ul class="nav nav-tabs review-tabs" id="tabContainer">
            <li class="contentqusClass active"><a data-toggle="tab"
                                                  href="#contentTab">Content</a></li>
            <li class="scheduleQusClass"><a data-toggle="tab"
                                            href="#schedule">Schedule</a></li>
        </ul>--->

    </div>
    <!--  End body tab section -->
    <div class="modal fade" id="myAutoModal" role="dialog">
        <div class="modal-dialog modal-sm flr_modal">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-body">
                  <div id="autoSavedMessage" class="text-right">
                    <div class="blue_text">Last saved now</div>
                    <div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt">15 minutes</span></div>
                    </div>
                  </div>
                </div>
            </div>
        </div>

                <div class="modal fade" id="timeOutModal" role="dialog">
                                    <div class="modal-dialog modal-sm flr_modal">
                                        <!-- Modal content-->
                                        <div class="modal-content">
                                                <div class="modal-body">
                                                <div id="timeOutMessage" class="text-right blue_text"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in  15 minutes</div>
                                                </div>
                                            </div>
                                        </div>
                            </div>
</div>
<!-- End right Content here -->
<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
        <!-- Modal content-->
        <div class="modal-content">

            <div class="cust-hdr pt-lg">
                <button type="button" class="close pull-right pr-4" data-dismiss="modal">&times;</button>
                <h5 class="modal-title pl-lg">
                    <b>Setting up a Questionnaire</b>
                </h5>
            </div>

            <div class="modal-body pt-xs pb-lg pl-xlg pr-xlg">
                <div>
                    <div>
                        <ul class="square">
                            <li>Add all possible Steps you can have in the
                                questionnaire.
                            </li>
                            <li>Order the steps to represent the order you want them in
                                the app.
                            </li>
                            <li>This constitutes your Master Order of steps.</li>
                            <li>If you need to deviate from the Master Order under
                                special conditions, take the following steps:
                            </li>
                            <li>
                                <ul class="circle">
                                    <li>Ensure the Master Order is such that all possible
                                        destinations to a Step are listed immediately below the
                                        Step,
                                        one after the other.
                                    </li>
                                    <li>Check the Apply Branching checkbox to start defining
                                        alternate questionnaire paths.
                                    </li>
                                    <li>This shows up the default destination step for each
                                        step.
                                    </li>
                                    <li>Visit each step and change the destination step as
                                        desired.
                                    </li>
                                    <li>You can do this by editing the step-level destination
                                        attribute or by defining a destination for each response
                                        choice if that provision is available for the selected
                                        response type.
                                    </li>
                                    <li>The step-level destination is used if the response
                                        level destination conditions are not met at runtime in the
                                        app.
                                    </li>
                                    <li>To choose a destination step, you can select either
                                        one of the next steps in the Maser Order OR the
                                        Questionnaire
                                        Completion Step.
                                    </li>
                                </ul>
                            </li>
                            <li>Note that if you wish to change the Master Order after
                                applying branching, all the applied branching will be lost, and
                                you would need to set it up again once the new Master Order is
                                defined.
                            </li>
                            <li>The above also holds good if you decide to delete a
                                Step.
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

  <c:if test="${actionType == 'view'}">
  $('#contentFormId input[type="text"]').prop('disabled', true);
  $('#contentFormId input[type="checkbox"]').prop('disabled', true);
  $('#oneTimeFormId input').prop('disabled', true);
  $('#oneTimeFormId input[type="text"]').prop('disabled', true);
  $('#dailyFormId input[type="text"]').prop('disabled', true);
  $('#weeklyFormId input[type="text"]').prop('disabled', true);
  $('#monthlyFormId input[type="text"]').prop('disabled', true);
  $('#customFormId input[type="text"]').prop('disabled', true);
  $('select').prop('disabled', true);
  $('#schedule1,#schedule2,#inlineRadio1,#inlineRadio2,#inlineRadio3,#inlineRadio4,#inlineRadio5,#inlineRadio6').prop(
      'disabled', true);
  $('.addBtnDis, .remBtnDis').addClass('dis-none');
  $('#studyLanguage').attr('disabled', false);
  </c:if>

  /*<c:if test="${questionnaireBo.shortTitleDuplicate <= 0}">
$('#contentFormId input[type="text"]').prop('disabled', true);
$('#contentFormId input[type="checkbox"]').prop('disabled', true);
$('#schedule input[type="radio"]').prop('disabled', true);
$('#oneTimeFormId input').prop('disabled', true);
$('#oneTimeFormId input[type="text"]').prop('disabled', true);
$('#dailyFormId input[type="text"]').prop('disabled', true);
$('#weeklyFormId input[type="text"]').prop('disabled', true);
$('#monthlyFormId input[type="text"]').prop('disabled', true);
$('select').prop('disabled', true);
$('#inlineRadio1,#inlineRadio2,#inlineRadio3,#inlineRadio4,#inlineRadio5').prop('disabled', true);
$('.addBtnDis, .remBtnDis').addClass('');
$('.cursor-display').removeClass('cursor-none');
</c:if>*/


  /* <c:if test="${questionnaireBo.shortTitleDuplicate > 0}">
  $('span.addBtnDis').remove();
  $('span.remBtnDis').remove();
</c:if> */
var idleTime = 0;
  var count = 0;
  var customCount = 0;
  var frequencey = "${questionnaireBo.frequency}";
  customCount = '${customCount}';
  count = Number('${count}');
  var isValidManuallySchedule = true;
  var multiTimeVal = true;
  var table1;
  var customAnchorCount = 0;
  //customAnchorCount = '${customCount}';
  /* var scheduletype = "${questionnaireBo.scheduleType}";
if(scheduletype != '' && scheduletype != null && typeof scheduletype != 'undefined'){
	scheduletype = $('input[name="scheduleType"]:checked').val();
}  */
  var isParticipantProp = "";
  var scheduletype = "";
  $(document).ready(function () {
    scheduletype = $('input[name="scheduleType"]:checked').val();
    if (scheduletype == 'AnchorDate') {
      $("#inlineRadio6").parent().hide();
    }

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    isParticipantProp = $('#anchorDateId').find('option:selected').attr('data-id');
    if (isParticipantProp === 'true') {
      $('.manuallyAnchorContainer').find('.addbtnZero').hide();
      $('.addNewRuns').show();
    } else {
      $('.manuallyAnchorContainer').find('.addbtnZero').show();
      $('.addNewRuns').hide();
    }

    if ($('.manually-option').length == 1) {
      $('.manually-option').find('.delete').addClass('display__none');
    } else {
      $('.delete').removeClass('display__none');
    }

    if (frequencey == 'Ongoing') {
      $("#ongoingNoteId").show();
    }

    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');

    $(".scheduleQusClass").click(function () {
      if ($("#schedule2").prop("checked")) {
        $("#weekDaysId").hide();
        $(".weeklyRegular").hide();
        $("#monthlyDateId").hide();
        $(".monthlyRegular").hide();
      }
    })
    $("#anchorDateId").change(function () {
      isParticipantProp = $('#anchorDateId').find('option:selected').attr('data-id');
      if (isParticipantProp === 'true') {
        $('.manually-anchor-option').nextAll().remove();
        $('.manuallyAnchorContainer').find('.addbtnZero').hide();
        //$("#customFormId").trigger("reset");
        $('.manually-anchor-option').find("input[type=text]").val("");
        $('.addNewRuns').show();
      } else {
        $('.manuallyAnchorContainer').find('.addbtnZero').show();
        //$("#customFormId").trigger("reset");
        $('.manually-anchor-option').find("input[type=text]").val("");
        $('.addNewRuns').hide();
      }
    });

    $('#addNewRunsId').change(function () {
      if (this.checked) {
        $("#addNewRunsId").val('true');
      } else {
        $("#addNewRunsId").val('false');
      }
    });

    $(".scheduleQusClass").click(function () {
      if ($("#schedule1").prop("checked")) {
        $("#weekDaysId").show();
        $(".weeklyRegular").show();
        $("#monthlyDateId").show();
        $(".monthlyRegular").show();
      }
    })
    $(".typeofschedule").change(function () {
      isParticipantProp = $('#anchorDateId').find('option:selected').attr('data-id');
      var scheduletype = $(this).attr('scheduletype');
      $('#isLaunchStudy').prop('checked', false);
      $('#isStudyLifeTime').prop('checked', false);
      $("#chooseDate").attr("disabled", false);
      $("#selectTime1").attr("disabled", false);
      $("#chooseEndDate").attr("disabled", false);
      $("#onetimexdaysId").prop('disabled', false);
      $("#selectTime").attr("disabled", false);
      $("#onetimeydaysId").prop('disabled', false);
      var schedule_opts = $("input[name='frequency']:checked").val();
      if (scheduletype == 'AnchorDate') {

        $("#weekDaysId").hide();
        $("#weekDaysId").find('input:text').removeAttr('required', true);
        $(".weeklyRegular").hide();
        $(".weeklyRegular").removeAttr('required');

        $("#monthlyDateId").hide();
        $("#monthlyDateId").find('input:text').removeAttr('required', true);
        $(".monthlyRegular").hide();
        $(".monthlyRegular").removeAttr('required');

        localStorage.setItem("IsAnchorDateSelected", "true");
        localStorage.setItem("IsRegularSelected", "false");

        if (schedule_opts == 'One time') {
          $(".onetimeanchorClass").show();
          $(".onetimeanchorClass").find('input:text').attr('required', true);
        }
        if (schedule_opts == 'Ongoing') {
          $("#inlineRadio1").prop("checked", true);
          $(".oneTime").removeClass("dis-none");
          $(".onetimeanchorClass").show();
          $(".onetimeanchorClass").find('input:text').attr('required', true);
        }
        if (schedule_opts == 'Daily') {
          $("#endDateId").text('NA');
          $("#lifeTimeId").text('-');
          $(".dailyanchorDiv").show();
          $(".dailyanchorDiv").find('input:text').attr('required', true);
        }
        if (schedule_opts == 'Weekly') {
          $("#weekEndDate").text('NA');
          $("#weekLifeTimeEnd").text('-');
          $("#weekDaysId").hide();
          $("#weekDaysId").find('input:text').removeAttr('required', true);
          $(".weeklyanchorDiv").show();
          $(".weeklyanchorDiv").find('input:text').attr('required', true);
        }
        if (schedule_opts == 'Monthly') {
          $("#monthEndDate").text('NA');
          $("#monthLifeTimeDate").text('-');
          $(".monthlyanchorDiv").show();
          $(".monthlyanchorDiv").find('input:text').attr('required', true);
        }
        if (schedule_opts == 'Manually Schedule') {
          $(".manuallyAnchorContainer").show();
          $(".manuallyAnchorContainer").find('input:text').attr('required', true);
          if (isParticipantProp === 'true') {
            $('.manuallyAnchorContainer').find('.addbtnZero').hide();
            $('.addNewRuns').show();
          } else {
            $('.manuallyAnchorContainer').find('.addbtnZero').show();
            $('.addNewRuns').hide();
          }
        }
        $('.regularClass').hide();
        $('.regularClass').find('input:text').removeAttr('required');
        $('.anchortypeclass').show();
        $('.anchortypeclass').find('input:select').attr('required', true);
        $('.selectpicker').selectpicker('refresh');
        $('.dailyStartCls').hide();
        $('.dailyStartCls').find('input:text').removeAttr('required');
        $('.weeklyStartCls').hide();
        $('.weeklyStartCls').find('input:text,select').removeAttr('required');
        $('.monthlyStartCls').hide();
        $('.monthlyStartCls').find('input:text').removeAttr('required');
        $(".manuallyContainer").hide();
        $(".manuallyContainer").find('input:text').removeAttr('required');
        $("#inlineRadio6").parent().hide();
        $("#ongoingNoteId").hide();
      } else {

        localStorage.setItem("IsAnchorDateSelected", "false");
        localStorage.setItem("IsRegularSelected", "true");

        $(".onetimeanchorClass").hide();
        $('.onetimeanchorClass').find('input:text').removeAttr('required');
        $('.regularClass').show();
        $('.regularClass').find('input:text').attr('required', true);

        $('.dailyStartCls').show();
        $('.dailyStartCls').find('input:text').attr('required', true);
        $(".dailyanchorDiv").hide();
        $(".dailyanchorDiv").find('input:text').removeAttr('required', true);

        $('.weeklyStartCls').show();
        $('.weeklyStartCls').find('input:text,select').attr('required', true);
        $("#weekDaysId").show();
        $("#weekDaysId").find('input:text').attr('required', true);

        $(".weeklyRegular").show();
        $(".weeklyRegular").attr('required', true);

        $(".weeklyanchorDiv").hide();
        $(".weeklyanchorDiv").find('input:text').removeAttr('required', true);

        $('.monthlyStartCls').show();
        $('.monthlyStartCls').find('input:text').attr('required', true);
        $("#monthlyDateId").show();
        $("#monthlyDateId").find('input:text').attr('required', true);

        $(".monthlyRegular").show();
        $(".monthlyRegular").attr('required', true);

        $(".monthlyanchorDiv").hide();
        $(".monthlyanchorDiv").find('input:text').removeAttr('required', true);

        $('.manuallyContainer').show();
        $('.manuallyContainer').find('input:text').attr('required', true);
        $(".manuallyAnchorContainer").hide();
        $(".manuallyAnchorContainer").find('input:text').removeAttr('required', true);
        $(".manuallyAnchorContainerParticipantProperties").hide();
        $(".manuallyAnchorContainerParticipantProperties").find('input:text').removeAttr('required',
            true);
        $('.anchortypeclass').hide();
        $('.anchortypeclass').removeAttr('required');
        $("#anchorDateId").val("");
        $("#inlineRadio6").parent().show();
      }

      if (schedule_opts == 'One time') {
        $("#chooseDate").val('');
        $("#selectTime1").val('');
        $("#chooseEndDate").val('');
        $("#isLaunchStudy").val('');
        $("#isStudyLifeTime").val('');
        $("#selectTime").val('');
        $('#onetimexdaysId').val('');
        $('#onetimeydaysId').val('');
        var frequency_txt = "${questionnaireBo.frequency}";
        if (frequency_txt != '' && frequency_txt != null && typeof frequency_txt != 'undefined') {
          $("#previousFrequency").val(frequency_txt);
        }
      }
    });

    $("#onetimexdaysId, #onetimeydaysId").on('blur', function () {
      chkDaysValid(false);
    });

    $('.signDropDown').on('change', function () {
      chkDaysValid(false);
    });
    var qId = "${questionnaireBo.id}";
    if (qId != '' && qId != null && typeof qId != 'undefined') {
      $("#stepContainer").show();
      $("#content").show();
    } else {
      $("#stepContainer").hide();
      $("#content").hide();
    }
    checkDateRange();
    customStartDate('StartDate' + customCount, customCount);
    customEndDate('EndDate' + customCount, customCount);
    if ($('.time-opts').length > 1) {
      $('.dailyContainer').find(".remBtnDis").removeClass("hide");
    } else {
      $('.dailyContainer').find(".remBtnDis").addClass("hide");
    }
    if ($('.manually-option').length > 1) {
      $('.manuallyContainer').find(".remBtnDis").removeClass("hide");
    } else {
      $('.manuallyContainer').find(".remBtnDis").addClass("hide");
    }

    var actionPage = "${actionType}";
    var reorder = true;
    if (actionPage == 'view') {
      reorder = false;
    } else {
      reorder = true;
    }
    table1 = $('#content').DataTable({
      "paging": false,
      "info": false,
      "filter": false,
      rowReorder: reorder,
      "columnDefs": [
        {orderable: false, targets: [0, 1, 2, 3]},
      ],
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        if (actionPage !== 'view') {
          $('td:eq(0)', nRow).addClass("cursonMove dd_icon");
        }
        $('td:eq(0)', nRow).addClass("qs-items static-width");
        $('td:eq(1)', nRow).addClass("qs-items");
        $('td:eq(2)', nRow).addClass("qs-items");
        $('td:eq(3)', nRow).addClass("qs-items");
      }
    });
    table1.on('row-reorder', function (e, diff, edit) {
      var oldOrderNumber = '', newOrderNumber = '';
      var oldClass = '', newclass = '';
      var result = 'Reorder started on row: ' + edit.triggerRow.data()[1] + '<br>';
      var studyId = $("#studyId").val();
      var questionnaireId = $("#id").val();
      for (var i = 0, ien = diff.length; i < ien; i++) {
        var rowData = table1.row(diff[i].node).data();
        var r1;
        if (i == 0) {
          r1 = $(rowData[0]).attr('id');
        }
        if (i == 1) {
          if (parseInt(r1) > parseInt($(rowData[0]).attr('id'))) {
            oldOrderNumber = $(diff[0].oldData).attr('id');
            newOrderNumber = $(diff[0].newData).attr('id');
          } else {
            oldOrderNumber = $(diff[diff.length - 1].oldData).attr('id');
            newOrderNumber = $(diff[diff.length - 1].newData).attr('id');
          }

        }
        result += rowData[1] + ' updated to be in position ' +
            diff[i].newData + ' (was ' + diff[i].oldData + ')<br>';
      }
      if (oldOrderNumber !== undefined && oldOrderNumber != null && oldOrderNumber != ""
          && newOrderNumber !== undefined && newOrderNumber != null && newOrderNumber != "") {

        $.ajax({
          url: "/fdahpStudyDesigner/adminStudies/reOrderQuestionnaireStepInfo.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            questionnaireId: questionnaireId,
            oldOrderNumber: oldOrderNumber,
            newOrderNumber: newOrderNumber,
            "${_csrf.parameterName}": "${_csrf.token}",
          },
          success: function consentInfo(data) {
            var status = data.message;

            if (status == "SUCCESS") {

              $('#alertMsg').show();
              $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                  "Reorder done successfully");

              var questionnaireSteps = data.questionnaireJsonObject;
              var isDone = data.isDone;
              reloadQuestionnaireStepData(questionnaireSteps, isDone);
              if ($('.seventhQuestionnaires').find('span').hasClass(
                  'sprites-icons-2 tick pull-right mt-xs')) {
                $('.seventhQuestionnaires').find('span').removeClass(
                    'sprites-icons-2 tick pull-right mt-xs');
              }
            } else {
              $('#alertMsg').show();
              $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                  "Unable to reorder questionnaire");

            }
            setTimeout(hideDisplayMessage, 4000);
          },
          error: function (xhr, status, error) {
            $("#alertMsg").removeClass('s-box').addClass('e-box').text(error);
            setTimeout(hideDisplayMessage, 4000);
          }
        });

      }

    });

    if (document.getElementById("doneId") != null && document.getElementById("doneId").disabled) {
      $('[data-toggle="tooltip"]').tooltip();
    }

    $(".schedule").change(function () {
      $(".all").addClass("dis-none");
      var schedule_opts = $(this).attr('frequencytype');
      var val = $(this).val();
      isParticipantProp = $('#anchorDateId').find('option:selected').attr('data-id');
      $("." + schedule_opts).removeClass("dis-none");
      resetValidation($("#oneTimeFormId"));
      resetValidation($("#customFormId"));
      resetValidation($("#dailyFormId"));
      resetValidation($("#weeklyFormId"));
      resetValidation($("#monthlyFormId"));
      if ((frequencey != null && frequencey != "" && typeof frequencey != 'undefined')) {
        if (frequencey != val) {
          if (val == 'One time') {
            $("#chooseDate").val('');
            $("#selectTime").val('');
            $("#selectTime1").val('');
            $("#chooseEndDate").val('');
            $("#oneTimeFreId").val('');
            $("#isLaunchStudy").val('');
            $("#isStudyLifeTime").val('');
            var frequency_txt = "${questionnaireBo.frequency}";
            if (frequency_txt != '' && frequency_txt != null && typeof frequency_txt
                != 'undefined') {
              $("#previousFrequency").val(frequency_txt);
            }
            $('#onetimexdaysId').val('');
            $('#onetimeydaysId').val('');
            $("#ongoingNoteId").hide();
          } else if (val == 'Manually Schedule') {
            $('.manually').find('input:text').val('');
            isValidManuallySchedule = true;
            $('.manually-option:not(:first)').find('.remBtnDis').click();
            $('.manually-option').find('input').val('');
            $('.manually-option').find('.cusTime').prop('disabled', true);
            $('.manually-anchor-option:not(:first)').find('.remBtnDis').click();
            $('.manually-anchor-option').find('input').val('');
            $('.manually-anchor-option').find('.cusTime').prop('disabled', true);
            if ($('.manually-option').length == 1) {
              $('.manually-option').find('.delete').addClass('display__none');
            } else {
              $('.delete').removeClass('display__none');
            }
            $("#ongoingNoteId").hide();
          } else if (val == 'Daily') {
            $("#startDate").val('');
            $("#days").val('');
            $("#endDateId").text('NA');
            $("#lifeTimeId").text('-');
            $('.dailyClock').val('');
            $('.dailyClock:not(:first)').parent().parent().remove();
            multiTimeVal = true;
            $('#dailyxdaysId').val('');
            $("#ongoingNoteId").hide();
          } else if (val == 'Weekly') {
            $("#startDateWeekly").val('');
            $("#weeklyFreId").val('');
            $("#questionnairesFrequenciesBo.frequencyTime").val('');
            $("#startWeeklyDate").val('');
            $("#weeks").val('');
            $("#weekEndDate").text('NA');
            $("#weekLifeTimeEnd").text('-');
            $("#selectWeeklyTime").val('');
            $('#weeklyxdaysId').val('');
            $("#ongoingNoteId").hide();
          } else if (val == 'Monthly') {
            $("#monthFreId").val('');
            $("#startDateMonthly").val('');
            $("#selectMonthlyTime").val('');
            $("#pickStartDate").val('');
            $("#months").val('');
            $("#monthEndDate").text('NA');
            $("#monthLifeTimeDate").text('-');
            $('#monthlyxdaysId').val('');
            $("#ongoingNoteId").hide();
          } else if (val == 'Ongoing') {
            $("#ongoingNoteId").show();
          }
        }
      } else {
        $('.oneTime').find('input:text').val('');
        $(".daily").find('input:text').val('');
        $(".week").find('input:text').val('');
        $("#startDateWeekly").val('');
        $(".month").find('input:text').val('');
        $('.manually').find('input:text').val('');
        $("#isLaunchStudy").val('');
        $("#isStudyLifeTime").val('');
        $("#monthEndDate").text('NA');
        $("#monthLifeTimeDate").text('-');
        $("#weekEndDate").text('NA');
        $("#weekLifeTimeEnd").text('-');
        $("#endDateId").text('NA');
        $("#lifeTimeId").text('-');
        $('.manually-option:not(:first)').find('.remBtnDis').click();
        $('.manually-option').find('input').val('');
        $('.dailyClock').val('');
        $('.dailyClock:not(:first)').parent().parent().remove();
        $('.manually-option').find('.cusTime').prop('disabled', true);
        $('.manually-anchor-option').find('.cusTime').prop('disabled', true);
        if ($('.manually-option').length == 1) {
          $('.manually-option').find('.delete').addClass('display__none');
        } else {
          $('.delete').removeClass('display__none');
        }
      }

      if (val == 'Ongoing') {
        $("#ongoingNoteId").show();
      } else {
        $("#ongoingNoteId").hide();
      }

      //AnchorDate start
      var scheduletype = $('input[name="scheduleType"]:checked').val();
      if (scheduletype == 'AnchorDate') {
        var element = $('#anchorDateId').find('option:selected').text();
        setAnchorDropdown(val, element);
        if (val == 'One time') {
          $(".onetimeanchorClass").show();
          $(".onetimeanchorClass").find('input:text').attr('required', true);
        }
        if (val == 'Daily') {
          $("#startDate").val('');
          $("#endDateId").text('NA');
          $("#lifeTimeId").text('-');
          $(".dailyanchorDiv").show();
          $(".dailyanchorDiv").find('input:text').attr('required', true);
        }
        if (val == 'Weekly') {
          $("#weekEndDate").text('NA');
          $("#weekLifeTimeEnd").text('-');
          $(".weeklyanchorDiv").show();
          $(".weeklyanchorDiv").find('input:text').attr('required', true);
        }
        if (val == 'Monthly') {
          $("#monthEndDate").text('NA');
          $("#monthLifeTimeDate").text('-');
          $(".monthlyanchorDiv").show();
          $(".monthlyanchorDiv").find('input:text').attr('required', true);
        }
        if (val == 'Manually Schedule') {
          $(".manuallyAnchorContainer").show();
          $(".manuallyAnchorContainer").find('input:text').attr('required', true);
          if (isParticipantProp === 'true') {
            $('.manuallyAnchorContainer').find('.addbtnZero').hide();
            $('.addNewRuns').show();
          } else {
            $('.manuallyAnchorContainer').find('.addbtnZero').show();
            $('.addNewRuns').hide();
          }
          if ($('.manually-anchor-option').length == 1) {
            $('.manually-anchor-option').find('.delete').addClass('display__none');
          } else {
            $('.delete').removeClass('display__none');
          }
        }
        $('.regularClass').hide();
        $('.regularClass').find('input:text').removeAttr('required');
        $('.anchortypeclass').show();
        $('.anchortypeclass').find('input:select').attr('required', true);
        $('.selectpicker').selectpicker('refresh');
        $('.dailyStartCls').hide();
        $('.dailyStartCls').find('input:text').removeAttr('required');
        $('.weeklyStartCls').hide();
        $('.weeklyStartCls').find('input:text,select').removeAttr('required');
        $('.monthlyStartCls').hide();
        $('.monthlyStartCls').find('input:text').removeAttr('required');
        $(".manuallyContainer").hide();
        $(".manuallyContainer").find('input:text').removeAttr('required');
        if ($('.manually-anchor-option').length == 1) {
          $('.manually-anchor-option').find('.delete').addClass('display__none');
        } else {
          $('.delete').removeClass('display__none');
        }
      } else {
        $(".onetimeanchorClass").hide();
        $('.onetimeanchorClass').find('input:text').removeAttr('required');
        $('.regularClass').show();
        $('.regularClass').find('input:text').attr('required', true);

        $('.dailyStartCls').show();
        $('.dailyStartCls').find('input:text').attr('required', true);
        $(".dailyanchorDiv").hide();
        $(".dailyanchorDiv").find('input:text').removeAttr('required', true);

        $('.weeklyStartCls').show();
        $('.weeklyStartCls').find('input:text,select').attr('required', true);
        $(".weeklyanchorDiv").hide();
        $(".weeklyanchorDiv").find('input:text').removeAttr('required', true);

        $('.monthlyStartCls').show();
        $('.monthlyStartCls').find('input:text').attr('required', true);
        $(".monthlyanchorDiv").hide();
        $(".monthlyanchorDiv").find('input:text').removeAttr('required', true);

        $('.manuallyContainer').show();
        $('.manuallyContainer').find('input:text').attr('required', true);
        $(".manuallyAnchorContainer").hide();
        $(".manuallyAnchorContainer").find('input:text').removeAttr('required', true);
        $(".manuallyAnchorContainerParticipantProperties").hide();
        $(".manuallyAnchorContainerParticipantProperties").find('input:text').removeAttr('required',
            true);
        $('.anchortypeclass').hide();
        $('.anchortypeclass').removeAttr('required');
        $("#anchorDateId").val("");
        if ($('.manually-anchor-option').length == 1) {
          $('.manually-anchor-option').find('.delete').addClass('display__none');
        } else {
          $('.delete').removeClass('display__none');
        }
      }
      //AnchorDate type end
    });

    if (frequencey != null && frequencey != "" && typeof frequencey != 'undefined') {
      $(".all").addClass("dis-none");
      if (frequencey == 'One time') {
        $(".oneTime").removeClass("dis-none");
      } else if (frequencey == 'Manually Schedule') {
        $(".manually").removeClass("dis-none");
      } else if (frequencey == 'Daily') {
        $(".daily").removeClass("dis-none");
      } else if (frequencey == 'Weekly') {
        $(".week").removeClass("dis-none");
      } else if (frequencey == 'Monthly') {
        $(".month").removeClass("dis-none");
      }
      var scheduletype = $('input[name="scheduleType"]:checked').val();
      if (scheduletype != '' && scheduletype != null && typeof scheduletype != 'undefined'
          && scheduletype == 'AnchorDate') {
        if (frequencey == 'One time') {
          $(".onetimeanchorClass").show();
          $('#chooseDate').removeAttr('required');
          $("#selectTime1").removeAttr('required');
          $(".onetimeanchorClass").find('input:text').attr('required', true);
          $('.anchortypeclass').find('input:select').attr('required', true);
          $('.selectpicker').selectpicker('refresh');
        }
        if (frequencey == 'Daily') {
          $(".dailyanchorDiv").show();
          $(".dailyanchorDiv").find('input:text').attr('required', true);
        }
        if (frequencey == 'Weekly') {
          $(".weeklyanchorDiv").show();
          $(".weeklyanchorDiv").find('input:text').attr('required', true);
        }
        if (frequencey == 'Monthly') {
          $(".monthlyanchorDiv").show();
          $(".monthlyanchorDiv").find('input:text').attr('required', true);
        }
        if (frequencey == 'Manually Schedule') {
          $(".manuallyAnchorContainer").show();
          $(".manuallyAnchorContainer").find('input:text').attr('required', true);
        }
        $('.regularClass').hide();
        $('.regularClass').find('input:text').removeAttr('required');
        $('.anchortypeclass').show();
        $('.anchortypeclass').find('input:text').attr('required', true);
        $('.dailyStartCls').hide();
        $('.dailyStartCls').find('input:text').removeAttr('required');
        $('.weeklyStartCls').hide();
        $('.weeklyStartCls').find('input:text,select').removeAttr('required');
        $('.monthlyStartCls').hide();
        $('.monthlyStartCls').find('input:text').removeAttr('required');
        $(".manuallyContainer").hide();
        $(".manuallyContainer").find('input:text').removeAttr('required');
      } else {
        $(".onetimeanchorClass").hide();
        $('.onetimeanchorClass').find('input:text').removeAttr('required');
        $('.regularClass').show();
        $('.regularClass').find('input:text').attr('required', true);

        $('.dailyStartCls').show();
        $('.dailyStartCls').find('input:text').attr('required', true);
        $(".dailyanchorDiv").hide();
        $(".dailyanchorDiv").find('input:text').removeAttr('required', true);

        $('.weeklyStartCls').show();
        $('.weeklyStartCls').find('input:text,select').attr('required', true);
        $(".weeklyanchorDiv").hide();
        $(".weeklyanchorDiv").find('input:text').removeAttr('required', true);

        $('.monthlyStartCls').show();
        $('.monthlyStartCls').find('input:text').attr('required', true);
        $(".monthlyanchorDiv").hide();
        $(".monthlyanchorDiv").find('input:text').removeAttr('required', true);

        $('.manuallyContainer').show();
        $('.manuallyContainer').find('input:text').attr('required', true);
        $(".manuallyAnchorContainer").hide();
        $(".manuallyAnchorContainer").find('input:text,select').removeAttr('required', true);
        $('.anchortypeclass').hide();
        $('.anchortypeclass').removeAttr('required');
      }
    }

    $('#chooseDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      minDate: serverDate(),
      useCurrent: false,
    })
    .on("dp.change", function (e) {
      if (e.date._d)
        $("#chooseEndDate").data("DateTimePicker").clear().minDate(new Date(e.date._d));
      else
        $("#chooseEndDate").data("DateTimePicker").minDate(serverDate());
    });

    $(document).on('change dp.change ', '.dailyClock', function () {

      $('.dailyContainer').find('.dailyTimeDiv').each(function () {
        var chkVal = true;
        var thisDailyTimeDiv = $(this);
        var thisAttr = $(this).find('.dailyClock');
        $('.dailyContainer').find('.dailyTimeDiv').each(function () {
          if (!thisDailyTimeDiv.is($(this)) && $(this).find('.dailyClock').val()) {
            if ($(this).find('.dailyClock').val() == thisAttr.val()) {
              if (chkVal)
                chkVal = false;
            }
          }
        });
        if (!chkVal) {
          thisAttr.parents('.dailyTimeDiv').find('.dailyClock').parent().find(
              ".help-block").empty().append(
              $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                  "Duplicate times cannot be set."));
        } else {
          thisAttr.parents('.dailyTimeDiv').find('.dailyClock').parent().find(
              ".help-block").empty();
        }
      });
      var a = 0;
      $('.dailyContainer').find('.dailyTimeDiv').each(function () {
        if ($(this).find('.dailyClock').parent().find('.help-block.with-errors').children().length
            > 0) {
          a++;
        }
      });
      multiTimeVal = !(a > 0);
    });

    $('#chooseEndDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      minDate: serverDate(),
      useCurrent: false,
    });

    $('#startDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      useCurrent: false,
    }).on("dp.change", function (e) {
      var startDate = $("#startDate").val();
      var days = $("#days").val();
      var endDate = ''
      if (startDate && days && days > 0) {
        var dt = new Date(startDate);
        dt.setDate(dt.getDate() + Number(days) - 1);
        endDate = formatDate(dt);
      } else {
        startDate = '';
        endDate = '';
      }
      $("#studyDailyLifetimeEnd").val(endDate);
      $("#lifeTimeId").text(startDate + ' - ' + endDate);
      $("#endDateId").text(endDate ? endDate : 'NA');
    }).on("dp.show", function (e) {
      $('#startDate').data("DateTimePicker").minDate(serverDate());
    });
    $('#startDateMonthly').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      useCurrent: false,
    }).on("dp.show", function (e) {
      $('#startDateMonthly').data("DateTimePicker").minDate(serverDate());
    }).on("dp.change", function (e) {
      if (e.date._d != $('#pickStartDate').data("DateTimePicker").date()) {
        $('#pickStartDate').val('');
      }
      var dateArr = [];
      for (var i = new Date(e.date._d).getFullYear(); i < 2108; i++) {
        for (var j = 0; j < 12; j++) {
          var allowedDate = new Date(i, j, new Date(e.date._d).getDate());
          if (allowedDate.getMonth() !== j) {
            allowedDate = new Date(i, j + 1, 0);
          }
          dateArr.push(allowedDate);
        }
      }
      $('#pickStartDate').data("DateTimePicker").enabledDates(dateArr);
    });

    $(".clock").not('.cursor-none').datetimepicker({
      format: 'h:mm a',
      useCurrent: false,
    });

    $(document).on('dp.change', '.cusStrDate', function (e) {
      if (e.date._d) {
        var nxtDate = moment(new Date(e.date._d)).add(1, 'days');
      }
      if (!$(this).parents('.manually-option').find('.cusEndDate').data("DateTimePicker")) {
        customEndDate($(this).parents('.manually-option').find('.cusEndDate').attr('id'), 0);
      }
      if (nxtDate)
        $(this).parents('.manually-option').find('.cusEndDate').val('').data(
            "DateTimePicker").minDate(nxtDate);
    });
    $(document).on('dp.change change', '.cusStrDate, .cusEndDate', function () {
      if ($(this).parents('.manually-option').find('.cusStrDate').val() && $(this).parents(
          '.manually-option').find('.cusEndDate').val()) {
        $(this).parents('.manually-option').find('.cusTime').prop('disabled', false);
      } else {
        $(this).parents('.manually-option').find('.cusTime').prop('disabled', true);
      }
      resetValidation($(this).parents('form'));
    });

    $('#pickStartDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      useCurrent: false,
      ignoreReadonly: true
    }).on("dp.change", function (e) {
      $('#pickStartDate').attr("readonly", true);
      var pickStartDate = $("#pickStartDate").val();
      var months = $("#months").val();
      if ((pickStartDate != null && pickStartDate != '' && typeof pickStartDate != 'undefined')
          && (months != null && months != '' && typeof months != 'undefined')) {
        var dt = new Date(pickStartDate);
        endDate = moment(moment(dt).add(Number(months), 'M')).format("MM/DD/YYYY");
        $("#studyMonthlyLifetimeEnd").val(endDate);
        $("#monthEndDate").text(endDate);
        $("#monthLifeTimeDate").text(pickStartDate + ' - ' + endDate);
      }
    }).on("click", function (e) {
      $('#pickStartDate').data("DateTimePicker").minDate(serverDate());
    });
    $('#startWeeklyDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      useCurrent: false,
      ignoreReadonly: true
    }).on("dp.change", function (e) {
      var weeklyDate = $("#startWeeklyDate").val();
      var weeks = $("#weeks").val();
      $('#startWeeklyDate').attr("readonly", true);
      if ((weeklyDate != null && weeklyDate != '' && typeof weeklyDate != 'undefined') && (weeks
          != null && weeks != '' && typeof weeks != 'undefined')) {
        var dt = new Date(weeklyDate);
        var weekcount = Number(weeks) * 7;

        dt.setDate(dt.getDate() + Number(weekcount));
        endDate = formatDate(dt);
        $("#studyWeeklyLifetimeEnd").val(endDate);
        $("#weekEndDate").text(endDate);
        $("#weekLifeTimeEnd").text(weeklyDate + ' - ' + endDate);
      }
    }).on("click", function (e) {
      $('#startWeeklyDate').data("DateTimePicker").minDate(serverDate());
    });
    $('.customCalnder').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      minDate: serverDate(),
      useCurrent: false,
    });
    var daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    $("#startDateWeekly").on('change', function () {
      var weekDay = $("#startDateWeekly").val();
      var weeks = [];
      for (var i = 0; i < daysOfWeek.length; i++) {
        if (weekDay != daysOfWeek[i]) {
          weeks.push(i);
        }
      }
      $('#startWeeklyDate').data("DateTimePicker").destroy();
      $('#startWeeklyDate').not('.cursor-none, :disabled').datetimepicker({
        format: 'MM/DD/YYYY',
        minDate: serverDate(),
        daysOfWeekDisabled: weeks,
        useCurrent: false,
        ignoreReadonly: true
      }).on("dp.change", function (e) {
        var weeklyDate = $("#startWeeklyDate").val();
        var weeks = $("#weeks").val();

        if ((weeklyDate != null && weeklyDate != '' && typeof weeklyDate != 'undefined') && (weeks
            != null && weeks != '' && typeof weeks != 'undefined')) {
          var dt = new Date(weeklyDate);
          var weekcount = Number(weeks) * 7;

          dt.setDate(dt.getDate() + Number(weekcount));
          endDate = formatDate(dt);
          $("#studyWeeklyLifetimeEnd").val(endDate);
          $("#weekEndDate").text(endDate);
          $("#weekLifeTimeEnd").text(weeklyDate + ' - ' + endDate);
        }
      });
      $('#startWeeklyDate').val('');
    });
    $("#doneId").click(function () {

      var res = localStorage.getItem("IsAnchorDateSelected");

      if (res === 'true') {
        $("#weekDaysId").hide();
        $("#startDateWeekly").removeAttr('required');
        $("#startDateWeekly").parent().parent().removeClass("has-error has-danger");
        $("#startDateWeekly").next().children().remove();
        $(".weeklyRegular").hide();
        $(".weeklyRegular").removeAttr('required');

        $("#monthlyDateId").hide();
        $("#startDateMonthly").removeAttr('required');
        $("#startDateMonthly").parent().parent().removeClass("has-error has-danger");
        $("#startDateMonthly").next().children().remove();
        $(".monthlyRegular").hide();
        $(".monthlyRegular").removeAttr('required');
      } else {
        $("#weekDaysId").show();
        $("#startDateWeekly").attr('required');
        $(".weeklyRegular").show();
        $(".weeklyRegular").attr('required', true);

        $("#monthlyDateId").show();
        $("#startDateMonthly").attr('required');
        $(".monthlyRegular").show();
        $(".monthlyRegular").attr('required', true);

      }

      var table = $('#content').DataTable();
      validateShortTitle('', function (val) {
        if (val) {
          if ($('#pickStartDate').val() == '') {
            $('#pickStartDate').attr("readonly", false);
          }
          if ($('#startWeeklyDate').val() == '') {
            $('#startWeeklyDate').attr("readonly", false);
          }
          if (isFromValid("#contentFormId")) {
            doneQuestionnaire(this, 'done', function (val) {
              if (val) {
                validateLinceChartSchedule('', '', function (valid) {
                  if (valid) {
                    document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do?_S=${param._S}";
                    document.contentFormId.submit();
                  } else {
                    $("body").removeClass("loading");
                    $('.contentqusClass a').tab('show');
                    showErrMsg(
                        "One or more steps has a question added to dashboard line chart. Please update the time range for these line charts based on the questionnaire schedule.");
                  }
                });

              }
            });
          } else {
            showErrMsg("Please fill in all mandatory fields.");
            var slaCount = $('#contentTab').find('.has-error.has-danger').length;
            var flaCount = $('#schedule').find('.has-error.has-danger').length;
            if (parseInt(slaCount) >= 1) {
              $('.contentqusClass a').tab('show');
            } else if (parseInt(flaCount) >= 1) {
              $('.scheduleQusClass a').tab('show');
            }
          }
        } else {
          showErrMsg("Please fill in all mandatory fields.");
          var slaCount = $('#contentTab').find('.has-error.has-danger').length;
          var flaCount = $('#schedule').find('.has-error.has-danger').length;
          if (parseInt(slaCount) >= 1) {
            $('.contentqusClass a').tab('show');
          } else if (parseInt(flaCount) >= 1) {
            $('.scheduleQusClass a').tab('show');
          }
        }
      });
    });
    $("#saveId").click(function () {
     autoSaveQuestionnaire('manual');
    });
    $("#days").on('change', function () {

      var startDate = $("#startDate").val();
      var days = $("#days").val();
      var endDate = ''
      if (startDate && days && days > 0) {
        var dt = new Date(startDate);
        dt.setDate(dt.getDate() + Number(days) - 1);
        endDate = formatDate(dt);
      } else {
        startDate = '';
        endDate = '';
      }
      $("#studyDailyLifetimeEnd").val(endDate);
      $("#lifeTimeId").text(startDate + ' - ' + endDate);
      $("#endDateId").text(endDate ? endDate : 'NA');
    })

    $("#weeks").on('change', function () {
      var weeklyDate = $("#startWeeklyDate").val();
      var weeks = $("#weeks").val();

      if ((weeklyDate != null && weeklyDate != '' && typeof weeklyDate != 'undefined') && (weeks
          != null && weeks != '' && typeof weeks != 'undefined')) {
        var dt = new Date(weeklyDate);
        var weekcount = Number(weeks) * 7;

        dt.setDate(dt.getDate() + Number(weekcount));
        endDate = formatDate(dt);
        $("#studyWeeklyLifetimeEnd").val(endDate);
        $("#weekEndDate").text(endDate);
        $("#weekLifeTimeEnd").text(weeklyDate + ' - ' + endDate);
      }
    });
    $("#months").on('change', function () {
      var pickStartDate = $("#pickStartDate").val();
      var months = $("#months").val();
      if ((pickStartDate != null && pickStartDate != '' && typeof pickStartDate != 'undefined')
          && (months != null && months != '' && typeof months != 'undefined')) {
        var dt = new Date(pickStartDate);
        endDate = moment(moment(dt).add(Number(months), 'M')).format("MM/DD/YYYY");
        $("#studyMonthlyLifetimeEnd").val(endDate);
        $("#monthEndDate").text(endDate);
        $("#monthLifeTimeDate").text(pickStartDate + ' - ' + endDate);
      }
    });
    $("#isLaunchStudy").change(function () {
      var scheduletype = $('input[name="scheduleType"]:checked').val();
      if (!$("#isLaunchStudy").is(':checked')) {
        if (scheduletype != '' && scheduletype != null && typeof scheduletype != 'undefined'
            && scheduletype == 'AnchorDate') {
          $("#onetimexdaysId").prop('disabled', false);
          $("#selectTime").attr("disabled", false);
          $("#selectTime").required = true;
          $("#onetimexdaysId").required = true;
        } else {
          $("#chooseDate").attr("disabled", false);
          $("#selectTime1").attr("disabled", false);
          $("#chooseDate").required = false;
          $("#selectTime1").required = false;
          $('#chooseDate').datetimepicker({
            format: 'MM/DD/YYYY',
            minDate: serverDate(),
            useCurrent: false,
          })
          .on("dp.change", function (e) {
            if (e.date._d)
              $("#chooseEndDate").data("DateTimePicker").clear().minDate(new Date(e.date._d));
            else
              $("#chooseEndDate").data("DateTimePicker").minDate(serverDate());
          });
        }
      } else {
        $("#chooseDate").val('');
        $("#selectTime").val('');
        if (scheduletype == 'AnchorDate') {
          $("#selectTime").attr("disabled", true);
          $("#selectTime").required = false;
          $("#onetimexdaysId").prop('disabled', true);
          $("#onetimexdaysId").required = false;
        } else {
          $("#chooseDate").attr("disabled", true);
          $("#selectTime1").attr("disabled", true);
          $("#chooseDate").required = true;
          $("#selectTime1").required = true;
        }
      }
      resetValidation($(this).parents('form'));
      //resetValidation($("#oneTimeFormId"));
    });
    $("#isStudyLifeTime").change(function () {
      var scheduletype = $('input[name="scheduleType"]:checked').val();
      if (!$("#isStudyLifeTime").is(':checked')) {
        if (scheduletype != '' && scheduletype != null && typeof scheduletype != 'undefined'
            && scheduletype == 'AnchorDate') {
          $("#onetimeydaysId").prop('disabled', false);
          $('#onetimeydaysId').parent().removeClass('has-error has-danger').find(
              ".help-block").empty();
          resetValidation($('#onetimeydaysId').parents('form'));
        } else {
          $("#chooseEndDate").attr("disabled", false);
          $("#chooseEndDate").required = false;
          $('#chooseEndDate').datetimepicker({
            format: 'MM/DD/YYYY',
            minDate: serverDate(),
            useCurrent: false,
          });
          $("#chooseEndDate").val('');
        }
      } else {
        if (scheduletype == 'AnchorDate') {
          $("#onetimeydaysId").prop('disabled', true);

        } else {
          $("#chooseEndDate").attr("disabled", true);
          $("#chooseEndDate").required = true;
          $("#chooseEndDate").val('');
        }
      }
      resetValidation($(this).parents('form'));
    });
    $("#shortTitleId").blur(function () {
      validateShortTitle('', function (val) {
      });
    });

    // Branching Logic starts here

    $("#branchingId").change(function () {
      if ($("#branchingId").is(':checked')) {
        $(".deleteStepButton").hide();
        $(".destinationStep").show();
        table1.rowReorder.disable();
      } else {
        $(".deleteStepButton").show();
        $(".destinationStep").hide();
        table1.rowReorder.enable();
      }
    });
    var branching = "${questionnaireBo.branching}";
    if (branching == "true") {
      $(".destinationStep").show();
      $(".deleteStepButton").hide();
      table1.rowReorder.disable();
    } else {
      $(".destinationStep").hide();
      $(".deleteStepButton").show();
      table1.rowReorder.enable();
    }
    // Branching Logic starts here

    disablePastTime('#selectWeeklyTime', '#startWeeklyDate');
    disablePastTime('#selectMonthlyTime', '#startDateMonthly');
    disablePastTime('#selectTime', '#chooseDate', '#selectTime1');

    $(document).on('click change dp.change', '.cusStrDate, .cusTime', function (e) {
      if ($(this).is('.cusTime') && !$(this).prop('disabled')) {
        disablePastTime('#' + $(this).attr('id'),
            '#' + $(this).parents('.manually-option').find('.cusStrDate').attr('id'));
      }
      if ($(this).is('.cusStrDate') && !$(this).parents('.manually-option').find('.cusTime').prop(
          'disabled')) {
        disablePastTime('#' + $(this).parents('.manually-option').find('.cusTime').attr('id'),
            '#' + $(this).attr('id'));
      }
    });

    $(document).on('click change dp.change', '.dailyClock, #startDate', function (e) {
      var dt = $('#startDate').val();
      var date = new Date();
      var day = date.getDate() >= 10 ? date.getDate() : ('0' + date.getDate());
      var month = (date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : ('0' + (date.getMonth()
          + 1));
      var today = moment(serverDate()).format("MM/DD/YYYY"); // month + '/' +  day + '/' + date.getFullYear();

      $('.time-opts').each(function () {
        var id = $(this).attr("id");
        var timeId = '#time' + id;
        $(timeId).data("DateTimePicker").minDate(false);
        if (dt) {
          if (dt != today) {
            $(timeId).data("DateTimePicker").minDate(false);
          } else {
            $(timeId).data("DateTimePicker").minDate(serverDateTime());
          }
          if ($(timeId).val() && dt == today && moment($(timeId).val(), 'h:mm a').toDate()
              < serverDateTime()) {
            $(timeId).val('');
          }
        } else {
          $(timeId).data("DateTimePicker").minDate(false);
        }
      });
    });

    $('[data-toggle="tooltip"]').tooltip();

    $("#infoIconId").hover(function () {
      $('#myModal').modal('show');
    });

    $('#anchorDateId').change(function () {
      var frequency_text = $('input[name="frequency"]:checked').val();
      var element = $(this).find('option:selected').text();
      setAnchorDropdown(frequency_text, element);
    });
    
    setInterval(function () {
        idleTime += 1;
        if (idleTime > 3) {
                <c:if test="${actionType ne 'view'}">
                autoSaveQuestionnaire('auto');
                 </c:if>
                <c:if test="${actionType eq 'view'}">
                    timeOutFunction();
                </c:if>
        }
    }, 226000); // 5 minutes

    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });

    function timeOutFunction() {
     $('#timeOutModal').modal('show');
      let i = 14;
      let timeOutInterval = setInterval(function () {
       if (i === 0) {
        $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
         if ($('#timeOutModal').hasClass('show')) {
           $('#backToLoginPage').submit();
        }
         clearInterval(timeOutInterval);
           } else {
             if (i === 1) {
           $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
             } else {
             $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
            }
             idleTime = 0;
             i-=1;
              }
              }, 60000);
              }
  });
  function autoSaveQuestionnaire(mode){
	     var table = $('#content').DataTable();
	      validateShortTitle('', function (val) {
	        if (val) {
	          if (isFromValid("#contentFormId")) {
                  if (mode === 'auto') {
                      $('#isAutoSaved').val('true');
                  } else {
                      $('#isAutoSaved').val('false');
                  }
	            doneQuestionnaire(this, 'save', function (val) {
	              if (val) {
	                showSucMsg("Content saved as draft.");
	              } else {
	                var slaCount = $('#contentTab').find('.has-error.has-danger').length;
	                var flaCount = $('#schedule').find('.has-error.has-danger').length;
	                if (parseInt(slaCount) >= 1) {
	                  $('.contentqusClass a').tab('show');
	                } else if (parseInt(flaCount) >= 1) {
	                  $('.scheduleQusClass a').tab('show');
	                }
	              }
	            });
	          } else {
	            var slaCount = $('#contentTab').find('.has-error.has-danger').length;
	            var flaCount = $('#schedule').find('.has-error.has-danger').length;
	            if (parseInt(slaCount) >= 1) {
	              $('.contentqusClass a').tab('show');
	            } else if (parseInt(flaCount) >= 1) {
	              $('.scheduleQusClass a').tab('show');
	            }
	          }
	        } else {
	          var slaCount = $('#contentTab').find('.has-error.has-danger').length;
	          var flaCount = $('#schedule').find('.has-error.has-danger').length;
	          if (parseInt(slaCount) >= 1) {
	            $('.contentqusClass a').tab('show');
	          } else if (parseInt(flaCount) >= 1) {
	            $('.scheduleQusClass a').tab('show');
	          }
	        }
	      });
  }
  function formatDate(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [month, day, year].join('/');
  }

  function addTime() {
    count = count + 1;
    var newTime = "<div class='time-opts mt-md dailyTimeDiv' id=" + count + ">" +
        "  <span class='form-group m-none dis-inline vertical-align-middle pr-md'>" +
        "  <input id='time" + count + "' type='text' required name='questionnairesFrequenciesList["
        + count
        + "].frequencyTime' placeholder='Time' class='form-control clock dailyClock' placeholder='00:00' onclick='timep(this.id);'/>"
        +
        "<span class='help-block with-errors red-txt'></span>" +
        " </span>" +
        "  <span class='addBtnDis addbtn mr-sm align-span-center' onclick='addTime();'>+</span>" +
        " <span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeTime(this);'></span>"
        +
        "</div>";
    $(".time-opts:last").after(newTime);
    $(".time-opts").parents("form").validator("destroy");
    $(".time-opts").parents("form").validator();
    if ($('.time-opts').length > 1) {
      $(".remBtnDis").removeClass("hide");
    } else {
      $(".remBtnDis").addClass("hide");
    }
    timep('time' + count);
    $('#time' + count).val("");
    $(document).find('.dailyClock').trigger('dp.change');
    $('#' + count).find('input:first').focus();
  }

  function removeTime(param) {
    $(param).parents(".time-opts").remove();
    $(".time-opts").parents("form").validator("destroy");
    $(".time-opts").parents("form").validator();
    if ($('.time-opts').length > 1) {
      $(".remBtnDis").removeClass("hide");
    } else {
      $(".remBtnDis").addClass("hide");
    }
  }

  function addDate() {
    customCount = customCount + 1;
    var newDateCon = "<div class='manually-option mb-md form-group' id='" + customCount + "'>"
        + "  <span class='form-group dis-inline vertical-align-middle pr-md'>"
        + "  <input id='StartDate" + customCount + "' type='text' count='" + customCount
        + "' required name='questionnaireCustomScheduleBo[" + customCount
        + "].frequencyStartDate' class='form-control calendar customCalnder cusStrDate' placeholder='Start Date' onclick='customStartDate(this.id,"
        + customCount + ");'/>"
        + "	<span class='help-block with-errors red-txt'></span>"
        + "  </span>"
        + "  <span class='gray-xs-f mb-sm pr-md align-span-center'>"
        + "  to "
        + "  </span>"
        + "  <span class='form-group dis-inline vertical-align-middle pr-md'>"
        + "  <input id='EndDate" + customCount + "' type='text' count='" + customCount
        + "' required name='questionnaireCustomScheduleBo[" + customCount
        + "].frequencyEndDate' class='form-control calendar customCalnder cusEndDate' placeholder='End Date' onclick='customEndDate(this.id,"
        + customCount + ");'/>"
        + "<span class='help-block with-errors red-txt'></span>"
        + "  </span>"
        + "  <span class='form-group dis-inline vertical-align-middle pr-md'>"
        + "  <input id='customTime" + customCount + "' type='text' count='" + customCount
        + "' required name='questionnaireCustomScheduleBo[" + customCount
        + "].frequencyTime' class='form-control clock customTime cusTime' placeholder='Time' onclick='timep(this.id);' disabled/>"
        + "<span class='help-block with-errors red-txt'></span>"
        + "  </span>"
        + "  <span class='addbtn addBtnDis align-span-center mr-md' onclick='addDate();'>+</span>"
        + "  <span id='delete' class='sprites_icon delete vertical-align-middle remBtnDis hide align-span-center' onclick='removeDate(this);'></span>"
        + "</div>";

    $(".manually-option:last").after(newDateCon);
    $(".manually-option").parents("form").validator("destroy");
    $(".manually-option").parents("form").validator();
    if ($('.manually-option').length > 1) {
      $('.manuallyContainer').find(".remBtnDis").removeClass("hide");
    } else {
      $('.manuallyContainer').find(".remBtnDis").addClass("hide");
    }
    if ($('.manually-option').length == 1) {
      $('.manually-option').find('.delete').addClass('display__none');
    } else {
      $('.delete').removeClass('display__none');
    }
    customStartDate('StartDate' + customCount, customCount);
    customEndDate('EndDate' + customCount, customCount);
    timep('customTime' + customCount);
    $('#customTime' + customCount).val("");
    $('#' + customCount).find('input:first').focus();
  }

  function removeDate(param) {
    $(param).parents(".manually-option").remove();
    $(".manually-option").parents("form").validator("destroy");
    $(".manually-option").parents("form").validator();
    if ($('.manually-option').length > 1) {
      $('.manuallyContainer').find(".remBtnDis").removeClass("hide");
    } else {
      $('.manuallyContainer').find(".remBtnDis").addClass("hide");
    }
    if ($('.manually-option').length == 1) {
      $('.manually-option').find('.delete').addClass('display__none');
    } else {
      $('.delete').removeClass('display__none');
    }
    $(document).find('.cusTime').trigger('dp.change');
  }

  function timep(item) {
    $('#' + item).not('.cursor-none').datetimepicker({
      format: 'h:mm a',
      useCurrent: false,
    });
  }

  function customStartDate(id, count) {

    $('.cusStrDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      minDate: serverDate(),
      useCurrent: false,
    }).on("dp.change", function (e) {
      $("#" + id).parent().removeClass("has-danger").removeClass("has-error");
      $("#" + id).parent().find(".help-block").empty();
      $("#EndDate" + count).parent().removeClass("has-danger").removeClass("has-error");
      $("#EndDate" + count).parent().find(".help-block").empty();
      var startDate = $("#" + id).val();
      var endDate = $("#EndDate" + count).val();
      if (startDate != '' && endDate != '' && toJSDate(startDate) > toJSDate(endDate)) {
        $("#" + id).parent().addClass("has-danger").addClass("has-error");
        $("#" + id).parent().find(".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "Start Date and Time Should not be greater than End Date and Time"));
      } else {
        $("#id").parent().removeClass("has-danger").removeClass("has-error");
        $("#id").parent().find(".help-block").empty();
        $("#EndDate" + count).parent().removeClass("has-danger").removeClass("has-error");
        $("#EndDate" + count).parent().find(".help-block").empty();

      }
    });
  }

  function customEndDate(id, count) {
    $('.cusEndDate').not('.cursor-none, :disabled').datetimepicker({
      format: 'MM/DD/YYYY',
      minDate: serverDate(),
      useCurrent: false,
    }).on("dp.change", function (e) {
      $('#' + id).parent().removeClass("has-danger").removeClass("has-error");
      $('#' + id).parent().find(".help-block").empty();
      $("#StartDate" + count).parent().removeClass("has-danger").removeClass("has-error");
      $("#StartDate" + count).parent().find(".help-block").empty();
      var startDate = $("#StartDate" + count).val();
      var endDate = $('#' + id).val();
      if (startDate != '' && endDate != '' && toJSDate(startDate) > toJSDate(endDate)) {
        $('#' + id).parent().addClass("has-danger").addClass("has-error");
        $('#' + id).parent().find(".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "End Date and Time Should not be less than Start Date and Time"));
      } else {
        $('#' + id).parent().removeClass("has-danger").removeClass("has-error");
        $('#' + id).parent().find(".help-block").empty();
        $("#StartDate" + count).parent().removeClass("has-danger").removeClass("has-error");
        $("#StartDate" + count).parent().find(".help-block").empty();
      }
    });
  }

  function toJSDate(dateTime) {
    if (dateTime != null && dateTime != '' && typeof dateTime != 'undefined') {
      var date = dateTime.split("/");
      return new Date(date[2], (date[0] - 1), date[1]);
    }
  }

  function saveQuestionnaire(item, callback) {

    var id = $("#id").val();
    var study_id = $("#studyId").val();
    var title_text = $("#titleId").val();
    var short_title = $("#shortTitleId").val();
    var frequency_text = $('input[name="frequency"]:checked').val();
    var schedule_text = $('input[name="scheduleType"]:checked').val();
    var previous_frequency = $("#previousFrequency").val();
    var addNewRuns = $("#addNewRunsId").val();
    var isFormValid = true;
    var statusText = $("#status").val();
    var study_lifetime_end = '';
    var study_lifetime_start = ''
    var repeat_questionnaire = ''
    var scheduletype = document.querySelector('input[name="scheduleType"]:checked').value;
    branching = $('input[name="branching"]:checked').val();

    var type_text = "";
    var tab = $("#tabContainer li.active").text();

    type_text = "schedule";
    var questionnaire = new Object();
    questionnaire.status = statusText;
    var anchorDateId = $("#anchorDateId option:selected").val();
    if (anchorDateId != null && anchorDateId != '' && typeof anchorDateId != 'undefined') {
      questionnaire.anchorDateId = anchorDateId;
    }

    if (id != null && id != '' && typeof id != 'undefined') {
      questionnaire.id = id;
    }
    if (branching != null && branching != '' && typeof branching != 'undefined') {
      questionnaire.branching = branching;
    }
    if (frequency_text != null && frequency_text != '' && typeof frequency_text != 'undefined') {
      questionnaire.frequency = frequency_text;
    }
    if (study_id != null && study_id != '' && typeof study_id != 'undefined') {
      questionnaire.studyId = study_id;
    }
    if (short_title != null && short_title != '' && typeof short_title != 'undefined') {
      questionnaire.shortTitle = short_title;
    }
    if (title_text != null && title_text != '' && typeof title_text != 'undefined') {
      questionnaire.title = title_text;
    }
    if (previous_frequency != null && previous_frequency != '' && typeof previous_frequency
        != 'undefined') {
      questionnaire.previousFrequency = previous_frequency;
    } else {
      questionnaire.previousFrequency = frequency_text;
    }
    if (type_text != null && type_text != '' && typeof type_text != 'undefined') {
      questionnaire.type = type_text;
    }
    if (schedule_text != null && schedule_text != '' && typeof schedule_text != 'undefined') {
      questionnaire.scheduleType = schedule_text;
    }
    if (addNewRuns != null && addNewRuns != '' && typeof addNewRuns != 'undefined') {
      questionnaire.addNewRuns = addNewRuns;
    }

    var questionnaireFrequencey = new Object();
    if (frequency_text == 'One time') {

      var frequence_id = $("#oneTimeFreId").val();
      var frequency_date = $("#chooseDate").val();
      var freQuence_time = $("#selectTime1").val();
      if ($('#isLaunchStudy').is(':checked')) {
        var isLaunch_study = true;
      }
      if ($('#isStudyLifeTime').is(':checked')) {
        var isStudy_lifeTime = true;
      }

      study_lifetime_end = $("#chooseEndDate").val();
      if (study_lifetime_end != null && study_lifetime_end != '' && typeof study_lifetime_end
          != 'undefined') {
        questionnaire.studyLifetimeEnd = study_lifetime_end;
      }
      if (frequence_id != null && frequence_id != '' && typeof frequence_id != 'undefined') {
        questionnaireFrequencey.id = frequence_id;
      }
      if (frequency_date != null && frequency_date != '' && typeof frequency_date != 'undefined') {
        questionnaireFrequencey.frequencyDate = frequency_date;
        questionnaire.studyLifetimeStart = frequency_date;
      }
      if (freQuence_time != null && freQuence_time != '' && typeof freQuence_time != 'undefined') {
        questionnaireFrequencey.frequencyTime = freQuence_time;
      }
      if (isLaunch_study != null && isLaunch_study != '' && typeof isLaunch_study != 'undefined') {
        questionnaireFrequencey.isLaunchStudy = isLaunch_study;
      }
      if (isStudy_lifeTime != null && isStudy_lifeTime != '' && typeof isStudy_lifeTime
          != 'undefined') {
        questionnaireFrequencey.isStudyLifeTime = isStudy_lifeTime;
      }
      if (id != null && id != '' && typeof id != 'undefined') {
        questionnaireFrequencey.questionnairesId = id;
      }
      if (scheduletype == 'AnchorDate') {
        var onetimeXSign = $('#onetimeXSign').val();
        var onetimeXSignVal = $('#onetimexdaysId').val();
        var onetimeYSign = $('#onetimeYSign').val();
        var onetimeYSignVal = $('#onetimeydaysId').val();
        if (onetimeXSign != null && onetimeXSign != '' && typeof onetimeXSign != 'undefined') {
          var xval = true;
          if (onetimeXSign == '0')
            xval = false;
          questionnaireFrequencey.xDaysSign = xval;
        }
        if (onetimeXSignVal != null && onetimeXSignVal != '' && typeof onetimeXSignVal
            != 'undefined') {
          questionnaireFrequencey.timePeriodFromDays = onetimeXSignVal;
        }
        if (onetimeYSign != null && onetimeYSign != '' && typeof onetimeYSign != 'undefined') {
          var yval = true;
          if (onetimeYSign == '0')
            yval = false;
          questionnaireFrequencey.yDaysSign = yval;
        }
        if (onetimeYSignVal != null && onetimeYSignVal != '' && typeof onetimeYSignVal
            != 'undefined') {
          questionnaireFrequencey.timePeriodToDays = onetimeYSignVal;
        }
        var freQuence_time = $("#selectTime").val();
        if (freQuence_time != null && freQuence_time != '' && typeof freQuence_time
            != 'undefined') {
          questionnaireFrequencey.frequencyTime = freQuence_time;
        }

        if ($('#isLaunchStudy').is(':checked')) {
          questionnaireFrequencey.timePeriodFromDays = null;
          questionnaireFrequencey.xDaysSign = true;
          questionnaireFrequencey.frequencyTime = null;
        }
        if ($('#isStudyLifeTime').is(':checked')) {
          questionnaireFrequencey.timePeriodToDays = null;
          questionnaireFrequencey.yDaysSign = true;
        }
        questionnaireFrequencey.frequencyDate = null;
        //questionnaire.studyLifetimeStart=null;
        //questionnaire.studyLifetimeEnd=null;
      } else {
        questionnaire.anchorDateId = null;
        questionnaireFrequencey.timePeriodFromDays = null;
        questionnaireFrequencey.xDaysSign = true;
        questionnaireFrequencey.timePeriodToDays = null;
        questionnaireFrequencey.yDaysSign = true;
      }
      questionnaire.questionnairesFrequenciesBo = questionnaireFrequencey;
      isFormValid = validateTime($("#chooseDate").not('.cursor-none, :disabled'),
          $("#selectTime").not('.cursor-none, :disabled'));
    } else if (frequency_text == 'Manually Schedule') {
      var customArray = new Array();
      isFormValid = isValidManuallySchedule;
      if (scheduletype == 'AnchorDate') {
        $('.manually-anchor-option').each(function () {
          var questionnaireCustomFrequencey = new Object();
          questionnaireCustomFrequencey.questionnairesId = id;
          var id = $(this).attr("id");
          var xSign = $('#xSign' + id).val();
          var xSignVal = $('#xdays' + id).val();
          var ySign = $('#ySign' + id).val();
          var ySignVal = $('#ydays' + id).val();
          var time = $("#manualTime" + id).val();
          var isUsed = $("#isUsed" + id).val();

          questionnaireCustomFrequencey.frequencyStartDate = null;
          questionnaireCustomFrequencey.frequencyEndDate = null;
          if (time != null && time != '' && typeof time != 'undefined') {
            questionnaireCustomFrequencey.frequencyTime = time;
          }
          if (isUsed) {
            questionnaireCustomFrequencey.used = isUsed;
          }
          if (xSign != null && xSign != '' && typeof xSign != 'undefined') {
            var xval = true;
            if (xSign == '0')
              xval = false;
            questionnaireCustomFrequencey.xDaysSign = xval;
          }
          if (xSignVal != null && xSignVal != '' && typeof xSignVal != 'undefined') {
            questionnaireCustomFrequencey.timePeriodFromDays = xSignVal;
          }
          if (ySign != null && ySign != '' && typeof ySign != 'undefined') {
            var yval = true;
            if (ySign == '0')
              yval = false;
            questionnaireCustomFrequencey.yDaysSign = yval;
          }
          if (ySignVal != null && ySignVal != '' && typeof ySignVal != 'undefined') {
            questionnaireCustomFrequencey.timePeriodToDays = ySignVal;
          }
          customArray.push(questionnaireCustomFrequencey)
        })
        questionnaire.questionnaireCustomScheduleBo = customArray;
      } else {
        $('.manually-option').each(function () {
          var questionnaireCustomFrequencey = new Object();
          questionnaireCustomFrequencey.questionnairesId = id;
          var id = $(this).attr("id");
          var startdate = $("#StartDate" + id).val();
          var enddate = $("#EndDate" + id).val();
          var time = $("#customTime" + id).val();
          var isUsed = $("#isUsed" + id).val();
          if (startdate != null && startdate != '' && typeof startdate != 'undefined') {
            questionnaireCustomFrequencey.frequencyStartDate = startdate;
          }
          if (enddate != null && enddate != '' && typeof enddate != 'undefined') {
            questionnaireCustomFrequencey.frequencyEndDate = enddate;
          }
          if (time != null && time != '' && typeof time != 'undefined') {
            questionnaireCustomFrequencey.frequencyTime = time;
          }
          if (isUsed) {
            questionnaireCustomFrequencey.used = isUsed;
          }
          questionnaireFrequencey.xDaysSign = 0;
          questionnaireFrequencey.timePeriodFromDays = null;
          questionnaireFrequencey.yDaysSign = 0;
          questionnaireFrequencey.timePeriodToDays = null;
          customArray.push(questionnaireCustomFrequencey)
        })
        questionnaire.questionnaireCustomScheduleBo = customArray;
        if (isValidManuallySchedule) {
          $(document).find('.manually-option').each(function () {
            var returnFlag = validateTime(
                $(this).find(".cusStrDate").not('.cursor-none, :disabled'),
                $(this).find(".cusTime").not('.cursor-none, :disabled'));
            if (isFormValid) {
              isFormValid = returnFlag;
            }
          });
        }
      }
    } else if (frequency_text == 'Daily') {
      isFormValid = multiTimeVal;
      var frequenceArray = new Array();
      study_lifetime_start = $("#startDate").val();
      repeat_questionnaire = $("#days").val();
      study_lifetime_end = $("#endDateId").text();
      var dailyXSign = $('#dailyXSign').val();
      var dailyXSignVal = $('#dailyxdaysId').val();

      if ($('.time-opts').length > 1) {
        questionnaire.currentFrequency = "Daily";
      } else {
        questionnaire.currentFrequency = "One Time";
      }
      //1st record dailyxsign need to store
      var count = 0;
      $('.time-opts').each(function () {
        var questionnaireFrequencey = new Object();
        var id = $(this).attr("id");

        var frequence_time = $('#time' + id).val();

        if (frequence_time != null && frequence_time != '' && typeof frequence_time
            != 'undefined') {
          questionnaireFrequencey.frequencyTime = frequence_time;
        }
        if (dailyXSign != null && dailyXSign != '' && typeof dailyXSign != 'undefined' && count
            == 0) {
          var xval = true;
          if (dailyXSign == '0')
            xval = false;
          questionnaireFrequencey.xDaysSign = xval;
          if (dailyXSignVal != null && dailyXSignVal != '' && typeof dailyXSignVal != 'undefined') {
            questionnaireFrequencey.timePeriodFromDays = dailyXSignVal;
            questionnaireFrequencey.timePeriodToDays = null;
            questionnaireFrequencey.yDaysSign = true;
          } else {
            questionnaireFrequencey.timePeriodFromDays = null;
            questionnaireFrequencey.xDaysSign = true;
            questionnaireFrequencey.timePeriodToDays = null;
            questionnaireFrequencey.yDaysSign = true;
          }
          count = 1;
        }
        frequenceArray.push(questionnaireFrequencey);

      })
      questionnaire.questionnairesFrequenciesList = frequenceArray;
      if (study_lifetime_start != null && study_lifetime_start != '' && typeof study_lifetime_start
          != 'undefined') {
        questionnaire.studyLifetimeStart = study_lifetime_start;
      }
      if (study_lifetime_end != null && study_lifetime_end != '' && typeof study_lifetime_end
          != 'undefined') {
        questionnaire.studyLifetimeEnd = study_lifetime_end;
      }
      if (repeat_questionnaire != null && repeat_questionnaire != '' && typeof repeat_questionnaire
          != 'undefined') {
        questionnaire.repeatQuestionnaire = repeat_questionnaire;
      }
      questionnaire.questionnairesFrequenciesBo = questionnaireFrequencey;
      if (multiTimeVal && $('#dailyFormId').find('.numChk').val() && $('#dailyFormId').find(
          '.numChk').val() == 0 || !(validateTime(
          $(document).find("#startDate").not('.cursor-none, :disabled'),
          $(document).find(".dailyClock").not('.cursor-none, :disabled')))) {
        isFormValid = false;
      }

      if (scheduletype == 'AnchorDate') {
        questionnaire.studyLifetimeStart = null;
        questionnaire.studyLifetimeEnd = null;
      }

    } else if (frequency_text == 'Weekly') {
      var frequence_id = $("#weeklyFreId").val();
      study_lifetime_start = $("#startWeeklyDate").val();
      var frequence_time = $("#selectWeeklyTime").val();
      var frequence_time_anchor = $("#selectWeeklyTimeAnchor").val();
      var dayOftheweek = $("#startDateWeekly").val();
      repeat_questionnaire = $("#weeks").val();
      repeat_questionnaire_anchor = $("#weeksAnchor").val();
      study_lifetime_end = $("#weekEndDate").text();
      var weeklyXSign = $('#weeklyXSign').val();
      var weeklyXSignVal = $('#weeklyxdaysId').val();

      if (dayOftheweek != null && dayOftheweek != '' && typeof dayOftheweek != 'undefined') {
        questionnaire.dayOfTheWeek = dayOftheweek;
      }
      if (study_lifetime_start != null && study_lifetime_start != '' && typeof study_lifetime_start
          != 'undefined') {
        questionnaire.studyLifetimeStart = study_lifetime_start;
      }
      if (study_lifetime_end != null && study_lifetime_end != '' && typeof study_lifetime_end
          != 'undefined') {
        questionnaire.studyLifetimeEnd = study_lifetime_end;
      }
      if (repeat_questionnaire != null && repeat_questionnaire != '' && typeof repeat_questionnaire
          != 'undefined') {
        questionnaire.repeatQuestionnaire = repeat_questionnaire;
      }

      if (repeat_questionnaire_anchor != null && repeat_questionnaire_anchor != ''
          && typeof repeat_questionnaire_anchor != 'undefined') {
        questionnaire.repeatQuestionnaire = repeat_questionnaire_anchor;
      }

      if (id != null && id != '' && typeof id != 'undefined') {
        questionnaireFrequencey.questionnairesId = id;
      }
      if (frequence_id != null && frequence_id != '' && typeof frequence_id != 'undefined') {
        questionnaireFrequencey.id = frequence_id;
      }
      if (frequence_time != null && frequence_time != '' && typeof frequence_time != 'undefined') {
        questionnaireFrequencey.frequencyTime = frequence_time;
      }

      if (frequence_time_anchor != null && frequence_time_anchor != ''
          && typeof frequence_time_anchor != 'undefined') {
        questionnaireFrequencey.frequencyTime = frequence_time_anchor;
      }

      if (weeklyXSign != null && weeklyXSign != '' && typeof weeklyXSign != 'undefined') {
        var xval = true;
        if (weeklyXSign == '0')
          xval = false;
        questionnaireFrequencey.xDaysSign = xval;
      }
      if (weeklyXSignVal != null && weeklyXSignVal != '' && typeof weeklyXSignVal != 'undefined') {
        questionnaireFrequencey.timePeriodFromDays = weeklyXSignVal;
        questionnaireFrequencey.timePeriodToDays = null;
        questionnaireFrequencey.yDaysSign = true;
      } else {
        questionnaireFrequencey.timePeriodFromDays = null;
        questionnaireFrequencey.xDaysSign = true;
        questionnaireFrequencey.timePeriodToDays = null;
        questionnaireFrequencey.yDaysSign = true;
      }
      questionnaire.questionnairesFrequenciesBo = questionnaireFrequencey;
      if ($('#weeklyFormId').find('.numChk').val() && $('#weeklyFormId').find('.numChk').val() == 0
          || !(validateTime($(document).find("#startWeeklyDate").not('.cursor-none, :disabled'),
              $(document).find("#selectWeeklyTime").not('.cursor-none, :disabled')))) {
        isFormValid = false;
      }
      if (scheduletype == 'AnchorDate') {
        questionnaire.studyLifetimeStart = null;
        questionnaire.studyLifetimeEnd = null;
      }
    } else if (frequency_text == 'Monthly') {

      var frequence_id = $("#monthFreId").val();
      var frequencydate = $("#startDateMonthly").val();
      var frequencetime = $("#selectMonthlyTime").val();
      var frequencetime_anchor = $("#selectMonthlyTimeAnchor").val();
      study_lifetime_start = $("#pickStartDate").val();
      repeat_questionnaire = $("#months").val();
      repeat_questionnaire_anchor = $("#monthsAnchor").val();
      study_lifetime_end = $("#monthEndDate").text();
      var monthlyXSign = $('#monthlyXSign').val();
      var monthlyXSignVal = $('#monthlyxdaysId').val();

      if (study_lifetime_start != null && study_lifetime_start != '' && typeof study_lifetime_start
          != 'undefined') {
        questionnaire.studyLifetimeStart = study_lifetime_start;
      }
      if (study_lifetime_end != null && study_lifetime_end != '' && typeof study_lifetime_end
          != 'undefined') {
        questionnaire.studyLifetimeEnd = study_lifetime_end;
      }
      if (repeat_questionnaire != null && repeat_questionnaire != '' && typeof repeat_questionnaire
          != 'undefined') {
        questionnaire.repeatQuestionnaire = repeat_questionnaire;
      }

      if (repeat_questionnaire_anchor != null && repeat_questionnaire_anchor != ''
          && typeof repeat_questionnaire_anchor != 'undefined') {
        questionnaire.repeatQuestionnaire = repeat_questionnaire_anchor;
      }

      if (id != null && id != '' && typeof id != 'undefined') {
        questionnaireFrequencey.questionnairesId = id;
      }
      if (frequence_id != null && frequence_id != '' && typeof frequence_id != 'undefined') {
        questionnaireFrequencey.id = frequence_id;
      }
      if (frequencydate != null && frequencydate != '' && typeof frequencydate != 'undefined') {
        questionnaireFrequencey.frequencyDate = frequencydate;
      }
      if (frequencetime != null && frequencetime != '' && typeof frequencetime != 'undefined') {
        questionnaireFrequencey.frequencyTime = frequencetime;
      }
      if (frequencetime_anchor != null && frequencetime_anchor != '' && typeof frequencetime_anchor
          != 'undefined') {
        questionnaireFrequencey.frequencyTime = frequencetime_anchor;
      }
      if (monthlyXSign != null && monthlyXSign != '' && typeof monthlyXSign != 'undefined') {
        var xval = true;
        if (monthlyXSign == '0')
          xval = false;
        questionnaireFrequencey.xDaysSign = xval;
      }
      if (monthlyXSignVal != null && monthlyXSignVal != '' && typeof monthlyXSignVal
          != 'undefined') {
        questionnaireFrequencey.timePeriodFromDays = monthlyXSignVal;
        questionnaireFrequencey.timePeriodToDays = null;
        questionnaireFrequencey.yDaysSign = true;
      } else {
        questionnaireFrequencey.timePeriodFromDays = null;
        questionnaireFrequencey.xDaysSign = true;
        questionnaireFrequencey.timePeriodToDays = null;
        questionnaireFrequencey.yDaysSign = true;
      }
      questionnaire.questionnairesFrequenciesBo = questionnaireFrequencey;
      if ($('#monthlyFormId').find('.numChk').val() && $('#monthlyFormId').find('.numChk').val()
          == 0 || !(validateTime(
              $(document).find("#startDateMonthly").not('.cursor-none, :disabled'),
              $(document).find("#selectMonthlyTime").not('.cursor-none, :disabled')))) {
        isFormValid = false;
      }
      if (scheduletype == 'AnchorDate') {
        questionnaire.studyLifetimeStart = null;
        questionnaire.studyLifetimeEnd = null;
      }
    } else if (frequency_text == 'Ongoing') {
      /* var frequence_id = $("#monthFreId").val();
		var frequencydate = $("#startDateMonthly").val();
		var frequencetime = $("#selectMonthlyTime").val();
		var frequencetime_anchor = $("#selectMonthlyTimeAnchor").val();
		study_lifetime_start = $("#pickStartDate").val();
		repeat_questionnaire = $("#months").val();
		repeat_questionnaire_anchor = $("#monthsAnchor").val();
		study_lifetime_end = $("#monthEndDate").text();
		var monthlyXSign = $('#monthlyXSign').val();
		var monthlyXSignVal = $('#monthlyxdaysId').val();  */

      questionnaire.studyLifetimeStart = null;
      questionnaire.studyLifetimeEnd = null;

      questionnaire.repeatQuestionnaire = null;

      questionnaire.repeatQuestionnaire = null;

      if (id != null && id != '' && typeof id != 'undefined') {
        questionnaireFrequencey.questionnairesId = id;
      }

      questionnaireFrequencey.id = null;

      questionnaireFrequencey.frequencyDate = null;

      questionnaireFrequencey.frequencyTime = null;

      questionnaireFrequencey.frequencyTime = null;

      questionnaireFrequencey.xDaysSign = null;

      questionnaireFrequencey.timePeriodFromDays = null;
      questionnaireFrequencey.xDaysSign = true;
      questionnaireFrequencey.timePeriodToDays = null;
      questionnaireFrequencey.yDaysSign = true;

      questionnaire.questionnairesFrequenciesBo = questionnaireFrequencey;

      /* if($('#monthlyFormId').find('.numChk').val() && $('#monthlyFormId').find('.numChk').val() == 0 || !(validateTime($(document).find("#startDateMonthly").not('.cursor-none, :disabled'), $(document).find("#selectMonthlyTime").not('.cursor-none, :disabled')))){
			isFormValid = false;
		} */
      /* if(scheduletype == 'AnchorDate'){
			questionnaire.studyLifetimeStart=null;
			questionnaire.studyLifetimeEnd=null;
		} */
    }

    /* if(scheduletype == 'Regular'){
		alert('hello');
		questionnaire.anchorDateId=null;
	} */

    var data = JSON.stringify(questionnaire);
    $(item).prop('disabled', true);
    if (study_id != null && short_title != '' && short_title != null && isFormValid) {
      $("body").addClass("loading");
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/saveQuestionnaireSchedule.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
            questionnaireScheduleInfo: data,
            language: $('#studyLanguage').val(),
            isAutoSaved : $('#isAutoSaved').val()
        },
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
        },
        success: function (data) {
          var message = data.message;
          if (message === "SUCCESS") {
            $("#preShortTitleId").val(short_title);
            var questionnaireId = data.questionnaireId;
            var questionnaireFrequenceId = data.questionnaireFrequenceId;
            $("#id").val(questionnaireId);
            $("#questionnaireId").val(questionnaireId);
            var anchorList = "${anchorTypeList}";
            var length = anchorList.length;
            if (length < 3) {
              $("#schedule2").attr('disabled', true);
            } else {
              $("#schedule2").attr('disabled', false);
            }
            if (frequency_text == 'Daily') {
              var previous_frequency = $("#previousFrequency").val();
              if (previous_frequency != '' && previous_frequency != null && previous_frequency
                  != 'undefined') {
                $("#previousFrequency").val(previous_frequency);
              } else {
                $("#previousFrequency").val(frequency_text);
              }
            } else {
              $("#previousFrequency").val(frequency_text);
            }
            let studyLang=$('#studyLanguage').val();
            if (studyLang !== undefined && studyLang !== null && studyLang !== '' && studyLang !== 'en') {
              $(".add-steps-btn").addClass('cursor-none');
              $('#shortTitleId, #branchingId').attr('disabled', true);
              $('#schedule1, #schedule2, #inlineRadio1, #inlineRadio2, #inlineRadio3, #inlineRadio4, #inlineRadio5, ' +
                  '#inlineRadio6, #isLaunchStudy, #isStudyLifeTime, .xdays, .ydays, .clock, #monthsAnchor, #monthlyxdaysId, ' +
                  '#days, #startDateWeekly, #weeks, #months, .calendar, .daysMask, #weeksAnchor').attr(
                  'disabled', true);
              $('.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
                  ' .signDropDown').addClass('cursor-none');
            } else {
              $(".add-steps-btn").removeClass('cursor-none');
              $('#shortTitleId, #branchingId').attr('disabled', false);
              $('#schedule1, #schedule2, #inlineRadio1, #inlineRadio2, #inlineRadio3, #inlineRadio4, #inlineRadio5, ' +
                  '#inlineRadio6, #isLaunchStudy, #isStudyLifeTime, .xdays, .ydays, .clock, #monthsAnchor, #monthlyxdaysId, ' +
                  '#days, #startDateWeekly, #weeks, #months, .calendar, .daysMask, #weeksAnchor').attr(
                  'disabled', false);
              $('.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
                  ' .signDropDown').removeClass('cursor-none');
                if ($('#isLaunchStudy').prop('checked')) {
                    $('#chooseDate').prop('disabled', true);
                    $('#selectTime1').prop('disabled', true);
                    $('#onetimexdaysId').prop('disabled', true);
                    $('#selectTime').prop('disabled', true);
                }
                if ($('#isStudyLifeTime').prop('checked')) {
                    $('#chooseEndDate').prop('disabled', true);
                    $('#onetimeydaysId').prop('disabled', true);
                }
            }
            if (frequency_text == 'One time') {
              $("#oneTimeFreId").val(questionnaireFrequenceId);
            } else if (frequency_text == 'Weekly') {
              $("#weeklyFreId").val(questionnaireFrequenceId);
            } else if (frequency_text == 'Monthly') {
              $("#monthFreId").val(questionnaireFrequenceId);
            }
            if ($('.seventhQuestionnaires').find('span').hasClass(
                'sprites-icons-2 tick pull-right mt-xs')) {
              $('.seventhQuestionnaires').find('span').removeClass(
                  'sprites-icons-2 tick pull-right mt-xs');
            }
            $("#stepContainer").show();
            $("#content").show();
            $("#saveId").text("Save");
            if (!$('#content').DataTable().data().count()) {
              $('#helpNote').attr('data-original-title',
                  'Please ensure you add one or more Steps to this questionnaire before attempting to mark this section as Complete.');
            }
            frequencey = frequency_text;
            if (callback)
              callback(true);
              // pop message after 15 minutes
              if ($('#isAutoSaved').val() === 'true') {
                  $('#myAutoModal').modal('show');
                  let i = 1;
                  let j = 14;
                  let lastSavedInterval = setInterval(function () {
                      if ((i === 15) || (j === 0)) {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                          if ($('#myAutoModal').hasClass('show')) {
                              $('#backToLoginPage').submit();
                          }
                          clearInterval(lastSavedInterval);
                      } else {
                          if ((i === 1) || (j === 14)){
                           $('#autoSavedMessage').html('<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css("fontSize", "15px");
                          } else if ((i === 14) || (j === 1)) {
                          $('#autoSavedMessage').html('<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>')
                          }
                          else {
                           $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                          }
                          idleTime = 0;
                          i+=1;
                          j-=1;
                      }
                  }, 60000);
              }
          } else {
            $("body").removeClass("loading");
            var errMsg = data.errMsg;
            if (errMsg != '' && errMsg != null && typeof errMsg != 'undefined') {
              showErrMsg(errMsg);
            } else {
              showErrMsg("Something went Wrong");
            }
            if (callback)
              callback(false);
          }
        },
        error: function (xhr, status, error) {
          $("body").removeClass("loading");
          if (callback)
            callback(false);
        },
        complete: function () {
          $(item).prop('disabled', false);
        },
        global: false,
      });
    } else {
      $(item).prop('disabled', false);
      if (callback)
        callback(false);
    }
  }

  function checkDateRange() {
    $(document).on('dp.change change', '.cusStrDate, .cusEndDate, .cusTime', function (e) {
      var chkVal = true;
      if ($(this).parents('.manually-option').find('.cusStrDate').val() && $(this).parents(
          '.manually-option').find('.cusEndDate').val() && $(this).parents('.manually-option').find(
          '.cusTime').val()) {
        var thisAttr = this;
        $(this).parents('.manuallyContainer').find('.manually-option').each(function () {
          if ((!$(thisAttr).parents('.manually-option').is($(this))) && $(this).find(
              '.cusStrDate').val() && $(this).find('.cusEndDate').val() && $(this).find(
              '.cusTime').val()) {
            var fromDate = moment($(this).find('.cusStrDate').val(), "MM/DD/YYYY").toDate();
            var cusTime = moment($(this).find('.cusTime').val(), "HH:mm").toDate()
            fromDate.setHours(cusTime.getHours());
            fromDate.setMinutes(cusTime.getMinutes());
            var toDate = moment($(this).find('.cusEndDate').val(), "MM/DD/YYYY").toDate();
            toDate.setHours(cusTime.getHours());
            toDate.setMinutes(cusTime.getMinutes());
            var thisFromDate = moment(
                $(thisAttr).parents('.manually-option').find('.cusStrDate').val(),
                "MM/DD/YYYY").toDate();
            var thisCusTime = moment($(thisAttr).parents('.manually-option').find('.cusTime').val(),
                "HH:mm").toDate()
            thisFromDate.setHours(thisCusTime.getHours());
            thisFromDate.setMinutes(thisCusTime.getMinutes());
            var thisToDate = moment(
                $(thisAttr).parents('.manually-option').find('.cusEndDate').val(),
                "MM/DD/YYYY").toDate();
            thisToDate.setHours(thisCusTime.getHours());
            thisToDate.setMinutes(thisCusTime.getMinutes());
            if (chkVal)
              chkVal = !((thisFromDate >= fromDate && thisFromDate <= toDate) || (thisToDate
                  >= fromDate && thisToDate <= toDate));
          }
        });
      }
      if (!chkVal) {
        $(thisAttr).parents('.manually-option').find('.cusTime').parent().addClass(
            'has-error has-danger').find(".help-block").removeClass('with-errors').empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").attr("style",
                "font-size: 10px;").text(
                "Please ensure that the runs created do not have any overlapping time period."));
      } else {
        $(thisAttr).parents('.manually-option').find('.cusTime').parent().removeClass(
            'has-error has-danger').find(".help-block").addClass('with-errors').empty();
      }
      var a = 0;
      $('.manuallyContainer').find('.manually-option').each(function () {
        if ($(this).find('.cusTime').parent().find('.help-block').children().length > 0) {
          a++;
        }
      });
      isValidManuallySchedule = !(a > 0);
    });
    return isValidManuallySchedule;
  }

  function doneQuestionnaire(item, actType, callback) {
    var frequency = $('input[name="frequency"]:checked').val();
    var scheduletype = document.querySelector('input[name="scheduleType"]:checked').value;
    var valForm = false;
    var anchorForm = true;
    var onetimeForm = true;
    var anchorDateForm = true;
    $('.typeofschedule').prop('disabled', false);
    if (actType !== 'save') {
      $("#status").val(true);
      if (scheduletype == 'AnchorDate') {
        if (!isFromValid("#anchorFormId"))
          anchorForm = false;
      }
      if (frequency == 'One time') {
        $("#frequencyId").val(frequency);
        if (isFromValid("#oneTimeFormId")) {
          valForm = true;
        }
        var x = $("#onetimexdaysId").val();
        var y = $("#onetimeydaysId").val();
        if (x != null && x != '' && typeof x != 'undefined'
            && y != null && y != '' && typeof y != 'undefined') {
          onetimeForm = chkDaysValid(true);
        }
        if (scheduletype == 'AnchorDate') {
          if ($('#isLaunchStudy').is(':checked') && $('#isStudyLifeTime').is(':checked')) {
            anchorDateForm = false;
          }
        }
      } else if (frequency == 'Manually Schedule') {
        $("#customfrequencyId").val(frequency);
        if (isFromValid("#customFormId")) {
          valForm = true;
        }
      } else if (frequency == 'Daily') {
        $("#dailyFrequencyId").val(frequency);
        if (isFromValid("#dailyFormId")) {
          valForm = true;
        }
      } else if (frequency == 'Weekly') {
        $("#weeklyfrequencyId").val(frequency);
        if (isFromValid("#weeklyFormId")) {
          valForm = true;
        }
      } else if (frequency == 'Monthly') {
        $("#monthlyfrequencyId").val(frequency);
        if (isFromValid("#monthlyFormId")) {
          valForm = true;
        }
      } else if (frequency == 'Ongoing') {
        /* $("#monthlyfrequencyId").val(frequency);
	    		if(isFromValid("#monthlyFormId")){
	    			valForm = true;
	    		} */
        valForm = true;
      }
    } else {
      valForm = true;
      $("#status").val(false);
    }
    if (valForm && anchorForm && onetimeForm) {
      if (anchorDateForm) {
        saveQuestionnaire(item, function (val) {
          if (!val) {
            $('.scheduleQusClass a').tab('show');
          } else if (actType === 'save') {
            showSucMsg("Content saved as draft.");
            $("body").removeClass("loading");
          }
          callback(val);
        });
      } else {
        showErrMsg("Please choose anchor date for date/time of the launch.");
        $('.scheduleTaskClass a').tab('show');
        if (callback)
          callback(false);
      }
    } else {
      showErrMsg("Please fill all the mandatory fields.");
      $('.scheduleQusClass a').tab('show');
      if (callback)
        callback(false);
    }
  }

  function deletStep(stepId, stepType) {
    bootbox.confirm({
      message: "Are you sure you want to delete this step item? This item will no longer appear on the mobile app or admin portal. Response data already gathered against this item, if any, will still be available on the response database.",
      buttons: {
        confirm: {
          label: 'Yes',
        },
        cancel: {
          label: 'No',
        }
      },
      callback: function (result) {
        if (result) {
          var questionnaireId = $("#id").val();
          var studyId = $("#studyId").val();
          if ((stepId != null && stepId != '' && typeof stepId != 'undefined') &&
              (questionnaireId != null && questionnaireId != '' && typeof questionnaireId
                  != 'undefined')) {
            $.ajax({
              url: "/fdahpStudyDesigner/adminStudies/deleteQuestionnaireStep.do?_S=${param._S}",
              type: "POST",
              datatype: "json",
              data: {
                questionnaireId: questionnaireId,
                stepId: stepId,
                stepType: stepType,
                "${_csrf.parameterName}": "${_csrf.token}",
              },
              success: function deleteConsentInfo(data) {
                var status = data.message;
                if (status == "SUCCESS") {
                  $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                      "Questionnaire step deleted successfully");
                  $('#alertMsg').show();
                  var questionnaireSteps = data.questionnaireJsonObject;
                  var isDone = data.isDone;
                  reloadQuestionnaireStepData(questionnaireSteps, isDone);
                  if ($('.seventhQuestionnaires').find('span').hasClass(
                      'sprites-icons-2 tick pull-right mt-xs')) {
                    $('.seventhQuestionnaires').find('span').removeClass(
                        'sprites-icons-2 tick pull-right mt-xs');
                  }
                  var isAnchorQuestionnaire = data.isAnchorQuestionnaire;
                  if (isAnchorQuestionnaire) {
                    $('#anchorspanId').prop('title',
                        'This option has been disabled, since this questionnaire has 1 or more Anchor Dates defined in it.');
                    $('#anchorspanId').attr('disabled', true);
                    $('#schedule2').attr('disabled', true);
                    $('.schedule').attr('disabled', true);
                  } else {
                    $('#anchorspanId').removeAttr('data-original-title');
                    $('#anchorspanId').attr('disabled', false);
                    $('#schedule2').attr('disabled', false);
                    $('.schedule').attr('disabled', false);
                  }
                } else {
                  if (status == 'FAILUREanchorused') {
                    $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                        "Questionnaire step already live anchorbased.unable to delete");
                  } else {
                    $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                        "Unable to delete questionnaire step");
                  }
                  $('#alertMsg').show();
                }
                setTimeout(hideDisplayMessage, 4000);
              },
              error: function (xhr, status, error) {
                $("#alertMsg").removeClass('s-box').addClass('e-box').text(error);
                setTimeout(hideDisplayMessage, 4000);
              }
            });
          } else {
            bootbox.alert("Ooops..! Something went worng. Try later");
          }
        }
      }
    });
  }

  function reloadQuestionnaireStepData(questionnaire, isDone) {
    $('#content').DataTable().clear();
    if (typeof questionnaire != 'undefined' && questionnaire != null && Object.keys(
        questionnaire).length > 0) {
      $.each(questionnaire, function (key, value) {
        var datarow = [];
        if (typeof key === "undefined") {
          datarow.push(' ');
        } else {
          var dynamicTable = '';
          if (value.stepType == 'Instruction') {
            datarow.push(
                '<span id="' + parseInt(key) + '" class="round blue-round">' + parseInt(key)
                + '</span>');
          } else if (value.stepType == 'Question') {
            datarow.push(
                '<span id="' + parseInt(key) + '" class="round green-round">' + parseInt(key)
                + '</span>');
          } else {
            dynamicTable += '<span id="' + parseInt(key) + '" class="round teal-round">' + parseInt(
                key) + '</span>';
            datarow.push(dynamicTable);
          }
        }
        if (typeof value.title == "undefined") {
          datarow.push(' ');
        } else {
          var title = "";
          if (value.stepType == 'Form') {
            $.each(value.fromMap, function (key, value) {
              title += '<div class="dis-ellipsis" >' + DOMPurify.sanitize(value.title)
                  + '</div><br/>';
            });
          } else {
            title += '<div class="dis-ellipsis" >' + DOMPurify.sanitize(value.title) + '</div>';
          }
          datarow.push(title);
        }
        if ($("#branchingId").is(':checked')) {
          datarow.push(
              '<div class="destinationStep questionnaireStepClass" style="display: block;">'
              + DOMPurify.sanitize(value.destinationText) + '</div>');
        } else {
          datarow.push('<div class="destinationStep questionnaireStepClass" style="display: none;">'
              + DOMPurify.sanitize(value.destinationText) + '</div>');
        }
        var dynamicAction = '<div>' +
            '<div class="text-right pos-relative">';
        if (value.stepType != 'Instruction') {
          if (value.responseTypeText == 'Double' && (value.lineChart == 'Yes' || value.statData
              == 'Yes')) {
            dynamicAction += '<span class="sprites_v3 status-blue mr-md"></span>';
          } else if (value.responseTypeText == 'Double' && (value.lineChart == 'No'
              && value.statData == 'No')) {
            dynamicAction += '<span class="sprites_v3 status-gray mr-md"></span>';
          } else if (value.responseTypeText == 'Date' && value.useAnchorDate) {
            dynamicAction += '<span class="sprites_v3 calender-blue mr-md"></span>';
          } else if (value.responseTypeText == 'Date' && !value.useAnchorDate) {
            dynamicAction += '<span class="sprites_v3 calender-gray mr-md"></span>';
          }
        }
        dynamicAction += '<span class="ellipse" onmouseenter="ellipseHover(this);"></span>' +
            '<div class="ellipse-hover-icon" onmouseleave="ellipseUnHover(this);">' +
            '  <span class="sprites_icon preview-g mr-sm" onclick="viewStep(' + parseInt(
                value.stepId) + ',&#34;' + DOMPurify.sanitize(value.stepType) + '&#34;)"></span>';
        if (value.status) {
          dynamicAction += '<span class="sprites_icon edit-inc editIcon mr-sm" onclick="editStep(' + parseInt(
              value.stepId) + ',&#34;' + DOMPurify.sanitize(value.stepType) + '&#34;)"></span>';
        } else {
          dynamicAction += '<span class="edit-inc-draft editIcon mr-md mr-sm" onclick="editStep(' + parseInt(
              value.stepId) + ',&#34;' + DOMPurify.sanitize(value.stepType) + '&#34;)"></span>';
        }
        dynamicAction += '  <span class="sprites_icon delete deleteStepButton" onclick="deletStep('
            + parseInt(value.stepId) + ',&#34;' + DOMPurify.sanitize(value.stepType)
            + '&#34;)"></span>' +
            '</div>' +
            '</div>';

        if (value.stepType == 'Form') {
          if (Object.keys(value.fromMap).length > 0) {
            for (var j = 0; j < Object.keys(value.fromMap).length - 1; j++) {
              dynamicAction += '<div>&nbsp;</div>';
            }
          }
        }
        dynamicAction += '</div>';
        datarow.push(dynamicAction);
        $('#content').DataTable().row.add(datarow);
      });

      if (isDone != null && isDone) {
        $("#doneId").attr("disabled", false);
        $('#helpNote').attr('data-original-title', '');
      } else {
        $("#doneId").attr("disabled", true);
        $('#helpNote').attr('data-original-title',
            'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.');
      }
      $('#content').DataTable().draw();
    } else {
      $('#content').DataTable().draw();
      $("#doneId").attr("disabled", true);
      $('#helpNote').attr('data-original-title',
          'Please ensure you add one or more Steps to this questionnaire before attempting to mark this section as Complete.');
    }
  }

  function ellipseHover(item) {
    $(item).hide();
    $(item).next().show();
  }

  function ellipseUnHover(item) {
    $(item).hide();
    $(item).prev().show();
  }

  function getQuestionnaireStep(stepType) {
    $("#actionTypeForQuestionPage").val('add');
    if (stepType == 'Instruction') {
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/instructionsStep.do?_S=${param._S}";
      document.contentFormId.submit();
    } else if (stepType == 'Form') {
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/formStep.do?_S=${param._S}";
      document.contentFormId.submit();
    } else if (stepType == 'Question') {
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/questionStep.do?_S=${param._S}";
      document.contentFormId.submit();
    }
  }

  function editStep(stepId, stepType) {
    $("#actionTypeForQuestionPage").val('edit');
    if (stepType == 'Instruction') {
      $("#instructionId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/instructionsStep.do?_S=${param._S}";
      document.contentFormId.submit();
    } else if (stepType == 'Form') {
      $("#formId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/formStep.do?_S=${param._S}";
      document.contentFormId.submit();
    } else if (stepType == 'Question') {
      $("#questionId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/questionStep.do?_S=${param._S}";
      document.contentFormId.submit();
    }
  }

  function viewStep(stepId, stepType) {
    $("#actionTypeForQuestionPage").val('view');
    let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
    if (stepType == 'Instruction') {
      $("#instructionId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/instructionsStep.do?_S=${param._S}&language="
          + lang;
      document.contentFormId.submit();
    } else if (stepType == 'Form') {
      $("#formId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/formStep.do?_S=${param._S}&language="
          + lang;
      document.contentFormId.submit();
    } else if (stepType == 'Question') {
      $("#questionId").val(stepId);
      document.contentFormId.action = "/fdahpStudyDesigner/adminStudies/questionStep.do?_S=${param._S}&language="
          + lang;
      document.contentFormId.submit();
    }
  }

  function goToBackPage(item) {
    $(item).prop('disabled', true);
    <c:if test="${actionType ne 'view'}">
    bootbox.confirm({
      closeButton: false,
      message: 'You are about to leave the page and any unsaved changes will be lost. Are you sure you want to proceed?',
      buttons: {
        'cancel': {
          label: 'Cancel',
        },
        'confirm': {
          label: 'OK',
        },
      },
      callback: function (result) {
        if (result) {
          var a = document.createElement('a');
          let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
          a.href = "/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do?_S=${param._S}&language="
              + lang;
          document.body.appendChild(a).click();
        } else {
          $(item).prop('disabled', false);
        }
      }
    });
    </c:if>
    <c:if test="${actionType eq 'view'}">
    var a = document.createElement('a');
    let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
    a.href = "/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do?_S=${param._S}&language="
        + lang;
    document.body.appendChild(a).click();
    </c:if>
  }

  function disablePastTime(timeId, dateId) {
    $(document).on('click change dp.change', timeId + ', ' + dateId, function () {
      var dt = $(dateId).val();
      var date = new Date();
      var day = date.getDate() >= 10 ? date.getDate() : ('0' + date.getDate());
      var month = (date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : ('0' + (date.getMonth()
          + 1));
      var today = moment(serverDate()).format("MM/DD/YYYY");
      if (dt) {
        if (dt != today) {
          $(timeId).data("DateTimePicker").minDate(false);
        } else {
          $(timeId).data("DateTimePicker").minDate(serverDateTime());
        }
        if ($(timeId).val() && dt == today && moment($(timeId).val(), 'h:mm a').toDate()
            < serverDateTime()) {
          $(timeId).val('');
        }
      } else {
        $(timeId).data("DateTimePicker").minDate(false);
      }
    });
  }

  function validateShortTitle(item, callback) {
    var shortTitle = $("#shortTitleId").val();
    var studyId = $("#studyId").val();
    var thisAttr = $("#shortTitleId");
    var existedKey = $("#preShortTitleId").val();
    var customStudyId = $("#customStudyId").val();
    if (shortTitle != null && shortTitle != '' && typeof shortTitle != 'undefined') {
      $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
      $(thisAttr).parent().find(".help-block").empty();
      if (existedKey != shortTitle) {
        $.ajax({
          url: "/fdahpStudyDesigner/adminStudies/validateQuestionnaireKey.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            shortTitle: shortTitle,
            studyId: studyId,
            customStudyId: customStudyId
          },
          beforeSend: function (xhr, settings) {
            xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
          },
          success: function getResponse(data) {
            var message = data.message;

            if ('SUCCESS' != message) {
              $(thisAttr).validator('validate');
              $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
              $(thisAttr).parent().find(".help-block").empty();
              callback(true);
            } else {
              $(thisAttr).val('');
              $(thisAttr).parent().addClass("has-danger").addClass("has-error");
              $(thisAttr).parent().find(".help-block").empty();
              $(thisAttr).parent().find(".help-block").append(
                  $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                      shortTitle
                      + " has already been used in the past."));
              callback(false);
            }
          },
          global: false
        });
      } else {
        callback(true);
        $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
        $(thisAttr).parent().find(".help-block").empty();
      }
    } else {
      callback(false);
    }
  }

  function validateLinceChartSchedule(questionnaireId, frequency, callback) {
    var questionnaireId = $("#id").val();
    var frequencyTxt = $('input[name="frequency"]:checked').val();
    if (frequencyTxt == "Daily") {
      var length = $(".time-opts").length;
      if (length == "1") {
        frequencyTxt = "Within a day";
      }
    }

    if ((questionnaireId != null && questionnaireId != '' && typeof questionnaireId != 'undefined')
        &&
        (frequencyTxt != null && frequencyTxt != '' && typeof frequencyTxt != 'undefined')) {
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/validateLineChartSchedule.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          questionnaireId: questionnaireId,
          frequency: frequencyTxt
        },
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
        },
        success: function getResponse(data) {
          var message = data.message;
          if ('SUCCESS' != message) {
            callback(true);
          } else {
            callback(false);
            var questionnaireSteps = data.questionnaireJsonObject;
            if (typeof questionnaireSteps != 'undefined' && questionnaireSteps != null
                && questionnaireSteps != '') {
              reloadQuestionnaireStepData(questionnaireSteps, null);
            }
          }
        },
        global: false
      });
    } else {
      callback(true);
    }
  }

  function validateTime(dateRef, timeRef) {
    var valid = true;

    var tm = $('#timepicker1').val();
    var dt;

    dateRef.each(function () {
      dt = dateRef.val();
      if (dt) {
        dt = moment(dt, "MM/DD/YYYY").toDate();
        if (dt < serverDate()) {
          $(this).data("DateTimePicker").clear();
          $(this).parent().addClass('has-error has-danger');

        } else {

        }
        timeRef.each(function () {
          if ($(this).val()) {
            thisDate = moment($(this).val(), "h:mm a").toDate();
            dt.setHours(thisDate.getHours());
            dt.setMinutes(thisDate.getMinutes());
            if (dt < serverDateTime()) {
              $(this).data("DateTimePicker").clear();
              $(this).parent().addClass('has-error has-danger');
              if (valid)
                valid = false;
            } else {
            }
          }
        });
      }
    });

    return valid;
  }

  function chkDaysValid(clickDone) {
    var x = $("#onetimexdaysId").val();
    var y = $("#onetimeydaysId").val();
    var xSign = $('#onetimeXSign').val();
    var ySign = $('#onetimeYSign').val();
    if (xSign === '0') {
      x = "+" + x;
    } else if (xSign === '1') {
      x = "-" + x;
    }
    if (ySign === '0') {
      y = "+" + y;
    } else if (ySign === '1') {
      y = "-" + y;
    }
    var valid = true;
    if (y && x) {
      if (parseInt(x) > parseInt(y)) {
        if (clickDone && isFromValid($('#onetimeydaysId').parents('form')))
          $('#onetimeydaysId').focus();
        $('#onetimeydaysId').parent().addClass('has-error has-danger').find(
            ".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "Y days should be greater than X days."));
        valid = false;
      } else {
        $('#onetimeydaysId').parent().removeClass('has-error has-danger').find(
            ".help-block").empty();
        resetValidation($('#onetimeydaysId').parents('form'));
      }
    }
    return valid;
  }

  function addDateAnchor() {
    //customAnchorCount = parseInt(customAnchorCount) +1;
    customAnchorCount = parseInt($('.manually-anchor-option').length);
    var newDateCon = "<div class='manually-anchor-option mb-md form-group' id='" + customAnchorCount
        + "'>"
        + "<span class='mb-sm pr-md'><span class='light-txt opacity06'> Anchor Date </span></span>"
        + "<span class='mr-xs'><select class='signDropDown selectpicker sign-box selectXSign' count='"
        + customAnchorCount + "' title='Select' name='questionnaireCustomScheduleBo["
        + customAnchorCount + "].xDaysSign' id='xSign" + customAnchorCount + "'>"
        + "<option value='0' selected>+</option><option value='1'>-</option>"
        + "</select></span>"
        + "<span class='form-group m-none dis-inline vertical-align-middle'>"
        + "<input id='xdays" + customAnchorCount
        + "' type='text' class='form-control wid70 disRadBtn1 disBtn1 remReqOnSave xdays daysMask resetAncDate'"
        + "count='" + customAnchorCount + "' placeholder='X' name='questionnaireCustomScheduleBo["
        + customAnchorCount + "].timePeriodFromDays'"
        + "maxlength='3' required pattern='[0-9]+' data-pattern-error='Please enter valid number.'/><span class='help-block with-errors red-txt'></span>"
        + "</span>"
        + "<span class='mb-sm pr-md'><span class='light-txt opacity06'> days <span style='padding-right:5px;padding-left:5px'>to </span>  Anchor Date </span></span>"
        + "<span class='mr-xs'><select class='signDropDown selectpicker sign-box selectYSign' count='"
        + customAnchorCount + "' title='Select' name='questionnaireCustomScheduleBo["
        + customAnchorCount + "].yDaysSign' id='ySign" + customAnchorCount + "'>"
        + "<option value='0' selected>+</option><option value='1'>-</option>"
        + "</select></span>"
        + "<span class='form-group m-none dis-inline vertical-align-middle'>"
        + "<input id='ydays" + customAnchorCount
        + "' type='text' class='form-control wid70 disRadBtn1 disBtn1 remReqOnSave ydays daysMask resetAncDate' count='"
        + customAnchorCount + "' placeholder='Y'"
        + "name='questionnaireCustomScheduleBo[" + customAnchorCount
        + "].timePeriodToDays' maxlength='3' required pattern='[0-9]+' data-pattern-error='Please enter valid number.'/><span class='help-block with-errors red-txt'></span>"
        + "</span>"
        + "<span class='mb-sm pr-md'><span class='light-txt opacity06'> days </span></span>"
        + "<span class='form-group  dis-inline vertical-align-middle pr-md' style='margin-bottom: 0px'>"
        + "<input id='manualTime" + customAnchorCount + "' type='text' count='" + customAnchorCount
        + "' class='form-control clock' name='questionnaireCustomScheduleBo[" + customAnchorCount
        + "].frequencyTime' placeholder='Time' required/>"
        + "<span class='help-block with-errors red-txt'></span>"
        + "</span>"
        + "<span id='addbtn" + customAnchorCount
        + "' class='addbtn addBtnDis addbtnZero align-span-center mr-md' onclick='addDateAnchor();'>+</span>"
        + "<span id='deleteAncchor" + customAnchorCount
        + "' class='sprites_icon delete vertical-align-middle remBtnDis hide align-span-center' onclick='removeDateAnchor(this);'></span>"
        + "</div>";

    $(".manually-anchor-option:last").after(newDateCon);
    $(".manually-anchor-option").parents("form").validator("destroy");
    $(".manually-anchor-option").parents("form").validator();
    if ($('.manually-anchor-option').length > 1) {
      $('.manuallyAnchorContainer').find(".remBtnDis").removeClass("hide");
      if ($('#anchorDateId').find('option:selected').text() == 'Enrollment Date') {
        setAnchorDropdown('Manually Schedule', 'Enrollment Date');
      }
    } else {
      $('.manuallyAnchorContainer').find(".remBtnDis").addClass("hide");
    }

    if ($('.manually-anchor-option').length == 1) {
      $('.manually-anchor-option').find('.delete').addClass('display__none');
    } else {
      $('.delete').removeClass('display__none');
    }
    timep('manualTime' + customAnchorCount);
    //$('#manualTime'+customAnchorCount).val("");
    $('#' + customAnchorCount).find('input:first').focus();
    $('.selectpicker').selectpicker('refresh');
  }

  function removeDateAnchor(param) {
    $(param).parents(".manually-anchor-option").remove();
    $(".manually-anchor-option").parents("form").validator("destroy");
    $(".manually-anchor-option").parents("form").validator();
    if ($('.manually-anchor-option').length > 1) {
      $('.manuallyAnchorContainer').find(".remBtnDis").removeClass("hide");
    } else {
      $('.manuallyAnchorContainer').find(".remBtnDis").addClass("hide");
    }

    if ($('.manually-anchor-option').length == 1) {
      $('.manually-anchor-option').find('.delete').addClass('display__none');
    } else {
      $('.delete').removeClass('display__none');
    }
    //$(document).find('.cusTime').trigger('dp.change');
  }

  function setAnchorDropdown(frequency_text, anchorType) {
    if (anchorType == 'Enrollment Date') {
      if (frequency_text == 'One time') {
        $('#onetimeXSign').children('option').remove();
        $('#onetimeXSign').append("<option value='0' selected>+</option>");
        $('#onetimeYSign').children('option').remove();
        $('#onetimeYSign').append("<option value='0' selected>+</option>");
      }
      if (frequency_text == 'Manually Schedule') {
        $('.manually-anchor-option').each(function () {
          var id = $(this).attr("id");
          $("#xSign" + id).children('option').remove();
          $("#ySign" + id).children('option').remove();
          $("#xSign" + id).append("<option value='0' selected>+</option>");
          $("#ySign" + id).append("<option value='0' selected>+</option>");
        });
      }
      if (frequency_text == 'Daily') {
        $('#dailyXSign').children('option').remove();
        $('#dailyXSign').append("<option value='0' selected>+</option>");
      }
      if (frequency_text == 'Weekly') {
        $('#weeklyXSign').children('option').remove();
        $('#weeklyXSign').append("<option value='0' selected>+</option>");
      }
      if (frequency_text == 'Monthly') {
        $('#monthlyXSign').children('option').remove();
        $('#monthlyXSign').append("<option value='0' selected>+</option>");
      }
      $('.selectpicker').selectpicker('refresh');
    } else {
      if (frequency_text == 'One time') {
        $('#onetimeXSign').children('option').remove();
        $('#onetimeXSign').append(
            "<option value='0' selected>+</option><option value='1'>-</option>");
        $('#onetimeYSign').children('option').remove();
        $('#onetimeYSign').append(
            "<option value='0' selected>+</option><option value='1'>-</option>");
      }
      if (frequency_text == 'Manually Schedule') {
        $('.manually-anchor-option').each(function () {
          var id = $(this).attr("id");
          $("#xSign" + id).children('option').remove();
          $("#ySign" + id).children('option').remove();
          $("#xSign" + id).append(
              "<option value='0' selected>+</option><option value='1'>-</option>");
          $("#ySign" + id).append(
              "<option value='0' selected>+</option><option value='1'>-</option>");
        });
      }
      if (frequency_text == 'Daily') {
        $('#dailyXSign').children('option').remove();
        $('#dailyXSign').append(
            "<option value='0' selected>+</option><option value='1'>-</option>");
      }
      if (frequency_text == 'Weekly') {
        $('#weeklyXSign').children('option').remove();
        $('#weeklyXSign').append(
            "<option value='0' selected>+</option><option value='1'>-</option>");
      }
      if (frequency_text == 'Monthly') {
        $('#monthlyXSign').children('option').remove();
        $('#monthlyXSign').append(
            "<option value='0' selected>+</option><option value='1'>-</option>");
      }
      $('.selectpicker').selectpicker('refresh');
    }
  }

  $(document).ready(function () {

    jQuery(document).on("keyup", ".xdays", function () {

      var xday = $(this).val()
      var parent_id = $(this).parent().parent().attr("id");
      var xsign = $("#xSign" + parent_id).val() === "0" ? "+" : "-";
      var xdayValue = parseInt(xsign + "" + xday);
      var yday = $("#ydays" + parent_id).val();
      var ysign = $("#ySign" + parent_id).val() === "0" ? "+" : "-";
      var ydayValue = parseInt(ysign + "" + yday);

      //var siblings_length = $(".manuallyAnchorContainer > div").length;
      //
      //for(i= parseInt(parent_id)+1  ; i<= siblings_length; i++){
//		    $("#"+i).remove();
      //}

      //$("#"+parent_id).next().remove();

      if (parent_id === "0") {

        if (ydayValue !== "") {
          if (xdayValue > ydayValue) {
            $(this).addClass("red-border");
            $("#ydays" + parent_id).addClass("red-border");
            $("#ydays" + parent_id).parent().addClass('has-error has-danger').find(
                ".help-block").empty().append(
                $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                    "Y days should be greater than X days."));
            $("#addbtn" + parent_id).addClass("not-allowed");
          } else {
            $(this).removeClass("red-border");
            $("#ydays" + parent_id).removeClass("red-border");
            $("#ydays" + parent_id).parent().removeClass('has-error has-danger').find(
                ".help-block").empty();
            $("#addbtn" + parent_id).removeClass("not-allowed");
          }
        }

      } else {

        var pre_parent = $("#" + parent_id).prev().attr("id");
        var pyday = $("#ydays" + pre_parent).val();
        var pysign = $("#ySign" + parent_id).val() === "0" ? "+" : "-";
        var pydayValue = parseInt(pysign + "" + pyday);

        if (xdayValue < pydayValue) {
          $(this).addClass("red-border");
          $("#ydays" + pre_parent).addClass("red-border");
          $(this).parent().addClass('has-error has-danger').find(".help-block").empty().append(
              $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                  "Child X days should be greater than parent Y days."));
          $("#addbtn" + parent_id).addClass("not-allowed");
        } else {
          $(this).removeClass("red-border");
          $("#ydays" + pre_parent).removeClass("red-border");
          $(this).parent().removeClass('has-error has-danger').find(".help-block").empty();
          $("#addbtn" + parent_id).addClass("not-allowed");
          if (ydayValue !== "") {
            if (xdayValue > ydayValue) {
              $(this).addClass("red-border");
              $("#ydays" + parent_id).addClass("red-border");
              $("#ydays" + parent_id).parent().addClass('has-error has-danger').find(
                  ".help-block").empty().append(
                  $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                      "Y days should be greater than X days."));
              $("#addbtn" + parent_id).addClass("not-allowed");
            } else {
              $(this).removeClass("red-border");
              $("#ydays" + parent_id).removeClass("red-border");
              $("#ydays" + parent_id).parent().removeClass('has-error has-danger').find(
                  ".help-block").empty();
              $("#addbtn" + parent_id).removeClass("not-allowed");
            }
          }
        }

        /* if(xdayValue < pydayValue){
			 $("xdays"+parent_id).addClass("red-border");
		     $("#ydays"+pre_parent).addClass("red-border");
		  }else if(ydayValue !== ""){
		      if(xdayValue > ydayValue){
		    	  $(this).addClass("red-border");
				  $("#ydays"+parent_id).addClass("red-border");
				  $("#addbtn"+parent_id).addClass("not-allowed");
		       }else{
		    	   $(this).removeClass("red-border");
				   $("#ydays"+parent_id).removeClass("red-border");
				   $("#addbtn"+parent_id).removeClass("not-allowed");
		       }
	     } */

      }

    });

    jQuery(document).on("change", ".xdays", function () {
      $(this).parent().parent().siblings().removeClass("current");
      $(this).parent().parent().addClass("current");

      $(".current").nextAll().remove();
    });

    jQuery(document).on("keyup", ".ydays", function () {

      var parent_id = $(this).parent().parent().attr("id");
      var xsign = $("#xSign" + parent_id).val() === "0" ? "+" : "-";
      var xday = $("#xdays" + parent_id).val();
      var xdayValue = parseInt(xsign + "" + xday);
      var yday = $("#ydays" + parent_id).val();
      var ysign = $("#ySign" + parent_id).val() === "0" ? "+" : "-";
      var ydayValue = parseInt(ysign + "" + yday);

      if (ydayValue < xdayValue) {
        $(this).addClass("red-border");
        $("#xdays" + parent_id).addClass("red-border");
        $("#ydays" + parent_id).parent().addClass('has-error has-danger').find(
            ".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "Y days should be greater than X days."));
        $(this).parent().parent().siblings().removeClass("current");
        $(this).parent().parent().addClass("current");
        $(".current").nextAll().remove();
        $("#addbtn" + parent_id).addClass("not-allowed");
      } else {
        $(this).removeClass("red-border");
        $("#xdays" + parent_id).removeClass("red-border");
        $("#ydays" + parent_id).parent().removeClass('has-error has-danger').find(
            ".help-block").empty();
        $("#addbtn" + parent_id).removeClass("not-allowed");
      }

    });

    jQuery(document).on("change", ".ydays", function () {
      $(this).parent().parent().siblings().removeClass("current");
      $(this).parent().parent().addClass("current");
      $(".current").nextAll().remove();

    });

    jQuery(document).on("change", ".sign-box select", function () {
      var parent_id = $(this).attr("count");
      var signValue = $("#xSign" + parent_id).val();

      var xsign = signValue === "0" ? "+" : "-";
      var xday = $("#xdays" + parent_id).val();
      var xdayValue = parseInt(xsign + "" + xday);

      var yday = $("#ydays" + parent_id).val();
      var ysign = $("#ySign" + parent_id).val() === "0" ? "+" : "-";
      var ydayValue = parseInt(ysign + "" + yday);

      if (ydayValue < xdayValue) {
        $("#xdays" + parent_id).addClass("red-border");
        $("#ydays" + parent_id).addClass("red-border");
        $("#ydays" + parent_id).parent().addClass('has-error has-danger').find(
            ".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "Y days should be greater than X days."));
        $("#addbtn" + parent_id).addClass("not-allowed");
      } else {
        $("#xdays" + parent_id).removeClass("red-border");
        $("#ydays" + parent_id).removeClass("red-border");
        $("#ydays" + parent_id).parent().removeClass('has-error has-danger').find(
            ".help-block").empty();
        $("#addbtn" + parent_id).removeClass("not-allowed");
      }

      if ($('.manually-anchor-option').length > 1) {
        var pre_parent = $("#" + parent_id).prev().attr("id");
        var pyday = $("#ydays" + pre_parent).val();
        var pysign = $("#ySign" + parent_id).val() === "0" ? "+" : "-";
        var pydayValue = parseInt(pysign + "" + pyday);

        if (xdayValue < pydayValue) {
          $(this).addClass("red-border");
          $("#ydays" + pre_parent).addClass("red-border");
          $("#xdays" + parent_id).parent().addClass('has-error has-danger').find(
              ".help-block").empty().append(
              $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                  "Child X days should be greater than parent Y days."));
          $("#addbtn" + parent_id).addClass("not-allowed");
        } else {
          $(this).removeClass("red-border");
          $("#ydays" + pre_parent).removeClass("red-border");
          $("#xdays" + parent_id).parent().removeClass('has-error has-danger').find(
              ".help-block").empty();
          $("#addbtn" + parent_id).addClass("not-allowed");
        }
      }

      $(this).parent().parent().parent().siblings().removeClass("current");
      $(this).parent().parent().parent().addClass("current");

      /* $("#"+parent_id).siblings().removeClass("current");
			$("#"+parent_id).parent().parent().addClass("current"); */

      $(".current").nextAll().remove();

      /* var siblings_length = $(".manuallyAnchorContainer > div").length;
			for(i= 0; i<= siblings_length; i++){

				if(i !== parseInt(parent_id)){
					$("#"+i).remove();
				}

		    } */

    });

  });

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
        questionnaireId : $('#questionnaireId').val()
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          $('td.sorting_1').addClass('sorting_disabled');
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          // $('#shortTitleId, #branchingId').attr('disabled', true);
          $('#shortTitleId, #branchingId, #schedule1, #schedule2, #inlineRadio1, #inlineRadio2, #inlineRadio3, #inlineRadio4, #inlineRadio5, ' +
              '#inlineRadio6, #isLaunchStudy, #isStudyLifeTime, .xdays, .ydays, .clock, #monthsAnchor, #monthlyxdaysId, ' +
              '#days, #startDateWeekly, #weeks, #months, .calendar, .daysMask, #weeksAnchor, ' +
              '.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
              ' .signDropDown').addClass('ml-disabled').attr('disabled', true);
          // $('.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
          //     ' .signDropDown').addClass('cursor-none');
          $('#titleId').val($('#mlTitle', htmlData).val());
          let mark=true;
          $('tbody tr', htmlData).each(function (index, value) {
            let id = value.getAttribute('id').split('_')[1];
            let type = value.getAttribute('type');
            if (type==='Instruction') {
              $('#instructionsLangBOList option', htmlData).each(function (index, langEle) {
                let langId = langEle.getAttribute('id');
                if (id===langId) {
                  $('#row_'+id).find('td.title').text(langEle.getAttribute('value'));
                  if (langEle.getAttribute('status')==="true") {
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc')) {
                      edit.addClass('edit-inc');
                    }
                    if (edit.hasClass('edit-inc-draft')) {
                      edit.removeClass('edit-inc-draft');
                    }
                  }
                  else {
                    mark=false;
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc-draft')) {
                      edit.addClass('edit-inc-draft');
                    }
                    if (edit.hasClass('edit-inc')) {
                      edit.removeClass('edit-inc');
                    }
                  }
                  return false;
                }
              })
            } else if (type==='Question') {
              $('#questionLangBOList option', htmlData).each(function (index, langEle) {
                let langId = langEle.getAttribute('id');
                if (id===langId) {
                  $('#row_'+id).find('td.title').text(langEle.getAttribute('value'));
                  if (langEle.getAttribute('status')==="true") {
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc')) {
                      edit.addClass('edit-inc');
                    }
                    if (edit.hasClass('edit-inc-draft')) {
                      edit.removeClass('edit-inc-draft');
                    }
                  }
                  else {
                    mark=false;
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc-draft')) {
                      edit.addClass('edit-inc-draft');
                    }
                    if (edit.hasClass('edit-inc')) {
                      edit.removeClass('edit-inc');
                    }
                  }
                  return false;
                }
              })
            } else if (type==='Form') {
              $('#formLangList option', htmlData).each(function (index, langEle) {
                let formId = langEle.getAttribute('id');
                if (id===formId) {
                  if (langEle.getAttribute('status')==="true") {
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc')) {
                      edit.addClass('edit-inc');
                    }
                    if (edit.hasClass('edit-inc-draft')) {
                      edit.removeClass('edit-inc-draft');
                    }
                  }
                  else {
                    mark=false;
                    let edit = $('#row_'+id).find('span.editIcon');
                    if (!edit.hasClass('edit-inc-draft')) {
                      edit.addClass('edit-inc-draft');
                    }
                    if (edit.hasClass('edit-inc')) {
                      edit.removeClass('edit-inc');
                    }
                  }
                  $('#row_'+id).find('td.title div.dis-ellipsis').each(function (index, value){
                    let divId = value.getAttribute('id').split('_')[1];
                    $('#questionLangBOList option', htmlData).each(function (index, ele) {
                      let langId = ele.getAttribute('id');
                      if (langId===divId) {
                        $('#row_'+id).find('td.title div#div_'+divId).text(ele.getAttribute('value'));
                        return false;
                      }
                    })
                  });
                  return false;
                }
              })
            }
          });
          if (!mark) {
            $('#doneId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#doneId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
        } else {
          $('td.sorting_1').removeClass('sorting_disabled');
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          // $('#shortTitleId, #branchingId').attr('disabled', false);
          $('#shortTitleId, #branchingId, #schedule1, #schedule2, #inlineRadio1, #inlineRadio2, #inlineRadio3, #inlineRadio4, #inlineRadio5, ' +
              '#inlineRadio6, #isLaunchStudy, #isStudyLifeTime, .xdays, .ydays, .clock, #monthsAnchor, #monthlyxdaysId, ' +
              '#days, #startDateWeekly, #weeks, #months, .calendar, .daysMask, #weeksAnchor,' +
              '.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
              ' .signDropDown').removeClass('ml-disabled').attr('disabled', false);
            if ($('#isLaunchStudy').prop('checked')) {
                $('#chooseDate').prop('disabled', true);
                $('#selectTime1').prop('disabled', true);
                $('#onetimexdaysId').prop('disabled', true);
                $('#selectTime').prop('disabled', true);
            }
            if ($('#isStudyLifeTime').prop('checked')) {
                $('#chooseEndDate').prop('disabled', true);
                $('#onetimeydaysId').prop('disabled', true);
            }
          // $('.blue-bg, .green-bg, .skyblue-bg, .deleteStepButton, .addBtnDis, .delete, [data-id="anchorDateId"],' +
          //     ' .signDropDown').removeClass('cursor-none');
          $('#titleId').val($('#titleId', htmlData).val());

            <c:if test="${not empty questionnaireBo.shortTitleDuplicate && (questionnaireBo.shortTitleDuplicate gt 0)}">
            $('#shortTitleId').attr('disabled', true);
            </c:if>

            <c:if test="${empty anchorTypeList || fn:length(anchorTypeList) le 1}">
            $('#schedule1').attr('disabled', true);
            </c:if>

            <c:if test="${empty anchorTypeList || questionnaireBo.shortTitleDuplicate > 0 || isAnchorQuestionnaire}">
            $('#schedule2').attr('disabled', true);
            </c:if>

            <c:if test="${questionnaireBo.shortTitleDuplicate > 0 || isAnchorQuestionnaire}">
            $('input[name="frequency"]').attr('disabled', true);
            </c:if>

          <c:if test="${actionType == 'view'}">
          $(".blue-bg, .green-bg, .skyblue-bg").addClass('cursor-none');
          $('#branchingId').attr('disabled', true);
          $('#schedule1, #schedule2, #inlineRadio1, #inlineRadio2, #inlineRadio3, #inlineRadio4, #inlineRadio5, #inlineRadio6, #isLaunchStudy, #isStudyLifeTime').attr(
                  'disabled', true);
          </c:if>
          let mark=true;
          $('tbody tr', htmlData).each(function (index, value) {
            let id = value.getAttribute('id').split('_')[1];
            if (value.getAttribute('status')==="true") {
              let edit = $('#row_'+id).find('span.editIcon');
              if (!edit.hasClass('edit-inc')) {
                edit.addClass('edit-inc');
              }
              if (edit.hasClass('edit-inc-draft')) {
                edit.removeClass('edit-inc-draft');
              }
            }
            else {
              mark=false;
              let edit = $('#row_'+id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
            }

            let type = value.getAttribute('type');
            if (type!=='Form') {
              $('#row_'+id).find('td.title').text($('#row_'+id, htmlData).find('td.title').text());
            } else {
              $('#row_'+id).find('td.title div.dis-ellipsis').each(function (index, value){
                let divId = value.getAttribute('id').split('_')[1];
                $('#row_'+id).find('td.title div#div_'+divId).text($('#row_'+id, htmlData).find('td.title div#div_'+divId).text());
              });
            }
          });
          if (!mark) {
            $('#doneId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#doneId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
        }
      }
    });
  }
</script>