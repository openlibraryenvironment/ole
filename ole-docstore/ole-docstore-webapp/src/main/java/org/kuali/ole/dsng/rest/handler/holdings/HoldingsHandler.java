package org.kuali.ole.dsng.rest.handler.holdings;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.HoldingsAndItemsGeneralHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 12/24/15.
 */
public abstract class HoldingsHandler extends HoldingsAndItemsGeneralHandler {
    public abstract void processDataMappings(JSONObject requestJsonObject, Exchange exchange);

    public List<String> parseCommaSeperatedValues(String value){
        List values = new ArrayList();

        StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
        while(stringTokenizer.hasMoreTokens()){
            values.add(stringTokenizer.nextToken());
        }
        return values;
    }
}
