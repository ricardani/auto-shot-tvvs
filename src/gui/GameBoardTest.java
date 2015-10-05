package gui;

import org.junit.Test;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
@SuppressWarnings("UnusedDeclaration")
public class GameBoardTest {

    @Test
    public void testGetAvailableMoves() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (test.getAvailableMoves().size() != 2) {
            throw new Exception("Too many or too few moves!");
        }

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        if (test.getAvailableMoves().size() == 0) {
            throw new Exception("There should not be any moves left!");
        }

    }

    @Test
    public void testDoMove() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                if (x == 1 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else if (x == 4 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else {
                    if (test.getBoardPiece(x, y)) {
                        throw new Exception("Too many pieces on the game board!");
                    }
                }
            }
        }

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                if (x == 3 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("One piece is missing from the game board!");
                    }
                } else {
                    if (test.getBoardPiece(x, y)) {
                        throw new Exception("Too many pieces on the game board!");
                    }
                }
            }
        }
    }

    @Test
    public void testUndoMove() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        test.undoMove();

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                if (x == 1 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else if (x == 4 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else {
                    if (test.getBoardPiece(x, y)) {
                        throw new Exception("Too many pieces on the game board!");
                    }
                }
            }
        }
    }

    @Test
    public void testRedoMove() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        test.undoMove();
        test.redoMove();

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                if (x == 3 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("One piece is missing from the game board!");
                    }
                } else {
                    if (test.getBoardPiece(x, y)) {
                        throw new Exception("Too many pieces on the game board!");
                    }
                }
            }
        }
    }

    @Test
    public void testGetBoardPiece() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                if (x == 1 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else if (x == 4 && y == 3) {
                    if (!test.getBoardPiece(x, y)) {
                        throw new Exception("At least a piece is missing from the game board!");
                    }
                } else {
                    if (test.getBoardPiece(x, y)) {
                        throw new Exception("Too many pieces on the game board!");
                    }
                }
            }
        }
    }

    @Test
    public void testGetNumberOfPiecesLeft() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (test.getNumberOfPiecesLeft() != 2) {
            throw new Exception("Wrong number of pieces on board");
        }

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        if (test.getNumberOfPiecesLeft() != 1) {
            throw new Exception("One piece should have been removed from the board");
        }
    }

    @Test
    public void testBoardSolvedOnVictory() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (test.isBoardSolved()) {
            throw new Exception("The Game is not solved!");
        }

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        if (!test.isBoardSolved()) {
            throw new Exception("The Game has been won!");
        }
    }

    @Test
    public void testBoardSolvedOnLoss() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][2] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (test.isBoardSolved()) {
            throw new Exception("The Game has been lost!");
        }
    }

    @Test
    public void testBoardLostOnVictory() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][3] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (test.isBoardLost()) {
            throw new Exception("The Game is still playable!");
        }

        GameMove testMove = new GameMove(1, 3, 'r');

        test.doMove(testMove, false);

        if (test.isBoardLost()) {
            throw new Exception("The Game has been won!");
        }
    }

    @Test
    public void testBoardLostOnLoss() throws Exception {
        boolean testBoard[][] = new boolean[GameBoard.horizontalSize][GameBoard.verticalSize];
        testBoard[1][2] = true;
        testBoard[4][3] = true;

        GameBoard test = new GameBoard(testBoard);

        if (!test.isBoardLost()) {
            throw new Exception("The Game is NOT playable!");
        }
    }

    // @Test
    // public void testSolveOriginalLevels() throws Exception {
    //     long numberOfMovesTriedDFS=0, numberOfBacktracksDFS=0;
    //     long numberOfMovesTriedAS=0, numberOfBacktracksAS=0;
    //     long[][] levels = MapSelectorController.loadLevels();

    //     if (levels.length == 7 && levels[0].length == 500) {
    //         for (int i = 0; i < levels.length; i++) {
    //             for (int j = 0; j < levels[0].length; j++) {
    //                 GameBoard temp = new GameBoard(levels[i][j]);
    //                 SolverDFS tempSolver = new SolverDFS(temp);
    //                 tempSolver.searchSolution();
    //                 /* Compare */
    //                 numberOfBacktracksDFS += tempSolver.getNumberOfBackTracks();
    //                 numberOfMovesTriedDFS += tempSolver.getNumberOfMovesTried();

    //                 if (!tempSolver.getIsSolutionFound()) {
    //                     throw new Exception("Level " + Integer.toString(i) + " " + Integer.toString(j) + "could not be solved!");
    //                 }

    //                 temp = new GameBoard(levels[i][j]);
    //                 SolverHeuristicBFS tempSolver2 = new SolverHeuristicBFS(temp);
    //                 tempSolver2.searchSolution();
    //                 /* Compare */
    //                 numberOfBacktracksAS += tempSolver2.getNumberOfBackTracks();
    //                 numberOfMovesTriedAS += tempSolver2.getNumberOfMovesTried();

    //                 if (!tempSolver.getIsSolutionFound()) {
    //                     throw new Exception("Level " + Integer.toString(i) + " " + Integer.toString(j) + "could not be solved!");
    //                 }
    //             }
    //         }
    //     } else {
    //         throw new Exception("Levels are missing!");
    //     }

    //     System.out.println("DFS: Moves: " + Long.toString(numberOfMovesTriedDFS) + " Backtracks: " + Long.toString(numberOfBacktracksDFS));
    //     System.out.println("AS : Moves: " + Long.toString(numberOfMovesTriedAS) + " Backtracks: " + Long.toString(numberOfBacktracksAS));
    // }

}
