package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * Created by SheikS on 12/3/2015.
 */
@Repository("bibDAO")
@Scope("prototype")
public class BibDAOImpl extends OleDsNgDAOBase implements BibDAO {

    public BibRecord save(BibRecord bibRecord) {
        return getBusinessObjectService().save(bibRecord);
    }

    public BibRecord retrieveBibById(String id) {
        BibRecord matchedBibRecord = getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, id);
        return matchedBibRecord;
    }
}
