package org.kuali.ole.springtaskscheduler;

/**
 * Created by ritter on 11.03.2016.
 */
public class SampleTask implements Runnable {

    @Override
    public void run() {
        System.out.println("I am your task");
    }
}
