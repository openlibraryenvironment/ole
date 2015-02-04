package org.kuali.ole.deliver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.defaultload.LoadDefaultCirculationPoliciesBean;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/31/12
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class LoadDefaultCirculationPoliciesBean_IT extends SpringBaseTestCase {
    protected LoadDefaultCirculationPoliciesBean loadDefaultCirculationPoliciesBean;
    private BusinessObjectService businessObjectService;

    //TODO: This needs to be moved to src/it. Also should have proper asserts.

    @Before
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        loadDefaultCirculationPoliciesBean = GlobalResourceLoader.getService("loadDefaultCirculationPoliciesBean");
    }
    @Ignore
    @Test
    @Transactional
    public void loadDefaultLocations() throws Exception {
        List<String> policies = loadDefaultCirculationPoliciesBean.loadDefaultCircPolicies(false);
        assertNotNull(policies);
        for (Iterator<String> iterator = policies.iterator(); iterator.hasNext(); ) {
            String policy = iterator.next();
            System.out.println(policy);
        }
    }
}
