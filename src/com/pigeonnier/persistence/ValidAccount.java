package com.pigeonnier.persistence;

import java.io.Serializable;
import java.util.Base64;

/**
 * Serializable is a marker interface which is used to mark a class which object needs to be persisted
 * We need to save email address and password so this class will hold two members address and password
 */
public class ValidAccount implements Serializable {

    private String encodedAddress;
    private String encodedPassword;

    public ValidAccount(String address, String password) {
        this.encodedAddress = Base64.getEncoder().encodeToString(address.getBytes());
        this.encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String getAddress() {
        return new String(Base64.getDecoder().decode(encodedAddress));
    }

    public String getPassword() {
        return new String(Base64.getDecoder().decode(encodedPassword));
    }
}
