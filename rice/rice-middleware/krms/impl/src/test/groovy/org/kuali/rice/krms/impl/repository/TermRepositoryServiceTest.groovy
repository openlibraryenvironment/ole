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
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.krms.api.repository.RuleRepositoryService
import org.kuali.rice.krms.api.repository.term.TermRepositoryService
import org.kuali.rice.krms.api.repository.term.TermDefinition
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition

import static groovy.util.GroovyTestCase.assertEquals
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException

class TermRepositoryServiceTest {

    private def MockFor mock
    private final shouldFail = new GroovyTestCase().&shouldFail
    TermBoServiceImpl termRepositoryServiceImpl;
    TermRepositoryService termRepositoryService;

    @Before
    void setupServiceUnderTest() {
        termRepositoryServiceImpl = new TermBoServiceImpl()
        termRepositoryService = termRepositoryServiceImpl
    }

    @Before
    void setupBoServiceMockContext() {
        mock = new MockFor(BusinessObjectService.class)
    }

//
// TermRepositoryService Tests
//			
	
	// Test TermRepository Service.getTerm()
	@Test
	public void test_get_term() {

        TermSpecificationBo resultTermSpecBo = new TermSpecificationBo(id:"1", name:"FooTerm", namespace: "RICE", type: "java.lang.String");
        TermBo resultTermBo = new TermBo(id:"1", specificationId:"1", description:"desc",specification:resultTermSpecBo, parameters:[]);

        mock.demand.findBySinglePrimaryKey { a, b -> resultTermBo };

        def boService = mock.proxyDelegateInstance()
		termRepositoryServiceImpl.setBusinessObjectService(boService)

        assertEquals("FooTerm", termRepositoryService.getTerm("1").getSpecification().getName());

        mock.verify(boService)
	}


    @Test
    public void test_get_term_blank() {
        shouldFail(RiceIllegalArgumentException.class) {
            termRepositoryService.getTerm(" ")
        }
    }

    @Test
    public void test_get_term_null() {
        shouldFail(RiceIllegalArgumentException.class) {
            termRepositoryService.getTerm(null)
        }
    }

    // Test TermRepositoryService.findTermResolversByNamespace()
    @Test
    public void test_find_term_resolvers() {

        TermSpecificationBo resultTermSpecBo = new TermSpecificationBo(id:"1", name:"FooTerm", namespace: "RICE", type: "java.lang.String");
        TermResolverBo resultTermResolverBo = new TermResolverBo(id: "1", name: "FooResolver", namespace: "RICE", contextId: "1", typeId: "1", outputId: "1", output: resultTermSpecBo);

        mock.demand.findMatching { a, b -> [resultTermResolverBo] };

        def boService = mock.proxyDelegateInstance()
        termRepositoryServiceImpl.setBusinessObjectService(boService)

        assertEquals("FooResolver", termRepositoryService.findTermResolversByNamespace("RICE").get(0).getName());

        mock.verify(boService)
    }


    @Test
    public void test_find_term_resolvers_blank() {
        shouldFail(RiceIllegalArgumentException.class) {
            termRepositoryService.findTermResolversByNamespace(" ")
        }
    }

    @Test
    public void test_find_term_resolvers_null() {
        shouldFail(RiceIllegalArgumentException.class) {
            termRepositoryService.findTermResolversByNamespace(null)
        }
    }

}
