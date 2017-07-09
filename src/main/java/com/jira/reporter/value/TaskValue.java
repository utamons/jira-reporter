package com.jira.reporter.value;

/**
 * @author Oleg Zaidullin
 */
public class TaskValue {
    private String key;
    private String issueType;
    private String priority;
    private String status;
    private String summary;

    public TaskValue(String key, String issueType, String priority, String status, String summary) {
        this.key = key;
        this.issueType = issueType;
        this.priority = priority;
        this.status = status;
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
