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
package org.kuali.rice.core.api.criteria;


import org.junit.Test
import org.kuali.rice.core.api.criteria.Person.Name
import org.kuali.rice.core.test.JAXBAssert
import static org.junit.Assert.*

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class QueryResultsTest {
	
	private static final String WITH_DEFAULTS_XML = "<personQueryResults xmlns=\"http://rice.kuali.org/core/v2_0\"><results/><moreResultsAvailable>false</moreResultsAvailable></personQueryResults>";
	private static final String WITH_RESULTS_XML = "<personQueryResults xmlns=\"http://rice.kuali.org/core/v2_0\"><results><person><name><first>first1</first><last>last1</last></name><displayName>first1 last1</displayName></person><person><name><first>first2</first><last>last2</last></name><displayName>first2 last2</displayName></person><person><name><first>first3</first><last>last3</last></name><displayName>first3 last3</displayName></person></results><moreResultsAvailable>false</moreResultsAvailable></personQueryResults>";
	private static final String WITH_EVERYTHING_XML = "<personQueryResults xmlns=\"http://rice.kuali.org/core/v2_0\"><results><person><name><first>first1</first><last>last1</last></name><displayName>first1 last1</displayName></person><person><name><first>first2</first><last>last2</last></name><displayName>first2 last2</displayName></person><person><name><first>first3</first><last>last3</last></name><displayName>first3 last3</displayName></person></results><totalRowCount>5000</totalRowCount><moreResultsAvailable>true</moreResultsAvailable></personQueryResults>";

	@Test
	public void testBuild_withDefaults() {
		PersonQueryResults queryResults = createWithDefaults();
		assertTrue("results shoudl be empty by default", queryResults.getResults().isEmpty());
		assertNull("totalRowCount should default to null", queryResults.getTotalRowCount());
		assertFalse("isMoreResultsAvailable should default to false", queryResults.isMoreResultsAvailable());
	}
	
	@Test
	public void testBuild_withResults() {
		PersonQueryResults queryResults = createWithResults();
		assertEquals("should have 3 results", 3, queryResults.getResults().size());
		assertNull("totalRowCount should default to null", queryResults.getTotalRowCount());
		assertFalse("isMoreResultsAvailable should default to false", queryResults.isMoreResultsAvailable());
		
		Person person1 = queryResults.getResults().get(0);
		Person person2 = queryResults.getResults().get(1);
		Person person3 = queryResults.getResults().get(2);
		Name name1 = person1.getName();
		Name name2 = person2.getName();
		Name name3 = person3.getName();
		
		assertEquals("first1", name1.getFirst());
		assertEquals("last1", name1.getLast());
		assertEquals("first2", name2.getFirst());
		assertEquals("last2", name2.getLast());
		assertEquals("first3", name3.getFirst());
		assertEquals("last3", name3.getLast());
		
		assertEquals("first1 last1", person1.getDisplayName());
		assertEquals("first2 last2", person2.getDisplayName());
		assertEquals("first3 last3", person3.getDisplayName());
	}
	
	@Test
	public void testBuild_withEverything() {
		PersonQueryResults queryResults = createWithEverything();
		assertEquals("totalRowCount should be 5000", new Integer(5000), queryResults.getTotalRowCount());
		assertTrue("isMoreResultsAvailable should be true", queryResults.isMoreResultsAvailable());
		
		Person person1 = queryResults.getResults().get(0);
		Person person2 = queryResults.getResults().get(1);
		Person person3 = queryResults.getResults().get(2);
		Name name1 = person1.getName();
		Name name2 = person2.getName();
		Name name3 = person3.getName();
		
		assertEquals("first1", name1.getFirst());
		assertEquals("last1", name1.getLast());
		assertEquals("first2", name2.getFirst());
		assertEquals("last2", name2.getLast());
		assertEquals("first3", name3.getFirst());
		assertEquals("last3", name3.getLast());
		
		assertEquals("first1 last1", person1.getDisplayName());
		assertEquals("first2 last2", person2.getDisplayName());
		assertEquals("first3 last3", person3.getDisplayName());
	}
	
	@Test
	public void testJAXB_withDefaults() {
		PersonQueryResults queryResults = createWithDefaults();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(queryResults, WITH_DEFAULTS_XML, PersonQueryResults.class);
	}
	
	@Test
	public void testJAXB_withResults() {
		PersonQueryResults queryResults = createWithResults();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(queryResults, WITH_RESULTS_XML, PersonQueryResults.class);
	}
	
	@Test
	public void testJAXB_withEverything() {
		PersonQueryResults queryResults = createWithEverything();
		JAXBAssert.assertEqualXmlMarshalUnmarshal(queryResults, WITH_EVERYTHING_XML, PersonQueryResults.class);
	}
	
	private static PersonQueryResults createWithDefaults() {
		return PersonQueryResults.Builder.create().build();		
	}
	
	private static PersonQueryResults createWithResults() {
		PersonQueryResults.Builder builder =  PersonQueryResults.Builder.create();
		builder.setResults(buildMeSomePeeps());
		return builder.build();
	}
	
	private static PersonQueryResults createWithEverything() {
		PersonQueryResults.Builder builder =  PersonQueryResults.Builder.create();
		builder.setResults(buildMeSomePeeps());
		builder.setMoreResultsAvailable(true);
		builder.setTotalRowCount(5000);
		return builder.build();
	}
	
	private static List<Person> buildMeSomePeeps() {
		List<Person> results = new ArrayList<Person>();
		
		Name name1 = new Person.Name("first1", "last1");
		Name name2 = new Person.Name("first2", "last2");
		Name name3 = new Person.Name("first3", "last3");
		Person person1 = new Person(name1, "first1 last1", null);
		Person person2 = new Person(name2, "first2 last2", null);
		Person person3 = new Person(name3, "first3 last3", null);
		
		results.add(person1);
		results.add(person2);
		results.add(person3);
		
		return results;
	}
	
}
