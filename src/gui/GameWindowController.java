package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
@SuppressWarnings("WeakerAccess")
public class GameWindowController extends GridPane implements Initializable {
    Main application1;
    @FXML
    Pane mainPane;
    @FXML
    GridPane gameGrid;
    @FXML
    Label lblNumberOfMovesDone;
    @FXML
    Label lblNumberOfAvailableMoves;
    @FXML
    Label lblIsSolved;
    @FXML
    Label lblIsLost;
    @FXML
    Button upButton;
    @FXML
    Button downButton;
    @FXML
    Button leftButton;
    @FXML
    Button rightButton;
    @FXML
    Button undoButton;
    @FXML
    Button redoButton;
    private GameBoard gameBoard;
    private ImageView[][] spheres;
    private int selectedX = -1, selectedY = -1;
    private EventHandler<MouseEvent> mouseEventHandler;

    public void setApp(Main application) {
        application1 = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            gameBoard = Main.selectedBoard;
            setupBoard();
            updateGUI();
            createEventHandlers();
            gameGrid.setOnMouseClicked(mouseEventHandler);
        } catch (Exception e) {
            Main.logSevereAndExit(e);
        }
    }

    private void createEventHandlers() {
        mouseEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (int x = 0; x < GameBoard.horizontalSize; x++) {
                    for (int y = 0; y < GameBoard.verticalSize; y++) {
                        if (event.getTarget() == spheres[x][y] && event.getButton() == MouseButton.PRIMARY) {
                            selectPiece(x, y);
                            return;
                        }
                    }
                }
                clearSelection();
            }
        };
    }

    private void setupBoard() {
        spheres = new ImageView[GameBoard.horizontalSize][GameBoard.verticalSize];

        for (int x = 0; x < GameBoard.horizontalSize; x++) {
            for (int y = 0; y < GameBoard.verticalSize; y++) {
                spheres[x][y] = new ImageView(Main.sphereImage);
                spheres[x][y].setVisible(false);
                gameGrid.add(spheres[x][y], x, y);
            }
        }
    }

    private void updateGUI() {
        updateBoard();
        updateStats();
        updateButtons();
    }

    private void updateBoard() {
        try {
            // Sphere draw
            for (int x = 0; x < GameBoard.horizontalSize; x++) {
                for (int y = 0; y < GameBoard.verticalSize; y++) {
                    if (gameBoard.getBoardPiece(x, y)) {
                        if (selectedX == x && selectedY == y) {
                            spheres[x][y].setImage(Main.selectedSphereImage);
                        } else {
                            spheres[x][y].setImage(Main.sphereImage);
                        }
                        spheres[x][y].setVisible(true);
                    } else {
                        spheres[x][y].setVisible(false);
                    }
                }
            }

            // Available moves draw
            if (isSelected()) {
                ArrayList<GameMove> moves = gameBoard.getAvailableMovesOnPosition(selectedX, selectedY);
                for (GameMove gameMove : moves) {
                    switch (gameMove.getDirection()) {
                        case GameMove.MoveUp:
                            spheres[selectedX][selectedY - 1].setImage(Main.upArrowImage);
                            spheres[selectedX][selectedY - 1].setVisible(true);
                            break;

                        case GameMove.MoveDown:
                            spheres[selectedX][selectedY + 1].setImage(Main.downArrowImage);
                            spheres[selectedX][selectedY + 1].setVisible(true);
                            break;

                        case GameMove.MoveLeft:
                            spheres[selectedX - 1][selectedY].setImage(Main.leftArrowImage);
                            spheres[selectedX - 1][selectedY].setVisible(true);
                            break;

                        case GameMove.MoveRight:
                            spheres[selectedX + 1][selectedY].setImage(Main.rightArrowImage);
                            spheres[selectedX + 1][selectedY].setVisible(true);
                            break;

                        default:
                            Main.logSevereAndExit("Invalid move on updateBoard");
                    }
                }
            }

        } catch (Exception e) {
            Main.logSevereAndExit(e);
        }
    }

    private void updateStats() {
        lblNumberOfMovesDone.setText("Number of moves: " + Integer.toString(gameBoard.getNumberOfMovesMade()));

        lblNumberOfAvailableMoves.setText("Number of available moves: " + Integer.toString(gameBoard.getNumberOfAvailableMoves()));

        if (gameBoard.isBoardSolved()) {
            lblIsSolved.setText("Is the board solved? Yes");
        } else {
            lblIsSolved.setText("Is the board solved? No");
        }

        if (gameBoard.isBoardLost()) {
            lblIsLost.setText("Is the game lost? Yes");
        } else {
            lblIsLost.setText("Is the game lost? No");
        }
    }

    private void clearSelection() {
        selectedX = -1;
        selectedY = -1;
        updateGUI();
    }

    private void updateButtons() {
        upButton.setDisable(true);
        downButton.setDisable(true);
        leftButton.setDisable(true);
        rightButton.setDisable(true);

        if (isSelected()) {
            try {
                ArrayList<GameMove> moves = gameBoard.getAvailableMovesOnPosition(selectedX, selectedY);
                for (GameMove gameMove : moves) {
                    switch (gameMove.getDirection()) {
                        case GameMove.MoveUp:
                            upButton.setDisable(false);
                            break;

                        case GameMove.MoveDown:
                            downButton.setDisable(false);
                            break;

                        case GameMove.MoveLeft:
                            leftButton.setDisable(false);
                            break;

                        case GameMove.MoveRight:
                            rightButton.setDisable(false);
                            break;

                        default:
                            Main.logSevereAndExit("Invalid move on updateButtons");
                    }
                }
            } catch (Exception e) {
                Main.logSevereAndExit(e);
            }

        }

        undoButton.setDisable(!gameBoard.isUndoAvailable());
        redoButton.setDisable(!gameBoard.isRedoAvailable());
    }

    private void selectPiece(int x, int y) {
        selectedX = x;
        selectedY = y;
        updateGUI();
    }

    private boolean isSelected() {
        return !(selectedX == -1 && selectedY == -1);
    }

    private void doMove(int x, int y, char direction) {
        if (!isSelected()) {
            System.out.println("No piece selected");
            return;
        }

        try {
            GameMove move = new GameMove(x, y, direction);
            gameBoard.doMove(move, false);
            clearSelection();
        } catch (Exception e) {
            System.out.println("Invalid Move");
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleUpButtonAction() {
        doMove(selectedX, selectedY, GameMove.MoveUp);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleDownButtonAction() {
        doMove(selectedX, selectedY, GameMove.MoveDown);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleLeftButtonAction() {
        doMove(selectedX, selectedY, GameMove.MoveLeft);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleRightButtonAction() {
        doMove(selectedX, selectedY, GameMove.MoveRight);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleUndoButtonAction() {
        gameBoard.undoMove();
        clearSelection();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleRedoButtonAction() {
        gameBoard.redoMove();
        clearSelection();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleExitButtonAction() {
        application1.gotoStartWindow();
    }

}
