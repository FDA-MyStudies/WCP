<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="UTF-8">
</head>
<style>
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
</style>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <form:form
            action="/fdahpStudyDesigner/adminStudies/saveOrUpdateComprehensionTestQuestion.do?_S=${param._S}&${_csrf.parameterName}=${_csrf.token}"
            name="comprehensionFormId" id="comprehensionFormId" method="post"
            role="form">
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f dis-line pull-left line34">
					<span class="pr-sm cur-pointer" onclick="goToBackPage(this);"><img
                            src="../images/icons/back-b.png"/></span>
                    <c:if test="${empty comprehensionQuestionBo.id}">Add Comprehension Test Question</c:if>
                    <c:if
                            test="${not empty comprehensionQuestionBo.id && actionPage eq 'addEdit'}">Edit Comprehension Test Question</c:if>
                    <c:if
                            test="${not empty comprehensionQuestionBo.id && actionPage eq 'view'}">View Comprehension Test Question<c:set
                            var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}
                    </c:if>
                </div>

                <c:if test="${studyBo.multiLanguageFlag eq true and not empty comprehensionQuestionBo.id}">
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

                <c:if test="${studyBo.multiLanguageFlag eq true and empty comprehensionQuestionBo.id}">
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
                <div class="dis-line form-group mb-none mr-sm ">
                    <button type="button"
                            class="btn btn-default gray-btn TestQuestionButtonHide"
                            id="saveId">Save
                    </button>
                </div>
                <div class="dis-line form-group mb-none">
                    <button type="button"
                            class="btn btn-primary blue-btn TestQuestionButtonHide"
                            id="doneId">Done
                    </button>
                </div>
            </div>
        </div>
        <!-- End top tab section-->
        <!-- Start body tab section -->
        <div class="right-content-body pt-none pb-none">
            <input type="hidden" id="id" name="id" value="${comprehensionQuestionBo.id}">
            <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
            <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
            <input type="hidden" id="questionTextLang"
                   value="${comprehensionQuestionLangBO.questionText}">
            <select id="responseItems" style="display: none">
                <c:forEach items="${comprehensionQuestionLangBO.comprehensionResponseLangBoList}"
                           var="responseList">
                    <option id='${responseList.comprehensionResponseLangPK.id}'
                            value="${responseList.responseOption}">${responseList.responseOption}</option>
                </c:forEach>
            </select>

            <input type="hidden" id="currentLanguage" name="currentLanguage"
                   value="${currLanguage}">
                    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
            <c:if test="${not empty comprehensionQuestionBo.id}">
                <input type="hidden" id="studyId" name="studyId"
                       value="${comprehensionQuestionBo.studyId}">
            </c:if>
            <c:if test="${empty comprehensionQuestionBo.id}">
                <input type="hidden" id="studyId" name="studyId" value="${studyId}">
            </c:if>
            <div>
                <div class="gray-xs-f mb-xs mt-md">
                    Question Text (1 to 300 characters)<span class="requiredStar">*</span>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control" name="questionText"
                           id="questionText" required
                           value="${comprehensionQuestionBo.questionText}" maxlength="300"/>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
            <!-- Answer option section-->
            <div class="col-md-11 col-lg-12 p-none">
                <!-- Bending Answer options -->
                <div class="unitDivParent">
                    <c:if
                            test="${fn:length(comprehensionQuestionBo.responseList) eq 0}">
                        <div class="col-md-12 p-none">
                            <div class='col-md-6 pl-none'>
                                <div class="gray-xs-f mb-xs">
                                    Answer Options (1 to 150 characters)<span
                                        class="requiredStar">*</span>
                                </div>
                            </div>
                            <div class='col-md-3'>
                                <div class="gray-xs-f mb-xs">
                                    Correct Answer <span class="requiredStar">*</span>
                                </div>
                            </div>

                            <div class="col-md-3">
                                <div class="gray-xs-f mb-xs">&nbsp;</div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <div class="ans-opts col-md-12 p-none" id="0">
                            <div class='col-md-6 pl-none'>
                                <div class='form-group'>
                                    <input type='text' class='form-control responseOptionClass'
                                           name="responseList[0].responseOption"
                                           id="responseOptionId0"
                                           required maxlength="150"
                                           onblur="validateForUniqueValue(this,function(){});"
                                           onkeypress="resetValue(this);"/>
                                    <div class='help-block with-errors red-txt'></div>
                                </div>
                            </div>
                            <div class='col-md-3'>
                                <div class="form-group">
                                    <select class='selectpicker wid100'
                                            name="responseList[0].correctAnswer"
                                            id="correctAnswerId0"
                                            required data-error='Please choose one option'>
                                        <option value=''>Select</option>
                                        <option value="true">Yes</option>
                                        <option value="false">No</option>
                                    </select>
                                    <div class='help-block with-errors red-txt'></div>
                                </div>
                            </div>
                            <div class="col-md-3 pl-none">
                                <div class="clearfix"></div>
                                <div class="mt-xs formgroup">
									<span class="addBtnDis addbtn mr-sm align-span-center"
                                          onclick='addAns();'>+</span> <span
                                        class="delete vertical-align-middle remBtnDis hide pl-md align-span-center"
                                        onclick='removeAns(this);'></span>
                                </div>
                            </div>
                        </div>
                        <div class="ans-opts col-md-12 p-none" id="1">
                            <div class='col-md-6 pl-none'>
                                <div class='form-group'>
                                    <input type='text' class='form-control'
                                           name="responseList[1].responseOption"
                                           id="responseOptionId1"
                                           required maxlength="150"
                                           onblur="validateForUniqueValue(this,function(){});"
                                           onkeypress="resetValue(this);"/>
                                    <div class='help-block with-errors red-txt'></div>
                                </div>
                            </div>
                            <div class='col-md-3'>
                                <div class="form-group">
                                    <select class='selectpicker wid100'
                                            name="responseList[1].correctAnswer"
                                            id="correctAnswerId1"
                                            required data-error='Please choose one option'>
                                        <option value=''>Select</option>
                                        <option value="true">Yes</option>
                                        <option value="false">No</option>
                                    </select>
                                    <div class='help-block with-errors red-txt'></div>
                                </div>
                            </div>
                            <div class="col-md-3 pl-none">
                                <div class="clearfix"></div>
                                <div class="mt-xs formgroup">
									<span class="addBtnDis addbtn mr-sm align-span-center"
                                          onclick='addAns();'>+</span> <span
                                        class="delete vertical-align-middle remBtnDis hide pl-md align-span-center"
                                        onclick='removeAns(this);'></span>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if
                            test="${fn:length(comprehensionQuestionBo.responseList) gt 0}">
                        <div class="col-md-12 p-none">
                            <div class='col-md-6 pl-none'>
                                <div class="gray-xs-f mb-xs">
                                    Answer Options (1 to 150 characters)<span
                                        class="requiredStar">*</span>
                                </div>
                            </div>
                            <div class='col-md-2'>
                                <div class="gray-xs-f mb-xs">
                                    Correct Answer<span class="requiredStar">*</span>
                                </div>
                            </div>

                            <div class="col-md-4">
                                <div class="gray-xs-f mb-xs">&nbsp;</div>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <c:forEach items="${comprehensionQuestionBo.responseList}"
                                   var="responseBo" varStatus="responseBoVar">
                            <div class="ans-opts col-md-12 p-none"
                                 id="${responseBoVar.index}">
                                <div class='col-md-6 pl-none'>
                                    <div class='form-group'>
                                        <input type='hidden' id="responseId${responseBoVar.index}"
                                               name="responseList[${responseBoVar.index}].id"
                                               value="${responseBo.id}"/>
                                        <input type='text' class='form-control'
                                               name="responseList[${responseBoVar.index}].responseOption"
                                               id="responseOptionId${responseBoVar.index}"
                                               value="${responseBo.responseOption}" required
                                               maxlength="150"
                                               onblur="validateForUniqueValue(this,function(){});"
                                               onkeypress="resetValue(this);"/>
                                        <div class='help-block with-errors red-txt'></div>
                                    </div>
                                </div>
                                <div class='col-md-3'>
                                    <div class="form-group">
                                        <select class='selectpicker wid100' required
                                                data-error='Please choose one option'
                                                name="responseList[${responseBoVar.index}].correctAnswer"
                                                id="correctAnswerId${responseBoVar.index}">
                                            <option value=''>Select</option>
                                            <option value="true"
                                                ${responseBo.correctAnswer ? 'selected':''}>Yes
                                            </option>
                                            <option value="false"
                                                ${responseBo.correctAnswer eq false ? 'selected':''}>
                                                No
                                            </option>
                                        </select>
                                        <div class='help-block with-errors red-txt'></div>
                                    </div>
                                </div>
                                <div class="col-md-3 pl-none">
                                    <div class="clearfix"></div>
                                    <div class="mt-xs formgroup">
										<span class="addBtnDis study-addbtn ml-none"
                                              onclick='addAns();'>+</span> <span
                                            class="delete vertical-align-middle remBtnDis hide pl-md align-span-center"
                                            onclick='removeAns(this);'></span>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
            <div class="clearfix"></div>

            <div>
                <div class="gray-xs-f mb-sm">
                    Choose structure of the correct answer <span class="requiredStar">*</span>
                </div>
                <div class="form-group">
					<span class="radio radio-info radio-inline p-45"> <input
                            type="radio" id="inlineRadio1" value="false"
                            name="structureOfCorrectAns"
                        ${!comprehensionQuestionBo.structureOfCorrectAns ? 'checked' : ''}>
						<label for="inlineRadio1">Any of the ones marked as
							Correct Answers</label>
					</span> <span class="radio radio-inline p-45"> <input type="radio"
                                                                          id="inlineRadio2"
                                                                          value="true"
                                                                          name="structureOfCorrectAns"
                    ${empty comprehensionQuestionBo.structureOfCorrectAns || comprehensionQuestionBo.structureOfCorrectAns ? 'checked' : ''}>
						<label for="inlineRadio2">All the ones marked as Correct
							Answers</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
        </div>
    </form:form>
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
    <!--  End body tab section -->
