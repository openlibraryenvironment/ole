/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.impl.actionlist

import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationHandlerService
import org.junit.Before
import org.kuali.rice.kew.doctype.service.DocumentTypeService
import org.kuali.rice.kew.doctype.bo.DocumentType
import org.junit.Test
import org.kuali.rice.kew.actionlist.CustomActionListAttribute
import org.kuali.rice.kew.api.action.ActionItem
import org.kuali.rice.kew.api.action.ActionSet

import static org.junit.Assert.assertEquals
import org.kuali.rice.kew.api.action.ActionRequest
import org.kuali.rice.kew.api.action.ActionRequestType
import org.joda.time.DateTime

import static org.junit.Assert.assertNotNull

/**
 * Tests invoking ActionListCustomizationHandlerService
 * This test is pretty brittle, and relies on some assumptions about the implementation
 * to avoid a massive amount of mocking.
 * The original underlying motivation is to test that ActionSet DTOs are getting
 * empty actionSetList assigned as an empty map after unmarshalling.
 */
class ActionListCustomizationHandlerServiceImplTest {
    ActionListCustomizationHandlerServiceImpl handlerServiceImpl
    ActionListCustomizationHandlerService handlerService

    @Before
    void setup() {
        setupServiceUnderTest()
        stubService()
    }

    void setupServiceUnderTest() {
        handlerServiceImpl = new ActionListCustomizationHandlerServiceImpl()
        handlerService = handlerServiceImpl
    }

    void stubService() {
        // our attribute
        def testAttribute= [
            getLegalActions: { String principalId, ActionItem actionItem ->
                // empty action set
                ActionSet.Builder.create().build()
            },
            getDocHandlerDisplayParameters: { String principalId, ActionItem actionItem ->
                null
            }
        ] as CustomActionListAttribute
        handlerServiceImpl.setDocumentTypeService([
            findByName: { name ->
                [ getCustomActionListAttribute: {
                    testAttribute
                } ] as DocumentType
            }
        ] as DocumentTypeService)
    }

    @Test
    void testActionSetUnmarshall() {
        // need id on action item otherwise actioncustomization builder blows up
        ActionItem.Builder ai = ActionItem.Builder.create("docid", ActionRequestType.APPROVE.code, "arid", DateTime.now(), "doclabel", "", "docname", "respid", "princid")
        ai.setId("1234")
        def customizations = handlerService.customizeActionList("notblank", [ ai.build() ])
        assertEquals(1, customizations.size())
        assertNotNull(customizations[0].actionSet.actionSetList)
        assertEquals(0, customizations[0].actionSet.actionSetList.size())
    }
}
