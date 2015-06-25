package org.kuali.ole.ingest.pojo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/8/12
 * Time: 9:22 AM
 * To change this template use File | Settings | File Templates.
 */

public class ProfileAttributeBo_IT extends KFSTestCaseBase{
    
    @Test
    @Transactional
    public void testCreateProfileAttributeBo() throws Exception {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        ProfileAttributeBo profileAttributeBo = new ProfileAttributeBo();
        profileAttributeBo.setAgendaName("mock_agenda");
        profileAttributeBo.setAttributeName("mock_attribute");
        profileAttributeBo.setAttributeValue("mock_value");
        businessObjectService.save(Arrays.asList(profileAttributeBo));

        HashMap map = new HashMap();
        map.put("agenda_name", "mock_agenda");
        List<ProfileAttributeBo> matching = (List<ProfileAttributeBo>) businessObjectService.findMatching(ProfileAttributeBo.class, map);
        assertNotNull(matching);
        assertTrue(!matching.isEmpty());
    }
    
    
}
