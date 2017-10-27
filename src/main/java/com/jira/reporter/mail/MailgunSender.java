package com.jira.reporter.mail;

import com.jira.reporter.value.TaskValue;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class is meant to work with Mailgun service. Not used, but can be used, if needed.
 *
 * @author Oleg Zaidullin
 */
public class MailgunSender implements MailSender {

    private static final int    MAX_CONN_TOTAL = 200;

    private static final String MESSAGES = "messages";

    private final String url;

    private VelocityMailComposer composer;

    private CloseableHttpClient client;

    public MailgunSender(String url, String key) {
        final int TIMEOUT = 120000;

        this.url = url;

        composer = new VelocityMailComposer();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        String host   = "api.mailgun.net";
        credentialsProvider.setCredentials(
                new AuthScope(new HttpHost(host)),
                new UsernamePasswordCredentials("api", key));


        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();

        client = HttpClients.custom()
                            .setMaxConnTotal(MAX_CONN_TOTAL).setDefaultRequestConfig(requestConfig)

                            .setDefaultCredentialsProvider(credentialsProvider)
                            .build();
    }

    private void send(MailMessageValue message) throws IOException {
        if (ArrayUtils.isEmpty(message.getTo()) || message.getTo()[0] == null)
            throw new IOException("Empty \"to\" field in message");

        StringBuilder to = new StringBuilder();
        for (String s: message.getTo()){
            to.append(s).append(",");
        }

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("from", message.getFrom()));
        urlParameters.add(new BasicNameValuePair("to", to.toString()));
        urlParameters.add(new BasicNameValuePair("subject", message.getSubject()));

        if (message.getBodyHtml() != null && !message.getBodyHtml().isEmpty())
            urlParameters.add(new BasicNameValuePair("html", message.getBodyHtml()));

        HttpPost post = new HttpPost(url + MESSAGES);

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters);
        post.setEntity(entity);

        try (CloseableHttpResponse response = client.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new IOException("Mailgun error, response code was " + statusCode);
            }

            EntityUtils.consume(entity);
        }
    }

    public void sendMessage(List<TaskValue> tasks, String comment, int num, List<String> emails) throws IOException {
        send(composer.getMessage(tasks, comment, num, emails));
    }


}
