package gui;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
public class GameBoard {
    /* Constants */
    public static final int horizontalSize = 7;
    public static final int verticalSize = 7;
    private static final int arrayOffset = 1;
    private static final int minimumDistanceFromEdge = 2;

    private final boolean[][] board;

    /* Save GameBoards + Moves done (GameBoards +1 offset) */
    private int currentMoveIndex;
    private ArrayList<boolean[][]> gameBoards;
    private ArrayList<GameMove> gameMoves;

    /* Empty board constructor */
    @SuppressWarnings("WeakerAccess")
    GameBoard() {
        board = new boolean[horizontalSize][verticalSize];
        clearBoard();
        initializeUndoRedo();
    }

    GameBoard( boolean[][] in) {
        board = in;
        initializeUndoRedo();
    }

    /*
     * Internal map loader
     */
    @SuppressWarnings("SameParameterValue")
    GameBoard( String levelFile) throws Exception {
        board = new boolean[horizontalSize][verticalSize];

        InputStream file = GameBoard.class.getResourceAsStream("levels/" + levelFile);

        if (file == null) {
            throw new Exception("Problem while loading level");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        String line;
        int readLines = 0;

        while ((line = reader.readLine()) != null) {
            if (readLines >= verticalSize) {
                throw new Exception("Invalid map (too many lines)");
            }

            String[] parts = line.split(",");

            /* Validate line size */
            if (parts.length != horizontalSize) {
                throw new Exception("Invalid map");
            }

            for (int i = 0; i < parts.length; i++) {
                switch (parts[i].charAt(0)) {
                    case 'O':
                        board[i][readLines] = true;
                        break;

                    case ' ':
                        board[i][readLines] = false;
                        break;

                    default:
                        throw new Exception("Invalid char in map");
                }
            }
            readLines++;
        }

        file.close();
        initializeUndoRedo();
    }

    /*
     * Original game level decoder
     */
    GameBoard( long map) {
        board = new boolean[horizontalSize][verticalSize];
        String binaryString = toBinaryStringCustom(map);

        int x = 6;
        int y = 6;

        for (int i = 0; i < binaryString.length(); i++) {
            char chPos = binaryString.charAt(i);

            switch (chPos) {
                case '0':
                    board[x][y] = false;
                    break;

                case '1':
                    board[x][y] = true;
                    break;
            }

            if (x == 0) {
                x = 6;
                y--;
            } else {
                x--;
            }
        }

        initializeUndoRedo();
    }

    /* Custom toBinaryString (custom padding) */
    private static String toBinaryStringCustom( long x) {

        byte[] bytes = new byte[64]; // 64 bits per long
        int pos = 0;

        do {
            bytes[63 - pos++] = (byte) (x % 2);
            x = x >> 1; // /2
        } while (x > 0);

        String output = "";
        for (int i = 15; i < 64; i++) {
            switch (bytes[i]) {
                case 1:
                    output += "1";
                    break;

                case 0:
                    output += "0";
                    break;
            }
        }

        return output;
    }

    void initializeUndoRedo() {
        currentMoveIndex = 0;
        gameBoards = new ArrayList<>();
        gameMoves = new ArrayList<>();

        /* Add original board to vector */
        gameBoards.add(getBoardCopy());
    }

    private void clearBoard() {
        for (int x = 0; x < horizontalSize; x++) {
            for (int y = 0; y < verticalSize; y++) {
                board[x][y] = false;
            }
        }
    }

    boolean[][] getBoardCopy() {
        boolean[][] boardCopy = new boolean[horizontalSize][verticalSize];
        for (int x = 0; x < horizontalSize; x++) {
            System.arraycopy(board[x], 0, boardCopy[x], 0, verticalSize);
        }
        return boardCopy;
    }

    public ArrayList<GameMove> getAvailableMovesOnPosition(int x, int y) throws Exception {
        ArrayList<GameMove> moves = new ArrayList<>(0);

        /* Check if there is a sphere on the position */
        if (!board[x][y]) {
            return moves;
        }

        /* Verify left */
        if (x >= minimumDistanceFromEdge) {
            /* Check if there is a piece blocking our move */
            if (!board[x - 1][y]) {
                for (int i = x - 2; i >= 0; i--) {
                    if (board[i][y]) {
                        GameMove tempMove = new GameMove(x, y, GameMove.MoveLeft);
                        moves.add(tempMove);
                        break;
                    }
                }
            }
        }

        /* Verify right */
        if (x <= (horizontalSize - arrayOffset - minimumDistanceFromEdge)) {
            /* Check if there is a piece blocking our move */
            if (!board[x + 1][y]) {
                for (int i = x + 2; i < horizontalSize; i++) {
                    if (board[i][y]) {
                        GameMove tempMove = new GameMove(x, y, GameMove.MoveRight);
                        moves.add(tempMove);
                        break;
                    }
                }
            }
        }

        /* Verify up */
        if (y >= minimumDistanceFromEdge) {
            /* Check if there is a piece blocking our move */
            if (!board[x][y - 1]) {
                for (int i = y - 2; i >= 0; i--) {
                    if (board[x][i]) {
                        GameMove tempMove = new GameMove(x, y, GameMove.MoveUp);
                        moves.add(tempMove);
                        break;
                    }
                }
            }
        }

        /* Verify down */
        if (y <= (verticalSize - arrayOffset - minimumDistanceFromEdge)) {
            /* Check if there is a piece blocking our move */
            if (!board[x][y + 1]) {
                for (int i = y + 2; i < verticalSize; i++) {
                    if (board[x][i]) {
                        GameMove tempMove = new GameMove(x, y, GameMove.MoveDown);
                        moves.add(tempMove);
                        break;
                    }
                }
            }
        }

        return moves;
    }

    public ArrayList<GameMove> getAvailableMoves() {
        ArrayList<GameMove> moves = new ArrayList<>(0);

        /* Get all possible gameMoves from the board */
        for (int y = 0; y < verticalSize; y++) {
            for (int x = 0; x < horizontalSize; x++) {
                try {
                    moves.addAll(getAvailableMovesOnPosition(x, y));
                } catch (Exception e) {
                    Main.logSevereAndExit(e);
                }
            }
        }

        return moves;
    }

    public void doMove( GameMove move,  boolean redo) throws Exception {
        int x = move.getPieceX();
        int y = move.getPieceY();

        boolean canContinue = false;

        ArrayList<GameMove> movesAvailable = getAvailableMovesOnPosition(x, y);

        for (GameMove aMovesAvailable : movesAvailable) {
            if (move.equals(aMovesAvailable)) {
                /* Move is valid */
                canContinue = true;
                break;
            }
        }

        if (!canContinue) {
            throw new Exception("Invalid Move, doing nothing.");
        }

        if (!redo) {
            /* Clear 'future' gameMoves if there are any */
            if (currentMoveIndex < gameMoves.size()) {
                for (int i = gameMoves.size(); i > currentMoveIndex; i--) {
                    gameMoves.remove(i - 1);
                }
            }
        }

        /* Clear 'future' gameBoards if there are any */
        if ((currentMoveIndex + 1) < gameBoards.size()) {
            for (int i = gameBoards.size(); i > currentMoveIndex + 1; i--) {
                gameBoards.remove(i - 1);
            }
        }

        currentMoveIndex++;
        if (!redo) {
            /* Save the move */
            gameMoves.add(move);
        }

        switch (move.getDirection()) {
            case GameMove.MoveUp:
                doMoveAuxUp(x, y);
                break;

            case GameMove.MoveDown:
                doMoveAuxDown(x, y);
                break;

            case GameMove.MoveLeft:
                doMoveAuxLeft(x, y);
                break;

            case GameMove.MoveRight:
                doMoveAuxRight(x, y);
                break;

            default:
                Main.logSevereAndExit("Invalid move on doMove()");
        }

        /* Add new board to vector */
        gameBoards.add(getBoardCopy());
    }

    private void doMoveAuxUp(int x, int y) {
        /* Check if we are propagating a collision */
        if (y > 0 && board[x][y - 1]) {
            doMoveAuxUp(x, y - 1);
            return;
        }

        /* Move piece */
        for (int i = y; i >= 0; i--) {
            /* Piece has left the board */
            if (i == 0) {
                board[x][y] = false;
                return;
            }

            if (board[x][i - 1]) {
                board[x][i] = true;
                board[x][y] = false;
                doMoveAuxUp(x, i - 1);
                return;
            }
        }
    }

    private void doMoveAuxDown(int x, int y) {
        /* Check if we are propagating a collision */
        if (y < (verticalSize - arrayOffset) && board[x][y + 1]) {
            doMoveAuxDown(x, y + 1);
            return;
        }

        /* Move piece */
        for (int i = y; i < verticalSize; i++) {
            /* Piece has left the board */
            if (i == verticalSize - arrayOffset) {
                board[x][y] = false;
                return;
            }

            if (board[x][i + 1]) {
                board[x][i] = true;
                board[x][y] = false;
                doMoveAuxDown(x, i + 1);
                return;
            }
        }
    }

    private void doMoveAuxLeft(int x, int y) {
        /* Check if we are propagating a collision */
        if (x > 0 && board[x - 1][y]) {
            doMoveAuxLeft(x - 1, y);
            return;
        }

        /* Move piece */
        for (int i = x; i >= 0; i--) {
            /* Piece has left the board */
            if (i == 0) {
                board[x][y] = false;
                return;
            }

            if (board[i - 1][y]) {
                board[i][y] = true;
                board[x][y] = false;
                doMoveAuxLeft(i - 1, y);
                return;
            }
        }
    }

    private void doMoveAuxRight(int x, int y) {
        /* Check if we are propagating a collision */
        if (x < (horizontalSize - arrayOffset) && board[x + 1][y]) {
            doMoveAuxRight(x + 1, y);
            return;
        }

        /* Move piece */
        for (int i = x; i < horizontalSize; i++) {
            /* Piece has left the board */
            if (i == horizontalSize - arrayOffset) {
                board[x][y] = false;
                return;
            }

            /* We have a collision */
            if (board[i + 1][y]) {
                board[i][y] = true;
                board[x][y] = false;
                doMoveAuxRight(i + 1, y);
                return;
            }
        }
    }

    public boolean isUndoAvailable(){
        return currentMoveIndex > 0 && gameMoves.size() > 0;
    }

    public void undoMove() {
        if (isUndoAvailable()) {
            currentMoveIndex--;
            restoreBoard(gameBoards.get(currentMoveIndex));
        }
    }

    public boolean isRedoAvailable(){
        /* Save GameBoards + Moves done (GameBoards +1 offset) */
        return currentMoveIndex < gameMoves.size();
    }

    public void redoMove() {
        if (isRedoAvailable()) {
            try {
                doMove(gameMoves.get(currentMoveIndex), true);
            } catch (Exception e) {
                Main.logSevereAndExit(e);
            }
        }
    }

    private void restoreBoard( boolean[][] restore) {
        for (int x = 0; x < horizontalSize; x++) {
            System.arraycopy(restore[x], 0, board[x], 0, verticalSize);
        }
    }

    public boolean getBoardPiece(int x, int y) throws Exception {
        if ((x >= 0 && x < horizontalSize) && (y >= 0 && y < verticalSize)) {
            return board[x][y];
        }

        throw new Exception("Invalid location!");
    }

    public int getNumberOfPiecesLeft() {
        int numberOfPieces = 0;

        for (int y = 0; y < verticalSize; y++) {
            for (int x = 0; x < horizontalSize; x++) {
                if (board[x][y]) {
                    numberOfPieces++;
                }
            }
        }

        return numberOfPieces;
    }

    public boolean isBoardSolved() {
        return getNumberOfPiecesLeft() == 1;
    }

    public boolean isBoardLost() {
        try {
            ArrayList<GameMove> availableMoves = getAvailableMoves();
            return availableMoves.size() <= 0 && !isBoardSolved();

        } catch (Exception e) {
            Main.logSevereAndExit(e);
        }

        return true;
    }

    @Override
    public String toString() {
        String output = "";
        for (int y = 0; y < verticalSize; y++) {
            for (int x = 0; x < horizontalSize; x++) {
                if (board[x][y]) {
                    output += "O";
                } else {
                    output += " ";
                }
            }
            if (y < verticalSize - 1) {
                output += "\n";
            }
        }
        return output;
    }

    public int getNumberOfMovesMade() {
        return currentMoveIndex;
    }

    public int getNumberOfAvailableMoves() {
        return getAvailableMoves().size();
    }

    public Pair<Integer, Integer> getSize() {
        int horizontal = board.length;
        int vertical = board[0].length;
        return new Pair<>(horizontal, vertical);
    }

    public void removeExtra() {
        for (int i = currentMoveIndex + 1; i < gameBoards.size(); i++) {
            gameBoards.remove(i);
        }
        for (int i = currentMoveIndex; i < gameMoves.size(); i++) {
            gameMoves.remove(i);
        }
    }
}
