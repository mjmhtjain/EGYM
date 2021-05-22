package com.egym.recruiting.codingtask.dao;

import com.egym.recruiting.codingtask.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {


	/**
	 * Use a JPA/Hibernate query to find all exercises for a given user.
	 *
	 * @param userId the id of the user for which we are searching
	 * @return A list of Exercises for a user or an empty list.
	 */
	@Query("SELECT e FROM Exercise e WHERE e.userId = ?1")
	List<Exercise> getUserExercises(Long userId);


	// TODO fix the query
	@Query("SELECT e FROM Exercise e WHERE e.userId = ?1 AND e.startTime >= ?2 AND e.startTime <= ?3")
	List<Exercise> getUserExercisesBetweenTwoDates(final Long userId, final OffsetDateTime start,
												   final OffsetDateTime end);

}
