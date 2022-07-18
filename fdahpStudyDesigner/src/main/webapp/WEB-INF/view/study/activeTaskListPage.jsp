<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
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

    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">ACTIVE TASKS</div>

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
                <div class="dis-line form-group mb-none">
						<span id="spancomId" class="tool-tip" data-toggle="tooltip"
                              data-placement="bottom"
                                <c:if test="${!markAsComplete}"> title="${activityMsg}" </c:if> >
							<button type="button" class="btn btn-primary blue-btn"
                                    id="markAsComp" onclick="markAsCompleted();"
                                    <c:if test="${!markAsComplete}">disabled</c:if>>
								Mark as Completed</button>
						</span>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->


    <!--  Start body tab section -->
    <div class="right-content-body pt-none">
        <div>
            <table id="activedatatable_list" class="display bor-none dragtbl" cellspacing="0"
                   width="100%">
                <thead>
                <tr>
                    <th style="display: none;"></th>
                    <th>TITLE<span class="sort"></span></th>
                    <th>TYPE<span class="sort"></span></th>
                    <th>FREQUENCY<span class="sort"></span></th>
                    <th>
                        <div class="dis-line form-group mb-none">
                            <c:if test="${empty permission}">
                                <button type="button" class="btn btn-primary blue-btn" id="addBtn"
                                        onclick="addActiveTaskPage();">Add Active Task
                                </button>
                            </c:if>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${activeTasks}" var="activeTasksInfo">
                    <tr id="row${activeTasksInfo.id}" status="${activeTasksInfo.action}">
                        <td style="display: none;">${activeTasksInfo.createdDate}</td>
                        <td class="title">
                            <div class="dis-ellipsis pr-100"
                                 title="${fn:escapeXml(activeTasksInfo.displayName)}">${activeTasksInfo.displayName}</div>
                        </td>
                        <td>${activeTasksInfo.type}</td>
                        <td>${activeTasksInfo.frequency  == 'Manually Schedule' ? 'Custom Schedule' : activeTasksInfo.frequency}</td>
                        <td>
                            <span class="sprites_icon preview-g mr-lg" data-toggle="tooltip"
                                  data-placement="top" title="View"
                                  onclick="viewTaskInfo(${activeTasksInfo.id});"></span>
                            <span class="${activeTasksInfo.action?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-lg <c:if test="${not empty permission}"> cursor-none </c:if>"
                                  data-toggle="tooltip" data-placement="top" title="Edit"
                                  id="editTask"
                                  onclick="editTaskInfo(${activeTasksInfo.id});"></span>
                            <span class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                  data-toggle="tooltip" data-placement="top" title="Delete"
                                  id="delTask"
                                  onclick="deleteTaskInfo(${activeTasksInfo.id});"></span>
                        </td>
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
<form:form action="/fdahpStudyDesigner/adminStudies/viewActiveTask.do?_S=${param._S}"
           name="activeTaskInfoForm" id="activeTaskInfoForm" method="post">
    <input type="hidden" name="activeTaskInfoId" id="activeTaskInfoId" value="">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" name="studyId" id="studyId" value="${studyBo.id}"/>
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
    <select id="activeTaskLangItems" style="display: none">
        <c:forEach items="${activeTaskLangBOS}" var="activeTaskLang">
            <option id='${activeTaskLang.activeTaskLangPK.id}' status="${activeTaskLang.status}"
                    value="${activeTaskLang.displayName}">${activeTaskLang.displayName}</option>
        </c:forEach>
    </select>
</form:form>
<form:form action="/fdahpStudyDesigner/adminStudies/activeTAskMarkAsCompleted.do?_S=${param._S}"
           name="completeInfoForm" id="completeInfoForm" method="post">
    <input type="hidden" name="studyId" id="studyId" value="${studyBo.id}"/>
