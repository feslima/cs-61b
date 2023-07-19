package hw3.hash;

import java.awt.Color;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;


public class SimpleOomage implements Oomage {
    protected int red;
    protected int green;
    protected int blue;

    private static final double WIDTH = 0.01;
    private static final boolean USE_PERFECT_HASH = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            // are the references equal?
            return true;
        }

        if (o == null) {
            // is the other object null?
            return false;
        }

        if (o.getClass() != this.getClass()) {
            // the classes of the objects are different
            return false;
        }

        boolean isSameRed = this.red == ((SimpleOomage) o).red;
        boolean isSameGreen = this.green == ((SimpleOomage) o).green;
        boolean isSameBlue = this.blue == ((SimpleOomage) o).blue;
        return isSameRed && isSameGreen && isSameBlue;
    }

    @Override
    public int hashCode() {
        if (!USE_PERFECT_HASH) {
            return red + green + blue;
        } else {
            /* Since each color ranges from 0 to 255, this mean that each one can be represented
             * as 8-bit number. Thus, to represent the Oomage as whole, we can use a bit mask of
             * 24 bits to represent this Oomage hash. For example:
             * 00000000 00000000 00000000
             * red      green    blue
             *
             * To achieve this mask, we simply need to shift the red color 16 bits to the left, and the green
             * color 8 bits to the left.*/
            return red * (2 << 16) + green * (2 << 8) + blue;
        }
    }

    public SimpleOomage(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            throw new IllegalArgumentException();
        }
        if ((r % 5 != 0) || (g % 5 != 0) || (b % 5 != 0)) {
            throw new IllegalArgumentException("red/green/blue values must all be multiples of 5!");
        }
        red = r;
        green = g;
        blue = b;
    }

    @Override
    public void draw(double x, double y, double scalingFactor) {
        StdDraw.setPenColor(new Color(red, green, blue));
        StdDraw.filledSquare(x, y, WIDTH * scalingFactor);
    }

    public static SimpleOomage randomSimpleOomage() {
        int red = StdRandom.uniform(0, 51) * 5;
        int green = StdRandom.uniform(0, 51) * 5;
        int blue = StdRandom.uniform(0, 51) * 5;
        return new SimpleOomage(red, green, blue);
    }

    public static void main(String[] args) {
        System.out.println("Drawing 4 random simple Oomages.");
        randomSimpleOomage().draw(0.25, 0.25, 1);
        randomSimpleOomage().draw(0.75, 0.75, 1);
        randomSimpleOomage().draw(0.25, 0.75, 1);
        randomSimpleOomage().draw(0.75, 0.25, 1);
    }

    public String toString() {
        return "R: " + red + ", G: " + green + ", B: " + blue;
    }
} 
