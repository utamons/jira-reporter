package com.jira.reporter.controller;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

/**
 * @author Oleg Zaidullin
 */
public class SettingsController {

    public  TextField     assignee;
    public  TextField     lastDate;
    public  TextArea      emails;
    public  TextField     reportNum;
    public  TextField     mailgunUrl;
    public  TextField     mailgunKey;
    private Preferences   settings;
    public  TextField     username;
    public  PasswordField password;
    public  TextField     board;
    public  Button        cancelButton;
    public  Button        okButton;

    public void initialize() {
        settings = Preferences.userRoot().node("jira_reporter");
        String usernameValue = settings.get("username", null);
        if (usernameValue != null)
            username.setText(usernameValue);

        String passwordValue = settings.get("password", null);
        if (passwordValue != null)
            this.password.setText(passwordValue);

        String boardValue = settings.get("board", null);
        if (boardValue != null)
            board.setText(boardValue);

        String assigneeValue = settings.get("assignee", null);
        if (assigneeValue != null)
            assignee.setText(assigneeValue);

        String lastDateValue = settings.get("lastDate", null);
        if (lastDateValue != null)
            lastDate.setText(lastDateValue);

        String emailsValue = settings.get("emails", null);
        if (emailsValue != null)
            emails.setText(emailsValue);

        String numValue = settings.get("reportNum", null);
        if (numValue != null)
            reportNum.setText(numValue);

        String mailgunUrlValue = settings.get("mailgunUrl", null);
        if (mailgunUrlValue != null)
            mailgunUrl.setText(mailgunUrlValue);

        String mailgunKeyValue = settings.get("mailgunKey", null);
        if (mailgunKeyValue != null)
            mailgunKey.setText(mailgunKeyValue);
    }

    public void save(MouseEvent mouseEvent) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        settings.put("username", username.getText());
        settings.put("password", password.getText());
        settings.put("board", board.getText());
        settings.put("lastDate", lastDate.getText());
        settings.put("emails", emails.getText());
        settings.put("assignee", assignee.getText());
        settings.putInt("reportNum", Integer.parseInt(reportNum.getText()));
        settings.put("mailgunUrl",mailgunUrl.getText());
        settings.put("mailgunKey",mailgunKey.getText());
        stage.close();
    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
