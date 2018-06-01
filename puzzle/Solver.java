import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {

    private final Board initialBoard;

    private SearchNode goalNode;

    private final static Comparator<SearchNode> MANHATTAN_COMPARATOR = (nodeA, nodeB) -> {
        int manhattanResult = Comparator.comparingInt(SearchNode::getManhattenPriority).compare(nodeA, nodeB);
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
        SearchNode currentNode = new SearchNode(initialBoard, 0, null);

        int step = 0;
        Set<Board> visited = new HashSet<>();
        for (frontier.insert(currentNode); !frontier.isEmpty(); currentNode = frontier.delMin()) {
            for (Board neighbour : currentNode.getBoard().neighbors()) {
                if (currentNode.getBoard().equals(neighbour) ||
                        (currentNode.getPrevious() != null && currentNode.getPrevious().getBoard().equals(neighbour))) {
                    continue;
                }

                SearchNode nextNode = new SearchNode(neighbour, step, currentNode);
                if (!visited.contains(neighbour)) {
                    step++;
                    frontier.insert(nextNode);
                    visited.add(neighbour);
                }

                if (nextNode.isGoalNode()) {
                    goalNode = nextNode;
                    return;
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return goalNode == null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return goalNode == null ? -1: (composeSolution().size()-1);
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return composeSolution();
    }

    private Deque<Board> composeSolution() {
        if (goalNode == null) {
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
        //testPuzzle(new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}}));

        //unsolvable
        testPuzzle(new Board(new int[][]{{1,2,3},{4,5,6},{8,7,0}}));
    }

    private static void testPuzzle(Board board) {
        Solver solver = new Solver(board);
        System.out.println(solver.isSolvable());
        for (Board solutionStep : solver.solution()) {
            System.out.println(solutionStep);
            System.out.println(" ");
        }
    }

    private class SearchNode {
        private final Board board;
        private final int steps;
        private final SearchNode previous;

        private SearchNode(Board board, int steps, SearchNode previous) {
            this.board = board;
            this.steps = steps;
            this.previous = previous;
        }

        public boolean isGoalNode() {
            return board.isGoal();
        }

        public int getSteps() {
            return steps;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public boolean isStartNode() {
            return previous == null;
        }

        public int getHammingPriority() {
            return steps + board.hamming();
        }

        public int getManhattenPriority() {
            return steps + board.manhattan();
        }
    }
}
