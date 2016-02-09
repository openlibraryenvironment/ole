package org.kuali.ole.oleng.batch.reports;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.HoldingsResponse;
import org.kuali.ole.docstore.common.response.ItemResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.spring.batch.processor.BatchReportLogHandler;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by angelind on 1/27/16.
 */
public class BatchReportLogHandlerTest extends OLETestCaseBase {

    @Test
    public void injectIntermediateBatchPoint() throws Exception {
        OleNGBibImportResponse oleNGBibImportResponse = buildOleNGBibImportResponse();
        BatchReportLogHandler batchReportLogHandler = BatchReportLogHandler.getInstance();
        batchReportLogHandler.logMessage(oleNGBibImportResponse);
        Thread.sleep(5000);
        String fileContent = FileUtils.readFileToString(new File(ConfigContext.getCurrentContextConfig().getProperty("project.home") + "/reports/batch-report"+"-${date:now:yyyyMMddHHmmssSSS}"+".txt"));
        assertNotNull(fileContent);
        assertTrue(StringUtils.isNotBlank(fileContent));
        System.out.println(fileContent);
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

}
