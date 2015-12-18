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
    public void testCreatePurchaseOrderDocument() throws Exception {
        String jsonString = "{\n" +
                "\t\"oleBibRecord\": {\n" +
                "\t\t\"bib\": {\n" +
                "\t\t\t\"title\": \"Test Bib For req\",\n" +
                "\t\t\t\"author\": \"Author\",\n" +
                "\t\t\t\"publisher\": \"Publisher\"\n" +
                "\t\t},\n" +
                "\t\t\"bibUUID\": \"wbm-10362693\"\n" +
                "\t},\n" +
                "\t\"oleTxRecord\": {\n" +
                "\t\t\"percent\": \"100\",\n" +
                "\t\t\"itemType\": \"ITEM\",\n" +
                "\t\t\"chartCode\": \"UC\",\n" +
                "\t\t\"orgCode\": \"LIB\",\n" +
                "\t\t\"orderType\": \"Firm, Fixed\",\n" +
                "\t\t\"objectCode\": \"4000\",\n" +
                "\t\t\"costSource\": \"EST\",\n" +
                "\t\t\"listPrice\": \"100\",\n" +
                "\t\t\"quantity\": \"1\",\n" +
                "\t\t\"accountNumber\": \"4BIBS\",\n" +
                "\t\t\"buildingCode\": \"JRL\",\n" +
                "\t\t\"deliveryCampusCode\": \"UC\",\n" +
                "\t\t\"deliveryBuildingRoomNumber\": \"170\",\n" +
                "\t\t\"vendorNumber\": \"514-0\",\n" +
                "\t\t\"itemChartCode\": \"UC\",\n" +
                "\t\t\"fundingSource\": \"INST\",\n" +
                "\t\t\"methodOfPOTransmission\": \"NO PRINT\",\n" +
                "\t\t\"itemNoOfParts\": \"1\"\n" +
                "\t}\n" +
                "}";
        String responseContent = sendPostRequest(URL, jsonString);
        assertTrue(org.apache.commons.lang3.StringUtils.isNotBlank(responseContent));
        System.out.println(responseContent);
        JSONObject jsonObject = new JSONObject(responseContent);
        assertTrue(jsonObject.has("status") && jsonObject.getString("status").equalsIgnoreCase("success"));
    }
}