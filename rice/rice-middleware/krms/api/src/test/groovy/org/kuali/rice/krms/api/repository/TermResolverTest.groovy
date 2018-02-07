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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;


/**
 * This class tests out the buiding of a KrmsTypeAttribute object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class TermResolverTest {
	
	private static final String NAMESPACE = "KRMS_UNIT_TEST"
	
	private static final String ID="123TERMRESOLVER"
	private static final String NAMESPACE_CODE="foo-namespace"
	private static final String NAME="termResolver-name"
    private static final String TYPE_ID="1234TYPE"
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	private static final String TERM_SPEC_NAMESPACE ="foo-namespace"
	private static final String TERM_SPEC_NAME="termSpec-name"
	private static final String TERM_SPEC_TYPE="term.spec.Type"
	private static final String ATTR_ID="123ATTR"
	private static final String ATTR_NAME="attrName"
	private static final String ATTR_DEF_ID ="123ATTR_DEF"
	private static final String ATTR_VALUE="attrValue"
	private static final String PARAM_NAME="paramName"
	
	private static final String EXPECTED_XML = """
	<termResolverDefinition xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/krms/v2_0">
    <id>123TERMRESOLVER</id>
    <name>termResolver-name</name>
    <namespace>foo-namespace</namespace>
    <typeId>1234TYPE</typeId>
    <output>
        <id>123TERMRESOLVER</id>
        <name>termSpec-name</name>
        <namespace>foo-namespace</namespace>
        <type>term.spec.Type</type>
        <categories>
        </categories>
    </output>
    <prerequisites>
        <termSpecificationDefinition>
            <id>PREREQ123TERMRESOLVER</id>
            <name>PREREQtermSpec-name</name>
            <namespace>foo-namespace</namespace>
            <type>PREREQterm.spec.Type</type>
            <categories>
            </categories>
        </termSpecificationDefinition>
    </prerequisites>
    <attributes>
        <ns2:entry key="attrName">attrValue</ns2:entry>
    </attributes>
    <parameterNames>
        <parameterName>paramName1</parameterName>
        <parameterName>paramName2</parameterName>
    </parameterNames>
</termResolverDefinition>
"""
	
	@Test
	public void testXmlMarshaling() {
		// create(String termSpecificationId, String contextId, String name, String type)
		TermSpecificationDefinition.Builder output = TermSpecificationDefinition.Builder.create(ID, TERM_SPEC_NAME, TERM_SPEC_NAMESPACE, TERM_SPEC_TYPE);
		
		TermSpecificationDefinition.Builder prereq = TermSpecificationDefinition.Builder.create("PREREQ" + ID, "PREREQ" + TERM_SPEC_NAME, TERM_SPEC_NAMESPACE, "PREREQ" + TERM_SPEC_TYPE);
				
		/*
		public Builder create(String id,
		   String namespace,
		   String name,
		   String typeId,
		   TermSpecificationDefinition.Builder output,
		   List<TermSpecificationDefinition.Builder> prerequisites,
		   Map<String, String> attributes,
		   List<String> parameterNames) {
*/
		java.util.Set<TermSpecificationDefinition.Builder> prereqs = [prereq];
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(ATTR_NAME, ATTR_VALUE);
		java.util.Set<String> params = [PARAM_NAME+"1",PARAM_NAME+2];
		
		TermResolverDefinition termResolver = TermResolverDefinition.Builder.create(ID, NAMESPACE_CODE, NAME, TYPE_ID, output, prereqs, attributes, params).build()
				
		JAXBContext jc = JAXBContext.newInstance(TermResolverDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(termResolver, sw)
		String xml = sw.toString()
		
		print xml;

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(expected, actual)
	}

	// TODO: test builder validations, etc

}
