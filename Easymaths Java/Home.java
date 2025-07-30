import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Home extends JFrame {
    private final String username;

    public Home(String username) {
        this.username = username;
        setTitle("EasyMaths - Learning Hub");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with gradient background (same as Register page)
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // App title
        JLabel titleLabel = new JLabel("EasyMaths");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel usernameLabel = new JLabel(" " + username);
        usernameLabel.setFont(new Font("Calibri", Font.BOLD, 18));
        usernameLabel.setForeground(Color.WHITE);
        userPanel.add(usernameLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // Welcome panel
        JPanel welcomePanel = new JPanel();
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Calibri", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomePanel.add(welcomeLabel);

        // Feature buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Create feature buttons
        buttonsPanel.add(createFeatureButton(" Explore Topics", "topics"));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(createFeatureButton(" View Formulas", "formulas"));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(createFeatureButton(" Take a Quiz", "quiz"));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(createFeatureButton("️ Profile Settings", "profile"));
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonsPanel.add(createFeatureButton(" Logout", "logout"));

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Load and display last quiz score
        //loadQuizScore(welcomePanel);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createFeatureButton(String text, String action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Calibri", Font.PLAIN, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 60));
       // button.setBackground(new Color(255, 255, 255, 180));

       button.setForeground(new Color(255, 255, 255));
        button.setBackground(new Color(0, 51, 102));
       // button.setForeground(new Color(0, 51, 102));
      //  button.setForeground(new Color(32, 99, 155));

        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 200), 2));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 51, 102));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 51, 102));
            }
        });

        // Add action listener
        button.addActionListener(e -> handleButtonAction(action));

        return button;
    }

    private void handleButtonAction(String action) {
        switch (action) {
            case "topics":
                new Topics(username);
                dispose();
                break;
            case "formulas":
                new Formulas(username);
                dispose();
                break;
            case "quiz":
                new Quiz(username);
                dispose();
                break;
            case "profile":
                new Profile(username);
                dispose();
                break;
            case "logout":
                new Login();
                dispose();
                break;
        }
    }

//    private void loadQuizScore(JPanel welcomePanel) {
//        try {
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/easymaths", "root", "b12PW#97@je");
//            PreparedStatement pst = con.prepareStatement("SELECT last_score FROM users WHERE username = ?");
//            pst.setString(1, username);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                int score = rs.getInt("last_score");
//                if (score > 0) {
//                    JLabel scoreLabel = new JLabel("Last Quiz Score: " + score + "/10");
//                    scoreLabel.setFont(new Font("Calibri", Font.BOLD, 20));
//                    scoreLabel.setForeground(new Color(255, 215, 0)); // Gold color
//                    scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
//                    welcomePanel.add(scoreLabel);
//                    welcomePanel.revalidate();
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error loading quiz score: " + e.getMessage());
//        }
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home("testuser1"));
    }
}
