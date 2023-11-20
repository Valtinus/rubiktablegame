/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package beadando2;

/**
 *
 * @author ASUS
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.event.MouseEvent;

public class RubikTablaGUI extends JFrame {
    private int n;
    private int[][] board;
    private int moves;

    public RubikTablaGUI(int size) {
        setTitle("Rubik Tábla Játék");
        setLayout(new BorderLayout());
        setGameSize(size);
        initializeBoard();

        JButton newGameButton = new JButton("Új játék");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newSize = Integer.parseInt(JOptionPane.showInputDialog("Add meg a tábla méretét (2, 4 vagy 6):"));
                setGameSize(newSize);
                newGame();
            }
        });
        

        JPanel controlPanel = new JPanel();
        controlPanel.add(newGameButton);

        GameBoardPanel gameBoardPanel = new GameBoardPanel();
        gameBoardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int cellSize = Math.min(gameBoardPanel.getWidth() / n, gameBoardPanel.getHeight() / n);
                int i = (evt.getX() - (gameBoardPanel.getWidth() - n * cellSize) / 2) / cellSize;
                int j = (evt.getY() - (gameBoardPanel.getHeight() - n * cellSize) / 2) / cellSize;

                if (i >= 0 && i < n && j >= 0 && j < n) {
                    showMoveOptionsDialog(i, j);
                }
            }
        });

        add(gameBoardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public int getN() {
        return n;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] newBoard) {
        board = newBoard;
    }

    private void setGameSize(int size) {
        if (size != 2 && size != 4 && size != 6) {
            JOptionPane.showMessageDialog(null, "Érvénytelen méret. Válassz 2, 4 vagy 6 közül.");
            throw new IllegalArgumentException("A méret csak 2, 4 vagy 6 lehet.");
        }
        n = size;
    }

    private void initializeBoard() {
        List<Integer> colors = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < n; j++) {
                colors.add(i);
            }
        }
        Collections.shuffle(colors);

        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = colors.get(i * n + j);
            }
        }
    }

    private void newGame() {
        initializeBoard();
        moves = 0;
        repaint();
    }

    public boolean isGameFinished() {
        boolean allRowsOneColor = true;
        boolean allColumnsOneColor = true;

        for (int i = 0; i < n; i++) {
            if (!areAllColorsSameInRow(i)) {
                allRowsOneColor = false;
            }
            if (!areAllColorsSameInColumn(i)) {
                allColumnsOneColor = false;
            }
        }

        return allRowsOneColor || allColumnsOneColor;
    }

    private boolean areAllColorsSameInRow(int row) {
        int firstColor = board[row][0];
        for (int j = 1; j < n; j++) {
            if (board[row][j] != firstColor) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllColorsSameInColumn(int col) {
        int firstColor = board[0][col];
        for (int i = 1; i < n; i++) {
            if (board[i][col] != firstColor) {
                return false;
            }
        }
        return true;
    }

    private void shiftColumnDown(int col) {
        int temp = board[col][n - 1];
        for (int i = n - 1; i > 0; i--) {
            board[col][i] = board[col][i - 1];
        }
        board[col][0] = temp;
    }

    private void shiftColumnUp(int col) {
        int temp = board[col][0];
        for (int i = 0; i < n - 1; i++) {
            board[col][i] = board[col][i + 1];
        }
        board[col][n - 1] = temp;
    }

    private void shiftRowRight(int row) {
        int temp = board[n - 1][row];
        for (int i = n - 1; i > 0; i--) {
            board[i][row] = board[i - 1][row];
        }
        board[0][row] = temp;
    }

    private void shiftRowLeft(int row) {
        int temp = board[0][row];
        for (int i = 0; i < n - 1; i++) {
            board[i][row] = board[i + 1][row];
        }
        board[n - 1][row] = temp;
    }

    private void showMoveOptionsDialog(int row, int col) {
        String[] options = {"Balra", "Jobbra", "Felfele", "Lefele"};
        int result = JOptionPane.showOptionDialog(
                this,
                "Válaszd ki a mozgatás irányát:",
                "Mozgatás",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (result >= 0 && result < options.length) {
            performMove(result, row, col);
        }
    }

    public void performMove(int direction, int row, int col) {
        switch (direction) {
            case 0:
                shiftRowLeft(row);
                break;
            case 1:
                shiftRowRight(row);
                break;
            case 2:
                shiftColumnUp(col);
                break;
            case 3:
                shiftColumnDown(col);
                break;
        }

        moves++;
        repaint();

        if (isGameFinished()) {
            JOptionPane.showMessageDialog(null, "Gratulálok! Nyertél " + moves + " lépésben.", "Játék vége", JOptionPane.INFORMATION_MESSAGE);
            int response = JOptionPane.showConfirmDialog(null, "Szeretnél új játékot kezdeni?", "Új játék", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                System.exit(0);
            }
        }
    }

    private class GameBoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellSize = Math.min(getWidth() / n, getHeight() / n);
            int xOffset = (getWidth() - n * cellSize) / 2;
            int yOffset = (getHeight() - n * cellSize) / 2;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    g.setColor(getColorForNumber(board[i][j]));
                    g.fillRect(xOffset + i * cellSize, yOffset + j * cellSize, cellSize, cellSize);
                }
            }

            g.setColor(Color.BLACK);
            for (int i = 0; i <= n; i++) {
                g.drawLine(xOffset + i * cellSize, yOffset, xOffset + i * cellSize, getHeight() - yOffset);
                g.drawLine(xOffset, yOffset + i * cellSize, getWidth() - xOffset, yOffset + i * cellSize);
            }
        }

        private Color getColorForNumber(int number) {
            int colorCount = n;
            float hue = (float) number / colorCount;
            return Color.getHSBColor(hue, 1f, 1f);
        }

        private int getClickedRow(int y, int yOffset, int cellSize) {
            int relativeY = y - yOffset;
            return Math.min(Math.max(0, relativeY / cellSize), n - 1);
        }

        private int getClickedColumn(int x, int xOffset, int cellSize) {
            int relativeX = x - xOffset;
            return Math.min(Math.max(0, relativeX / cellSize), n - 1);
        }

        @Override
        protected void processMouseEvent(MouseEvent e) {
            if (e.getID() == MouseEvent.MOUSE_CLICKED) {
                int cellSize = Math.min(getWidth() / n, getHeight() / n);
                int xOffset = (getWidth() - n * cellSize) / 2;
                int yOffset = (getHeight() - n * cellSize) / 2;

                int i = getClickedColumn(e.getX(), xOffset, cellSize);
                int j = getClickedRow(e.getY(), yOffset, cellSize);

                if (i >= 0 && i < n && j >= 0 && j < n) {
                    showMoveOptionsDialog(j, i);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int size = Integer.parseInt(JOptionPane.showInputDialog("Add meg a tábla méretét (2, 4 vagy 6):"));
            if (size != 2 && size != 4 && size != 6) {
                JOptionPane.showMessageDialog(null, "Érvénytelen méret. Válassz 2, 4 vagy 6 közül.");
                return;
            }

            new RubikTablaGUI(size);
        });
    }
}

