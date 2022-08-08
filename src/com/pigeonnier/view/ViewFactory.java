package com.pigeonnier.view;

import com.pigeonnier.EmailManager;
import com.pigeonnier.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewFactory {

    private EmailManager emailManager;
    private ArrayList<Stage> activeStages;
    public ComposeMessageWindowController composeMessageWindowController;
    private boolean isMainWindowOpened;
    //Options Window Handling
    ColorTheme colorTheme = ColorTheme.Default;
    FontSize fontSize = FontSize.Default;

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<>();
        isMainWindowOpened = false;
    }

    public boolean isMainWindowOpened() {
        return isMainWindowOpened;
    }

    public void setMainWindowOpened(boolean mainWindowOpened) {
        isMainWindowOpened = mainWindowOpened;
    }
    /**
     * Initializes login window.
     */
    public void showLoginWindow() {
        BaseController controller = new LoginWindowController(emailManager, this, "LoginWindow.fxml");

        InitializeStage(controller);
    }

    /**
     * Initializes Main Window.
     */
    public void showMainWindow() {
        BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        isMainWindowOpened = true;
        InitializeStage(controller);
    }

    /**
     * Initializes Options Window.
     */
    public void showOptionsWindow() {
        BaseController controller = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");

        InitializeStage(controller);
    }

    /**
     * Initializes Compose Message Window
     */
    public void showComposeMessageWindow() {
        BaseController controller = new ComposeMessageWindowController(emailManager, this, "ComposeMessageWindow.fxml");
        composeMessageWindowController = (ComposeMessageWindowController) controller;
        InitializeStage(controller);
    }

    /**
     * Initializes MessageDetailsWindow.
     */
    public void showMessageDetailsWindow() {
        BaseController controller = new MessageDetailsWindowController(emailManager, this, "MessageDetailsWindow.fxml");
        InitializeStage(controller);
    }

    /**
     * Initializes a Stage and add it to activeStages list.
     * @param controller
     */
    private void InitializeStage(BaseController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);

        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Problem in InitializeStage method.");
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        stage.setResizable(false);
        activeStages.add(stage);
        updateStyles();
    }

    /**
     * Close a stage.
     * @param stage
     */
    public void closeStage(Stage stage) {
        stage.close();
        activeStages.remove(stage);
    }

    /**
     * Update the style of the window according to current choice of the user.
     */
    public void updateStyles() {
        for(Stage stage: activeStages) {
            Scene scene = stage.getScene();
            // handle css
            scene.getStylesheets().clear();

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(ColorTheme.getPath(colorTheme))).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(FontSize.getPath(fontSize))).toExternalForm());
        }
    }


}
