package com.jira.reporter.value;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

/**
 * @author Oleg Zaidullin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEntry {
    private NamedProperty author;
    private Date created;
    private List<LogField> items;

    public NamedProperty getAuthor() {
        return author;
    }

    public void setAuthor(NamedProperty author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<LogField> getItems() {
        return items;
    }

    public void setItems(List<LogField> items) {
        this.items = items;
    }

    public boolean isAuthor(String author) {
        return this.author.getDisplayName().equals(author);
    }

    public boolean after(Date date) {
        return created.after(date);
    }
}
