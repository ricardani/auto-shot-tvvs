package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
@SuppressWarnings("WeakerAccess")
public class Main extends Application {
    /* Constants */
    private static final String mainWindowTitle = "Auto-Shot";

    private static final String mainWindowXml = "StartWindow.fxml";
    private static final String gameWindowXml = "GameWindow.fxml";
    private static final String gameWindowAutoXml = "GameWindowAuto.fxml";
    private static final String levelSelectorXml = "MapSelector.fxml";

    public static Image sphereImage;
    public static Image selectedSphereImage;

    public static Image leftArrowImage;
    public static Image rightArrowImage;
    public static Image upArrowImage;
    public static Image downArrowImage;
    public static GameBoard selectedBoard = null;

    /* JavaFX */
    private Stage stage;

    /**
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        logInfo("Application has started");
        Application.launch(Main.class, args);
    }

    public static void exit() {
        logInfo("Application is exiting");
        System.exit(0);
    }

    public static void logInfo(String in) {
        Logger.getLogger(Main.class.getName()).log(Level.INFO, in, (Object) null);
    }

    public static void logSevereAndExit(String in) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, in, (Object) null);
        System.exit(-1);
    }

    public static void logSevereAndExit(Exception ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        System.exit(-1);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadResources();
        selectedBoard = new GameBoard("level7500.map");

        try {
            stage = primaryStage;
            stage.setTitle(mainWindowTitle);
            stage.setResizable(false);
            gotoStartWindow();
            primaryStage.show();
        } catch (Exception ex) {
            logSevereAndExit(ex);
        }
    }

    private void loadResources() {
        logInfo("Loading resources...");
        try {
            sphereImage = new Image(Main.class.getResourceAsStream("resources/sphere_white.png"));
            selectedSphereImage = new Image(Main.class.getResourceAsStream("resources/sphere_blue.png"));
            leftArrowImage = new Image(Main.class.getResourceAsStream("resources/arrow_left.png"));
            rightArrowImage = new Image(Main.class.getResourceAsStream("resources/arrow_right.png"));
            upArrowImage = new Image(Main.class.getResourceAsStream("resources/arrow_up.png"));
            downArrowImage = new Image(Main.class.getResourceAsStream("resources/arrow_down.png"));
        } catch (IllegalArgumentException e) {
            logSevereAndExit("Some resources could not be loaded, exiting.");
        }
        logInfo("Resources loaded");
    }

    public void gotoStartWindow() {
        try {
            StartWindowController startWindowController = (StartWindowController) replaceSceneContent(mainWindowXml);
            startWindowController.setApp(this);
        } catch (Exception ex) {
            logSevereAndExit(ex);
        }
    }

    public void startGame() {
        logInfo("Loading game scene");
        gotoGameWindow();
    }

    private void gotoGameWindow() {
        try {
            GameWindowController gameWindowController = (GameWindowController) replaceSceneContent(gameWindowXml);
            gameWindowController.setApp(this);
        } catch (Exception ex) {
            logSevereAndExit(ex);
        }
    }

    public void startLevelSelector() {
        logInfo("Loading level selector scene");
        gotoLevelSelectWindow();
    }

    private void gotoLevelSelectWindow() {
        try {
            MapSelectorController mapSelectorController = (MapSelectorController) replaceSceneContent(levelSelectorXml);
            mapSelectorController.setApp(this);
        } catch (Exception ex) {
            logSevereAndExit(ex);
        }
    }

    public void startGameAuto() {
        logInfo("Loading automatic game solver scene");
        gotoGameWindowAuto();
    }

    private void gotoGameWindowAuto() {
        try {
            GameWindowAutoController gameWindowAutoController = (GameWindowAutoController) replaceSceneContent(gameWindowAutoXml);
            gameWindowAutoController.setApp(this);
        } catch (Exception ex) {
            logSevereAndExit(ex);
        }
    }

    /**
     * @param fxml XML scene file to load
     */
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));
        GridPane page;
        try (InputStream in = Main.class.getResourceAsStream(fxml)) {
            page = loader.load(in);
        }
        Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

}
