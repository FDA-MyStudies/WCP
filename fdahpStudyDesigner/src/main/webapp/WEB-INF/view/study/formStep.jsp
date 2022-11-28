<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
    <meta charset="UTF-8">
    <style>
      .cursonMove {
        cursor: move !important;
      }

      .tool-tip {
        display: inline-block;
      }

      .tool-tip [disabled] {
        pointer-events: none;
      }

      .sorting_disabled {
        pointer-events: none;
        cursor: not-allowed;
      }

      .ml-disabled {
        background-color: #eee !important;
        opacity: 1;
        cursor: not-allowed;
        pointer-events: none;
      }

      .preload-tooltip {
          margin-bottom: 3px;
      }

      .text-normal > button > .filter-option{
          text-transform: inherit !important;
      }

      .formula-box {
          height: 50px;
          border:1px solid #bfdceb;
          border-bottom:0;
          padding: 15px;
          color: #007cba;
      }

      .operator {
          width: 63% !important;
      }

      .dest-label {
          padding-left: 0 !important;
          padding-top: 7px;
      }


      .dest-row {
          margin-top: 12px;
      }

      .langSpecific {
        position: relative;
      }

      .langSpecific > button::before {
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

      .langSpecific > button {
        padding-left: 30px;
      }

      .static-width {
        width:20% !important;
      }

      #autoSavedMessage{
      width:257px;
      }

      #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
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
    </style>
