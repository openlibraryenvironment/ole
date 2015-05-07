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
package org.kuali.ole.coa.businessobject;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import static junit.framework.Assert.assertEquals;

public class OleStewardshipTest extends KFSTestCaseBase {

 private BusinessObjectService boService ;

    public void setup() throws Exception {
        super.setUp();
        changeCurrentUser(UserNameFixture.khuntley);
    }
    @Test
    public void testSave() {
         Integer stewardshipTypeCode = 500;
         String stewardshipTypeName = "Test";
         OleStewardship stewardshipRequirement = new OleStewardship();
         stewardshipRequirement.setStewardshipTypeCode(stewardshipTypeCode);
         stewardshipRequirement.setStewardshipTypeName(stewardshipTypeName);
         stewardshipRequirement.setActive(true);
         boService = KRADServiceLocator.getBusinessObjectService();
         boService.save(stewardshipRequirement);
         OleStewardship stewardshipRequirement1 = boService.findBySinglePrimaryKey(OleStewardship.class,stewardshipRequirement.getStewardshipTypeCode());
         assertEquals(stewardshipTypeCode,stewardshipRequirement1.getStewardshipTypeCode());
         assertEquals(stewardshipTypeName,stewardshipRequirement.getStewardshipTypeName());
    }
    @Test
    public void testSearch(){
        Integer stewardshipTypeCode = 500;
        String stewardshipTypeName = "Test";
        OleStewardship stewardshipRequirement = new OleStewardship();
        stewardshipRequirement.setStewardshipTypeCode(stewardshipTypeCode);
        stewardshipRequirement.setStewardshipTypeName(stewardshipTypeName);
        stewardshipRequirement.setActive(true);
        boService = KRADServiceLocator.getBusinessObjectService();
        boService.save(stewardshipRequirement);
        OleStewardship stewardshipRequirement1 = boService.findBySinglePrimaryKey(OleStewardship.class,stewardshipRequirement.getStewardshipTypeCode());
        assertEquals(stewardshipTypeCode,stewardshipRequirement1.getStewardshipTypeCode());
        assertEquals(stewardshipTypeName,stewardshipRequirement1.getStewardshipTypeName());
        boService.delete(stewardshipRequirement1);
    }
}
