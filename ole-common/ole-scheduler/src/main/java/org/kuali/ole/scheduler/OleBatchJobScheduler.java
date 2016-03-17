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
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OleBatchJobScheduler extends HttpServlet {

    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml"};
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    private String urlBase;
    private List<String> restResult;
    /**
     * Maps the scheduleId to the schedule's future
     */
    private HashMap<Integer, ScheduledFuture> scheduledFutures = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // invoke in separate thread to complete the deployment in parallel
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    scheduler.setThreadNamePrefix("OleBatchJobScheduler");
                    scheduler.initialize();
                    scheduleAllJobs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void destroy() {
        scheduler.shutdown();
    }

    /**
     * Try 20 Minutes to get the list of jobs and their schedules from the
     * REST API.  Schedule the jobs, or throw an exception.
     *
     * @throws IOException      when REST call fails
     * @throws JSONException    when REST return invalid JSON
     * @throws InterruptedException when waiting is interrupted
     * @throws RuntimeException     if REST API is not available for 20 Minutes
     */
    private void scheduleAllJobs() throws IOException, JSONException, InterruptedException {
        System.err.println("-- initializing batch jobs --");
        for (int n = 1; n < 50; n++) {
            if (scheduleAllJobsOneTry()) {
                return;
            }
            System.err.println("rest not yet available, not fully deployed?");
            // increasing sleep time
            TimeUnit.SECONDS.sleep(n);
        }

        throw new RuntimeException("REST api not available: " + OleNGConstants.BATCH_PROCESS_JOBS);
    }

    /**
     * Invoke REST API for jobs and their schedules and do the scheduling.
     *
     * @return true if REST API is available and scheduling all jobs was successful, false otherwise
     * @throws IOException  when REST call fails
     * @throws JSONException    when REST return invalid JSON
     */
    private boolean scheduleAllJobsOneTry() throws IOException, JSONException {
        String schedulesString = rest(OleNGConstants.BATCH_PROCESS_JOBS);

        // null if REST API is not yet available (HTTP 404)
        if (schedulesString == null) {
            return false;
        }

        System.err.println("REST schedules string =");
        System.err.println(schedulesString);

        // The list of schedules, can be an empty list.
        JSONArray schedules = new JSONArray(schedulesString);
        List<OleBatchJob> jobs = new ArrayList<>(schedules.length());
        for (int i = 0; i < schedules.length(); i++) {
            try {
                JSONObject schedule = schedules.getJSONObject(i);
                int scheduleId = schedule.getInt(   OleNGConstants.PROCESS_ID);
                String name    = schedule.optString(OleNGConstants.PROCESS_NAME);
                String cron    = schedule.optString(OleNGConstants.CRON_EXPRESSION);

                modifySchedule(scheduleId, name, cron);
                System.err.println("added oleBatchJob: " + scheduleId + " " + name + " " + cron + "\n   " + schedule.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
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

    /**
     * Set the answers of the REST call.  Used for regression testing.
     * @param restResult    the list of result strings, may contain null
     */
    void setRestResult(String [] restResult) {
        if (restResult == null) {
            this.restResult = null;
            return;
        }
        List<String> list = new LinkedList<>();
        for (String s : restResult) {
            list.add(s);
        }
        this.restResult = list;
    }

    /**
     * Do a HTTP Get at some rest URL.
     *
     * @param restPath the path starting with "rest/" of the URL where to do the GET
     * @return response to the Get request, null if restPath not found (HTTP 404)
     */
    private String rest(String restPath) throws IOException {
        // simlate rest answer for regression testing?
        if (restResult != null) {
            if (restResult.isEmpty()) {
                restResult = null;
            } else {
                String result = restResult.get(0);
                restResult.remove(0);
                return result;
            }
        }

        String url = getUrlBase() + "/" + restPath;
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("ole-quickstart", ""));
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        HttpResponse response = httpClient.execute(new HttpGet(url));
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
            return null;
        }
        InputStream body = response.getEntity().getContent();
        Scanner scanner = new Scanner(body, "UTF-8").useDelimiter("\\A");
        if (!scanner.hasNext()) {
            return "";
        }
        return scanner.next();
    }

    /**
     * Add, alter or delete a schedule.  Use cron for the scheduling, where
     * null means to delete a schedule.
     * @param id    id of the schedule
     * @param jobName  name of the job
     * @param cron  cron expression or null
     */
    void modifySchedule(int id, String jobName, String cron) {
        ScheduledFuture scheduledFuture = scheduledFutures.get(id);

        // stop current schedule
        if (scheduledFuture != null) {
            if (!scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(false);
            }
        }

        if (cron == null) {
            return;
        }

        // set new schedule
        Job springBatchJob = (Job) context.getBean(jobName);
        OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cron, jobName, jobLauncher);
        scheduledFuture = scheduler.schedule(oleBatchJob, new CronTrigger(oleBatchJob.getCronExpression()));

        // store for cancelling
        scheduledFutures.put(id, scheduledFuture);
    }
}