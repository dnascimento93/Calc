import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

public class Calc extends JFrame {
    public Calc() {
        final double[] result = {0};
        
        // Create the content pane
        Container calc = getContentPane();
        calc.setLayout(new BorderLayout());

        // Create the display text field and add it to the content pane
        JTextField display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Consolas", Font.BOLD, 24));
        display.setBackground(Color.LIGHT_GRAY);
        display.setForeground(new Color(175, 50, 50));
        display.setPreferredSize(new Dimension(calc.getWidth(), 85));
        calc.add(display, BorderLayout.NORTH);

        // Create a 4x4 panel for the buttons and add it to the calc pane
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,4));
        calc.add(buttonPanel, BorderLayout.CENTER);

        // Create an array of 10 buttons (0-9) and populate that array with a loop
        JButton[] btn = new JButton[10];
        for (int i = 0; i <= 9; i++) {
            btn[i] = createButton((char)('0' + i), display, result);
        }
        // Other buttons get created separately
        JButton btnDivide = createButton('/', display, result);
        JButton btnMultiply = createButton('*', display, result);
        JButton btnMinus = createButton('-', display, result);
        JButton btnPlus = createButton('+', display, result);
        JButton btnClear = createButton('C', display, result);
        JButton btnEqual = createButton('=', display, result);

        // For each button
        for (int i = 1; i <= 9; i++) {
            // Add that button to the buttonPanel
            buttonPanel.add(btn[i]);

            // Finish the first row of buttons
            if (i == 3) {
                buttonPanel.add(btn[i]);
                buttonPanel.add(btnDivide);

            // Finish the second row of buttons
            } else if (i == 6) {
                buttonPanel.add(btn[i]);
                buttonPanel.add(btnMultiply);

            // Finish the third and fourth row of buttons
            } else if (i == 9) {
                buttonPanel.add(btn[i]);
                buttonPanel.add(btnMinus);
                buttonPanel.add(btnClear);
                buttonPanel.add(btn[0]);
                buttonPanel.add(btnEqual);
                buttonPanel.add(btnPlus);
            }
        }

        // Set JFrame values
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setSize(400, 550);
        setVisible(true);
    }

    public JButton createButton(Character label, JTextField display, double[] result) {
        JButton button = new JButton(String.valueOf(label));
        button.setFont(new Font("Consolas", Font.BOLD, 30));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(new Color(175, 50, 50));
        button.setBorder(new BasicBorders.ButtonBorder(Color.LIGHT_GRAY, Color.LIGHT_GRAY, Color.RED, Color.RED));

        // Button action listener logic
        button.addActionListener(_ -> {
            Set<Character> operators = Set.of('+', '-', '*', '/');
            String displayText = display.getText();
            boolean isEmpty = displayText.isEmpty();
            boolean lastCharIsOperator = !displayText.isEmpty() && operators.contains(displayText.charAt(displayText.length() - 1));
            boolean hasResult = displayText.contains("=");

            // If clear button
            if (label == 'C') {
                display.setText("");

            // If an operator button
            } else if (operators.contains(label)) {
                if (lastCharIsOperator) {
                    display.setText(displayText.substring(0, displayText.length() - 1) + label);
                } else if (isEmpty) {
                    display.setText("");
                } else if (hasResult) {
                    display.setText(result[0] + String.valueOf(label));
                } else {
                    display.setText(displayText + label);
                }

            // If equal button
            } else if (label == '=') {
                if (isEmpty) {
                    display.setText("");
                } else if (hasResult || lastCharIsOperator) {
                    display.setText(displayText);
                } else {
                    String[] splitNums = displayText.split("[-+/*]");
                    List<Double> numbers = new ArrayList<>();
                    for (String splitNum : splitNums) {
                        numbers.add(Double.parseDouble(splitNum));
                    }

                    List<Character> operatorsUsed = new ArrayList<>();
                    for (int i = 0; i < displayText.length(); i++) {
                        if (operators.contains(displayText.charAt(i))) {
                            operatorsUsed.add(displayText.charAt(i));
                        }
                    }

                    if (operatorsUsed.isEmpty()) {
                        result[0] = Double.parseDouble(displayText);
                    } else {
                        for (int i = 0; i < operatorsUsed.size(); i++) {
                            Character currentOperator = operatorsUsed.get(i);
                            if (currentOperator == '/' || currentOperator == '*') {
                                if (currentOperator == '/') {
                                    result[0] = numbers.get(i) / numbers.get(i + 1);
                                } else {
                                    result[0] = numbers.get(i) * numbers.get(i + 1);
                                }
                                numbers.set(i, result[0]);
                                numbers.remove(i + 1);
                                operatorsUsed.remove(i);
                                i--;
                            }
                        }
                        for (int i = 0; i < operatorsUsed.size(); i++) {
                            Character currentOperator = operatorsUsed.get(i);
                            if (currentOperator == '+' || currentOperator == '-') {
                                if (currentOperator == '+') {
                                    result[0] = numbers.get(i) + numbers.get(i + 1);
                                } else {
                                    result[0] = numbers.get(i) - numbers.get(i + 1);
                                }
                                numbers.set(i, result[0]);
                                numbers.remove(i + 1);
                                operatorsUsed.remove(i);
                                i--;
                            }
                        }
                    }
                    display.setText(displayText + " = " + result[0]);
                }

            // All other button actions
            } else {
                if (hasResult) {
                    display.setText(String.valueOf(label));
                } else {
                    display.setText(displayText + label);
                }
            }
        });
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calc::new);
    }
}