package org.kuali.ole.dsng.rest.handler.holdings;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

import java.util.Iterator;
import java.util.List;

/**
 * Created by pvsubrah on 12/23/15.
 */
public class UpdateHoldingsHandler extends Handler {
    @Override
    public Boolean isInterested(String operation) {
        List<String> operationsList = getOperationsList(operation);
        for (Iterator iterator = operationsList.iterator(); iterator.hasNext(); ) {
            String op = (String) iterator.next();
            if(op.equals("122") || op.equals("222")){
                return true;
            }
        }
        return false;
    }

    @Override
    public void process(JSONObject requestJsonObject, Exchange exchange) {
        try {
            JSONObject holdingJsonObject = requestJsonObject.getJSONObject("holdings");
            if(holdingJsonObject.has("matchPoints")){
                JSONObject matchPoints = holdingJsonObject.getJSONObject("matchPoints");

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
