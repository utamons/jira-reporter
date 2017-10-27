package com.jira.reporter.mail;

import com.jira.reporter.value.TaskValue;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author Oleg Zaidullin
 */
public class GmailSender implements MailSender {

    private final String               username;
    private final String               password;
    private final VelocityMailComposer composer;

    public GmailSender(String username, String password) {
        this.composer = new VelocityMailComposer();
        this.username = username;
        this.password = password;
    }

    @Override
    public void sendMessage(List<TaskValue> tasks, String comment, int num, List<String> emails) throws IOException, MessagingException {
        MailMessageValue messageValue = composer.getMessage(tasks, comment, num, emails);
        send(
                messageValue.getFrom(),
                messageValue.getTo(),
                messageValue.getSubject(),
                messageValue.getBodyHtml()
            );
    }

    private void send(String from, String[] to, String subject, String body) throws UnsupportedEncodingException, MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session     session = Session.getDefaultInstance(props, auth);

        MimeMessage msg     = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(body, "text/html; charset=utf-8");
        msg.setSentDate(new Date());

        List<Address> addresses = new ArrayList<>();
        for (String addr : to) {
            addresses.add(new InternetAddress(addr));
        }

        msg.setRecipients(Message.RecipientType.TO, addresses.toArray(new Address[0]));
        Transport.send(msg);
    }
}
