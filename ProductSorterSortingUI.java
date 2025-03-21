import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSorterSortingUI {
    private JFrame frame;
    private List<List<Product>> batches;
    private DefaultTableModel tableModel;
    private int maxWeight;
    private JTextArea outputArea;

    public ProductSorterSortingUI(List<List<Product>> batches, int maxWeight) {
        this.batches = batches;
        this.maxWeight = maxWeight;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Kiki's Sorted Batches");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel backgroundPanel = createBackgroundPanel();
        frame.setContentPane(backgroundPanel);

        JPanel sortingPanel = createMainPanel();
        backgroundPanel.add(sortingPanel);

        setupTable(sortingPanel);
        setupOutputArea(sortingPanel);
        setupButtons(sortingPanel);

        populateTableWithBatches(batches);

        frame.setVisible(true);
    }

    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel(null) {
            Image backgroundImage = loadImage("C:\\Users\\DELL\\eclipse-workspace\\HEXPRESS\\src\\clouds.gif", "Background");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        frame.setIconImage(loadImage("kikilogo.png", "Icon"));
        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(245, 222, 179, 230));

        int panelWidth = 1400, panelHeight = 700;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - panelWidth) / 2;
        int y = (screen.height - panelHeight) / 2;
        panel.setBounds(x, y, panelWidth, panelHeight);

        JLabel title = new JLabel("KIKI'S SORTED DELIVERY BATCHES", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        title.setForeground(new Color(104, 2, 2));
        title.setBounds(50, 20, 1300, 50);
        panel.add(title);

        return panel;
    }

    private void setupTable(JPanel panel) {
        String[] columnNames = {"Batch", "Product Names", "Qty", "Weight", "Value"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 650, 450);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 5));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);
    }

    private JTable createStyledTable() {
        JTable table = new JTable(tableModel) {
            @Override
            public void doLayout() {
                TableColumnModel cm = getColumnModel();
                cm.getColumn(0).setPreferredWidth(80);
                cm.getColumn(1).setPreferredWidth(300);
                cm.getColumn(2).setPreferredWidth(90);
                cm.getColumn(3).setPreferredWidth(90);
                cm.getColumn(4).setPreferredWidth(90);
                super.doLayout();
            }
        };

        table.getColumnModel().getColumn(1).setCellRenderer(new WrappingCellRenderer());
        table.setFont(new Font("Serif", Font.BOLD, 20));
        table.setForeground(new Color(105, 3, 3));
        table.setRowHeight(60);
        table.setBackground(new Color(245, 222, 179, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        header.setForeground(Color.BLACK);
        header.setBackground(new Color(200, 180, 150));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i : new int[]{0, 2, 3, 4}) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setShowGrid(false);
        table.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 3));
        return table;
    }

    private void setupOutputArea(JPanel panel) {
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setForeground(new Color(105, 3, 3));
        outputArea.setBackground(new Color(245, 222, 179, 230));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBounds(750, 100, 600, 450);
        outputScroll.setBorder(BorderFactory.createLineBorder(new Color(104, 2, 2), 5));
        panel.add(outputScroll);
    }

    private void setupButtons(JPanel panel) {
        JButton sortNameButton = createButton("PRODUCT NAMES", new Color(150, 200, 220), 50, 570, 200, 60);
        JButton sortQtyButton = createButton("QUANTITY", new Color(150, 200, 220), 270, 570, 200, 60);
        JButton sortWeightButton = createButton("WEIGHT", new Color(150, 200, 220), 490, 570, 200, 60);
        JButton sortValueButton = createButton("VALUE", new Color(150, 200, 220), 710, 570, 200, 60);
        JButton searchButton = createButton("SEARCH CUSTOMER", new Color(200, 150, 200), 930, 570, 200, 60);
        JButton backButton = createButton("BACK", new Color(180, 180, 180), 1150, 570, 90, 60);
        JButton exitButton = createButton("EXIT", new Color(255, 100, 100), 1260, 570, 90, 60);

        panel.add(sortNameButton);
        panel.add(sortQtyButton);
        panel.add(sortWeightButton);
        panel.add(sortValueButton);
        panel.add(searchButton);
        panel.add(backButton);
        panel.add(exitButton);

        sortNameButton.addActionListener(e -> {
            ProductSorter.sortByProductName(batches);
            populateTableWithBatches(batches);
            displaySortedBatches("Product Name (Alphabetically)", batches);
        });

        sortQtyButton.addActionListener(e -> {
            ProductSorter.sortByTotalQuantity(batches);
            populateTableWithBatches(batches);
            displaySortedBatches("Total Quantity", batches);
        });

        sortWeightButton.addActionListener(e -> {
            ProductSorter.sortByTotalWeight(batches);
            populateTableWithBatches(batches);
            displaySortedBatches("Total Weight", batches);
        });

        sortValueButton.addActionListener(e -> {
            ProductSorter.sortByTotalValue(batches);
            populateTableWithBatches(batches);
            displaySortedBatches("Total Value", batches);
        });

        // Updated to use existing StringMatching class
        searchButton.addActionListener(e -> {
            // Create a new frame for StringMatchingGUI
            JFrame searchFrame = new JFrame("Customer Search");
            searchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            searchFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            searchFrame.setIconImage(loadImage("kikilogo.png", "Icon"));

            // Initialize StringMatchingGUI with the new frame
            StringMatchingGUI stringMatchingGUI = new StringMatchingGUI(searchFrame);

            // Set visible after initialization
            searchFrame.setVisible(true);

            // Update output area to indicate redirection
            outputArea.setText("Redirected to customer search interface.");
        });

        backButton.addActionListener(e -> goBack());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text, Color bgColor, int x, int y, int w, int h) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        button.setForeground(Color.BLACK);
        button.setBounds(x, y, w, h);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setOpaque(false);
        return button;
    }

    private void populateTableWithBatches(List<List<Product>> batches) {
        tableModel.setRowCount(0);
        if (batches == null || batches.isEmpty()) {
            outputArea.setText("No batches to display.");
            return;
        }

        int batchNumber = 1;
        for (List<Product> batch : batches) {
            if (batch == null || batch.isEmpty()) continue;

            String productNames = batch.stream()
                    .filter(p -> p != null && p.product_Name != null)
                    .map(p -> p.product_Name)
                    .collect(Collectors.joining(", "));
            int totalQty = batch.stream().mapToInt(p -> p.quantity).sum();
            int totalWeight = batch.stream().mapToInt(p -> p.product_Weight).sum();
            int totalValue = batch.stream().mapToInt(p -> p.product_Value).sum();

            tableModel.addRow(new Object[]{batchNumber, productNames, totalQty, totalWeight, totalValue});
            batchNumber++;
        }
    }

    private void displaySortedBatches(String sortType, List<List<Product>> sortedBatches) {
        StringBuilder sb = new StringBuilder();

        sb.append("╔════════════════════════════════════╗\n");
        sb.append("║   KIKI'S MAGICAL DELIVERY MANIFEST  ║\n");
        sb.append("╚════════════════════════════════════╝\n");
        sb.append(String.format(" Sorted by: %-26s \n", sortType));
        sb.append("──────────────────────────────────────\n");

        if (sortedBatches == null || sortedBatches.isEmpty()) {
            sb.append(" No packages to deliver today!      \n");
            sb.append(" Time for a catnap! 😺              \n");
            outputArea.setText(sb.toString());
            return;
        }

        int batchNumber = 1;
        int grandTotalItems = 0, grandTotalWeight = 0, grandTotalValue = 0;

        for (List<Product> batch : sortedBatches) {
            if (batch == null || batch.isEmpty()) continue;

            sb.append(String.format("\n Delivery Batch #%d\n", batchNumber));
            sb.append(" ┌──────────────────────────────┐\n");
            sb.append(" │      Magical Cargo Details   │\n");
            sb.append(" └──────────────────────────────┘\n");

            int totalWeight = 0, totalValue = 0, totalQty = 0;

            for (Product p : batch) {
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQty += p.quantity;
                grandTotalItems += p.quantity;
                grandTotalWeight += p.product_Weight;
                grandTotalValue += p.product_Value;

                sb.append(String.format(" %-20s │ Qty: %3d │ Wt: %3d kg │ Val: %4d\n",
                        p.product_Name, p.quantity, p.product_Weight, p.product_Value));
            }

            sb.append(" ──────────────────────────────\n");
            sb.append(" Batch Summary:\n");
            sb.append(String.format("   Total Items:  %4d\n", totalQty));
            sb.append(String.format("   Total Weight: %4d kg%s\n", totalWeight,
                    totalWeight > maxWeight ? " (Overloaded!)" : ""));
            sb.append(String.format("   Total Value:  %5d\n", totalValue));
            sb.append(" ──────────────────────────────\n");
            batchNumber++;
        }

        sb.append("\n╔════════════════════════════════════╗\n");
        sb.append("║        GRAND DELIVERY TOTALS       ║\n");
        sb.append("╚════════════════════════════════════╝\n");
        sb.append(String.format(" Total Items Delivered: %5d\n", grandTotalItems));
        sb.append(String.format(" Total Weight Shipped:  %5d kg\n", grandTotalWeight));
        sb.append(String.format(" Total Value Sent:      %5d\n", grandTotalValue));
        sb.append("──────────────────────────────────────\n");
        sb.append(" Happy Shipping! ✈️\n");

        outputArea.setText(sb.toString());
        outputArea.setCaretPosition(0);
    }

    private void goBack() {
        frame.dispose();
        SwingUtilities.invokeLater(() -> new ProductSorterBatchUI(maxWeight));
    }

    private Image loadImage(String path, String type) {
        try {
            return new ImageIcon(path).getImage();
        } catch (Exception e) {
            System.err.println(type + " image not found: " + e.getMessage());
            return null;
        }
    }

    private class WrappingCellRenderer implements TableCellRenderer {
        private final JTextArea textArea = new JTextArea();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            textArea.setText(value != null ? value.toString() : "");
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(new Font("Serif", Font.BOLD, 20));
            textArea.setForeground(new Color(105, 3, 3));
            textArea.setBackground(new Color(245, 222, 179, 230));
            if (isSelected) textArea.setBackground(table.getSelectionBackground());
            textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return textArea;
        }
    }
}
