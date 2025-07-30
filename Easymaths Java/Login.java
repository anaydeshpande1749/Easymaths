import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        setTitle("Login - EasyMaths");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("EasyMaths Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Calibri", Font.BOLD, 18));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        panel.add(loginButton, gbc);

        // Login Button
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginButton1 = new JButton("Admin Login");
        loginButton1.setFont(new Font("Calibri", Font.BOLD, 18));
        loginButton1.setBackground(new Color(70, 130, 180));
        loginButton1.setForeground(Color.WHITE);
        panel.add(loginButton1, gbc);
        loginButton1.addActionListener(e -> dispose());
        loginButton1.addActionListener(e -> new Alogin());

        // Register Link
        gbc.gridy = 4;
        JLabel registerLabel = new JLabel("Don't have an account? Register");
        registerLabel.setForeground(Color.BLUE.darker());
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(registerLabel, gbc);

        add(panel);

        // Action Listeners
        loginButton.addActionListener(e -> authenticateUser());
        registerLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new Register();
                dispose();
            }
        });

        setVisible(true);
    }

    private void authenticateUser() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        System.out.println("Attempting login with:");
        System.out.println("Username: [" + username + "]");
        System.out.println("Password: [" + password + "]");


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;




        try {
            conn = DBUtil.getConnection();
            //String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            String sql = "SELECT * FROM users WHERE BINARY username = ? AND BINARY password = ?";

            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password); // In real app, hash password
            rs = pst.executeQuery();

            if (rs.next()) {
                new Home(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            DBUtil.close(conn, pst, rs);
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}


//import javax.swing.*;
//
//public class Login extends JFrame {
//    public Login() {
//        setTitle("Login - EasyMaths");
//        setSize(400, 300);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setVisible(true);
//    }
//}
