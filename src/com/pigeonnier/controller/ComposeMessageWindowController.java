package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.services.EmailComposerService;
import com.pigeonnier.view.EmailSendingResult;
import com.pigeonnier.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ComposeMessageWindowController extends BaseController implements Initializable {

    public ComposeMessageWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField recipientTextFIeld;

    @FXML
    private TextField subjectTextView;

    @FXML
    private ChoiceBox<EmailAccount> accountChoiceBox;

    @FXML
    void sendButtonAction(ActionEvent event) {
//        System.out.println(htmlEditor.getHtmlText());
        System.out.println("Send Button Clicked!");

        EmailComposerService emailComposerService = new EmailComposerService(
                accountChoiceBox.getValue(),
                htmlEditor.getHtmlText(),
                subjectTextView.getText(),
                recipientTextFIeld.getText()
        );

        emailComposerService.start();
        emailComposerService.setOnSucceeded(event1 -> {
            EmailSendingResult emailSendingResult = emailComposerService.getValue();

            switch (emailSendingResult) {
                case SUCCESS -> {
                    Stage stage = (Stage) recipientTextFIeld.getScene().getWindow();
                    viewFactory.closeStage(stage);
                }
                case FAILED_BY_PROVIDER -> errorLabel.setText("Provider error!");
                case FAILED_BY_UNEXPECTED_ERROR -> errorLabel.setText("Unexpected error!");
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpChoiceBox();
    }

    private void setUpChoiceBox() {
        accountChoiceBox.setItems(emailManager.getEmailAccountsList());
        accountChoiceBox.setValue(emailManager.getEmailAccountsList().get(0));
    }

}
