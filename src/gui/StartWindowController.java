package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Eduardo Fernandes
 * <p/>
 * Main Window controller
 * Filipe Eiras
 */
@SuppressWarnings("WeakerAccess")
public class StartWindowController extends GridPane implements Initializable {
    @SuppressWarnings("UnusedDeclaration")
    @FXML
    Button buttonPlay;
    @SuppressWarnings("UnusedDeclaration")
    @FXML
    MenuButton algorithmCombo;
    @SuppressWarnings("UnusedDeclaration")
    @FXML
    Button buttonAutoPlay;
    @SuppressWarnings("UnusedDeclaration")
    @FXML
    Button buttonCancel;
    private Main application;

    public void setApp(Main application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @SuppressWarnings("UnusedDeclaration")
    public void handlePlayButtonAction() {
        if (application != null) {
            application.startGame();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleAutoPlayButtonAction() {
        if (application != null) {
            application.startGameAuto();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleLevelSelectButtonAction() {
        if (application != null) {
            application.startLevelSelector();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void handleExitButtonAction() {
        if (application != null) {
            Main.exit();
        }
    }
}
