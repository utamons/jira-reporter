package com.jira.reporter.mail;

/**
 * @author Oleg Zaidullin
 */
public class MailMessageValue {

    private String bodyHtml;

    private String from;

    private String[] to;

    private String subject;

    public MailMessageValue() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

}
