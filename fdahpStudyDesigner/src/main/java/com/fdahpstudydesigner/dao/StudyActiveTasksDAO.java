/**
 * 
 */
package com.fdahpstudydesigner.dao;

import java.util.List;

import com.fdahpstudydesigner.bo.ActiveTaskBo;
import com.fdahpstudydesigner.bo.ActiveTaskListBo;
import com.fdahpstudydesigner.bo.ActiveTaskMasterAttributeBo;
import com.fdahpstudydesigner.bo.ActivetaskFormulaBo;
import com.fdahpstudydesigner.bo.StatisticImageListBo;

/**
 * @author Vivek
 *
 */
public interface StudyActiveTasksDAO {

	public List<ActiveTaskBo> getStudyActiveTasksByStudyId(String studyId);
	public ActiveTaskBo getActiveTaskById(Integer activeTaskId);
	public ActiveTaskBo saveOrUpdateActiveTaskInfo(ActiveTaskBo activeTaskBo);
	public String deleteActiveTask(ActiveTaskBo activeTaskBo);
	
	public ActiveTaskBo saveOrUpdateActiveTask(ActiveTaskBo addActiveTaskeBo);
	public List<ActiveTaskListBo> getAllActiveTaskTypes();
	public List<ActiveTaskMasterAttributeBo> getActiveTaskMasterAttributesByType(String activeTaskType);
	public List<StatisticImageListBo> getStatisticImages();
	public List<ActivetaskFormulaBo> getActivetaskFormulas();
	public boolean validateActiveTaskAttrById(Integer studyId, String activeTaskName, String activeTaskAttIdVal, String activeTaskAttIdName);
}