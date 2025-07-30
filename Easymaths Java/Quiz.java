
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Quiz extends JFrame {
    private String username;
    private int userId;

    public Quiz(String username) {
        this.username = username;
        setTitle("EasyMaths - Quiz Selection");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fetch user ID from database
        userId = getUserId(username);

        // Main panel with light blue background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Math Quiz Selection", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Welcome message
        gbc.gridy++;
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        panel.add(welcomeLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subLabel = new JLabel("Choose a topic to start practicing:", SwingConstants.CENTER);
        subLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        panel.add(subLabel, gbc);

        // Topic Buttons
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Get topics from database
        List<String> topics = getTopicsFromDB();

        // Create buttons in 2-column layout
        for (int i = 0; i < topics.size(); i++) {
            gbc.gridy = 4 + (i / 2);  // Start from row 4
            gbc.gridx = i % 2;         // Column position

            String topic = topics.get(i);
            JButton topicButton = createStyledButton(topic);
            topicButton.addActionListener(e -> openTopicQuiz(topic));
            panel.add(topicButton, gbc);
        }

        // Back to Home button
        gbc.gridy = 4 + (int) Math.ceil(topics.size() / 2.0) + 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton backButton = createStyledButton("Back to Home");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new Home(username);
            dispose();
        });
        panel.add(backButton, gbc);

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Calibri", Font.BOLD, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 40));
        return button;
    }

    private int getUserId(String username) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id FROM users WHERE username = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, rs);
        }
        return -1;
    }

    private List<String> getTopicsFromDB() {
        List<String> topics = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT name FROM topics";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                topics.add(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, rs);
        }
        return topics;
    }

    private void openTopicQuiz(String topic) {
        // Open the topic-specific quiz window
        switch (topic) {

            case "Algebra":
                new AlgebraQuiz(username, userId, topic);
                break;

            case "Geometry":
                new GeometryQuiz(username, userId, topic);
                break;

            case "Arithmetic":
                new ArithmeticQuiz(username, userId, topic);
                break;

            case "Calculus":
                new CalculusQuiz(username, userId, topic);
                break;

            case "Differential Equations":
                new DifferentialEquationsQuiz(username, userId, topic);
                break;

            case "Discrete Math":
                new DiscreteMathQuiz(username, userId, topic);
                break;

            case "Linear Algebra":
                new LinearAlgebraQuiz(username, userId, topic);
                break;

            case "Logic":
                new LogicQuiz(username, userId, topic);
                break;

            case "Number Theory":
                new NumberTheoryQuiz(username, userId, topic);
                break;

            case "Probability":
                new ProbabilityQuiz(username, userId, topic);
                break;

            case "Statistics":
                new StatisticsQuiz(username, userId, topic);
                break;

            case "Topology":
                new TopologyQuiz(username, userId, topic);
                break;

            case "Trigonometry":
                new TrigonometryQuiz(username, userId, topic);
                break;



            default:
                JOptionPane.showMessageDialog(this, "Quiz for " + topic + " is under development");
        }
        dispose();
    }

    public static void main(String[] args) {
        new Quiz("testuser1");
    }
}
