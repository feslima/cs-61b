package hw2;

public class Percolation {
    private final int BLOCKED = -1;
    private final int EMPTY = 0;

    private final int FULL = 1;
    private final int gridSize;
    private final int []grid;

    private int openSites;
    public Percolation(int N) {
        // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }

        gridSize = N;

        grid = new int[N*N];

        for (int idx = 0; idx < gridSize * gridSize; idx++) {
                grid[idx] = BLOCKED;
        }
        openSites = 0;
    }

    private int xyTo1d(int r, int c){
        return r * gridSize + c;
    }

    private void checkAccess(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IndexOutOfBoundsException("Can't have negative index.");
        }
        if (row >= gridSize || col >= gridSize) {
            throw new IndexOutOfBoundsException("Row/Column index too large.");
        }
    }

    /*
    Open the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        checkAccess(row, col);

        if (!isOpen(row, col)) {
            openSites += 1;

            int value = row == 0 ? FULL : EMPTY;
            grid[xyTo1d(row, col)] = value;

        }

    }

    public boolean isOpen(int row, int col) {
        checkAccess(row, col);
        int value = grid[xyTo1d(row, col)];
        return value == EMPTY || value == FULL;
    }

    public boolean isFull(int row, int col) {
        checkAccess(row, col);
        return grid[xyTo1d(row, col)] == FULL;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    /* Does the system percolate? */
    public boolean percolates() {
        return false;
    }

    public static void main(String[] args) {

    }
}
