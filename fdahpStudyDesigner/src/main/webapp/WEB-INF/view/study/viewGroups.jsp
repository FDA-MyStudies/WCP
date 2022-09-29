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
       <div class="black-md-f dis-line pull-left line34">
				<span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
				<img src="../images/icons/back-b.png" class="pr-md"/></span>
            </div>
            <div class="black-md-f text-uppercase dis-line pull-left line34">GROUPS</div>
          <div class="dis-line form-group mb-none">
         <button type="button" class="btn btn-primary blue-btn addOrEditGroups" style="margin-top: 12px;">+ Add Group</button>
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
                  <span class=" ${groupsList.action?'edit-inc':'edit-inc-draft mr-md'} editIcon mr-lg <c:if test="${not empty permission}"> cursor-none </c:if> addOrEditGroups"
                  id="${groupsList.id}" data-toggle="tooltip"
                  	data-placement="top" title="Edit" id="editIcon${groupsList.id}">
                  	</span>
                  <span class="sprites_icon copy delete <c:if test="${not empty permission}"> cursor-none </c:if>"
                  data-toggle="tooltip" data-placement="top" title="Delete" id="${groupsList.id}" onclick=deleteGroup(${groupsList.id});></span>
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
<input type="hidden" name="groupId" id="groupId" value="">
<input type="hidden" name="actionType" id="actionType">
<input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
<input type="hidden" name="chkRefreshflag" value="y">
</form:form>


<form:form
 action="/fdahpStudyDesigner/adminStudies/addOrEditGroupsDetails.do?_S=${param._S}"
 name="addgroupsInfoForm" id="addgroupsInfoForm" method="post">
 <input type="hidden" name="groupId" id="groupId" value="${groupId}">
 <input type="hidden" name="id" id="id" value="${id}">
 <input type="hidden" name="actionType" id="actionType">
 <input type="hidden" name="studyId" id="studyId" value="${studyId}"/>
 <input type="hidden" id="checkRefreshFlag" name="checkRefreshFlag">
 <input type="hidden" name="questionnaireId" id="questionnaireId" value="${questionnaireId}">
</form:form>

<script>
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

  });

function goToBackPage(item) {
          var a = document.createElement('a'); 
              a.href = "/fdahpStudyDesigner/adminStudies/viewQuestionnaire.do?_S=${param._S}";
              document.body.appendChild(a).click();
      }
	$('.addOrEditGroups').on('click',function(){
			$('#id').val($(this).attr('id'));
			$('#checkRefreshFlag').val('Y');
			$('#addgroupsInfoForm').submit();
	});

	function deleteGroup(id) {
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
                      $("#alertMsg").removeClass('e-box').addClass('s-box').text(
                          "Group deleted successfully");
                      $('#alertMsg').show();

                      location.reload();
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

</script>

