package com.fdahpStudyDesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Kanchana 
 * 
 */
@Entity
@Table(name = "notification_history")
public class NotificationHistoryBO implements Serializable{

	private static final long serialVersionUID = 3634540541782531200L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="history_id")
	private Integer historyId;
	
	@Column(name = "notification_id")
	private Integer notificationId;
	
	@Column(name = "notification_sent_date_time")
	private String notificationSentDateTime;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;
	
	@Transient
	private String notificationSentdtTime;
	
	
	public Integer getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Integer historyId) {
		this.historyId = historyId;
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationSentDateTime() {
		return notificationSentDateTime;
	}

	public void setNotificationSentDateTime(String notificationSentDateTime) {
		this.notificationSentDateTime = notificationSentDateTime;
	}

	public String getNotificationSentdtTime() {
		return notificationSentdtTime;
	}

	public void setNotificationSentdtTime(String notificationSentdtTime) {
		this.notificationSentdtTime = notificationSentdtTime;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}


	

}