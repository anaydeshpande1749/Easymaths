
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProbabilityQuiz extends JFrame {
    private String username;
    private int userId;
    private String topic;
    private int lastScore;
    private int topicId;

    public ProbabilityQuiz(String username, int userId, String topic) {
        this.username = username;
        this.userId = userId;
        this.topic = topic;
        this.topicId = getTopicId(topic);
        setTitle("EasyMaths - " + topic + " Quiz");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Fetch last score from database
        lastScore = getLastScore();

        // Main panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel(topic + " Quiz", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        panel.add(titleLabel, gbc);

        // Last Score
        gbc.gridy++;
        JLabel scoreLabel = new JLabel("Your Last Score: " + lastScore + "/10", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        panel.add(scoreLabel, gbc);

        // Start Quiz Button
        gbc.gridy++;
        JButton startButton = createStyledButton("Start Quiz");
        startButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Start the " + topic + " quiz? You'll have 60 minutes to complete 10 questions.",
                    "Start Quiz",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                new QuizSession(username, userId, topic);
                dispose();
            }
        });
        panel.add(startButton, gbc);

        // Back to Quiz Selection Button
        gbc.gridy++;
        JButton backButton = createStyledButton("Back to Quiz Selection");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new Quiz(username);
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

    private int getLastScore() {
        int topicId = getTopicId(topic); // Add this helper method

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT score FROM user_scores WHERE user_id = ? AND topic_id = ? ORDER BY last_attempt DESC LIMIT 1";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, userId);
            pst.setInt(2, topicId);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("score");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, rs);
        }
        return 0; // Return 0 if no score exists
    }

    // Add this helper method to get topic ID
    private int getTopicId(String topic) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id FROM topics WHERE name = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, topic);
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
}
