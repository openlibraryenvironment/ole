package org.kuali.ole.oleng.service.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.OLERestBaseTestCase;
import org.kuali.ole.pojo.OleInvoiceRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 12/17/2015.
 */
public class OleNGInvoiceController_IT extends OLERestBaseTestCase {

    private String URL = OLEFS_APPLICATION_URL + "/rest/acq/invoice/createInvoice";

    @Test
    public void testInvoiceDocument() throws Exception {
        String jsonString = getInvoiceRecordJsonString();

        String responseContent = sendPostRequest(URL, jsonString,"json");
        assertTrue(org.apache.commons.lang3.StringUtils.isNotBlank(responseContent));
        System.out.println(responseContent);
        JSONObject jsonObject = new JSONObject(responseContent);
        assertTrue(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success"));
    }

    private String getInvoiceRecordJsonString() throws IOException {
        OleInvoiceRecord oleInvoiceRecord = new OleInvoiceRecord();

        oleInvoiceRecord.setInvoiceNumber("101011");
        oleInvoiceRecord.setInvoiceDate("20151215");
        oleInvoiceRecord.setVendorNumber("514-0");
        oleInvoiceRecord.setInvoiceCurrencyExchangeRate("66.40");
        oleInvoiceRecord.setPurchaseOrderNumber(122937);
        oleInvoiceRecord.setQuantity("1");
        oleInvoiceRecord.setListPrice("100");
        oleInvoiceRecord.setUnitPrice("100");
        oleInvoiceRecord.setLineItemAdditionalCharge("100");
        oleInvoiceRecord.setAccountNumber("4BIBS");
        oleInvoiceRecord.setObjectCode("4000");
        oleInvoiceRecord.setItemDescription("Invoice Item Description");
        oleInvoiceRecord.setAdditionalCharge("100");

        List<OleInvoiceRecord> oleInvoiceRecords = new ArrayList<>();
        oleInvoiceRecords.add(oleInvoiceRecord);
        return new ObjectMapper().writeValueAsString(oleInvoiceRecords);
    }
}