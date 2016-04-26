package org.kuali.ole.spring.batch.controller;

import org.junit.Test;
import org.kuali.ole.docstore.common.document.EHoldings;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.HoldingsResponse;
import org.kuali.ole.docstore.common.response.ItemResponse;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.oleng.util.BatchExcelReportUtil;
import org.kuali.ole.spring.batch.rest.controller.BatchRestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 3/17/2016.
 */
public class BatchRestControllerTest {

    @Test
    public void testExcelForBib() throws IOException {
        String filePath = FileUtils.getFilePath("org/kuali/ole/BibImport.txt");
        String content = org.apache.commons.io.FileUtils.readFileToString(new File(filePath));
        byte[] byteArray = new BatchExcelReportUtil().getExcelSheetForBibImport(content);
        assertNotNull(byteArray);
        String tempLocation = System.getProperty("java.io.tmpdir");
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(tempLocation + File.separator + "BibImport-Excel.xlsx"), byteArray);
    }

    @Test
    public void testExcelForOrder() throws IOException {
        String filePath = FileUtils.getFilePath("org/kuali/ole/OrderImport.txt");
        String content = org.apache.commons.io.FileUtils.readFileToString(new File(filePath));
        byte[] byteArray = new BatchExcelReportUtil().getExcelSheetForOrderImport(content);
        assertNotNull(byteArray);
        String tempLocation = System.getProperty("java.io.tmpdir");
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(tempLocation + File.separator + "OrderImport-Excel.xlsx"), byteArray);
    }

    @Test
    public void testExcelForInvoice() throws IOException {
        String filePath = FileUtils.getFilePath("org/kuali/ole/InvoiceImport.txt");
        String content = org.apache.commons.io.FileUtils.readFileToString(new File(filePath));
        byte[] byteArray = new BatchExcelReportUtil().getExcelSheetForInvoiceImport(content);
        assertNotNull(byteArray);
        String tempLocation = System.getProperty("java.io.tmpdir");
        org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(tempLocation + File.separator + "InvoiceImport-Excel.xlsx"), byteArray);
    }


    @Test
    public void testBibResponse() {

        List<BibResponse> bibResponses = new ArrayList<>();
        BibResponse bibResponse = new BibResponse();
        bibResponse.setRecordIndex(2);
        bibResponse.setOperation("Created");
        bibResponse.setBibId("wbm-101");

        List<HoldingsResponse> holdingsResponses = new ArrayList<>();
        HoldingsResponse holdingsResponse = new HoldingsResponse();
        holdingsResponse.setHoldingsId("who-1");
        holdingsResponse.setOperation("Created");
        holdingsResponse.setHoldingsType(PHoldings.PRINT);
        holdingsResponses.add(holdingsResponse);

        bibResponse.setHoldingsResponses(holdingsResponses);
        bibResponses.add(bibResponse);

        BibResponse bibResponse1 = new BibResponse();
        bibResponse1.setRecordIndex(2);
        bibResponse1.setOperation("Created");
        bibResponse1.setBibId("wbm-101");

        List<HoldingsResponse> holdingsResponses1 = new ArrayList<>();
        HoldingsResponse holdingsResponse1 = new HoldingsResponse();
        holdingsResponse1.setHoldingsId("who-2");
        holdingsResponse1.setOperation("Updated");
        holdingsResponse1.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsResponses1.add(holdingsResponse1);

        bibResponse1.setHoldingsResponses(holdingsResponses1);
        bibResponses.add(bibResponse1);

        BibResponse bibResponse2 = new BibResponse();
        bibResponse2.setRecordIndex(3);
        bibResponse2.setOperation("Updated");
        bibResponse2.setBibId("wbm-103");

        List<HoldingsResponse> holdingsResponses2 = new ArrayList<>();
        HoldingsResponse holdingsResponse2 = new HoldingsResponse();
        holdingsResponse2.setHoldingsId("who-3");
        holdingsResponse2.setOperation("Updated");
        holdingsResponse2.setHoldingsType(PHoldings.PRINT);
        ItemResponse itemResponse2 = new ItemResponse();
        itemResponse2.setItemId("wio-3");
        itemResponse2.setOperation("Updated");
        holdingsResponse2.getItemResponses().add(itemResponse2);
        holdingsResponses2.add(holdingsResponse2);

        bibResponse2.setHoldingsResponses(holdingsResponses2);
        bibResponses.add(bibResponse2);

        BibResponse bibResponse3 = new BibResponse();
        bibResponse3.setRecordIndex(4);
        bibResponse3.setOperation("Created");
        bibResponse3.setBibId("wbm-104");


        List<HoldingsResponse> holdingsResponses3 = new ArrayList<>();
        HoldingsResponse holdingsResponse3 = new HoldingsResponse();
        holdingsResponse3.setHoldingsId("who-4");
        holdingsResponse3.setOperation("Created");
        holdingsResponse3.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsResponses3.add(holdingsResponse3);

        bibResponse3.setHoldingsResponses(holdingsResponses3);
        bibResponses.add(bibResponse3);

        BatchRestController batchRestController = new BatchRestController();
        Map<String, List<List<String>>> bibContent = new BatchExcelReportUtil().getBibContent(bibResponses);
        assertTrue(bibContent.size() == 4);
        System.out.println(bibContent);
    }

    @Test
    public void testHoldingsResponse() {
        List<HoldingsResponse> holdingsResponses = new ArrayList<>();

        HoldingsResponse holdingsResponse = new HoldingsResponse();
        holdingsResponse.setHoldingsId("who-1");
        holdingsResponse.setOperation("Created");
        holdingsResponse.setHoldingsType(PHoldings.PRINT);
        holdingsResponses.add(holdingsResponse);

        HoldingsResponse holdingsResponse1 = new HoldingsResponse();
        holdingsResponse1.setHoldingsId("who-2");
        holdingsResponse1.setOperation("Updated");
        holdingsResponse1.setHoldingsType(EHoldings.ELECTRONIC);
        holdingsResponses.add(holdingsResponse1);

        HoldingsResponse holdingsResponse2 = new HoldingsResponse();
        holdingsResponse2.setHoldingsId("who-3");
        holdingsResponse2.setOperation("Updated");
        holdingsResponse2.setHoldingsType(PHoldings.PRINT);
        holdingsResponses.add(holdingsResponse2);

        HoldingsResponse holdingsResponse4 = new HoldingsResponse();
        holdingsResponse4.setHoldingsId("who-4");
        holdingsResponse4.setOperation("Updated");
        holdingsResponse4.setHoldingsType(PHoldings.PRINT);
        holdingsResponses.add(holdingsResponse4);

        BatchRestController batchRestController = new BatchRestController();
        Map<String, List<List<String>>> holdingsContent = new BatchExcelReportUtil().getHoldingsContent(holdingsResponses, "wbm-101", "5");
        assertTrue(holdingsContent.size() == 2);
        System.out.println(holdingsContent);
    }

}