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
import org.kuali.ole.KualiTestBase;
import org.kuali.ole.coa.businessobject.OrganizationReversion;
import org.kuali.ole.coa.service.OrganizationReversionService;
import org.kuali.ole.sys.context.SpringContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * This class...
 */
public class OrganizationReversionTestCase extends KualiTestBase {

    @Test
    public void testGetByPrimaryKey() throws Exception {
        OrganizationReversionService organizationReversionService = SpringContext.getBean(OrganizationReversionService.class);
        assertNotNull("Service shouldn't be null", organizationReversionService);

        Integer fiscalYear = new Integer("2004");

        OrganizationReversion notexist = organizationReversionService.getByPrimaryId(fiscalYear, "BL", "TEST");
        assertNull("BL-TEST org reversion shouldn't exist in table", notexist);

        OrganizationReversion exist = organizationReversionService.getByPrimaryId(fiscalYear, "BL", "PSY");
        assertNotNull("BL-PSY should exist in table", exist);

    }
}
