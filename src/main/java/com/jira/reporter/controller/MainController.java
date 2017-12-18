package com.jira.reporter.controller;

import com.jira.reporter.mail.GmailSender;
import com.jira.reporter.mail.MailSender;
import com.jira.reporter.rest.JiraClient;
import com.jira.reporter.util.Log;
import com.jira.reporter.value.Task;
import com.jira.reporter.value.TaskFields;
import com.jira.reporter.value.TaskValue;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

/**
 * @author Oleg Zaidullin
 */
@SuppressWarnings("unused")
public class MainController {

    public  Button     sendButton;
    private JiraClient client;

    private SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

    public TextArea viewText;
    public TextArea addText;
    public Button   startButton;
    public Button   exitButton;

    private String       username;
    private String       password;
    private String       board;
    private String       assignee;
    private List<String> emails;
    private Date         lastDate;
    private MailSender   mailManager;

    private Log             log;
    private List<TaskValue> taskValues;
    private int reportNum = 1;
    private Preferences prefs;

    public void initialize() {
        try {
            viewText.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> viewText.setScrollTop(Double.MAX_VALUE));
            sendButton.setDisable(true);
            log = new Log(viewText);
            prefs = Preferences.userRoot().node("jira_reporter");
            readSettings(Settings.fromPrefs(prefs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void readSettings(Settings settings) {
        username = settings.getUsername();
        password = settings.getPassword();
        board = settings.getBoard();
        assignee = settings.getAssignee();
        emails = new ArrayList<>();
        for (String email : Arrays.asList(settings.getEmails().split(","))) {
            emails.add(email.trim());
        }
        reportNum = settings.getReportNum();
        lastDate = settings.getLastDate();

        client = new JiraClient(username, password, board, log);
        mailManager = new GmailSender(settings.getGmailUser(), settings.getGmailPassword());
    }

    public void run(MouseEvent mouseEvent) {
        Thread th = new Thread(new TasksCollector());
        th.setDaemon(true);
        th.start();
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void settings(MouseEvent mouseEvent) throws IOException {
        String             fxmlFile           = "/fxml/settings.fxml";
        FXMLLoader         loader             = new FXMLLoader();
        Parent             root               = loader.load(getClass().getResourceAsStream(fxmlFile));
        SettingsController settingsController = loader.getController();

        settingsController.setEvent(this::readSettings);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    private int getIssues() throws IOException {
        log.add("Start getting issues...");

        taskValues = new ArrayList<>();

        List<Task> issues = client.getTasks(assignee, lastDate);

        for (Task t : issues) {
            final TaskFields fields = t.getFields();
            taskValues.add(new TaskValue(t.getKey(),
                                         fields.getIssuetype().getName(),
                                         fields.getPriority().getName(),
                                         fields.getStatus().getName(),
                                         fields.getSummary()));
        }

        log.add("Got " + issues.size() + " done issues");
        taskValues.forEach(t -> log.add(t.getKey() + " " + t.getSummary()));
        return issues.size();
    }

    public void send(MouseEvent mouseEvent) {
        Thread th = new Thread(new Sender());
        th.setDaemon(true);
        th.start();
    }

    class TasksCollector extends javafx.concurrent.Task<Integer> {

        @Override
        protected Integer call() throws Exception {
            startButton.setDisable(true);
            sendButton.setDisable(true);
            int issues = 0;
            if (username == null || password == null || board == null)
                log.add("No settings found.");
            else
                try {
                    log.add("Connecting...");
                    issues = getIssues();
                    log.add("Finish.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            if (issues > 0) {
                sendButton.setDisable(false);
                startButton.setDisable(false);
            } else
                startButton.setDisable(false);
            return 0;
        }
    }


    class Sender extends javafx.concurrent.Task<Integer> {

        @Override
        protected Integer call() {
            if (taskValues.size() == 0) {
                log.add("Nothing to send.");
            } else {
                startButton.setDisable(true);
                sendButton.setDisable(true);
                StringBuilder emailStr = new StringBuilder();
                for (String email : emails)
                    emailStr.append(email).append(", ");

                emailStr.setLength(emailStr.length() - 2);
                log.add("Sending to: " + emailStr.toString());
                try {
                    mailManager.sendMessage(taskValues, addText.getText(), reportNum, emails);
                } catch (MessagingException | IOException e) {
                    log.add("Mail sending error: " + e.getClass().getName() + ", " + e.getMessage());
                }
                log.add("Finished.");
                startButton.setDisable(false);
                lastDate = new Date();
                prefs.put("lastDate", sf.format(lastDate));
                prefs.putInt("reportNum", ++reportNum);
            }
            return 0;
        }
    }
}
