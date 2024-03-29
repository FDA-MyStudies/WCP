<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date"/>
<c:set var="tz" value="America/Los_Angeles"/>

<head>
    <meta charset="UTF-8">
    <style nonce="${nonce}">
      .modal-dialog {
        left: -3px !important;
      }

      .col-rc {
        width: 1100px !important;
      }

      #addGroupFormId {
        display: contents !important;
      }

      .text-normal > button > .filter-option {
        text-transform: inherit !important;
      }

      .formula-box {
        height: 50px;
        border: 1px solid #bfdceb;
        border-bottom: 0;
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
    </style>

</head>

<!-- Start right Content here -->
<form:form
        action="/fdahpStudyDesigner/adminStudies/deassignGroup.do?_S=${param._S}"
        name="addGroupFormId" id="addGroupFormId" method="post">
    <input type="hidden" name="language" id="language" value="${currLanguage}">
    <input type="hidden" id="actionType" name="actionType" value="${fn:escapeXml(actionType)}">
    <input type="hidden" id="buttonText" name="buttonText" value="">
    <input type="hidden" value="${groupsBean.action}" id="action" name="action">
    <input type="hidden" value="" id="buttonText" value="${id}" name="buttonText">
    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>

    <div class="col-sm-10 col-rc white-bg p-none">
        <!--  Start top tab section-->
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f text-uppercase dis-line pull-left line34">
                <span class="mr-xs cur-pointer back-page">
                <img src="../images/icons/back-b.png"/></span>
                    Survey List
                </div>
                <c:if test="${studyBo.multiLanguageFlag eq true and actionType != 'add'}">
                    <div class="dis-line form-group mb-none mr-sm wid-150"">
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
                    <div class="dis-line form-group mb-none mr-sm wid-150"">
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
                    <button type="button" class="btn btn-default gray-btn back-page">Cancel
                    </button>
                </div>
            </div>
        </div>
        <!--  End  top tab section-->
        <div class="right-content-body">
            <div>
                <div class="form-group"></div>
                <div class="row form-group">
                    <div class="col-md-6 pl-none" id="deassignId">
                        <div class="study-selected mt-md">
                            <c:forEach items="${groupsAssignedList}" var="groupsAssignedList"
                                       varStatus="loop">
                                <div class="study-selected-item selStd"
                                     id="std${groupsAssignedList.stepId}">
                                    <input type="hidden" class="stdCls"
                                           id="${groupsAssignedList.stepId}" name=""
                                           value="${groupsAssignedList.stepId}"
                                           stdTxt="${groupsAssignedList.description}">
                                    <c:if test="${loop.index < 2}">
										<span class="mr-md close_img tool-tip" data-toggle="tooltip"
                                              data-placement="bottom"
                                              title="Cannot deassign first two steps">
										<img src="/fdahpStudyDesigner/images/icons/close.png"/>
										</span>
                                    </c:if>
                                    <c:if test="${loop.index >= 2}">
                                        	<span class="mr-md close_img"><img
                                                    src="/fdahpStudyDesigner/images/icons/close.png" class="delFunct" id="${groupsAssignedList.stepId}"/></span>
                                                   <%--  onclick="del(${groupsAssignedList.stepId});" --%>
                                    </c:if>
                                    <c:if test="${groupsAssignedList.stepType ne 'Form'}">
                                        <c:forEach items="${groupsAssignedList.description}"
                                                   var="descriptions">
											<span>
                                                    ${descriptions}</span>
                                            <br></c:forEach>
                                    </c:if>
                                    <c:if test="${groupsAssignedList.stepType eq 'Form'}">
                                        <c:if test="${groupsAssignedList.description.size() == 1}">
                                            <c:forEach items="${groupsAssignedList.description}"
                                                       var="descriptions">
											<span>
                                                    ${descriptions}</span>
                                                <br></c:forEach>
                                        </c:if>
                                        <c:if test="${groupsAssignedList.description.size() > 1}">
                                            <div style="margin-left: 30px;margin-top: -20px;">
                                                <c:forEach items="${groupsAssignedList.description}"
                                                           var="descriptions">
											<span>
                                                    ${descriptions}</span>
                                                    <br></c:forEach>
                                            </div>
                                        </c:if>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
            <br>
        </div>
    </div>
    </div>

    <!-- End right Content here -->
</form:form>
<div class="modal fade" id="myModal" role="dialog">
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

<script nonce="${nonce}">
  var idleTime = 0;
  $(document).ready(function () {
    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');
    $("#saveId").click(function () {
      var questnId = $('#questionnaireId').val();
      var groupId = $('#groupId').val();
      var groupName = $('#groupName').val();
      var defaultVisibility = $('#groupDefaultVisibility').val();
      var destinationTrueAsGroup = $('#destinationTrueAsGroup').val();

      let currLang = $('#studyLanguage').val();
      if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
        $('#currentLanguage').val(currLang);
        refreshAndFetchLanguageData(currLang);
      }
      var id = $('#id').val();
      if (groupId != '' && groupId != null && typeof groupId != 'undefined' && groupName != ''
          && groupName != null && typeof groupName != 'undefined') {
        $("#action").val('false');
        $('#id').val();
        $('#groupId').val();
        $("#groupName").val();
        $("#groupDefaultVisibility").val();
        $("#destinationTrueAsGroup").val();
        $("#buttonText").val('save');
        $("#isAutoSaved").val('true');
        $('#addGroupFormId').submit();
      } else {
        $("#alertMsg").removeClass('s-box').addClass('e-box').text(
            "Please fill out this all the mandatory fields");
        $('#alertMsg').show();
      }
      setTimeout(hideDisplayMessage, 4000);
    });

    parentInterval();

    function parentInterval() {
      let timeOutInterval = setInterval(function () {
        idleTime += 1;
        if (idleTime > 3) { // 5 minutes
          clearInterval(timeOutInterval);
          // keepAlive();
          timeOutFunction();
        }
      }, 225000);
    }

    $(this).mousemove(function (e) {
      idleTime = 0;
    });
    $(this).keypress(function (e) {
      idleTime = 0;
    });

    var timeOutInterval;

    function timeOutFunction() {
      $('#myModal').modal('show');
      let i = 14;
      timeOutInterval = setInterval(function () {
        if (i === 0) {
          $('#timeOutMessage').html(
              '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
              + i + ' minutes');
          if ($('#myModal').hasClass('show')) {
            var a = document.createElement('a');
            a.href = "/fdahpStudyDesigner/sessionOut.do";
            document.body.appendChild(a).click();
          }
          clearInterval(timeOutInterval);
        } else {
          if (i === 1) {
            $('#timeOutMessage').html(
                '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
          } else {
            $('#timeOutMessage').html(
                '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
                + i + ' minutes');
          }
          idleTime = 0;
          i -= 1;
        }
      }, 60000);
    }

    $(document).click(function (e) {
      if ($(e.target).closest('#myModal').length) {
        clearInterval(timeOutInterval);
        $('#timeOutMessage').html(
            '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 15 minutes');
        parentInterval();
      }
    });
  });

  $(".back-page").on('click', function () {
  //function goToBackPage(item) {
    var a = document.createElement('a');
    let lang = ($('#studyLanguage').val() !== undefined) ? $('#studyLanguage').val() : '';
    a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}&actionType=${actionType}&language="
        + lang;
    document.body.appendChild(a).click();
  })

  $('#chkSelect').on('change', function (e) {
    if ($(this).is(':checked')) {
      $('#contents').show();
    } else {
      $('#contents').hide();
    }
  });

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    let id =${grpId};
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/deassignGroup.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language,
        id: id
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          updateCompletionTicks(htmlData);
          $('#deassignId').addClass('ml-disabled');
        } else {
          updateCompletionTicksForEnglish();
          $('#deassignId').removeClass('ml-disabled');
        }
      }
    });
  }

  let size =${fn:length(groupsAssignedList)};
  let count = size;

  $('delFunct').on('click', function() {
  //function del(stepId) {
	  let stepId = $(this).attr('id');
    bootbox.confirm({
      message: "This step and following steps will be deassigned. Are you sure?",
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
          if (count > 2) {
            if (size > 2) {
              $.ajax({
                url: "/fdahpStudyDesigner/adminStudies/deassignSteps.do?_S=${param._S}",
                type: "POST",
                datatype: "json",
                data: {
                  stepId: stepId,
                  "${_csrf.parameterName}": "${_csrf.token}",
                },
                success: function (data) {
                  let status = data.message;
                  if (status === "SUCCESS") {
                    let id = '#std' + stepId;
                    $(id).nextAll('div.selStd').remove();
                    $(id).remove();
                    count--;
                  }
                  window.location.reload();
                }
              })
            }
          } else {
            $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                "Group should at least contain 2 steps");
            $('#alertMsg').show();
          }
          setTimeout(hideDisplayMessage, 4000);
        }
      }
    });
  })

  <c:if test="${actionType == 'view'}">
  $('#deassignId').addClass('ml-disabled');
  </c:if>
  <c:if test="${currLanguage eq 'es'}">
  $('#deassignId').addClass('ml-disabled');
  </c:if>

</script>