<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<head>
    <meta charset="UTF-8">
</head>
<style>
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

  .ml-disabled {
      background-color: #eee !important;
      opacity: 1;
      cursor: not-allowed;
      pointer-events: none;
  }

  .langSpecific > button {
    padding-left: 30px;
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
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <form:form action="/fdahpStudyDesigner/sessionOut.do" id="backToLoginPage" name="backToLoginPage" method="post"></form:form>
    <form:form
            action="/fdahpStudyDesigner/adminStudies/saveOrUpdateInstructionStep.do?_S=${param._S}"
            name="basicInfoFormId" id="basicInfoFormId" method="post"
            data-toggle="validator" role="form">
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f text-uppercase dis-line pull-left line34">
					<span class="mr-xs cur-pointer" onclick="goToBackPage(this);"><img
                            src="../images/icons/back-b.png"/></span>
                    <c:if test="${actionTypeForQuestionPage == 'edit'}">Edit Instruction Step</c:if>
                    <c:if test="${actionTypeForQuestionPage == 'view'}">View Instruction Step <c:set
                            var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}
                    </c:if>
                    <c:if test="${actionTypeForQuestionPage == 'add'}">Add Instruction Step</c:if>
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
                <c:if test="${actionTypeForQuestionPage ne 'view'}">
                    <div class="dis-line form-group mb-none mr-sm">
                        <button type="button" class="btn btn-default gray-btn" id="saveId"
                                onclick="saveIns(this);">Save
                        </button>
                    </div>
                    <div class="dis-line form-group mb-none">
                        <button type="button" class="btn btn-primary blue-btn" id="doneId">Done
                        </button>
                    </div>
                </c:if>
            </div>
        </div>
        <!-- End top tab section-->
        <!-- Start body tab section -->
        <div class="right-content-body">
            <!-- form- input-->
            <input type="hidden" name="id" id="id" value="${instructionsBo.id}">
            <input type="hidden" id="seqNo" value="${instructionsBo.questionnairesStepsBo.sequenceNo}">
            <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
            <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
            <input type="hidden" name="questionnaireId" id="questionnaireId"
                   value="${questionnaireId}"> <input type="hidden"
                                                      id="questionnaireShortId"
                                                      value="${questionnaireBo.shortTitle}">
            <input type="hidden" id="type" name="type" value="complete"/> <input
                type="hidden" name="questionnairesStepsBo.stepId" id="stepId"
                value="${instructionsBo.questionnairesStepsBo.stepId}">
            <input type="hidden" id="mlTitle" value="${instructionsLangBO.instructionTitle}">
            <input type="hidden" id="mlSnippet" value="${instructionsLangBO.pipingSnippet}">
            <input type="hidden" id="mlText" value="${instructionsLangBO.instructionText}">
            <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
             <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
             <input type="hidden" id="stepOrGroupPostLoad" value="${instructionsBo.questionnairesStepsBo.stepOrGroupPostLoad}" name="questionnairesStepsBo.stepOrGroupPostLoad"/>
            <div class="row"><div class="col-md-6 pl-none">
                <div class="gray-xs-f mb-xs">
                    Step title or Key (1 to 15 characters)<span class="requiredStar">*</span><span
                        class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip"
                        title="A human readable step identifier and must be unique across all steps of the questionnaire.Note that this field cannot be edited once the study is Launched."></span>
                </div>
                <div class="form-group">
                    <input autofocus="autofocus" type="text" custAttType="cust"
                           class="form-control" name="questionnairesStepsBo.stepShortTitle"
                           id="shortTitleId"
                           value="${fn:escapeXml(instructionsBo.questionnairesStepsBo.stepShortTitle)}"
                           required="required" maxlength="15"
                            <c:if test="${not empty instructionsBo.questionnairesStepsBo.isShorTitleDuplicate && (instructionsBo.questionnairesStepsBo.isShorTitleDuplicate gt 0)}"> disabled</c:if> />
                    <div class="help-block with-errors red-txt"></div>
                    <input type="hidden" id="preShortTitleId"
                           value="${fn:escapeXml(instructionsBo.questionnairesStepsBo.stepShortTitle)}"/>
                </div>
            </div>
            <div class="col-md-6">
                <div class="gray-xs-f mb-xs">Step Type</div>
                <div>Instruction Step</div>
            </div>
            </div>
            <div class="clearfix"></div>
            <div class="gray-xs-f mb-xs">
                Title (1 to 250 characters)<span class="requiredStar">*</span>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" required
                       name="instructionTitle" id="instructionTitle"
                       value="${fn:escapeXml(instructionsBo.instructionTitle)}"
                       maxlength="250"/>
                <div class="help-block with-errors red-txt"></div>
            </div>
            <div class="clearfix"></div>

            <div class="gray-xs-f mb-xs">
                Instruction Text (1 to 500 characters)<span class="requiredStar">*</span>
            </div>
            <div class="form-group">
				<textarea class="form-control" rows="5" id="instructionText"
                          name="instructionText" required
                          maxlength="500">${instructionsBo.instructionText}</textarea>
                <div class="help-block with-errors red-txt"></div>
            </div>
            <div class="clearfix"></div>
            <c:if test="${questionnaireBo.branching}">
                <div class="col-md-4 col-lg-3 p-none">
                    <div class="gray-xs-f mb-xs">
                        Default Destination Step <span class="requiredStar">*</span> <span
                            class="ml-xs sprites_v3 filled-tooltip"></span>
                    </div>
                    <div class="form-group">
                        <select name="questionnairesStepsBo.destinationStep"
                                id="destinationStepId" data-error="Please choose one title"
                                class="selectpicker" required>
                            <c:forEach items="${destinationStepList}" var="destinationStep">
                                <option value="${destinationStep.stepId}" data-type="step"
                                    ${instructionsBo.questionnairesStepsBo.destinationStep eq destinationStep.stepId ? 'selected' :''}>
                                    Step
                                        ${destinationStep.sequenceNo} :
                                        ${destinationStep.stepShortTitle}</option>
                            </c:forEach>
                            <c:forEach items="${groupsPostLoadList}" var="group" varStatus="status">
                                <option value="${group.id}"  data-type="group" id="selectGroup${group.id}"
                                ${instructionsBo.questionnairesStepsBo.destinationStep eq group.id ? 'selected' :''}>
                                Group :  ${group.groupName}&nbsp;</option>
                            </c:forEach>
                            <option value="0" data-type="step"
                                ${instructionsBo.questionnairesStepsBo.destinationStep eq 0 ? 'selected' :''}>
                                Completion
                                Step
                            </option>
                        </select>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
            </c:if>

            <div class="row">
                <button type="button" class="btn btn-primary blue-btn" id="pbutton">Piping</button>
            </div><br>
        </div>
    </form:form>

    <div class="modal fade" id="pipingModal" role="dialog">
        <div class="modal-dialog modal-lg">
            <div class="modal-content" style="width: 65%; margin-left: 17%;">
                <div class="pl-xlg cust-hdr pt-xl">
                    <h5 class="modal-title">
                        <b>Piping</b>
                    </h5>
                </div>
                <br>
                <div class="modal-body pt-xs pb-lg pl-xlg pr-xlg">
                    <div class="gray-xs-f mb-xs">Target Element</div>
                    <div class="mb-xs" id="titleText"></div>
                    <br>

                    <div class="gray-xs-f mb-xs">Snippet</div>
                    <div class="mb-xs">
                        <input type="text" class="form-control req" placeholder="Enter" id="pipingSnippet" name="pipingSnippet" value="${instructionsBo.questionnairesStepsBo.pipingSnippet}"/>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                    <br>

                    <div class="mb-xs">
					<span class="checkbox checkbox-inline">
						<input type="checkbox" id="differentSurvey" name="differentSurvey"
                               <c:if test="${not empty instructionsBo.questionnairesStepsBo.differentSurvey
								and instructionsBo.questionnairesStepsBo.differentSurvey}">checked</c:if> />
						<label for="differentSurvey"> Is different survey? </label>
					</span>
                    </div>
                    <br>

                    <div id="surveyBlock" <c:if test="${empty instructionsBo.questionnairesStepsBo.differentSurvey
				or !instructionsBo.questionnairesStepsBo.differentSurvey}">style="display:none"</c:if>>
                        <div class="gray-xs-f mb-xs">Survey ID</div>
                        <div class="mb-xs">
                            <select class="selectpicker text-normal req" name="pipingSurveyId" id="surveyId" title="-select-">
                                <c:forEach items="${questionnaireIds}" var="key" varStatus="loop">
                                    <option data-id="${key.id}" value="${key.shortTitle}" id="${key.shortTitle}"
                                            <c:if test="${key.id eq instructionsBo.questionnairesStepsBo.pipingSurveyId}"> selected</c:if>>
                                        Survey ${loop.index+1} : ${key.shortTitle}
                                    </option>
                                </c:forEach>
                                <c:if test="${questionnaireIds eq null || questionnaireIds.size() eq 0}">
                                    <option style="text-align: center; color: #000000" disabled>- No items found -</option>
                                </c:if>
                            </select>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        <br>
                    </div>

                    <div class="gray-xs-f mb-xs">Source Question</div>
                    <div class="mb-xs">
                        <select class="selectpicker text-normal req" name="pipingSourceQuestionKey" id="sourceQuestion" title="-select-">
                            <c:forEach items="${sameSurveyPipingSourceKeys}" var="key" varStatus="loop">
                                <option data-id="${key.stepId}" value="${key.stepId}"
                                        <c:if test="${key.stepId eq instructionsBo.questionnairesStepsBo.pipingSourceQuestionKey}"> selected</c:if>>
                                    Step ${key.sequenceNo} : ${key.stepShortTitle}
                                </option>
                            </c:forEach>
                            <c:if test="${sameSurveyPipingSourceKeys eq null || sameSurveyPipingSourceKeys.size() eq 0}">
                                <option style="text-align: center; color: #000000" disabled>- No items found -</option>
                            </c:if>
                        </select>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                    <br><br>

                    <div class="dis-line form-group mb-none mr-sm">
                        <button type="button" class="btn btn-default gray-btn" id="cancelPiping">Cancel</button>
                    </div>
                    <div class="dis-line form-group mb-none mr-sm">
                        <button type="button" class="btn btn-primary blue-btn" id="savePiping" onclick="submitPiping();">Submit</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!--  End body tab section -->
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
                    <div id="timeOutMessage" class="text-right blue_text"><span class="timerPos"><img
                            src="../images/timer2.png"/></span>Your session expires in 15 minutes
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End right Content here -->
<script type="text/javascript">
  var idleTime = 0;
  $(document).ready(function () {

    <c:if test="${actionTypeForQuestionPage == 'view'}">
    $('#basicInfoFormId input,textarea ').prop('disabled', true);
    $('#pipingSnippet').prop('disabled', true);
    		  $('#sourceQuestion').prop('disabled', true);
    		  $('#surveyId').prop('disabled', true);
    		  $('#savePiping').prop('disabled', true);
    		  $('#differentSurvey').prop('disabled', true);
    $('#basicInfoFormId select').addClass('linkDis');
    $('#studyLanguage').removeClass('linkDis');
    $('.selectpicker').selectpicker('refresh');
    </c:if>

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');
    $("#shortTitleId").blur(function () {
      validateShortTitle('', function (val) {
      });
    });
    $('[data-toggle="tooltip"]').tooltip();
    $("#doneId").click(function () {
        let lang = $('#studyLanguage').val();
        if (lang !== '' && lang !== 'en' && $('#sourceQuestion').val() !== '' && $('#pipingSnippet').val() === '') {
            $('#pipingModal').modal('show');
            $('#pipingSnippet').parent().addClass('has-error has-danger').find(".help-block")
                .empty()
                .append($("<ul><li> </li></ul>")
                    .attr("class","list-unstyled")
                    .text("Please fill out this field."));
            return false;
        }
      $("#doneId").attr("disabled", true);
      validateShortTitle('', function (val) {
        if (val) {
          $('#shortTitleId').prop('disabled', false);
          if (isFromValid("#basicInfoFormId")) {
          			  if ('${questionnaireBo.branching}' === 'true') {
                            $('#stepOrGroupPostLoad').val($('#destinationStepId option:selected').attr('data-type'));
          			  } else {
                             $('#stepOrGroupPostLoad').val('');
                           }
            document.basicInfoFormId.submit();
          } else {
            $("#doneId").attr("disabled", false);

          }
        } else {
          $("#doneId").attr("disabled", false);
        }
      });
    });
    setInterval(function () {
            idleTime += 1;
            if (idleTime > 3) {
                    <c:if test="${actionTypeForQuestionPage ne 'view'}">
                    autoSaveInstructionStepPage('auto');
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

  $('#pbutton').on('click', function() {
      $('#titleText').text($('#instructionTitle').val());
      $('#pipingModal').modal('toggle');
  });

  function saveIns() {
   autoSaveInstructionStepPage('manual');
  }
  function autoSaveInstructionStepPage(mode) {
      $("body").addClass("loading");
      $("#saveId").attr("disabled", true);
      validateShortTitle('', function (val) {
        if (val) {
            if (mode === 'auto') {
                $('#isAutoSaved').val('true');
            }
          saveInstruction();
        } else {
          $("#saveId").attr("disabled", false);
          $("body").removeClass("loading");
        }
      });
  }
  function validateShortTitle(item, callback) {
    var shortTitle = $("#shortTitleId").val();
    var questionnaireId = $("#questionnaireId").val();
    var stepType = "Instruction";
    var thisAttr = $("#shortTitleId");
    var existedKey = $("#preShortTitleId").val();
    var questionnaireShortTitle = $("#questionnaireShortId").val();
    if (shortTitle != null && shortTitle != ''
        && typeof shortTitle != 'undefined') {
      if (existedKey != shortTitle) {
        $
        .ajax({
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
            xhr.setRequestHeader("X-CSRF-TOKEN",
                "${_csrf.token}");
          },
          success: function getResponse(data) {
            var message = data.message;

            if ('SUCCESS' != message) {
              $(thisAttr).validator('validate');
              $(thisAttr).parent().removeClass(
                  "has-danger").removeClass(
                  "has-error");
              $(thisAttr).parent().find(".help-block")
              .empty();
              callback(true);
            } else {
              $(thisAttr).val('');
              $(thisAttr).parent().addClass("has-danger")
              .addClass("has-error");
              $(thisAttr).parent().find(".help-block")
              .empty();
              $(thisAttr)
              .parent()
              .find(".help-block")
              .append(
                  $("<ul><li> </li></ul>")
                  .attr("class",
                      "list-unstyled")
                  .text(
                      shortTitle
                      + " has already been used in the past."));
              callback(false);
            }
          },
          global: false
        });
      } else {
        callback(true);
      }
    } else {
      callback(false);
    }
  }

  function saveInstruction() {
    var instruction_id = $("#id").val();
    var questionnaire_id = $("#questionnaireId").val();
    var instruction_title = $("#instructionTitle").val();
    var instruction_text = $("#instructionText").val();

    var shortTitle = $("#shortTitleId").val();
    var destinationStep = $("#destinationStepId").val();
    var step_id = $("#stepId").val();

    var instruction = new Object();
    if ((questionnaire_id != null && questionnaire_id != '' && typeof questionnaire_id
            != 'undefined')
        && (shortTitle != null && shortTitle != '' && typeof shortTitle != 'undefined')) {
      instruction.questionnaireId = questionnaire_id;
      instruction.id = instruction_id;
      instruction.instructionTitle = instruction_title;
      instruction.instructionText = instruction_text;
      instruction.type = "save";

      var questionnaireStep = new Object();
      questionnaireStep.stepId = step_id;
      questionnaireStep.stepShortTitle = shortTitle;
      questionnaireStep.destinationStep = destinationStep;
      if ('${questionnaireBo.branching}' === 'true') {
         questionnaireStep.stepOrGroupPostLoad =  $('#destinationStepId option:selected').attr('data-type');
         }
      instruction.questionnairesStepsBo = questionnaireStep;

      var data = JSON.stringify(instruction);

        let pipingObject = null;
        if ($('#isAutoSaved').val() === 'true' && $('#pipingModal').hasClass('show') && validatePipingRequiredFields()) {
            let object = {};
            object.pipingSnippet = $('#pipingSnippet').val();
            object.pipingSourceQuestionKey = $('#sourceQuestion option:selected').attr('data-id');
            if ($('#differentSurvey').is(':checked')) {
                object.differentSurvey = true;
                object.pipingSurveyId = $('#surveyId option:selected').attr('data-id');
            }
            object.language = $('#studyLanguage').val();
            object.stepId = $('#stepId').val();
            pipingObject = JSON.stringify(object);
        }
      $.ajax({
        url: "/fdahpStudyDesigner/adminStudies/saveInstructionStep.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          instructionsInfo: data,
          pipingObject: pipingObject,
          language: $('#studyLanguage').val(),
          isAutoSaved : $('#isAutoSaved').val()
        },
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN",
              "${_csrf.token}");
        },
        success: function (data) {
          var message = data.message;
          if (message === "SUCCESS") {
            $("#preShortTitleId").val(shortTitle);
            let instructionId = data.instructionId;
            let stepId = data.stepId;
            $("#id").val(instructionId);
            $("#stepId").val(stepId);
            $("#alertMsg").removeClass('e-box').addClass('s-box').text("Content saved as draft.");
            $("#saveId").attr("disabled", false);
            $('#alertMsg').show();
            if ($('.seventhQuestionnaires').find('span').hasClass('sprites-icons-2 tick pull-right mt-xs')) {
              $('.seventhQuestionnaires').find('span').removeClass('sprites-icons-2 tick pull-right mt-xs');
            }
            $("body").removeClass("loading");
            // pop message after 15 minutes
            if (data.isAutoSaved === 'true') {
                $('#myModal').modal('show');
                let i = 1;
                let j = 14;
                let lastSavedInterval = setInterval(function () {
                   if ((i === 15) || (j === 0)) {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                        if ($('#myModal').hasClass('show')) {
                            $('#backToLoginPage').submit();
                        }
                        clearInterval(lastSavedInterval);
                    } else {
                    if ((i === 1) || (j === 14)) {
                    $('#autoSavedMessage').html('<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css("fontSize", "15px");
                    }
                     else if ((i === 14) || (j === 1)) {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>')
                     }
                     else {
                     $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> ' + j +' minutes</span></div>').css("fontSize", "15px");
                        }
                        idleTime = 0;
                        i += 1;
                        j -=1;
                    }
                }, 60000);
                $("#isAutoSaved").val('false');
            }
          } else {
            $("#alertMsg").removeClass('s-box').addClass(
                'e-box').text("Something went Wrong");
            $('#alertMsg').show();
          }
          setTimeout(hideDisplayMessage, 4000);
        },
        error: function (xhr, status, error) {
          $('#alertMsg').show();
          $("#alertMsg").removeClass('s-box').addClass(
              'e-box').text("Something went Wrong");
          setTimeout(hideDisplayMessage, 4000);
        }
      });
    } else {
      $('#shortTitleId').validator('destroy').validator();
      if (!$('#shortTitleId')[0].checkValidity()) {
        $("#shortTitleId").parent().addClass('has-error has-danger')
        .find(".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class",
                "list-unstyled").text(
                "This is a required field."));
      }
    }
  }

  function goToBackPage(item) {
    $(item).prop('disabled', true);
    <c:if test="${actionTypeForQuestionPage ne 'view'}">
    bootbox
    .confirm({
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

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/instructionsStep.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        validatePipingData();
        if (language !== 'en') {
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          $('#shortTitleId, [data-id="destinationStepId"]').addClass('ml-disabled');
          $('#differentSurvey').attr('disabled', true).addClass('ml-disabled');
          $('#surveyId, [data-id="surveyId"]').addClass('ml-disabled');
          $('#sourceQuestion, [data-id="sourceQuestion"]').addClass('ml-disabled');
          $('#instructionTitle').val($('#mlTitle', htmlData).val());
          $('#pipingSnippet').val($('#mlSnippet', htmlData).val());
          $('#instructionText').val($('#mlText', htmlData).val());
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#shortTitleId, [data-id="destinationStepId"]').removeClass('ml-disabled');
          $('#piping').removeClass('cursor-none');
          $('#instructionTitle').val($('#instructionTitle', htmlData).val());
          $('#pipingSnippet').val($('#pipingSnippet', htmlData).val());
          $('#instructionText').val($('#instructionText', htmlData).val());
          $('#differentSurvey').attr('disabled', false).removeClass('ml-disabled');
          $('#sourceQuestion, [data-id="sourceQuestion"]').removeClass('ml-disabled');
          $('#surveyId, [data-id="surveyId"]').removeClass('ml-disabled');

          <c:if test="${actionTypeForQuestionPage == 'view'}">
          $('#basicInfoFormId input,textarea ').prop('disabled', true);
          </c:if>
        }
      }
    })
  }


  $('#cancelPiping').on('click', function() {
      $('#pipingModal').modal('hide');
  })

  $('#differentSurvey').on('change', function(e) {
      if ($('#surveyId').closest('div.mb-xs').hasClass('has-error has-danger')) {
          $('#surveyId').closest('div.mb-xs').removeClass('has-error has-danger').find(".help-block").empty();
      }
      if($(this).is(':checked')) {
          $('#surveyBlock').show();
      } else {
          $('#surveyBlock').hide();
          refreshSourceKeys($('#questionnaireId').val(), null);
          $('#surveyId').val('').selectpicker('refresh');
          $('#sourceQuestion').val('').selectpicker('refresh');
      }
  });

  $('#surveyId').on('change', function () {
      let surveyId = $('#surveyId option:selected').attr('data-id');
      refreshSourceKeys(surveyId, 'piping');
  })

  function refreshSourceKeys(surveyId, type) {
      let id = $('#sourceQuestion');
      id.empty().selectpicker('refresh');
      if (surveyId !== '') {
          $.ajax({
              url : "/fdahpStudyDesigner/adminStudies/refreshSourceKeys.do",
              type : "GET",
              datatype : "json",
              data : {
                  caller : "piping",
                  seqNo : $('#seqNo').val(),
                  questionnaireId : surveyId,
                  isDifferentSurveyPiping : $('#differentSurvey').is(':checked'),
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
                                  .text("Step " + (option.sequenceNo) + " : " + option.stepShortTitle);
                              id.append($option);
                          });
                      }
                      id.selectpicker('refresh');
                      if (options == null || options.length === 0) {
                          let $option = $("<option></option>")
                              .attr("style", "text-align: center; color: #000000")
                              .attr("disabled", true)
                              .text("- No items found -");
                          id.append($option).selectpicker('refresh');
                      }
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

  $('select.req, input.req').on('change', function () {
      let parent = $(this).parent();
      if ($(this).is('select')) {
          parent = $(this).closest('div.mb-xs');
      }
      if ($(this).val() === '') {
          if (id !== 'surveyId' || (id === 'surveyId' && $('#differentSurvey').is(':checked'))) {
              parent.addClass('has-error has-danger').find(".help-block")
                  .empty()
                  .append($("<ul><li> </li></ul>")
                      .attr("class","list-unstyled")
                      .text("Please fill out this field."));
          }
      } else {
          if (parent.hasClass('has-error has-danger')) {
              parent.removeClass('has-error has-danger').find(".help-block").empty();
          }
      }
  });

  function submitPiping() {
      if (validatePipingRequiredFields()) {
          if ($('#stepId').val() !== '') {
              let pipingObject = {};
              pipingObject.pipingSnippet = $('#pipingSnippet').val();
              pipingObject.pipingSourceQuestionKey = $('#sourceQuestion option:selected').attr('data-id');
              if ($('#differentSurvey').is(':checked')) {
                  pipingObject.differentSurvey = true;
                  pipingObject.pipingSurveyId = $('#surveyId option:selected').attr('data-id');
              }
              pipingObject.language = $('#studyLanguage').val();
              pipingObject.stepId = $('#stepId').val();
              let dataObject = JSON.stringify(pipingObject);
              $.ajax({
                  url: "/fdahpStudyDesigner/adminStudies/submitPiping.do?_S=${param._S}",
                  type: "POST",
                  datatype: "json",
                  data: {
                      dataObject : dataObject
                  },
                  beforeSend: function (xhr) {
                      xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
                  },
                  success: function (data) {
                      let message = data.message;
                      let status = data.status
                      $('#pipingModal').modal('hide');
                      if (status === 'SUCCESS') {
                          showSucMsg(message);
                      } else {
                          showErrMsg(message);
                      }

                  },
                  error: function (xhr, status, error) {
                      $('#pipingModal').modal('hide');
                      showErrMsg("Error while saving piping details");
                  }
              });
          } else {
              $('#pipingModal').modal('hide');
              showErrMsg("Please save step first!");
          }
      }
  }

  function validatePipingRequiredFields() {
      let valid = true;
      let language = $('#studyLanguage').val();
      if (language !== null && language !== undefined && language !== 'en') {
          let parent = $('#pipingSnippet').parent();
          if ($('#pipingSnippet').val() === '') {
              parent.addClass('has-error has-danger').find(".help-block")
                  .empty()
                  .append($("<ul><li> </li></ul>")
                      .attr("class","list-unstyled")
                      .text("Please fill out this field."));
              if (valid) {
                  valid = false;
              }
          } else {
              if (parent.hasClass('has-error has-danger')) {
                  parent.removeClass('has-error has-danger').find(".help-block").empty();
              }
          }
      }
      else {
          $('select.req, input.req').each(function () {
              let parent = $(this).parent();
              let id = $(this).attr('id');
              if ($(this).is('select')) {
                  parent = $(this).closest('div.mb-xs');
              }
              if ($(this).val() === '') {
                  if (id !== 'surveyId' || (id === 'surveyId' && $('#differentSurvey').is(':checked'))) {
                      parent.addClass('has-error has-danger').find(".help-block")
                          .empty()
                          .append($("<ul><li> </li></ul>")
                              .attr("class","list-unstyled")
                              .text("Please fill out this field."));
                      if (valid) {
                          valid = false;
                      }
                  }
              } else {
                  if (parent.hasClass('has-error has-danger')) {
                      parent.removeClass('has-error has-danger').find(".help-block").empty();
                  }
              }
          });
      }
      return valid;
  }

  function validatePipingData() {
        $('select.req, input.req').each(function () {
            let parent = $(this).parent();
            let id = $(this).attr('id');
            if ($(this).is('select')) {
                parent = $(this).closest('div.mb-xs');
            }
            if (parent.hasClass('has-error has-danger')) {
                parent.removeClass('has-error has-danger').find(".help-block").empty();
            }
        });
  }
</script>