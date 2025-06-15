import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 70;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int appleX, appleY;
    char direction = 'R';
    boolean startsc = true;
    boolean running = false;
    boolean gameOver = false;
    boolean goldApple = false;
    ScorePanel scorePanel;
    DataPanel dataPanel;
    Timer timer;
    Random random;

    GamePanel(ScorePanel scorePanel, DataPanel dataPanel) {
        random = new Random();
        this.scorePanel = scorePanel;
        this.dataPanel = dataPanel;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.decode("#000000"));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        scorePanel.loadFont();
        resetGame();
    }

    public void resetGame() {
        bodyParts = 3;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = SCREEN_WIDTH / 4 - i * UNIT_SIZE;
            y[i] = SCREEN_HEIGHT / 2;
        }
        food();
        scorePanel.resetScore();
        running = false;
        gameOver = false;
        startsc = true;
    }

    public void startGame() {
        startsc = false;
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        // green chess board
        for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            for (int j = 0; j < SCREEN_WIDTH / UNIT_SIZE; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.decode("#a2d149"));
                } else {
                    g.setColor(Color.decode("#aad751"));
                }
                g.fillRect(j * UNIT_SIZE, i * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
        }

        // apple
        if (goldApple) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        // snake
        for(int i = 0; i < bodyParts; i++) {
            if(i == 0) { //head
                g.setColor(Color.decode("#426fe3"));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else { //body
                g.setColor(Color.decode("#4977ed"));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        if (startsc) {StartScr(g);}
        if (gameOver) {GameOverScr(g);}
    }

    private void StartScr(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(scorePanel.getGameFont().deriveFont(scorePanel.getGameFont().getSize() * 0.8f));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String msg = "Press Right to Start";
        g.drawString(msg, (SCREEN_WIDTH - metrics.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2);
    }
    private void GameOverScr(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.setColor(Color.RED);
        g.setFont(scorePanel.getGameFont().deriveFont(scorePanel.getGameFont().getSize() * 0.8f));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverMsg = "Game Over!";
        g.drawString(gameOverMsg, (SCREEN_WIDTH - metrics.stringWidth(gameOverMsg)) / 2, SCREEN_HEIGHT / 2 - 40);
        g.setColor(Color.WHITE);
        String restartMsg = "Press SPACE to Restart";
        g.drawString(restartMsg, (SCREEN_WIDTH - metrics.stringWidth(restartMsg)) / 2, SCREEN_HEIGHT / 2 + 20);
    }

    public void food() { // food can generate through body
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        goldApple = random.nextInt(50) == 1;
    }
    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE; break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE; break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE; break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; break;
        }
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            if (goldApple) { // eating gold apple occurs a body placement glitch, cosmetics
                for (int x=0; x<5; x++) {
                    bodyParts++;
                    scorePanel.increaseScore();
                }
            } else {
                bodyParts++;
                scorePanel.increaseScore();
            }
            food();
        }
    }
    public void checkCollision() throws SQLException {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0){running = false;}
        if (x[0] >= SCREEN_WIDTH){running = false;}
        if (y[0] < 0){running = false;}
        if (y[0] >= SCREEN_HEIGHT){running = false;}

        if (!running) {
            timer.stop();
            gameOver = true;
            Connect.insScores(scorePanel.getScore());
            dataPanel.displayTable();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            try {
                checkCollision();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (startsc && e.getKeyCode() == KeyEvent.VK_RIGHT) {
                startGame();
            } else if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
                resetGame();
                repaint();
            }

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (direction != 'D') {direction = 'U';} break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {direction = 'D';} break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {direction = 'L';} break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {direction = 'R';} break;
            }
        }
    }
}
