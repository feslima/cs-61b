import java.util.*;
import java.util.stream.Collectors;

public class Board {
    public static class Cell {
        private final int row;
        private final int col;

        Cell(int i, int j) {
            row = i;
            col = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private final char[][] board;
    private final int width;
    private final int height;
    private final ArrayList<Integer[]> OFFSETS = new ArrayList<>();
    public final Set<String> allCombinations = new HashSet<>();
    private final Trie trie;

    public Board(String[] rows, Trie trie) {
        height = rows.length;
        width = rows[0].length();

        board = new char[height][width];

        for (int i = 0; i < height; i++) {
            String row = rows[i];
            int cols = row.length();
            if (cols != width) {
                String msg = "Column size on board is invalid. Row " + i + " expected to have " + width + " columns.";
                throw new IllegalArgumentException(msg);
            }

            for (int j = 0; j < cols; j++) {
                board[i][j] = row.charAt(j);
            }
        }

        initializeOffsets();
        this.trie = trie;
    }

    private void initializeOffsets() {
        OFFSETS.add(new Integer[]{-1, -1}); // upper left
        OFFSETS.add(new Integer[]{-1, 0}); // upper middle
        OFFSETS.add(new Integer[]{-1, 1}); // upper right
        OFFSETS.add(new Integer[]{0, -1}); // left
        OFFSETS.add(new Integer[]{0, 1}); // right
        OFFSETS.add(new Integer[]{1, -1}); // lower left
        OFFSETS.add(new Integer[]{1, 0}); // lower middle
        OFFSETS.add(new Integer[]{1, 1}); // lower right
    }

    public List<String> getKLargestWords(int k) {
        Comparator<String> wordComparator = (o1, o2) -> {
            /* for same length words, keep lexicographic ascending order.
             * Otherwise, order words by size.
             */
            if (o1.length() == o2.length()) {
                return o1.compareTo(o2);
            }
            return o2.length() - o1.length();
        };

        Set<String> sortedSet = new TreeSet<>(wordComparator);
        sortedSet.addAll(allCombinations);

        return sortedSet.stream().limit(k).collect(Collectors.toList());
    }

    public List<Cell> getNeighborsIndexes(int i, int j) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        for (Integer[] offset : OFFSETS) {
            int row = i + offset[0];
            int col = j + offset[1];

            boolean isRowValid = row >= 0 && row < height;
            boolean isColValid = col >= 0 && col < width;
            if (isRowValid && isColValid) {
                neighbors.add(new Cell(row, col));
            }
        }

        return neighbors;

    }

    public void searchEntireBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                doDepthFirstSearch(i, j, new HashSet<>(), "");
            }
        }
    }

    private void doDepthFirstSearch(int row, int col, Set<Cell> visitedCells, String currentWord) {
        char letter = board[row][col];
        visitedCells.add(new Cell(row, col));
        currentWord += letter;

        if (trie.hasWord(currentWord)) {
            allCombinations.add(currentWord);
        }

        List<String> possibleWords = trie.keysWithPrefix(currentWord, -1);
        if (possibleWords.isEmpty()) {
            return;
        }

        List<Cell> currentNeighbors = getNeighborsIndexes(row, col);

        for (Cell neighbor : currentNeighbors) {
            if (!visitedCells.contains(neighbor)) {
                Set<Cell> visitedClone = new HashSet<>(visitedCells);
                doDepthFirstSearch(neighbor.row, neighbor.col, visitedClone, currentWord);
            }
        }
    }
}
