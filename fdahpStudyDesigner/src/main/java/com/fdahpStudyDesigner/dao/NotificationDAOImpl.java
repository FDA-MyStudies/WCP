package com.fdahpStudyDesigner.dao;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.fdahpStudyDesigner.bo.NotificationBO;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerConstants;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerUtil;

@Repository
public class NotificationDAOImpl implements NotificationDAO{
	
	private static Logger logger = Logger.getLogger(NotificationDAOImpl.class);
	HibernateTemplate hibernateTemplate;
	private Query query = null;
	private Transaction transaction = null;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NotificationBO> getNotificationList(Integer studyId, String type) throws Exception {
		logger.info("NotificationDAOImpl - getNotificationList() - Starts");
		List<NotificationBO> notificationList = null;
		//List<Object[]> objList = null;
		Session session = null; 
		String queryString = null;
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			if(type.equals("studyNotification") && !"".equals(studyId)){
				queryString = "from NotificationBO NBO where NBO.studyId = "+studyId+" and NBO.notificationType = 'ST' ";
				query = session.createQuery(queryString);
				notificationList = query.list();
			}else {
				queryString = "from NotificationBO";
				query = session.createQuery(queryString);
				notificationList = query.list();
			}
			/*if(null != objList && objList.size() > 0 ){
				notificationList = new ArrayList<NotificationBO>();
				for(Object[] obj:objList){
					NotificationBO notificationBO = new NotificationBO();
					notificationBO.setNotificationId(null != obj[0] ? (Integer)obj[0] : 0);
					notificationBO.setStudyId(null != obj[1] ? (Integer)(obj[1]) : 0);
					notificationBO.setNotificationText(null != obj[2] ? String.valueOf(obj[2]) : "");
					notificationBO.setScheduleDate(null != obj[3] ? String.valueOf(obj[3]) : "");
					notificationBO.setScheduleTime(null != obj[4] ? String.valueOf(obj[4]) : "");
					notificationList.add(notificationBO);
				}
			}*/
		}catch(Exception e){
			logger.error("NotificationDAOImpl - getNotificationList() - ERROR" , e);
		}finally{
			session.close();
		}
		logger.info("NotificationDAOImpl - getNotificationList() - Ends");
		return notificationList;
	}

	@Override
	public NotificationBO getNotification(Integer notificationId) throws Exception {
			logger.info("NotificationDAOImpl - getNotification() - Starts");
			Session session = null;
			Query query = null;
			NotificationBO notificationBO = null;
			try{
				session = hibernateTemplate.getSessionFactory().openSession();
				query = session.createQuery("from NotificationBO NBO where NBO.notificationId = " +notificationId);
				notificationBO = (NotificationBO) query.uniqueResult();
				if(null != notificationBO){
					notificationBO.setNotificationId(null != notificationBO.getNotificationId() ? notificationBO.getNotificationId() : 0);
					notificationBO.setNotificationText(null != notificationBO.getNotificationText() ? notificationBO.getNotificationText() : "");
					notificationBO.setScheduleDate(null != notificationBO.getScheduleDate() ? notificationBO.getScheduleDate() : "");
					notificationBO.setScheduleTime(null != notificationBO.getScheduleTime() ? notificationBO.getScheduleTime() : "");
					notificationBO.setNotificationSent(notificationBO.isNotificationSent());
					notificationBO.setNotificationScheduleType(null != notificationBO.getNotificationScheduleType() ? notificationBO.getNotificationScheduleType() : "");
					if(notificationBO.getNotificationScheduleType().equalsIgnoreCase("nowDateTime")){
						notificationBO.setScheduleDate("");
						notificationBO.setScheduleTime("");
					}
				}
			}catch(Exception e){
				logger.error("NotificationDAOImpl - getNotification - ERROR",e);
			}finally{
				if(null != session){
					session.close();
				}
			}
			logger.info("NotificationDAOImpl - getNotification - Ends");
			return notificationBO;
		}

	@Override
	public Integer saveOrUpdateNotification(NotificationBO notificationBO, String notificationType) {
		logger.info("NotificationDAOImpl - saveOrUpdateNotification() - Starts");
		Session session = null;
		NotificationBO notificationBOUpdate = null;
		Integer notificationId = 0;
		try{
				session = hibernateTemplate.getSessionFactory().openSession();
				transaction = session.beginTransaction();
				if(notificationBO.getNotificationId() == null) {
					notificationBOUpdate = new NotificationBO();
					notificationBOUpdate.setNotificationText(notificationBO.getNotificationText().trim());
					if(notificationBO.getScheduleTime().equals("") || notificationBO.getScheduleDate().equals("")){
						notificationBOUpdate.setScheduleTime(null);
						notificationBOUpdate.setScheduleDate(null);
						notificationBOUpdate.setNotificationScheduleType(notificationBO.getNotificationScheduleType());
					}else {
						notificationBOUpdate.setScheduleTime(notificationBO.getScheduleTime());
						notificationBOUpdate.setScheduleDate(notificationBO.getScheduleDate());
						notificationBOUpdate.setNotificationScheduleType(notificationBO.getNotificationScheduleType());
					}
					if(notificationType.equals("studyNotification")){
						notificationBOUpdate.setNotificationDone(notificationBO.isNotificationDone());
						notificationBOUpdate.setNotificationType("ST");
						notificationBOUpdate.setStudyId(notificationBO.getStudyId());
						notificationBOUpdate.setNotificationAction(notificationBO.isNotificationAction());
					}else{
						notificationBOUpdate.setNotificationType("GT");
						notificationBOUpdate.setStudyId(0);
						notificationBOUpdate.setNotificationAction(false);
						notificationBOUpdate.setNotificationDone(true);
					}
					notificationId = (Integer) session.save(notificationBOUpdate);
				} else {
					query = session.createQuery(" from NotificationBO NBO where NBO.notificationId = "+notificationBO.getNotificationId());
					notificationBOUpdate = (NotificationBO) query.uniqueResult();
					if (StringUtils.isNotBlank(notificationBO.getNotificationText())) {
						notificationBOUpdate.setNotificationText(notificationBO.getNotificationText().trim());
					}else {
						notificationBOUpdate.setNotificationText(notificationBOUpdate.getNotificationText().trim());
					}
					notificationBOUpdate.setStudyId(notificationBOUpdate.getStudyId());
					notificationBOUpdate.setNotificationSent(notificationBO.isNotificationSent());
					notificationBOUpdate.setNotificationScheduleType(notificationBO.getNotificationScheduleType());
					if(fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleTime()) && fdahpStudyDesignerUtil.isNotEmpty(notificationBO.getScheduleDate())){
						notificationBOUpdate.setScheduleTime(notificationBO.getScheduleTime());
						notificationBOUpdate.setScheduleDate(notificationBO.getScheduleDate());
					}else{
						notificationBOUpdate.setScheduleTime(null);
						notificationBOUpdate.setScheduleDate(null);
					}
					if(notificationType.equals("studyNotification")){
						notificationBOUpdate.setNotificationDone(notificationBO.isNotificationDone());
						notificationBOUpdate.setNotificationType("ST");
						notificationBOUpdate.setNotificationAction(notificationBO.isNotificationAction());
					}else{
						notificationBOUpdate.setNotificationDone(notificationBOUpdate.isNotificationDone());
						notificationBOUpdate.setNotificationType("GT");
						notificationBOUpdate.setNotificationAction(notificationBOUpdate.isNotificationAction());
					}
					session.saveOrUpdate(notificationBOUpdate);
					notificationId = notificationBOUpdate.getNotificationId(); 
				}
				transaction.commit();
		} catch(Exception e){
			transaction.rollback();
			logger.error("NotificationDAOImpl - saveOrUpdateNotification - ERROR", e);
		}finally{
			if(null != session){
				session.close();
			}
		}
		logger.info("NotificationDAOImpl - saveOrUpdateNotification - Ends");
		return notificationId;
	}
	
	@Override
	public String deleteNotification(Integer notificationIdForDelete) {
		logger.info("NotificationDAOImpl - deleteNotification() - Starts");
		Session session = null;
	    String message = fdahpStudyDesignerConstants.FAILURE;
	    int i = 0;
		try{
				session = hibernateTemplate.getSessionFactory().openSession();
				transaction = session.beginTransaction();
				if(notificationIdForDelete != null){
					query = session.createQuery("delete from NotificationBO NBO where NBO.notificationId = " +notificationIdForDelete);
					i = query.executeUpdate();
					if(i > 0){
						message = fdahpStudyDesignerConstants.SUCCESS;
					}
				}
				transaction.commit();
				message = fdahpStudyDesignerConstants.SUCCESS;
		} catch(Exception e){
			transaction.rollback();
			logger.error("NotificationDAOImpl - deleteNotification - ERROR", e);
		}finally{
			if(null != session){
				session.close();
			}
		}
		logger.info("NotificationDAOImpl - deleteNotification - Ends");
		return message;
	}
	
