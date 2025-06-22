package nl.nijhuissven.orbit.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
public class TimeEntry {
    private int time;
    private TimeUnit unit;

    public String getReadableTime() {
        if (this.time == -1) return "Permanent";
        return this.time + " " + this.unit.name().toLowerCase();
    }

    public long getLength() {
        if (this.time == -1) return -1;

        long duration = System.currentTimeMillis() + this.unit.toMillis(this.time);
        return duration == -1 ? -1 : (duration + 1000);
    }

    public static TimeEntry from(String rawEntry) {
        if(rawEntry.contains(".")) return null;

        String entry = rawEntry.toLowerCase();

        if (entry.equalsIgnoreCase("perm")) {
            return new TimeEntry(-1, TimeUnit.DAYS);
        }

        try {
            if (entry.contains("y")) return new TimeEntry(stripEntry(entry) * 365, TimeUnit.DAYS);
            if (entry.contains("mo")) return new TimeEntry(stripEntry(entry) * 30, TimeUnit.DAYS);
            if (entry.contains("w")) return new TimeEntry(stripEntry(entry) * 7, TimeUnit.DAYS);
            if (entry.contains("d")) return new TimeEntry(stripEntry(entry), TimeUnit.DAYS);
            if (entry.contains("h")) return new TimeEntry(stripEntry(entry), TimeUnit.HOURS);
            if (entry.contains("m")) return new TimeEntry(stripEntry(entry), TimeUnit.MINUTES);
            if (entry.contains("s")) return new TimeEntry(stripEntry(entry), TimeUnit.SECONDS);
        } catch (NumberFormatException e){
            return null;
        }

        return null;
    }

    public static boolean isTimeEntry(String input) {
        if (input.equalsIgnoreCase("perm")) return true;
        return input.matches("^[0-9]+[y|mo|w|d|h|m|s]$");
    }

    private static Integer stripEntry(String entry) throws NumberFormatException {
        String replace = entry.replace("y", "")
                .replace("mo", "")
                .replace("w", "")
                .replace("d", "")
                .replace("h", "")
                .replace("m", "")
                .replace("s", "");

        return Integer.parseInt(replace);
    }

    public static long getParsedTime(TimeEntry entry) {
        if (entry.getTime() == -1) return -1;

        long duration;
        switch (entry.getUnit()) {
            case DAYS -> duration = TimeUnit.DAYS.toSeconds(entry.getTime());
            case HOURS -> duration = TimeUnit.HOURS.toSeconds(entry.getTime());
            case MINUTES -> duration = TimeUnit.MINUTES.toSeconds(entry.getTime());
            default -> duration = TimeUnit.SECONDS.toSeconds(entry.getTime());
        }

        return duration;
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}