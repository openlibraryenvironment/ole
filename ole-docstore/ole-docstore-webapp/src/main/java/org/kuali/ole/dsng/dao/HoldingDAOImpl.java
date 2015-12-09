package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by SheikS on 12/7/2015.
 */
@Repository("holdingDAO")
@Scope("prototype")
public class HoldingDAOImpl extends OleDsNgDAOBase implements HoldingDAO {

    public HoldingsRecord save(HoldingsRecord holdingsRecord) {
        return getBusinessObjectService().save(holdingsRecord);
    }

    public HoldingsRecord retrieveHoldingById(String id) {
        return getBusinessObjectService().findBySinglePrimaryKey(HoldingsRecord.class, id);
    }
}
