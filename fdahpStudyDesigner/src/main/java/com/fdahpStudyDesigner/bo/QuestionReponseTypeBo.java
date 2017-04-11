package com.fdahpStudyDesigner.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="response_type_value")
public class QuestionReponseTypeBo implements Serializable {

	private static final long serialVersionUID = 2659206312696342901L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="response_type_id")
	private Integer responseTypeId;
	
	@Column(name="questions_response_type_id")
	private Integer questionsResponseTypeId;
	
	@Column(name="max_value")
	private Integer maxValue;
	
	@Column(name="min_value")
	private Integer minValue;
	
	@Column(name="default_value")
	private Integer defaultValue;
	
	@Column(name="step")
	private Integer step;
	
	@Column(name="vertical")
	private Boolean vertical;
	
	@Column(name="max_desc")
	private String maxDescription;
	
	@Column(name="min_desc")
	private String minDescription;
	
	@Column(name="min_image")
	private String minImage;
	
	@Column(name="max_image")
	private String maxImage;
	
	@Column(name="max_fraction_digits")
	private Integer maxFractionDigits;
	
	@Column(name="text_choices")
	private String textChoices;
	
	@Column(name="selection_style")
	private String selectionStyle;
	
	@Column(name="image_size")
	private String imageSize;
	
	@Column(name="style")
	private String style;
	
	@Column(name="unit")
	private String unit;
	
	@Column(name="placeholder")
	private String placeholder;
	
	@Column(name="min_date")
	private String minDate;
	
	@Column(name="max_date")
	private String maxDate;
	
	@Column(name="default_date")
	private String defaultDate;
	
	@Column(name="max_length")
	private Integer maxLength;
	
	@Column(name="validation_regex")
	private String validationRegex;
	
	@Column(name="invalid_message")
	private String invalidMessage;
	
	@Column(name="multiple_lines")
	private Boolean multipleLines;
	
	@Column(name="measurement_system")
	private String measurementSystem;
	
	@Column(name="use_current_location")
	private Boolean useCurrentLocation;
	
	@Column(name = "study_version")
	private Integer studyVersion=1;

	public Integer getResponseTypeId() {
		return responseTypeId;
	}

	public void setResponseTypeId(Integer responseTypeId) {
		this.responseTypeId = responseTypeId;
	}

	public Integer getQuestionsResponseTypeId() {
		return questionsResponseTypeId;
	}

	public void setQuestionsResponseTypeId(Integer questionsResponseTypeId) {
		this.questionsResponseTypeId = questionsResponseTypeId;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Boolean getVertical() {
		return vertical;
	}

	public void setVertical(Boolean vertical) {
		this.vertical = vertical;
	}

	public String getMaxDescription() {
		return maxDescription;
	}

	public void setMaxDescription(String maxDescription) {
		this.maxDescription = maxDescription;
	}

	public String getMinDescription() {
		return minDescription;
	}

	public void setMinDescription(String minDescription) {
		this.minDescription = minDescription;
	}

	public String getMinImage() {
		return minImage;
	}

	public void setMinImage(String minImage) {
		this.minImage = minImage;
	}

	public String getMaxImage() {
		return maxImage;
	}

	public void setMaxImage(String maxImage) {
		this.maxImage = maxImage;
	}

	public Integer getMaxFractionDigits() {
		return maxFractionDigits;
	}

	public void setMaxFractionDigits(Integer maxFractionDigits) {
		this.maxFractionDigits = maxFractionDigits;
	}

	public String getTextChoices() {
		return textChoices;
	}

	public void setTextChoices(String textChoices) {
		this.textChoices = textChoices;
	}

	public String getSelectionStyle() {
		return selectionStyle;
	}

	public void setSelectionStyle(String selectionStyle) {
		this.selectionStyle = selectionStyle;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public String getDefaultDate() {
		return defaultDate;
	}

	public void setDefaultDate(String defaultDate) {
		this.defaultDate = defaultDate;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getValidationRegex() {
		return validationRegex;
	}

	public void setValidationRegex(String validationRegex) {
		this.validationRegex = validationRegex;
	}

	public String getInvalidMessage() {
		return invalidMessage;
	}

	public void setInvalidMessage(String invalidMessage) {
		this.invalidMessage = invalidMessage;
	}

	public Boolean getMultipleLines() {
		return multipleLines;
	}

	public void setMultipleLines(Boolean multipleLines) {
		this.multipleLines = multipleLines;
	}

	public String getMeasurementSystem() {
		return measurementSystem;
	}

	public void setMeasurementSystem(String measurementSystem) {
		this.measurementSystem = measurementSystem;
	}

	public Boolean getUseCurrentLocation() {
		return useCurrentLocation;
	}

	public void setUseCurrentLocation(Boolean useCurrentLocation) {
		this.useCurrentLocation = useCurrentLocation;
	}

	public Integer getStudyVersion() {
		return studyVersion;
	}

	public void setStudyVersion(Integer studyVersion) {
		this.studyVersion = studyVersion;
	}
	
}