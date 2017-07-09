package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    private String key;
    private String id;
    private TaskFields fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TaskFields getFields() {
        return fields;
    }

    public boolean isDone() {
        return fields.getStatus().getName().equals("Done") || fields.getStatus().getName().equals("Ready for Test");
    }

    public boolean hasAssignee(String assignee) {
        return fields.getAssignee().getDisplayName().equals(assignee);
    }

    public boolean isAfter(Date date) {
        return fields.getUpdated().compareTo(date) >= 0;
    }

    public void setFields(TaskFields fields) {
        this.fields = fields;
    }

    public boolean hasSprint() {
        return fields.getSprint() != null;
    }
}