</head>
<!-- Start right Content here -->
<div class="col-sm-10 col-rc white-bg p-none">
<form:form action="/fdahpStudyDesigner/sessionOut.do" id="backToLoginPage" name="backToLoginPage" method="post"></form:form>
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f dis-line pull-left line34">
				<span class="mr-xs cur-pointer" onclick="goToBackPage(this);"><img
                        src="../images/icons/back-b.png"/></span>
                <c:if test="${actionTypeForQuestionPage == 'edit'}">Edit Form Step</c:if>
                <c:if test="${actionTypeForQuestionPage == 'add'}">Add Form Step</c:if>
                <c:if test="${actionTypeForQuestionPage == 'view'}">View Form Step <c:set
                        var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}
                </c:if>
            </div>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionTypeForQuestionPage != 'add'}">
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                    <select
                            class="selectpicker aq-select aq-select-form studyLanguage langSpecific"
                            id="studyLanguage" name="studyLanguage" title="Select">
                        <option value="en" ${((currLanguage eq null) or (currLanguage eq '') or  (currLanguage eq 'undefined') or (currLanguage eq 'en')) ?'selected':''}>
                            English
                        </option>
                        <c:forEach items="${languageList}" var="language">
                            <option value="${language.key}"
                                ${currLanguage eq language.key ?'selected':''}>${language.value}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>

            <c:if test="${studyBo.multiLanguageFlag eq true and actionTypeForQuestionPage == 'add'}">
                <div class="dis-line form-group mb-none mr-sm">
                    <span style="width: 150px;" class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
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
            <c:if test="${actionTypeForQuestionPage ne 'view'}">
                <div class="dis-line form-group mb-none mr-sm">
                    <c:choose>
                        <c:when test="${not empty questionnairesStepsBo.stepId}">
                            <button type="button" id="saveBtn"
                                    class="btn btn-default gray-btn" onclick="saveFormStep(this);">
                                Save
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" id="saveBtn"
                                    class="btn btn-default gray-btn" onclick="saveFormStep(this);">
                                Next
                            </button>
                        </c:otherwise>
                    </c:choose>

                </div>
                <div class="dis-line form-group mb-none">
					<span class="tool-tip" id="helpNote" data-toggle="tooltip"
                          data-placement="bottom"
                            <c:if test="${empty questionnairesStepsBo.stepId}"> title="Please click on Next to continue." </c:if>
						<c:if test="${fn:length(questionnairesStepsBo.formQuestionMap) eq 0}">
                            title="Please ensure you add one or more questions to this Form Step before attempting this action." </c:if>
						<c:if test="${!questionnairesStepsBo.status}">
                            title="Please ensure individual list items on this page are marked Done before attempting this action." </c:if>>
						<button type="button" class="btn btn-primary blue-btn" id="doneId"
                                <c:if test="${fn:length(questionnairesStepsBo.formQuestionMap) eq 0 || !questionnairesStepsBo.status}">disabled</c:if>>Done</button>
					</span>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->
    <!--  Start body tab section -->
    <form:form
            action="/fdahpStudyDesigner/adminStudies/saveOrUpdateFromStepQuestionnaire.do?_S=${param._S}"
            name="formStepId" id="formStepId" method="post"
            data-toggle="validator" role="form">
        <div class="right-content-body pt-none pl-none pr-none">


         <ul class="nav nav-tabs customTabs gray-bg" id="formTabConstiner" role="tablist">
           <li class="nav-item stepLeve" role="presentation">
             <button class="nav-link active"  data-toggle="tab" data-target="#sla" type="button" role="tab"  aria-selected="true">Step-level Attributes</button>
           </li>
           <li class="nav-item formLevel" role="presentation">
             <button class="nav-link"  data-toggle="tab" data-target="#fla" type="button" role="tab"  aria-selected="false">Form-level Attributes </button>
           </li>

         </ul>
         <div class="tab-content pl-xlg pr-xlg">
          <!-- Step-level Attributes-->
                         <input type="hidden" name="stepId" id="stepId"
                                value="${questionnairesStepsBo.stepId}">
                         <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
             <input type="hidden" id="stepOrGroupPostLoad" value="${questionnairesStepsBo.stepOrGroupPostLoad}" name="stepOrGroupPostLoad"/>
             <input type="hidden" id="stepOrGroup" value="${questionnairesStepsBo.stepOrGroup}" name="stepOrGroup"/>
             <input type="hidden" id="lastResponseType" value="${lastResponseType}"/>
                         <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
                          <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
                         <input
                                 type="hidden" name="questionnairesId" id="questionnairesId"
                                 value="${questionnaireId}"> <input type="hidden"
                                                                    id="questionnaireShortId"
                                                                    value="${questionnaireBo.shortTitle}">
                         <input type="hidden" name="stepType" id="stepType" value="Form">
                         <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
                         <input type="hidden" id="mlText" value="${formLangBO.repeatableText}">
                         <input type="hidden" name="formId" id="formId"
                                value="${questionnairesStepsBo.instructionFormId}"> <input
                             type="hidden" name="instructionFormId" id="instructionFormId"
                             value="${questionnairesStepsBo.instructionFormId}"> <input
                             type="hidden" id="type" name="type" value="complete"/> <input
                             type="hidden" id="questionId" name="questionId"/><input
                             type="hidden" id="actionTypeForFormStep"
                             name="actionTypeForFormStep"/>
             <input type="hidden" id="seqNo" value="${questionnairesStepsBo.sequenceNo}">
                         <select id="questionLangBOList" style="display: none">
                             <c:forEach items="${questionLangBOList}" var="questionLang">
                                 <option id='${questionLang.questionLangPK.id}' status="${questionLang.status}"
                                         value="${questionLang.question}">${questionLang.question}</option>
                             </c:forEach>
                         </select>
     <div id="sla" class="tab-pane fade in show active mt-xlg">
                        <div class="row">
                            <div class="col-md-6 pl-none">
                                <div class="gray-xs-f mb-xs">
                                    Step title or Key (1 to 15 characters) <span
                                        class="requiredStar">*</span> <span
                                        class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                        title="A human readable step identifier and must be unique across all steps of the questionnaire.Note that this field cannot be edited once the study is Launched."></span>
                                </div>
                                <div class="form-group">
                                    <input autofocus="autofocus" type="text" custAttType="cust"
                                           class="form-control" name="stepShortTitle"
                                           id="stepShortTitle"
                                           value="${fn:escapeXml(questionnairesStepsBo.stepShortTitle)}"
                                           required maxlength="15"
                                            <c:if test="${not empty questionnairesStepsBo.isShorTitleDuplicate && (questionnairesStepsBo.isShorTitleDuplicate gt 0)}"> disabled</c:if> />
                                    <div class="help-block with-errors red-txt"></div>
                                    <input type="hidden" id="preShortTitleId"
                                           value="${fn:escapeXml(questionnairesStepsBo.stepShortTitle)}"/>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="gray-xs-f mb-xs">Step Type</div>
                                <div>Form Step</div>
                            </div>
                            <div class="clearfix"></div>
                            <div class="col-md-12 pl-0">
                                <div class="gray-xs-f mb-xs pt-3">
                                    Is this a Skippable Step?<span
                                        class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                                        title="If marked as Yes, it means the user can skip the entire step meaning no responses are captured from this form step. If marked No, it means the user cannot skip the step and has to answer at least one of the questions to proceed."></span>
                                </div>
                                <div>
    								<span class="radio radio-info radio-inline p-45 pl-1"> <input
                                            type="radio" id="skiappableYes" value="Yes"
                                            name="skiappable"
                                        ${empty questionnairesStepsBo.skiappable  || questionnairesStepsBo.skiappable=='Yes' ? 'checked':''}>
    									<label for="skiappableYes">Yes</label>
    								</span> <span class="radio radio-inline"> <input type="radio"
                                                                                     id="skiappableNo"
                                                                                     value="No"
                                                                                     name="skiappable"
                                    ${questionnairesStepsBo.skiappable=='No' ?'checked':''}>
    									<label for="skiappableNo">No</label>
    								</span>
                                </div>
                            </div>
                            <div class="clearfix"></div>
                            <c:if test="${questionnaireBo.branching}">
                            
                                <div class="col-md-4 col-lg-3 p-none mt-2">
                                    <div class="gray-xs-f mb-xs">
                                        Default Destination Step <span class="requiredStar">*</span>
                                        <span
                                                class="ml-xs sprites_v3 filled-tooltip"
                                                data-toggle="tooltip"
                                                title="The step that the user must be directed to from this step."></span>
                                    </div>
                                    <div class="form-group">
                                        <select class="selectpicker" name="destinationStep"
                                                id="destinationStepId"
                                                value="${questionnairesStepsBo.destinationStep}"
                                                required>
                                            <c:forEach items="${destinationStepList}"
                                                       var="destinationStep">
                                                <option value="${destinationStep.stepId}" data-type="step"
                                                    ${questionnairesStepsBo.destinationStep eq destinationStep.stepId ? 'selected' :''}>
                                                    Step
                                                        ${destinationStep.sequenceNo} :
                                                        ${destinationStep.stepShortTitle}</option>
                                            </c:forEach>
                                            <c:forEach items="${groupsListPostLoad}" var="group" varStatus="status">
                                                <option value="${group.id}" data-type="group" id="selectGroup${group.id}"
                                                ${questionnairesStepsBo.destinationStep eq group.id ? 'selected' :''}>
                                                Group :  ${group.groupName}&nbsp;</option>
                                            </c:forEach>
                                            <option value="0" data-type="step"
                                                ${questionnairesStepsBo.destinationStep eq '0' ? 'selected' :''}>
                                                Completion
                                                Step
                                            </option>
                                        </select>
                                        <div class="help-block with-errors red-txt"></div>
                                    </div>
                                </div>
                            </c:if>
                        </div>

         <div>
             <div class="gray-xs-f mb-xs">Default Visibility</div>
             <div>
                 <input type="hidden" id="defaultVisibility" name="groupDefaultVisibility" value="${questionnairesStepsBo.defaultVisibility}"/>
                 <label class="switch bg-transparent mt-xs">
                     <input type="checkbox" class="switch-input"
                            id="groupDefaultVisibility"
                            <c:if test="${groupsBo.defaultVisibility eq 'false'}">
                              <c:out value="disabled='disabled'"/>
                            </c:if>
                     <c:if test="${empty questionnairesStepsBo.defaultVisibility || questionnairesStepsBo.defaultVisibility eq 'true'}"> checked</c:if>>
                     <span class="switch-label bg-transparent" data-on="On" data-off="Off"></span>
                     <span class="switch-handle"></span>
                 </label>
             </div>
         </div>

         <div id="logicDiv">
             <div class="row">
                 <div class="gray-xs-f mb-xs">Pre-Load Logic</div>
             </div>
             <div class="row">
                 <div class="col-md-3 dest-label">
                     If True, Destination step =
                 </div>
                 <div class="col-md-1"></div>
                 <div class="col-md-3 mt__8">
                        <span class="checkbox checkbox-inline">
                                            <input type="checkbox" id="differentSurveyPreLoad" name="differentSurveyPreLoad"
                                                   <c:if test="${not empty questionnairesStepsBo.differentSurveyPreLoad
                                                        and questionnairesStepsBo.differentSurveyPreLoad}">checked</c:if> />
                                            <label for="differentSurveyPreLoad"> Is different survey? </label>
                        </span>
                 </div>
                 <div class="col-md-5"></div>
             </div>
             <div class="row">
                 <div class="col-md-4"></div>
                 <div class="col-md-5 form-group" id="contents" style="display:none">
                     <select class="selectpicker text-normal" name="preLoadSurveyId"
                             <c:if test="${not empty questionnairesStepsBo.differentSurveyPreLoad and questionnairesStepsBo.differentSurveyPreLoad}"> required </c:if>
                             id="preLoadSurveyId" title="-select survey id-">
                         <c:forEach items="${questionnaireIds}" var="key" varStatus="loop">
                             <option data-id="${key.id}" value="${key.id}" id="${key.shortTitle}"
                                     <c:if test="${key.id eq questionnairesStepsBo.preLoadSurveyId}"> selected</c:if>>
                                 Survey ${loop.index+1} : ${key.shortTitle}
                             </option>
                         </c:forEach>
                         <c:if test="${questionnaireIds eq null || questionnaireIds.size() eq 0}">
                             <option style="text-align: center; color: #000000" disabled>- No items found -</option>
                         </c:if>
                     </select>
                     <div class="help-block with-errors red-txt"></div>
                 </div>
                 <div class="col-md-3"></div>
             </div>
             <div class="row">
                 <div class="col-md-4"></div>
                 <div class="col-md-5 form-group">
                     <select name="destinationTrueAsGroup" id="destinationTrueAsGroup" 
                             data-error="Please select an option" class="selectpicker text-normal"  title="-select destination step-">
                         <c:forEach items="${sameSurveyPreloadSourceKeys}" var="destinationStep">
                             <option value="${destinationStep.stepId}" data-type="step"
                                 ${questionnairesStepsBo.destinationTrueAsGroup eq destinationStep.stepId ? 'selected' :''}>
                                 Step ${destinationStep.sequenceNo} :${destinationStep.stepShortTitle}
                             </option>
                         </c:forEach>
                         <option value="0" data-type="step"
                             ${questionnairesStepsBo.destinationTrueAsGroup eq 0 ? 'selected' :''}>
                             Completion Step</option>
                         <c:forEach items="${groupsListPreLoad}" var="group" varStatus="status">
                             <option value="${group.id}" data-type="group"
                                     <c:if test="${questionnairesStepsBo.destinationTrueAsGroup eq group.id}">
                                         selected
                                     </c:if>
                                     id="selectGroup${group.id}">Group :  ${group.groupName}&nbsp;</option>
                         </c:forEach>
