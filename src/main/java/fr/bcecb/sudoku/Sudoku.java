package fr.bcecb.sudoku;

import fr.bcecb.util.Constants;
import fr.bcecb.util.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Sudoku {
    public static final int SIZE = 9;
    private static final int SIZE_BOX = (int) Math.floor(Math.sqrt(SIZE));
    private static final Random RANDOM = new Random();

    /**
     * The sudoku grid
     */
    private final int[][] grid = new int[SIZE][SIZE];
    private final int[][] generatedGrid = new int[SIZE][SIZE];

    public Sudoku(int difficulty) {
        difficulty = MathHelper.clamp(difficulty, 20, 60);

        for (int i = 0; i < Sudoku.SIZE; i += Sudoku.SIZE_BOX) {
            this.fillBox(i, i);
        }

        this.fillRemaining(0, Sudoku.SIZE_BOX);

        List<Integer> toRemove = IntStream.range(0, Sudoku.SIZE * Sudoku.SIZE).boxed().collect(Constants.toShuffledList()).subList(0, difficulty);

        int x, y;
        for (int i : toRemove) {
            x = i / Sudoku.SIZE;
            y = i % Sudoku.SIZE;

            this.grid[x][y] = 0;
        }

        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(this.grid[i], 0, this.generatedGrid[i], 0, SIZE);
        }
    }

    /**
     * @return a random {@code int} between 1 and 9 (inclusive)
     */
    static int randomInt() {
        return RANDOM.nextInt(9) + 1;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getGeneratedGrid() {
        return generatedGrid;
    }

    /**
     * Computes valid candidates for slot of given row and column
     *
     * @param x the row
     * @param y the column
     * @return an array of valid values for the specified slot
     */
    public int[] computeCandidates(int x, int y) {
        return IntStream.rangeClosed(1, 9).filter(n -> checkGrid(n, x, y)).toArray();
    }

    public boolean[][] computeConflicts(int n, int x, int y) {
        boolean[][] conflicts = new boolean[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            if (grid[x][i] == n) conflicts[x][i] = true;
            if (grid[i][y] == n) conflicts[i][y] = true;
        }

        x -= (x % SIZE_BOX);
        y -= (y % SIZE_BOX);
        for (int i = x; i < x + SIZE_BOX; i++) {
            for (int j = y; j < y + SIZE_BOX; j++) {
                if (grid[i][j] == n) conflicts[i][j] = true;
            }
        }

        return conflicts;

    }

    /**
     * Checks if the row, column and box contain valid values (i.e no duplicates)
     *
     * @param x the row
     * @param y the column
     * @return {@code true} if the given row, column and box are valid
     */
    boolean checkGrid(int x, int y) {
        return checkRow(x) && checkColumn(y) && checkBox(x - (x % SIZE_BOX), y - (y % SIZE_BOX));
    }

    /**
     * Checks if the value {@code n} is already present in given row, column and box
     *
     * @param n the value to check against the grid
     * @param x the row
     * @param y the column
     * @return {@code false} if the given row, column and box already contain the value
     */
    boolean checkGrid(int n, int x, int y) {
        return !checkRow(n, x) && !checkColumn(n, y) && !checkBox(n, x - (x % SIZE_BOX), y - (y % SIZE_BOX));
    }

    /**
     * Checks if the given box is valid
     *
     * @param x the row
     * @param y the column
     * @return {@code true} if the box doesn't contain any duplicate
     */
    boolean checkBox(int x, int y) {
        boolean found = false;
        for (int n = 1; n <= 9; ++n, found = false) {
            for (int i = x; i < x + SIZE_BOX; ++i) {
                for (int j = y; j < y + SIZE_BOX; ++j) {
                    if (this.grid[i][j] == n) {
                        if (!found) {
                            found = true;
                        } else return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Checks if {@code n} is already in the box
     *
     * @param n the number to check in the box
     * @param x the row
     * @param y the column
     * @return {@code true} if the number is already present in the specified box, {@code false} otherwise
     */
    boolean checkBox(int n, int x, int y) {
        for (int i = x; i < x + SIZE_BOX; i++) {
            for (int j = y; j < y + SIZE_BOX; j++) {
                if (this.grid[i][j] == n) return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given row contains valid values (i.e no duplicates)
     *
     * @param x the row
     * @return {@code true} if the row doesn't contain any duplicate
     */
    boolean checkRow(int x) {
        boolean found = false;
        for (int n = 1; n <= 9; ++n, found = false) {
            for (int y = 0; y < SIZE; y++) {
                if (this.grid[x][y] == n) {
                    if (!found) {
                        found = true;
                    } else return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the given value is already present in the given row
     *
     * @param n the value
     * @param x the row
     * @return {@code true} if the row already does contain the value
     */
    boolean checkRow(int n, int x) {
        for (int i = 0; i < SIZE; i++) {
            if (this.grid[x][i] == n) return true;
        }

        return false;
    }

    /**
     * Checks if the given column contains valid values (i.e no duplicates)
     *
     * @param y the column
     * @return {@code true} if the column doesn't contain any duplicate
     */
    boolean checkColumn(int y) {
        boolean found = false;
        for (int n = 1; n <= 9; ++n, found = false) {
            for (int x = 0; x < SIZE; x++) {
                if (this.grid[x][y] == n) {
                    if (!found) {
                        found = true;
                    } else return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the given value is already present in the given column
     *
     * @param n the value
     * @param y the column
     * @return {@code true} if the column already does contain the value
     */
    boolean checkColumn(int n, int y) {
        for (int i = 0; i < SIZE; i++) {
            if (this.grid[i][y] == n) return true;
        }

        return false;
    }

    /**
     * Fills a given diagonal box with random values
     *
     * @param x the row
     * @param y the column
     */
    void fillBox(int x, int y) {
        int n;
        for (int i = 0; i < SIZE_BOX; i++) {
            for (int j = 0; j < SIZE_BOX; j++) {
                do {
                    n = randomInt();
                }
                while (checkBox(n, x, y));

                this.grid[x + i][y + j] = n;
            }
        }
    }

    /**
     * Fills remaining box (i.e other than diagonals)
     *
     * @param x the row
     * @param y the column
     * @return {@code true} if the box could be filled completely
     */
    boolean fillRemaining(int x, int y) {
        if (y >= SIZE && x < SIZE - 1) {
            x = x + 1;
            y = 0;
        }

        if (x >= SIZE && y >= SIZE) {
            return true;
        }

        if (x < SIZE_BOX) {
            if (y < SIZE_BOX) {
                y = SIZE_BOX;
            }
        } else if (x < SIZE - SIZE_BOX) {
            if (y == (x / SIZE_BOX) * SIZE_BOX)
                y = y + SIZE_BOX;
        } else {
            if (y == SIZE - SIZE_BOX) {
                x = x + 1;
                y = 0;
                if (x >= SIZE)
                    return true;
            }
        }

        for (int n = 1; n <= SIZE; n++) {
            if (checkGrid(n, x, y)) {
                this.grid[x][y] = n;
                if (fillRemaining(x, y + 1))
                    return true;

                this.grid[x][y] = 0;
            }
        }
        return false;
    }

    /**
     * Tries to solve the current grid.
     *
     * @return {@code true} if the grid could be solved, {@code false} otherwise
     */
    public boolean solve() {
        int[][] copy = Arrays.stream(this.grid).map(int[]::clone).toArray(int[][]::new);

        return solve(copy);
    }

    /**
     * Tries to solve the grid.
     *
     * @param grid the grid to be solved
     * @return {@code true} if the grid could be solved, {@code false} otherwise
     */
    public boolean solve(int[][] grid) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (grid[x][y] == 0) {
                    for (int n = 1; n <= 9; ++n) {
                        grid[x][y] = n;
                        if (checkGrid(x, y) && solve(grid)) {
                            return true;
                        }
                        grid[x][y] = 0;
                    }
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isCandidateValue(int[] candidatesValues, int value) {
        for (int candidateValue : candidatesValues) {
            if (candidateValue == value) return true;
        }
        return false;

    }


    public boolean winCondition() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) return false;

            }
        }
        return true;
    }
}