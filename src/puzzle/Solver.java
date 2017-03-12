// package puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

/**
 * Created by oderor on 3/12/2017.
 */
public class Solver {
    private final Queue<Board> solution_boards;
    private int moves;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previous;
        private int h = -1; // initially INFINITY

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
        // priority function h(x) = f(x) + g(x)
        private int h() {
            if (h == -1) {
                h = moves + board.manhattan();
            }
            return h;
        }
        @Override
        public int compareTo(SearchNode that) {
            if (this.h() < that.h()) {
                return -1;
            }
            if (this.h() > that.h()) {
                return 1;
            }
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initialBoard) {
        if (initialBoard == null) {
            throw new NullPointerException();
        }

        solution_boards = new Queue<>();
        // if the first SearchNode is the goal, then add it to the list and return
        if (initialBoard.isGoal()) {
            isSolvable = true;
            moves = 0;
            solution_boards.enqueue(initialBoard);
            return;
        }

        // to detect unsolvable puzzles, use the fact that boards are divided into 2 classes w/respect to reachability: 
        // (i) those that lead to the goal board and 
        // (ii) those that lead to the goal board if we modify the initial board by swapping any pair of blocks 
        // (the blank square is not a block).
        // Thus, need to keep an eye on a 'twin' board during the execution.

        // if a twin of the first SearchNode is the goal, then the original one can NOT be solved
        if (initialBoard.twin().isGoal()) {
            isSolvable = false;
            moves = -1;
            return;
        }

        // use a priority queue to maintain the open set (see A* algorithm)
        MinPQ<SearchNode> minPQ = new MinPQ<>(),
                minPQTwin = new MinPQ<>();
        // initialize a starting SearchNode using the initial board, moves = 0 and null as parent reference
        Board board = initialBoard,
                boardTwin = initialBoard.twin();
        moves = 0;
        SearchNode node = new SearchNode(board, moves, null),
                nodeTwin = new SearchNode(boardTwin, moves, null);
        minPQ.insert(node);
        minPQTwin.insert(nodeTwin);

//        while (moves < 100) {
        while (true) {
            node = minPQ.delMin();
            nodeTwin = minPQTwin.delMin();

            board = node.board;
            boardTwin = nodeTwin.board;
            // if the twin board becomes solved, then the original can NOT be solved
            if (boardTwin.isGoal()) {
                isSolvable = false;
                moves = -1;
                return;
            }
            // if the original board becomes solved, add it to the solution and restore solution steps.
            if (board.isGoal()) {
                isSolvable = true;
                solution_boards.enqueue(board);
                // reconstruct 'solution path'
                while (node.previous != null) {
                    node = node.previous;
                    solution_boards.enqueue(node.board);
                }
                return;
            }

            node.moves++;
            nodeTwin.moves++;
            Iterable<Board> neighbors = board.neighbors();
            for (Board neighbor : neighbors) {
                // if one of neighbours is the same as the previous board, do not add it!
                if (node.previous != null && node.previous.board.equals(neighbor)) {
                    continue;
                }
                minPQ.insert(new SearchNode(neighbor, node.moves, node));
            }
            // the same with twin boards...
            Iterable<Board> neighborsTwin = boardTwin.neighbors();
            for (Board neighbor : neighborsTwin) {
                if (nodeTwin.previous != null && nodeTwin.previous.board.equals(neighbor)) {
                    continue;
                }
                minPQTwin.insert(new SearchNode(neighbor, nodeTwin.moves, nodeTwin));
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
//        return isSolvable ? moves + 1 : -1;
        return isSolvable ? (solution_boards.size() - 1) : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return isSolvable ? solution_boards : null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

    }
}
