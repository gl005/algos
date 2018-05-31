import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver {

    private final Board initialBoard;

    private boolean isSolvable;

    private int numberOfSteps;

    private List<Board> path;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.initialBoard = initial;
        Comparator<Board> hammingComparator = (boardA, boardB) -> {
            int priorityA = boardA.hamming() + boardA.manhattan();
            int priorityB = boardB.hamming() + boardA.manhattan();
            if (priorityA > priorityB) {
                return 1;
            }
            if (priorityA == priorityB) {
                return 0;
            } else {
                return -1;
            }
        };
        MinPQ<Board> frontier = new MinPQ<Board>(initialBoard.dimension(), hammingComparator);
        path = new ArrayList<>();

        frontier.insert(initialBoard);
        while (!frontier.isEmpty()) {
            Board current = frontier.delMin();
            int currentHamming = current.hamming();
            boolean solvable = false;
            path.add(current);

            if (current.isGoal()) {
                break;
            }

            for (Board neighbour : current.neighbors()) {
                frontier.insert(neighbour);
                if (neighbour.hamming() < currentHamming) {
                    solvable = true;
                }
            }

            if (solvable == false) {
                isSolvable = solvable;
                break;
            }
        }
        numberOfSteps = isSolvable ? path.size() -1: -1;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return numberOfSteps;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return path;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        Board a = new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}});
        Solver solver = new Solver(a);

        for (Board board : solver.solution()) {
            System.out.println(board);
            System.out.println(" ");
        }
    }
}
