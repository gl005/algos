import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Board {

    protected final int[][] blocks;
    private int dimension;
    private int blockCount;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)  {
        this.blocks = deepCopy(blocks);
        dimension = this.blocks.length;
        blockCount = dimension * dimension;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        return blocksOutOfPlace(false).size();
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int sum = 0;
        for (Integer distance: blocksOutOfPlace(false)) {
            sum += distance;
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return blocksOutOfPlace(true).isEmpty();
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        return new Board(blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbours = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int value = blocks[i][j];
                if (value != 0) continue;

                //check upper neighbour
                if (i > 0) {
                    neighbours.add(swap(i, j, i-1, j));
                }
                //check right neighbour
                if (j < dimension-1) {
                    neighbours.add(swap(i, j, i, j+1));
                }
                //check lower neighbour
                if (i < dimension-1) {
                    neighbours.add(swap(i, j,  i+1, j));
                }
                //check left neighbour
                if (j > 0) {
                    neighbours.add(swap(i, j, i, j - 1));
                }
                return neighbours;
            }
        }
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        return this == y || Arrays.deepEquals(blocks, ((Board)y).blocks);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row:blocks) {
            for (int value:row) {
                sb.append((value == 0 ? "-": value) +"\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    private List<Integer> blocksOutOfPlace(boolean breakOnFirst) {
        int place = 1;
        List<Integer> distanceOutOfPlace = new ArrayList<>();

        for (int[] row : blocks) {
            for (int value : row) {
                int stepsToValue = Math.abs(value == 0 ? (blockCount - 1 - place): value - place);

                if ((value == 0 && place != blockCount -1) || place != value) {
                    distanceOutOfPlace.add(stepsToValue);
                }

                if (breakOnFirst && !distanceOutOfPlace.isEmpty()) {
                    return distanceOutOfPlace;
                }
                place++;
            }
        }
        return distanceOutOfPlace;
    }

    private Board swap(int fromRow, int fromCell, int toRow, int toCell) {
        int[][] copy = deepCopy(blocks);

        int fromValue = copy[fromRow][fromCell];
        int toValue = copy[toRow][toCell];
        copy[toRow][toCell] = fromValue;
        copy[fromRow][fromCell] = toValue;

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
        Board a = new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}});
        Board b = new Board(new int[][]{{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}});

        System.out.print(a.toString());
        System.out.println("steps to goal: "+a.manhattan());
        System.out.println("blocks out of place: "+a.hamming());
        System.out.println("is goal: "+a.isGoal());
        System.out.println(" ");

//        for (Board board : a.neighbors()) {
//            System.out.print(board.toString());
//            //System.out.println("is goal: "+board.isGoal());
//            System.out.println("steps to goal: "+board.manhattan());
//            System.out.println("blocks out of place: "+board.hamming());
//            System.out.println(" ");
//        }
//
//        System.out.println(a.equals(b));
//        System.out.println(a.isGoal());
    }
}