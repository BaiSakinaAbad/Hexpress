import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ProductSorterBatchUI {

    private JFrame frame;
    private int maxWeight;
    private List<List<List<Product>>> allBatchSets; // Store multiple batch sets
    private int currentBatchSetIndex; // Track current batch set
    private DefaultTableModel tableModel;

    public ProductSorterBatchUI(int maxWeight) {
        this.maxWeight = maxWeight;
        this.allBatchSets = new ArrayList<>();
        this.currentBatchSetIndex = 0;
        initializeGUI();
    }

    private void initializeGUI() {
        // Initialize JFrame
        frame = new JFrame("Kiki's Delivery Batches");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Icon (optional)
        ImageIcon icon;
        try {
            icon = new ImageIcon("kikilogo.png");
        } catch (Exception e) {
            System.err.println("Icon image not found: " + e.getMessage());
            icon = new ImageIcon();
        }
        frame.setIconImage(icon.getImage());

        // Background image
        final ImageIcon backgroundIcon;
        ImageIcon icon1;
        try {
            icon1 = new ImageIcon("C:\\Users\\DELL\\eclipse-workspace\\HEXPRESS\\src\\clouds.gif", "Background");
        } catch (Exception e) {
            System.err.println("Background image not found: " + e.getMessage());
            icon1 = new ImageIcon();
        }

        backgroundIcon = icon1;
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
        JPanel batchPanel = new JPanel(null);
        batchPanel.setBackground(new Color(245, 222, 179, 230));

        int panelWidth = 1400, panelHeight = 700; // Increased panel width for longer table
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;

        batchPanel.setBounds(x, y, panelWidth, panelHeight);
        backgroundPanel.add(batchPanel);

        // Title Label
        JLabel title = new JLabel("KIKI NEEDS TO SORT HER DELIVERY BATCHES!", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(new Color(104, 2, 2));
        title.setBounds(50, 20, 1300, 50); // Adjusted for wider panel
        batchPanel.add(title);

        // Column Names for the batches table
        String[] columnNames = {
                "Batch", "Product Name", "Quantity", "Weight", "Value" // Adjusted column names
        };

        // Table setup
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        JTable table = new JTable(tableModel) {
            @Override
            public void doLayout() {
                TableColumnModel cm = getColumnModel();
                cm.getColumn(0).setPreferredWidth(100);  // Batch #
                cm.getColumn(1).setPreferredWidth(700);  // Product Name (increased for longer display)
                cm.getColumn(2).setPreferredWidth(150);  // Quantity
                cm.getColumn(3).setPreferredWidth(150);  // Weight
                cm.getColumn(4).setPreferredWidth(150);  // Value
                super.doLayout();
            }
        };

        // Custom renderer for Product Name column with text wrapping
        table.getColumnModel().getColumn(1).setCellRenderer(new TableCellRenderer() {
            private JTextArea textArea = new JTextArea();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                textArea.setText(value != null ? value.toString() : "");
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setFont(new Font("Serif", Font.BOLD, 20)); // Keeping original font
                textArea.setForeground(new Color(105, 3, 3));
                textArea.setBackground(new Color(245, 222, 179, 230));
                if (isSelected) {
                    textArea.setBackground(table.getSelectionBackground());
                }
                textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return textArea;
            }
        });

        // Table appearance customization
        table.setFont(new Font("Serif", Font.BOLD, 20)); // Keeping original font
        table.setForeground(new Color(105, 3, 3));
        table.setRowHeight(60); // Increased for wrapped text
        table.setBackground(new Color(245, 222, 179, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Allow horizontal scrolling

        // Header customization
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        header.setForeground(Color.BLACK);
        header.setBackground(new Color(200, 180, 150));

        // Center alignment for numeric columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 3));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 1300, 450); // Increased width for longer table
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 5));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        batchPanel.add(scrollPane);

        // Generate multiple batch sets
        try {
            generateBatchSets();
            if (allBatchSets.isEmpty() || allBatchSets.get(0).isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No batches generated.");
            } else {
                populateTableWithBatches(allBatchSets.get(currentBatchSetIndex));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error generating batches: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        // Next Button (Centered)
        JButton nextButton = new JButton("NEXT") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 200, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        nextButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        nextButton.setForeground(Color.BLACK);
        nextButton.setBounds(550, 570, 300, 60); // Centered in the panel (1400 width)
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setBorder(BorderFactory.createEmptyBorder());
        nextButton.setOpaque(false);
        batchPanel.add(nextButton);

        // Proceed Button (Lower Right)
        JButton proceedButton = new JButton("PROCEED") {
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
        proceedButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        proceedButton.setForeground(Color.BLACK);
        proceedButton.setBounds(1050, 570, 300, 60); // Lower right (panelWidth - 350)
        proceedButton.setContentAreaFilled(false);
        proceedButton.setFocusPainted(false);
        proceedButton.setBorder(BorderFactory.createEmptyBorder());
        proceedButton.setOpaque(false);
        batchPanel.add(proceedButton);

        // Next button action: Show next batch set
        nextButton.addActionListener(e -> {
            if (!allBatchSets.isEmpty()) {
                currentBatchSetIndex = (currentBatchSetIndex + 1) % allBatchSets.size();
                populateTableWithBatches(allBatchSets.get(currentBatchSetIndex));
            }
        });

        // Proceed button action: Open next UI with current batch set
        proceedButton.addActionListener(e -> {
            frame.dispose();
            List<List<Product>> currentBatches = allBatchSets.isEmpty() ? new ArrayList<>() : allBatchSets.get(currentBatchSetIndex);
            SwingUtilities.invokeLater(() -> new ProductSorterSortingUI(currentBatches, maxWeight));
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private void generateBatchSets() {
        // Generate multiple batch sets (e.g., 3 different runs) without sorting
        for (int i = 0; i < 3; i++) {
            Knapsack_Algorithm knapsack = new Knapsack_Algorithm(maxWeight);
            List<List<Product>> batches = knapsack.knapsackBatchSelection();
            // No sorting here to keep the original generated order
            allBatchSets.add(batches);
        }
    }

    private void populateTableWithBatches(List<List<Product>> batches) {
        tableModel.setRowCount(0);
        if (batches == null || batches.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No batches generated.");
            return;
        }

        int batchNumber = 1;

        for (List<Product> batch : batches) {
            if (batch == null || batch.isEmpty()) continue;

            // Add each product in the batch as a separate row
            for (Product product : batch) {
                if (product == null || product.product_Name == null) continue;

                tableModel.addRow(new Object[] {
                        batchNumber,
                        product.product_Name,
                        product.quantity,
                        product.product_Weight,
                        product.product_Value
                });
            }
            batchNumber++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductSorterBatchUI(50));
    }
}