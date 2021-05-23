package com.egym.recruiting.codingtask.service;

import com.egym.recruiting.codingtask.config.ExerciseConstants;
import com.egym.recruiting.codingtask.dao.ExerciseRepository;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.exception.ConflictException;
import com.egym.recruiting.codingtask.exception.NotFoundException;
import com.egym.recruiting.codingtask.exception.SecurityException;
import com.egym.recruiting.codingtask.model.Exercise;
import com.egym.recruiting.codingtask.model.ExerciseType;
import com.egym.recruiting.codingtask.model.RankingUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExerciseService {
    private ExerciseRepository exerciseRepository;
    private Map<ExerciseType, Integer> exerciseTypePointMultipleMap;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        initPointMultipleMap();
    }

    private void initPointMultipleMap() {
        exerciseTypePointMultipleMap = new HashMap<>();
        exerciseTypePointMultipleMap.put(ExerciseType.RUNNING, ExerciseConstants.RUNNING_POINT_MULTIPLE);
        exerciseTypePointMultipleMap.put(ExerciseType.SWIMMING, ExerciseConstants.SWIMMING_POINT_MULTIPLE);
        exerciseTypePointMultipleMap.put(ExerciseType.STRENGTH_TRAINING, ExerciseConstants.STRENGTH_TRAINING_POINT_MULTIPLE);
        exerciseTypePointMultipleMap.put(ExerciseType.CIRCUIT_TRAINING, ExerciseConstants.CIRCUIT_TRAINING_POINT_MULTIPLE);
    }

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
        if (e.getType() != null && !e.getType().equals(exerciseInDb.getType()))
            throw new IllegalArgumentException();

        // Check if it is changing user id
        if (e.getUserId() != null && !e.getUserId().equals(exerciseInDb.getUserId()))
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

        //fetch ranking of all userIds
        rankingUsers = userIds.stream()
                .map(userId -> {
                    List<Exercise> exercises = exerciseRepository.getUserExercisesBetweenTwoDates(userId,
                            OffsetDateTime.now().minus(ExerciseConstants.EXCERCISE_START_DAY_OFFSET, ChronoUnit.DAYS),
                            OffsetDateTime.now());

                    RankingUser rankingUser = genRankingUser(userId, exercises);
                    return rankingUser;
                })
                .collect(Collectors.toList());

        //sort the ranking in desc order
        Collections.sort(rankingUsers, new Comparator<RankingUser>() {
            @Override
            public int compare(RankingUser a, RankingUser b) {
                int compare = Float.compare(b.getPoints(), a.getPoints());
                if (compare != 0) return compare;

                return b.getEndExercise().compareTo(a.getEndExercise());
            }
        });

        return rankingUsers;
    }

    private RankingUser genRankingUser(long userId, List<Exercise> exercises) {
        RankingUser rankingUser = new RankingUser(userId);

        for (Exercise exercise : exercises) {
            float points = calcPoints(exercise) + rankingUser.getPoints();
            rankingUser.setPoints(points);
        }

        Collections.sort(exercises, new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                return o2.getDuration().compareTo(o1.getDuration());
            }
        });

        if (!exercises.isEmpty()) {
            rankingUser.setEndExercise(exercises.get(0).getStartTime());
        }

        return rankingUser;
    }

    private float calcPoints(Exercise exercise) {
        return (pointsPerCalories(exercise) + pointsPerStartMinute(exercise))
                * percentBasedOnDay(exercise)
                * multiplicationFactor(exercise);
    }

    private int multiplicationFactor(Exercise exercise) {
        return exerciseTypePointMultipleMap.get(exercise.getType());
    }

    private float percentBasedOnDay(Exercise exercise) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime exerciseStartTime = exercise.getStartTime();

        long offsetInDays = exerciseStartTime.until(now, ChronoUnit.DAYS);
        float percent = (100 - (float) (offsetInDays * ExerciseConstants.PERCENT_DEDUCTION_PER_DAY)) / 100;
        return percent < 0 ? 0 : percent;
    }

    private float pointsPerStartMinute(Exercise exercise) {
        return (exercise.getDuration() / ExerciseConstants.SECONDS_IN_MINUTE) + 1;
    }

    private float pointsPerCalories(Exercise exercise) {
        return exercise.getCalories();
    }

    private boolean isInConflict(final Exercise exerciseToPersist, final Collection<Exercise> userExercises,
                                 final boolean update) {

        return false;
    }

    public boolean isValidInputForInsert(final ExerciseDTO e) {
        return isValidInputForUpdate(e);
    }

    public boolean isValidInputForUpdate(final ExerciseDTO e) {
        if (isNegativeCalories(e) || isFutureStartEndDate(e)) {
            return false;
        }

        return true;
    }

    private boolean isFutureStartEndDate(ExerciseDTO exerciseDTO) {
        OffsetDateTime startTime = exerciseDTO.getStartTime();
        OffsetDateTime endTime = startTime.plus(exerciseDTO.getDuration(), ChronoUnit.SECONDS);

        return startTime.compareTo(OffsetDateTime.now()) > 0 &&
                endTime.compareTo(OffsetDateTime.now()) > 0;
    }

    private boolean isNegativeCalories(ExerciseDTO e) {
        return e.getCalories() < 0;
    }

}
