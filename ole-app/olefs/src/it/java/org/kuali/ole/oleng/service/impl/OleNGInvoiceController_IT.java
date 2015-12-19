package org.kuali.ole.oleng.service.impl;

import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.kuali.ole.OLERestBaseTestCase;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 12/17/2015.
 */
public class OleNGInvoiceController_IT extends OLERestBaseTestCase {

    private String URL = OLEFS_APPLICATION_URL + "/batchProfile/invoice/createInvoice";

    @Test
    public void testInvoiceDocument() throws Exception {
        String jsonString = "[{\n" +
                "\t\"lineItemAdditionalCharge\": \"40\",\n" +
                "\t\"purchaseOrderNumber\": 122937,\n" +
                "\t\"additionalCharge\": \"50\",\n" +
                "\t\"objectCode\": \"4000\",\n" +
                "\t\"listPrice\": \"100\",\n" +
                "\t\"quantity\": \"1\",\n" +
                "\t\"accountNumber\": \"4BIBS\",\n" +
                "\t\"vendorNumber\": \"514-0\",\n" +
                "\t\"itemDescription\": \"Invoice Item Description\",\n" +
                "\t\"invoiceDate\": \"20151215\",\n" +
                "\t\"invoiceCurrencyExchangeRate\": \"66.40\",\n" +
                "\t\"validDoc\": true,\n" +
                "\t\"invoiceNumber\": \"101011\",\n" +
                "\t\"unitPrice\": \"5\"\n" +
                "}]";
        String responseContent = sendPostRequest(URL, jsonString);
        assertTrue(org.apache.commons.lang3.StringUtils.isNotBlank(responseContent));
        System.out.println(responseContent);
        JSONObject jsonObject = new JSONObject(responseContent);
        assertTrue(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success"));
    }
}