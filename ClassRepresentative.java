import javax.swing.*;

public class ClassRepresentative extends User {

    public ClassRepresentative(String name, String key) {
        super(name, key);
    }

    @Override
    public void displayDashboard(Schedule schedule, CommentSection commentSection) {
        while (true) {
            // Options for the CR dashboard
            String[] options = {"View Daily Schedule", "Access Comment Section", "Logout"};
            int choice = JOptionPane.showOptionDialog(null,
                    "--- Class Representative Dashboard ---\nSelect an option:",
                    "CR Dashboard",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    // View Daily Schedule (show all bookings using GUI)
                    schedule.displayAllBookings();  // Assumes `displayAllBookings` is GUI-based
                    break;
                case 1:
                    // Access Comment Section (comment section GUI interaction)
                    commentSection.interact(this);  // Assumes `interact` is GUI-based
                    break;
                case 2:
                    // Logout action
                    JOptionPane.showMessageDialog(null, "Logging out...", "Logout", JOptionPane.INFORMATION_MESSAGE);
                    return;
                default:
                    // Handle closing or invalid actions (for safety)
                    return;
            }
        }
    }
}
