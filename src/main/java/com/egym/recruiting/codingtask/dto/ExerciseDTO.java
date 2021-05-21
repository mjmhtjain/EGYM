package com.egym.recruiting.codingtask.dto;

import com.egym.recruiting.codingtask.model.Exercise;
import com.egym.recruiting.codingtask.model.ExerciseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ExerciseDTO extends ExerciseUpdateDTO {
	@JsonProperty("userId")
	@NotNull
	private Long userId;

	@JsonProperty("type")
	@NotNull
	private ExerciseType type;

	public static Exercise fromDTO(final ExerciseDTO dto) {
		final Exercise e = new Exercise();
		e.userId(dto.getUserId());
		e.description(dto.getDescription());
		e.type(dto.getType());
		e.startTime(dto.getStartTime());
		e.duration(dto.getDuration());
		e.calories(dto.getCalories());
		return e;
	}

	/**
	 * User who did the exercise
	 *
	 * @return userId
	 */
	@ApiModelProperty(value = "User who did the exercise")

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	/**
	 * Get type
	 *
	 * @return type
	 */
	@ApiModelProperty(value = "")

	@Valid

	public ExerciseType getType() {
		return type;
	}

	public void setType(final ExerciseType type) {
		this.type = type;
	}
}
