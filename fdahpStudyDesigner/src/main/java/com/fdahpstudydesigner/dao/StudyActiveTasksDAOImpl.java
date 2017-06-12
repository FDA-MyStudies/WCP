/**
 * 
 */
package com.fdahpstudydesigner.dao;

import java.math.BigInteger;
import java.util.ArrayList;
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

import com.fdahpstudydesigner.bo.ActiveTaskAtrributeValuesBo;
import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.ActiveTaskCustomScheduleBo;
import com.fdahpstudydesigner.bo.ActiveTaskFrequencyBo;
import com.fdahpstudydesigner.bo.ActiveTaskListBo;
import com.fdahpstudydesigner.bo.ActiveTaskMasterAttributeBo;
import com.fdahpstudydesigner.bo.ActivetaskFormulaBo;
import com.fdahpstudydesigner.bo.QuestionnaireBo;
import com.fdahpstudydesigner.bo.QuestionsBo;
import com.fdahpstudydesigner.bo.StatisticImageListBo;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudySequenceBo;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;

/**
 * @author Vivek
 *
 */
@Repository
public class StudyActiveTasksDAOImpl implements StudyActiveTasksDAO{

	private static Logger logger = Logger.getLogger(StudyActiveTasksDAOImpl.class.getName());
	HibernateTemplate hibernateTemplate;
	private Query query = null;
	private Transaction transaction = null;
	String queryString = "";
	@Autowired
	private AuditLogDAO auditLogDAO;
	
