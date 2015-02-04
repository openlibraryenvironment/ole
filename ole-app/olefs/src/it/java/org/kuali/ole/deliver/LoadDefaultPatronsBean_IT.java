package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.defaultload.LoadDefaultPatronsBean;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/31/12
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class LoadDefaultPatronsBean_IT extends SpringBaseTestCase {
    protected LoadDefaultPatronsBean loadDefaultPatronsBean;
    private BusinessObjectService businessObjectService;

    //TODO: This needs to be moved to src/it. Also should have proper asserts.

    @Before
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        loadDefaultPatronsBean = GlobalResourceLoader.getService("loadDefaultPatronsBean");
    }
    @Ignore
    @Test
    @Transactional
    public void loadDefaultLocations() throws Exception {
        List<OlePatronDocument> olePatronDocuments = loadDefaultPatronsBean.loadDefaultPatrons(false);
        assertNotNull(olePatronDocuments);
    }
}
