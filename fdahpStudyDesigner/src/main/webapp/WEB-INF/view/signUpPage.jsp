<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html class="overflow-hidden">
<head>
<!-- Basic -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
            
    <title>FDA HSMP</title>	
    
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="author" content="">

    <!-- Favicon -->
    <link rel="shortcut icon" href="#" type="image/x-icon" />
    <link rel="apple-touch-icon" href="#">
        
    <!-- Mobile Metas -->
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        
    <!-- Web Fonts  -->
   <link href="https://fonts.googleapis.com/css?family=Roboto:300,400" rel="stylesheet">
        
    <!-- Vendor CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/boostrap/bootstrap.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/datatable/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/datatable/css/rowReorder.dataTables.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/dragula/dragula.min.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/magnific-popup/magnific-popup.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/font-awesome/font-awesome.min.css"> 
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/select2/bootstrap-select.min.css">  
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/select2/bootstrap-multiselect.css">      
    <link rel="stylesheet" href="/fdahpStudyDesigner/vendor/animation/animate.css">
        
    <!-- Theme Responsive CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/layout.css">   
        
    <!-- Theme CSS -->
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/theme.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/style.css">
    <link rel="stylesheet" href="/fdahpStudyDesigner/css/sprites_icon.css">
        
    <!-- Head Libs -->
    <script src="/fdahpStudyDesigner/vendor/modernizr/modernizr.js"></script>
    
    <!-- Vendor -->
    <script src="/fdahpStudyDesigner/vendor/jquery/jquery-3.1.1.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/boostrap/bootstrap.min.js"></script>
    <script src="/fdahpStudyDesigner/js/validator.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/animation/wow.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/datatable/js/jquery.dataTables.min.js"></script>
     <script src="/fdahpStudyDesigner/vendor/datatable/js/dataTables.rowReorder.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/dragula/react-dragula.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/magnific-popup/jquery.magnific-popup.min.js"></script>    
    <script src="/fdahpStudyDesigner/vendor/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/select2/bootstrap-select.min.js"></script>
    
    
    
    
    
    
    <script>
      (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
      (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
      m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
      })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

      ga('create', 'UA-71064806-1', 'auto');
      ga('send', 'pageview');
    </script>
        
