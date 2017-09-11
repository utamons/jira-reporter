package com.jira.reporter.rest;

import com.jira.reporter.util.Log;
import com.jira.reporter.value.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleg Zaidullin
 */
public class JiraClient {

    private static final String agileUrl = "https://swellby.atlassian.net/rest/agile/latest/";
    private static final String logUrl   = "https://swellby.atlassian.net/rest/api/latest/";

    private final String     board;
    private final Log        log;
    private final RestClient client;

    public JiraClient(String username, String password, String board, Log log) {
        this.board = board;
        this.log = log;
        client = new RestClient(username, password);
    }

    private int getBoardId() throws IOException {
        log.add("Start getting board id...");
        final String url = JiraClient.agileUrl + "board";
        int          id  = 0;

        BoardsResult result = client.get(BoardsResult.class, url, false);

        for (Board b : result.getValues()) {
            if (b.getName().equals(board)) {
                id = b.getId();
            }
        }

        if (id != 0)
            log.add("Got id=" + id);
        else
            log.add("Error! id is not found!");

        return id;
    }

    private List<Task> filterByLog(List<Task> issues, String assignee, Date lastDate) throws IOException {
        final List<Task> result = new ArrayList<>();


        for (Task t : issues) {
           // this.log.add("Checking "+t.getKey());
            final String url       = JiraClient.logUrl + "issue/" + t.getId() + "/changelog";
            final LogResult    logResult = client.get(LogResult.class, url, false);
            final List<LogEntry> log = logResult.getValues();

            for (LogEntry entry: log) {
                if (entry.isAuthor(assignee) && entry.after(lastDate)) {
                    for (LogField f: entry.getItems()) {
                        if (f.isDone() && !result.contains(t)) {
                            result.add(t);
                            break;
                        }
                    }
                }
            }
           // this.log.add("Finished "+t.getKey());
        }

        return result;
    }

    public List<Task> getTasks(String assignee, Date lastDate) throws IOException {
        List<Task> issues = null;
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd HH:mm");

        final int boardId = getBoardId();

        if (boardId > 0) {
            String dateStr = sf.format(lastDate);
            final String url = JiraClient.agileUrl + "board/" + boardId + "/issue?maxResults=1000&jql=assignee=currentUser()"
                    +URLEncoder.encode(" AND updated>\""+dateStr+"\"","UTF-8");

            log.add("Request for tasks...");
            TasksResult result = client.get(TasksResult.class, url, false);
            log.add("Found "+result.getIssues().size()+" tasks updated after "+lastDate);
            issues = result.getIssues()
                           .stream()
                           .filter(t -> t.isDone() && t.hasSprint())
                           .collect(Collectors.toList());
        }

        return filterByLog(issues, assignee, lastDate);
        //return issues;
    }
}
