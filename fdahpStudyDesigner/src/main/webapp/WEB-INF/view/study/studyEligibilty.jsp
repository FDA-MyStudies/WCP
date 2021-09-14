<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="com.fdahpstudydesigner.util.SessionObject" %>
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
    </style>
</head>
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <form:form data-toggle="validator"
               action="/fdahpStudyDesigner/adminStudies/saveOrUpdateStudyEligibilty.do?_S=${param._S}"
               id="eleFormId">
        <div class="right-content-head">
            <div class="text-right">
                <div class="black-md-f text-uppercase dis-line pull-left line34">
                    Eligibility
                    <c:set var="isLive">${_S}isLive</c:set>
                        ${not empty  sessionScope[isLive] ? '<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}</div>

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
                    <div class="dis-line form-group mb-none mr-sm">
                        <button type="button" class="btn btn-default gray-btn submitEle"
                                actType="save">Save
                        </button>
                    </div>

                    <div class="dis-line form-group mb-none">
						<span id="spancomId" class="tool-tip" data-toggle="tooltip"
                              data-placement="bottom" data-original-title="">
							<button type="button" class="btn btn-primary blue-btn submitEle"
                                    actType="mark" id="doneBut">Mark as Completed</button>
                    </div>
                </c:if>
            </div>
        </div>
        <!-- End top tab section-->
        <input type="hidden" value="${eligibility.studyId}" name="studyId"
               id="studyId"/>
        <input type="hidden" value="${eligibility.id}" name="id"/>
        <input type="hidden" id="mlInstText" value="${mlInstructionalText}"/>
        <input type="hidden" id="currentLanguage" name="currentLanguage" value="${currLanguage}">
        <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
        <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
        <select id="eligibilityItems" style="display: none">
            <c:forEach items="${eligibilityTestLangList}" var="eligibilityLang">
                <option id='lang_${eligibilityLang.eligibilityTestLangPK.id}'
                        value="${eligibilityLang.question}">${eligibilityLang.question}</option>
            </c:forEach>
        </select>
        <!-- Start body tab section -->
        <div class="right-content-body">
            <div class="mb-xlg form-group" id="eligibilityOptDivId">
                <div class="gray-xs-f mb-sm">
                    Choose the method to be used for ascertaining participant
                    eligibility <small style="display: none;" id="forceContinueMsgId">(Please
                    save to continue)</small>
                </div>
                <span class="radio radio-info radio-inline p-45"> <input
                        type="radio" id="inlineRadio1" value="1" class="eligibilityOptCls"
                        name="eligibilityMechanism" required
                        <c:if test="${eligibility.eligibilityMechanism eq 1}">checked</c:if>
                <c:if test="${liveStatus}"> disabled</c:if>> <label
                        for="inlineRadio1">Token Validation Only</label>
				</span> <span class="radio radio-inline p-45"> <input type="radio"
                                                                      id="inlineRadio2" value="2"
                                                                      class="eligibilityOptCls"
                                                                      name="eligibilityMechanism"
                                                                      required
                                                                      <c:if test="${eligibility.eligibilityMechanism eq 2}">checked</c:if>
            <c:if test="${liveStatus}"> disabled</c:if>> <label
                    for="inlineRadio2">Token Validation and Eligibility Test</label>
				</span> <span class="radio radio-inline"> <input type="radio"
                                                                 id="inlineRadio3" value="3"
                                                                 class="eligibilityOptCls"
                                                                 name="eligibilityMechanism"
                                                                 required
                                                                 <c:if test="${eligibility.eligibilityMechanism eq 3}">checked</c:if>
            <c:if test="${liveStatus}"> disabled</c:if>> <label
                    for="inlineRadio3">Eligibility Test Only</label>
				</span>
                <div class="help-block with-errors red-txt"></div>
            </div>
            <div id="instructionTextDivId"
                 <c:if test="${eligibility.eligibilityMechanism eq 3}">style="display: none;"</c:if>>
                <div class="blue-md-f mb-md text-uppercase">Token Validation</div>
                <div>
                    <div class="gray-xs-f mb-xs">
                        Instruction Text <small>(230 characters max) </small><span
                            class="requiredStar">*</span>
                    </div>
                    <div class="form-group elaborateClass">
						<textarea class="form-control" rows="5" id="comment"
                                  maxlength="230" required
                                  name="instructionalText">${eligibility.instructionalText}</textarea>
                        <div class="help-block with-errors red-txt"></div>
                    </div>
                </div>
            </div>
        </div>
        <div id="eligibilityQusDivId"
             <c:if test="${eligibility.eligibilityMechanism eq 1}">style="display: none;"</c:if>>
            <div class="right-content-head">
                <div class="text-right">
                    <div class="black-md-f  dis-line pull-left line34">Eligibility
                        Test
                    </div>
                    <div class="dis-line form-group mb-none mr-sm">
                        <c:if test="${empty permission}">
                            <button type="button" class="btn btn-primary blue-btn"
                                    id="addQaId">+ Add Question
                            </button>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="right-content-body  pt-none pb-none">
                <table id="consent_list" class="display bor-none" cellspacing="0"
                       width="100%">
                    <thead>
                    <tr>
                        <th><span class="marL10">#</span></th>
                        <th>QA</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set value="true" var="chkDone"/>
                    <c:forEach items="${eligibilityTestList}" var="etQusAns">
                        <tr id="${etQusAns.id}">
                            <td>${etQusAns.sequenceNo}</td>
                            <td class="title"><span class="dis-ellipsis"
                                                    title="${fn:escapeXml(etQusAns.question)}">${etQusAns.question}</span>
                            </td>
                            <td><span class=" preview-g mr-lg viewIcon"
                                      data-toggle="tooltip" data-placement="top" title="View"
                                      etId="${etQusAns.id}"></span> <span
                                    class="${etQusAns.status ? 'edit-inc' : 'edit-inc-draft mr-md'} mr-lg <c:if test="${not empty permission}"> cursor-none </c:if> editIcon"
                                    data-toggle="tooltip" data-placement="top" title="Edit"
                                    etId='${etQusAns.id}'></span> <span
                                    class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if> deleteIcon"
                                    data-toggle="tooltip" data-placement="top" title="Delete"
                                    onclick="deleteEligibiltyTestQusAns('${etQusAns.id}', this);"></span>
                            </td>
                        </tr>
                        <c:if test="${chkDone eq 'true' && not etQusAns.status}">
                            <c:set value="false" var="chkDone"/>
                        </c:if>
                    </c:forEach>
                    <c:if test="${empty eligibilityTestList}">
                        <c:set value="false" var="chkDone"/>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </form:form>
</div>
<form:form
        action="/fdahpStudyDesigner/adminStudies/viewStudyEligibiltyTestQusAns.do?_S=${param._S}"
        id="viewQAFormId">
</form:form>
<script type="text/javascript">
  var viewPermission = "${permission}";
  var permission = "${permission}";
  var chkDone = $
  {
    chkDone
  }
  ;
  var eligibilityMechanism = '${eligibility.eligibilityMechanism}';
  //  	var lastEligibilityOpt = ${not empty lastEligibilityOpt && lastEligibilityOpt ne '1'};
  console.log("viewPermission:" + viewPermission);
  var reorder = true;
  var table1;
  var emVal = $("input[name='eligibilityMechanism']:checked").val();
  var eligibilityTestSize =${eligibilityTestList.size()};
  $(document)
  .ready(
      function () {
        $(".menuNav li.active").removeClass('active');
        $(".menuNav li.fourth").addClass('active');

        <c:if test="${not empty permission}">
        $('#eleFormId input,textarea,select').prop('disabled',
            true);
        $('#eleFormId').find('.elaborateClass').addClass(
            'linkDis');
        </c:if>

        let currLang = $('#studyLanguage').val();
        if (currLang !== undefined && currLang !== null && currLang !== '' && currLang
            !== 'en') {
          $('#currentLanguage').val(currLang);
          refreshAndFetchLanguageData(currLang);
        }

        <c:if test="${empty eligibility.id}">
        $('#addQaId').prop('disabled', true);
        $('.viewIcon, .editIcon, .deleteIcon').addClass('cursor-none');
        </c:if>

        if (emVal != "1") {
          if (eligibilityTestSize === 0) {
            $("#doneBut").attr("disabled", true);
            $('#spancomId').attr('data-original-title',
                'Please ensure you add one or more Eligibility Test before attempting to mark this section as Complete.');
          }
        }

        if ((!chkDone) && eligibilityMechanism != "1") {
          $('#doneBut').prop('disabled', true);
          $('#spancomId')
          .attr(
              'data-original-title',
              'Please ensure individual list items are marked Done, before marking the section as Complete');
          $('[data-toggle="tooltip"]').tooltip();
        }
        initActions();
        $('.submitEle').click(
            function (e) {
              debugger
              e.preventDefault();
              $('#actTy').remove();
              $('<input />').attr('type', 'hidden').attr(
                  'name', "actionType").attr('value',
                  $(this).attr('actType')).attr('id',
                  'actTy').appendTo('#eleFormId');
              if ($(this).attr('actType') == 'save') {
                $('#eleFormId').validator('destroy');
                if (${liveStatus}) {
                  var eligibilityVal = $("input[name='eligibilityMechanism']:checked").val();
                  debugger
                  if (eligibilityVal == 1) {
                    $("#inlineRadio1").prop("disabled", false);
                  } else if (eligibilityVal == 2) {
                    $("#inlineRadio2").prop("disabled", false);
                  } else if (eligibilityVal == 3) {
                    $("#inlineRadio3").prop("disabled", false);
                  }
                }
                $('#eleFormId').submit();
              } else {
                if (isFromValid('#eleFormId')) {
                  if (${liveStatus}) {
                    var eligibilityVal = $("input[name='eligibilityMechanism']:checked").val();
                    if (eligibilityVal == 1) {
                      $("#inlineRadio1").prop("disabled", false);
                    } else if (eligibilityVal == 2) {
                      $("#inlineRadio2").prop("disabled", false);
                    } else if (eligibilityVal == 3) {
                      $("#inlineRadio3").prop("disabled", false);
                    }
                  }
                  $('#eleFormId').submit();
                }
              }
            });

        if (viewPermission == 'view') {
          reorder = false;
        } else {
          reorder = true;
        }
        table1 = $('#consent_list').DataTable(
            {
              "paging": false,
              "info": false,
              "filter": false,
              rowReorder: reorder,
              "columnDefs": [{
                orderable: false,
                targets: [0, 1, 2]
              }],
              "fnRowCallback": function (nRow, aData,
                  iDisplayIndex, iDisplayIndexFull) {
                if (viewPermission != 'view') {
                  $('td:eq(0)', nRow).addClass(
                      "cursonMove dd_icon");
                }
              }
            });

        table1
        .on(
            'row-reorder',
            function (e, diff, edit) {
              var oldOrderNumber = '', newOrderNumber = '';
              var result = 'Reorder started on row: '
                  + edit.triggerRow.data()[1]
                  + '<br>';
              var studyId = $("#studyId").val();
              for (var i = 0, ien = diff.length; i < ien; i++) {
                var rowData = table1.row(
                    diff[i].node).data();
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
                result += rowData[1]
                    + ' updated to be in position '
                    + diff[i].newData
                    + ' (was '
                    + diff[i].oldData
                    + ')<br>';
              }

              if (oldOrderNumber
                  && newOrderNumber) {
                $
                .ajax({
                  url: "/fdahpStudyDesigner/adminStudies/reOrderStudyEligibiltyTestQusAns.do?_S=${param._S}",
                  type: "POST",
                  datatype: "json",
                  data: {
                    eligibilityId: '${eligibility.id}',
                    oldOrderNumber: oldOrderNumber,
                    newOrderNumber: newOrderNumber,
                    "${_csrf.parameterName}": "${_csrf.token}",
                  },
                  success: function consentInfo(
                      data) {
                    var message = data.message;
                    if (message == "SUCCESS") {
                      $(
                          "#alertMsg")
                      .removeClass(
                          'e-box')
                      .addClass(
                          's-box')
                      .text(
                          "Reorder done successfully");
                      $(
                          '#alertMsg')
                      .show();
                      if ($(
                          '.fourth')
                      .find(
                          'span')
                      .hasClass(
                          'sprites-icons-2 tick pull-right mt-xs')) {
                        $(
                            '.fourth')
                        .find(
                            'span')
                        .removeClass(
                            'sprites-icons-2 tick pull-right mt-xs');
                      }
                    } else {
                      $(
                          '#alertMsg')
                      .show();
                      $(
                          "#alertMsg")
                      .removeClass(
                          's-box')
                      .addClass(
                          'e-box')
                      .text(
                          "Unable to reorder consent");
                    }
                    setTimeout(
                        hideDisplayMessage,
                        4000);
                  },
                  error: function (
                      xhr,
                      status,
                      error) {
                    $("#alertMsg")
                    .removeClass(
                        's-box')
                    .addClass(
                        'e-box')
                    .text(
                        error);
                    setTimeout(
                        hideDisplayMessage,
                        4000);
                  }
                });
              }
            });

        $('#eligibilityOptDivId input[type=radio]')
        .change(
            function () {
              if ($(this).val() != '1'
                  && eligibilityMechanism != $(
                      this).val()) {
                $('#forceContinueMsgId').show();
                $('#addQaId').prop('disabled',
                    true);
                $(
                    '.viewIcon, .editIcon, .deleteIcon')
                .addClass('cursor-none');
                if (!chkDone
                    && $(this).val() != '1') {
                  $('#doneBut').prop(
                      'disabled', true);
                  $('#spancomId')
                  .attr(
                      'data-original-title',
                      'Please ensure individual list items are marked Done, before marking the section as Complete');
                }
              } else {
                $('#forceContinueMsgId').hide();
                $('#doneBut, #addQaId').prop(
                    'disabled', false);
                $('#spancomId').attr(
                    'data-original-title',
                    '');
                $(
                    '.viewIcon, .editIcon, .deleteIcon')
                .removeClass(
                    'cursor-none');
                if (!chkDone
                    && $(this).val() != '1') {
                  $('#doneBut').prop(
                      'disabled', true);
                  $('#spancomId')
                  .attr(
                      'data-original-title',
                      'Please ensure individual list items are marked Done, before marking the section as Complete');
                }
              }
              if ($('#inlineRadio1:checked').length > 0) {
                $('#eligibilityQusDivId')
                .slideUp('fast');
                $('#instructionTextDivId')
                .slideDown('fast');
                $('#doneBut').prop('disabled',
                    false);
              } else if ($('#inlineRadio3:checked').length > 0) {
                $('#instructionTextDivId')
                .slideUp('fast');
                $('#eligibilityQusDivId')
                .slideDown('fast');
                if (!chkDone)
                  $('#doneBut').prop(
                      'disabled', true);
              } else {
                $('#eligibilityQusDivId')
                .slideDown('fast');
                $('#instructionTextDivId')
                .slideDown('fast');
                if (!chkDone)
                  $('#doneBut').prop(
                      'disabled', true);
              }

              emVal = $("input[name='eligibilityMechanism']:checked").val();
              eligibilityTestSize =${eligibilityTestList.size()};
              if (emVal != "1") {
                if (eligibilityTestSize === 0) {
                  $("#doneBut").attr("disabled", true);
                  $('#spancomId').attr('data-original-title',
                      'Please ensure you add one or more Eligibility Test before attempting to mark this section as Complete.');
                }
              }

            })
        // 		if(lastEligibilityOpt)
        // 			$('#eligibilityOptDivId input[type=radio]').trigger('change');
      });

  function addOrEditOrViewQA(actionTypeForQuestionPage, eligibilityTestId) {
    var form = $('#viewQAFormId');
    var input = document.createElement("input");
    input.setAttribute('type', "hidden");
    input.setAttribute('name', 'actionTypeForQuestionPage');
    input.setAttribute('value', actionTypeForQuestionPage);
    form.append(input);

    input = document.createElement("input");
    input.setAttribute('type', "hidden");
    input.setAttribute('name', 'language');
    input.setAttribute('value', $('#studyLanguage').val());
    form.append(input);

    input = document.createElement("input");
    input.setAttribute('type', "hidden");
    input.setAttribute('name', 'eligibilityTestId');
    input.setAttribute('value', eligibilityTestId);
    form.append(input);

    input = document.createElement("input");
    input.setAttribute('type', "hidden");
    input.setAttribute('name', 'eligibilityId');
    input.setAttribute('value', "${eligibility.id}");
    form.append(input);

    // 		input = document.createElement("input");
    // 		input.setAttribute('type',"hidden");
    // 		input.setAttribute('name', 'lastEligibilityOpt');
    // 		input.setAttribute('value', $('.eligibilityOptCls:checked').val());
    // 		form.append(input);

    form.submit();
  }

  function deleteEligibiltyTestQusAns(eligibilityTestId, thisAttr) {
    var studyId = $('#studyId').val();
    bootbox
    .confirm(
        "Are you sure you want to delete this eligibility test item?",
        function (result) {
          if (result) {
            if (eligibilityTestId) {
              $
              .ajax({
                url: "/fdahpStudyDesigner/adminStudies/deleteEligibiltyTestQusAns.do?_S=${param._S}",
                type: "POST",
                datatype: "json",
                data: {
                  eligibilityTestId: eligibilityTestId,
                  eligibilityId: '${eligibility.id}',
                  studyId: studyId,
                  "${_csrf.parameterName}": "${_csrf.token}",
                },
                success: function deleteConsentInfo(
                    data) {
                  var status = data.message;
                  if (status == "SUCCESS") {
                    $("#alertMsg")
                    .removeClass(
                        'e-box')
                    .addClass(
                        's-box')
                    .text(
                        "Question deleted successfully");
                    $('#alertMsg').show();
                    if ($('.fifthConsent')
                    .find('span')
                    .hasClass(
                        'sprites-icons-2 tick pull-right mt-xs')) {
                      $('.fifthConsent')
                      .find(
                          'span')
                      .removeClass(
                          'sprites-icons-2 tick pull-right mt-xs');
                    }
                    reloadEligibiltyTestDataTable(data.eligibiltyTestList);
                    if ($('#consent_list tbody tr').length == 1
                        && $('#consent_list tbody tr td').length == 1) {
                      chkDone = false;
                      $('#doneBut').prop(
                          'disabled',
                          true);
                    }
                  } else {
                    $("#alertMsg")
                    .removeClass(
                        's-box')
                    .addClass(
                        'e-box')
                    .text(
                        "Unable to delete Question");
                    $('#alertMsg').show();
                  }
                  setTimeout(
                      hideDisplayMessage,
                      4000);
                },
                error: function (xhr, status,
                    error) {
                  $("#alertMsg").removeClass(
                      's-box').addClass(
                      'e-box')
                  .text(error);
                  setTimeout(
                      hideDisplayMessage,
                      4000);
                }
              });
            }
          }
        });
  }

  function reloadEligibiltyTestDataTable(eligibiltyTestList) {
    $('#consent_list').DataTable().clear();
    if (eligibiltyTestList != null && eligibiltyTestList.length > 0) {
      $
      .each(
          eligibiltyTestList,
          function (i, obj) {
            var datarow = [];
            if (typeof obj.sequenceNo === "undefined"
                && typeof obj.sequenceNo === "undefined") {
              datarow.push(' ');
            } else {
              datarow.push(obj.sequenceNo);
            }
            if (typeof obj.question === "undefined"
                && typeof obj.question === "undefined") {
              datarow.push(' ');
            } else {
              datarow
              .push("<span class='dis-ellipsis' title='" + DOMPurify.sanitize(obj.question) + "'>"
                  + DOMPurify.sanitize(obj.question) + "</span>");
            }
            var actions = '<span class=" preview-g mr-lg viewIcon" data-toggle="tooltip" data-placement="top" title="View" etId="'
                + parseInt(obj.id) + '"></span> '
                + '<span class="'
                + (DOMPurify.sanitize(obj.status) ? "edit-inc"
                    : "edit-inc-draft")
                + ' mr-md mr-lg  editIcon" data-toggle="tooltip" data-placement="top" title="Edit"  etId="'
                + parseInt(obj.id)
                + '"></span>'
                + '<span class="sprites_icon copy delete deleteIcon" data-toggle="tooltip" data-placement="top" title="Delete" onclick="deleteEligibiltyTestQusAns('
                + parseInt(obj.id) + ', this);"></span>';
            //                  var actions = '<span class="sprites_icon preview-g mr-lg viewIcon" data-toggle="tooltip" data-placement="top" title="" data-original-title="View"></span>'+
            //                  '<span class="edit-inc mr-lg  editIcon" data-toggle="tooltip" data-placement="top" title="" etid="15" data-original-title="Edit"></span>'+
            //                  '<span class="sprites_icon copy delete  deleteIcon" data-toggle="tooltip" data-placement="top" title="" onclick="deleteEligibiltyTestQusAns('+15+', this);" data-original-title="Delete"></span>';

            // 				 var actions = "<span class='sprites_icon preview-g mr-lg' onclick='viewConsentInfo("+obj.id+");'></span><span class='sprites_icon edit-g mr-lg' onclick='editConsentInfo("+obj.id+");'></span><span class='sprites_icon copy delete' onclick='deleteConsentInfo("+obj.id+");'></span>";
            datarow.push(actions);
            $('#consent_list').DataTable().row.add(datarow);
          });
      $('#consent_list').DataTable().draw();
      initActions();
    } else {
      $('#consent_list').DataTable().draw();
      $("#doneBut").attr("disabled", true);
      $('#spancomId').attr('data-original-title',
          'Please ensure you add one or more Eligibility Test before attempting to mark this section as Complete.');
    }
  }

  function initActions() {
    $(document).find('#addQaId').click(function () {
      addOrEditOrViewQA("add", "");
    });

    $(document).find('.viewIcon').click(function () {
      addOrEditOrViewQA("view", $(this).attr('etId'));
    });

    $(document).find('.editIcon').click(function () {
      addOrEditOrViewQA("edit", $(this).attr('etId'));
    });
  }

  //multi language feature enable
  $('#studyLanguage').on('change', function () {
    $('#currentLanguage').val($('#studyLanguage').val());
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/viewStudyEligibilty.do?_S=${param._S}',
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
          $('#addQaId, #inlineRadio1, #inlineRadio2, #inlineRadio3').attr('disabled', true);
          $('.sprites_icon').css('pointer-events', 'none');
          $('#eligibilityItems option', htmlData).each(function (index, value) {
            let id = value.getAttribute('id').split('_')[1];
            $('#' + id).find('td.title').text(value.getAttribute('value'));
          })
          if ($('#inlineRadio3').prop('checked')===true) {
            $('#comment').removeAttr('required')
          }
          else {
            $('#comment').val($('#mlInstText', htmlData).val());
          }
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#addQaId, #inlineRadio1, #inlineRadio2, #inlineRadio3').attr('disabled', false);
          $('.sprites_icon').removeAttr('style');
          $('tbody tr', htmlData).each(function (index, value) {
            let id = value.getAttribute('id');
            $('#' + id).find('td.title').text($('#' + id, htmlData).find('td.title').text());
          });
          if ($('#inlineRadio3').prop('checked')===true) {
            $('#comment').removeAttr('required')
          }
          else {
            $('#comment').val($('#comment', htmlData).val());
          }
        }
      }
    });
  }
</script>