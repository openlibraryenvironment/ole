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
package org.kuali.rice.krms.framework;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.engine.TermResolutionEngine;
import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.framework.engine.TermResolutionEngineImpl;
import org.springframework.util.CollectionUtils;


public class TermResolutionEngineTest {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TermResolutionEngineTest.class);

	private TermResolutionEngine termResolutionEngine = null;
	
	@Before
	public void setUp() {
		termResolutionEngine = new TermResolutionEngineImpl();
	}
	
	@Test
	public void testNoResolution() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);
		
		// GIVENS:
		testHelper.addGivens("A");

		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("A");
	}
	
	@Test
	public void testSimpleResolution() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);
		
		// GIVENS:
		testHelper.addGivens("A");
		
		// RESOLVERS:
		testHelper.addResolver("B", /* <-- */ "A");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("B");
	}
	
	@Test
	public void testTwoStepResolution() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);
		
		// GIVENS:
		testHelper.addGivens("A");
		
		// RESOLVERS:
		testHelper.addResolver("B", /* <-- */ "A");
		testHelper.addResolver("C", /* <-- */ "B");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("C");
	}

	@Test
	public void testForkingResolution() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("A", "Z");
		
		// RESOLVERS:
		testHelper.addResolver("D", /* <-- */ "B","C");
		testHelper.addResolver("C", /* <-- */ "Z");
		testHelper.addResolver("B", /* <-- */ "A");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("D");
	}
	
	@Test
	public void testMultipleValidPaths() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);
		
		// GIVENS:
		testHelper.addGivens("A", "Z");
		
		// RESOLVERS:
		testHelper.addResolver("D", /* <-- */ "B","C");
		testHelper.addResolver("C", /* <-- */ "Z");
		testHelper.addResolver("B", /* <-- */ "A");
		testHelper.addResolver("D", /* <-- */ "A");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("D");
	}
	
	@Test
	public void testDiamond() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("A");
		
		// RESOLVERS:
		testHelper.addResolver("D", /* <-- */ "B","C");
		testHelper.addResolver("C", /* <-- */ "A");
		testHelper.addResolver("B", /* <-- */ "A");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("D");
		
	}

	@Test
	public void testComplexPath() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("Q","R","S");
		
		// RESOLVERS:
		testHelper.addResolver("A", /* <-- */ "B","F");
		testHelper.addResolver("A", /* <-- */ "Z");
		testHelper.addResolver("B", /* <-- */ "D");
		testHelper.addResolver("B", /* <-- */ "C");
		testHelper.addResolver("C", /* <-- */ "S");
		testHelper.addResolver("D", /* <-- */ "E");
		testHelper.addResolver("E", /* <-- */ "S");
		testHelper.addResolver("F", /* <-- */ "G","Q");
		testHelper.addResolver("G", /* <-- */ "R");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("A");
		
	}

	@Test
	public void testCycle() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		// none
		
		// RESOLVERS:
		testHelper.addResolver("D", /* <-- */ "C");
		testHelper.addResolver("C", /* <-- */ "D");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertException("D");
		
	}
	

	@Test
	public void testUnreachableTerm() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		// none
		
		// RESOLVERS:
		testHelper.addResolver("D", /* <-- */ "C");
		testHelper.addResolver("C", /* <-- */ "B");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertException("D");
		
	}
	
	@Test
	public void testRedHerringPath() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("Q");
		
		// RESOLVERS:
		testHelper.addResolver("A", /* <-- */ "B");
		// this is the shortest path, but C can't be resolved
		testHelper.addResolver("B", /* <-- */ "C");
		
		testHelper.addResolver("B", /* <-- */ "D");
		testHelper.addResolver("D", /* <-- */ "Q");
		
		testHelper.logScenarioDescription();
		
		testHelper.assertSuccess("A");
	}
	
	@Test
	public void testResolveParamaterizedTerm() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("Q");
		
		// RESOLVERS:
		testHelper.addResolver(1, "A", new String[] {"param"}, /* <-- */ "B");
		testHelper.addResolver("B", /* <-- */ "Q");
		
		testHelper.logScenarioDescription();
		
		Map<String,String> params = new HashMap();
		params.put("param", "value");
		testHelper.assertSuccess(testHelper.getTerm("A", params));

		// termName A needs parameters, so this shouldn't work
		testHelper.assertException(new Term("A"));

	}
	
	@Test
	public void testIntermediateParamaterizedTerm() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("Q");
		
		// RESOLVERS:
		testHelper.addResolver(1, "A", new String[] {"foo"}, /* <-- */ "B");
		testHelper.addResolver(1, "B", new String[] {"bar"},  /* <-- */ "Q");
		
		testHelper.logScenarioDescription();
		
		Map<String,String> params = new HashMap();
		params.put("foo", "foovalue");

		// Resolver for termName b requires parameters, so this shouldn't work
		testHelper.assertException(testHelper.getTerm("A", params));
	}
	
	private static class WhiteBoxTermResolutionEngineImpl extends TermResolutionEngineImpl {
		
		// expose this for testing purposes
		@Override
		public List<TermResolverKey> buildTermResolutionPlan(String termName) {
			return super.buildTermResolutionPlan(termName);
		}
	}
	
	
	@Test
	public void testShortestPath() {
		
		WhiteBoxTermResolutionEngineImpl whiteBoxTermResolutionService = new WhiteBoxTermResolutionEngineImpl();
		TestScenarioHelper testHelper = new TestScenarioHelper(whiteBoxTermResolutionService);

		// GIVENS:
		testHelper.addGivens("Q");
		
		// RESOLVERS:
		// this one costs 3 (instead of default cost of 1)
		testHelper.addResolver(3, "A", /* <-- */ "Q");

		// the steps for the alternate path each cost 1, but total length is 4
		testHelper.addResolver("A", /* <-- */ "B");
		testHelper.addResolver("B", /* <-- */ "C");
		testHelper.addResolver("C", /* <-- */ "D");
		testHelper.addResolver("D", /* <-- */ "Q");
		
		testHelper.logScenarioDescription();
		
		List<?> plan = whiteBoxTermResolutionService.buildTermResolutionPlan("A");
		LOG.info("resolutionPlan: " + StringUtils.join(plan, ", ") + " <-- should be length 1!");
		assertTrue("didn't choose the shortest resolution path (of length 1)", plan.size() == 1);
	}
	
	/*
	 *  TODO: test exception variants:
	 *  - TermResolver throws TermResolutionException
	 *  - TermResolutionEngine is passed a null termName
	 */
	
	// TODO: what should the TermResolutionEngine do if a resolver throws a RuntimeException?
	/*
	@Test
	public void testExplodingResolver() {
		TestScenarioHelper testHelper = new TestScenarioHelper(termResolutionEngine);

		// GIVENS:
		testHelper.addGivens("Q");
		
		// RESOLVERS:
		TermResolverMock exploder = new TermResolverMock(testHelper.getTerm("A"), testHelper.getResult("A"), testHelper.getTerm("Q"));
		exploder.setIsExploder(true);
		termResolutionEngine.addTermResolver(exploder);
		
		testHelper.logScenarioDescription();
		
		testHelper.assertException("A");
	}
	*/
	
	
	private static class TermResolverMock<T> implements TermResolver<T> {
		private String output;
		private Set<String> params;
		private T result;
		private Set<String> prereqs;
		private int cost;
		private boolean isExploder = false;

		public TermResolverMock(String output, T result, int cost, String ... prereqs) {
			this(output, null, result, cost, prereqs);
		}
		
		public TermResolverMock(String output, Set<String> params, T result, int cost, String ... prereqs) {
			this.output = output;
			this.params = Collections.unmodifiableSet(params);
			this.result = result;
			this.prereqs = new HashSet<String>(Arrays.asList(prereqs));
			this.cost = cost;
		}
		
		@Override
		public int getCost() {
			return cost;
		}
		
		@Override
		public String getOutput() {
			return output;
		}
		
		@Override
		public Set<String> getPrerequisites() {
			return prereqs;
		}
		
		@Override
		public Set<String> getParameterNames() {
			return params;
		}
		
		public void setIsExploder(boolean isExploder) {
			this.isExploder = isExploder;
		}
		
		@Override
		public T resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) {
			
			if (isExploder) {
				throw new RuntimeException("I'm the exploder, coo coo catchoo");
			}
			
			// get all prereqs first
			for (String prereq : prereqs) {
				Object result = resolvedPrereqs.get(prereq);
				if (result == null) fail("got back null for prereq " + prereq);
			}

			LOG.info("resolving " + output);
			return result;
		}
		
		@Override
		public String toString() {
			String paramStr = "";
			if (!CollectionUtils.isEmpty(params)) {
				paramStr = "+(" + StringUtils.join(params, ",") + ")";
			}
			return getClass().getSimpleName()+"[ "+output+paramStr+ " <- " + StringUtils.join(prereqs.iterator(), ",") + " ]";
		}

	}
	

	private static class TestScenarioHelper {
		
		private final TermResolutionEngine ars;
		
		private List<String> givens = new LinkedList<String>();
		private List<String> resolvers = new LinkedList<String>();

		public TestScenarioHelper(TermResolutionEngine tre) {
			this.ars = tre;
		}
		
		public void addGivens(String ... names) {
			for (String name : names) {
				ars.addTermValue(new Term(name, null), getResult(name));
				givens.add(name);
			}
		}
		
		public String getResult(String name) {
			return getResult(new Term(name, null));
		}
		
		public String getResult(Term term) {
			return term.getName()  +"-result";
		}
		
		public Term getTerm(String name, Map<String,String> params) {
			return new Term(name, params);
		}
		
		public void addResolver(String out, String ... prereqs) {
			addResolver(1, out, prereqs);
		}

		public void addResolver(int cost, String out, String ... prereqs) {
			addResolver(1, out, null, prereqs);
		}
		
		public void addResolver(int cost, String out, String[] params, String ... prereqs) {
			String [] prereqTerms = new String [prereqs.length];
			
			for (int i=0; i<prereqs.length; i++) prereqTerms[i] = prereqs[i];
			
			Set<String> paramSet = Collections.emptySet();
			if (params != null) paramSet = new HashSet<String>(Arrays.asList(params));
			
			ars.addTermResolver(new TermResolverMock<Object>(out, paramSet, getResult(out), cost, prereqTerms));
			resolvers.add("(" + out + " <- " + StringUtils.join(prereqs, ",") + ")");
		}
		
		public void logScenarioDescription() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("givens: " + StringUtils.join(givens.iterator(), ", ") + "\n\n");
			sb.append("resolvers:\n----------------------\n");
			if (resolvers == null || resolvers.size() == 0) {
				sb.append("none");
			} else { 
				sb.append(StringUtils.join(resolvers.iterator(), "\n"));
			}
			sb.append("\n");

			LOG.info("Test Scenario:\n\n" + sb.toString());
		}
		
		public void assertSuccess(String toResolve) {
			assertSuccess(new Term(toResolve, null));
		}
		
		public void assertSuccess(Term toResolve) {
			LOG.info("Testing resolution of " + toResolve);
			try {
				assertEquals(getResult(toResolve), ars.resolveTerm(toResolve));
				LOG.info("Success!");
			} catch (TermResolutionException e) {
				fail("Should resolve the termName w/o exceptions");
			}
		}

		public void assertException(String toResolve) {
			assertException(new Term(toResolve, null));
		}
			
		public void assertException(Term toResolve) {
			LOG.info("Testing resolution of " + toResolve);
			try {
				ars.resolveTerm(toResolve);
				fail("Should throw TermResolutionException");
			} catch (TermResolutionException e) {
				LOG.info("Success! threw " + e);
				// Good, this is what we expect
			}
		}
	}
	
}
