package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;

/**
 * Created by pvsubrah on 12/7/15.
 */
public interface BibDAO {
    public BibRecord retrieveBibById(String id);
}
