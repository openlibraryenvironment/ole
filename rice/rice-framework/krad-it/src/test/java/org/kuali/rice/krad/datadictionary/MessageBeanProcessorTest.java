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
package org.kuali.rice.krad.datadictionary;

import org.junit.Test;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.test.TestDictionaryBean;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.test.data.PerSuiteUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestFile;
import org.kuali.rice.krad.test.KRADTestCase;
import org.kuali.rice.krad.test.KRADTestConstants;
import org.kuali.rice.krad.test.TestDictionaryConfig;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for org.kuali.rice.krad.datadictionary.MessageBeanProcessor
 *
 * <p>
 * Note these test methods are simply verifying (not assembling and invoking) because the actual
 * processing of external messages will be done by the creation of the data dictionary in the KRAD
 * base test case
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@PerSuiteUnitTestData(
        value = @UnitTestData(
                order = {UnitTestData.Type.SQL_STATEMENTS, UnitTestData.Type.SQL_FILES},
                sqlFiles = {@UnitTestFile(filename = "classpath:testExternalMessages.sql", delimiter = ";")}))
@TestDictionaryConfig(
        namespaceCode = "TEST",
        dataDictionaryFiles = "classpath:org/kuali/rice/krad/datadictionary/MessageProcessorTestBeans.xml")
public class MessageBeanProcessorTest extends KRADTestCase {

    /**
     * Verifies a message setup with component equal to a bean id and message key
     * a simple property path (non-nested) is picked up and inserted into the correct bean definition
     * and property
     */
    @Test
    public void testMatchBeanIdSimpleKey() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesSimpleProperty");

        assertEquals("Property1 was not overridden with external message", "ext p1 value", tObject.getProperty1());
        assertEquals("Property2 was not overridden with external message", "ext p2 value", tObject.getProperty2());
    }

    @Test
    public void testMatchExplicitKey() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesMessageKey");

        assertEquals("Property1 was not overridden with explicit message key (with default)", "ext key p1 value",
                tObject.getProperty1());
        assertEquals("Property2 was not overridden with explicit message key (without default)", "ext key p2 value",
                tObject.getProperty2());
    }

    @Test
    public void testMatchExplicitListKey() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesListMessageKey");

        assertEquals("List1 entry 2 was not overridden with explicit message key", "ext list key value",
                tObject.getList1().get(1));
    }

    @Test
    public void testMatchExplicitMapKey() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesMapMessageKey");

        assertEquals("Map1 key 'k2' was not overridden with explicit message key", "ext map key value",
                tObject.getMap1().get("k2"));
    }

    @Test
    public void testMessageExpressionMerge() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesExpressionMerge");

        assertEquals("Expression for property 1 was not merged", "Value '@{expr1}' is invalid",
                tObject.getExpressionGraph().get("property1"));
        assertEquals("Expression for property 2 was not merged", "The @{expr1} code should not equal @{expr2 + 3}",
                tObject.getExpressionGraph().get("property2"));

        tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesKeyExprMerge");

        assertEquals("Expression for property 1 was not merged with explicit message key",
                "Expr @{expr1} then expr @{expr2}", tObject.getExpressionGraph().get("property1"));
    }

    @Test
    public void testMessagesForListBean() throws Exception {
        List<KeyValue> keyValues = (List<KeyValue>) getTestDictionaryObject("TestMessagesOptions");

        assertEquals("First key value not overridden by message", "Ext Summer", keyValues.get(0).getValue());
        assertEquals("Second key value was overriden and should not be", "Fall", keyValues.get(1).getValue());
        assertEquals("Third key value not overridden by message", "Ext Spring", keyValues.get(2).getValue());
    }

    @Test
    public void testModuleResourcesMessages() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestMessagesResources");

        assertEquals("Property 1 value was not overridden with message resource", "Message for Property 1",
                tObject.getProperty1());
        assertEquals("Property 2 value with explicit key was not overridden with message resource",
                "Message for explicit message key", tObject.getProperty2());

        // get validation message by key
        String messageText = getMessageService().getMessageText(KRADTestConstants.TEST_NAMESPACE_CODE, "",
                "validation.test.error");
        assertEquals("Validation message text not correct", "Error found for {0}",
                messageText);
    }
    
    @Test
    public void testApplicationResourcesMessages() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestApplicationMessagesResources");

        assertEquals("Property 1 value was not overridden with message resource", "App Message for Property 1",
                tObject.getProperty1());
        assertEquals("Property 2 value with explicit key was not overridden with message resource",
                "App Message for explicit message key", tObject.getProperty2());

        // get validation message by key
        String messageText = getMessageService().getMessageText(KRADConstants.DEFAULT_NAMESPACE, "",
                "validation.test.error");
        assertEquals("Validation message text not correct", "App Error found for {0}",
                messageText);
    }

    /**
     * Verifies a message keys within an expression get replaced with the text
     */
    @Test
    public void testExpressionMessages() throws Exception {
        TestDictionaryBean tObject = (TestDictionaryBean) getTestDictionaryObject("TestExpressionMessages");

        assertEquals("Messages in expression for property 1 not replaced",
                "@{foo eq '1' ? 'ext key p1 value' : 'ext key p2 value'}", tObject.getExpressionGraph().get(
                "property1"));
    }

    public MessageService getMessageService() {
        return KRADServiceLocatorWeb.getMessageService();

    }

}
