package com.jira.reporter.controller;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.text.ParseException;
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
    public  TextField     username;
    public  PasswordField password;
    public  TextField     board;
    public  Button        cancelButton;
    public  Button        okButton;
    private Settings      settings;
    private Preferences prefs;

    private void settingsFromPref() throws ParseException {
        settings = new Settings(
                prefs.get("assignee", ""),
                prefs.get("lastDate", "01.01.1970 00:00:00"),
                prefs.get("emails", ""),
                Integer.parseInt(prefs.get("reportNum", "1")),
                prefs.get("mailgunUrl", ""),
                prefs.get("mailgunKey", ""),
                prefs.get("username", ""),
                prefs.get("password", ""),
                prefs.get("board", "")
        );
    }

    private void setSettingsFromFields() throws ParseException {
        settings = new Settings(
                assignee.getText(),
                lastDate.getText(),
                emails.getText(),
                Integer.parseInt(reportNum.getText()),
                mailgunUrl.getText(),
                mailgunKey.getText(),
                username.getText(),
                password.getText(),
                board.getText());
    }

    private void prefsFromSetting() {
        prefs.put("username", settings.getUsername());
        prefs.put("password", settings.getPassword());
        prefs.put("board", settings.getBoard());
        prefs.put("lastDate", settings.getDateString());
        prefs.put("emails", settings.getEmails());
        prefs.put("assignee", settings.getAssignee());
        prefs.putInt("reportNum", Integer.parseInt(reportNum.getText()));
        prefs.put("mailgunUrl", settings.getMailgunUrl());
        prefs.put("mailgunKey",settings.getMailgunKey());
    }

    private void fieldsFromSettings() {
        assignee.setText(settings.getAssignee());
        lastDate.setText(settings.getDateString());
        emails.setText(settings.getEmails());
        reportNum.setText(String.valueOf(settings.getReportNum()));
        mailgunUrl.setText(settings.getMailgunUrl());
        mailgunKey.setText(settings.getMailgunKey());
        username.setText(settings.getUsername());
        password.setText(settings.getPassword());
        board.setText(settings.getBoard());
    }

    public void initialize() {
        try {
            prefs = Preferences.userRoot().node("jira_reporter");
            settingsFromPref();
            fieldsFromSettings();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void save(MouseEvent mouseEvent) {
        try {
            Stage stage = (Stage) okButton.getScene().getWindow();
            setSettingsFromFields();
            prefsFromSetting();
            stage.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void close(MouseEvent mouseEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
