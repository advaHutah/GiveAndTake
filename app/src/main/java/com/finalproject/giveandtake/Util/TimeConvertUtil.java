package com.finalproject.giveandtake.Util;


public class TimeConvertUtil {

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
