package org.kuali.ole.dsng.rest.processor;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.common.document.PHoldings;
import org.kuali.ole.docstore.common.response.BibResponse;
import org.kuali.ole.docstore.common.response.OleNGBibImportResponse;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.model.HoldingsRecordAndDataMapping;
import org.kuali.ole.dsng.model.ItemRecordAndDataMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pvsubrah on 1/21/16.
 */
public class OleDsNgOverlayProcessorTest {

    @Test
    public void buildBibResponseTest() {
        Exchange exchange = new Exchange();
        OleDsNgOverlayProcessor oleDsNgOverlayProcessor = new OleDsNgOverlayProcessor();
        BibResponse bibResponse = new BibResponse();
        List<BibResponse> bibResponses = new ArrayList<BibResponse>();
        bibResponse.setOperation(OleNGConstants.CREATED);
        bibResponse.setBibId("1001");
        HoldingsRecordAndDataMapping holdingsRecordAndDataMapping = new HoldingsRecordAndDataMapping();
        HoldingsRecord holdingsRecordToCreate = new HoldingsRecord();
        holdingsRecordToCreate.setHoldingsId("1");
        holdingsRecordToCreate.setHoldingsType(PHoldings.PRINT);
        ItemRecord itemRecord = new ItemRecord();
        itemRecord.setItemId("1");
        itemRecord.setHoldingsId(holdingsRecordToCreate.getHoldingsId());
        holdingsRecordAndDataMapping.setHoldingsRecord(holdingsRecordToCreate);
        List<HoldingsRecordAndDataMapping> holdingsRecordAndDataMappingList = new ArrayList<HoldingsRecordAndDataMapping>();
        holdingsRecordAndDataMappingList.add(holdingsRecordAndDataMapping);
        ItemRecordAndDataMapping itemRecordAndDataMapping = new ItemRecordAndDataMapping();
        itemRecordAndDataMapping.setItemRecord(itemRecord);
        List<ItemRecordAndDataMapping> itemRecordAndDataMappingList = new ArrayList<ItemRecordAndDataMapping>();
        itemRecordAndDataMappingList.add(itemRecordAndDataMapping);
        exchange.add(OleNGConstants.HOLDINGS_FOR_CREATE, holdingsRecordAndDataMappingList);
        exchange.add(OleNGConstants.ITEMS_FOR_CREATE, itemRecordAndDataMappingList);
        BibRecord bibRecord = new BibRecord();
        oleDsNgOverlayProcessor.buildBibResponses(bibResponse, bibRecord, exchange, Collections.singletonList("211"));
        bibResponses.add(bibResponse);
        OleNGBibImportResponse oleNGBibImportResponse = new OleNGBibImportResponse();
        oleNGBibImportResponse.setBibResponses(bibResponses);
        try {
            String message = new ObjectMapper().defaultPrettyPrintingWriter().writeValueAsString(oleNGBibImportResponse);
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}