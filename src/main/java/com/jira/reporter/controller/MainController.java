package com.jira.reporter.controller;

import com.jira.reporter.mail.MailgunManager;
import com.jira.reporter.rest.JiraClient;
import com.jira.reporter.util.Log;
import com.jira.reporter.value.Task;
import com.jira.reporter.value.TaskFields;
import com.jira.reporter.value.TaskValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public  Button              sendButton;
    private JiraClient client;

    private SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);

    public TextArea viewText;
    public TextArea addText;
    public Button   startButton;
    public Button   exitButton;

    private String         username;
    private String         password;
    private String         board;
    private String         assignee;
    private List<String>   emails;
    private Date           lastDate;
    private MailgunManager mailManager;

    private Log             log;
    private List<TaskValue> taskValues;
    private int reportNum = 1;
    private Preferences settings;

    public void initialize() {
        sendButton.setDisable(true);

        log = new Log(viewText);
        settings = Preferences.userRoot().node("jira_reporter");
        username = settings.get("username", null);
        password = settings.get("password", null);
        board = settings.get("board", null);
        assignee = settings.get("assignee", null);
        emails = Arrays.asList(settings.get("emails", null).split(","));
        reportNum = settings.getInt("reportNum", 0);
        try {
            lastDate = sf.parse(settings.get("lastDate", null));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        client = new JiraClient(username,password,board,log);
        mailManager = new MailgunManager(settings.get("mailgunUrl", null), settings.get("mailgunKey", null));
    }

    public void run(MouseEvent mouseEvent) {
        startButton.setDisable(true);

        Thread th = new Thread(new TasksCollector());
        th.setDaemon(true);
        th.start();
    }

    public void exit(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void settings(MouseEvent mouseEvent) throws IOException {
        String     fxmlFile = "/fxml/settings.fxml";
        FXMLLoader loader   = new FXMLLoader();
        Parent     root     = loader.load(getClass().getResourceAsStream(fxmlFile));
        SettingsController settingsController = loader.getController();
        Stage      stage    = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Settings");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
        stage.show();
    }

    private void getIssues() throws IOException {
        log.add("Start getting issues...");

        taskValues = new ArrayList<>();

        List<Task> issues = client.getTasks(assignee,lastDate);

        for (Task t : issues) {
            final TaskFields fields = t.getFields();
            taskValues.add(new TaskValue(t.getKey(),
                                         fields.getIssuetype().getName(),
                                         fields.getPriority().getName(),
                                         fields.getStatus().getName(),
                                         fields.getSummary()));
        }

        log.add("Got " + issues.size() + " issues");
    }

    public void send(MouseEvent mouseEvent) {
        Thread th = new Thread(new MailSender());
        th.setDaemon(true);
        th.start();
    }

    class TasksCollector extends javafx.concurrent.Task<Integer> {

        @Override
        protected Integer call() throws Exception {
            if (username == null || password == null || board == null)
                log.add("No settings found.");
            else
                try {
                    log.add("Last date - " + lastDate);
                    log.add("Connecting...");
                    getIssues();
                    log.add("Finish.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            sendButton.setDisable(false);
            return 0;
        }
    }

    class MailSender extends javafx.concurrent.Task<Integer> {

        @Override
        protected Integer call() throws Exception {
            if (taskValues.size() == 0) {
                log.add("Nothing to send.");
            }
            else {
                sendButton.setDisable(true);
                log.add("Sending to:");
                for (String email : emails)
                    log.add(email);
                mailManager.sendMessage(taskValues, addText.getText(), reportNum, emails);
                log.add("Finished.");
                startButton.setDisable(false);
                settings.put("lastDate", sf.format(new Date()));
                settings.putInt("reportNum", ++reportNum);
            }
            return 0;
        }
    }
}
