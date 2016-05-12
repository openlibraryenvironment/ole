package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AuthenticationTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.AuthenticationTypeUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SheikS on 12/31/2015.
 */
public class AuthenticationTypeHandler extends HoldingsHandler {

    private final String TYPE = "Authentication Type";

    @Override
    public Boolean isInterested(String operation) {
        return operation.equals(TYPE);
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
        String authendicationType = getStringValueFromJsonObject(requestJsonObject, TYPE);
        List<String> parsedValues = parseCommaSeperatedValues(authendicationType);
        for (Iterator<String> iterator = parsedValues.iterator(); iterator.hasNext(); ) {
            String authendicationTypeValue = iterator.next();
            if (null != holdingRecord.getAuthenticationType() &&
                    StringUtils.equals(holdingRecord.getAuthenticationType().getCode(), authendicationTypeValue)) {
                exchange.add(OleNGConstants.MATCHED_HOLDINGS, Boolean.TRUE);
                exchange.add(OleNGConstants.MATCHED_VALUE, authendicationTypeValue);
            }
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        JSONArray jsonArrayeFromJsonObject = getJSONArrayeFromJsonObject(requestJsonObject, TYPE);
        List<String> listFromJSONArray = getListFromJSONArray(jsonArrayeFromJsonObject.toString());
        if(CollectionUtils.isNotEmpty(listFromJSONArray)) {
            String authentication = listFromJSONArray.get(0);
            HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get(OleNGConstants.HOLDINGS_RECORD);
            AuthenticationTypeRecord authenticationTypeRecord = getOleDsNGMemorizeService().fetchAuthenticationTypeRecordByCode(authentication);
            if (null != authenticationTypeRecord) {
                holdingRecord.setAuthenticationTypeId(authenticationTypeRecord.getAuthenticationTypeId());
                holdingRecord.setAuthenticationType(authenticationTypeRecord);
            }
            exchange.add(OleNGConstants.HOLDINGS_RECORD, holdingRecord);
        }
    }
}
