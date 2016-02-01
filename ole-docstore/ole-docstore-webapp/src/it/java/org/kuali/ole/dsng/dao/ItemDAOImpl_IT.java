package org.kuali.ole.dsng.dao;

import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/7/2015.
 */
public class ItemDAOImpl_IT extends DocstoreTestCaseBase {

    @Autowired
    ItemDAO itemDAO;

    @Test
    public void testRetrieveItemById() throws Exception {
        ItemRecord itemRecord = itemDAO.retrieveItemById("1");
        assertNotNull(itemRecord);
    }
}