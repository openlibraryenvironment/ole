/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.responsibility;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.kuali.rice.kew.responsibility.service.ResponsibilityIdService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.test.KEWTestCase;

/**
 * Test the Responsibility ID Service
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ResponsibilityIdServiceTest extends KEWTestCase {
    @Test
    public void testResponsibilityIdService() {
        ResponsibilityIdService idService = (ResponsibilityIdService)KEWServiceLocator.getService("enResponsibilityIdService");
        final String firstId = idService.getNewResponsibilityId();
        assertNotNull("Did not get a responsibility id", firstId);
        final String secondId = idService.getNewResponsibilityId();
        assertNotNull("Did not get a second responsibility id", secondId);
        assertTrue("Both responsibilityIds are equal", firstId.compareTo(secondId) != 0);
    }
}
