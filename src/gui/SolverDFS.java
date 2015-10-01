package gui;

/**
 * Depth First Search
 * Eduardo Fernandes
 * Filipe Eiras
 */
public class SolverDFS extends Solver {
    private int numberOfMovesTried;
    private int numberOfBackTracks;

    SolverDFS( GameBoard input) {
        super(input);
        numberOfMovesTried = 0;
        numberOfBackTracks = 0;
    }

    public void searchSolution() throws Exception {
        initializeSolver();
        if (!solutionFound) {
            searchSolutionAux();
        }
        hasRun = true;
    }

    private void searchSolutionAux() throws Exception {
        /* Try available moves */
        for (int i = 0; i < gameBoard.getNumberOfAvailableMoves(); i++) {
            attemptMove(gameBoard.getAvailableMoves().get(i));
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
        return "DFS Algorithm";
    }
}
