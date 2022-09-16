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
</style>
</head>
<!-- Start right Content here -->
<form:form
action="/fdahpStudyDesigner/adminStudies/addOrUpdateGroupsDetails.do?_S=${param._S}"
name="addGroupFormId" id="addGroupFormId" method="post">
<input type="hidden" name="id" id="id" value="${groupsBean.id}">
          <div class="col-sm-10 col-rc white-bg p-none">
            <!--  Start top tab section-->
            <!-- <div class="right-content-head"><div class="text-right"><div class="black-md-f dis-line pull-left line34"><span class="mr-sm"><a href="#"><img src="images/icons/back-b.png"/></a></span> Group
                    </div></div></div> -->
            <div class="right-content-head">
              <div class="text-right">
                <div class="black-md-f dis-line pull-left line34">
                  <span class="pr-sm cur-pointer">
                    <img src="images/icons/back-b.png" class="pr-md" />
                  </span>  Group-Level Attributes
                </div>
                <div class="dis-line form-group mb-none mr-sm" style="width: 150px;">
                  <span class="tool-tip" id="markAsTooltipId" data-toggle="tooltip" data-placement="bottom" title="Language selection is available in edit screen only">
                    <select class="selectpicker aq-select aq-select-form studyLanguage langSpecific" title="Select" disabled>
                      <option selected>English</option>
                    </select>
                  </span>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn">Cancel </button>
                </div>
                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn" id="saveId">Save </button>
                </div>
                <div class="dis-line form-group mb-none">
                  <span class="tool-tip" data-toggle="tooltip" data-placement="bottom" id="helpNote"></span>
                  <button type="button" class="btn btn-primary blue-btn" id="doneId">Done</button>
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
                    </div>
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Enter group ID"  name ="groupId" id="groupId" value="${fn:escapeXml(groupsBo.groupId)}">
                    </div>
                  </div>
                  <div class="col-md-6 pl-none">
                    <div class="gray-xs-f mb-xs">Group Name * <span class="ml-xs sprites_v3 filled-tooltip" data-toggle="tooltip" title="" data-original-title="The Tooltip plugin is small pop-up box that appears when the user moves."></span>
                    </div>
                    <div class="form-group">
                      <input type="text" class="form-control" placeholder="Enter group name" name ="groupName" id="groupName" value="${fn:escapeXml(groupsBo.groupName)}">
                    </div>
                  </div>



        <!-- End right Content here -->


</form:form>

<script>
 $(document).ready(function () {
      //Adding Answers option
        $('#ans-btn').click(function(){
            $('#ans-opts').append("<div class='row mt-lg'><div class='col-md-11 pl-none'><div> <span class='radio radio-info radio-inline p-45'> <input type='radio' id='and' value='And' name='and'> <label for='and'>And</label> </span> <span class='radio radio-inline'> <input type='radio' id='or' value='Or' name='or'> <label for='or'>Or</label> </span> <div class='help-block with-errors red-txt'></div></div><div class='group-box'> <div class='group-head'> <div class='gray-xs-f blue-link mb-xs mt-sm'>Formula </div></div><div class='group-body'> <div class='row'> <div class='col-md-3 pl-none pr-none'> <div class='gray-xs-f mb-xs mt-sm'>Define functions </div></div><div class='col-md-4 pl-none pr-none'> <div class='gray-xs-f mb-xs mt-sm'>Define Inputs </div></div><div class='col-md-5 pl-none pr-none'></div></div><div class='row p-none'> <div class='col-md-3 pl-none pr-none'> <div class='col-md-4 pl-none pr-none'> <div class='gray-xs-f black-xs-f mb-xs mt-sm'>Operator=</div></div><div class='col-md-4 pl-none'> <div class='form-group p-none mb-none'> <select class='selectpicker' required> <option selected disabled> < </option> <option> < </option> <option>> </option> <option>& </option> <option>! </option> <option>=</option> </select> <div class='help-block with-errors red-txt'></div></div></div></div><div class='col-md-5 pl-none pr-none'> <div class='col-md-2 pl-none pr-none'> <div class='gray-xs-f black-xs-f mb-xs mt-sm'>Value=</div></div><div class='col-md-8 pl-none'> <div class='form-group p-none mb-none'> <input type='text' class='form-control' placeholder='Enter Value' name=''> <div class='help-block with-errors red-txt'></div></div></div></div><div class='col-md-4 pl-none pr-none'></div></div></div></div></div><div class='col-md-1'><div class='clearfix'></div><div class='mt-xs mt-xxlg'> <span class='cur-pointer mt-xxlg'><img id='del' src='images/icons/delete-g.png' class='mt-xxlg'></span> </div></div></div></div>");
       });

    //Removing answer option
     $("#del").click(function(){
       $(this).parents().remove();
    });

  });
  $("#saveId").click(function () {
      debugger
          //$('.required-attr').prop('required', false);
                 $("#actionButtonType").val('save');
                 $('#id').val();
                 $('#groupId').val();
                 $("#groupname").val();
                 $('#addGroupFormId').submit();
                 showSucMsg("Content saved as draft.");
  });
    </script>