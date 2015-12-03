package org.kuali.ole.dsng.dao;

import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;

/**
 * Created by SheikS on 12/3/2015.
 */
public interface BibDAO {

    public BibRecord retrieveBibById(String id);
}
