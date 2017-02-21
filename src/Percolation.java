/**
 * Created by oderor on 2/19/2017.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private boolean[][] open;
    private boolean[][] full;
    private int numberOfOpenSites;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private final int extraSiteTop;
    private final int extraSiteBottom;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        open = new boolean[n + 1][n + 1];
        full = new boolean[n + 1][n + 1];
        for (int r = 1; r <= n; r++) {
            for (int c = 1; c <= n; c++) {
                open[r][c] = false;
                full[r][c] = false;
            }
        }
        numberOfOpenSites = 0;
        // create a weightedQuickUnionUF based on the n-by-n grid + 2 additional virtual sites
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        // Numbers n^2 and n^2+1 are reserved for virtual sites
        extraSiteTop = n * n;
        extraSiteBottom = n * n + 1;
    }

    // transform (r,c) to a linear index [0;n^2)
    private int getLinearIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private void checkRowColumnIndex(int row, int col, int num) {
        if ((row <= 0) || (row > num) || (col <= 0) || (col > num)) {
            throw new IndexOutOfBoundsException();
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRowColumnIndex(row, col, n);
        if (open[row][col])
            return;
        // open
        open[row][col] = true;
        numberOfOpenSites++;
        // try to connect with neighbouring sites
        // try top site
        if (row != 1)
            if (open[row - 1][col]) {
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(row - 1, col));
                if (full[row - 1][col]) {
                    full[row][col] = true;
                }
            }
        // try bottom site
        if (row != n)
            if (open[row + 1][col]) {
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(row + 1, col));
                if (full[row + 1][col]) {
                    full[row][col] = true;
                }
            }
        // try left site
        if (col != 1)
            if (open[row][col - 1]) {
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(row, col - 1));
                if (full[row][col - 1]) {
                    full[row][col] = true;
                }
            }
        // try right site
        if (col != n)
            if (open[row][col + 1]) {
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(row, col + 1));
                if (full[row][col + 1]) {
                    full[row][col] = true;
                }
            }
        // additional case:
        // if current site is one of top or bottom rows,
        // we have to connect it to the 'extra' virtual sites above and below the grid
        if (row == 1) {
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    extraSiteTop
            );
            full[row][col] = true;
        }
        if (row == n) {
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    extraSiteBottom
            );
        }

        // try to fill
        if (full[row][col]) {
            fillNeighbours(row, col, n);
        }
    }

    private void fillNeighbours(int row, int col, int num) {
        fillSite(row - 1, col, num);
        fillSite(row + 1, col, num);
        fillSite(row, col - 1, num);
        fillSite(row, col + 1, num);
    }

    private void fillSite(int row, int col, int num) {
        if ((row <= 0) || (row > num) || (col <= 0) || (col > num))
            return;
        if (!open[row][col] || full[row][col])
            return;
        full[row][col] = true;
        fillSite(row - 1, col, num);
        fillSite(row + 1, col, num);
        fillSite(row, col - 1, num);
        fillSite(row, col + 1, num);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRowColumnIndex(row, col, n);
        return open[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRowColumnIndex(row, col, n);
        return full[row][col];
//        if (!isOpen(row, col))
//            return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // check whether they are connected
        return weightedQuickUnionUF.connected(extraSiteTop, extraSiteBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = StdIn.readInt();
        Percolation percolation = new Percolation(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            percolation.open(p, q);
        }
        StdOut.print("Computing percolation...\t");
        StdOut.println(percolation.percolates());
        StdOut.println("");
    }
}
