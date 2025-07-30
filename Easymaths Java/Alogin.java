import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Alogin extends JFrame {
    private JTextField adminUserField;
    private JPasswordField adminPassField;

    public Alogin() {
        setTitle("Admin Login - EasyMaths");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Admin Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Admin Username:"), gbc);

        gbc.gridx = 1;
        adminUserField = new JTextField(15);
        panel.add(adminUserField, gbc);

        // Admin Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Admin Password:"), gbc);

        gbc.gridx = 1;
        adminPassField = new JPasswordField(15);
        panel.add(adminPassField, gbc);

        // Admin Login Button (Blue)
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.setFont(new Font("Calibri", Font.BOLD, 18));
        adminLoginButton.setBackground(new Color(70, 130, 180));
        adminLoginButton.setForeground(Color.WHITE);
        panel.add(adminLoginButton, gbc);

        // Back to Login Button (Red)
        gbc.gridy = 4;
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Calibri", Font.BOLD, 18));
        backButton.setBackground(new Color(220, 60, 60));
        backButton.setForeground(Color.WHITE);
        panel.add(backButton, gbc);

        add(panel);

        // Action Listeners
        adminLoginButton.addActionListener(e -> authenticateAdmin());
        backButton.addActionListener(e -> {
            new Login();
            dispose();
        });

        setVisible(true);
    }

    private void authenticateAdmin() {
        String username = adminUserField.getText().trim();
        String password = new String(adminPassField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM admin WHERE BINARY username = ? AND BINARY password = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, password);
            rs = pst.executeQuery();

            if (rs.next()) {
                new ADashboard(username).setVisible(true);  // Open admin dashboard
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            DBUtil.close(conn, pst, rs);
        }
    }
    public static void main(String[] args) {
        new Alogin();
    }
}