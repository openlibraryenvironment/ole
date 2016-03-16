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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OleBatchJobScheduler extends HttpServlet {

    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml"};
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    private String urlBase;
    private HashMap<String, ScheduledFuture> scheduledFutures = new HashMap<>();

    public void init() throws ServletException {
        // invoke in separate thread to complete the deployment in parallel
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startAllJobs();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startAllJobs() throws IOException, JSONException, InterruptedException {
        scheduler.setThreadNamePrefix("OleBatchJobScheduler");
        startScheduler();
        System.err.println("-- initializing batch jobs --");
        for (int n = 1; n < 50; n++) {
            if (scheduleAllPersitedJobs() == true) {
                break;
            }
            System.err.println("rest not yet available, not fully deployed?");
            TimeUnit.SECONDS.sleep(n);
        }
        if (scheduleAllPersitedJobs() == false) {
            throw new RuntimeException("REST api not available: " + OleNGConstants.BATCH_PROCESS_JOBS);
        }
    }

    private boolean scheduleAllPersitedJobs() throws IOException, JSONException {
        String schedulesString = rest(OleNGConstants.BATCH_PROCESS_JOBS);

        // ToDo
        // i think we should have some more elaborated handling here
        // could be the case that there are no persited jobs but rest is available, right?
        if (schedulesString == null) {
            return false;
        }
        JSONArray schedules = new JSONArray(schedulesString);
        List<OleBatchJob> jobs = new ArrayList<>(schedules.length());
        for (int i = 0; i < schedules.length(); i++) {
            try {
                JSONObject schedule = schedules.getJSONObject(i);
                String springJobName = schedule.optString(OleNGConstants.PROCESS_NAME);
                String cron = schedule.optString(OleNGConstants.CRON_EXPRESSION);

                // ToDo
                // Last argument "name" should be some kind of id!!!
                // This has to be in the db somewhere
                addJob(springJobName, cron, springJobName);

                System.err.println("added oleBatchJob: " + springJobName + " " + cron + " " + schedule.toString());
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
     * Do a http Get at some rest URL.
     *
     * @param restPath the path starting with "rest/" of the URL where to do the GET
     * @return response to the Get request, null if restPath not found
     */
    private String rest(String restPath) throws IOException {
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

    public void addJob(String springJobName, String cronExpression, String name) {
        if (!scheduledFutures.containsKey(name)) {
            Job springBatchJob = (Job) context.getBean(springJobName);
            OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cronExpression, name, jobLauncher);
            ScheduledFuture scheduledFuture = scheduler.schedule(oleBatchJob, new CronTrigger(oleBatchJob.getCronExpression()));
            scheduledFutures.put(name, scheduledFuture);
        } else {
            // we have to handle this as some kind of exception
            System.err.println("there is already a job with this name!");
        }
    }

    public void deleteJob(String name) {
        if (scheduledFutures.containsKey(name)) {
            scheduledFutures.get(name).cancel(true);
            System.err.println("cancelled job with name: " + name);
            scheduledFutures.remove(name);
        } else {
            System.err.println("no job found with this name!");
        }
    }

    public void startScheduler() {
        scheduler.initialize();
    }

    public void stopScheduler() {
        scheduler.shutdown();
    }
}