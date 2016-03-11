package org.kuali.ole.springtaskscheduler;

import org.springframework.scheduling.commonj.TimerManagerTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

/**
 * Created by ritter on 11.03.2016.
 */
public class TestTaskScheduler {

    TimerManagerTaskScheduler timerManagerTaskScheduler = new TimerManagerTaskScheduler();

    public static void main(String[] args) {
        TestTaskScheduler testTaskScheduler = new TestTaskScheduler();
        testTaskScheduler.scheduleSomething();
    }

    public void scheduleSomething() {
        SampleTask sampleTask = new SampleTask();
        timerManagerTaskScheduler.schedule(sampleTask, new CronTrigger("0/15 * * * * ?"));
        timerManagerTaskScheduler.start();
    }

}
