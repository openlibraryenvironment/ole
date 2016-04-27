package org.kuali.ole.dsng.rest.handler.items;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.Exchange;
import org.kuali.ole.dsng.rest.handler.HoldingsAndItemsGeneralHandler;

/**
 * Created by SheikS on 12/20/2015.
 */
public abstract class ItemHandler extends HoldingsAndItemsGeneralHandler {
    public abstract void processDataMappings(JSONObject requestJsonObject, Exchange exchange);
}