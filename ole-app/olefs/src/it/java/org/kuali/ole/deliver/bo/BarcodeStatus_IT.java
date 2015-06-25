/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class BarcodeStatus_IT extends KFSTestCaseBase {

    
    private BusinessObjectService boService;

     @Test
     @Transactional
     public void testSave() {
         BarcodeStatus barcodeStatus = new BarcodeStatus();
         barcodeStatus.setBarcodeStatusCode("mockCode");
         barcodeStatus.setBarcodeStatusName("mockName");
         barcodeStatus.setActive(true);
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(barcodeStatus);
         BarcodeStatus barcodeStatuseService = boService.findBySinglePrimaryKey(BarcodeStatus.class,barcodeStatus.getBarcodeStatusId());
         assertEquals("mockCode",barcodeStatuseService.getBarcodeStatusCode());
         assertEquals("mockName",barcodeStatuseService.getBarcodeStatusName());
    }

     @Test
     @Transactional
     public void testSearch(){
         BarcodeStatus barcodeStatus = new BarcodeStatus();
         barcodeStatus.setBarcodeStatusCode("mockCode");
         barcodeStatus.setBarcodeStatusName("mockName");
         barcodeStatus.setActive(true);
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(barcodeStatus);
         BarcodeStatus barcodeStatuseService = boService.findBySinglePrimaryKey(BarcodeStatus.class,barcodeStatus.getBarcodeStatusId());
         assertEquals("mockCode",barcodeStatuseService.getBarcodeStatusCode());
         assertEquals("mockName",barcodeStatuseService.getBarcodeStatusName());
    }

}
