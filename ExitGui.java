package DAA;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitGui {
    private JFrame frame;
    private JLabel textLabel;
    private JButton exitButton;
    private exitClass exit;
    private String fullText = "All deliveries are completed! 🎉<br>Great job helping Kiki today!";
    private StringBuilder currentText = new StringBuilder();
    private Timer timer;
    private int index = 0;

    public ExitGui() {
        exit = new exitClass();

        frame = new JFrame("Exit Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        ImageIcon gifIcon = new ImageIcon("C:/Users/DELL/OneDrive/Documents/DAA/DAA midterm Proj/Videos/0320-ezgif.com-animated-gif-maker.gif");
        Image gifImage = gifIcon.getImage().getScaledInstance(screenWidth, screenHeight, Image.SCALE_DEFAULT);
        JLabel background = new JLabel(new ImageIcon(gifImage));
        background.setLayout(new BorderLayout());
        frame.setContentPane(background);

        textLabel = new JLabel("<html><div style='text-align: center; font-size: 50px; color: white;'></div></html>", SwingConstants.CENTER);
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        frame.add(textLabel, BorderLayout.CENTER);

        startTypewriterEffect();

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        exitButton = new JButton("Exit Program") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 180, 150, exitButton.getForeground().getAlpha()));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        exitButton.setForeground(new Color(105, 3, 3, 0));
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorder(BorderFactory.createEmptyBorder());
        exitButton.setOpaque(false);
        exitButton.setVisible(false);

        exitButton.addActionListener(e -> startExitAnimation());

        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void startTypewriterEffect() {
        timer = new Timer(30, e -> {
            if (index < fullText.length()) {
                currentText.append(fullText.charAt(index));
                textLabel.setText("<html><div style='text-align: center; font-size: 50px; color: white;'>" + currentText.toString() + "</div></html>");
                index++;
            } else {
                timer.stop();
                animateButton();
            }
        });
        timer.start();
    }

    private void animateButton() {
        exitButton.setVisible(true);
        Timer fadeInTimer = new Timer(50, new ActionListener() {
            int alpha = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 255) {
                    alpha += 15;
                    exitButton.setForeground(new Color(105, 3, 3, Math.min(alpha, 255)));
                    exitButton.repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        fadeInTimer.start();
    }

    private void startExitAnimation() {
        exitButton.setVisible(false);
        String exitText = "EXITING...";
        animateText(exitText, this::animateProgrammersNames);
    }

    private void animateProgrammersNames() {
        String programmersText = "<span style='font-size:25px; font-family:Comic Sans MS; font-weight:bold;'>PROGRAMMER'S NAME</span><br><br>" +
                "<span style='font-size:20px; font-family:Comic Sans MS; font-weight: thin; font-style:italic; line-height:1; display:inline-block;'>" 
                + String.join("<br>", exit.pNames) + 
                "</span><br><br>" +
                "<span style='font-size:20px; font-family:Comic Sans MS; font-style:italic;'>2BSCS-2</span>";
        animateText(programmersText, this::showFinalExitButton);
    }
    private void showFinalExitButton() {
        animateButton();
        exitButton.addActionListener(e -> closeWindow());
    }

    private void animateText(String text, Runnable callback) {
        StringBuilder textBuilder = new StringBuilder();
        Timer textTimer = new Timer(15, new ActionListener() {
            int textIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (textIndex < text.length()) {
                    textBuilder.append(text.charAt(textIndex));
                    textLabel.setText("<html><div style='text-align: center; color: white; font-size: 30px;'>" + textBuilder.toString() + "</div></html>");
                    textIndex++;
                } else {
                    ((Timer) e.getSource()).stop();
                    callback.run();
                }
            }
        });
        textTimer.start();
    }

    private void closeWindow() {
        Timer closeTimer = new Timer(500, e -> frame.dispose());
        closeTimer.setRepeats(false);
        closeTimer.start();
    }
}
