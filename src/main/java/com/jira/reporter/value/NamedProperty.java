package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamedProperty {
    private String name;
    private String displayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
