package puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * Created by oderor on 3/12/2017.
 */
public class Board {
    private final int n;
    private final int[] tiles;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        n = blocks.length;
        tiles = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[getLinearIndex(i, j, n)] = blocks[i][j];
            }
        }
    }

    private Board(int[] blocks, int n) {
        this.n = n;
        tiles = new int[n * n];
        System.arraycopy(blocks, 0, tiles, 0, n * n);
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    private static int getLinearIndex(int r, int c, int n) {
        return r * n + c;
    }

    private static int getRowFromLinearIndex(int index, int n) {
        return index / n;
    }

    private static int getColFromLinearIndex(int index, int n) {
        return index % n;
    }

    // get block's value at [r][c] in the current board
    private int blockAt(int r, int c) {
        return tiles[getLinearIndex(r, c, n)];
    }

    private int blockAt(int linearIndex) {
        return tiles[linearIndex];
    }

    // get block's value at [r][c] in the goal board
    private static int goalBlockAt(int r, int c, int n) {
        if ((r == n - 1) && (c == n - 1)) {
            return 0;
        }
        return 1 + r * n + c;
    }

    private static int goalRowOf(int v, int n) {
        if (v == 0) {
            return n - 1;
        }
        return (v - 1) / n;
    }

    private static int goalColOf(int v, int n) {
        if (v == 0) {
            return n - 1;
        }
        return (v - 1) % n;
    }

    // number of blocks out of place
    public int hamming() {
        int h = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // if goalBlockAt equals current block and is not an empty one at the end,
                // increment hamming function
                if ((blockAt(i, j) != goalBlockAt(i, j, n)) &&
                        (i != n - 1 || j != n - 1)) {
                    h++;
                }
            }
        }
        return h;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int m = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = blockAt(i, j);
                if (value != 0 && value != goalBlockAt(i, j, n)) {
                    m += Math.abs(i - goalRowOf(value, n)) +
                            Math.abs(j - goalColOf(value, n));
                }
            }
        }
        return m;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blockAt(i, j) != goalBlockAt(i, j, n)) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twinBoard = new Board(tiles, n);
        twinBoard.swapAnyPairOfBlocks();
        return twinBoard;
    }

    private void swapAnyPairOfBlocks() {
        boolean bothFound = false;
        int i1 = -1;
        int i2 = -1;
        // let's swap first found two non-null blocks
        for (int i = 0; i < n * n && !bothFound; i++) {
            if (blockAt(i) == 0) {
                continue;
            }
            if (i1 == -1) {
                i1 = i;
                continue;
            }
            if (i2 == -1) {
                i2 = i;
                bothFound = true;
            }
        }
        if (i1 != -1 && i2 != -1) {
            swapBlocks(i1, i2);
        } else {
            throw new RuntimeException("Not enough blocks to find two to swap!");
        }
    }

    private void swapBlocks(int i, int j) {
        int tmp = tiles[i];
        tiles[i] = tiles[j];
        tiles[j] = tmp;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return (this.n == that.n) && Arrays.equals(this.tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i0 = 0, j0 = 0;
        for (int i = 0; i < n * n; i++) {
            if (blockAt(i) == 0) {
                i0 = getRowFromLinearIndex(i, n);
                j0 = getColFromLinearIndex(i, n);
                break;
            }
        }
        Queue<Board> boards = new Queue<>();
        // if left neighbour exists
        if (0 <= i0 - 1) {
            Board board = new Board(tiles, n);
            board.swapBlocks(getLinearIndex(i0 - 1, j0, n), getLinearIndex(i0, j0, n));
            boards.enqueue(board);
        }
        // if right neighbour exists
        if (i0 + 1 < n) {
            Board board = new Board(tiles, n);
            board.swapBlocks(getLinearIndex(i0 + 1, j0, n), getLinearIndex(i0, j0, n));
            boards.enqueue(board);
        }
        // if top neighbour exists
        if (0 <= j0 - 1) {
            Board board = new Board(tiles, n);
            board.swapBlocks(getLinearIndex(i0, j0 - 1, n), getLinearIndex(i0, j0, n));
            boards.enqueue(board);
        }
        // if bottom neighbour exists
        if (j0 + 1 < n) {
            Board board = new Board(tiles, n);
            board.swapBlocks(getLinearIndex(i0, j0 + 1, n), getLinearIndex(i0, j0, n));
            boards.enqueue(board);
        }
        return boards;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blockAt(i, j)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }
}
