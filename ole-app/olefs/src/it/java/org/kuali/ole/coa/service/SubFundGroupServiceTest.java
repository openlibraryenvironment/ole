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
import org.kuali.ole.coa.businessobject.SubFundGroup;
import org.kuali.ole.coa.service.SubFundGroupService;
import org.kuali.ole.sys.context.SpringContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * This class tests the subFundGroup service.
 */
public class SubFundGroupServiceTest extends KFSTestCaseBase {

    @Test
    public final void testGetByCode_knownCode() {
        // known-good code
        SubFundGroup subFundGroup = SpringContext.getBean(SubFundGroupService.class).getByPrimaryId("LOANFD");
        assertEquals("Known code does not produce expected name.", "LOAN FUNDS", subFundGroup.getSubFundGroupDescription());
    }

    @Test
    public final void testGetByCode_knownCode2() {
        // known-good code
        SubFundGroup subFundGroup = SpringContext.getBean(SubFundGroupService.class).getByPrimaryId("CLEAR");
        assertEquals("Known code does not produce expected name.", "CLEARING AND ROTATING FUNDS", subFundGroup.getSubFundGroupDescription());
    }

    @Test
    public final void testGetByCode_unknownCode() {
        // known-bad code
        SubFundGroup subFundGroup = SpringContext.getBean(SubFundGroupService.class).getByPrimaryId("SMELL");
        assertNull("Known-bad code does not produce expected null object.", subFundGroup);
    }

    @Test
    public final void testGetByChartAndAccount() {
        String chartCode = "BL";
        String accountNumber = "1031420";
        SubFundGroup subFundGroup = SpringContext.getBean(SubFundGroupService.class).getByChartAndAccount(chartCode, accountNumber);
        assertNotNull(subFundGroup);
        assertEquals("Foo", "GENFND", subFundGroup.getSubFundGroupCode());
    }

    @Test
    public final void testGetByName_knownName() {
        // TODO: commented out, because there is no equivalent to getByName on regular business objects
        // known-good name
        // subFundGroup = null;
        // subFundGroup = (subFundGroup) kualiCodeService.getByName(subFundGroup.class, "LOAN FUNDS");
        // assertEquals("Known code does not produce expected name.", "LOANFD", subFundGroup.getCode());
    }

    @Test
    public final void testGetByName_knownName2() {
        // TODO: commented out, because there is no equivalent to getByName on regular business objects
        // known-good name
        // subFundGroup = null;
        // subFundGroup = (subFundGroup) kualiCodeService.getByName(subFundGroup.class, "CLEARING AND ROTATING FUNDS");
        // assertEquals("Known code does not produce expected name.", "CLEAR", subFundGroup.getCode());
        // assertEquals("Known code's active indicator conversion failed.", true, subFundGroup.isActive());
        // assertEquals("Known code's wage indicator conversion failed.", false, subFundGroup.isWageIndicator());
    }

    @Test
    public final void testGetByName_unknownName() {
        // TODO: commented out, because there is no equivalent to getByName on regular business objects
        // known-bad name
        // subFundGroup = null;
        // subFundGroup = (subFundGroup) kualiCodeService.getByName(subFundGroup.class, "Smelly Cat");
        // assertNull("Known-bad name does not produce expected null object.", subFundGroup);
    }
}
