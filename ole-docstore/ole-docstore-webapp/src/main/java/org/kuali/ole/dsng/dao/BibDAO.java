package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;

import java.util.List;

/**
 * Created by pvsubrah on 12/7/15.
 */
public interface BibDAO {
    public BibRecord save(BibRecord bibRecord);
    public List<BibRecord> saveAll(List<BibRecord> bibRecords);
    public BibRecord retrieveBibById(String id);
    public void deleteBibTreeRecord(String bibId);
}
