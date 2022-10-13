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
    <!--  Start top tab section-->
    <div class="right-content-head">
        <div class="text-right">
       <div class="black-md-f text-uppercase dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
				<img src="../images/icons/back-b.png" class="pr-md"/></span>
                  <c:if test="${actionType eq 'edit'}">Groups</c:if>
                  <c:if test="${actionType eq 'view'}">View Groups</c:if>
            </div>
          <div class="dis-line form-group mb-none">
          <c:if test="${studyBo.multiLanguageFlag eq true and actionType != 'add'}">
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
            <c:if test="${studyBo.multiLanguageFlag eq true and actionType == 'add'}">
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
             <button type="button" class="btn btn-primary blue-btn addOrEditGroups" value="add">+ Add Group</button>
          </div>
        </div>
    </div>
    <!--  End  top tab section-->
   <!--  Start body tab section -->
    <div class="right-content-body pt-none">
        <div>
            <table id="groups_list" class="display bor-none dragtbl"
                   cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>GROUP ID<span class="sort"></span></th>
                    <th>GROUP NAME<span class="sort"></span></th>
                    <th>ACTION</th>
                </tr>
                </thead>
                <tbody>
                 <c:forEach items="${groupsList}" var="groupsList">
                 <tr id="row${groupsList.groupId}">
                 <td>${groupsList.groupId}</td>
                 <td class="wid50 title">${groupsList.groupName}</td>
                 <td style="width: 200px !important;">
                 <span class="sprites_icon <c:if test="${not empty permission}"> cursor-none </c:if>"
                  id="${groupsList.id}" data-toggle="tooltip" 
                  	data-placement="top" title="Deassign" id="" onclick=deAssignGroups(${groupsList.id});>
                  	<img src="../images/deassign.png" class="pr-md" style="margin-top: -6px;"/>
                  	</span>
                  	<c:choose>
                    <c:when test="${actionType eq 'view'}">
                    <span class="editIcon mr-lg addOrEditGroups <c:if test="${actionType eq 'view'}"> cursor-none </c:if> ${groupsList.action?'edit-inc':'edit-inc-draft mr-md'}"
                    id="${groupsList.id}" data-toggle="tooltip"
                    data-placement="top" title="Edit" id="editIcon${groupsList.id}">
                    </span>
                    </c:when>
                    <c:otherwise>
                    <span class="editIcon mr-lg addOrEditGroups ${groupsList.action?'edit-inc':'edit-inc-draft mr-md'}"
                    id="${groupsList.id}" data-toggle="tooltip"
                    data-placement="top" title="Edit" id="editIcon${groupsList.id}">
                    </span>
                    </c:otherwise>
                    </c:choose>
					<span class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                    data-toggle="tooltip" data-placement="top" title="Delete" id="${groupsList.id}" onclick="deleteGroup(${groupsList.id}, '${groupsList.groupId}');"></span>
                  </td>
                 </tr>
                 </c:forEach>
                  </tbody>
            </table>
        </div>
    </div>
    <!--  End body tab section -->
</div>
<!-- End right Content here -->
<form:form
action="/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}"
name="groupsInfoForm" id="groupsInfoForm" method="post">
<input type="hidden" name="groupId" id="groupId" value="${groupId}">
 <input type="hidden" name="language" value="${currLanguage}">
<input type="hidden" name="actionType" id="actionType" value="${actionType}">
<input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
<input type="hidden" name="chkRefreshflag" value="y">
</form:form>

<form:form
 action="/fdahpStudyDesigner/adminStudies/addOrEditGroupsDetails.do?_S=${param._S}"
 name="addgroupsInfoForm" id="addgroupsInfoForm" method="post">
 <input type="hidden" name="groupId" id="groupId" value="${groupId}">
 <input type="hidden" name="id" id="id" value="${id}">
 <input type="hidden" name="actionOn" id="actionOn" value="">
 <input type="hidden" name="actionType" id="actionType" value="${actionType}">
 <input type="hidden" name="language" id="currentLanguage" value="${currLanguage}">
 <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
 <input type="hidden" id="checkRefreshFlag" name="checkRefreshFlag">
 <input type="hidden" name="questionnaireId" id="questionnaireId" value="${questionnaireId}">
