import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;



public class FastCollinearPoints {

    private final List<LineSegment> segments = new ArrayList<>();

    private final List<String> segmentsFound = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        checkDuplicateEntries(points);

        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Point[] sortingCopy = Arrays.copyOf(points, points.length);

        for (Point point : pointsCopy) {
            Arrays.sort(sortingCopy, point.slopeOrder());
            extractCollinearPoints(sortingCopy);
        }
    }

    private void extractCollinearPoints(Point[] points) {
        Double lastSlope = null;
        Point start = points[0];

        List<Point> collinearPoints = new ArrayList<>();

        for (int i = 1; i < points.length; i++) {
            Point curPoint = points[i];
            double currentSlope = start.slopeTo(curPoint);

            // new slope
            if (lastSlope == null || currentSlope != lastSlope) {
                // last slope had 3 or more collinear points, break iteration
                if (collinearPoints.size() > 3) {
                    break;
                }
                lastSlope = currentSlope;
                collinearPoints = new ArrayList<>();
                collinearPoints.add(start);
            }

            collinearPoints.add(curPoint);
        }
        if (collinearPoints.size() < 4) {
            return;
        }
        pushSegment(orderedSegment(collinearPoints));
    }

    private LineSegment orderedSegment(List<Point> collinearPoints) {
        Point startPoint = collinearPoints.get(0);
        Point endPoint = collinearPoints.get(1);

        // find out which 2 points are the outliers and use them as from and to points of the line segment
        for (Point collinearPoint : collinearPoints) {
            if (collinearPoint == null) {
                break;
            }

            if (collinearPoint.compareTo(endPoint) > 0) {
                endPoint = collinearPoint;
            } else if (collinearPoint.compareTo(startPoint) < 0) {
                startPoint = collinearPoint;
            }
        }
        return new LineSegment(startPoint, endPoint);
    }

    private void pushSegment(LineSegment segment) {
        if (segment == null || segmentsFound.contains(segment.toString())) {
            return;
        }
        segments.add(segment);
        segmentsFound.add(segment.toString());
    }


    private void checkDuplicateEntries(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Points can not be null");
            }
            if (i == points.length -1) {
                break;
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null) {
                    throw new IllegalArgumentException("Points can not be null");
                }
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicated entries in given points.");
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }


//    public static void main(String[] args) {
//        Point p1 = new Point(10,10);
//        Point p2 = new Point(5,5);
//        Point p3 = new Point(8,8);
//        Point p4 = new Point(19,19);
//        Point p5 = new Point(7,6);
//        Point p6 = new Point(7,7);
//        Point p7 = new Point(7,3);
//        Point p8 = new Point(7,1);
//        Point p9 = new Point(8,1);
//        Point p10 = new Point(9,1);
//        FastCollinearPoints fcp = new FastCollinearPoints(new Point[] {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10});
//    }
}