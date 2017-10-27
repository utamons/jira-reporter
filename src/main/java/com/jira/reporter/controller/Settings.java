package com.jira.reporter.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * @author Oleg Zaidullin
 */
public class Settings {

    private final String           assignee;
    private final Date             lastDate;
    private final String           emails;
    private final int              reportNum;
    private final String           gmailUser;
    private final String           gmailPassword;
    private final String           username;
    private final String           password;
    private final String           board;
    private final SimpleDateFormat sf;

    public Settings(String assignee, String lastDateStr, String emails, int reportNum, String gmailUser,
                    String gmailPassword, String username, String password, String board) throws ParseException {


        sf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.ENGLISH);
        lastDate = sf.parse(lastDateStr);
        this.assignee = assignee;
        this.emails = emails;
        this.reportNum = reportNum;
        this.gmailPassword = gmailPassword;
        this.gmailUser = gmailUser;
        this.username = username;
        this.password = password;
        this.board = board;
    }

    static Settings fromPrefs(Preferences prefs) throws ParseException {
        return new Settings(
                prefs.get("assignee", ""),
                prefs.get("lastDate", "01.01.1970 00:00:00"),
                prefs.get("emails", ""),
                Integer.parseInt(prefs.get("reportNum", "1")),
                prefs.get("gmailUser", ""),
                prefs.get("gmailPassword", ""),
                prefs.get("username", ""),
                prefs.get("password", ""),
                prefs.get("board", "")
        );
    }

    String getDateString() {
        return sf.format(lastDate);
    }

    String getAssignee() {
        return assignee;
    }

    Date getLastDate() {
        return lastDate;
    }

    String getEmails() {
        return emails;
    }

    int getReportNum() {
        return reportNum;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getBoard() {
        return board;
    }

    public String getGmailUser() {
        return gmailUser;
    }

    public String getGmailPassword() {
        return gmailPassword;
    }
}
