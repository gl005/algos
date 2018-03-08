import java.util.Arrays;

public class FastCollinearPoints {

    private Point[] points;

    private LineSegment[] lineSegments;

    private int numCollinear = 0;

    private int numPoints;

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
        return numCollinear;
    }

    public LineSegment[] segments() {
        if (lineSegments != null) {
            return lineSegments;
        }

        for (Point point : points) {
            Arrays.sort(points, point.slopeOrder());
            pushSegment(checkColinear());
        }

        lineSegments = Arrays.copyOfRange(lineSegments, 0, numCollinear);
        return lineSegments;
    }

    private void pushSegment(LineSegment segment) {
        if (lineSegments == null) {
            lineSegments = new LineSegment[numPoints/3];
        }

        if (segment!= null) {
            lineSegments[numCollinear] = segment;
            numCollinear++;
        }
    }

    public LineSegment checkColinear() {

        Double firstSlope = null;
        Point p = points[0];
        Point t = new Point(0,0);
        int i = 1;

        for (; i < numPoints; i++) {
            Point curPoint = points[i];
            double currentSlope = p.slopeTo(curPoint);
            if (firstSlope == null) {
                t = curPoint;
                firstSlope = currentSlope;
                continue;
            }

            if (currentSlope != firstSlope) {
                break;
            }

            if (curPoint.isGreatherThan(t)) {
                t = curPoint;
            }
            else if (p.isGreatherThan(curPoint)) {
                p = curPoint;
            }
        }
        if (i < 3) {
            return null;
        }
        return new LineSegment(p, t);
    }

    public static void main(String[] args) {

    }
}
