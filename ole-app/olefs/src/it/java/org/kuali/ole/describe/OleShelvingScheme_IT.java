package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/11/12
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleShelvingScheme_IT extends SpringBaseTestCase {

    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
        OleShelvingScheme oleShelvingScheme = new OleShelvingScheme();
        oleShelvingScheme.setShelvingSchemeCode("n1");
        oleShelvingScheme.setShelvingSchemeName("No information provided");
        oleShelvingScheme.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleShelvingScheme.setSourceDate(new java.sql.Date(st.getTime()));
        oleShelvingScheme.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleShelvingScheme);
        OleShelvingScheme oleShelvingSchemeService = boService.findBySinglePrimaryKey(OleShelvingScheme.class,oleShelvingScheme.getShelvingSchemeId());
        assertEquals("n1",oleShelvingSchemeService.getShelvingSchemeCode());
        assertEquals("No information provided",oleShelvingSchemeService.getShelvingSchemeName());
    }

    @Test
    @Transactional
    public void testSearch() {
        OleShelvingScheme oleShelvingScheme = new OleShelvingScheme();
        oleShelvingScheme.setShelvingSchemeCode("01");
        oleShelvingScheme.setShelvingSchemeName("Library of Congress classification ");
        oleShelvingScheme.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleShelvingScheme.setSourceDate(new java.sql.Date(st.getTime()));
        oleShelvingScheme.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleShelvingScheme);
        OleShelvingScheme oleShelvingSchemeService = boService.findBySinglePrimaryKey(OleShelvingScheme.class,oleShelvingScheme.getShelvingSchemeId());
        assertEquals("01",oleShelvingSchemeService.getShelvingSchemeCode());
        assertEquals("Library of Congress classification ",oleShelvingSchemeService.getShelvingSchemeName());
    }
}
