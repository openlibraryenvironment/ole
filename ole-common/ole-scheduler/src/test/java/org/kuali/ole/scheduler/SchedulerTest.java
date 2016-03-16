package org.kuali.ole.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ritter on 16.03.2016.
 */
public class SchedulerTest {

    public static void main(String[] args) {
        String[] springConfig = {"org/kuali/ole/scheduler/jobs/*.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
        OleBatchJobScheduler oleBatchJobScheduler = new OleBatchJobScheduler();
        oleBatchJobScheduler.startScheduler();
        oleBatchJobScheduler.addJob("exampleJob", "0/10 * * * * ?", "my first test job");
        oleBatchJobScheduler.addJob("anotherExampleJob", "0/10 * * * * ?", "my second test job");
        // cannot schedule job with the same name twice
        oleBatchJobScheduler.addJob("anotherExampleJob", "0/10 * * * * ?", "my second test job");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        oleBatchJobScheduler.deleteJob("my first test job");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        oleBatchJobScheduler.stopScheduler();
    }
}
