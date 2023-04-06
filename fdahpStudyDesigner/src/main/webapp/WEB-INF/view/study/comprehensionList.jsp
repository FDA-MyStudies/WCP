<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<head>
    <meta charset="UTF-8">
    <style>
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

      /* without z-index to avoid collision with multi-language dropdown */
      .right-content-head-wo-z {
        padding: 12px 30px;
        position: relative;
        border-bottom: 1px solid #ddd;
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

      #autoSavedMessage {
        width: 257px;
      }

      #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
        position: relative !important;
        right: -14px !important;
        margin-top: 6% !important;
      }

      #timeOutModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal {
        position: relative !important;
        right: -14px !important;
        margin-top: 6% !important;
      }

      .flr_modal {
        float: right !important;
      }

      .grey_txt {
        color: grey;
        font-size: 15px;
        font-weight: 500;
      }

      .blue_text {
        color: #007CBA !important;
        font-size: 15px;
        font-weight: 500;
      }

      .timerPos {
        position: relative;
        top: -2px;
        right: 2px !important;
      }

      .bold_txt {
        font-weight: 900 !important;
        color: #007cba !important;
        font-size: 15px;
      }

      #comprehensionInfoForm {
        display: contents;
      }

      .right-content-head-wo-z {
        padding: 12px 30px 0px 30px !important;
      }
    </style>
</head>
<script type="text/javascript">
  function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }
