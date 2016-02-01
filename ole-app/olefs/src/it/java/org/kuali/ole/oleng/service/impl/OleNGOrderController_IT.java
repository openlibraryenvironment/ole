package org.kuali.ole.oleng.service.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.OLERestBaseTestCase;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.pojo.OleBibRecord;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.pojo.OleTxRecord;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 12/17/2015.
 */
public class OleNGOrderController_IT extends OLERestBaseTestCase {

    private String URL = OLEFS_APPLICATION_URL + "/rest/acq/order/createOrder";

    @Test
    public void testCreatePurchaseOrderDocument() throws Exception {
        String jsonString = getOrderRecordJsonString();

        String responseContent = sendPostRequest(URL, jsonString,"json");
        assertTrue(org.apache.commons.lang3.StringUtils.isNotBlank(responseContent));
        System.out.println(responseContent);
        JSONObject jsonObject = new JSONObject(responseContent);
        assertTrue(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success"));
    }

    private String getOrderRecordJsonString() throws IOException {
        OleOrderRecord oleOrderRecord = new OleOrderRecord();
        OleTxRecord oleTxRecord = new OleTxRecord();
        oleTxRecord.setVendorNumber("1050");
        oleTxRecord.setDeliveryBuildingRoomNumber("170");
        oleTxRecord.setChartCode("UC");
        oleTxRecord.setOrgCode("LIB");
        oleTxRecord.setFundingSource("INST");
        oleTxRecord.setDeliveryCampusCode("UC");
        oleTxRecord.setBuildingCode("JRL");
        oleTxRecord.setMethodOfPOTransmission("NO PRINT");
        oleTxRecord.setCostSource("EST");
        oleTxRecord.setObjectCode("4000");
        oleTxRecord.setItemChartCode("UC");
        oleTxRecord.setOrderType("Firm, Fixed");
        oleTxRecord.setQuantity("1");
        oleTxRecord.setItemNoOfParts("1");
        oleTxRecord.setItemType(PurapConstants.ItemTypeCodes.ITEM_TYPE_ITEM_CODE);
        oleTxRecord.setVendorNumber("514-0");
        oleTxRecord.setListPrice("100");
        oleTxRecord.setAccountNumber("4BIBS");
        oleTxRecord.setPercent("100");
        oleOrderRecord.setOleTxRecord(oleTxRecord);
        OleBibRecord oleBibRecord = new OleBibRecord();
        oleBibRecord.setBibUUID("wbm-10362693");
        Bib bib = new Bib();
        bib.setTitle("Test Bib For req");
        bib.setAuthor("Author");
        bib.setPublisher("Publisher");
        bib.setIsbn("1010101010");
        oleBibRecord.setBib(bib);
        oleOrderRecord.setOleBibRecord(oleBibRecord);

        return new ObjectMapper().writeValueAsString(oleOrderRecord);
    }
}