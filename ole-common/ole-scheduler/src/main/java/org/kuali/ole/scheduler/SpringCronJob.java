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
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpringCronJob extends HttpServlet {

    private ThreadPoolTaskScheduler scheduler;
    String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml"};
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    private String urlBase;

    public void init() throws ServletException {
        // invoke in separate thead to complete the deployment in parallel
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
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

    public void start() throws IOException, JSONException, InterruptedException {
        System.err.println("SpringCronJob.start()");
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("SpringCronJob");
        scheduler.initialize();

        System.err.println("-- initializing --");
        List<OleBatchJob> allJobs = null;
        for (int n = 1; n < 50; n++) {
            allJobs = getAllScheduledJobs();
            if (allJobs != null) {
                break;
            }
            System.err.println("rest not yet available, not fully deployed?");
            TimeUnit.SECONDS.sleep(n);
        }

        if (allJobs == null) {
            throw new RuntimeException("REST api not available: " + OleNGConstants.BATCH_PROCESS_JOBS);
        }

        scheduleJobs(allJobs);
    }

    private void scheduleJobs(List<OleBatchJob> allJobs) {
        for (OleBatchJob oleBatchJob : allJobs) {
            scheduler.schedule(oleBatchJob, new CronTrigger(oleBatchJob.getCronExpression()));
        }
    }

    List<OleBatchJob> getAllScheduledJobs() throws IOException, JSONException {
        String schedulesString = rest(OleNGConstants.BATCH_PROCESS_JOBS);
        if (schedulesString == null) {
            return null;
        }
        JSONArray schedules = new JSONArray(schedulesString);
        List<OleBatchJob> jobs = new ArrayList<>(schedules.length());
        for (int i = 0; i < schedules.length(); i++) {
            try {
                JSONObject schedule = schedules.getJSONObject(i);
                String name = schedule.optString(OleNGConstants.PROCESS_NAME);
                String cron = schedule.optString(OleNGConstants.CRON_EXPRESSION);
                Job springBatchJob = (Job) context.getBean(name);
                OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cron, name, jobLauncher);
                jobs.add(oleBatchJob);
                System.err.println("added oleBatchJob: " + name + " " + cron + " " + schedule.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jobs;
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
}