</script>
<!-- Start right Content here -->
<!-- ============================================================== -->
<form:form
        action="/fdahpStudyDesigner/adminStudies/consentReview.do?_S=${param._S}"
        name="comprehensionInfoForm" id="comprehensionInfoForm" method="post">
    <div class="col-sm-10 col-rc white-bg p-none">
        <!--  Start top tab section-->
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f text-uppercase dis-line pull-left line34">COMPREHENSION
                    TEST
                </div>

                <c:if test="${studyBo.multiLanguageFlag eq true}">
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

                <div class="dis-line form-group mb-none mr-sm">
                    <button type="button" class="btn btn-default gray-btn cancelBut">Cancel</button>
                </div>
                <c:if test="${empty permission}">
                    <div
                            class="dis-line form-group mb-none mr-sm TestQuestionButtonHide">
                        <button type="button" class="btn btn-default gray-btn" id="saveId">Save
                        </button>
                    </div>
                    <div class="dis-line form-group mb-none">
						<span class="tool-tip" data-toggle="tooltip"
                              data-placement="bottom" id="helpNote"
                                <c:if test="${!markAsComplete && consentBo.needComprehensionTest eq 'Yes'}"> title="Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete." </c:if>>
							<button type="button" class="btn btn-primary blue-btn"
                                    id="markAsCompleteBtnId"
                                    <c:if test="${!markAsComplete && consentBo.needComprehensionTest eq 'Yes'}">disabled</c:if>
                                    onclick="markAsCompleted();">Mark as Completed</button>
						</span>
                    </div>
                </c:if>
            </div>
        </div>
        <!--  End  top tab section-->
        <div class="right-content-head-wo-z">
            <div class="" id="displayTitleId">
                <div class="gray-xs-f mb-xs">
                    Do you need a Comprehension Test for your study? <span
                        class="ct_panel" id="addHelpNote"><small>(Please
							save to continue)</small></span>
                </div>
                <div class="form-group col-md-5 p-none mb-0">
					<span class="radio radio-info radio-inline p-45 pl-1"> <input
                            type="radio" id="comprehensionTestYes" value="Yes"
                            name="needComprehensionTest"
                        ${consentBo.needComprehensionTest eq 'Yes' ? 'checked' : ''}>
						<label for="comprehensionTestYes" class="mb-0">Yes</label>
					</span> <span class="radio radio-inline"> <input type="radio"
                                                                     id="comprehensionTestNo"
                                                                     value="No"
                                                                     name="needComprehensionTest"
                    ${empty consentBo.needComprehensionTest || consentBo.needComprehensionTest eq 'No' ? 'checked' : ''}>
						<label for="comprehensionTestNo" class="mb-0">No</label>
					</span>
                    <div class="help-block with-errors red-txt"></div>
                </div>
            </div>
        </div>
        <!--  Start body tab section -->
        <div
                class="right-content-body pt-none pb-none <c:if test="${empty consentBo.needComprehensionTest || consentBo.needComprehensionTest eq 'No'}">ct_panel</c:if>"
                id="mainContainer">
            <div>
                <table id="comprehension_list" class="display bor-none"
                       cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th><span class="marL10">#</span></th>
                        <th>Question</th>
                        <th><c:if test="${empty permission}">
                            <div class="dis-line form-group mb-none">
                                <button type="button" class="btn btn-primary blue-btn"
                                        id="addQuestionId" onclick="addComphernsionQuestionPage();">
                                    Add
                                    Question
                                </button>
                            </div>
                        </c:if></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${comprehensionTestQuestionList}"
                               var="comprehensionTestQuestion">
                        <tr id="${comprehensionTestQuestion.id}"
                            status="${comprehensionTestQuestion.status}">
                            <td>${comprehensionTestQuestion.sequenceNo}</td>
                            <td class="title">
                                <div class="dis-ellipsis"
                                     title="${fn:escapeXml(comprehensionTestQuestion.questionText)}">${fn:escapeXml(comprehensionTestQuestion.questionText)}</div>
                            </td>
                            <td><span class="sprites_icon preview-g mr-lg"
                                      data-toggle="tooltip" data-placement="top" title="View"
                                      onclick="viewComprehensionQuestion(${comprehensionTestQuestion.id});"></span>
                                <span
                                        class="${comprehensionTestQuestion.status?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-lg <c:if test="${not empty permission}"> cursor-none </c:if>"
                                        onclick="editComprehensionQuestion(${comprehensionTestQuestion.id});"></span>
                                <span
                                        class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                        onclick="deleteComprehensionQuestion(${comprehensionTestQuestion.id});"></span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="mt-xlg" id="displayTitleId">
                <div class="gray-xs-f mb-xs">Minimum score needed to pass</div>
                <div class="form-group col-md-5 p-none scoreClass">
                    <input type="text" id="comprehensionTestMinimumScore"
                           class="form-control" name="comprehensionTestMinimumScore"
                           value="${consentBo.comprehensionTestMinimumScore}" maxlength="3"
                           onkeypress="return isNumber(event)">
                    <div class="help-block with-errors red-txt"></div>
                </div>
                <input type="hidden" name="consentId" id="consentId"
                       value="${consentBo.id}"/>
                <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
                <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
            </div>
        </div>
        <!--  End body tab section -->
    </div>
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <select id="comprehensionLangItems" style="display: none;">
        <c:forEach items="${comprehensionQuestionLangList}" var="comprehensionLang">
            <option id='lang_${comprehensionLang.comprehensionQuestionLangPK.id}'
                    status="${comprehensionLang.status}"
                    value="${comprehensionLang.questionText}">${comprehensionLang.questionText}</option>
        </c:forEach>
    </select>
    <c:if test="${not empty consentBo.id}">
        <input type="hidden" id="studyId" name="studyId"
               value="${consentBo.studyId}">
    </c:if>
    <c:if test="${empty consentBo.id}">
        <input type="hidden" id="studyId" name="studyId" value="${studyId}">
    </c:if>
</form:form>

<div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-sm flr_modal">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-body">
                <div id="autoSavedMessage" class="text-right">
                    <div class="blue_text">Last saved now</div>
                    <div class="grey_txt"><span class="timerPos"><img
                            src="../images/timer2.png"/></span>Your session expires in <span
                            class="bold_txt">15 minutes</span></div>
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
<form:form
        action="/fdahpStudyDesigner/adminStudies/comprehensionQuestionPage.do?_S=${param._S}"
        name="comprehenstionQuestionForm" id="comprehenstionQuestionForm"
        method="post">
    <input type="hidden" name="comprehensionQuestionId"
           id="comprehensionQuestionId" value="">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>
