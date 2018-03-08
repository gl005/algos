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

        Point[] copy = Arrays.copyOf(points, numPoints);

        for (Point point : copy) {
            System.out.println(point.toString());
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


        Point[] allPoints = new Point[]{p1,p2,p3,p5,p6,p7,p8,p9,p11,p10, p12, p13};

        FastCollinearPoints pfcp = new FastCollinearPoints(allPoints);
        LineSegment[] segments = pfcp.segments();
        int i = 0;
    }
}
