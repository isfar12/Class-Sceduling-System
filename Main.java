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
    private JButton loginButton, registerButton;
    private JLabel background;

    public Main() {
        setTitle("Class Scheduling System");
        setSize(600, 400); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Using absolute layout for background image

        // Set Background Image
        ImageIcon bgImage = new ImageIcon("images/background.jpg"); // Path to image
        Image img = bgImage.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
        background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, 600, 400);
        add(background);

        // Add components on top of background
        initializeUsers();
        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        // Panel for content
        JPanel panel = new JPanel();
        panel.setBounds(50, 50, 500, 300);
        panel.setOpaque(false); // Make panel transparent
        panel.setLayout(new GridLayout(5, 1));

        JLabel welcomeLabel = new JLabel("Welcome to the Class Scheduling System", JLabel.CENTER);
        welcomeLabel.setForeground(Color.WHITE); // Make text readable over the image
        panel.add(welcomeLabel);

        JLabel studentMessageLabel = new JLabel("Students can login using the key: student123", JLabel.CENTER);
        studentMessageLabel.setForeground(Color.YELLOW);
        panel.add(studentMessageLabel);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout());
        JLabel keyLabel = new JLabel("Enter your secret key:");
        keyLabel.setForeground(Color.WHITE);
        inputPanel.add(keyLabel);
    
        keyField = new JTextField(15);
        inputPanel.add(keyField);
        panel.add(inputPanel);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());
        panel.add(loginButton);

        registerButton = new JButton("Register as Instructor");
        registerButton.addActionListener(new RegisterActionListener());
        panel.add(registerButton);

        background.add(panel);
    }

    private static void initializeUsers() {
        if (users.isEmpty()) {
            fetchUsersFromDatabase();
        }
        users.put("arif123", new ClassRepresentative("Arif", "arif123"));
        users.put("student123", new Student("Student", "student123"));
    }

    private static void fetchUsersFromDatabase() {
        Connector connector = new Connector();
        try (Connection connection = connector.connect()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM instructors");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String key = resultSet.getString("key");
                String courses = resultSet.getString("courses");
                List<String> courseList = Arrays.asList(courses.split(","));
                users.put(key, new Instructor(name, key, courseList));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoginActionListener implements ActionListener {
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

    private class RegisterActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
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

            try (Connection connection = new Connector().connect()) {
                var preparedStatement = connection.prepareStatement("INSERT INTO instructors (name, `key`, courses) VALUES (?, ?, ?)");
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, key);
                preparedStatement.setString(3, courses);
                preparedStatement.executeUpdate();

                users.put(key, new Instructor(name, key, Arrays.asList(courses.split(","))));
                JOptionPane.showMessageDialog(Main.this, "Instructor registered successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Main.this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
