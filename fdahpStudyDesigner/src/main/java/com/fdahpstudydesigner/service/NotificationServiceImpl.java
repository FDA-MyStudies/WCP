package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bo.MultiLanguageCodes;
import com.fdahpstudydesigner.bo.NotificationBO;
import com.fdahpstudydesigner.bo.NotificationHistoryBO;
import com.fdahpstudydesigner.bo.NotificationLangBO;
import com.fdahpstudydesigner.bo.NotificationLangPK;
import com.fdahpstudydesigner.bo.StudySequenceLangBO;
import com.fdahpstudydesigner.dao.NotificationDAO;
import com.fdahpstudydesigner.dao.StudyDAO;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

  @Autowired private NotificationDAO notificationDAO;

  @Autowired private StudyDAO studyDAO;

  @Autowired private StudyService studyService;

  /**
   * Deleting detail of notification by Id.
   *
   * @author BTC
   * @param notificationIdForDelete
   * @param sessionObject , Object of {@link SessionObject}
   * @param notificationType , global/study notification
   * @return Object of {@link String}
   */
  @Override
  public String deleteNotification(
      int notificationIdForDelete, SessionObject sessionObject, String notificationType) {
    logger.info("NotificationServiceImpl - deleteNotification - Starts");
    String message = FdahpStudyDesignerConstants.FAILURE;
    try {
      message =
          notificationDAO.deleteNotification(
              notificationIdForDelete, sessionObject, notificationType);
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - deleteNotification - ERROR", e);
    }
    logger.info("NotificationServiceImpl - deleteNotification - Ends");
    return message;
  }

  /**
   * Getting detail of notification by Id.
   *
   * @author BTC
   * @param notificationId
   * @return Object of {@link NotificationBO}
   */
  @Override
  public NotificationBO getNotification(int notificationId) {
    logger.info("NotificationServiceImpl - getNotification - Starts");
    NotificationBO notificationBO = null;
    try {
      notificationBO = notificationDAO.getNotification(notificationId);
      if (null != notificationBO) {
        notificationBO.setScheduleDate(
            FdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleDate())
                ? String.valueOf(
                    FdahpStudyDesignerUtil.getFormattedDate(
                        notificationBO.getScheduleDate(),
                        FdahpStudyDesignerConstants.DB_SDF_DATE,
                        FdahpStudyDesignerConstants.UI_SDF_DATE))
                : "");
        notificationBO.setScheduleTime(
            FdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleTime())
                ? String.valueOf(
                    FdahpStudyDesignerUtil.getFormattedDate(
                        notificationBO.getScheduleTime(),
                        FdahpStudyDesignerConstants.DB_SDF_TIME,
                        FdahpStudyDesignerConstants.SDF_TIME))
                : "");
      }
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - getNotification - ERROR", e);
    }
    logger.info("NotificationServiceImpl - getNotification - Ends");
    return notificationBO;
  }

  /**
   * Getting detail of NotificationHistorylist that has been triggered to users which has dateTime.
   *
   * @author BTC
   * @param notificationId
   * @return List of {@link NotificationHistoryBO}
   */
  @Override
  public List<NotificationHistoryBO> getNotificationHistoryListNoDateTime(int notificationId) {
    logger.info("NotificationServiceImpl - getNotificationHistoryListNoDateTime() - Starts");
    List<NotificationHistoryBO> notificationHistoryListNoDateTime = null;
    try {
      notificationHistoryListNoDateTime =
          notificationDAO.getNotificationHistoryListNoDateTime(notificationId);
      if (notificationHistoryListNoDateTime != null
          && !notificationHistoryListNoDateTime.isEmpty()) {
        for (NotificationHistoryBO notificationHistoryBO : notificationHistoryListNoDateTime) {
          if (notificationHistoryBO.getNotificationSentDateTime() != null) {
            String date =
                FdahpStudyDesignerUtil.getFormattedDate(
                    notificationHistoryBO.getNotificationSentDateTime(),
                    FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                    FdahpStudyDesignerConstants.UI_SDF_DATE); // 8/29/2011
            String time =
                FdahpStudyDesignerUtil.getFormattedDate(
                    notificationHistoryBO.getNotificationSentDateTime(),
                    FdahpStudyDesignerConstants.DB_SDF_DATE_TIME,
                    FdahpStudyDesignerConstants.SDF_TIME); // 11:16:12
            // AM
            notificationHistoryBO.setNotificationSentdtTime("Last Sent on " + date + " at " + time);
          }
        }
      }
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - getNotificationHistoryListNoDateTime - ERROR", e);
    }
    logger.info("NotificationServiceImpl - getNotificationHistoryListNoDateTime - Ends");
    return notificationHistoryListNoDateTime;
  }

  /**
   * This method is used to list study and global notification
   *
   * @author BTC
   * @param studyId
   * @param type , global/study notification
   * @return list of {@link NotificationBO}
   */
  @Override
  public List<NotificationBO> getNotificationList(int studyId, String type) {
    logger.info("NotificationServiceImpl - getNotificationList() - Starts");
    List<NotificationBO> notificationList = null;
    try {
      notificationList = notificationDAO.getNotificationList(studyId, type);
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - getNotificationList() - ERROR ", e);
    }
    logger.info("NotificationServiceImpl - getNotificationList() - Ends , e");
    return notificationList;
  }

  @Override
  public List<NotificationLangBO> getNotificationLangList(
      int studyId, String langCode, List<NotificationBO> notificationBOList, int userId) {
    logger.info("NotificationServiceImpl - getNotificationLangList() - Starts");
    List<NotificationLangBO> notificationList = null;
    try {
      if (notificationBOList != null && notificationBOList.size() > 0) {
        for (NotificationBO notificationBO : notificationBOList) {
          NotificationLangBO notificationLangBO =
              notificationDAO.getNotificationLang(notificationBO.getNotificationId(), langCode);
          if (notificationLangBO == null) {
            notificationLangBO = new NotificationLangBO();
            notificationLangBO.setNotificationLangPK(
                new NotificationLangPK(notificationBO.getNotificationId(), langCode));
            notificationLangBO.setStudyId(notificationBO.getStudyId());
            notificationLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            notificationLangBO.setCreatedBy(userId);
            notificationLangBO.setNotificationStatus(true);
            notificationLangBO.setNotificationSent(false);
            studyDAO.saveOrUpdateObject(notificationLangBO);

            StudySequenceLangBO studySequenceLangBO =
                studyDAO.getStudySequenceLangBO(notificationBO.getStudyId(), langCode);
            if (studySequenceLangBO != null) {
              studySequenceLangBO.setMiscellaneousNotification(false);
              studyDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
        }
        notificationList = notificationDAO.getNotificationLangList(studyId, langCode);
      }
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - getNotificationLangList() - ERROR ", e);
    }
    logger.info("NotificationServiceImpl - getNotificationLangList() - Ends , e");
    return notificationList;
  }

  /**
   * This method is used to save/update/resend of study/global notification
   *
   * @author BTC
   * @param notificationBO , {@link NotificationBO}
   * @param notificationType , to define global/study notification
   * @param buttonType , action like add/edit/resend/done to update in audit log for both
   *     global/study notification
   * @param sessionObject , object of {@link SessionObject}
   * @param customStudyId , unique Id of study for audit log reference
   * @return Object of {@link Integer}
   */
  @Override
  public Integer saveOrUpdateOrResendNotification(
      NotificationBO notificationBO,
      String notificationType,
      String buttonType,
      SessionObject sessionObject,
      String customStudyId,
      String language) {
    logger.info("NotificationServiceImpl - saveOrUpdateNotification - Starts");
    int notificationId = 0;
    try {
      if (notificationBO != null) {
        if (FdahpStudyDesignerUtil.isNotEmpty(language)
            && !MultiLanguageCodes.ENGLISH.getKey().equals(language)) {
          NotificationLangBO notificationLangBO =
              notificationDAO.getNotificationLang(notificationBO.getNotificationId(), language);
          if (notificationLangBO == null) {
            notificationLangBO = new NotificationLangBO();
            notificationLangBO.setNotificationLangPK(
                new NotificationLangPK(notificationBO.getNotificationId(), language));
            notificationLangBO.setStudyId(notificationBO.getStudyId());
            notificationLangBO.setCreatedBy(notificationBO.getCreatedBy());
            notificationLangBO.setCreatedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
            notificationLangBO.setNotificationStatus(true);
            notificationLangBO.setNotificationSent(false);
          } else {
            notificationLangBO.setModifiedBy(notificationBO.getCreatedBy());
            notificationLangBO.setModifiedOn(FdahpStudyDesignerUtil.getCurrentDateTime());
          }
          notificationLangBO.setNotificationText(notificationBO.getNotificationText());
          notificationLangBO.setNotificationAction(notificationBO.isNotificationAction());
          studyDAO.saveOrUpdateObject(notificationLangBO);

          StudySequenceLangBO studySequenceLangBO =
              studyService.getStudySequenceById(notificationBO.getStudyId(), language);
          if (studySequenceLangBO != null) {
            if (FdahpStudyDesignerConstants.SAVE_BUTTON.equals(buttonType)) {
              studySequenceLangBO.setMiscellaneousNotification(false);
              studyDAO.saveOrUpdateObject(studySequenceLangBO);
            }
          }
          if (notificationType.equals(FdahpStudyDesignerConstants.STUDYLEVEL)) {
            studyDAO.updateDraftStatusInStudyBo(
                sessionObject.getUserId(), notificationBO.getStudyId());
          }
          notificationId = notificationBO.getNotificationId();
        } else {
          notificationId =
              notificationDAO.saveOrUpdateOrResendNotification(
                  notificationBO, notificationType, buttonType, sessionObject);
          if (notificationType.equals(FdahpStudyDesignerConstants.STUDYLEVEL)
              && notificationId != 0) {
            studyDAO.markAsCompleted(
                notificationBO.getStudyId(),
                FdahpStudyDesignerConstants.NOTIFICATION,
                false,
                sessionObject,
                customStudyId);
          }
        }
      }
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - saveOrUpdateNotification - ERROR", e);
    }
    logger.info("NotificationServiceImpl - saveOrUpdateNotification - Ends");
    return notificationId;
  }

  public void setStudyDAO(StudyDAO studyDAO) {
    this.studyDAO = studyDAO;
  }

  @Override
  public List<String> getGatwayAppList() {
    logger.info("NotificationServiceImpl - saveOrUpdateNotification - Starts");
    List<String> gatewayAppList = new ArrayList<>(new HashSet(notificationDAO.getGatwayAppList()));
    logger.info("NotificationServiceImpl - saveOrUpdateNotification - Ends");
    return gatewayAppList;
  }

  @Override
  public NotificationLangBO getNotificationLang(int notificationId, String lang) {
    logger.info("NotificationServiceImpl - getNotificationLang - Starts");
    NotificationLangBO notificationBO = null;
    try {
      notificationBO = notificationDAO.getNotificationLang(notificationId, lang);
    } catch (Exception e) {
      logger.error("NotificationServiceImpl - getNotificationLang - ERROR", e);
    }
    logger.info("NotificationServiceImpl - getNotificationLang - Ends");
    return notificationBO;
  }
}