<%--                         <c:if test="${(sameSurveyPreloadSourceKeys eq null || sameSurveyPreloadSourceKeys.size() eq 0) &&--%>
<%--									         (groupsList eq null || groupsList.size() eq 0) }">--%>
<%--                             <option style="text-align: center; color: #000000" disabled>- No items found -</option>--%>
<%--                         </c:if>--%>
                     </select>
                     <div class="help-block with-errors red-txt"></div>
                 </div>
                 <div class="col-md-3"></div>
         </div>
         <br>

             <div id="formulaContainer${status.index}">
                 <c:choose>
                     <c:when test="${questionnairesStepsBo.preLoadLogicBeans.size() gt 0}">
                         <c:forEach items="${questionnairesStepsBo.preLoadLogicBeans}" var="preLoadLogicBean" varStatus="status">
                             <div id="form-div${status.index}"
                                  <c:if test="${status.index gt 0}">style="height: 200px; margin-top:20px"</c:if>
                                  <c:if test="${status.index eq 0}">style="height: 150px;"</c:if>
                                  class="form-div <c:if test="${status.index gt 0}">deletable</c:if>">
                                 <c:if test="${status.index gt 0}">
                                     <div class="form-group">
												<span class="radio radio-info radio-inline p-45 pl-2">
													<input type="radio" id="andRadio${status.index}" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans[${status.index}].conditionOperator"
                                                           <c:if test="${preLoadLogicBean.conditionOperator eq '&&'}">checked </c:if> />
													<label for="andRadio${status.index}">AND</label>
												</span>
                                         <span class="radio radio-inline">
													<input type="radio" id="orRadio${status.index}" value="||" class="con-radio con-op-or" name="preLoadLogicBeans[${status.index}].conditionOperator"
                                                           <c:if test="${preLoadLogicBean.conditionOperator eq '||'}">checked </c:if> />
													<label for="orRadio${status.index}">OR</label>
												</span>
                                     </div>
                                 </c:if>
                                 <div>
                                     <div class="row formula-box">
                                         <div class="col-md-2">
                                             <strong class="font-family: arial;">Formula</strong>
                                         </div>
                                         <div class="col-md-10 text-right">
                                             <c:if test="${status.index gt 0}">
                                                 <span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" data-id="form-div${status.index}" onclick="removeFormulaContainer(this)"></span>
                                             </c:if>
                                         </div>
                                     </div>
                                     <div style="height: 100px; border:1px solid #bfdceb;">
                                         <div class="row">
                                             <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>
                                             <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">
                                                 Define Inputs
                                                 <span class="ml-xs sprites_v3 filled-tooltip preload-tooltip" data-toggle="tooltip"
                                                       title="For response including 'Height' please provide response in cm.">
                                                 </span>
                                             </div>
                                             <div class="col-md-6"></div>
                                         </div>
                                         <div class="row data-div">
                                             <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                             <div class="col-md-2 form-group">
                                                 <select class="selectpicker operator text-normal" data-error="Please select an option" required
                                                         id="operator${status.index}" name="preLoadLogicBeans[${status.index}].operator" title="-select-">
                                                     <c:forEach items="${operators}" var="operator">
                                                         <option value="${operator}" ${preLoadLogicBean.operator eq operator ?'selected':''}>${operator}</option>
                                                     </c:forEach>
                                                    </select>
                                                 <div class="help-block with-errors red-txt"></div>
                                             </div>

                                             <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>
                                             <div class="col-md-3 form-group">
                                                 <input type="hidden" value="${preLoadLogicBean.id}" class="id" name="preLoadLogicBeans[${status.index}].id" >
                                                 <input type="text" required class="form-control value" value="${preLoadLogicBean.inputValue}" id="value${status.index}" name="preLoadLogicBeans[${status.index}].inputValue" placeholder="Enter">
                                                 <div class="help-block with-errors red-txt"></div>
                                             </div>
                                         </div>
                                     </div>
                                 </div>
                                 <br>
                             </div>
                         </c:forEach>
                     </c:when>

                     <c:otherwise>
                         <div style="height: 136px" class="form-div">
                             <div class="row formula-box">
                                 <div class="col-md-2">
                                     <strong class="font-family: arial;">Formula</strong>
                                 </div>
                             </div>
                             <div style="height: 100px; border:1px solid #bfdceb;">
                                 <div class="row">
                                     <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>
                                     <div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">
                                         Define Inputs
                                         <span class="ml-xs sprites_v3 filled-tooltip preload-tooltip" data-toggle="tooltip"
                                               title="For response including 'Height' please provide response in cm.">
                                         </span>
                                     </div>
                                     <div class="col-md-6"></div>
                                 </div>
                                 <div class="row data-div">
                                     <div class="col-md-1" style="padding-top: 7px">Operator</div>
                                     <div class="col-md-2 form-group">
                                         <select class="selectpicker operator text-normal"  data-error="Please select an option"
                                                 id="operator0" name="preLoadLogicBeans[0].operator" title="-select-">
                                             <c:forEach items="${operators}" var="operator">
                                                 <option value="${operator}">${operator}</option>
                                             </c:forEach>
                                         </select>
                                         <div class="help-block with-errors red-txt"></div>
                                     </div>

                                     <div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>
                                     <div class="col-md-3 form-group">
                                         <input type="hidden" id="id${status.index}">
                                         <input type="text"  class="form-control value" id="value0" name="preLoadLogicBeans[0].inputValue" placeholder="Enter">
                                         <div class="help-block with-errors red-txt"></div>
                                     </div>
                                 </div>
                             </div>
                         </div>
                         <br>
                     </c:otherwise>
                 </c:choose>
             </div>
             <button type="button" id="addFormula" style="margin-top:10px" class="btn btn-primary blue-btn">Add Formula</button>
         </div>
     </div>



<!---  Form-level Attributes --->
                <div id="fla" class="tab-pane fade mt-xlg">
                    <div>
                        <div class="gray-xs-f mb-xs">Make form repeatable?</div>
                        <div>
							<span class="radio radio-info radio-inline p-45 pl-1"> <input
                                    type="radio" id="repeatableYes" value="Yes" name="repeatable"
                                ${questionnairesStepsBo.repeatable eq'Yes' ?'checked':''}>
								<label for="repeatableYes">Yes</label>
							</span> <span class="radio radio-inline"> <input type="radio"
                                                                             id="repeatableNo"
                                                                             value="No"
                                                                             name="repeatable"
                            ${empty questionnairesStepsBo.repeatable || questionnairesStepsBo.repeatable eq 'No' ?'checked':''}>
								<label for="repeatableNo">No</label>
							</span>
                        </div>
                    </div>
                    <div>
                        <div class="gray-xs-f mb-xs mt-md">Repeatable Form Button
                            text (1 to 30 characters)
                        </div>
                        <div class="gray-xs-f mb-xs">
                            <small>Enter text the user should see and tap on, to
                                repeat the form</small>
                        </div>
                        <div class="form-group mb-none col-md-4 p-none">
                            <input type="text" class="form-control"
                                   placeholder="Eg: I have more medications to add"
                                   name="repeatableText" id="repeatableText"
                                   value="${fn:escapeXml(questionnairesStepsBo.repeatableText)}"
                                   <c:if test="${questionnairesStepsBo.repeatable ne 'Yes'}">disabled</c:if>
                                   maxlength="30"
                                   <c:if test="${questionnairesStepsBo.repeatable eq'Yes'}">required</c:if> />
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div class="row mt-lg" id="addQuestionContainer">
                        <div class="col-md-6 p-none blue-md-f mt-xs text-uppercase">
                            Questions in the Form
                        </div>
                        <div class="col-md-6 p-none">
                            <div class="dis-line form-group mb-md pull-right">
                                <button type="button"
                                        class="btn btn-primary  blue-btn hideButtonOnView <c:if test="${empty questionnairesStepsBo.stepId}"> cursor-none </c:if>"
                                        onclick="addNewQuestion('');" id="addQuestionId">Add
                                    New Question
                                </button>
                            </div>
                        </div>
                        
                        <div class="clearfix"></div>
                        <div class="col-12 mt-md mb-lg">
                     
                            <table id="content" class="display" cellspacing="0" width="100%">
                                <thead style="display: none"></thead>
                                <c:forEach items="${questionnairesStepsBo.formQuestionMap}" var="entry">
                                    <tr id="row${entry.value.questionInstructionId}" status="${entry.value.status}">
                                        <td><span id="${entry.key}">${entry.key}</span></td>
                                        <td class="title">
                                            <div>
                                                <div class="dis-ellipsis"
                                                     title="${fn:escapeXml(entry.value.title)}">${entry.value.title}</div>
                                            </div>
                                        </td>
                                        <td>
                                            <div>
                                                <div class="text-right pos-relative">
                                                    <c:choose>
                                                        <c:when
                                                                test="${entry.value.responseTypeText eq 'Double' && (entry.value.lineChart eq 'Yes' || entry.value.statData eq 'Yes')}">
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
                                                    <span class="ellipse"
                                                          onmouseenter="ellipseHover(this);"></span>
                                                    <div class="ellipse-hover-icon"
                                                         onmouseleave="ellipseUnHover(this);">
														<span class="sprites_icon preview-g mr-sm"
                                                              onclick="viewQuestion(${entry.value.questionInstructionId});"></span>
                                                        <span
                                                                class="${entry.value.status?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-sm <c:if test="${actionTypeForQuestionPage eq 'view'}"> cursor-none-without-event </c:if>"
                                                                <c:if test="${actionTypeForQuestionPage ne 'view'}">onclick="editQuestion(${entry.value.questionInstructionId});"</c:if>></span>
                                                        <span
                                                                class="sprites_icon delete <c:if test="${actionTypeForQuestionPage eq 'view'}"> cursor-none-without-event </c:if>"
                                                                <c:if test="${actionTypeForQuestionPage ne 'view'}">onclick="deletQuestion(${entry.value.stepId},${entry.value.questionInstructionId})"</c:if>></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                  </c:forEach>
                            </table>
                            
                        </div>
                      </div>
                </div>
         </div>

        </div>
    </form:form>

    <div class="modal fade" id="myModal" role="dialog">
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
<script type="text/javascript">
    var table1 = null;
    var defaultVisibility = $('#groupDefaultVisibility');
