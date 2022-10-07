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

.text-normal > button > .filter-option{
          text-transform: inherit !important;
      }

      .formula-box {
          height: 50px;
          border:1px solid #bfdceb;
          border-bottom:0;
          padding: 15px;
          color: #007cba;
      }

      .operator {
          width: 63% !important;
      }

      .dest-label {
          padding-left: 0 !important;
          padding-top: 7px;
      }


      .dest-row {
          margin-top: 12px;
      }
      
</style>

</head>

<!-- Start right Content here -->
<form:form
action="/fdahpStudyDesigner/adminStudies/addOrUpdateGroupsDetails.do?_S=${param._S}"
name="addGroupFormId" id="addGroupFormId" method="post">
<input type="hidden" name="language" value="${currLanguage}">
<input type="hidden" id="actionType" name="actionType"
                           value="${fn:escapeXml(actionType)}">
                    <input type="hidden" id="buttonText" name="buttonText"
                           value="">
        			<input type="hidden" value="${groupsBean.action}" id="action" name="action"> 
				<input type="hidden" value="" id="buttonText" value="${id}" name="buttonText">
                           
                    <input type="hidden" id="isAutoSaved" value="${isAutoSaved}" name="isAutoSaved"/>

          <div class="col-sm-10 col-rc white-bg p-none">
            <!--  Start top tab section-->
            <!-- <div class="right-content-head"><div class="text-right"><div class="black-md-f dis-line pull-left line34"><span class="mr-sm"><a href="#"><img src="images/icons/back-b.png"/></a></span> Group
                    </div></div></div> -->
            <div class="right-content-head">
              <div class="text-right">
           <div class="black-md-f dis-line pull-left line34">
                <span class="pr-sm cur-pointer" onclick="goToBackPage(this);">
                <img src="../images/icons/back-b.png" class="pr-md"/></span>
                Survey List
            </div>

                <div class="dis-line form-group mb-none mr-sm">
                  <button type="button" class="btn btn-default gray-btn" onclick="goToBackPage(this);">Cancel </button>
                </div>
                
              </div>
            </div>
            <!--  End  top tab section-->
             <div class="right-content-body">
              <div>
                <div class="form-group"></div>
                <div class="row form-group">
                  <div class="col-md-6 pl-none">
                  						<div class="study-selected mt-md">
 							<c:forEach items="${groupsAssignedList}" var="groupsAssignedList">
 								<div class="study-selected-item selStd" id="std${groupsAssignedList.stepId}">
									<input type="hidden" class="stdCls" id="${groupsAssignedList.stepId}" name=""
										value="${groupsAssignedList.stepId}"
										stdTxt="${groupsAssignedList.description}"
										<c:if test="${actionPage eq 'VIEW_PAGE'}">disabled</c:if>>
									<c:if test="${actionPage ne 'VIEW_PAGE'}">
										<span class="mr-md"><img
											src="/fdahpStudyDesigner/images/icons/close.png"
											onclick="del(${groupsAssignedList.stepId});" /></span>
									</c:if>
									<span>${groupsAssignedList.description}</span> 
								</div>
							</c:forEach>
 						</div>
                  
                  </div>
                  
			</div>
		</div>
		
                         <br>

                     </div>
            
         </div>
	</div>


        <!-- End right Content here -->
</form:form>

