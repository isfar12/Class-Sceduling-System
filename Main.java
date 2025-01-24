import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import java.sql.Connection;


public class Main extends JFrame {
    private static Map<String, User> users = new HashMap<>();
    private static Schedule schedule = new Schedule();
    private static CommentSection commentSection = new CommentSection();
    private JTextField keyField;
    private JButton loginButton;
    private JButton registerButton;

    public Main() {
        setTitle("Class Scheduling System");
        setSize(400, 300);  // Increased size to accommodate new buttons
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeUsers();
        initComponents();
    }

    // Initialize GUI components
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));  // Increased rows to add the new message
    
        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Class Scheduling System", JLabel.CENTER);
        panel.add(welcomeLabel);
    
        // Message for students
        JLabel studentMessageLabel = new JLabel("Students can login using the key: student123", JLabel.CENTER);
        studentMessageLabel.setForeground(Color.BLUE); // Optional: Make the message stand out
        panel.add(studentMessageLabel);
    
        // Key input field
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Enter your secret key: "));
        keyField = new JTextField(15);
        inputPanel.add(keyField);
        panel.add(inputPanel);
    
        // Login button
        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        panel.add(loginButton);
    
        // Register button for instructor
        registerButton = new JButton("Register as Instructor");
        registerButton.addActionListener(new RegisterActionListener());
        panel.add(registerButton);
    
        add(panel);
    }
    
    // Initialize users with their keys and roles
    private static void initializeUsers() {
        // Instructors and others (Fetch from database the first time)
        if (users.isEmpty()) {
            fetchUsersFromDatabase();
        }

        // Class Representatives (CR)
        users.put("arif123", new ClassRepresentative("Arif", "arif123"));
        // All students share the same key for login
        users.put("student123", new Student("Student", "student123"));
    }

    // Fetch users from database for the first time
    private static void fetchUsersFromDatabase() {
        Connector connector = new Connector();
        try (Connection connection = connector.connect()) {
            String query = "SELECT * FROM instructors";
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String key = resultSet.getString("key");
                String courses = resultSet.getString("courses");
                List<String> courseList = Arrays.asList(courses.split(","));

                Instructor instructor = new Instructor(name, key, courseList);
                users.put(key, instructor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login button listener
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String key = keyField.getText();

            if (users.containsKey(key)) {
                User user = users.get(key);
                JOptionPane.showMessageDialog(Main.this, "Login successful! Welcome, " + user.getName() + ".");
                user.displayDashboard(schedule, commentSection);
            } else {
                JOptionPane.showMessageDialog(Main.this, "Invalid key. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                keyField.setText("");
            }
        }
    }

    // Register button listener
    private class RegisterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Show registration dialog
            String name = JOptionPane.showInputDialog(Main.this, "Enter Instructor's Name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String key = JOptionPane.showInputDialog(Main.this, "Enter Instructor's Key:");
            if (key == null || key.trim().isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Key is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String courses = JOptionPane.showInputDialog(Main.this, "Enter Courses (comma-separated):");
            if (courses == null || courses.trim().isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Courses are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add instructor to the database and the users map
            try (Connection connection = new Connector().connect()) {
                String insertQuery = "INSERT INTO instructors (name, `key`, courses) VALUES (?, ?, ?)";
                var preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, key);
                preparedStatement.setString(3, courses);
                preparedStatement.executeUpdate();

                // Add instructor to the users map
                List<String> courseList = Arrays.asList(courses.split(","));
                Instructor newInstructor = new Instructor(name, key, courseList);
                users.put(key, newInstructor);

                JOptionPane.showMessageDialog(Main.this, "Instructor registered successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Main.this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Main method to launch the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}
