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
package org.kuali.rice.kew.api.actionlist

import org.junit.Assert
import org.junit.Test

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

class DisplayParametersTest {
    private static final Integer FRAME_HEIGHT = 161803399;
    
    private static final String EXPECTED_XML = """
        <displayParameters xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kew/v2_0">
            <frameHeight>${FRAME_HEIGHT}</frameHeight>
        </displayParameters>
        """
    
    @Test
    void happy_path(){
        DisplayParameters.Builder.create(FRAME_HEIGHT)
    }
    
    @Test(expected = IllegalArgumentException.class)
    void test_Builder_create_fail_null_contract(){
        DisplayParametersContract contract = null
        DisplayParameters.Builder.create(contract)
    }
    
    @Test(expected = IllegalArgumentException.class)
    void test_Builder_create_fail_null_frameHeight(){
        Integer frameHeight = null
        DisplayParameters.Builder.create(frameHeight)
    }
    
    @Test
    void test_copy(){
        def o1b = DisplayParameters.Builder.create(FRAME_HEIGHT)
        def o1 = o1b.build()
        def o2 = DisplayParameters.Builder.create(o1).build()
        Assert.assertEquals(o1.getFrameHeight(), o2.getFrameHeight())
    }
    
    @Test
    public void testXmlMarshaling() {
        DisplayParameters displayParameters = buildDisplayParameters()
        JAXBContext jc = JAXBContext.newInstance(DisplayParameters.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(displayParameters, sw)
        String xml = sw.toString()
        print xml
        
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        DisplayParameters actual = (DisplayParameters)unmarshaller.unmarshal(new StringReader(xml))
        DisplayParameters expected = (DisplayParameters)unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
        Assert.assertEquals(expected.getFrameHeight(), actual.getFrameHeight())
    }

    @Test
    public void testXmlUnmarshal() {
        JAXBContext jc = JAXBContext.newInstance(DisplayParameters.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        DisplayParameters displayParameters = (DisplayParameters) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
        Assert.assertEquals(FRAME_HEIGHT, displayParameters.frameHeight)
    }
    
    public static DisplayParameters buildDisplayParameters() {
        DisplayParameters displayParameters = DisplayParameters.Builder.create(FRAME_HEIGHT).build()
        return displayParameters
    }
}
