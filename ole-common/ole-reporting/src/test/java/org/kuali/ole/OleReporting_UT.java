package org.kuali.ole;

import junit.framework.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

/**
 * Created by angelind on 1/25/16.
 */
public class OleReporting_UT {

    @Test
    public void generateReportLogTest() {

        String message = "Welcome OleReporting. These messages will be reported and logged in the text file.";
        String filePath = "/kuali/main/local/reports";
        String fileName = "test-report.txt";
        boolean isReportLogged = false;
        try {
            ReportLogHandler.getInstance().setFileNameAndPath(filePath, fileName);
            ReportLogHandler.getInstance().logMessage(message);
            Thread.sleep(5000);
            File file = new File(System.getProperty("user.home") + filePath + "/" + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            StringBuffer stringBuffer = new StringBuffer();
            while((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            if(StringUtils.isNotBlank(stringBuffer.toString())) {
                isReportLogged = true;
            }
            System.out.println(stringBuffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(isReportLogged);
    }
}
