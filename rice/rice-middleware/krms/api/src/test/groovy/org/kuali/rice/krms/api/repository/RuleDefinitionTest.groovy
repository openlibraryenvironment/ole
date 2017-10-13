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

import java.util.List

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterContract
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;


/**
 * This is a description of what this class does - dseibert don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class RuleDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_TEST"
	private static final String RULE_ID_1 = "RULEID001"
	private static final String RULE_NAME = "Rule1"
	private static final String PROP_ID_1 = "PROP-001"
	private static final String ACTION_ID_1 = "ACTIONID01"

	private static final String PROP_DESCRIPTION = "is Campus Bloomington"
	private static final String PROPOSITION_TYPE_CD_S = "S"
	private static final List<PropositionParameter.Builder> PARM_LIST_1 = createPropositionParametersSet1()

	private static final String AGENDA_ID = "500Agenda"
	private static final String AGENDA_ITEM_ID_1 = "AgendaItem1"
	private static final String SUB_AGENDA_1= "SubAgenda1"

	private static final String SMALL_RULE = """
		<rule xmlns="http://rice.kuali.org/krms/v2_0">
			<id>RULEID001</id>
			<name>Rule1</name>
			<namespace>KRMS_TEST</namespace>
			<proposition>
				<id>PROP-001</id>
				<description>is Campus Bloomington</description>
                <ruleId>RULEID001</ruleId>
        		<propositionTypeCode>S</propositionTypeCode>
        		<parameters>
        			<parameter>
            			<id>1000</id>
            			<propId>PROP-001</propId>
            			<value>campusCode</value>
            			<parameterType>T</parameterType>
            			<sequenceNumber>0</sequenceNumber>
            			<versionNumber>1</versionNumber>
        			</parameter>
        			<parameter>
						<id>1001</id>
            			<propId>PROP-001</propId>
            			<value>BL</value>
            			<parameterType>C</parameterType>
            			<sequenceNumber>1</sequenceNumber>
            			<versionNumber>1</versionNumber>
        			</parameter>
        			<parameter>
						<id>1003</id>
            			<propId>PROP-001</propId>
            			<value>EQUALS</value>
            			<parameterType>F</parameterType>
            			<sequenceNumber>2</sequenceNumber>
            			<versionNumber>1</versionNumber>
        			</parameter>
        		</parameters>
    		</proposition>
       		<attributes></attributes>
		</rule>	"""

	private static final String FULL_RULE = """
		<rule xmlns="http://rice.kuali.org/krms/v2_0">
			<id>RULEID001</id>
			<name>Rule1</name>
			<namespace>KRMS_TEST</namespace>
			<proposition/>
       		<active>Y</active>
			<actions>
				<action></action>
				<action></action>
			</actions>
			<attributes>
			</attributes>
		</rule>		"""


	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_all_null() {
		RuleDefinition.Builder.create(null, null, null, null, null)
	}	

	@Test
	void test_RuleDefinition_Builder_create_null_rule_id() {
        // null id is legit
		RuleDefinition.Builder.create(null, RULE_NAME, NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_empty_rule_id() {
		RuleDefinition.Builder.create("", RULE_NAME, NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_whitespace_rule_id() {
		RuleDefinition.Builder.create("  	", RULE_NAME, NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_null_rule_name() {
		RuleDefinition.Builder.create(RULE_ID_1, null, NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_empty_rule_name() {
		RuleDefinition.Builder.create(RULE_ID_1, "", NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_whitespace_rule_name() {
		RuleDefinition.Builder.create(RULE_ID_1, "  	", NAMESPACE, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_null_rule_namespace() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, null, null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_empty_rule_namespace() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME,"", null, PROP_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_whitespace_rule_namespace() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, "  	", null, PROP_ID_1)
	}

	@Test
	void test_RuleDefinition_Builder_create_null_prop_id() {
        // null propId is legit
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, null)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_empty_prop_id() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, "")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_RuleDefinition_Builder_create_fail_whitespace_prop_id() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, "    ")
	}

	@Test
	void test_RuleDefinition_Builder_create() {
		RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, PROP_ID_1)
	}
	
	@Test
	void test_RuleDefinition_Builder_create_and_build() {
		PropositionDefinition.Builder myPropBuilder = PropositionDefinition.Builder.create(PROP_ID_1, PROPOSITION_TYPE_CD_S, RULE_ID_1, null, PARM_LIST_1)
		myPropBuilder.setDescription(PROP_DESCRIPTION)

		RuleDefinition.Builder myBuilder = RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, PROP_ID_1)
		myBuilder.setProposition myPropBuilder
		myBuilder.build()
	}
	
	@Test
	public void testXmlMarshaling_small_RuleDefinition() {
		PropositionDefinition.Builder myPropBuilder = PropositionDefinition.Builder.create(PROP_ID_1, PROPOSITION_TYPE_CD_S, RULE_ID_1, null, PARM_LIST_1)
		myPropBuilder.setDescription(PROP_DESCRIPTION)

		RuleDefinition.Builder myBuilder = RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, PROP_ID_1)
		myBuilder.setProposition myPropBuilder
		RuleDefinition myRule = myBuilder.build()

		JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myRule, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_RULE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_small_RuleDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  RuleDefinition myRule = (RuleDefinition) unmarshaller.unmarshal(new StringReader(SMALL_RULE))
	  Assert.assertEquals(RULE_ID_1, myRule.getId())
	  Assert.assertEquals(myRule.proposition.description, "is Campus Bloomington")
	}

	@Test
	// TODO: add attributes
	public void testXmlMarshaling_RuleDefinition_with_attributes() {
		PropositionDefinition.Builder myPropBuilder = PropositionDefinition.Builder.create(PROP_ID_1, PROPOSITION_TYPE_CD_S, RULE_ID_1, null, PARM_LIST_1)
		myPropBuilder.setDescription(PROP_DESCRIPTION)

		RuleDefinition.Builder myBuilder = RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, PROP_ID_1)
		myBuilder.setProposition myPropBuilder
		RuleDefinition myRule = myBuilder.build()

		JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myRule, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_RULE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	// TODO: add attributes
	public void testXmlUnmarshal_RuleDefinition_with_attributes() {
	  JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  RuleDefinition myRule = (RuleDefinition) unmarshaller.unmarshal(new StringReader(SMALL_RULE))
	  Assert.assertEquals(RULE_ID_1, myRule.getId())
	  Assert.assertEquals(myRule.proposition.description, "is Campus Bloomington")
	}

	@Test
	// TODO: add actions
	public void testXmlMarshaling_RuleDefinition_with_actions() {
		PropositionDefinition.Builder myPropBuilder = PropositionDefinition.Builder.create(PROP_ID_1, PROPOSITION_TYPE_CD_S, RULE_ID_1, null, PARM_LIST_1)
		myPropBuilder.setDescription(PROP_DESCRIPTION)

		RuleDefinition.Builder myBuilder = RuleDefinition.Builder.create(RULE_ID_1, RULE_NAME, NAMESPACE, null, PROP_ID_1)
		myBuilder.setProposition myPropBuilder
		RuleDefinition myRule = myBuilder.build()

		JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myRule, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_RULE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	// TODO: add actions
	public void testXmlUnmarshal_RuleDefinition_with_actions() {
	  JAXBContext jc = JAXBContext.newInstance(RuleDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  RuleDefinition myRule = (RuleDefinition) unmarshaller.unmarshal(new StringReader(SMALL_RULE))
	  Assert.assertEquals(RULE_ID_1, myRule.getId())
	  Assert.assertEquals(myRule.proposition.description, "is Campus Bloomington")
	}


	
	private static createPropositionParametersSet1(){
		List <PropositionParameter.Builder> propParms = new ArrayList <PropositionParameter.Builder> ()
		PropositionParameter.Builder ppBuilder1 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1000"
			def String propId = "2001"
			def String value = "campusCode"
			def String parameterType = "T"
			def Integer sequenceNumber = new Integer(0)
			def Long versionNumber = new Long(1)                 
                        public TermDefinition getTermValue () {
                            return null;
                        }
		})
		PropositionParameter.Builder ppBuilder2 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1001"
			def String propId = "2001"
			def String value = "BL"
			def String parameterType = "C"
			def Integer sequenceNumber = new Integer(1)
			def Long versionNumber = new Long(1)              
                        public TermDefinition getTermValue () {
                            return null;
                        }
		})
		PropositionParameter.Builder ppBuilder3 = PropositionParameter.Builder.create(new PropositionParameterContract() {
			def String id = "1003"
			def String propId = "2001"
			def String value = "EQUALS"
			def String parameterType = "F"
			def Integer sequenceNumber = new Integer(2)
			def Long versionNumber = new Long(1)              
                        public TermDefinition getTermValue () {
                            return null;
                        }
		})
		for ( ppb in [ppBuilder1, ppBuilder2, ppBuilder3]){
			propParms.add (ppb)
		}
		return propParms;
	}
  
}
