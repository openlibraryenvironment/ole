package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.List;

/**
 * Created by SheikS on 12/7/2015.
 */
public interface ItemDAO {
    public ItemRecord save(ItemRecord itemRecord);
    public List<ItemRecord> saveAll(List<ItemRecord> itemRecords);
    public ItemRecord retrieveItemById(String id);
}
