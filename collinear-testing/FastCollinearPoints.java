import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;

    private LineSegment[] lineSegments;

    private int lineSegmentCount = 0;

    private final int numPoints;

    public FastCollinearPoints(Point[] points) {
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
        this.numPoints = points.length;
    }

    public int numberOfSegments() {
        return lineSegmentCount;
    }

    public LineSegment[] segments() {
        if (lineSegments != null) {
            return lineSegments;
        }

        Point[] copy = Arrays.copyOf(points, numPoints);

        for (Point point : copy) {
            Arrays.sort(points, point.slopeOrder());
            pushSegment(extractCollinearPoints());
        }

        lineSegments = Arrays.copyOfRange(lineSegments, 0, lineSegmentCount);
        return lineSegments;
    }

    private void pushSegment(LineSegment segment) {
        if (lineSegments == null) {
            lineSegments = new LineSegment[numPoints];
        }

        if (segment == null) {
            return;
        }

        lineSegments[lineSegmentCount] = segment;
        lineSegmentCount++;
    }

    private LineSegment extractCollinearPoints() {

        Double lastSlope = null;
        Point start = points[0];
        int colinear = 0;
        Point[] collinearPoints = new Point[10];

        collinearPoints[0] = start;

        for (int i = 0; i < numPoints; i++) {

            Point curPoint = points[i];
            if (curPoint == start) continue;

            double currentSlope = start.slopeTo(curPoint);

            // new slope
            if (lastSlope == null || currentSlope != lastSlope) {
                // last slope had 3 or more collinear points, break iteration
                if (colinear >= 2) {
                    break;
                }
                colinear = 1;
                lastSlope = currentSlope;
            }
            else {
                colinear++;
            }

            collinearPoints[colinear] = curPoint;
        }

        if (colinear < 3) {
            return null;
        }

        return orderedSegment(collinearPoints);
    }

    private LineSegment orderedSegment(Point[] collinearPoints) {
        Point startPoint = collinearPoints[0];
        Point endPoint = collinearPoints[1];

        // sort collinear points
        for (Point collinearPoint : collinearPoints) {
            if (collinearPoint == null) {
                break;
            }

            if (collinearPoint.compareTo(endPoint) > 0) {
                endPoint = collinearPoint;
            }
            else if (collinearPoint.compareTo(startPoint) < 0) {
                startPoint = collinearPoint;
            }
        }
        return new LineSegment(startPoint, endPoint);
    }

    public static void main(String[] args) {
        Point p1 = new Point(3,3); // a
        Point p2 = new Point(5,7);
        Point p3 = new Point(4,4); // a
        Point p4 = new Point(1,1); // a
        Point p5 = new Point(5,1);
        Point p6 = new Point(6,6); // a|b
        Point p7 = new Point(7,50);
        Point p8 = new Point(3,8);
        Point p9 = new Point(6, 3); // b
        Point p10 = new Point(6, 1); // b
        Point p11 = new Point(6, 2); // b
        Point p12 = new Point(0, 0); // a
        Point p13 = new Point(15, 15); // a


        Point[] allPoints = new Point[]{p1,p2,p3,p4,p5,p6,p7,p8,p9,p11,p10, p12, p13};

        FastCollinearPoints pfcp = new FastCollinearPoints(allPoints);
        LineSegment[] segments = pfcp.segments();

    }
}
