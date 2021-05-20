package com.egym.recruiting.codingtask.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Exercise
 */
@Entity
public class Exercise implements Serializable, Persistable<Long> {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long userId;

	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	private ExerciseType type;

	@NotNull
	private OffsetDateTime startTime;

	@NotNull
	private Long duration;

	@NotNull
	private Long calories;

	public Exercise id(final Long id) {
		this.id = id;
		return this;
	}

	public Long getId() {
		return id;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return null == getId();
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Exercise userId(final Long userId) {
		this.userId = userId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public Exercise description(final String description) {
		this.description = description;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Exercise type(final ExerciseType type) {
		this.type = type;
		return this;
	}

	public ExerciseType getType() {
		return type;
	}

	public void setType(final ExerciseType type) {
		this.type = type;
	}

	public Exercise startTime(final OffsetDateTime startTime) {
		this.startTime = startTime;
		return this;
	}

	public OffsetDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(final OffsetDateTime startTime) {
		this.startTime = startTime;
	}

	public Exercise duration(final Long duration) {
		this.duration = duration;
		return this;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(final Long duration) {
		this.duration = duration;
	}

	public Exercise calories(final Long calories) {
		this.calories = calories;
		return this;
	}

	public Long getCalories() {
		return calories;
	}

	public void setCalories(final Long calories) {
		this.calories = calories;
	}

	@Override
	public boolean equals(final java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Exercise exercise = (Exercise) o;
		return Objects.equals(this.id, exercise.id) && Objects.equals(this.userId, exercise.userId) && Objects.equals(
				this.description,
				exercise.description) && Objects.equals(this.type, exercise.type) && Objects.equals(this.startTime,
				exercise.startTime) && Objects.equals(this.duration, exercise.duration) && Objects.equals(this.calories,
				exercise.calories);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, userId, description, type, startTime, duration, calories);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class Exercise {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    description: ").append(toIndentedString(description)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
		sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
		sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
		sb.append("    calories: ").append(toIndentedString(calories)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(final java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