</div>
<!-- End right Content here -->
<script type="text/javascript">
var idleTime = 0;
  $(document).ready(
      function () {
    	  $(".menuNav li").removeClass('active');
    	  $(".fifthComre").addClass('active');
    	  $("#createStudyId").show();
    	    
        <c:if test="${actionPage eq 'view'}">
        $('#comprehensionFormId input,textarea,select').prop(
            'disabled', true);
        $('#studyLanguage').attr('disabled', false);
        $('.TestQuestionButtonHide').hide();
        $('.addBtnDis, .remBtnDis').addClass('dis-none');
        $('#studyLanguage').removeAttr('disabled');
        </c:if>

        let currLang = $('#studyLanguage').val();
        if (currLang !== undefined && currLang !== null && currLang !== '' && currLang
            !== 'en') {
          $('#currentLanguage').val(currLang);
          refreshAndFetchLanguageData(currLang);
        }

        $("#doneId").on(
            "click",
            function () {
              if (isFromValid("#comprehensionFormId")
                  && validateCorrectAnswers()) {
                validateForUniqueValue('', function (val) {
                  if (val) {
                    $("#comprehensionFormId").submit();
                  }
                });
              }
            });
        $("#saveId").on(
            "click",
            function () {
             autoSaveComprehensionQuestionPage('manual');
            });
        if ($('.ans-opts').length > 2) {
          $(".remBtnDis").removeClass("hide");
        } else {
          $(".remBtnDis").addClass("hide");
        }
                              setInterval(function () {
                                  idleTime += 1;
                                  if (idleTime > 2) { // 5 minutes
                                          autoSaveComprehensionQuestionPage('auto');
                                  }
                              }, 3000); // 5 minutes

                              $(this).mousemove(function (e) {
                                  idleTime = 0;
                              });
                              $(this).keypress(function (e) {
                                  idleTime = 0;
                              });

                              // pop message after 15 minutes
                              if ($('#isAutoSaved').val() === 'true') {
                                  $('#myModal').modal('show');
                                  let i = 2;
                                  setInterval(function () {
                                      $('#autoSavedMessage').text('Last saved was '+i+' minutes ago');
                                      i+=1;
                                  }, 60000);
                              }
      });
      function autoSaveComprehensionQuestionPage(mode) {
          $(".right-content-body").parents("form").validator(
              "destroy");
          $(".right-content-body").parents("form")
              .validator();
          if (mode === 'auto') {
              $("#isAutoSaved").val('true');
          }
          saveComrehensionTestQuestion(mode);
      }


  var ansCount = $(".ans-opts").length - 1;

  function addAns() {
    ansCount = ansCount + 1;
    var newAns = "<div class='ans-opts col-md-12 p-none' id='" + ansCount
        + "'><div class='col-md-6 pl-none'>"
        + "<div class='form-group'>"
        + "<input type='text' class='form-control' required name='responseList["
        + ansCount
        + "].responseOption' id='responseOptionId"
        + ansCount
        + "'  maxlength='150' onblur='validateForUniqueValue(this,function(){});' onkeypress='resetValue(this);'/>"
        + "<div class='help-block with-errors red-txt'></div>"
        + "</div>"
        + "</div>"
        + "<div class='col-md-3'><div class='form-group'>"
        + "<select class='selectpicker' required data-error='Please choose one option' name='responseList["
        + ansCount + "].correctAnswer' id='correctAnswerId" + ansCount + "'>"
        + "<option value=''>Select</option>"
        + "<option value='true'>Yes</option>"
        + "<option value='false'>No</option>"
        + "</select>"
        + "<div class='help-block with-errors red-txt'></div>"
        + "</div>"
        + "</div>"
        + "<div class='col-md-3 pl-none'>"
        + "	<div class='clearfix'></div>"
        + "	<div class='mt-xs form-group'> "
        + "		<span id='ans-btn' class='addBtnDis addbtn mr-sm align-span-center' onclick='addAns();'>+</span>"
        + "		<span class='delete vertical-align-middle remBtnDis hide pl-md align-span-center' onclick='removeAns(this);'></span>"
        + "    </div> " + "</div>" + " </div>" + "</div></div>";
    $(".ans-opts:last").after(newAns);
    $(".ans-opts").parents("form").validator("destroy");
    $(".ans-opts").parents("form").validator();
    if ($('.ans-opts').length > 1) {
      $(".remBtnDis").removeClass("hide");

    } else {
      $('.unitDivParent').find(".remBtnDis").addClass("hide");

    }
    $('.selectpicker').selectpicker('refresh');
    $('#' + ansCount).find('input:first').focus();
  }

  function removeAns(param) {
    ansCount = ansCount - 1;
    $(param).parents(".ans-opts").remove();
    $(".ans-opts").parents("form").validator("destroy");
    $(".ans-opts").parents("form").validator();
    if ($('.ans-opts').length > 2) {
      $(".remBtnDis").removeClass("hide");

    } else {
      $(".remBtnDis").addClass("hide");

    }
  }

  function goToBackPage(item) {
    <c:if test="${actionPage ne 'view'}">
    $(item).prop('disabled', true);
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
          a.href = "/fdahpStudyDesigner/adminStudies/comprehensionQuestionList.do?_S=${param._S}&language="
              + lang;
          document.body.appendChild(a).click();
        } else {
          $(item).prop('disabled', false);
        }
      }
    });
    </c:if>
    <c:if test="${actionPage eq 'view'}">
    var a = document.createElement('a');
    a.href = "/fdahpStudyDesigner/adminStudies/comprehensionQuestionList.do?_S=${param._S}";
    document.body.appendChild(a).click();
    </c:if>
  }

  function saveComrehensionTestQuestion(mode) {
    var comprehensionTestQuestion = new Object();
    var testQuestionId = $("#id").val();
    var studyId = $("#studyId").val();
    var questiontext = $("#questionText").val();
    var structureOfCorrectTxt = $(
        'input[name="structureOfCorrectAns"]:checked').val();
    var questionResponseArray = new Array();
    $('.ans-opts').each(function () {
      var testQuestionResponse = new Object();
      var id = $(this).attr("id");
      let responseId = $("#responseId" + id).val();
      var responseOption = $("#responseOptionId" + id).val();
      var correctAnswer = $("#correctAnswerId" + id).val();
      testQuestionResponse.id = responseId;
      testQuestionResponse.responseOption = responseOption;
      testQuestionResponse.correctAnswer = correctAnswer;
      testQuestionResponse.comprehensionTestQuestionId = testQuestionId;

      questionResponseArray.push(testQuestionResponse);
    });
    comprehensionTestQuestion.id = testQuestionId;
    comprehensionTestQuestion.studyId = studyId;
    comprehensionTestQuestion.questionText = questiontext;
    comprehensionTestQuestion.structureOfCorrectAns = structureOfCorrectTxt;
    comprehensionTestQuestion.responseList = questionResponseArray;
    var formData = new FormData();
    if (studyId != null && studyId != '' && typeof studyId != 'undefined'
        && questiontext != null && questiontext != ''
        && typeof questiontext != 'undefined') {
      formData.append("language", $('#currentLanguage').val());
      if (mode === 'auto') {
          $("#isAutoSaved").val('true');
      }
      formData.append("comprehenstionQuestionInfo", JSON
      .stringify(comprehensionTestQuestion));
      formData.append("isAutoSaved", $("#isAutoSaved").val());
      $
      .ajax({
        url: "/fdahpStudyDesigner/adminStudies/saveComprehensionTestQuestion.do?_S=${param._S}",
        type: "POST",
        datatype: "json",
        data: formData,
        processData: false,
        contentType: false,
        beforeSend: function (xhr, settings) {
          xhr.setRequestHeader("X-CSRF-TOKEN",
              "${_csrf.token}");
        },
        success: function (data) {
          var message = data.message;
          if (message == "SUCCESS") {
            var questionId = data.questionId;
            $("#id").val(questionId);
            $("#alertMsg").removeClass('e-box').addClass(
                's-box').text("Content saved as draft");
            $('#alertMsg').show();
            if (data.isAutoSaved === 'true') {
                $('#myModal').modal('show');
                let i = 2;
                setInterval(function () {
                    $('#autoSavedMessage').text('Last saved was '+i+' minutes ago');
                    i+=1;
                }, 60000);
                $("#isAutoSaved").val('false');
            }
          } else {
            var errMsg = data.errMsg;
            if (errMsg != '' && errMsg != null
                && typeof errMsg != 'undefined') {
              $("#alertMsg").removeClass('s-box')
              .addClass('e-box').text(errMsg);
            } else {
              $("#alertMsg").removeClass('s-box')
              .addClass('e-box').text(
                  "Something went Wrong");
            }
          }
          setTimeout(hideDisplayMessage, 4000);
          $('.fifthComre').find('span').remove();
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
      $('#questionText').validator('destroy').validator();
      if (!$('#questionText')[0].checkValidity()) {
        $("#questionText").parent().addClass('has-error has-danger')
        .find(".help-block").empty().append(
            $("<ul><li> </li></ul>").attr("class",
                "list-unstyled").text(
                "This is a required field."));
      }
    }
  }

  function validateCorrectAnswers() {
    var questionResponseArray = new Array();
    $('.ans-opts').each(function () {
      var id = $(this).attr("id");
      var correctAnswer = $("#correctAnswerId" + id).val();
      questionResponseArray.push(correctAnswer);
    });
    if (questionResponseArray.indexOf("true") != -1) {
      return true;
    } else {
      $('#alertMsg').show();
      $("#alertMsg").removeClass('s-box').addClass('e-box').text(
          "Please select at least one correct answer as yes.");
      setTimeout(hideDisplayMessage, 3000);
      return false;
    }
  }

  function validateForUniqueValue(item, callback) {
    var isValid = true;
    var valueArray = new Array();
    $('.ans-opts')
    .each(
        function () {
          var id = $(this).attr("id");
          var diaplay_value = $("#responseOptionId" + id)
          .val();
          $("#responseOptionId" + id).parent().removeClass(
              "has-danger").removeClass("has-error");
          $("#responseOptionId" + id).parent().find(
              ".help-block").empty();
          if (diaplay_value != '') {
            if (valueArray.indexOf(diaplay_value
            .toLowerCase()) != -1) {
              isValid = false;

              $("#responseOptionId" + id).parent()
              .addClass("has-danger").addClass(
                  "has-error");
              $("#responseOptionId" + id).parent().find(
                  ".help-block").empty();
              $("#responseOptionId" + id)
              .parent()
              .find(".help-block")
              .append(
                  $("<ul><li> </li></ul>")
                  .attr("class",
                      "list-unstyled")
                  .text(
                      "The value should be unique "));
            } else
              valueArray
              .push(diaplay_value.toLowerCase());
          } else {
            $("#responseOptionId" + id).parent().addClass(
                "has-danger").addClass("has-error");
            $("#responseOptionId" + id).parent().find(
                ".help-block").empty();
          }

        });
    callback(isValid);
  }

  function resetValue(item) {
    $(item).parent().addClass("has-danger").addClass("has-error");
    $(item).parent().find(".help-block").empty();
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/comprehensionQuestionPage.do?_S=${param._S}',
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
          $('#inlineRadio1').attr('disabled', true);
          $('#inlineRadio2').attr('disabled', true);
          $('.addBtnDis,.remBtnDis').addClass('cursor-none');
          $('.unitDivParent').children('div.ans-opts').each(function (index, value) {
            let id = 'correctAnswerId' + index;
            $('[data-id=' + id + ']').attr('disabled', true).css('background-color', '#eee').css(
                'opacity', '1');
          });
          $('#questionText').val($('#questionTextLang', htmlData).val());
          $('#responseItems option', htmlData).each(function (index, value) {
            $('#responseOptionId' + index).val(value.getAttribute('value'));
          })
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#inlineRadio1').attr('disabled', false);
          $('#inlineRadio2').attr('disabled', false);
          $('.addBtnDis,.remBtnDis').removeClass('cursor-none');
          $('.unitDivParent').children('div.ans-opts').each(function (index, value) {
            let id = 'correctAnswerId' + index;
            let responseOptionId = 'responseOptionId' + index;
            $('[data-id=' + id + ']').attr('disabled', false).removeAttr('style');
            $('#' + responseOptionId).val($('#' + responseOptionId, htmlData).val());
          });
          $('#questionText').val($('#questionText', htmlData).val());
          
          <c:if test="${actionPage eq 'view'}">
          $('#comprehensionFormId input,textarea').prop('disabled', true);
          $('.remBtnDis').addClass('cursor-none');
          </c:if>
        }
      }
    })
  }
</script>
