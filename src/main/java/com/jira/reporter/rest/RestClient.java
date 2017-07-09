package com.jira.reporter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Zaidullin
 */
class RestClient {
    private CloseableHttpClient client;

    private final static int TIMEOUT = 120000;
    private final String username;
    private final String password;

    RestClient(String username, String password) {
        this.username = username;
        this.password = password;

        final RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectionRequestTimeout(TIMEOUT)
                .setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();

        client = HttpClients.custom()
                            .setDefaultRequestConfig(requestConfig)
                            .build();
    }

    private void authenticate(HttpRequestBase requestBase) {
        requestBase.addHeader(new BasicHeader("Authorization",
                                              "Basic " + Base64.encodeBase64String((username + ":" + password).getBytes())));
        requestBase.addHeader(new BasicHeader("Content-Type", "application/json"));
    }


    @SuppressWarnings("SameParameterValue")
    <T> T get(Class<T> type, String url, boolean test) throws IOException {
        final HttpGet get = new HttpGet(url);
        authenticate(get);
        T result = null;

        try (CloseableHttpResponse response = client.execute(get)) {
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new IOException("Error, response code was " + statusCode + ", reason " + response.getStatusLine()
                                                                                                       .getReasonPhrase());
            }

            try (InputStream stream = response.getEntity().getContent()) {

                if (test) {

                    final List<String> strings = new ArrayList<>();

                    try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            strings.add(line);
                        }
                    }

                    try (PrintWriter out = new PrintWriter("tasks.json")) {
                        for (String s : strings) {
                            out.println(s);
                        }
                    }
                }
                else {
                    final ObjectMapper mapper = new ObjectMapper();
                    result = mapper.readValue(stream, type);
                }

            }
        }

        return result;
    }

}
