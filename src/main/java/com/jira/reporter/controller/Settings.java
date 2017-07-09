package com.jira.reporter.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Oleg Zaidullin
 */
public class Settings {
    
    private final String assignee;
    private final Date   lastDate;
    private final String emails;
    private final int reportNum;
    private final String mailgunUrl;
    private final String mailgunKey;
    private final String username;
    private final String password;
    private final String board;
    private final SimpleDateFormat sf;

    public Settings(String assignee, String lastDateStr, String emails, int reportNum, String mailgunUrl,
                    String mailgunKey, String username, String password, String board) throws ParseException {


        sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
        lastDate = sf.parse(lastDateStr);
        this.assignee = assignee;
        this.emails = emails;
        this.reportNum = reportNum;
        this.mailgunUrl = mailgunUrl;
        this.mailgunKey = mailgunKey;
        this.username = username;
        this.password = password;
        this.board = board;
    }

    public String getDateString() {
        return sf.format(lastDate);
    }

    public String getAssignee() {
        return assignee;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public String getEmails() {
        return emails;
    }

    public int getReportNum() {
        return reportNum;
    }

    public String getMailgunUrl() {
        return mailgunUrl;
    }

    public String getMailgunKey() {
        return mailgunKey;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBoard() {
        return board;
    }
}
