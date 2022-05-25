package com.pigeonnier.services;

import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.view.EmailSendingResult;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailComposerService extends Service<EmailSendingResult> {

    private EmailAccount emailAccount;
    private String content;
    private String subject;
    private String recipient;

    public EmailComposerService(EmailAccount emailAccount, String content, String subject, String recipient) {
        this.emailAccount = emailAccount;
        this.content = content;
        this.subject = subject;
        this.recipient = recipient;
    }

    @Override
    protected Task<EmailSendingResult> createTask() {
        return new Task<EmailSendingResult>() {
            @Override
            protected EmailSendingResult call() throws Exception {
                try {
                    // Creating Message
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    mimeMessage.setFrom(emailAccount.getAddress());
                    mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
                    mimeMessage.setSubject(subject);

                    // Setting Content
                    Multipart multipart = new MimeMultipart();
                    BodyPart bodyPart = new MimeBodyPart();
                    bodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(bodyPart);
                    mimeMessage.setContent(multipart);

                    // Sending Message
                    Transport transport = emailAccount.getSession().getTransport();
                    transport.connect(emailAccount.getProperties().getProperty("outgoingHost"), emailAccount.getAddress(), emailAccount.getPassword());
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();
                    return EmailSendingResult.SUCCESS;
                }catch (MessagingException e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                } catch (Exception e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }
}
