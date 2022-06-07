package com.pigeonnier.model;

import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

public class EmailAccount {

    private String address;
    private String password;
    private Properties properties;
    private Store store;
    private Session session;

    public EmailAccount(String address, String password) {
        this.address = address;
        this.password = password;
        this.properties = new Properties();
//            this.properties = System.getProperties();
//        properties.put("incomingHost", "imap.gmail.com");
//        properties.put("mail.store.protocol", "imaps");
//
//        properties.put("mail.smtps.host", "smtp.gmail.com");
//        properties.put("mail.smtps.auth", "true");
//        properties.put("mail.transport.protocol", "smtps");
//        properties.put("outgoingHost", "smtp.gmail.com");
        String outgoingHost, incomingHost;

        if(address.contains("yahoo")) {
            outgoingHost = "smtp.mail.yahoo.com";
            incomingHost = "imap.mail.yahoo.com";
        }
        else {
            outgoingHost = "smtp.gmail.com";
            incomingHost = "imap.gmail.com";
        }
        properties.put("mail.smtps.host", outgoingHost);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.transport.protocol", "smtps");
        properties.put("mail.store.protocol", "imap");
        properties.put("outgoingHost", outgoingHost);
        properties.put("incomingHost", incomingHost);
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.mail.auth", "true");

    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
