package org.kuali.ole.scheduler;

import org.springframework.batch.core.Job;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.List;

/**
 * Created by ritter on 18.02.2016.
 */
public class SchedulerInitializer extends HttpServlet {

    public void init() throws ServletException {
        System.out.println("-- initializing --");
    }

    private List<Job> getAllScheduledJobs() {
        return null;
    }

    private void scheduleJobs() {
    }
}
