package org.kuali.ole.ingest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/30/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class LoadDefaultIngestProfile_IT extends SpringBaseTestCase {
    protected LoadDefaultIngestProfileBean loadDefaultIngestProfileBean;
    private BusinessObjectService businessObjectService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
        loadDefaultIngestProfileBean = GlobalResourceLoader.getService("loadDefaultIngestProfileBean");
    }

    @Test
    @Transactional
    public void ingestDefaultIntestProfile() throws Exception {
        loadDefaultIngestProfileBean.loadDefaultIngestProfile(false);
        Collection<AgendaBo> agendas = businessObjectService.findAll(AgendaBo.class);
        assertNotNull(agendas);
        List<String> agendaNames = new ArrayList<String>();
        for (Iterator<AgendaBo> iterator = agendas.iterator(); iterator.hasNext(); ) {
            AgendaBo agendaBo = iterator.next();
            agendaNames.add(agendaBo.getName());
        }
        assertTrue(agendaNames.contains("YBP"));
    }
}
