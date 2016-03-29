package org.kuali.ole.oleng.batch.reports;

import org.junit.Test;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.HoldingsResponse;
import org.kuali.ole.docstore.common.response.ItemResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.oleng.batch.reports.processors.*;
import org.kuali.ole.utility.MarcRecordUtil;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SheikS on 2/18/2016.
 */
public class BibImportReportLogHandlerTest {

    @Test
    public void testLogMessage() throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = buildOleNGBibImportResponse();
        oleNGBibImportResponse.setDirectoryName("1");
        oleNGBibImportResponse.setBibImportProfileName("profile1");
        MarcRecordUtil marcRecordUtil = new MarcRecordUtil();
        Record record = marcRecordUtil.getMarcFactory().newRecord();
        oleNGBibImportResponse.setMatchedRecords(Collections.singletonList(record));
        oleNGBibImportResponse.setUnmatchedRecords(Collections.singletonList(record));
        oleNGBibImportResponse.setMultipleMatchedRecords(Collections.singletonList(record));

        List<OleNGReportProcessor> processors = new ArrayList<>();
        processors.add(new MockMatchedRecordsReportProcessor());
        processors.add(new MockMultipleMatchedRecordsReportProcessor());
        processors.add(new MockUnMatchedRecordsReportProcessor());
        processors.add(new MockSummaryReportProcessor());

        BibImportReportLogHandler bibImportReportLogHandler1 = BibImportReportLogHandler.getInstance();
        bibImportReportLogHandler1.setProcessors(processors);
        bibImportReportLogHandler1.logMessage(oleNGBibImportResponse,oleNGBibImportResponse.getDirectoryName());


        oleNGBibImportResponse.setDirectoryName("2");
        oleNGBibImportResponse.setBibImportProfileName("profile2");
        BibImportReportLogHandler bibImportReportLogHandler2 = BibImportReportLogHandler.getInstance();
        bibImportReportLogHandler2.setProcessors(processors);
        bibImportReportLogHandler2.logMessage(oleNGBibImportResponse,oleNGBibImportResponse.getDirectoryName());


        oleNGBibImportResponse.setDirectoryName("3");
        oleNGBibImportResponse.setBibImportProfileName("profile3");
        BibImportReportLogHandler bibImportReportLogHandler3 = BibImportReportLogHandler.getInstance();
        bibImportReportLogHandler3.setProcessors(processors);
        bibImportReportLogHandler3.logMessage(oleNGBibImportResponse,oleNGBibImportResponse.getDirectoryName());
        Thread.sleep(5000);
    }

    private OleNGBibImportResponse buildOleNGBibImportResponse() {
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        List<ItemResponse> itemResponses = new ArrayList<>();
        List<HoldingsResponse> holdingsResponses = new ArrayList<>();
        List<BibResponse> bibResponses = new ArrayList<>();
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setItemId("wio-300001");
        itemResponse.setOperation("Created");
        itemResponses.add(itemResponse);
        HoldingsResponse holdingsResponse = new HoldingsResponse();
        holdingsResponse.setHoldingsId("who-200001");
        holdingsResponse.setOperation("Created");
        holdingsResponse.setItemResponses(itemResponses);
        holdingsResponses.add(holdingsResponse);
        BibResponse bibResponse = new BibResponse();
        bibResponse.setBibId("wbm-100001");
        bibResponse.setOperation("Created");
        bibResponse.setHoldingsResponses(holdingsResponses);
        bibResponses.add(bibResponse);
        oleNGBibImportResponse.setBibResponses(bibResponses);
        return oleNGBibImportResponse;
    }


    public class MockSummaryReportProcessor extends SummaryReportProcessor{
        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + "reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

    public class MockMultipleMatchedRecordsReportProcessor extends MultipleMatchedRecordsReportProcessor {
        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + "reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

    public class MockMatchedRecordsReportProcessor extends MatchedRecordsReportProcessor {
        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + "reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }

    public class MockUnMatchedRecordsReportProcessor extends UnMatchedRecordsReportProcessor {
        @Override
        public String getReportDirectoryPath() {
            String tempLocation = System.getProperty("java.io.tmpdir") + "reports";
            System.out.println("Temp dir : " + tempLocation);
            return tempLocation;
        }
    }
}