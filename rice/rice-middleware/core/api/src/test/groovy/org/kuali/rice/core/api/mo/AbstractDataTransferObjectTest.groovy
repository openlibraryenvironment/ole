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
package org.kuali.rice.core.api.mo

import javax.xml.bind.JAXBContext
import org.apache.commons.lang.SerializationUtils
import org.junit.Test
import org.kuali.rice.core.api.util.io.SerializationUtils
import org.apache.commons.lang.SerializationUtils

/**
 * This test verifies that AbstractDataTransferObjects work the way they are expected to work in terms of JAXB
 * marshalling/unmarshalling and standard java serialization/deserialization.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class AbstractDataTransferObjectTest {

    private final shouldFail = new GroovyTestCase().&shouldFail

	private static final String XML_WITH_FUTURE_ELEMENTS = """
<sampleDataTransferObject xmlns="http://rice.kuali.org/core/v2_0">
  <name>myName</name>
  <values>
    <value>value1</value>
    <value>value2</value>
  </values>
  <attributes>
    <entry key="attribute1">attribute1Value</entry>
    <entry key="attribute2">attribute2Value</entry>
  </attributes>
  <theseAreSomeFutureElements>which shouldn't get included in the unmarshalled version</theseAreSomeFutureElements>
</sampleDataTransferObject>
    """
    
    private static final JAXBContext JAXB = JAXBContext.newInstance(SampleDataTransferObject.class)

    private SampleDataTransferObject createDto() {
        List<String> values = [ "value1", "value2" ]
        Map<String> attributes = [ attribute1:"attribute1Value", attribute2:"attribute2Value" ]
        return new SampleDataTransferObject("myName", values, attributes)
    }

    @Test
    void test_futureElements_serialization() {
        SampleDataTransferObject dto = createDto()
        byte[] serializedDto = SerializationUtils.serialize(dto)
        SampleDataTransferObject deserializedDto = SerializationUtils.deserialize(serializedDto)
        assert dto == deserializedDto
        assert deserializedDto._futureElements == null
    }

    /**
     * Tests that XML from jaxb that includes "_futureElements" does not affect serialization
     */
    @Test
    void test_futureElements_serialization_withJaxb() {
        SampleDataTransferObject dto = createDto()
        assert dto._futureElements == null
        SampleDataTransferObject unmarshalledDto = JAXB.createUnmarshaller().unmarshal(new StringReader(XML_WITH_FUTURE_ELEMENTS))
        assert dto == unmarshalledDto
        assert unmarshalledDto._futureElements != null

        byte[] serializedDto = SerializationUtils.serialize(unmarshalledDto)
        // the serialization clears out the future elements so it should be null here
        assert unmarshalledDto._futureElements == null
        SampleDataTransferObject deserializedDto = SerializationUtils.deserialize(serializedDto)
        assert dto == deserializedDto
        assert deserializedDto._futureElements == null
    }

    /**
     * Tests serializing an object, deserializing it, and then reserializing it.  Verifies that the synchronization
     * mutex inside of the AbstractDataTransferObject is restored successfully on a read.
     */
    @Test
    void test_serialize_deserialize_serialize() {
        SampleDataTransferObject dto = createDto()
        byte[] serialized = SerializationUtils.serialize(dto)
        SampleDataTransferObject deserializedDto = SerializationUtils.deserialize(serialized)
        assert dto == deserializedDto
        byte[] serializedAgain = SerializationUtils.serialize(deserializedDto)
        assert serialized == serializedAgain
        SampleDataTransferObject deserializedAgainDto = SerializationUtils.deserialize(serializedAgain)
        assert deserializedDto == deserializedAgainDto
    }

    @Test
    void test_immutableList() {
        SampleDataTransferObject dto = createDto()
        StringWriter stringWriter = new StringWriter()
        JAXB.createMarshaller().marshal(dto, stringWriter)
        SampleDataTransferObject unmarshalledDto = JAXB.createUnmarshaller().unmarshal(new StringReader(stringWriter.toString()))
        assert unmarshalledDto.values.size() == 2
        shouldFail(UnsupportedOperationException.class) {
            unmarshalledDto.values.add("value3")
        }
    }

    @Test
    void test_immutableMap() {
        SampleDataTransferObject dto = createDto()
        StringWriter stringWriter = new StringWriter()
        JAXB.createMarshaller().marshal(dto, stringWriter)
        SampleDataTransferObject unmarshalledDto = JAXB.createUnmarshaller().unmarshal(new StringReader(stringWriter.toString()))
        assert unmarshalledDto.attributes.size() == 2
        shouldFail(UnsupportedOperationException.class) {
            unmarshalledDto.attributes.put("attribute3", "attributeValue3")
        }
    }


}
