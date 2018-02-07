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
package org.kuali.rice.kew.api.action

import org.joda.time.DateTime
import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert

/**
 * Unit tests for the ActionItem DTO (the one in kew-api -- there is a bo named ActionItem in kew-impl too so beware!)
 */
class ActionItemTest {

    private static final String WITH_REMOVED_FIELDS_XML = """
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<actionItem xmlns="http://rice.kuali.org/kew/v2_0">
    <dateTimeAssigned/>
    <actionRequestCd>A</actionRequestCd>
    <actionRequestId>actnReqId123</actionRequestId>
    <documentId>docId123</documentId>
    <docLabel>docLabelFoo</docLabel>
    <docHandlerURL>http://dochandler.kuali.org/url</docHandlerURL>
    <docName>docNameFoo</docName>

    <!-- Removed fields ahead, will we still unmarshal without error? -->
    <dateAssignedString>Foo</dateAssignedString>
    <actionToTake>Bar</actionToTake>
    <actionItemIndex>999</actionItemIndex>

    <responsibilityId>respId123</responsibilityId>
    <principalId>prncplId123</principalId>
    <dateTimeAssignedValue>2012-08-15T09:48:46.631-07:00</dateTimeAssignedValue>
</actionItem>
        """

    /**
     * Prove that the changes in r22060 do not break version compatibility
     */
    @Test
    public void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), WITH_REMOVED_FIELDS_XML, ActionItem.class)
    }

    ActionItem create() {
        ActionItem.Builder actionItemBuilder =
            ActionItem.Builder.create("docId123", "A", "actnReqId123",
                    // Need to instantiate this with a long, otherwise a different Chronology is
                    // used and it won't be equal
                    new DateTime(1345049326631),
                "docLabelFoo", "http://dochandler.kuali.org/url", "docNameFoo", "respId123", "prncplId123");
        actionItemBuilder.build();
    }

}
