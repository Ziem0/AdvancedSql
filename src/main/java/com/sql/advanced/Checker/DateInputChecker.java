package com.sql.advanced.Checker;

public class DateInputChecker {

    public static boolean isMonthCorrect(int month) {
        return !String.valueOf(month).matches("^(1[0-2]|[1-9])$");
    }

    public static boolean isDayCorrect(int day) {
        return !String.valueOf(day).matches("[0-9]|[1-2]{1}[0-9]{1}|3[0-1]");
    }

}
