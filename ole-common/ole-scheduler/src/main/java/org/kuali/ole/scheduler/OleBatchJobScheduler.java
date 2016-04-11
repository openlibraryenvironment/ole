package org.kuali.ole.scheduler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.batchxml.BatchProcessProfileLoader;
import org.kuali.ole.batchxml.Cron;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.utility.OleHttpRestGet;
import org.kuali.ole.utility.OleHttpRestGetImpl;
import org.kuali.ole.utility.OleHttpRestGetStringImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OleBatchJobScheduler extends HttpServlet {
    private ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml" };
    private ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
    private JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
    private OleHttpRestGet oleHttpRestGet = new OleHttpRestGetImpl();
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
                    //scheduleAllJobs();
                    BatchProcessProfileLoader loader = new BatchProcessProfileLoader();
                    loader.load();
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

        throw new RuntimeException("REST api not available: " + OleNGConstants.REST_BATCH_PROCESS_JOBS);
    }

    /**
     * Invoke REST API for jobs and their schedules and do the scheduling.
     *
     * @return true if REST API is available and scheduling all jobs was successful, false otherwise
     * @throws IOException  when REST call fails
     * @throws JSONException    when REST return invalid JSON
     */
    private boolean scheduleAllJobsOneTry() throws IOException, JSONException {
        String processString = oleHttpRestGet.rest(OleNGConstants.REST_BATCH_PROCESS_JOBS);

        // null if REST API is not yet available (HTTP 404)
        if (processString == null) {
            return false;
        }

        System.err.println("REST process string: ");
        System.err.println(processString);

        // The list of schedules, can be an empty list.
        JSONArray processes = new JSONArray(processString);
        for (int i = 0; i < processes.length(); i++) {
            JSONObject process = processes.getJSONObject(i);
            try {
                /** @see org.kuali.ole.oleng.handler.BatchProfileRequestHandlerUtil#prepareBatchProcessJobs() */

                int id      = process.getInt(   OleNGConstants.JOB_ID);
                String name = process.optString(OleNGConstants.JOB_NAME);
                String cron = process.optString(OleNGConstants.CRON_EXPRESSION);

                modifySchedule(id, name, cron);
            } catch (Exception e) {
                System.err.println("failing Process: " + process.toString());
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Set the answers of the REST call.  Used for regression testing.
     * @param restResult    the list of result strings, may contain null
     */
    void setRestResult(String ... restResult) {
        oleHttpRestGet = new OleHttpRestGetStringImpl(restResult);
    }

    /**
     * Add, alter or delete a schedule.  Use cron for the scheduling, where
     * null means to delete a schedule.
     * @param id    id of the schedule
     * @param scheduleName  name of the job schedule
     * @param cron  cron expression, or null
     */
    void modifySchedule(int id, String scheduleName, String cron) {
        System.err.println("modifySchedule("+id+", "+scheduleName+", "+cron+")");

        ScheduledFuture scheduledFuture = scheduledFutures.get(id);

        // stop current schedule
        if (scheduledFuture != null) {
            if (!scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(false);
            }
        }

        if (scheduleName == null) {
            return;
        }

        Cron cronBean = context.getBean(scheduleName, Cron.class);
        String jobName = cronBean.getJobId();
        Job springBatchJob = (Job) context.getBean(jobName);

        cron = cronBean.getCronPattern();

        if (cron == null) {
            return;
        }

        // set new schedule
        OleBatchJob oleBatchJob = new OleBatchJob(springBatchJob, cron, jobName, jobLauncher);
        scheduledFuture = scheduler.schedule(oleBatchJob, new CronTrigger(oleBatchJob.getCronExpression()));

        // store for cancelling
        scheduledFutures.put(id, scheduledFuture);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        String jobName = request.getParameter("jobName");
        String cron = request.getParameter("cron");
        int idAsInt = Integer.parseInt(id);
        modifySchedule(idAsInt, jobName, cron);
        PrintWriter out = response.getWriter();
        out.println(id + "," + jobName + "," + cron);
    }

}