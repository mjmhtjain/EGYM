package com.egym.recruiting.codingtask.util;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class ExerciseValidationUtil {
    public static boolean isNonUTCTimestamp(OffsetDateTime timestamp){
        return timestamp.getOffset().compareTo(ZoneOffset.UTC) != 0;
    }

    public static boolean isInCorrectTimeStampFormat(OffsetDateTime timestamp){
        return !ExerciseConstants.RFC3339_PATTERN.matcher(timestamp.toString()).matches();
    }

    public static boolean isNonAlphanumeric(String s){
        return !ExerciseConstants.ALPHANUMERIC_WITH_SPACES_PATTERN.matcher(s).matches();
    }

    public static boolean isFutureTimeStamp(OffsetDateTime dateTime, Long duration) {
        OffsetDateTime endTime = dateTime.plus(duration, ChronoUnit.SECONDS);

        return isFutureTimeStamp(dateTime) ||
                isFutureTimeStamp(endTime);
    }

    public static boolean isFutureTimeStamp(OffsetDateTime dateTime) {
        return dateTime.compareTo(OffsetDateTime.now()) > 0;
    }

    public static boolean isNegative(Long val) {
        if (val == null) return false;
        return val < 0;
    }
}