</form:form>
<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>
<c:set var="studyId">${_S}studyId</c:set>
<script>
  var idleTime = 0;
  var dataTable;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li.active").removeClass('active');
    $(".seventhTask").addClass('active');

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    } else {
      $('#currentLanguage').val('en');
    }

    // Fancy Scroll Bar
    /*  $(".left-content").niceScroll({cursorcolor:"#95a2ab",cursorborder:"1px solid #95a2ab"});
     $(".right-content-body").niceScroll({cursorcolor:"#d5dee3",cursorborder:"1px solid #d5dee3"}); */
    $('[data-toggle="tooltip"]').tooltip();
    dataTable = $('#activedatatable_list').DataTable({
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
      "pageLength": 10
    });

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

  function addActiveTaskPage() {
    $("#actionType").val('add');
    $("#activeTaskInfoId").val('');
    $("#activeTaskInfoForm").submit();
  }

  function viewTaskInfo(taskInfoId) {
    if (taskInfoId != null && taskInfoId != '' && typeof taskInfoId != 'undefined') {
      $("#actionType").val('view');
      $("#activeTaskInfoId").val(taskInfoId);
      $("#activeTaskInfoForm").submit();
    }
  }

  function editTaskInfo(taskInfoId) {
    if (taskInfoId != null && taskInfoId != '' && typeof taskInfoId != 'undefined') {
      $('#editTask').addClass('cursor-none');
      $("#actionType").val('addEdit');
      $("#activeTaskInfoId").val(taskInfoId);
      $("#activeTaskInfoForm").submit();
    }
  }

  function deleteTaskInfo(activeTaskInfoId) {
    $('#delTask').addClass('cursor-none');
    bootbox.confirm({
      message: "Are you sure you want to delete this active task item? This item will no longer appear on the mobile app or admin portal. Response data already gathered against this item, if any, will still be available on the response database.",
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
          if (activeTaskInfoId != '' && activeTaskInfoId != null && typeof activeTaskInfoId
              != 'undefined') {
            $.ajax({
              url: "/fdahpStudyDesigner/adminStudies/deleteActiveTask.do?_S=${param._S}",
              type: "POST",
              datatype: "json",
              data: {
                activeTaskInfoId: activeTaskInfoId,
                language: $('#studyLanguage').val(),
                studyId: '${sessionScope[studyId]}',
                "${_csrf.parameterName}": "${_csrf.token}",
              },
              success: function deleteActiveInfo(data) {
                var status = data.message;
                var markAsComplete = data.markAsComplete;
                var activityMsg = data.activityMsg;
                if (status == "SUCCESS") {
                  dataTable
                  .row($('#row' + activeTaskInfoId))
                  .remove()
                  .draw();
                  if (!markAsComplete) {
                    $('#markAsComp').prop('disabled', true);
                    //$('[data-toggle="tooltip"]').tooltip();
                    $('#spancomId').attr("data-original-title", activityMsg);
                  } else {
                    $('#markAsComp').prop('disabled', false);
                    //$('[data-toggle="tooltip"]').tooltip('destroy');
                    $('#spancomId').removeAttr('data-original-title');
                  }
                  $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                      "ActiveTask deleted successfully");
                  $('#alertMsg').show();
                  /* reloadData(studyId); */
                  if ($('.seventhTask').find('span').hasClass(
                      'sprites-icons-2 tick pull-right mt-xs')) {
                    $('.seventhTask').find('span').removeClass(
                        'sprites-icons-2 tick pull-right mt-xs');
                  }
                } else {
                  $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                      "Unable to delete resource");
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
    $('#delTask').removeClass('cursor-none');
  }

  function markAsCompleted() {
        let input = $("<input>").attr("name", "language").val($('#studyLanguage').val());
          $('#completeInfoForm').append(input);
          $("#completeInfoForm").submit();
  }
  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewStudyActiveTasks.do?_S=${param._S}',
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
          let mark=true;
          $('#activeTaskLangItems option', htmlData).each(function (index, value) {
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
            $('#markAsComp').addClass('cursor-none').prop('disabled', true);
            $('#spancomId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsComp').removeClass('cursor-none').prop('disabled', false);
            $('#spancomId').removeAttr('data-original-title');
          }
          $('#addBtn').attr('disabled', true);
          $('.sorting').css('pointer-events', 'none');
          $('.delete').addClass('cursor-none');
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
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
            $('#markAsComp').addClass('cursor-none').prop('disabled', true);
            $('#spancomId').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsComp').removeClass('cursor-none').prop('disabled', false);
            $('#spancomId').removeAttr('data-original-title');
          }
          $('#addBtn').attr('disabled', false);
          $('.sorting').removeAttr('style');
          $('.delete').removeClass('cursor-none');
          
          <c:if test="${not empty permission}">
          $('#delTask').addClass('cursor-none');
          </c:if>
        }
      }
    });
  }
</script>     
        
        