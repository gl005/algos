import java.util.Arrays;

public class BruteCollinearPoints {

    private static final int K = 4;

    private final Point[] points;

    private LineSegment[] lineSegments;

    private final int numPoints;

    private final int numCombinations;

    private int numCollinear = 0;


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points can not be null");
        }

        numPoints = points.length;
        this.points = Arrays.copyOf(points, numPoints);
        Point[] foundPoints = new Point[numPoints];

        for (int i = 0; i < numPoints; i++) {
            Point point = this.points[i];
            if (point == null) {
                throw new IllegalArgumentException("Null point");
            }

            // check if point is duplicate
            for (Point foundPoint : foundPoints) {
                if (foundPoint == null) {
                    break;
                }
                if (foundPoint.equals(point)) {
                    throw new IllegalArgumentException("Duplicate point: "+point.toString());
                }
            }
            foundPoints[i] = point;
        }
        numCombinations = numberOfCombinations();
        findSegments();
    }

    // the number of line segments
    public int numberOfSegments() {
        return numCollinear;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(lineSegments, numCollinear);
    }

    // the line segments
    private void findSegments() {
        if (lineSegments != null) {
            return;
        }

        int[] s = new int[K];
        // first index sequence: 0, 1, 2, ...
        for (int i = 0; i < K; i++) {
            s[i] = i;
        }

        pushSegment(getSubset(points, s));

        while (true) {
            int i;
            // find position of item that can be incremented
            for (i = K - 1; i >= 0 && s[i] == numPoints - K + i; i--);
            if (i < 0) {
                break;
            }
            s[i]++;                    // increment this item
            for (++i; i < K; i++) {    // fill up remaining items
                s[i] = s[i - 1] + 1;
            }
            // subsets[setIndex] = getSubset(points, s);
            pushSegment(getSubset(points, s));
        }

        // trim array
        LineSegment[] temp = new LineSegment[numCollinear];
        for (int i = 0; i < numCollinear; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }

    private void pushSegment(Point[] pointSet) {
        if (lineSegments == null) {
            lineSegments = new LineSegment[numCombinations];
        }

        LineSegment collinear = extractCollinearPoints(pointSet);
        if (collinear != null) {
            lineSegments[numCollinear] = collinear;
            numCollinear++;
        }
    }

    private LineSegment extractCollinearPoints(Point[] pointSet) {
        Double firstSlope = null;
        Point from = null;
        Point to = null;
        for (Point point : pointSet) {
            if (from == null) {
                from = point;
                continue;
            } else if (to == null) {
                to = point;
                if (to.compareTo(from) < 0) {
                    Point oldTo = to;
                    to = from;
                    from = oldTo;
                }
            }

            if (firstSlope == null) {
                firstSlope = from.slopeTo(to);
                continue;
            }

            double newSlope = to.slopeTo(point);
            newSlope = newSlope == Double.NEGATIVE_INFINITY ? 0 : newSlope;

            if (newSlope - firstSlope < 0.00001) {
                return null;
            }

            if (point.compareTo(to) > 0) {
                to = point;
            }
            else if (point.compareTo(from) < 0) {
                from = point;
            }
        }
        return new LineSegment(from, to);
    }

    // generate actual subset by index sequence
    private Point[] getSubset(Point[] input, int[] subset) {
        Point[] result = new Point[subset.length];
        for (int i = 0; i < subset.length; i++) {
            result[i] = input[subset[i]];
        }
        return result;
    }

    private int numberOfCombinations()
    {
        int permutations = 1;
        for (int i = 0; i < K; i++) {
            int factor = numPoints-i;
            permutations = permutations * factor;
        }
        return permutations/ 24;
    }

    // test method
    public static void main(String[] args) {
    }
}
