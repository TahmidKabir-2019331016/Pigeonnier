package com.pigeonnier.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessages {

    private SimpleStringProperty subject;
    private SimpleStringProperty sender;
    private SimpleStringProperty recipient;
    private SimpleObjectProperty<SizeInteger> size;
    private SimpleObjectProperty<Date> date;
    private boolean isRead;
    private Message message;
    private List<MimeBodyPart> attachmentList = new ArrayList<>();
    private boolean hasAttachments = false;

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public List<MimeBodyPart> getAttachmentList() {
        return attachmentList;
    }

    public EmailMessages(String subject, String sender, String recipient, SizeInteger size, Date date, boolean isRead, Message message) {
        this.subject = new SimpleStringProperty(subject);
        this.sender = new SimpleStringProperty(sender);
        this.recipient = new SimpleStringProperty(recipient);
        this.size = new SimpleObjectProperty<>(size);
        this.date = new SimpleObjectProperty<>(date);
        this.isRead = isRead;
        this.message = message;
    }

    public String getSubject() {
        return subject.get();
    }

    public String getSender() {
        return sender.get();
    }

    public String getRecipient() {
        return recipient.get();
    }

    public SizeInteger getSize() {
        return size.get();
    }

    public Date getDate() {
        return date.get();
    }

    public boolean isRead() {
        return isRead;
    }

    public Message getMessage() {
        return message;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    /**
     * Adds attachments to message
     * @param mbp: MimeBodyPart
     */
    public void addAttachments(MimeBodyPart mbp) {
        hasAttachments = true;
        if(!attachmentList.contains(mbp)) {
            attachmentList.add(mbp);
            try {
                System.out.println("Attachment Added " + mbp.getFileName());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
