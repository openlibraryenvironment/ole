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
package org.kuali.rice.kew.actionitem

import org.junit.Test
import org.kuali.rice.kew.api.action.ActionRequestType
import static org.junit.Assert.assertTrue
import org.kuali.rice.core.api.delegation.DelegationType
import static org.junit.Assert.assertEquals

/**
 * Tests action item comparison
 */
class ActionItemComparatorTest {
    @Test
    void testApproveAndCompleteHaveSamePriority() {
        def a1 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code)
        def a2 = new ActionItem(actionRequestCd: ActionRequestType.COMPLETE.code)
        assertEquals(0, new ActionItemComparator().compare(a1, a2))
    }

    @Test
    void testCompareActionCode() {
        def a1 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code)
        def a2 = new ActionItem(actionRequestCd: ActionRequestType.ACKNOWLEDGE.code)
        assertEquals(0, new ActionItemComparator().compare(a1, a1))
        assertTrue(new ActionItemComparator().compare(a1, a2) > 0)
    }

    @Test
    void testCompareRecipientType() {
        def a1 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                principalId: "test principal id")
        def a2 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                roleName: "test role name")
        assertEquals(0, new ActionItemComparator().compare(a1, a1))
        assertTrue(new ActionItemComparator().compare(a1, a2) > 0)
    }

    @Test
    void testCompareDelegationType() {
        def a1 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                roleName: "test role name",
                                delegationType: DelegationType.PRIMARY)
        def a2 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                roleName: "test role name",
                                delegationType: DelegationType.SECONDARY)
        assertEquals(0, new ActionItemComparator().compare(a1, a1))
        assertTrue(new ActionItemComparator().compare(a1, a2) > 0)
    }

    @Test
    void testCompareNullDelegationType() {
        def a1 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                roleName: "test role name")
        def a2 = new ActionItem(actionRequestCd: ActionRequestType.APPROVE.code,
                                roleName: "test role name")
        assertEquals(0, new ActionItemComparator().compare(a1, a2))
    }
}
