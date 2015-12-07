package org.kuali.ole.dsng.dao;

import org.junit.Test;
import org.kuali.ole.DocstoreTestCaseBase;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.HoldingsRecord;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/7/2015.
 */
public class HoldingDAOImpl_IT extends DocstoreTestCaseBase {

    @Autowired
    HoldingDAO holdingDAO;

    @Test
    public void testRetrieveHoldingById() throws Exception {
        HoldingsRecord holdingsRecord = holdingDAO.retrieveHoldingById("1");
        assertNotNull(holdingsRecord);
    }
}