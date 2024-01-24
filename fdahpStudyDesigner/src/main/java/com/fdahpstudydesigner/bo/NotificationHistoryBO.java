package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


/**
 * The persistent class for the notification_history database table.
 *
 * @author BTC
 */
@Entity
@Table(name = "notification_history")
public class NotificationHistoryBO implements Serializable {

  private static final long serialVersionUID = 3634540541782531200L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "history_id")
  private Integer historyId;

  @Column(name = "notification_id")
  private Integer notificationId;

  @Column(name = "notification_sent_date_time")
  private String notificationSentDateTime;

  @Transient private String notificationSentdtTime;

  public Integer getHistoryId() {
    return historyId;
  }

  public Integer getNotificationId() {
    return notificationId;
  }

  public String getNotificationSentDateTime() {
    return notificationSentDateTime;
  }

  public String getNotificationSentdtTime() {
    return notificationSentdtTime;
  }

  public void setHistoryId(Integer historyId) {
    this.historyId = historyId;
  }

  public void setNotificationId(Integer notificationId) {
    this.notificationId = notificationId;
  }

  public void setNotificationSentDateTime(String notificationSentDateTime) {
    this.notificationSentDateTime = notificationSentDateTime;
  }

  public void setNotificationSentdtTime(String notificationSentdtTime) {
    this.notificationSentdtTime = notificationSentdtTime;
  }
}
