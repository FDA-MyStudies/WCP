package com.fdahpstudydesigner.controller;

import com.fdahpstudydesigner.bean.StudyIdBean;
import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bean.StudyPageBean;
import com.fdahpstudydesigner.bean.StudySessionBean;
import com.fdahpstudydesigner.bo.AnchorDateTypeBo;
import com.fdahpstudydesigner.bo.Checklist;
import com.fdahpstudydesigner.bo.ComprehensionQuestionLangBO;
import com.fdahpstudydesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpstudydesigner.bo.ConsentBo;
import com.fdahpstudydesigner.bo.ConsentInfoBo;
import com.fdahpstudydesigner.bo.ConsentInfoLangBO;
import com.fdahpstudydesigner.bo.ConsentMasterInfoBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.EligibilityTestBo;
import com.fdahpstudydesigner.bo.EligibilityTestLangBo;
import com.fdahpstudydesigner.bo.MultiLanguageCodes;
import com.fdahpstudydesigner.bo.NotificationBO;
import com.fdahpstudydesigner.bo.NotificationHistoryBO;
import com.fdahpstudydesigner.bo.NotificationLangBO;
import com.fdahpstudydesigner.bo.ParticipantPropertiesBO;
import com.fdahpstudydesigner.bo.ReferenceTablesBo;
import com.fdahpstudydesigner.bo.ResourceBO;
import com.fdahpstudydesigner.bo.ResourcesLangBO;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyLanguageBO;
import com.fdahpstudydesigner.bo.StudyPageBo;
import com.fdahpstudydesigner.bo.StudyPageLanguageBO;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.bo.StudySequenceBo;
import com.fdahpstudydesigner.bo.StudySequenceLangBO;
import com.fdahpstudydesigner.bo.UserBO;
import com.fdahpstudydesigner.service.NotificationService;
import com.fdahpstudydesigner.service.StudyQuestionnaireService;
import com.fdahpstudydesigner.service.StudyService;
import com.fdahpstudydesigner.service.UsersService;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.Test;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/** @author BTC */
@Controller
public class StudyController {

  private static Logger logger = LogManager.getLogger(StudyController.class.getName());

  @Autowired private NotificationService notificationService;

  @Autowired private StudyQuestionnaireService studyQuestionnaireService;

  @Autowired private StudyService studyService;

  @Autowired private UsersService usersService;

  /**
   * This method shows the following actions(Publish (as Upcoming Study) Start / Launch or Publish
   * Updates Pause or Resume Deactivate) of a study
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/actionList.do")
  public ModelAndView actionList(HttpServletRequest request) {
    logger.info("StudyController - actionList - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    String errMsg = "";
    StudyBo studyBo = null;
    StudyBo liveStudyBo = null;
    String actionSucMsg = "";
    StudyPermissionBO studyPermissionBO = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_SUC_MSG)) {
          actionSucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG, actionSucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (FdahpStudyDesignerUtil.isEmpty(studyId)) {
          studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        }
        // String permission = (String)
        // request.getSession().getAttribute(FdahpStudyDesignerConstants.PERMISSION);
        String permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          liveStudyBo = studyService.getStudyLiveStatusByCustomId(studyBo.getCustomStudyId());
          studyPermissionBO =
              studyService.findStudyPermissionBO(studyBo.getId(), sesObj.getUserId());
          if (studyBo.getStudyMode() == null) {
            map.addAttribute("studyMode", FdahpStudyDesignerConstants.STUDY_MODE_LIVE);
          } else {
            map.addAttribute("studyMode", studyBo.getStudyMode());
          }
          map.addAttribute("_S", sessionStudyCount);
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
          map.addAttribute("liveStudyBo", liveStudyBo);
          map.addAttribute("studyPermissionBO", studyPermissionBO);
          String language = request.getParameter("language");
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute("currLanguage", language);
          mav = new ModelAndView("actionList", map);
        } else {
          return new ModelAndView("redirect:/adminStudies/studyList.do");
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - actionList - ERROR", e);
    }
    logger.info("StudyController - actionList - Ends");
    return mav;
  }

  /**
   * This method is used add or edit Study Resource
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/addOrEditResource.do")
  public ModelAndView addOrEditResource(HttpServletRequest request) {
    logger.info("StudyController - addOrEditResource() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    ResourceBO resourceBO = null;
    StudyBo studyBo = null;
    String sucMsg = "";
    String errMsg = "";
    List<AnchorDateTypeBo> anchorTypeList = new ArrayList<>();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String resourceInfoId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.RESOURCE_INFO_ID);
        if (StringUtils.isEmpty(resourceInfoId)) {
          resourceInfoId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.RESOURCE_INFO_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.RESOURCE_INFO_ID);
        }
        String studyProtocol =
            (String)
                request
                    .getSession()
                    .getAttribute(
                        sessionStudyCount + FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL);
        if (StringUtils.isEmpty(studyProtocol)) {
          studyProtocol =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL);
        }
        String action =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_ON);
        if (StringUtils.isEmpty(action)) {
          action =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.ACTION_ON))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.ACTION_ON);
        }
        map.addAttribute("_S", sessionStudyCount);
        String language = request.getParameter("language");
        if (!FdahpStudyDesignerUtil.isEmpty(action)) {
          if (!("").equals(resourceInfoId)) {
            resourceBO = studyService.getResourceInfo(Integer.parseInt(resourceInfoId));
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.RESOURCE_INFO_ID);
          }
          if ((null != studyProtocol)
              && !("").equals(studyProtocol)
              && (FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL).equalsIgnoreCase(studyProtocol)) {
            map.addAttribute(
                FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL,
                FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL);
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL);
          }
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute("resourceBO", resourceBO);
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            map.addAttribute(
                "resourceLangBO", studyService.getResourceLangInfo(resourceInfoId, language));
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute(FdahpStudyDesignerConstants.ACTION_ON, action);
          if ((studyBo != null) && !studyBo.getCustomStudyId().isEmpty()) {
            anchorTypeList =
                studyQuestionnaireService.getAnchorTypesByStudyId(studyBo.getCustomStudyId());
            String languages = studyBo.getSelectedLanguages();
            List<String> langList = new ArrayList<>();
            Map<String, String> langMap = new HashMap<>();
            if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
              langList = Arrays.asList(languages.split(","));
              for (String string : langList) {
                langMap.put(string, MultiLanguageCodes.getValue(string));
              }
            }
            map.addAttribute("languageList", langMap);
          }
          map.addAttribute("currLanguage", language);

          map.addAttribute("anchorTypeList", anchorTypeList);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_ON);
          mav = new ModelAndView("addOrEditResourcePage", map);
        } else {
          mav = new ModelAndView("redirect:getResourceList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - addOrEditResource() - ERROR", e);
    }
    logger.info("StudyController - addOrEditResource() - Ends");
    return mav;
  }

  /**
   * Making the comprehension test question creating completed in comprehension list page
   *
   * @author BTC
   * @param request {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/comprehensionTestMarkAsCompleted.do")
  public ModelAndView comprehensionTestMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - comprehensionTestMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:studyList.do");
    ModelMap map = new ModelMap();
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId),
                FdahpStudyDesignerConstants.COMPREHENSION_TEST,
                sesObj,
                customStudyId,
                language);
        map.addAttribute("_S", sessionStudyCount);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          map.put("language", language);
          mav = new ModelAndView("redirect:consentReview.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          mav = new ModelAndView("redirect:comprehensionQuestionList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - comprehensionTestMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - comprehensionTestMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * This allow the admin make as the consent creation completed those will appear in mobile screen
   * by screen before start of an study.
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/consentMarkAsCompleted.do")
  public ModelAndView consentMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - consentMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:studyList.do");
    ModelMap map = new ModelMap();
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId),
                FdahpStudyDesignerConstants.CONESENT,
                sesObj,
                customStudyId,
                language);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          map.addAttribute("_S", sessionStudyCount);
          map.put("language", language);
          mav = new ModelAndView("redirect:comprehensionQuestionList.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          map.addAttribute("_S", sessionStudyCount);
          mav = new ModelAndView("redirect:consentListPage.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - consentMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - consentMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * This allow the admin to make as complete the consent review screen which contains the shara
   * data permission consent document for review and e-consent form
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/consentReviewMarkAsCompleted.do")
  public ModelAndView consentReviewMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - consentReviewMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:studyList.do");
    ModelMap map = new ModelMap();
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId),
                FdahpStudyDesignerConstants.CONESENT_REVIEW,
                sesObj,
                customStudyId,
                language);
        map.addAttribute("_S", sessionStudyCount);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          map.put("language", language);
          mav = new ModelAndView("redirect:participantPropertiesPage.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          mav = new ModelAndView("redirect:consentReview.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - consentReviewMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - consentReviewMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * used to create copy of live study as new study
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   */
  @RequestMapping("/adminStudies/crateNewStudy.do")
  public ModelAndView crateNewStudy(HttpServletRequest request) {
    logger.info("StudyController - crateNewStudy() - Starts");
    new ModelMap();
    ModelAndView modelAndView = new ModelAndView("redirect:/adminStudies/studyList.do");
    boolean flag = false;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      String customStudyId =
          FdahpStudyDesignerUtil.isEmpty(
                  request.getParameter(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID))
              ? ""
              : request.getParameter(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
      if (StringUtils.isNotEmpty(customStudyId) && (sesObj != null)) {
        flag = studyService.copyliveStudyByCustomStudyId(customStudyId, sesObj);
      }
      if (flag) {
        request
            .getSession()
            .setAttribute(
                FdahpStudyDesignerConstants.ACTION_SUC_MSG,
                FdahpStudyDesignerConstants.COPY_STUDY_SUCCESS_MSG);
      } else {
        request
            .getSession()
            .setAttribute(
                FdahpStudyDesignerConstants.ERR_MSG,
                FdahpStudyDesignerConstants.COPY_STUDY_FAILURE_MSG);
      }
    } catch (Exception e) {
      logger.error("StudyController - crateNewStudy - ERROR", e);
    }
    logger.info("StudyController - crateNewStudy() - Ends");
    return modelAndView;
  }

