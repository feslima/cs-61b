import java.util.ArrayList;
import java.util.List;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        if (k <= 0) {
            throw new IllegalArgumentException("k must be positive.");
        }
        Board b = buildBoard(boardFilePath);
        b.searchEntireBoard();
        return b.getKLargestWords(k);
    }

    private static String[] getBoardRowsFromFile(String filepath) {
        In in = new In(filepath);
        ArrayList<String> rows = new ArrayList<>();
        while (!in.isEmpty()) {
            rows.add(in.readLine());
        }

        String[] results = new String[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            results[i] = rows.get(i);
        }
        return results;
    }
    private static Board buildBoard(String filepath) {
        String[] rows = getBoardRowsFromFile(filepath);
        Trie trie = Trie.buildTrieFromFile(dictPath);

        return new Board(rows, trie);
    }
}
