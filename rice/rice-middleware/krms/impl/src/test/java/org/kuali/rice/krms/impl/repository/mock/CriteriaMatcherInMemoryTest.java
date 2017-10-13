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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.impl.repository.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;

/**
 *
 * @author nwright
 */
public class CriteriaMatcherInMemoryTest {

    public CriteriaMatcherInMemoryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private List<ContextDefinition> getTestContexts() {
        List<ContextDefinition> contexts = new ArrayList<ContextDefinition>();
        addContext(contexts, "ID-AA", "AA", "Automobile Association", "Consumer", "true", "Automobile Association towing");
        addContext(contexts, "ID-CR", "CR", "Consumer Reports", "Consumer", "true", "Consumer reports magazine");
        addContext(contexts, "ID-ARP", "ARP", "Assciation of Retired Persons", "Consumer", "false", null);
        addContext(contexts, "ID-NRA", "NRA", "National Rifle Association", "Lobby", "true", "Guns and roses");
        addContext(contexts, "ID-UFT", "UFT", "United Federation of Teachers", "Lobby", "true", "tenure for everyone");
        addContext(contexts, "ID-AFL", "AFL", "AFL-CIO", "Union", "true", "Jimmy Hoffa may he rest in peace somewhere");
        return contexts;
    }

    private void addContext(List<ContextDefinition> contexts, 
            String id, 
            String namespace, 
            String name, 
            String typeId, 
            String active, 
            String description) {
        ContextDefinition.Builder info = ContextDefinition.Builder.create(namespace, name);
        info.setId(id);
        info.setTypeId(typeId);
        info.setActive(Boolean.parseBoolean(active));
        info.setDescription(description);
        contexts.add(info.build());
    }

    @Test
    public void testFindAll1() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria criteria = null;
        instance.setCriteria(criteria);
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
        expectedIds.add("ID-ARP");
        expectedIds.add("ID-NRA");
        expectedIds.add("ID-UFT");
        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }

    @Test
    public void testFindAll2() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
        expectedIds.add("ID-ARP");
        expectedIds.add("ID-NRA");
        expectedIds.add("ID-UFT");
        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }

    @Test
    public void testFindEquals1() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.equal("typeId", "Consumer"));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
        expectedIds.add("ID-ARP");
//        expectedIds.add("ID-NRA");
//        expectedIds.add("ID-UFT");
//        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }

    @Test
    public void testFindEquals2() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.equal("active", "true"));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
//        expectedIds.add("ID-ARP");
        expectedIds.add("ID-NRA");
        expectedIds.add("ID-UFT");
        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }

    @Test
    public void testFindLike() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.like("name", "%Association%"));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
//        expectedIds.add("ID-CR");
//        expectedIds.add("ID-ARP");
        expectedIds.add("ID-NRA");
//        expectedIds.add("ID-UFT");
//        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }
    
    @Test
    public void testFindDeepLike() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.like("description", "%magazine%"));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
//        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
//        expectedIds.add("ID-ARP");
//        expectedIds.add("ID-NRA");
//        expectedIds.add("ID-UFT");
//        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }
    
    @Test
    public void testFindOr() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.or (
                PredicateFactory.equal("typeId", "Lobby"),
                PredicateFactory.equal("active", "false")));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
//        expectedIds.add("ID-AA");
//        expectedIds.add("ID-CR");
        expectedIds.add("ID-ARP");
        expectedIds.add("ID-NRA");
        expectedIds.add("ID-UFT");
//        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }

    @Test
    public void testFindAnd() {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteria.setPredicates(PredicateFactory.and (
                PredicateFactory.equal("typeId", "Consumer"),
                PredicateFactory.equal("active", "true")));
        instance.setCriteria(criteria.build());
        Collection<ContextDefinition> result = instance.findMatching(getTestContexts());
        Set<String> expectedIds = new LinkedHashSet<String>();
        expectedIds.add("ID-AA");
        expectedIds.add("ID-CR");
//        expectedIds.add("ID-ARP");
//        expectedIds.add("ID-NRA");
//        expectedIds.add("ID-UFT");
//        expectedIds.add("ID-AFL");
        testIds(expectedIds, result);
    }
    
    private void testIds(Set<String> expectedIds, Collection<ContextDefinition> result) {
        for (ContextDefinition info : result) {
            if (!expectedIds.remove(info.getId())) {
                fail("Unexpectedly got " + info.getId());
            }
        }
        if (!expectedIds.isEmpty()) {
            fail("expected " + expectedIds.size() + " more");
        }
    }

    @Test
    public void testMatchesEqual() {
        assertEquals(true, CriteriaMatcherInMemory.matchesEqual("a", "a"));
        assertEquals(false, CriteriaMatcherInMemory.matchesEqual("a", "b"));
        assertEquals(false, CriteriaMatcherInMemory.matchesEqual("b", "a"));
    }

    @Test
    public void testMatchesLessThan() {
        assertEquals(false, CriteriaMatcherInMemory.matchesLessThan("a", "a"));
        assertEquals(true, CriteriaMatcherInMemory.matchesLessThan("a", "b"));
        assertEquals(false, CriteriaMatcherInMemory.matchesLessThan("b", "a"));
    }

    @Test
    public void testMatchesGreaterThan() {
        assertEquals(false, CriteriaMatcherInMemory.matchesGreaterThan("a", "a"));
        assertEquals(false, CriteriaMatcherInMemory.matchesGreaterThan("a", "b"));
        assertEquals(true, CriteriaMatcherInMemory.matchesGreaterThan("b", "a"));
    }

    @Test
    public void testMatchesLike() {
        assertEquals(true, CriteriaMatcherInMemory.matchesLike("a", "a"));
        assertEquals(false, CriteriaMatcherInMemory.matchesLike("a", "b"));
        assertEquals(false, CriteriaMatcherInMemory.matchesLike("b", "a"));
        assertEquals(true, CriteriaMatcherInMemory.matchesLike("b", "%"));
        assertEquals(true, CriteriaMatcherInMemory.matchesLike("b", "b%"));
        assertEquals(false, CriteriaMatcherInMemory.matchesLike("b", "a%"));
        assertEquals(true, CriteriaMatcherInMemory.matchesLike("bbbbba", "%a%"));
    }
    
    
    @Test
    public void testExtractValue() {
//        addCont(orgs, "ID-AA", "AA", "Automobile Association", "Consumer", "true", "Automobile Association towing");
        ContextDefinition org = this.getTestContexts().get(0);
        assertEquals("ID-AA", CriteriaMatcherInMemory.extractValue("id", org));
        assertEquals("AA", CriteriaMatcherInMemory.extractValue("namespace", org));
        assertEquals("Automobile Association", CriteriaMatcherInMemory.extractValue("name", org));
        assertEquals("Consumer", CriteriaMatcherInMemory.extractValue("typeId", org));
        assertEquals("true", CriteriaMatcherInMemory.extractValue("active", org));
        assertEquals("Automobile Association towing", CriteriaMatcherInMemory.extractValue("description", org));
    }
    
}
