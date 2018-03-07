import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;

    private final int y;

    // constructs the point (x, y)
    public Point(int x, int y) {
       this.x = x;
       this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // string representation
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    // compare two points by y-coordinates, breaking ties by x-coordinates
    public int compareTo(Point that) {
        int diff = getY() - that.getY();
        if (diff < 0) {
            return -1;
        }
        else if (diff == 0) {
            if (getX() < that.getX()) {
                return -1;
            }
            return 0;
        }
        else {
            return 1;
        }
    }

    public boolean isGreatherThan(Point p) {
        return p.getX() < getX() || p.getY() < getY();
    }

    // the slope between this point and that point
    public double slopeTo(Point that) {

        if (this.compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        }
        int changeInX = (that.getX() - getX());
        int changeInY = (that.getY() - getY());

        if (changeInY == 0) {
            return 0;
        }
        else if (changeInX == 0) {
           return Double.POSITIVE_INFINITY;
        }

        return changeInY/ changeInX;
    }

    // compare two points by slopes they make with this point
    public Comparator<Point> slopeOrder() {
        return new BySlope();
    }

    private class BySlope implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            double slope0To1 = slopeTo(o1);
            double slope0To2 = slopeTo(o2);

            if (slope0To1 < slope0To2) {
                return -1;
            }
            else if (slope0To1 == slope0To2) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }
}
