package com.jira.reporter.util;


import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * @author Oleg Zaidullin
 */
public class Log {
    private TextArea      out;
    private StringBuilder log;

    public Log(TextArea out) {
        this.out = out;
        log = new StringBuilder();
    }

    public void add(String line) {
        Platform.runLater(() -> {
            log.append(line).append("\n");
            out.setText(log.toString());
            out.appendText(""); //a workaround for setScrollTop() on out change listener. Weird, but works.
        });
    }
}
