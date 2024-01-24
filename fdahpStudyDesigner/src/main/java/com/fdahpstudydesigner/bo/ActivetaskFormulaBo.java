package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The persistent class for the activetask_formula database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "activetask_formula")
public class ActivetaskFormulaBo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "activetask_formula_id")
  private Integer activetaskFormulaId;

  @Column(name = "value")
  private String value;

  public Integer getActivetaskFormulaId() {
    return activetaskFormulaId;
  }

  public String getValue() {
    return value;
  }

  public void setActivetaskFormulaId(Integer activetaskFormulaId) {
    this.activetaskFormulaId = activetaskFormulaId;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
