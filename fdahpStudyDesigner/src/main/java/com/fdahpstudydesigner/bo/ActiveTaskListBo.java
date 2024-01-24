package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


/**
 * The persistent class for the active_task_list database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "active_task_list")
@NamedQueries({
  @NamedQuery(name = "ActiveTaskListBo.findAll", query = "SELECT ATLB FROM ActiveTaskListBo ATLB"),
})
public class ActiveTaskListBo implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "active_task_list_id")
  private Integer activeTaskListId;

  @Column(name = "task_name")
  private String taskName;

  @Column(name = "type")
  private String type;

  public Integer getActiveTaskListId() {
    return activeTaskListId;
  }

  public String getTaskName() {
    return taskName;
  }

  public String getType() {
    return type;
  }

  public void setActiveTaskListId(Integer activeTaskListId) {
    this.activeTaskListId = activeTaskListId;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  public void setType(String type) {
    this.type = type;
  }
}
