package org.kuali.ole.scheduler;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

public class BatchJobLauncher {

    private String profileName;
    private String batchType;
    private String url;

    public BatchJobLauncher(String profileName, String batchType, String url) {
        this.profileName = profileName;
        this.batchType = batchType;
        this.url = url;
    }

    public void start() {
        System.out.println("--- start batch job ---");
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("profileName", profileName)
                .addTextBody("batchType", batchType)
                .addBinaryBody("file", new File("src/test/resources/sample-marc.xml"))
                .build();
        HttpPost request = new HttpPost(url);
        request.setEntity(entity);
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.getStatusLine());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}