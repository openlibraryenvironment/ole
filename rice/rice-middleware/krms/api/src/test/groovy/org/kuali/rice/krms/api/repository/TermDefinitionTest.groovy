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
import org.junit.Assert
import org.junit.Test
import org.kuali.rice.krms.api.repository.term.TermDefinition
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition

/**
 * This class tests out the buiding of a KrmsTypeAttribute object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class TermDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_UNIT_TEST"
	
	private static final String ID="123"
    private static final String DESCRIPTION="this is a joyful parameterized term"
	private static final String TERM_SPEC_NAMESPACE ="foo-namespace"
	private static final String TERM_SPEC_NAME="termSpec-name"
    private static final String TERM_SPEC_TYPE="term.spec.Type"
    private static final String TERM_SPEC_DESCRIPTION ="this is a happy term spec"
	private static final String PARAM_NAME="paramName"
	private static final String PARAM_VALUE="paramValue"
	
	private static final String EXPECTED_XML = """
	<termDefinition xmlns="http://rice.kuali.org/krms/v2_0">
	    <id>123DEF</id>
	    <specification>
	        <id>123SPEC</id>
	        <name>termSpec-name</name>
	        <namespace>foo-namespace</namespace>
	        <type>term.spec.Type</type>
	        <description>this is a happy term spec</description>
	        <categories>
	        </categories>
	    </specification>
	    <description>this is a joyful parameterized term</description>
	    <parameters>
	        <parameter>
	            <id>123PARAM</id>
	            <name>paramName</name>
	            <value>paramValue</value>
	        </parameter>
	    </parameters>
	</termDefinition>
	"""
	
	@Test
	public void testXmlMarshaling() {
		TermDefinition termDef = buildFullTermDefinition();


		JAXBContext jc = JAXBContext.newInstance(TermDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(termDef, sw)
		String xml = sw.toString()
		
		print xml;

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(expected, actual)
	}

    public static TermDefinition buildFullTermDefinition() {
        // create(String termSpecificationId, String contextId, String name, String type)
        TermSpecificationDefinition.Builder spec =
            TermSpecificationDefinition.Builder.create(ID + "SPEC", TERM_SPEC_NAME, TERM_SPEC_NAMESPACE, TERM_SPEC_TYPE);
        spec.setDescription(TERM_SPEC_DESCRIPTION);

        // create(String id, String name, String value) {
        TermParameterDefinition.Builder param = TermParameterDefinition.Builder.create(ID + "PARAM", ID + "SPEC", PARAM_NAME, PARAM_VALUE);

        // create(String id, TermSpecificationDefinition termSpecificationDefinition,
        //		Set<TermParameterDefinition> termParameters) {
        TermDefinition.Builder termDefBuilder = TermDefinition.Builder.create(ID + "DEF", spec, Collections.singletonList(param));
        termDefBuilder.setDescription(DESCRIPTION);

        TermDefinition termDef = termDefBuilder.build()
        return termDef
    }

    // TODO: test builder validations, etc

}
