package com.egym.recruiting.codingtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

class ExerciseUpdateDTO {

	@JsonProperty("description")
	@NotNull @NotBlank
	private String description;

	@JsonProperty("startTime")
	@NotNull
	private OffsetDateTime startTime;

	@JsonProperty("duration")
	@NotNull
	private Long duration;

	@JsonProperty("calories")
	@NotNull
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
