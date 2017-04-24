/**
 * 
 */
package com.fdahpstudydesigner.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.ActiveTaskCustomScheduleBo;
import com.fdahpstudydesigner.bo.ActiveTaskFrequencyBo;
import com.fdahpstudydesigner.bo.ActiveTaskListBo;
import com.fdahpstudydesigner.bo.ActiveTaskMasterAttributeBo;
import com.fdahpstudydesigner.bo.ActivetaskFormulaBo;
import com.fdahpstudydesigner.bo.StatisticImageListBo;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.dao.StudyActiveTasksDAO;
import com.fdahpstudydesigner.util.FdahpStudyDesignerConstants;
import com.fdahpstudydesigner.util.FdahpStudyDesignerUtil;
import com.fdahpstudydesigner.util.SessionObject;

/**
 * @author Vivek
 *
 */
@Service
public class StudyActiveTasksServiceImpl implements StudyActiveTasksService{

	private static Logger logger = Logger.getLogger(StudyActiveTasksServiceImpl.class);
	
	@Autowired
	private StudyActiveTasksDAO studyActiveTasksDAO;

	/**
	 * return active tasks based on user's Study Id
	 * @author Vivek
	 * 
	 * @param studyId , studyId of the {@link StudyBo}
	 * @return List of {@link ActiveTaskBo}
	 * @exception Exception
	 */
	@Override
	public List<ActiveTaskBo> getStudyActiveTasksByStudyId(String studyId) {
		logger.info("StudyActiveTasksServiceImpl - getStudyActiveTasksByStudyId() - Starts");
		List<ActiveTaskBo> activeTasks = null;
		try {
			activeTasks = studyActiveTasksDAO.getStudyActiveTasksByStudyId(studyId);
		} catch (Exception e) {
			logger.error("StudyActiveTasksServiceImpl - getStudyActiveTasksByStudyId() - ERROR ", e);
		}
		logger.info("StudyActiveTasksServiceImpl - getStudyActiveTasksByStudyId() - Ends");
		return activeTasks;
	}
	
