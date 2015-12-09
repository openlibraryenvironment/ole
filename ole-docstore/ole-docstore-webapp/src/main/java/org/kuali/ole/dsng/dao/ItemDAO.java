package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

/**
 * Created by SheikS on 12/7/2015.
 */
public interface ItemDAO {
    public ItemRecord save(ItemRecord ItemRecord);
    public ItemRecord retrieveItemById(String id);
}
