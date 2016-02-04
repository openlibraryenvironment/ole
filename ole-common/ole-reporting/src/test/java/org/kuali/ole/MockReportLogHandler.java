package org.kuali.ole;

import org.apache.camel.Processor;

/**
 * Created by pvsubrah on 1/27/16.
 */
public class MockReportLogHandler {
    private static MockReportLogHandler mockReportLogHandler;

    protected MockReportLogHandler() {

    }

    private static void injectProcessor(Processor mockProcessor) {
        ReportLogHandler.getInstance().addProcessor(mockProcessor);
    }

    public static MockReportLogHandler getInstance() {
        if (null == mockReportLogHandler) {
            mockReportLogHandler = new MockReportLogHandler();
            injectProcessor(new MockProcessor());
        }

        return mockReportLogHandler;
    }

    public void setFileNameAndPath(String filePath, String fileName) {
        ReportLogHandler.getInstance().setFileNameAndPath(filePath, fileName);
    }

    public void logMessage(String message) throws Exception {
        ReportLogHandler.getInstance().logMessage(message);
    }

}