import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {

    private final Board initialBoard;

    private SearchNode goalNode;

    private boolean solvable = false;

    private final static Comparator<SearchNode> MANHATTAN_COMPARATOR = (nodeA, nodeB) -> {
        int manhattanResult = Comparator.comparingInt(SearchNode::getManhattanPriority).compare(nodeA, nodeB);
        if (manhattanResult == 0) {
            return Comparator.comparingInt(SearchNode::getHammingPriority).compare(nodeA, nodeB);
        }
        return manhattanResult;
    };

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.initialBoard = initial;
        solvePuzzle();
    }

    private void solvePuzzle() {

        MinPQ<SearchNode> frontier = new MinPQ<>(initialBoard.dimension(), MANHATTAN_COMPARATOR);
        SearchNode currentNode = new SearchNode(initialBoard, 0, false, null);
        SearchNode twinNode = new SearchNode(initialBoard.twin(), 0, true, null);

        frontier.insert(currentNode);
        frontier.insert(twinNode);

        int step = 0;
        int twinStep = 0;

        while (!frontier.isEmpty()) {
            currentNode = frontier.delMin();
            for (Board neighbour : currentNode.getBoard().neighbors()) {
                if (currentNode.getPrevious() != null && currentNode.getPrevious().getBoard().equals(neighbour)) {
                    continue;
                }

                if (currentNode.isTwin()) {
                    twinStep++;
                }
                else {
                    step++;
                }

                SearchNode nextNode = new SearchNode(neighbour, currentNode.isTwin() ? twinStep: step, currentNode.isTwin(), currentNode);
                // todo: check if node has been visited before adding it to the frontier, pretty hard if we're not allowed to implement hashCode
                frontier.insert(nextNode);

                if (nextNode.isGoalNode()) {
                    goalNode = nextNode;
                    solvable = !goalNode.isTwin();
                    return;
                }
            }
        }

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        Deque<Board> steps = composeSolution();
        return solvable && steps != null ? (steps.size() - 1): -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return composeSolution();
    }

    private Deque<Board> composeSolution() {
        if (!solvable) {
            return null;
        }
        Deque<Board> solution = new Deque<>();
        SearchNode current = goalNode;
        while(!current.isStartNode()) {
            solution.addFirst(current.getBoard());
            current = current.getPrevious();
        }
        solution.addFirst(current.getBoard());
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        testPuzzle(new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}}));

        //unsolvable
        testPuzzle(new Board(new int[][]{{1,2,3},{4,5,6},{8,7,0}}));
    }

    private static void testPuzzle(Board board) {
        Solver solver = new Solver(board);
        System.out.println("solvable? " + solver.isSolvable());
        if (!solver.isSolvable()) return;
        for (Board solutionStep : solver.solution()) {
            System.out.println(solutionStep);
            System.out.println(" ");
        }
    }

    private class SearchNode {
        private final Board board;
        private final int steps;
        private final SearchNode previous;
        private final boolean isTwin;

        private SearchNode(Board board, int steps, boolean isTwin, SearchNode previous) {
            this.board = board;
            this.steps = steps;
            this.previous = previous;
            this.isTwin = isTwin;
        }

        boolean isTwin() {
            return isTwin;
        }

        boolean isGoalNode() {
            return board.isGoal();
        }

        Board getBoard() {
            return board;
        }

        SearchNode getPrevious() {
            return previous;
        }

        boolean isStartNode() {
            return previous == null;
        }

        int getHammingPriority() {
            return steps + board.hamming();
        }

        int getManhattanPriority() {
            return steps + board.manhattan();
        }
    }
}
