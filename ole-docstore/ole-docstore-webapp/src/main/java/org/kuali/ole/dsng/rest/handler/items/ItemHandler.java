package org.kuali.ole.dsng.rest.handler.items;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.rest.Exchange;
import org.kuali.ole.dsng.rest.handler.Handler;
import org.kuali.ole.dsng.util.OleDsNgOverlayUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public abstract class ItemHandler extends Handler {
    public abstract void processDataMappings(JSONObject requestJsonObject, Exchange exchange);
}