</head>
<body class="white-bg">
    
    <div class="lg-container">
        
        <!-- Login Left Section-->
        <div class="lg-space-left">
            <div class="lg-space-img">
                <img src="images/logo/fda-logo-w.png"/>
            </div>
            <div class="lg-space-txt">
               Health Study <br>Management Portal
            </div>
             <div class="lg-space-cover">
                <img src="images/icons/web.png"/>
            </div>
        </div>
        <!-- End Login Left Section-->
        
        <!-- Login Right Section-->
        <div class="lg-space-right">
            <div>
             <input type="hidden" id="csrfDet" csrfParamName="${_csrf.parameterName}" csrfToken="${_csrf.token}" />
            <div class="lg-register-center col-xs-12">
             <form:form id="signUpForm" data-toggle="validator"  role="form" action="addPassword.do" method="post" autocomplete="off">
                    <div id="errMsg" class="error_msg">${errMsg}</div>
                    <div id="sucMsg" class="suceess_msg">${sucMsg}</div>
                    <c:if test="${isValidToken}">
                    <p class="col-xs-10 text-center boxcenter mb-xlg">To begin using the services on FDA and complete your account setup process, kindly use the access code provided on your email and set up your account password.</p>
                    <div class="login">
                      <div class="col-xs-6">
                        <div class="mb-lg form-group">
                             <input type="text" class="input-field wow_input" id="" name="firstName" value="${fn:escapeXml(userBO.firstName)}" maxlength="50" required autocomplete="off"/>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                      </div>
                      <div class="col-xs-6">
                        <div class="mb-lg form-group">
                             <input type="text" class="input-field wow_input" id="" name="lastName" value="${fn:escapeXml(userBO.lastName)}" maxlength="50" required autocomplete="off"/>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                      </div>
                      <div class="col-xs-6">
                        <div class="mb-lg form-group">
                             <input type="text" class="input-field wow_input validateUserEmail" name="userEmail" value="${userBO.userEmail}" oldVal="${userBO.userEmail}" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" data-pattern-error="Email address is invalid" maxlength="100" required readonly="readonly" autocomplete="off"/>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                        <div class="col-xs-6">
                        <div class="mb-lg form-group">
                             <input type="text" class="input-field wow_input phoneMask" id="" name="phoneNumber" value="${userBO.phoneNumber}" data-minlength="12" maxlength="12" required autocomplete="off"/>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                        <div class="col-xs-12">
                        <div class="mb-lg form-group">
                             <input type="text" class="input-field wow_input" id="" name="accessCode" maxlength="6" placeholder="Access Code" data-error="Access Code is invalid" required autocomplete="off"/>
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                        <div class="col-xs-6">
                        <div class="mb-lg form-group">
                            <input type="password" class="input-field wow_input" id="password"  name="password" maxlength="14"  data-minlength="8" placeholder="Password"  required
                        pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!&quot;#$%&amp;'()*+,-.:;&lt;=&gt;?@[\]^_`{|}~])[A-Za-z\d!&quot;#$%&amp;'()*+,-.:;&lt;=&gt;?@[\]^_`{|}~]{7,13}" autocomplete="off" data-error="Password is invalid" />
                        <div class="help-block with-errors red-txt"></div>
                        <!-- <input type="text" name="password" id="hidePass" /> -->
                        <span class="arrowLeftSugg"></span>
                            
                        </div>
                        </div>
                        <div class="col-xs-6">
                        <div class="mb-lg form-group">
                            <input type="password" class="input-field wow_input" id="cfnPassword" name="" maxlength="14" data-match="#password" data-match-error="Whoops, these don't match" placeholder="Confirm password" 
                              required  autocomplete="off"/> 
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                        <div class="col-xs-12">
                        <div class="mb-lg form-group">
                             <span class="checkbox checkbox-inline">
                                <input type="checkbox" id="inlineCheckbox" value="option1" required="required">
                                <label for="inlineCheckbox"> I Agree to the terms and conditions </label>
                            </span> 
                            <div class="help-block with-errors red-txt"></div>
                        </div>
                        </div>
                        <div class="mb-lg form-group col-xs-3 boxcenter">
                            <button type="submit" class="btn lg-btn" id="log-btn">Submit</button>
                        </div>
                        </c:if>
                        <c:if test="${not isValidToken}"><p class="passwordExp text-center"><i class="fa fa-exclamation-circle" aria-hidden="true"></i>The Password Reset Link is either expired or invalid.</p></c:if>
                        <div class="backToLogin text-center">
                            <a id="login" class="gray-link" href="javascript:void(0)" id="backToLogin">Back to Sign in</a>
                        </div>
                   </div>
                   <input type="hidden" name="securityToken" value="${securityToken}" />
                </form:form>
               </div>
            </div>
            
            
            <div class="clearfix"></div>
            
             <div class="footer">
                    <span>Copyright � 2016 FDA</span><span><a href="#">Terms</a></span><span><a href="#">Privacy Policy</a></span>
              </div>
             
        </div>
        <!-- End Login Right Section-->
        
    </div>
    <form:form action="/fdahpStudyDesigner/login.do" id="backToLoginForm" name="backToLoginForm" method="post">
	</form:form>
    
    <script src="/fdahpStudyDesigner/js/theme.js"></script>
    <script src="/fdahpStudyDesigner/js/jquery.mask.min.js"></script>
    <script src="/fdahpStudyDesigner/js/common.js"></script>
    <script src="/fdahpStudyDesigner/js/jquery.nicescroll.min.js"></script>
    <script src="/fdahpStudyDesigner/vendor/tinymce/tinymce.min.js"></script>
    <script src="/fdahpStudyDesigner/js/bootbox.min.js"></script>
   
   
   <script>
    	$(document).ready(function(e) {
    		$('.backToLogin').on('click',function(){
				$('#backToLoginForm').submit();
			});
    		$("form").submit(function() {
        		$(this).submit(function() {
           	 		return false;
        		});
        		 	return true;
    		});
    		
    		var errMsg = '${errMsg}';
			if(errMsg.length > 0){
				$("#errMsg").html(errMsg);
			   	$("#errMsg").show("fast");
			   	//$("#sucMsg").hide("fast");
			   	setTimeout(hideDisplayMessage, 4000);
			}
			var sucMsg = '${sucMsg}';
			if(sucMsg.length > 0){
				$("#sucMsg").html(sucMsg);
		    	$("#sucMsg").show("fast");
		    	$("#errMsg").hide("fast");
		    	setTimeout(hideDisplayMessage, 4000);
			}
			/* $("#password").passwordValidator({
				// list of qualities to require
				require: ['length', 'lower', 'upper', 'digit','spacial'],
				// minimum length requirement
				length: 8
			});  */
			
			
			/* $(".validateUserEmail").change(function(){
		        var email = $(this).val();
		        var oldEmail = $(this).attr('oldVal');
		        var isEmail = false;
		        var regEX = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/;
		        isEmail = regEX.test(email);
		        
		        if(isEmail && ('' == oldEmail || ('' != oldEmail && oldEmail != email))){
		        	var csrfDetcsrfParamName = $('#csrfDet').attr('csrfParamName');
		            var csrfToken = $('#csrfDet').attr('csrfToken');
		            var thisAttr= this;
		            $(thisAttr).parent().find(".help-block").html("<ul class='list-unstyled'><li></li></ul>");
		                if(email != ''){
		                    $.ajax({
		                        url: "/fdahpStudyDesigner/isEmailValid.do?"+csrfDetcsrfParamName+"="+csrfToken,
		                        type: "POST",
		                        datatype: "json",
		                        data: {
		                            email : email,
		                        },
		                        success:  function getResponse(data){
		                            var message = data.message;
		                            if('SUCCESS' != message){
		                                $(thisAttr).validator('validate');
		                                $(thisAttr).parent().removeClass("has-danger").removeClass("has-error");
		                                $(thisAttr).parent().find(".help-block").html("");
		                            }else{
		                                $(thisAttr).val('');
		                                $(thisAttr).parent().addClass("has-danger").addClass("has-error");
		                                $(thisAttr).parent().find(".help-block").empty();
		                                $(thisAttr).parent().find(".help-block").append("<ul class='list-unstyled'><li>'" + email + "' already exists.</li></ul>");
		                            }
		                        }
		                  });
		              }
		        }else{
		        	$("#removeText .help-block ul").remove();
		        }
		    }); */
    	});
    	function hideDisplayMessage(){
			$('#sucMsg').hide();
			$('#errMsg').hide();
		}
    	window.onload = function () {
		    if (typeof history.pushState === "function") {
		        history.pushState("jibberish", null, null);
		        window.onpopstate = function () {
		            history.pushState('newjibberish', null, null);
		            // Handle the back (or forward) buttons here
		            // Will NOT handle refresh, use onbeforeunload for this.
		        };
		    }
		    else {
		        var ignoreHashChange = true;
		        window.onhashchange = function () {
		            if (!ignoreHashChange) {
		                ignoreHashChange = true;
		                window.location.hash = Math.random();
		                // Detect and redirect change here
		                // Works in older FF and IE9
		                // * it does mess with your hash symbol (anchor?) pound sign
		                // delimiter on the end of the URL
		            }
		            else {
		                ignoreHashChange = false;   
		            }
		        };
		    }
		}
    </script>

</body>
</html>