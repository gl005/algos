import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        if (StdIn.isEmpty()) {
            throw new IllegalArgumentException("Text input provided");
        }
        if (args.length == 0) {
            throw new IllegalArgumentException("Length argument required");
        }

        RandomizedQueue<String> vals = new RandomizedQueue<>();
        while (StdIn.hasNextLine()) {
            while (StdIn.hasNextChar()) {
                if (StdIn.readChar() == '\n') {
                    break;
                }
                vals.enqueue(StdIn.readString());
            }
        }

        int k = Integer.parseInt(args[0]);

        for (int i = 0; i < k; i++) {
            System.out.println(vals.sample());
        }
    }
}