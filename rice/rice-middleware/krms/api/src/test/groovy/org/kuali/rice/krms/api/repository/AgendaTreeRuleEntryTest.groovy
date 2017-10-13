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


/**
 * This is a description of what this class does - dseibert don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class AgendaTreeRuleEntryTest {
	
	private static final String AGENDA_ID_1 = "500Agenda"
	private static final String AGENDA_ITEM_ID_1 = "AgendaItem1"
	private static final String RULE_ID_1 = "Rule1"
	private static final String AGENDA_ITEM_ID_2 = "AgendaItem2"
	private static final String RULE_ID_2 = "Rule2"
	private static final String AGENDA_ITEM_ID_3 = "AgendaItem3"
	private static final String RULE_ID_3 = "Rule3"

	private static final String TINY_AGENDA_RULE_ENTRY = """
		<agendaTreeRuleEntry xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaItemId>AgendaItem1</agendaItemId>
			<ruleId>Rule1</ruleId>
		</agendaTreeRuleEntry>
		"""

	private static final String SMALL_IFTRUE_AGENDA_RULE_ENTRY = """
		<agendaTreeRuleEntry xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaItemId>AgendaItem1</agendaItemId>
			<ruleId>Rule1</ruleId>
			<ifTrue>
				<agendaId>500Agenda</agendaId>
				<rule>
					<agendaItemId>AgendaItem2</agendaItemId>
					<ruleId>Rule2</ruleId>
				</rule>
			</ifTrue>
		</agendaTreeRuleEntry>	"""
	
	private static final String SMALL_IFFALSE_AGENDA_RULE_ENTRY = """
		<agendaTreeRuleEntry xmlns="http://rice.kuali.org/krms/v2_0">
			<agendaItemId>AgendaItem1</agendaItemId>
			<ruleId>Rule1</ruleId>
			<ifFalse>
				<agendaId>500Agenda</agendaId>
				<rule>
					<agendaItemId>AgendaItem3</agendaItemId>
					<ruleId>Rule3</ruleId>
				</rule>
			</ifFalse>
		</agendaTreeRuleEntry>	"""
	
	private static final String IFTRUE_AND_IFFALSE_AGENDA_RULE_ENTRY = """
		<agendaTreeRuleEntry xmlns="http://rice.kuali.org/krms/v2_0">
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
		</agendaTreeRuleEntry>	"""
	
	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_all_null() {
		AgendaTreeRuleEntry.Builder.create(null, null)
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_null_agenda_id() {
		AgendaTreeRuleEntry.Builder.create(null, RULE_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_empty_agenda_id() {
		AgendaTreeRuleEntry.Builder.create("", RULE_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_whitespace_agenda_id() {
		AgendaTreeRuleEntry.Builder.create("        ", RULE_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_null_rule_id() {
		AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, null)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_empty_rule_id() {
		AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, "")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaTreeRule_Builder_create_fail_whitespace_rule_id() {
		AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, "     ")
	}

	@Test
	void test_AgendaTreeRule_Builder_create_tiny_success() {
		AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
	}

	@Test
	void test_AgendaTreeRule_Builder_create_and_build_tiny_success() {
		AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1).build()
	}

	@Test
	public void testXmlMarshaling_tiny_AgendaRuleEntry() {
 		AgendaTreeRuleEntry myTiny = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1).build()
        JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
	    Marshaller marshaller = jc.createMarshaller()
	    StringWriter sw = new StringWriter()
	    marshaller.marshal(myTiny, sw)
	    String xml = sw.toString()
  
	    Unmarshaller unmarshaller = jc.createUnmarshaller();
	    Object actual = unmarshaller.unmarshal(new StringReader(xml))
	    Object expected = unmarshaller.unmarshal(new StringReader(TINY_AGENDA_RULE_ENTRY))
	    Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_tiny_AgendaRuleEntry() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeRuleEntry myTiny = (AgendaTreeRuleEntry) unmarshaller.unmarshal(new StringReader(TINY_AGENDA_RULE_ENTRY))
	  Assert.assertEquals(AGENDA_ITEM_ID_1, myTiny.agendaItemId)
	  Assert.assertEquals(RULE_ID_1, myTiny.ruleId)
	  Assert.assertNull(myTiny.ifTrue)
	  Assert.assertNull(myTiny.ifFalse)
	}

	@Test
	void test_AgendaTreeRule_Builder_setIfTrue_null_ifTrue() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		builder.setIfTrue(null)
	}

	@Test
	void test_AgendaTreeRule_Builder_create_small_ifTrue_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifTrueBuilder = AgendaTreeDefinition.Builder.create()
		ifTrueBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		ifTrueBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue ifTrueBuilder
	}

	@Test
	void test_AgendaTreeRule_Builder_create_and_build_ifTrue_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifTrueBuilder = AgendaTreeDefinition.Builder.create()
		ifTrueBuilder.setAgendaId(AGENDA_ID_1)
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		ifTrueBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue(ifTrueBuilder)
		builder.build()
	}

	@Test
	public void testXmlMarshaling_small_ifTrue_AgendaRuleEntry() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifTrueBuilder = AgendaTreeDefinition.Builder.create()
		ifTrueBuilder.setAgendaId(AGENDA_ID_1)
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		ifTrueBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue(ifTrueBuilder)
		AgendaTreeRuleEntry myTiny = builder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTiny, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_IFTRUE_AGENDA_RULE_ENTRY))
		Assert.assertEquals(expected, actual)
	}
	
	@Test
	public void testXmlUnmarshal_small_ifTrue_AgendaRuleEntry() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeRuleEntry myTiny = (AgendaTreeRuleEntry) unmarshaller.unmarshal(new StringReader(SMALL_IFTRUE_AGENDA_RULE_ENTRY))
	  Assert.assertEquals(AGENDA_ITEM_ID_1, myTiny.agendaItemId)
	  Assert.assertEquals(RULE_ID_1, myTiny.ruleId)
	  Assert.assertEquals(RULE_ID_2, myTiny.ifTrue.entries[0].ruleId)
	  Assert.assertNull(myTiny.ifFalse)
	}

	@Test
	void test_AgendaTreeRule_Builder_setIfFalse_null_ifFalse() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		builder.setIfFalse(null)
	}

	@Test
	void test_AgendaTreeRule_Builder_create_small_ifFalse_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifFalseBuilder = AgendaTreeDefinition.Builder.create()
		ifFalseBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		ifFalseBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse ifFalseBuilder
	}

	@Test
	void test_AgendaTreeRule_Builder_create_and_build_ifFalse_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifFalseBuilder = AgendaTreeDefinition.Builder.create()
		ifFalseBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		ifFalseBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse(ifFalseBuilder)
		builder.build()
	}

	@Test
	public void testXmlMarshaling_small_ifFalse_AgendaRuleEntry() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder ifFalseBuilder = AgendaTreeDefinition.Builder.create()
		ifFalseBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		ifFalseBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse(ifFalseBuilder)
		AgendaTreeRuleEntry myTiny = builder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTiny, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_IFFALSE_AGENDA_RULE_ENTRY))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_small_ifFalse_AgendaRuleEntry() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeRuleEntry myTiny = (AgendaTreeRuleEntry) unmarshaller.unmarshal(new StringReader(SMALL_IFFALSE_AGENDA_RULE_ENTRY))
	  Assert.assertEquals(AGENDA_ITEM_ID_1, myTiny.agendaItemId)
	  Assert.assertEquals(RULE_ID_1, myTiny.ruleId)
	  Assert.assertEquals(RULE_ID_3, myTiny.ifFalse.entries[0].ruleId)
	  Assert.assertNull(myTiny.ifTrue)
	}

	@Test
	void test_AgendaTreeRule_Builder_create_true_and_false_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue treeBuilder
		
		treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse treeBuilder
	}

	@Test
	void test_AgendaTreeRule_Builder_create_and_build_true_and_false_success() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue treeBuilder
		
		treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse treeBuilder
		builder.build()
	}

	@Test
	public void testXmlMarshaling_true_and_false_AgendaRuleEntry() {
		AgendaTreeRuleEntry.Builder builder = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_1, RULE_ID_1)
		AgendaTreeDefinition.Builder treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		AgendaTreeRuleEntry.Builder builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_2, RULE_ID_2)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfTrue treeBuilder
		
		treeBuilder = AgendaTreeDefinition.Builder.create()
		treeBuilder.setAgendaId AGENDA_ID_1
		builder2 = AgendaTreeRuleEntry.Builder.create(AGENDA_ITEM_ID_3, RULE_ID_3)
		treeBuilder.addRuleEntry(builder2.build())
		builder.setIfFalse treeBuilder
		AgendaTreeRuleEntry myTree = builder.build()
		JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myTree, sw)
		String xml = sw.toString()
  
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(IFTRUE_AND_IFFALSE_AGENDA_RULE_ENTRY))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_true_and_false_AgendaRuleEntry() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaTreeRuleEntry.class, AgendaTreeDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaTreeRuleEntry myTree = (AgendaTreeRuleEntry) unmarshaller.unmarshal(new StringReader(IFTRUE_AND_IFFALSE_AGENDA_RULE_ENTRY))
	  Assert.assertEquals(AGENDA_ITEM_ID_1, myTree.agendaItemId)
	  Assert.assertEquals(RULE_ID_1, myTree.ruleId)
	  Assert.assertEquals(RULE_ID_2, myTree.ifTrue.entries[0].ruleId)
	  Assert.assertEquals(RULE_ID_3, myTree.ifFalse.entries[0].ruleId)
	}

}
