package com.pigeonnier.services;

import com.pigeonnier.EmailManager;
import com.pigeonnier.controller.LoginResults;
import com.pigeonnier.model.EmailAccount;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.*;

public class LoginService extends Service<LoginResults> {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    @Override
    protected Task<LoginResults> createTask() {
        return new Task<LoginResults>() {
            @Override
            protected LoginResults call() throws Exception {
                return login();
            }
        };
    }

    private LoginResults login() {
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };
        try {
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            emailAccount.setSession(session);
            Store store = session.getStore("imaps");
            store.connect(emailAccount.getProperties().getProperty("incomingHost"), emailAccount.getAddress(), emailAccount.getPassword());
            emailAccount.setStore(store);
            emailManager.addEmailAccount(emailAccount);

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return LoginResults.FAILED_BY_NETWORK;
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
            return LoginResults.FAILED_BY_CREDENTIALS;
        } catch (MessagingException e) {
            e.printStackTrace();
            return LoginResults.FAILED_BY_UNEXPECTED_ERROR;
        } catch (Exception e) {
            return LoginResults.FAILED_BY_UNEXPECTED_ERROR;
        }
        return LoginResults.SUCCESS;
    }
}
