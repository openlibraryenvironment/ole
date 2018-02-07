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
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition.Builder;


/**
 * This class tests out the buiding of a KrmsAttributeDefinition object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class TermSpecificationDefinitionTest {

	private static final String TERM_SPEC_ID_1 = "1001"
	private static final String TERM_SPEC_NAMESPACE = "KRMS_TEST"
	private static final String TERM_SPEC_NAME = "total"
	private static final String TERM_SPEC_TYPE = "java.math.BigDecimal"
    private static final String TERM_SPEC_DESCRIPTION = "the total number of widgets sold in the last quarter"

	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	private static final String EXPECTED_XML = """
		<termSpecification xmlns="http://rice.kuali.org/krms/v2_0">
			<id>1001</id>
			<name>total</name>
			<namespace>KRMS_TEST</namespace>
			<type>java.math.BigDecimal</type>
			<description>the total number of widgets sold in the last quarter</description>
			<categories>
			</categories>
		</termSpecification>
	"""

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		TermSpecificationDefinition.Builder.create(null, null, null, null);
	}

	//	TermSpecificationDefinition.Builder.create(TERM_SPEC_ID, TERM_SPEC_NAMESPACE, TERM_SPEC_NAME, TERM_SPEC_TYPE);

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_context_id() {
		TermSpecificationDefinition.Builder.create(TERM_SPEC_ID_1, TERM_SPEC_NAME, null, TERM_SPEC_TYPE);
	}

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_id() {
		TermSpecificationDefinition.Builder.create(TERM_SPEC_ID_1, TERM_SPEC_NAME, null, TERM_SPEC_TYPE);
	}

	@Test
	void test_Builder_create_success_null_id() {
		TermSpecificationDefinition termSpecDef =
				TermSpecificationDefinition.Builder.create(null, TERM_SPEC_NAME, TERM_SPEC_NAMESPACE, TERM_SPEC_TYPE).build();
		Assert.assertEquals(null, termSpecDef.getId())
		Assert.assertEquals(TERM_SPEC_NAMESPACE, termSpecDef.getNamespace())
		Assert.assertEquals(TERM_SPEC_NAME, termSpecDef.getName())
		Assert.assertEquals(TERM_SPEC_TYPE, termSpecDef.getType())
	}

	@Test
	public void testXmlMarshaling() {
        TermSpecificationDefinition termSpecDef = buildFullTermSpecificationDefinition();

		JAXBContext jc = JAXBContext.newInstance(TermSpecificationDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(termSpecDef, sw)
		String xml = sw.toString()

		print xml;
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(expected, actual)
	}

    public static TermSpecificationDefinition buildFullTermSpecificationDefinition() {
        Builder termSpecDefBuilder =
        Builder.create(TERM_SPEC_ID_1, TERM_SPEC_NAME, TERM_SPEC_NAMESPACE, TERM_SPEC_TYPE);
        termSpecDefBuilder.setDescription(TERM_SPEC_DESCRIPTION);

        TermSpecificationDefinition termSpecDef = termSpecDefBuilder.build()
        return termSpecDef
    }

    // TODO: more complete testing
	
}
