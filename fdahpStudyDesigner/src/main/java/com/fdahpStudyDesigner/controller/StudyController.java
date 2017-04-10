package com.fdahpStudyDesigner.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fdahpStudyDesigner.bean.StudyListBean;
import com.fdahpStudyDesigner.bean.StudyPageBean;
<<<<<<< HEAD
import com.fdahpStudyDesigner.bo.Checklist;
=======
import com.fdahpStudyDesigner.bo.ActiveTaskBo;
>>>>>>> 640aff0487748b97b28a23b7f55f45bb756c3211
import com.fdahpStudyDesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpStudyDesigner.bo.ConsentBo;
import com.fdahpStudyDesigner.bo.ConsentInfoBo;
import com.fdahpStudyDesigner.bo.ConsentMasterInfoBo;
import com.fdahpStudyDesigner.bo.EligibilityBo;
import com.fdahpStudyDesigner.bo.NotificationBO;
import com.fdahpStudyDesigner.bo.NotificationHistoryBO;
import com.fdahpStudyDesigner.bo.ReferenceTablesBo;
import com.fdahpStudyDesigner.bo.ResourceBO;
import com.fdahpStudyDesigner.bo.StudyBo;
import com.fdahpStudyDesigner.bo.StudyPageBo;
import com.fdahpStudyDesigner.bo.StudySequenceBo;
import com.fdahpStudyDesigner.service.NotificationService;
import com.fdahpStudyDesigner.service.StudyService;
import com.fdahpStudyDesigner.service.UsersService;
import com.fdahpStudyDesigner.util.SessionObject;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerConstants;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerUtil;

/**
 * @author Ronalin
 *
 */
@Controller
public class StudyController {

    private static Logger logger = Logger.getLogger(StudyController.class.getName());
	
	@Autowired
	private StudyService studyService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private NotificationService notificationService;
	
