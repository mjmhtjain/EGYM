package com.egym.recruiting.codingtask.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * RankingUser
 */
public class RankingUser {
	@JsonProperty("userId")
	private Long userId;

	@JsonProperty("points")
	private Float points = 0.0f;

	@JsonIgnore
	private OffsetDateTime endExercise;

	public RankingUser() {
	}

	public RankingUser(Long userId) {
		this.userId = userId;
	}

	public OffsetDateTime getEndExercise() {
		return endExercise;
	}

	public void setEndExercise(final OffsetDateTime endExercise) {
		this.endExercise = endExercise;
	}

	public RankingUser endExercise(final OffsetDateTime endExercise) {
		this.endExercise = endExercise;
		return this;
	}

	public RankingUser userId(Long userId) {
		this.userId = userId;
		return this;
	}

	/**
	 * User id
	 *
	 * @return userId
	 */
	@ApiModelProperty(example = "1", value = "User id")

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public RankingUser points(Float points) {
		this.points = points;
		return this;
	}

	/**
	 * Points scored by the user in the last 28 days
	 *
	 * @return points
	 */
	@ApiModelProperty(example = "1000.51", value = "Points scored by the user in the last 28 days")

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RankingUser rankingUser = (RankingUser) o;
		return Objects.equals(this.userId, rankingUser.userId) && Objects.equals(this.points, rankingUser.points);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, points);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class RankingUser {\n");

		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    points: ").append(toIndentedString(points)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}

