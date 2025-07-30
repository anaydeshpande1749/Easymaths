import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Topics extends JFrame {

    public Topics(String username) {
        setTitle("EasyMaths - Topics");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));  // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);  // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subLabel = new JLabel("Choose a topic to start practicing:", SwingConstants.CENTER);
        subLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        panel.add(subLabel, gbc);

        // Topic Buttons
        gbc.gridwidth = 1;
        gbc.gridy++;

        gbc.gridx = 0;
        JButton algebraBtn = createStyledButton("Algebra");
        panel.add(algebraBtn, gbc);

        gbc.gridx = 1;
        JButton arithmeticBtn = createStyledButton("Arithmetic");
        panel.add(arithmeticBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton CalculusBtn = createStyledButton("Calculus");
        panel.add(CalculusBtn, gbc);

        gbc.gridx = 1;
        JButton Differential_EquationsBtn = createStyledButton("Differential Equations");
        panel.add(Differential_EquationsBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton Discrete_MathBtn = createStyledButton("Discrete Math");
        panel.add(Discrete_MathBtn, gbc);

        gbc.gridx = 1;
        JButton geometryBtn = createStyledButton("Geometry");
        panel.add(geometryBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton Linear_AlgebraBtn = createStyledButton("Linear Algebra");
        panel.add(Linear_AlgebraBtn, gbc);

        gbc.gridx = 1;
        JButton LogicBtn = createStyledButton("Logic");
        panel.add(LogicBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton Number_TheoryBtn = createStyledButton("Number Theory");
        panel.add(Number_TheoryBtn, gbc);

        gbc.gridx = 1;
        JButton ProbabilityBtn = createStyledButton("Probability");
        panel.add(ProbabilityBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton StatisticsBtn = createStyledButton("Statistics");
        panel.add(StatisticsBtn, gbc);

        gbc.gridx = 1;
        JButton TopologyBtn = createStyledButton("Topology");
        panel.add(TopologyBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton TrigonometryBtn = createStyledButton("Trigonometry");
        panel.add(TrigonometryBtn, gbc);


        gbc.gridx = 1;
        JButton logoutBtn = createStyledButton("Back to Home");
        logoutBtn.setBackground(Color.RED);
        logoutBtn.setForeground(Color.WHITE);
        panel.add(logoutBtn, gbc);

        // Add panel to frame
        add(panel);

        // Action Listeners
        algebraBtn.addActionListener(e -> {
            new SelectinTopics(username, "Algebra");
            dispose();
        });

        geometryBtn.addActionListener(e -> {
            new SelectinTopics(username, "Geometry");
            dispose();
        });

        TrigonometryBtn.addActionListener(e -> {
            new SelectinTopics(username, "Trigonometry" );
            dispose();
        });

        CalculusBtn.addActionListener(e -> {
            new SelectinTopics(username, "Calculus");
            dispose();
        });

        arithmeticBtn.addActionListener(e -> {
            new SelectinTopics(username, "Arithmetic");
            dispose();
        });

        StatisticsBtn.addActionListener(e -> {
            new SelectinTopics(username, "Statistics");
            dispose();
        });

        ProbabilityBtn.addActionListener(e -> {
            new SelectinTopics(username, "Probability");
            dispose();
        });

        Number_TheoryBtn.addActionListener(e -> {
            new SelectinTopics(username, "Number_Theory");
            dispose();
        });

        Linear_AlgebraBtn.addActionListener(e -> {
            new SelectinTopics(username, "Linear_Algebra");
            dispose();
        });

        Differential_EquationsBtn.addActionListener(e -> {
            new SelectinTopics(username, "Differential_Equations");
            dispose();
        });

        Discrete_MathBtn.addActionListener(e -> {
            new SelectinTopics(username, "Discrete_Math");
            dispose();
        });

        TopologyBtn.addActionListener(e -> {
            new SelectinTopics(username,  "Topology");
            dispose();
                });

        //LogicBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Arithmetic selected!"));
        LogicBtn.addActionListener(e -> {
            new SelectinTopics(username, "Logic");
            dispose();
        });

        logoutBtn.addActionListener(e -> {
            new Home(username);  // Return to Home screen
            dispose();
        });

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Calibri", Font.BOLD, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    public static void main(String[] args) {
        new Topics("testuser1");  // For testing purposes
    }
}