var idleTime = 0;
  $(document).ready(function () {

      <c:if test="${(operators eq null || operators.size() eq 0)}">
      defaultVisibility.prop('checked', true).trigger('change');
      defaultVisibility.prop('disabled', true);
      </c:if>

       <c:if test = "${IsSkippableFlag eq 'true' && groupsBo.defaultVisibility eq 'false'}">
           $('#skiappableNo').attr('checked', true);
           $('[name="skiappable"]').addClass('ml-disabled').attr('disabled', true);
       </c:if>

    <c:if test="${actionTypeForQuestionPage == 'view'}">
      $('#formStepId input[type="text"], input[type="checkbox"], input[type="radio"]').prop('disabled', true);
      $('#formStepId select').addClass('linkDis');
      $('#studyLanguage').removeClass('linkDis');
      $('.hideButtonOnView').addClass('dis-none');
      $('#logicDiv').find('div.bootstrap-select').each( function () {
          $(this).addClass('ml-disabled');
      });
      $('#sourceQuestion').prop('disabled', true);
      $('#differentSurvey').prop('disabled', true);
      $('#addFormula').attr('disabled', true);
      $('.selectpicker').selectpicker('refresh');
    </c:if>
    var id = "${questionnairesStepsBo.stepId}";
    if (id != '' && id != null && typeof id != 'undefined') {
      $("#addQuestionContainer").show();
    } else {
      $("#addQuestionContainer").hide();
    }

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');
    var question = "${Question}";

    if (question != null && question != '' && typeof question != 'undefined' && question == 'Yes') {
      $('.formLevel a').tab('show');
    } else {
      $('.stepLevel a').tab('show');
    }
    $("#doneId").click(function () {
      $("#doneId").attr("disabled", true);
      var table = $('#content').DataTable();
      var stepId = $("#stepId").val();
      validateShortTitle('', function (val) {
        if (val) {
          if (isFromValid("#formStepId")) {
              if (!$('#groupDefaultVisibility').is(':checked')) {
                  $('#stepOrGroup').val($('#destinationTrueAsGroup option:selected').attr('data-type'));
              }
              if ('${questionnaireBo.branching}' === 'true') {
                  $('#stepOrGroupPostLoad').val($('#destinationStepId option:selected').attr('data-type'));
              }
              $('input.con-radio').each(function(e) {
                  $(this).removeAttr('disabled');
              })
            if (stepId != null && stepId != '' && typeof stepId != 'undefined') {
              if (!table.data().count()) {
                $("#doneId").attr("disabled", false);
                $('#alertMsg').show();
                $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                    "Add atleast one question");
                setTimeout(hideDisplayMessage, 4000);
                $('.formLevel a').tab('show');
              } else {
                var repeatable = $('input[name="repeatable"]:checked').val();
                if (repeatable == "Yes") {
                  validateRepeatableQuestion('', function (valid) {
                    if (!valid) {
                      document.formStepId.submit();
                    } else {
                      $("#doneId").attr("disabled", false);
                    }
                  });
                } else {
                  document.formStepId.submit();
                }
              }
            } else {
              var repeatable = $('input[name="repeatable"]:checked').val();
              if (repeatable == "Yes") {
                validateRepeatableQuestion('', function (valid) {
                  if (!valid) {
                    saveFormStepQuestionnaire(this, function (val) {
                      if (val) {
                        if (!table.data().count()) {
                          $("#doneId").attr("disabled", false);
                          $('#alertMsg').show();
                          $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                              "Add atleast one question");
                          setTimeout(hideDisplayMessage, 4000);
                          $('.formLevel a').tab('show');
                        }
                      }
                    });
                  } else {
                    $("#doneId").attr("disabled", false);
                  }
                });
              } else {
                saveFormStepQuestionnaire(this, function (val) {
                  if (val) {
                    if (!table.data().count()) {
                      $("#doneId").attr("disabled", false);
                      $('#alertMsg').show();
                      $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                          "Add atleast one question");
                      setTimeout(hideDisplayMessage, 4000);
                      $('.formLevel a').tab('show');
                    }
                  }
                });
              }

            }

          } else {
            $("#doneId").attr("disabled", false);
            var slaCount = $('#sla').find('.has-error.has-danger').length;
            var flaCount = $('#fla').find('.has-error.has-danger').length;
            if (parseInt(slaCount) >= 1) {
              $('.stepLevel a').tab('show');
            } else if (parseInt(flaCount) >= 1) {
              $('.formLevel a').tab('show');
            }
          }
        } else {
          $("#doneId").attr("disabled", false);
          var slaCount = $('#sla').find('.has-error.has-danger').length;
          var flaCount = $('#fla').find('.has-error.has-danger').length;
          if (parseInt(slaCount) >= 1) {
            $('.stepLevel a').tab('show');
          } else if (parseInt(flaCount) >= 1) {
            $('.formLevel a').tab('show');
          }
        }
      });

   
    // if($('#differentSurveyPreLoad').is(':checked')) {
    //     $('#preLoadSurveyId').prop('required', true);
    // } else {
    //     $('#preLoadSurveyId').val('').prop('required', false).selectpicker('refresh');
    //     $("#preLoadSurveyId").parent().removeClass("has-danger").removeClass("has-error");
    //     $("#preLoadSurveyId").parent().find(".help-block").empty();
    // };


    });
    $("#stepShortTitle").blur(function () {
      validateShortTitle('', function (val) {
      });
    });
    $('input[name="repeatable"]').on('change', function () {
      var val = $(this).val();
      if (val == 'Yes') {
        var textVal = "${questionnairesStepsBo.repeatableText}";
        $("#repeatableText").attr("disabled", false);
        if (textVal != null && textVal != "" && typeof textVal != 'undefined') {
          $("#repeatableText").val(textVal);
        }
        $("#repeatableText").attr("required", true);
      } else {
        $("#repeatableText").attr("required", false);
        $("#repeatableText").attr("disabled", true);
        $("#repeatableText").val('');
        $("#repeatableText").validator('validate');
        $("#repeatableText").parent().removeClass("has-danger").removeClass("has-error");
        $("#repeatableText").parent().find(".help-block").empty();
      }
    });

      <c:if test="${questionnaireBo.branching}">
      defaultVisibility.prop('checked', true).trigger('change');
      defaultVisibility.prop('disabled', true);
      </c:if>

    var actionPage = "${actionTypeForQuestionPage}";
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
      "columnDefs": [{orderable: false, targets: [0, 1, 2]}],
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        if (actionPage != 'view') {
          $('td:eq(0)', nRow).addClass("cursonMove dd_icon");
        }
        $('td:eq(0)', nRow).addClass("qs-items static-width");
        $('td:eq(1)', nRow).addClass("qs-items");
        $('td:eq(2)', nRow).addClass("qs-items");
      }
    });
    table1.on('row-reorder', function (e, diff, edit) {
      var oldOrderNumber = '', newOrderNumber = '';
      var result = 'Reorder started on row: ' + edit.triggerRow.data()[1] + '<br>';
      var formId = $("#instructionFormId").val();
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
          url: "/fdahpStudyDesigner/adminStudies/reOrderFormQuestions.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            formId: formId,
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
              if ($('.seventhQuestionnaires').find('span').hasClass(
                  'sprites-icons-2 tick pull-right mt-xs')) {
                $('.seventhQuestionnaires').find('span').removeClass(
                    'sprites-icons-2 tick pull-right mt-xs');
              }
            } else {
              $('#alertMsg').show();
              $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                  "Unable to reorder consent");
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
      if (table1 != null && $('#groupDefaultVisibility').is(':checked')) {
          table1.rowReorder.enable();
      } else {
          table1.rowReorder.disable();
      }
    if (document.getElementById("doneId") != null && document.getElementById("doneId").disabled) {
      $('[data-toggle="tooltip"]').tooltip();
    }
    $('[data-toggle="tooltip"]').tooltip();
      setInterval(function () {
            idleTime += 1;
            if (idleTime > 3) {
                    <c:if test="${actionTypeForQuestionPage ne 'view'}">
                    autoSaveFormStep('auto');
                     </c:if>
                    <c:if test="${actionTypeForQuestionPage eq 'view'}">
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

  function saveFormStep() {
    autoSaveFormStep('manual');
  }
  function autoSaveFormStep(mode){
  $("body").addClass("loading");
      validateShortTitle('', function (val) {
        if (val) {
          if (mode === 'auto') {
              $("#isAutoSaved").val('true');
          }
          var repeatable = $('input[name="repeatable"]:checked').val();
          if (repeatable === "Yes") {
            validateRepeatableQuestion('', function (valid) {
              if (!valid) {
              if (mode === 'auto') {
              $("#isAutoSaved").val('true');
               }
                saveFormStepQuestionnaire();
              } else {
                $("body").removeClass("loading");
              }
            });
          } else {
          if (mode === 'auto') {
          $("#isAutoSaved").val('true');
           }
              saveFormStepQuestionnaire();
          }
        } else {
          $("body").removeClass("loading");
          var slaCount = $('#sla').find('.has-error.has-danger').length;
          var flaCount = $('#fla').find('.has-error.has-danger').length;
          if (parseInt(slaCount) >= 1) {
            $('.stepLevel a').tab('show');
          } else if (parseInt(flaCount) >= 1) {
            $('.formLevel a').tab('show');
          }
        }
      });
  }
  function addNewQuestion(questionId) {
    $("#questionId").val(questionId);
    $("#actionTypeForFormStep").val('add');
    document.formStepId.action = "/fdahpStudyDesigner/adminStudies/formQuestion.do?_S=${param._S}";
    document.formStepId.submit();
  }

  function viewQuestion(questionId) {
    $("#questionId").val(questionId);
    $("#actionTypeForFormStep").val('view');
    document.formStepId.action = "/fdahpStudyDesigner/adminStudies/formQuestion.do?_S=${param._S}";
    document.formStepId.submit();
  }

  function editQuestion(questionId) {
    $("#questionId").val(questionId);
    $("#actionTypeForFormStep").val('edit');
    document.formStepId.action = "/fdahpStudyDesigner/adminStudies/formQuestion.do?_S=${param._S}";
    document.formStepId.submit();
  }

  function saveFormStepQuestionnaire(item, callback) {
    var stepId = $("#stepId").val();
    var quesstionnaireId = $("#questionnairesId").val();
    var formId = $("#instructionFormId").val();
    var shortTitle = $("#stepShortTitle").val();
    var skiappable = $('input[name="skiappable"]:checked').val();
    var destionationStep = $("#destinationStepId").val();
    var repeatable = $('input[name="repeatable"]:checked').val();
    var repeatableText = $("#repeatableText").val();
    var step_type = $("#stepType").val();
    var questionnaireStep = new Object();
    questionnaireStep.stepId = stepId;
    questionnaireStep.questionnairesId = quesstionnaireId;
    questionnaireStep.instructionFormId = formId;
    questionnaireStep.stepShortTitle = shortTitle;
    questionnaireStep.skiappable = skiappable;
    questionnaireStep.destinationStep = destionationStep;
    questionnaireStep.repeatable = repeatable;
    questionnaireStep.repeatableText = repeatableText;
    questionnaireStep.type = "save";
    questionnaireStep.stepType = step_type;
      questionnaireStep.groupDefaultVisibility = $('#groupDefaultVisibility').is(':checked');
      questionnaireStep.destinationTrueAsGroup = $('#destinationTrueAsGroup').val();
      if (!$('#groupDefaultVisibility').is(':checked')) {
          questionnaireStep.stepOrGroup = $('#destinationTrueAsGroup option:selected').attr('data-type');
      }
      if ('${questionnaireBo.branching}' === 'true') {
          questionnaireStep.stepOrGroupPostLoad = $('#destinationStepId option:selected').attr('data-type');
      }
      questionnaireStep.differentSurveyPreLoad = $('#differentSurveyPreLoad').is(':checked');
      questionnaireStep.preLoadSurveyId = $('#preLoadSurveyId option:selected').attr('data-id');
      let beanArray = [];
      $('#formulaContainer').find('div.form-div').each(function (index) {
          let preLoadBean = {};
          preLoadBean.operator = $(this).find('select.operator').val();
          preLoadBean.id = $(this).find('input.id').val();
          preLoadBean.inputValue = $(this).find('input.value').val();
          if (index > 0) {
              let isOr = $(this).find('input.con-op-or');
              let coop = '&&';
              if (isOr.val() !== undefined && isOr.is(':checked')) {
                  coop = '||';
              }
              preLoadBean.conditionOperator = coop;
          }
          beanArray.push(preLoadBean);
      });
      questionnaireStep.preLoadLogicBeans = beanArray;
    if (quesstionnaireId != null && quesstionnaireId != '' && typeof quesstionnaireId != 'undefined'
        &&
        shortTitle != null && shortTitle != '' && typeof shortTitle != 'undefined') {
      var data = JSON.stringify(questionnaireStep);
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/saveFromStep.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          questionnaireStepInfo: data,
          language: $('#studyLanguage').val(),
          isAutoSaved : $('#isAutoSaved').val()
        },
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
        },
        success: function (data) {
          var message = data.message;
          if (message === "SUCCESS") {

            $("#preShortTitleId").val(shortTitle);

            var stepId = data.stepId;
            var formId = data.formId;

            $("#stepId").val(stepId);
            $("#formId").val(formId);
            if ($('.seventhQuestionnaires').find('span').hasClass(
                'sprites-icons-2 tick pull-right mt-xs')) {
              $('.seventhQuestionnaires').find('span').removeClass(
                  'sprites-icons-2 tick pull-right mt-xs');
            }
            $("#addQuestionId").removeClass("cursor-none");
            $("#alertMsg").removeClass('e-box').addClass('s-box').text("Content saved as draft.");
            $(item).prop('disabled', false);
            $('#alertMsg').show();
            if ($("#saveBtn").text() == 'Next') {
              $('.formLevel a').tab('show');
            }
            $("#addQuestionContainer").show();
            if (!$('#content').DataTable().data().count()) {
              $('#helpNote').attr('data-original-title',
                  'Please ensure you add one or more questions to this Form Step before attempting this action.');
            }
            $("#saveBtn").text("Save");
            $("body").removeClass("loading");
            if (callback)
              callback(true);
              // pop message after 15 minutes
           if (data.isAutoSaved === 'true') {
               $('#myModal').modal('show');
               let i = 1;
               let j = 14;
               let lastSavedInterval = setInterval(function () {
                   if ((i === 15) || (j === 0))  {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                       if ($('#myModal').hasClass('show')) {
                           $('#backToLoginPage').submit();
                       }
                       clearInterval(lastSavedInterval);
                   } else {
                       if ((i === 1) || (j === 14))  {
                         $('#autoSavedMessage').html('<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css("fontSize", "15px");
                       }
                       else if ((i === 14) || (j === 1)) {
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
               $("#isAutoSaved").val('false');
            }
          } else {
            var errMsg = data.errMsg;
            if (errMsg != '' && errMsg != null && typeof errMsg != 'undefined') {
              $("#alertMsg").removeClass('s-box').addClass('e-box').text(errMsg);
            } else {
              $("#alertMsg").removeClass('s-box').addClass('e-box').text("Something went Wrong");
            }
            $('#alertMsg').show();
            if (callback)
              callback(false);
          }
          setTimeout(hideDisplayMessage, 4000);
        },
        error: function (xhr, status, error) {
          $(item).prop('disabled', false);
          $('#alertMsg').show();
          $("#alertMsg").removeClass('s-box').addClass('e-box').text("Something went Wrong");
          setTimeout(hideDisplayMessage, 4000);
        }
      });
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

  function deletQuestion(formId, questionId) {
    var questionnairesId = $("#questionnairesId").val();
    bootbox.confirm({
      message: "Are you sure you want to delete this question item? This item will no longer appear on the mobile app or admin portal. Response data already gathered against this item, if any, will still be available on the response database.",
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
          if ((formId != null && formId != '' && typeof formId != 'undefined') &&
              (questionId != null && questionId != '' && typeof questionId != 'undefined')) {
            $.ajax({
              url: "/fdahpStudyDesigner/adminStudies/deleteFormQuestion.do?_S=${param._S}",
              type: "POST",
              datatype: "json",
              data: {
                formId: formId,
                questionId: questionId,
                questionnairesId: questionnairesId,
                "${_csrf.parameterName}": "${_csrf.token}",
              },
              success: function deleteConsentInfo(data) {
                var status = data.message;
                if (status === "SUCCESS") {
                  $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                      "Questionnaire step deleted successfully");
                  $('#alertMsg').show();

                  var questionnaireSteps = data.questionnaireJsonObject;
                  var isDone = data.isDone;
                  reloadQuestionsData(questionnaireSteps, isDone);
                  if ($('.seventhQuestionnaires').find('span').hasClass(
                      'sprites-icons-2 tick pull-right mt-xs')) {
                    $('.seventhQuestionnaires').find('span').removeClass(
                        'sprites-icons-2 tick pull-right mt-xs');
                  }
                  if (data.lastResponseType != null && data.lastResponseType !== '') {
                      $('#lastResponseType').val(data.lastResponseType);
                      setOperatorDropDown(data.lastResponseType);
                  }
                } else {
                  if (status == 'FAILUREanchorused') {
                    $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                        "Form Step Question already live anchorbased.unable to delete");
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

  function reloadQuestionsData(questions, isDone) {
    $('#content').DataTable().clear();
    if (typeof questions != 'undefined' && questions != null && Object.keys(questions).length > 0) {
      $.each(questions, function (key, value) {
        var datarow = [];
        if (typeof key === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push('<span id="' + key + '">' + key + '</span>');
        }
        if (typeof value.title == "undefined") {
          datarow.push(' ');
        } else {
          datarow.push('<div class="dis-ellipsis">' + DOMPurify.sanitize(value.title) + '</div>');
        }
        var dynamicAction = '<div><div class="text-right pos-relative">';
        if (value.responseTypeText == 'Double' && (value.lineChart == 'Yes' || value.statData
            == 'Yes')) {
          dynamicAction += '<span class="sprites_v3 status-blue mr-md"></span>';
        } else if (value.responseTypeText == 'Double' && (value.lineChart == 'No' && value.statData
            == 'No')) {
          dynamicAction += '<span class="sprites_v3 status-gray mr-md"></span>';
        } else if (value.responseTypeText == 'Date' && value.useAnchorDate) {
          dynamicAction += '<span class="sprites_v3 calender-blue mr-md"></span>';
        } else if (value.responseTypeText == 'Date' && !value.useAnchorDate) {
          dynamicAction += '<span class="sprites_v3 calender-gray mr-md"></span>';
        }
        dynamicAction += '<span class="ellipse" onmouseenter="ellipseHover(this);"></span>' +
            '<div class="ellipse-hover-icon" onmouseleave="ellipseUnHover(this);">' +
            '  <span class="sprites_icon preview-g mr-sm"></span>';
        if (value.status) {
          dynamicAction += '<span class="sprites_icon edit-inc editIcon mr-sm" onclick="editQuestion('
              + parseInt(value.questionInstructionId) + ');"></span>';
        } else {
          dynamicAction += '<span class="edit-inc-draft editIcon mr-md mr-sm" onclick="editQuestion('
              + parseInt(value.questionInstructionId) + ');"></span>';
        }
        dynamicAction += '<span class="sprites_icon delete" onclick="deletQuestion(' + parseInt(
                value.stepId) + ',' + parseInt(value.questionInstructionId) + ')"></span>' +
            '</div>' +
            '</div></div>';
        datarow.push(dynamicAction);
        $('#content').DataTable().row.add(datarow);
      });

      if (isDone != null && isDone) {
        $("#doneId").attr("disabled", false);
        $('#helpNote').attr('data-original-title', '');
      }
      $('#content').DataTable().draw();
    } else {
      $('#content').DataTable().draw();
      $("#doneId").attr("disabled", true);
      $('#helpNote').attr('data-original-title',
          'Please ensure you add one or more questions to this Form Step before attempting this action.');
    }
  }

  function goToBackPage(item) {
    $(item).prop('disabled', true);
    <c:if test="${actionTypeForQuestionPage ne 'view'}">
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
          a.href = "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}&language="
              + lang;
          document.body.appendChild(a).click();
        } else {
          $(item).prop('disabled', false);
        }
      }
    });
    </c:if>
    <c:if test="${actionTypeForQuestionPage eq 'view'}">
    var a = document.createElement('a');
    let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
    a.href = "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}&language="
        + lang;
    document.body.appendChild(a).click();
    </c:if>
  }

  function validateShortTitle(item, callback) {
    var shortTitle = $("#stepShortTitle").val();
    var questionnaireId = $("#questionnairesId").val();
    var stepType = "Form";
    var thisAttr = $("#stepShortTitle");
    var existedKey = $("#preShortTitleId").val();
    var questionnaireShortTitle = $("#questionnaireShortId").val();
    if (shortTitle != null && shortTitle != '' && typeof shortTitle != 'undefined') {
      $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
      $(thisAttr).parent().find(".help-block").empty();
      if (existedKey != shortTitle) {
        $.ajax({
          url: "/fdahpStudyDesigner/adminStudies/validateQuestionnaireStepKey.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            shortTitle: shortTitle,
            questionnaireId: questionnaireId,
            stepType: stepType,
            questionnaireShortTitle: questionnaireShortTitle
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

  function validateRepeatableQuestion(item, callback) {
    var formId = $("#formId").val();
    if (formId != null && formId != '' && typeof formId != 'undefined') {
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/validateRepeatableQuestion.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          formId: formId
        },
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
        },
        success: function getResponse(data) {
          var message = data.message;

          if ('SUCCESS' == message) {
            callback(true);
            showErrMsg(
                "The following properties for questions cannot be used if the form is of Repeatable type:  Anchor Date, Charts/Statistics for Dashboard.");
          } else {
            callback(false);
          }
        },
        global: false
      });
    } else {
      callback(false);
    }
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  var isBranching = ${questionnaireBo.branching};

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/formStep.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          $('td.sorting_1').addClass('sorting_disabled');
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          $('#stepShortTitle, [name="skiappable"], [name="repeatable"]').addClass("ml-disabled").attr('disabled', true);
            $('#logicDiv').find('div.bootstrap-select, input').each( function () {
                $(this).addClass('ml-disabled');
                if ($(this).is("input")) {
                    $(this).attr('disabled', true);
                }
            });
          $('#addQuestionId').attr('disabled', true);
          if ($('#repeatableYes').prop('checked') === true) {
            $('#repeatableText').val($('#mlText', htmlData).val());
          }
          if (isBranching) {
            $('[data-id="destinationStepId"]').addClass("ml-disabled");
          }
          $('.delete, #addFormula, .switch').addClass('cursor-none');
          let mark=true;
          $('#questionLangBOList option', htmlData).each(function (index, value) {
            let id = '#row' + value.getAttribute('id');
            $(id).find('td.title').text(value.getAttribute('value'));
            if (value.getAttribute('status')==="true") {
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc')) {
                edit.addClass('edit-inc');
              }
              if (edit.hasClass('edit-inc-draft')) {
                edit.removeClass('edit-inc-draft');
              }
            }
            else {
              mark=false;
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
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
          $('#stepShortTitle, [name="repeatable"]').removeClass("ml-disabled").attr('disabled', false);
          <c:choose>
            <c:when test = "${IsSkippableFlag eq 'true' && groupsBo.defaultVisibility eq 'false'}">
             $('[name="skiappable"]').addClass('ml-disabled').attr('disabled', true);
             $('#skiappableNo').attr('checked', true);
            </c:when>
            <c:otherwise>
            $('[name="skiappable"]').removeClass('ml-disabled').attr('disabled', false);
             </c:otherwise>
          </c:choose>
            $('#logicDiv').find('div.bootstrap-select, input').each( function () {
                $(this).removeClass('ml-disabled');
                if ($(this).is("input")) {
                    $(this).attr('disabled', false).removeClass('cursor-none');;
                }
            });
            $('#addQuestionId').attr('disabled', false);
          $('#repeatableText').val($('#repeatableText', htmlData).val());
          if (isBranching) {
            $('[data-id="destinationStepId"]').removeClass("ml-disabled");
          }
          $('.delete, #addFormula, .switch').removeClass('cursor-none');
          <c:if test="${actionTypeForQuestionPage == 'view'}">
          $('#formStepId input[type="text"]').prop('disabled', true);
          $('#formStepId input[type="radio"]').prop('disabled', true);
          </c:if>
          let mark=true;
          $('tbody tr', htmlData).each(function (index, value) {
            let id = '#'+value.getAttribute('id');
            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
            if (value.getAttribute('status')==="true") {
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc')) {
                edit.addClass('edit-inc');
              }
              if (edit.hasClass('edit-inc-draft')) {
                edit.removeClass('edit-inc-draft');
              }
            }
            else {
              mark=false;
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
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
    })
  }

$('#addFormula').on('click', function () {
    let formContainer = $('#formulaContainer');
    let count = formContainer.find('div.formula-box').length;
    let formula =
        '<div id="form-div' + count + '" class="form-div deletable" style="height: 200px; margin-top:20px">'+
        '<div class="form-group">'+
        '<span class="radio radio-info radio-inline p-45 pl-2">'+
        '<input type="radio" id="andRadio' + count + '" value="&&" class="con-radio con-op-and" name="preLoadLogicBeans['+count+'].conditionOperator" checked/>'+
        '<label for="andRadio' + count + '">AND</label>'+
        '</span>'+
        '<span class="radio radio-inline">'+
        '<input type="radio" id="orRadio' + count + '" value="||" class="con-radio con-op-or" name="preLoadLogicBeans['+count+'].conditionOperator" />'+
        '<label for="orRadio' + count + '">OR</label>'+
        '</span>'+
        '</div>'+
        '<div style="height: 150px">'+
        '<div class="row formula-box">'+
        '<div class="col-md-2"><strong class="font-family: arial;">Formula</strong></div>'+
        '<div class="col-md-10 text-right">'+
        '<span class="delete vertical-align-middle remBtnDis hide pl-md align-span-center" data-id="form-div' + count + '" onclick="removeFormulaContainer(this)"></span>'+
        '</div>'+
        '</div>'+
        '<div style="height: 100px; border:1px solid #bfdceb;">'+
        '<div class="row">'+
        '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Functions</div>'+
        '<div class="col-md-3 gray-xs-f mb-xs" style="padding-top: 18px;">Define Inputs ' +
        '<span class="ml-xs sprites_v3 filled-tooltip preload-tooltip" data-toggle="tooltip" ' +
        'title="For response including \'Height\' please provide response in cm.">' +
        '</span>'+
        '</div>'+
        '<div class="col-md-6"></div>'+
        '</div>'+
        '<div class="row data-div">'+
        '<div class="col-md-1" style="padding-top: 7px">Operator</div>'+
        '<div class="col-md-2 form-group">'+
        '<select  class="selectpicker operator text-normal" data-error="Please select an option" '+
        'id="operator' + count + '" name="preLoadLogicBeans['+count+'].operator" title="-select-">'+
        '<option> < </option>'+
        '<option> > </option>'+
        '<option> = </option>'+
        '<option> != </option>'+
        '<option> >= </option>'+
        '<option> <= </option>'+
        '</select>'+
        '<div class="help-block with-errors red-txt"></div>'+
        '</div>'+
        '<div class="col-md-1" style="padding-top: 7px">Value&nbsp;&nbsp;&nbsp;= </div>'+
        '<div class="col-md-3 form-group">'+
        '<input type="hidden" class="id"/>'+
        '<input type="text" data-error="Please fill out this field." class="form-control value" id="value' + count + '" name="preLoadLogicBeans['+count+'].inputValue" placeholder="Enter">'+
        '<div class="help-block with-errors red-txt"></div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>'+
        '</div>';
    formContainer.append(formula);
    setOperatorDropDownOnAdd($('#lastResponseType').val(), count);
    $('[data-toggle="tooltip"]').tooltip({container: 'body'});
});

if (defaultVisibility.is(':checked')) {
    $('.deletable').remove();
    $('#logicDiv').find('div.bootstrap-select, input, select').each( function () {
        $(this).addClass('ml-disabled');
        if ($(this).is("input.con-radio")) {
            $(this).attr('disabled', true);
        }
        $(this).attr('required', false);
    });
    $('#destinationTrueAsGroup, #preLoadSurveyId').val('').selectpicker('refresh');
    $('#differentSurveyPreLoad').attr('checked', false).attr('disabled', true);
    $('#defaultVisibility').val('true');
    $('#addFormula').attr('disabled', true);
    $('#skiappableYes').prop('disabled', false);
} else {
    $('#skiappableYes').prop('checked', false).prop('disabled', true);
    $('#skiappableNo').prop('checked', true);
}

defaultVisibility.on('change', function () {
    let toggle = $(this);
    let logicDiv = $('#logicDiv');
    let addForm = $('#addFormula');
    if  (toggle.is(':checked')) {
        if (table1 != null) {
            table1.rowReorder.enable();
        }
        $('.deletable').remove();
        logicDiv.find('div.bootstrap-select, input, select').each( function () {
            $(this).addClass('ml-disabled');
            if ($(this).is("select")) {
                $(this).val('').selectpicker('refresh');
                $(this).removeClass('has-error has-danger').find(".help-block").empty();
                $(this).parent().parent().removeClass('has-error has-danger').find(".help-block").empty();
            }
            if ($(this).is("input")) {
                $(this).val('').attr('disabled', true);
                $(this).parent().removeClass('has-error has-danger').find(".help-block").empty();
            }
            $(this).attr('required', false);
        });

        $('#defaultVisibility').val('true');
        if($('#differentSurveyPreLoad').is(':checked')){
            $('#contents').hide();
        }
        $('#destinationTrueAsGroup, #preLoadSurveyId').val('').selectpicker('refresh');
        $('#differentSurveyPreLoad').prop('checked', false).attr('disabled', true);
        $('#preLoadSurveyId').prop('required', false);
        addForm.attr('disabled', true);
        $('#skiappableYes').prop('disabled', false);
        $('#repeatableYes').attr('disabled', false);
    } else {
        if (table1 != null) {
            table1.rowReorder.disable();
        }
        logicDiv.find('div.bootstrap-select, input, select').each( function () {
            $(this).removeClass('ml-disabled');
            if ($(this).is("select")) {
                $(this).selectpicker('refresh');
            }
            if ($(this).is("input")) {
                $(this).attr('disabled', false);
            }
            $(this).attr('required',true);
            $(this).parent().removeClass('has-error has-danger').find(".help-block").empty();
        });
        $('#defaultVisibility').val('false');
        $('#preLoadSurveyId').prop('required', false);
        toggle.attr('checked', false);
        addForm.attr('disabled', false);
        $('#skiappableYes').prop('checked', false).prop('disabled', true);
        $('#skiappableNo').prop('checked', true);
        $('#repeatableYes').attr('disabled', true);
        $('#repeatableNo').attr('checked', true);
    }
})

function removeFormulaContainer(object) {
    let id = object.getAttribute('data-id');
    $('#'+id).remove();
}

    function setOperatorDropDown(responseType) {
        if (responseType != null) {
            if (responseType === '1'|| responseType === '2' ||
                responseType === '8') {
                defaultVisibility.prop('disabled', false);
                let operatorList = ["<", ">", "=", "!=", "<=", ">="];
                let operator = $('select.operator');
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                $('.selectpicker').selectpicker('refresh');
            } else if ((responseType >= '3' && responseType <= '7') || responseType === '11') {
                defaultVisibility.prop('disabled', false);
                let operatorList = ["=", "!="];
                let operator = $('select.operator');
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                $('.selectpicker').selectpicker('refresh');
            } else if (responseType === '14') {
                let operatorList = ["<", ">"];
                let operator = $('select.operator');
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                $('.selectpicker').selectpicker('refresh');
            } else {
                defaultVisibility.prop('checked', true).trigger('change');
                defaultVisibility.prop('disabled', true);
            }
        }
    }

    function setOperatorDropDownOnAdd(responseType, count) {
        if (responseType != null) {
            let operator = $('#operator' + count);
            if (responseType === '1'|| responseType === '2' ||
                responseType === '8') {
                defaultVisibility.prop('disabled', false);
                let operatorList = ["<", ">", "=", "!=", "<=", ">="];
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                operator.selectpicker('refresh');
            } else if ((responseType >= '3' && responseType <= '7') || responseType === '11') {
                defaultVisibility.prop('disabled', false);
                let operatorList = ["=", "!="];
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                operator.selectpicker('refresh');
            } else if (responseType === '14') {
                defaultVisibility.prop('disabled', false);
                let operatorList = ["<", ">"];
                operator.empty();
                $.each(operatorList, function (index, val) {
                    operator.append('<option value="'+val+'">'+val+'</option>');
                });
                operator.selectpicker('refresh');
            } else {
                defaultVisibility.prop('checked', true).trigger('change');
                defaultVisibility.prop('disabled', true);
            }
        }
    }


$('#differentSurveyPreLoad').on('change', function(e) {
    if($(this).is(':checked')) {
        $('#contents').show();
        $('#preLoadSurveyId').prop('required', true);
    } else {
        $('#contents').hide();
        refreshSourceKeys($('#questionnairesId').val(), 'preload');
        $('#preLoadSurveyId').val('').prop('required', false).selectpicker('refresh');
        $('#destinationTrueAsGroup').val('').selectpicker('refresh');
    }
});

if($('#differentSurveyPreLoad').is(':checked')){
    $('#contents').show();
}

$('#preLoadSurveyId').on('change', function () {
    let surveyId = $('#preLoadSurveyId option:selected').attr('data-id');
    refreshSourceKeys(surveyId, 'preload');
})

function refreshSourceKeys(surveyId, type) {
	let id = $('#sourceQuestion');
	if (type === 'preload') {
		id = $('#destinationTrueAsGroup');
	}
	id.empty().selectpicker('refresh');
    if (surveyId !== '') {
        $.ajax({
            url : "/fdahpStudyDesigner/adminStudies/refreshSourceKeys.do",
            type : "GET",
            datatype : "json",
            data : {
                caller : type,
                seqNo : $('#seqNo').val(),
                stepId : $('#stepId').val(),
                questionnaireId : surveyId,
                isDifferentSurveyPreload : $('#differentSurveyPreLoad').is(':checked'),
                preLoadQuestionnaireId : $('#questionnairesId').val(),
                instructionFormId : $('#instructionFormId').val(),
                "${_csrf.parameterName}":"${_csrf.token}"
            },
            success : function(data) {
                let message = data.message;
                if(message === 'SUCCESS'){
                    let options = data.sourceKeys;
                    if (options != null && options.length > 0) {
                        $.each(options, function(index, option) {
                            let $option = $("<option></option>")
                                .attr("value", option.stepId)
                                .attr("data-id", option.stepId)
                                .attr("data-type", 'step')
                                .text("Step " + (option.sequenceNo) + " : " + option.stepShortTitle);
                            id.append($option);
                        });
                    }
                    if (type === 'preload') {
                        id.append('<option data-type="step" value="0">Completion Step</option>');
                        if (!$('#differentSurveyPreLoad').is(':checked')) {
                            <c:forEach items="${groupsListPostLoad}" var="group" varStatus="status">
                            id.append('<option data-type="group" value="${group.id}" id="selectGroup${group.id}">'+
                                'Group : ${group.groupName}&nbsp;'+
                                '</option>');
                            </c:forEach>
                        } else {
                            let groups = data.groupList;
                            if (groups != null && groups.length > 0) {
                                $.each(groups, function (index, option) {
                                    let $option = $("<option></option>")
                                        .attr("value", option.id)
                                        .attr("data-id", option.id)
                                        .attr("data-type", 'group')
                                        .text("Group " + (index + 1) + " : " + option.groupName);
                                    id.append($option);
                                });
                            }
                        }
                    }
                    id.selectpicker('refresh');

                    <%--let groupsList = '${groupsListPreLoad}';--%>
                    <%--if ((options == null || options.length === 0) && (groupsList.length === 0)) {--%>
                    <%--    let $option = $("<option></option>")--%>
                    <%--        .attr("style", "text-align: center; color: #000000")--%>
                    <%--        .attr("disabled", true)--%>
                    <%--        .text("- No items found -");--%>
                    <%--    id.append($option).selectpicker('refresh');--%>
                    <%--}--%>
                } else {
                    showErrMsg('Server error while fetching data.');
                }
            },
            error : function status(data, status) {
                console.log(data, status);
            },
        });
    }
}
</script>