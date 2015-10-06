package org.kuali.ole.sip2.sip2Server.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.kuali.ole.bo.OLERenewItemList;
import org.kuali.ole.converter.OLERenewItemConverter;
import org.kuali.ole.request.OLESIP2RenewRequestParser;
import org.kuali.ole.response.OLESIP2RenewResponse;
import org.kuali.ole.sip2.response.OLESIP2RenewTurnedOffResponse;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by chenchulakshmig on 9/29/15.
 */
public class RenewNettyProcessor extends NettyProcessor {
    private final Properties properties;
    private String serverURL;

    public RenewNettyProcessor(Properties properties, String serverURL) {
        this.properties = properties;
        this.serverURL = serverURL;
    }

    @Override
    public boolean isInterested(String code) {
        return code.equals("29");
    }

    @Override
    public String process(String requestData) {
        String response = "";
        OLESIP2RenewRequestParser sip2RenewRequestParser = new OLESIP2RenewRequestParser(requestData);
        requestData = createJSONForRenewItemRequest(sip2RenewRequestParser.getPatronIdentifier(), sip2RenewRequestParser.getItemIdentifier(), "SIP2_OPERATOR_ID");

        response = postRequest(requestData, "/renewItemForSIP2", serverURL);

        if (StringUtils.isNotBlank(response)) {
            OLERenewItemList oleRenewItemList = (OLERenewItemList) new OLERenewItemConverter().generateRenewItemListObjectForSip2(response);
            OLESIP2RenewResponse sip2RenewResponse = new OLESIP2RenewResponse();

            if (oleRenewItemList!=null && CollectionUtils.isNotEmpty(oleRenewItemList.getRenewItemList())){
                response = sip2RenewResponse.getSIP2RenewResponse(oleRenewItemList.getRenewItemList().get(0), sip2RenewRequestParser);
            }
        }
        return response;
    }

    public String createJSONForRenewItemRequest(String patronBarcode, String itemBarcode, String operatorId) {
        JSONObject jsonObject = new JSONObject();
        List<String> itemBarcodes = new ArrayList<>();
        itemBarcodes.add(itemBarcode);
        try {
            jsonObject.put("patronBarcode", patronBarcode);
            jsonObject.put("itemBarcodes", itemBarcodes);
            jsonObject.put("operatorId", operatorId);
            jsonObject.put("requestFormatType", "JSON");
            jsonObject.put("responseFormatType", "XML");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @Override
    public boolean isServiceTurnedOn() {
        return properties.getProperty("sip2.service.renew").equalsIgnoreCase("yes");
    }

    @Override
    public String getResponseForServiceTurnedOff(String requestData) {
        OLESIP2RenewTurnedOffResponse renewTurnedOffResponse = new OLESIP2RenewTurnedOffResponse();
        return renewTurnedOffResponse.getOLESIP2RenewTurnedOffResponse(requestData);
    }
}
