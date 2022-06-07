package com.pigeonnier;;

import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.model.EmailMessages;
import com.pigeonnier.persistence.PersistenceAccess;
import com.pigeonnier.persistence.ValidAccount;
import com.pigeonnier.services.LoginService;
import com.pigeonnier.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(emailManager);

        List<ValidAccount> list = persistenceAccess.loadFromPersistence();
        if(list.isEmpty()) {
            System.out.println("list is empty");
            viewFactory.showLoginWindow();
        } else {
            for(ValidAccount validAccount: list) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
            viewFactory.showMainWindow();
        }

//        viewFactory.showLoginWindow();
//        viewFactory.showMainWindow();
//        viewFactory.showOptionsWindow();
//        viewFactory.showComposeMessageWindow();
    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> list = new ArrayList<>();

        for(EmailAccount emailAccount: emailManager.getEmailAccountsList()) {
            list.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }

        persistenceAccess.saveToPersistence(list);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
