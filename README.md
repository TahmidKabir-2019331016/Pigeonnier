# Pigeonnier - Java Email Client

Pigeonnier is a feature-rich email client built with JavaFX that supports multiple email accounts, attachments, and real-time email notifications.

## Features

### Functional Features

#### 1. Multi-Account Support

- Support for multiple email accounts (Gmail and Yahoo)
- Seamless account switching
- Persistent login sessions

```java
public void addEmailAccount(EmailAccount emailAccount) {
    emailAccountsList.add(emailAccount);
    EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
    treeItem.setGraphic(iconResolver.getIconForFolder(emailAccount.getAddress()));
    treeItem.setExpanded(true);
    // ... setup folder fetching
    foldersRoot.getChildren().add(treeItem);
}
```

#### 2. Email Management

- Compose and send emails with rich text formatting
- Support for email attachments
- Mark emails as read/unread
- Delete emails
- View email details including attachments

```java
public void setRead() {
    try {
        selectedMessage.setRead(true);
        selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
        selectedFolder.decrementMessageCount();
    } catch (MessagingException e) {
        e.printStackTrace();
    }
}
```

#### 3. Real-time Updates

- Live folder updates
- Real-time email notifications
- Unread message counter

```java
private void addMessageListener(Folder folder, EmailTreeItem treeItem) {
    folder.addMessageCountListener(new MessageCountListener() {
    @Override
    public void messagesAdded(MessageCountEvent messageCountEvent) {
    // ... handle new messages
        Notifications notifications = Notifications.create()
        .title("New Message!")
        .text("You Have New Message from " + message.getFrom()[0].toString());
    }
    });
}
```

#### 4. Folder Organization

- Hierarchical folder structure
- Custom folder icons
- Automatic folder synchronization

### Non-Functional Features

#### 1. Security

- Encrypted password storage using Base64 encoding
- Secure IMAP/SMTP connections
- Session-based authentication

```java
public class ValidAccount implements Serializable {
    private String encodedAddress;
    private String encodedPassword;
    public ValidAccount(String address, String password) {
        this.encodedAddress = Base64.getEncoder().encodeToString(address.getBytes());
        this.encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
    }
}
```

#### 2. Performance

- Asynchronous email fetching using JavaFX Service
- Lazy loading of email content
- Background folder updates

```java
public class FetchFolderService extends Service<Void> {
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
}
```

#### 3. Customization

- Multiple color themes (Light, Dark, Default)
- Configurable font sizes
- Persistent user preferences

```java
public enum ColorTheme {
    Light, Default, Dark;
    public static String getPath(ColorTheme colorTheme) {
        return switch (colorTheme) {
            case Default -> "css/themeDefault.css";
            case Dark -> "css/themeDark.css";
            case Light -> "css/themeLight.css";
        };
    }
}
```

#### 4. User Experience

- Progress indicators for long operations
- Error handling with user-friendly messages
- Intuitive folder navigation
- Rich text email composition

## Technical Requirements

- Java 11 or higher
- JavaFX
- Internet connection for email services
- Support for IMAP/SMTP protocols

## Setup Instructions

1. Clone the repository
2. Configure your email credentials:
   - For Gmail: Enable 2-factor authentication and generate an app password
   - For Yahoo: Generate an app password from account settings
3. Run the Launcher class
4. Enter your email and app password when prompted

## Usage

1. **Adding an Account**:

   - Click "Add Account" button
   - Enter email address and app password
   - Select "Remember Me" for persistent login

2. **Composing Email**:

   - Click "Compose" button or select "Compose Message" folder
   - Select sending account if multiple accounts are configured
   - Add recipients, subject, and content
   - Attach files if needed
   - Click "Send"

3. **Managing Emails**:
   - Click on folders to view messages
   - Double-click messages to view content
   - Right-click for additional options (Mark as read/unread, Delete)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- JavaFX for the UI framework
- JavaMail API for email functionality
- ControlsFX for additional UI components
- 