	@Override
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo) {
		logger.info("StudyQuestionnaireServiceImpl - saveORUpdateQuestionnaire - Starts");
		ActiveTaskBo addActiveTaskeBo = null;
		try{
			if(null != activeTaskBo){
				if(activeTaskBo.getId() != null){
					addActiveTaskeBo = studyActiveTasksDAO.getActiveTaskById(activeTaskBo.getId());
				}else{
					addActiveTaskeBo = new ActiveTaskBo();
				}
				if(activeTaskBo.getStudyId() != null){
					addActiveTaskeBo.setStudyId(activeTaskBo.getStudyId());
				}
				if(StringUtils.isNotBlank(activeTaskBo.getActiveTaskLifetimeStart()) && !("NA").equalsIgnoreCase(activeTaskBo.getActiveTaskLifetimeStart())){
					addActiveTaskeBo.setActiveTaskLifetimeStart(FdahpStudyDesignerConstants.SD_DATE_FORMAT.format(FdahpStudyDesignerConstants.SDF_DATE_FORMAT.parse(activeTaskBo.getActiveTaskLifetimeStart())));
				}
				if(StringUtils.isNotBlank(activeTaskBo.getActiveTaskLifetimeEnd()) && !("NA").equalsIgnoreCase(activeTaskBo.getActiveTaskLifetimeEnd())){
					addActiveTaskeBo.setActiveTaskLifetimeEnd(FdahpStudyDesignerConstants.SD_DATE_FORMAT.format(FdahpStudyDesignerConstants.SDF_DATE_FORMAT.parse(activeTaskBo.getActiveTaskLifetimeEnd())));
				}
				if(activeTaskBo.getFrequency() != null){
					addActiveTaskeBo.setFrequency(activeTaskBo.getFrequency());
				}
				if(activeTaskBo.getTitle() != null){
					addActiveTaskeBo.setTitle(activeTaskBo.getTitle());
				}
				if(activeTaskBo.getCreatedDate() != null){
					addActiveTaskeBo.setCreatedDate(activeTaskBo.getCreatedDate());
				}
				if(activeTaskBo.getCreatedBy() != null){
					addActiveTaskeBo.setCreatedBy(activeTaskBo.getCreatedBy());
				}
				if(activeTaskBo.getModifiedDate() != null){
					addActiveTaskeBo.setModifiedDate(activeTaskBo.getModifiedDate());
				}
				if(activeTaskBo.getModifiedBy() != null){
					addActiveTaskeBo.setModifiedBy(activeTaskBo.getModifiedBy());
				}
				if(activeTaskBo.getRepeatActiveTask() != null){
					addActiveTaskeBo.setRepeatActiveTask(activeTaskBo.getRepeatActiveTask());
				}
				if(activeTaskBo.getDayOfTheWeek() != null){
					addActiveTaskeBo.setDayOfTheWeek(activeTaskBo.getDayOfTheWeek());
				}
				if(activeTaskBo.getType() != null){
					addActiveTaskeBo.setType(activeTaskBo.getType());
				}
				if(activeTaskBo.getFrequency() != null){
					if(!activeTaskBo.getFrequency().equalsIgnoreCase(activeTaskBo.getPreviousFrequency())){
						addActiveTaskeBo.setActiveTaskCustomScheduleBo(activeTaskBo.getActiveTaskCustomScheduleBo());
						addActiveTaskeBo.setActiveTaskFrequenciesList(activeTaskBo.getActiveTaskFrequenciesList());
						addActiveTaskeBo.setActiveTaskFrequenciesBo(activeTaskBo.getActiveTaskFrequenciesBo());
						if(activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)){
							if(activeTaskBo.getActiveTaskFrequenciesBo() != null){
								if(activeTaskBo.getActiveTaskFrequenciesBo().getIsLaunchStudy()){
									addActiveTaskeBo.setActiveTaskLifetimeStart(null);
								}
								if(activeTaskBo.getActiveTaskFrequenciesBo().getIsStudyLifeTime()){
									addActiveTaskeBo.setActiveTaskLifetimeEnd(null);
								}
							}
						}
					}else{
						if(activeTaskBo.getActiveTaskCustomScheduleBo() != null && !activeTaskBo.getActiveTaskCustomScheduleBo().isEmpty()){
							addActiveTaskeBo.setActiveTaskCustomScheduleBo(activeTaskBo.getActiveTaskCustomScheduleBo());
						}
						if(activeTaskBo.getActiveTaskFrequenciesList() != null && !activeTaskBo.getActiveTaskFrequenciesList().isEmpty()){
							addActiveTaskeBo.setActiveTaskFrequenciesList(activeTaskBo.getActiveTaskFrequenciesList());
						}
						if(activeTaskBo.getActiveTaskFrequenciesBo()!= null){
							if(activeTaskBo.getFrequency().equalsIgnoreCase(FdahpStudyDesignerConstants.FREQUENCY_TYPE_ONE_TIME)){
								if(activeTaskBo.getActiveTaskFrequenciesBo().getIsLaunchStudy()){
									addActiveTaskeBo.setActiveTaskLifetimeStart(null);
								}
								if(activeTaskBo.getActiveTaskFrequenciesBo().getIsStudyLifeTime()){
									addActiveTaskeBo.setActiveTaskLifetimeEnd(null);
								}
							}
							addActiveTaskeBo.setActiveTaskFrequenciesBo(activeTaskBo.getActiveTaskFrequenciesBo());
						}
					}
				}
				if(activeTaskBo.getPreviousFrequency() != null){
					addActiveTaskeBo.setPreviousFrequency(activeTaskBo.getPreviousFrequency());
				}
				addActiveTaskeBo = studyActiveTasksDAO.saveOrUpdateActiveTask(addActiveTaskeBo);
			}
		}catch(Exception e){
			logger.error("StudyActiveTaskServiceImpl - saveORUpdateQuestionnaire - Error",e);
		}
		logger.info("StudyQuestionnaireServiceImpl - saveORUpdateQuestionnaire - Ends");
		return addActiveTaskeBo;
	}

	/**
	 * @author Ronalin
	 * @param ActiveTaskBo
	 * @return ActiveTaskBo
	 * 
	 * This method is used to save the activeTask
	 */
	@Override
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo,SessionObject sessionObject) {
		logger.info("StudyActiveTasksServiceImpl - saveOrUpdateActiveTask() - Starts");
		ActiveTaskBo updateActiveTaskBo = null;
		try{
			if(activeTaskBo != null){
				if(activeTaskBo.getId() != null){
					updateActiveTaskBo = studyActiveTasksDAO.getActiveTaskById(activeTaskBo.getId());
					updateActiveTaskBo.setModifiedBy(sessionObject.getUserId());
					updateActiveTaskBo.setModifiedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
				}else{
					updateActiveTaskBo = new ActiveTaskBo();
					updateActiveTaskBo.setStudyId(activeTaskBo.getStudyId());
					updateActiveTaskBo.setTaskTypeId(activeTaskBo.getTaskTypeId());
					updateActiveTaskBo.setCreatedBy(sessionObject.getUserId());
					updateActiveTaskBo.setCreatedDate(FdahpStudyDesignerUtil.getCurrentDateTime());
					updateActiveTaskBo.setDisplayName(StringUtils.isEmpty(activeTaskBo.getDisplayName())?"":activeTaskBo.getDisplayName());
					updateActiveTaskBo.setShortTitle(StringUtils.isEmpty(activeTaskBo.getShortTitle())?"":activeTaskBo.getShortTitle());
					updateActiveTaskBo.setInstruction(StringUtils.isEmpty(activeTaskBo.getInstruction())?"":activeTaskBo.getInstruction());
					updateActiveTaskBo.setTaskAttributeValueBos(activeTaskBo.getTaskAttributeValueBos());
				}
				updateActiveTaskBo.setStudyId(activeTaskBo.getStudyId());
				updateActiveTaskBo.setTaskTypeId(activeTaskBo.getTaskTypeId());
				updateActiveTaskBo.setDisplayName(StringUtils.isEmpty(activeTaskBo.getDisplayName())?"":activeTaskBo.getDisplayName());
				updateActiveTaskBo.setShortTitle(StringUtils.isEmpty(activeTaskBo.getShortTitle())?"":activeTaskBo.getShortTitle());
				updateActiveTaskBo.setInstruction(StringUtils.isEmpty(activeTaskBo.getInstruction())?"":activeTaskBo.getInstruction());
				updateActiveTaskBo.setTaskAttributeValueBos(activeTaskBo.getTaskAttributeValueBos());
				updateActiveTaskBo.setAction(activeTaskBo.isAction());
				updateActiveTaskBo.setButtonText(activeTaskBo.getButtonText());
				updateActiveTaskBo = studyActiveTasksDAO.saveOrUpdateActiveTaskInfo(updateActiveTaskBo);
			}
			
		}catch(Exception e){
			logger.error("StudyActiveTasksServiceImpl - saveOrUpdateActiveTask() - Error",e);
		}
		logger.info("StudyActiveTasksServiceImpl - saveOrUpdateActiveTask() - Ends");
		return updateActiveTaskBo;
	}
	
	/**
	 * @author Ronalin
	 * @param Integer :aciveTaskId
	 * @return Object :ActiveTaskBo
	 * 
	 * This method is used to get the ativeTask info object based on consent info id 
	 */
	@Override
	public ActiveTaskBo getActiveTaskById(Integer ativeTaskId) {
		logger.info("StudyActiveTasksServiceImpl - getActiveTaskById() - Starts");
		ActiveTaskBo activeTask = null;
		try {
			activeTask = studyActiveTasksDAO.getActiveTaskById(ativeTaskId);
			if(activeTask != null) {
				if(activeTask.getActiveTaskCustomScheduleBo() != null && !activeTask.getActiveTaskCustomScheduleBo().isEmpty()) {
					for (ActiveTaskCustomScheduleBo activeTaskCustomScheduleBo : activeTask.getActiveTaskCustomScheduleBo()) {
						if(StringUtils.isNotBlank(activeTaskCustomScheduleBo.getFrequencyStartDate())) {
							activeTaskCustomScheduleBo.setFrequencyStartDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskCustomScheduleBo.getFrequencyStartDate(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
						}
						if(StringUtils.isNotBlank(activeTaskCustomScheduleBo.getFrequencyEndDate())) {
							activeTaskCustomScheduleBo.setFrequencyEndDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskCustomScheduleBo.getFrequencyEndDate(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
						}
					}
				}
				if(activeTask.getActiveTaskFrequenciesList() != null && !activeTask.getActiveTaskFrequenciesList().isEmpty()) {
					for ( ActiveTaskFrequencyBo activeTaskFrequencyBo : activeTask.getActiveTaskFrequenciesList()) {
						if(StringUtils.isNotBlank(activeTaskFrequencyBo.getFrequencyDate())) {
							activeTaskFrequencyBo.setFrequencyDate(FdahpStudyDesignerUtil.getFormattedDate(activeTaskFrequencyBo.getFrequencyDate(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
						}
						
					}
				}
				if(activeTask.getActiveTaskFrequenciesBo() != null) {
					if(StringUtils.isNotBlank(activeTask.getActiveTaskFrequenciesBo().getFrequencyDate())) {
						activeTask.getActiveTaskFrequenciesBo().setFrequencyDate(FdahpStudyDesignerUtil.getFormattedDate(activeTask.getActiveTaskFrequenciesBo().getFrequencyDate(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
					}
				}
				if(StringUtils.isNotBlank(activeTask.getActiveTaskLifetimeEnd())) {
					activeTask.setActiveTaskLifetimeEnd(FdahpStudyDesignerUtil.getFormattedDate(activeTask.getActiveTaskLifetimeEnd(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
				}
				if(StringUtils.isNotBlank(activeTask.getActiveTaskLifetimeStart())) {
					activeTask.setActiveTaskLifetimeStart(FdahpStudyDesignerUtil.getFormattedDate(activeTask.getActiveTaskLifetimeStart(), FdahpStudyDesignerConstants.ACTUAL_DATE, FdahpStudyDesignerConstants.REQUIRED_DATE));
				}
				
			}
		} catch (Exception e) {
			logger.error("StudyActiveTasksServiceImpl - getActiveTaskById() - ERROR ", e);
		}
		logger.info("StudyActiveTasksServiceImpl - getActiveTaskById() - Ends");
		return activeTask;
	}
	
	/**
	 * @author Ronalin
	 * @param Integer : activeTaskInfoId
	 *  @param Integer : studyId
	 * @return String :SUCCESS or FAILURE
	 *  TThis method used to get the delete the consent information
	 */
	@Override
	public String deleteActiveTask(Integer activeTaskInfoId,Integer studyId) {
		logger.info("StudyServiceImpl - deleteActiveTask() - Starts");
		String message = null;
		ActiveTaskBo activeTaskBo = null;
		try {
			activeTaskBo = studyActiveTasksDAO.getActiveTaskById(activeTaskInfoId);
			if(activeTaskBo != null)
				message = studyActiveTasksDAO.deleteActiveTask(activeTaskBo);
		} catch (Exception e) {
			logger.error("StudyServiceImpl - deleteActiveTask() - Error", e);
		}
		logger.info("StudyServiceImpl - deleteActiveTask() - Ends");
		return message;
	}
	
	/**
	 * @author Ronalin
	 * @return List :ActiveTaskListBos
	 *  This method used to get all type of activeTask
	 */
	@Override
	public List<ActiveTaskListBo> getAllActiveTaskTypes(){
		logger.info("StudyActiveTasksServiceImpl - getAllActiveTaskTypes() - Starts");
		List<ActiveTaskListBo> activeTaskListBos = new ArrayList<>();
		try {
			activeTaskListBos = studyActiveTasksDAO.getAllActiveTaskTypes();
		} catch (Exception e) {
			logger.error("StudyActiveTasksServiceImpl - getAllActiveTaskTypes() - ERROR ", e);
		}
		logger.info("StudyActiveTasksServiceImpl - getAllActiveTaskTypes() - Ends");
		return activeTaskListBos;
	}
	
	/**
	 * @author Ronalin
	 * @return List :ActiveTaskMasterAttributeBo
	 *  This method used to get  all the field names based on of activeTaskType
	 */
	@Override
	public List<ActiveTaskMasterAttributeBo> getActiveTaskMasterAttributesByType(String activeTaskType) {
		logger.info("StudyActiveTasksServiceImpl - getActiveTaskMasterAttributesByType() - Starts");
		List<ActiveTaskMasterAttributeBo> taskMasterAttributeBos = new ArrayList<>();
		try {
			taskMasterAttributeBos = studyActiveTasksDAO.getActiveTaskMasterAttributesByType(activeTaskType);
		} catch (Exception e) {
			logger.error("StudyActiveTasksServiceImpl - getActiveTaskMasterAttributesByType() - ERROR ", e);
		}
		logger.info("StudyActiveTasksServiceImpl - getActiveTaskMasterAttributesByType() - Ends");
		return taskMasterAttributeBos;
	}

	/**
	 * @author Ronalin
	 * @return List :StatisticImageListBo
	 *  This method used to get  all  statistic images
	 */
	@Override
	public List<StatisticImageListBo> getStatisticImages() {
		logger.info("StudyActiveTasksServiceImpl - getStatisticImages() - Starts");
		List<StatisticImageListBo> statisticImageListBos = new ArrayList<>();
		try {
			statisticImageListBos = studyActiveTasksDAO.getStatisticImages();
		} catch (Exception e) {
			logger.error("StudyActiveTasksServiceImpl - getStatisticImages() - ERROR ", e);
		}
		logger.info("StudyActiveTasksServiceImpl - getStatisticImages() - Ends");
		return statisticImageListBos;
	}

	/**
	 * @author Ronalin
	 * @return List :ActivetaskFormulaBo
	 *  This method used to get  all  static formulas
	 */
	@Override
	public List<ActivetaskFormulaBo> getActivetaskFormulas() {
			logger.info("StudyActiveTasksServiceImpl - getActivetaskFormulas() - Starts");
			List<ActivetaskFormulaBo> activetaskFormulaList = new ArrayList<>();
			try {
				activetaskFormulaList = studyActiveTasksDAO.getActivetaskFormulas();
			} catch (Exception e) {
				logger.error("StudyActiveTasksServiceImpl - getActivetaskFormulas() - ERROR ", e);
			}
			logger.info("StudyActiveTasksServiceImpl - getActivetaskFormulas() - Ends");
			return activetaskFormulaList;
		}

	@Override
	public boolean validateActiveTaskAttrById(Integer studyId, String activeTaskAttName, String activeTaskAttIdVal, String activeTaskAttIdName) {
		logger.info("StudyActiveTasksServiceImpl - validateActiveTaskAttrById() - Starts");
		boolean valid = false;
		try{
			if(studyId!=null && StringUtils.isNotEmpty(activeTaskAttName) && StringUtils.isNotEmpty(activeTaskAttIdVal) && StringUtils.isNotEmpty(activeTaskAttIdName)){
				valid = studyActiveTasksDAO.validateActiveTaskAttrById(studyId, activeTaskAttName, activeTaskAttIdVal, activeTaskAttIdName);
			}
		}catch(Exception e){
			logger.error("StudyActiveTasksServiceImpl - validateActiveTaskAttrById() - ERROR ", e);
		}
		
		logger.info("StudyActiveTasksServiceImpl - validateActiveTaskAttrById() - Starts");
		return valid;
	}
}