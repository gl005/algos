import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Comparator;

public class Solver {

    private final Board initialBoard;

    private SearchNode goalNode;

    private boolean solvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        this.initialBoard = initial;
        solvePuzzle();
    }

    private void solvePuzzle() {

        MinPQ<SearchNode> frontier = new MinPQ<>(initialBoard.dimension(), (nodeA, nodeB) -> {
            int manhattanResult = Comparator.comparingInt(SearchNode::getManhattanPriority).compare(nodeA, nodeB);
            if (manhattanResult == 0) {
                return Comparator.comparingInt(SearchNode::getHammingPriority).compare(nodeA, nodeB);
            }
            return manhattanResult;
        });
        SearchNode currentNode = new SearchNode(initialBoard, false, null);
        if (currentNode.isGoalNode()) {
            goalNode = currentNode;
            solvable = true;
            return;
        }
        SearchNode twinNode = new SearchNode(initialBoard.twin(), true, null);

        frontier.insert(currentNode);
        frontier.insert(twinNode);

        while (!frontier.isEmpty()) {
            currentNode = frontier.delMin();
            for (Board nextBoard : currentNode.getBoard().neighbors()) {
                if ((currentNode.getPrevious() != null && currentNode.getPrevious().getBoard().equals(nextBoard)) || currentNode.descendsFrom(nextBoard)) {
                    continue;
                }

                SearchNode nextNode = new SearchNode(nextBoard, currentNode.isTwin(), currentNode);
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
        ArrayDeque<Board> steps = getSolution();
        return solvable && steps != null ? (steps.size() - 1) : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return getSolution();
    }

    private ArrayDeque<Board> getSolution() {
        if (!solvable) {
            return null;
        }
        return goalNode.ancestors();
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // testPuzzle(new Board(new int[][]{{0,1,3},{4,2,5},{7,8,6}}));
        // unsolvable
        // testPuzzle(new Board(new int[][]{{1,2,3},{4,5,6},{8,7,0}}));
    }

    private class SearchNode {
        private final Board board;
        private final SearchNode previous;
        private final boolean isTwin;
        private final int hammingPriority;
        private final int manhattenPriority;
        private final boolean goal;

        private SearchNode(Board board, boolean isTwin, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.isTwin = isTwin;
            int steps = depth();
            hammingPriority = steps + board.hamming();
            manhattenPriority = steps + board.manhattan();
            goal = board.isGoal();
        }

        boolean isTwin() {
            return isTwin;
        }

        boolean isGoalNode() {
            return goal;
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
            return hammingPriority;
        }

        int getManhattanPriority() {
            return manhattenPriority;
        }

        boolean descendsFrom(Board board) {
            SearchNode node = this;
            while (node.previous != null) {
                if (node.previous.equals(board)) {
                    return true;
                }
                node = node.previous;
            }
            return false;
        }

        int depth() {
            int curStep = 0;
            SearchNode node = this;
            while (node.previous != null) {
                curStep++;
                node = node.previous;
            }
            return curStep;
        }

        ArrayDeque<Board> ancestors() {
            ArrayDeque<Board> ancestors = new ArrayDeque<>();
            SearchNode current = goalNode;
            while (!current.isStartNode()) {
                ancestors.addFirst(current.getBoard());
                current = current.getPrevious();
            }
            ancestors.addFirst(current.getBoard());
            return ancestors;
        }
    }
}
