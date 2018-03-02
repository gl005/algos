import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        if (StdIn.isEmpty()) {
            throw new IllegalArgumentException("Text input provided");
        }
        if (args.length == 0) {
            throw new IllegalArgumentException("Length argument required");
        }

        String[] val = StdIn.readAllStrings();
        int k = Integer.valueOf(args[0]);

        for (int i = 0; i < k; i++) {
            System.out.println(val[StdRandom.uniform(k+1)]);
        }
    }
}