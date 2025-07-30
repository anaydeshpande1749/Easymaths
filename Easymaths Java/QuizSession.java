
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class QuizSession extends JFrame {
    private String username;
    private int userId;
    private String topic;
    private int topicId;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private Timer timer;
    private int timeLeft = 3600; // 60 minutes in seconds

    // Components
    private JLabel timerLabel;
    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton prevButton;
    private JButton nextButton;
    private JButton submitButton;
    private JPanel navigationPanel;

    public QuizSession(String username, int userId, String topic) {
        this.username = username;
        this.userId = userId;
        this.topic = topic;

        setTitle("EasyMaths - " + topic + " Quiz");
        setSize(900, 700);
        setLocationRelativeTo(null);
       // setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


        // Add window listener to handle closing properly
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        // Fetch topic ID
        topicId = getTopicId(topic);

        // Fetch questions from database
        fetchQuestions();

        // Initialize UI
        initUI();

        // Start timer
        startTimer();

        setVisible(true);
    }

    // Add this inside the QuizSession class
    private void handleWindowClosing() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        int option = JOptionPane.showConfirmDialog(

                //this,
                QuizSession.this,
                "Are you sure you want to quit?\nYour quiz will be submitted automatically.",
                "Confirm Quit",

                JOptionPane.YES_NO_OPTION
        );



        if (option == JOptionPane.YES_OPTION) {
            submitQuiz();
            dispose();
        }
        else {
           startTimer(); // Restart timer if user cancels

        }
    }



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

    private void fetchQuestions() {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
          //  String sql = "SELECT * FROM questions WHERE topic_id = ?";
            String sql = "SELECT * FROM questions WHERE topic_id = ? ORDER BY RAND() LIMIT 10";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, topicId);
            rs = pst.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("id"));
                question.setQuestionText(rs.getString("question"));
                question.setOptions(new String[]{
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4")
                });
                question.setCorrectOption(rs.getInt("correct_option"));
                questions.add(question);
            }

            // Shuffle and select 10 questions
            Collections.shuffle(questions);
            if (questions.size() > 10) {
                questions = questions.subList(0, 10);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, rs);
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Timer at top
        timerLabel = new JLabel("Time left: 60:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        timerLabel.setForeground(Color.RED);
        mainPanel.add(timerLabel, BorderLayout.NORTH);

        // Question area
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 248, 255));

        questionLabel = new JLabel("Question will appear here");
        questionLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        questionLabel.setBorder(new EmptyBorder(10, 10, 20, 10));
        centerPanel.add(questionLabel, BorderLayout.NORTH);

        // Options
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBackground(new Color(240, 248, 255));
        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setBackground(new Color(240, 248, 255));
            optionButtons[i].setFont(new Font("Calibri", Font.PLAIN, 16));
            int optionIndex = i;
            optionButtons[i].addActionListener(e -> {
                questions.get(currentQuestionIndex).setSelectedOption(optionIndex);
                updateNavigationButtons();
            });
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Navigation panel at bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 248, 255));

        // Question navigation buttons (1 to 10)
        navigationPanel = new JPanel(new GridLayout(1, 10, 5, 5));
        navigationPanel.setBackground(new Color(240, 248, 255));

        for (int i = 0; i < questions.size(); i++) {
            JButton navButton = new JButton(String.valueOf(i+1));
            navButton.setFont(new Font("Calibri", Font.PLAIN, 16));
            int index = i;
            navButton.addActionListener(e -> showQuestion(index));
            navigationPanel.add(navButton);
        }

        bottomPanel.add(navigationPanel, BorderLayout.NORTH);

        // Prev, Next, Submit buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));

        prevButton = new JButton("Previous");
        prevButton.setFont(new Font("Calibri", Font.BOLD, 16));
        prevButton.addActionListener(e -> showQuestion(currentQuestionIndex - 1));
        buttonPanel.add(prevButton);

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Calibri", Font.BOLD, 16));
        nextButton.addActionListener(e -> showQuestion(currentQuestionIndex + 1));
        buttonPanel.add(nextButton);

        submitButton = new JButton("Submit Quiz");
        submitButton.setFont(new Font("Calibri", Font.BOLD, 16));
        submitButton.setBackground(new Color(50, 150, 50));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> submitQuiz());
        buttonPanel.add(submitButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Show first question
        showQuestion(0);
    }

    private void showQuestion(int index) {
        if (index < 0 || index >= questions.size()) {
            return;
        }

        currentQuestionIndex = index;
        Question question = questions.get(index);

        // Update question label
        questionLabel.setText((index+1) + ". " + question.getQuestionText());

        // Update options
        optionGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(question.getOptions()[i]);
            optionButtons[i].setSelected(i == question.getSelectedOption());
        }

        // Update navigation buttons
        updateNavigationButtons();

        // Update prev/next button states
        prevButton.setEnabled(index > 0);
        nextButton.setEnabled(index < questions.size() - 1);
    }

    private void updateNavigationButtons() {
        Component[] buttons = navigationPanel.getComponents();
        for (int i = 0; i < buttons.length; i++) {
            JButton button = (JButton) buttons[i];
            if (questions.get(i).getSelectedOption() != -1) {
                button.setBackground(Color.GREEN);
            } else {
                button.setBackground(null);
            }
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    timer.cancel();
                    // Time's up, submit automatically
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(QuizSession.this, "Time's up! Submitting your quiz.");
                        submitQuiz();
                    });
                    return;
                }

                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                SwingUtilities.invokeLater(() -> {
                    timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));
                });
            }
        }, 1000, 1000);
    }

    private void submitQuiz() {
        // Stop the timer
        if (timer != null) {
            timer.cancel();

        }

        // Calculate score
        score = 0;
        for (Question question : questions) {
            if (question.getSelectedOption()+1 == question.getCorrectOption()) {
                score++;
            }
        }

        // Update database
       // updateScore();

        //newly added for user_scores
        // Update user_scores table
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DBUtil.getConnection();

            // Check if score exists for this user+topic
            String checkSql = "SELECT id FROM user_scores WHERE user_id = ? AND topic_id = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setInt(1, userId);
            pst.setInt(2, topicId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Update existing score
                int recordId = rs.getInt("id");
                String updateSql = "UPDATE user_scores SET score = ?, last_attempt = CURRENT_TIMESTAMP WHERE id = ?";
                pst = conn.prepareStatement(updateSql);
                pst.setInt(1, score);
                pst.setInt(2, recordId);
                pst.executeUpdate();
            } else {
                // Insert new score
                String insertSql = "INSERT INTO user_scores (user_id, topic_id, score) VALUES (?, ?, ?)";
                pst = conn.prepareStatement(insertSql);
                pst.setInt(1, userId);
                pst.setInt(2, topicId);
                pst.setInt(3, score);
                pst.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, null);
        }

        // Show result
        JOptionPane.showMessageDialog(this,
                "Quiz submitted!\nYour score: " + score + "/" + questions.size(),
                "Quiz Results",
                JOptionPane.INFORMATION_MESSAGE);

        // Go back to topic quiz
//        new AlgebraQuiz(username, userId, topic);
//        dispose();
        openTopicQuiz(topic);
        timer = null; // Clear timer reference to allow garbage collection
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

    private void updateScore() {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE users SET last_score = ? WHERE id = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, score);
            pst.setInt(2, userId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            DBUtil.close(conn, pst, null);
        }
    }

    // Inner class to represent a question
    private class Question {
        private int id;
        private String questionText;
        private String[] options;
        private int correctOption;
        private int selectedOption = -1; // -1 means not answered

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String[] getOptions() { return options; }
        public void setOptions(String[] options) { this.options = options; }
        public int getCorrectOption() { return correctOption; }
        public void setCorrectOption(int correctOption) { this.correctOption = correctOption; }
        public int getSelectedOption() { return selectedOption; }
        public void setSelectedOption(int selectedOption) { this.selectedOption = selectedOption; }
    }
}
