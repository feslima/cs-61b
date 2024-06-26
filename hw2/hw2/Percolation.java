package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private static class Site {
        private final int x;
        private final int y;

        Site(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final int BLOCKED = -1;
    private final int EMPTY = 0;

    private final int FULL = 1;
    private final int gridSize;
    private final int[] grid;
    private final WeightedQuickUnionUF uf;

    private int openSites;

    public Percolation(int N) {
        // create N-by-N grid, with all sites initially blocked
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }

        gridSize = N;
        int nElements = gridSize * gridSize;

        grid = new int[N * N];

        for (int idx = 0; idx < nElements; idx++) {
            grid[idx] = BLOCKED;
        }
        openSites = 0;

        uf = new WeightedQuickUnionUF(nElements);
    }

    private int xyTo1d(int r, int c) {
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

    private void appendSiteIfValid(int row, int col, List<Site> siteList) {
        try {
            checkAccess(row, col);
        } catch (IndexOutOfBoundsException ex) {
            return;
        }
        siteList.add(new Site(row, col));
    }

    private List<Site> getNeighbors(int row, int col) {
        ArrayList<Site> neighbors = new ArrayList<>();

        appendSiteIfValid(row - 1, col, neighbors);
        appendSiteIfValid(row + 1, col, neighbors);
        appendSiteIfValid(row, col - 1, neighbors);
        appendSiteIfValid(row, col + 1, neighbors);

        return neighbors;
    }

    private List<Site> getEmptyNeighbors(int row, int col) {
        ArrayList<Site> emptyNeighbors = new ArrayList<>();

        for (Site site : getNeighbors(row, col)) {
            if (isOpen(site.x, site.y) && !isFull(site.x, site.y)) {
                emptyNeighbors.add(site);
            }
        }

        return emptyNeighbors;
    }

    private void fillOpenSitesIfConnected(int row, int col) {
        List<Site> neighbors = getEmptyNeighbors(row, col);
        if (neighbors.size() == 0) {
            return;
        }

        for (Site site : neighbors) {
            int idx = xyTo1d(site.x, site.y);
            grid[idx] = FULL;
            fillOpenSitesIfConnected(site.x, site.y);
        }
    }

    /*
    Open the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        checkAccess(row, col);

        if (!isOpen(row, col)) {
            openSites += 1;

            int idx = xyTo1d(row, col);
            if (row == 0) {
                grid[idx] = FULL;
            } else {
                grid[idx] = EMPTY;
            }


            for (Site site : getNeighbors(row, col)) {
                if (isOpen(site.x, site.y)) {
                    int idxNeighbor = xyTo1d(site.x, site.y);
                    uf.union(idx, idxNeighbor);

                    for (int j = 0; j < gridSize; j++) {
                        if (isFull(0, j) && uf.connected(j, idxNeighbor)) {
                            grid[idxNeighbor] = FULL;
                            fillOpenSitesIfConnected(site.x, site.y);
                        }
                    }
                }
            }
        }

    }

    public boolean isOpen(int row, int col) {
        checkAccess(row, col);
        int value = grid[xyTo1d(row, col)];
        return value != BLOCKED;
    }

    public boolean isFull(int row, int col) {
        checkAccess(row, col);
        return grid[xyTo1d(row, col)] == FULL;
    }

    public int numberOfOpenSites() {
        return openSites;
    }


    private List<Site> getTopFilledRow() {
        ArrayList<Site> filledSites = new ArrayList<>();

        for (int j = 0; j < gridSize; j++) {
            if (isFull(0, j)) {
                filledSites.add(new Site(0, j));
            }
        }

        return filledSites;
    }

    private List<Site> getBottomFilledRow() {
        ArrayList<Site> filledSites = new ArrayList<>();

        int row = gridSize - 1;
        for (int j = 0; j < gridSize; j++) {
            if (isFull(row, j)) {
                filledSites.add(new Site(row, j));
            }
        }

        return filledSites;
    }

    /* Does the system percolate? */
    public boolean percolates() {
        List<Site> topRow = getTopFilledRow();
        List<Site> bottomRow = getBottomFilledRow();
        if (topRow.size() == 0 || bottomRow.size() == 0) {
            return false;
        }

        for (Site bottomSite : bottomRow) {
            for (Site topSite : topRow) {
                int idxTopSite = xyTo1d(topSite.x, topSite.y);
                int idxBottomSite = xyTo1d(bottomSite.x, bottomSite.y);
                if (uf.connected(idxTopSite, idxBottomSite)) {
                    return true;
                }
            }
        }

        return false;
    }
}
