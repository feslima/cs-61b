import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double[][] energy;
    private double[][] cost;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        computePictureProperties(picture);
    }

    private void computePictureProperties(Picture picture) {
        width = picture.width();
        height = picture().height();
        energy = new double[height][width];
        cost = new double[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy(i, j);
            }
        }

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                double eij = energy[j][i];

                if (j == 0) {
                    cost[j][i] = eij;
                } else {
                    double[] row = cost[j - 1];

                    double middle = row[i];
                    double left = i == 0 ? Double.POSITIVE_INFINITY : row[i - 1];
                    double right = i == width - 1 ? Double.POSITIVE_INFINITY : row[i + 1];
                    double[] tmp = new double[]{left, middle, right};
                    int minIdx = getMinIdx(tmp);

                    cost[j][i] = eij + tmp[minIdx];
                }
            }
        }

    }

    public Picture picture()                       // current picture
    {
        return picture;
    }

    public int width()                         // width of current picture
    {
        return width;
    }

    public int height()                        // height of current picture
    {
        return height;
    }

    public double energy(int x, int y)            // energy of pixel at column x and row y
    {
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

    private void transposeCost() {
        // not so efficient, but meh, it was in the assignment...
        double[][] transposed = new double[cost[0].length][cost.length];
        for (int i = 0; i < cost.length; i++) {
            for (int j = 0; j < cost[0].length; j++) {
                transposed[j][i] = cost[i][j];
            }
        }
        cost = transposed;
        width = transposed[0].length;
        height = transposed.length;
    }

    public int[] findHorizontalSeam()            // sequence of indices for horizontal seam
    {
        transposeCost();
        int[] ints = findVerticalSeam();
        transposeCost(); // restore picture to original
        return ints;
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

    public int[] findVerticalSeam()              // sequence of indices for vertical seam
    {
        // algorithm minimizes from bottom to top because the minimum cost will be found at bottom
        int[] path = new int[cost.length];
        int current = cost.length - 1;
        double[] row = cost[cost.length - 1];
        path[current] = getMinIdx(row);
        int pointer = path[current];
        for (int i = current - 1; i >= 0; i--) {
            row = cost[i];

            pointer = path[i + 1];
            double middle = row[pointer];
            double left = pointer == 0 ? middle : row[pointer - 1]; // repeat middle when already on column 0
            double right = pointer == width - 1 ? middle : row[pointer + 1]; // repeat middle when already on rightmost

            row = new double[]{left, middle, right};
            int minIdx = getMinIdx(row);

            if (minIdx == 0) {
                if (pointer > 0) {
                    pointer -= 1; // only modify path if not on borders
                }
            } else if (minIdx == 2) {
                if (pointer < width - 1) {
                    pointer += 1;
                }
            }
            path[i] = pointer;
        }
        return path;
    }

    private void validateSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Seam differs by more than 1 at elements " + i + " and " + (i + 1));
            }
        }
    }

    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
    {
        if (seam.length > width) {
            throw new IllegalArgumentException("Seam larger than width.");
        }
        validateSeam(seam);
        Picture pic = SeamRemover.removeHorizontalSeam(picture, seam);
        picture = pic;
        computePictureProperties(pic);
    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from picture
    {
        if (seam.length > height) {
            throw new IllegalArgumentException("Seam larger than height.");
        }
        validateSeam(seam);
        Picture pic = SeamRemover.removeVerticalSeam(picture, seam);
        picture = pic;
        computePictureProperties(pic);
    }
}
