<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
  <!-- ============================================================== -->
         <!-- Start right Content here -->
         <!-- ============================================================== --> 
        <div class="col-sm-10 col-rc white-bg p-none">
        <form:form action="/fdahpStudyDesigner/adminStudies/saveOrDoneChecklist.do?${_csrf.parameterName}=${_csrf.token}" id="checklistForm" role="form" method="post" autocomplete="off" enctype="multipart/form-data">    
            <input type="hidden" name="checklistId" value="${checklist.checklistId}">
            <input type="hidden" id="actionBut" name="actionBut">
            <!--  Start top tab section-->
            <div class="right-content-head">        
                <div class="text-right">
                    <div class="black-md-f dis-line pull-left line34"><span class="pr-sm">Checklist</span></div>
                    <div class="dis-line form-group mb-none mr-sm">
                         <button type="button" class="btn btn-default gray-btn cancelBut">Cancel</button>
                     </div>
                    
                     <div class="dis-line form-group mb-none mr-sm">
                         <button type="button" class="btn btn-default gray-btn viewAct" id="saveChecklistId">Save</button>
                     </div>

                     <div class="dis-line form-group mb-none">
                         <button type="button" class="btn btn-primary blue-btn viewAct" id="doneChecklistId">Done</button>
                     </div>
                 </div>
            </div>
            <div class="right-content-body">
            <div>
                 <span>Checklist def</span>
            </div></br>
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox1" class="class" name="checkbox1" <c:if test="${checklist.checkbox1}">checked</c:if> required>
                            <label for="inlineCheckbox1"> checklist1 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox2" class="class" name="checkbox2" <c:if test="${checklist.checkbox2}">checked</c:if> required>
                            <label for="inlineCheckbox2"> checklist2 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox3" class="class" name="checkbox3" <c:if test="${checklist.checkbox3}">checked</c:if> required>
                            <label for="inlineCheckbox3"> checklist3 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox4" class="class" name="checkbox4" <c:if test="${checklist.checkbox4}">checked</c:if> required>
                            <label for="inlineCheckbox4"> checklist4 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox5" class="class" name="checkbox5" <c:if test="${checklist.checkbox5}">checked</c:if> required>
                            <label for="inlineCheckbox5"> checklist5 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox6" class="class" name="checkbox6" <c:if test="${checklist.checkbox6}">checked</c:if> required>
                            <label for="inlineCheckbox6"> checklist6 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox7" class="class" name="checkbox7" <c:if test="${checklist.checkbox7}">checked</c:if> required>
                            <label for="inlineCheckbox7"> checklist7 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox8" class="class" name="checkbox8" <c:if test="${checklist.checkbox8}">checked</c:if> required>
                            <label for="inlineCheckbox8"> checklist8 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox9" class="class" name="checkbox9" <c:if test="${checklist.checkbox9}">checked</c:if> required>
                            <label for="inlineCheckbox9"> checklist9 </label>
                      </span>
            </div></br>
            
            <div>
                       <span class="checkbox checkbox-inline p-45">
                            <input type="checkbox" id="inlineCheckbox10" class="class" name="checkbox10" <c:if test="${checklist.checkbox10}">checked</c:if> required>
                            <label for="inlineCheckbox10"> checklist10	 </label>
                      </span>
            </div></br>
            </div>
            <!--  End body tab section -->
        </form:form>   
        </div>
        <!-- End right Content here -->

<script type="text/javascript">
$(document).ready(function(){
	    $(".menuNav li").removeClass('active');
	    $(".nine").addClass('active'); 
		
		$('#saveChecklistId').click(function() {
			 $('#actionBut').val('save');
		     $('#checklistForm').submit();
		});
		
		$("#doneChecklistId").on('click', function(){
			 var count = 0;
			 $('input:checkbox.class:checked').each(function () {
				 count++;
			 });
			 if(count == 10){
				 $('#actionBut').val('done');
		 		 $('#checklistForm').submit();
			 }else{
				 bootbox.alert("Please select all the checkboxes.")
			 }
		});
});

</script>
