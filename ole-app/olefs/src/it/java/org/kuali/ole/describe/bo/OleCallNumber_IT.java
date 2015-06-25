package org.kuali.ole.describe.bo;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.select.bo.OleCallNumber;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/24/12
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */

@Transactional
public class OleCallNumber_IT extends KFSTestCaseBase {
    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
        OleCallNumber oleCallNumber = new OleCallNumber();
        oleCallNumber.setProfileId("Mock Profile Id");
        oleCallNumber.setCallNumberPreferenceOne("Mock Call Number Preference One");
        oleCallNumber.setCallNumberPreferenceTwo("Mock Call Number Preference Two");
        oleCallNumber.setCallNumberPreferenceThree("Mock Call Number Preference Three");
        oleCallNumber.setInputValue("Some abc");
        oleCallNumber.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleCallNumber savedOleCallNumber = boService.save(oleCallNumber);
        assertNotNull(savedOleCallNumber);
        assertNotNull(savedOleCallNumber.getInputValue());
    }


    @Test
    @Transactional
    public void testSearch() {
        OleCallNumber oleCallNumber = new OleCallNumber();
        oleCallNumber.setProfileId("Mock Profile Id describe");
        oleCallNumber.setCallNumberPreferenceOne("Mock Call Number Preference One");
        oleCallNumber.setCallNumberPreferenceTwo("Mock Call Number Preference Two");
        oleCallNumber.setCallNumberPreferenceThree("Mock Call Number Preference Three");
        oleCallNumber.setInputValue("Some abc describe");
        oleCallNumber.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        OleCallNumber savedOleCallNumber = boService.save(oleCallNumber);
        assertNotNull(savedOleCallNumber);
        OleCallNumber callNumber = boService.findBySinglePrimaryKey(OleCallNumber.class,savedOleCallNumber.getInputValue());
        assertEquals("Mock Profile Id describe", callNumber.getProfileId());
        assertEquals("Mock Call Number Preference One",callNumber.getCallNumberPreferenceOne());
        assertEquals("Mock Call Number Preference Two", callNumber.getCallNumberPreferenceTwo());
        assertEquals("Mock Call Number Preference Three", callNumber.getCallNumberPreferenceThree());
        assertEquals(true, callNumber.isActive());
    }
}
