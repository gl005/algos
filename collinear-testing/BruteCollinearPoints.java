import javax.swing.text.Segment;

public class BruteCollinearPoints {

    private final Point[] points;


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
    }

    private void findSegments() {

        for (Point p: points) {
            for (Point q: points) {
                if (q == p) {
                    continue;
                }
                double slope = p.slopeTo(q);
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return 0;
    }

    // the line segments
//    public LineSegment[] segments() {
//    }

    public static void main(String[] args) {
        Point p1 = new Point(1,1);
        Point p2 = new Point(1,2);
        Point p3 = new Point(2,2);

        BruteCollinearPoints pfcp = new BruteCollinearPoints(new Point[]{p1, p2, p3, p1});


    }

    private class SegmentContainer {
        private LineSegment[] segments = new LineSegment[16];

        private final double slope;

        private int count = 0;

        public SegmentContainer(double slope) {
            this.slope = slope;
        }

        public void addSegment(LineSegment ls) {
            if (count == segments.length-1) {
                throw new IllegalStateException("Too many segments");
            }
            segments[count] = ls;
            count++;
        }

    }
}
