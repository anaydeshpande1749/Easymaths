import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;

public class Formulas extends JFrame {
    private String username;

    public Formulas(String username) {
        this.username = username;
        setTitle("EasyMaths - Formulas Explorer");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Main panel with light blue background
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));  // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);  // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;


        // Title Label
        JLabel titleLabel = new JLabel("Math Formulas Explorer", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(new Color(70, 130, 180));  // Steel blue
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subLabel = new JLabel("Select a topic to view formulas:", SwingConstants.CENTER);
        subLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
        panel.add(subLabel, gbc);

        // Topic Buttons
        gbc.gridwidth = 1;
        gbc.gridy++;

        // Define all topics
        String[] topics = {
                "Algebra", "Arithmetic", "Calculus", "Differential Equations",
                "Discrete Math", "Geometry", "Linear Algebra", "Logic",
                "Number Theory", "Probability", "Statistics", "Topology", "Trigonometry"
        };

        // Create buttons in 2-column layout
        for (int i = 0; i < topics.length; i++) {
            gbc.gridy = 2 + (i / 2);  // Calculate row position
            gbc.gridx = i % 2;         // Calculate column position

            // Create effectively final variable for lambda
            String topic = topics[i];

            JButton topicButton = createStyledButton(topic);
            topicButton.addActionListener(e -> showFormulasForTopic(topic));
            panel.add(topicButton, gbc);
        }

        // Back to Home button (at bottom)
        gbc.gridy = 2 + (int) Math.ceil(topics.length / 2.0) + 1;
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
        button.setBackground(new Color(70, 130, 180));  // Steel blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 40));
        return button;
    }

    private void showFormulasForTopic(String topic) {
        // Create dialog to display formulas
        JDialog formulaDialog = new JDialog(this, topic + " Formulas" , true);
        formulaDialog.setSize(800, 600);
        formulaDialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(new Color(240, 248, 255));  // Light blue

        // Title label
        JLabel titleLabel = new JLabel(topic + " Formulas," + " Double click to view more.", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));  // Steel blue
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Table to display formulas - ADD DERIVATION COLUMN TO MODEL
        String[] columnNames = {"Title", "Formula", "Derivation"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        JTable formulaTable = new JTable(model);
        formulaTable.setFont(new Font("Calibri", Font.PLAIN, 16));
        formulaTable.setRowHeight(30);
        formulaTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 18));
        formulaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // HIDE THE DERIVATION COLUMN (INDEX 2)
        TableColumnModel tcm = formulaTable.getColumnModel();
        tcm.removeColumn(tcm.getColumn(2));

       // formulaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

       // formulaTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Title
       // formulaTable.getColumnModel().getColumn(1).setPreferredWidth(50); // Formula

       // formulaTable.setPreferredScrollableViewportSize(new Dimension(1000, 300));

        //formulaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

       // TableColumn titleColumn = tcm.getColumn(0);
       // TableColumn formulaColumn = tcm.getColumn(1);

//        titleColumn.setPreferredWidth(200);
//        titleColumn.setMinWidth(150);
//        titleColumn.setMaxWidth(300);

