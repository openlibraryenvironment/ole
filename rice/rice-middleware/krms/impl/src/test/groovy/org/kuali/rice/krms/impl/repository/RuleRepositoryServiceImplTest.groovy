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
package org.kuali.rice.krms.impl.repository

import groovy.mock.interceptor.MockFor
import org.junit.Before
import org.junit.Test
import org.kuali.rice.core.api.criteria.CriteriaLookupService
import org.kuali.rice.core.api.criteria.GenericQueryResults
import org.kuali.rice.krms.api.repository.RuleRepositoryService
import org.kuali.rice.krms.api.repository.context.ContextDefinition
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria

import static groovy.util.GroovyTestCase.assertEquals
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException

class RuleRepositoryServiceImplTest {
    private def MockFor mock
    private final shouldFail = new GroovyTestCase().&shouldFail
    RuleRepositoryServiceImpl ruleRepositoryServiceImpl;
    RuleRepositoryService ruleRepositoryService;

    @Before
    void setupServiceUnderTest() {
        ruleRepositoryServiceImpl = new RuleRepositoryServiceImpl()
        ruleRepositoryService = ruleRepositoryServiceImpl
    }

    @Before
    void setupCriteriaLookupServiceMockContext() {
        mock = new MockFor(CriteriaLookupService.class)
    }

//
// RuleRepositoryService Tests
//			
	
	// Test RuleRepositoryService.selectContext()
	@Test
	public void test_select_context_valid_criteria() {
        AgendaBo resultAgenda = new AgendaBo(id: "1", name: "agenda1", typeId: "2", contextId: "1" )
        ContextBo resultContext = new ContextBo(id: "1", name: "context1", namespace: "RICE", typeId: "1")
        resultContext.agendas = [resultAgenda]

        GenericQueryResults.Builder<ContextBo> queryResults = GenericQueryResults.Builder.create();
        queryResults.results = [resultContext]
        mock.demand.lookup() { a, b -> queryResults.build() }

        def criteriaLookupService = mock.proxyDelegateInstance()
        ruleRepositoryServiceImpl.setCriteriaLookupService(criteriaLookupService)

        ContextSelectionCriteria criteria = ContextSelectionCriteria.newCriteria("RICE", "context1", Collections.emptyMap());

        ContextDefinition context = ruleRepositoryService.selectContext(criteria);

        assertEquals("agenda1", context.getAgendas().get(0).getName())
	}

    @Test
    public void test_select_context_null_criteria() {

        def criteriaLookupService = mock.proxyDelegateInstance()
        ruleRepositoryServiceImpl.setCriteriaLookupService(criteriaLookupService)

        shouldFail(RiceIllegalArgumentException.class) {
            ContextDefinition context = ruleRepositoryService.selectContext(null);
        }
    }

}
