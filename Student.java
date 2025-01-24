import javax.swing.*;

public class Student extends User {

    public Student(String name, String key) {
        super(name, key);
    }

    @Override
    public void displayDashboard(Schedule schedule, CommentSection commentSection) {
        while (true) {
            String[] options = {"View Daily Schedule", "Logout"};
            int choice = JOptionPane.showOptionDialog(null,
                    "--- Student Dashboard ---\nSelect an option:",
                    "Student Dashboard",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    // View Daily Schedule (showing all bookings)
                    schedule.displayAllBookings();  // Assuming `displayAllBookings` shows the schedule using JOptionPane
                    break;
                case 1:
                    // Logout option
                    JOptionPane.showMessageDialog(null, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
                    return;
                default:
                    // Handles closing the dialog window or invalid options
                    return;
            }
        }
    }
}
