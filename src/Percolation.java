/**
 * Created by oderor on 2/19/2017.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.BitSet;

public class Percolation {
    private int n;
    private BitSet open;
    private int numberOfOpenSites;
    private WeightedQuickUnionUF weightedQuickUnionUF;


    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        open = new BitSet(n*n);
        numberOfOpenSites = 0;
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
        if (open.get(getLinearIndex(row, col)))
            return;
        // open
        open.set(getLinearIndex(row, col));
        numberOfOpenSites++;
        // try to connect with neighbouring sites
        if (row != 1)
            if (open.get(getLinearIndex(top(row, col)[0], top(row, col)[1])))
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(top(row, col)[0], top(row, col)[1]));
        if (row != n)
            if (open.get(getLinearIndex(bottom(row, col)[0], bottom(row, col)[1])))
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(bottom(row, col)[0], bottom(row, col)[1]));
        if (col != 1)
            if (open.get(getLinearIndex(left(row, col)[0], left(row, col)[1])))
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(left(row, col)[0], left(row, col)[1]));
        if (col != n)
            if (open.get(getLinearIndex(right(row, col)[0], right(row, col)[1])))
                weightedQuickUnionUF.union(
                        getLinearIndex(row, col),
                        getLinearIndex(right(row, col)[0], right(row, col)[1]));
    }


    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IndexOutOfBoundsException();
        }
        return open.get(getLinearIndex(row, col));
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if ((row <= 0) || (row > n) || (col <= 0) || (col > n)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col))
            return false;

        for (int c = 1; c <= n; c++) {
            if (isOpen(1, c) && weightedQuickUnionUF.connected(getLinearIndex(row, col), getLinearIndex(1, c)))
                return true;
        }
        return false;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // for each site of row #1
        for (int cn = 1; cn <= n; cn++) {
            if (open.get(getLinearIndex(n, cn))) {
                // for each site of row #n
                for (int c1 = 1; c1 <= n; c1++) {
                    if (open.get(getLinearIndex(1, c1))) {
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
