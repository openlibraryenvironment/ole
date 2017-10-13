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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.repository.RepositoryDataException;
import org.kuali.rice.krms.api.repository.RuleRepositoryService;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeEntryDefinitionContract;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeRuleEntry;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeSubAgendaEntry;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.engine.AgendaTree;
import org.kuali.rice.krms.framework.engine.AgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicAgendaTree;
import org.kuali.rice.krms.framework.engine.BasicAgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicContext;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.engine.SubAgenda;
import org.kuali.rice.krms.framework.type.AgendaTypeService;
import org.kuali.rice.krms.framework.type.TermResolverTypeService;
import org.kuali.rice.krms.impl.type.AgendaTypeServiceBase;
import org.kuali.rice.krms.impl.type.KrmsTypeResolver;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO... 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RepositoryToEngineTranslatorImpl implements RepositoryToEngineTranslator {

	private RuleRepositoryService ruleRepositoryService;
    private TermRepositoryService termRepositoryService;
	private KrmsTypeResolver typeResolver;

	@Override
	public Context translateContextDefinition(ContextDefinition contextDefinition) {
		if (contextDefinition == null) {
			return null;
		}
		List<Agenda> agendas = new ArrayList<Agenda>();
		for (AgendaDefinition agendaDefinition : contextDefinition.getAgendas()) {
			Agenda agenda = translateAgendaDefinition(agendaDefinition);
			agendas.add(agenda);
		}
		
		List<TermResolverDefinition> termResolverDefs = 
			getTermRepositoryService().findTermResolversByNamespace(contextDefinition.getNamespace());
		
		List<TermResolver<?>> termResolvers = new ArrayList<TermResolver<?>>();

		if (!CollectionUtils.isEmpty(termResolverDefs)) for (TermResolverDefinition termResolverDef : termResolverDefs) {
			if (termResolverDef != null) {
				TermResolver<?> termResolver = translateTermResolver(termResolverDef);
				if (termResolver != null) termResolvers.add(termResolver);
			}
		}
		
		return new BasicContext(agendas, termResolvers); 
	}

	/**
	 * This method translates a {@link TermResolverDefinition} into a {@link TermResolver}
	 * 
	 * @param termResolverDef
	 * @return
	 */
	private TermResolver<?> translateTermResolver(TermResolverDefinition termResolverDef) {
		if (termResolverDef == null) {
			throw new IllegalArgumentException("termResolverDef must not be null");
		}
		TermResolverTypeService termResolverTypeService = 
			typeResolver.getTermResolverTypeService(termResolverDef);
		
		TermResolver<?> termResolver = termResolverTypeService.loadTermResolver(termResolverDef);
		// TODO: log warning when termResolver comes back null? or throw exception?
		return termResolver;
	}
	
	@Override
	public Agenda translateAgendaDefinition(AgendaDefinition agendaDefinition) {
        Agenda result = null;
        
        // unless the type is undefined, translate it using the AgendaTypeService
        if (StringUtils.isEmpty(agendaDefinition.getTypeId())) {
            // our default agenda implementation
            result = AgendaTypeServiceBase.defaultAgendaTypeService.loadAgenda(agendaDefinition);
        } else {
            AgendaTypeService agendaTypeService = typeResolver.getAgendaTypeService(agendaDefinition);
            // our typeResolver will throw an appropriate exception if it can't get the type
            // so no need for null check here
            result = agendaTypeService.loadAgenda(agendaDefinition);
        }

		return result;
	}
		
	@Override
	public AgendaTree translateAgendaDefinitionToAgendaTree(AgendaDefinition agendaDefinition) {
		AgendaTreeDefinition agendaTreeDefinition = ruleRepositoryService.getAgendaTree(agendaDefinition.getId());
		return translateAgendaTreeDefinition(agendaTreeDefinition);
	}
	
	@Override
	public AgendaTree translateAgendaTreeDefinition(AgendaTreeDefinition agendaTreeDefinition) {
	
		List<String> ruleIds = new ArrayList<String>();
		List<String> subAgendaIds = new ArrayList<String>();
		for (AgendaTreeEntryDefinitionContract entryDefinition : agendaTreeDefinition.getEntries()) {
			if (entryDefinition instanceof AgendaTreeRuleEntry) {
				ruleIds.add(((AgendaTreeRuleEntry)entryDefinition).getRuleId());
			} else if (entryDefinition instanceof AgendaTreeSubAgendaEntry) {
				subAgendaIds.add(((AgendaTreeSubAgendaEntry)entryDefinition).getSubAgendaId());
			} else {
				throw new IllegalStateException("Encountered invalid agenda tree entry definition class, did not understand type: " + entryDefinition);
			}
		}
		
		Map<String, Rule> rules = loadRules(ruleIds);
		Map<String, SubAgenda> subAgendas = loadSubAgendas(subAgendaIds);
		
		List<AgendaTreeEntry> entries = new ArrayList<AgendaTreeEntry>();
	
		for (AgendaTreeEntryDefinitionContract entryDefinition : agendaTreeDefinition.getEntries()) {
			if (entryDefinition instanceof AgendaTreeRuleEntry) {
				AgendaTreeRuleEntry ruleEntry = (AgendaTreeRuleEntry)entryDefinition;
				AgendaTree ifTrue = null;
				AgendaTree ifFalse = null;
				if (ruleEntry.getIfTrue() != null) {
					ifTrue = translateAgendaTreeDefinition(ruleEntry.getIfTrue());
				}
				if (ruleEntry.getIfFalse() != null) {
					ifFalse = translateAgendaTreeDefinition(ruleEntry.getIfFalse());
				}
				Rule rule = rules.get(ruleEntry.getRuleId());
				if (rule == null) {
					throw new IllegalStateException("Failed to locate rule with id: " + ruleEntry.getRuleId());
				}
				BasicAgendaTreeEntry agendaTreeEntry = new BasicAgendaTreeEntry(rule, ifTrue, ifFalse);
				entries.add(agendaTreeEntry);
			} else if (entryDefinition instanceof AgendaTreeSubAgendaEntry) {
				AgendaTreeSubAgendaEntry subAgendaEntry = (AgendaTreeSubAgendaEntry)entryDefinition;
				SubAgenda subAgenda = subAgendas.get(subAgendaEntry.getSubAgendaId());
				if (subAgenda == null) {
					throw new IllegalStateException("Failed to locate sub agenda with id: " + subAgendaEntry.getSubAgendaId());
				}
				BasicAgendaTreeEntry agendaTreeEntry = new BasicAgendaTreeEntry(subAgenda, null, null);
				entries.add(agendaTreeEntry);
			} else {
				throw new IllegalStateException("Encountered invalid agenda tree entry class, did not understand type: " + entryDefinition);
			}
		}
		return new BasicAgendaTree(entries);
	}
	
	protected Map<String, Rule> loadRules(List<String> ruleIds) {
		List<RuleDefinition> ruleDefinitions = ruleRepositoryService.getRules(ruleIds);
		validateRuleDefinitions(ruleIds, ruleDefinitions);
		Map<String, Rule> rules = new HashMap<String, Rule>();
		for (RuleDefinition ruleDefinition : ruleDefinitions) {
			rules.put(ruleDefinition.getId(), translateRuleDefinition(ruleDefinition));
		}
		return rules;
	}
	
	/**
	 * Ensures that there is a rule definition for every rule id in the original list.
	 */
	private void validateRuleDefinitions(List<String> ruleIds, List<RuleDefinition> ruleDefinitions) {
		if (ruleIds.size() != ruleDefinitions.size()) {
			Map<String, RuleDefinition> indexedRuleDefinitions = indexRuleDefinitions(ruleDefinitions);
			for (String ruleId : ruleIds) {
				if (!indexedRuleDefinitions.containsKey(ruleId)) {
					throw new RepositoryDataException("Failed to locate a rule with id '" + ruleId + "' in the repository.");
				}
			}
		}
	}
	
	private Map<String, RuleDefinition> indexRuleDefinitions(List<RuleDefinition> ruleDefinitions) {
		Map<String, RuleDefinition> ruleDefinitionMap = new HashMap<String, RuleDefinition>();
		for (RuleDefinition ruleDefinition : ruleDefinitions) {
			ruleDefinitionMap.put(ruleDefinition.getId(), ruleDefinition);
		}
		return ruleDefinitionMap;
	}
	
	protected Map<String, SubAgenda> loadSubAgendas(List<String> subAgendaIds) {
		List<AgendaTreeDefinition> subAgendaDefinitions = ruleRepositoryService.getAgendaTrees(subAgendaIds);
		validateSubAgendaDefinitions(subAgendaIds, subAgendaDefinitions);
		Map<String, SubAgenda> subAgendas = new HashMap<String, SubAgenda>();
		for (AgendaTreeDefinition subAgendaDefinition : subAgendaDefinitions) {
			subAgendas.put(subAgendaDefinition.getAgendaId(), translateAgendaTreeDefinitionToSubAgenda(subAgendaDefinition));
		}
		return subAgendas;
	}
	
	/**
	 * Ensures that there is a rule definition for every rule id in the original list.
	 */
	private void validateSubAgendaDefinitions(List<String> subAgendaIds, List<AgendaTreeDefinition> subAgendaDefinitions) {
		if (subAgendaIds.size() != subAgendaDefinitions.size()) {
			Map<String, AgendaTreeDefinition> indexedSubAgendaDefinitions = indexSubAgendaDefinitions(subAgendaDefinitions);
			for (String subAgendaId : subAgendaIds) {
				if (!indexedSubAgendaDefinitions.containsKey(subAgendaId)) {
					throw new RepositoryDataException("Failed to locate an agenda with id '" + subAgendaId + "' in the repository.");
				}
			}
		}
	}
	
	private Map<String, AgendaTreeDefinition> indexSubAgendaDefinitions(List<AgendaTreeDefinition> subAgendaDefinitions) {
		Map<String, AgendaTreeDefinition> subAgendaDefinitionMap = new HashMap<String, AgendaTreeDefinition>();
		for (AgendaTreeDefinition subAgendaDefinition : subAgendaDefinitions) {
			subAgendaDefinitionMap.put(subAgendaDefinition.getAgendaId(), subAgendaDefinition);
		}
		return subAgendaDefinitionMap;
	}
	
	@Override
	public Rule translateRuleDefinition(RuleDefinition ruleDefinition) {
		List<Action> actions = new ArrayList<Action>();
		if (ruleDefinition.getActions() != null) {
			for (ActionDefinition actionDefinition : ruleDefinition.getActions()) {
				actions.add(translateActionDefinition(actionDefinition));
			}
		}
        return new LazyRule(ruleDefinition, typeResolver);
	}
	
	@Override
	public Proposition translatePropositionDefinition(PropositionDefinition propositionDefinition) {
		return new LazyProposition(propositionDefinition, typeResolver);
	}
	
	@Override
	public Action translateActionDefinition(ActionDefinition actionDefinition) {
		if (actionDefinition.getTypeId() == null) {
			throw new RepositoryDataException("Given ActionDefinition does not have a typeId, actionId was: " + actionDefinition.getId());
		}
		return new LazyAction(actionDefinition, typeResolver);
	}

    @Override
    public List<Action> translateActionDefinitions(List<ActionDefinition> actionDefinitions) {
        List<Action> actions = new ArrayList<Action>();
        for (ActionDefinition actionDefinition : actionDefinitions) {
            actions.add(translateActionDefinition(actionDefinition));
        }
        return actions;
    }

    @Override
	public SubAgenda translateAgendaTreeDefinitionToSubAgenda(AgendaTreeDefinition subAgendaDefinition) {
		return new SubAgenda(translateAgendaTreeDefinition(subAgendaDefinition));
	}
	
	/**
	 * @param ruleRepositoryService the ruleRepositoryService to set
	 */
	public void setRuleRepositoryService(
			RuleRepositoryService ruleRepositoryService) {
		this.ruleRepositoryService = ruleRepositoryService;
	}

	/**
	 * @param typeResolver the typeResolver to set
	 */
	public void setTypeResolver(KrmsTypeResolver typeResolver) {
		this.typeResolver = typeResolver;
	}

    public TermRepositoryService getTermRepositoryService() {
        return termRepositoryService;
    }

    public void setTermRepositoryService(TermRepositoryService termRepositoryService) {
        this.termRepositoryService = termRepositoryService;
    }
}
