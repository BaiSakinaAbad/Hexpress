
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class Knapsack_UI {

    JFrame frame;

    public Knapsack_UI(int maxWeight) {
        initializeGUI(maxWeight);
    }
    private ImageIcon backgroundGif;


    private void initializeGUI(int maxWeight) {
        frame = new JFrame();
        frame.setTitle("All Possible Product Combinations");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        // Icon
        ImageIcon icon = new ImageIcon("kikilogo.png");
        frame.setIconImage(icon.getImage());

        // Background image
        ImageIcon backgroundIcon = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\clouds.gif");

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
        JPanel knapsackPanel = new JPanel(null);
        knapsackPanel.setBackground(new Color(245, 222, 179, 230));

        int panelWidth = 1200, panelHeight = 700;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;

        knapsackPanel.setBounds(x, y, panelWidth, panelHeight);
        backgroundPanel.add(knapsackPanel);

        // Title Label
        JLabel title = new JLabel("ALL POSSIBLE PRODUCT COMBINATIONS", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(new Color(104, 2, 2));
        title.setBounds(50, 20, 1100, 50);
        knapsackPanel.add(title);

        // Column Names
        String[] columnNames = {
            "Combination", "Product Names", "Total Quantity", "Total Weight", "Total Value"
        };

        // Table setup
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

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

        // Remove grid lines and use borders
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 3));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 1100, 450);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 5));
        knapsackPanel.add(scrollPane);

        // ✅ Load data from Knapsack_Algorithm
        KnapsackSolver.KnapsackResult result = KnapsackSolver.getAllPossibleOutcomes(maxWeight);
        populateTableFromResult(result.combinationsTable, tableModel);

        // ✅ Proceed Button (opens DeliveryCombination_UI)
        JButton proceedButton = new JButton("Proceed to Delivery Batches") {
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
        proceedButton.setBounds(450, 570, 300, 60);
        proceedButton.setContentAreaFilled(false);
        proceedButton.setFocusPainted(false);
        proceedButton.setBorder(BorderFactory.createEmptyBorder());
        proceedButton.setOpaque(false);
        knapsackPanel.add(proceedButton);

        // ✅ Proceed button action: Dispose Knapsack_UI and open DeliveryCombination_UI
        proceedButton.addActionListener(e -> {
            frame.dispose(); // Close Knapsack_UI window
            SwingUtilities.invokeLater(DeliveryCombination_UI::new); // Open DeliveryCombination_UI window
        });

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private void populateTableFromResult(List<String> combinationsTable, DefaultTableModel tableModel) {
        if (combinationsTable == null || combinationsTable.size() <= 2) {
            JOptionPane.showMessageDialog(frame, "No combinations found.");
            return;
        }

        // Skip header rows
        for (int i = 2; i < combinationsTable.size(); i++) {
            String rowString = combinationsTable.get(i);
            String[] rowData = rowString.trim().split("\\s{2,}");

            if (rowData.length >= 5) {
                tableModel.addRow(new Object[] {
                    rowData[0], // Combination number
                    rowData[1], // Product Names
                    rowData[2], // Total Quantity
                    rowData[3], // Total Weight
                    rowData[4]  // Total Value
                });
            } else {
                System.err.println("Skipping invalid row: " + rowString);
            }
        }
    }

    // Optional main for testing Knapsack_UI directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Knapsack_UI(50)); // Example maxWeight
    }
}