</form:form>
<form:form
             action="/fdahpStudyDesigner/sessionOut.do"
              id="backToLoginPage"
              name="backToLoginPage"
              method="post">
</form:form>

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

<script>
var idleTime = 0;
  $(document).ready(function () {
    $('[data-toggle="tooltip"]').tooltip();
    $(".menuNav li.active").removeClass('active');
    $(".seventhQuestionnaires").addClass('active');
    $('#studyLanguage').removeClass('linkDis');
	
    let currLang = $('#studyLanguage').val();
    if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
      $('#currentLanguage').val(currLang);
      refreshAndFetchLanguageData(currLang);
    }

    $('#groups_list').DataTable({
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
       // $('td:eq(0)', nRow).addClass("dis-none");
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
                      $('#backToLoginPage').submit();
                        }
                        clearInterval(timeOutInterval);
                         } else {
                         if (i === 1) {
                        $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in 1 minute');
                          } else {
                          $('#timeOutMessage').html('<span class="timerPos"><img src="../images/timer2.png"/></span>Your session expires in ' + i +' minutes');
                            }
                            idleTime = 0;
                            i-=1;
                             }
                           }, 60000);
                         }

  });
 
  
  $('#studyLanguage').on('change', function () {
    let currLang = $('#studyLanguage').val();
    $('#currentLanguage').val(currLang);
    refreshAndFetchLanguageData($('#studyLanguage').val());
  })

      function goToBackPage(item) {
      $(item).prop('disabled', true);
      <c:if test="${actionType ne 'view'}">
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
      <c:if test="${actionType eq 'view'}">
      var a = document.createElement('a');
      a.href = "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}";
      document.body.appendChild(a).click();
      </c:if>
      }
	$('.addOrEditGroups').on('click',function(){
			$('#id').val($(this).attr('id'));
			$('#checkRefreshFlag').val('Y');
			$('#addgroupsInfoForm').submit();
	});
	
	function deAssignGroups(id){
    	 var a = document.createElement('a');
         a.href = "/fdahpStudyDesigner/adminStudies/deassignGroup.do?_S=${param._S}&id="
             + id;
         document.body.appendChild(a).click();
    }

	function deleteGroup(id,groupId) {
          bootbox.confirm("Are you sure you want to delete this group?", function (result) {
            if (result) {
              var studyId = $("#studyId").val();
              if (id != '' && id != null && typeof id != 'undefined') {
                $.ajax({
                  url: "/fdahpStudyDesigner/adminStudies/deleteGroup.do?_S=${param._S}",
                  type: "POST",
                  datatype: "json",
                  data: {
                    id: id,
                    studyId: studyId,
                    "${_csrf.parameterName}": "${_csrf.token}",
                  },
                  success: function deleteConsentInfo(data) {
                    var status = data.message;
                    if (status == "SUCCESS") {                     
                    	$('#row'+groupId).remove();
                    	 $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                         "Group deleted successfully");
                     $('#alertMsg').show();
                    } else {
                      $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                          "Unable to delete Group");
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
	
	  function refreshAndFetchLanguageData(language) {
		    $.ajax({
		      url: '/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}',
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
		          $('.addOrEditGroups').attr('disabled', true);
		          $('.delete,thead').addClass('cursor-none');
		          let mark=true;
		          $('#groups_list option', htmlData).each(function (index, value) {
		            let id = '#row' + value.getAttribute('id');
		            $(id).find('td.title').text(value.getAttribute('value'));
	
		          });
		         
		        } else {
		          updateCompletionTicksForEnglish();
		          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
		          $('.addOrEditGroups').attr('disabled', false);
		          $('.delete, thead').removeClass('cursor-none');
		          $('#studyProtocolId').prop('disabled', false);
		          let mark=true;
		          $('tbody tr', htmlData).each(function (index, value) {
		            let id = '#'+value.getAttribute('id');
		            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
		          });
		          
		          <c:if test="${not empty permission}">
		          $('.delete').addClass('cursor-none');
		          </c:if>
		        }
		      }
		    });
		  }
      <c:if test="${actionType eq 'view'}">
       $('.addOrEditGroups').attr('disabled', true);
       $('.delete,thead').addClass('cursor-none');
      </c:if>
</script>

