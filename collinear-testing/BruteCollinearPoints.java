public class BruteCollinearPoints {

    private final Point[] points;

    private LineSegment[] lineSegments;

    private final int numPoints;

    private final int numCombinations;

    private int numCollinear = 0;

    private static final int K = 4;
    private static final int K_FACTORIAL = 24; // 4! = 24 hardcoded for convenience


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points can not be null");
        }

        this.points = points;
        Point[] foundPoints = new Point[this.points.length];
        for (int i = 0; i < this.points.length; i++) {
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
        numPoints = points.length;
        numCombinations = numberOfCombinations();
        findSegments();
    }

    // the number of line segments
    public int numberOfSegments() {
        return numCollinear;
    }

    public LineSegment[] segments() {
        return lineSegments;
    }

    // the line segments
    public void findSegments() {
        if (lineSegments != null) {
            return;
        }

        int[] s = new int[K];
        // first index sequence: 0, 1, 2, ...
        for (int i = 0; i < K; i++) {
            s[i] = i;
        }

        pushSegment(getSubset(points, s));

        for(;;) {
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
            //subsets[setIndex] = getSubset(points, s);
            pushSegment(getSubset(points, s));
        }

        //trim array
        LineSegment[] temp = new LineSegment[numCollinear];
        for (int i = 0; i < numCollinear; i++) {
            temp[i] = lineSegments[i];
        }
        lineSegments = temp;
    }

    private void pushSegment(Point[] points) {
        if (lineSegments == null) {
            lineSegments = new LineSegment[numCombinations];
        }

        LineSegment collinear = collinear(points);
        if (collinear != null) {
            lineSegments[numCollinear] = collinear;
            numCollinear++;
        }
    }

    private LineSegment collinear(Point[] pointSet) {
        Double firstSlope = null;
        Point from = null;
        Point to = null;
        for (Point point : pointSet) {
            if (from == null) {
                from = point;
                continue;
            } else if (to == null) {
                to = point;
                if (!to.isGreatherThan(from)) {
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
            newSlope = newSlope == Double.NEGATIVE_INFINITY ? 0: newSlope;

            if (newSlope != firstSlope) {
                return null;
            }

            if (point.isGreatherThan(to)) {
                to = point;
            }
            else if (from.isGreatherThan(point)) {
                from = point;
            }
        }
        return new LineSegment(from, to);
    }

    // generate actual subset by index sequence
    private Point[] getSubset(Point[] input, int[] subset) {
        Point[] result = new Point[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = input[subset[i]];
        return result;
    }

    private int numberOfCombinations()
    {
        int permutations = 1;
        for (int i = 0; i < K; i++) {
            int factor = numPoints-i;
            permutations = permutations * factor;
        }
        return permutations/ K_FACTORIAL;
    }

    public static void main(String[] args) {

        Point p1 = new Point(3,3);
        Point p2 = new Point(5,7);
        Point p3 = new Point(4,4);
        Point p4 = new Point(1,1);
        Point p5 = new Point(5,1);
        Point p6 = new Point(6,6);
        Point p7 = new Point(7,50);
        Point p8 = new Point(3,8);
        Point p9 = new Point(6, 3);
        Point p10 = new Point(6, 1);
        Point p11 = new Point(6, 2);

        Point[] allPoints = new Point[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p11,p10};

        BruteCollinearPoints pfcp = new BruteCollinearPoints(allPoints);
        for (Point point : allPoints) {
            point.draw();
        }

        LineSegment[] segments = pfcp.segments();
        for (LineSegment segment : segments) {
            segment.draw();
        }

    }
}
