import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private int blankX;
    private int blankY;

    private final int[][] blocks;
    private final int dimension;
    private final List<Integer> manhattanDistances = new ArrayList<>();
    private final StringBuilder stringBuilder;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)  {
        this.blocks = deepCopy(blocks);
        this.dimension = this.blocks.length;

        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                int value = this.blocks[y][x];
                int targetY, targetX;
                sb.append(value == 0 ? " " : value).append("\t");

                if (value == 0) {
                    this.blankX = x;
                    this.blankY = y;
                }

                targetY = getValueTargetY(value);
                targetX = getValueTargetX(value, targetY);

                if (targetY != y || targetX != x) {
                    int manhattanDistance = Math.abs(targetX - x) + Math.abs(targetY - y);
                    manhattanDistances.add(manhattanDistance);
                }
            }
            sb.append("\n");
        }
        stringBuilder = sb;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        return manhattanDistances.size();
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (int distance : manhattanDistances) {
            sum += distance;
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattanDistances.isEmpty();
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int fromX = -1;
        int fromY = -1;
        int toX = -1;
        int toY = -1;

        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (y == blankY && x == blankX) continue;

                if (fromX < 0) {
                    fromX = x;
                    fromY = y;
                    continue;
                }

                if (toX < 0) {
                    toX = x;
                    toY = y;
                    break;
                }
            }
        }
        return swap(fromY, fromX, toY, toX);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<>(4);

        // check lower neighbour
        if (blankY < dimension-1) {
            neighbours.add(swap(blankY, blankX,  blankY+1, blankX));
        }
        // check upper neighbour
        if (blankY > 0) {
            neighbours.add(swap(blankY, blankX, blankY-1, blankX));
        }
        // check right neighbour
        if (blankX < dimension-1) {
            neighbours.add(swap(blankY, blankX, blankY, blankX+1));
        }
        // check left neighbour
        if (blankX > 0) {
            neighbours.add(swap(blankY, blankX, blankY, blankX - 1));
        }
        return neighbours;

    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        return this == y || ((Board) y).getHash().equals(getHash());
    }

    private String getHash() {
        return stringBuilder.toString().replaceAll("/\n\t /", "");
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
       return stringBuilder.toString();
    }

    private int getValueTargetX(int value, int targetY) {
        if (value == 0) {
            // noinspection SuspiciousNameCombination
            return targetY;
        }
        return value - ((dimension * targetY) + 1);
    }

    private int getValueTargetY(int value) {
        if (value == 0) {
            return dimension - 1;
        }
        return (int) Math.ceil(value / (double) dimension) -1;
    }

    private Board swap(int fromY, int fromX, int toY, int toX) {
        int[][] copy = deepCopy(blocks);

        int fromValue = copy[fromY][fromX];
        int toValue = copy[toY][toX];
        copy[toY][toX] = fromValue;
        copy[fromY][fromX] = toValue;

        return new Board(copy);
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

    // unit tests (not graded)
    public static void main(String[] args) {
        /*
        Board a = new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}});

        System.out.print(a.toString());
        System.out.println("steps to goal: " + a.manhattan());
        System.out.println("blocks out of place: " + a.hamming());
        System.out.println("is goal: " + a.isGoal());
        System.out.println(" ");

        for (Board board : a.neighbors()) {
            System.out.print(board.toString());
            //System.out.println("is goal: "+board.isGoal());
            System.out.println("steps to goal: " + board.manhattan());
            System.out.println("blocks out of place: " + board.hamming());
            System.out.println(" ");
        }
        */
    }
}