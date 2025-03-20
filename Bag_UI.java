package hexpress_algorithm;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class Bag_UI {

    private JFrame frame;
    private int maxWeight;
    private Consumer<Integer> onWeightConfirmed;

    // ✅ Constructor accepts a callback for when weight is entered
    public Bag_UI(Consumer<Integer> onWeightConfirmed) {
        this.onWeightConfirmed = onWeightConfirmed;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Kiki's Delivery Service - Basket Overview");

        // ✅ Set frame icon (optional)
        ImageIcon icon = new ImageIcon("kikilogo.png");
        frame.setIconImage(icon.getImage());

        // ✅ Maximize window to full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // ✅ Background ImageIcon (optional)
        ImageIcon backgroundIcon = new ImageIcon("kiki sky.jpg");

        // ✅ Custom panel to paint background
        JPanel backgroundPanel = new JPanel() {
            Image backgroundImage = backgroundIcon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        // ✅ Light brown panel container
        JPanel productPanel = new JPanel();
        Color lightBrown = new Color(245, 222, 179);
        productPanel.setBackground(lightBrown);

        int panelWidth = 1200;
        int panelHeight = 700;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;

        productPanel.setBounds(x, y, panelWidth, panelHeight);
        productPanel.setLayout(null);
        productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        backgroundPanel.add(productPanel);

        // ✅ Title label
        JLabel titleLabel = new JLabel("Kiki's Basket Can Only Hold So Much!", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(104, 2, 2));
        titleLabel.setBounds(200, 30, 800, 50);
        productPanel.add(titleLabel);

        // ✅ Instruction label
        JLabel instructionLabel = new JLabel("Please enter the maximum weight Kiki can carry (9-15kg):", JLabel.CENTER);
        instructionLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        instructionLabel.setForeground(new Color(104, 2, 2));
        instructionLabel.setBounds(150, 100, 900, 40);
        productPanel.add(instructionLabel);

        // ✅ Input field
        JTextField weightField = new JTextField();
        weightField.setFont(new Font("Serif", Font.PLAIN, 22));
        weightField.setBounds(400, 170, 150, 40);
        productPanel.add(weightField);

        // ✅ Submit button
        JButton submitButton = new JButton("Confirm") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(200, 180, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        submitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        submitButton.setForeground(new Color(105, 3, 3));
        submitButton.setBounds(600, 170, 150, 40);
        submitButton.setContentAreaFilled(false);
        submitButton.setFocusPainted(false);
        submitButton.setBorder(BorderFactory.createEmptyBorder());
        submitButton.setOpaque(false);

        productPanel.add(submitButton);

        // ✅ Status label
        JLabel statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Serif", Font.PLAIN, 22));
        statusLabel.setForeground(new Color(104, 2, 2));
        statusLabel.setBounds(300, 250, 600, 40);
        productPanel.add(statusLabel);

        // ✅ Submit button logic
        submitButton.addActionListener(e -> {
            String input = weightField.getText().trim();
            try {
                int weight = Integer.parseInt(input);
                if (weight >= 9 && weight <= 15) {
                    maxWeight = weight;
                    statusLabel.setText("Max weight set to " + maxWeight + " kg!");

                    // ✅ Pass weight back to Main_ using the Consumer callback
                    onWeightConfirmed.accept(maxWeight);

                    // ✅ Optionally disable inputs after confirming
                    weightField.setEnabled(false);
                    submitButton.setEnabled(false);
                } else {
                    statusLabel.setText("Please enter a number between 9 and 15.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid input. Enter a valid number.");
            }
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }
}
