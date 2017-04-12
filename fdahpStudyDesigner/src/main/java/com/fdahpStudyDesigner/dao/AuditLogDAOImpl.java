package com.fdahpStudyDesigner.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.fdahpStudyDesigner.bo.AuditLogBO;
import com.fdahpStudyDesigner.util.SessionObject;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerConstants;
import com.fdahpStudyDesigner.util.fdahpStudyDesignerUtil;

@Repository
public class AuditLogDAOImpl implements AuditLogDAO{
	
	
	private static Logger logger = Logger.getLogger(AuditLogDAOImpl.class);
	HibernateTemplate hibernateTemplate;
	private Query query = null;
	private Transaction transaction = null;
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	
	

	@Override
	public String saveToAuditLog(Session session, SessionObject sessionObject, String activity, String activityDetails, String classsMethodName){
		logger.info("AuditLogDAOImpl - saveToAuditLog() - Starts");
		String message = fdahpStudyDesignerConstants.FAILURE;
		AuditLogBO auditLog = null;
		Session newSession = null;
		try{
				if(session == null) {
					newSession = hibernateTemplate.getSessionFactory().openSession();
					transaction = newSession.beginTransaction();
				} else {
					transaction = session.beginTransaction();
				}
				if(sessionObject != null && fdahpStudyDesignerUtil.isNotEmpty(activity) && fdahpStudyDesignerUtil.isNotEmpty(activityDetails)){
					auditLog = new AuditLogBO();
					auditLog.setActivity(activity);
					auditLog.setActivityDetails(activityDetails);
					auditLog.setUserId(sessionObject.getUserId());
					auditLog.setClassMethodName(classsMethodName);
					auditLog.setCreatedDateTime(fdahpStudyDesignerUtil.getCurrentDateTime());
					if(newSession != null){
						newSession.save(auditLog);
					}
					else{
						session.save(auditLog);
					}
					transaction.commit();
					message = fdahpStudyDesignerConstants.SUCCESS;
				}
		} catch(Exception e){
			transaction.rollback();
			logger.error("AuditLogDAOImpl - saveToAuditLog - ERROR", e);
		}finally{
			if(null != newSession){
				newSession.close();
			}
		}
		logger.info("AuditLogDAOImpl - saveToAuditLog - Ends");
		return message;
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<AuditLogBO> getTodaysAuditLogs() {
		logger.info("AuditLogDAOImpl - getTodaysAuditLogs() - Starts");
		List<AuditLogBO> auditLogs = null;
		Session session = null;
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			String date = fdahpStudyDesignerConstants.DB_SDF_DATE.format(fdahpStudyDesignerUtil.addDaysToDate(new Date(), -1));
			auditLogs = session.createQuery(
		             "select ALBO.auditLogId AS auditLogId, ALBO.userId AS userId, ALBO.activity AS activity, "
		             + "ALBO.activityDetails AS activityDetails, ALBO.createdDateTime AS createdDateTime, ALBO.classMethodName AS classMethodName, "
		             + "(select UBO from UserBO UBO where UBO.userId = ALBO.userId) As userBO " 
		             + "from AuditLogBO ALBO "
		             + "where ALBO.createdDateTime BETWEEN :stDate AND :edDate")
		         .setString("stDate", date+" 00:00:00")
		         .setString("edDate", date+" 23:59:59")
		         .setResultTransformer(Transformers.aliasToBean(AuditLogBO.class))
		         .list();
		} catch(Exception e){
			logger.error("AuditLogDAOImpl - getTodaysAuditLogs() - ERROR", e);
		}finally{
			if(session != null && !session.isOpen()){
				session.close();
			}
		}
		logger.info("AuditLogDAOImpl - getTodaysAuditLogs() - Ends");
		return auditLogs;
	}

}
