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
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeRuleEntry;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeSubAgendaEntry;


/**
 * This is a description of what this class does - dseibert don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class AgendaTreeTest {
	
	private static final String AGENDA_ID = "500Agenda"
	private static final String AGENDA_ITEM_ID_1 = "AgendaItem1"
	private static final String AGENDA_ITEM_ID_2 = "AgendaItem2"
	private static final String AGENDA_ITEM_ID_3 = "AgendaItem3"
	private static final String AGENDA_ITEM_ID_4 = "AgendaItem4"
	private static final String AGENDA_ITEM_ID_5 = "AgendaItem5"
	private static final String AGENDA_ITEM_ID_6 = "AgendaItem6"
	private static final String AGENDA_ITEM_ID_7 = "AgendaItem7"
	private static final String RULE_ID_1 = "Rule1"
	private static final String RULE_ID_2 = "Rule2"
	private static final String RULE_ID_3 = "Rule3"
	private static final String RULE_ID_4 = "Rule4"
	private static final String RULE_ID_5 = "Rule5"
	private static final String RULE_ID_6 = "Rule6"
	private static final String RULE_ID_7 = "Rule7"
	private static final String SUB_AGENDA_1= "SubAgenda1"

	private static final String TINY_AGENDA_TREE = """
		<agendaTreeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaId>500Agenda</agendaId>
		</agendaTreeDefinition>		"""

	private static final String SINGLE_RULE_AGENDA_TREE = """
		<agendaTreeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaId>500Agenda</agendaId>
			<rule>
				<agendaItemId>AgendaItem1</agendaItemId>
				<ruleId>Rule1</ruleId>
			</rule>
		</agendaTreeDefinition>		"""

	private static final String SINGLE_NODE_MULTIPLE_RULE_AGENDA_TREE = """
		<agendaTreeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaId>500Agenda</agendaId>
			<rule>
				<agendaItemId>AgendaItem1</agendaItemId>
				<ruleId>Rule1</ruleId>
			</rule>
			<rule>
				<agendaItemId>AgendaItem2</agendaItemId>
				<ruleId>Rule2</ruleId>
			</rule>
			<subAgenda>
				<agendaItemId>AgendaItem3</agendaItemId>
				<subAgendaId>SubAgenda1</subAgendaId>
			</subAgenda>
		</agendaTreeDefinition>		"""
	/**
	 * evaluate Rule 1
	 * if True
	 * 		evaluate Rule 2
	 * if False
	 * 		evaluate Rule 3
	 */
	private static final String THREE_NODE_TRUE_FALSE_AGENDA_TREE = """
		<agendaTreeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaId>500Agenda</agendaId>
			<rule>
				<agendaItemId>AgendaItem1</agendaItemId>
				<ruleId>Rule1</ruleId>
				<ifTrue>
					<agendaId>500Agenda</agendaId>
					<rule>
						<agendaItemId>AgendaItem2</agendaItemId>
						<ruleId>Rule2</ruleId>
					</rule>
				</ifTrue>
				<ifFalse>
					<agendaId>500Agenda</agendaId>
					<rule>
						<agendaItemId>AgendaItem3</agendaItemId>
						<ruleId>Rule3</ruleId>
					</rule>
				</ifFalse>
			</rule>
		</agendaTreeDefinition>		"""

	/**
	 * Rule 1
	 * Rule 2
	 * if True
	 * 		Rule 3
	 * 		Rule 4
	 * 		Rule 5
	 * if False
	 * 		Rule 6
	 * Rule 7
	 *
	 */
	private static final String THE_BIG_TREE = """
		<agendaTreeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaId>500Agenda</agendaId>
			<rule>
				<agendaItemId>AgendaItem1</agendaItemId>
				<ruleId>Rule1</ruleId>
			</rule>
			<rule>
				<agendaItemId>AgendaItem2</agendaItemId>
				<ruleId>Rule2</ruleId>
				<ifTrue>
					<agendaId>500Agenda</agendaId>
					<rule>
						<agendaItemId>AgendaItem3</agendaItemId>
						<ruleId>Rule3</ruleId>
					</rule>
					<rule>
						<agendaItemId>AgendaItem4</agendaItemId>
						<ruleId>Rule4</ruleId>
					</rule>
					<rule>
						<agendaItemId>AgendaItem5</agendaItemId>
						<ruleId>Rule5</ruleId>
					</rule>
				</ifTrue>
				<ifFalse>
					<agendaId>500Agenda</agendaId>
					<rule>
						<agendaItemId>AgendaItem6</agendaItemId>
						<ruleId>Rule6</ruleId>
					</rule>
				</ifFalse>
			</rule>
			<rule>
				<agendaItemId>AgendaItem7</agendaItemId>
				<ruleId>Rule7</ruleId>
			</rule>
		</agendaTreeDefinition>		"""

	@Test
	void test_AgendaTreeDefinition_Builder_create() {
		AgendaTreeDefinition.Builder.create()
	}	
	
	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeDefinition_Builder_fail_null_agendaId() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.setAgendaId(null);
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeDefinition_Builder_fail_blank_agendaId() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.setAgendaId("");
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeDefinition_Builder_fail_whitespace_agendaId() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.setAgendaId("     ");
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeDefinition_Builder_fail_null_ruleEntry() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.addRuleEntry(null);
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeDefinition_Builder_fail_null_subAgendaEntry() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.addSubAgendaEntry(null);
	}

	@Test
	void test_AgendaTreeDefinition_Builder_create_and_build_tiny_success() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		myBuilder.build()
	}

	@Test
	public void testXmlMarshaling_tiny_AgendaTreeDefinition() {
		AgendaTreeDefinition.Builder myBuilder  = AgendaTreeDefinition.Builder.create()
		myBuilder.setAgendaId AGENDA_ID
		AgendaTreeDefinition myTiny = myBuilder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTiny, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(TINY_AGENDA_TREE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_tiny_AgendaTreeDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeDefinition myTiny = (AgendaTreeDefinition) unmarshaller.unmarshal(new StringReader(TINY_AGENDA_TREE))
	  Assert.assertEquals(AGENDA_ID, myTiny.agendaId)
	  Assert.assertTrue(myTiny.entries.size() == 0)
	}

	@Test
	void test_AgendaTreeDefinition_Builder_create_and_build_single_rule_success() {
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create()
		AgendaTreeRuleEntry ruleEntry = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1).build()
		myBuilder.setAgendaId AGENDA_ID
		myBuilder.addRuleEntry(ruleEntry)
		myBuilder.build()
	}

	@Test
	public void testXmlMarshaling_single_rule_AgendaTreeDefinition() {
		AgendaTreeDefinition.Builder myBuilder  = AgendaTreeDefinition.Builder.create()
		AgendaTreeRuleEntry ruleEntry = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1).build()
		myBuilder.setAgendaId AGENDA_ID
		myBuilder.addRuleEntry(ruleEntry)
		AgendaTreeDefinition myTiny = myBuilder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTiny, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SINGLE_RULE_AGENDA_TREE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_single_rule_AgendaTreeDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeDefinition myTiny = (AgendaTreeDefinition) unmarshaller.unmarshal(new StringReader(SINGLE_RULE_AGENDA_TREE))
	  Assert.assertEquals(AGENDA_ID, myTiny.agendaId)
	  Assert.assertTrue(myTiny.entries.size() == 1)
	}

	@Test
	public void testXmlMarshaling_single_node_multiple_rule_AgendaTreeDefinition() {
		AgendaTreeDefinition.Builder myBuilder  = AgendaTreeDefinition.Builder.create()
		AgendaTreeRuleEntry ruleEntry = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1).build()
		myBuilder.setAgendaId AGENDA_ID
		myBuilder.addRuleEntry(ruleEntry)
		ruleEntry = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2).build()
		myBuilder.addRuleEntry(ruleEntry)
		AgendaTreeSubAgendaEntry subEntry = AgendaTreeSubAgendaEntry.Builder.create(AGENDA_ITEM_ID_3, SUB_AGENDA_1).build()
		myBuilder.addSubAgendaEntry(subEntry)
		AgendaTreeDefinition myTiny = myBuilder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTiny, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SINGLE_NODE_MULTIPLE_RULE_AGENDA_TREE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_single_node_multiple_rule_AgendaTreeDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeDefinition myTree = (AgendaTreeDefinition) unmarshaller.unmarshal(new StringReader(SINGLE_NODE_MULTIPLE_RULE_AGENDA_TREE))
	  Assert.assertEquals(AGENDA_ID, myTree.agendaId)
	  Assert.assertTrue(myTree.entries.size() == 3)
	}

	/**
	 * 
	 * This method tests the following use case:
	 * <p>
	 * 		Rule 1
	 * 		If True
	 * 			Rule 2
	 * 		If False
	 * 			Rule 3
	 * </p>
	 */
	@Test
	public void testXmlMarshaling_3Node_True_False_AgendaTreeDefinition() {
		AgendaTreeDefinition.Builder myBuilder  = AgendaTreeDefinition.Builder.create()
		myBuilder.setAgendaId AGENDA_ID
		AgendaTreeRuleEntry.Builder ruleBuilder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		
		AgendaTreeDefinition.Builder treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		treeBuilder.addRuleEntry(builder2.build())
		ruleBuilder.setIfTrue treeBuilder		
		
		treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID
		builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		treeBuilder.addRuleEntry(builder2.build())
		ruleBuilder.setIfFalse treeBuilder
		
		myBuilder.addRuleEntry ruleBuilder.build()
		AgendaTreeDefinition myTree = myBuilder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myTree, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(THREE_NODE_TRUE_FALSE_AGENDA_TREE))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_3node_true_false_AgendaTreeDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeDefinition myTree = (AgendaTreeDefinition) unmarshaller.unmarshal(new StringReader(THREE_NODE_TRUE_FALSE_AGENDA_TREE))
	  Assert.assertEquals(AGENDA_ID, myTree.agendaId)
	  Assert.assertTrue(myTree.entries.size() == 1)
	  Assert.assertEquals RULE_ID_1, myTree.entries[0].ruleId
	  Assert.assertEquals RULE_ID_2, myTree.entries[0].ifTrue.entries[0].ruleId
	  Assert.assertEquals RULE_ID_3, myTree.entries[0].ifFalse.entries[0].ruleId
	}

	@Test
	public void testXmlUnmarshal_the_big_tree_AgendaTreeDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class, AgendaTreeSubAgendaEntry.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeDefinition myTree = (AgendaTreeDefinition) unmarshaller.unmarshal(new StringReader(THE_BIG_TREE))
	  Assert.assertEquals(AGENDA_ID, myTree.agendaId)
	  Assert.assertTrue(myTree.entries.size() == 3)
	  Assert.assertEquals RULE_ID_1, myTree.entries[0].ruleId
	  Assert.assertEquals RULE_ID_2, myTree.entries[1].ruleId
	  Assert.assertEquals RULE_ID_7, myTree.entries[2].ruleId
	  Assert.assertEquals RULE_ID_3, myTree.entries[1].ifTrue.entries[0].ruleId
	  Assert.assertEquals RULE_ID_4, myTree.entries[1].ifTrue.entries[1].ruleId
	  Assert.assertEquals RULE_ID_5, myTree.entries[1].ifTrue.entries[2].ruleId
	  Assert.assertEquals RULE_ID_6, myTree.entries[1].ifFalse.entries[0].ruleId
	}
}
