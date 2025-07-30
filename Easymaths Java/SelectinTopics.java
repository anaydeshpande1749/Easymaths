import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SelectinTopics extends JFrame {
    private String username;
    private String topic;

    public SelectinTopics(String username, String topic) {
        this.username = username;
        this.topic = topic;

        setTitle("EasyMaths - " + topic);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));  // Light blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);  // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        JLabel titleLabel = new JLabel(topic, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Subtitle
        gbc.gridy++;
        JLabel subLabel = new JLabel("Choose what you want to learn:", SwingConstants.CENTER);
        subLabel.setFont(new Font("Calibri", Font.PLAIN, 20));
        panel.add(subLabel, gbc);

        // Option Buttons
        gbc.gridwidth = 1;
        gbc.gridy++;

        // History Button
        gbc.gridx = 0;
        JButton historyBtn = createStyledButton("History of " + topic);
        panel.add(historyBtn, gbc);

        // Derivations Button
        gbc.gridx = 1;
        JButton derivationsBtn = createStyledButton("Derivations in " + topic);
        panel.add(derivationsBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton sumsBtn = createStyledButton("Sums in " + topic);
        panel.add(sumsBtn, gbc);

        // Back to Topics button
        gbc.gridx = 1;
        JButton backBtn = createStyledButton("Back to Topics");
        backBtn.setBackground(new Color(220, 20, 60)); // Crimson color
        backBtn.setForeground(Color.WHITE);
        panel.add(backBtn, gbc);

        // Add panel to frame
        add(panel);

        // Action Listeners
        historyBtn.addActionListener(e -> showHistoryContent());
        derivationsBtn.addActionListener(e -> showDerivationsContent());
        sumsBtn.addActionListener(e -> showSumsContent());
        backBtn.addActionListener(e -> {
            new Topics(username);
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
        button.setPreferredSize(new Dimension(350, 50));
        return button;
    }

    private void showHistoryContent() {
        // Create a scrollable text area with formatted history content
        JTextArea historyText = new JTextArea(15, 50);
        historyText.setEditable(false);
        historyText.setFont(new Font("Calibri", Font.PLAIN, 18));
        historyText.setLineWrap(true);
        historyText.setWrapStyleWord(true);

        // Add topic-specific history content
        historyText.setText(getTopicHistory(topic));

        // Display in a scroll pane
        JScrollPane scrollPane = new JScrollPane(historyText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, scrollPane,
                "History of " + topic, JOptionPane.PLAIN_MESSAGE);
    }

    private void showDerivationsContent() {
        JTextArea derivationsText = new JTextArea(15, 50);
        derivationsText.setEditable(false);
        derivationsText.setFont(new Font("Calibri", Font.PLAIN, 18));
        derivationsText.setLineWrap(true);
        derivationsText.setWrapStyleWord(true);

        derivationsText.setText(getTopicDerivations(topic));

        JScrollPane scrollPane = new JScrollPane(derivationsText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Derivations in " + topic, JOptionPane.PLAIN_MESSAGE);
    }

    private void showSumsContent() {
        JTextArea sumsText = new JTextArea(15, 50);
        sumsText.setEditable(false);
        sumsText.setFont(new Font("Calibri", Font.PLAIN, 18));
        sumsText.setLineWrap(true);
        sumsText.setWrapStyleWord(true);

        sumsText.setText(getTopicSums(topic));

        JScrollPane scrollPane = new JScrollPane(sumsText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Sums in " + topic, JOptionPane.PLAIN_MESSAGE);
    }

    private String getTopicHistory(String topic) {
        // Add actual historical content for each topic here
        switch (topic.toLowerCase()) {

            case "algebra":
                return "History of Algebra:\n\n" +
                        "Algebra is one of the broad areas of mathematics. " +
                        "The word algebra comes from the Arabic word الجبر (al-jabr), " +
                        "which means 'reunion of broken parts'. The origins of algebra " +
                        "can be traced to the ancient Babylonians, who developed an " +
                        "advanced arithmetical system with which they were able to do " +
                        "calculations in an algorithmic fashion.\n\n" +
                        "Key Developments:\n" +
                        "- 3rd century AD: Diophantus writes Arithmetica\n" +
                        "- 9th century: Al-Khwarizmi writes The Compendious Book on Calculation by Completion and Balancing\n" +
                        "- 16th century: Development of symbolic algebra";

            case "geometry":
                return "History of Geometry:\n\n" +
                        "Geometry (from the Ancient Greek: γεωμετρία; geo- 'earth', " +
                        "-metron 'measurement') arose as the field of knowledge dealing " +
                        "with spatial relationships. Geometry was one of the two fields " +
                        "of pre-modern mathematics, the other being the study of numbers.\n\n" +
                        "Key Developments:\n" +
                        "- 300 BC: Euclid writes Elements\n" +
                        "- 17th century: Development of coordinate geometry by Descartes\n" +
                        "- 19th century: Development of non-Euclidean geometries";

            case "arithmetic":
                return "History of Arithmetic:\n\n" +
                        "Arithmetic is the oldest and most fundamental branch of mathematics. " +
                        "It deals with numbers and the basic operations: addition, subtraction, " +
                        "multiplication, and division. The earliest evidence of arithmetic " +
                        "dates back to the Sumerians and Babylonians around 2000-3000 BC.\n\n" +
                        "Key Developments:\n" +
                        "- 3000 BC: Babylonians develop base-60 number system\n" +
                        "- 7th century AD: Indian mathematicians develop the decimal system\n" +
                        "- 13th century: Fibonacci introduces Arabic numerals to Europe";

            case "calculus":
                return "History of Calculus:\n\n" +
                        "Calculus is the mathematical study of continuous change, in the " +
                        "same way that geometry is the study of shape and algebra is the " +
                        "study of generalizations of arithmetic operations.\n\n" +
                        "Key Developments:\n" +
                        "- 17th century: Newton and Leibniz independently develop calculus\n" +
                        "- 18th century: Euler contributes significantly to calculus\n" +
                        "- 19th century: Rigorous foundations laid by Cauchy and Weierstrass";

            case "differential equations":
                return "History of Differential Equations:\n\n" +
                        "Differential equations arose with the invention of calculus by Newton " +
                        "and Leibniz. They describe how quantities change and are fundamental " +
                        "in expressing physical laws.\n\n" +
                        "Key Developments:\n" +
                        "- 17th century: Newton uses differential equations in Principia Mathematica\n" +
                        "- 18th century: Euler develops methods for solving differential equations\n" +
                        "- 19th century: Fourier series developed for solving partial differential equations";

            case "discrete math":
                return "History of Discrete Mathematics:\n\n" +
                        "Discrete mathematics deals with distinct and separate values rather " +
                        "than continuous values. It includes topics such as graph theory, " +
                        "combinatorics, and logic.\n\n" +
                        "Key Developments:\n" +
                        "- 18th century: Euler solves the Seven Bridges of Königsberg problem\n" +
                        "- 19th century: George Boole develops Boolean algebra\n" +
                        "- 20th century: Emergence as a distinct field with computer science";

            case "linear algebra":
                return "History of Linear Algebra:\n\n" +
                        "Linear algebra concerns vector spaces and linear mappings between them. " +
                        "It includes the study of lines, planes, and subspaces, but is also " +
                        "concerned with properties common to all vector spaces.\n\n" +
                        "Key Developments:\n" +
                        "- 1843: Hamilton discovers quaternions\n" +
                        "- 1844: Grassmann publishes his theory of linear extensions\n" +
                        "- 20th century: Formalization of vector spaces and matrices";

            case "logic":
                return "History of Mathematical Logic:\n\n" +
                        "Mathematical logic is a subfield of mathematics exploring the " +
                        "applications of formal logic to mathematics. It emerged in the " +
                        "mid-19th century as a distinct field.\n\n" +
                        "Key Developments:\n" +
                        "- 4th century BC: Aristotle develops formal logic\n" +
                        "- 17th century: Leibniz conceives of a universal logical calculus\n" +
                        "- 20th century: Gödel's incompleteness theorems";

            case "number theory":
                return "History of Number Theory:\n\n" +
                        "Number theory is the branch of mathematics devoted primarily to the " +
                        "study of integers and integer-valued functions. It was historically " +
                        "known as arithmetic.\n\n" +
                        "Key Developments:\n" +
                        "- 3rd century AD: Diophantus writes Arithmetica\n" +
                        "- 17th century: Fermat's Last Theorem proposed\n" +
                        "- 19th century: Gauss publishes Disquisitiones Arithmeticae";

            case "probability":
                return "History of Probability:\n\n" +
                        "Probability is the branch of mathematics concerning numerical " +
                        "descriptions of how likely an event is to occur, or how likely it " +
                        "is that a proposition is true.\n\n" +
                        "Key Developments:\n" +
                        "- 16th century: Cardano writes first book on probability\n" +
                        "- 17th century: Pascal and Fermat correspond about gambling problems\n" +
                        "- 20th century: Kolmogorov axiomatizes probability theory";

            case "statistics":
                return "History of Statistics:\n\n" +
                        "Statistics is the discipline that concerns the collection, organization, " +
                        "analysis, interpretation, and presentation of data.\n\n" +
                        "Key Developments:\n" +
                        "- 17th century: John Graunt analyzes mortality records\n" +
                        "- 19th century: Gauss develops the method of least squares\n" +
                        "- 20th century: Fisher founds modern statistical inference";

            case "topology":
                return "History of Topology:\n\n" +
                        "Topology is the mathematical study of the properties that are preserved " +
                        "through deformations, twistings, and stretchings of objects.\n\n" +
                        "Key Developments:\n" +
                        "- 1736: Euler solves the Seven Bridges of Königsberg problem\n" +
                        "- 19th century: Möbius describes the Möbius strip\n" +
                        "- 20th century: Development of algebraic topology";

            case "trigonometry":
                return "History of Trigonometry:\n\n" +
                        "Trigonometry is a branch of mathematics that studies relationships " +
                        "between side lengths and angles of triangles.\n\n" +
                        "Key Developments:\n" +
                        "- 2nd century BC: Hipparchus compiles trigonometric tables\n" +
                        "- 5th century AD: Indian mathematicians develop modern definitions\n" +
                        "- 15th century: Regiomontanus writes first European trigonometry treatise";

            default:
                return "History content for " + topic + " is not available yet.";
        }
    }

    private String getTopicDerivations(String topic) {
        // Add actual derivations for each topic here
        switch (topic.toLowerCase()) {

            case "algebra":
                return "Key Algebraic Derivations:\n\n" +
                        "1. Quadratic Formula Derivation:\n" +
                        "   The standard form: ax² + bx + c = 0\n" +
                        "   Step 1: Divide both sides by a: x² + (b/a)x + c/a = 0\n" +
                        "   Step 2: Move constant term: x² + (b/a)x = -c/a\n" +
                        "   Step 3: Complete the square: x² + (b/a)x + (b/2a)² = (b/2a)² - c/a\n" +
                        "   Step 4: Simplify: (x + b/2a)² = (b² - 4ac)/4a²\n" +
                        "   Step 5: Take square root: x + b/2a = ±√(b² - 4ac)/2a\n" +
                        "   Step 6: Solve for x: x = [-b ± √(b² - 4ac)] / (2a)\n\n" +
                        "2. Binomial Theorem Derivation:\n" +
                        "   (a + b)ⁿ = Σ [n! / (k!(n-k)!)] a^(n-k) b^k for k=0 to n";

            case "geometry":
                return "Key Geometric Derivations:\n\n" +
                        "1. Pythagorean Theorem Derivation:\n" +
                        "   Consider a right triangle with legs a, b and hypotenuse c\n" +
                        "   Arrange 4 triangles to form a square with side (a+b)\n" +
                        "   The area of the large square: (a+b)² = a² + 2ab + b²\n" +
                        "   The area is also equal to 4*(ab/2) + c² = 2ab + c²\n" +
                        "   Therefore: a² + 2ab + b² = 2ab + c²\n" +
                        "   Simplify: a² + b² = c²\n\n" +
                        "2. Area of Circle Derivation:\n" +
                        "   Divide circle into sectors and arrange to form parallelogram\n" +
                        "   Height ≈ r, base ≈ πr\n" +
                        "   Area = base * height = πr * r = πr²";

            case "arithmetic":
                return "Key Arithmetic Derivations:\n\n" +
                        "1. Sum of Arithmetic Series Derivation:\n" +
                        "   S = n/2 * (a₁ + aₙ)\n" +
                        "   Where S is sum, n is number of terms,\n" +
                        "   a₁ is first term, aₙ is last term\n\n" +
                        "2. Multiplication Algorithm Derivation:\n" +
                        "   Based on distributive property:\n" +
                        "   a × (b + c) = (a × b) + (a × c)";

            case "calculus":
                return "Key Calculus Derivations:\n\n" +
                        "1. Derivative of x^n:\n" +
                        "   Using first principles:\n" +
                        "   f'(x) = lim(h→0) [f(x+h) - f(x)] / h\n" +
                        "   = lim(h→0) [(x+h)^n - x^n] / h\n" +
                        "   Expand binomial: [x^n + nx^{n-1}h + ... - x^n] / h\n" +
                        "   = lim(h→0) [nx^{n-1} + ...] = nx^{n-1}\n\n" +
                        "2. Fundamental Theorem of Calculus Derivation:\n" +
                        "   Part 1: d/dx ∫ₐˣ f(t) dt = f(x)\n" +
                        "   Part 2: ∫ₐᵇ f(x) dx = F(b) - F(a) where F' = f";

            case "differential equations":
                return "Key Differential Equation Derivations:\n\n" +
                        "1. Solution to First-Order Linear DE:\n" +
                        "   dy/dx + P(x)y = Q(x)\n" +
                        "   Integrating factor: μ(x) = e^∫P(x)dx\n" +
                        "   Solution: y = (1/μ(x)) ∫μ(x)Q(x)dx\n\n" +
                        "2. Separation of Variables Derivation:\n" +
                        "   dy/dx = g(x)h(y)\n" +
                        "   ∫(1/h(y)) dy = ∫g(x) dx";

            case "discrete math":
                return "Key Discrete Math Derivations:\n\n" +
                        "1. Euler's Formula Derivation:\n" +
                        "   For planar graphs: V - E + F = 2\n" +
                        "   Where V=vertices, E=edges, F=faces\n\n" +
                        "2. Binomial Coefficient Derivation:\n" +
                        "   C(n,k) = n! / (k!(n-k)!)";

            case "linear algebra":
                return "Key Linear Algebra Derivations:\n\n" +
                        "1. Matrix Multiplication Derivation:\n" +
                        "   Element cᵢⱼ = Σ aᵢₖ bₖⱼ for k=1 to n\n\n" +
                        "2. Determinant Derivation:\n" +
                        "   For 2x2 matrix: |a b| = ad - bc\n" +
                        "                  |c d|";

            case "logic":
                return "Key Logic Derivations:\n\n" +
                        "1. De Morgan's Laws Derivation:\n" +
                        "   ¬(P ∧ Q) ≡ ¬P ∨ ¬Q\n" +
                        "   ¬(P ∨ Q) ≡ ¬P ∧ ¬Q\n\n" +
                        "2. Modus Ponens Derivation:\n" +
                        "   If P→Q and P are true, then Q must be true";
            
//            case "logic":
//                return "Key Logic Derivations:\n\n" +
//                        "1. De Morgan's Laws Derivation:\n" +
//                        "   \u00AC(P \u2227 Q) \u2261 \u00ACP \u2228 \u00ACQ\n" +
//                        "   \u00AC(P \u2228 Q) \u2261 \u00ACP \u2227 \u00ACQ\n\n" +
//                        "2. Modus Ponens Derivation:\n" +
//                        "   If P\u2192Q and P are true, then Q must be true";


            case "number theory":
                return "Key Number Theory Derivations:\n\n" +
                        "1. Euclidean Algorithm Derivation:\n" +
                        "   gcd(a,b) = gcd(b, a mod b)\n\n" +
                        "2. Fermat's Little Theorem Derivation:\n" +
                        "   If p is prime and a not divisible by p, then a^{p-1} ≡ 1 (mod p)";

            case "probability":
                return "Key Probability Derivations:\n\n" +
                        "1. Bayes' Theorem Derivation:\n" +
                        "   P(A|B) = P(B|A)P(A) / P(B)\n\n" +
                        "2. Binomial Probability Formula:\n" +
                        "   P(X=k) = C(n,k) p^k (1-p)^{n-k}";

            case "statistics":
                return "Key Statistics Derivations:\n\n" +
                        "1. Mean Formula Derivation:\n" +
                        "   μ = (Σx_i) / N\n\n" +
                        "2. Standard Deviation Formula:\n" +
                        "   σ = √[Σ(x_i - μ)² / N]";

            case "topology":
                return "Key Topology Derivations:\n\n" +
                        "1. Euler Characteristic Derivation:\n" +
                        "   χ = V - E + F for polyhedra\n\n" +
                        "2. Brouwer Fixed-Point Theorem:\n" +
                        "   Any continuous function from a compact convex set to itself has a fixed point";

            case "trigonometry":
                return "Key Trigonometry Derivations:\n\n" +
                        "1. Pythagorean Identity Derivation:\n" +
                        "   sin²θ + cos²θ = 1\n" +
                        "   From unit circle: x² + y² = 1\n\n" +
                        "2. Law of Cosines Derivation:\n" +
                        "   c² = a² + b² - 2ab cosC";

            default:
                return "Derivations content for " + topic + " is not available yet.";
        }
    }

    private String getTopicSums(String topic) {
        // Add actual sums with solutions for each topic here
        switch (topic.toLowerCase()) {

            case "algebra":
                return "Algebra Sums with Solutions:\n\n" +
                        "1. Solve for x: 2x + 5 = 15\n" +
                        "   Solution: 2x = 10 → x = 5\n\n" +
                        "2. Factor: x² - 5x + 6\n" +
                        "   Solution: (x - 2)(x - 3)\n\n" +
                        "3. Solve: |2x - 3| = 7\n" +
                        "   Solution: 2x - 3 = 7 → x = 5\n" +
                        "        or 2x - 3 = -7 → x = -2";

            case "geometry":
                return "Geometry Sums with Solutions:\n\n" +
                        "1. Area of triangle: base=10cm, height=5cm\n" +
                        "   Solution: Area = ½ × 10 × 5 = 25 cm²\n\n" +
                        "2. Circumference of circle: radius=7cm\n" +
                        "   Solution: C = 2πr ≈ 2 × 3.14 × 7 = 43.96 cm\n\n" +
                        "3. Volume of sphere: radius=3cm\n" +
                        "   Solution: V = ⁴/₃πr³ ≈ ⁴/₃ × 3.14 × 27 = 113.04 cm³";

            case "arithmetic":
                return "Arithmetic Sums with Solutions:\n\n" +
                        "1. Calculate: 15 × (7 + 3)\n" +
                        "   Solution: 15 × 10 = 150\n\n" +
                        "2. Find GCD of 48 and 60\n" +
                        "   Solution: Factors of 48: 1,2,3,4,6,8,12,16,24,48\n" +
                        "            Factors of 60: 1,2,3,4,5,6,10,12,15,20,30,60\n" +
                        "            GCD = 12\n\n" +
                        "3. Simplify: ³⁄₄ ÷ ²⁄₅\n" +
                        "   Solution: ³⁄₄ × ⁵⁄₂ = ¹⁵⁄₈ = 1⅞";

            case "calculus":
                return "Calculus Sums with Solutions:\n\n" +
                        "1. Derivative of f(x) = 3x² + 2x - 5\n" +
                        "   Solution: f'(x) = 6x + 2\n\n" +
                        "2. Integrate ∫(2x + 3) dx from 1 to 3\n" +
                        "   Solution: [x² + 3x] from 1 to 3 = (9+9) - (1+3) = 18-4=14\n\n" +
                        "3. Limit: lim(x→0) (sin x)/x\n" +
                        "   Solution: Using L'Hôpital's rule: cos(0)/1 = 1";

            case "differential equations":
                return "Differential Equations Sums with Solutions:\n\n" +
                        "1. Solve: dy/dx = 2x\n" +
                        "   Solution: y = x² + C\n\n" +
                        "2. Solve: dy/dx + y = eˣ\n" +
                        "   Solution: Integrating factor eˣ\n" +
                        "            d/dx(y·eˣ) = e²ˣ\n" +
                        "            y·eˣ = ½e²ˣ + C\n" +
                        "            y = ½eˣ + Ce⁻ˣ";

            case "discrete math":
                return "Discrete Math Sums with Solutions:\n\n" +
                        "1. How many subsets of {1,2,3}?\n" +
                        "   Solution: 2³ = 8 subsets\n\n" +
                        "2. Solve recurrence: aₙ = aₙ₋₁ + 3, a₁=1\n" +
                        "   Solution: aₙ = 1 + 3(n-1) = 3n-2";

            case "linear algebra":
                return "Linear Algebra Sums with Solutions:\n\n" +
                        "1. Multiply matrices: [1 2; 3 4] × [5 6; 7 8]\n" +
                        "   Solution: [1*5+2*7, 1*6+2*8; 3*5+4*7, 3*6+4*8] = [19,22;43,50]\n\n" +
                        "2. Find determinant: |2 3; 4 1|\n" +
                        "   Solution: (2*1) - (3*4) = 2-12 = -10";

            case "logic":
                return "Logic Sums with Solutions:\n\n" +
                        "1. Truth table for P ∧ (Q ∨ R)\n" +
                        "   Solution: \n" +
                        "   P Q R | Result\n" +
                        "   T T T | T\n" +
                        "   T T F | T\n" +
                        "   T F T | T\n" +
                        "   T F F | F\n" +
                        "   F T T | F\n" +
                        "   F T F | F\n" +
                        "   F F T | F\n" +
                        "   F F F | F";

            case "number theory":
                return "Number Theory Sums with Solutions:\n\n" +
                        "1. Find GCD(48, 60) using Euclidean algorithm\n" +
                        "   Solution: gcd(60,48)=gcd(48,12)=gcd(12,0)=12\n\n" +
                        "2. Solve: 3x ≡ 2 mod 5\n" +
                        "   Solution: x ≡ 4 mod 5 (since 3*4=12≡2 mod 5)";

            case "probability":
                return "Probability Sums with Solutions:\n\n" +
                        "1. Probability of rolling 7 with two dice\n" +
                        "   Solution: 6/36 = 1/6\n\n" +
                        "2. Binomial: P(X=3) for n=5, p=0.5\n" +
                        "   Solution: C(5,3)(0.5)^3(0.5)^2 = 10*(0.125)*(0.25) = 0.3125";

            case "statistics":
                return "Statistics Sums with Solutions:\n\n" +
                        "1. Mean of {2,4,6,8}\n" +
                        "   Solution: (2+4+6+8)/4 = 5\n\n" +
                        "2. Standard deviation of {1,2,3,4,5}\n" +
                        "   Solution: Mean=3, deviations:[-2,-1,0,1,2]\n" +
                        "            Variance=(4+1+0+1+4)/5=2\n" +
                        "            SD=√2≈1.414";

            case "topology":
                return "Topology Sums with Solutions:\n\n" +
                        "1. Euler characteristic for cube\n" +
                        "   Solution: V=8, E=12, F=6 → χ=8-12+6=2\n\n" +
                        "2. Classify surface: Torus\n" +
                        "   Solution: Genus=1, orientable";

            case "trigonometry":
                return "Trigonometry Sums with Solutions:\n\n" +
                        "1. Solve: sin θ = 0.5 for 0≤θ<360\n" +
                        "   Solution: θ = 30° or 150°\n\n" +
                        "2. Prove identity: tan²θ + 1 = sec²θ\n" +
                        "   Solution: sin²θ/cos²θ + 1 = (sin²θ + cos²θ)/cos²θ = 1/cos²θ = sec²θ";

            default:
                return "Sums content for " + topic + " is not available yet.";
        }
    }

    public static void main(String[] args) {
        // Test with sample topic
        new SelectinTopics("testuser", "Algebra");
    }
}