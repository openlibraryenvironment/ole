package org.kuali.ole.scheduler;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.ole.constants.OleNGConstants;
import org.quartz.SchedulerException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 */
public class SchedulerInitializer extends HttpServlet {

    String[] springConfig = {"spring/batch/jobs/*.xml"};
    BatchJobScheduler batchJobScheduler;
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    private static final Logger LOG = Logger.getLogger(SchedulerInitializer.class);
    private String urlBase;

    public void init() throws ServletException {
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        System.out.println("-- initializing --");
        List<OleBatchJob> allJobs = getAllScheduledJobs();
        for (OleBatchJob oleBatchJob : allJobs) {
            try {
                getBatchJobScheduler().scheduleSpringBatchJob(oleBatchJob.getJob(), jobLauncher,
                        oleBatchJob.getCronExpression(), oleBatchJob.getName(), "myTestTrigger");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BatchJobScheduler getBatchJobScheduler() throws SchedulerException {
        if (batchJobScheduler == null) {
            return new BatchJobScheduler();
        }
        return batchJobScheduler;
    }

    /**
     * Set the urlBase, this can be used for regression testing when the ConfigContext
     * hasn't been initialized.
     *
     * @param urlBase URL excluding the trailing slash
     */
    void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }

    private String getUrlBase() {
        if (urlBase != null) {
            return urlBase;
        }
        urlBase = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
        return urlBase;
    }

    List<OleBatchJob> getAllScheduledJobs() {
        try {
            String schedulesString = rest(OleNGConstants.BATCH_PROCESS_JOBS);
            JSONArray schedules = new JSONArray(schedulesString);
            List<OleBatchJob> jobs = new ArrayList<>(schedules.length());
            for (int i = 0; i < schedules.length(); i++) {
                JSONObject schedule = schedules.getJSONObject(i);
                String name = schedule.optString(OleNGConstants.PROCESS_NAME);
                String cron = schedule.optString(OleNGConstants.CRON_EXPRESSION);
                Job springBatchJob = (Job) context.getBean(name);
                OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cron, name);
                jobs.add(oleBatchJob);
                System.err.println("***" + name + " " + cron + " " + schedule.toString());
            }
            return jobs;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Do a http Get at some rest URL.
     *
     * @param restPath the path starting with "rest/" of the URL where to do the GET
     * @return response to the Get request
     */
    private String rest(String restPath) {
        try {
            String url = getUrlBase() + "/" + restPath;
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ole-quickstart", ""));
            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            HttpResponse response = httpClient.execute(new HttpGet(url));
            System.err.println("URL: " + url);
            System.err.println("response: " + response.toString());
            System.err.println("response: " + response.getEntity().toString());
            InputStream body = response.getEntity().getContent();
            Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
            if (!scanner.hasNext()) {
                return "";
            }
            return scanner.next();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
