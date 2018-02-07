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

import org.junit.Assert
import org.junit.Test

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

class ActionSetTest {
    private static final String ACTION = "action1";
    
    private static final String EXPECTED_XML = """
        <actionSet xmlns="http://rice.kuali.org/kew/v2_0">
            <actionSetList>${ACTION}</actionSetList>
        </actionSet>
        """

    private static final String EXPECTED_XML2 = """
        <actionSet xmlns="http://rice.kuali.org/kew/v2_0">
        </actionSet>
        """

    @Test
    void happy_path(){
        ActionSet.Builder.create()
    }
    
    @Test(expected = IllegalArgumentException.class)
    void test_Builder_create_fail_null_contract(){
        ActionSetContract contract = null
        ActionSet.Builder.create(contract)
    }
    
    @Test
    void test_copy(){
        def o1b = ActionSet.Builder.create()
        def o1 = o1b.build()
        def o2 = ActionSet.Builder.create(o1).build()
        Assert.assertEquals(o1.actionSetList, o2.actionSetList)
    }
    
    @Test
    public void testXmlMarshaling() {
        ActionSet actionSet = buildActionSet()
        JAXBContext jc = JAXBContext.newInstance(ActionSet.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(actionSet, sw)
        String xml = sw.toString()
        print xml
        
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ActionSet actual = (ActionSet)unmarshaller.unmarshal(new StringReader(xml))
        ActionSet expected = (ActionSet)unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
        Assert.assertEquals(expected.actionSetList, actual.actionSetList)
    }

    @Test
    public void testXmlMarshalingEmptyActionSet() {
        ActionSet actionSet =  ActionSet.Builder.create().build()
        JAXBContext jc = JAXBContext.newInstance(ActionSet.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(actionSet, sw)
        String xml = sw.toString()
        print xml

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ActionSet actual = (ActionSet)unmarshaller.unmarshal(new StringReader(xml))
        ActionSet expected = (ActionSet)unmarshaller.unmarshal(new StringReader(EXPECTED_XML2))
        Assert.assertEquals(expected.actionSetList, actual.actionSetList)
    }

    @Test
    public void testXmlUnmarshal() {
        JAXBContext jc = JAXBContext.newInstance(ActionSet.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ActionSet actionSet = (ActionSet) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
        Assert.assertEquals(1, actionSet.actionSetList.size())
        Assert.assertEquals(ACTION, actionSet.actionSetList.get(0))
        Assert.assertTrue(actionSet.hasAction(ACTION))
    }

    @Test
    public void testXmlUnmarshalEmptyActionSet() {
        JAXBContext jc = JAXBContext.newInstance(ActionSet.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ActionSet actionSet = (ActionSet) unmarshaller.unmarshal(new StringReader(EXPECTED_XML2))
        Assert.assertFalse(actionSet.hasAction(ACTION))
    }
    
    public static ActionSet buildActionSet() {
        ActionSet actionSet = ActionSet.Builder.create().build()
        actionSet.addAction(ACTION);
        return actionSet
    }
}
