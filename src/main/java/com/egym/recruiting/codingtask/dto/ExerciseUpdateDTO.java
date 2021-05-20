package com.egym.recruiting.codingtask.dto;

import java.time.OffsetDateTime;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

class ExerciseUpdateDTO {

	@JsonProperty("description")
	private String description;

	@JsonProperty("startTime")
	private OffsetDateTime startTime;

	@JsonProperty("startTime")
	private Long duration;

	@JsonProperty("calories")
	private Long calories;

	/**
	 * Description of the exercise, a non empty string containing only alphanumeric characters and spaces in between
	 *
	 * @return description
	 */
	@ApiModelProperty(value = "Description of the exercise, a non empty string containing only alphanumeric characters and spaces in between")

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Date and time the user started the exercise
	 *
	 * @return startTime
	 */
	@ApiModelProperty(value = "Date and time the user started the exercise")

	@Valid

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(final OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Duration of the exercise in seconds
	 *
	 * @return duration
	 */
	@ApiModelProperty(value = "Duration of the exercise in seconds")

	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	/**
	 * Calories burnt in the exercise
	 *
	 * @return calories
	 */
	@ApiModelProperty(value = "Calories burnt in the exercise")

	public Long getCalories() {
		return calories;
	}

	public void setCalories(final Long calories) {
		this.calories = calories;
	}
}
