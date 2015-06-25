package org.kuali.ole;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/11/12
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */

public class DataCarrierService_IT extends KFSTestCaseBase {
    //TODO: This test needs to test the methods. Also no need to get it from Spring.
    @Test
    @Transactional
    public void addData() throws Exception {
        DataCarrierService dataCarrierService = GlobalResourceLoader.getService("dataCarrierService");
        assertNotNull(dataCarrierService);
        dataCarrierService.addData(OLEConstants.OLE_BIB_RECORD, new BibliographicRecord());
        BibliographicRecord data = (BibliographicRecord) dataCarrierService.getData(OLEConstants.OLE_BIB_RECORD);
        assertNotNull(data);
    }
}
