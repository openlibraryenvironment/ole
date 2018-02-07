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

import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.Term;
import org.kuali.rice.krms.api.repository.RuleRepositoryService;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.ContextProvider;

public class RuleRepositoryContextProvider implements ContextProvider {

	// may want to move these out to a constants file somewhere so they are accessible to engine clients?
	private static final String NAME_CONTEXT_QUALIFIER = "name";
	private static final String NAMESPACE_CODE_CONTEXT_QUALIFIER = "namespaceCode";
	
	private RuleRepositoryService ruleRepositoryService;
	private RepositoryToEngineTranslator repositoryToEngineTranslator;
	
	@Override
	public Context loadContext(SelectionCriteria selectionCriteria, Map<Term, Object> facts, ExecutionOptions executionOptions) {
		ContextSelectionCriteria contextSelectionCriteria = constructContextSelectionCriteria(selectionCriteria);
		ContextDefinition contextDefinition = ruleRepositoryService.selectContext(contextSelectionCriteria);
		
		// TODO should have an execution option that throws an error here if a context does not exist?
		
		if (contextDefinition != null) {
			return loadContextFromDefinition(contextDefinition);
		}
		return null;
	}
	
	protected Context loadContextFromDefinition(ContextDefinition contextDefinition) {
		return repositoryToEngineTranslator.translateContextDefinition(contextDefinition);
	}
	
	public void setRuleRepositoryService(RuleRepositoryService ruleRepositoryService) {
		this.ruleRepositoryService = ruleRepositoryService;
	}
	
	public void setRepositoryToEngineTranslator(RepositoryToEngineTranslator repositoryToEngineTranslator) {
		this.repositoryToEngineTranslator = repositoryToEngineTranslator;
	}
	
	protected ContextSelectionCriteria constructContextSelectionCriteria(SelectionCriteria selectionCriteria) {
		Map<String, String> givenContextQualifiers = selectionCriteria.getContextQualifiers();
		if (givenContextQualifiers == null || givenContextQualifiers.isEmpty()) {
			throw new IllegalArgumentException("Context qualifiers in the selection criteria were null or empty.  At least one context qualifier must be passed in selection criteria.");
		}
		
		// extract the "standard" context qualifiers for the rule repository, name and namespaceCode
		
		String namespaceCode = null;
		String name = null;
		Map<String, String> contextQualifiers = new HashMap<String, String>();
		for (String key : givenContextQualifiers.keySet()) {
			String value = givenContextQualifiers.get(key);
			if (key.equals(NAME_CONTEXT_QUALIFIER)) {
				name = value;
			} else if (key.equals(NAMESPACE_CODE_CONTEXT_QUALIFIER)) {
				namespaceCode = value;
			} else {
				contextQualifiers.put(key, value);
			}
		}
		
		return ContextSelectionCriteria.newCriteria(namespaceCode, name, contextQualifiers);
		
	}

}
