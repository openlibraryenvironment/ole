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
package org.kuali.rice.krms.impl.provider.repository;

import static org.junit.Assert.*

import java.util.Collections;
import java.util.List;
import java.util.HashMap;

import org.junit.Before
import org.junit.Test
import org.kuali.rice.krms.api.engine.ExecutionEnvironment
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.repository.LogicalOperator
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinitionContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.framework.engine.BasicExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.PropositionResult;

import groovy.mock.interceptor.MockFor
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition
import org.joda.time.DateTime

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class CompoundPropositionTypeServiceTest {

	CompoundPropositionTypeService service;
	MockFor mockTranslator;
		
	@Before
	void setUp() {
		service = new CompoundPropositionTypeService();
		mockTranslator = new MockFor(RepositoryToEngineTranslator);
	}
	
	private void demandDefaultTranslator() {
		mockTranslator.demand.translatePropositionDefinition(2) { PropositionDefinition subProp ->
			String value = subProp.getParameters().get(0).getValue();
			if (value == "true") {
				return new Proposition() {
					public PropositionResult evaluate(ExecutionEnvironment environment) { return new PropositionResult(true) }
                    public List<Proposition> getChildren() { return Collections.emptyList() }
                    public boolean isCompound() { return false }
				}
			} else if (value== "false") {
				return new Proposition() {
                    public PropositionResult evaluate(ExecutionEnvironment environment) { return new PropositionResult(false) }
                    public List<Proposition> getChildren() { return Collections.emptyList() }
                    public boolean isCompound() { return false }
				}
			} else {
				fail("Invalid subProp type: " + subProp.getType());
			}
		}
		service.setTranslator(mockTranslator.proxyDelegateInstance());
	}
	
	private void verifyMocks() {
        mockTranslator.verify(service.translator)
    }
	
	private static final String ID = "1";
	private static final String DESCRIPTION = "description"
	private static final String TYPE_ID = "1";
	
	private static PropositionDefinition createTrueProp() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract() {
					String id = "2";
					String description = "";
                    String ruleId = "1";
					String typeId = "true";
					String propositionTypeCode = PropositionType.SIMPLE.getCode();
                                        Integer compoundSequenceNumber = new Integer (1)
					String compoundOpCode = null;
					List parameters = [ PropositionParameter.Builder.create("1", "2", "true", PropositionParameterType.CONSTANT.getCode(), 1) ];
					List compoundComponents = []; 
					Long versionNumber = new Long(1)
				}).build();
	}
	
	private static PropositionDefinition createFalseProp() {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract() {
					String id = "2";
					String description = "";
                    String ruleId = "1";
					String typeId = null;
					String propositionTypeCode = PropositionType.SIMPLE.getCode();
                                        Integer compoundSequenceNumber = new Integer (1)
					String compoundOpCode = null;
					List parameters = [ PropositionParameter.Builder.create("1", "2", "false", PropositionParameterType.CONSTANT.getCode(), 1) ];
					List compoundComponents = [];
					Long versionNumber = new Long(1)
				}).build();
	}
	
	private static PropositionDefinition createCompoundPropositionDefinition(LogicalOperator logicalOperator) {
		return PropositionDefinition.Builder.create(new PropositionDefinitionContract() {
			String id = CompoundPropositionTypeServiceTest.ID;
			String description = CompoundPropositionTypeServiceTest.DESCRIPTION;
            String ruleId = "1";
			String typeId = CompoundPropositionTypeServiceTest.TYPE_ID;
			String propositionTypeCode = PropositionType.COMPOUND.getCode();
			String compoundOpCode = logicalOperator.getCode();
                        Integer compoundSequenceNumber = new Integer (1)
			List parameters = [];
			List compoundComponents = [
				CompoundPropositionTypeServiceTest.createTrueProp(),
				CompoundPropositionTypeServiceTest.createFalseProp()
			];
			Long versionNumber = new Long(1)
		}).build();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testLoadProposition_nullTranslator() {
		PropositionDefinition propositionDefintion = createCompoundPropositionDefinition(LogicalOperator.AND);
		service.setTranslator(null);
		service.loadProposition(propositionDefintion);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLoadProposition_nullPropositionDefinition() {
		demandDefaultTranslator();
		service.loadProposition(null);
	}

	@Test
	public void testLoadProposition_And() {
		demandDefaultTranslator();		
		PropositionDefinition propositionDefintion = createCompoundPropositionDefinition(LogicalOperator.AND);
		Proposition proposition = service.loadProposition(propositionDefintion);
		assert proposition != null;		
		BasicExecutionEnvironment environment = new BasicExecutionEnvironment(
			SelectionCriteria.createCriteria(null, Collections.EMPTY_MAP, Collections.singletonMap(AgendaDefinition.Constants.EVENT, "name")),
                new HashMap<Term, Object>(), null, null
        );
		assert !(proposition.evaluate(environment).getResult());
		verifyMocks();
	}
	
	@Test
	public void testLoadProposition_Or() {
		demandDefaultTranslator();
		PropositionDefinition propositionDefintion = createCompoundPropositionDefinition(LogicalOperator.OR);
		Proposition proposition = service.loadProposition(propositionDefintion);
		assert proposition != null;
        BasicExecutionEnvironment environment = new BasicExecutionEnvironment(
            SelectionCriteria.createCriteria(null, Collections.EMPTY_MAP, Collections.singletonMap(AgendaDefinition.Constants.EVENT, "name")),
                new HashMap<Term, Object>(), null, null
        );
		assert (proposition.evaluate(environment).getResult());
		verifyMocks();
	}

}