<script>
 $(document).ready(function () {
     $(".menuNav li.active").removeClass('active');
     $(".seventhQuestionnaires").addClass('active');
  $("#saveId").click(function () {
  var questnId = $('#questionnaireId').val();
     var groupId = $('#groupId').val();
      var groupName = $('#groupName').val();
      var defaultVisibility = $('#groupDefaultVisibility').val();
      var destinationTrueAsGroup = $('#destinationTrueAsGroup').val();

      let currLang = $('#studyLanguage').val();
      if (currLang !== undefined && currLang !== null && currLang !== '' && currLang !== 'en') {
        $('#currentLanguage').val(currLang);
        refreshAndFetchLanguageData(currLang);
      }
      
     var id =  $('#id').val();
         if(groupId != '' && groupId != null && typeof groupId != 'undefined' && groupName != '' && groupName != null && typeof groupName != 'undefined'){
                 $("#action").val('false');

                 $('#id').val();
                 $('#groupId').val();
                 $("#groupName").val();
                 $("#groupDefaultVisibility").val();
                 $("#destinationTrueAsGroup").val();
                 $("#buttonText").val('save');
                 $("#isAutoSaved").val('true');
                 $('#addGroupFormId').submit();
                 //showSucMsg("Content saved as draft.");
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
                 let lang = ($('#studyLanguage').val()!==undefined)?$('#studyLanguage').val():'';
                     a.href = "/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}&language="
                         + lang;;
                     document.body.appendChild(a).click();
             }

         $('#chkSelect').on('change', function(e) {
             if($(this).is(':checked')) {
                 $('#contents').show();
             } else {
                 $('#contents').hide();
             }
         });
         
         




                $('#studyLanguage').on('change', function () {
       	  debugger
           let currLang = $('#studyLanguage').val();
           $('#currentLanguage').val(currLang);
          // $('#loader').show();
           refreshAndFetchLanguageData($('#studyLanguage').val());
         })
         
         function refreshAndFetchLanguageData(language) {
   		  debugger
   		    $.ajax({
   		      url: '/fdahpStudyDesigner/adminStudies/viewGroups.do?_S=${param._S}',
   		      type: "GET",
   		      data: {
   		        language: language
   		      },
   		      success: function (data) {
   		    	  debugger
   		        let htmlData = document.createElement('html');
   		        htmlData.innerHTML = data;
   		        if (language !== 'en') {
   		        	debugger
   		          updateCompletionTicks(htmlData);
   		          $('.tit_wrapper').text($('#mlName', htmlData).val());
   		          $('#groupName').attr('disabled', true);
   		       $('#groupId').attr('disabled', true);
   		    
   		 	$('#groupDefaultVisibility').attr('disabled', true);
   		          //$('.delete,thead').addClass('cursor-none');
   		          let mark=true;
   		          $('#groups_list option', htmlData).each(function (index, value) {
   		            let id = '#row' + value.getAttribute('id');
   		            $(id).find('td.title').text(value.getAttribute('value'));
   
   		          });
   		         // view_spanish_deactivemode();
   		         
   		        } else {
   		        	debugger
   		          updateCompletionTicksForEnglish();
   		          $('.tit_wrapper').text($('#customStudyName', htmlData).val());
   		       $('#groupName').attr('disabled', false);
   		       $('#groupId').attr('disabled', false);
   		    $('#groupDefaultVisibility').attr('disabled', false);
   		          $('#studyProtocolId').prop('disabled', false);
   		          let mark=true;
   		          $('tbody tr', htmlData).each(function (index, value) {
   		        	  debugger
   		            let id = '#'+value.getAttribute('id');
   		            $(id).find('td.title').text($(id, htmlData).find('td.title').text());
   		          
   		          });
   		          
   		          <c:if test="${not empty permission}">
   		          $('.delete').addClass('cursor-none');
   		          </c:if>
   		          //view_spanish_activemode();
   		        }
   		      }
   		    });
   		  }

          /*  var count=0;
            var stepIdarray = new Array(); */
            function del(stepId){
              debugger
               	$.ajax({
                    url: "/fdahpStudyDesigner/adminStudies/deassignSteps.do?_S=${param._S}",
                    type: "POST",
                    datatype: "json",
                    data: {
                    	stepId: stepId,
                      "${_csrf.parameterName}": "${_csrf.token}",
                    },
                    success: function deleteConsentInfo(data) {
                    	debugger
                        var status = data.message;
                        if (status == "SUCCESS") {
                        	debugger
                        	 $('#std'+stepId).remove();
                        	//location.reload();
                        }
                    }
               	 
              })
            }
            /* var stepList=stepIdarray;
            console.log("hi",stepList); */

    </script>