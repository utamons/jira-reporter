package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TasksResult {
    private int     maxResults;
    private int     startAt;
    private int     total;
    private List<Task>  issues;

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getStartAt() {
        return startAt;
    }

    public void setStartAt(int startAt) {
        this.startAt = startAt;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Task> getIssues() {
        return issues;
    }

    public void setIssues(List<Task> issues) {
        this.issues = issues;
    }
}
