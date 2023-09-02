package io.github.adainish.cobblemontournamentsforge.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class TimeUtil
{


    public static String timeSinceInHoursMinutesFromString(long timer) {
        // Parsing Time Period in the format HH:MM:SS
        Instant previousInstant = Instant.ofEpochMilli(timer);
        Instant currentInstant = Instant.ofEpochMilli(System.currentTimeMillis());
        TemporalAccessor ta = Instant.ofEpochMilli(timer);

        long days = ChronoUnit.DAYS.between(previousInstant, currentInstant);

        // Calculating the difference in Hours
        long hours = ChronoUnit.HOURS.between(previousInstant, currentInstant);

        // Calculating the difference in Minutes
        long minutes
                = ChronoUnit.MINUTES.between(previousInstant, currentInstant) % 60;

        // Calculating the difference in Seconds
        long seconds
                = ChronoUnit.SECONDS.between(previousInstant, currentInstant) % 60;

        return "&b" + hours + " Hours and " + minutes + " Minutes &7ago";
    }
}
