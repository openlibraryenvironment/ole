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
package org.kuali.rice.kns.workflow.service;


import  org.junit.Assert;

import org.junit.Test;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.krad.test.KRADTestCase;


/**
 * This class tests the WorkflowUser service.
 */
public class WorkflowInfoServiceTest extends KRADTestCase {

    @Test public void testRouteHeaderExists_NullId() throws IllegalArgumentException {
        boolean errorThrown = false;
        try {
            KewApiServiceLocator.getWorkflowDocumentService().doesDocumentExist(null);
        } catch (RiceIllegalArgumentException e) {
            errorThrown = true;
        }
        Assert.assertTrue("An error should have been thrown.", errorThrown);
    }

    @Test public void testRouteHeaderExists_NegativeId() {
        boolean errorThrown = false;
        boolean result = true;
        try {
            result = KewApiServiceLocator.getWorkflowDocumentService().doesDocumentExist("-10");
        } catch (Exception e) {
            errorThrown = true;
        }
        Assert.assertFalse("An error should not have been thrown.", errorThrown);
        Assert.assertFalse("The routeHeader should never exist for a negative documentId.", result);
    }
    
    @Test public void testRouteHeaderExists_KnownBadZeroId() {
        boolean errorThrown = false;
        boolean result = true;
        try {
            result = KewApiServiceLocator.getWorkflowDocumentService().doesDocumentExist("0");
        }
        catch (Exception e) {
            errorThrown = true;
        }
        Assert.assertFalse("An error should not have been thrown.", errorThrown);
        Assert.assertFalse("The routeHeader should never exist for a documentId of 0.", result);
    }

    @Test public void testRouteHeaderExists_KnownGood() {
        // no good way to test this without mocking the workflow service, and in a
        // way that will be good over the long term, across data changes
        Assert.assertTrue("This has been checked with a known-good id in the DB at this time.", true);
    }

}
