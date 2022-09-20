<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="date" class="java.util.Date" />
<c:set var="tz" value="America/Los_Angeles" />

<head>
<meta charset="UTF-8">
<style>
.modal-dialog {
left: -3px !important;
}
.col-rc {
width:1100px !important;
}
#addGroupFormId{
display:contents !important;
}
</style>

</head>

<!-- Start right Content here -->
<form:form
action="/fdahpStudyDesigner/adminStudies/addOrUpdateGroupsDetails.do?_S=${param._S}"
name="addGroupFormId" id="addGroupFormId" method="post">

          <div class="col-sm-10 col-rc white-bg p-none">
            <!--  Start top tab section-->
            <!-- <div class="right-content-head"><div class="text-right"><div class="black-md-f dis-line pull-left line34"><span class="mr-sm"><a href="#"><img src="images/icons/back-b.png"/></a></span> Group
                    </div></div></div> -->
            <div class="right-content-head">
              <div class="text-right">
           <div class="black-md-f dis-line pull-left line34">
                <span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
                <img src="../images/icons/back-b.png" class="pr-md"/></span>
                Group-Level Attributes
            </div>
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                  <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip" data-placement="bottom" title="Language selection is available in edit screen only">
                    <select class="selectpicker aq-select aq-select-form studyLanguage langSpecific" title="Select" disabled>
                      <option selected>English</option>
                    </select>
                  </span>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn" onclick="goToBackPage(this);">Cancel </button>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn" id="saveId">Save </button>
                </div>
                <div class="dis-line form-group mb-none">
                  <span class="tool-tip" data-toggle="tooltip" data-placement="bottom" id="helpNote"></span>
                  <button type="button" class="btn btn-primary blue-btn" id="saveId">Done</button>
                  </span>
                </div>
              </div>
            </div>
            <!--  End  top tab section-->
            <!--  Start body tab section -->
            <div class="right-content-body">
              <div>
                <div class="form-group"></div>
                <div class="row form-group">
                  <div class="col-md-6 pl-none">
                    <div class="gray-xs-f mb-xs">Group ID * <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="" data-original-title="The Tooltip plugin is small pop-up box that appears when the user moves."></span>
                    <div class="help-block with-errors red-txt"></div>
                    </div>
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Enter group ID"  name ="groupId" id="groupId" value="${fn:escapeXml(groupsBo.groupId)}" required>
                      <div class="help-block with-errors red-txt"></div>
                    </div>
                  </div>
                  <div class="col-md-6 pl-none">
                    <div class="gray-xs-f mb-xs">Group Name * <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="" data-original-title="The Tooltip plugin is small pop-up box that appears when the user moves."></span>
                    <div class="help-block with-errors red-txt"></div>


                    </div>
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Enter group name" name ="groupName" id="groupName" value="${fn:escapeXml(groupsBo.groupName)}" required>
                    <div class="help-block with-errors red-txt"></div>
                    </div>
                  </div>


        <!-- End right Content here -->
</form:form>

<script>
 $(document).ready(function () {
  $("#saveId").click(function () {
  var questnId = $('#questionnaireId').val();
     var groupId = $('#groupId').val();
      var groupName = $('#groupName').val();
         if(groupId != '' && groupId != null && typeof groupId != 'undefined' && groupName != '' && groupName != null && typeof groupName != 'undefined'){
                 $("#actionButtonType").val('save');
                 $('#id').val();
                 $('#groupId').val();
                 $("#groupName").val();
                 $('#addGroupFormId').submit();
                 showSucMsg("Content saved as draft.");

                 }
                 else
                 {
                 $("#alertMsg").removeClass('s-box').addClass('e-box').text(
                      "Please fill out this all the mandatory fields");
                  $('#alertMsg').show();
                 }
                 setTimeout(hideDisplayMessage, 4000);
               });
       });

         function goToBackPage(item) {
                 var a = document.createElement('a');
                     a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}";
                     document.body.appendChild(a).click();
             }
    </script>