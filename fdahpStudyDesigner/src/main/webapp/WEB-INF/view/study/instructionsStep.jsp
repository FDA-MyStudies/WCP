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

  .langSpecific > button {
    padding-left: 30px;
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
            <input type="hidden" id="mlText" value="${instructionsLangBO.instructionText}">
            <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
             <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
            <div class="col-md-6 pl-none">
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
                                <option value="${destinationStep.stepId}"
                                    ${instructionsBo.questionnairesStepsBo.destinationStep eq destinationStep.stepId ? 'selected' :''}>
                                    Step
                                        ${destinationStep.sequenceNo} :
                                        ${destinationStep.stepShortTitle}</option>
                            </c:forEach>
                            <option value="0"
                                ${instructionsBo.questionnairesStepsBo.destinationStep eq 0 ? 'selected' :''}>
                                Completion
                                Step
                            </option>
                        </select>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
            </c:if>
        </div>
    </form:form>
    <!--  End body tab section -->
    <div class="modal fade" id="myModal" role="dialog">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content" style="width: 49%; margin-left: 82%; color: #22355e">
                    <div class="modal-header cust-hdr pt-lg">
                        <button type="button" class="close pull-right" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title pl-lg text-center">
                            <b id="autoSavedMessage">Last saved was 1 minute ago</b>
                        </h4>
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
      $("#doneId").attr("disabled", true);
      validateShortTitle('', function (val) {
        if (val) {
          $('#shortTitleId').prop('disabled', false);
          if (isFromValid("#basicInfoFormId")) {
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
            if (idleTime > 3) { // 5 minutes
                    autoSaveInstructionStepPage('auto');
            }
        }, 10000); // 5 minutes

        $(this).mousemove(function (e) {
            idleTime = 0;
        });
        $(this).keypress(function (e) {
            idleTime = 0;
        });
  });

  function saveIns() {
   autoSaveInstructionStepPage('manual');
  }
  function autoSaveInstructionStepPage(mode){
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

  function saveInstruction(item) {
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
      questionnaireStep.destinationStep = destinationStep
      instruction.questionnairesStepsBo = questionnaireStep;

      var data = JSON.stringify(instruction);
      $
      .ajax({
        url: "/fdahpStudyDesigner/adminStudies/saveInstructionStep.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: {
          instructionsInfo: data,
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
            var instructionId = data.instructionId;
            var stepId = data.stepId;
            $("#id").val(instructionId);
            $("#stepId").val(stepId);
            $("#alertMsg").removeClass('e-box').addClass(
                's-box')
            .text("Content saved as draft.");
            $(item).prop('disabled', false);
            $("#saveId").attr("disabled", false);
            $('#alertMsg').show();
            if ($('.seventhQuestionnaires')
            .find('span')
            .hasClass(
                'sprites-icons-2 tick pull-right mt-xs')) {
              $('.seventhQuestionnaires')
              .find('span')
              .removeClass(
                  'sprites-icons-2 tick pull-right mt-xs');
            }
            $("body").removeClass("loading");
            // pop message after 15 minutes
            if (data.isAutoSaved === 'true') {
                $('#myModal').modal('show');
                let i = 2;
        setInterval(function () {
            if (i===16) {
                $('#backToLoginPage').submit();
            } else {
                $('#autoSavedMessage').text('Last saved was '+i+' minutes ago');
                i+=1;
            }
        }, 500);
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
          $(item).prop('disabled', false);
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
        if (language !== 'en') {
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          $('#shortTitleId, #destinationStepId').addClass('ml-disabled');
          $('#instructionTitle').val($('#mlTitle', htmlData).val());
          $('#instructionText').val($('#mlText', htmlData).val());
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#shortTitleId, #destinationStepId').removeClass('ml-disabled');
          $('#instructionTitle').val($('#instructionTitle', htmlData).val());
          $('#instructionText').val($('#instructionText', htmlData).val());

          <c:if test="${actionTypeForQuestionPage == 'view'}">
          $('#basicInfoFormId input,textarea ').prop('disabled', true);
          </c:if>
        }
      }
    })
  }
</script>