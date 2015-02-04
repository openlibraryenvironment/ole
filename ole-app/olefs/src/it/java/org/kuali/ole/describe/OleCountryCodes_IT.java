package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleCountryCodes;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: meenau
 * Date: 6/1/12
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */


public class OleCountryCodes_IT extends SpringBaseTestCase {
    private BusinessObjectService boService ;
    @Test
    @Transactional
    public void testSave() {
        OleCountryCodes oleCountryCodes = new OleCountryCodes();
        oleCountryCodes.setCountryCode("mockCde");
        oleCountryCodes.setCountryName("mockCountryName");
        oleCountryCodes.setCountryRegionName("mockCountryRegionName");
        oleCountryCodes.setCountryNameSequence("mockCountryNameSequence");
        oleCountryCodes.setSource("mockSource");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleCountryCodes.setSourceDate(new java.sql.Date(st.getTime()));
        oleCountryCodes.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleCountryCodes);
        OleCountryCodes oleCountryCodesService = boService.findBySinglePrimaryKey(OleCountryCodes.class,oleCountryCodes.getCountryCodeId());
        assertEquals("mockCde",oleCountryCodesService.getCountryCode());
        assertEquals("mockCountryName",oleCountryCodesService.getCountryName());
        assertEquals("mockCountryRegionName",oleCountryCodesService.getCountryRegionName());
        assertEquals("mockCountryNameSequence",oleCountryCodesService.getCountryNameSequence());
    }

    @Test
    @Transactional
    public void testSearch(){
        OleCountryCodes oleCountryCodes = new OleCountryCodes();
        oleCountryCodes.setCountryCode("mockCde0");
        oleCountryCodes.setCountryName("mockCountryName0");
        oleCountryCodes.setCountryRegionName("mockCountryRegionName0");
        oleCountryCodes.setCountryNameSequence("mockCountryNameSequence0");
        oleCountryCodes.setSource("mockSource0");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleCountryCodes.setSourceDate(new java.sql.Date(st.getTime()));
        oleCountryCodes.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(oleCountryCodes);
        OleCountryCodes oleCountryCodesService = boService.findBySinglePrimaryKey(OleCountryCodes.class,oleCountryCodes.getCountryCodeId());
        assertEquals("mockCde0",oleCountryCodesService.getCountryCode());
        assertEquals("mockCountryName0",oleCountryCodesService.getCountryName());
        assertEquals("mockCountryRegionName0",oleCountryCodesService.getCountryRegionName());
        assertEquals("mockCountryNameSequence0",oleCountryCodesService.getCountryNameSequence());
    }
}
