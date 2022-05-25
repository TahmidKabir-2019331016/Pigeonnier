package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.view.ColorTheme;
import com.pigeonnier.view.FontSize;
import com.pigeonnier.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class OptionsWindowController extends BaseController implements Initializable {

    public OptionsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Slider FontPicker;

    @FXML
    private ChoiceBox<ColorTheme> ThemePicker;

    @FXML
    void ApplyAction(ActionEvent event) {
        viewFactory.setColorTheme(ThemePicker.getValue());
        viewFactory.setFontSize(FontSize.values()[(int) FontPicker.getValue()]);
        System.out.println("Theme: " + viewFactory.getColorTheme());
        System.out.println("Font: " + viewFactory.getFontSize());
        viewFactory.updateStyles();
    }

    @FXML
    void CancelAction(ActionEvent event) {
        Stage stage = (Stage) FontPicker.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColorTheme();
        setFontSize();
    }

    // Has to learn
    private void setFontSize() {
        FontPicker.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                int i = object.intValue();
                return FontSize.values()[i].toString();
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });
        FontPicker.valueProperty().addListener(((observable, oldValue, newValue) -> {
            FontPicker.setValue(newValue.intValue());
        }));
    }

    private void setColorTheme() {
        ThemePicker.setItems(FXCollections.observableArrayList(ColorTheme.values()));
        ThemePicker.setValue(viewFactory.getColorTheme());
    }

}
