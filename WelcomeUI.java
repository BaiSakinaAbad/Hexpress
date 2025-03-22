import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;

public class WelcomeUI extends JFrame {
    private JLabel animatedTextLabel;
    private JButton startButton, exitButton;
    private Timer textAnimationTimer;
    private String text = "Would you like to join Kiki on her adventure?";
    private int charIndex = 0;
    private ImageIcon backgroundGif;
    private static Clip backgroundMusic; // Static to persist across pages

    public WelcomeUI() {
        // Set Fullscreen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Load Background GIF
        backgroundGif = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\kikibg.gif");

        // Create Custom Panel for Background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Animated Text Label with Shadow Effect
        animatedTextLabel = new JLabel("", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow Effect
                g2d.setColor(Color.BLACK);
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        g2d.drawString(getText(), i + 10, j + 60);
                    }
                }

                // White Foreground Text
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), 10, 60);
            }
        };
        animatedTextLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 67));
        animatedTextLabel.setBounds(20, 300, 1500, 100);
        backgroundPanel.add(animatedTextLabel);

        // Custom Start Button
        startButton = new CustomButtonUI("Yes, Let's go!");
        startButton.setBounds(600, 400, 350, 80);
        startButton.addActionListener(e -> openNextPage());
        backgroundPanel.add(startButton);

        // Play Background Music
        playBackgroundMusic("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\kkibgmusic.wav");

        // Text Animation Timer
        textAnimationTimer = new Timer(100, e -> {
            if (charIndex < text.length()) {
                animatedTextLabel.setText(text.substring(0, charIndex + 1));
                charIndex++;
            } else {
                textAnimationTimer.stop();
            }
        });
        textAnimationTimer.start();

        setVisible(true);
    }

    private void playBackgroundMusic(String filePath) {
        try {
            if (backgroundMusic == null || !backgroundMusic.isRunning()) {
                File musicFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop music
                backgroundMusic.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Unable to play background music.");
        }
    }

    private void openNextPage() {
        new secondPageUi();
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomeUI::new);
    }
}
