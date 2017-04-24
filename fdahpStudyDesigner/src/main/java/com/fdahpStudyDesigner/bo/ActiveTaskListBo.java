package com.fdahpstudydesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author Ronalin
 *
 */
@Entity
@Table(name="active_task_list")
@NamedQueries({ 
	@NamedQuery(name="ActiveTaskListBo.findAll", query="SELECT ATLB FROM ActiveTaskListBo ATLB"), 
})
public class ActiveTaskListBo implements Serializable{
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="active_task_list_id")
	private Integer activeTaskListId;
	
	@Column(name = "task_name")
	private String taskName;
	
	@Column(name="type")
	private String type;
	
	public Integer getActiveTaskListId() {
		return activeTaskListId;
	}

	public void setActiveTaskListId(Integer activeTaskListId) {
		this.activeTaskListId = activeTaskListId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
