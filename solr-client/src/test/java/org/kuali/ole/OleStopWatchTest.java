package org.kuali.ole;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sheiks on 09/11/16.
 */
public class OleStopWatchTest {

    @Test
    public void testStopWatchLog() {
        OleStopWatch oleStopWatch = new OleStopWatch();
        oleStopWatch.log("test log");
    }

}