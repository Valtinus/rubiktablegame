/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import beadando2.RubikTablaGUI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ASUS
 */
public class RubikTest {
    
    @Test
    public void testSetGameSizeValid() {
        RubikTablaGUI game = new RubikTablaGUI(4);
        assertEquals(4, game.getN());
    }

    @Test
    public void testInitializeBoard() {
        RubikTablaGUI game = new RubikTablaGUI(4);
        int[][] board = game.getBoard();
        assertEquals(4, board.length);
        assertEquals(4, board[0].length);
    }

    @Test
    public void testMoveRowLeft() {
        RubikTablaGUI game = new RubikTablaGUI(2);
        int[][] initialBoard = game.getBoard();
        game.performMove(0, 0, 0);
        int[][] newBoard = game.getBoard();
        assertNotEquals(initialBoard, newBoard);
    }

    @Test
    public void testGameFinished() {
        RubikTablaGUI game = new RubikTablaGUI(2);
        assertFalse(game.isGameFinished());

        int[][] board = {{1, 1}, {2, 2}};
        game.setBoard(board);

        assertTrue(game.isGameFinished());
    }
}