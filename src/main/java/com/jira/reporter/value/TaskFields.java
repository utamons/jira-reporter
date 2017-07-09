package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskFields {
    private NamedProperty issuetype;
    private String        timespent;
    private NamedProperty priority;
    private NamedProperty assignee;
    private Date          updated;
    private NamedProperty status;
    private String description;
    private String summary;
    private NamedProperty creator;
    private NamedProperty sprint;

    public NamedProperty getSprint() {
        return sprint;
    }

    public void setSprint(NamedProperty sprint) {
        this.sprint = sprint;
    }

    public NamedProperty getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(NamedProperty issuetype) {
        this.issuetype = issuetype;
    }

    public String getTimespent() {
        return timespent;
    }

    public void setTimespent(String timespent) {
        this.timespent = timespent;
    }

    public NamedProperty getPriority() {
        return priority;
    }

    public void setPriority(NamedProperty priority) {
        this.priority = priority;
    }

    public NamedProperty getAssignee() {
        return assignee;
    }

    public void setAssignee(NamedProperty assignee) {
        this.assignee = assignee;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public NamedProperty getStatus() {
        return status;
    }

    public void setStatus(NamedProperty status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public NamedProperty getCreator() {
        return creator;
    }

    public void setCreator(NamedProperty creator) {
        this.creator = creator;
    }
}
