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
        this.numPoints = points.length;
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
    }

    public int numberOfSegments() {
        return lineSegmentCount;
    }

    public LineSegment[] segments() {
        if (lineSegments != null) {
            return Arrays.copyOf(lineSegments, lineSegmentCount);
        }

        Point[] copy = Arrays.copyOf(points, numPoints);

        for (Point point : copy) {
            Arrays.sort(points, point.slopeOrder());
            pushSegment(extractCollinearPoints());
        }

        return Arrays.copyOf(lineSegments, lineSegmentCount);
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
            if (lastSlope == null || (currentSlope - lastSlope) < 0.00001) {
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

    // test method
    public static void main(String[] args) {
    }
}
