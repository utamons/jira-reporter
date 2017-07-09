package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogField {
    private String field;
    private String toString;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getToString() {
        return toString;
    }

    public void setToString(String toString) {
        this.toString = toString;
    }

    public boolean isDone() {
        return toString.equals("Ready for Test") || toString.equals("Done");
    }
}
