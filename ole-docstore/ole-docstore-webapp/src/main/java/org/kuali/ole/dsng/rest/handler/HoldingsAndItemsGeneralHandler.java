package org.kuali.ole.dsng.rest.handler;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by pvsubrah on 1/4/16.
 */
public abstract class HoldingsAndItemsGeneralHandler extends Handler {
    public HoldingsRecord getHoldingsRecordById(String holdingsId) {
        return getBusinessObjectService().findBySinglePrimaryKey(HoldingsRecord.class, holdingsId);
    }
}
