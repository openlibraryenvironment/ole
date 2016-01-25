package org.kuali.ole;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * Created by angelind on 1/25/16.
 */
public class OleReporting_UT {

    @Test
    public void generateReportLogTest() {

        String message = "Welcome OleReporting. These messages will be reported and logged in the text file";
        try {
            ReportLogHandler.getInstance().logMessage(message);
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