</form:form>
<!-- End right Content here -->
<script type="text/javascript">
  var idleTime = 0;
  $(document).ready(function () {
    $(".menuNav li").removeClass('active');
    $(".fifthComre").addClass('active');
    $("#createStudyId").show();

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    <c:if test="${permission eq 'view'}">
    $('#comprehensionInfoForm input,textarea,select').prop('disabled', true);
    $('.TestQuestionButtonHide').hide();
    $('.addBtnDis, .remBtnDis').addClass('dis-none');
    $('#studyLanguage').removeAttr('disabled');
    </c:if>
    $('input[name="needComprehensionTest"]').change(function () {
      var val = $(this).val();
      $("#addQuestionId").attr("disabled", true);
      if (val == "Yes") {

        $("#mainContainer").show();
        var markAsComplete = "${markAsComplete}"
        if (markAsComplete == "false") {
          $("#markAsCompleteBtnId").attr("disabled", true);
          $("#helpNote").attr('data-original-title',
              'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.');
          $('[data-toggle="tooltip"]').tooltip();
        }
        if (document.getElementById("addQuestionId") != null && document.getElementById(
            "addQuestionId").disabled) {
          $("#addHelpNote").show();
        }
      } else {

        $("#comprehensionTestMinimumScore").val('');
        $("#mainContainer").hide();
        $("#addHelpNote").hide();
        if (document.getElementById("markAsCompleteBtnId") != null && document.getElementById(
            "markAsCompleteBtnId").disabled) {
          $("#markAsCompleteBtnId").attr("disabled", false);
          $("#helpNote").attr('data-original-title', '');
        }
      }
    });
    var viewPermission = "${permission}";

    var reorder = true;
    if (viewPermission == 'view') {
      reorder = false;
    } else {
      reorder = true;
    }
    var table1 = $('#comprehension_list').DataTable({
      "paging": false,
      "info": false,
      "filter": false,
      rowReorder: reorder,
      "columnDefs": [{orderable: false, targets: [0, 1]}],
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        if (viewPermission != 'view') {
          $('td:eq(0)', nRow).addClass("cursonMove dd_icon");
        }
      }
    });
    table1.on('row-reorder', function (e, diff, edit) {
      var oldOrderNumber = '', newOrderNumber = '';
      var result = 'Reorder started on row: ' + edit.triggerRow.data()[1] + '<br>';
      var studyId = $("#studyId").val();
      for (var i = 0, ien = diff.length; i < ien; i++) {
        var rowData = table1.row(diff[i].node).data();
        var r1;
        if (i == 0) {
          r1 = rowData[0];
        }
        if (i == 1) {
          if (r1 > rowData[0]) {
            oldOrderNumber = diff[0].oldData;
            newOrderNumber = diff[0].newData;
          } else {
            oldOrderNumber = diff[diff.length - 1].oldData;
            newOrderNumber = diff[diff.length - 1].newData;
          }

        }
        result += rowData[1] + ' updated to be in position ' +
            diff[i].newData + ' (was ' + diff[i].oldData + ')<br>';
      }

      if (oldOrderNumber !== undefined && oldOrderNumber != null && oldOrderNumber != ""
          && newOrderNumber !== undefined && newOrderNumber != null && newOrderNumber != "") {
        $.ajax({
          url: "/fdahpStudyDesigner/adminStudies/reOrderComprehensionTestQuestion.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            studyId: studyId,
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
              if ($('.fifthComre').find('span').hasClass('sprites-icons-2 tick pull-right mt-xs')) {
                $('.fifthComre').find('span').removeClass('sprites-icons-2 tick pull-right mt-xs');
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
    $("#comprehensionTestMinimumScore").keyup(function () {
      $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
    });
    $("#comprehensionTestMinimumScore").blur(function () {
      $("#comprehensionTestMinimumScore").parent().removeClass("has-danger").removeClass(
          "has-error");
      $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
      var value = $(this).val();
      var questionCount = $("#comprehension_list").find("tbody").find("tr").length;
      if (value != '' && value != null && (value == 0 || parseInt(value) > parseInt(
          questionCount))) {

        $("#comprehensionTestMinimumScore").parent().addClass("has-danger").addClass("has-error");
        $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
        $("#comprehensionTestMinimumScore").parent().find(".help-block").append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "The value should not be more than no of questions or zero"));
      } else {
        $("#comprehensionTestMinimumScore").parent().removeClass("has-danger").removeClass(
            "has-error");
        $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
      }
    });

    parentInterval();

    function parentInterval() {
      let timeOutInterval = setInterval(function () {
        idleTime += 1;
        if (idleTime > 3) { // 5 minutes
          <c:if test="${permission ne 'view'}">
        autoSaveComprehensionList('auto');
        </c:if>
        <c:if test="${permission eq 'view'}">
          clearInterval(timeOutInterval);
          keepAlive();
          timeOutFunction();
        </c:if>
      }
      }, 226000); // 5 minutes
    }

    $(this).mousemove(function (e) {
      idleTime = 0;
    });
    $(this).keypress(function (e) {
      idleTime = 0;
    });

    var timeOutInterval;

    function timeOutFunction() {
      $('#timeOutModal').modal('show');
      let i = 14;
      timeOutInterval = setInterval(function () {
        if (i === 0) {
          $('#timeOutMessage').html(
              '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in '
              + i + ' minutes');
          if ($('#timeOutModal').hasClass('show')) {
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
      if ($(e.target).closest('#timeOutModal').length) {
        clearInterval(timeOutInterval);
        $('#timeOutMessage').html(
            '<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 15 minutes');
        parentInterval();
      }
    });

    $("#saveId").click(function () {
      autoSaveComprehensionList('manual');
    });
    if (document.getElementById("markAsCompleteBtnId") != null && document.getElementById(
        "markAsCompleteBtnId").disabled) {
      $('[data-toggle="tooltip"]').tooltip();
    }

  });

  function deleteComprehensionQuestion(questionId) {
    bootbox.confirm("Are you sure you want to delete this question?", function (result) {
      if (result) {
        var studyId = $("#studyId").val();
        if (questionId != '' && questionId != null && typeof questionId != 'undefined') {
          $.ajax({
            url: "/fdahpStudyDesigner/adminStudies/deleteComprehensionQuestion.do?_S=${param._S}",
            type: "POST",
            datatype: "json",
            data: {
              comprehensionQuestionId: questionId,
              studyId: studyId,
              "${_csrf.parameterName}": "${_csrf.token}",
            },
            success: function deleteConsentInfo(data) {
              var status = data.message;
              if (status == "SUCCESS") {
                $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                    "Question deleted successfully");
                $('#alertMsg').show();
                reloadData(studyId);
              } else {
                $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                    "Unable to delete Question");
                $('#alertMsg').show();
              }
              setTimeout(hideDisplayMessage, 4000);
            },
            error: function (xhr, status, error) {
              $("#alertMsg").removeClass('s-box').addClass('e-box').text(error);
              setTimeout(hideDisplayMessage, 4000);
            }
          });
        }
      }
    });
  }

  function reloadData(studyId) {
    $.ajax({
      url: "/fdahpStudyDesigner/adminStudies/reloadComprehensionQuestionListPage.do?_S=${param._S}",
      type: "POST",
      datatype: "json",
      data: {
        studyId: studyId,
        "${_csrf.parameterName}": "${_csrf.token}",
      },
      success: function status(data, status) {
        var message = data.message;
        if (message == "SUCCESS") {
          reloadComprehensionQuestionDataTable(data.comprehensionTestQuestionList);
        }
      },
      error: function status(data, status) {

      },
    });
  }

  function addComphernsionQuestionPage() {
    $("#comprehensionQuestionId").val('');
    $("#actionType").val('addEdit');
    $("#comprehenstionQuestionForm").submit();
  }

  function editComprehensionQuestion(testQuestionId) {
    if (testQuestionId != null && testQuestionId != '' && typeof testQuestionId != 'undefined') {
      $("#comprehensionQuestionId").val(testQuestionId);
      $("#actionType").val('addEdit');
      $("#comprehenstionQuestionForm").submit();
    }
  }

  function viewComprehensionQuestion(testQuestionId) {
    if (testQuestionId != null && testQuestionId != '' && typeof testQuestionId != 'undefined') {
      $("#comprehensionQuestionId").val(testQuestionId);
      $("#actionType").val('view');
      $("#comprehenstionQuestionForm").submit();
    }
  }

  function reloadComprehensionQuestionDataTable(comprehensionTestQuestionList) {
    $('#comprehension_list').DataTable().clear();
    if (typeof comprehensionTestQuestionList != 'undefined' && comprehensionTestQuestionList != null
        && comprehensionTestQuestionList.length > 0) {
      $.each(comprehensionTestQuestionList, function (i, obj) {
        var datarow = [];
        if (typeof obj.sequenceNo === "undefined" && typeof obj.sequenceNo === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.sequenceNo);
        }
        if (typeof obj.questionText === "undefined" && typeof obj.questionText === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(
              "<div class='dis-ellipsis'>" + DOMPurify.sanitize(obj.questionText) + "</div>");
        }
        var actions = "<span class='sprites_icon preview-g mr-lg' onclick='viewComprehensionQuestion("
            + parseInt(obj.id) + ");'></span>"
            + "<span class='sprites_icon edit-inc editIcon mr-lg' onclick='editComprehensionQuestion("
            + parseInt(obj.id) + ");'>"
            + "</span><span class='sprites_icon copy delete' onclick='deleteComprehensionQuestion("
            + parseInt(obj.id) + ");'>"
            + "</span>";
        datarow.push(actions);
        $('#comprehension_list').DataTable().row.add(datarow);
      });
      $('#comprehension_list').DataTable().draw();
    } else {
      $('#comprehension_list').DataTable().draw();
    }
    if ($('.fifthComre').find('span').hasClass('sprites-icons-2 tick pull-right mt-xs')) {
      $('.fifthComre').find('span').removeClass('sprites-icons-2 tick pull-right mt-xs');
    }
  }

  function markAsCompleted() {
    var table = $('#comprehension_list').DataTable();
    var minimumScore = $("#comprehensionTestMinimumScore").val();
    var needComprehensionTestTxt = $('input[name="needComprehensionTest"]:checked').val();
    if (needComprehensionTestTxt == "Yes") {

      if (!table.data().count()) {
        $('#alertMsg').show();
        $("#alertMsg").removeClass('s-box').addClass('e-box').text("Add atleast one question !");
        setTimeout(hideDisplayMessage, 4000);
      } else if (isFromValid("#comprehensionInfoForm")) {
        saveConsent("Done");
      }
    } else {
      if (isFromValid("#comprehensionInfoForm")) {
        saveConsent("Done");
      }
    }
  }

  function autoSaveComprehensionList(mode) {
    $("#comprehensionTestMinimumScore").trigger('blur');
    $("#comprehensionTestMinimumScore").parents("form").validator("destroy");
    $("#comprehensionTestMinimumScore").parents("form").validator();
    $("#comprehensionTestMinimumScore").parent().removeClass("has-danger").removeClass(
        "has-error");
    $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
    if (mode === 'auto') {
      $("#isAutoSaved").val('true');
    }
    saveConsent('save');
  }

  function saveConsent(type) {
    var consentId = $("#consentId").val();
    var minimumScore = $("#comprehensionTestMinimumScore").val();
    var needComprehensionTestTxt = $('input[name="needComprehensionTest"]:checked').val();
    var studyId = $("#studyId").val();
    var minScoreFlag = true;
    if (studyId != null && studyId != '' && typeof studyId != 'undefined' &&
        needComprehensionTestTxt != null && needComprehensionTestTxt != ''
        && typeof needComprehensionTestTxt != 'undefined') {
      if (type == "save") {
        $("body").addClass("loading");
      }
      var consentInfo = {};
      if (consentId != null && consentId != '' && typeof consentId != 'undefined') {
        consentInfo.id = consentId;
      }
      consentInfo.studyId = studyId;
      consentInfo.comprehensionTestMinimumScore = minimumScore;
      consentInfo.needComprehensionTest = needComprehensionTestTxt;
      if (type == "save") {
        consentInfo.comprehensionTest = "save";
      } else {
        consentInfo.comprehensionTest = "done";
      }

      $("#comprehensionTestMinimumScore").parent().removeClass("has-danger").removeClass(
          "has-error");
      $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
      var value = $('#comprehensionTestMinimumScore').val();
      var questionCount = $("#comprehension_list").find("tbody").find("tr").length;
      if (value != '' && value != null && (value == 0 || parseInt(value) > parseInt(
          questionCount))) {
        minScoreFlag = false;

        $("#comprehensionTestMinimumScore").parent().addClass("has-danger").addClass("has-error");
        $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
        $("#comprehensionTestMinimumScore").parent().find(".help-block").append(
            $("<ul><li> </li></ul>").attr("class", "list-unstyled").text(
                "The value should not be more than no of questions or zero"));
      } else {
        $("#comprehensionTestMinimumScore").parent().removeClass("has-danger").removeClass(
            "has-error");
        $("#comprehensionTestMinimumScore").parent().find(".help-block").empty();
      }
      if (minScoreFlag) {
        var data = JSON.stringify(consentInfo);
        var pageName = 'comprehenstionTest';
        $.ajax({
          url: "/fdahpStudyDesigner/adminStudies/saveConsentReviewAndEConsentInfo.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            consentInfo: data,
            page: pageName,
            language: $('#studyLanguage').val(),
            isAutoSaved: $('#isAutoSaved').val()
          },
          beforeSend: function (xhr, settings) {
            xhr.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
          },
          success: function (data) {
            var message = data.message;
            if (message === "SUCCESS") {
              var consentId = data.consentId;
              $("#consentId").val(consentId);
              $("#addQuestionId").attr("disabled", false);
              $("#addHelpNote").hide();
              if (type !== "save") {
                let lang = ($('#studyLanguage').val() !== undefined) ? $('#studyLanguage').val()
                    : '';
                document.comprehensionInfoForm.action = "/fdahpStudyDesigner/adminStudies/comprehensionTestMarkAsCompleted.do?_S=${param._S}&language="
                    + lang;
                document.comprehensionInfoForm.submit();
              } else {
                $("body").removeClass("loading");
                $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                    "Content saved as draft");
                $('#alertMsg').show();
                if ($('.fifthComre').find('span').hasClass(
                    'sprites-icons-2 tick pull-right mt-xs')) {
                  $('.fifthComre').find('span').removeClass(
                      'sprites-icons-2 tick pull-right mt-xs');
                }
              }
              // pop message after 15 minutes
              if (data.isAutoSaved === 'true') {
                $('#myModal').modal('show');
                let i = 1;
                let j = 14;
                let lastSavedInterval = setInterval(function () {
                  if ((i === 15) || (j === 0)) {
                    $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i
                        + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> '
                        + j + ' minutes</span></div>').css("fontSize", "15px");
                    if ($('#myModal').hasClass('show')) {
                      var a = document.createElement('a');
                      a.href = "/fdahpStudyDesigner/sessionOut.do";
                      document.body.appendChild(a).click();
                    }
                    clearInterval(lastSavedInterval);
                  } else {
                    if ((i === 1) || (j === 14)) {
                      $('#autoSavedMessage').html(
                          '<div class="blue_text">Last saved was 1 minute ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 14 minutes</span></div>').css(
                          "fontSize", "15px");
                    } else if ((i === 14) || (j === 1)) {
                      $('#autoSavedMessage').html(
                          '<div class="blue_text">Last saved was 14 minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> 1 minute</span></div>')
                    } else {
                      $('#autoSavedMessage').html('<div class="blue_text">Last saved was ' + i
                          + ' minutes ago</div><div class="grey_txt"><span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in <span class="bold_txt"> '
                          + j + ' minutes</span></div>').css("fontSize", "15px");
                    }
                    idleTime = 0;
                    i += 1;
                    j -= 1;
                  }
                }, 60000);
                $("#isAutoSaved").val('false');
              }
            } else {
              $("body").removeClass("loading");
              $("#alertMsg").removeClass('s-box').addClass('e-box').text("Something went Wrong");
              $('#alertMsg').show();
            }
            setTimeout(hideDisplayMessage, 4000);
          },
          error: function (xhr, status, error) {
            $("body").removeClass("loading");
            $('#alertMsg').show();
            $("#alertMsg").removeClass('s-box').addClass('e-box').text("Something went Wrong");
            setTimeout(hideDisplayMessage, 4000);
          },
          global: false,
        });
      } else {
        $("body").removeClass("loading");
      }
    }
  }

  $('#studyLanguage').on('change', function () {
    $('#currentLanguage').val($('#studyLanguage').val());
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/comprehensionQuestionList.do?_S=${param._S}',
      type: "GET",
      data: {
        language: language
      },
      success: function (data) {
        let htmlData = document.createElement('html');
        htmlData.innerHTML = data;
        if (language !== 'en') {
          $('td.sorting_1').addClass('sorting_disabled');
          updateCompletionTicks(htmlData);
          $('.tit_wrapper').text($('#mlName', htmlData).val());
          let mark = true;
          $('#comprehensionLangItems option', htmlData).each(function (index, value) {
            let id = '#' + value.getAttribute('id').split('_')[1];
            $(id).find('td.title').text(value.getAttribute('value'));
            if (value.getAttribute('status') === "true") {
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc')) {
                edit.addClass('edit-inc');
              }
              if (edit.hasClass('edit-inc-draft')) {
                edit.removeClass('edit-inc-draft');
              }
            } else {
              mark = false;
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
            }
          })

          if (!mark) {
            $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title',
                'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          $('#addQuestionId, #comprehensionTestYes, #comprehensionTestNo').attr('disabled', true);
          $('#comprehensionTestMinimumScore').attr('disabled', true);
          $('.delete').addClass('cursor-none');
          $('.sorting, .sorting_asc, .sorting_desc').css('pointer-events', 'none');
        } else {
          $('td.sorting_1').removeClass('sorting_disabled');
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          let mark = true;
          $('tbody tr', htmlData).each(function (index, value) {
            let id = '#' + value.getAttribute('id');
            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
            if (value.getAttribute('status') === "true") {
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc')) {
                edit.addClass('edit-inc');
              }
              if (edit.hasClass('edit-inc-draft')) {
                edit.removeClass('edit-inc-draft');
              }
            } else {
              mark = false;
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
            $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title',
                'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          $('#addQuestionId , #comprehensionTestYes, #comprehensionTestNo').attr('disabled', false);
          $('.delete').removeClass('cursor-none');
          $('#comprehensionTestMinimumScore').attr('disabled', false);
          $('.sorting, .sorting_asc, .sorting_desc').removeAttr('style');

          <c:if test="${permission eq 'view'}">
          $('#comprehensionInfoForm input,textarea').prop('disabled', true);
          $('.delete').addClass('cursor-none');
          </c:if>
        }
      }
    });
  }
</script>