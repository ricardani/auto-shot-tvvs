package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * BFS + Heuristic solver
 * Eduardo Fernandes
 * Filipe Eiras
 */
public class SolverHeuristicBFS extends Solver {
    private int numberOfMovesTried;
    private int numberOfBackTracks;

    SolverHeuristicBFS( GameBoard input) {
        super(input);
        numberOfMovesTried = 0;
        numberOfBackTracks = 0;
    }

    void initializeSolver() {
        moveStack = new Stack<>();
        hasRun = false;
        solutionFound = gameBoard.isBoardSolved();
    }

    int linesChoice(int i, ArrayList<GameMove> availableMoves) throws Exception {
        gameBoard.doMove(availableMoves.get(i), false);
        int lines = getVerticalLines();
        lines += getHorizontalLines();
        gameBoard.undoMove();
        gameBoard.removeExtra();
        return lines;
    }

    void orderChoices(ArrayList<GameMove> availableMoves, List<MoveValues> moves) throws Exception {
        for (int i = 0; i < availableMoves.size(); i++) {
            moves.add(new MoveValues(i, linesChoice(i, availableMoves)));
        }
        Collections.sort(moves);
    }

    public void searchSolution() throws Exception {
        initializeSolver();
        if (!solutionFound) {
            searchSolutionAux();
        }
        hasRun = true;
    }

    int getVerticalLines() {
        int lines = 0;
        for (int i = 0; i < gameBoard.getSize().getKey(); i++) {
            for (int j = 0; j < gameBoard.getSize().getValue(); j++) {
                try {
                    if (gameBoard.getBoardPiece(i, j)) {
                        lines++;
                        break;
                    }
                } catch (Exception e) {
                    Main.logSevereAndExit(e);
                }
            }
        }
        return lines;
    }

    int getHorizontalLines() {
        int lines = 0;
        for (int j = 0; j < gameBoard.getSize().getValue(); j++) {
            for (int i = 0; i < gameBoard.getSize().getKey(); i++) {
                try {
                    if (gameBoard.getBoardPiece(i, j)) {
                        lines++;
                        break;
                    }
                } catch (Exception e) {
                    Main.logSevereAndExit(e);
                }
            }
        }
        return lines;
    }

    private void searchSolutionAux() throws Exception {
        //ArrayList<GameMove> availableMoves = gameBoard.getAvailableMoves();
        List<MoveValues> moves = new ArrayList<>();
        orderChoices(gameBoard.getAvailableMoves(), moves);

        for (MoveValues move : moves) {
            if (solutionFound) {
                return;
            }
            attemptMove(gameBoard.getAvailableMoves().get(move.getNumber()));
            solutionFound = gameBoard.isBoardSolved();

            /* Stop recursion if the solution has been found */
            if (solutionFound) {
                return;
            }

            searchSolutionAux();
        }

        /* No more moves and no solution? backtrack */
        if (!solutionFound) {
            backtrack();
        }
    }

    private void backtrack() {
        gameBoard.undoMove();
        moveStack.pop();
        numberOfBackTracks++;
    }

    private void attemptMove(GameMove in) throws Exception {
        moveStack.push(in);
        gameBoard.doMove(in, false);
        numberOfMovesTried++;
    }

    @Override
    public void printStatistics() {
        System.out.println("Number of backtracks: " + Integer.toString(numberOfBackTracks));
        System.out.println("Number of moves tried: " + Integer.toString(numberOfMovesTried));
    }

    public int getNumberOfBackTracks(){
        return numberOfBackTracks;
    }

    public int getNumberOfMovesTried(){
        return numberOfMovesTried;
    }

    @Override
    public String toString() {
        return "BFS + Heuristic Algorithm";
    }

}
