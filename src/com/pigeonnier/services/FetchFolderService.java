package com.pigeonnier.services;

import com.pigeonnier.model.EmailTreeItem;
import com.pigeonnier.view.IconResolver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.awt.*;
import java.util.List;

public class FetchFolderService extends Service<Void> {
    private IconResolver iconResolver = new IconResolver();
    private Store store;
    private EmailTreeItem<String> foldersRoot;

    private List<Folder> foldersList;
    public FetchFolderService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> foldersList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.foldersList = foldersList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
        EmailTreeItem composeTreeItem = new EmailTreeItem("Compose Message");
        composeTreeItem.setGraphic(iconResolver.getIconForFolder("Compose"));
        foldersRoot.getChildren().add(composeTreeItem);
        EmailTreeItem logoutTreeItem = new EmailTreeItem("Logout");
        logoutTreeItem.setGraphic(iconResolver.getIconForFolder("logout"));
        foldersRoot.getChildren().add(logoutTreeItem);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for (Folder folder : folders) {
            foldersList.add(folder);
            EmailTreeItem treeItem = new EmailTreeItem(folder.getName());
            treeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add(treeItem);
            foldersRoot.setExpanded(true);
            fetchMessages(folder, treeItem);
            addMessageListener(folder, treeItem);
            if(folder.getType() == Folder.HOLDS_FOLDERS) {
                Folder[] subFolders = folder.list();
                handleFolders(subFolders,treeItem);
            }
         }
    }

    private void addMessageListener(Folder folder, EmailTreeItem treeItem) {
        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent messageCountEvent) {
                for(int i = 0; i < messageCountEvent.getMessages().length; ++i) {
                    try {
                        Message message = folder.getMessage(folder.getMessageCount() - i);
                        treeItem.addMessageToTop(message);
                        Notifications notifications = Notifications.create().title("New Message!").text("You Have New Message from " + message.getFrom()[0].toString());
                        notifications.graphic(null);
                        notifications.hideAfter(Duration.seconds(2));
                        notifications.position(Pos.BOTTOM_RIGHT);
                        notifications.onAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Notification Clicked!");
                            }
                        });
                        notifications.showConfirm();
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent messageCountEvent) {
                System.out.println("Message Removed!");
            }
        });
    }

    private void fetchMessages(Folder folder, EmailTreeItem treeItem) {
       new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(folder.getType() != Folder.HOLDS_FOLDERS) {
                            folder.open(Folder.READ_WRITE);
                            int messageCount = folder.getMessageCount();
                            for(int i = messageCount; i >= 0; i--) {
                                treeItem.addMessage(folder.getMessage(i));
                            }
                        }
                        return null;
                    }
                };
            }
        }.start();
    }
}
