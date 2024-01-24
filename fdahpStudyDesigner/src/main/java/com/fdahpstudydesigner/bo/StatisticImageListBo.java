package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * The persistent class for the statistic_master_images database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "statistic_master_images")
public class StatisticImageListBo implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "statistic_image_id")
  private Integer statisticImageId;

  @Column(name = "value")
  private String value;

  public Integer getStatisticImageId() {
    return statisticImageId;
  }

  public String getValue() {
    return value;
  }

  public void setStatisticImageId(Integer statisticImageId) {
    this.statisticImageId = statisticImageId;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
