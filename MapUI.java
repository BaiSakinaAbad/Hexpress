import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MapUI extends JFrame {
    private ImageIcon backgroundGif, mapPng;
    private JLabel mapLabel;
    private DeliveryCombination deliveryCombination;
    private List<String> allBatchLocations;

    public MapUI(DeliveryCombination deliveryCombination, List<String> allBatchLocations) {
        this.deliveryCombination = deliveryCombination;
        this.allBatchLocations = allBatchLocations;

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        backgroundGif = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\clouds.gif");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        JPanel mapPanel = new JPanel();
        mapPanel.setBackground(new Color(255, 235, 205));
        mapPanel.setBounds(150, 100, 1300, 800);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        mapPanel.setLayout(null);
        backgroundPanel.add(mapPanel);

        JLabel mapTitle = new JLabel("GHIBLI MAP", SwingConstants.CENTER);
        mapTitle.setFont(new Font("Bradley Hand", Font.BOLD, 36));
        mapTitle.setForeground(new Color(139, 0, 0));
        mapTitle.setOpaque(true);
        mapTitle.setBackground(new Color(173, 216, 230));
        mapTitle.setBounds(0, 0, mapPanel.getWidth(), 60);
        mapTitle.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        mapPanel.add(mapTitle);

        mapPng = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\ghiblimap.png");
        Image mapImage = mapPng.getImage().getScaledInstance(mapPanel.getWidth() - 100, mapPanel.getHeight() - 150, Image.SCALE_SMOOTH);
        mapPng = new ImageIcon(mapImage);

        mapLabel = new JLabel(mapPng);
        mapLabel.setBounds(0, 10, mapPanel.getWidth(), mapPanel.getHeight() - 60);
        mapPanel.add(mapLabel);

        CustomButtonUI deliveryButton = new CustomButtonUI("Delivery Simulation");
        deliveryButton.setBounds(1100, 20, 400, 60);
        deliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMapPage();
            }
        });
        backgroundPanel.add(deliveryButton);

        mapPanel.revalidate();
        mapPanel.repaint();

        setVisible(true);
    }

    private void openMapPage() {
        new TSPui(deliveryCombination, allBatchLocations);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MapUI(null, null));
    }
}