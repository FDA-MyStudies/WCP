package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The persistent class for the question_responsetype_master_info database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "question_responsetype_master_info")
@NamedQueries({
  @NamedQuery(name = "getResponseTypes", query = "from QuestionResponseTypeMasterInfoBo QRTMBO"),
})
public class QuestionResponseTypeMasterInfoBo implements Serializable {

  private static final long serialVersionUID = -2666359241071290949L;

  @Column(name = "anchor_date")
  private Boolean anchorDate;

  @Column(name = "choice_based_branching")
  private Boolean choinceBasedBraching;

  @Column(name = "dashboard_allowed")
  private Boolean dashBoardAllowed;

  @Column(name = "data_type")
  private String dataType;

  @Column(name = "description")
  private String description;

  @Column(name = "formula_based_logic")
  private Boolean formulaBasedLogic;

  @Column(name = "healthkit_alternative")
  private Boolean healthkitAlternative;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "response_type")
  private String responseType;

  @Column(name = "response_type_code")
  private String responseTypeCode;

  public Boolean getAnchorDate() {
    return anchorDate;
  }

  public Boolean getChoinceBasedBraching() {
    return choinceBasedBraching;
  }

  public Boolean getDashBoardAllowed() {
    return dashBoardAllowed;
  }

  public String getDataType() {
    return dataType;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getFormulaBasedLogic() {
    return formulaBasedLogic;
  }

  public Boolean getHealthkitAlternative() {
    return healthkitAlternative;
  }

  public Integer getId() {
    return id;
  }

  public String getResponseType() {
    return responseType;
  }

  public void setAnchorDate(Boolean anchorDate) {
    this.anchorDate = anchorDate;
  }

  public void setChoinceBasedBraching(Boolean choinceBasedBraching) {
    this.choinceBasedBraching = choinceBasedBraching;
  }

  public void setDashBoardAllowed(Boolean dashBoardAllowed) {
    this.dashBoardAllowed = dashBoardAllowed;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setFormulaBasedLogic(Boolean formulaBasedLogic) {
    this.formulaBasedLogic = formulaBasedLogic;
  }

  public void setHealthkitAlternative(Boolean healthkitAlternative) {
    this.healthkitAlternative = healthkitAlternative;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setResponseType(String responseType) {
    this.responseType = responseType;
  }
}
