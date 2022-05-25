package com.pigeonnier.services;

import com.pigeonnier.model.EmailMessages;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.ContentType;
import java.io.IOException;

public class MessageRenderer extends Service {

    private StringBuffer displayMessage;
    private WebEngine webEngine;
    private EmailMessages emailMessages;

    public MessageRenderer(WebEngine webEngine) {
        this.webEngine = webEngine;
        this.displayMessage = new StringBuffer();
        this.setOnSucceeded(event -> {
            displayMessageOnWebView();
        });
    }

    private void displayMessageOnWebView() {
        webEngine.loadContent(displayMessage.toString());
    }

    public void setEmailMessages(EmailMessages emailMessages) {
        this.emailMessages = emailMessages;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    loadMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    private void loadMessage() throws MessagingException, IOException {
        displayMessage.setLength(0);
        Message message = emailMessages.getMessage();
        String contentType = message.getContentType();

        if (isSimpleContent(contentType)) {
            displayMessage.append(message.getContent().toString());
        } else if (isMultiPartContent(contentType)) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = multipart.getCount() - 1; i >= 0; --i) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                contentType = bodyPart.getContentType();
                if (isSimpleContent(contentType)) {
                    displayMessage.append(bodyPart.getContent().toString());
                }
            }
        }
    }

    private boolean isMultiPartContent(String contentType) {
        return contentType.contains("multipart");
    }

    private boolean isSimpleContent(String contentType) {
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }


}
