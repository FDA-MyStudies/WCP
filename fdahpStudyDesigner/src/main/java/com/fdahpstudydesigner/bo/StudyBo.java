package com.fdahpstudydesigner.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.web.multipart.MultipartFile;

import com.fdahpstudydesigner.bean.StudyListBean;

/**
 * 
 * @author Ronalin
 *
 */
@Entity
@Table(name = "studies")
@NamedQueries({
	@NamedQuery(name = "StudyBo.getStudiesById", query = " From StudyBo SBO WHERE SBO.id =:id"),
})
public class StudyBo implements Serializable{
	
	private static final long serialVersionUID = 2147840266295837728L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "custom_study_id")
	private String customStudyId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "platform")
	private String platform;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "research_sponsor")
	private String researchSponsor;
	
	@Column(name = "data_partner")
	private String dataPartner;
	
	@Column(name = "tentative_duration")
	private Integer tentativeDuration;
	
	@Column(name = "tentative_duration_weekmonth")
	private String tentativeDurationWeekmonth;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "enrolling_participants")
	private String enrollingParticipants;
	
	@Column(name = "retain_participant")
	private String retainParticipant;
	
	@Column(name = "allow_rejoin")
	private String allowRejoin;
	
	@Column(name = "allow_rejoin_text")
	private String allowRejoinText;
	
	@Column(name = "irb_review")
	private String irbReview;
	
	@Column(name = "inbox_email_address")
	private String inboxEmailAddress;
	
	@Column(name = "created_on")
	private String createdOn;
	
	@Column(name = "modified_on")
	private String modifiedOn;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "sequence_number")
	private Integer sequenceNumber;
	
	@Column(name = "thumbnail_image")
	private String thumbnailImage;
	
	@Column(name="media_link")
	private String mediaLink;
	
	@Column(name="study_website")
	private String studyWebsite;
	
	@Column(name="study_tagline")
	private String studyTagLine;
	
	@Column(name="study_version")
	private Integer studyVersion=1;
	
	@Column(name="study_lunched_date")
	private String studylunchDate;
	
	@Column(name = "study_pre_active_flag")
	@Type(type="yes_no")
	private boolean studyPreActiveFlag = false;
	
	@Transient
	private List<StudyListBean> studyPermissions = new ArrayList<>();
	
	@Transient
	private MultipartFile file;
	
	@Transient
	private Integer userId;
	
	@Transient
	StudySequenceBo studySequenceBo = new StudySequenceBo();
	
	@Transient
	private boolean viewPermission = true;
	
	@Transient
	private String buttonText;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomStudyId() {
		return customStudyId;
	}

	public void setCustomStudyId(String customStudyId) {
		this.customStudyId = customStudyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getResearchSponsor() {
		return researchSponsor;
	}

	public void setResearchSponsor(String researchSponsor) {
		this.researchSponsor = researchSponsor;
	}

	public String getDataPartner() {
		return dataPartner;
	}

	public void setDataPartner(String dataPartner) {
		this.dataPartner = dataPartner;
	}

	public Integer getTentativeDuration() {
		return tentativeDuration;
	}

	public void setTentativeDuration(Integer tentativeDuration) {
		this.tentativeDuration = tentativeDuration;
	}

	public String getTentativeDurationWeekmonth() {
		return tentativeDurationWeekmonth;
	}

	public void setTentativeDurationWeekmonth(String tentativeDurationWeekmonth) {
		this.tentativeDurationWeekmonth = tentativeDurationWeekmonth;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnrollingParticipants() {
		return enrollingParticipants;
	}

	public void setEnrollingParticipants(String enrollingParticipants) {
		this.enrollingParticipants = enrollingParticipants;
	}

	public String getRetainParticipant() {
		return retainParticipant;
	}

	public void setRetainParticipant(String retainParticipant) {
		this.retainParticipant = retainParticipant;
	}

	public String getAllowRejoin() {
		return allowRejoin;
	}

	public void setAllowRejoin(String allowRejoin) {
		this.allowRejoin = allowRejoin;
	}
	
	public String getAllowRejoinText() {
		return allowRejoinText;
	}

	public void setAllowRejoinText(String allowRejoinText) {
		this.allowRejoinText = allowRejoinText;
	}

	public String getIrbReview() {
		return irbReview;
	}

	public void setIrbReview(String irbReview) {
		this.irbReview = irbReview;
	}

	public String getInboxEmailAddress() {
		return inboxEmailAddress;
	}

	public void setInboxEmailAddress(String inboxEmailAddress) {
		this.inboxEmailAddress = inboxEmailAddress;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	
	public String getMediaLink() {
		return mediaLink;
	}

	public void setMediaLink(String mediaLink) {
		this.mediaLink = mediaLink;
	}
	
	public String getStudyWebsite() {
		return studyWebsite;
	}

	public void setStudyWebsite(String studyWebsite) {
		this.studyWebsite = studyWebsite;
	}
	public String getStudyTagLine() {
		return studyTagLine;
	}

	public void setStudyTagLine(String studyTagLine) {
		this.studyTagLine = studyTagLine;
	}
	
	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}

	public String getStudylunchDate() {
		return studylunchDate;
	}

	public void setStudylunchDate(String studylunchDate) {
		this.studylunchDate = studylunchDate;
	}
	
	public boolean isStudyPreActiveFlag() {
		return studyPreActiveFlag;
	}

	public void setStudyPreActiveFlag(boolean studyPreActiveFlag) {
		this.studyPreActiveFlag = studyPreActiveFlag;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public StudySequenceBo getStudySequenceBo() {
		return studySequenceBo;
	}

	public void setStudySequenceBo(StudySequenceBo studySequenceBo) {
		this.studySequenceBo = studySequenceBo;
	}
	
	public boolean isViewPermission() {
		return viewPermission;
	}

	public void setViewPermission(boolean viewPermission) {
		this.viewPermission = viewPermission;
	}
	
	public String getButtonText() {
		return buttonText;
	}

	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	public List<StudyListBean> getStudyPermissions() {
		return studyPermissions;
	}

	public void setStudyPermissions(List<StudyListBean> studyPermissions) {
		this.studyPermissions = studyPermissions;
	}
}