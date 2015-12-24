package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SheikS on 12/7/2015.
 */
@Repository("itemDAO")
@Scope("prototype")
public class ItemDAOImpl extends OleDsNGDAOBase implements ItemDAO {

    public ItemRecord save(ItemRecord itemRecord) {
        return getBusinessObjectService().save(itemRecord);
    }

    @Override
    public List<ItemRecord> saveAll(List<ItemRecord> itemRecords) {
        return (List<ItemRecord>) getBusinessObjectService().save(itemRecords);
    }

    public ItemRecord retrieveItemById(String id) {
        return getBusinessObjectService().findBySinglePrimaryKey(ItemRecord.class, id);
    }
}
