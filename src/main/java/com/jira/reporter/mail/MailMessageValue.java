package com.jira.reporter.mail;

/**
 * @author Oleg Zaidullin
 */
class MailMessageValue {

    private String bodyHtml;

    private String from;

    private String[] to;

    private String subject;

    MailMessageValue() {
    }

    String getFrom() {
        return from;
    }

    @SuppressWarnings("SameParameterValue")
    void setFrom(String from) {
        this.from = from;
    }

    String[] getTo() {
        return to;
    }

    void setTo(String[] to) {
        this.to = to;
    }

    String getSubject() {
        return subject;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    String getBodyHtml() {
        return bodyHtml;
    }

    void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

}
