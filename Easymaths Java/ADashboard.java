import javax.swing.*;
import java.awt.*;

public class ADashboard extends JFrame {

    //private String username;
    private String adminUsername;

    public ADashboard(String adminUsername) {
        this.adminUsername = adminUsername;

        setTitle("Admin Dashboard - EasyMaths");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));  // Same background as login
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + adminUsername + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(70, 130, 180));  // Steel blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(welcomeLabel, gbc);

        // Dashboard title
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        gbc.gridy = 1;
        panel.add(titleLabel, gbc);

        // Spacer
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(30), gbc);

        // Check Leaderboard Button (Blue)
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JButton leaderboardButton = new JButton("Check Leaderboard");
        styleButton(leaderboardButton, new Color(70, 130, 180));
        panel.add(leaderboardButton, gbc);

        // Check Database Button (Blue)
        gbc.gridy = 3;
        gbc.gridx = 1;
        JButton databaseButton = new JButton("Check Database");
        styleButton(databaseButton, new Color(70, 130, 180));
        panel.add(databaseButton, gbc);

        // Back to Admin Login Button (Red)
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton backButton = new JButton("Back to Admin Login");
        styleButton(backButton, new Color(220, 60, 60));  // Red
        panel.add(backButton, gbc);

        // Action Listeners
        leaderboardButton.addActionListener(e -> {
            new Leaderboard(adminUsername);
            dispose();
        });

        databaseButton.addActionListener(e -> {
            new DatabaseViewer(adminUsername);
            dispose();
        });

        backButton.addActionListener(e -> {
            new Alogin();
            dispose();
        });

        add(panel);
        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Calibri", Font.BOLD, 22));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 50));
    }

//    public static void main(String[] args) {
//        new ADashboard("admin");
//    }

    public static void main(String[] args) {
        new ADashboard("admin123"); // Replace with desired admin username
    }

}
