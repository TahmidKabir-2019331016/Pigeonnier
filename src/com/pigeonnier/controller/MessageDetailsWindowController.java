package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.model.EmailMessages;
import com.pigeonnier.services.MessageRenderer;
import com.pigeonnier.view.ViewFactory;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MessageDetailsWindowController extends BaseController implements Initializable {

    private final String LOCATION_OF_DOWNLOAD = System.getProperty("user.home") + "/Downloads/";
    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private Label attachmentsLabel;

    @FXML
    private WebView detailsMessageWebView;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HBox hBoxForAttachments;

    public MessageDetailsWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EmailMessages emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        try {
            loadAttachments(emailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        MessageRenderer messageRenderer = new MessageRenderer(detailsMessageWebView.getEngine());
        messageRenderer.setEmailMessages(emailMessage);
        messageRenderer.restart();
    }

    private void loadAttachments(EmailMessages emailMessages) throws MessagingException {
        if (emailMessages.isHasAttachments()) {
            for (MimeBodyPart mimeBodyPart : emailMessages.getAttachmentList()) {
                CustomButton button = new CustomButton(mimeBodyPart);
                hBoxForAttachments.getChildren().add(button);
            }
        } else {
            attachmentsLabel.setText("");
        }
    }

    private class CustomButton extends Button {
        MimeBodyPart mimeBodyPart;
        String downloadPath;
        File file;
        Desktop desktop;
        public CustomButton(MimeBodyPart mimeBodyPart) throws MessagingException {
            this.mimeBodyPart = mimeBodyPart;
            this.setText(mimeBodyPart.getFileName());
            this.downloadPath = LOCATION_OF_DOWNLOAD + mimeBodyPart.getFileName();
            file = new File(downloadPath);
            desktop = Desktop.getDesktop();
            this.setOnAction(event -> downloadAttachment());
        }

        private void openCurrentFile() {
                try {
                    desktop.open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        private void downloadAttachment() {
            if(!file.exists()) {
                this.setStyle("-fx-background-color: lightblue");
                Service service = new Service() {
                    @Override
                    protected Task createTask() {
                        return new Task() {
                            @Override
                            protected Object call() throws Exception {
                                mimeBodyPart.saveFile(downloadPath);
                                return null;
                            }
                        };
                    }
                };
                service.restart();
                service.setOnSucceeded(event2 -> {
                    this.setStyle("-fx-background-color: lightgreen");
                });
            }
            openCurrentFile();
        }
    }

}
