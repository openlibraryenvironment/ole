package org.kuali.ole.dsng.rest.handler.eholdings;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.AuthenticationTypeRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.holdings.HoldingsHandler;
import org.kuali.ole.dsng.util.AuthenticationTypeUtil;

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
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        String authendicationType = getStringValueFromJsonObject(requestJsonObject, TYPE);
        if (null != holdingRecord.getAuthenticationType() &&
                StringUtils.equals(holdingRecord.getAuthenticationType().getCode(), authendicationType)) {
            exchange.add("matchedHoldings", holdingRecord);
        }
    }

    @Override
    public void processDataMappings(JSONObject requestJsonObject, Exchange exchange) {
        String authentication = getStringValueFromJsonObject(requestJsonObject, TYPE);
        HoldingsRecord holdingRecord = (HoldingsRecord) exchange.get("holdingsRecord");
        AuthenticationTypeRecord authenticationTypeRecord = new AuthenticationTypeUtil().fetchAuthenticationTypeRecordByCode(authentication);
        if (null != authenticationTypeRecord) {
            holdingRecord.setAuthenticationTypeId(authenticationTypeRecord.getAuthenticationTypeId());
            holdingRecord.setAuthenticationType(authenticationTypeRecord);
        }
        exchange.add("holdingsRecord", holdingRecord);
    }
}
