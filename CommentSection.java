import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommentSection {
    private List<String> comments;

    public CommentSection() {
        comments = new ArrayList<>();
    }

    // Interact with the comment section
    public void interact(User user) {
        while (true) {
            String[] options = {"View Comments", "Add a Comment", "Back to Dashboard"};
            int choice = JOptionPane.showOptionDialog(null,
                    "--- Comment Section ---\nSelect an option:",
                    "Comment Section",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0:
                    viewComments();
                    break;
                case 1:
                    addComment(user);
                    break;
                case 2:
                    return;  // Exit to dashboard
                default:
                    // In case of an invalid or closed dialog
                    return;
            }
        }
    }

    // Display all comments in a scrollable text area
    private void viewComments() {
        if (comments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No comments available.", "Comments", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a text area to display comments
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        StringBuilder commentText = new StringBuilder();
        for (String comment : comments) {
            commentText.append(comment).append("\n");
        }
        textArea.setText(commentText.toString());

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Show the comments in a dialog
        JOptionPane.showMessageDialog(null, scrollPane, "Comments", JOptionPane.INFORMATION_MESSAGE);
    }

    // Add a new comment from the user
    private void addComment(User user) {
        // Create an input dialog for entering the comment
        String comment = JOptionPane.showInputDialog(null, "Enter your comment:", "Add Comment", JOptionPane.PLAIN_MESSAGE);

        // If comment is not null or empty, add it to the list
        if (comment != null && !comment.trim().isEmpty()) {
            String formattedComment = user.getName() + ": " + comment.trim();
            comments.add(formattedComment);
            JOptionPane.showMessageDialog(null, "Comment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Comment cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
