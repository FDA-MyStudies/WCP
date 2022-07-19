<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<head>
    <meta charset="UTF-8">
    <style>
      .tool-tip {
        display: inline-block;
      }

      .tool-tip [disabled] {
        pointer-events: none;
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
    </style>
</head>
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <input type="hidden" name="studyId" value="${studyBo.id}" id="studyId">
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <select id="questionnaireLangBOS" style="display: none">
        <c:forEach items="${questionnaireLangBOS}" var="questionnaireLang">
            <option id='${questionnaireLang.questionnaireLangPK.id}' status="${questionnaireLang.status}"
                    value="${questionnaireLang.title}">${questionnaireLang.title}</option>
        </c:forEach>
    </select>
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">QUESTIONNAIRES</div>

            <c:if test="${studyBo.multiLanguageFlag eq true}">
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

            <div class="dis-line form-group mb-none">
                <button type="button" class="btn btn-default gray-btn cancelBut">Cancel</button>
            </div>

            <!-- <div class="dis-line form-group mb-none mr-sm">
                         <button type="button" class="btn btn-default gray-btn">Save</button>
                     </div> -->
            <c:if test="${empty permission}">
                <div class="dis-line form-group mb-none ml-sm">
					<span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip"
                          data-placement="bottom"
                            <c:if test="${!markAsComplete}"> title="${activityMsg}" </c:if>>
						<button type="button" class="btn btn-primary blue-btn"
                                id="markAsCompleteBtnId" onclick="markAsCompleted();"
                                <c:if test="${!markAsComplete}"> disabled </c:if>>Mark
							as Completed</button>
					</span>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->


    <!--  Start body tab section -->
    <div class="right-content-body pt-none">
        <div>
            <table id="questionnaire_list" class="display bor-none dragtbl"
                   cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th style="display: none;"></th>
                    <th>TITLE<span class="sort"></span></th>
                    <th>FREQUENCY<span class="sort"></span></th>
                    <th><c:if test="${empty permission}">
                        <div class="dis-line form-group mb-none">
                            <button type="button" class="btn btn-primary blue-btn"
                                    id="addButton" onclick="addQuestionnaires();">Add Questionnaire
                            </button>
                        </div>
                    </c:if></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${questionnaires}" var="questionnaryInfo">
                    <tr id="row${questionnaryInfo.id}" status="${questionnaryInfo.status}">
                        <td>${questionnaryInfo.createdDate}</td>
                        <td class="title">
                            <div class="dis-ellipsis pr-100"
                                 title="${fn:escapeXml(questionnaryInfo.title)}">${questionnaryInfo.title}</div>
                        </td>
                        <td>${questionnaryInfo.frequency == 'Manually Schedule' ? 'Custom Schedule' :questionnaryInfo.frequency}</td>
                        <td style="width: 200px !important;"><span
                                class="sprites_icon preview-g mr-lg" data-toggle="tooltip"
                                data-placement="top" title="View"
                                onclick="viewQuestionnaires(${questionnaryInfo.id});"></span> <span
                                class="${questionnaryInfo.status?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-lg <c:if test="${not empty permission}"> cursor-none </c:if>"
                                data-toggle="tooltip" data-placement="top" title="Edit"
                                onclick="editQuestionnaires(${questionnaryInfo.id});"></span> <span
                                class="sprites_icon copy  mr-lg<c:if test="${not empty permission}"> cursor-none </c:if>"
                                data-toggle="tooltip" data-placement="top" title="Copy"
                                onclick="copyQuestionnaire(${questionnaryInfo.id});"></span> <span
                                class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                data-toggle="tooltip" data-placement="top" title="Delete"
                                onclick="deleteQuestionnaire(${questionnaryInfo.id});"></span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <!--  End body tab section -->
 <div class="modal fade" id="myModal" role="dialog">
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
<form:form
        action="/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}"
        name="questionnaireInfoForm" id="questionnaireInfoForm" method="post">
    <input type="hidden" name="questionnaireId" id="questionnaireId"
           value="">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
</form:form>
<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>

<script>
var idleTime = 0;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $('#questionnaire_list').DataTable({
      "paging": true,
      "abColumns": [
        {"bSortable": true},
        {"bSortable": true},
        {"bSortable": true}
      ],
      "order": [[0, "desc"]],
      "info": false,
      "lengthChange": false,
      "searching": false,
      "pageLength": 10,
      "fnRowCallback": function (nRow, aData, iDisplayIndex, iDisplayIndexFull) {
        $('td:eq(0)', nRow).addClass("dis-none");
      }
    });

    if (document.getElementById("markAsCompleteBtnId") != null && document.getElementById(
        "markAsCompleteBtnId").disabled) {
      $('[data-toggle="tooltip"]').tooltip();
    }

         setInterval(function () {
               idleTime += 1;
                if (idleTime > 3) { // 5 minutes
                timeOutFunction();
                 }
                 }, 226000);

                 $(this).mousemove(function (e) {
                   idleTime = 0;
                 });
                 $(this).keypress(function (e) {
                  idleTime = 0;
                  });

                  function timeOutFunction() {
                  $('#myModal').modal('show');
                   let i = 14;
                   let timeOutInterval = setInterval(function () {
                   if (i === 0) {
                   $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                   if ($('#myModal').hasClass('in')) {
                   $('#backToLoginPage').submit();
                     }
                     clearInterval(timeOutInterval);
                      } else {
                      if (i === 1) {
                     $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
                       } else {
                       $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                         }
                         i-=1;
                          }
                        }, 60000);
                      }
  });

  function editQuestionnaires(questionnaryId) {
    console.log("consentInfoId:" + questionnaryId);
    if (questionnaryId != null && questionnaryId != '' && typeof questionnaryId != 'undefined') {
      $("#actionType").val('edit');
      $("#questionnaireId").val(questionnaryId);
      $("#questionnaireInfoForm").submit();
    }
  }

  function viewQuestionnaires(questionnaryId) {
    console.log("consentInfoId:" + questionnaryId);
    if (questionnaryId != null && questionnaryId != '' && typeof questionnaryId != 'undefined') {
      $("#actionType").val('view');
      $("#questionnaireId").val(questionnaryId);
      $("#questionnaireInfoForm").submit();
    }
  }

  function copyQuestionnaire(questionnaryId) {
    console.log("consentInfoId:" + questionnaryId);
    if (questionnaryId != null && questionnaryId != '' && typeof questionnaryId != 'undefined') {
      $("#questionnaireId").val(questionnaryId);
      $("#actionType").val('edit');
      document.questionnaireInfoForm.action = "/fdahpStudyDesigner/adminStudies/copyQuestionnaire.do?_S=${param._S}";
      document.questionnaireInfoForm.submit();
    }
  }

  function addQuestionnaires() {
    $("#actionType").val('add');
    $("#questionnaireId").val('');
    $("#questionnaireInfoForm").submit();
  }

  function deleteQuestionnaire(questionnaireId) {
    var studyId = $("#studyId").val();
    bootbox.confirm({
      message: "Are you sure you want to delete this questionnaire item? This item will no longer appear on the mobile app or admin portal. Response data already gathered against this item, if any, will still be available on the response database.",
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
          if (questionnaireId != null && questionnaireId != '' && typeof questionnaireId
              != 'undefined') {
            $.ajax({
              url: "/fdahpStudyDesigner/adminStudies/deleteQuestionnaire.do?_S=${param._S}",
              type: "POST",
              datatype: "json",
              data: {
                questionnaireId: questionnaireId,
                studyId: studyId,
                "${_csrf.parameterName}": "${_csrf.token}",
              },
              success: function deleteConsentInfo(data) {
                var status = data.message;
                var markAsComplete = data.markAsComplete;
                var activityMsg = data.activityMsg;
                if (status == "SUCCESS") {
                  $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                      "Questionnaire deleted successfully");
                  $('#alertMsg').show();
                  var questionnaireList = data.questionnaireList;
                  reloadDataTabel(questionnaireList);
                  if ($('.seventhQuestionnaires').find('span').hasClass(
                      'sprites-icons-2 tick pull-right mt-xs')) {
                    $('.seventhQuestionnaires').find('span').removeClass(
                        'sprites-icons-2 tick pull-right mt-xs');
                  }
                  if (!markAsComplete) {
                    $('#markAsCompleteBtnId').prop('disabled', true);
                    $('#markAsTooltipId').attr("data-original-title", activityMsg);
                  } else {
                    $('#markAsCompleteBtnId').prop('disabled', false);
                    $('#markAsTooltipId').removeAttr('data-original-title');
                  }
                } else {
                  if (status == 'FAILUREanchorused') {
                    $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                        "Questionnaire already live anchorbased.unable to delete");
                  } else {
                    $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                        "Unable to delete consent");

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
          }
        }
      }
    });
  }

  function reloadDataTabel(questionnaireList) {
    $('#questionnaire_list').DataTable().clear();
    if (typeof questionnaireList != 'undefined' && questionnaireList != null
        && questionnaireList.length > 0) {
      $.each(questionnaireList, function (i, obj) {
        var datarow = [];
        if (typeof obj.createdDate === "undefined" && typeof obj.createdDate === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.createdDate);
        }
        if (typeof obj.title === "undefined" && typeof obj.title === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(
              '<div class="dis-ellipsis pr-100" title=' + DOMPurify.sanitize(obj.title) + '>'
              + DOMPurify.sanitize(obj.title) + '</div>');
        }
        if (typeof obj.frequency === "undefined" && typeof obj.frequency === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.frequency);
        }
        var actionDiv = "<span class='sprites_icon preview-g mr-lg' data-toggle='tooltip' data-placement='top' title='View' onclick='viewQuestionnaires("
            + parseInt(obj.id) + ");'></span>";
        if (obj.status) {
          actionDiv += "<span class='sprites_icon edit-inc editIcon mr-lg' data-toggle='tooltip' data-placement='top' title='Edit' onclick='editQuestionnaires("
              + parseInt(obj.id) + ");'></span>";
        } else {
          actionDiv += "<span class='edit-inc-draft editIcon mr-md mr-lg' data-toggle='tooltip' data-placement='top' title='Edit' onclick='editQuestionnaires("
              + parseInt(obj.id) + ");'></span>";
        }
        actionDiv += "<span class='sprites_icon copy  mr-lg' data-toggle='tooltip' data-placement='top' title='Copy' onclick='copyQuestionnaire("
            + parseInt(obj.id) + ");'></span>";

        actionDiv += "<span class='sprites_icon copy delete' data-toggle='tooltip' data-placement='top' title='Delete' onclick='deleteQuestionnaire("
            + parseInt(obj.id) + ");'></span>";
        datarow.push(actionDiv);
        $('#questionnaire_list').DataTable().row.add(datarow);
      });
      $('#questionnaire_list').DataTable().draw();
    } else {
      $('#questionnaire_list').DataTable().draw();
      $("#markAsCompleteBtnId").prop("disabled", false);
      $("#markAsTooltipId").removeAttr('data-original-title');
    }
    $('[data-toggle="tooltip"]').tooltip();
  }

  function markAsCompleted() {
    document.questionnaireInfoForm.action = "/fdahpStudyDesigner/adminStudies/questionnaireMarkAsCompleted.do?_S=${param._S}";
    document.questionnaireInfoForm.submit();
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

function refreshAndFetchLanguageData(language) {
	$.ajax({
		url: '/fdahpStudyDesigner/adminStudies/viewStudyQuestionnaires.do?_S=${param._S}',
		type: "GET",
		data: {
			language: language
		},
		success: function (data) {
          let htmlData = document.createElement('html');
          htmlData.innerHTML = data;
			if (language !== 'en') {
              updateCompletionTicks(htmlData);
              $('.tit_wrapper').text($('#mlName', htmlData).val());
				$('#addButton').attr('disabled', true);
				$('.sorting').css('pointer-events', 'none');
				$('.delete ').addClass('cursor-none');
				$('.copy ').addClass('cursor-none');
				let mark =true;
              $('#questionnaireLangBOS option', htmlData).each(function (index, value) {
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
                  mark =false;
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
                $('#markAsTooltipId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
              } else {
                $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
                $('#markAsTooltipId').removeAttr('data-original-title');
              }
			} else {
              updateCompletionTicksForEnglish();
              $('.tit_wrapper').text($('#customStudyName', htmlData).val());
				$('#addButton').attr('disabled', false);
				$('.delete ').removeClass('cursor-none');
				$('.copy ').removeClass('cursor-none');	
				$('.sorting').removeAttr('style');
				<c:if test="${not empty permission}">
				$('.delete, .copy').addClass('cursor-none');
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
                $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
                $('#markAsTooltipId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
              } else {
                $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
                $('#markAsTooltipId').removeAttr('data-original-title');
              }
			}
		}
	});
}
</script>

