import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private final Picture picture;
    private final int width;
    private final int height;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        width = picture.width();
        height = picture().height();
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

        return dx2 + dy2;
    }

    public int[] findHorizontalSeam()            // sequence of indices for horizontal seam
    {
        int[] ints = new int[]{0};
        return ints;
    }

    public int[] findVerticalSeam()              // sequence of indices for vertical seam
    {
        int[] ints = new int[]{0};
        return ints;
    }

    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from picture
    {
    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from picture
    {
    }
}
