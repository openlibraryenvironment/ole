package org.kuali.ole.scheduler;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public class BatchJobLauncher {

    private String profileId;
    private String batchType;
    private String url;

    public BatchJobLauncher(String profileName, String batchType, String url) {
        this.profileId = profileName;
        this.batchType = batchType;
        this.url = url;
    }

    public BatchJobLauncher() {
    }

    public void startSpringBatchJob(Job job, JobLauncher jobLauncher) {
        try {
            JobParameters jobParameters = new JobParametersBuilder().addDate("run date", new Date()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, jobParameters);
            System.out.println("Exit Status: " + execution.getStatus());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    public void startJobByRestCall() {
        try {
            System.out.println("--- start batch job ---");
            FileBody fileBody = new FileBody(new File(getClass().getResource("sample-marc").toURI()),
                    ContentType.APPLICATION_OCTET_STREAM, "sample-marc");

            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .setMode(HttpMultipartMode.STRICT)
                    .addPart("file", fileBody)
                    .addTextBody("profileName", profileId, ContentType.TEXT_PLAIN)
                    .addTextBody("batchType", batchType, ContentType.TEXT_PLAIN)
                    .build();
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ole-quickstart", ""));
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            HttpResponse response = httpClient.execute(request);
            InputStream body = response.getEntity().getContent();
            String inputStreamString = new
                    Scanner(body, "UTF-8").useDelimiter("\\A").next();
            System.out.println(inputStreamString);
            System.out.println(response.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}