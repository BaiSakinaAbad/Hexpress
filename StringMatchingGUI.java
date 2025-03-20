package DAA;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class StringMatchingGUI {
    private static HashMap<String, String[]> customerData = new HashMap<>();

    public StringMatchingGUI(JFrame frame) {
        // Initialize customer data
        customerData.put("kocho", new String[]{"Kingsbury","6", "Tea Pot", "9kg", "3445"});
        customerData.put("howl", new String[]{"Howl's Castle","3", "Books", "5kg", "225"});
        customerData.put("ashitaka", new String[]{"Emishi Village","15", "Arrows", "8kg", "1500"});
        customerData.put("madame sulliman", new String[]{"Kingsbury","2", "Apple pie", "3kg", "180"});
        customerData.put("madame gina", new String[]{"Hotel Adriano","1", "Flower Vase", "2kg", "150"});
        customerData.put("porco rosso", new String[]{"Hotel Adriano","1", "Big Stuffed Toy", "7kg", "700"});
        customerData.put("king", new String[]{"Kingsbury","1", "Music Box", "3kg", "300"});
        customerData.put("san", new String[]{"Emishi Village","5", "Magic Amulet", "4kg", "2500"});

        frame.setLayout(null);

        // Get screen size for proper scaling
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Set Background GIF with full-screen scaling
        ImageIcon gifIcon = new ImageIcon("C:/Users/DELL/OneDrive/Documents/DAA/DAA midterm Proj/Videos/Background1.gif");
        Image gifImage = gifIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
        JLabel backgroundLabel = new JLabel(new ImageIcon(gifImage));
        backgroundLabel.setBounds(0, 0, screenWidth, screenHeight);

        // Set font for UI components
        Font font = new Font("Courier New", Font.BOLD, 16);
        Font inputFont = new Font("Courier New", Font.ITALIC, 14);

        // Customer List Table
        DefaultTableModel customerModel = new DefaultTableModel(new Object[]{"Customer List"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable customerTable = new JTable(customerModel);
        customerTable.setFont(font);
        customerTable.setRowHeight(30);
        customerTable.setBackground(new Color(245, 222, 179));
        customerTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        customerTable.getTableHeader().setBackground(new Color(155,211,221));
        customerTable.getTableHeader().setReorderingAllowed(false);

        for (String name : customerData.keySet()) {
            customerModel.addRow(new Object[]{capitalize(name)});
        }

        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBounds(30, 150, 250, 155);
        frame.add(customerScrollPane);

        JTextField inputField = new JTextField("Search Name", 20);
        inputField.setFont(inputFont);
        inputField.setBounds(30, 350, 250, 30);
        inputField.setForeground(Color.GRAY);
        frame.add(inputField);

        inputField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (inputField.getText().equals("Search Name")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (inputField.getText().trim().isEmpty()) {
                    inputField.setText("Search Name");
                    inputField.setForeground(Color.GRAY);
                }
            }
        });

        // Define columns for customer details table
        String[] columns = {"Customer", "Location", "Quantity", "Product", "Weight", "Value"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setFont(font);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(155,211,221));
        table.setBackground(new Color(245, 222, 179));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        customerTable.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(320, 150, 1000, 60);
        frame.add(scrollPane);

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String customerName = inputField.getText().trim().toLowerCase();
                    model.setRowCount(0);
                    if (customerData.containsKey(customerName)) {
                        String[] details = customerData.get(customerName);
                        model.addRow(new Object[]{capitalize(customerName), details[0], details[1], details[2], details[3], details[4]});
                    } else {
                        JOptionPane.showMessageDialog(frame, "Customer doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the background last to ensure it's behind everything
        frame.add(backgroundLabel);
    
    // Add an image at the lower-right corner
    ImageIcon kittyIcon = new ImageIcon("C:/Users/DELL/OneDrive/Documents/DAA/DAA midterm Proj/Pictures/CatUi.png");
    Image kitty = kittyIcon.getImage().getScaledInstance(500, 350, Image.SCALE_SMOOTH);
    JLabel kittyLabel = new JLabel(new ImageIcon(kitty));
    kittyLabel.setBounds(screenWidth - 500, screenHeight - 390, 500, 500); // Positioning the image
    frame.add(kittyLabel);

    // Add the background last to ensure it's behind everything
    frame.add(backgroundLabel);
    
    // Add an image at the lower-right corner
    ImageIcon imageIcon = new ImageIcon("C://Users//DELL//OneDrive//Documents//DAA//DAA midterm Proj//Pictures//DesignUI.png/");
    Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    JLabel imageLabel = new JLabel(new ImageIcon(image));
    imageLabel.setBounds(screenWidth - 1665, screenHeight - 885, 900, 900); // Positioning the image
    frame.add(imageLabel);

    // Add the background last to ensure it's behind everything
    frame.add(backgroundLabel);
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
}
