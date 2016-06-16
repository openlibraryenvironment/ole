package org.kuali.ole.oleng.batch.reports.processors;

import org.junit.Test;
import org.kuali.ole.docstore.common.response.BibFailureResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;

/**
 * Created by angelind on 3/15/16.
 */
public class BibImportFailureReportProcessorTest {

    @Test
    public void testFailureReportProcessor() {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        BibFailureResponse failureResponse = new BibFailureResponse();
        failureResponse.setFailureMessage("NullPointerException");
        failureResponse.setIndex(1);
        failureResponse.setNoOfFailureHoldings(5);
        failureResponse.setNoOfFailureItems(4);
        failureResponse.setNoOfFailureEHoldings(3);
        oleNGBibImportResponse.addFailureResponse(failureResponse);
        OleNGReportProcessor oleNGReportProcessor = new MockBibImportFailureReportProcessor();
        try {
            oleNGReportProcessor.process(oleNGBibImportResponse, "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MockBibImportFailureReportProcessor extends BibImportFailureReportProcessor {

        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + "/reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

}
