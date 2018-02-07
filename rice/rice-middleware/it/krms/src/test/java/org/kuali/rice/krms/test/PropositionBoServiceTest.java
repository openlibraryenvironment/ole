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

import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krms.api.repository.type.KrmsTypeBoService;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.impl.repository.KrmsTypeBoServiceImpl;
import org.kuali.rice.krms.impl.repository.PropositionBoService;
import org.kuali.rice.test.BaselineTestCase.BaselineMode;
import org.kuali.rice.test.BaselineTestCase.Mode;

@BaselineMode(Mode.CLEAR_DB)
public class PropositionBoServiceTest extends AbstractBoTest {
//public class PropositionBoServiceTest extends LightWeightBoTest {
	
	private PropositionBoService propositionBoService;
	private KrmsTypeBoService krmsTypeBoService;
	
	@Before
	public void setup() {

		krmsTypeBoService = new KrmsTypeBoServiceImpl();
		((KrmsTypeBoServiceImpl) krmsTypeBoService).setBusinessObjectService(getBoService());
		
//		dao.setJcdAlias("krmsDataSource");
//		
//		// wire up BO services
//		
//		propositionBoService = new PropositionBoServiceImpl();
//		((PropositionBoServiceImpl)propositionBoService).setBusinessObjectService(getBoService());
//
//		contextRepository = new ContextBoServiceImpl();
//		((ContextBoServiceImpl)contextRepository).setBusinessObjectService(getBoService());
//		
//		krmsTypeRepository = new KrmsTypeBoServiceImpl();
//		((KrmsTypeBoServiceImpl)krmsTypeRepository).setBusinessObjectService(getBoService());
	}
	
	@Test
	public void creationTest() {

		// KrmsType for context
		KrmsTypeDefinition krmsContextTypeDefinition = KrmsTypeDefinition.Builder.create("KrmsTestContextType", "KRMS").build();
		krmsContextTypeDefinition = krmsTypeBoService.createKrmsType(krmsContextTypeDefinition);

//		// Context
//		ContextDefinition.Builder contextBuilder = ContextDefinition.Builder.create("KRMS", "testContext");
//		contextBuilder.setTypeId(krmsContextTypeDefinition.getId());
//		ContextDefinition contextDefinition = contextBuilder.build();
//		contextDefinition = contextRepository.createContext(contextDefinition);
//		
//		// output TermSpec
//		TermSpecificationDefinition outputTermSpec = 
//			TermSpecificationDefinition.Builder.create(null, contextDefinition.getId(), 
//					"outputTermSpec", "java.lang.String").build();
//		outputTermSpec = propositionBoService.createTermSpecification(outputTermSpec);
//
//		// prereq TermSpec
//		TermSpecificationDefinition prereqTermSpec = 
//			TermSpecificationDefinition.Builder.create(null, contextDefinition.getId(), 
//					"prereqTermSpec", "java.lang.String").build();
//		prereqTermSpec = propositionBoService.createTermSpecification(prereqTermSpec);
//
//		// KrmsType for TermResolver
//		KrmsTypeDefinition krmsTermResolverTypeDefinition = KrmsTypeDefinition.Builder.create(null, "KrmsTestResolverType", "KRMS").build();
//		krmsTermResolverTypeDefinition = krmsTypeRepository.createKrmsType(krmsTermResolverTypeDefinition);
//
//		// TermResolver
//		TermResolverDefinition termResolverDef = 
//			TermResolverDefinition.Builder.create(null, "KRMS", "testResolver", krmsTermResolverTypeDefinition.getId(), 
//					TermSpecificationDefinition.Builder.create(outputTermSpec), 
//					Collections.singleton(TermSpecificationDefinition.Builder.create(prereqTermSpec)), 
//					null, 
//					Collections.singleton("testParamName")).build();
//		termResolverDef = propositionBoService.createTermResolver(termResolverDef);
//
//		// Term Param
//		TermParameterDefinition.Builder termParamBuilder = 
//			TermParameterDefinition.Builder.create(null, null, "testParamName", "testParamValue");
//		
//		// Term
//		TermDefinition termDefinition = 
//			TermDefinition.Builder.create(null, TermSpecificationDefinition.Builder.create(outputTermSpec), Collections.singleton(termParamBuilder)).build();
//		propositionBoService.createTerm(termDefinition);
	}

}
