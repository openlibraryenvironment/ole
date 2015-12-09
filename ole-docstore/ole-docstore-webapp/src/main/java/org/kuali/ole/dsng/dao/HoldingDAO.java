package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

/**
 * Created by SheikS on 12/7/2015.
 */
public interface HoldingDAO {
    public HoldingsRecord save(HoldingsRecord holdingsRecord);
    public HoldingsRecord retrieveHoldingById(String id);
}
