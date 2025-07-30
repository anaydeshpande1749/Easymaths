
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Profile extends JFrame {
    private String username;
    private int userId;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;

    public Profile(String username) {
        this.username = username;
        setTitle("EasyMaths - User Profile");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fetch user details from database
        fetchUserDetails();

        // Main panel with light blue background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("User Profile Settings", SwingConstants.CENTER);

       // JLabel titleLabel1 = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));

     //   titleLabel1.setFont(new Font("Georgia", Font.BOLD, 24));

        titleLabel.setForeground(new Color(70, 130, 180));
       panel.add(titleLabel, gbc);

       // panel.add(titleLabel1, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        panel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Calibri", Font.PLAIN, 16));
        usernameField.setText(username);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Calibri", Font.PLAIN, 16));
        panel.add(passwordField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        panel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Calibri", Font.PLAIN, 16));
        panel.add(emailField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton applyButton = new JButton("Apply Changes");
        applyButton.setFont(new Font("Calibri", Font.BOLD, 16));
        applyButton.setBackground(new Color(70, 130, 180));
        applyButton.setForeground(Color.WHITE);
        applyButton.addActionListener(e -> applyChanges());
        //applyButton.addActionListener(e -> dispose());
        buttonPanel.add(applyButton);

        JButton cancelButton = new JButton("Back to Home");
        cancelButton.setFont(new Font("Calibri", Font.BOLD, 16));
        cancelButton.setBackground(new Color(200, 50, 50));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dispose());
        cancelButton.addActionListener(e -> new Home(username));

        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, gbc);

        add(panel);
        setVisible(true);
    }

    private void fetchUserDetails() {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id, email FROM users WHERE username = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                emailField = new JTextField(rs.getString("email"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            DBUtil.close(conn, pst, rs);
        }
    }

    private void applyChanges() {
        String newUsername = usernameField.getText().trim();
        String newPassword = new String(passwordField.getPassword()).trim();
        String newEmail = emailField.getText().trim();

        // Validation
        if (newUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return;
        }

        if (!newPassword.isEmpty() && newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!");
            return;
        }

        if (!isValidEmail(newEmail)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!");
            return;
        }

        // Update database
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DBUtil.getConnection();

            // Check if username already exists
            if (!newUsername.equals(username)) {
                String checkSql = "SELECT id FROM users WHERE username = ?";
                pst = conn.prepareStatement(checkSql);
                pst.setString(1, newUsername);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Username already exists! Choose a different username.");
                    return;
                }
                pst.close();
            }

            // Update user details
            String updateSql;
            if (!newPassword.isEmpty()) {
                updateSql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";
                pst = conn.prepareStatement(updateSql);
                pst.setString(1, newUsername);
                pst.setString(2, newPassword); // In real app, hash the password
                pst.setString(3, newEmail);
                pst.setInt(4, userId);
            } else {
                updateSql = "UPDATE users SET username = ?, email = ? WHERE id = ?";
                pst = conn.prepareStatement(updateSql);
                pst.setString(1, newUsername);
                pst.setString(2, newEmail);
                pst.setInt(3, userId);
            }

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully! Please login with your new credentials.");
                new Login();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update profile. Please try again.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            DBUtil.close(conn, pst, null);
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static void main(String[] args) {
        // For testing
        new Profile("testuser6");
    }
}
