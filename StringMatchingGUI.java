import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.LinkedList;

public class StringMatchingGUI extends JFrame {
    private StringMatching stringMatching;

    public StringMatchingGUI() {
        // Initialize StringMatching
        stringMatching = new StringMatching();

        // Set up the JFrame
        setTitle("Customer Lookup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setResizable(false); 
        setLayout(null);

        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Background image (clouds.gif)
        ImageIcon gifIcon = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\clouds.gif");
        Image gifImage = gifIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
        JLabel backgroundLabel = new JLabel(new ImageIcon(gifImage));
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);

        // Fonts
        Font font = new Font("Comic Sans MS", Font.BOLD, 16);
        Font inputFont = new Font("Comic Sans MS", Font.ITALIC, 14);

        // Customer table
        DefaultTableModel customerModel = new DefaultTableModel(new Object[]{"Customer List"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable customerTable = new JTable(customerModel);
        customerTable.setFont(new Font("Courier New", Font.PLAIN, 18));
        customerTable.setRowHeight(30);
        customerTable.setBackground(new Color(245, 222, 179, 230));
        customerTable.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        customerTable.getTableHeader().setBackground(new Color(155, 211, 230));
        customerTable.getTableHeader().setForeground(new Color(105, 3, 3));
        customerTable.setForeground(new Color(105, 3, 3));
        customerTable.getTableHeader().setReorderingAllowed(false);

        LinkedList<String> customerNames = stringMatching.getAllCustomerNames();
        Collections.sort(customerNames);
        for (String name : customerNames) {
            customerModel.addRow(new Object[]{capitalize(name)});
        }

        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBounds(100, 150, 250, 155);
        add(customerScrollPane);

        // Search input field
        JTextField inputField = new JTextField("Search Name", 20);
        inputField.setFont(inputFont);
        inputField.setBounds(100, 350, 250, 30);
        inputField.setForeground(Color.GRAY);
        add(inputField);

        // Focus listener for the input field
        inputField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (inputField.getText().equals("Search Name")) {
                    inputField.setText("");
                    inputField.setForeground(new Color(105, 3, 3));
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (inputField.getText().trim().isEmpty()) {
                    inputField.setText("Search Name");
                    inputField.setForeground(new Color(105, 3, 3));
                }
            }
        });

        // Product table
        String[] columns = {"Customer", "Location", "Quantity", "Product", "Weight", "Value"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Courier New", Font.PLAIN, 18));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(155, 211, 230));
        table.getTableHeader().setForeground(new Color(105, 3, 3));
        table.setBackground(new Color(245, 222, 179, 230));
        table.setForeground(new Color(105, 3, 3));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        customerTable.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(400, 150, 1000, 60);
        add(scrollPane);

        // Key listener for the input field
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String customerName = inputField.getText().trim().toLowerCase();
                    model.setRowCount(0);

                    LinkedList<Product> products = stringMatching.getCustomerProducts(customerName);
                    if (!products.isEmpty()) {
                        for (Product product : products) {
                            model.addRow(new Object[]{
                                    product.customer_name,
                                    product.location,
                                    product.quantity,
                                    product.product_Name,
                                    product.product_Weight + "kg",
                                    product.product_Value
                            });
                        }
                    } else {
                        JOptionPane.showMessageDialog(StringMatchingGUI.this, "Customer doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Exit button
        JButton exitButton = new JButton("Next...") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 222, 179, 230));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        exitButton.setForeground(new Color(105, 3, 3));
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setOpaque(false);
        exitButton.setBounds(screenWidth - 250, screenHeight - 100, 200, 50);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExitGui();
                dispose();
            }
        });
        add(exitButton);

        // Kitty image at the lower-right corner
        ImageIcon kittyIcon = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\CatUi.png");
        Image kitty = kittyIcon.getImage().getScaledInstance(230, 170, Image.SCALE_SMOOTH);
        JLabel kittyLabel = new JLabel(new ImageIcon(kitty));
        kittyLabel.setBounds(screenWidth - 395, screenHeight - 409, 500, 500); 
        add(kittyLabel);

        // Design UI image
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\DesignUI.png");
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(screenWidth - 1760, screenHeight - 980, 900, 900); 
        add(imageLabel);

       
        add(backgroundLabel);
    }

    private static String capitalize(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return capitalized.toString().trim();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StringMatchingGUI().setVisible(true);
        });
    }
}
