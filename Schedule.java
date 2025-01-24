import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private List<Booking> bookings;

    public Schedule() {
        bookings = new ArrayList<>();
    }

    // Book a class
    public boolean bookClass(String instructorName, String courseCode, TimeSlot timeSlot, String roomName) {
        // Check for room conflicts
        for (Booking b : bookings) {
            if (b.getRoomName().equalsIgnoreCase(roomName)) {
                if (b.getTimeSlot().overlaps(timeSlot)) {
                    JOptionPane.showMessageDialog(null,
                            "Room " + roomName + " is already booked during " + b.getTimeSlot(),
                            "Booking Conflict", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }

            // Check if instructor is already booked
            if (b.getInstructorName().equalsIgnoreCase(instructorName)) {
                if (b.getTimeSlot().overlaps(timeSlot)) {
                    JOptionPane.showMessageDialog(null,
                            "You are already booked during " + b.getTimeSlot(),
                            "Instructor Conflict", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
        }

        // If no conflicts, add the booking
        Booking newBooking = new Booking(instructorName, courseCode, timeSlot, roomName);
        bookings.add(newBooking);
        JOptionPane.showMessageDialog(null,
                "Booking successful for " + courseCode + " in " + roomName,
                "Booking Successful", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    // Cancel a booking
    public void cancelBooking(String instructorName, String courseCode, LocalTime startTime) {
        Booking toRemove = null;
        for (Booking b : bookings) {
            if (b.getInstructorName().equalsIgnoreCase(instructorName) &&
                    b.getCourseCode().equalsIgnoreCase(courseCode) &&
                    b.getTimeSlot().getStartTime().equals(startTime)) {
                toRemove = b;
                break;
            }
        }

        if (toRemove != null) {
            bookings.remove(toRemove);
            JOptionPane.showMessageDialog(null,
                    "Booking for " + courseCode + " has been canceled.",
                    "Booking Canceled", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No matching booking found to cancel.",
                    "Cancellation Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Display all bookings in a GUI with background image
    public void displayAllBookings() {
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "No bookings available.",
                    "All Bookings", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Load background image
        ImageIcon backgroundIcon = new ImageIcon("classroutine.jpg");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setLayout(new BorderLayout()); // Use BorderLayout to position the table properly

        // Create the table to display all bookings
        String[] columns = {"Instructor", "Course Code", "Room", "Start Time", "End Time"};
        Object[][] data = new Object[bookings.size()][5];

        // Populate data for the table
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            data[i][0] = b.getInstructorName();
            data[i][1] = b.getCourseCode();
            data[i][2] = b.getRoomName();
            data[i][3] = b.getTimeSlot().getStartTime().toString();
            data[i][4] = b.getTimeSlot().getEndTime().toString();
        }

        // Create the JTable to display the data
        JTable bookingTable = new JTable(data, columns);
        bookingTable.setFillsViewportHeight(true); // Allow table to fill the window
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));

        // Customize JTable appearance (optional)
        bookingTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookingTable.setRowHeight(30); // Increase row height for readability
        bookingTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        // Add table to background panel
        backgroundLabel.add(scrollPane, BorderLayout.CENTER);

        // Create a new frame to display the bookings with the background image
        JFrame frame = new JFrame("All Bookings");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400); // Adjust frame size
        frame.setLocationRelativeTo(null); // Center the window
        frame.setContentPane(backgroundLabel); // Set the background image as the content pane
        frame.setVisible(true);
    }

    // Get all bookings for communication purposes
    public List<Booking> getBookings() {
        return bookings;
    }
}
