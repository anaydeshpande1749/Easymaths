import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.*;

public class Register extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;

    public Register() {
        setTitle("Register - EasyMaths");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(70, 130, 180);   // Steel Blue
                Color color2 = new Color(32, 99, 155);    // Darker Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);
        JButton backButton = new JButton("← Back to Login");
        backButton.setFont(new Font("Calibri", Font.PLAIN, 16));
        backButton.setBackground(new Color(255, 255, 255, 150));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new Login();
            dispose();
        });
        headerPanel.add(backButton);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel titleLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel userLabel = createLabel("Username:");
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = createTextField();
        formPanel.add(usernameField, gbc);

        // Email
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel emailLabel = createLabel("Email (optional):");
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createTextField();
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel passLabel = createLabel("Password:");
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = createPasswordField();
        formPanel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel confirmLabel = createLabel("Confirm Password:");
        formPanel.add(confirmLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField = createPasswordField();
        formPanel.add(confirmPasswordField, gbc);

        // Register Button
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        JButton registerButton = new JButton("Create Account");
        registerButton.setFont(new Font("Calibri", Font.BOLD, 20));
        registerButton.setBackground(new Color(46, 204, 113)); // Green
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(200, 45));
        registerButton.addActionListener(e -> registerUser());
        formPanel.add(registerButton, gbc);

        // Password requirements
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel passReqLabel = new JLabel("<html><center>Password must be at least 8 characters long<br>and include letters and numbers</center></html>");
        passReqLabel.setFont(new Font("Calibri", Font.ITALIC, 14));
        passReqLabel.setForeground(new Color(230, 230, 230));
        formPanel.add(passReqLabel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Calibri", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Calibri", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Calibri", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 35));
        return field;
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // Validation
        if (username.isEmpty()) {
            showError("Username cannot be empty");
            return;
        }

        if (username.length() < 4) {
            showError("Username must be at least 4 characters");
            return;
        }

        if (!email.isEmpty() && !isValidEmail(email)) {
            showError("Please enter a valid email address");
            return;
        }

        if (password.isEmpty()) {
            showError("Password cannot be empty");
            return;
        }

        if (!isValidPassword(password)) {
            showError("Password must be at least 8 characters and contain both letters and numbers");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        // Database connection
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/easymaths", "root", "b12PW#97@je");

            // Check if username exists
            String checkSql = "SELECT * FROM users WHERE username = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                showError("Username already taken");
                return;
            }

            // Insert new user
            String insertSql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(insertSql);
            pst.setString(1, username);
            pst.setString(2, password); // In real app, hash the password
            pst.setString(3, email.isEmpty() ? null : email);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful!\nYou can now log in to your account",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                new Login();
                dispose();
            } else {
                showError("Registration failed. Please try again.");
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        // At least 8 characters, contains letter and number
        return password.length() >= 8 &&
                password.matches(".*[a-zA-Z].*") &&
                password.matches(".*\\d.*");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Register());
    }
}