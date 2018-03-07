import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {

    private final Point[] points;

    private final int numPoints;

    private final int numCombinations;

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
    }

    private Point[][] findKCombinations() {
        //for all combinations of K=4 of the array, check if all points are collinear
        Point[][] subsets = new Point[numCombinations][K];
        int setIndex = 0;

        int[] s = new int[K];
        // first index sequence: 0, 1, 2, ...
        for (int i = 0; i < K; i++) {
            s[i] = i;
        }

        subsets[setIndex] = getSubset(points, s);
        setIndex++;
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
            subsets[setIndex] = getSubset(points, s);
            setIndex++;
        }
        return subsets;
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

    // the number of line segments
    public int numberOfSegments() {
        return 0;
    }

    // the line segments
//    public LineSegment[] segments() {
//    }

    public static void main(String[] args) {
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p5 = new Point(5,5);
        Point p6 = new Point(6,6);
        Point p7 = new Point(7,7);
        Point p8 = new Point(8,8);

        BruteCollinearPoints pfcp = new BruteCollinearPoints(new Point[]{p1, p2, p3, p4, p5, p6, p7, p8});
//        for (Point[] points : pfcp.findSegments()) {
//
//            for (Point point : points) {
//                System.out.print(point);
//            }
//            System.out.println("");
//        }


    }
}