	public StudyActiveTasksDAOImpl() {
		// Do nothing
	}
	
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}
	
	/**
	 * return  active tasks based on user's Study Id
	 * @author Vivek
	 * 
	 * @param studyId , studyId of the {@link StudyBo}
	 * @return List of {@link ActiveTaskBo}
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveTaskBo> getStudyActiveTasksByStudyId(String studyId) {
		logger.info("StudyActiveTasksDAOImpl - getStudyActiveTasksByStudyId() - Starts");
		Session session = null;
		List<ActiveTaskBo> activeTasks = null;
		List<ActiveTaskListBo> activeTaskListBos = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			if (StringUtils.isNotEmpty(studyId)) {
				query = session.getNamedQuery("ActiveTaskBo.getActiveTasksByByStudyId").setInteger("studyId", Integer.parseInt(studyId));
				activeTasks = query.list();
				
				query = session.createQuery("from ActiveTaskListBo");
				activeTaskListBos = query.list();
				
				if(activeTasks!=null && !activeTasks.isEmpty() && activeTaskListBos!=null && !activeTaskListBos.isEmpty()){
					for(ActiveTaskBo activeTaskBo:activeTasks){
						if(activeTaskBo.getTaskTypeId()!=null){
							for(ActiveTaskListBo activeTaskListBo:activeTaskListBos){
								if(activeTaskListBo.getActiveTaskListId().intValue() == activeTaskBo.getTaskTypeId().intValue()){
									activeTaskBo.setType(activeTaskListBo.getTaskName());
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"StudyActiveTasksDAOImpl - getStudyActiveTasksByStudyId() - ERROR ", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getStudyActiveTasksByStudyId() - Ends");
		return activeTasks;
	}

	/**
	 * @author Ronalin
	 * @param Integer :aciveTaskId
	 * @return Object :ActiveTaskBo
	 * 
	 * This method is used to get the ativeTask info object based on consent info id 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActiveTaskBo getActiveTaskById(Integer activeTaskId, String customStudyId) {
		logger.info("StudyActiveTasksDAOImpl - getActiveTaskById() - Starts");
		ActiveTaskBo activeTaskBo = null;
		Session session = null;
		List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos = null;
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			activeTaskBo = (ActiveTaskBo)session.get(ActiveTaskBo.class, activeTaskId);
			if(activeTaskBo!=null){
				query = session.createQuery("from ActiveTaskAtrributeValuesBo where activeTaskId="+activeTaskBo.getId());
				activeTaskAtrributeValuesBos = query.list();
				if(StringUtils.isNotEmpty(customStudyId)){
				//Duplicate ShortTitle per activeTask
				BigInteger shortTitleCount = (BigInteger)session.createSQLQuery("select count(*) from active_task a "
						+ "where a.short_title='"+activeTaskBo.getShortTitle()+"' and custom_study_id='"+customStudyId+"' and a.active=1 and a.is_live=1").uniqueResult();
				if(shortTitleCount!=null && shortTitleCount.intValue() > 0)
					activeTaskBo.setIsDuplicate(shortTitleCount.intValue());
				else
					activeTaskBo.setIsDuplicate(0);
				}else{
					activeTaskBo.setIsDuplicate(0);
				}
				
				if(activeTaskAtrributeValuesBos!=null && !activeTaskAtrributeValuesBos.isEmpty()){
					for(ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo: activeTaskAtrributeValuesBos){
						if(StringUtils.isNotEmpty(customStudyId)){
							BigInteger statTitleCount = (BigInteger)session.createSQLQuery("select count(*) from active_task_attrtibutes_values at "
									+ "where at.identifier_name_stat='"+activeTaskAtrributeValuesBo.getIdentifierNameStat()+"'"
									+ "and  at.active_task_id in "
						            +"(select a.id from active_task a where a.custom_study_id='"+customStudyId+"' and a.active=1 and a.is_live=1)").uniqueResult();
							if(statTitleCount!=null && statTitleCount.intValue() > 0)
								activeTaskAtrributeValuesBo.setIsIdentifierNameStatDuplicate(statTitleCount.intValue());
							else
								activeTaskAtrributeValuesBo.setIsIdentifierNameStatDuplicate(0);
						}else{
							activeTaskAtrributeValuesBo.setIsIdentifierNameStatDuplicate(0);
						}
					}
					activeTaskBo.setTaskAttributeValueBos(activeTaskAtrributeValuesBos);
				}
				
				
				String searchQuery="";
				if(null!=activeTaskBo.getFrequency()) {
					if(activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_MANUALLY_SCHEDULE)){
						searchQuery = "From ActiveTaskCustomScheduleBo ATSBO where ATSBO.activeTaskId="+activeTaskBo.getId();
						query = session.createQuery(searchQuery);
						List<ActiveTaskCustomScheduleBo> activeTaskCustomScheduleBos = query.list();
						activeTaskBo.setActiveTaskCustomScheduleBo(activeTaskCustomScheduleBos);
					}else {
						searchQuery = "From ActiveTaskFrequencyBo ATBO where ATBO.activeTaskId="+activeTaskBo.getId();
						query = session.createQuery(searchQuery);
						if(activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_DAILY)){
							List<ActiveTaskFrequencyBo> activeTaskFrequencyBos = query.list();	
							activeTaskBo.setActiveTaskFrequenciesList(activeTaskFrequencyBos);
						}else{
							ActiveTaskFrequencyBo activeTaskFrequencyBo = (ActiveTaskFrequencyBo) query.uniqueResult();
							activeTaskBo.setActiveTaskFrequenciesBo(activeTaskFrequencyBo);
						}
						
					}
				}
				if(activeTaskBo.getVersion()!=null){
					activeTaskBo.setActiveTaskVersion(" (V"+activeTaskBo.getVersion()+")");
				}
			}
		}catch(Exception e){
			logger.error("StudyActiveTasksDAOImpl - getActiveTaskById() - Error",e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getActiveTaskById() - Ends");
		return activeTaskBo;
	}

	/**
	 * @author Ronalin
	 * Add/Update the ActiveTaskBo
	 * @param StudyBo , {@link ActiveTaskBo}
	 * @return {@link String}
	 */
	@Override
	public ActiveTaskBo saveOrUpdateActiveTaskInfo(ActiveTaskBo activeTaskBo, SessionObject sesObj ,String customStudyId) {
		logger.info("StudyActiveTasksDAOImpl - saveOrUpdateActiveTaskInfo() - Starts");
		Session session = null;
		StudySequenceBo studySequence = null;
		List<ActiveTaskAtrributeValuesBo> taskAttributeValueBos = new ArrayList<>();
		String activitydetails = "";
		String activity = "";
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			if(activeTaskBo.getTaskAttributeValueBos()!=null && !activeTaskBo.getTaskAttributeValueBos().isEmpty())
				taskAttributeValueBos = activeTaskBo.getTaskAttributeValueBos();
			session.saveOrUpdate(activeTaskBo);
			if(taskAttributeValueBos!=null && !taskAttributeValueBos.isEmpty()){
				for(ActiveTaskAtrributeValuesBo activeTaskAtrributeValuesBo:taskAttributeValueBos){
					   if(activeTaskAtrributeValuesBo.isAddToDashboard()){
						   if(!activeTaskAtrributeValuesBo.isAddToLineChart()){
							   activeTaskAtrributeValuesBo.setTimeRangeChart(null);
							   activeTaskAtrributeValuesBo.setRollbackChat(null);
							   activeTaskAtrributeValuesBo.setTitleChat(null);
						   }
						   if(!activeTaskAtrributeValuesBo.isUseForStatistic()){
							   activeTaskAtrributeValuesBo.setIdentifierNameStat(null);
							   activeTaskAtrributeValuesBo.setDisplayNameStat(null);
							   activeTaskAtrributeValuesBo.setDisplayUnitStat(null);
							   activeTaskAtrributeValuesBo.setUploadTypeStat(null);
							   activeTaskAtrributeValuesBo.setFormulaAppliedStat(null);
							   activeTaskAtrributeValuesBo.setTimeRangeStat(null);
						   }
						   activeTaskAtrributeValuesBo.setActiveTaskId(activeTaskBo.getId());
						   activeTaskAtrributeValuesBo.setActive(1);
						   if(activeTaskAtrributeValuesBo.getAttributeValueId()==null){
							   session.save(activeTaskAtrributeValuesBo);
					        }else{
							   session.update(activeTaskAtrributeValuesBo); 
						   }
					   }
				   }
			}
			
			if(StringUtils.isNotEmpty(activeTaskBo.getButtonText())){
				studySequence = (StudySequenceBo) session.getNamedQuery("getStudySequenceByStudyId").setInteger("studyId", activeTaskBo.getStudyId()).uniqueResult();
				if(studySequence != null){
					studySequence.setStudyExcActiveTask(false);
				}
				session.saveOrUpdate(studySequence);
			}
			
			if(activeTaskBo.getButtonText().equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)){
				activity = "ActiveTask saved";
				activitydetails = customStudyId+" -- ActiveTask saved but not eligible for mark as completed action untill unless it is DONE";
			}else{
				activity = "ActiveTask done";
				activitydetails = customStudyId+" -- ActiveTask done and eligible for mark as completed action";
				auditLogDAO.updateDraftToEditedStatus(session, transaction, sesObj.getUserId(), FdahpStudyDesignerConstants.DRAFT_ACTIVETASK, activeTaskBo.getStudyId());
			}
			auditLogDAO.saveToAuditLog(session, transaction, sesObj, activity, activitydetails, "StudyActiveTasksDAOImpl - saveOrUpdateActiveTaskInfo");
			
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			logger.error("StudyActiveTasksDAOImpl - saveOrUpdateActiveTaskInfo() - Error",e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - saveOrUpdateActiveTaskInfo() - Ends");
		return activeTaskBo;
	}

	/**
	 * @author Ronalin
	 * @param Integer : activeTaskInfoId
	 * @return String :SUCCESS or FAILURE
	 *  This method used to get the delete the activeTask information
	 */
	@Override
	public String deleteActiveTask(ActiveTaskBo activeTaskBo, SessionObject sesObj,String customStudyId) {
		logger.info("StudyActiveTasksDAOImpl - deleteActiveTAsk() - Starts");
		String message = FdahpStudyDesignerConstants.FAILURE;
		Session session = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			if(activeTaskBo != null) {
				transaction =session.beginTransaction();
				String deleteQuery = "update ActiveTaskAtrributeValuesBo set active=0 where activeTaskId="+activeTaskBo.getId();
				query = session.createQuery(deleteQuery);
				query.executeUpdate();
				if(activeTaskBo.getActiveTaskFrequenciesList() != null && !activeTaskBo.getActiveTaskFrequenciesList().isEmpty()){
					deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
					query = session.createSQLQuery(deleteQuery);
					query.executeUpdate();
					String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
					query = session.createSQLQuery(deleteQuery2);
					query.executeUpdate();
				}
					ActiveTaskFrequencyBo activeTaskFrequencyBo = activeTaskBo.getActiveTaskFrequenciesBo();
					if(activeTaskFrequencyBo != null && (activeTaskFrequencyBo.getFrequencyDate() != null 
							|| activeTaskFrequencyBo.getFrequencyTime() != null 
							|| (null != activeTaskBo.getFrequency() 
								&& activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME))) 
							&& !activeTaskBo.getFrequency().equalsIgnoreCase(activeTaskBo.getPreviousFrequency())){
						deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery);
						query.executeUpdate();
						String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery2);
						query.executeUpdate();
					}
				if(activeTaskBo.getActiveTaskCustomScheduleBo() != null && activeTaskBo.getActiveTaskCustomScheduleBo().size() > 0){
					deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
					query = session.createSQLQuery(deleteQuery);
					query.executeUpdate();
					String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
					query = session.createSQLQuery(deleteQuery2);
					query.executeUpdate();
				}
				
				query = session.createQuery(" UPDATE StudySequenceBo SET studyExcActiveTask =false WHERE studyId = "+activeTaskBo.getStudyId() );
				query.executeUpdate();
				
				deleteQuery = "update ActiveTaskBo set active=0 where id="+activeTaskBo.getId();
				query = session.createQuery(deleteQuery);
				query.executeUpdate();
				
				message = FdahpStudyDesignerConstants.SUCCESS;
				
				auditLogDAO.saveToAuditLog(session, transaction, sesObj, customStudyId+" -- ActiveTask deleted", customStudyId+" -- ActiveTask deleted for the respective study", "StudyActiveTasksDAOImpl - deleteActiveTAsk");
				
				transaction.commit();
			}
		} catch (Exception e) {
			transaction.rollback();
			logger.error("StudyActiveTasksDAOImpl - deleteActiveTAsk() - ERROR " , e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - deleteActiveTAsk() - Ends");
		return message;
	}

	@Override
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo, String customStudyId) {
		logger.info("StudyActiveTasksDAOImpl - saveOrUpdateActiveTask() - Starts");
		Session session = null;
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(activeTaskBo);
			if(activeTaskBo.getType().equalsIgnoreCase(FdahpStudyDesignerConstants.SCHEDULE) && activeTaskBo != null &&  activeTaskBo.getId() != null){
					if(activeTaskBo.getActiveTaskFrequenciesList() != null && !activeTaskBo.getActiveTaskFrequenciesList().isEmpty()){
						String deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery);
						query.executeUpdate();
						String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery2);
						query.executeUpdate();
						for(ActiveTaskFrequencyBo activeTaskFrequencyBo : activeTaskBo.getActiveTaskFrequenciesList()){
							if(activeTaskFrequencyBo.getFrequencyTime() != null){
								activeTaskFrequencyBo.setFrequencyTime(FdahpStudyDesignerUtil.getFormattedDate(activeTaskFrequencyBo.getFrequencyTime(), FdahpStudyDesignerConstants.SDF_TIME, FdahpStudyDesignerConstants.UI_SDF_TIME));
								if(activeTaskFrequencyBo.getActiveTaskId() == null){
									activeTaskFrequencyBo.setId(null);
									activeTaskFrequencyBo.setActiveTaskId(activeTaskBo.getId());
								}
								session.saveOrUpdate(activeTaskFrequencyBo);
							}
						} 
					}
					if(activeTaskBo.getActiveTaskFrequenciesList() != null){
						ActiveTaskFrequencyBo activeTaskFrequencyBo = activeTaskBo.getActiveTaskFrequenciesBo();
						if(activeTaskFrequencyBo.getFrequencyDate() != null || activeTaskFrequencyBo.getFrequencyTime() != null || activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)){
							if(!activeTaskBo.getFrequency().equalsIgnoreCase(activeTaskBo.getPreviousFrequency())){
								String deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
								query = session.createSQLQuery(deleteQuery);
								query.executeUpdate();
								String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
								query = session.createSQLQuery(deleteQuery2);
								query.executeUpdate();
							}
							if(activeTaskFrequencyBo.getActiveTaskId() == null){
								activeTaskFrequencyBo.setActiveTaskId(activeTaskBo.getId());
							}
							if(activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyDate() != null && !activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyDate().isEmpty()){
								activeTaskFrequencyBo.setFrequencyDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyDate(), FdahpStudyDesignerConstants.UI_SDF_DATE, FdahpStudyDesignerConstants.SD_DATE_FORMAT));
							}
							if(activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyTime() != null && !activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyTime().isEmpty()){
								activeTaskBo.getActiveTaskFrequenciesBo().setFrequencyTime(FdahpStudyDesignerUtil.getFormattedDate(activeTaskBo.getActiveTaskFrequenciesBo().getFrequencyTime(), FdahpStudyDesignerConstants.SDF_TIME, FdahpStudyDesignerConstants.UI_SDF_TIME));
							}
							session.saveOrUpdate(activeTaskFrequencyBo);
						}
					}
					if(activeTaskBo.getActiveTaskCustomScheduleBo() != null && activeTaskBo.getActiveTaskCustomScheduleBo().size() > 0){
						String deleteQuery = "delete from active_task_custom_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery);
						query.executeUpdate();
						String deleteQuery2 = "delete from active_task_frequencies where active_task_id="+activeTaskBo.getId();
						query = session.createSQLQuery(deleteQuery2);
						query.executeUpdate();
						for(ActiveTaskCustomScheduleBo activeTaskCustomScheduleBo  : activeTaskBo.getActiveTaskCustomScheduleBo()){
							if(activeTaskCustomScheduleBo.getFrequencyStartDate() != null && !activeTaskCustomScheduleBo.getFrequencyStartDate().isEmpty() && activeTaskCustomScheduleBo.getFrequencyEndDate() != null 
									&& !activeTaskCustomScheduleBo.getFrequencyEndDate().isEmpty() && activeTaskCustomScheduleBo.getFrequencyTime() != null){
								if(activeTaskCustomScheduleBo.getActiveTaskId() == null){
									activeTaskCustomScheduleBo.setActiveTaskId(activeTaskBo.getId());
								}
								activeTaskCustomScheduleBo.setFrequencyStartDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskCustomScheduleBo.getFrequencyStartDate(), FdahpStudyDesignerConstants.UI_SDF_DATE, FdahpStudyDesignerConstants.SD_DATE_FORMAT));
								activeTaskCustomScheduleBo.setFrequencyEndDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskCustomScheduleBo.getFrequencyEndDate(), FdahpStudyDesignerConstants.UI_SDF_DATE, FdahpStudyDesignerConstants.SD_DATE_FORMAT));
								if(activeTaskCustomScheduleBo.getFrequencyTime() != null && !activeTaskCustomScheduleBo.getFrequencyTime().isEmpty()){
									activeTaskCustomScheduleBo.setFrequencyTime(FdahpStudyDesignerUtil.getFormattedDate(activeTaskCustomScheduleBo.getFrequencyTime(), FdahpStudyDesignerConstants.SDF_TIME, FdahpStudyDesignerConstants.UI_SDF_TIME));
								}
								session.saveOrUpdate(activeTaskCustomScheduleBo);
							}
						}
					}
			}
			transaction.commit();
		}catch(Exception e){
			transaction.rollback();
			logger.info("StudyActiveTasksDAOImpl - saveOrUpdateActiveTask() - Error", e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - saveOrUpdateActiveTask() - Ends");
		return activeTaskBo;
	}
	/**
	 * @author Ronalin
	 * @return List :ActiveTaskListBos
	 *  This method used to get all type of activeTask
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveTaskListBo> getAllActiveTaskTypes() {
		logger.info("StudyActiveTasksDAOImpl - getAllActiveTaskTypes() - Starts");
		Session session = null;
		List<ActiveTaskListBo> activeTaskListBos = new ArrayList<>();
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			query = session.createQuery("from ActiveTaskListBo");
			activeTaskListBos = query.list();
		}catch(Exception e){
			logger.error("StudyActiveTasksDAOImpl - getAllActiveTaskTypes() - ERROR " , e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getAllActiveTaskTypes() - Ends");
		return activeTaskListBos;
	}
	
	/**
	 * @author Ronalin
	 * @return List :ActiveTaskMasterAttributeBo
	 *  This method used to get  all the field names based on of activeTaskType
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActiveTaskMasterAttributeBo> getActiveTaskMasterAttributesByType(String activeTaskType) {
		logger.info("StudyActiveTasksDAOImpl - getActiveTaskMasterAttributesByType() - Starts");
		Session session = null;
		List<ActiveTaskMasterAttributeBo> taskMasterAttributeBos = new ArrayList<>();
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			query = session.createQuery(" from ActiveTaskMasterAttributeBo where taskTypeId="+Integer.parseInt(activeTaskType));
			taskMasterAttributeBos = query.list();
		}catch(Exception e){
			logger.error("StudyActiveTasksDAOImpl - getActiveTaskMasterAttributesByType() - ERROR " , e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getActiveTaskMasterAttributesByType() - Ends");
		return taskMasterAttributeBos;
	}
	
	/**
	 * @author Ronalin
	 * @return List :StatisticImageListBo
	 *  This method used to get  all  statistic images
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<StatisticImageListBo> getStatisticImages() {
		logger.info("StudyActiveTasksDAOImpl - getStatisticImages() - Starts");
		Session session = null;
		List<StatisticImageListBo> imageListBos = new ArrayList<>();
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			query = session.createQuery("from StatisticImageListBo");
			imageListBos = query.list();
		}catch(Exception e){
			logger.error("StudyActiveTasksDAOImpl - getStatisticImages() - ERROR " , e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getStatisticImages() - Ends");
		return imageListBos;
	}
	
	
	/**
	 * @author Ronalin
	 * @return List :ActivetaskFormulaBo
	 *  This method used to get  all  static formulas
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivetaskFormulaBo> getActivetaskFormulas() {
		logger.info("StudyActiveTasksDAOImpl - getActivetaskFormulas() - Starts");
		Session session = null;
		List<ActivetaskFormulaBo> activetaskFormulaList = new ArrayList<>();
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			query = session.createQuery("from ActivetaskFormulaBo");
			activetaskFormulaList = query.list();
		}catch(Exception e){
			logger.error("StudyActiveTasksDAOImpl - getActivetaskFormulas() - ERROR " , e);
		}finally{
			if (session != null) {
				session.close();
			}
		}
		logger.info("StudyActiveTasksDAOImpl - getActivetaskFormulas() - Ends");
		return activetaskFormulaList;
	}


	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public boolean validateActiveTaskAttrById(Integer studyId, String activeTaskAttName, String activeTaskAttIdVal, String activeTaskAttIdName, String customStudyId)
			 {
		logger.info("StudyDAOImpl - validateActiveTaskAttrById() - Starts");
		boolean flag = false;
		Session session =null;
		String queryString = "", subString="";
		List<ActiveTaskBo>  taskBos = null;
		List<ActiveTaskAtrributeValuesBo> taskAtrributeValuesBos = new ArrayList<>();
		List<QuestionnaireBo> questionnaireBo = null;
		List<ActiveTaskAtrributeValuesBo> activeTaskAtrributeValuesBos = null;
		List questionnairesStepsBo = null;
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			if(studyId!=null && StringUtils.isNotEmpty(activeTaskAttName) && StringUtils.isNotEmpty(activeTaskAttIdVal)){
				if(activeTaskAttName.equalsIgnoreCase(FdahpStudyDesignerConstants.SHORT_NAME_STATISTIC)){
					if(customStudyId != null && !customStudyId.isEmpty()){
						if(!activeTaskAttIdName.equals("static"))
							  subString = " and attributeValueId!="+activeTaskAttIdName;
						queryString = "from ActiveTaskAtrributeValuesBo where activeTaskId in(select id from ActiveTaskBo where studyId IN "
								+ "(select id From StudyBo SBO WHERE customStudyId='"+customStudyId+"')) and identifierNameStat='"+activeTaskAttIdVal+"'"+subString+"";
						activeTaskAtrributeValuesBos = session.createQuery(queryString).list();
						if(activeTaskAtrributeValuesBos!=null && !activeTaskAtrributeValuesBos.isEmpty()){
							flag = true;
						}else{
							//check in questionnaries as well
						    queryString = "From QuestionsBo QBO where QBO.id IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select id from QuestionnaireBo Q where Q.studyId in(select id From StudyBo SBO WHERE customStudyId='"+customStudyId+"') and QSBO.stepType='"+FdahpStudyDesignerConstants.QUESTION_STEP+"') and QBO.statShortName='"+activeTaskAttIdVal+"'";
							query = session.createQuery(queryString);   
							questionnairesStepsBo =  query.list();   
							if(questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()){    
								flag = true;   
							}else{
								queryString = "From QuestionsBo QBO where QBO.id IN (select f.questionId from FormMappingBo f where f.formId in (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select id from QuestionnaireBo Q where Q.studyId IN(select id From StudyBo SBO WHERE customStudyId='"+customStudyId+"')) and QSBO.stepType='"+FdahpStudyDesignerConstants.FORM_STEP+"')) and QBO.statShortName='"+activeTaskAttIdVal+"'";
								query = session.createQuery(queryString);
								List<QuestionsBo> questionsBo = query.list();   
							    if(questionsBo != null && !questionsBo.isEmpty())
								 flag = true;  
							    else
							     flag = false;
							}
					  }
					}else{
						if(!activeTaskAttIdName.equals("static"))
							  subString = " and attributeValueId!="+activeTaskAttIdName;
						queryString = "from ActiveTaskAtrributeValuesBo where activeTaskId in(select id from ActiveTaskBo where studyId="+studyId+") "
								+ "and identifierNameStat='"+activeTaskAttIdVal+"'"+subString+"";
						activeTaskAtrributeValuesBos = session.createQuery(queryString).list();
						if(activeTaskAtrributeValuesBos!=null && !activeTaskAtrributeValuesBos.isEmpty()){
							flag = true;
						}else{
							//check in questionnaries as well
						    queryString = "From QuestionsBo QBO where QBO.id IN (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select id from QuestionnaireBo Q where Q.studyId="+studyId+") and QSBO.stepType='"+FdahpStudyDesignerConstants.QUESTION_STEP+"') and QBO.statShortName='"+activeTaskAttIdVal+"'";
							query = session.createQuery(queryString);   
							questionnairesStepsBo =  query.list();   
							if(questionnairesStepsBo != null && !questionnairesStepsBo.isEmpty()){    
								flag = true;   
							}else{
								queryString = "From QuestionsBo QBO where QBO.id IN (select f.questionId from FormMappingBo f where f.formId in (select QSBO.instructionFormId from QuestionnairesStepsBo QSBO where QSBO.questionnairesId IN (select id from QuestionnaireBo Q where Q.studyId="+studyId+") and QSBO.stepType='"+FdahpStudyDesignerConstants.FORM_STEP+"')) and QBO.statShortName='"+activeTaskAttIdVal+"'";    
								query = session.createQuery(queryString);
								List<QuestionsBo> questionsBo = query.list();   
							    if(questionsBo != null && !questionsBo.isEmpty())
								 flag = true;  
							    else
							     flag = false;
							}
					  }
					}
				}else if(activeTaskAttName.equalsIgnoreCase(FdahpStudyDesignerConstants.SHORT_TITLE)){
					if(customStudyId != null && !customStudyId.isEmpty()){
						queryString = "from ActiveTaskBo where studyId IN (select id From StudyBo SBO WHERE customStudyId='"+customStudyId+"') and shortTitle='"+activeTaskAttIdVal+"'";
						taskBos = session.createQuery(queryString).list();
						if(taskBos!=null && !taskBos.isEmpty()){
							flag = true;   
						}else{
							queryString = "From QuestionnaireBo QBO where QBO.studyId IN(select id From StudyBo SBO WHERE customStudyId='"+customStudyId+"') and QBO.shortTitle='"+activeTaskAttIdVal+"'";
							query = session.createQuery(queryString);
							questionnaireBo = query.list();
							if(questionnaireBo != null && !questionnaireBo.isEmpty()){
						    	flag = true;
						    }else{
						    	flag = false;
						    }
						}
					}else{
						queryString = "from ActiveTaskBo where studyId="+studyId+" and shortTitle='"+activeTaskAttIdVal+"'";
						taskBos = session.createQuery(queryString).list();
						if(taskBos!=null && !taskBos.isEmpty()){
							flag = true;   
						}else{
							questionnaireBo = session.getNamedQuery("checkQuestionnaireShortTitle").setInteger("studyId", studyId).setString("shortTitle", activeTaskAttIdVal).list();
							if(questionnaireBo != null && !questionnaireBo.isEmpty()){
						    	flag = true;
						    }else{
						    	flag = false;
						    }
						}
					}
				}
			}
		}catch(Exception e){
			logger.error("StudyDAOImpl - validateActiveTaskAttrById() - ERROR",e);
		}finally{
			if(null != session && session.isOpen()){
					session.close();
			}
		}
		logger.info("StudyDAOImpl - validateActiveTaskAttrById() - Starts");
		return flag;
	}
}
