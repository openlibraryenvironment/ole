package org.kuali.ole.dsng.rest.handler.holdings;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;

/**
 * Created by pvsubrah on 12/24/15.
 */
public abstract class HoldingsHandler extends Handler {
    public abstract void processDataMappings(JSONObject requestJsonObject, Exchange exchange);
}
