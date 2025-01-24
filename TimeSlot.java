
// TimeSlot.java
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    public TimeSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static LocalTime parseTime(String timeStr) throws DateTimeParseException {
        return LocalTime.parse(timeStr, FORMATTER);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return startTime + " to " + endTime;
    }

    // Check if two time slots overlap
    public boolean overlaps(TimeSlot other) {
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }
}