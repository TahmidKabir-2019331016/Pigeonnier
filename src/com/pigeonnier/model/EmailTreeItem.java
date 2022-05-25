package com.pigeonnier.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class EmailTreeItem<String> extends TreeItem<String> {
    private String name;
    private ObservableList<EmailMessages> emailMessages;
    private int unreadMessagesCount;

    public ObservableList<EmailMessages> getEmailMessages() {
        return emailMessages;
    }

    public EmailTreeItem(String name) {
        super(name);
        this.name = name;
        emailMessages = FXCollections.observableArrayList();
        unreadMessagesCount = 0;
    }

    public void setName(String messageCount) {
        this.name = name;
    }

    public void addMessage(Message message) throws MessagingException {
        EmailMessages emailMessage = FetchMessage(message);
        emailMessages.add(emailMessage);
    }

    public void addMessageToTop(Message message) throws MessagingException {
        EmailMessages emailMessage = FetchMessage(message);
        emailMessages.add(0, emailMessage);
    }
    private EmailMessages FetchMessage(Message message) throws MessagingException {
        boolean isRead = message.getFlags().contains(Flags.Flag.SEEN);

        EmailMessages emailMessage = new EmailMessages(
                message.getSubject(),
                message.getFrom()[0].toString(),
                message.getRecipients(MimeMessage.RecipientType.TO)[0].toString(),
                new SizeInteger(message.getSize()),
                message.getReceivedDate(),
                isRead,
                message
        );
//        System.out.println(message.getSubject() + "added.");
        if(!isRead) {
            incrementMessageCount();
        }
        return emailMessage;
    }

    public void incrementMessageCount() {
        unreadMessagesCount++;
        updateName();
    }

    public void decrementMessageCount() {
        unreadMessagesCount--;
        updateName();
    }

    private void updateName() {
        if(unreadMessagesCount > 0)
            this.setValue((String) (name + "(" + unreadMessagesCount + ")"));
        else this.setValue((String)name);
    }


}
