/*
 * Copyright 2005-2006 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.coa.service;

import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.coa.businessobject.ObjectLevel;
import org.kuali.ole.coa.service.ObjectLevelService;
import org.kuali.ole.sys.context.SpringContext;

import static junit.framework.Assert.assertEquals;

/**
 * This class tests the ObjLevel service.
 */
public class ObjectLevelServiceTest extends KFSTestCaseBase {

    @Test
    public void testFindById() {
        ObjectLevel objectLevel = SpringContext.getBean(ObjectLevelService.class).getByPrimaryId("UA", "BASE");
        assertEquals("Object Level Code should be BASE", objectLevel.getFinancialObjectLevelCode(), "BASE");
    }
}
