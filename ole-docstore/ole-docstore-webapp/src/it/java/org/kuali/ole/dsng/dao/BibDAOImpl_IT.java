package org.kuali.ole.dsng.dao;

import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.BibRecord;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/3/2015.
 */
public class BibDAOImpl_IT extends DocstoreTestCaseBase {

    @Autowired
    BibDAO bibDAO;

    @Test
    public void testRetrieveBibById() throws Exception {
        BibRecord bibRecord = bibDAO.retrieveBibById("10000046");
        assertNotNull(bibRecord);
    }
}