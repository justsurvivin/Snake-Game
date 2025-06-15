import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ScorePanel extends JPanel {
    static final int SCREEN_WIDTH = 200;
    static final int SCREEN_HEIGHT = 200;
    private int score;
    private Font gameFont;

    ScorePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.decode("#4a752c"));
        this.setFocusable(true);
        this.score = 0;
        loadFont();
    }

    public void loadFont() {
        try (InputStream fontStream = getClass().getResourceAsStream("resources/fonts/Minecrafter.Reg.ttf")) {
            gameFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 40);
        } catch (IOException | FontFormatException e) {
            gameFont = new Font("SansSerif", Font.BOLD, 30);
        }
    }

    public Font getGameFont() {return gameFont;}
    public int getScore() {return score;}

    public void increaseScore() {
        this.score++;
        repaint();
    }

    public void resetScore() {
        this.score = 0;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        board(g);
    }
    public void board(Graphics s) {
        s.setColor(Color.WHITE);
        s.setFont(gameFont);
        FontMetrics metrics = s.getFontMetrics();
        s.drawString("SCORE", (SCREEN_WIDTH - metrics.stringWidth("SCORE"))/2, s.getFont().getSize() * 2);
        s.drawString(String.valueOf(score), (SCREEN_WIDTH - metrics.stringWidth(String.valueOf(score))) / 2, s.getFont().getSize() * 4);
    }
}
