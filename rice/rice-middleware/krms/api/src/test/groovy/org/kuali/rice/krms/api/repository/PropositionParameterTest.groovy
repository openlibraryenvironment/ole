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
package org.kuali.rice.krms.api.repository

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Test
import org.junit.Assert
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;


/**
 * This class tests out the buiding of a PropositionParameter object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class PropositionParameterTest {
	
	private static final String ID = "1001"
	private static final String PROP_ID = "202"
	private static final String VALUE = "campusCode"
	private static final String PARAMETER_TYPE_C = "C"   // Constant
	private static final String PARAMETER_TYPE_T = "T"	 // Term
	private static final String PARAMETER_TYPE_F = "F"   // Function
	private static final String PARAMETER_TYPE_BAD = "X"  // some invalid value
	
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	private static final String EXPECTED_XML = """
		<PropositionParameter xmlns="http://rice.kuali.org/krms/v2_0">
			<id>1001</id>
			<propId>202</propId>
			<value>campusCode</value>
			<parameterType>C</parameterType>
			<sequenceNumber>1</sequenceNumber>
		</PropositionParameter>
	"""

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		PropositionParameter.Builder.create(null, null, null, null, null)
	}
	
	@Test
	void test_Builder_create_null_id() {
        // null id is legit
		PropositionParameter.Builder.create(null, PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_id() {
		PropositionParameter.Builder.create("", PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_id() {
		PropositionParameter.Builder.create("    ", PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test
	void test_Builder_create_null_prop_id() {
        // null prop id is legit
		PropositionParameter.Builder.create(ID, null, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_prop_id() {
		PropositionParameter.Builder.create(ID, "", VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_prop_id() {
		PropositionParameter.Builder.create(ID, "   ", VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test
	void test_Builder_create_succeed_null_value() {
		PropositionParameter.Builder.create(ID, PROP_ID, null, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_value() {
		PropositionParameter.Builder.create(ID, PROP_ID, "", PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_value() {
		PropositionParameter.Builder.create(ID, PROP_ID, "    ", PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_parameter_type() {
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, null, SEQUENCE_NUMBER_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_parameter_type() {
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, "", SEQUENCE_NUMBER_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_parameter_type() {
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, "	  ", SEQUENCE_NUMBER_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_invalid_parameter_type() {
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, PARAMETER_TYPE_BAD, SEQUENCE_NUMBER_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_sequence_number() {
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, PARAMETER_TYPE_C, null)
	}
	
	@Test
	void test_create_only_required(){
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1)
	}

	@Test
	void test_create_and_build_only_required(){
		PropositionParameter.Builder.create(ID, PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1).build()
	}

	@Test
	public void testXmlMarshaling() {
      PropositionParameter myParameter = PropositionParameter.Builder.create(ID, PROP_ID, VALUE, PARAMETER_TYPE_C, SEQUENCE_NUMBER_1).build()
	  JAXBContext jc = JAXBContext.newInstance(PropositionParameter.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()
	  marshaller.marshal(myParameter, sw)
	  String xml = sw.toString()
  
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
	  Assert.assertEquals(expected, actual)
	}
  
  @Test
  public void testXmlUnmarshal() {
    JAXBContext jc = JAXBContext.newInstance(PropositionParameter.class)
    Unmarshaller unmarshaller = jc.createUnmarshaller();
    PropositionParameter myParameter = (PropositionParameter) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
    Assert.assertEquals(ID, myParameter.id)
    Assert.assertEquals(PROP_ID, myParameter.propId)
	Assert.assertEquals(VALUE, myParameter.value)
	Assert.assertEquals(PARAMETER_TYPE_C, myParameter.parameterType)
	Assert.assertEquals(SEQUENCE_NUMBER_1, myParameter.sequenceNumber)
  }
}
