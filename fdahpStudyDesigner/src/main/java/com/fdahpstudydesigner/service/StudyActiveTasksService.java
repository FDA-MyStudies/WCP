/**
 * 
 */
package com.fdahpstudydesigner.service;

import java.util.List;

import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.ActiveTaskListBo;
import com.fdahpstudydesigner.bo.ActiveTaskMasterAttributeBo;
import com.fdahpstudydesigner.bo.ActivetaskFormulaBo;
import com.fdahpstudydesigner.bo.StatisticImageListBo;
import com.fdahpstudydesigner.util.SessionObject;

/**
 * @author Vivek
 *
 */
public interface StudyActiveTasksService {

	public List<ActiveTaskBo> getStudyActiveTasksByStudyId(String studyId);
	
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo);
	public ActiveTaskBo getActiveTaskById(Integer activeTaskId);
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo activeTaskBo,SessionObject sessionObject);
	public String deleteActiveTask(Integer activeTaskInfoId,Integer studyId);
	public List<ActiveTaskListBo> getAllActiveTaskTypes();
	public List<ActiveTaskMasterAttributeBo> getActiveTaskMasterAttributesByType(String activeTaskType);
	public List<StatisticImageListBo> getStatisticImages();
	public List<ActivetaskFormulaBo> getActivetaskFormulas();
	public boolean validateActiveTaskAttrById(Integer studyId, String activeTaskName, String activeTaskAttIdVal, String activeTaskAttIdName);
}