package org.kuali.ole.scheduler;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Read all job schedules from REST api, start them.
 */
public class SchedulerInitializer extends HttpServlet {
//    String[] springConfig = {"spring/batch/jobs/*.xml"};
//    BatchJobScheduler batchJobScheduler = null;
//    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
//
//    private String urlBase;
//
//    public void init() throws ServletException {
//        // invoke in separate thead to complete the deployment in parallel
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                initScheduler();
//            }
//        }).start();
//    }
//
//    void initScheduler() {
//        try {
//            System.err.println("-- initializing --");
//            List<OleBatchJob> allJobs = null;
//            for (int n=1; n<50; n++) {
//                allJobs = getAllScheduledJobs();
//                if (allJobs != null) {
//                    break;
//                }
//                System.err.println("rest not yet available, not fully deployed?");
//                TimeUnit.SECONDS.sleep(n);
//            }
//            if (allJobs == null) {
//                throw new RuntimeException("REST api not available: " + OleNGConstants.BATCH_PROCESS_JOBS);
//            }
//            scheduleJobs(allJobs);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void scheduleJobs(List<OleBatchJob> allJobs) throws SchedulerException {
//        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
//
//        for (OleBatchJob oleBatchJob : allJobs) {
//            try {
//                getBatchJobScheduler().scheduleSpringBatchJob(oleBatchJob.getJob(), jobLauncher,
//                        oleBatchJob.getCronExpression(), oleBatchJob.getName(), "myTestTrigger");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (batchJobScheduler == null) {
//            return;
//        }
//        batchJobScheduler.startScheduler();
//    }
//
//    private BatchJobScheduler getBatchJobScheduler() throws SchedulerException {
//        if (batchJobScheduler == null) {
//            return new BatchJobScheduler();
//        }
//        return batchJobScheduler;
//    }
//
//    /**
//     * Set the urlBase, this can be used for regression testing when the ConfigContext
//     * hasn't been initialized.
//     *
//     * @param urlBase URL excluding the trailing slash
//     */
//    void setUrlBase(String urlBase) {
//        this.urlBase = urlBase;
//    }
//
//    private String getUrlBase() {
//        if (urlBase != null) {
//            return urlBase;
//        }
//        urlBase = ConfigContext.getCurrentContextConfig().getProperty("ole.fs.url.base");
//        return urlBase;
//    }
//
//    /**
//     * All batch jobs.
//     * @return List of jobs, may be empty, or null if the REST api is not yet available.
//     */
//    List<OleBatchJob> getAllScheduledJobs() throws IOException, JSONException {
//        String schedulesString = rest(OleNGConstants.BATCH_PROCESS_JOBS);
//        if (schedulesString == null) {
//            return null;
//        }
//        JSONArray schedules = new JSONArray(schedulesString);
//        List<OleBatchJob> jobs = new ArrayList<>(schedules.length());
//        for (int i = 0; i < schedules.length(); i++) {
//            try {
//                JSONObject schedule = schedules.getJSONObject(i);
//                String name = schedule.optString(OleNGConstants.PROCESS_NAME);
//                String cron = schedule.optString(OleNGConstants.CRON_EXPRESSION);
//                Job springBatchJob = (Job) context.getBean(name);
//                OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cron, name);
//                jobs.add(oleBatchJob);
//                System.err.println("added oleBatchJob: " + name + " " + cron + " " + schedule.toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return jobs;
//    }
//
//    /**
//     * Do a http Get at some rest URL.
//     *
//     * @param restPath the path starting with "rest/" of the URL where to do the GET
//     * @return response to the Get request, null if restPath not found
//     */
//    private String rest(String restPath) throws IOException {
//        String url = getUrlBase() + "/" + restPath;
//        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ole-quickstart", ""));
//        CloseableHttpClient httpClient = HttpClientBuilder
//                .create()
//                .setDefaultCredentialsProvider(credentialsProvider)
//                .build();
//        HttpResponse response = httpClient.execute(new HttpGet(url));
//        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
//            return null;
//        }
//        InputStream body = response.getEntity().getContent();
//        Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
//        if (!scanner.hasNext()) {
//            return "";
//        }
//        return scanner.next();
//    }
}
