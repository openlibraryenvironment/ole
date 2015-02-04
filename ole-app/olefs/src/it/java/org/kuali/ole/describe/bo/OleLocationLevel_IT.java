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
package org.kuali.ole.describe.bo;

import org.junit.Test;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;


public class OleLocationLevel_IT extends SpringBaseTestCase {

    
    private BusinessObjectService boService;

     @Test
     @Transactional
     public void testSave() {
         OleLocationLevel oleLocationLevel = new OleLocationLevel();
         oleLocationLevel.setLevelId("5000");
         oleLocationLevel.setLevelCode("mockCode");
         oleLocationLevel.setLevelName("mockName");
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(oleLocationLevel);
         OleLocationLevel oleLocationLevelService = boService.findBySinglePrimaryKey(OleLocationLevel.class,oleLocationLevel.getLevelId());
         assertEquals("mockCode",oleLocationLevelService.getLevelCode());
         assertEquals("mockName",oleLocationLevelService.getLevelName());
    }

     @Test
     @Transactional
     public void testSearch(){
         OleLocationLevel oleLocationLevel = new OleLocationLevel();
         oleLocationLevel.setLevelId("5000");
         oleLocationLevel.setLevelCode("mockCode");
         oleLocationLevel.setLevelName("mockName");
         boService = KRADServiceLocator.getBusinessObjectService();
         OleLocationLevel oleLocationLevels=boService.save(oleLocationLevel);
         OleLocationLevel oleLocationLevelService = boService.findBySinglePrimaryKey(OleLocationLevel.class,oleLocationLevels.getLevelId());
         assertEquals("mockCode",oleLocationLevelService.getLevelCode());
         assertEquals("mockName",oleLocationLevelService.getLevelName());

     }

}
