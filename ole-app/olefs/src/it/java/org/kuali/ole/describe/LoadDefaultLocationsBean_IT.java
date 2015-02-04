package org.kuali.ole.describe;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.describe.defaultload.LoadDefaultLocationsBean;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/31/12
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class LoadDefaultLocationsBean_IT extends SpringBaseTestCase{
    protected LoadDefaultLocationsBean loadDefaultLocationsBean;
    private BusinessObjectService businessObjectService;

    @Before
    @Ignore
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        loadDefaultLocationsBean = GlobalResourceLoader.getService("loadDefaultLocationsBean");
    }
    @Ignore
    @Test
    @Transactional
    public void loadDefaultLocations() throws Exception {
        loadDefaultLocationsBean.loadDefaultLocations(false);
        Collection<OleLocation> locations = businessObjectService.findAll(OleLocation.class);
        assertNotNull(locations);
    }
}
