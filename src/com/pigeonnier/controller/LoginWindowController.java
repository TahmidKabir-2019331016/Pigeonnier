package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.model.EmailAccount;
import com.pigeonnier.services.LoginService;
import com.pigeonnier.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController implements Initializable {

    @FXML
    private TextField EmailTextField;

    @FXML
    private Label ErrorLabel;

    @FXML
    private PasswordField PasswordTextField;
    @FXML
    private Label welcome;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        EmailTextField.setText("tahmidkabiraddin@yahoo.com");
//        PasswordTextField.setText("nozxuulofqrcwxiw");
        EmailTextField.setText("tahmidkabiraddin@gmail.com");
        PasswordTextField.setText("tkmhgmvcxotmhawy");

    }

    @FXML
    void LoginButtonAction(ActionEvent event) throws InterruptedException {
        ErrorLabel.setText("");
        System.out.println("Login Button Clicked!");
        if (isValid()) {
            EmailAccount emailAccount = new EmailAccount(EmailTextField.getText(), PasswordTextField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event1 -> {
                LoginResults loginResults = loginService.getValue();

                switch (loginResults) {
                    case SUCCESS -> {
                        System.out.println("login successful!");
                        if(!viewFactory.isMainWindowOpened()) {
                            viewFactory.showMainWindow();
                            viewFactory.setMainWindowOpened(true);
                        }
                        Stage stage = (Stage) ErrorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                    }
                    case FAILED_BY_UNEXPECTED_ERROR -> ErrorLabel.setText("Unexpected Error! Try Again");
                    case FAILED_BY_NETWORK -> ErrorLabel.setText("Failed by Network!");
                    case FAILED_BY_CREDENTIALS -> ErrorLabel.setText("Wrong Credentials!");
                }
            });
        }
    }

    private boolean isValid() {
        if (EmailTextField.getText().isBlank()) {
            ErrorLabel.setText("Please Fill your email");
            return false;
        }
        if (PasswordTextField.getText().isBlank()) {
            ErrorLabel.setText("Please Fill your password");
            return false;
        }
        return true;
    }
}
