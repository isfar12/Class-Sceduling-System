import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.List;

public class Instructor extends User {
    private List<String> courses;
    private JFrame instructorFrame;
    private Schedule schedule;
    private CommentSection commentSection;

    public Instructor(String name, String key, List<String> courses) {
        super(name, key);
        this.courses = courses;
    }

    public List<String> getCourses() {
        return courses;
    }

    @Override
    public void displayDashboard(Schedule schedule, CommentSection commentSection) {
        this.schedule = schedule;
        this.commentSection = commentSection;

        instructorFrame = new JFrame("Instructor Dashboard - " + this.name);
        instructorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructorFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        // Dashboard buttons
        JButton viewScheduleButton = new JButton("View Daily Schedule");
        JButton bookClassButton = new JButton("Book a Class");
        JButton cancelBookingButton = new JButton("Cancel a Booking");
        JButton commentSectionButton = new JButton("Access Comment Section");
        JButton logoutButton = new JButton("Logout");

        // Add action listeners for each button
        viewScheduleButton.addActionListener(e -> schedule.displayAllBookings()); // Assume this method displays via GUI
        bookClassButton.addActionListener(e -> showBookClassDialog());
        cancelBookingButton.addActionListener(e -> showCancelBookingDialog());
        commentSectionButton.addActionListener(e -> commentSection.interact(this)); // Assume GUI interaction is added
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(instructorFrame, "Logging out...");
            instructorFrame.dispose();  // Close the dashboard
        });

        // Add buttons to the panel
        panel.add(viewScheduleButton);
        panel.add(bookClassButton);
        panel.add(cancelBookingButton);
        panel.add(commentSectionButton);
        panel.add(logoutButton);

        instructorFrame.add(panel);
        instructorFrame.setVisible(true);
    }

    private void showBookClassDialog() {
        // Create a dialog for booking a class
        JDialog bookClassDialog = new JDialog(instructorFrame, "Book a Class", true);
        bookClassDialog.setSize(400, 300);
        bookClassDialog.setLayout(new GridLayout(5, 2));

        // Inputs
        JLabel courseLabel = new JLabel("Course Code:");
        JComboBox<String> courseCombo = new JComboBox<>(courses.toArray(new String[0]));
        
        JLabel roomLabel = new JLabel("Room Number:");
        JTextField roomField = new JTextField();

        JLabel startTimeLabel = new JLabel("Start Time (HH:MM):");
        JTextField startTimeField = new JTextField();

        JLabel durationLabel = new JLabel("Duration (in minutes):");
        JTextField durationField = new JTextField();

        JButton bookButton = new JButton("Book");

        bookButton.addActionListener(e -> {
            String courseCode = (String) courseCombo.getSelectedItem();
            String roomName = roomField.getText();
            String startTimeInput = startTimeField.getText();
            int duration = Integer.parseInt(durationField.getText());

            // Time calculation and booking logic
            try {
                LocalTime startTime = TimeSlot.parseTime(startTimeInput);
                LocalTime endTime = startTime.plusMinutes(duration);
                TimeSlot timeSlot = new TimeSlot(startTime, endTime);

                boolean success = schedule.bookClass(this.name, courseCode, timeSlot, roomName);
                if (success) {
                    JOptionPane.showMessageDialog(bookClassDialog, "Booking successful!");
                } else {
                    JOptionPane.showMessageDialog(bookClassDialog, "Booking failed due to a conflict.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(bookClassDialog, "Error: " + ex.getMessage());
            }
        });

        // Add components to the dialog
        bookClassDialog.add(courseLabel);
        bookClassDialog.add(courseCombo);
        bookClassDialog.add(roomLabel);
        bookClassDialog.add(roomField);
        bookClassDialog.add(startTimeLabel);
        bookClassDialog.add(startTimeField);
        bookClassDialog.add(durationLabel);
        bookClassDialog.add(durationField);
        bookClassDialog.add(new JLabel()); // empty space
        bookClassDialog.add(bookButton);

        bookClassDialog.setVisible(true);
    }

    private void showCancelBookingDialog() {
        // Create a dialog for canceling a booking
        JDialog cancelDialog = new JDialog(instructorFrame, "Cancel a Booking", true);
        cancelDialog.setSize(400, 200);
        cancelDialog.setLayout(new GridLayout(3, 2));

        // Inputs
        JLabel courseLabel = new JLabel("Course Code:");
        JTextField courseField = new JTextField();

        JLabel startTimeLabel = new JLabel("Start Time (HH:MM):");
        JTextField startTimeField = new JTextField();

        JButton cancelButton = new JButton("Cancel Booking");

        cancelButton.addActionListener(e -> {
            String courseCode = courseField.getText().toUpperCase();
            String startTimeInput = startTimeField.getText();

            try {
                LocalTime startTime = TimeSlot.parseTime(startTimeInput);
                schedule.cancelBooking(this.name, courseCode, startTime);
                JOptionPane.showMessageDialog(cancelDialog, "Booking canceled successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(cancelDialog, "Error: " + ex.getMessage());
            }
        });

        // Add components to the dialog
        cancelDialog.add(courseLabel);
        cancelDialog.add(courseField);
        cancelDialog.add(startTimeLabel);
        cancelDialog.add(startTimeField);
        cancelDialog.add(new JLabel()); // empty space
        cancelDialog.add(cancelButton);

        cancelDialog.setVisible(true);
    }
}
