/******************************************************************************
 *  Compilation:  javac PuzzleChecker.java
 *  Execution:    java PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PuzzleChecker {

    public static void main(String[] args) {


        if (args.length == 1 && args[0].equals("ALL")) {
            List<String> allFiles = new ArrayList<>();
            for (int i = 5; i < 44; i++) {
                String num = i < 10 ? "0" + i : String.valueOf(i);
                allFiles.add("data/puzzle" + num + ".txt");
            }
            args = allFiles.toArray(new String[]{});
        }

        // for each command-line argument
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Instant boardStart = Instant.now();
            Board initial = new Board(tiles);
            Instant boardEnd = Instant.now();
            double boardTime = getRunTime(boardStart, boardEnd);

            Instant solverStart = Instant.now();
            Solver solver = new Solver(initial);
            Instant solverEnd = Instant.now();
            double solverTime = getRunTime(solverStart, solverEnd);

            StdOut.println(filename + ";" + solver.moves() + ";" + initial.dimension() + ";" + boardTime + ";" + solverTime + ";");
        }
    }

    private static double getRunTime(Instant start, Instant end) {
        return (double) (end.toEpochMilli() - start.toEpochMilli()) / 1000;
    }


}
