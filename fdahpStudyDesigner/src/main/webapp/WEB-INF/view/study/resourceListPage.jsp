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

      .tool-tip {
        display: inline-block;
      }

      .tool-tip [disabled] {
        pointer-events: none;
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
<!-- ============================================================== -->
<!-- Start right Content here -->
<!-- ============================================================== -->
<div class="col-sm-10 col-rc white-bg p-none">
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
            <div class="black-md-f text-uppercase dis-line pull-left line34">
                RESOURCES
                <c:set var="isLive">${_S}isLive</c:set>
                ${not empty  sessionScope[isLive]?'<span class="eye-inc ml-sm vertical-align-text-top"></span>':''}</div>

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
					<span class="tool-tip" data-toggle="tooltip" id="helpNote"
                          data-placement="bottom"
                          <c:if test="${not empty resourcesSavedList}">title="Please ensure individual list items are marked Done, before marking the section as Complete" </c:if>>
						<button type="button" class="btn btn-primary blue-btn"
                                id="markAsComp" onclick="markAsCompleted();"
                                <c:if test="${fn:length(resourcesSavedList) ne 0}">disabled</c:if>>
							Mark as Completed</button>
					</span>
                </div>
            </c:if>
        </div>
    </div>
    <!--  End  top tab section-->
    <!--  Start body tab section -->
    <div class="right-content-body pt-none pb-none">
        <div>
            <table id="resource_list" class="display bor-none tbl_rightalign"
                   cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th><span class="marL10">#</span></th>
                    <th>RESOURCE TITLE</th>
                    <th class="text-right"><c:if test="${empty permission}">
                        <div class="dis-line form-group mb-none mr-sm">

									<span id="studyProSpan" class="tool-tip" data-toggle="tooltip"
                                          data-placement="bottom">
										<button type="button" id="studyProtocolId"
                                                class="btn btn-primary blue-btn"
                                                onclick="addStudyProtocol(${studyProtocolResourceBO.id});">
										Study Protocol</button>
									</span>
                        </div>

                        <div class="dis-line form-group mb-none">
                            <button type="button" id="addResourceId"
                                    class="btn btn-primary blue-btn" onclick="addResource();">Add
                                Resource
                            </button>
                        </div>
                    </c:if></th>

                </tr>
                </thead>
                <tbody>
                <c:forEach items="${resourceBOList}" var="resourceInfo">
                    <tr id="row${resourceInfo.id}" status="${resourceInfo.action}">
                        <td>${resourceInfo.sequenceNo}</td>
                        <td class="wid50 title">${resourceInfo.title}</td>
                        <td class="wid50 text-right"><span
                                class="sprites_icon preview-g mr-lg" data-toggle="tooltip"
                                data-placement="top" title="View" id="viewRes"
                                onclick="viewResourceInfo(${resourceInfo.id});"></span> <span
                                class="${resourceInfo.action?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-lg <c:if test="${not empty permission}"> cursor-none </c:if>"
                                data-toggle="tooltip" data-placement="top" title="Edit"
                                id="editRes" onclick="editResourceInfo(${resourceInfo.id});"></span>
                            <span
                                    class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                                    data-toggle="tooltip" data-placement="top" title="Delete"
                                    id="delRes"
                                    onclick="deleteResourceInfo(${resourceInfo.id});"></span>
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
        action="/fdahpStudyDesigner/adminStudies/addOrEditResource.do?_S=${param._S}"
        name="resourceInfoForm" id="resourceInfoForm" method="post">
    <input type="hidden" name="resourceInfoId" id="resourceInfoId" value="">
    <input type="hidden" name="isstudyProtocol" id="isstudyProtocol"
           value="">
    <input type="hidden" name="language" value="${currLanguage}">
    <input type="hidden" name="actionOn" id="actionOn" value="">
</form:form>
<form:form
        action="/fdahpStudyDesigner/adminStudies/resourceMarkAsCompleted.do?_S=${param._S}"
        name="resourceMarkAsCompletedForm" id="resourceMarkAsCompletedForm"
        method="post">
    <input type="hidden" name="studyId" id="studyId" value="${studyBo.id}"/>
    <input type="hidden" name="language" value="${currLanguage}">
    <input type="hidden" id="mlName" value="${studyLanguageBO.name}"/>
    <input type="hidden" id="customStudyName" value="${fn:escapeXml(studyBo.name)}"/>
    <select id="resourceLangBOList" style="display: none">
        <c:forEach items="${resourceLangBOList}" var="resourceLang">
            <option id='${resourceLang.resourcesLangPK.id}' status="${resourceLang.action}"
                    value="${resourceLang.title}">${resourceLang.title}</option>
        </c:forEach>
    </select>
</form:form>
<script type="text/javascript">
var idleTime = 0;
  var dataTable;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="tooltip"]').tooltip();

    // Fancy Scroll Bar
    $(".menuNav li").removeClass('active');
    $(".eighthResources").addClass('active');
    $("#createStudyId").show();
    $('.eighthResources').removeClass('cursor-none');
    var viewPermission = "${permission}";
    var permission = "${permission}";
    console.log("viewPermission:" + viewPermission);

    var reorder = true;
    if (viewPermission == 'view') {
      reorder = false;
    } else {
      reorder = true;
    }
