package org.kuali.ole.deliver.bo;

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

import static junit.framework.Assert.assertEquals;

public class OlePatronNoteType_IT extends KFSTestCaseBase {
    private BusinessObjectService boService ;

    @Test
    @Transactional
    public void testSave() {
         OlePatronNoteType olePatronNoteType = new OlePatronNoteType();
         olePatronNoteType.setPatronNoteTypeId("mockId");
         olePatronNoteType.setPatronNoteTypeCode("mockCd");
         olePatronNoteType.setPatronNoteTypeName("mockNoteTypeName");
         olePatronNoteType.setActive(true);
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(olePatronNoteType);
         OlePatronNoteType patronNoteTypeService = boService.findBySinglePrimaryKey(OlePatronNoteType.class,olePatronNoteType.getPatronNoteTypeId());
         assertEquals("mockCd",patronNoteTypeService.getPatronNoteTypeCode());
         assertEquals("mockNoteTypeName",patronNoteTypeService.getPatronNoteTypeName());
    }

    @Test
    @Transactional
    public void testSearch(){
         OlePatronNoteType olePatronNoteType = new OlePatronNoteType();
         olePatronNoteType.setPatronNoteTypeId("mockId");
         olePatronNoteType.setPatronNoteTypeCode("mockCd");
         olePatronNoteType.setPatronNoteTypeName("mockNoteTypeName");
         olePatronNoteType.setActive(true);
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(olePatronNoteType);
         OlePatronNoteType patronNoteTypeService = boService.findBySinglePrimaryKey(OlePatronNoteType.class,olePatronNoteType.getPatronNoteTypeId());
         assertEquals("mockCd",patronNoteTypeService.getPatronNoteTypeCode());
         assertEquals("mockNoteTypeName",patronNoteTypeService.getPatronNoteTypeName());
    }
}

