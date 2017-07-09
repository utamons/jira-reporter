package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogResult {
    private List<LogEntry> values;

    public List<LogEntry> getValues() {
        return values;
    }

    public void setValues(List<LogEntry> values) {
        this.values = values;
    }
}
