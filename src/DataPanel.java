import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DataPanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 200;
    static final int SCREEN_HEIGHT = 400;
    ScorePanel scorePanel;
    JLabel highScore;
    JButton clear;
    JTable scoreTable;

    DataPanel(ScorePanel scorePanel) {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.decode("#578a34"));
        this.setFocusable(true);
        this.setLayout(null);
        this.scorePanel = scorePanel;

        highScore = new JLabel("High Score", SwingConstants.CENTER);
        highScore.setForeground(Color.WHITE);
        highScore.setFont(scorePanel.getGameFont().deriveFont(scorePanel.getGameFont().getSize() * 0.6f));
        highScore.setVerticalAlignment(SwingConstants.TOP);
        highScore.setHorizontalAlignment(SwingConstants.CENTER);
        highScore.setBounds(0, 30, SCREEN_WIDTH, SCREEN_HEIGHT);

        scoreTable = new JTable();
        scoreTable.setEnabled(false);
        scoreTable.setBackground(Color.decode("#578a34"));
        scoreTable.setForeground(Color.WHITE);
        scoreTable.setFont(scorePanel.getGameFont().deriveFont(scorePanel.getGameFont().getSize() * 0.35f));
        scoreTable.setBounds(10, 90, SCREEN_WIDTH-20, SCREEN_HEIGHT/2);
        scoreTable.setShowGrid(false);
        displayTable();

        ImageIcon trash = new ImageIcon(Objects.requireNonNull(getClass().getResource("resources/icons/trash.png")));
        clear = new JButton();
        clear.setBounds((SCREEN_WIDTH - (SCREEN_WIDTH/2))/2, SCREEN_HEIGHT-90, SCREEN_WIDTH/2, SCREEN_HEIGHT/10);
        clear.addActionListener(this);
        clear.setFocusable(false);
        clear.setIcon(trash);
        clear.setBackground(Color.decode("#aad751"));
        clear.setBorder(BorderFactory.createLineBorder(Color.decode("#4a752c"), 5));

        this.add(highScore);
        this.add(scoreTable);
        this.add(clear);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear){
            try {
                Connect.delScores();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            displayTable();
        }
    }

    public void displayTable() {
        DefaultTableModel table = new DefaultTableModel();
        table.addColumn("DATE");
        table.addColumn("SCORE");
        try {
            ResultSet set = Connect.getScores();
            int rowCount = 0;
            while (set.next() && rowCount < 10) {
                String score = set.getString("score");
                String date = set.getString("tanggal");
                table.addRow(new String[]{date, score});
                rowCount++;
            }
            while (rowCount < 10) {
                table.addRow(new Object[]{"-", "-"});
                rowCount++;
            }

            scoreTable.setModel(table);

            DefaultTableCellRenderer rightAlignRenderer = new DefaultTableCellRenderer();
            rightAlignRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            scoreTable.getColumnModel().getColumn(1).setCellRenderer(rightAlignRenderer);
            scoreTable.getColumnModel().getColumn(0).setPreferredWidth(126);
            scoreTable.getColumnModel().getColumn(1).setPreferredWidth(54);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
