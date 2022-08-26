<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="UTF-8">
    <style>
      <!--
      .sorting, .sorting_asc, .sorting_desc {
        background: none !important;
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

      -->

      #myModal .modal-dialog, #learnMyModal .modal-dialog .flr_modal{
            position:relative !important;
            right:-14px !important;
            margin-top:6% !important;
            }

            .flr_modal{
            float:right !important;
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

            #timeOutMessage{
            width:257px;
            }
    </style>
</head>
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">Notifications</div>

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
                <div class="dis-line form-group mb-none" id="helpNote"
                     <c:if test="${not empty notificationSavedList}">data-toggle="tooltip"
                     data-placement="bottom"
                     title="Please ensure individual list items are marked Done, before marking the section as Complete"</c:if>>
                    <button type="button"
                            class="btn btn-primary blue-btn markCompleted <c:if test="${not empty notificationSavedList}">linkDis</c:if>"
                            onclick="markAsCompleted();"
                    >Mark as Completed
                    </button>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->
    <!--  Start body tab section -->
    <div class="right-content-body pt-none">
        <div>
            <table id="notification_list" class="display bor-none tbl_rightalign" cellspacing="0"
                   width="100%">
                <thead>
                <tr>
                    <th>Title</th>
                    <th class="linkDis">Status</th>
                    <th class="text-right">
                        <c:if test="${empty permission}">
                            <div class="dis-line form-group mb-none">
                                <button type="button" id="addBtn"
                                        class="btn btn-primary blue-btn hideButtonIfPaused studyNotificationDetails">
                                    Add Notification
                                </button>
                            </div>
                        </c:if>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${notificationList}" var="studyNotification">
                    <tr id="${studyNotification.notificationId}" status="${studyNotification.notificationAction}">
                        <td width="60%" class="title">
                            <div class="dis-ellipsis"
                                 title="${fn:escapeXml(studyNotification.notificationText)}">${fn:escapeXml(studyNotification.notificationText)}</div>
                        </td>
                        <td class="wid20">${studyNotification.checkNotificationSendingStatus}</td>
                        <td class="wid20 text-right">
                            <span class="sprites_icon preview-g mr-lg studyNotificationDetails"
                                  actionType="view"
                                  notificationId="${studyNotification.notificationId}"
                                  data-toggle="tooltip" data-placement="top" title="view"></span>
                            <c:if test="${studyNotification.notificationSent}">
                                <span class="sprites-icons-2 send mr-lg hideButtonIfPaused studyNotificationDetails <c:if test="${not empty permission}"> cursor-none </c:if>"
                                      actionType="resend"
                                      notificationId="${studyNotification.notificationId}"
                                      data-toggle="tooltip" data-placement="top"
                                      title="Resend"></span>
                            </c:if>
                            <c:if test="${not studyNotification.notificationSent}">
                                <span class="${studyNotification.notificationDone?'edit-inc':'edit-inc-draft'} editIcon mr-lg hideButtonIfPaused studyNotificationDetails <c:if test="${not empty permission}"> cursor-none </c:if>"
                                      actionType="edit"
                                      notificationId="${studyNotification.notificationId}"
                                      data-toggle="tooltip" data-placement="top"
                                      title="Edit"></span>
                            </c:if>
                            <span class="sprites_icon copy hideButtonIfPaused studyNotificationDetails <c:if test="${not empty permission}"> cursor-none </c:if>"
                                  actionType="addOrEdit"
                                  notificationText="${fn:escapeXml(studyNotification.notificationText)}"
                                  data-toggle="tooltip" data-placement="top" title="Copy"></span>
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
<form:form action="/fdahpStudyDesigner/adminStudies/getStudyNotification.do?_S=${param._S}"
           id="getStudyNotificationEditPage" name="getNotificationEditPage" method="post">
    <input type="hidden" id="notificationId" name="notificationId">
    <input type="hidden" id="notificationText" name="notificationText">
    <input type="hidden" id="actionType" name="actionType">
    <input type="hidden" id="appId" name="appId">
    <input type="hidden" name="chkRefreshflag" value="y">

    <input type="hidden" name="language" value="${currLanguage}">
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <select id="notificationLangBOList" style="display: none">
        <c:forEach items="${notificationLangBOList}" var="notificationLang">
            <option id='${notificationLang.notificationLangPK.notificationId}' status="${notificationLang.notificationAction}"
                    value="${notificationLang.notificationText}">${notificationLang.notificationText}</option>
        </c:forEach>
    </select>
