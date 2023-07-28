package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Objects;

public class Board implements WorldState {
    private final int BLANK = 0;
    private final int[] tiles;
    private final int size;

    public Board(int[][] tiles) {
        int nCols = tiles.length;
        int nRows = tiles[0].length;

        if (nCols != nRows) {
            throw new IllegalArgumentException("Board must be square");
        }
        size = nCols;

        // store the tiles as 1D array, for optimization purposes
        this.tiles = new int[size * size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.tiles[xyTo1d(i, j)] = tiles[i][j];
            }
        }
    }

    private int xyTo1d(int r, int c) {
        return r * size + c;
    }

    private int oneDtoX(int i) {
        return i / size;
    }

    private int oneDToY(int i) {
        return i % size;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j >= size) {
            throw new IndexOutOfBoundsException("i and j must be between 0 and " + (size - 1));
        }

        return tiles[xyTo1d(i, j)];
    }

    public int size() {
        return size;
    }

    public Iterable<WorldState> neighbors() {
        // Copied from provided solution in hw4 handout;

        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;


        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int distance = 0;
        for (int i = 0; i < size * size; i++) {
            int current = tiles[i];
            if (current != BLANK && current != i + 1) {
                distance += 1;
            }
        }
        return distance;
    }

    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < size * size; i++) {
            int current = tiles[i];

            if (current != BLANK) {
                int expectedX = oneDtoX(current - 1);
                int expectedY = oneDToY(current - 1);
                int actualX = oneDtoX(i);
                int actualY = oneDToY(i);

                distance += Math.abs(actualX - expectedX);
                distance += Math.abs(actualY - expectedY);
            }
        }
        return distance;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (this.getClass() != y.getClass()) {
            return false;
        }

        Board other = (Board) y;
        if (this.size != other.size()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }

        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }

    /**
     * Returns the string representation of the board.
     * Uncomment this method.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
