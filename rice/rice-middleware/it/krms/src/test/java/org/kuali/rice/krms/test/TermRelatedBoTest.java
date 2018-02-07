/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeBoService;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.ContextBoServiceImpl;
import org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImpl;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.krms.impl.repository.TermBoServiceImpl;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

@BaselineMode(Mode.CLEAR_DB)
public class TermRelatedBoTest extends AbstractBoTest {
	
	private TermBoService termBoService;
	private ContextBoService contextRepository;
	private KrmsTypeBoService krmsTypeBoService;
	
	@Before
	public void setup() {

		// wire up BO services

		termBoService = new TermBoServiceImpl();
		((TermBoServiceImpl)termBoService).setBusinessObjectService(getBoService());

		contextRepository = new ContextBoServiceImpl();
		((ContextBoServiceImpl)contextRepository).setBusinessObjectService(getBoService());
		
		krmsTypeBoService = new KrmsTypeBoServiceImpl();
		((KrmsTypeBoServiceImpl)krmsTypeBoService).setBusinessObjectService(getBoService());
	}
	
	@Test
	public void creationTest() {

		// KrmsType for context
		KrmsTypeDefinition krmsContextTypeDefinition = KrmsTypeDefinition.Builder.create("KrmsTestContextType", "KRMS").build();
		krmsContextTypeDefinition = krmsTypeBoService.createKrmsType(krmsContextTypeDefinition);

		// Context
		ContextDefinition.Builder contextBuilder = ContextDefinition.Builder.create("KRMS", "testContext");
		contextBuilder.setTypeId(krmsContextTypeDefinition.getId());
		ContextDefinition contextDefinition = contextBuilder.build();
		contextDefinition = contextRepository.createContext(contextDefinition);
		
		// output TermSpec
		TermSpecificationDefinition outputTermSpec = 
			TermSpecificationDefinition.Builder.create(null, "outputTermSpec", contextDefinition.getId(),
                    "java.lang.String").build();
		outputTermSpec = termBoService.createTermSpecification(outputTermSpec);

		// prereq TermSpec
		TermSpecificationDefinition prereqTermSpec = 
			TermSpecificationDefinition.Builder.create(null, "prereqTermSpec", contextDefinition.getId(),
                    "java.lang.String").build();
		prereqTermSpec = termBoService.createTermSpecification(prereqTermSpec);

		// KrmsType for TermResolver
		KrmsTypeDefinition krmsTermResolverTypeDefinition = KrmsTypeDefinition.Builder.create("KrmsTestResolverType", "KRMS").build();
		krmsTermResolverTypeDefinition = krmsTypeBoService.createKrmsType(krmsTermResolverTypeDefinition);

		// TermResolver
		TermResolverDefinition termResolverDef = 
			TermResolverDefinition.Builder.create(null, "KRMS", "testResolver", krmsTermResolverTypeDefinition.getId(),
					TermSpecificationDefinition.Builder.create(outputTermSpec), 
					Collections.singleton(TermSpecificationDefinition.Builder.create(prereqTermSpec)), 
					null, 
					Collections.singleton("testParamName")).build();
		termResolverDef = termBoService.createTermResolver(termResolverDef);

		// Term Param
		TermParameterDefinition.Builder termParamBuilder = 
			TermParameterDefinition.Builder.create(null, null, "testParamName", "testParamValue");
		
		// Term
		TermDefinition termDefinition = 
			TermDefinition.Builder.create(null, TermSpecificationDefinition.Builder.create(outputTermSpec), Collections.singletonList(termParamBuilder)).build();
		termBoService.createTerm(termDefinition);
	}
	
}
