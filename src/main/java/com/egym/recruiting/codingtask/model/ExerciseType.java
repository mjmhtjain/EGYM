package com.egym.recruiting.codingtask.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Exercise type
 */
public enum ExerciseType {

	RUNNING("RUNNING"),

	SWIMMING("SWIMMING"),

	STRENGTH_TRAINING("STRENGTH_TRAINING"),

	CIRCUIT_TRAINING("CIRCUIT_TRAINING");

	private final String value;

	ExerciseType(final String value) {
		this.value = value;
	}

	@JsonCreator
	public static ExerciseType fromValue(final String value) {
		for (final ExerciseType b : ExerciseType.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}
}

