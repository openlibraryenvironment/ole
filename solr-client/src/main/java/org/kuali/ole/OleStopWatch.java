package org.kuali.ole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * Created by sheiks on 09/11/16.
 */
public class OleStopWatch {
    Logger logger = LoggerFactory.getLogger(OleStopWatch.class);

    private StopWatch stopWatch;

    public OleStopWatch() {
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    public void log(String message){
        this.stopWatch.stop();
        logger.info(message + "  : " + this.stopWatch.getTotalTimeSeconds() + " Secs");

    }
}
