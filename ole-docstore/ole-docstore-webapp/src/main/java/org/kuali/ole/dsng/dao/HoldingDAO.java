package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;

import java.util.List;

/**
 * Created by SheikS on 12/7/2015.
 */
public interface HoldingDAO {
    public HoldingsRecord save(HoldingsRecord holdingsRecord);
    public List<HoldingsRecord> saveAll(List<HoldingsRecord> holdingsRecords);
    public HoldingsRecord retrieveHoldingById(String id);
}
