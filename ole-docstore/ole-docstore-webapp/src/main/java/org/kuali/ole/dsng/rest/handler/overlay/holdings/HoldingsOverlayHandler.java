package org.kuali.ole.dsng.rest.handler.overlay.holdings;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.dsng.util.OleDsHelperUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public abstract class HoldingsOverlayHandler extends OleDsHelperUtil {

    public abstract boolean isInterested(JSONObject jsonObject);

    public abstract boolean isMatching(HoldingsRecord holdingsRecord, JSONObject jsonObject);
}
