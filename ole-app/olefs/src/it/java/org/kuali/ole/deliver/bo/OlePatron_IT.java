package org.kuali.ole.deliver.bo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.service.OlePatronHelperService;
import org.kuali.ole.service.OlePatronHelperServiceImpl;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/9/12
 * Time: 7:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class OlePatron_IT extends SpringBaseTestCase {
    @Test
    @Transactional
    public void createPatronRecord() throws Exception {
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        IdentityService identityService = KimApiServiceLocator.getIdentityService();

        OleBorrowerType oleBorrowerType = new OleBorrowerType();
        oleBorrowerType.setBorrowerTypeCode("mockBorrowerTypeCD1");
        oleBorrowerType.setBorrowerTypeDescription("mockDescription");
        oleBorrowerType.setBorrowerTypeName("mockName");
        oleBorrowerType.setActive(true);
        OleBorrowerType savedBorrowerType = businessObjectService.save(oleBorrowerType);
        assertNotNull(savedBorrowerType);
        String oleBorrowerTypeId = savedBorrowerType.getBorrowerTypeId();
        assertNotNull(oleBorrowerTypeId);

        EntityBo entityBo = new EntityBo();
        entityBo.setActive(Boolean.TRUE);
        EntityNameBo entityNameBo = new EntityNameBo();
        entityNameBo.setFirstName("mockFirstName");
        entityBo.setNames(Arrays.asList(entityNameBo));
        Entity entity = identityService.createEntity(EntityBo.to(entityBo));
        assertNotNull(entity.getId());

        OlePatronDocument olePatronBo = new OlePatronDocument();
       olePatronBo.setEntity(entityBo);
        olePatronBo.setBarcode("mockBarcode");
        olePatronBo.setBorrowerType(oleBorrowerTypeId);
        OlePatronHelperService olePatronHelperService = new OlePatronHelperServiceImpl();
        olePatronBo.setEntity(olePatronHelperService.copyAndSaveEntityBo(olePatronBo));
        olePatronBo.setOlePatronId(olePatronBo.getEntity().getId());
        OlePatronDocument savedPatronBo = businessObjectService.save(olePatronBo);
        assertNotNull(savedPatronBo);
        assertNotNull(savedPatronBo.getOlePatronId());
        
        
    }
}

