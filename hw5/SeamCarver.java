import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.ArrayList;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double[][] energy;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        computePictureProperties(picture);
    }

    private void computePictureProperties(Picture picture) {
        width = picture.width();
        height = picture().height();
        energy = new double[height][width];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy(i, j);
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

    private void transposeEnergy() {
        // not so efficient, but meh, it was in the assignment...
        double[][] transposed = new double[energy[0].length][energy.length];
        for (int i = 0; i < energy.length; i++) {
            for (int j = 0; j < energy[0].length; j++) {
                transposed[j][i] = energy[i][j];
            }
        }
        energy = transposed;
        width = transposed[0].length;
        height = transposed.length;
    }

    public int[] findHorizontalSeam()            // sequence of indices for horizontal seam
    {
        transposeEnergy();
        int[] ints = findVerticalSeam();
        transposeEnergy(); // restore picture to original
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
        int[] path = new int[energy.length];
        int current = 0;
        double[] row = energy[0];
        path[current] = getMinIdx(row);
        current = path[current];
        for (int i = 1; i < energy.length; i++) {
            row = energy[i];

            double middle = row[current];
            double left = current == 0 ? Double.POSITIVE_INFINITY : row[current - 1];
            double right = current == width - 1 ? Double.POSITIVE_INFINITY : row[current + 1];

            row = new double[]{left, middle, right};
            int minIdx = getMinIdx(row);

            if (minIdx == 0) {
                current -= 1;
            } else if (minIdx == 2) {
                current += 1;
            }
            path[i] = current;
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
