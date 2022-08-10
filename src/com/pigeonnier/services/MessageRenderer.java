package com.pigeonnier.services;

import com.pigeonnier.model.EmailMessages;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.web.WebEngine;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeBodyPart;
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

    /**`
     * This method will load Message according to its type and save that to stringBuffer
     * @throws MessagingException
     * @throws IOException
     */
    private void loadMessage() throws MessagingException, IOException {
        displayMessage.setLength(0); //clears the SB
        Message message = emailMessages.getMessage();
        String contentType = message.getContentType();
        if(isSimpleContent(contentType)){
            displayMessage.append(message.getContent().toString());
        } else if(isMultiPartContent(contentType)){
            Multipart multipart = (Multipart) message.getContent();
            loadMultipart(multipart, displayMessage);
        }
    }

    /**
     * This Method will load multipart type messages recursively
     * @param multipart
     * @param stringBuffer
     * @throws MessagingException
     * @throws IOException
     */
    private void loadMultipart(Multipart multipart, StringBuffer stringBuffer) throws MessagingException, IOException {
        for (int i = multipart.getCount() - 1; i>=0; i--){
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if (isSimpleContent(contentType)){
                stringBuffer.append(bodyPart.getContent().toString());
            } else if(isMultiPartContent(contentType)){
                Multipart multipart2 = (Multipart) bodyPart.getContent();
                loadMultipart(multipart2, stringBuffer);
            } else if(!isTextPlain(contentType)){
                //handling attachments
                MimeBodyPart mbp = (MimeBodyPart) bodyPart;
                emailMessages.addAttachments(mbp);
            }
        }
    }

    /**
     * Checks whether content is MultiPart or not
     * @param contentType
     * @return
     */
    private boolean isMultiPartContent(String contentType) {
        return contentType.contains("multipart");
    }

    /**
     * Checks whether content is Simple or not
     * @param contentType
     * @return
     */
    private boolean isSimpleContent(String contentType) {
        return contentType.contains("TEXT/HTML") || contentType.contains("mixed") || contentType.contains("text");
    }

    /**
     * Checks whether content only contains Text
     * @param contentType
     * @return
     */
    private boolean isTextPlain(String contentType) {
        return contentType.contains("TEXT/PLAIN");
    }

    public void cleanWebEngine() {
        webEngine.load("");
    }
}
