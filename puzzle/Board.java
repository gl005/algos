import java.util.Arrays;
import java.util.Collections;

public class Board {

    protected final int[][] blocks;
    private int dimension;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)  {
        this.blocks = deepCopy(blocks);
        dimension = this.blocks.length;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        return 0;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int i = 1;
        for (int[] column : blocks) {
            for (int cell : column) {
                if (cell != i) {
                    return false;
                }
                else {
                    i++;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        return new Board(blocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        return this == y || Arrays.deepEquals(blocks, ((Board)y).blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return Collections.emptyList();
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        return "";
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        Board a = new Board(new int[][]{{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,10,15}});
        Board b = new Board(new int[][]{{0,4,8,12},{1,5,9,13},{2,6,10,14},{3,7,10,15}});

        System.out.println(a.equals(b));
    }

    private int[][] deepCopy(int[][] original) {
        if (original == null) {
            return null;
        }

        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}