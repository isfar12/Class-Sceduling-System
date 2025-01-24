// Booking.java
public class Booking {
    private String instructorName;
    private String courseCode;
    private TimeSlot timeSlot;
    private String roomName;

    public Booking(String instructorName, String courseCode, TimeSlot timeSlot, String roomName) {
        this.instructorName = instructorName;
        this.courseCode = courseCode;
        this.timeSlot = timeSlot;
        this.roomName = roomName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public String toString() {
        return "Course: " + courseCode + ", Instructor: " + instructorName +
                ", Time: " + timeSlot + ", Room: " + roomName;
    }
}