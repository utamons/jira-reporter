package com.jira.reporter.mail;

import com.jira.reporter.value.TaskValue;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleg Zaidullin
 */
class VelocityMailComposer {

    private final Template template;

     VelocityMailComposer() {

        VelocityContext context = new VelocityContext();

        context.put("name", "Velocity");

        template = Velocity.getTemplate("mail.html");
    }

     MailMessageValue getMessage(List<TaskValue> tasks, String comment, int num, List<String> emails) {
        Map<String, Object> context = new HashMap<>();
        context.put("tasks",tasks);
        if (comment != null && comment.trim().length() > 0)
            context.put("comment",comment);

        return compose("Report "+num, context, emails);
    }


    private MailMessageValue compose(String subject, Map<String, Object> fields,
                                     List<String> emails) {
        MailMessageValue msg     = new MailMessageValue();
        VelocityContext  context = new VelocityContext(fields);

        msg.setFrom("Oleg Zaidullin <cornknight@gmail.com>");
        String [] to = new String[emails.size()];
        to = emails.toArray(to);

        msg.setTo(to);
        msg.setSubject(subject);

        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        msg.setBodyHtml(sw.toString());

        return msg;
    }
}
