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
    private WeightedQuickUnionUF weightedQuickUnionUF;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        open = new boolean[n + 1][n + 1];
        full = new boolean[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                open[i][j] = false;
                full[i][j] = false;
            }
        }
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n);
    }

    private int getLinearIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private int[] top(int row, int col) {
        int[] site = new int[2];
        site[0] = row;
        site[1] = col;
        if (row > 1) {
            site[0] = row - 1;
        }
        return site;
    }

    private int[] bottom(int row, int col) {
        int[] site = new int[2];
        site[0] = row;
        site[1] = col;
        if (row < n) {
            site[0] = row + 1;
        }
        return site;
    }

    private int[] left(int row, int col) {
        int[] site = new int[2];
        site[0] = row;
        site[1] = col;
        if (col > 1) {
            site[1] = col - 1;
        }
        return site;
    }

    private int[] right(int row, int col) {
        int[] site = new int[2];
        site[0] = row;
        site[1] = col;
        if (col < n) {
            site[1] = col + 1;
        }
        return site;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IndexOutOfBoundsException();
        }
        if (open[row][col])
            return;
        // open
        open[row][col] = true;
        // try to connect with neighbouring sites
        if (open[top(row, col)[0]][top(row, col)[1]])
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    getLinearIndex(top(row, col)[0], top(row, col)[1]));

        if (open[bottom(row, col)[0]][bottom(row, col)[1]])
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    getLinearIndex(bottom(row, col)[0], bottom(row, col)[1]));
        if (open[left(row, col)[0]][left(row, col)[1]])
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    getLinearIndex(left(row, col)[0], left(row, col)[1]));
        if (open[right(row, col)[0]][right(row, col)[1]])
            weightedQuickUnionUF.union(
                    getLinearIndex(row, col),
                    getLinearIndex(right(row, col)[0], right(row, col)[1]));
        // try to fill
        if ((row == 1) ||
                (full[top(row, col)[0]][top(row, col)[1]] ||
                        full[bottom(row, col)[0]][bottom(row, col)[1]] ||
                        full[left(row, col)[0]][left(row, col)[1]] ||
                        full[right(row, col)[0]][right(row, col)[1]])) {
            full[row][col] = true;
            tryToFillRecursively(row - 1, col);
            tryToFillRecursively(row + 1, col);
            tryToFillRecursively(row, col - 1);
            tryToFillRecursively(row, col + 1);
        }

    }

    private void tryToFillRecursively(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            return;
        }
        if (!open[row][col] || full[row][col]) {
            return;
        }
        full[row][col] = true;
        tryToFillRecursively(row - 1, col);
        tryToFillRecursively(row + 1, col);
        tryToFillRecursively(row, col - 1);
        tryToFillRecursively(row, col + 1);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IndexOutOfBoundsException();
        }
        return open[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IndexOutOfBoundsException();
        }
        return full[row][col];
    }

    // number of open sites
    public int numberOfOpenSites() {
        int result = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (isOpen(i, j)) {
                    result++;
                }
            }
        }
        return result;
    }

    // does the system percolate?
    public boolean percolates() {
        // for each site of row #1
        for (int c1 = 1; c1 <= n; c1++) {
            if (isOpen(1, c1)) {
                // for each site of row #n
                for (int cn = 1; cn <= n; cn++) {
                    if (isOpen(n, cn)) {
                        // check whether they are connected
                        if (weightedQuickUnionUF.connected(getLinearIndex(n, cn), getLinearIndex(1, c1))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
