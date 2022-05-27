package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.model.EmailMessages;
import com.pigeonnier.services.MessageRenderer;
import com.pigeonnier.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
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
        if(emailMessages.isHasAttachments()) {
            for(MimeBodyPart mimeBodyPart: emailMessages.getAttachmentList()) {
                Button button = new Button(mimeBodyPart.getFileName());
                hBoxForAttachments.getChildren().add(button);
            }
        } else {
            attachmentsLabel.setText("");
        }
    }


}
