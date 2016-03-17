package org.kuali.ole.scheduler;

import org.javers.common.collections.Collections;
import org.springframework.batch.core.Job;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import java.util.LinkedList;

/**
 * Created by ritter on 16.03.2016.
 */
public class SchedulerTest {

    public static void main(String[] args) {
        try {
            String [] restAnswer = {
                "[" +
                        "{'processId':2,'batchProcessName':'exampleJob','cronExpression':'0/3 * * * * ?'}," +
                        "{'processId':3,'batchProcessName':'exampleJob','cronExpression':'0/3 * * * * ?'}," +
                        "{'processId':4,'batchProcessName':'anotherExampleJob','cronExpression':'0/3 * * * * ?'}" +
                "]"
            };
            OleBatchJobScheduler oleBatchJobScheduler = new OleBatchJobScheduler();
            oleBatchJobScheduler.setRestResult(restAnswer);
            oleBatchJobScheduler.init();
            Thread.sleep(10000);
            oleBatchJobScheduler.modifySchedule(3, null, null);
            Thread.sleep(10000);
            oleBatchJobScheduler.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
