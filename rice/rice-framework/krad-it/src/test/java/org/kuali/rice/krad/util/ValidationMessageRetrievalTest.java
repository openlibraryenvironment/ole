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
package org.kuali.rice.krad.util;

import org.junit.Test;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.krad.test.KRADTestCase;
import org.springframework.util.AutoPopulatingList;

import static org.junit.Assert.assertEquals;

/**
 * Test retrieval of validation messages that have been added to a {@link MessageMap}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@PerSuiteUnitTestData(
        value = @UnitTestData(
                order = {UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES},
                sqlFiles = {@UnitTestFile(filename = "classpath:testValidationMessages.sql", delimiter = ";")}))
public class ValidationMessageRetrievalTest extends KRADTestCase {

    private MessageMap messageMap;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        messageMap = new MessageMap();
    }

    /**
     * Test that message text is correctly retrieved for a validation message specified with only the
     * message key
     */
    @Test
    public void testRetrieveMessage_keyOnly() throws Exception {
        messageMap.putError("field1", "testErrorKey");

        AutoPopulatingList<ErrorMessage> fieldErrors = messageMap.getErrorMessagesForProperty("field1");
        assertEquals("Incorrect number of messages for field1", 1, fieldErrors.size());

        ErrorMessage message = fieldErrors.get(0);
        String messageText = KRADUtils.getMessageText(message, true);
        assertEquals("Message for field1 is not correct", "Error on field1", messageText);
    }

    /**
     * Test that message text is correctly retrieved for a validation message specified by namespace and
     * message key
     */
    @Test
    public void testRetrieveMessage_namespaceKey() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setNamespaceCode("KR-NS");
        errorMessage.setErrorKey("testErrorKey");

        messageMap.putError("field1", errorMessage);

        AutoPopulatingList<ErrorMessage> fieldErrors = messageMap.getErrorMessagesForProperty("field1");
        assertEquals("Incorrect number of messages for field1", 1, fieldErrors.size());

        ErrorMessage message = fieldErrors.get(0);
        String messageText = KRADUtils.getMessageText(message, true);
        assertEquals("Message for field1 is not correct", "Error on field1", messageText);
    }

    /**
     * Test that message text is correctly retrieved for a validation message specified by namespace,
     * component, and message key
     */
    @Test
    public void testRetrieveMessage_componentKey() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setNamespaceCode("KR-NS");
        errorMessage.setComponentCode("GeneralGroup");
        errorMessage.setErrorKey("testErrorKey");

        messageMap.putError("field1", errorMessage);

        AutoPopulatingList<ErrorMessage> fieldErrors = messageMap.getErrorMessagesForProperty("field1");
        assertEquals("Incorrect number of messages for field1", 1, fieldErrors.size());

        ErrorMessage message = fieldErrors.get(0);
        String messageText = KRADUtils.getMessageText(message, true);
        assertEquals("Message for field1 is not correct", "Error on field1", messageText);
    }
}