	/**
     * @author Ronalin
	 * Getting Study list
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/studyList.do")
	public ModelAndView getStudies(HttpServletRequest request){
		logger.info("StudyController - getStudies - Starts");
		ModelAndView mav = new ModelAndView("loginPage");
		ModelMap map = new ModelMap();
		List<StudyListBean> studyBos = null;
		//List<UserBO> userList = null;
		String sucMsg = "";
		String errMsg = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				
				if(request.getSession().getAttribute("studyId") != null){
					request.getSession().removeAttribute("studyId");
				}
				if(request.getSession().getAttribute("permission") != null){
					request.getSession().removeAttribute("permission");
				}
				studyBos = studyService.getStudyList(sesObj.getUserId());
				//userList = usersService.getUserList();
				//map.addAttribute("userList"+userList);
				map.addAttribute("studyBos", studyBos);
				map.addAttribute("studyListId","true"); 
				mav = new ModelAndView("studyListPage", map);
			}
		}catch(Exception e){
			logger.error("StudyController - getStudies - ERROR",e);
		}
		logger.info("StudyController - getStudies - Ends");
		return mav;
	}
	
	/**
     * @author Ronalin
	 * add baisc info page
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/viewBasicInfo.do")
	public ModelAndView viewBasicInfo(HttpServletRequest request){
		logger.info("StudyController - viewBasicInfo - Starts");
		ModelAndView mav = new ModelAndView("loginPage");
		ModelMap map = new ModelMap();
		HashMap<String, List<ReferenceTablesBo>> referenceMap = null;
		List<ReferenceTablesBo> categoryList = null;
		List<ReferenceTablesBo> researchSponserList = null;
		List<ReferenceTablesBo> dataPartnerList = null;
		StudyBo studyBo = null;
		String sucMsg = "";
		String errMsg = "";
		ConsentBo consentBo = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String  studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true? "" : request.getParameter("studyId");
				if(fdahpStudyDesignerUtil.isEmpty(studyId)){
					studyId = (String) request.getSession().getAttribute("studyId");
				} else {
					request.getSession().setAttribute("studyId", studyId);
				}
				
				String  permission = fdahpStudyDesignerUtil.isEmpty(request.getParameter("permission")) == true? "" : request.getParameter("permission");
				if(fdahpStudyDesignerUtil.isEmpty(permission)){
					permission = (String) request.getSession().getAttribute("permission");
				} else {
					request.getSession().setAttribute("permission", permission);
				}
				if(fdahpStudyDesignerUtil.isNotEmpty(studyId)){
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					
					//get consentId if exists for studyId
					request.getSession().removeAttribute("consentId");
					consentBo = studyService.getConsentDetailsByStudyId(studyId);
					if( consentBo != null){
						request.getSession().setAttribute("consentId", consentBo.getId());
					}else{
						request.getSession().removeAttribute("consentId");
					}
				}
				if(studyBo == null){
					studyBo = new StudyBo();
				}
				/*if(fdahpStudyDesignerUtil.isNotEmpty(studyId) && studyBo!=null && !studyBo.isViewPermission()){
					mav = new ModelAndView("redirect:unauthorized.do");
				}else{*/
				referenceMap = studyService.getreferenceListByCategory();
				if(referenceMap!=null && referenceMap.size()>0){
				for (String key : referenceMap.keySet()) {
					if (StringUtils.isNotEmpty(key)) {
						switch (key) {
						case fdahpStudyDesignerConstants.REFERENCE_TYPE_CATEGORIES:
							 categoryList = referenceMap.get(key);
							 break;
						case fdahpStudyDesignerConstants.REFERENCE_TYPE_RESEARCH_SPONSORS:
							researchSponserList = referenceMap.get(key);
 							break;
						case fdahpStudyDesignerConstants.REFERENCE_TYPE_DATA_PARTNER:
							dataPartnerList = referenceMap.get(key);
							break;
						default:
							break;
						}
					}
				  }
				}
				map.addAttribute("categoryList",categoryList);
				map.addAttribute("researchSponserList",researchSponserList);
				map.addAttribute("dataPartnerList",dataPartnerList);
				map.addAttribute("studyBo",studyBo);
				map.addAttribute("createStudyId","true");
				map.addAttribute("permission",permission); 
				mav = new ModelAndView("viewBasicInfo", map);
				//}
				
			}
		}catch(Exception e){
			logger.error("StudyController - viewBasicInfo - ERROR",e);
		}
		logger.info("StudyController - viewBasicInfo - Ends");
		return mav;
	}
	
	/** 
	  * @author Ronalin
	  * validating particular Study custom Id
	  * @param request , {@link HttpServletRequest}
	  * @param response , {@link HttpServletResponse}
	  * @throws IOException
	  * @return void
	  */
		@RequestMapping(value="/adminStudies/validateStudyId.do",  method = RequestMethod.POST)
		public void validateStudyId(HttpServletRequest request, HttpServletResponse response) throws IOException{
			logger.info("StudyController - validateStudyId() - Starts ");
			JSONObject jsonobject = new JSONObject();
			PrintWriter out = null;
			String message = fdahpStudyDesignerConstants.FAILURE;
			boolean flag = false;
			try{
				HttpSession session = request.getSession();
				SessionObject userSession = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
				if (userSession != null) {
					String customStudyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("customStudyId")) == true?"":request.getParameter("customStudyId");
					flag = studyService.validateStudyId(customStudyId);
					if(flag)
						message = fdahpStudyDesignerConstants.SUCCESS;
				}
			}catch (Exception e) {
				logger.error("StudyController - validateStudyId() - ERROR ", e);
			}
			logger.info("StudyController - validateStudyId() - Ends ");
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}
	
	/**
     * @author Ronalin
	 * save or update baisc info page
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/saveOrUpdateBasicInfo.do")
	public ModelAndView saveOrUpdateBasicInfo(HttpServletRequest request,@ModelAttribute("studyBo") StudyBo studyBo,BindingResult result){
		logger.info("StudyController - saveOrUpdateBasicInfo - Starts");
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		ModelAndView mav = new ModelAndView("viewBasicInfo");
		String fileName = "", file="";
		String buttonText = "";
		String message = fdahpStudyDesignerConstants.FAILURE;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			buttonText = fdahpStudyDesignerUtil.isEmpty(request.getParameter("buttonText")) == true ? "" : request.getParameter("buttonText");
			if(sesObj!=null){
				
				if(studyBo.getId()==null){
					StudySequenceBo studySequenceBo = new StudySequenceBo();
					studySequenceBo.setBasicInfo(true);
					studyBo.setStudySequenceBo(studySequenceBo);
					studyBo.setStatus(fdahpStudyDesignerConstants.STUDY_PRE_LAUNCH);
					//studyBo.setSequenceNumber(fdahpStudyDesignerConstants.SEQUENCE_NO_1);
					studyBo.setUserId(sesObj.getUserId());
				}
				if(studyBo.getFile()!=null && !studyBo.getFile().isEmpty()){
					if(fdahpStudyDesignerUtil.isNotEmpty(studyBo.getThumbnailImage())){
						file = studyBo.getThumbnailImage().replace("."+studyBo.getThumbnailImage().split("\\.")[studyBo.getThumbnailImage().split("\\.").length - 1], "");
					} else {
						file = fdahpStudyDesignerUtil.getStandardFileName("STUDY",studyBo.getName(), studyBo.getCustomStudyId());
					}
					fileName = fdahpStudyDesignerUtil.uploadImageFile(studyBo.getFile(),file, fdahpStudyDesignerConstants.STUDTYLOGO);
					studyBo.setThumbnailImage(fileName);
				} 
				studyBo.setButtonText(buttonText);
				message = studyService.saveOrUpdateStudy(studyBo, sesObj.getUserId());
				request.getSession().setAttribute("studyId", studyBo.getId()+"");
				if(fdahpStudyDesignerConstants.SUCCESS.equals(message)) {
					if(buttonText.equalsIgnoreCase(fdahpStudyDesignerConstants.COMPLETED_BUTTON)){
						  request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
						  return new ModelAndView("redirect:viewSettingAndAdmins.do");
						  
					}else{
						  request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));  
						  return new ModelAndView("redirect:viewBasicInfo.do");
					}
				}else {
					request.getSession().setAttribute("errMsg", "Error in set BasicInfo.");
					return new ModelAndView("redirect:viewBasicInfo.do");
				}
			}
		}catch(Exception e){
			logger.error("StudyController - saveOrUpdateBasicInfo - ERROR",e);
		}
		logger.info("StudyController - saveOrUpdateBasicInfo - Ends");
		return mav;
	}
	
	
	/**
     * @author Ronalin
	 * view baisc info page
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/viewSettingAndAdmins.do")
	public ModelAndView viewSettingAndAdmins(HttpServletRequest request){
		logger.info("StudyController - viewSettingAndAdmins - Starts");
		ModelAndView mav = new ModelAndView("viewSettingAndAdmins");
		ModelMap map = new ModelMap();
		StudyBo studyBo = null;
		//List<UserBO> userList = null;
		//List<StudyListBean> studyPermissionList = null;
		String sucMsg = "", errMsg = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String  studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true? "" : request.getParameter("studyId");
				if(fdahpStudyDesignerUtil.isEmpty(studyId)){
					studyId = (String) request.getSession().getAttribute("studyId");
				}
				String permission = (String) request.getSession().getAttribute("permission");
				if(fdahpStudyDesignerUtil.isNotEmpty(studyId)){
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					//userList = usersService.getUserList();
					/*studyPermissionList = studyService.getStudyList(sesObj.getUserId());
					if(studyPermissionList!=null && studyPermissionList.size()>0){
						studyBo.setStudyPermissions(studyPermissionList);
					}*/
					//map.addAttribute("userList", userList);
					map.addAttribute("studyBo",studyBo);
					map.addAttribute("permission", permission);
					mav = new ModelAndView("viewSettingAndAdmins", map);
				}else{
					return new ModelAndView("redirect:studyList.do");
				}
			}
		}catch(Exception e){
			logger.error("StudyController - viewSettingAndAdmins - ERROR",e);
		}
		logger.info("StudyController - viewSettingAndAdmins - Ends");
		return mav;
	}
	
	/** 
	  * @author Ronalin
	  * Removing particular Study permission for the current user
	  * @param request , {@link HttpServletRequest}
	  * @param response , {@link HttpServletResponse}
	  * @throws IOException
	  * @return void
	  */
		@RequestMapping("/adminStudies/removeStudyPermissionById.do")
		public void removeStudyPermissionById(HttpServletRequest request, HttpServletResponse response) throws IOException{
			logger.info("StudyController - removeStudyPermissionById() - Starts ");
			JSONObject jsonobject = new JSONObject();
			PrintWriter out = null;
			String message = fdahpStudyDesignerConstants.FAILURE;
			boolean flag = false;
			try{
				HttpSession session = request.getSession();
				SessionObject userSession = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
				if (userSession != null) {
					String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
					flag = studyService.deleteStudyPermissionById(userSession.getUserId(), studyId);
					if(flag)
						message = fdahpStudyDesignerConstants.SUCCESS;
				}
			}catch (Exception e) {
				logger.error("StudyController - removeStudyPermissionById() - ERROR ", e);
			}
			logger.info("StudyController - removeStudyPermissionById() - Ends ");
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}
		
		/** 
		  * @author Ronalin
		  * Adding particular Study permission for the users
		  * @param request , {@link HttpServletRequest}
		  * @param response , {@link HttpServletResponse}
		  * @throws IOException
		  * @return void
		  */
			@RequestMapping("/adminStudies/addStudyPermissionByuserIds.do")
			public void addStudyPermissionByuserIds(HttpServletRequest request, HttpServletResponse response) throws IOException{
				logger.info("StudyController - addStudyPermissionByuserIds() - Starts ");
				JSONObject jsonobject = new JSONObject();
				PrintWriter out = null;
				String message = fdahpStudyDesignerConstants.FAILURE;
				boolean flag = false;
				try{
					HttpSession session = request.getSession();
					SessionObject userSession = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
					if (userSession != null) {
						String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
						String userIds = fdahpStudyDesignerUtil.isEmpty(request.getParameter("userIds")) == true?"":request.getParameter("userIds");
						flag = studyService.addStudyPermissionByuserIds(userSession.getUserId(), studyId, userIds);
						if(flag)
							message = fdahpStudyDesignerConstants.SUCCESS;
					}
				}catch (Exception e) {
					logger.error("StudyController - addStudyPermissionByuserIds() - ERROR ", e);
				}
				logger.info("StudyController - addStudyPermissionByuserIds() - Ends ");
				jsonobject.put("message", message);
				response.setContentType("application/json");
				out = response.getWriter();
				out.print(jsonobject);
			}
		
		/**
	     * @author Ronalin
		 * save or update setting and admins page
		 * @param request , {@link HttpServletRequest}
		 * @return {@link ModelAndView}
		 */
		@RequestMapping("/adminStudies/saveOrUpdateSettingAndAdmins.do")
		public ModelAndView saveOrUpdateSettingAndAdmins(HttpServletRequest request, StudyBo studyBo,BindingResult result){
			logger.info("StudyController - saveOrUpdateSettingAndAdmins - Starts");
			@SuppressWarnings("unchecked")
			HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
			ModelAndView mav = new ModelAndView("viewSettingAndAdmins");
			String message = fdahpStudyDesignerConstants.FAILURE;
			try{
				SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
				if(sesObj!=null){
					String buttonText = fdahpStudyDesignerUtil.isEmpty(request.getParameter("buttonText")) == true ? "" : request.getParameter("buttonText");
					studyBo.setButtonText(buttonText);
					studyBo.setUserId(sesObj.getUserId());
					message = studyService.saveOrUpdateStudySettings(studyBo);
					request.getSession().setAttribute("studyId", studyBo.getId()+"");
					if(fdahpStudyDesignerConstants.SUCCESS.equals(message)) {
						if(buttonText.equalsIgnoreCase(fdahpStudyDesignerConstants.COMPLETED_BUTTON)){
							request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
							return new ModelAndView("redirect:overviewStudyPages.do");
						}else{
							request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
							return new ModelAndView("redirect:viewSettingAndAdmins.do");
						}
					}else {
						request.getSession().setAttribute("errMsg", "Error in set Setting and Admins.");
						 return new ModelAndView("redirect:viewSettingAndAdmins.do");
					}
				}
			}catch(Exception e){
				logger.error("StudyController - saveOrUpdateSettingAndAdmins - ERROR",e);
			}
			logger.info("StudyController - saveOrUpdateSettingAndAdmins - Ends");
			return mav;
		}
		/**
	     * @author Ronalin
		 * view Overview Study page
		 * @param request , {@link HttpServletRequest}
		 * @return {@link ModelAndView}
		 */
		@RequestMapping("/adminStudies/overviewStudyPages.do")
		public ModelAndView overviewStudyPages(HttpServletRequest request){
			logger.info("StudyController - overviewStudyPages - Starts");
			ModelAndView mav = new ModelAndView("overviewStudyPage");
			ModelMap map = new ModelMap();
			List<StudyPageBo> studyPageBos = null;
			StudyBo studyBo = null;
			String sucMsg = "", errMsg = "";
			StudyPageBean studyPageBean = new StudyPageBean();
			try{
				SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
				if(sesObj!=null){
					if(null != request.getSession().getAttribute("sucMsg")){
						sucMsg = (String) request.getSession().getAttribute("sucMsg");
						map.addAttribute("sucMsg", sucMsg);
						request.getSession().removeAttribute("sucMsg");
					}
					if(null != request.getSession().getAttribute("errMsg")){
						errMsg = (String) request.getSession().getAttribute("errMsg");
						map.addAttribute("errMsg", errMsg);
						request.getSession().removeAttribute("errMsg");
					}
					String  studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true? "" : request.getParameter("studyId");
					if(fdahpStudyDesignerUtil.isEmpty(studyId)){
						studyId = (String) request.getSession().getAttribute("studyId");
					}
					String permission = (String) request.getSession().getAttribute("permission");
					if(StringUtils.isNotEmpty(studyId)){
						studyPageBos = studyService.getOverviewStudyPagesById(studyId, sesObj.getUserId());
						studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
						studyPageBean.setStudyId(studyBo.getId().toString());
						map.addAttribute("studyPageBos",studyPageBos);
						map.addAttribute("studyBo",studyBo);
						map.addAttribute("studyPageBean", studyPageBean);
						map.addAttribute("permission", permission);
						mav = new ModelAndView("overviewStudyPages", map);
					}else{
						return new ModelAndView("redirect:studyList.do");
					}
				}
			}catch(Exception e){
				logger.error("StudyController - overviewStudyPages - ERROR",e);
			}
			logger.info("StudyController - overviewStudyPages - Ends");
			return mav;
		}
				/**
			     * @author Ronalin
				 * save or update study page
				 * @param request , {@link HttpServletRequest}
				 * @return {@link ModelAndView}
				 */
				@RequestMapping("/adminStudies/saveOrUpdateStudyOverviewPage.do")
				public ModelAndView saveOrUpdateStudyOverviewPage(HttpServletRequest request,StudyPageBean studyPageBean){
					logger.info("StudyController - saveOrUpdateStudyOverviewPage - Starts");
					@SuppressWarnings("unchecked")
					HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
					ModelAndView mav = new ModelAndView("overviewStudyPage");
					String message = fdahpStudyDesignerConstants.FAILURE;
					try{
						SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
						String buttonText = studyPageBean.getActionType();
						if(sesObj!=null){
							message = studyService.saveOrUpdateOverviewStudyPages(studyPageBean);
							if(fdahpStudyDesignerConstants.SUCCESS.equals(message)) {
								if(buttonText.equalsIgnoreCase(fdahpStudyDesignerConstants.COMPLETED_BUTTON)){
									request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
									return new ModelAndView("redirect:viewStudyEligibilty.do");
								}else{
									request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
									return new ModelAndView("redirect:overviewStudyPages.do");
								}
							}else {
								request.getSession().setAttribute("errMsg", "Error in setting Overview.");
								 return new ModelAndView("redirect:overviewStudyPages.do");
							}
						}
					}catch(Exception e){
						logger.error("StudyController - saveOrUpdateStudyOverviewPage - ERROR",e);
					}
					logger.info("StudyController - saveOrUpdateStudyOverviewPage - Ends");
					return mav;
				}
				
	/**
	 * @author Ravinder			
	 * @param request
	 * @param response
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/consentListPage.do")
	public ModelAndView getConsentListPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - getConsentPage - Starts");
		ModelAndView mav = new ModelAndView("consentInfoListPage");
		ModelMap map = new ModelMap();
		StudyBo studyBo = null;
		ConsentBo consentBo = null;
		String sucMsg = "";
		String errMsg = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			List<ConsentInfoBo> consentInfoList = new ArrayList<ConsentInfoBo>();
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				String permission = (String) request.getSession().getAttribute("permission");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				}
				if(StringUtils.isNotEmpty(studyId)){
					consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
					boolean markAsComplete = true;
					consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
					if(consentInfoList != null && consentInfoList.size() > 0){
						for(ConsentInfoBo conInfoBo : consentInfoList){
							if(!conInfoBo.getStatus()){
								markAsComplete = false;
								break;
							}
						}
					}
					map.addAttribute("markAsComplete", markAsComplete);
					map.addAttribute("consentInfoList", consentInfoList);
					map.addAttribute("studyId", studyId);
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
					
					//get consentbo details by studyId
					consentBo = studyService.getConsentDetailsByStudyId(studyId);
					if( consentBo != null){
						request.getSession().setAttribute("consentId", consentBo.getId());
						map.addAttribute("consentId", consentBo.getId());
					}
				}
				map.addAttribute("permission", permission);
				mav = new ModelAndView("consentInfoListPage",map);
			}
		}catch(Exception e){
			logger.error("StudyController - getConsentPage - ERROR",e);
		}
		logger.info("StudyController - getConsentPage - Ends");
		return mav;
		
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/adminStudies/reOrderConsentInfo.do", method = RequestMethod.POST)
	public void reOrderConsentInfo(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - reOrderConsentInfo - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			int oldOrderNumber = 0;
			int newOrderNumber = 0;
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				}
				String oldOrderNo = fdahpStudyDesignerUtil.isEmpty(request.getParameter("oldOrderNumber")) == true?"":request.getParameter("oldOrderNumber");
				String newOrderNo = fdahpStudyDesignerUtil.isEmpty(request.getParameter("newOrderNumber")) == true?"":request.getParameter("newOrderNumber");
				if((studyId != null && !studyId.isEmpty()) && !oldOrderNo.isEmpty() && !newOrderNo.isEmpty()){
					oldOrderNumber = Integer.valueOf(oldOrderNo);
					newOrderNumber = Integer.valueOf(newOrderNo);
					message = studyService.reOrderConsentInfoList(Integer.valueOf(studyId), oldOrderNumber, newOrderNumber);
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - reOrderConsentInfo - ERROR",e);
		}
		logger.info("StudyController - reOrderConsentInfo - Ends");
	}
	
	/**
	 * @author Ravinder			
	 * @param request
	 * @param response
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/reloadConsentListPage.do")
	public void reloadConsentListPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - reloadConsentListPage - Starts");
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		ObjectMapper mapper = new ObjectMapper();
		JSONArray consentJsonArray = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			List<ConsentInfoBo> consentInfoList = new ArrayList<ConsentInfoBo>();
			if(sesObj!=null){
				String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				if(StringUtils.isNotEmpty(studyId)){
					consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
					if(consentInfoList!= null && consentInfoList.size() > 0){
						consentJsonArray = new JSONArray(mapper.writeValueAsString(consentInfoList));
					}
					message = fdahpStudyDesignerConstants.SUCCESS;
				}
				jsonobject.put("consentInfoList",consentJsonArray);
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - reloadConsentListPage - ERROR",e);
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out.print(jsonobject);
		}
		logger.info("StudyController - reloadConsentListPage - Ends");
		
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/adminStudies/deleteConsentInfo.do",method = RequestMethod.POST)
	public void deleteConsentInfo(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - deleteConsentInfo - Starts");
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String consentInfoId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("consentInfoId")) == true?"":request.getParameter("consentInfoId");
				String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				if(!consentInfoId.isEmpty() && !studyId.isEmpty()){
					message = studyService.deleteConsentInfo(Integer.valueOf(consentInfoId),Integer.valueOf(studyId));
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - deleteConsentInfo - ERROR",e);
		}
		logger.info("StudyController - deleteConsentInfo - Ends");
	}
	
	/**
	 * 
	 * @author Ravinder
	 * @param request
	 * @param response
	 * @param consentInfoBo
	 * @return
	 */
	@RequestMapping("/adminStudies/saveOrUpdateConsentInfo.do")
	public ModelAndView saveOrUpdateConsentInfo(HttpServletRequest request , HttpServletResponse response,ConsentInfoBo consentInfoBo){
		logger.info("StudyController - saveOrUpdateConsentInfo - Starts");
		ModelAndView mav = new ModelAndView("consentInfoListPage");
		ConsentInfoBo addConsentInfoBo = null;
		ModelMap map = new ModelMap();
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(consentInfoBo != null){
					if(consentInfoBo.getStudyId() != null && consentInfoBo.getId() == null){
						int order = studyService.consentInfoOrder(consentInfoBo.getStudyId());
						consentInfoBo.setSequenceNo(order);
					}
					addConsentInfoBo = studyService.saveOrUpdateConsentInfo(consentInfoBo, sesObj);
					if(addConsentInfoBo != null){
						if(consentInfoBo.getId() != null){
							request.getSession().setAttribute("sucMsg", propMap.get("update.consent.success.message"));
						}else{
							request.getSession().setAttribute("sucMsg", propMap.get("save.consent.success.message"));
						}
						mav = new ModelAndView("redirect:/adminStudies/consentListPage.do",map);
					}else{
						request.getSession().setAttribute("errMsg", "Consent not added successfully.");
						mav = new ModelAndView("redirect:/adminStudies/consentListPage.do", map);
					}
				}
			}	
		}catch(Exception e){
			logger.error("StudyController - saveOrUpdateConsentInfo - ERROR",e);
		}
		logger.info("StudyController - saveOrUpdateConsentInfo - Ends");
		return mav;
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/adminStudies/consentInfo.do")
	public ModelAndView getConsentPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - getConsentPage - Starts");
		ModelAndView mav = new ModelAndView("consentInfoPage");
		ModelMap map = new ModelMap();
		ConsentInfoBo consentInfoBo = null;
		StudyBo studyBo = null;
		List<ConsentInfoBo> consentInfoList = new ArrayList<ConsentInfoBo>();
		List<ConsentMasterInfoBo> consentMasterInfoList = new ArrayList<ConsentMasterInfoBo>();
		String sucMsg = "";
		String errMsg = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			if(sesObj!=null){
				String consentInfoId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("consentInfoId")) == true?"":request.getParameter("consentInfoId");
				String actionType = fdahpStudyDesignerUtil.isEmpty(request.getParameter("actionType")) == true?"":request.getParameter("actionType");
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
					request.getSession().setAttribute("studyId", studyId);
				}
				if(StringUtils.isEmpty(consentInfoId)){
					consentInfoId = (String) request.getSession().getAttribute("consentInfoId");
					request.getSession().setAttribute("consentInfoId", consentInfoId);
				}
				map.addAttribute("studyId", studyId);
				if(!studyId.isEmpty()){
					consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
					if(actionType.equals("view")){
						map.addAttribute("actionPage", "view");
					}else{
						map.addAttribute("actionPage", "addEdit");
					}
					consentMasterInfoList = studyService.getConsentMasterInfoList();
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
					map.addAttribute("consentMasterInfoList", consentMasterInfoList);
					if(consentMasterInfoList != null && consentMasterInfoList.size()>0){
						map.addAttribute("consentInfoList", consentInfoList);
					}
				}
				if(consentInfoId != null && !consentInfoId.isEmpty()){
					consentInfoBo = studyService.getConsentInfoById(Integer.valueOf(consentInfoId));
					map.addAttribute("consentInfoBo", consentInfoBo);
				}
				mav = new ModelAndView("consentInfoPage",map);
			}
		}catch(Exception e){
			logger.error("StudyController - getConsentPage - Error",e);
		}
		logger.info("StudyController - getConsentPage - Ends");
		return mav;
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/adminStudies/comprehensionQuestionList.do")
	public ModelAndView getComprehensionQuestionList(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - getComprehensionQuestionList - Starts");
		ModelAndView mav = new ModelAndView("comprehensionListPage");
		ModelMap map = new ModelMap();
		StudyBo studyBo=null;
		ConsentBo consentBo = null;
		String sucMsg = "";
		String errMsg = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = new ArrayList<ComprehensionTestQuestionBo>();
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				}
				if(StringUtils.isNotEmpty(studyId)){
					comprehensionTestQuestionList = studyService.getComprehensionTestQuestionList(Integer.valueOf(studyId));
					map.addAttribute("comprehensionTestQuestionList", comprehensionTestQuestionList);
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
					
					//get consentId if exists for studyId
					consentBo = studyService.getConsentDetailsByStudyId(studyId);
					if( consentBo != null){
						request.getSession().setAttribute("consentId", consentBo.getId());
						map.addAttribute("consentId", consentBo.getId());
						map.addAttribute("comprehensionTestMinimumScore", consentBo.getComprehensionTestMinimumScore());
					}
				}
				map.addAttribute("studyId", studyId);
				mav = new ModelAndView("comprehensionListPage",map);
			}
		}catch(Exception e){
			logger.error("StudyController - getComprehensionQuestionList - ERROR",e);
		}
		logger.info("StudyController - getComprehensionQuestionList - Ends");
		return mav;
	}
	
	@RequestMapping("/adminStudies/comprehensionQuestionPage.do")
	public ModelAndView getComprehensionQuestionPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - getConsentPage - Starts");
		ModelAndView mav = new ModelAndView("comprehensionQuestionPage");
		ModelMap map = new ModelMap();
		ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
		String sucMsg = "";
		String errMsg = "";
		StudyBo studyBo = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			if(sesObj!=null){
				//String comprehensionQuestionId = (String) request.getSession().getAttribute("comprehensionQuestionId");
				String comprehensionQuestionId =  fdahpStudyDesignerUtil.isEmpty(request.getParameter("comprehensionQuestionId")) == true?"":request.getParameter("comprehensionQuestionId");
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
					request.getSession().setAttribute("studyId", studyId);
				}
				if(StringUtils.isNotEmpty(studyId)){
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
				}
				if(StringUtils.isNotEmpty(comprehensionQuestionId)){
					comprehensionTestQuestionBo = studyService.getComprehensionTestQuestionById(Integer.valueOf(comprehensionQuestionId));
					map.addAttribute("comprehensionQuestionBo", comprehensionTestQuestionBo);
					mav = new ModelAndView("comprehensionQuestionPage",map);
				}
			}
		}catch(Exception e){
			logger.error("StudyController - getConsentPage - Error",e);
		}
		logger.info("StudyController - getConsentPage - Ends");
		return mav;
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 */
	@RequestMapping("/adminStudies/deleteComprehensionQuestion.do")
	public void deleteComprehensionTestQuestion(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - deleteComprehensionTestQuestion - Starts");
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String comprehensionQuestionId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("comprehensionQuestionId")) == true?"":request.getParameter("comprehensionQuestionId");
				String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				if(StringUtils.isNotEmpty(comprehensionQuestionId) && StringUtils.isNotEmpty(studyId)){
					message = studyService.deleteComprehensionTestQuestion(Integer.valueOf(comprehensionQuestionId),Integer.valueOf(studyId));
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - deleteComprehensionTestQuestion - ERROR",e);
		}
		logger.info("StudyController - deleteComprehensionTestQuestion - Ends");
	}
	
	/**
	 * 
	 * @author Ravinder
	 * @param request
	 * @param response
	 * @param consentInfoBo
	 * @return
	 */
	@RequestMapping("/adminStudies/saveOrUpdateComprehensionTestQuestion.do")
	public ModelAndView saveOrUpdateComprehensionTestQuestionPage(HttpServletRequest request , HttpServletResponse response,ComprehensionTestQuestionBo comprehensionTestQuestionBo){
		logger.info("StudyController - saveOrUpdateComprehensionTestQuestionPage - Starts");
		ModelAndView mav = new ModelAndView("consentInfoListPage");
		ComprehensionTestQuestionBo addComprehensionTestQuestionBo = null;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(comprehensionTestQuestionBo != null){
					if(comprehensionTestQuestionBo.getStudyId() != null){
						int order = studyService.comprehensionTestQuestionOrder(comprehensionTestQuestionBo.getStudyId());
						comprehensionTestQuestionBo.setSequenceNo(order);
					}
					if(comprehensionTestQuestionBo.getId() != null){
						comprehensionTestQuestionBo.setModifiedBy(sesObj.getUserId());
						comprehensionTestQuestionBo.setModifiedOn(fdahpStudyDesignerUtil.getCurrentDateTime());
					}else{
						comprehensionTestQuestionBo.setCreatedBy(sesObj.getUserId());
						comprehensionTestQuestionBo.setCreatedOn(fdahpStudyDesignerUtil.getCurrentDateTime());
					}
					addComprehensionTestQuestionBo = studyService.saveOrUpdateComprehensionTestQuestion(comprehensionTestQuestionBo);
					if(addComprehensionTestQuestionBo != null){
						request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
						return new ModelAndView("redirect:/adminStudies/comprehensionQuestionList.do");
					}else{
						request.getSession().setAttribute("sucMsg", "Unable to add Question added.");
						return new ModelAndView("redirect:/adminStudies/comprehensionQuestionList.do");
					}
				}
			}
		}catch(Exception e){
			logger.error("StudyController - saveOrUpdateComprehensionTestQuestionPage - ERROR",e);
		}
		logger.info("StudyController - saveOrUpdateComprehensionTestQuestionPage - Ends");
		return mav;
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 */
	@RequestMapping("/adminStudies/reOrderComprehensionTestQuestion.do")
	public void reOrderComprehensionTestQuestion(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - reOrderComprehensionTestQuestion - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			int oldOrderNumber = 0;
			int newOrderNumber = 0;
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				}
				String oldOrderNo = fdahpStudyDesignerUtil.isEmpty(request.getParameter("oldOrderNumber")) == true?"":request.getParameter("oldOrderNumber");
				String newOrderNo = fdahpStudyDesignerUtil.isEmpty(request.getParameter("newOrderNumber")) == true?"":request.getParameter("newOrderNumber");
				if((studyId != null && !studyId.isEmpty()) && !oldOrderNo.isEmpty() && !newOrderNo.isEmpty()){
					oldOrderNumber = Integer.valueOf(oldOrderNo);
					newOrderNumber = Integer.valueOf(newOrderNo);
					message = studyService.reOrderComprehensionTestQuestion(Integer.valueOf(studyId), oldOrderNumber, newOrderNumber);
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - reOrderComprehensionTestQuestion - ERROR",e);
		}
		logger.info("StudyController - reOrderComprehensionTestQuestion - Ends");
	}
	
	/**
	 * @author Ravinder
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/adminStudies/saveConsentInfo.do")
	public void saveConsentInfo(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - saveConsentInfo - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		ConsentInfoBo addConsentInfoBo = null;
		ObjectMapper mapper = new ObjectMapper();
		ConsentInfoBo consentInfoBo = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			String conInfo = request.getParameter("consentInfo");
			if(null != conInfo){
				 consentInfoBo = mapper.readValue(conInfo, ConsentInfoBo.class);
			}
			if(sesObj != null){
				if(consentInfoBo != null){
					if(consentInfoBo.getStudyId() != null && consentInfoBo.getId() == null){
						int order = studyService.consentInfoOrder(consentInfoBo.getStudyId());
						consentInfoBo.setSequenceNo(order);
					}
					addConsentInfoBo = studyService.saveOrUpdateConsentInfo(consentInfoBo, sesObj);
					if(addConsentInfoBo != null){
						jsonobject.put("consentInfoId", addConsentInfoBo.getId());
						message = fdahpStudyDesignerConstants.SUCCESS;
					}
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - saveConsentInfo - ERROR",e);
		}
		logger.info("StudyController - saveConsentInfo - Ends");
	}
	
	/**
	 * @author Ravinder			
	 * @param request
	 * @param response
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/reloadComprehensionQuestionListPage.do")
	public void reloadComprehensionQuestionListPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("StudyController - reloadConsentListPage - Starts");
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		ObjectMapper mapper = new ObjectMapper();
		JSONArray comprehensionJsonArray = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			List<ComprehensionTestQuestionBo> comprehensionTestQuestionList = new ArrayList<ComprehensionTestQuestionBo>();
			if(sesObj!=null){
				String studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true?"":request.getParameter("studyId");
				if(StringUtils.isNotEmpty(studyId)){
					comprehensionTestQuestionList = studyService.getComprehensionTestQuestionList(Integer.valueOf(studyId));
					if(comprehensionTestQuestionList!= null && comprehensionTestQuestionList.size() > 0){
						comprehensionJsonArray = new JSONArray(mapper.writeValueAsString(comprehensionTestQuestionList));
					}
					message = fdahpStudyDesignerConstants.SUCCESS;
				}
				jsonobject.put("comprehensionTestQuestionList",comprehensionJsonArray);
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - reloadConsentListPage - ERROR",e);
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out.print(jsonobject);
		}
		logger.info("StudyController - reloadConsentListPage - Ends");
		
	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/adminStudies/consentMarkAsCompleted.do")
	public ModelAndView consentMarkAsCompleted(HttpServletRequest request) {
		logger.info("StudyController - consentMarkAsCompleted() - Starts");
		ModelAndView mav = new ModelAndView("redirect:studyList.do");
		ModelMap map = new ModelMap();
		String message = fdahpStudyDesignerConstants.FAILURE;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				message = studyService.markAsCompleted(Integer.parseInt(studyId) , fdahpStudyDesignerConstants.CONESENT);	
				if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
					request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:consentReview.do");
				}else{
					request.getSession().setAttribute("errMsg", "Unable to mark as complete.");
					mav = new ModelAndView("redirect:consentListPage.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - consentMarkAsCompleted() - ERROR", e);
		}
		logger.info("StudyController - consentMarkAsCompleted() - Ends");
		return mav;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/adminStudies/consentReviewMarkAsCompleted.do")
	public ModelAndView consentReviewMarkAsCompleted(HttpServletRequest request) {
		logger.info("StudyController - consentReviewMarkAsCompleted() - Starts");
		ModelAndView mav = new ModelAndView("redirect:studyList.do");
		ModelMap map = new ModelMap();
		String message = fdahpStudyDesignerConstants.FAILURE;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				message = studyService.markAsCompleted(Integer.parseInt(studyId) , fdahpStudyDesignerConstants.CONESENT_REVIEW);	
				if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
					request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:getResourceList.do");
				}else{
					request.getSession().setAttribute("errMsg", "Unable to mark as complete.");
					mav = new ModelAndView("redirect:consentReview.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - consentReviewMarkAsCompleted() - ERROR", e);
		}
		logger.info("StudyController - consentReviewMarkAsCompleted() - Ends");
		return mav;
	}
	
	/*------------------------------------Added By Vivek Start---------------------------------------------------*/
	/**
	 * view Eligibility page
	 * @author Vivek 
	 * 
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/viewStudyEligibilty.do")
	public ModelAndView viewStudyEligibilty(HttpServletRequest request) {
		logger.info("StudyController - viewStudyEligibilty - Starts");
		ModelAndView mav = new ModelAndView("redirect:viewBasicInfo.do");
		ModelMap map = new ModelMap();
		StudyBo studyBo = null;
		String sucMsg = "";
		String errMsg = "";
		EligibilityBo eligibilityBo = null;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			
			String studyId = (String) request.getSession().getAttribute("studyId");
			
			if (StringUtils.isEmpty(studyId)) {
				studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "0" : request.getParameter("studyId");
			} 
			String permission = (String) request.getSession().getAttribute("permission");
			if (StringUtils.isNotEmpty(studyId)) {
				studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
				eligibilityBo = studyService.getStudyEligibiltyByStudyId(studyId);
				//map.addAttribute("studyPageBos", studyPageBos);
				map.addAttribute("studyBo", studyBo);
				if(eligibilityBo == null){
					eligibilityBo = new EligibilityBo();
					eligibilityBo.setStudyId(Integer.parseInt(studyId));
				}
				map.addAttribute("eligibility", eligibilityBo);
				map.addAttribute("permission", permission);
				mav = new ModelAndView("studyEligibiltyPage", map);
			} 
		} catch (Exception e) {
			logger.error("StudyController - viewStudyEligibilty - ERROR", e);
		}
		logger.info("StudyController - viewStudyEligibilty - Ends");
		return mav;
	}
	
	/**
	 * save or update Study Eligibility
	 * @author Vivek 
	 * 
	 * @param request , {@link HttpServletRequest}
	 * @param eligibilityBo , {@link EligibilityBo}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/saveOrUpdateStudyEligibilty.do")
	public ModelAndView saveOrUpdateStudyEligibilty(HttpServletRequest request, EligibilityBo eligibilityBo) {
		logger.info("StudyController - saveOrUpdateStudyEligibilty - Starts");
		ModelAndView mav = new ModelAndView("overviewStudyPage");
		ModelMap map = new ModelMap();
		String result = fdahpStudyDesignerConstants.FAILURE;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try {
			//actionType = fdahpStudyDesignerUtil.isEmpty(request.getParameter("actionType")) == true ? "" : request.getParameter("actionType");
			if (eligibilityBo != null) {
				result = studyService.saveOrUpdateStudyEligibilty(eligibilityBo);
			}
			request.getSession().setAttribute("studyId", eligibilityBo.getStudyId()+"");
			
			if(fdahpStudyDesignerConstants.SUCCESS.equals(result)) {
				if(eligibilityBo.getActionType().equals("save")){
					request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
					mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
				}else{
					request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:consentListPage.do", map);
				}	
			}else {
				request.getSession().setAttribute("errMsg", "Error in set Eligibility.");
				mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
			}
		} catch (Exception e) {
			logger.error("StudyController - saveOrUpdateStudyEligibilty - ERROR", e);
		}
		logger.info("StudyController - saveOrUpdateStudyEligibilty - Ends");
		return mav;
	}
	/*------------------------------------Added By Vivek End---------------------------------------------------*/
	
	/*----------------------------------------added by MOHAN T starts----------------------------------------*/
	/**
	 * @author Mohan
	 * @param request
	 * @param response
	 * @return ModelAndView
	 * 
	 * Description : This method is used to get the details of consent review and e-consent by studyId
	 */
	@RequestMapping("/adminStudies/consentReview.do")
	public ModelAndView getConsentReviewAndEConsentPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("INFO: StudyController - getConsentReviewAndEConsentPage() :: Starts");
		ModelAndView mav = new ModelAndView("consentInfoPage");
		ModelMap map = new ModelMap();
		SessionObject sesObj = null;
		String studyId = "";
		List<ConsentInfoBo> consentInfoBoList = null;
		StudyBo studyBo = null;
		ConsentBo consentBo = null;
		String consentId = "";
		String sucMsg = "";
		String errMsg = "";
		try{
			sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if( sesObj != null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				if( request.getSession().getAttribute("studyId") != null){
					studyId = (String) request.getSession().getAttribute("studyId").toString();
				}
				
				if( request.getSession().getAttribute("consentId") != null){
					consentId = (String) request.getSession().getAttribute("consentId").toString();
				}
				
				if(StringUtils.isEmpty(studyId)){
					studyId = StringUtils.isEmpty(request.getParameter("studyId"))==true?"":request.getParameter("studyId");
				}
				if(StringUtils.isEmpty(consentId)){
					consentId = StringUtils.isEmpty(request.getParameter("consentId"))==true?"":request.getParameter("consentId");
				}
				
				if(StringUtils.isNotEmpty(studyId)){
					request.getSession().setAttribute("studyId", studyId);
					consentInfoBoList = studyService.getConsentInfoDetailsListByStudyId(studyId);
					if( null != consentInfoBoList && consentInfoBoList.size() > 0){
						map.addAttribute("consentInfoList", consentInfoBoList);
					}else{
						map.addAttribute("consentInfoList", "");
					}
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
					
					//get consentId if exists for studyId
					consentBo = studyService.getConsentDetailsByStudyId(studyId);
					if( consentBo != null){
						request.getSession().setAttribute("consentId", consentBo.getId());
						map.addAttribute("consentId", consentBo.getId());
					}
					
					String permission = (String) request.getSession().getAttribute("permission");
					if(StringUtils.isNotEmpty(permission) && permission.equals("view")){
						map.addAttribute("permission", "view");
					}else{
						map.addAttribute("permission", "addEdit");
					}
				}
				map.addAttribute("studyId", studyId);
				map.addAttribute("consentId", consentId);
				map.addAttribute("consentBo", consentBo);
				mav = new ModelAndView("consentReviewAndEConsentPage", map);
			}
		}catch(Exception e){
			logger.error("StudyController - getConsentReviewAndEConsentPage() - ERROR ", e);
		}
		logger.info("INFO: StudyController - getConsentReviewAndEConsentPage() :: Ends");
		return mav;
	}
	
	
	/**
	 * @author Mohan
	 * @param request
	 * @param response
	 * @param consentInfoBo
	 * @return ModelAndView
	 * 
	 * Description : This method is used to save the consent eview and E-consent info for study 
	 */
	@RequestMapping("/adminStudies/saveConsentReviewAndEConsentInfo.do")
	public void saveConsentReviewAndEConsentInfo(HttpServletRequest request, HttpServletResponse response){
		logger.info("INFO: StudyController - saveConsentReviewAndEConsentInfo() :: Starts");
		ConsentBo consentBo = null;
		String consentInfoParamName = "";
		ObjectMapper mapper = new ObjectMapper();
		JSONObject jsonobj = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		String studyId = "";
		String consentId = "";
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj != null){
				consentInfoParamName = request.getParameter("consentInfo");
				if(StringUtils.isNotEmpty(consentInfoParamName)){
					consentBo = mapper.readValue(consentInfoParamName, ConsentBo.class);
					if(consentBo != null){
						consentBo = studyService.saveOrCompleteConsentReviewDetails(consentBo, sesObj);
						studyId = StringUtils.isEmpty(String.valueOf(consentBo.getStudyId()))==true?"":String.valueOf(consentBo.getStudyId());
						consentId = StringUtils.isEmpty(String.valueOf(consentBo.getId()))==true?"":String.valueOf(consentBo.getId());
						
						//setting consentId in requestSession
						request.getSession().removeAttribute("consentId");
						request.getSession().setAttribute("consentId", consentBo.getId());
						message = fdahpStudyDesignerConstants.SUCCESS;
					}
				}
			}
			jsonobj.put("message", message);
			jsonobj.put("studyId", studyId);
			jsonobj.put("consentId", consentId);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobj);
		}catch(Exception e){
			logger.error("StudyController - saveConsentReviewAndEConsentInfo() - ERROR ", e);
		}
		logger.info("INFO: StudyController - saveConsentReviewAndEConsentInfo() :: Ends");
	}
	
	/*----------------------------------------added by MOHAN T ends----------------------------------------*/
	
	/**
	 * @author Pradyumn			
	 * @param request
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/getResourceList.do")
	public ModelAndView getResourceList(HttpServletRequest request){
		logger.info("StudyController - getResourceList() - Starts");
		ModelAndView mav = new ModelAndView("resourceListPage");
		ModelMap map = new ModelMap();
		String sucMsg = "";
		String errMsg = "";
		List<ResourceBO> resourceBOList = null;
		List<ResourceBO> resourcesSavedList = null;
		ResourceBO studyProtocolResourceBO = null;
		StudyBo studyBo = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				String permission = (String) request.getSession().getAttribute("permission");
				if(StringUtils.isNotEmpty(studyId)){
					resourceBOList = studyService.getResourceList(Integer.valueOf(studyId));
					for(ResourceBO rBO:resourceBOList){
						if(rBO.isStudyProtocol()){
							studyProtocolResourceBO = new ResourceBO();
							studyProtocolResourceBO.setId(rBO.getId());
						}
					}
					resourcesSavedList = studyService.resourcesSaved(Integer.valueOf(studyId));
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("studyBo", studyBo);
					map.addAttribute("resourceBOList", resourceBOList);
					map.addAttribute("resourcesSavedList", resourcesSavedList);
					map.addAttribute("studyProtocolResourceBO", studyProtocolResourceBO);
				}
				map.addAttribute("permission", permission);
				mav = new ModelAndView("resourceListPage",map);
			}
		}catch(Exception e){
			logger.error("StudyController - getResourceList() - ERROR",e);
		}
		logger.info("StudyController - getResourceList() - Ends");
		return mav;
		
	}
	
	/**
	 * @author Pradyumn
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("null")
	@RequestMapping(value="/adminStudies/deleteResourceInfo",method = RequestMethod.POST)
	public void deleteResourceInfo(HttpServletRequest request ,HttpServletResponse response){
		logger.info("StudyController - deleteResourceInfo() - Starts");
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		String message = fdahpStudyDesignerConstants.FAILURE;
		List<ResourceBO> resourcesSavedList = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String resourceInfoId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("resourceInfoId")) == true?"":request.getParameter("resourceInfoId");
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				if(!resourceInfoId.isEmpty()){
					message = studyService.deleteResourceInfo(Integer.valueOf(resourceInfoId));
				}
				resourcesSavedList = studyService.resourcesSaved(Integer.valueOf(studyId));
				if(resourcesSavedList.size() > 0){
					jsonobject.put("resourceSaved", true);
				}else{
					jsonobject.put("resourceSaved", false);
				}
			}
			jsonobject.put("message", message);
			response.setContentType("application/json");
			out = response.getWriter();
			out.print(jsonobject);
		}catch(Exception e){
			logger.error("StudyController - deleteResourceInfo() - ERROR",e);
		}
		logger.info("StudyController - deleteConsentInfo() - Ends");
	}
	
	/**
	 * add or edit Study Resource
	 * @author Pradyumn 
	 * 
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/addOrEditResource.do")
	public ModelAndView addOrEditResource(HttpServletRequest request) {
		logger.info("StudyController - addOrEditResource() - Starts");
		ModelAndView mav = new ModelAndView("redirect:getResourceList.do");
		ModelMap map = new ModelMap();
		ResourceBO resourceBO = null;
		StudyBo studyBo = null;
		String sucMsg = "";
		String errMsg = "";
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				String resourceInfoId = (String) request.getSession().getAttribute("resourceInfoId");
				if(StringUtils.isEmpty(resourceInfoId)){
					resourceInfoId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("resourceInfoId")) == true ? "" : request.getParameter("resourceInfoId");
				}
				String studyProtocol = (String) request.getSession().getAttribute("studyProtocol");
				if(StringUtils.isEmpty(studyProtocol)){
					studyProtocol = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyProtocol")) == true ? "" : request.getParameter("studyProtocol");
				}
				String action = fdahpStudyDesignerUtil.isEmpty(request.getParameter("action")) == true ? "" : request.getParameter("action");
				if(!resourceInfoId.equals("")){
					resourceBO = studyService.getResourceInfo(Integer.parseInt(resourceInfoId));
					map.addAttribute("action", action);
					request.getSession().removeAttribute("resourceInfoId");
				}
				if(null != studyProtocol && !studyProtocol.equals("") && studyProtocol.equalsIgnoreCase("studyProtocol")){
					map.addAttribute("studyProtocol", "studyProtocol");
					request.getSession().removeAttribute("studyProtocol");
				}
				studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
				map.addAttribute("studyBo", studyBo);
				map.addAttribute("resourceBO", resourceBO);
				mav = new ModelAndView("addOrEditResourcePage",map);
			}
		} catch (Exception e) {
			logger.error("StudyController - addOrEditResource() - ERROR", e);
		}
		logger.info("StudyController - addOrEditResource() - Ends");
		return mav;
	}
	
	/**
	 * save or update Study Resource
	 * @author Pradyumn 
	 * 
	 * @param request , {@link HttpServletRequest}
	 * @param resourceBO , {@link ResourceBO}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/saveOrUpdateResource.do")
	public ModelAndView saveOrUpdateResource(HttpServletRequest request, ResourceBO resourceBO, BindingResult result) {
		logger.info("StudyController - saveOrUpdateResource() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		/*String message = fdahpStudyDesignerConstants.FAILURE;*/
		Integer resourseId = 0;
		String markCompleted = "";
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String textOrPdfParam = fdahpStudyDesignerUtil.isEmpty(request.getParameter("textOrPdfParam")) == true?"":request.getParameter("textOrPdfParam");
				String resourceVisibilityParam = fdahpStudyDesignerUtil.isEmpty(request.getParameter("resourceVisibilityParam")) == true?"":request.getParameter("resourceVisibilityParam");
				String buttonText = fdahpStudyDesignerUtil.isEmpty(request.getParameter("buttonText")) == true?"":request.getParameter("buttonText");
				String studyId = (String) request.getSession().getAttribute("studyId");
				String studyProtocol = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyProtocol")) == true ? "" : request.getParameter("studyProtocol");
				if (resourceBO != null) {
					if(!buttonText.equals("")){
						if(buttonText.equalsIgnoreCase("save")){
							resourceBO.setAction(false);
						}else if(buttonText.equalsIgnoreCase("done")){
							resourceBO.setAction(true);
						}
					}
					if(!studyProtocol.equals("") && studyProtocol.equalsIgnoreCase("studyProtocol")){
						resourceBO.setStudyProtocol(true);
					}else{
						resourceBO.setStudyProtocol(false);
					}
					resourceBO.setStudyId(Integer.parseInt(studyId));
					resourceBO.setTextOrPdf(textOrPdfParam.equals("0") ? false : true);
					resourceBO.setResourceVisibility(resourceVisibilityParam.equals("0") ? false : true);
					resourseId = studyService.saveOrUpdateResource(resourceBO, sesObj);	
				}
				if(!resourseId.equals(0)){
					if(resourceBO.getId() == null){
						if(buttonText.equalsIgnoreCase("save")){
							request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
						}else{
							request.getSession().setAttribute("sucMsg", "Resource successfully added.");
						}
					}else{
						if(buttonText.equalsIgnoreCase("save")){
							request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
						}else{
							request.getSession().setAttribute("sucMsg", "Resource successfully updated.");
						}
					}
				}else{
					if(resourceBO.getId() == null){
						request.getSession().setAttribute("errMsg", "Failed to add resource.");
					}else{
						request.getSession().setAttribute("errMsg", "Failed to update resource.");
					}
				}
				if(buttonText.equalsIgnoreCase("save")){
					/*map.addAttribute("resourceInfoId", resourseId);
					map.addAttribute("studyProtocol", studyProtocol);*/
					request.getSession().setAttribute("resourceInfoId", resourseId+"");
					request.getSession().setAttribute("studyProtocol", studyProtocol+"");
					mav = new ModelAndView("redirect:addOrEditResource.do");
				}else{
					mav = new ModelAndView("redirect:getResourceList.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - saveOrUpdateResource() - ERROR", e);
		}
		logger.info("StudyController - saveOrUpdateResource() - Ends");
		return mav;
	}
	
	/**
	 * Set Mark as completed
	 * @author Pradyumn 
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/resourceMarkAsCompleted.do")
	public ModelAndView resourceMarkAsCompleted(HttpServletRequest request) {
		logger.info("StudyController - saveOrUpdateResource() - Starts");
		ModelAndView mav = new ModelAndView("redirect:studyList.do");
		ModelMap map = new ModelMap();
		String message = fdahpStudyDesignerConstants.FAILURE;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				message = studyService.markAsCompleted(Integer.parseInt(studyId), fdahpStudyDesignerConstants.RESOURCE);	
				if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
					request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:studyList.do");
				}else{
					request.getSession().setAttribute("errMsg", "Unable to mark as complete.");
					mav = new ModelAndView("redirect:getResourceList.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - resourceMarkAsCompleted() - ERROR", e);
		}
		logger.info("StudyController - resourceMarkAsCompleted() - Ends");
		return mav;
	}
	
	 /*Study notification starts*/
	@RequestMapping("/adminStudies/viewStudyNotificationList.do")
	public ModelAndView viewStudyNotificationList(HttpServletRequest request){
		logger.info("StudyController - viewNotificationList() - Starts");
		ModelMap map = new ModelMap();
		ModelAndView mav = new ModelAndView("login", map);
		String sucMsg = "";
		String errMsg = "";
		List<NotificationBO> notificationList = null;
		List<NotificationBO> notificationSavedList = null;
		StudyBo studyBo = null;
		try{
			HttpSession session = request.getSession();
			SessionObject sessionObject = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != sessionObject){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String type = "studyNotification";
				String studyId = (String) request.getSession().getAttribute("studyId");
				String permission = (String) request.getSession().getAttribute("permission");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				if(StringUtils.isNotEmpty(studyId)){
					notificationList = notificationService.getNotificationList(Integer.valueOf(studyId) ,type);
					studyBo = studyService.getStudyById(studyId, sessionObject.getUserId());
					notificationSavedList = studyService.notificationSaved(Integer.valueOf(studyId));
					map.addAttribute("notificationList", notificationList);
					map.addAttribute("studyBo", studyBo);
					map.addAttribute("notificationSavedList", notificationSavedList);
				}
				map.addAttribute("permission", permission);
				mav = new ModelAndView("studyNotificationList", map);
			}
		}catch(Exception e){
			logger.error("StudyController - viewStudyNotificationList() - ERROR ", e);
		}
		logger.info("StudyController - viewStudyNotificationList() - ends");
		return mav;
	}
	
	@RequestMapping("/adminStudies/getStudyNotification.do")
	public ModelAndView getStudyNotification(HttpServletRequest request){
		logger.info("StudyController - getStudyNotification - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		NotificationBO notificationBO = null;
		List<NotificationHistoryBO> notificationHistoryList = null;
		StudyBo studyBo = null;
		String sucMsg = "";
		String errMsg = "";
		try{
			HttpSession session = request.getSession();
			SessionObject sessionObject = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != sessionObject){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String notificationId = (String) request.getSession().getAttribute("notificationId");
				if(StringUtils.isEmpty(notificationId)){
					notificationId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("notificationId")) == true ? "" : request.getParameter("notificationId");
				}
				String chkRefreshflag = (String) request.getSession().getAttribute("chkRefreshflag");
				if(StringUtils.isEmpty(chkRefreshflag)){
					chkRefreshflag = fdahpStudyDesignerUtil.isEmpty(request.getParameter("chkRefreshflag")) == true ? "" : request.getParameter("chkRefreshflag");
				}
				String actionType = (String) request.getSession().getAttribute("actionType");
				if(StringUtils.isEmpty(actionType)){
					actionType = fdahpStudyDesignerUtil.isEmpty(request.getParameter("actionType")) == true ? "" : request.getParameter("actionType");
				}
				//String notificationId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("notificationId")) == true?"":request.getParameter("notificationId");
				String notificationText = fdahpStudyDesignerUtil.isEmpty(request.getParameter("notificationText")) == true?"":request.getParameter("notificationText");
				//String chkRefreshflag = fdahpStudyDesignerUtil.isEmpty(request.getParameter("chkRefreshflag")) == true?"":request.getParameter("chkRefreshflag");
				//String actionType = fdahpStudyDesignerUtil.isEmpty(request.getParameter("actionType")) == true?"":request.getParameter("actionType");
				if(!"".equals(chkRefreshflag)){
					String studyId = (String) request.getSession().getAttribute("studyId");
					if(StringUtils.isEmpty(studyId)){
						studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
					}
					studyBo = studyService.getStudyById(studyId, sessionObject.getUserId());
					if(!"".equals(notificationId)){
						notificationBO = notificationService.getNotification(Integer.parseInt(notificationId));
						notificationHistoryList = notificationService.getNotificationHistoryList(Integer.parseInt(notificationId));
						/*if(notificationBO !=null && fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getNotificationSentDateTime())){
							String[] dateTime =null;
							notificationBO.setNotificationSentDateTime(fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getNotificationSentDateTime())?String.valueOf(fdahpStudyDesignerConstants.UI_SDF_DATE_TIME_AMPM.format(fdahpStudyDesignerConstants.DB_SDF_DATE_TIME_AMPM.parse(notificationBO.getNotificationSentDateTime()))):"");
							String dateAndTime = notificationBO.getNotificationSentDateTime();
							dateTime = dateAndTime.split(" ");
							String date = dateTime[0].toString(); // 8/29/2011
							String time = dateTime[1].toString() + " " + dateTime[2].toString(); // 11:16:12 AM
							notificationBO.setNotificationSentDate(date);
							notificationBO.setNotificationSentTime(time);
						}*/
						if(actionType.equals("edit")){
							notificationBO.setActionPage("edit");
						}else if(actionType.equals("resend")){
							notificationBO.setScheduleDate("");
							notificationBO.setScheduleTime("");
							notificationBO.setActionPage("resend");
						}else{
							notificationBO.setActionPage("view");
						}
						request.getSession().removeAttribute("notificationId");
						request.getSession().removeAttribute("actionType");
						request.getSession().removeAttribute("chkRefreshflag");
					}else if(!"".equals(notificationText) && "".equals(notificationId)){
						notificationBO = new NotificationBO();
						notificationBO.setNotificationText(notificationText);
						notificationBO.setActionPage("addOrCopy");
					}else if("".equals(notificationText) && "".equals(notificationId)){
						notificationBO = new NotificationBO();
						notificationBO.setActionPage("addOrCopy");
					}
					map.addAttribute("notificationBO", notificationBO);
					map.addAttribute("notificationHistoryList", notificationHistoryList);
					map.addAttribute("studyBo", studyBo);
					mav = new ModelAndView("addOrEditStudyNotification",map);
				}
				else {
					mav = new ModelAndView("redirect:viewStudyNotificationList.do");
				}
			}
		}catch(Exception e){
			logger.error("StudyController - getStudyNotification - ERROR", e);

		}
		logger.info("StudyController - getStudyNotification - Ends");
		return mav;
	}
	
	
	
	@RequestMapping("/adminStudies/saveOrUpdateStudyNotification.do")
	public ModelAndView saveOrUpdateStudyNotification(HttpServletRequest request, NotificationBO notificationBO){
		logger.info("StudyController - saveOrUpdateStudyNotification - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		Integer notificationId = 0;
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		
		try{
			HttpSession session = request.getSession();
			SessionObject sessionObject = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != sessionObject){
				String notificationType = "studyNotification";
				String currentDateTime = fdahpStudyDesignerUtil.isEmpty(request.getParameter("currentDateTime")) == true?"":request.getParameter("currentDateTime");
				String buttonType = fdahpStudyDesignerUtil.isEmpty(request.getParameter("buttonType")) == true?"":request.getParameter("buttonType");
				if(notificationBO!=null){
				if(!buttonType.equals("")){
						if(buttonType.equalsIgnoreCase("save")){
							notificationBO.setNotificationDone(false);
							notificationBO.setNotificationAction(false);
						}else if(buttonType.equalsIgnoreCase("done") || buttonType.equalsIgnoreCase("resend")){
							notificationBO.setNotificationDone(true);
							notificationBO.setNotificationAction(true);
						}
					}
					if(currentDateTime.equals("notImmediate")){
						notificationBO.setScheduleDate(fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleDate())?String.valueOf(fdahpStudyDesignerConstants.DB_SDF_DATE.format(fdahpStudyDesignerConstants.UI_SDF_DATE.parse(notificationBO.getScheduleDate()))):"");
						notificationBO.setScheduleTime(fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleTime())?String.valueOf(fdahpStudyDesignerConstants.DB_SDF_TIME.format(fdahpStudyDesignerConstants.SDF_TIME.parse(notificationBO.getScheduleTime()))):"");
						notificationBO.setNotificationScheduleType("notImmediate");
					} else if(currentDateTime.equals("immediate")){
						notificationBO.setScheduleDate(fdahpStudyDesignerUtil.getCurrentDate());
						notificationBO.setScheduleTime(fdahpStudyDesignerUtil.getCurrentTime());
						notificationBO.setNotificationScheduleType("immediate");
					} else{
						notificationBO.setScheduleDate("");
						notificationBO.setScheduleTime("");
						notificationBO.setNotificationScheduleType("0");
					}
					String studyId = (String) request.getSession().getAttribute("studyId");
					if(StringUtils.isEmpty(studyId)){
						studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
					}
					if(StringUtils.isNotEmpty(studyId)){
						notificationBO.setStudyId(Integer.valueOf(studyId));
					}
					notificationId = notificationService.saveOrUpdateNotification(notificationBO, notificationType, buttonType);
				}
				if(!notificationId.equals(0)){
					if(notificationBO.getNotificationId() == null){
						if(buttonType.equalsIgnoreCase("save")){
							request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
						}else{
							request.getSession().setAttribute("sucMsg", "Notification successfully added.");
						}
					}else{
						if(buttonType.equalsIgnoreCase("save")){
							request.getSession().setAttribute("sucMsg", propMap.get("save.study.success.message"));
						}else if(buttonType.equalsIgnoreCase("resend")){
							request.getSession().setAttribute("sucMsg", "Notification successfully resended.");
						}else{
							request.getSession().setAttribute("sucMsg", "Notification successfully updated.");
						}
					}
				}else{
					if(notificationBO.getNotificationId() == null){
						request.getSession().setAttribute("errMsg", "Failed to add notification.");
					}else{
						request.getSession().setAttribute("errMsg", "Failed to update notification.");
					}
				}
				if(buttonType.equalsIgnoreCase("save")){
					/*map.addAttribute("notificationId", notificationId);
					map.addAttribute("chkRefreshflag", "Y");
					map.addAttribute("actionType", "edit");*/
					request.getSession().setAttribute("notificationId", notificationId+"");
					request.getSession().setAttribute("chkRefreshflag", "Y"+"");
					request.getSession().setAttribute("actionType", "edit"+"");
					mav = new ModelAndView("redirect:getStudyNotification.do",map);
				}else{
					mav = new ModelAndView("redirect:/adminStudies/viewStudyNotificationList.do");
				}
			}
		}catch(Exception e){
			logger.error("StudyController - saveOrUpdateStudyNotification - ERROR", e);

		}
		logger.info("StudyController - saveOrUpdateStudyNotification - Ends");
		return mav;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/adminStudies/deleteStudyNotification.do")
	public ModelAndView deleteStudyNotification(HttpServletRequest request){
		logger.info("StudyController - deleteStudyNotification - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		ModelAndView mav = new ModelAndView();
		try{
			HttpSession session = request.getSession();
			SessionObject sessionObject = (SessionObject) session.getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			String notificationId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("notificationId")) == true?"":request.getParameter("notificationId");
			if(null != notificationId){
					message = notificationService.deleteNotification(Integer.parseInt(notificationId));
					if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
						request.getSession().setAttribute("sucMsg", "Notification successfully deleted.");
					}else{
						request.getSession().setAttribute("errMsg", "Failed to delete notification.");
					}
					mav = new ModelAndView("redirect:/adminStudies/viewStudyNotificationList.do");
			}
		}catch(Exception e){
			logger.error("StudyController - deleteStudyNotification - ERROR", e);

		}
		return mav;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping("/adminStudies/notificationMarkAsCompleted.do")
	public ModelAndView notificationMarkAsCompleted(HttpServletRequest request) {
		logger.info("StudyController - notificationMarkAsCompleted() - Starts");
		ModelAndView mav = new ModelAndView("redirect:studyList.do");
		ModelMap map = new ModelMap();
		@SuppressWarnings("unchecked")
		HashMap<String, String> propMap = fdahpStudyDesignerUtil.configMap;
		String message = fdahpStudyDesignerConstants.FAILURE;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				String markCompleted = "notification";
				message = studyService.markAsCompleted(Integer.parseInt(studyId) , markCompleted);	
				if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
					request.getSession().setAttribute("sucMsg", propMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:studyList.do");
				}else{
					request.getSession().setAttribute("errMsg", "Unable to mark as complete.");
					mav = new ModelAndView("redirect:viewStudyNotificationList.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - notificationMarkAsCompleted() - ERROR", e);
		}
		logger.info("StudyController - notificationMarkAsCompleted() - Ends");
		return mav;
	}
	
	/*Study notification ends*/
	
	/*Study CheckList Starts*/
	/**
	 * @author Pradyumn			
	 * @param request
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/getChecklist.do")
	public ModelAndView getChecklist(HttpServletRequest request){
		logger.info("StudyController - getChecklist() - Starts");
		ModelAndView mav = new ModelAndView("checklist");
		ModelMap map = new ModelMap();
		String sucMsg = "";
		String errMsg = "";
		StudyBo studyBo = null;
		Checklist checklist = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != request.getSession().getAttribute("sucMsg")){
				sucMsg = (String) request.getSession().getAttribute("sucMsg");
				map.addAttribute("sucMsg", sucMsg);
				request.getSession().removeAttribute("sucMsg");
			}
			if(null != request.getSession().getAttribute("errMsg")){
				errMsg = (String) request.getSession().getAttribute("errMsg");
				map.addAttribute("errMsg", errMsg);
				request.getSession().removeAttribute("errMsg");
			}
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				if(StringUtils.isNotEmpty(studyId)){
					checklist = studyService.getchecklistInfo(Integer.valueOf(studyId));
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					map.addAttribute("checklist", checklist);
					map.addAttribute("studyBo", studyBo);
				}
				mav = new ModelAndView("checklist",map);
			}
		}catch(Exception e){
			logger.error("StudyController - getChecklist() - ERROR",e);
		}
		logger.info("StudyController - getChecklist() - Ends");
		return mav;
		
	}
	
	/**
	 * Save or Done Checklist
	 * @author Pradyumn 
	 * 
	 * @param request , {@link HttpServletRequest}
	 * @param resourceBO , {@link ResourceBO}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/saveOrDoneChecklist.do")
	public ModelAndView saveOrDoneChecklist(HttpServletRequest request) {
		logger.info("StudyController - saveOrDoneChecklist() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				mav = new ModelAndView("redirect:getChecklist.do");
			}
		} catch (Exception e) {
			logger.error("StudyController - saveOrDoneChecklist() - ERROR", e);
		}
		logger.info("StudyController - saveOrDoneChecklist() - Ends");
		return mav;
	}
	
	
	/*@SuppressWarnings("unused")
	@RequestMapping("/adminStudies/checkListMarkAsCompleted.do")
	public ModelAndView checkListMarkAsCompleted(HttpServletRequest request) {
		logger.info("StudyController - checkListMarkAsCompleted() - Starts");
		ModelAndView mav = new ModelAndView("redirect:studyList.do");
		ModelMap map = new ModelMap();
		String message = fdahpStudyDesignerConstants.FAILURE;
		try {
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				String studyId = (String) request.getSession().getAttribute("studyId");
				if(StringUtils.isEmpty(studyId)){
					studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true ? "" : request.getParameter("studyId");
				}
				String markCompleted = "checkList";
				message = studyService.markAsCompleted(Integer.parseInt(studyId) , markCompleted);	
				if(message.equals(fdahpStudyDesignerConstants.SUCCESS)){
					request.getSession().setAttribute("sucMsg", fdahpStudyDesignerUtil.configMap.get("complete.study.success.message"));
					mav = new ModelAndView("redirect:studyList.do");
				}else{
					request.getSession().setAttribute("errMsg", "Unable to mark as complete.");
					mav = new ModelAndView("redirect:viewStudyNotificationList.do");
				}
			}
		} catch (Exception e) {
			logger.error("StudyController - checkListMarkAsCompleted() - ERROR", e);
		}
		logger.info("StudyController - checkListMarkAsCompleted() - Ends");
		return mav;
	}*/
	
	/*Study checkList ends*/
	/**
     * @author Ronalin
	 * Getting Actions
	 * @param request , {@link HttpServletRequest}
	 * @return {@link ModelAndView}
	 */
	@RequestMapping("/adminStudies/actionList.do")
	public ModelAndView actionList(HttpServletRequest request){
		logger.info("StudyController - actionList - Starts");
		ModelAndView mav = new ModelAndView("");
		ModelMap map = new ModelMap();
		String sucMsg = "";
		String errMsg = "";
		StudyBo studyBo = null;
		try{
			SessionObject sesObj = (SessionObject) request.getSession().getAttribute(fdahpStudyDesignerConstants.SESSION_OBJECT);
			if(sesObj!=null){
				if(null != request.getSession().getAttribute("sucMsg")){
					sucMsg = (String) request.getSession().getAttribute("sucMsg");
					map.addAttribute("sucMsg", sucMsg);
					request.getSession().removeAttribute("sucMsg");
				}
				if(null != request.getSession().getAttribute("errMsg")){
					errMsg = (String) request.getSession().getAttribute("errMsg");
					map.addAttribute("errMsg", errMsg);
					request.getSession().removeAttribute("errMsg");
				}
				String  studyId = fdahpStudyDesignerUtil.isEmpty(request.getParameter("studyId")) == true? "" : request.getParameter("studyId");
				if(fdahpStudyDesignerUtil.isEmpty(studyId)){
					studyId = (String) request.getSession().getAttribute("studyId");
				}
				String permission = (String) request.getSession().getAttribute("permission");
				if(fdahpStudyDesignerUtil.isNotEmpty(studyId)){
					studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
					boolean markAsComplete = true;
					if(studyBo != null && studyBo.getStudySequenceBo()!=null){
						if(!studyBo.getStudySequenceBo().isBasicInfo() &&
								!studyBo.getStudySequenceBo().isEligibility() && !studyBo.getStudySequenceBo().isSettingAdmins() 
								&& !studyBo.getStudySequenceBo().isOverView() && !studyBo.getStudySequenceBo().iseConsent() 
								&& !studyBo.getStudySequenceBo().isConsentEduInfo() 
								&& !studyBo.getStudySequenceBo().isComprehensionTest() && !studyBo.getStudySequenceBo().isStudyExcActiveTask()
								){
							markAsComplete = false;
						}
					}
					map.addAttribute("studyBo",studyBo);
					map.addAttribute("permission", permission);
					mav = new ModelAndView("actionList", map);
				}else{
					return new ModelAndView("redirect:studyList.do");
				}
		 }
		}catch(Exception e){
			logger.error("StudyController - actionList - ERROR",e);
		}
		logger.info("StudyController - actionList - Ends");
		return mav;
	}
}