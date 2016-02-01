package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by SheikS on 12/3/2015.
 */
@Repository("bibDAO")
@Scope("prototype")
public class BibDAOImpl extends OleDsNGDAOBase implements BibDAO {

    public BibRecord save(BibRecord bibRecord) {
        return getBusinessObjectService().save(bibRecord);
    }

    @Override
    public List<BibRecord> saveAll(List<BibRecord> bibRecords) {
        return (List<BibRecord>) getBusinessObjectService().save(bibRecords);
    }

    public BibRecord retrieveBibById(String id) {
        return getBusinessObjectService().findBySinglePrimaryKey(BibRecord.class, id);
    }
}
