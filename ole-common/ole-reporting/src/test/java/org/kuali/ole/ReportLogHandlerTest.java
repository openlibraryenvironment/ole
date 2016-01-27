package org.kuali.ole;

import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by angelind on 1/25/16.
 */
public class ReportLogHandlerTest {


    @Test
    public void exceptionWhenFilePathNotProvided() {
        try {
            ReportLogHandler.getInstance().logMessage("Should failed!");
            fail();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void generateReportLogTest() {

        String message = "Hello World";
        String filePath = System.getProperty("java.io.tmpdir");
        String fileName = "test-report.txt";
        try {
            ReportLogHandler.getInstance().setFileNameAndPath(filePath, fileName);
            ReportLogHandler.getInstance().logMessage(message);
            Thread.sleep(5000);

            String fileContent = FileUtils.readFileToString(new File(filePath + "/" + fileName));
            assertNotNull(fileContent);
            assertTrue(StringUtils.isNotBlank(fileContent));

            System.out.println(fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
