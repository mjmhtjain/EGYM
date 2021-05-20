package com.egym.recruiting.codingtask.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import com.egym.recruiting.codingtask.model.Exercise;

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
	@Query("SELECT e FROM Exercise e")
	List<Exercise> getUserExercisesBetweenTwoDates(final Long userId, final LocalDate begin,
			final LocalDate end);

}
