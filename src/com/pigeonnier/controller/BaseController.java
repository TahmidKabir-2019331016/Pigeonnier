package com.pigeonnier.controller;

import com.pigeonnier.EmailManager;
import com.pigeonnier.view.ViewFactory;

public class BaseController {
    protected EmailManager emailManager;
    protected ViewFactory viewFactory;
    private String FxmlName;

    public BaseController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        this.emailManager = emailManager;
        this.viewFactory = viewFactory;
        FxmlName = fxmlName;
    }

    public String getFxmlName() {
        return FxmlName;
    }
}


