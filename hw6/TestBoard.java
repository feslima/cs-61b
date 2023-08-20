import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TestBoard {
    private Board board;

    @Before
    public void setUp() {
        String filepath = "exampleBoard.txt";
        String dictionaryPath = "words.txt";

        In in = new In(filepath);
        ArrayList<String> rows = new ArrayList<>();
        while (!in.isEmpty()) {
            rows.add(in.readLine());
        }

        String[] results = new String[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            results[i] = rows.get(i);
        }

        Trie trie = Trie.buildTrieFromFile(dictionaryPath);
        board = new Board(results, trie);
    }

    @Test
    public void testGetNeighborsNonBorder() {
        int i = 1;
        int j = 1;

        Board.Cell[] expected = new Board.Cell[]{
                new Board.Cell(0, 0),
                new Board.Cell(0, 1),
                new Board.Cell(0, 2),
                new Board.Cell(1, 0),
                new Board.Cell(1, 2),
                new Board.Cell(2, 0),
                new Board.Cell(2, 1),
                new Board.Cell(2, 2)
        };
        Object[] actual = board.getNeighborsIndexes(i, j).toArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetNeighborsAtBorder() {
        int i = 0;
        int j = 0;

        Board.Cell[] expected = new Board.Cell[]{
                new Board.Cell(0, 1),
                new Board.Cell(1, 0),
                new Board.Cell(1, 1),
        };
        Object[] actual = board.getNeighborsIndexes(i, j).toArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testSearchBoard() {
        board.searchEntireBoard();

        assertFalse(board.allCombinations.isEmpty());
    }
}
