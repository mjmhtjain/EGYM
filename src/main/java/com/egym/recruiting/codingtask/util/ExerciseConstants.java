package com.egym.recruiting.codingtask.util;

import java.util.regex.Pattern;

public class ExerciseConstants {
    /** Regular expression for parsing RFC3339 date/times. */
    public static final Pattern RFC3339_PATTERN = Pattern.compile(
            "^(\\d{4})-(\\d{2})-(\\d{2})" // yyyy-MM-dd
                    + "([Tt](\\d{2}):(\\d{2}):(\\d{2})(\\.\\d+)?)?" // 'T'HH:mm:ss.milliseconds
                    + "([Zz]|([+-])(\\d{2}):(\\d{2}))?"); // 'Z' or time zone shift HH:mm following '+' or '-'

    public static final Pattern ALPHANUMERIC_WITH_SPACES_PATTERN = Pattern.compile("^[a-zA-Z0-9 ]*$");

    public static final Integer RUNNING_POINT_MULTIPLE = 2;
    public static final Integer SWIMMING_POINT_MULTIPLE = 3;
    public static final Integer STRENGTH_TRAINING_POINT_MULTIPLE = 3;
    public static final Integer CIRCUIT_TRAINING_POINT_MULTIPLE = 4;

    public static final Integer EXCERCISE_START_DAY_OFFSET = 28;
    public static final Integer PERCENT_DEDUCTION_PER_DAY = 10;
    public static final Integer SECONDS_IN_MINUTE = 60;
}
