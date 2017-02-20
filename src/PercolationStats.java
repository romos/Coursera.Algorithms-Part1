/**
 * Created by oderor on 2/19/2017.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private int n;
    private int tcount;
    private double[] percolationThresholds;
    private Percolation[] percolations;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.tcount = trials;
        this.percolationThresholds = new double[tcount];
        this.percolations = new Percolation[trials];
        for (int t = 0; t < tcount; t++) {
            percolationThresholds[t] = 0;
            percolations[t] = new Percolation(n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return StdStats.mean(percolationThresholds) -
                1.96 * StdStats.stddev(percolationThresholds) / Math.sqrt(tcount);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(percolationThresholds) +
                1.96 * StdStats.stddev(percolationThresholds) / Math.sqrt(tcount);
    }

    private static void openAmongBlocked(int siteToOpen, Percolation percolation, int n) {
        for (int r = 1; r <= n; r++) {
            for (int c = 1; c <= n; c++) {
                if (percolation.isOpen(r, c))
                    continue;

                if (siteToOpen == 0) {
                    percolation.open(r, c);
                    return;
                }
                else {
                    siteToOpen--;
                }
            }
        }
    }

    private void execute() {
        for (int t = 0; t < tcount; t++) {
            while (!percolations[t].percolates()) {
                int siteToOpen = StdRandom.uniform(n * n - percolations[t].numberOfOpenSites());
                openAmongBlocked(siteToOpen, percolations[t], n);
            }
            percolationThresholds[t] = (double) percolations[t].numberOfOpenSites() / (n * n);
        }
    }

    // test client (described below)
    public static void main(String[] args) {

        int n = StdIn.readInt();
        int t = StdIn.readInt();
        PercolationStats percolationStats = new PercolationStats(n, t);
        percolationStats.execute();
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }
}
