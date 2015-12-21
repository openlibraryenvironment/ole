package org.kuali.ole.dsng.rest.handler.overlay.item;

import org.codehaus.jettison.json.JSONObject;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.dsng.util.OleDsNgOverlayUtil;

/**
 * Created by SheikS on 12/20/2015.
 */
public abstract class ItemOverlayHandler extends OleDsNgOverlayUtil {

    public abstract boolean isInterested(JSONObject jsonObject);

    public abstract boolean isMatching(ItemRecord itemRecord, JSONObject jsonObject);
}