package hexpress_algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class DeliveryCombination_UI {

    private JFrame frame;
    private JTextArea outputArea;
    private DeliveryCombination deliveryCombination;
    private int maxWeight;

    public DeliveryCombination_UI() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Kiki's Delivery Batches");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Icon
        ImageIcon icon = new ImageIcon("kikilogo.png");
        frame.setIconImage(icon.getImage());

        // Background image
        ImageIcon backgroundIcon = new ImageIcon("kiki sky.jpg");

        JPanel backgroundPanel = new JPanel() {
            Image backgroundImage = backgroundIcon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        // Panel for delivery controls
        JPanel deliveryPanel = new JPanel(null);
        deliveryPanel.setBackground(new Color(245, 222, 179, 230));

        int panelWidth = 1200, panelHeight = 700;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;

        deliveryPanel.setBounds(x, y, panelWidth, panelHeight);
        backgroundPanel.add(deliveryPanel);

        // Title label
        JLabel title = new JLabel("Kiki's Delivery Service - Batches", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(new Color(104, 2, 2));
        title.setBounds(50, 20, 1100, 50);
        deliveryPanel.add(title);

        // Max weight input
        JLabel weightLabel = new JLabel("Enter Max Weight (kg):");
        weightLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        weightLabel.setBounds(50, 90, 300, 30);
        deliveryPanel.add(weightLabel);

        JTextField weightField = new JTextField();
        weightField.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
        weightField.setBounds(350, 90, 200, 30);
        deliveryPanel.add(weightField);

        // "Find & Deliver Batches" Button
        JButton findDeliverButton = createStyledButton("Find & Deliver Batches");
        findDeliverButton.setBounds(600, 90, 400, 50);
        deliveryPanel.add(findDeliverButton);

        // Output Area (TextArea inside JScrollPane)
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
        outputArea.setBackground(new Color(255, 248, 220));
        outputArea.setForeground(new Color(105, 3, 3));

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(50, 160, 1100, 400);
        deliveryPanel.add(scrollPane);

        // "Deliver Next Combination" Button
        JButton deliverNextButton = createStyledButton("Deliver Next Combination");
        deliverNextButton.setBounds(450, 580, 300, 60);
        deliverNextButton.setEnabled(false);
        deliveryPanel.add(deliverNextButton);

        // Find & Deliver batches action
        findDeliverButton.addActionListener((ActionEvent e) -> {
            try {
                maxWeight = Integer.parseInt(weightField.getText().trim());

                // Run the knapsack batch selection process
                outputArea.setText("");
                findAndDisplayBatches(maxWeight);

                deliverNextButton.setEnabled(true);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid max weight.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Deliver next combination action
        deliverNextButton.addActionListener((ActionEvent e) -> {
            if (deliveryCombination != null && !deliveryCombination.isEmpty()) {
                deliverNextCombination();
            } else {
                outputArea.append("\nAll batches delivered!\n");
                deliverNextButton.setEnabled(false);
            }
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(200, 180, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setOpaque(false);

        return button;
    }

    private void findAndDisplayBatches(int maxWeight) {
        Knapsack_Algorithm knapsack = new Knapsack_Algorithm(maxWeight);
        List<List<Product>> allBatches = knapsack.knapsackBatchSelection();

        deliveryCombination = new DeliveryCombination();

        int batchNumber = 1;

        for (List<Product> batch : allBatches) {
            if (batch.isEmpty()) {
                outputArea.append("\nNo valid batch found. Stopping deliveries.\n");
                break;
            }

            int totalWeight = 0, totalValue = 0, totalQuantity = 0;
            String deliveryLocation = "Multiple Locations";
            List<String> batchLocations = new java.util.ArrayList<>();
            StringBuilder productNames = new StringBuilder();

            for (Product p : batch) {
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;
                if (!batchLocations.contains(p.location)) {
                    batchLocations.add(p.location);
                }
                productNames.append(p.product_Name).append(", ");
            }

            if (batchLocations.size() == 1) {
                deliveryLocation = batchLocations.get(0);
            }

            // Add to delivery queue
            deliveryCombination.addCombination(batch, totalWeight, totalValue, deliveryLocation);

            outputArea.append("\nBatch " + batchNumber + " ready for delivery!\n");
            outputArea.append("Products: " + productNames.toString() + "\n");
            outputArea.append("Total Quantity: " + totalQuantity + "\n");
            outputArea.append("Total Weight: " + totalWeight + " kg\n");
            outputArea.append("Total Value: " + totalValue + "\n");
            outputArea.append("Locations: " + String.join(", ", batchLocations) + "\n");
            outputArea.append("------------------------------------------------------\n");

            batchNumber++;
        }

        if (deliveryCombination.isEmpty()) {
            outputArea.append("\nNo batches available for delivery.\n");
        } else {
            outputArea.append("\nPress 'Deliver Next Combination' to start deliveries.\n");
        }
    }

    private void deliverNextCombination() {
        DeliveryCombination.Combination bestCombo = deliveryCombination.peekCombination();
        if (bestCombo == null) {
            outputArea.append("\nNo more deliveries!\n");
            return;
        }

        deliveryCombination.deliverCombination();

        StringBuilder productNames = new StringBuilder();
        for (Product p : bestCombo.products) {
            productNames.append(p.product_Name).append(", ");
        }

        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        }

        outputArea.append("\nDelivered to: " + bestCombo.location + "\n");
        outputArea.append("Products: " + productNames.toString() + "\n");
        outputArea.append("Total Weight: " + bestCombo.totalWeight + " kg\n");
        outputArea.append("Total Value: " + bestCombo.totalValue + "\n");
        outputArea.append("------------------------------------------------------\n");

        // Run TSP Solver for locations (optional)
        TSP_Solver.runTSP(java.util.Arrays.asList(bestCombo.location));

        if (deliveryCombination.isEmpty()) {
            outputArea.append("\nAll batches delivered!\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DeliveryCombination_UI::new);
    }
}
