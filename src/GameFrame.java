import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameFrame extends JFrame {

    GameFrame() {
        ScorePanel score = new ScorePanel();
        DataPanel data = new DataPanel(score);
        GamePanel game = new GamePanel(score, data);

        JPanel side = new JPanel();
        side.setLayout(new BorderLayout());
        side.add(score, BorderLayout.NORTH);
        side.add(data, BorderLayout.CENTER);

        this.setLayout(new BorderLayout());
        this.add(game, BorderLayout.CENTER);
        this.add(side, BorderLayout.EAST);
        game.setBorder(BorderFactory.createLineBorder(Color.decode("#4a752c"), 5));

        ImageIcon image = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/icons/snake.jpeg")));
        this.setIconImage(image.getImage());

        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