//        formulaColumn.setPreferredWidth(600);  // Default width
//        formulaColumn.setMinWidth(400);     // Minimum allowed width

        formulaTable.setPreferredScrollableViewportSize(new Dimension(850, 300));

        JScrollPane scrollPane = new JScrollPane(formulaTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        formulaTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = formulaTable.getSelectedRow();
                    int col = formulaTable.getSelectedColumn();
                    Object value = formulaTable.getValueAt(row, col);
                    if (value != null) {
                        JOptionPane.showMessageDialog(null, value.toString(), "Cell Content", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });


        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Derivation panel
        JTextArea derivationArea = new JTextArea();
        derivationArea.setEditable(false);
        derivationArea.setLineWrap(true);
        derivationArea.setWrapStyleWord(true);
        derivationArea.setFont(new Font("Calibri", Font.PLAIN, 16));
        derivationArea.setBorder(BorderFactory.createTitledBorder("Derivation"));
        derivationArea.setBackground(new Color(240, 248, 255));  // Light blue

        JScrollPane derivationScroll = new JScrollPane(derivationArea);
        derivationScroll.setPreferredSize(new Dimension(0, 150));
        contentPanel.add(derivationScroll, BorderLayout.SOUTH);

        // Load formulas directly without database
        loadFormulasDirectly(topic, model);

        // Show derivation when a formula is selected - FIXED COLUMN INDEX
        formulaTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = formulaTable.getSelectedRow();
                if (viewRow >= 0) {
                    int modelRow = formulaTable.convertRowIndexToModel(viewRow);
                    String derivation = (String) model.getValueAt(modelRow, 2); // Get from model
                    derivationArea.setText(derivation != null ? derivation : "No derivation available");
                }
            }
        });

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Calibri", Font.BOLD, 16));
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> formulaDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));  // Light blue
        buttonPanel.add(closeButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        formulaDialog.add(contentPanel);
        formulaDialog.setVisible(true);
    }

    private void loadFormulasDirectly(String topic, DefaultTableModel model) {
        model.setRowCount(0); // Clear existing data

        switch (topic.toLowerCase()) {
            case "algebra":
                model.addRow(new Object[]{"Quadratic Formula",
                        "x = [-b ± √(b² - 4ac)] / (2a)",
                        "Derived from completing the square:\n" +
                                "1. Start with ax² + bx + c = 0\n" +
                                "2. Divide by a: x² + (b/a)x = -c/a\n" +
                                "3. Complete the square: x² + (b/a)x + (b/2a)² = (b/2a)² - c/a\n" +
                                "4. Simplify: (x + b/2a)² = (b² - 4ac)/4a²\n" +
                                "5. Solve: x = [-b ± √(b² - 4ac)] / (2a)"});

                model.addRow(new Object[]{"Distance Formula",
                        "d = √[(x₂ - x₁)² + (y₂ - y₁)²]",
                        "Derived from Pythagorean theorem:\n" +
                                "The distance between two points is the hypotenuse of a\n" +
                                "right triangle formed by their coordinate differences"});

                model.addRow(new Object[]{"Slope Formula",
                        "m = (y₂ - y₁)/(x₂ - x₁)",
                        "Definition of slope as rise over run"});

                model.addRow(new Object[]{"Midpoint Formula",
                        "M = [(x₁ + x₂)/2, (y₁ + y₂)/2]",
                        "Averages the x and y coordinates:\n" +
                                "Midpoint is the average position between two points"});

                model.addRow(new Object[]{"Factoring: Difference of Squares",
                        "a² - b² = (a + b)(a - b)",
                        "Expansion proof:\n" +
                                "(a + b)(a - b) = a² - ab + ab - b² = a² - b²"});

                model.addRow(new Object[]{"Factoring: Perfect Square Trinomial",
                        "a² + 2ab + b² = (a + b)²\na² - 2ab + b² = (a - b)²",
                        "Expansion of binomial squares:\n" +
                                "(a ± b)² = a² ± 2ab + b²"});

                model.addRow(new Object[]{"Binomial Theorem",
                        "(a + b)ⁿ = Σ [n!/(k!(n-k)!)] aⁿ⁻ᵏ bᵏ",
                        "Coefficients follow Pascal's Triangle:\n" +
                                "Each term represents a combination of a and b"});

                model.addRow(new Object[]{"Exponential Laws",
                        "aᵐ × aⁿ = aᵐ⁺ⁿ\naᵐ / aⁿ = aᵐ⁻ⁿ\n(aᵐ)ⁿ = aᵐⁿ",
                        "Derived from exponent definitions:\n" +
                                "Repeated multiplication properties"});

                model.addRow(new Object[]{"Logarithmic Multiplication Properties ",
                        "log(ab) = log(a) + log(b)\n",
                        "Inverse properties of exponents:\n" +
                                "Reflect exponential relationships"});

                model.addRow(new Object[]{"Logarithmic Division Properties",
                        "log(a/b) = log(a) - log(b)\n",
                        "Inverse properties of exponents:\n" +
                                "Reflect exponential relationships"});

                model.addRow(new Object[]{"Logarithmic Exponential Properties",
                        "log(aᵇ) = b log(a)",
                        "Inverse properties of exponents:\n" +
                                "Reflect exponential relationships"});

                model.addRow(new Object[]{"Arithmetic Series Sum",
                        "Sₙ = n/2 × (a₁ + aₙ)\nSₙ = n/2 × [2a₁ + (n-1)d]",
                        "Derived by pairing terms:\n" +
                                "Sum = (number of terms) × (average of first/last terms)"});

                model.addRow(new Object[]{"Geometric Series Sum",
                        "Sₙ = a(1 - rⁿ)/(1 - r)  (r ≠ 1)",
                        "Multiply sum by r and subtract:\n" +
                                "S - rS = a - arⁿ → S(1-r) = a(1-rⁿ)"});

                model.addRow(new Object[]{"Vertex of Parabola",
                        "Vertex = (-b/2a, f(-b/2a))",
                        "Completing the square in quadratic:\n" +
                                "y = a(x - h)² + k → vertex at (h,k)"});


                break;

            case "arithmetic":
                model.addRow(new Object[]{"Arithmetic Mean",
                        "mean = (x₁ + x₂ + ... + xₙ) / n",
                        "Sum of values divided by count of values"});

                model.addRow(new Object[]{"Compound Interest",
                        "A = P(1 + r/n)ⁿᵗ",
                        "A = final amount, P = principal, r = annual interest rate,\n" +
                                "n = compounding periods per year, t = time in years"});

                model.addRow(new Object[]{"Division Algorithm",
                        "a = bq + r, 0 ≤ r < |b|",
                        "Fundamental theorem of integer division:\n" +
                                "a = dividend, b = divisor, q = quotient, r = remainder"});

                model.addRow(new Object[]{"Percentage Change",
                        "Δ% = [(New - Original)/Original] × 100%",
                        "Measures relative change as a percentage"});

                model.addRow(new Object[]{"Simple Interest",
                        "I = P × r × t",
                        "I = interest, P = principal, r = annual rate, t = time in years"});

                model.addRow(new Object[]{"Speed Formula",
                        "speed = distance / time",
                        "Rate of change of distance with respect to time"});
                break;


            case "calculus":
                model.addRow(new Object[]{"Chain Rule",
                        "d/dx[f(g(x))] = f'(g(x)) · g'(x)",
                        "Derivative of composition is product of derivatives:\n" +
                                "Applies when functions are nested"});

                model.addRow(new Object[]{"Derivative of Exponential",
                        "d/dx[eˣ] = eˣ\nd/dx[aˣ] = aˣ ln a",
                        "Limit definition: limₕ→₀ (eˣ⁺ʰ - eˣ)/h = eˣ"});

                model.addRow(new Object[]{"Derivative of Logarithm",
                        "d/dx[ln x] = 1/x\nd/dx[logₐx] = 1/(x ln a)",
                        "Derived from inverse function theorem\n" +
                                "and properties of natural log"});

                model.addRow(new Object[]{"Derivative of Sine/Cosine",
                        "d/dx[sin x] = cos x\nd/dx[cos x] = -sin x",
                        "Using limit definitions and trig identities:\n" +
                                "limₕ→₀ (sin h)/h = 1, limₕ→₀ (cos h - 1)/h = 0"});

                model.addRow(new Object[]{"Fundamental Theorem of Calculus",
                        "∫ₐᵇ f(x) dx = F(b) - F(a)\nwhere F'(x) = f(x)",
                        "Connects differentiation and integration:\n" +
                                "Part 1: Definite integral of rate gives net change\n" +
                                "Part 2: Derivative of accumulation function is integrand"});

                model.addRow(new Object[]{"Integration by Parts",
                        "∫u dv = uv - ∫v du",
                        "Reverse of product rule:\n" +
                                "Derived from d(uv)/dx = u dv/dx + v du/dx"});

                model.addRow(new Object[]{"L'Hôpital's Rule",
                        "lim x→c f(x)/g(x) = lim x→c f'(x)/g'(x)\nwhen 0/0 or ∞/∞",
                        "Uses local linear approximation\n" +
                                "to resolve indeterminate forms"});

                model.addRow(new Object[]{"Power Rule (Derivative)",
                        "d/dx[xⁿ] = nxⁿ⁻¹",
                        "Limit definition:\n" +
                                "limₕ→₀ [(x+h)ⁿ - xⁿ]/h = nxⁿ⁻¹"});

                model.addRow(new Object[]{"Product Rule",
                        "d/dx[uv] = u'v + uv'",
                        "Limit definition of derivative:\n" +
                                "Expands [u(x+h)v(x+h) - u(x)v(x)]/h"});

                model.addRow(new Object[]{"Quotient Rule",
                        "d/dx[u/v] = (u'v - uv')/v²",
                        "Derived from product rule and chain rule\n" +
                                "by writing u/v = u · (v)⁻¹"});

                model.addRow(new Object[]{"Taylor Series",
                        "f(x) = Σ [f⁽ⁿ⁾(a)/n!] (x - a)ⁿ",
                        "Infinite polynomial approximation\n" +
                                "using derivatives at a point"});
                break;

            case "differential equations":
                model.addRow(new Object[]{"Bernoulli Equation",
                        "y' + P(x)y = Q(x)yⁿ",
                        "Substitute v = y¹⁻ⁿ to linearize:\n" +
                                "Transforms to linear equation in v"});

                model.addRow(new Object[]{"Euler's Method",
                        "yₙ₊₁ = yₙ + h·f(xₙ, yₙ)",
                        "Numerical approximation:\n" +
                                "Uses tangent line with step size h"});

                model.addRow(new Object[]{"Exact Differential Equation",
                        "M dx + N dy = 0 where ∂M/∂y = ∂N/∂x",
                        "Solution: F(x,y) = ∫M dx + ∫[N - ∂/∂y(∫M dx)] dy = C\n" +
                                "Requires test for exactness"});

                model.addRow(new Object[]{"First-Order Linear",
                        "dy/dx + P(x)y = Q(x)",
                        "Integrating factor: μ = e^{∫P dx}\n" +
                                "Solution: y = (1/μ) ∫μQ dx"});

                model.addRow(new Object[]{"Laplace Transform",
                        "L{f(t)} = ∫₀^∞ e⁻ˢᵗ f(t) dt",
                        "Converts DE to algebraic equation:\n" +
                                "Useful for solving linear DEs with given initial conditions"});

                model.addRow(new Object[]{"Logistic Equation",
                        "dP/dt = kP(1 - P/K)",
                        "Models population growth with carrying capacity K:\n" +
                                "Solution: P(t) = K/(1 + Ae⁻ᵏᵗ)"});

                model.addRow(new Object[]{
                        "Method of Undetermined Coefficients",
                        "For y″ + p·y′ + q·y = f(x):\n" +
                                "• If f(x) = e^(kx):yₚ(x) = A·e^(kx)\n" +
                                "• If f(x) = polynomial of degree n:yₚ(x) = Aₙxⁿ + ... + A₀\n" +
                                "• If f(x) = sin(ωx) or cos(ωx):yₚ(x) = A·cos(ωx) + B·sin(ωx)",
                        "Guess a particular solution form based on the forcing function f(x), substitute into the DE,\n" +
                                "and solve for the coefficients (A, B, …). For example, if f(x) = eˣ, try yₚ(x) = A·eˣ."
                });


                model.addRow(new Object[]{"Riccati Equation",
                        "y' = P(x) + Q(x)y + R(x)y²",
                        "Requires particular solution:\n" +
                                "Substitute y = yₚ + 1/v to linearize"});

                model.addRow(new Object[]{"Second-Order Linear Homogeneous",
                        "ay'' + by' + cy = 0",
                        "Characteristic equation: ar² + br + c = 0\n" +
                                "Solution depends on roots (real/complex/distinct)"});

                model.addRow(new Object[]{"Separable Equation",
                        "dy/dx = g(x)h(y)",
                        "Solution: ∫ dy/h(y) = ∫ g(x) dx\n" +
                                "Variables separated and integrated"});

                model.addRow(new Object[]{"Variation of Parameters",
                        "yₚ = -y₁∫(y₂r/W) dx + y₂∫(y₁r/W) dx\nW = Wronskian",
                        "General method for nonhomogeneous DEs:\n" +
                                "Uses fundamental solution set"});
                break;

            case "discrete math":
                model.addRow(new Object[]{"Binomial Coefficient",
                        "C(n,k) = n! / (k!(n - k)!)",
                        "Counts k-combinations:\n" +
                                "Number of ways to choose k items from n without repetition"});

                model.addRow(new Object[]{"Euler's Formula (Graph Theory)",
                        "v - e + f = 2\n(for connected planar graphs)",
                        "Relates vertices, edges, and faces:\n" +
                                "Fundamental relationship for planar graphs"});

                model.addRow(new Object[]{"Handshaking Lemma",
                        "Σ deg(v) = 2e",
                        "Sum of degrees is twice the number of edges:\n" +
                                "Each edge contributes to two vertices' degrees"});

                model.addRow(new Object[]{"Inclusion-Exclusion Principle",
                        "|A ∪ B| = |A| + |B| - |A ∩ B|\n" +
                                "|A ∪ B ∪ C| = |A| + |B| + |C| - |A∩B| - |A∩C| - |B∩C| + |A∩B∩C|",
                        "General formula: \n|∪Aᵢ| = Σ|Aᵢ| - Σ|Aᵢ∩Aⱼ| + Σ|Aᵢ∩Aⱼ∩Aₖ| - ... + (-1)ⁿ⁺¹|∩Aᵢ|"});

                model.addRow(new Object[]{"Mathematical Induction",
                        "Base case + Inductive step → P(n) is true ∀ n ≥ base",
                        "Proof technique to show a statement is true for all natural numbers"});

                model.addRow(new Object[]{"Modular Arithmetic",
                        "a ≡ b (mod m) iff m|(a-b)\n" +
                                "a mod m = remainder when a divided by m",
                        "Properties:\n" +
                                "1. (a + b) mod m = (a mod m + b mod m) mod m\n" +
                                "2. (a × b) mod m = (a mod m × b mod m) mod m"});

                model.addRow(new Object[]{"Permutation",
                        "P(n, r) = n! / (n - r)!",
                        "Number of ways to arrange r elements from n distinct items"});

                model.addRow(new Object[]{"Pigeonhole Principle",
                        "If n items in m containers,\nn > m ⇒ at least one container has ≥ ⌈n/m⌉ items",
                        "Fundamental counting principle:\n" +
                                "Used to prove existence of duplicates/overflows"});

                model.addRow(new Object[]{"Recurrence Relations (Linear)",
                        "aₙ = c₁aₙ₋₁ + c₂aₙ₋₂ + ... + cₖaₙ₋ₖ",
                        "Solved by characteristic equation:\n" +
                                "rᵏ - c₁rᵏ⁻¹ - c₂rᵏ⁻² - ... - cₖ = 0"});

                model.addRow(new Object[]{"Set Cardinality",
                        "|A × B| = |A| × |B|\n" +
                                "|P(A)| = 2^{|A|}",
                        "Cartesian product and power set sizes"});

                model.addRow(new Object[]{"Standard Deviation",
                        "σ = √[Σ(xᵢ - μ)² / N]",
                        "Measure of dispersion around the mean"});

                model.addRow(new Object[]{"Stirling Numbers (Second Kind)",
                        "S(n,k) = # of ways to partition n objects into k non-empty subsets",
                        "Recurrence: S(n,k) = k·S(n-1,k) + S(n-1,k-1)\n" +
                                "S(n,1) = 1, S(n,n) = 1"});

                model.addRow(new Object[]{"Summation Formulas",
                        "Σ i = n(n+1)/2\n" +
                                "Σ i² = n(n+1)(2n+1)/6\n" +
                                "Σ i³ = [n(n+1)/2]²",
                        "Proved by mathematical induction"});

                model.addRow(new Object[]{"Sum Rule",
                        "If a task can be done in m ways and another in n ways (non-overlapping), then total = m + n",
                        "Basic counting principle"});

                model.addRow(new Object[]{"Truth Tables",
                        "2ⁿ rows for n variables\n" +
                                "Systematic evaluation of logical expressions",
                        "Basis for propositional logic:\n" +
                                "AND, OR, NOT, IMPLIES, XOR, etc."});

                model.addRow(new Object[]{"Venn Diagram Formula (3 Sets)",
                        "|A ∪ B ∪ C| = |A| + |B| + |C| - |A ∩ B| - |B ∩ C| - |C ∩ A| + |A ∩ B ∩ C|",
                        "Used to calculate union size of three overlapping sets"});

                model.addRow(new Object[]{"Vertex Coloring",
                        "χ(G) = minimum colors needed\n" +
                                "so adjacent vertices have different colors",
                        "Four Color Theorem: χ(G) ≤ 4 for planar graphs"});
                break;


            case "geometry":
                model.addRow(new Object[]{
                        "Area of Circle",
                        "A = πr²",
                        "Derived by dividing the circle into infinitesimal triangles and summing their areas"
                });
                model.addRow(new Object[]{
                        "Area of Ellipse",
                        "A = πab",
                        "Where ‘a’ and ‘b’ are the semi-major and semi-minor axes respectively"
                });
                model.addRow(new Object[]{
                        "Area of Parallelogram",
                        "A = base × height",
                        "Calculated by multiplying the length of the base by the perpendicular height"
                });
                model.addRow(new Object[]{
                        "Area of Rectangle",
                        "A = length × width",
                        "Computed by multiplying the length by the width"
                });
                model.addRow(new Object[]{
                        "Area of Square",
                        "A = s²",
                        "The area is the square of the side length (s)"
                });
                model.addRow(new Object[]{
                        "Area of Triangle",
                        "A = ½ × base × height",
                        "Half the product of the base and the height of the triangle"
                });
                model.addRow(new Object[]{
                        "Area of Trapezoid",
                        "A = ½ × (a + b) × h",
                        "Half the sum of the lengths of the parallel sides (a and b) multiplied by the height (h)"
                });
                model.addRow(new Object[]{
                        "Circumference of Circle",
                        "C = 2πr",
                        "The distance around a circle is twice the product of π and its radius (r)"
                });
                model.addRow(new Object[]{
                        "Lateral Surface Area of Cone",
                        "LSA = πrl",
                        "Calculated as π times the radius (r) times the slant height (l) of the cone"
                });
                model.addRow(new Object[]{
                        "Perimeter of Rectangle",
                        "P = 2(l + w)",
                        "Twice the sum of the length (l) and width (w) of the rectangle"
                });
                model.addRow(new Object[]{
                        "Perimeter of Square",
                        "P = 4s",
                        "Equal to four times the side length (s) of the square"
                });
                model.addRow(new Object[]{
                        "Pythagorean Theorem",
                        "a² + b² = c²",
                        "Proof by rearrangement: four right triangles can form two squares, one with side (a+b) and another with side c"
                });
                model.addRow(new Object[]{
                        "Surface Area of Cube",
                        "SA = 6a²",
                        "The total area is six times the area of one square face (where a is the side length)"
                });
                model.addRow(new Object[]{
                        "Surface Area of Cylinder",
                        "SA = 2πr(h + r)",
                        "Sum of the lateral surface area (2πrh) and the areas of the two circular bases (2πr²)"
                });
                model.addRow(new Object[]{
                        "Surface Area of Sphere",
                        "SA = 4πr²",
                        "The total surface area of a sphere is four times π times the radius squared"
                });
                model.addRow(new Object[]{
                        "Volume of Cone",
                        "V = ⅓πr²h",
                        "One third the product of the base area (πr²) and the height (h)"
                });
                model.addRow(new Object[]{
                        "Volume of Cube",
                        "V = a³",
                        "The volume is the cube of the side length (a)"
                });
                model.addRow(new Object[]{
                        "Volume of Cylinder",
                        "V = πr²h",
                        "Computed as the product of the area of the circular base (πr²) and the height (h)"
                });
                model.addRow(new Object[]{
                        "Volume of Sphere",
                        "V = ⁴⁄₃πr³",
                        "Calculated as four-thirds times π times the cube of the radius (r)"
                });
                break;


            case "linear algebra":
                model.addRow(new Object[]{
                        "Cross Product",
                        "a × b = (a₂b₃ - a₃b₂, a₃b₁ - a₁b₃, a₁b₂ - a₂b₁)",
                        "Produces a vector perpendicular to both a and b"
                });
                model.addRow(new Object[]{
                        "Determinant of 2x2 Matrix",
                        "det(A) = ad - bc",
                        "For matrix A = [[a, b], [c, d]], this value indicates the invertibility of A"
                });
                model.addRow(new Object[]{
                        "Dot Product of Vectors",
                        "a · b = a₁b₁ + a₂b₂ + … + aₙbₙ",
                        "Measures the similarity of two vectors and helps determine the angle between them"
                });
                model.addRow(new Object[]{
                        "Eigenvalue Equation",
                        "Av = λv  (with det(A - λI) = 0)",
                        "Provides a way to determine eigenvalues (λ) and eigenvectors (v) for matrix A"
                });
                model.addRow(new Object[]{
                        "Inverse of 2x2 Matrix",
                        "A⁻¹ = 1/(ad - bc) × [[d, -b], [-c, a]]",
                        "Gives the inverse of a 2x2 matrix, valid when the determinant is non-zero"
                });
                model.addRow(new Object[]{
                        "Matrix Multiplication",
                        "(AB)₍ij₎ = Σₖ A₍ik₎B₍kj₎",
                        "Defines the product of two matrices by summing products of row and column elements"
                });
                model.addRow(new Object[]{
                        "Norm of a Vector",
                        "||v|| = √(v₁² + v₂² + … + vₙ²)",
                        "Computes the length or magnitude of vector v"
                });
                model.addRow(new Object[]{
                        "Projection of a Vector",
                        "proj₍u₎v = (v · u / ||u||²) u",
                        "Represents the component of vector v in the direction of vector u"
                });
                model.addRow(new Object[]{
                        "Singular Value Decomposition",
                        "A = UΣVᵀ",
                        "Factorizes matrix A into orthogonal matrices U and V, and a diagonal matrix Σ"
                });
                model.addRow(new Object[]{
                        "Standard Deviation",
                        "σ = √[Σ(xᵢ - μ)² / N]",
                        "Measures dispersion of data points around the mean (μ)"
                });
                break;

            case "logic":
                model.addRow(new Object[]{
                        "Absorption Law",
                        "P ∨ (P ∧ Q) ≡ P;  P ∧ (P ∨ Q) ≡ P",
                        "Simplifies logical expressions by absorbing redundant components"
                });
                model.addRow(new Object[]{
                        "Biconditional Equivalence",
                        "P ↔ Q ≡ (P ∧ Q) ∨ (¬P ∧ ¬Q)",
                        "Represents that two propositions are equivalent if both are true or both are false"
                });
                model.addRow(new Object[]{
                        "Contrapositive",
                        "P → Q ≡ ¬Q → ¬P",
                        "A conditional statement is equivalent to its contrapositive"
                });
                model.addRow(new Object[]{
                        "De Morgan's Law (Conjunction)",
                        "¬(P ∧ Q) ≡ ¬P ∨ ¬Q",
                        "The negation of a conjunction equals the disjunction of the negations"
                });
                model.addRow(new Object[]{
                        "De Morgan's Law (Disjunction)",
                        "¬(P ∨ Q) ≡ ¬P ∧ ¬Q",
                        "The negation of a disjunction equals the conjunction of the negations"
                });
                model.addRow(new Object[]{
                        "Distributive Law (Conjunction over Disjunction)",
                        "P ∧ (Q ∨ R) ≡ (P ∧ Q) ∨ (P ∧ R)",
                        "Conjunction distributes over disjunction"
                });
                model.addRow(new Object[]{
                        "Distributive Law (Disjunction over Conjunction)",
                        "P ∨ (Q ∧ R) ≡ (P ∨ Q) ∧ (P ∨ R)",
                        "Disjunction distributes over conjunction"
                });
                model.addRow(new Object[]{
                        "Double Negation Law",
                        "¬(¬P) ≡ P",
                        "Eliminates double negatives to restore the original proposition"
                });
                model.addRow(new Object[]{
                        "Implication Equivalence",
                        "P → Q ≡ ¬P ∨ Q",
                        "Expresses a conditional in terms of disjunction (material implication)"
                });
                model.addRow(new Object[]{
                        "Law of Excluded Middle",
                        "P ∨ ¬P",
                        "States that any proposition is either true or false with no middle state"
                });
                model.addRow(new Object[]{
                        "Modus Ponens",
                        "From P → Q and P, infer Q",
                        "If the conditional holds and the antecedent is true, then the consequent must be true"
                });
                model.addRow(new Object[]{
                        "Modus Tollens",
                        "From P → Q and ¬Q, infer ¬P",
                        "If the conditional holds and the consequent is false, then the antecedent must be false"
                });
                break;


            case "number theory":
                model.addRow(new Object[]{
                        "Chinese Remainder Theorem",
                        "x ≡ Σ (aᵢ Mᵢ yᵢ) (mod M)",
                        "Solves systems of congruences with pairwise coprime moduli, where M = ∏ mᵢ, Mᵢ = M/mᵢ, and yᵢ is the modular inverse of Mᵢ modulo mᵢ"
                });
                model.addRow(new Object[]{
                        "Euclid's Formula (Pythagorean Triples)",
                        "a = m² - n²,   b = 2mn,   c = m² + n²",
                        "Generates a primitive Pythagorean triple for integers m and n with m > n > 0"
                });
                model.addRow(new Object[]{
                        "Euclidean Algorithm",
                        "gcd(a, b) = gcd(b, a mod b)",
                        "Recursively computes the greatest common divisor (GCD) of two integers"
                });
                model.addRow(new Object[]{
                        "Euler's Criterion",
                        "a^((p-1)/2) ≡ (a/p) (mod p)",
                        "Determines quadratic residuosity for an integer a modulo an odd prime p, with (a/p) denoting the Legendre symbol"
                });
                model.addRow(new Object[]{
                        "Euler's Totient Function",
                        "φ(n) = n ∏ (1 - 1/p)  (for all primes p dividing n)",
                        "Calculates the number of positive integers up to n that are coprime with n"
                });
                model.addRow(new Object[]{
                        "Fermat's Little Theorem",
                        "a^(p-1) ≡ 1 (mod p)",
                        "For a prime p and integer a not divisible by p, ensures that a^(p-1) is congruent to 1 modulo p"
                });
                model.addRow(new Object[]{
                        "Fundamental Theorem of Arithmetic",
                        "n = ∏ pᵢ^(αᵢ)",
                        "Every integer greater than 1 can be uniquely expressed as a product of prime powers"
                });
                model.addRow(new Object[]{
                        "Legendre's Formula",
                        "Exponent of p in n! = Σ₍i=1₎^∞ ⌊ n/p^i ⌋",
                        "Calculates the exponent of a prime p in the prime factorization of n!"
                });
                model.addRow(new Object[]{
                        "Möbius Inversion Formula",
                        "f(n) = ∑₍d|n₎ g(d)  ↔  g(n) = ∑₍d|n₎ μ(n/d) f(d)",
                        "Relates two arithmetic functions f and g via summation over the divisors of n using the Möbius function μ"
                });
                model.addRow(new Object[]{
                        "Prime Factorization Theorem",
                        "n = p₁^(α₁) × p₂^(α₂) × ... × p_k^(α_k)",
                        "Asserts that every integer greater than 1 has a unique prime factorization, up to the order of factors"
                });
                model.addRow(new Object[]{
                        "Wilson's Theorem",
                        "(p-1)! ≡ -1 (mod p)",
                        "States that a positive integer p > 1 is prime if and only if the factorial of (p-1) is congruent to -1 modulo p"
                });
                break;


            case "probability":
                model.addRow(new Object[]{
                        "Addition Rule",
                        "P(A ∪ B) = P(A) + P(B) - P(A ∩ B)",
                        "Calculates the probability that at least one of two events occurs"
                });
                model.addRow(new Object[]{
                        "Bayes' Theorem",
                        "P(A|B) = (P(B|A) × P(A)) / P(B)",
                        "Updates the probability of event A based on new evidence B"
                });
                model.addRow(new Object[]{
                        "Binomial Probability",
                        "P(X = k) = C(n, k) × p^k × (1 - p)^(n - k)",
                        "Determines the probability of exactly k successes in n independent Bernoulli trials"
                });
                model.addRow(new Object[]{
                        "Complement Rule",
                        "P(A') = 1 - P(A)",
                        "Gives the probability of the complement (non-occurrence) of event A"
                });
                model.addRow(new Object[]{
                        "Conditional Probability",
                        "P(A|B) = P(A ∩ B) / P(B)",
                        "Calculates the likelihood of event A given that event B has occurred"
                });
                model.addRow(new Object[]{
                        "Expected Value",
                        "E[X] = Σ x · P(x)",
                        "Represents the weighted average of all possible values of a random variable"
                });
                model.addRow(new Object[]{
                        "Law of Total Probability",
                        "P(A) = Σ P(A|Bᵢ) · P(Bᵢ)",
                        "Breaks down the probability of an event using a partition of the sample space"
                });
                model.addRow(new Object[]{
                        "Multiplication Rule",
                        "P(A ∩ B) = P(A) × P(B)  or  P(A ∩ B) = P(A) × P(B|A)",
                        "Computes the probability of the intersection of two events; for independent events, it simplifies to P(A) × P(B)"
                });
                model.addRow(new Object[]{
                        "Probability of an Event",
                        "P(E) = n(E) / n(S)",
                        "Defines probability as the ratio of favorable outcomes to the total number of outcomes"
                });
                model.addRow(new Object[]{
                        "Standard Deviation",
                        "σ = √[Σ(xᵢ - μ)² / N]",
                        "Measures dispersion around the mean in a probability distribution"
                });
                model.addRow(new Object[]{
                        "Variance",
                        "Var(X) = Σ[(x - μ)² · P(x)] or Var(X) = E[(X - E[X])²]",
                        "Quantifies the spread of a random variable's values around its expected value"
                });
                break;



            case "statistics":
                model.addRow(new Object[]{
                        "Arithmetic Mean",
                        "x̄ = Σx / n",
                        "Calculates the average of a dataset by summing all values and dividing by the number of observations"
                });
                model.addRow(new Object[]{
                        "Coefficient of Variation",
                        "CV = (σ / μ) × 100",
                        "Expresses variability relative to the mean by measuring the ratio of the standard deviation to the mean as a percentage"
                });
                model.addRow(new Object[]{
                        "Confidence Interval for Mean",
                        "CI = x̄ ± z × (σ / √n)",
                        "Estimates the range in which the true population mean lies with a specified level of confidence"
                });
                model.addRow(new Object[]{
                        "Geometric Mean",
                        "GM = (Πxᵢ)^(1/n)",
                        "Useful for averaging multiplicative rates; computed as the nth root of the product of n values"
                });
                model.addRow(new Object[]{
                        "Harmonic Mean",
                        "HM = n / Σ(1/xᵢ)",
                        "Often used when averaging rates, it is the reciprocal of the arithmetic mean of reciprocals"
                });
                model.addRow(new Object[]{
                        "Interquartile Range",
                        "IQR = Q₃ - Q₁",
                        "Measures the spread of the middle 50% of the data, providing a robust indication of variability"
                });
                model.addRow(new Object[]{
                        "Median",
                        "Middle value in ordered data",
                        "Splits the dataset into two equal halves and represents the central point when data are sorted"
                });
                model.addRow(new Object[]{
                        "Mode",
                        "Most frequently occurring value",
                        "Identifies the value or values that appear most often within a dataset"
                });
                model.addRow(new Object[]{
                        "Population Standard Deviation",
                        "σ = √[Σ(x - μ)² / N]",
                        "Measures dispersion for an entire population by taking the square root of the average squared deviations from the mean"
                });
                model.addRow(new Object[]{
                        "Population Variance",
                        "σ² = Σ(x - μ)² / N",
                        "Calculates the average squared deviation from the population mean"
                });
                model.addRow(new Object[]{
                        "Range",
                        "Range = Max - Min",
                        "Determines the spread of the dataset by subtracting the smallest value from the largest"
                });
                model.addRow(new Object[]{
                        "Sample Standard Deviation",
                        "s = √[Σ(x - x̄)² / (n - 1)]",
                        "Estimates dispersion for a sample with a correction (n - 1) to account for degrees of freedom"
                });
                model.addRow(new Object[]{
                        "Sample Variance",
                        "s² = Σ(x - x̄)² / (n - 1)",
                        "Represents the average squared deviation from the sample mean, adjusted for sample size"
                });
                model.addRow(new Object[]{
                        "Z-Score",
                        "z = (x - μ) / σ",
                        "Indicates how many standard deviations an observation is from the population mean"
                });
                break;


            case "topology":
                model.addRow(new Object[]{
                        "Betti Number Formula",
                        "χ = Σ (-1)^i b_i",
                        "Expresses the Euler characteristic (χ) as the alternating sum of Betti numbers (b_i), which are the ranks of the homology groups"
                });
                model.addRow(new Object[]{
                        "Euler Characteristic (Polyhedron)",
                        "χ = V - E + F",
                        "Relates the number of vertices (V), edges (E), and faces (F) for convex polyhedra; a fundamental invariant in combinatorial topology"
                });
                model.addRow(new Object[]{
                        "Euler Characteristic (Surface)",
                        "χ = 2 - 2g",
                        "Gives the Euler characteristic for a closed orientable surface in terms of its genus (g), representing the number of holes"
                });
                model.addRow(new Object[]{
                        "Fundamental Group of S¹",
                        "π₁(S¹) = ℤ",
                        "States that the set of homotopy classes of loops in a circle is isomorphic to the integers, with the integer representing the winding number"
                });
                model.addRow(new Object[]{
                        "Gauss-Bonnet Theorem",
                        "∫ₘ K dA = 2πχ(M)",
                        "Connects the total Gaussian curvature (K) of a compact 2D surface M to its Euler characteristic (χ(M)), bridging geometry and topology"
                });
                model.addRow(new Object[]{
                        "Poincaré Duality",
                        "Hⁱ(M) ≅ Hₙ₋₱(M)",
                        "Establishes an isomorphism between the kth cohomology and (n−k)th homology groups of an n-dimensional closed oriented manifold, revealing a deep symmetry"
                });
                model.addRow(new Object[]{
                        "Topological Degree",
                        "deg(f) = Σ sign(det(Df(x)))",
                        "Defines the degree of a continuous map f by summing the signs of the Jacobian determinants at preimage points of a regular value, indicating the map’s covering behavior"
                });
                break;


            case "trigonometry":
                model.addRow(new Object[]{
                        "Co-function Identities",
                        "sin(90° − θ) = cosθ\ncos(90° − θ) = sinθ\ntan(90° − θ) = cotθ",
                        "Express relationships between trigonometric functions of complementary angles"
                });
                model.addRow(new Object[]{
                        "Double Angle Formulas",
                        "sin2θ = 2 sinθ cosθ\ncos2θ = cos²θ − sin²θ\ntan2θ = (2 tanθ) / (1 − tan²θ)",
                        "Express functions of double angles in terms of single-angle functions"
                });
                model.addRow(new Object[]{
                        "Half-Angle Formulas",
                        "sin²(θ/2) = (1 − cosθ) / 2\ncos²(θ/2) = (1 + cosθ) / 2\ntan(θ/2) = (1 − cosθ) / sinθ",
                        "Derive the trigonometric functions for half angles using the cosine of the full angle"
                });
                model.addRow(new Object[]{
                        "Law of Cosines",
                        "c² = a² + b² − 2ab cosC",
                        "Generalizes the Pythagorean theorem to any triangle by relating its sides and the cosine of one angle"
                });
                model.addRow(new Object[]{
                        "Law of Sines",
                        "a/sinA = b/sinB = c/sinC",
                        "Relates the sides of a triangle to the sines of its opposite angles, useful for solving oblique triangles"
                });
                model.addRow(new Object[]{
                        "Law of Tangents",
                        "(a − b)/(a + b) = tan[(A − B)/2] / tan[(A + B)/2]",
                        "Relates the differences and sums of two sides of a triangle to the tangents of half the difference and half the sum of the corresponding angles"
                });
                model.addRow(new Object[]{
                        "Product-to-Sum Formulas",
                        "sinA sinB = ½[cos(A − B) − cos(A + B)]\ncosA cosB = ½[cos(A − B) + cos(A + B)]\nsinA cosB = ½[sin(A + B) + sin(A − B)]",
                        "Convert products of sines and cosines into sums or differences, easing integration and simplification tasks"
                });
                model.addRow(new Object[]{
                        "Pythagorean Identity",
                        "sin²θ + cos²θ = 1",
                        "Follows from the unit circle where x² + y² = 1 with x = cosθ and y = sinθ"
                });
                model.addRow(new Object[]{
                        "Reciprocal Identities",
                        "cscθ = 1/sinθ\nsecθ = 1/cosθ\ncotθ = 1/tanθ",
                        "Define the trigonometric functions as reciprocals of sine, cosine, and tangent respectively"
                });
                model.addRow(new Object[]{
                        "Sum and Difference Formulas",
                        "sin(α ± β) = sinα cosβ ± cosα sinβ\ncos(α ± β) = cosα cosβ ∓ sinα sinβ\ntan(α ± β) = (tanα ± tanβ) / (1 ∓ tanα tanβ)",
                        "Allow calculation of sine, cosine, and tangent for sums or differences of two angles"
                });
                model.addRow(new Object[]{
                        "Sum-to-Product Formulas",
                        "sinA + sinB = 2 sin((A + B)/2) cos((A − B)/2)\nsinA − sinB = 2 cos((A + B)/2) sin((A − B)/2)\ncosA + cosB = 2 cos((A + B)/2) cos((A − B)/2)\ncosA − cosB = −2 sin((A + B)/2) sin((A − B)/2)",
                        "Transform sums or differences of sines and cosines into products to simplify expressions"
                });
                model.addRow(new Object[]{
                        "Tangent Sum Formula",
                        "tan(α + β) = (tanα + tanβ) / (1 − tanα tanβ)",
                        "Specifically computes the tangent of the sum of two angles using the tangent of each individual angle"
                });
                break;




            // Add cases for other topics similarly
            default:
                model.addRow(new Object[]{"Formula 1", "Expression 1", "Derivation 1"});
                model.addRow(new Object[]{"Formula 2", "Expression 2", "Derivation 2"});
                break;
        }
    }