  /**
   * Deleting the comprehension test question from the list of comprehension questions
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/deleteComprehensionQuestion.do")
  public void deleteComprehensionTestQuestion(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - deleteComprehensionTestQuestion - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (sesObj != null) {
        String comprehensionQuestionId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID);
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isNotEmpty(comprehensionQuestionId) && StringUtils.isNotEmpty(studyId)) {
          message =
              studyService.deleteComprehensionTestQuestion(
                  Integer.valueOf(comprehensionQuestionId), Integer.valueOf(studyId), sesObj);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - deleteComprehensionTestQuestion - ERROR", e);
    }
    logger.info("StudyController - deleteComprehensionTestQuestion - Ends");
  }

  /**
   * Deleting the consent info from the list consent of an study
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return void
   */
  @RequestMapping(value = "/adminStudies/deleteConsentInfo.do", method = RequestMethod.POST)
  public void deleteConsentInfo(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - deleteConsentInfo - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String consentInfoId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.CONSENT_INFO_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.CONSENT_INFO_ID);
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        if (!consentInfoId.isEmpty() && !studyId.isEmpty()) {
          message =
              studyService.deleteConsentInfo(
                  Integer.valueOf(consentInfoId), Integer.valueOf(studyId), sesObj, customStudyId);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - deleteConsentInfo - ERROR", e);
    }
    logger.info("StudyController - deleteConsentInfo - Ends");
  }

  /**
   * @author BTC
   * @param request
   * @param response Description : delete the eligibility question and answer
   */
  @RequestMapping(
      value = "/adminStudies/deleteEligibiltyTestQusAns.do",
      method = RequestMethod.POST)
  public void deleteEligibiltyTestQusAns(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - deleteEligibiltyTestQusAns - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String customStudyId = "";
    List<EligibilityTestBo> testBos;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray eligibilityTestJsonArray = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String eligibilityTestId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("eligibilityTestId"))
                ? "0"
                : request.getParameter("eligibilityTestId");
        String eligibilityId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("eligibilityId"))
                ? "0"
                : request.getParameter("eligibilityId");
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        message =
            studyService.deleteEligibilityTestQusAnsById(
                Integer.parseInt(eligibilityTestId),
                Integer.parseInt(studyId),
                sesObj,
                customStudyId);
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          testBos =
              studyService.viewEligibilityTestQusAnsByEligibilityId(
                  Integer.parseInt(eligibilityId));
          if ((testBos != null) && !testBos.isEmpty()) {
            eligibilityTestJsonArray = new JSONArray(mapper.writeValueAsString(testBos));
          }
          jsonobject.put("eligibiltyTestList", eligibilityTestJsonArray);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - deleteEligibiltyTestQusAns - ERROR", e);
    }
    logger.info("StudyController - deleteEligibiltyTestQusAns - Ends");
  }

  /**
   * This method is used to delete the resource
   *
   * @author BTC
   * @param request , {@link HttpServletResponse}
   * @param response , {@link HttpServletResponse}
   */
  @RequestMapping(value = "/adminStudies/deleteResourceInfo.do", method = RequestMethod.POST)
  public void deleteResourceInfo(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - deleteResourceInfo() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    List<ResourceBO> resourcesSavedList = null;
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String resourceInfoId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.RESOURCE_INFO_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.RESOURCE_INFO_ID);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        if (!resourceInfoId.isEmpty()) {
          message =
              studyService.deleteResourceInfo(
                  Integer.valueOf(resourceInfoId), sesObj, customStudyId, Integer.valueOf(studyId));
        }
        resourcesSavedList = studyService.resourcesSaved(Integer.valueOf(studyId));
        if (!resourcesSavedList.isEmpty()) {
          jsonobject.put("resourceSaved", true);
        } else {
          jsonobject.put("resourceSaved", false);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - deleteResourceInfo() - ERROR", e);
    }
    logger.info("StudyController - deleteConsentInfo() - Ends");
  }

  /**
   * delete study by customStudyId
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   */
  @RequestMapping("/deleteStudy.do")
  public ModelAndView deleteStudy(HttpServletRequest request) {
    logger.info("StudyController - deleteStudy - Starts");
    ModelAndView mav = new ModelAndView("redirect:login.do");
    boolean flag = false;
    try {

      String cusId =
          FdahpStudyDesignerUtil.isEmpty(request.getParameter("cusId"))
              ? ""
              : request.getParameter("cusId");
      if (!cusId.isEmpty()) {
        flag = studyService.deleteStudyByCustomStudyId(cusId);
        if (flag) {
          request.getSession(false).setAttribute("sucMsg", "deleted successfully");
        } else {
          request.getSession(false).setAttribute("errMsg", "DB issue or study does not exist");
        }
      }

    } catch (Exception e) {
      logger.error("StudyController - deleteStudy - ERROR", e);
    }
    logger.info("StudyController - deleteStudy - Ends");
    return mav;
  }

  /**
   * Description : Soft delete study notification details
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/deleteStudyNotification.do")
  public ModelAndView deleteStudyNotification(HttpServletRequest request) {
    logger.info("StudyController - deleteStudyNotification - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    ModelMap map = new ModelMap();
    try {
      HttpSession session = request.getSession();
      SessionObject sessionObject =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sessionObject != null)
          && (sessionObject.getStudySession() != null)
          && sessionObject.getStudySession().contains(sessionStudyCount)) {
        String notificationId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.NOTIFICATIONID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.NOTIFICATIONID);
        if (null != notificationId) {
          String notificationType = FdahpStudyDesignerConstants.STUDYLEVEL;
          message =
              notificationService.deleteNotification(
                  Integer.parseInt(notificationId), sessionObject, notificationType);
          if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get("delete.notification.success.message"));
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    propMap.get("delete.notification.error.message"));
          }
          map.addAttribute("_S", sessionStudyCount);
          mav = new ModelAndView("redirect:/adminStudies/viewStudyNotificationList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - deleteStudyNotification - ERROR", e);
    }
    return mav;
  }

  /**
   * to download the pdf
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return {@link ModelAndView}
   * @throws IOException
   */
  @RequestMapping(value = "/downloadPdf.do")
  public ModelAndView downloadPdf(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.info("StudyController - downloadPdf - Starts");
    Map<String, String> configMap = FdahpStudyDesignerUtil.getAppProperties();
    InputStream is = null;
    SessionObject sesObj =
        (SessionObject)
            request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
    Integer sessionStudyCount =
        StringUtils.isNumeric(request.getParameter("_S"))
            ? Integer.parseInt(request.getParameter("_S"))
            : 0;
    ModelAndView mav = null;
    try {
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String fileName =
            (request.getParameter("fileName")) == null ? "" : request.getParameter("fileName");
        String fileFolder =
            (request.getParameter("fileFolder")) == null ? "" : request.getParameter("fileFolder");
        if (StringUtils.isNotBlank(fileName) && StringUtils.isNotBlank(fileFolder)) {
          request.getSession().setAttribute(sessionStudyCount + "fileName", fileName);
          request.getSession().setAttribute(sessionStudyCount + "fileFolder", fileFolder);
        } else {
          fileName = (String) request.getSession().getAttribute(sessionStudyCount + "fileName");
          fileFolder = (String) request.getSession().getAttribute(sessionStudyCount + "fileFolder");
        }
        String currentPath =
            configMap.get("fda.currentPath") != null
                ? System.getProperty(configMap.get("fda.currentPath"))
                : "";
        String rootPath = currentPath.replace('\\', '/') + configMap.get("fda.imgUploadPath");
        File pdfFile = new File(rootPath + fileFolder + "/" + fileName);
        is = new FileInputStream(pdfFile);
        response.setContentType("application/pdf");
        response.setContentLength((int) pdfFile.length());
        response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
      } else {
        mav = new ModelAndView("redirect:studyList.do");
      }
    } catch (Exception e) {
      logger.error("StudyController - downloadPdf() - ERROR", e);
    } finally {
      if (null != is) {
        is.close();
      }
    }
    logger.info("StudyController - downloadPdf() - Starts");
    return mav;
  }

  /**
   * This method is used to get checklist data
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/getChecklist.do")
  public ModelAndView getChecklist(HttpServletRequest request) {
    logger.info("StudyController - getChecklist() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    String sucMsg = "";
    String errMsg = "";
    StudyBo studyBo = null;
    Checklist checklist = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        map.addAttribute("_S", sessionStudyCount);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String language = request.getParameter("language");
        if (StringUtils.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          checklist = studyService.getchecklistInfo(Integer.valueOf(studyId));
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            this.setStudyLangData(studyId, language, map);
          }
        }
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
        map.addAttribute("checklist", checklist);
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("currLanguage", language);
        mav = new ModelAndView("checklist", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getChecklist() - ERROR", e);
    }
    logger.info("StudyController - getChecklist() - Ends");
    return mav;
  }

  /**
   * Study consent have the 0 or more comprehension test question to check the understanding of the
   * consent to a participant here will show the list comprehension test question of consent of a
   * study
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/comprehensionQuestionList.do")
  public ModelAndView getComprehensionQuestionList(HttpServletRequest request) {
    logger.info("StudyController - getComprehensionQuestionList - Starts");
    ModelAndView mav = new ModelAndView("comprehensionListPage");
    ModelMap map = new ModelMap();
    StudyBo studyBo = null;
    ConsentBo consentBo = null;
    String sucMsg = "";
    String errMsg = "";
    String consentStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        request
            .getSession()
            .removeAttribute(
                sessionStudyCount + FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        // Added for live version Start
        String isLive =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE);
        if (StringUtils.isNotEmpty(isLive)
            && isLive.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
          consentStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_STUDY_ID);
        }
        // Added for live version End
        if (StringUtils.isNotEmpty(studyId)) {
          if (StringUtils.isNotEmpty(consentStudyId)) {
            consentBo = studyService.getConsentDetailsByStudyId(consentStudyId);
          } else {
            consentBo = studyService.getConsentDetailsByStudyId(studyId);
          }
          comprehensionTestQuestionList =
              studyService.getComprehensionTestQuestionList(Integer.valueOf(studyId));
          boolean markAsComplete = true;
          if ((comprehensionTestQuestionList != null) && !comprehensionTestQuestionList.isEmpty()) {
            for (ComprehensionTestQuestionBo comprehensionTestQuestionBo :
                comprehensionTestQuestionList) {
              if (!comprehensionTestQuestionBo.getStatus()) {
                markAsComplete = false;
                break;
              }
            }
          }
          map.addAttribute("markAsComplete", markAsComplete);
          map.addAttribute("comprehensionTestQuestionList", comprehensionTestQuestionList);

          String language = request.getParameter("language");
          if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
            List<ComprehensionQuestionLangBO> questionLangBOList =
                studyService.getComprehensionTestQuestionLangList(
                    comprehensionTestQuestionList, language);
            if (questionLangBOList != null && !questionLangBOList.isEmpty())
              map.addAttribute("comprehensionQuestionLangList", questionLangBOList);
            else
              map.addAttribute(
                  "comprehensionQuestionLangList", new ArrayList<ComprehensionQuestionLangBO>());
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute("currLanguage", language);

          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);

          // get consentId if exists for studyId
          if (consentBo != null) {
            request
                .getSession()
                .setAttribute(FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
            map.addAttribute(FdahpStudyDesignerConstants.CONESENT, consentBo.getId());
            map.addAttribute("consentBo", consentBo);
          }
        }
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_ID, studyId);
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView(FdahpStudyDesignerConstants.COMPREHENSION_LIST_PAGE, map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getComprehensionQuestionList - ERROR", e);
    }
    logger.info("StudyController - getComprehensionQuestionList - Ends");
    return mav;
  }

  /**
   * Study Consent have the 0 or more comprehension test question.Each question contains the
   * question text and answers and make correct response to the question as being ANY or ALL of the
   * 'correct' answer options.
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/comprehensionQuestionPage.do")
  public ModelAndView getComprehensionQuestionPage(HttpServletRequest request) {
    logger.info("StudyController - getConsentPage - Starts");
    ModelAndView mav = new ModelAndView("comprehensionQuestionPage");
    ModelMap map = new ModelMap();
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    ComprehensionQuestionLangBO comprehensionQuestionLangBO = null;
    String sucMsg = "";
    String errMsg = "";
    StudyBo studyBo = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String comprehensionQuestionId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID);
        String actionType =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE);
        String studyId =
            (String) request.getSession().getAttribute(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
          request.getSession().setAttribute(FdahpStudyDesignerConstants.STUDY_ID, studyId);
        }
        if (StringUtils.isEmpty(comprehensionQuestionId)) {
          comprehensionQuestionId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount
                              + FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID);
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID,
                  comprehensionQuestionId);
        }
        if (StringUtils.isNotEmpty(studyId)) {
          if (("view").equals(actionType)) {
            map.addAttribute(FdahpStudyDesignerConstants.ACTION_PAGE, "view");
          } else {
            map.addAttribute(FdahpStudyDesignerConstants.ACTION_PAGE, "addEdit");
          }
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);

          map.addAttribute("_S", sessionStudyCount);
        }
        if (StringUtils.isNotEmpty(comprehensionQuestionId)) {
          comprehensionTestQuestionBo =
              studyService.getComprehensionTestQuestionById(
                  Integer.valueOf(comprehensionQuestionId));

          String language = request.getParameter("language");
          map.addAttribute("currLanguage", language);
          if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
            String result =
                studyService.syncQuestionDataInLanguageTables(
                    comprehensionTestQuestionBo, language);
            if (FdahpStudyDesignerConstants.SUCCESS.equals(result)) {
              comprehensionQuestionLangBO =
                  studyService.getComprehensionQuestionLangById(
                      Integer.parseInt(comprehensionQuestionId), language);
            }
            this.setStudyLangData(studyId, language, map);
          }

          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.COMPREHENSION_QUESTION_ID,
                  comprehensionQuestionId);
        }
        map.addAttribute("comprehensionQuestionBo", comprehensionTestQuestionBo);
        map.addAttribute("comprehensionQuestionLangBO", comprehensionQuestionLangBO);
        mav = new ModelAndView("comprehensionQuestionPage", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getConsentPage - Error", e);
    }
    logger.info("StudyController - getConsentPage - Ends");
    return mav;
  }

  /**
   * Study can have the 1 or more consent items which are appeared as consent items in mobile
   * app.Before joining the study participant has to go through these consent items
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/consentListPage.do")
  public ModelAndView getConsentListPage(HttpServletRequest request) {
    logger.info("StudyController - getConsentListPage - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    StudyBo studyBo = null;
    ConsentBo consentBo = null;
    String sucMsg = "";
    String errMsg = "";
    String consentStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        List<ConsentInfoBo> consentInfoList;
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        // Added for live version Start
        String isLive =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE);
        if (StringUtils.isNotEmpty(isLive)
            && isLive.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
          consentStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_STUDY_ID);
        }
        // Added for live version End
        if (StringUtils.isNotEmpty(studyId)) {
          boolean markAsComplete = true;
          if (StringUtils.isNotEmpty(consentStudyId)) {
            consentInfoList = studyService.getConsentInfoList(Integer.valueOf(consentStudyId));
          } else {
            consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
          }

          String currLang = request.getParameter("language");
          List<ConsentInfoLangBO> consentInfoLangList = new ArrayList<>();
          if ((consentInfoList != null) && !consentInfoList.isEmpty()) {
            for (ConsentInfoBo conInfoBo : consentInfoList) {
              if (!conInfoBo.getStatus()) {
                markAsComplete = false;
                break;
              }
            }
            if (FdahpStudyDesignerUtil.isNotEmpty(currLang) && !"en".equals(currLang)) {
              consentInfoLangList =
                  studyService.syncConsentDataInLanguageTable(consentInfoList, currLang);
              this.setStudyLangData(studyId, currLang, map);
            }
          }
          if (FdahpStudyDesignerUtil.isNotEmpty(currLang) && !"en".equals(currLang)) {
            this.setStudyLangData(studyId, currLang, map);
          }
          map.addAttribute("currLanguage", currLang);
          map.addAttribute("consentInfoLangList", consentInfoLangList);
          map.addAttribute("markAsComplete", markAsComplete);
          map.addAttribute(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, consentInfoList);
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_ID, studyId);
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);

          // get consentbo details by studyId
          if (StringUtils.isNotEmpty(consentStudyId)) {
            consentBo = studyService.getConsentDetailsByStudyId(consentStudyId);
          } else {
            consentBo = studyService.getConsentDetailsByStudyId(studyId);
          }
          if (consentBo != null) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
            map.addAttribute(FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
          }
        }
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_LIST_PAGE, map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getConsentListPage - ERROR", e);
    }
    logger.info("StudyController - getConsentListPage - Ends");
    return mav;
  }

  /**
   * Each consent item can be one of the two types research kit provided or custom defined consent
   * item.ResearchKit already provides a few Consent Items screens by default.These Consent Items
   * allow the title and text to be modified as needed.Custom-defined Consent item are to be used in
   * cases where the available ResearchKit list of Consent Items do not sufficient
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/consentInfo.do")
  public ModelAndView getConsentPage(HttpServletRequest request) {
    logger.info("StudyController - getConsentPage - Starts");
    ModelAndView mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_PAGE);
    ModelMap map = new ModelMap();
    ConsentInfoBo consentInfoBo = null;
    StudyBo studyBo = null;
    List<ConsentInfoBo> consentInfoList = new ArrayList<>();
    List<ConsentMasterInfoBo> consentMasterInfoList = new ArrayList<>();
    String sucMsg = "";
    String errMsg = "";
    String consentStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String consentInfoId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.CONSENT_INFO_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.CONSENT_INFO_ID);
        String actionType =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
          request
              .getSession()
              .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyId);
        }
        // Added for live version Start
        String isLive =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE);
        if (StringUtils.isNotEmpty(isLive)
            && isLive.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
          consentStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_STUDY_ID);
        }
        // Added for live version End
        if (StringUtils.isEmpty(consentInfoId)) {
          consentInfoId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_INFO_ID);
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_INFO_ID, consentInfoId);
        }
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_ID, studyId);
        if (!studyId.isEmpty()) {
          if (StringUtils.isNotEmpty(consentStudyId)) {
            consentInfoList = studyService.getConsentInfoList(Integer.valueOf(consentStudyId));
          } else {
            consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
          }
          if (("view").equals(actionType)) {
            map.addAttribute(FdahpStudyDesignerConstants.ACTION_PAGE, "view");
          } else {
            map.addAttribute(FdahpStudyDesignerConstants.ACTION_PAGE, "addEdit");
          }
          consentMasterInfoList = studyService.getConsentMasterInfoList();
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);
          map.addAttribute("consentMasterInfoList", consentMasterInfoList);
          if ((consentMasterInfoList != null) && !consentMasterInfoList.isEmpty()) {
            map.addAttribute(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, consentInfoList);
          }
        }
        if ((consentInfoId != null) && !consentInfoId.isEmpty()) {
          consentInfoBo = studyService.getConsentInfoById(Integer.valueOf(consentInfoId));
          map.addAttribute("consentInfoBo", consentInfoBo);

          String language = request.getParameter("language");
          ConsentInfoLangBO consentInfoLangBO = new ConsentInfoLangBO();
          if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
            consentInfoLangBO =
                studyService.getConsentInfoLangById(consentInfoBo.getId(), language);
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute("currLanguage", language);
          map.addAttribute("consentInfoLangBO", consentInfoLangBO);
        }
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_PAGE, map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getConsentPage - Error", e);
    }
    logger.info("StudyController - getConsentPage - Ends");
    return mav;
  }

  /**
   * Study Consent admin can review the consent and can add the share data permission where
   * participant needs to be asked to provide permission further and admin can confirm the content
   * seen by users in the Review Terms (Consent Document) screen on the mobile app
   *
   * @author BTC
   * @param request
   * @return ModelAndView
   */
  @RequestMapping("/adminStudies/consentReview.do")
  public ModelAndView getConsentReviewAndEConsentPage(HttpServletRequest request) {
    logger.info("INFO: StudyController - getConsentReviewAndEConsentPage() :: Starts");
    ModelAndView mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_PAGE);
    ModelMap map = new ModelMap();
    SessionObject sesObj = null;
    String studyId = "";
    List<ConsentInfoBo> consentInfoBoList = null;
    StudyBo studyBo = null, liveStudyBo = null;
    ConsentBo consentBo = null;
    String sucMsg = "";
    String errMsg = "";
    String consentStudyId = "";
    try {
      Boolean isLiveStudy = false;
      sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }

        if (request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID)
            != null) {
          studyId =
              request
                  .getSession()
                  .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID)
                  .toString();
        }

        if (StringUtils.isEmpty(studyId)) {
          studyId =
              StringUtils.isEmpty(request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }

        // Added for live version Start

        String isLive =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE);
        if (StringUtils.isNotEmpty(isLive)
            && isLive.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
          consentStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_STUDY_ID);
        }
        // Added for live version End
        if (StringUtils.isNotEmpty(studyId)) {
          request
              .getSession()
              .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyId);
          if (StringUtils.isNotEmpty(consentStudyId)) {
            consentInfoBoList = studyService.getConsentInfoDetailsListByStudyId(consentStudyId);
            consentBo = studyService.getConsentDetailsByStudyId(consentStudyId);
          } else {
            consentInfoBoList = studyService.getConsentInfoDetailsListByStudyId(studyId);
            consentBo = studyService.getConsentDetailsByStudyId(studyId);
          }
          if ((null != consentInfoBoList) && !consentInfoBoList.isEmpty()) {
            map.addAttribute(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, consentInfoBoList);
          } else {
            map.addAttribute(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, "");
          }
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          String language = request.getParameter("language");
          if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
            this.setStudyLangData(studyId, language, map);
            List<ConsentInfoLangBO> consentInfoLangBOList =
                studyService.getConsentInfoLangByStudyId(Integer.parseInt(studyId), language);
            map.addAttribute(
                "consentInfoLangList", consentInfoLangBOList != null ? consentInfoLangBOList : "");
          }
          map.addAttribute("currLanguage", language);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);

          if (consentBo != null) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
            map.addAttribute(FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
          }

          String permission =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
          if (StringUtils.isNotEmpty(permission) && ("view").equals(permission)) {
            map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, "view");
          } else {
            map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, "addEdit");
          }
        }
        liveStudyBo = studyService.getStudyLiveStatusByCustomId(studyBo.getCustomStudyId());
        if (liveStudyBo != null) {
          isLiveStudy = true;
        }
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_ID, studyId);
        map.addAttribute("consentBo", consentBo);
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("studyLiveStatus", isLiveStudy);
        mav = new ModelAndView("consentReviewAndEConsentPage", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getConsentReviewAndEConsentPage() - ERROR ", e);
    }
    logger.info("INFO: StudyController - getConsentReviewAndEConsentPage() :: Ends");
    return mav;
  }

  /**
   * This method is used to get the list of resources
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/getResourceList.do")
  public ModelAndView getResourceList(HttpServletRequest request) {
    logger.info("StudyController - getResourceList() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    String sucMsg = "";
    String errMsg = "";
    String resourceErrMsg = "";
    List<ResourceBO> resourceBOList = null;
    List<ResourceBO> resourcesSavedList = null;
    ResourceBO studyProtocolResourceBO = null;
    StudyBo studyBo = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        if (null != request.getSession().getAttribute(sessionStudyCount + "resourceErrMsg")) {
          resourceErrMsg =
              (String) request.getSession().getAttribute(sessionStudyCount + "resourceErrMsg");
          map.addAttribute("resourceErrMsg", resourceErrMsg);
          request.getSession().removeAttribute(sessionStudyCount + "resourceErrMsg");
        }
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        String language = request.getParameter("language");
        if (StringUtils.isNotEmpty(studyId)) {
          resourceBOList = studyService.getResourceList(Integer.valueOf(studyId));
          studyProtocolResourceBO = studyService.getStudyProtocol(Integer.valueOf(studyId));
          resourcesSavedList = studyService.resourcesSaved(Integer.valueOf(studyId));
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute("resourceBOList", resourceBOList);
          map.addAttribute("resourcesSavedList", resourcesSavedList);
          map.addAttribute("studyProtocolResourceBO", studyProtocolResourceBO);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);

          map.addAttribute("currLanguage", language);
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            this.setStudyLangData(studyId, language, map);
            List<ResourcesLangBO> resourcesLangBOList =
                studyService.syncAndGetResourceLangList(
                    resourceBOList, Integer.parseInt(studyId), language);
            map.addAttribute("resourceLangBOList", resourcesLangBOList);
          }
        }
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView("resourceListPage", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getResourceList() - ERROR", e);
    }
    logger.info("StudyController - getResourceList() - Ends");
    return mav;
  }

  /**
   * Getting Study list
   *
   * <p>This method shows the user present with a list of studies
   *
   * <p>This method shows the user present with a list of studies
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/studyList.do")
  public ModelAndView getStudies(HttpServletRequest request) {
    logger.info("StudyController - getStudies - Starts");
    ModelAndView mav = new ModelAndView("loginPage");
    ModelMap map = new ModelMap();
    List<StudyListBean> studyBos = null;
    String sucMsg = "";
    String errMsg = "";
    String actionSucMsg = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (sesObj != null) {
        if (null != request.getSession().getAttribute(FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg = (String) request.getSession().getAttribute(FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null != request.getSession().getAttribute(FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg = (String) request.getSession().getAttribute(FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.ERR_MSG);
        }
        if (null != request.getSession().getAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG)) {
          actionSucMsg =
              (String)
                  request.getSession().getAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG, actionSucMsg);
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.STUDY_ID) != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.STUDY_ID);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.PERMISSION) != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.PERMISSION);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID)
            != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.IS_LIVE) != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.IS_LIVE);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.CONSENT_STUDY_ID)
            != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.CONSENT_STUDY_ID);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.ACTIVE_TASK_STUDY_ID)
            != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.ACTIVE_TASK_STUDY_ID);
        }
        if (request.getSession().getAttribute(FdahpStudyDesignerConstants.QUESTIONNARIE_STUDY_ID)
            != null) {
          request.getSession().removeAttribute(FdahpStudyDesignerConstants.QUESTIONNARIE_STUDY_ID);
        }
        studyBos = studyService.getStudyList(sesObj.getUserId());
        map.addAttribute("studyBos", studyBos);
        map.addAttribute("studyListId", "true");
        mav = new ModelAndView("studyListPage", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - getStudies - ERROR", e);
    }
    logger.info("StudyController - getStudies - Ends");
    return mav;
  }

  /**
   * Description : Details to view/edit/resend/addOrCopy of notification page
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/getStudyNotification.do")
  public ModelAndView getStudyNotification(HttpServletRequest request) {
    logger.info("StudyController - getStudyNotification - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    NotificationBO notificationBO = null;
    List<NotificationHistoryBO> notificationHistoryNoDateTime = null;
    StudyBo studyBo = null;
    String sucMsg = "";
    String errMsg = "";
    try {
      HttpSession session = request.getSession();
      SessionObject sessionObject =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sessionObject != null)
          && (sessionObject.getStudySession() != null)
          && sessionObject.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String notificationId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.NOTIFICATIONID);
        if (StringUtils.isEmpty(notificationId)) {
          notificationId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.NOTIFICATIONID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.NOTIFICATIONID);
        }
        String chkRefreshflag =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CHKREFRESHFLAG);
        if (StringUtils.isEmpty(chkRefreshflag)) {
          chkRefreshflag =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.CHKREFRESHFLAG))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.CHKREFRESHFLAG);
        }
        String actionType =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_TYPE);
        if (StringUtils.isEmpty(actionType)) {
          actionType =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.ACTION_TYPE);
        }
        String notificationText =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("notificationText"))
                ? ""
                : request.getParameter("notificationText");
        map.addAttribute("_S", sessionStudyCount);
        if (!"".equals(chkRefreshflag)) {
          String studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
          if (StringUtils.isEmpty(studyId)) {
            studyId =
                FdahpStudyDesignerUtil.isEmpty(
                        request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
          }
          // Getting study details by userId for notification
          studyBo = studyService.getStudyById(studyId, sessionObject.getUserId());
          String language = request.getParameter("language");
          map.addAttribute("currLanguage", language);
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            NotificationLangBO notificationLangBO = null;
            if (FdahpStudyDesignerUtil.isNotEmpty(notificationId))
              notificationLangBO =
                  notificationService.getNotificationLang(
                      Integer.parseInt(notificationId), language);
            map.addAttribute("notificationLangBO", notificationLangBO);
            this.setStudyLangData(studyId, language, map);
          }
          if (!"".equals(notificationId)) {
            // Fetching notification detail from notification table by
            // Id.
            notificationBO = notificationService.getNotification(Integer.parseInt(notificationId));
            // Fetching notification history of last sent detail from
            // notification table by Id.
            notificationHistoryNoDateTime =
                notificationService.getNotificationHistoryListNoDateTime(
                    Integer.parseInt(notificationId));
            if ("edit".equals(actionType)) {
              notificationBO.setActionPage("edit");
            } else if (FdahpStudyDesignerConstants.ADDORCOPY.equals(actionType)) {
              notificationBO.setActionPage(FdahpStudyDesignerConstants.ADDORCOPY);
            } else if (FdahpStudyDesignerConstants.RESEND.equals(actionType)) {
              if (notificationBO.isNotificationSent()) {
                notificationBO.setScheduleDate("");
                notificationBO.setScheduleTime("");
              }
              notificationBO.setActionPage(FdahpStudyDesignerConstants.RESEND);
            } else {
              notificationBO.setActionPage("view");
            }
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.NOTIFICATIONID);
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_TYPE);
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CHKREFRESHFLAG);
          } else if (!"".equals(notificationText) && "".equals(notificationId)) {
            notificationBO = new NotificationBO();
            notificationBO.setNotificationText(notificationText);
            notificationBO.setActionPage(FdahpStudyDesignerConstants.ADDORCOPY);
          } else if ("".equals(notificationText) && "".equals(notificationId)) {
            notificationBO = new NotificationBO();
            notificationBO.setActionPage(FdahpStudyDesignerConstants.ADDORCOPY);
          }
          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);
          map.addAttribute("notificationBO", notificationBO);
          map.addAttribute("notificationHistoryNoDateTime", notificationHistoryNoDateTime);
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute("appId", studyBo.getAppId());
          mav = new ModelAndView("addOrEditStudyNotification", map);

        } else {
          mav = new ModelAndView("redirect:viewStudyNotificationList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - getStudyNotification - ERROR", e);
    }
    logger.info("StudyController - getStudyNotification - Ends");
    return mav;
  }

  /**
   * Description : save notification as mark as completed
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/notificationMarkAsCompleted.do")
  public ModelAndView notificationMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - notificationMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String message = FdahpStudyDesignerConstants.FAILURE;
    String customStudyId = "";
    ModelMap map = new ModelMap();
    try {
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String markCompleted = FdahpStudyDesignerConstants.NOTIFICATION;
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        // markCompleted in param specify that notification to update it
        // as completed in table StudySequenceBo
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId), markCompleted, sesObj, customStudyId, language);
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("language", language);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          mav = new ModelAndView("redirect:getChecklist.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          mav = new ModelAndView("redirect:viewStudyNotificationList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - notificationMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - notificationMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * This method shows content for the Overview pages of the Study those pages shows in mobile side
   * as a set of swipe-able screens that carry information about the study, with each screen having
   * A title Description Image Link to Video (on first screen only) Link to Study Website
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/overviewStudyPages.do")
  public ModelAndView overviewStudyPages(HttpServletRequest request) {
    logger.info("StudyController - overviewStudyPages - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    List<StudyPageBo> studyPageBos = null;
    StudyBo studyBo = null;
    String sucMsg = "";
    String errMsg = "";
    StudyPageBean studyPageBean = new StudyPageBean();
    String user = "";
    List<StudyPageLanguageBO> studyPageLanguageBOS = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (FdahpStudyDesignerUtil.isEmpty(studyId)) {
          studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        }
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        user =
            (String)
                request
                    .getSession()
                    .getAttribute(
                        sessionStudyCount + FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER);
        if (StringUtils.isNotEmpty(studyId)) {
          studyPageBos = studyService.getOverviewStudyPagesById(studyId, sesObj.getUserId());
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          studyPageBean.setStudyId(studyBo.getId().toString());

          String language = request.getParameter("language");
          StudyLanguageBO studyLanguageBO = null;
          if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
            studyPageLanguageBOS =
                studyService.getOverviewStudyPagesLangById(studyPageBos, language);
            studyLanguageBO =
                studyService.getStudyLanguageById(Integer.parseInt(studyId), language);
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute("currLanguage", language);
          map.addAttribute(
              "studyPageLanguageList",
              studyPageLanguageBOS != null
                  ? studyPageLanguageBOS
                  : new ArrayList<StudyPageLanguageBO>());
          map.addAttribute(
              "mlMediaLink", studyLanguageBO != null ? studyLanguageBO.getMediaLink() : null);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);
          map.addAttribute("studyPageBos", studyPageBos);
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute("studyPageBean", studyPageBean);
          map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
          map.addAttribute("_S", sessionStudyCount);
          map.addAttribute("user", user);
          mav = new ModelAndView("overviewStudyPages", map);
        } else {
          return new ModelAndView("redirect:studyList.do");
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - overviewStudyPages - ERROR", e);
    }
    logger.info("StudyController - overviewStudyPages - Ends");
    return mav;
  }

  @RequestMapping("/adminStudies/participantPropertiesPage.do")
  public ModelAndView participantPropertiesPages(HttpServletRequest request) {
    logger.info("StudyController - participantPropertiesPages - Starts");
    ModelAndView mav = null;
    ModelMap map = null;
    String customStudyId = "";
    List<ParticipantPropertiesBO> propertiesList = null;
    Map<String, String> langMap = new HashMap<>();
    StudyBo studyBo = null;
    String permission = "";
    boolean markAsComplete = true;
    String actMsg = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID));
        permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        propertiesList = studyService.getParticipantProperties(customStudyId);
        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          actMsg = studyService.validateParticipantPropertyComplete(customStudyId);
          if (!actMsg.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) markAsComplete = false;
          String languages = studyBo.getSelectedLanguages();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            List<String> langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
        }
      }
      map = new ModelMap();
      String language = request.getParameter("language");
      if (FdahpStudyDesignerUtil.isNotEmpty(language)
          && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
        this.setStudyLangData(
            studyBo != null ? String.valueOf(studyBo.getId()) : "", language, map);
      }
      map.addAttribute("currLanguage", language);
      map.addAttribute("languageList", langMap);
      map.addAttribute("participantPropertiesList", propertiesList);
      map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
      map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
      map.addAttribute(FdahpStudyDesignerConstants.ACTIVITY_MESSAGE, actMsg);
      map.addAttribute("markAsComplete", markAsComplete);
      map.addAttribute("_S", sessionStudyCount);
      mav = new ModelAndView("participantPropertiesPage", map);
    } catch (Exception e) {
      logger.error("StudyController - participantPropertiesPages - ERROR", e);
    }
    logger.info("StudyController - participantPropertiesPages - Ends");
    return mav;
  }

  @RequestMapping("/adminStudies/addParticipantProperties.do")
  public ModelAndView addParticipantProperties(HttpServletRequest request) {
    logger.info("StudyController - addParticipantProperties - Starts");
    ModelAndView mav = null;
    ModelMap map = null;
    StudyBo studyBo = null;
    String permission = "";
    ParticipantPropertiesBO participantPropertiesBO = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {

        String studyId =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID));
        permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
        }
      }
      String actionType = request.getParameter("actionType");
      participantPropertiesBO = new ParticipantPropertiesBO();
      ResourceBundle resourceBundle = ResourceBundle.getBundle("messageResource");
      String[] dataType = resourceBundle.getString("participant.property.datatype").split(",");
      String[] dataSource = resourceBundle.getString("participant.property.datasource").split(",");
      map = new ModelMap();
      String language = request.getParameter("language");
      if (FdahpStudyDesignerUtil.isNotEmpty(language)
          && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
        this.setStudyLangData(
            studyBo != null ? String.valueOf(studyBo.getId()) : "", language, map);
      }
      map.addAttribute("participantProperties", participantPropertiesBO);
      map.addAttribute("dataType", dataType);
      map.addAttribute("_S", sessionStudyCount);
      map.addAttribute("dataSource", dataSource);
      map.addAttribute("actionType", actionType);
      map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
      map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
      mav = new ModelAndView("addParticipantProperties", map);
    } catch (Exception e) {
      logger.error("StudyController - addParticipantProperties - ERROR", e);
    }
    logger.info("StudyController - addParticipantProperties - Ends");
    return mav;
  }

  @RequestMapping(
      value = "/adminStudies/saveorUpdateParticipantProperties.do",
      method = RequestMethod.POST)
  public ModelAndView saveOrUpdateParticipantProperties(
      HttpServletRequest request,
      HttpServletResponse response,
      @ModelAttribute("participantProperties") ParticipantPropertiesBO participantPropertiesBO) {
    logger.info("StudyController - saveOrUpdateParticipantProperties - Starts");
    ModelAndView mav = null;
    ModelMap map = null;
    StudyBo studyBo = null;
    String customStudyId = "";
    String permission = "";
    String actionType = "", actionButtonType = "";
    Map<String, String> langMap = new HashMap<>();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      actionType = request.getParameter("actionType");
      actionButtonType = request.getParameter("actionButtonType");
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        if (participantPropertiesBO != null) {
          String studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
          customStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
          participantPropertiesBO.setStudyId(Integer.valueOf(studyId));
          participantPropertiesBO.setCustomStudyId(customStudyId);
          if (participantPropertiesBO.getId() != null) {
            participantPropertiesBO.setModifiedBy(sesObj.getUserId());
            participantPropertiesBO.setModifiedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
            /*
             * if (participantPropertiesBO.getActive()) {
             * request.getSession().setAttribute(sessionStudyCount +
             * FdahpStudyDesignerConstants.SUC_MSG,
             * "Participant Property Updated successfully."); }
             */
          } else {
            participantPropertiesBO.setCreatedBy(sesObj.getUserId());
            participantPropertiesBO.setCreatedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
            /*
             * if (participantPropertiesBO.getActive()) {
             * request.getSession().setAttribute(sessionStudyCount +
             * FdahpStudyDesignerConstants.SUC_MSG,
             * "Participant Property added successfully."); }
             */
          }
          if (!StringUtils.isEmpty(actionButtonType)) {
            if (actionButtonType.equalsIgnoreCase("save")) {
              participantPropertiesBO.setCompleted(false);
            } else {
              participantPropertiesBO.setCompleted(true);
              participantPropertiesBO.setIsChange(true);
            }
          }
          if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
            studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
            String languages = studyBo.getSelectedLanguages();
            List<String> langList = new ArrayList<>();
            if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
              langList = Arrays.asList(languages.split(","));
              for (String string : langList) {
                langMap.put(string, MultiLanguageCodes.getValue(string));
              }
            }
          }
          if (null != studyBo) {
            participantPropertiesBO.setAppId(studyBo.getAppId());
            participantPropertiesBO.setOrgId(studyBo.getOrgId());
          }
          participantPropertiesBO =
              studyService.saveOrUpdateParticipantProperties(participantPropertiesBO);
          if (null != participantPropertiesBO) {
            studyService.markAsCompleted(
                Integer.valueOf(studyId),
                FdahpStudyDesignerConstants.PARTICIPANT_PROPERTIES,
                false,
                sesObj,
                customStudyId);
          }
        }
      }
      if (actionButtonType.equalsIgnoreCase("save")) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("messageResource");
        String[] dataType = resourceBundle.getString("participant.property.datatype").split(",");
        String[] dataSource =
            resourceBundle.getString("participant.property.datasource").split(",");
        map = new ModelMap();
        map.addAttribute("languageList", langMap);
        map.addAttribute("participantProperties", participantPropertiesBO);
        map.addAttribute("dataType", dataType);
        map.addAttribute("dataSource", dataSource);
        map.addAttribute("actionType", actionType);
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        mav = new ModelAndView("addParticipantProperties", map);
      } else {
        map = new ModelMap();
        map.addAttribute("participantProperties", participantPropertiesBO);
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView("redirect:participantPropertiesPage.do", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateParticipantProperties - Error", e);
    }
    logger.info("StudyController - saveOrUpdateParticipantProperties - Ends");
    return mav;
  }

  @RequestMapping("/adminStudies/editParticipantProperties.do")
  public ModelAndView editParticipantProperties(HttpServletRequest request) {
    logger.info("StudyController - participantPropertiesPages - Starts");
    ModelAndView mav = null;
    ModelMap map = null;
    String customStudyId = "", participantPropertyId = "";
    ParticipantPropertiesBO participantPropertiesBO = null;
    String permission = "";
    StudyBo studyBo = null;
    Map<String, String> langMap = new HashMap<>();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID));
        permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
        }
        participantPropertyId = request.getParameter("participantPropertyId");
        if (StringUtils.isNotEmpty(participantPropertyId)
            && StringUtils.isNotEmpty(customStudyId)) {
          participantPropertiesBO =
              studyService.getParticipantProperty(participantPropertyId, customStudyId);
        }
      }
      String actionType = request.getParameter("actionType");
      ResourceBundle resourceBundle = ResourceBundle.getBundle("messageResource");
      String[] dataType = resourceBundle.getString("participant.property.datatype").split(",");
      String[] dataSource = resourceBundle.getString("participant.property.datasource").split(",");
      String language = request.getParameter("language");
      map = new ModelMap();
      if (FdahpStudyDesignerUtil.isNotEmpty(language)
          && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
        this.setStudyLangData(
            studyBo != null ? String.valueOf(studyBo.getId()) : "", language, map);
      }
      map.addAttribute("languageList", langMap);
      map.addAttribute("participantProperties", participantPropertiesBO);
      map.addAttribute("currLanguage", language);
      map.addAttribute("dataType", dataType);
      map.addAttribute("dataSource", dataSource);
      map.addAttribute("actionType", actionType);
      map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
      map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
      map.addAttribute("_S", sessionStudyCount);
      mav = new ModelAndView("addParticipantProperties", map);
    } catch (Exception e) {
      logger.error("StudyController - participantPropertiesPages - ERROR", e);
    }
    logger.info("StudyController - participantPropertiesPages - Ends");
    return mav;
  }

  @RequestMapping("/adminStudies/deactivateParticipantProperty.do")
  public ModelAndView deactivateParticipantProperty(HttpServletRequest request) throws IOException {
    logger.info("StudyController - deactivateParticipantProperty - Starts");
    ModelAndView mav = null;
    ModelMap map = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String participantPropertyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        participantPropertyId = request.getParameter("participantPropertyId");
        if (StringUtils.isNotEmpty(participantPropertyId)) {
          message =
              studyService.deactivateParticipantProperty(
                  Integer.valueOf(participantPropertyId), sesObj.getUserId());
        }
      }
      if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
        map = new ModelMap();
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView("redirect:participantPropertiesPage.do", map);
      } else {
        map = new ModelMap();
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView("redirect:editParticipantProperties.do", map);
      }

    } catch (Exception e) {
      logger.error("StudyController - deactivateParticipantProperty - ERROR", e);
    }
    logger.info("StudyController - deactivateParticipantProperty - Ends");
    return mav;
  }

  @RequestMapping(value = "/adminStudies/deleteParticipantProperty.do", method = RequestMethod.POST)
  public ModelAndView deleteParticipantPropertyInfo(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - deleteParticipantPropertyInfo - Starts");
    String message = FdahpStudyDesignerConstants.SUCCESS;
    ModelAndView mav = null;
    ModelMap map = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      String participantPropertyId = request.getParameter("participantPropertyId");
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        /*
         * customStudyId = (String) request.getSession() .getAttribute(sessionStudyCount
         * + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
         */
        if (StringUtils.isNotEmpty(participantPropertyId)) {
          message =
              studyService.deleteParticipantProperty(
                  Integer.valueOf(participantPropertyId), Integer.valueOf(sesObj.getUserId()));
          // participantPropertiesBOs =
          // studyService.getParticipantProperties(customStudyId);
          /*
           * if ((participantPropertiesBOs != null) &&
           * !participantPropertiesBOs.isEmpty()) { participantPropertiesJsonArray = new
           * JSONArray( mapper.writeValueAsString(participantPropertiesBOs));
           * jsonobject.put(FdahpStudyDesignerConstants.PARTICIPANTPROPERTIES_LIST,
           * participantPropertiesJsonArray); }
           */
          /*
           * if (StringUtils.isNotEmpty(studyId)) {
           * studyService.markAsCompleted(Integer.valueOf(studyId),
           * FdahpStudyDesignerConstants.QUESTIONNAIRE, false, sesObj, customStudyId); }
           */
          /*
           * boolean markAsComplete = true; actMsg =
           * studyService.validateActivityComplete(studyId,
           * FdahpStudyDesignerConstants.ACTIVITY_TYPE_QUESTIONNAIRE); if
           * (!actMsg.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS))
           * markAsComplete = false; jsonobject.put("markAsComplete", markAsComplete);
           * jsonobject.put(FdahpStudyDesignerConstants.ACTIVITY_MESSAGE, actMsg);
           */
        }
      }
      if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
        map = new ModelMap();
        map.addAttribute("_S", sessionStudyCount);
        mav = new ModelAndView("redirect:participantPropertiesPage.do", map);
      } /*
         * else { map = new ModelMap(); map.addAttribute("_S", sessionStudyCount); mav =
         * new ModelAndView("redirect:editParticipantProperties.do", map); }
         */
    } catch (Exception e) {
      logger.error("StudyController - deleteParticipantPropertyInfo - ERROR", e);
    }
    logger.info("StudyController - deleteParticipantPropertyInfo - Ends");
    return mav;
  }

  @RequestMapping(
      value = "/adminStudies/validateParticipantPropertyKey.do",
      method = RequestMethod.POST)
  public void validateParticipantPropertyShortTitle(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - validateParticipantPropertyShortTitle - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String shortTitle =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("shortTitle"))
                ? ""
                : request.getParameter("shortTitle");
        if (!StringUtils.isEmpty(shortTitle) && !StringUtils.isEmpty(customStudyId)) {
          message = studyService.checkParticipantPropertyShortTitle(shortTitle, customStudyId);
        }
      }
      jsonobject.put("message", message);
      response.setContentType("application/json");
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - validateParticipantPropertyShortTitle - ERROR", e);
    }
    logger.info("StudyController - validateParticipantPropertyShortTitle - Ends");
  }

  @RequestMapping("/adminStudies/participantPropertiesMarkAsCompleted.do")
  public ModelAndView participantPropertiesMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - participantPropertiesMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:studyList.do");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId),
                FdahpStudyDesignerConstants.PARTICIPANT_PROPERTIES,
                sesObj,
                customStudyId,
                language);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          map.put("language", language);
          map.addAttribute("_S", sessionStudyCount);
          mav = new ModelAndView("redirect:viewStudyQuestionnaires.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          map.addAttribute("_S", sessionStudyCount);
          mav = new ModelAndView("redirect:participantPropertiesPage.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - participantPropertiesMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - participantPropertiesMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * Describes the make the questionnaire mark as completed once creation of questionnaire completed
   *
   * @author BTC
   * @param request {@link HttpServletRequest}
   */
  @RequestMapping("/adminStudies/questionnaireMarkAsCompleted.do")
  public ModelAndView questionnaireMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - questionnaireMarkAsCompleted() - Starts");
    ModelAndView mav = new ModelAndView("redirect:studyList.do");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String language = request.getParameter("language");
        message =
            studyService.markAsCompleted(
                Integer.parseInt(studyId),
                FdahpStudyDesignerConstants.QUESTIONNAIRE,
                sesObj,
                customStudyId,
                language);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                  propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
          map.addAttribute("_S", sessionStudyCount);
          map.put("language", language);
          mav = new ModelAndView("redirect:viewStudyActiveTasks.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
          map.addAttribute("_S", sessionStudyCount);
          mav = new ModelAndView("redirect:viewStudyQuestionnaires.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - questionnaireMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - questionnaireMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * Describes the reloading of comprehension question once admin delete the question from list of
   * questions
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return void
   */
  @RequestMapping("/adminStudies/reloadComprehensionQuestionListPage.do")
  public void reloadComprehensionQuestionListPage(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reloadConsentListPage - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray comprehensionJsonArray = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      List<ComprehensionTestQuestionBo> comprehensionTestQuestionList;
      if (sesObj != null) {
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isNotEmpty(studyId)) {
          comprehensionTestQuestionList =
              studyService.getComprehensionTestQuestionList(Integer.valueOf(studyId));
          if ((comprehensionTestQuestionList != null) && !comprehensionTestQuestionList.isEmpty()) {
            comprehensionJsonArray =
                new JSONArray(mapper.writeValueAsString(comprehensionTestQuestionList));
          }
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        jsonobject.put("comprehensionTestQuestionList", comprehensionJsonArray);
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reloadConsentListPage - ERROR", e);
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      if (out != null) {
        out.print(jsonobject);
      }
    }
    logger.info("StudyController - reloadConsentListPage - Ends");
  }

  /**
   * Describes the reloading of consent items once admin delete the any consent from list of
   * consents
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return void
   */
  @RequestMapping("/adminStudies/reloadConsentListPage.do")
  public void reloadConsentListPage(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reloadConsentListPage - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray consentJsonArray = null;
    List<ConsentInfoBo> consentInfoList = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (sesObj != null) {
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isNotEmpty(studyId)) {
          consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
          if ((consentInfoList != null) && !consentInfoList.isEmpty()) {
            boolean markAsComplete = true;
            consentJsonArray = new JSONArray(mapper.writeValueAsString(consentInfoList));
            if ((consentInfoList != null) && !consentInfoList.isEmpty()) {
              for (ConsentInfoBo conInfoBo : consentInfoList) {
                if (!conInfoBo.getStatus()) {
                  markAsComplete = false;
                  break;
                }
              }
            }
            jsonobject.put("markAsComplete", markAsComplete);
          }
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        jsonobject.put(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, consentJsonArray);
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reloadConsentListPage - ERROR", e);
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      if (out != null) {
        out.print(jsonobject);
      }
    }
    logger.info("StudyController - reloadConsentListPage - Ends");
  }

  /**
   * This method is used to reload the resource list
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   */
  @RequestMapping("/adminStudies/reloadResourceListPage.do")
  public void reloadResourceListPage(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reloadResourceListPage - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray resourceJsonArray = null;
    List<ResourceBO> resourceList = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (sesObj != null) {
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isNotEmpty(studyId)) {
          resourceList = studyService.getResourceList(Integer.valueOf(studyId));
          if ((resourceList != null) && !resourceList.isEmpty()) {
            boolean markAsComplete = true;
            resourceJsonArray = new JSONArray(mapper.writeValueAsString(resourceList));
            if ((resourceList != null) && !resourceList.isEmpty()) {
              for (ResourceBO resource : resourceList) {
                if (!resource.isStatus()) {
                  markAsComplete = false;
                  break;
                }
              }
            }
            jsonobject.put("markAsComplete", markAsComplete);
          }
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        jsonobject.put("resourceList", resourceJsonArray);
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reloadResourceListPage - ERROR", e);
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      if (out != null) {
        out.print(jsonobject);
      }
    }
    logger.info("StudyController - reloadResourceListPage - Ends");
  }

  /*----------------------------------------added by MOHAN T ends----------------------------------------*/

  /**
   * Comprehension question will be created by default in the master order.Admin can manage the
   * order by drag and drop questions in the list
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/reOrderComprehensionTestQuestion.do")
  public void reOrderComprehensionTestQuestion(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reOrderComprehensionTestQuestion - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      int oldOrderNumber;
      int newOrderNumber;
      if (sesObj != null) {
        String studyId =
            (String) request.getSession().getAttribute(FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String oldOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER);
        String newOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER);
        if (((studyId != null) && !studyId.isEmpty())
            && !oldOrderNo.isEmpty()
            && !newOrderNo.isEmpty()) {
          oldOrderNumber = Integer.valueOf(oldOrderNo);
          newOrderNumber = Integer.valueOf(newOrderNo);
          message =
              studyService.reOrderComprehensionTestQuestion(
                  Integer.valueOf(studyId), oldOrderNumber, newOrderNumber);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reOrderComprehensionTestQuestion - ERROR", e);
    }
    logger.info("StudyController - reOrderComprehensionTestQuestion - Ends");
  }

  /**
   * Consent items will be created by default in master order.Admin can manage the order by
   * reordering the consents with in a list to make reorder admin can drag and drop the consent with
   * in a list
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return void
   */
  @RequestMapping(value = "/adminStudies/reOrderConsentInfo.do", method = RequestMethod.POST)
  public void reOrderConsentInfo(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reOrderConsentInfo - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray consentJsonArray = null;
    List<ConsentInfoBo> consentInfoList = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      int oldOrderNumber;
      int newOrderNumber;
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String oldOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER);
        String newOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER);
        if (((studyId != null) && !studyId.isEmpty())
            && !oldOrderNo.isEmpty()
            && !newOrderNo.isEmpty()) {
          oldOrderNumber = Integer.valueOf(oldOrderNo);
          newOrderNumber = Integer.valueOf(newOrderNo);
          message =
              studyService.reOrderConsentInfoList(
                  Integer.valueOf(studyId), oldOrderNumber, newOrderNumber);
          if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            consentInfoList = studyService.getConsentInfoList(Integer.valueOf(studyId));
            if ((consentInfoList != null) && !consentInfoList.isEmpty()) {
              consentJsonArray = new JSONArray(mapper.writeValueAsString(consentInfoList));
            }
            jsonobject.put(FdahpStudyDesignerConstants.CONSENT_INFO_LIST, consentJsonArray);
          }
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reOrderConsentInfo - ERROR", e);
    }
    logger.info("StudyController - reOrderConsentInfo - Ends");
  }

  /**
   * This method is used to reorder the resource list page
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   */
  @RequestMapping(value = "/adminStudies/reOrderResourceList.do", method = RequestMethod.POST)
  public void reOrderResourceList(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reOrderResourceList - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    ObjectMapper mapper = new ObjectMapper();
    JSONArray resourceJsonArray = null;
    List<ResourceBO> resourceList = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      int oldOrderNumber;
      int newOrderNumber;
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String oldOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER);
        String newOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER);
        if (((studyId != null) && !studyId.isEmpty())
            && !oldOrderNo.isEmpty()
            && !newOrderNo.isEmpty()) {
          oldOrderNumber = Integer.valueOf(oldOrderNo);
          newOrderNumber = Integer.valueOf(newOrderNo);
          message =
              studyService.reOrderResourceList(
                  Integer.valueOf(studyId), oldOrderNumber, newOrderNumber);
          if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            resourceList = studyService.getResourceList(Integer.valueOf(studyId));
            if ((resourceList != null) && !resourceList.isEmpty()) {
              resourceJsonArray = new JSONArray(mapper.writeValueAsString(resourceList));
            }
            jsonobject.put("resourceList", resourceJsonArray);
          }
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reOrderResourceList - ERROR", e);
    }
    logger.info("StudyController - reOrderResourceList - Ends");
  }

  /**
   * @author BTC
   * @param request
   * @param response Description : reorder the eligibility {@link Test} Question and Answers
   */
  @RequestMapping(
      value = "/adminStudies/reOrderStudyEligibiltyTestQusAns.do",
      method = RequestMethod.POST)
  public void reOrderStudyEligibiltyTestQusAns(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - reOrderStudyEligibiltyTestQusAns - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    Integer eligibilityId = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      int oldOrderNumber;
      int newOrderNumber;
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        eligibilityId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("eligibilityId"))
                ? 0
                : Integer.parseInt(request.getParameter("eligibilityId"));
        String oldOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.OLD_ORDER_NUMBER);
        String newOrderNo =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER))
                ? "0"
                : request.getParameter(FdahpStudyDesignerConstants.NEW_ORDER_NUMBER);
        if (((studyId != null) && !studyId.isEmpty())
            && !oldOrderNo.isEmpty()
            && !newOrderNo.isEmpty()) {
          oldOrderNumber = Integer.valueOf(oldOrderNo);
          newOrderNumber = Integer.valueOf(newOrderNo);
          message =
              studyService.reorderEligibilityTestQusAns(
                  eligibilityId, oldOrderNumber, newOrderNumber, Integer.valueOf(studyId));
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - reOrderStudyEligibiltyTestQusAns - ERROR", e);
    }
    logger.info("StudyController - reOrderStudyEligibiltyTestQusAns - Ends");
  }

  /**
   * reset study by customStudyId
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/resetStudy.do")
  public ModelAndView resetStudy(HttpServletRequest request) {
    logger.info("StudyController - resetStudy - Starts");
    ModelAndView mav = new ModelAndView("redirect:login.do");
    boolean flag = false;
    try {
      String cusId =
          FdahpStudyDesignerUtil.isEmpty(request.getParameter("cusId"))
              ? ""
              : request.getParameter("cusId");
      if (!cusId.isEmpty()) {
        flag = studyService.resetDraftStudyByCustomStudyId(cusId);
        if (flag) {
          request.getSession(false).setAttribute("sucMsg", "Reset successfully");
        } else {
          request.getSession(false).setAttribute("errMsg", "DB issue or study does not exist");
        }
      }

    } catch (Exception e) {
      logger.error("StudyController - resetStudy - ERROR", e);
    }
    logger.info("StudyController - resetStudy - Ends");
    return mav;
  }

  /* Study notification starts */

  /**
   * This method is user to set resources to Mark as completed
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/resourceMarkAsCompleted.do")
  public ModelAndView resourceMarkAsCompleted(HttpServletRequest request) {
    logger.info("StudyController - saveOrUpdateResource() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    String message = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    List<ResourceBO> resourceList;
    Boolean isAnchorDateExistsForStudy;
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        resourceList = studyService.resourcesWithAnchorDate(Integer.parseInt(studyId));
        if ((resourceList != null) && !resourceList.isEmpty()) {
          isAnchorDateExistsForStudy =
              studyQuestionnaireService.isAnchorDateExistsForStudy(
                  Integer.parseInt(studyId), customStudyId);
          if (isAnchorDateExistsForStudy) {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        } else {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
        map.addAttribute("_S", sessionStudyCount);
        if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
          String language = request.getParameter("language");
          message =
              studyService.markAsCompleted(
                  Integer.parseInt(studyId),
                  FdahpStudyDesignerConstants.RESOURCE,
                  sesObj,
                  customStudyId,
                  language);
          if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            map.put("language", language);
            mav = new ModelAndView("redirect:viewStudyNotificationList.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    FdahpStudyDesignerConstants.UNABLE_TO_MARK_AS_COMPLETE);
            mav = new ModelAndView("redirect:getResourceList.do", map);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + "resourceErrMsg",
                  FdahpStudyDesignerConstants.RESOURCE_ANCHOR_ERROR_MSG);
          mav = new ModelAndView("redirect:getResourceList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - resourceMarkAsCompleted() - ERROR", e);
    }
    logger.info("StudyController - resourceMarkAsCompleted() - Ends");
    return mav;
  }

  /**
   * Study consent can have 0 or more comprehension test questions.Admin can mark whether or not
   * this is required for the study if required admin will need to add question text,answer options
   * and correct option and minimum score
   *
   * @param request {@link HttpServletRequest}
   * @param response {@link HttpServletResponse}
   * @return
   */
  @RequestMapping(value = "/adminStudies/saveComprehensionTestQuestion.do")
  public void saveComprehensionTestQuestion(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyQuestionnaireController - saveQuestion - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    ObjectMapper mapper = new ObjectMapper();
    ComprehensionTestQuestionBo comprehensionTestQuestionBo = null;
    String customStudyId = "";
    ComprehensionTestQuestionBo addComprehensionTestQuestionBo = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String comprehensionQuestion = request.getParameter("comprehenstionQuestionInfo");
        if (null != comprehensionQuestion) {
          comprehensionTestQuestionBo =
              mapper.readValue(comprehensionQuestion, ComprehensionTestQuestionBo.class);
          if (comprehensionTestQuestionBo != null) {
            if (comprehensionTestQuestionBo.getId() != null) {
              comprehensionTestQuestionBo.setModifiedBy(sesObj.getUserId());
              comprehensionTestQuestionBo.setModifiedOn(
                  FdahpStudyDesignerUtil.getCurrentDateTime());
              comprehensionTestQuestionBo.setStatus(false);
            } else {
              if (comprehensionTestQuestionBo.getStudyId() != null) {
                int order =
                    studyService.comprehensionTestQuestionOrder(
                        comprehensionTestQuestionBo.getStudyId());
                comprehensionTestQuestionBo.setSequenceNo(order);
              }
              comprehensionTestQuestionBo.setCreatedBy(sesObj.getUserId());
              comprehensionTestQuestionBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
              comprehensionTestQuestionBo.setStatus(false);
            }
            String language = request.getParameter("language");
            addComprehensionTestQuestionBo =
                studyService.saveOrUpdateComprehensionTestQuestion(
                    comprehensionTestQuestionBo, language);
          }
        }
        if (addComprehensionTestQuestionBo != null) {
          jsonobject.put("questionId", addComprehensionTestQuestionBo.getId());
          message = FdahpStudyDesignerConstants.SUCCESS;
          if (StringUtils.isNotEmpty(studyId)) {
            studyService.markAsCompleted(
                Integer.valueOf(studyId),
                FdahpStudyDesignerConstants.COMPREHENSION_TEST,
                false,
                sesObj,
                customStudyId);
          }
        }
      }
      jsonobject.put("message", message);
      response.setContentType("application/json");
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyQuestionnaireController - saveQuestion - Error", e);
    }
    logger.info("StudyQuestionnaireController - saveQuestion - Ends");
  }

  /**
   * Study consent section is mandatory in mobile section admin can add one or more consent items
   * here.The consent item are two types which are research kit provided consents and custom defined
   * consent items admin can create the custom consent items which are not available in research kit
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response {@link HttpServletResponse}
   * @return {@link ModelAndView}
   */
  @RequestMapping(value = "/adminStudies/saveConsentInfo.do")
  public void saveConsentInfo(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - saveConsentInfo - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    ConsentInfoBo addConsentInfoBo = null;
    ObjectMapper mapper = new ObjectMapper();
    ConsentInfoBo consentInfoBo = null;
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      String conInfo = request.getParameter("consentInfo");
      if (null != conInfo) {
        consentInfoBo = mapper.readValue(conInfo, ConsentInfoBo.class);
      }
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (consentInfoBo != null) {
          if ((consentInfoBo.getStudyId() != null) && (consentInfoBo.getId() == null)) {
            int order = studyService.consentInfoOrder(consentInfoBo.getStudyId());
            consentInfoBo.setSequenceNo(order);
          }
          customStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
          String language = request.getParameter("language");
          addConsentInfoBo =
              studyService.saveOrUpdateConsentInfo(consentInfoBo, sesObj, customStudyId, language);
          if (addConsentInfoBo != null) {
            jsonobject.put(FdahpStudyDesignerConstants.CONSENT_INFO_ID, addConsentInfoBo.getId());
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - saveConsentInfo - ERROR", e);
    }
    logger.info("StudyController - saveConsentInfo - Ends");
  }

  /**
   * Study consent section have the consent review sub section which contain the share data
   * permission allows the admin to specify if as part of the Consent process, participants need to
   * be asked to provide permission for their response data to be shared with 3rd parties and
   * consent document review section is section is meant for the admin to confirm the content seen
   * by users in the Review Terms (Consent Document) screen on the mobile app In e-consent section
   * The admin sees the elements of the e-consent form as provided to the user in the mobile app
   *
   * @author BTC
   * @param request {@link HttpServletRequest}
   * @param response {@link HttpServletResponse}
   */
  @RequestMapping("/adminStudies/saveConsentReviewAndEConsentInfo.do")
  public void saveConsentReviewAndEConsentInfo(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("INFO: StudyController - saveConsentReviewAndEConsentInfo() :: Starts");
    ConsentBo consentBo = null;
    String consentInfoParamName = "";
    ObjectMapper mapper = new ObjectMapper();
    JSONObject jsonobj = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String studyId = "";
    String consentId = "";
    String customStudyId = "";
    String comprehensionTest = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        consentInfoParamName = request.getParameter("consentInfo");
        String page = request.getParameter("page");
        if (StringUtils.isNotEmpty(consentInfoParamName)) {
          consentBo = mapper.readValue(consentInfoParamName, ConsentBo.class);
          if (consentBo != null) {
            customStudyId =
                (String)
                    request
                        .getSession()
                        .getAttribute(
                            sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
            comprehensionTest = consentBo.getComprehensionTest();
            if (StringUtils.equals(FdahpStudyDesignerConstants.CONESENT_REVIEW, page)) {
              consentBo.setReviewAndEconsentPage(true);
            } else {
              consentBo.setReviewAndEconsentPage(false);
            }
            String language = request.getParameter("language");
            consentBo =
                studyService.saveOrCompleteConsentReviewDetails(
                    consentBo, sesObj, customStudyId, language);
            studyId =
                StringUtils.isEmpty(String.valueOf(consentBo.getStudyId()))
                    ? ""
                    : String.valueOf(consentBo.getStudyId());
            consentId =
                StringUtils.isEmpty(String.valueOf(consentBo.getId()))
                    ? ""
                    : String.valueOf(consentBo.getId());
            // setting consentId in requestSession
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID);
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
            message = FdahpStudyDesignerConstants.SUCCESS;
            if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
              if ((comprehensionTest != null)
                  && comprehensionTest.equalsIgnoreCase(FdahpStudyDesignerConstants.SAVE_BUTTON)) {
                studyService.markAsCompleted(
                    Integer.valueOf(studyId),
                    FdahpStudyDesignerConstants.COMPREHENSION_TEST,
                    false,
                    sesObj,
                    customStudyId);
              }
            }
          }
        }
      }
      jsonobj.put(FdahpStudyDesignerConstants.MESSAGE, message);
      jsonobj.put(FdahpStudyDesignerConstants.STUDY_ID, studyId);
      jsonobj.put(FdahpStudyDesignerConstants.CONSENT_ID, consentId);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobj);
    } catch (Exception e) {
      logger.error("StudyController - saveConsentReviewAndEConsentInfo() - ERROR ", e);
    }
    logger.info("INFO: StudyController - saveConsentReviewAndEConsentInfo() :: Ends");
  }

  /**
   * This method is used to Save or Done Checklist
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param checklist , {@link Checklist}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrDoneChecklist.do")
  public ModelAndView saveOrDoneChecklist(HttpServletRequest request, Checklist checklist) {
    logger.info("StudyController - saveOrDoneChecklist() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    Integer checklistId = 0;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String actionBut =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("actionBut"))
                ? ""
                : request.getParameter("actionBut");
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        checklist.setStudyId(Integer.valueOf(studyId));
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        checklistId = studyService.saveOrDoneChecklist(checklist, actionBut, sesObj, customStudyId);
        map.addAttribute("_S", sessionStudyCount);
        if (!checklistId.equals(0)) {
          if (checklist.getChecklistId() == null) {
            if (("save").equalsIgnoreCase(actionBut)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      "Checklist successfully added.");
            }
          } else {
            if (("save").equalsIgnoreCase(actionBut)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      "Checklist successfully updated.");
            }
          }
        } else {
          if (checklist.getChecklistId() == null) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    "Failed to add checklist.");
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    "Failed to update checklist.");
          }
        }
        mav = new ModelAndView("redirect:getChecklist.do", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrDoneChecklist() - ERROR", e);
    }
    logger.info("StudyController - saveOrDoneChecklist() - Ends");
    return mav;
  }

  /* Study notification ends */

  /**
   * This method captures basic information about the study basic info like Study ID, Study name,
   * Study full name, Study Category, Research Sponsor,Data Partner, Estimated Duration in
   * weeks/months/years, Study Tagline, Study Description, Study website, Study Type
   *
   * @author BTC
   * @param studyBo , {@link StudyBo}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateBasicInfo.do")
  public ModelAndView saveOrUpdateBasicInfo(
      HttpServletRequest request,
      @ModelAttribute(FdahpStudyDesignerConstants.STUDY_BO) StudyBo studyBo) {
    logger.info("StudyController - saveOrUpdateBasicInfo - Starts");
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    String fileName = "";
    String file = "";
    String buttonText = "";
    String message = FdahpStudyDesignerConstants.FAILURE;
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      buttonText =
          FdahpStudyDesignerUtil.isEmpty(
                  request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT))
              ? ""
              : request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {

        if (studyBo.getId() == null) {
          StudySequenceBo studySequenceBo = new StudySequenceBo();
          studySequenceBo.setBasicInfo(true);
          studyBo.setStudySequenceBo(studySequenceBo);
          studyBo.setStatus(FdahpStudyDesignerConstants.STUDY_PRE_LAUNCH);
        }
        studyBo.setUserId(sesObj.getUserId());
        if ((studyBo.getFile() != null) && !studyBo.getFile().isEmpty()) {
          if (FdahpStudyDesignerUtil.isNotEmpty(studyBo.getThumbnailImage())) {
            file =
                studyBo
                    .getThumbnailImage()
                    .replace(
                        "."
                            + studyBo.getThumbnailImage()
                                .split("\\.")[studyBo.getThumbnailImage().split("\\.").length - 1],
                        "");
          } else {
            file =
                FdahpStudyDesignerUtil.getStandardFileName(
                    "STUDY", studyBo.getName(), studyBo.getCustomStudyId());
          }
          fileName =
              FdahpStudyDesignerUtil.uploadImageFile(
                  studyBo.getFile(), file, FdahpStudyDesignerConstants.STUDTYLOGO);
          studyBo.setThumbnailImage(fileName);
        }
        studyBo.setButtonText(buttonText);
        String language = request.getParameter("currentLanguage");
        message = studyService.saveOrUpdateStudy(studyBo, sesObj.getUserId(), sesObj, language);
        request
            .getSession()
            .setAttribute(
                sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId() + "");
        map.addAttribute("_S", sessionStudyCount);
        if (FdahpStudyDesignerConstants.SUCCESS.equals(message)) {
          if (StringUtils.isNotEmpty(studyBo.getCustomStudyId())) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID,
                    studyBo.getCustomStudyId());
          }
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", language);
            return new ModelAndView("redirect:viewSettingAndAdmins.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", language);
            String isAutoSaved = request.getParameter("isAutoSaved");
            map.addAttribute("isAutoSaved", isAutoSaved);
            return new ModelAndView("redirect:viewBasicInfo.do", map);
          }
        } else if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.STUDY_PAUSED_ERR_MSG)) {
          request
              .getSession()
              .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG, message);
          map.addAttribute("language", language);
          return new ModelAndView("redirect:viewBasicInfo.do", map);
        } else if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.STUDY_ID_APP_ID_ERR_MSG)) {
          request
              .getSession()
              .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG, message);
          map.addAttribute("language", language);
          return new ModelAndView("redirect:viewBasicInfo.do", map);
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in set BasicInfo.");
          map.addAttribute("language", language);
          return new ModelAndView("redirect:viewBasicInfo.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateBasicInfo - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateBasicInfo - Ends");
    return mav;
  }

  /**
   * Study consent can have 0 or more comprehension test questions.Admin can mark whether or not
   * this is required for the study if required admin will need to add question text,answer options
   * and correct option and minimum score
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param comprehensionTestQuestionBo {@link ComprehensionTestQuestionBo}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateComprehensionTestQuestion.do")
  public ModelAndView saveOrUpdateComprehensionTestQuestionPage(
      HttpServletRequest request, ComprehensionTestQuestionBo comprehensionTestQuestionBo) {
    logger.info("StudyController - saveOrUpdateComprehensionTestQuestionPage - Starts");
    ModelAndView mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_LIST_PAGE);
    ComprehensionTestQuestionBo addComprehensionTestQuestionBo = null;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (comprehensionTestQuestionBo != null) {
          if (comprehensionTestQuestionBo.getId() != null) {
            comprehensionTestQuestionBo.setModifiedBy(sesObj.getUserId());
            comprehensionTestQuestionBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            comprehensionTestQuestionBo.setStatus(true);
          } else {
            if (comprehensionTestQuestionBo.getStudyId() != null) {
              int order =
                  studyService.comprehensionTestQuestionOrder(
                      comprehensionTestQuestionBo.getStudyId());
              comprehensionTestQuestionBo.setSequenceNo(order);
            }
            comprehensionTestQuestionBo.setCreatedBy(sesObj.getUserId());
            comprehensionTestQuestionBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            comprehensionTestQuestionBo.setStatus(true);
          }
          String language = request.getParameter("currentLanguage");
          addComprehensionTestQuestionBo =
              studyService.saveOrUpdateComprehensionTestQuestion(
                  comprehensionTestQuestionBo, language);
          map.addAttribute("_S", sessionStudyCount);
          if (addComprehensionTestQuestionBo != null) {
            if (addComprehensionTestQuestionBo.getId() != null) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("update.comprehensiontest.success.message"));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("save.comprehensiontest.success.message"));
            }
            map.addAttribute("language", language);
            return new ModelAndView("redirect:/adminStudies/comprehensionQuestionList.do", map);
          } else {
            request
                .getSession()
                .setAttribute(FdahpStudyDesignerConstants.SUC_MSG, "Unable to add Question added.");
            map.addAttribute("language", language);
            return new ModelAndView("redirect:/adminStudies/comprehensionQuestionList.do", map);
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateComprehensionTestQuestionPage - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateComprehensionTestQuestionPage - Ends");
    return mav;
  }

  /**
   * Study consent section is mandatory in mobile section admin can add one or more consent items
   * here.The consent item are two types which are research kit provided consents and custom defined
   * consent items admin can create the custom consent items which are not available in research kit
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param consentInfoBo {@link ConsentInfoBo}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateConsentInfo.do")
  public ModelAndView saveOrUpdateConsentInfo(
      HttpServletRequest request, ConsentInfoBo consentInfoBo) {
    logger.info("StudyController - saveOrUpdateConsentInfo - Starts");
    ModelAndView mav = new ModelAndView(FdahpStudyDesignerConstants.CONSENT_INFO_LIST_PAGE);
    ConsentInfoBo addConsentInfoBo = null;
    ModelMap map = new ModelMap();
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (consentInfoBo != null) {
          if ((consentInfoBo.getStudyId() != null) && (consentInfoBo.getId() == null)) {
            int order = studyService.consentInfoOrder(consentInfoBo.getStudyId());
            consentInfoBo.setSequenceNo(order);
          }
          customStudyId =
              (String)
                  request.getSession().getAttribute(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
          String language = request.getParameter("currentLanguage");
          addConsentInfoBo =
              studyService.saveOrUpdateConsentInfo(consentInfoBo, sesObj, customStudyId, language);
          if (addConsentInfoBo != null) {
            if (consentInfoBo.getId() != null) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("update.consent.success.message"));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("save.consent.success.message"));
            }
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    "Consent not added successfully.");
          }
          map.addAttribute("_S", sessionStudyCount);
          map.addAttribute("language", language);
          mav = new ModelAndView("redirect:/adminStudies/consentListPage.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateConsentInfo - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateConsentInfo - Ends");
    return mav;
  }

  /**
   * This method is used to save or update the Study Resource
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param resourceBO , {@link ResourceBO}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateResource.do")
  public ModelAndView saveOrUpdateResource(HttpServletRequest request, ResourceBO resourceBO) {
    logger.info("StudyController - saveOrUpdateResource() - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    Integer resourseId = 0;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String textOrPdfParam =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("textOrPdfParam"))
                ? ""
                : request.getParameter("textOrPdfParam");
        String resourceVisibilityParam =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("resourceVisibilityParam"))
                ? ""
                : request.getParameter("resourceVisibilityParam");
        String buttonText =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT);
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String studyProtocol =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL);
        String action =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.ACTION_ON))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.ACTION_ON);
        String resourceTypeParm =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("resourceTypeParm"))
                ? ""
                : request.getParameter("resourceTypeParm");
        String language = request.getParameter("language");
        if (resourceBO != null) {
          if (!("").equals(buttonText)) {
            if (("save").equalsIgnoreCase(buttonText)) {
              resourceBO.setAction(false);
            } else if (("done").equalsIgnoreCase(buttonText)) {
              resourceBO.setAction(true);
            }
          }
          if (!("").equals(studyProtocol)
              && (FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL).equalsIgnoreCase(studyProtocol)) {
            resourceBO.setStudyProtocol(true);
          } else {
            resourceBO.setStudyProtocol(false);
          }
          resourceBO.setStudyId(Integer.parseInt(studyId));
          resourceBO.setTextOrPdf(("0").equals(textOrPdfParam) ? false : true);
          resourceBO.setResourceVisibility(("0").equals(resourceVisibilityParam) ? false : true);
          if (!resourceBO.isResourceVisibility()) {
            resourceBO.setResourceType("0".equals(resourceTypeParm) ? false : true);
          } else {
            resourceBO.setResourceType(false);
          }
          if ((resourceBO.getStudyId() != null)
              && (resourceBO.getId() == null)
              && !resourceBO.isStudyProtocol()) {
            int order = studyService.resourceOrder(resourceBO.getStudyId());
            resourceBO.setSequenceNo(order);
          }
          resourseId = studyService.saveOrUpdateResource(resourceBO, sesObj, language);
        }
        if (!resourseId.equals(0)) {
          if ((resourceBO != null) && (resourceBO.getId() == null)) {
            if (("save").equalsIgnoreCase(buttonText)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      "Resource successfully added.");
            }
          } else {
            if (("save").equalsIgnoreCase(buttonText)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      "Resource successfully updated.");
            }
          }
        } else {
          if ((resourceBO != null) && (resourceBO.getId() == null)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    "Failed to add resource.");
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    "Failed to update resource.");
          }
        }
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("language", language);
        if (("save").equalsIgnoreCase(buttonText)) {
          request
              .getSession()
              .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_ON, action);
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.RESOURCE_INFO_ID,
                  resourseId + "");
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.IS_STUDY_PROTOCOL,
                  studyProtocol + "");
          mav = new ModelAndView("redirect:addOrEditResource.do", map);
        } else {
          mav = new ModelAndView("redirect:getResourceList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateResource() - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateResource() - Ends");
    return mav;
  }

  /**
   * save or update study setting and admins for the particular study study settings like Platforms
   * supported, Is the Study currently enrolling participants, Allow user to rejoin s the Study once
   * they leave it?, Retain participant data when they leave a study? managing admins for the
   * particular study
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateSettingAndAdmins.do")
  public ModelAndView saveOrUpdateSettingAndAdmins(HttpServletRequest request, StudyBo studyBo) {
    logger.info("StudyController - saveOrUpdateSettingAndAdmins - Starts");
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    String message = FdahpStudyDesignerConstants.FAILURE;
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {

        String currentLanguage = request.getParameter("currentLanguage");
        String userIds =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("userIds"))
                ? ""
                : request.getParameter("userIds");
        String permissions =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("permissions"))
                ? ""
                : request.getParameter("permissions");
        String newLanguages =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("newLanguages"))
                ? ""
                : request.getParameter("newLanguages");
        String deletedLanguages =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("deletedLanguages"))
                ? ""
                : request.getParameter("deletedLanguages");
        String projectLead =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("projectLead"))
                ? ""
                : request.getParameter("projectLead");
        String buttonText =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT);
        studyBo.setButtonText(buttonText);
        studyBo.setUserId(sesObj.getUserId());
        message =
            studyService.saveOrUpdateStudySettings(
                studyBo,
                sesObj,
                userIds,
                permissions,
                projectLead,
                newLanguages,
                deletedLanguages,
                currentLanguage);
        request
            .getSession()
            .setAttribute(
                sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyBo.getId() + "");
        map.addAttribute("_S", sessionStudyCount);
        if (FdahpStudyDesignerConstants.SUCCESS.equals(message)
            || FdahpStudyDesignerConstants.WARNING.equals(message)) {
          if (FdahpStudyDesignerConstants.WARNING.equals(message)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER,
                    FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER);
          }
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", currentLanguage);
            return new ModelAndView("redirect:overviewStudyPages.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", currentLanguage);
            return new ModelAndView("redirect:viewSettingAndAdmins.do", map);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in set Setting and Admins.");
          map.addAttribute("language", currentLanguage);
          return new ModelAndView("redirect:viewSettingAndAdmins.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateSettingAndAdmins - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateSettingAndAdmins - Ends");
    return mav;
  }

  /**
   * save or update Study Eligibility
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param eligibilityBo , {@link EligibilityBo}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateStudyEligibilty.do")
  public ModelAndView saveOrUpdateStudyEligibilty(
      HttpServletRequest request, EligibilityBo eligibilityBo) {
    logger.info("StudyController - saveOrUpdateStudyEligibilty - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    String result = FdahpStudyDesignerConstants.FAILURE;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      String language = request.getParameter("currentLanguage");
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (eligibilityBo != null) {
          if (eligibilityBo.getId() != null) {
            eligibilityBo.setModifiedBy(sesObj.getUserId());
            eligibilityBo.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          } else {
            eligibilityBo.setCreatedBy(sesObj.getUserId());
            eligibilityBo.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          customStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);

          result =
              studyService.saveOrUpdateStudyEligibilty(
                  eligibilityBo, sesObj, customStudyId, language);

          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID,
                  eligibilityBo.getStudyId() + "");
        }
        map.addAttribute("_S", sessionStudyCount);
        map.put("language", language);
        if (FdahpStudyDesignerConstants.SUCCESS.equals(result)) {
          if ((eligibilityBo != null) && ("save").equals(eligibilityBo.getActionType())) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            mav = new ModelAndView("redirect:consentListPage.do", map);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in set Eligibility.");
          mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateStudyEligibilty - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateStudyEligibilty - Ends");
    return mav;
  }

  /*------------------------------------Added By Vivek End---------------------------------------------------*/

  /**
   * save or update Study Eligibility
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param eligibilityTestBo , {@link EligibilityTestBo}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateStudyEligibiltyTestQusAns.do")
  public ModelAndView saveOrUpdateStudyEligibiltyTestQusAns(
      HttpServletRequest request, EligibilityTestBo eligibilityTestBo) {
    logger.info("StudyController - saveOrUpdateStudyEligibiltyTestQusAns - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    Integer result = 0;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      String studyId =
          (String)
              request
                  .getSession()
                  .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
      String actionTypeForQuestionPage =
          StringUtils.isNotBlank(request.getParameter("actionTypeForQuestionPage"))
              ? request.getParameter("actionTypeForQuestionPage")
              : "";
      String language = request.getParameter("currentLanguage");
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (eligibilityTestBo != null) {
          customStudyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(
                          sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
          result =
              studyService.saveOrUpdateEligibilityTestQusAns(
                  eligibilityTestBo, Integer.parseInt(studyId), sesObj, customStudyId, language);
        }
        map.addAttribute("_S", sessionStudyCount);
        if (result > 0) {
          if ((eligibilityTestBo != null)
              && (FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)
                  .equals(eligibilityTestBo.getType())) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + "actionTypeForQuestionPage", actionTypeForQuestionPage);
            request
                .getSession()
                .setAttribute(sessionStudyCount + "eligibilityTestId", eligibilityTestBo.getId());
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + "eligibilityId", eligibilityTestBo.getEligibilityId());
            map.put("language", language);
            mav = new ModelAndView("redirect:viewStudyEligibiltyTestQusAns.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            map.put("language", language);
            mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in set Eligibility Questions.");
          map.put("language", language);
          mav = new ModelAndView("redirect:viewStudyEligibilty.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateStudyEligibiltyTestQusAns - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateStudyEligibiltyTestQusAns - Ends");
    return mav;
  }

  /**
   * Description : save or update study notification details
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateStudyNotification.do")
  public ModelAndView saveOrUpdateStudyNotification(
      HttpServletRequest request, NotificationBO notificationBO) {
    logger.info("StudyController - saveOrUpdateStudyNotification - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    Integer notificationId = 0;
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String customStudyId = "";

    try {
      HttpSession session = request.getSession();
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      SessionObject sessionObject =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if ((sessionObject != null)
          && (sessionObject.getStudySession() != null)
          && sessionObject.getStudySession().contains(sessionStudyCount)) {
        String notificationType = "Study level";
        String currentDateTime =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("currentDateTime"))
                ? ""
                : request.getParameter("currentDateTime");
        String buttonType =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("buttonType"))
                ? ""
                : request.getParameter("buttonType");
        String actionPage =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ACTION_PAGE);
        /*
         * String appId1 = (String) request.getSession().getAttribute(sessionStudyCount
         * +"appId");
         */
        String appId1 = (String) request.getAttribute("appId");
        customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        if (StringUtils.isEmpty(actionPage)) {
          actionPage =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.ACTION_PAGE))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.ACTION_PAGE);
        }
        String language = request.getParameter("language");
        if (notificationBO != null) {
          if (!"".equals(buttonType)) {
            if ("save".equalsIgnoreCase(buttonType)) {
              notificationBO.setNotificationDone(false);
              notificationBO.setNotificationAction(false);
            } else if ("done".equalsIgnoreCase(buttonType)
                || FdahpStudyDesignerConstants.RESEND.equalsIgnoreCase(buttonType)) {
              notificationBO.setNotificationDone(true);
              notificationBO.setNotificationAction(true);
            }
          }
          if (FdahpStudyDesignerConstants.NOTIFICATION_NOTIMMEDIATE.equals(currentDateTime)) {
            notificationBO.setScheduleDate(
                FdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleDate())
                    ? String.valueOf(
                        FdahpStudyDesignerUtil.getFormattedDate(
                            notificationBO.getScheduleDate(),
                            FdahpStudyDesignerConstants.UI_SDF_DATE,
                            FdahpStudyDesignerConstants.DB_SDF_DATE))
                    : "");
            notificationBO.setScheduleTime(
                FdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleTime())
                    ? String.valueOf(
                        FdahpStudyDesignerUtil.getFormattedDate(
                            notificationBO.getScheduleTime(),
                            FdahpStudyDesignerConstants.SDF_TIME,
                            FdahpStudyDesignerConstants.DB_SDF_TIME))
                    : "");
            notificationBO.setNotificationScheduleType(
                FdahpStudyDesignerConstants.NOTIFICATION_NOTIMMEDIATE);
          } else if (FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE.equals(currentDateTime)) {
            notificationBO.setScheduleDate(FdahpStudyDesignerUtil.getCurrentDate());
            notificationBO.setScheduleTime(FdahpStudyDesignerUtil.getCurrentTime());
            notificationBO.setNotificationScheduleType(
                FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE);
          } else {
            notificationBO.setScheduleDate("");
            notificationBO.setScheduleTime("");
            notificationBO.setNotificationScheduleType("0");
          }
          String studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
          if (StringUtils.isEmpty(studyId)) {
            studyId =
                FdahpStudyDesignerUtil.isEmpty(
                        request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
          }
          if (StringUtils.isNotEmpty(studyId)) {
            StudyBo studyBo = studyService.getStudyById(studyId, 0);
            if (studyBo != null) {
              notificationBO.setCustomStudyId(studyBo.getCustomStudyId());
              notificationBO.setStudyId(Integer.valueOf(studyId));
              // notificationBO.setAppId(appId);
            }
          }
          if (notificationBO.getNotificationId() == null) {
            notificationBO.setCreatedBy(sessionObject.getUserId());
            notificationBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          } else {
            notificationBO.setModifiedBy(sessionObject.getUserId());
            notificationBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          notificationId =
              notificationService.saveOrUpdateOrResendNotification(
                  notificationBO,
                  notificationType,
                  buttonType,
                  sessionObject,
                  customStudyId,
                  language);
        }
        if (!notificationId.equals(0)) {
          if (notificationBO.getNotificationId() == null) {
            if ("save".equalsIgnoreCase(buttonType)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("save.notification.success.message"));
            }
          } else {
            if ("save".equalsIgnoreCase(buttonType)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            } else if (FdahpStudyDesignerConstants.RESEND.equalsIgnoreCase(buttonType)) {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("resend.notification.success.message"));
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                      propMap.get("update.notification.success.message"));
            }
          }
        } else {
          if ("save".equalsIgnoreCase(buttonType) && (notificationBO.getNotificationId() == null)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    propMap.get("save.notification.error.message"));
          } else if (FdahpStudyDesignerConstants.RESEND.equalsIgnoreCase(buttonType)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    propMap.get("resend.notification.error.message"));
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                    propMap.get("update.notification.error.message"));
          }
        }
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("language", language);
        if ("save".equalsIgnoreCase(buttonType)
            && !FdahpStudyDesignerConstants.ADDORCOPY.equals(actionPage)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.NOTIFICATIONID,
                  notificationId + "");
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.CHKREFRESHFLAG, "Y" + "");
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ACTION_TYPE, "edit" + "");
          mav = new ModelAndView("redirect:getStudyNotification.do", map);
        } else if ("save".equalsIgnoreCase(buttonType)
            && FdahpStudyDesignerConstants.ADDORCOPY.equals(actionPage)) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.NOTIFICATIONID,
                  notificationId + "");
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.CHKREFRESHFLAG, "Y" + "");
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ACTION_TYPE,
                  FdahpStudyDesignerConstants.ADDORCOPY + "");

          mav = new ModelAndView("redirect:getStudyNotification.do", map);
        } else {
          mav = new ModelAndView("redirect:/adminStudies/viewStudyNotificationList.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateStudyNotification - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateStudyNotification - Ends");
    return mav;
  }

  /**
   * save or update content(title,description,image) for the Overview pages of the Study those pages
   * will reflect on mobile overview screen
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/saveOrUpdateStudyOverviewPage.do")
  public ModelAndView saveOrUpdateStudyOverviewPage(
      HttpServletRequest request, StudyPageBean studyPageBean) {
    logger.info("StudyController - saveOrUpdateStudyOverviewPage - Starts");
    Map<String, String> propMap = FdahpStudyDesignerUtil.getAppProperties();
    String message = FdahpStudyDesignerConstants.FAILURE;
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      String buttonText = studyPageBean.getActionType();
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        studyPageBean.setUserId(sesObj.getUserId());
        String language = request.getParameter("currentLanguage");
        message = studyService.saveOrUpdateOverviewStudyPages(studyPageBean, sesObj, language);
        map.addAttribute("_S", sessionStudyCount);
        if (FdahpStudyDesignerConstants.SUCCESS.equals(message)) {
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.COMPLETED_BUTTON)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.COMPLETE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", language);
            return new ModelAndView("redirect:viewStudyEligibilty.do", map);
          } else {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG,
                    propMap.get(FdahpStudyDesignerConstants.SAVE_STUDY_SUCCESS_MESSAGE));
            map.addAttribute("language", language);
            return new ModelAndView("redirect:overviewStudyPages.do", map);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in setting Overview.");
          map.addAttribute("language", language);
          return new ModelAndView("redirect:overviewStudyPages.do", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - saveOrUpdateStudyOverviewPage - ERROR", e);
    }
    logger.info("StudyController - saveOrUpdateStudyOverviewPage - Ends");
    return mav;
  }

  /**
   * This method is used to validate the questionnaire have response type scale for android platform
   *
   * @author BTC
   * @param request
   * @param response
   */
  @RequestMapping(value = "/adminStudies/studyPlatformValidation", method = RequestMethod.POST)
  public void studyPlatformValidation(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - studyPlatformValidation() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String errorMessage = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String customStudyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        if (StringUtils.isEmpty(customStudyId)) {
          customStudyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.CUSTOM_STUDY_ID);
        }
        message =
            studyQuestionnaireService.checkQuestionnaireResponseTypeValidation(
                Integer.parseInt(studyId), customStudyId);
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          errorMessage = FdahpStudyDesignerConstants.PLATFORM_ERROR_MSG_ANDROID;
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      jsonobject.put("errorMessage", errorMessage);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - studyPlatformValidation() - ERROR", e);
    }
    logger.info("StudyController - studyPlatformValidation() - Ends");
  }

  /**
   * This method is used to validate the activetaskType for android platform
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @return
   */
  @RequestMapping(
      value = "/adminStudies/studyPlatformValidationforActiveTask.do",
      method = RequestMethod.POST)
  public void studyPlatformValidationforActiveTask(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - studyPlatformValidationforActiveTask() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String errorMessage = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        message = studyService.checkActiveTaskTypeValidation(Integer.parseInt(studyId));
        if (message.equals(FdahpStudyDesignerConstants.SUCCESS)) {
          errorMessage = FdahpStudyDesignerConstants.PLATFORM_ACTIVETASK_ERROR_MSG_ANDROID;
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      jsonobject.put("errorMessage", errorMessage);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - studyPlatformValidationforActiveTask() - ERROR", e);
    }
    logger.info("StudyController - studyPlatformValidationforActiveTask() - Ends");
  }

  /**
   * update the study status to Publish (as Upcoming Study)/Launch/Publish Updates/Pause or
   * Resume/Deactivate
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping(value = "/adminStudies/updateStudyAction.do", method = RequestMethod.POST)
  public ModelAndView updateStudyActionOnAction(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - updateStudyActionOnAction() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    String successMessage = "";
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        String buttonText =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT);
        if (StringUtils.isNotEmpty(studyId) && StringUtils.isNotEmpty(buttonText)) {
          message = studyService.updateStudyActionOnAction(studyId, buttonText, sesObj);
          if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.SUCCESS)) {
            if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PUBLISH)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_PUBLISH_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UNPUBLISH)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_UNPUBLISH_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_LUNCH_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_UPDATES_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_RESUME)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_RESUME_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PAUSE)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_PAUSE_SUCCESS_MSG;
            } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_DEACTIVATE)) {
              successMessage = FdahpStudyDesignerConstants.ACTION_DEACTIVATE_SUCCESS_MSG;
            }
            if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_DEACTIVATE)
                || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)
                || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {
              request
                  .getSession()
                  .setAttribute(FdahpStudyDesignerConstants.ACTION_SUC_MSG, successMessage);
            } else {
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.ACTION_SUC_MSG,
                      successMessage);
            }
          } else {
            if (message.equalsIgnoreCase(FdahpStudyDesignerConstants.FAILURE)) {
              request
                  .getSession()
                  .setAttribute(
                      FdahpStudyDesignerConstants.ERR_MSG,
                      FdahpStudyDesignerConstants.FAILURE_UPDATE_STUDY_MESSAGE);
            }
          }
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - updateStudyActionOnAction() - ERROR", e);
    }
    logger.info("StudyController - updateStudyActionOnAction() - Ends");
    return null;
  }

  @RequestMapping(value = "/adminStudies/switchToLiveMode.do", method = RequestMethod.POST)
  public ModelAndView switchStudyToLiveMode(
      HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - switchStudyToLiveMode() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);

        if (StringUtils.isNotEmpty(studyId)) {
          message = studyService.switchStudyToLiveMode(studyId);
        }
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      jsonobject.put("studyMode", "live");
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - switchStudyToLiveMode() - ERROR", e);
    }
    logger.info("StudyController - switchStudyToLiveMode() - Ends");
    return null;
  }

  /**
   * This method is used to validate the Eligibility Short Title
   *
   * @author BTC
   * @param request
   * @param response
   */
  @RequestMapping(
      value = "/adminStudies/validateEligibilityTestKey.do",
      method = RequestMethod.POST)
  public void validateEligibilityTestKey(HttpServletRequest request, HttpServletResponse response) {
    logger.info("StudyController - validateEligibilityTestKey() - Starts");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out = null;
    String message = FdahpStudyDesignerConstants.FAILURE;
    Integer eligibilityTestId;
    Integer eligibilityId;
    String shortTitle;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      eligibilityTestId =
          StringUtils.isNumeric(request.getParameter("eligibilityTestId"))
              ? Integer.parseInt(request.getParameter("eligibilityTestId"))
              : 0;
      shortTitle =
          StringUtils.isNotBlank(request.getParameter("shortTitle"))
              ? request.getParameter("shortTitle")
              : "";
      eligibilityId =
          StringUtils.isNumeric(request.getParameter("eligibilityId"))
              ? Integer.parseInt(request.getParameter("eligibilityId"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        message =
            studyService.validateEligibilityTestKey(eligibilityTestId, shortTitle, eligibilityId);
      }
      jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
      response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
      out = response.getWriter();
      out.print(jsonobject);
    } catch (Exception e) {
      logger.error("StudyController - validateEligibilityTestKey() - ERROR", e);
    }
    logger.info("StudyController - validateEligibilityTestKey() - Ends");
  }

  /**
   * validate the below items before do Publish (as Upcoming Study)/Launch/publish update of the
   * study
   *
   * <p>Publish (as Upcoming Study), following validations are done in this method. These are: Not
   * allowed, if the dates for study activities or resource availability are already crossed Not
   * allowed, if the Participant Enrollment status information is set to Yes in the Basic
   * Information section A warning is shown if the checklist items are not all marked as complete OR
   * if individual sections of study creation are not all marked as completed.
   *
   * <p>Launch/publish update , following validations are done in this method. These are: Not
   * allowed if individual sections of study creation are not all marked as completed. Not allowed,
   * if the dates for study activities or resource availability are already crossed A warning is
   * shown if the checklist items are not all marked as complete A warning is shown as Participant
   * Enrollment Status information
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @throws IOException
   * @return void
   */
  @RequestMapping(value = "/adminStudies/validateStudyAction.do", method = RequestMethod.POST)
  public void validateStudyAction(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.info("StudyActiveTasksController - validateStudyAction() - Starts ");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out;
    String message = FdahpStudyDesignerConstants.FAILURE;
    Checklist checklist = null;
    String checkListMessage = "No";
    String checkFailureMessage = "";
    boolean isRequiredSectionsCompleted = false;
    try {
      HttpSession session = request.getSession();
      SessionObject userSession =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((userSession != null)
          && (userSession.getStudySession() != null)
          && userSession.getStudySession().contains(sessionStudyCount)) {
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String buttonText =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.BUTTON_TEXT);
        if (StringUtils.isNotEmpty(buttonText)) {
          // validation and success/error message should send to
          // actionListPAge
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)
              || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)
              || buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PUBLISH)) {
            message = studyService.validateStudyAction(studyId, buttonText);
          } else {
            message = FdahpStudyDesignerConstants.SUCCESS;
          }
          if (message.equalsIgnoreCase(
              FdahpStudyDesignerConstants.PRE_PUBLISH_ENROLLMENT_ERROR_MSG)) {
            isRequiredSectionsCompleted = true;
          }
          checklist = studyService.getchecklistInfo(Integer.valueOf(studyId));
          if (checklist != null) {
            if ((checklist.isCheckbox1() && checklist.isCheckbox2())
                && (checklist.isCheckbox3() && checklist.isCheckbox4())) {
              checkListMessage = "Yes";
            } else {
              checkListMessage = "No";
            }
            if (checkListMessage.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              if ((checklist.isCheckbox3() && checklist.isCheckbox4())
                  && (checklist.isCheckbox5() && checklist.isCheckbox6())) {
                checkListMessage = "Yes";
              } else {
                checkListMessage = "No";
              }
            }
            if (checkListMessage.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              if ((checklist.isCheckbox5() && checklist.isCheckbox6())
                  && (checklist.isCheckbox7() && checklist.isCheckbox8())) {
                checkListMessage = "Yes";
              } else {
                checkListMessage = "No";
              }
            }
            if (checkListMessage.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)) {
              if (checklist.isCheckbox9() && checklist.isCheckbox10()) {
                checkListMessage = "Yes";
              } else {
                checkListMessage = "No";
              }
            }
          }
          if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_LUNCH)) {
            checkFailureMessage = FdahpStudyDesignerConstants.LUNCH_CHECKLIST_ERROR_MSG;
          } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_UPDATES)) {
            checkFailureMessage = FdahpStudyDesignerConstants.PUBLISH_UPDATE_CHECKLIST_ERROR_MSG;
          } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_RESUME)) {
            checkFailureMessage = FdahpStudyDesignerConstants.RESUME_CHECKLIST_ERROR_MSG;
          } else if (buttonText.equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_PUBLISH)) {
            checkListMessage = "Yes";
          }
        }
      }
    } catch (Exception e) {
      logger.error("StudyActiveTasksController - validateStudyAction() - ERROR ", e);
    }
    logger.info("StudyActiveTasksController - validateStudyAction() - Ends ");
    jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
    jsonobject.put("checkListMessage", checkListMessage);
    jsonobject.put("checkFailureMessage", checkFailureMessage);
    jsonobject.put("isRequiredSectionsCompleted", isRequiredSectionsCompleted);
    response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
    out = response.getWriter();
    out.print(jsonobject);
  }

  /**
   * validated for uniqueness of customStudyId of study throughout the application
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @throws IOException
   * @return void
   */
  @RequestMapping(value = "/adminStudies/validateStudyId.do", method = RequestMethod.POST)
  public void validateStudyId(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.info("StudyController - validateStudyId() - Starts ");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out;
    String message = FdahpStudyDesignerConstants.FAILURE;
    boolean flag = false;
    try {
      HttpSession session = request.getSession();
      SessionObject userSession =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (userSession != null) {
        String customStudyId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("customStudyId"))
                ? ""
                : request.getParameter("customStudyId");
        flag = studyService.validateStudyId(customStudyId);
        if (flag) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - validateStudyId() - ERROR ", e);
    }
    logger.info("StudyController - validateStudyId() - Ends ");
    jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
    response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
    out = response.getWriter();
    out.print(jsonobject);
  }

  /**
   * This method shows the basic information about the study basic info like Study ID, Study name,
   * Study full name, Study Category, Research Sponsor,Data Partner, Estimated Duration in
   * weeks/months/years, Study Tagline, Study Description, Study website, Study Type
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewBasicInfo.do")
  public ModelAndView viewBasicInfo(HttpServletRequest request) {
    logger.info("StudyController - viewBasicInfo - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    HashMap<String, List<ReferenceTablesBo>> referenceMap = null;
    List<ReferenceTablesBo> categoryList = null;
    List<ReferenceTablesBo> dataPartnerList = null;
    StudyBo studyBo = null;
    String sucMsg = "";
    String errMsg = "";
    ConsentBo consentBo = null;
    StudyIdBean studyIdBean = null;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID));
        String permission =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION));
        String isLive =
            (String)
                (FdahpStudyDesignerUtil.isEmpty(
                        (String)
                            request
                                .getSession()
                                .getAttribute(
                                    sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE))
                    ? ""
                    : request
                        .getSession()
                        .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE));

        if (FdahpStudyDesignerUtil.isEmpty(isLive)) {
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE);
        }

        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
          if (StringUtils.isNotEmpty(isLive)
              && isLive.equalsIgnoreCase(FdahpStudyDesignerConstants.YES)
              && (studyBo != null)) {
            studyIdBean = studyService.getLiveVersion(studyBo.getCustomStudyId());
            if (studyIdBean != null) {
              consentBo =
                  studyService.getConsentDetailsByStudyId(
                      studyIdBean.getConsentStudyId().toString());
              request
                  .getSession()
                  .setAttribute(
                      sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_STUDY_ID,
                      studyIdBean.getConsentStudyId().toString());
              if (studyIdBean.getActivetaskStudyId() != null) {
                request
                    .getSession()
                    .setAttribute(
                        sessionStudyCount + FdahpStudyDesignerConstants.ACTIVE_TASK_STUDY_ID,
                        studyIdBean.getActivetaskStudyId().toString());
              }
              if (studyIdBean.getQuestionnarieStudyId() != null) {
                request
                    .getSession()
                    .setAttribute(
                        sessionStudyCount + FdahpStudyDesignerConstants.QUESTIONNARIE_STUDY_ID,
                        studyIdBean.getQuestionnarieStudyId().toString());
              }
            }
          } else {
            consentBo = studyService.getConsentDetailsByStudyId(studyId);
          }
          // get consentId if exists for studyId
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID);
          if (consentBo != null) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID, consentBo.getId());
          } else {
            request
                .getSession()
                .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.CONSENT_ID);
          }
          String language = request.getParameter("language");
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            this.setStudyLangData(studyId, language, map);
          }
          map.addAttribute("currLanguage", language);
          map.addAttribute("isAutoSaved", request.getParameter("isAutoSaved"));
        }
        if (studyBo == null) {
          studyBo = new StudyBo();
          studyBo.setType(FdahpStudyDesignerConstants.STUDY_TYPE_GT);
          studyBo.setStudyLanguage(FdahpStudyDesignerConstants.STUDY_LANGUAGE_ENGLISH);
        } else if ((studyBo != null) && StringUtils.isNotEmpty(studyBo.getCustomStudyId())) {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.CUSTOM_STUDY_ID,
                  studyBo.getCustomStudyId());
        }
        // grouped for Study category , Research Sponsors , Data partner
        referenceMap =
            (HashMap<String, List<ReferenceTablesBo>>) studyService.getreferenceListByCategory();
        if ((referenceMap != null) && (referenceMap.size() > 0)) {
          for (String key : referenceMap.keySet()) {
            if (StringUtils.isNotEmpty(key)) {
              switch (key) {
                case FdahpStudyDesignerConstants.REFERENCE_TYPE_CATEGORIES:
                  categoryList = referenceMap.get(key);
                  break;
                case FdahpStudyDesignerConstants.REFERENCE_TYPE_DATA_PARTNER:
                  dataPartnerList = referenceMap.get(key);
                  break;
                default:
                  break;
              }
            }
          }
        }
        if (studyBo.getStudyMode() == null) {
          map.addAttribute("studyMode", FdahpStudyDesignerConstants.STUDY_MODE_LIVE);
        } else {
          map.addAttribute("studyMode", studyBo.getStudyMode());
        }
        if (studyBo != null && studyBo.getSwitchVal() != null && studyBo.getSwitchVal() == 1) {
          studyBo.setCustomStudyId("");
          studyBo.setAppId("");
        }

        String languages = studyBo.getSelectedLanguages();
        List<String> langList = new ArrayList<>();
        Map<String, String> langMap = new HashMap<>();
        if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
          langList = Arrays.asList(languages.split(","));
          for (String string : langList) {
            langMap.put(string, MultiLanguageCodes.getValue(string));
          }
        }
        map.addAttribute("languageList", langMap);

        map.addAttribute("categoryList", categoryList);
        map.addAttribute("dataPartnerList", dataPartnerList);
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
        map.addAttribute("createStudyId", "true");
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);

        mav = new ModelAndView("viewBasicInfo", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - viewBasicInfo - ERROR", e);
    }
    logger.info("StudyController - viewBasicInfo - Ends");
    return mav;
  }

  /**
   * This method shows configuration of study settings and admin list of the study study settings
   * like Platforms supported, Is the Study currently enrolling participants, Allow user to rejoin s
   * the Study once they leave it?, Retain participant data when they leave a study?
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewSettingAndAdmins.do")
  public ModelAndView viewSettingAndAdmins(HttpServletRequest request) {
    logger.info("StudyController - viewSettingAndAdmins - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    StudyBo studyBo = null;
    String sucMsg = "";
    String errMsg = "";
    List<UserBO> userList = null;
    List<StudyPermissionBO> studyPermissionList = null;
    List<Integer> permissions = null;
    String user = "";
    boolean isAnchorForEnrollmentLive = false;
    boolean isAnchorForEnrollmentDraft = false;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            FdahpStudyDesignerUtil.isEmpty(
                    request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                ? ""
                : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        if (FdahpStudyDesignerUtil.isEmpty(studyId)) {
          studyId =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        }
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        map.addAttribute("_S", sessionStudyCount);
        user =
            (String)
                request
                    .getSession()
                    .getAttribute(
                        sessionStudyCount + FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER);
        if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());

          String currLang = request.getParameter("language");
          if ("undefined".equals(currLang)) currLang = null;
          if (FdahpStudyDesignerUtil.isNotEmpty(currLang)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(currLang)) {
            this.setStudyLangData(studyId, currLang, map);
          }
          map.addAttribute("currLanguage", currLang);
          /*
           * Get the active user list whom are not yet added to the particular study
           */
          userList =
              studyService.getActiveNonAddedUserList(Integer.parseInt(studyId), sesObj.getUserId());
          /*
           * This method is used to get the uses whom are already added to the particular
           * study
           */
          studyPermissionList =
              studyService.getAddedUserListToStudy(Integer.parseInt(studyId), sesObj.getUserId());
          /* Get the permissions of the user */
          permissions = usersService.getPermissionsByUserId(sesObj.getUserId());
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
          map.addAttribute("userList", userList);
          map.addAttribute("studyPermissionList", studyPermissionList);
          map.addAttribute("permissions", permissions);
          map.addAttribute("user", user);

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new LinkedList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("selectedLanguages", langMap);

          if (studyBo.getMultiLanguageFlag() != null && studyBo.getMultiLanguageFlag()) {
            map.addAttribute(
                "langDeletableMap", studyService.isLanguageDeletable(studyBo.getCustomStudyId()));
          }

          ResourceBundle resourceBundle = ResourceBundle.getBundle("messageResource");
          String[] supportedLang = resourceBundle.getString("languageList").split(",");
          langMap = new HashMap<>();
          for (String str : supportedLang) {
            if (!langList.contains(str) && !"en".equals(str))
              langMap.put(str, MultiLanguageCodes.getValue(str));
          }
          map.addAttribute("supportedLanguages", langMap);

          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER);
          isAnchorForEnrollmentLive =
              studyService.isAnchorDateExistForEnrollment(
                  studyBo.getId(), studyBo.getCustomStudyId());
          isAnchorForEnrollmentDraft =
              studyService.isAnchorDateExistForEnrollmentDraftStudy(
                  studyBo.getId(), studyBo.getCustomStudyId());
          map.addAttribute("isAnchorForEnrollmentLive", isAnchorForEnrollmentLive);
          map.addAttribute("isAnchorForEnrollmentDraft", isAnchorForEnrollmentDraft);

          mav = new ModelAndView(FdahpStudyDesignerConstants.VIEW_SETTING_AND_ADMINS, map);
        } else {
          return new ModelAndView("redirect:studyList.do");
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - viewSettingAndAdmins - ERROR", e);
    }
    logger.info("StudyController - viewSettingAndAdmins - Ends");
    return mav;
  }

  /**
   * Setting Request Sessions (setting of study session count for each request is study live or not,
   * study permission for view/edit, setting studyId to the Request session, ) of particular study
   * and redirecting study basic page
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewStudyDetails.do")
  public ModelAndView viewStudyDetails(HttpServletRequest request) {
    Integer sessionStudyCount;
    ModelMap map = new ModelMap();
    ModelAndView modelAndView = new ModelAndView("redirect:/adminStudies/studyList.do");
    String studyId =
        FdahpStudyDesignerUtil.isEmpty(request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
            ? ""
            : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
    String permission =
        FdahpStudyDesignerUtil.isEmpty(request.getParameter(FdahpStudyDesignerConstants.PERMISSION))
            ? ""
            : request.getParameter(FdahpStudyDesignerConstants.PERMISSION);
    String isLive =
        FdahpStudyDesignerUtil.isEmpty(request.getParameter(FdahpStudyDesignerConstants.IS_LIVE))
            ? ""
            : request.getParameter(FdahpStudyDesignerConstants.IS_LIVE);
    SessionObject sesObj =
        (SessionObject)
            request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
    List<Integer> studySessionList = new ArrayList<>();
    List<StudySessionBean> studySessionBeans = new ArrayList<>();
    StudySessionBean studySessionBean = null;
    try {
      sessionStudyCount =
          (Integer)
              (request.getSession().getAttribute("sessionStudyCount") != null
                  ? request.getSession().getAttribute("sessionStudyCount")
                  : 0);
      if (sesObj != null) {
        if ((sesObj.getStudySessionBeans() != null) && !sesObj.getStudySessionBeans().isEmpty()) {
          for (StudySessionBean sessionBean : sesObj.getStudySessionBeans()) {
            if ((sessionBean != null)
                && sessionBean.getPermission().equals(permission)
                && sessionBean.getIsLive().equals(isLive)
                && sessionBean.getStudyId().equals(studyId)) {
              studySessionBean = sessionBean;
            }
          }
        }
        if (studySessionBean != null) {
          sessionStudyCount = studySessionBean.getSessionStudyCount();
        } else {
          ++sessionStudyCount;
          if ((sesObj.getStudySession() != null) && !sesObj.getStudySession().isEmpty()) {
            studySessionList.addAll(sesObj.getStudySession());
          }
          studySessionList.add(sessionStudyCount);
          sesObj.setStudySession(studySessionList);

          if ((sesObj.getStudySessionBeans() != null) && !sesObj.getStudySessionBeans().isEmpty()) {
            studySessionBeans.addAll(sesObj.getStudySessionBeans());
          }
          studySessionBean = new StudySessionBean();
          studySessionBean.setIsLive(isLive);
          studySessionBean.setPermission(permission);
          studySessionBean.setSessionStudyCount(sessionStudyCount);
          studySessionBean.setStudyId(studyId);
          studySessionBeans.add(studySessionBean);
          sesObj.setStudySessionBeans(studySessionBeans);
        }
      }

      map.addAttribute("_S", sessionStudyCount);
      request.getSession().setAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT, sesObj);
      request.getSession().setAttribute("sessionStudyCount", sessionStudyCount);
      request
          .getSession()
          .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyId);
      request
          .getSession()
          .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION, permission);
      request
          .getSession()
          .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.IS_LIVE, isLive);
      modelAndView = new ModelAndView("redirect:/adminStudies/viewBasicInfo.do", map);
    } catch (Exception e) {
      logger.error("StudyController - viewStudyDetails - ERROR", e);
    }
    return modelAndView;
  }

  /**
   * view Eligibility page
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewStudyEligibilty.do")
  public ModelAndView viewStudyEligibilty(HttpServletRequest request) {
    logger.info("StudyController - viewStudyEligibilty - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    StudyBo studyBo = null, liveStudyBo = null;
    String sucMsg = "";
    String errMsg = "";
    EligibilityBo eligibilityBo = null;
    List<EligibilityTestBo> eligibilityTestList = new ArrayList<EligibilityTestBo>();
    List<EligibilityTestLangBo> eligibilityTestLangList = new ArrayList<>();
    boolean isLiveStudy = false;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);

        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? "0"
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        if (StringUtils.isNotEmpty(studyId)) {
          studyBo = studyService.getStudyById(studyId, sesObj.getUserId());

          Map<String, String> langMap = new HashMap<>();
          if (studyBo != null) {
            String languages = studyBo.getSelectedLanguages();
            List<String> langList = new ArrayList<>();

            if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
              langList = Arrays.asList(languages.split(","));
              for (String string : langList) {
                langMap.put(string, MultiLanguageCodes.getValue(string));
              }
            }
          }
          map.addAttribute("languageList", langMap);

          eligibilityBo = studyService.getStudyEligibiltyByStudyId(studyId);
          liveStudyBo = studyService.getStudyLiveStatusByCustomId(studyBo.getCustomStudyId());
          if (liveStudyBo != null) {
            isLiveStudy = true;
          }
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

          if (eligibilityBo == null) {
            eligibilityBo = new EligibilityBo();
            eligibilityBo.setStudyId(Integer.parseInt(studyId));
            eligibilityBo.setInstructionalText(
                FdahpStudyDesignerConstants.ELIGIBILITY_TOKEN_TEXT_DEFAULT);
          }
          if (eligibilityBo.getId() != null) {
            eligibilityTestList =
                studyService.viewEligibilityTestQusAnsByEligibilityId(eligibilityBo.getId());
          }
          String currLang = request.getParameter("language");
          if (FdahpStudyDesignerUtil.isNotEmpty(currLang) && !"en".equals(currLang)) {
            StudyLanguageBO studyLanguageBO = new StudyLanguageBO();
            eligibilityTestLangList =
                studyService.syncEligibilityTestDataInLanguageTable(
                    eligibilityTestList, currLang, studyId);
            studyLanguageBO =
                studyService.getStudyLanguageById(Integer.parseInt(studyId), currLang);
            map.addAttribute(
                "mlInstructionalText",
                studyLanguageBO != null ? studyLanguageBO.getInstructionalText() : "");
            this.setStudyLangData(studyId, currLang, map);
          }
          map.addAttribute("currLanguage", currLang);
        }

        map.addAttribute("eligibilityTestLangList", eligibilityTestLangList);
        map.addAttribute("eligibilityTestList", eligibilityTestList);
        map.addAttribute("eligibility", eligibilityBo);
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("liveStatus", isLiveStudy);
        mav = new ModelAndView("studyEligibiltyPage", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - viewStudyEligibilty - ERROR", e);
    }
    logger.info("StudyController - viewStudyEligibilty - Ends");
    return mav;
  }

  /**
   * view Eligibility page
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewStudyEligibiltyTestQusAns.do")
  public ModelAndView viewStudyEligibiltyTestQusAns(HttpServletRequest request) {
    logger.info("StudyController - viewStudyEligibiltyTestQusAns - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    ModelMap map = new ModelMap();
    String sucMsg = "";
    String errMsg = "";
    EligibilityTestBo eligibilityTest = null;
    EligibilityTestLangBo eligibilityTestLangBo = null;
    StudyBo studyBo;
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      String actionTypeForQuestionPage =
          StringUtils.isNotBlank(request.getParameter("actionTypeForQuestionPage"))
              ? request.getParameter("actionTypeForQuestionPage")
              : "";
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }

        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        Integer eligibilityTestId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("eligibilityTestId"))
                ? 0
                : Integer.parseInt(request.getParameter("eligibilityTestId"));
        Integer eligibilityId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("eligibilityId"))
                ? 0
                : Integer.parseInt(request.getParameter("eligibilityId"));
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? "0"
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        if (StringUtils.isBlank(actionTypeForQuestionPage)) {
          actionTypeForQuestionPage =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + "actionTypeForQuestionPage");
          request.getSession().removeAttribute(sessionStudyCount + "actionTypeForQuestionPage");
        }
        if (eligibilityTestId.equals(0)) {
          eligibilityTestId =
              (Integer) request.getSession().getAttribute(sessionStudyCount + "eligibilityTestId");
          request.getSession().removeAttribute(sessionStudyCount + "eligibilityTestId");
        }
        if (eligibilityId.equals(0)) {
          eligibilityId =
              (Integer) request.getSession().getAttribute(sessionStudyCount + "eligibilityId");
          request.getSession().removeAttribute(sessionStudyCount + "eligibilityId");
        }
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        studyBo = studyService.getStudyById(studyId, sesObj.getUserId());
        map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);

        if (StringUtils.isNotBlank(studyId) && StringUtils.isNotBlank(actionTypeForQuestionPage)) {
          if (eligibilityTestId != null) {
            String language = request.getParameter("language");
            if (FdahpStudyDesignerUtil.isNotEmpty(language) && !"en".equals(language)) {
              eligibilityTestLangBo =
                  studyService.getEligibilityTestLangById(eligibilityTestId, language);
              this.setStudyLangData(studyId, language, map);
            }
            map.addAttribute("currLanguage", language);
            eligibilityTest = studyService.viewEligibilityTestQusAnsById(eligibilityTestId);
          }

          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);
          map.addAttribute("eligibilityTestLangBo", eligibilityTestLangBo);
          map.addAttribute("eligibilityTest", eligibilityTest);
          map.addAttribute("eligibilityId", eligibilityId);
          map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
          map.addAttribute("_S", sessionStudyCount);
          map.addAttribute("actionTypeForQuestionPage", actionTypeForQuestionPage);
          mav = new ModelAndView("studyEligibiltyTestPage", map);
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - viewStudyEligibiltyTestQusAns - ERROR", e);
    }
    logger.info("StudyController - viewStudyEligibiltyTestQusAns - Ends");
    return mav;
  }

  /**
   * Description : Study notification list details
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @return {@link ModelAndView}
   */
  @RequestMapping("/adminStudies/viewStudyNotificationList.do")
  public ModelAndView viewStudyNotificationList(HttpServletRequest request) {
    logger.info("StudyController - viewNotificationList() - Starts");
    ModelMap map = new ModelMap();
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    String sucMsg = "";
    String errMsg = "";
    List<NotificationBO> notificationList = null;
    List<NotificationBO> notificationSavedList = null;
    StudyBo studyBo = null;
    StudyBo studyLive = null;
    try {
      HttpSession session = request.getSession();
      SessionObject sessionObject =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sessionObject != null)
          && (sessionObject.getStudySession() != null)
          && sessionObject.getStudySession().contains(sessionStudyCount)) {
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG)) {
          sucMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.SUC_MSG, sucMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.SUC_MSG);
        }
        if (null
            != request
                .getSession()
                .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG)) {
          errMsg =
              (String)
                  request
                      .getSession()
                      .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
          map.addAttribute(FdahpStudyDesignerConstants.ERR_MSG, errMsg);
          request
              .getSession()
              .removeAttribute(sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG);
        }
        String type = FdahpStudyDesignerConstants.STUDYLEVEL;
        String studyId =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID);
        String permission =
            (String)
                request
                    .getSession()
                    .getAttribute(sessionStudyCount + FdahpStudyDesignerConstants.PERMISSION);
        if (StringUtils.isEmpty(studyId)) {
          studyId =
              FdahpStudyDesignerUtil.isEmpty(
                      request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
                  ? ""
                  : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
        }
        String language = request.getParameter("language");
        if (StringUtils.isNotEmpty(studyId)) {
          /*
           * Passing studyId in the param to fetch study related notification list and
           * type to define study notification in service level
           */
          notificationList =
              notificationService.getNotificationList(Integer.valueOf(studyId), type);
          for (NotificationBO notification : notificationList) {
            if (!notification.isNotificationSent()
                && notification
                    .getNotificationScheduleType()
                    .equals(FdahpStudyDesignerConstants.NOTIFICATION_NOTIMMEDIATE)) {
              notification.setCheckNotificationSendingStatus("Not sent");
            } else if (!notification.isNotificationSent()
                && notification
                    .getNotificationScheduleType()
                    .equals(FdahpStudyDesignerConstants.NOTIFICATION_IMMEDIATE)) {
              notification.setCheckNotificationSendingStatus("Sending");
            } else if (notification.isNotificationSent()) {
              notification.setCheckNotificationSendingStatus("Sent");
            }
          }
          studyBo = studyService.getStudyById(studyId, sessionObject.getUserId());
          if ((studyBo != null)
              && FdahpStudyDesignerConstants.STUDY_ACTIVE.equals(studyBo.getStatus())) {
            studyLive = studyService.getStudyLiveStatusByCustomId(studyBo.getCustomStudyId());
          } else {
            studyLive = studyBo;
          }
          notificationSavedList = studyService.getSavedNotification(Integer.valueOf(studyId));
          map.addAttribute("notificationList", notificationList);
          map.addAttribute("studyLive", studyLive);
          map.addAttribute(FdahpStudyDesignerConstants.STUDY_BO, studyBo);
          map.addAttribute("notificationSavedList", notificationSavedList);
          map.addAttribute("currLanguage", language);
          String languages = studyBo.getSelectedLanguages();
          List<String> langList = new ArrayList<>();
          Map<String, String> langMap = new HashMap<>();
          if (FdahpStudyDesignerUtil.isNotEmpty(languages)) {
            langList = Arrays.asList(languages.split(","));
            for (String string : langList) {
              langMap.put(string, MultiLanguageCodes.getValue(string));
            }
          }
          map.addAttribute("languageList", langMap);
          if (FdahpStudyDesignerUtil.isNotEmpty(language)
              && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
            List<NotificationLangBO> notificationLangBOList =
                notificationService.getNotificationLangList(
                    Integer.parseInt(studyId),
                    language,
                    notificationList,
                    sessionObject.getUserId());
            map.addAttribute("notificationLangBOList", notificationLangBOList);
            this.setStudyLangData(studyId, language, map);
          }
        }
        map.addAttribute(FdahpStudyDesignerConstants.PERMISSION, permission);
        map.addAttribute("_S", sessionStudyCount);
        map.addAttribute("appId", studyBo.getAppId());
        mav = new ModelAndView("studyNotificationList", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - viewStudyNotificationList() - ERROR ", e);
    }
    logger.info("StudyController - viewStudyNotificationList() - ends");
    return mav;
  }

  /**
   * validated for uniqueness of customStudyId of study throughout the application
   *
   * @author BTC
   * @param request , {@link HttpServletRequest}
   * @param response , {@link HttpServletResponse}
   * @throws IOException
   * @return void
   */
  @RequestMapping(value = "/adminStudies/validateAppId.do", method = RequestMethod.POST)
  public void validateAppId(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    logger.info("StudyController - validateAppId() - Starts ");
    JSONObject jsonobject = new JSONObject();
    PrintWriter out;
    String message = FdahpStudyDesignerConstants.FAILURE;
    boolean flag = false;
    try {
      HttpSession session = request.getSession();
      SessionObject userSession =
          (SessionObject) session.getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);
      if (userSession != null) {
        String customStudyId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("customStudyId"))
                ? ""
                : request.getParameter("customStudyId");

        String appId =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("appId"))
                ? ""
                : request.getParameter("appId");
        appId = appId.toUpperCase();

        String studyType =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("studyType"))
                ? ""
                : request.getParameter("studyType");
        flag = studyService.validateAppId(customStudyId, appId, studyType);
        if (flag) {
          message = FdahpStudyDesignerConstants.SUCCESS;
        }
      }
    } catch (Exception e) {
      logger.error("StudyController - validateAppId() - ERROR ", e);
    }
    logger.info("StudyController - validateAppId() - Ends ");
    jsonobject.put(FdahpStudyDesignerConstants.MESSAGE, message);
    response.setContentType(FdahpStudyDesignerConstants.APPLICATION_JSON);
    out = response.getWriter();
    out.print(jsonobject);
  }

  private void setStudyLangData(String studyId, String language, ModelMap map) {
    StudyLanguageBO studyLanguageBO = new StudyLanguageBO();
    StudySequenceLangBO studySequenceLangBO = new StudySequenceLangBO();
    if (FdahpStudyDesignerUtil.isNotEmpty(studyId)) {
      studyLanguageBO = studyService.getStudyLanguageById(Integer.parseInt(studyId), language);
      studySequenceLangBO = studyService.getStudySequenceById(Integer.parseInt(studyId), language);
    }
    map.addAttribute("studyLanguageBO", studyLanguageBO);
    map.addAttribute("sequenceLangBO", studySequenceLangBO);
  }

  @RequestMapping("/adminStudies/removeSelectedLanguage.do")
  public ModelAndView removeSelectedLanguage(HttpServletRequest request, StudyBo studyBo) {
    logger.info("StudyController - removeSelectedLanguage - Starts");
    ModelAndView mav = new ModelAndView("redirect:/adminStudies/studyList.do");
    String message;
    ModelMap map = new ModelMap();
    try {
      SessionObject sesObj =
          (SessionObject)
              request.getSession().getAttribute(FdahpStudyDesignerConstants.SESSION_OBJECT);

      String studyId =
          FdahpStudyDesignerUtil.isEmpty(request.getParameter(FdahpStudyDesignerConstants.STUDY_ID))
              ? ""
              : request.getParameter(FdahpStudyDesignerConstants.STUDY_ID);
      Integer sessionStudyCount =
          StringUtils.isNumeric(request.getParameter("_S"))
              ? Integer.parseInt(request.getParameter("_S"))
              : 0;
      if ((sesObj != null)
          && (sesObj.getStudySession() != null)
          && sesObj.getStudySession().contains(sessionStudyCount)) {

        String currentLanguage = request.getParameter("currentLanguage");
        String deletedLanguage =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("deletedLanguage"))
                ? ""
                : request.getParameter("deletedLanguage");
        String newLanguages =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("newLanguages"))
                ? ""
                : request.getParameter("newLanguages");
        String mlFlag =
            FdahpStudyDesignerUtil.isEmpty(request.getParameter("mlFlag"))
                ? ""
                : request.getParameter("mlFlag");
        studyBo.setUserId(sesObj.getUserId());
        message =
            studyService.removeExistingLanguageAndData(
                studyId, sesObj, deletedLanguage, newLanguages, mlFlag);
        request
            .getSession()
            .setAttribute(sessionStudyCount + FdahpStudyDesignerConstants.STUDY_ID, studyId + "");
        map.addAttribute("_S", sessionStudyCount);
        if (FdahpStudyDesignerConstants.SUCCESS.equals(message)
            || FdahpStudyDesignerConstants.WARNING.equals(message)) {
          if (FdahpStudyDesignerConstants.WARNING.equals(message)) {
            request
                .getSession()
                .setAttribute(
                    sessionStudyCount + FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER,
                    FdahpStudyDesignerConstants.LOGOUT_LOGIN_USER);
          }
        } else {
          request
              .getSession()
              .setAttribute(
                  sessionStudyCount + FdahpStudyDesignerConstants.ERR_MSG,
                  "Error in set Setting and Admins.");
        }
        map.addAttribute("language", currentLanguage);
        map.addAttribute("studyId", studyId);
        return new ModelAndView("redirect:viewSettingAndAdmins.do", map);
      }
    } catch (Exception e) {
      logger.error("StudyController - removeSelectedLanguage - ERROR", e);
    }
    logger.info("StudyController - removeSelectedLanguage - Ends");
    return mav;
  }
}
