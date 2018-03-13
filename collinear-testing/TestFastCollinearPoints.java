import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestFastCollinearPoints {

    public static void main(String[] args) throws IOException {

        List<Point> equidistant = parsePointsFromFile("equidistant");
        FastCollinearPoints fcp = new FastCollinearPoints(equidistant.toArray(new Point[equidistant.size()]));
        assert fcp.numberOfSegments() == 4;

        List<Point> input250 = parsePointsFromFile("input250");
        FastCollinearPoints fcp250 = new FastCollinearPoints(input250.toArray(new Point[input250.size()]));
        assert fcp250.numberOfSegments() == 1;

    }

    public static List<Point> parsePointsFromFile(String filename) throws FileNotFoundException {
        String path = "collinear-testing/data/"+ filename +".txt";
        BufferedReader br = new BufferedReader(new FileReader(path));
        return br.lines()
                .filter((line) -> !(line.indexOf(' ') < 0))
                .map((line) -> {
                    List<Integer> coordinates = Arrays.stream(line.trim().split(" "))
                            .filter((word) -> !(word.trim().isEmpty()))
                            .map(Integer::valueOf)
                            .collect(Collectors.toList());
                    return new Point(Integer.valueOf(coordinates.get(0)), Integer.valueOf(coordinates.get(1)));
                })
                .collect(Collectors.toList());
    }

}
