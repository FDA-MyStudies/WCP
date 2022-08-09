<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<head>
    <meta charset="UTF-8">
    <style>
      .cursonMove {
        cursor: move !important;
      }

      .sepimgClass {
        position: relative;
      }

      .sorting, .sorting_asc, .sorting_desc {
        background: none !important;
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
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-lg-9 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <div class="right-content-head">

        <select id="consentLangItems" style="display: none">
            <c:forEach items="${consentInfoLangList}" var="consentInfoLang">
                <option id='lang_${consentInfoLang.consentInfoLangPK.id}' status="${consentInfoLang.status}"
                        value="${consentInfoLang.displayTitle}">${consentInfoLang.visualStep}</option>
            </c:forEach>
        </select>

        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">
                Consent Sections

                <c:set var="isLive">${_S}isLive</c:set>${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span> ':''}
                <span>${not empty  sessionScope[isLive]?studyBo.studyVersionBo.consentLVersion:''}</span>
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
            <div class="dis-line form-group mb-none">
                <c:if test="${empty permission}">
					<span class="tool-tip" data-toggle="tooltip"
                          data-placement="bottom" id="helpNote"
                            <c:if test="${fn:length(consentInfoList) eq 0 }"> title="Please ensure you add one or more Consent Sections before attempting to mark this section as Complete." </c:if>
						<c:if test="${!markAsComplete}"> title="Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete." </c:if>>
						<button type="button" class="btn btn-primary blue-btn"
                                id="markAsCompleteBtnId" onclick="markAsCompleted();"
                                <c:if test="${fn:length(consentInfoList) eq 0 || !markAsComplete}">disabled</c:if>>Mark
							as Completed</button>
					</span>
                </c:if>
            </div>
        </div>
    </div>
    <!--  End  top tab section-->
    <!--  Start body tab section -->
    <div class="right-content-body pt-none pb-none">
        <div>
            <table id="consent_list" class="display bor-none" cellspacing="0"
                   width="100%">
                <thead>
                <tr>
                    <th><span class="marL10">#</span></th>
                    <th>Consent Title</th>
                    <th>visual step</th>
                    <th>
                        <div class="dis-line form-group mb-none">
                            <c:if test="${empty permission}">
                                <button type="button" class="btn btn-primary blue-btn"
                                        id="addConsent"
                                        onclick="addConsentPage();">Add Consent Section
                                </button>
                            </c:if>
                        </div>
                    </th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${consentInfoList}" var="consentInfo">
                    <tr id="${consentInfo.id}" status="${consentInfo.status}">
                        <td>${consentInfo.sequenceNo}</td>
                        <td class="title">${consentInfo.displayTitle}</td>
                        <td class="visualStep">${consentInfo.visualStep}</td>
                        <td>
								<span class="sprites_icon preview-g mr-lg"
                                      data-toggle="tooltip" data-placement="top" title="View"
                                      onclick="viewConsentInfo(${consentInfo.id});">
								</span>
                            <span
                                    class="${consentInfo.status?'edit-inc':'edit-inc-draft mr-md'} mr-lg editIcon<c:if test="${not empty permission}"> cursor-none </c:if>"
                                    data-toggle="tooltip" data-placement="top" title="Edit"
                                    onclick="editConsentInfo(${consentInfo.id});">
								</span>
                            <span
                                    class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                    data-toggle="tooltip" data-placement="top" title="Delete"
                                    onclick="deleteConsentInfo(${consentInfo.id});">
								</span>
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
<form:form
        action="/fdahpStudyDesigner/adminStudies/consentInfo.do?_S=${param._S}"
        name="consentInfoForm" id="consentInfoForm" method="post">
    <input type="hidden" name="consentInfoId" id="consentInfoId" value="">
    <input type="hidden" name="actionType" id="actionType">
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
    <input type="hidden" id="currentLanguage" name="language" value="${currLanguage}">
</form:form>
<form:form
        action="/fdahpStudyDesigner/adminStudies/consentMarkAsCompleted.do?_S=${param._S}"
        name="comprehensionInfoForm" id="comprehensionInfoForm" method="post">
    <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
</form:form>
<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>
<script type="text/javascript">
var idleTime = 0;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li").removeClass('active');
    $(".fifthConsent").addClass('active');
    $("#createStudyId").show();
    var viewPermission = "${permission}";
    var permission = "${permission}";

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    var reorder = true;
    if (viewPermission == 'view') {
      reorder = false;
    } else {
      reorder = true;
    }
    var table1 = $('#consent_list').DataTable({
      "paging": false,
      "info": false,
      "filter": false,
      rowReorder: reorder,
      "columnDefs": [{orderable: false, targets: [0, 1, 2]}],
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
          if (parseInt(r1) > parseInt(rowData[0])) {
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
          url: "/fdahpStudyDesigner/adminStudies/reOrderConsentInfo.do?_S=${param._S}",
          type: "POST",
          datatype: "json",
          data: {
            studyId: studyId,
            oldOrderNumber: oldOrderNumber,
            newOrderNumber: newOrderNumber,
            "${_csrf.parameterName}": "${_csrf.token}",
          },
          success: function consentInfo(data) {
            var message = data.message;
            if (message == "SUCCESS") {
              reloadConsentInfoDataTable(data.consentInfoList, null);
              $('#alertMsg').show();
              $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                  "Reorder done successfully");
              if ($('.fifthConsent').find('span').hasClass(
                  'sprites-icons-2 tick pull-right mt-xs')) {
                $('.fifthConsent').find('span').removeClass(
                    'sprites-icons-2 tick pull-right mt-xs');
              }
              if ($('.fifthConsentReview').find('span').hasClass(
                  'sprites-icons-2 tick pull-right mt-xs')) {
                $('.fifthConsentReview').find('span').removeClass(
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

  function deleteConsentInfo(consentInfoId) {
    bootbox.confirm("Are you sure you want to delete this consent item?", function (result) {
      if (result) {
        var studyId = $("#studyId").val();
        if (consentInfoId != '' && consentInfoId != null && typeof consentInfoId != 'undefined') {
          $.ajax({
            url: "/fdahpStudyDesigner/adminStudies/deleteConsentInfo.do?_S=${param._S}",
            type: "POST",
            datatype: "json",
            data: {
              consentInfoId: consentInfoId,
              studyId: studyId,
              "${_csrf.parameterName}": "${_csrf.token}",
            },
            success: function deleteConsentInfo(data) {
              var status = data.message;
              if (status == "SUCCESS") {
                $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                    "Consent Section deleted successfully.");
                $('#alertMsg').show();
                reloadData(studyId);
                if ($('.fifthConsent').find('span').hasClass(
                    'sprites-icons-2 tick pull-right mt-xs')) {
                  $('.fifthConsent').find('span').removeClass(
                      'sprites-icons-2 tick pull-right mt-xs');
                }
                if ($('.fifthConsentReview').find('span').hasClass(
                    'sprites-icons-2 tick pull-right mt-xs')) {
                  $('.fifthConsentReview').find('span').removeClass(
                      'sprites-icons-2 tick pull-right mt-xs');
                }
              } else {
                $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                    "Unable to delete consent");
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
      url: "/fdahpStudyDesigner/adminStudies/reloadConsentListPage.do?_S=${param._S}",
      type: "POST",
      datatype: "json",
      data: {
        studyId: studyId,
        "${_csrf.parameterName}": "${_csrf.token}",
      },
      success: function status(data, status) {
        var message = data.message;
        var markAsComplete = data.markAsComplete;
        if (message == "SUCCESS") {
          reloadConsentInfoDataTable(data.consentInfoList, markAsComplete);
        }
      },
      error: function status(data, status) {

      },
    });
  }

  function reloadConsentInfoDataTable(consentInfoList, markAsComplete) {
    $('#consent_list').DataTable().clear();
    let idList = [];
    if (typeof consentInfoList != 'undefined' && consentInfoList != null && consentInfoList.length
        > 0) {
      $.each(consentInfoList, function (i, obj) {
        var datarow = [];
        if (typeof obj.sequenceNo === "undefined" && typeof obj.sequenceNo === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.sequenceNo);
        }
        if (typeof obj.displayTitle === "undefined" && typeof obj.displayTitle === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.displayTitle);
        }
        if (typeof obj.visualStep === "undefined" && typeof obj.visualStep === "undefined") {
          datarow.push(' ');
        } else {
          datarow.push(obj.visualStep);
        }
        var actions = "<span class='sprites_icon preview-g mr-lg' onclick='viewConsentInfo("
            + parseInt(obj.id) + ");'></span>";
        if (obj.status) {
          actions += "<span class='sprites_icon edit-inc editIcon mr-lg' onclick='editConsentInfo(" + parseInt(
              obj.id) + ");'></span>"
        } else {
          actions += "<span class='sprites_icon edit-inc-draft editIcon mr-lg' onclick='editConsentInfo("
              + parseInt(obj.id) + ");'></span>";
        }
        actions += "<span class='sprites_icon copy delete' onclick='deleteConsentInfo(" + parseInt(
            obj.id) + ");'></span>";
        datarow.push(actions);
        $('#consent_list').DataTable().row.add(datarow);
        idList.push(obj.id);
      });
      if (typeof markAsComplete != 'undefined' && markAsComplete != null && markAsComplete) {
        $("#markAsCompleteBtnId").attr("disabled", false);
        $('#helpNote').attr('data-original-title', '');
      }
      $('#consent_list').DataTable().draw();
    } else {
      $('#consent_list').DataTable().draw();
      $('#helpNote').attr('data-original-title',
          'Please ensure you add one or more Consent Sections before attempting to mark this section as Complete.');
    }
    updateClassName(idList);
  }

  function addConsentPage() {
    $("#consentInfoId").val('');
    $("#actionType").val('addEdit');
    $("#consentInfoForm").submit();
  }

  function markAsCompleted() {
    var table = $('#consent_list').DataTable();
    if (!table.data().count()) {

      $(".tool-tip").attr("title",
          "Please ensure individual list items are marked Done, before marking the section as Complete");
      $('#markAsCompleteBtnId').prop('disabled', true);
      $('[data-toggle="tooltip"]').tooltip();
    } else {
      let input = $("<input>").attr("name", "language").val($('#studyLanguage').val());
      $('#comprehensionInfoForm').append(input);
      $("#comprehensionInfoForm").submit();
    }
  }

  function editConsentInfo(consentInfoId) {

    if (consentInfoId != null && consentInfoId != '' && typeof consentInfoId != 'undefined') {
      $("#consentInfoId").val(consentInfoId);
      $("#actionType").val('addEdit');
      $("#consentInfoForm").submit();
    }
  }

  function viewConsentInfo(consentInfoId) {

    if (consentInfoId != null && consentInfoId != '' && typeof consentInfoId != 'undefined') {
      $("#actionType").val('view');
      $("#consentInfoId").val(consentInfoId);
      $("#consentInfoForm").submit();
    }
  }

  function updateClassName(idList) {
    $('tr.odd,.even').each(function (index) {
      $(this).attr('id', idList[index])
      $(this).find('td:eq(1)').attr('class', 'title');
    })
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/consentListPage.do?_S=${param._S}',
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
          let readyForComplete = true;
          $('#consentLangItems option', htmlData).each(function (index, value) {
            let id = '#'+value.getAttribute('id').split('_')[1];
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
              readyForComplete = false;
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
            }
          })
          if (!readyForComplete) {
            $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          $('.sorting, .sorting_asc, .sorting_desc').css('pointer-events', 'none');
          $('#addConsent').attr('disabled', true);
          $('.delete').addClass('cursor-none');
        } else {
          $('td.sorting_1').removeClass('sorting_disabled');
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          let readyForComplete = true;
          $('tbody tr', htmlData).each(function (index, value) {
            let id = '#'+value.getAttribute('id');
            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
            $(id).find('td.visualStep').text(
                $(id, htmlData).find('td.visualStep').text());
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
              readyForComplete = false;
              let edit = $(id).find('span.editIcon');
              if (!edit.hasClass('edit-inc-draft')) {
                edit.addClass('edit-inc-draft');
              }
              if (edit.hasClass('edit-inc')) {
                edit.removeClass('edit-inc');
              }
            }
          });
          if (!readyForComplete) {
            $('#markAsCompleteBtnId').addClass('cursor-none').prop('disabled', true);
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsCompleteBtnId').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          $('.sorting, .sorting_asc, .sorting_desc').removeAttr('style');
          $('#addConsent').attr('disabled', false);
          $('.delete').removeClass('cursor-none');
          
          if ('${permission}' == 'view') {
        	  $('.delete').addClass('cursor-none');
          }
        }
      }
    });
  }

</script>

<script>
  // Fancy Scroll Bar
  (function ($) {
    $(window).on("load", function () {

      $(".scrollbars").mCustomScrollbar({
        theme: "minimal-dark"
      });
    });
  })(jQuery);
</script>