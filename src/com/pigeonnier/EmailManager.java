package com.pigeonnier;

import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.model.EmailMessages;
import com.pigeonnier.model.EmailTreeItem;
import com.pigeonnier.services.FetchFolderService;
import com.pigeonnier.services.FolderUpdaterService;
import com.pigeonnier.view.IconResolver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private IconResolver iconResolver = new IconResolver();
    private EmailMessages selectedMessage;

    private EmailTreeItem<String> selectedFolder;
    //Handling Folders
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    private List<Folder> folderList = new ArrayList<>();

    private ObservableList<EmailAccount> emailAccountsList = FXCollections.observableArrayList();

    public ObservableList<EmailAccount> getEmailAccountsList() {
        return emailAccountsList;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    private FolderUpdaterService folderUpdaterService;

    public EmailMessages getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessages selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public EmailManager() {
        folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        emailAccountsList.add(emailAccount);
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
        treeItem.setExpanded(true);
        FetchFolderService fetchFolderService = new FetchFolderService(emailAccount.getStore(), treeItem, folderList);
        fetchFolderService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementMessageCount();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void setUnread() {
        try {
            selectedMessage.setRead(false);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, false);
            selectedFolder.incrementMessageCount();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage() {
        try {
            selectedMessage.getMessage().setFlag(Flags.Flag.DELETED, true);
            selectedFolder.getEmailMessages().remove(selectedMessage);
            selectedFolder.decrementMessageCount();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
