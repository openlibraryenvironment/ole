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

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test

class RequestedActionsTest {

    private static final String EXPECTED_XML = """
        <requestedActions xmlns="http://rice.kuali.org/kew/v2_0">
            <completeRequested>false</completeRequested>
            <approveRequested>true</approveRequested>
            <acknowledgeRequested>false</acknowledgeRequested>
            <fyiRequested>true</fyiRequested>
        </requestedActions>
        """
    
    @Test
    void test_create_allTrue(){
        RequestedActions actions = RequestedActions.create(true, true, true, true)
        assert actions.isCompleteRequested()
        assert actions.isApproveRequested()
        assert actions.isAcknowledgeRequested()
        assert actions.isFyiRequested()

        assert actions.contains(ActionRequestType.COMPLETE)
        assert actions.contains(ActionRequestType.APPROVE)
        assert actions.contains(ActionRequestType.ACKNOWLEDGE)
        assert actions.contains(ActionRequestType.FYI)

        assert actions.getRequestedActions().size() == 4
    }

    @Test
    void test_create_allFalse(){
        RequestedActions actions = RequestedActions.create(false, false, false, false)
        assert !actions.isCompleteRequested()
        assert !actions.isApproveRequested()
        assert !actions.isAcknowledgeRequested()
        assert !actions.isFyiRequested()

        assert !actions.contains(ActionRequestType.COMPLETE)
        assert !actions.contains(ActionRequestType.APPROVE)
        assert !actions.contains(ActionRequestType.ACKNOWLEDGE)
        assert !actions.contains(ActionRequestType.FYI)

        assert actions.getRequestedActions().isEmpty()
    }

    @Test
    void test_equals() {
        assert RequestedActions.create(true, true, true, true) == RequestedActions.create(true, true, true, true)
        assert RequestedActions.create(false, false, false, false) == RequestedActions.create(false, false, false, false)
        assert RequestedActions.create(true, false, true, false) == RequestedActions.create(true, false, true, false)
        assert RequestedActions.create(true, true, true, true) == RequestedActions.create(true, true, true, true)
        assert RequestedActions.create(true, false, true, false) != RequestedActions.create(false, true, false, true)
    }
    
    @Test(expected = IllegalArgumentException.class)
    void test_contains_null(){
        RequestedActions.create(true, true, true, true).contains(null)
    }

    @Test
    void test_contains() {
        RequestedActions actions = RequestedActions.create(true, false, true, false)
        assert actions.contains(ActionRequestType.COMPLETE)
        assert !actions.contains(ActionRequestType.APPROVE)
        assert actions.contains(ActionRequestType.ACKNOWLEDGE)
        assert !actions.contains(ActionRequestType.FYI)
    }

    @Test
    void test_getRequestedActions() {
        RequestedActions actions = RequestedActions.create(true, false, true, false)
        assert actions.getRequestedActions().size() == 2
        assert actions.getRequestedActions().contains(ActionRequestType.COMPLETE)
        assert actions.getRequestedActions().contains(ActionRequestType.ACKNOWLEDGE)
    }

    @Test
    public void testXmlMarshaling() {
        RequestedActions actions = RequestedActions.create(false, true, false, true)
        JAXBContext jc = JAXBContext.newInstance(RequestedActions.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(actions, sw)
        String xml = sw.toString()
        print xml

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        RequestedActions actual = (RequestedActions)unmarshaller.unmarshal(new StringReader(xml))
        RequestedActions expected = (RequestedActions)unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
        Assert.assertEquals(expected, actual)
    }

}
