package com.jira.reporter.mail;

import com.jira.reporter.value.TaskValue;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * @author Oleg Zaidullin
 */
public interface MailSender {
    void sendMessage(List<TaskValue> tasks, String comment, int num, List<String> emails) throws IOException, MessagingException;
}
