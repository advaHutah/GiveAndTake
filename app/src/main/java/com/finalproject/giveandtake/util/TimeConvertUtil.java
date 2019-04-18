package com.finalproject.giveandtake.util;

/**
 * Created by win10 on 1/1/2019.
 */

public class TimeConvertUtil {

//    public static String secondToFullTime(long second) {
//        return timeUnitToFullTime(second, TimeUnit.SECONDS);
//    }
//
//    public static String timeUnitToFullTime(long time, TimeUnit timeUnit) {
//        long day = timeUnit.toDays(time);
//        long hour = timeUnit.toHours(time) % 24;
//        long minute = timeUnit.toMinutes(time) % 60;
//        long second = timeUnit.toSeconds(time) % 60;
//        if (day > 0) {
//            return String.format("%dday %02d:%02d:%02d", day, hour, minute, second);
//        } else if (hour > 0) {
//            return String.format("%d:%02d:%02d", hour, minute, second);
//        } else if (minute > 0) {
//            return String.format("%d:%02d", minute, second);
//        } else {
//            return String.format("%02d", second);
//        }
//    }

    public static String convertTime(long millis) {
        long seconds = millis / 1000;//convert to seconds
        long minutes = seconds / 60;//convert to minutes
        long hours = minutes / 60;//convert to hours

        if (minutes > 0)//if we have minutes, then there might be some remainder seconds
            seconds = seconds % 60;//seconds can be between 0-60, so we use the % operator to get the remainder
        if (hours > 0)
            minutes = minutes % 60;//similar to seconds
        return formatNumber(hours) + ":" + formatNumber(minutes) + ":" +
                formatNumber(seconds);
    }

    public static String formatNumber(long value) {
        if (value < 10)
            return "0" + value;
        return value + "";
    }
}
