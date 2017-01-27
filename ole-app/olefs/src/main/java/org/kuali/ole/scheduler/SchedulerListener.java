package org.kuali.ole.scheduler;

import org.apache.log4j.Logger;
import org.kuali.ole.oleng.scheduler.OleNGBatchJobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by sheiks on 27/01/17.
 */
@Component
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOG = Logger.getLogger(SchedulerListener.class);

    @Autowired
    private OleNGBatchJobScheduler oleNGBatchJobScheduler;

    public void onApplicationEvent(final ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        LOG.info("SchedulerListener is started to schedule the jobs.");
        oleNGBatchJobScheduler.initializeAllJobs();
        LOG.info("SchedulerListener is ended");
    }

}
