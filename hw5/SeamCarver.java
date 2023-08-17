import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double[][] energy;
    private double[][] transposedEnergy;
    private double[][] cost;
    private double[][] transposedCost;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        computePictureProperties(picture);
    }

    private double[][] transposeMatrix(double[][] M) {
        double[][] transposed = new double[M[0].length][M.length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                transposed[j][i] = M[i][j];
            }
        }
        return transposed;
    }

    private double[][] computeCostMatrix(double[][] energy) {
        int w = energy[0].length;
        int h = energy.length;
        double[][] computedCost = new double[h][w];

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                double eij = energy[j][i];

                if (j == 0) {
                    computedCost[j][i] = eij;
                } else {
                    double[] row = computedCost[j - 1];

                    double middle = row[i];
                    double left = i == 0 ? Double.POSITIVE_INFINITY : row[i - 1];
                    double right = i == w - 1 ? Double.POSITIVE_INFINITY : row[i + 1];
                    double[] tmp = new double[]{left, middle, right};
                    int minIdx = getMinIdx(tmp);

                    computedCost[j][i] = eij + tmp[minIdx];
                }
            }
        }

        return computedCost;
    }

    private void computePictureProperties(Picture pic) {
        width = pic.width();
        height = picture().height();
        energy = new double[height][width];
        cost = new double[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy(i, j);
            }
        }
        transposedEnergy = transposeMatrix(energy);

        cost = computeCostMatrix(energy);
        transposedCost = computeCostMatrix(transposedEnergy);

    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IndexOutOfBoundsException();
        }

        if (energy[y][x] == 0.0) {
            int xPrevIndex = x == 0 ? width - 1 : x - 1;
            int xNextIndex = x == width - 1 ? 0 : x + 1;
            Color xNext = picture.get(xNextIndex, y);
            Color xPrev = picture.get(xPrevIndex, y);
            double rx = xNext.getRed() - xPrev.getRed();
            double gx = xNext.getGreen() - xPrev.getGreen();
            double bx = xNext.getBlue() - xPrev.getBlue();
            double dx2 = rx * rx + gx * gx + bx * bx;

            int yPrevIndex = y == 0 ? height - 1 : y - 1;
            int yNextIndex = y == height - 1 ? 0 : y + 1;
            Color yNext = picture.get(x, yNextIndex);
            Color yPrev = picture.get(x, yPrevIndex);
            double ry = yNext.getRed() - yPrev.getRed();
            double gy = yNext.getGreen() - yPrev.getGreen();
            double by = yNext.getBlue() - yPrev.getBlue();
            double dy2 = ry * ry + gy * gy + by * by;

            energy[y][x] = dx2 + dy2;
        }

        return energy[y][x];
    }


    public int[] findHorizontalSeam() {
        return findSeam(transposedCost);
    }

    private int getMinIdx(double[] row) {
        int min = 0;
        double minVal = row[min];

        for (int i = min; i < row.length; i++) {
            if (row[i] < minVal) {
                min = i;
                minVal = row[min];
            }
        }

        return min;
    }

    private int[] findSeam(double[][] costMatrix) {
        // algorithm minimizes from bottom to top because the minimum cost will be found at bottom
        int w = costMatrix[0].length;

        int[] path = new int[costMatrix.length];
        int current = costMatrix.length - 1;
        double[] row = costMatrix[costMatrix.length - 1];
        path[current] = getMinIdx(row);
        int pointer;
        for (int i = current - 1; i >= 0; i--) {
            row = costMatrix[i];

            pointer = path[i + 1];
            double middle = row[pointer];
            double left = pointer == 0 ? middle : row[pointer - 1]; // repeat middle when already on column 0
            double right = pointer == w - 1 ? middle : row[pointer + 1]; // repeat middle when already on rightmost

            row = new double[]{left, middle, right};
            int minIdx = getMinIdx(row);

            if (minIdx == 0) {
                if (pointer > 0) {
                    pointer -= 1; // only modify path if not on borders
                }
            } else if (minIdx == 2) {
                if (pointer < w - 1) {
                    pointer += 1;
                }
            }
            path[i] = pointer;
        }
        return path;
    }

    public int[] findVerticalSeam() {
        return findSeam(cost);
    }

    private void validateSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                String msg = "Seam differs by more than 1 at elements " + i + " and " + (i + 1);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam.length > width) {
            throw new IllegalArgumentException("Seam larger than width.");
        }
        validateSeam(seam);
        Picture pic = SeamRemover.removeHorizontalSeam(picture, seam);
        picture = pic;
        computePictureProperties(pic);
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam.length > height) {
            throw new IllegalArgumentException("Seam larger than height.");
        }
        validateSeam(seam);
        Picture pic = SeamRemover.removeVerticalSeam(picture, seam);
        picture = pic;
        computePictureProperties(pic);
    }
}
