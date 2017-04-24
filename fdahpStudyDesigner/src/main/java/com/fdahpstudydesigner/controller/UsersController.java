package com.fdahpstudydesigner.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bo.RoleBO;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.UserBO;
import com.fdahpstudydesigner.service.StudyService;
import com.fdahpstudydesigner.service.UsersService;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;

@Controller
public class UsersController {
	
	private static Logger logger = Logger.getLogger(UsersController.class.getName());
	
	@Autowired
	private UsersService usersService;

	@Autowired
	private StudyService studyService;
	
	/*@RequestMapping("/adminUsersEdit/getUserList.do")
	public ModelAndView getUsersList(HttpServletRequest request){
		logger.info("UsersController - getUsersList() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		List<UserBO> userList = null;
		String sucMsg = "";
		String errMsg = "";
		try{
			if(FdahpStudyDesignerUtil.isSession(request)){
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
				userList = usersService.getUserList();
				map.addAttribute("userList", userList);
				mav = new ModelAndView("userListPage",map);
			}
		}catch(Exception e){
			logger.error("UsersController - getUsersList() - ERROR",e);
		}
		logger.info("UsersController - getUsersList() - Ends");
		return mav;
	}*/
	
	@RequestMapping("/adminUsersView/getUserList.do")
	public ModelAndView getUserList(HttpServletRequest request){
		logger.info("UsersController - getUserList() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		List<UserBO> userList = null;
		String sucMsg = "";
		String errMsg = "";
		String ownUser = "";
		try{
			if(FdahpStudyDesignerUtil.isSession(request)){
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
				ownUser = (String) request.getSession().getAttribute("ownUser");
				userList = usersService.getUserList();
				map.addAttribute("userList", userList);
				map.addAttribute("ownUser", ownUser);
				mav = new ModelAndView("userListPage",map);
			}
		}catch(Exception e){
			logger.error("UsersController - getUserList() - ERROR",e);
		}
		logger.info("UsersController - getUserList() - Ends");
		return mav;
	}
	
	@RequestMapping("/adminUsersEdit/activateOrDeactivateUser.do")
	public void activateOrDeactivateUser(HttpServletRequest request,HttpServletResponse response,String userId,String userStatus) throws IOException{
		logger.info("UsersController - activateOrDeactivateUser() - Starts");
		String msg = FdahpStudyDesignerConstants.FAILURE;
		JSONObject jsonobject = new JSONObject();
		PrintWriter out = null;
		try{
			HttpSession session = request.getSession();
			SessionObject userSession = (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != userSession){
				msg = usersService.activateOrDeactivateUser(Integer.valueOf(userId), Integer.valueOf(userStatus), userSession.getUserId(),userSession);
			}
		}catch(Exception e){
			logger.error("UsersController - activateOrDeactivateUser() - ERROR",e);
		}
		logger.info("UsersController - activateOrDeactivateUser() - Ends");
		jsonobject.put("message", msg);
		response.setContentType("application/json");
		out= response.getWriter();
		out.print(jsonobject);
	}
	
	@RequestMapping("/adminUsersEdit/addOrEditUserDetails.do")
	public ModelAndView addOrEditUserDetails(HttpServletRequest request){
		logger.info("UsersController - addOrEditUserDetails() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		UserBO userBO = null;
		List<StudyListBean> studyBOs = null;
		List<RoleBO> roleBOList = null;
		List<StudyBo> studyBOList = null;
		String actionPage = "";
		List<Integer> permissions = null;
		int usrId = 0;
		try{
			if(FdahpStudyDesignerUtil.isSession(request)){
				String userId = FdahpStudyDesignerUtil.isEmpty(request.getParameter("userId")) == true ? "" : request.getParameter("userId");
				String checkRefreshFlag = FdahpStudyDesignerUtil.isEmpty(request.getParameter("checkRefreshFlag")) == true ? "" : request.getParameter("checkRefreshFlag");
				if(!"".equalsIgnoreCase(checkRefreshFlag)){
					if(!userId.equals("")){
						usrId = Integer.valueOf(userId);
					}
					if(!"".equals(userId)){
						actionPage = FdahpStudyDesignerConstants.EDIT_PAGE;
						userBO = usersService.getUserDetails(usrId);
						if(null != userBO){
							studyBOs = studyService.getStudyList(userBO.getUserId());
							permissions = usersService.getPermissionsByUserId(userBO.getUserId());
						}
					}else{
						actionPage = FdahpStudyDesignerConstants.ADD_PAGE;
					}
					roleBOList = usersService.getUserRoleList();
					studyBOList = studyService.getStudies(usrId);
					map.addAttribute("actionPage", actionPage);
					map.addAttribute("userBO", userBO);
					map.addAttribute("permissions", permissions);
					map.addAttribute("roleBOList", roleBOList);
					map.addAttribute("studyBOList", studyBOList);
					map.addAttribute("studyBOs", studyBOs);
					mav = new ModelAndView("addOrEditUserPage",map);
				}else{
					mav = new ModelAndView("redirect:/adminUsersView/getUserList.do");
				}
			}
		}catch(Exception e){
			logger.error("UsersController - addOrEditUserDetails() - ERROR",e);
		}
		logger.info("UsersController - addOrEditUserDetails() - Ends");
		return mav;
	}
	
	@RequestMapping("/adminUsersView/viewUserDetails.do")
	public ModelAndView viewUserDetails(HttpServletRequest request){
		logger.info("UsersController - viewUserDetails() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		UserBO userBO = null;
		List<StudyListBean> studyBOs = null;
		List<RoleBO> roleBOList = null;
		List<StudyBo> studyBOList = null;
		String actionPage = FdahpStudyDesignerConstants.VIEW_PAGE;
		List<Integer> permissions = null;
		try{
			if(FdahpStudyDesignerUtil.isSession(request)){
				String userId = FdahpStudyDesignerUtil.isEmpty(request.getParameter("userId")) == true ? "" : request.getParameter("userId");
				String checkViewRefreshFlag = FdahpStudyDesignerUtil.isEmpty(request.getParameter("checkViewRefreshFlag")) == true ? "" : request.getParameter("checkViewRefreshFlag");
				if(!"".equalsIgnoreCase(checkViewRefreshFlag)){
					if(!"".equals(userId)){
						userBO = usersService.getUserDetails(Integer.valueOf(userId));
						if(null != userBO){
							studyBOs = studyService.getStudyList(userBO.getUserId());
							permissions = usersService.getPermissionsByUserId(userBO.getUserId());
						}
					}
					roleBOList = usersService.getUserRoleList();
					studyBOList = studyService.getStudies(Integer.valueOf(userId));
					map.addAttribute("actionPage", actionPage);
					map.addAttribute("userBO", userBO);
					map.addAttribute("permissions", permissions);
					map.addAttribute("roleBOList", roleBOList);
					map.addAttribute("studyBOList", studyBOList);
					map.addAttribute("studyBOs", studyBOs);
					mav = new ModelAndView("addOrEditUserPage",map);
				}else{
					mav = new ModelAndView("redirect:getUserList.do");
				}
			}
		}catch(Exception e){
			logger.error("UsersController - viewUserDetails() - ERROR",e);
		}
		logger.info("UsersController - viewUserDetails() - Ends");
		return mav;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping("/adminUsersEdit/addOrUpdateUserDetails.do")
	public ModelAndView addOrUpdateUserDetails(HttpServletRequest request,UserBO userBO, BindingResult result){
		logger.info("UsersController - addOrUpdateUserDetails() - Starts");
		ModelAndView mav = new ModelAndView();
		ModelMap map = new ModelMap();
		String msg = "";
		String permissions = "";
		int count = 1;
		List<Integer> permissionList = new ArrayList<>();
		boolean addFlag = false;
		HashMap<String, String> propMap = FdahpStudyDesignerUtil.configMap;
		try{
			HttpSession session = request.getSession();
			SessionObject userSession = (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != userSession){
				String manageUsers = FdahpStudyDesignerUtil.isEmpty(request.getParameter("manageUsers")) == true ? "" : request.getParameter("manageUsers");
				String manageNotifications = FdahpStudyDesignerUtil.isEmpty(request.getParameter("manageNotifications")) == true ? "" : request.getParameter("manageNotifications");
				String manageStudies = FdahpStudyDesignerUtil.isEmpty(request.getParameter("manageStudies")) == true ? "" : request.getParameter("manageStudies");
				String addingNewStudy = FdahpStudyDesignerUtil.isEmpty(request.getParameter("addingNewStudy")) == true ? "" : request.getParameter("addingNewStudy");
				String selectedStudies = FdahpStudyDesignerUtil.isEmpty(request.getParameter("selectedStudies")) == true ? "" : request.getParameter("selectedStudies");
				String permissionValues = FdahpStudyDesignerUtil.isEmpty(request.getParameter("permissionValues")) == true ? "" : request.getParameter("permissionValues");
				String ownUser = FdahpStudyDesignerUtil.isEmpty(request.getParameter("ownUser")) == true ? "" : request.getParameter("ownUser");
				if(null == userBO.getUserId()){
					addFlag = true;
					userBO.setCreatedBy(userSession.getUserId());
					userBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
				}else{
					addFlag = false;
					userBO.setModifiedBy(userSession.getUserId());
					userBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
				}
				if(!"".equals(manageUsers)){
					if("0".equals(manageUsers)){
						permissions += count > 1 ?(",'ROLE_MANAGE_USERS_VIEW'"):"'ROLE_MANAGE_USERS_VIEW'";
						count++;
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_USERS_VIEW);
					}else if("1".equals(manageUsers)){
						permissions += count > 1 ?(",'ROLE_MANAGE_USERS_VIEW'"):"'ROLE_MANAGE_USERS_VIEW'";
						count++;
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_USERS_VIEW);
						permissions += count > 1 ?(",'ROLE_MANAGE_USERS_EDIT'"):"'ROLE_MANAGE_USERS_EDIT'";
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_USERS_EDIT);
					}
				}
				if(!"".equals(manageNotifications)){
					if("0".equals(manageNotifications)){
						permissions += count > 1 ?(",'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW'"):"'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW'";
						count++;
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW);
					}else if("1".equals(manageNotifications)){
						permissions += count > 1 ?(",'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW'"):"'ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW'";
						count++;
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_APP_WIDE_NOTIFICATION_VIEW);
						permissions += count > 1 ?(",'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT'"):"'ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT'";
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_APP_WIDE_NOTIFICATION_EDIT);
					}
				}
				if(!"".equals(manageStudies)){
					if("1".equals(manageStudies)){
						permissions += count > 1 ?(",'ROLE_MANAGE_STUDIES'"):"'ROLE_MANAGE_STUDIES'";
						count++;
						permissionList.add(FdahpStudyDesignerConstants.ROLE_MANAGE_STUDIES);
						if(!"".equals(addingNewStudy) && "1".equals(addingNewStudy)){
								permissions += count > 1 ?(",'ROLE_CREATE_MANAGE_STUDIES'"):"'ROLE_CREATE_MANAGE_STUDIES'";
								permissionList.add(FdahpStudyDesignerConstants.ROLE_CREATE_MANAGE_STUDIES);
						}
					}else{
						selectedStudies = "";
						permissionValues = "";
					}
				}else{
					selectedStudies = "";
					permissionValues = "";
				}
				msg = usersService.addOrUpdateUserDetails(request,userBO,permissions,permissionList,selectedStudies,permissionValues,userSession);
				if (FdahpStudyDesignerConstants.SUCCESS.equals(msg)) {
					if(addFlag){
						request.getSession().setAttribute("sucMsg",	propMap.get("add.user.success.message"));
					}else{
						request.getSession().setAttribute("ownUser", ownUser);
						request.getSession().setAttribute("sucMsg",	propMap.get("update.user.success.message"));
					}
				} else  {
					request.getSession().setAttribute("errMsg",	propMap.get("addUpdate.user.error.message"));
				}
				mav = new ModelAndView("redirect:/adminUsersView/getUserList.do");
			}
		}catch(Exception e){
			logger.error("UsersController - addOrUpdateUserDetails() - ERROR",e);
		}
		logger.info("UsersController - addOrUpdateUserDetails() - Ends");
		return mav;
	}
	
	@RequestMapping("/adminUsersEdit/forceLogOut.do")
	public ModelAndView forceLogOut(HttpServletRequest request){
		logger.info("UsersController - forceLogOut() - Starts");
		ModelAndView mav = new ModelAndView();
		String msg = FdahpStudyDesignerConstants.FAILURE;
		try{
			HttpSession session = request.getSession();
			SessionObject userSession = (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
			if(null != userSession){
				msg = usersService.forceLogOut(userSession);
				if(msg.equals(FdahpStudyDesignerConstants.SUCCESS)){
					mav = new ModelAndView("loginPage");
				}
			}
			if(null != request.getSession().getAttribute("ownUser")){
				request.getSession().removeAttribute("ownUser");
			}
		}catch(Exception e){
			logger.error("UsersController - forceLogOut() - ERROR",e);
		}
		logger.info("UsersController - forceLogOut() - Ends");
		return mav;
	}
}