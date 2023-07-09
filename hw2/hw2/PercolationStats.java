package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

import java.util.ArrayList;

public class PercolationStats {
    private final int T;
    private final int N;
    private final ArrayList<Double> thresholdValues;
    private final PercolationFactory pf;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("T and N must be greater than 0.");
        }

        this.N = N;
        this.T = T;
        this.pf = pf;
        thresholdValues = new ArrayList<>();

        for (int i = 0; i < T; i++) {
            thresholdValues.add(performExperiment());
        }
    }

    private double performExperiment() {
        ArrayList<Integer> sampledSites = new ArrayList<>();
        for (int i = 0; i < N * N; i++) {
            sampledSites.add(i);
        }

        Percolation percolation = pf.make(N);
        while (!percolation.percolates()) {
            int siteSampleIdx = StdRandom.uniform(sampledSites.size());
            int siteIdx = sampledSites.remove(siteSampleIdx);
            int row = getRow(siteIdx);
            int col = getCol(siteIdx);

            percolation.open(row, col);
        }

        return (double) percolation.numberOfOpenSites() / N;
    }

    private int getRow(int idx) {
        return idx / N;
    }

    private int getCol(int idx) {
        return idx % N;
    }

    public double mean() {
        double[] values = new double[thresholdValues.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = thresholdValues.get(i);
        }
        return StdStats.mean(values);
    }

    public double stddev() {
        double[] values = new double[thresholdValues.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = thresholdValues.get(i);
        }
        return StdStats.stddev(values);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    public static void main(String[] args) {
    }
}
