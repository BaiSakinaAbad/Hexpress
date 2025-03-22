import javax.swing.*;
import java.awt.*;

public class CustomButtonUI extends JButton {
    public CustomButtonUI(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Bradley Hand", Font.BOLD, 36)); // Handwritten-style font
        setForeground(new Color(139, 0, 0)); // Dark reddish-brown text color
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background Color
        g2.setColor(new Color(173, 216, 230)); // Light Blue
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Rounded corners

        // Border Color
        g2.setColor(new Color(25, 25, 112)); // Navy border
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 50, 50);

        g2.dispose();
        super.paintComponent(g);
    }
}
