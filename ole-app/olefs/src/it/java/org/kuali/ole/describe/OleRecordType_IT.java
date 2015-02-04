package org.kuali.ole.describe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.bo.OleRecordType;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 6/11/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleRecordType_IT extends SpringBaseTestCase {

    private BusinessObjectService boService;

    @Test
    @Transactional
    public void testSave() {
        OleRecordType oleRecordType = new OleRecordType();
        oleRecordType.setRecordTypeCode("m1");
        oleRecordType.setRecordTypeName("Multipart item holdings");
        oleRecordType.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleRecordType.setSourceDate(new java.sql.Date(st.getTime()));
        oleRecordType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleRecordType> oleRecordTypeCollection = boService.findAll(OleRecordType.class);
        for (OleRecordType recordType : oleRecordTypeCollection) {
            if (recordType.getRecordTypeCode().equalsIgnoreCase(oleRecordType.getRecordTypeCode())) {
                exists = true;
            }
        }

        if (!exists && oleRecordType.getSourceDate() != null) {
            boService.save(oleRecordType);
            OleRecordType oleRecordTypeService = boService
                    .findBySinglePrimaryKey(OleRecordType.class, oleRecordType.getRecordTypeId());
            assertEquals("m1", oleRecordTypeService.getRecordTypeCode());
            assertEquals("Multipart item holdings", oleRecordTypeService.getRecordTypeName());
        }

    }

    @Test
    @Transactional
    public void testSearch() {
        OleRecordType oleRecordType = new OleRecordType();
        oleRecordType.setRecordTypeCode("m1");
        oleRecordType.setRecordTypeName("Multipart item holdings");
        oleRecordType.setSource("Test Source");
        Timestamp st = new Timestamp(System.currentTimeMillis());
        oleRecordType.setSourceDate(new java.sql.Date(st.getTime()));
        oleRecordType.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boolean exists = false;
        Collection<OleRecordType> oleRecordTypeCollection = boService.findAll(OleRecordType.class);
        for (OleRecordType recordType : oleRecordTypeCollection) {
            if (recordType.getRecordTypeCode().equalsIgnoreCase(oleRecordType.getRecordTypeCode())) {
                exists = true;
            }
        }

        if (!exists && oleRecordType.getSourceDate() != null) {
            boService.save(oleRecordType);
            OleRecordType oleRecordTypeService = boService
                    .findBySinglePrimaryKey(OleRecordType.class, oleRecordType.getRecordTypeId());
            assertEquals("m1", oleRecordTypeService.getRecordTypeCode());
            assertEquals("Multipart item holdings", oleRecordTypeService.getRecordTypeName());
        }

    }
}
