package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.model.EmailMessages;
import com.pigeonnier.model.EmailTreeItem;
import com.pigeonnier.model.SizeInteger;
import com.pigeonnier.services.MessageRenderer;
import com.pigeonnier.view.ColorTheme;
import com.pigeonnier.view.ViewFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowController extends BaseController implements Initializable {

    private MenuItem markUnreadMenutItem = new MenuItem("Mark as unread");
    private MenuItem deleteMessageMenutItem = new MenuItem("Delete message");
    private MenuItem viewDetails = new MenuItem("View Details");

    @FXML
    private TableView<EmailMessages> emailsTableView;

    @FXML
    private TableColumn<EmailMessages, Date> dateCol;

    @FXML
    private TableColumn<EmailMessages, String> recipientCol;

    @FXML
    private TableColumn<EmailMessages, String> senderCol;

    @FXML
    private TableColumn<EmailMessages, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessages, String> subjectCol;
    @FXML
    private TreeView<String> emailsTreeView;
    @FXML
    private WebView emailsWebView;

    private MessageRenderer messageRenderer;

    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        emailsWebView.setVisible(false);
        setUpTreeView();
        setUpTableView();
        setUpFolderSelection();
        updateRowBold();
        setUpMessageRenderer();
        setUpEmailSelection();
        setUpContextMenu();
    }

    private void setUpContextMenu() {
        markUnreadMenutItem.setOnAction(event -> {
            emailManager.setUnread();
        });
        deleteMessageMenutItem.setOnAction(event -> {
            emailManager.deleteMessage();
            emailsWebView.getEngine().loadContent("");
        });
        viewDetails.setOnAction(event -> {
            viewFactory.showMessageDetailsWindow();
        });
    }

    private void setUpMessageRenderer() {
        messageRenderer = new MessageRenderer(emailsWebView.getEngine());
    }

    private void updateRowBold() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessages>, TableRow<EmailMessages>>() {
            @Override
            public TableRow<EmailMessages> call(TableView<EmailMessages> param) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(EmailMessages item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            if (item.isRead()) {
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold;");
                            }
                        }
                    }
                };
            }
        });
    }

    private void setUpEmailSelection() {
        emailsTableView.setOnMouseClicked(event -> {
            EmailMessages emailMessages = emailsTableView.getSelectionModel().getSelectedItem();
            if (emailMessages != null) {
                emailManager.setSelectedMessage(emailMessages);
                if (!emailMessages.isRead()) {
                    emailManager.setRead();
                }
                messageRenderer.setEmailMessages(emailMessages);
                messageRenderer.restart();
            }
        });
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(event -> {
            EmailTreeItem<String> item = (EmailTreeItem<String>) emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                for(EmailAccount emailAccount: emailManager.getEmailAccountsList()) {
                    if (emailAccount.getAddress().equals(item.getParent().getValue())) {
                        emailManager.currentEmail = emailAccount;
                        break;
                    }
                }
                emailManager.setSelectedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());
                if (item.getValue().contains("Compose")) {
                    viewFactory.showComposeMessageWindow();
                    viewFactory.composeMessageWindowController.setUpChoiceBox();
                } else if (item.getValue().contains("Logout")) {
                    emailManager.removeAccount();
                    removeItem(item);
                }
            }

        });
    }

    private void removeItem(EmailTreeItem<String> item) {
        while (!item.getValue().contains("Root")) {
            EmailTreeItem<String> temp = (EmailTreeItem<String>) item.getParent();
            item.getParent().getChildren().remove(item);
            item = temp;
        }
    }

    private void setUpTableView() {
        senderCol.setCellValueFactory(new PropertyValueFactory<EmailMessages, String>("sender"));
        recipientCol.setCellValueFactory(new PropertyValueFactory<EmailMessages, String>("recipient"));
        dateCol.setCellValueFactory(new PropertyValueFactory<EmailMessages, Date>("date"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<EmailMessages, SizeInteger>("size"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<EmailMessages, String>("subject"));

        emailsTableView.setContextMenu(new ContextMenu(markUnreadMenutItem, deleteMessageMenutItem, viewDetails));
    }

    private void setUpTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);
    }

    @FXML
    void OptionsAction(ActionEvent event) {
        viewFactory.showOptionsWindow();
    }

    @FXML
    void AddAccount(ActionEvent event) {
        viewFactory.showLoginWindow();
    }

    @FXML
    void composeMessageAction(ActionEvent event) {
        emailManager.currentEmail = emailManager.getEmailAccountsList().get(0);
        viewFactory.showComposeMessageWindow();
    }

    /**
     * This function will show an alert dialog box to exit from teh application with OK and CANCEL option.
     * If Ok is selected It will close the application.
     */
    @FXML
    void ExitFromApp() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getDialogPane().getButtonTypes().add(cancelButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get().equals(ButtonType.OK))
                Platform.exit();
            else alert.close();
        }
    }

}