</form:form>

<form:form action="/fdahpStudyDesigner/adminStudies/studyList.do?_S=${param._S}"
           name="studyListPage" id="studyListPage" method="post">
</form:form>

<form:form action="/fdahpStudyDesigner/adminStudies/notificationMarkAsCompleted.do?_S=${param._S}"
           name="notificationMarkAsCompletedForm" id="notificationMarkAsCompletedForm"
           method="post">
    <input type="hidden" name="language" value="${currLanguage}">
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
    $(".menuNav li").removeClass('active');
    $(".eigthNotification").addClass('active');
    $("#createStudyId").show();
    $('.eigthNotification').removeClass('cursor-none');

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('[name="language"]').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    <c:if test="${studyLive.status eq 'Paused'}">
    $('.hideButtonIfPaused').addClass('dis-none');
    </c:if>

    $('.studyNotificationDetails').on('click', function () {
      var appId = '${appId}';
      $('.studyNotificationDetails').addClass('cursor-none');
      $('#notificationId').val($(this).attr('notificationId'));
      $('#notificationText').val($(this).attr('notificationText'));
      $('#actionType').val($(this).attr('actionType'));
      $('#appId').val(appId);
      $('#getStudyNotificationEditPage').submit();

    });

    var table = $('#notification_list').DataTable({
      "paging": false,
      "order": [],
      "columnDefs": [{orderable: false, orderable: false, targets: [0]}],
      "info": false,
      "lengthChange": false,
      "searching": false,
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
               if ($('#myModal').hasClass('show')) {
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

  function markAsCompleted() {
    $('.markCompleted').prop('disabled', true);
    $("#notificationMarkAsCompletedForm").submit();
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('[name="language"]').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewStudyNotificationList.do?_S=${param._S}',
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
          $('#addBtn').attr('disabled', true);
          $('.copy').addClass('cursor-none');
          $('.sorting, .sorting_asc, .sorting_desc').css('pointer-events', 'none');
          let mark=true;
          $('#notificationLangBOList option', htmlData).each(function (index, value) {
            let id = '#'+value.getAttribute('id');
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
              if ($(id).find('span.editIcon').length>0) {
                mark=false;
              }
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
            let markComplete = $('.markCompleted');
            markComplete.prop('disabled', true);
            if (!markComplete.hasClass('linkDis')) {
              markComplete.addClass('linkDis')
            }
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            let markComplete = $('.markCompleted');
            markComplete.prop('disabled', false);
            if (markComplete.hasClass('linkDis')) {
              markComplete.removeClass('linkDis')
            }
            $('#helpNote').removeAttr('data-original-title');
          }
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#addBtn').attr('disabled', false);
          $('.copy').removeClass('cursor-none');
          $('.sorting, .sorting_asc, .sorting_desc').removeAttr('style');
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
              if ($(id).find('span.editIcon').length>0) {
                mark=false;
              }
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
            let markComplete = $('.markCompleted');
            markComplete.prop('disabled', true);
            if (!markComplete.hasClass('linkDis')) {
              markComplete.addClass('linkDis')
            }
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            let markComplete = $('.markCompleted');
            markComplete.prop('disabled', false);
            if (markComplete.hasClass('linkDis')) {
              markComplete.removeClass('linkDis')
            }
            $('#helpNote').removeAttr('data-original-title');
          }
        }
      }
    });
  }
</script>
