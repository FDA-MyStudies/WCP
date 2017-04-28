/**
 * 
 */
package com.fdahpstudydesigner.dao;

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
				
				if(activeTasks!=null && activeTasks.size()>0 && activeTaskListBos!=null && activeTaskListBos.size()>0){
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
	public ActiveTaskBo getActiveTaskById(Integer activeTaskId) {
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
				if(activeTaskAtrributeValuesBos!=null && activeTaskAtrributeValuesBos.size()>0){
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
	public ActiveTaskBo saveOrUpdateActiveTaskInfo(ActiveTaskBo activeTaskBo, SessionObject sesObj ) {
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
						   if(activeTaskAtrributeValuesBo.getAttributeValueId()==null){
							   session.save(activeTaskAtrributeValuesBo);
					        }else{
							   session.update(activeTaskAtrributeValuesBo); 
						   }
					   }
				   }
			}
			
			if(StringUtils.isNotEmpty(activeTaskBo.getButtonText()) && 
					activeTaskBo.getButtonText().equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)){
				studySequence = (StudySequenceBo) session.getNamedQuery("getStudySequenceByStudyId").setInteger("studyId", activeTaskBo.getStudyId()).uniqueResult();
				if(studySequence != null){
					studySequence.setStudyExcActiveTask(false);
				}
				session.saveOrUpdate(studySequence);
			}
			
			if(activeTaskBo.getButtonText().equalsIgnoreCase(FdahpStudyDesignerConstants.ACTION_TYPE_SAVE)){
				activity = "ActiveTask saved";
				activitydetails = "ActiveTask saved but not eligible for mark as completed action untill unless it is DONE";
			}else{
				activity = "ActiveTask done";
				activitydetails = "ActiveTask done and eligible for mark as completed action";
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
	public String deleteActiveTask(ActiveTaskBo activeTaskBo, SessionObject sesObj) {
		logger.info("StudyActiveTasksDAOImpl - deleteActiveTAsk() - Starts");
		String message = FdahpStudyDesignerConstants.FAILURE;
		Session session = null;
		try {
			session = hibernateTemplate.getSessionFactory().openSession();
			if(activeTaskBo != null) {
				transaction =session.beginTransaction();
				String deleteQuery = "delete ActiveTaskAtrributeValuesBo where activeTaskId="+activeTaskBo.getId();
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
				
				deleteQuery = "delete ActiveTaskBo where id="+activeTaskBo.getId();
				query = session.createQuery(deleteQuery);
				query.executeUpdate();
				message = FdahpStudyDesignerConstants.SUCCESS;
				
				auditLogDAO.saveToAuditLog(session, transaction, sesObj, "ActiveTask deleted", "ActiveTask deleted for the respective study", "StudyActiveTasksDAOImpl - deleteActiveTAsk");
				
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
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo) {
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


	@SuppressWarnings("unchecked")
	@Override
	public boolean validateActiveTaskAttrById(Integer studyId, String activeTaskAttName, String activeTaskAttIdVal, String activeTaskAttIdName)
			 {
		logger.info("StudyDAOImpl - validateActiveTaskAttrById() - Starts");
		boolean flag = false;
		Session session =null;
		String queryString = "", subString="";
		ActiveTaskBo  taskBo = new ActiveTaskBo();
		List<ActiveTaskAtrributeValuesBo> taskAtrributeValuesBos = new ArrayList<>();
		try{
			session = hibernateTemplate.getSessionFactory().openSession();
			if(studyId!=null && StringUtils.isNotEmpty(activeTaskAttName) && StringUtils.isNotEmpty(activeTaskAttIdVal)){
				if(activeTaskAttName.equalsIgnoreCase(FdahpStudyDesignerConstants.SHORT_NAME_STATISTIC)){
					if(!activeTaskAttIdName.equals("static"))
					 subString = " and attributeValueId!="+activeTaskAttIdName;
					queryString = "from ActiveTaskAtrributeValuesBo where identifierNameStat='"+activeTaskAttIdVal+"'"+subString+")";
					taskAtrributeValuesBos = session.createQuery(queryString).list();
					if(taskAtrributeValuesBos!=null && !taskAtrributeValuesBos.isEmpty())
						flag = true;
				}else if(activeTaskAttName.equalsIgnoreCase(FdahpStudyDesignerConstants.SHORT_TITLE)){
					queryString = "from ActiveTaskBo where studyId="+studyId+" and shortTitle='"+activeTaskAttIdVal+"'";
					taskBo = (ActiveTaskBo)session.createQuery(queryString).uniqueResult();
					if(taskBo!=null)
						flag = true;
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
