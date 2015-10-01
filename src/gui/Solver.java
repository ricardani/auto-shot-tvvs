package gui;

import java.util.Stack;

/**
 * Solver class, to be extended by a class that implements the algorithms
 * Eduardo Fernandes
 * Filipe Eiras
 */
abstract class Solver {
    final GameBoard gameBoard;
    Stack<GameMove> moveStack;
    boolean hasRun;
    boolean solutionFound;

    Solver(GameBoard input) {
        gameBoard = input;
        hasRun = false;
        solutionFound = false;
    }

    abstract public void searchSolution() throws Exception;

    void initializeSolver() {
        moveStack = new Stack<>();
        hasRun = false;
        solutionFound = gameBoard.isBoardSolved();
    }

    public Stack<GameMove> getSolution() {
        if (hasRun && solutionFound) {
            return moveStack;
        } else {
            return null;
        }
    }

    public boolean getIsSolutionFound() {
        return solutionFound;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void printSolutionStack() {
        if (hasRun && solutionFound) {
            for (GameMove aMoveStack : moveStack) {
                System.out.println(aMoveStack);
            }
        }

        if (hasRun && !solutionFound) {
            System.out.println("No solution has found");
        }

        if (!hasRun && !solutionFound) {
            System.out.println("You must run the algorithm first");
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    abstract public void printStatistics();
}
