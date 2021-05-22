package com.egym.recruiting.codingtask.service;

import com.egym.recruiting.codingtask.dao.ExerciseRepository;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.exception.ConflictException;
import com.egym.recruiting.codingtask.exception.NotFoundException;
import com.egym.recruiting.codingtask.exception.SecurityException;
import com.egym.recruiting.codingtask.model.Exercise;
import com.egym.recruiting.codingtask.model.RankingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseService {


	@Autowired
	private ExerciseRepository exerciseRepository;

	public Exercise insert(final ExerciseDTO e) {
		// Validate input
		if (!isValidInputForInsert(e)) {
			throw new IllegalArgumentException();
		}

		final Exercise ex = ExerciseDTO.fromDTO(e);
		//Check if the exercise is gonna be a conflict
		if (isInConflict(ex, exerciseRepository.getUserExercises(e.getUserId()), false)) {
			throw new ConflictException();
		}

		return exerciseRepository.saveAndFlush(ex);
	}

	public Exercise update(final Long exerciseId, final ExerciseDTO e) {
		// Validate input
		if (!isValidInputForUpdate(e)) {
			throw new IllegalArgumentException();
		}

		//Check if the exercise exists
		Optional<Exercise> exerciseOptional = exerciseRepository.findById(exerciseId);
		if (exerciseOptional.isEmpty()) {
			throw new NotFoundException();
		}

		Exercise exerciseInDb = exerciseOptional.get();
		// Check if it is changing type
		if(e.getType() != null && !e.getType().equals(exerciseInDb.getType()))
			throw new IllegalArgumentException();

		// Check if it is changing user id
		if(e.getUserId() != null && !e.getUserId().equals(exerciseInDb.getUserId()))
			throw new SecurityException();

		final Exercise ex = ExerciseDTO.fromDTO(e);
		//Check if the exercise is gonna be a conflict
		ex.setId(exerciseId);
		if (isInConflict(ex, exerciseRepository.getUserExercises(exerciseInDb.getUserId()), true)) {
			throw new ConflictException();
		}

		// Update the exercise values
		exerciseInDb.setId(exerciseId);
		exerciseInDb.setCalories(ex.getCalories());
		exerciseInDb.setDescription(ex.getDescription());
		exerciseInDb.setDuration(ex.getDuration());
		exerciseInDb.setStartTime(ex.getStartTime());

		return exerciseRepository.saveAndFlush(exerciseInDb);
	}

    public List<RankingUser> ranking(final List<Long> userIds) {
        List<RankingUser> rankingUsers;

        rankingUsers = userIds.stream()
                .map(userId -> {
                    List<Exercise> exercises = exerciseRepository.getUserExercisesBetweenTwoDates(userId,
                            OffsetDateTime.now().minus(28, ChronoUnit.DAYS),
                            OffsetDateTime.now());

                    RankingUser rankingUser = genRankingUser(userId, exercises);
                    return rankingUser;
                })
                .collect(Collectors.toList());

        //TODO: sort in decreasing order
        return rankingUsers;
    }

    private RankingUser genRankingUser(long userId, List<Exercise> exercises) {
        RankingUser rankingUser = new RankingUser(userId);

        for(Exercise exercise : exercises){
            float points = calcPoints(exercise) + rankingUser.getPoints();
            rankingUser.setPoints(points);
        }

        return rankingUser;
    }

    private float calcPoints(Exercise exercise) {
		return (pointsPerCalories(exercise) + pointsPerStartMinute(exercise))
				* percentBasedOnDay(exercise)
				* multiplicationFactor(exercise);
    }

	private float multiplicationFactor(Exercise exercise) {
		switch (exercise.getType()) {
			case RUNNING:
				return 2;
			case SWIMMING:
				return 3;
			case STRENGTH_TRAINING:
				return 3;
			case CIRCUIT_TRAINING:
				return 4;
			default:
				return 0;
		}
	}

	private float percentBasedOnDay(Exercise exercise) {
		OffsetDateTime now = OffsetDateTime.now();
		OffsetDateTime exerciseStartTime = exercise.getStartTime();

		long offsetInDays = exerciseStartTime.until(now, ChronoUnit.DAYS);
		float percent =  (100 - (float)(offsetInDays * 10)) / 100;
		return percent < 0 ? 0 : percent;
	}

	private float pointsPerStartMinute(Exercise exercise) {
		return (exercise.getDuration() / 60) + 1;
	}

	private float pointsPerCalories(Exercise exercise) {
		return exercise.getCalories();
	}

	private boolean isInConflict(final Exercise exerciseToPersist, final Collection<Exercise> userExercises,
			final boolean update) {
		// TODO
		return false;
	}

	public boolean isValidInputForInsert(final ExerciseDTO e) {
		// TODO

		return isValidInputForUpdate(e);
	}

	public boolean isValidInputForUpdate(final ExerciseDTO e) {
		// TODO

		return true;
	}

}