// 	dataTable = $('#resource_list').DataTable({
// 	    "paging":   false	,
// 	    "order": [],
// 	     rowReorder: reorder,
// 		"columnDefs": [ { orderable: false, targets: [0] } ],
// 	    "info" : false, 
// 	    "lengthChange": false, 
// 	    "searching": false, 
// 	});

    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('[name="language"]').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    var dataTable = $('#resource_list').DataTable({
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

    dataTable.on('row-reorder', function (e, diff, edit) {
      var oldOrderNumber = '', newOrderNumber = '';
      var result = 'Reorder started on row: ' + edit.triggerRow.data()[1] + '<br>';
      var studyId = $("#studyId").val();
      for (var i = 0, ien = diff.length; i < ien; i++) {
        var rowData = dataTable.row(diff[i].node).data();
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
          url: "/fdahpStudyDesigner/adminStudies/reOrderResourceList.do?_S=${param._S}",
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
              reloadResourceDataTable(data.resourceList, null);
              $('#alertMsg').show();
              $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                  "Reorder done successfully.");
              if ($('.eighthResources').find('span').hasClass(
                  'sprites-icons-2 tick pull-right mt-xs')) {
                $('.eighthResources').find('span').removeClass(
                    'sprites-icons-2 tick pull-right mt-xs');
              }
            } else {
              $('#alertMsg').show();
              $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                  "Unable to reorder consent.");
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
              var a = document.createElement('a');
              a.href = "/fdahpStudyDesigner/sessionOut.do";
              document.body.appendChild(a).click();
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

  function deleteResourceInfo(resourceInfoId) {
    $('#delRes').addClass('cursor-none');
    bootbox.confirm("Are you sure you want to delete this resource?", function (result) {
      if (result) {
        var studyId = $("#studyId").val();
        console.log("delete:" + studyId);
        if (resourceInfoId != '' && resourceInfoId != null && typeof resourceInfoId
            != 'undefined') {
          $.ajax({
            url: "/fdahpStudyDesigner/adminStudies/deleteResourceInfo.do?_S=${param._S}",
            type: "POST",
            datatype: "json",
            data: {
              resourceInfoId: resourceInfoId,
              studyId: '${studyId}',
              "${_csrf.parameterName}": "${_csrf.token}",
            },
            success: function deleteConsentInfo(data) {
              var status = data.message;
              var resourceSaved = data.resourceSaved;
              if (status == "SUCCESS") {
// 							dataTable
// 	    			        .row($('#row'+resourceInfoId))
// 	    			        .remove()
// 	    			        .draw();
                if (resourceSaved) {
                  $('#markAsComp').prop('disabled', true);
                  $('[data-toggle="tooltip"]').tooltip();
                } else {
                  $('#markAsComp').prop('disabled', false);
                  $('[data-toggle="tooltip"]').tooltip('dispose');
                }
                $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                    "Resource deleted successfully.");
                $('#alertMsg').show();
                reloadData(studyId);
              } else {
                $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                    "Unable to delete resource.");
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
    $('#delRes').removeClass('cursor-none');
  }

  function reloadData(studyId) {
    $.ajax({
      url: "/fdahpStudyDesigner/adminStudies/reloadResourceListPage.do?_S=${param._S}",
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
          reloadResourceDataTable(data.resourceList, markAsComplete);
        }
      },
      error: function status(data, status) {

      },
    });
  }

  function reloadResourceDataTable(resourceList, markAsComplete) {
    $('#resource_list').DataTable().clear();
    let idList = [];
    if (typeof resourceList != 'undefined' && resourceList != null && resourceList.length > 0) {
      $.each(resourceList, function (i, obj) {
        var datarow = [];
        console.log("obj.studyProtocol:" + obj.studyProtocol);
        if (!obj.studyProtocol) {
          if (typeof obj.sequenceNo === "undefined" && typeof obj.sequenceNo === "undefined") {
            datarow.push(' ');
          } else {
            datarow.push(obj.sequenceNo);
          }
          if (typeof obj.title === "undefined" && typeof obj.title === "undefined") {
            datarow.push(' ');
          } else {
            datarow.push(obj.title);
          }
          var actions = "<span class='sprites_icon preview-g mr-lg' onclick='viewResourceInfo("
              + parseInt(obj.id) + ");'></span>";
          if (obj.status) {
            actions += "<span class='sprites_icon edit-inc editIcon mr-lg' onclick='editResourceInfo("
                + parseInt(obj.id) + ");'></span>"
          } else {
            actions += "<span class='sprites_icon edit-inc-draft editIcon mr-lg' onclick='editResourceInfo("
                + parseInt(obj.id) + ");'></span>";
          }
          actions += "<span class='sprites_icon copy delete' onclick='deleteResourceInfo("
              + parseInt(obj.id) + ");'></span>";
          datarow.push(actions);
          $('#resource_list').DataTable().row.add(datarow);
          // $('#resource_list tr').find('td[1]').addClass("wid50");
          idList.push(obj.id);
        }
      });
      if (typeof markAsComplete != 'undefined' && markAsComplete != null && markAsComplete) {
        $("#markAsComp").attr("disabled", false);
        //$('#helpNote').attr('data-original-title', '');
      }
      $('#resource_list').DataTable().draw();
      $('#resource_list tr').each(function () {
        $(this).find('td:nth-child(2)').addClass("wid50");
        $(this).find('td:last').addClass("wid50 text-right");
      });
    } else {
      $('#resource_list').DataTable().draw();
      //$('#helpNote').attr('data-original-title', 'Please ensure you add one or more Resource Sections before attempting to mark this section as Complete.');
    }
    updateClassName(idList);
  }

  function updateClassName(idList) {
    $('tr.odd,.even').each(function (index) {
      $(this).attr('id', 'row' + idList[index])
      $(this).find('td:eq(1)').attr('class', 'wid50 title');
    })
  }

  function addStudyProtocol(studyProResId) {
    $('#studyProtocolId').prop('disabled', true);
    if (studyProResId != null && studyProResId != '' && typeof studyProResId != 'undefined') {
      $("#resourceInfoId").val(studyProResId);
      $("#actionOn").val("edit");
    } else {
      $("#resourceInfoId").val('');
      $("#actionOn").val("add");
    }
    $("#isstudyProtocol").val('isstudyProtocol');
    $("#resourceInfoForm").submit();
  }

  function addResource() {
    $('#addResourceId').prop('disabled', true);
    $("#resourceInfoId").val('');
    $("#actionOn").val('add');
    $("#resourceInfoForm").submit();
  }

  function editResourceInfo(resourceInfoId) {
    if (resourceInfoId != null && resourceInfoId != '' && typeof resourceInfoId != 'undefined') {
      $('#editRes').addClass('cursor-none');
      $("#resourceInfoId").val(resourceInfoId);
      $("#actionOn").val('edit');
      $("#resourceInfoForm").submit();
    }
  }

  function viewResourceInfo(resourceInfoId) {
    if (resourceInfoId != null && resourceInfoId != '' && typeof resourceInfoId != 'undefined') {
      $('#viewRes').addClass('cursor-none');
      $("#resourceInfoId").val(resourceInfoId);
      $("#actionOn").val('view');
      $("#resourceInfoForm").submit();
    }
  }

  function markAsCompleted() {
    $('#resourceMarkAsCompletedForm').submit();
  }

  function hideDisplayMessage() {
    $('#alertMsg').hide();
  }

  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('[name="language"]').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

  function refreshAndFetchLanguageData(language) {
    let studyProExist = '${studyProtocolResourceBO.id}';
    $.ajax({
      url: '/fdahpStudyDesigner/adminStudies/getResourceList.do?_S=${param._S}',
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
          $('#addResourceId').attr('disabled', true);
          $('.delete, .sorting_1').addClass('cursor-none');
          let mark=true;
          $('#resourceLangBOList option', htmlData).each(function (index, value) {
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
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsComp').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          if (studyProExist == null || studyProExist === '' || typeof studyProExist
              === 'undefined') {
            $('#studyProtocolId').prop('disabled', true);
          } else {
            $('#studyProtocolId').prop('disabled', false);
          }
        } else {
          updateCompletionTicksForEnglish();
          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
          $('#addResourceId').attr('disabled', false);
          $('.delete, .sorting_1').removeClass('cursor-none');
          $('#studyProtocolId').prop('disabled', false);
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
            $('#helpNote').attr('data-original-title', 'Please ensure individual list items on this page are marked Done before attempting to mark this section as Complete.')
          } else {
            $('#markAsComp').removeClass('cursor-none').prop('disabled', false);
            $('#helpNote').removeAttr('data-original-title');
          }
          <c:if test="${not empty permission}">
          $('.delete').addClass('cursor-none');
          </c:if>
        }
      }
    });
  }
</script>