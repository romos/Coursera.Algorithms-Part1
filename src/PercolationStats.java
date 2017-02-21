/**
 * Created by oderor on 2/19/2017.
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.ArrayList;
import java.util.List;

public class PercolationStats {
    private int n;
    private int tcount;
    private double[] percolationThresholds;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.tcount = trials;
        this.percolationThresholds = new double[tcount];
        for (int t = 0; t < tcount; t++) {
            percolationThresholds[t] = 0;
        }
        execute();
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

    private static int[] getRowColFromLinear(int index, int n) {
        return new int[]{index / n + 1, index % n + 1};
    }

    private void execute() {
        int nsquare = n * n;
        for (int t = 0; t < tcount; t++) {
            List<Integer> closedSites = new ArrayList<>(nsquare);
            for (int i = 0; i < nsquare; i++) {
                closedSites.add(i);
            }
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int ind = StdRandom.uniform(closedSites.size());
                int site = closedSites.get(ind);
                closedSites.remove(ind);
                final int[] rc = getRowColFromLinear(site, n);
                percolation.open(rc[0], rc[1]);
            }
            percolationThresholds[t] = (double) percolation.numberOfOpenSites() / (nsquare);
        }
    }

    // test client (described below)
    public static void main(String[] args) {

        int n = StdIn.readInt();
        int t = StdIn.readInt();
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.println("mean                    = " + percolationStats.mean());
        StdOut.println("stddev                  = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }
}