//
//    private void loadFormulasForTopic(String topic, DefaultTableModel model) {
//        Connection conn = null;
//        PreparedStatement pst = null;
//        ResultSet rs = null;
//
//        try {
//            conn = DBUtil.getConnection();
//
//            // Get topic ID
//            int topicId = -1;
//            String sql = "SELECT id FROM topics WHERE name = ?";
//            pst = conn.prepareStatement(sql);
//            pst.setString(1, topic);
//            rs = pst.executeQuery();
//            if (rs.next()) {
//                topicId = rs.getInt("id");
//            }
//
//            if (topicId == -1) {
//                JOptionPane.showMessageDialog(this, "Topic not found in database");
//                return;
//            }
//
//            // Get formulas for this topic
//            sql = "SELECT title, formula, derivation FROM formulas WHERE topic_id = ?";
//            pst = conn.prepareStatement(sql);
//            pst.setInt(1, topicId);
//            rs = pst.executeQuery();
//
//            model.setRowCount(0); // Clear existing data
//
//            while (rs.next()) {
//                String title = rs.getString("title");
//                String formula = rs.getString("formula");
//                String derivation = rs.getString("derivation");
//                model.addRow(new Object[]{title, formula, derivation});
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
//        } finally {
//            DBUtil.close(conn, pst, rs);
//        }
//    }

    public static void main(String[] args) {
        new Formulas("testuser");
    }
}