//	public String resendNotification(Integer notificationId){
//		logger.info("NotificationDAOImpl - resendNotification() - Starts");
//		Session session = null;
//	    String message = fdahpStudyDesignerConstants.FAILURE;
//	    NotificationBO notificationBO = null;
//	    NotificationBO notification = null;
//		try{
//				session = hibernateTemplate.getSessionFactory().openSession();
//				transaction = session.beginTransaction();
//				if(notificationId != null){
//					query = session.createQuery(" from NotificationBO NBO where NBO.notificationId = "+notificationId);
//					notificationBO = (NotificationBO) query.uniqueResult();
//					if(notificationBO != null){
//						notification = new NotificationBO();
//						notification.setNotificationText(notificationBO.getNotificationText());
//						notification.setScheduleDate(fdahpStudyDesignerUtil.getCurrentDate());
//						notification.setScheduleTime(fdahpStudyDesignerUtil.getCurrentTime());
//						notification.setNotificationSent(false);
//						notification.setNotificationType("GT");
//						session.saveOrUpdate(notification);
//					}
//				}
//				transaction.commit();
//				message = fdahpStudyDesignerConstants.SUCCESS;
//		} catch(Exception e){
//			transaction.rollback();
//			logger.error("NotificationDAOImpl - resendNotification - ERROR", e);
//		}finally{
//			if(null != session){
//				session.close();
//			}
//		}
//		logger.info("NotificationDAOImpl - resendNotification - Ends");
//		return message;
//	}
}
