package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * The persistent class for the reference_tables database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "reference_tables")
public class ReferenceTablesBo implements Serializable {

  /** */
  private static final long serialVersionUID = 5124001773679736751L;

  @Column(name = "category")
  private String category;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "type")
  private String type;

  @Column(name = "str_value")
  private String value;

  public String getCategory() {
    return category;
  }

  public Integer getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
