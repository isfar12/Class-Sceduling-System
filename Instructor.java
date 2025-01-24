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
        instructorFrame.setSize(600, 400);
        instructorFrame.setLocationRelativeTo(null); // Center the window

        // Load background image
        ImageIcon backgroundIcon = new ImageIcon("instructor.jpg");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new GridBagLayout()); // Layout for centering components

        // Transparent panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Buttons
        JButton viewScheduleButton = new JButton("View Daily Schedule");
        JButton bookClassButton = new JButton("Book a Class");
        JButton cancelBookingButton = new JButton("Cancel a Booking");
        JButton commentSectionButton = new JButton("Access Comment Section");
        JButton logoutButton = new JButton("Logout");

        // Set button size
        Dimension buttonSize = new Dimension(200, 40);
        viewScheduleButton.setPreferredSize(buttonSize);
        bookClassButton.setPreferredSize(buttonSize);
        cancelBookingButton.setPreferredSize(buttonSize);
        commentSectionButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        // Add buttons with action listeners
        viewScheduleButton.addActionListener(e -> schedule.displayAllBookings());
        bookClassButton.addActionListener(e -> showBookClassDialog());
        cancelBookingButton.addActionListener(e -> showCancelBookingDialog());
        commentSectionButton.addActionListener(e -> commentSection.interact(this));
        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(instructorFrame, "Logging out...");
            instructorFrame.dispose();
        });

        // Add buttons to panel
        buttonPanel.add(viewScheduleButton, gbc);
        gbc.gridy = 1;
        buttonPanel.add(bookClassButton, gbc);
        gbc.gridy = 2;
        buttonPanel.add(cancelBookingButton, gbc);
        gbc.gridy = 3;
        buttonPanel.add(commentSectionButton, gbc);
        gbc.gridy = 4;
        buttonPanel.add(logoutButton, gbc);

        // Add button panel to background
        backgroundLabel.add(buttonPanel);

        // Add background to frame
        instructorFrame.setContentPane(backgroundLabel);
        instructorFrame.setVisible(true);
    }

    private void showBookClassDialog() {
        JDialog bookClassDialog = new JDialog(instructorFrame, "Book a Class", true);
        bookClassDialog.setSize(400, 300);
        bookClassDialog.setLayout(new GridLayout(5, 2));

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

        bookClassDialog.add(courseLabel);
        bookClassDialog.add(courseCombo);
        bookClassDialog.add(roomLabel);
        bookClassDialog.add(roomField);
        bookClassDialog.add(startTimeLabel);
        bookClassDialog.add(startTimeField);
        bookClassDialog.add(durationLabel);
        bookClassDialog.add(durationField);
        bookClassDialog.add(new JLabel());
        bookClassDialog.add(bookButton);

        bookClassDialog.setVisible(true);
    }

    private void showCancelBookingDialog() {
        JDialog cancelDialog = new JDialog(instructorFrame, "Cancel a Booking", true);
        cancelDialog.setSize(400, 200);
        cancelDialog.setLayout(new GridLayout(3, 2));

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

        cancelDialog.add(courseLabel);
        cancelDialog.add(courseField);
        cancelDialog.add(startTimeLabel);
        cancelDialog.add(startTimeField);
        cancelDialog.add(new JLabel());
        cancelDialog.add(cancelButton);

        cancelDialog.setVisible(true);
    }
}
