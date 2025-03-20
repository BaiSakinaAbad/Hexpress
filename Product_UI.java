package hexpress_algorithm;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class Product_UI {

    JFrame frame;
    private int maxWeight = -1; // Store the max weight entered in Bag_UI

    public Product_UI() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame();
        frame.setTitle("Knapsack Deliveries");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Icon
        ImageIcon icon = new ImageIcon("kikilogo.png");
        frame.setIconImage(icon.getImage());

        // Background image
        ImageIcon backgroundIcon = new ImageIcon("kiki sky.jpg");

        // Custom background panel
        JPanel backgroundPanel = new JPanel() {
            Image backgroundImage = backgroundIcon.getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        // Semi-transparent brown panel
        JPanel productPanel = new JPanel(null);
        productPanel.setBackground(new Color(245, 222, 179, 230));

        int panelWidth = 1200, panelHeight = 700;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;

        productPanel.setBounds(x, y, panelWidth, panelHeight);
        backgroundPanel.add(productPanel);

        // Title Label
        JLabel title = new JLabel("HERE ARE TODAY'S DELIVERIES!", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(new Color(104, 2, 2));
        title.setBounds(50, 20, 1100, 50);
        productPanel.add(title);

        // Column Names
        String[] columnNames = {
                "No.", "Customer Name", "Location", "Quantity", "Product Name", "Weight (kg)", "Value"
        };

        // Get data from Product.product array
        Object[][] data = getProductData(Product.product);

        JTable table = new JTable(data, columnNames);

        // Table appearance customization
        table.setFont(new Font("Serif", Font.BOLD, 20));
        table.setForeground(new Color(105, 3, 3));
        table.setRowHeight(50);
        table.setBackground(new Color(245, 222, 179, 230));

        // Header customization
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        header.setForeground(Color.BLACK);
        header.setBackground(new Color(200, 180, 150));

        // Center alignment for all cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Remove default grid lines and use borders
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 3));

        // Scroll pane with border
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 1100, 450);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 5));
        productPanel.add(scrollPane);

        // "Start Delivery" button
        JButton startButton = new JButton("Start Delivery") {
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

        startButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(450, 570, 300, 60);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder());
        startButton.setOpaque(false);
        productPanel.add(startButton);

        // Action: Open Bag_UI and capture maxWeight
        startButton.addActionListener(e -> {
            frame.dispose(); // Close current Product_UI
            new Bag_UI(enteredWeight -> {
                this.maxWeight = enteredWeight; // Save weight for reuse
                System.out.println("Max weight from Bag_UI: " + maxWeight);

                // Call Knapsack_Algorithm if needed
                Knapsack_Algorithm.KnapsackResult result = Knapsack_Algorithm.findCombinations(maxWeight);

                // Open Knapsack_UI (showing all possible combinations)
                SwingUtilities.invokeLater(() -> new Knapsack_UI(maxWeight));
            });
        });

     // "View All Combinations" button
        JButton viewCombinationsButton = new JButton("View All Combinations") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Matching background color (same style as Start Delivery)
                g2.setColor(new Color(200, 180, 150)); // You can change this color to match Start Delivery if you want
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

        viewCombinationsButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26)); // Same font and size as Start Delivery
        viewCombinationsButton.setForeground(Color.BLACK); // Same text color
        viewCombinationsButton.setBounds(800, 570, 300, 60); // Position and size (adjust if needed)
        viewCombinationsButton.setContentAreaFilled(false);
        viewCombinationsButton.setFocusPainted(false);
        viewCombinationsButton.setBorder(BorderFactory.createEmptyBorder());
        viewCombinationsButton.setOpaque(false);
        productPanel.add(viewCombinationsButton);

        // ✅ ActionListener for "View All Combinations" with custom popup
        viewCombinationsButton.addActionListener(e -> {
            if (maxWeight > 0) {
                System.out.println("Opening Knapsack_UI with existing maxWeight: " + maxWeight);
                SwingUtilities.invokeLater(() -> new Knapsack_UI(maxWeight));
            } else {
                // Customize JOptionPane to match your theme
                UIManager.put("OptionPane.background", new Color(245, 222, 179));
                UIManager.put("Panel.background", new Color(245, 222, 179));
                UIManager.put("OptionPane.messageForeground", new Color(105, 3, 3));
                UIManager.put("OptionPane.messageFont", new Font("Serif", Font.BOLD, 20));
                UIManager.put("Button.background", new Color(200, 180, 150));
                UIManager.put("Button.font", new Font("Comic Sans MS", Font.BOLD, 16));
                UIManager.put("Button.foreground", Color.BLACK);

                JOptionPane.showMessageDialog(
                        frame,
                        "Please start a delivery and enter the max weight first!",
                        "No Weight Entered",
                        JOptionPane.WARNING_MESSAGE
                );

                // Reset back to defaults after showing the dialog
                UIManager.put("OptionPane.background", null);
                UIManager.put("Panel.background", null);
                UIManager.put("OptionPane.messageForeground", null);
                UIManager.put("OptionPane.messageFont", null);
                UIManager.put("Button.background", null);
                UIManager.put("Button.font", null);
                UIManager.put("Button.foreground", null);
            }
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private Object[][] getProductData(Product[] products) {
        if (products == null || products.length == 0) {
            return new Object[][] {
                    {1, "John Doe", "City A", 10, "Product X", 5.0, 100.0}
            };
        }

        Object[][] data = new Object[products.length][7];

        for (int i = 0; i < products.length; i++) {
            Product p = products[i];
            data[i][0] = (i + 1);
            data[i][1] = p.customer_name;
            data[i][2] = p.location;
            data[i][3] = p.quantity;
            data[i][4] = p.product_Name;
            data[i][5] = p.product_Weight;
            data[i][6] = p.product_Value;
        }

        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Product_UI::new);
    }
}
