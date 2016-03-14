package org.kuali.ole.scheduler;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.concurrent.ScheduledExecutorService;

public class SpringCronJob extends HttpServlet implements Runnable {

    private ThreadPoolTaskScheduler scheduler;

    public void init() throws ServletException {
        // invoke in separate thead to complete the deployment in parallel
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    public void start() {
        System.err.println("SpringCronJob.start()");
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("SpringCronJob");
        scheduler.initialize();

        scheduler.schedule(this, new CronTrigger("0/3 * * * * ?"));
    }

    public void run() {
        System.err.println("SpringCronJob.run()");
    }

    public static void main(String[] args) {
        SpringCronJob job = new SpringCronJob();
        job.start();
    }
}