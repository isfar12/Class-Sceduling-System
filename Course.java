
// Course.java
import java.util.List;

public class Course {
    private String courseCode;
    private List<String> instructors;

    public Course(String courseCode, List<String> instructors) {
        this.courseCode = courseCode;
        this.instructors = instructors;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public List<String> getInstructors() {
        return instructors;
    }

    @Override
    public String toString() {
        return courseCode + " - " + String.join(", ", instructors);
    }
}