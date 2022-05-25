module Pigeonnier {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.mail;
    requires activation;

    opens com.pigeonnier;
    opens com.pigeonnier.view;
    opens com.pigeonnier.controller;
    opens com.pigeonnier.model;
}