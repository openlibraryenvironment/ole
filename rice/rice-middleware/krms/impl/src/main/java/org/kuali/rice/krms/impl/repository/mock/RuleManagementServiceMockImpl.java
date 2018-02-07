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
package org.kuali.rice.krms.impl.repository.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krms.api.repository.NaturalLanguageTree;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.impl.repository.TranslationUtility;
import org.kuali.rice.krms.impl.repository.language.SimpleNaturalLanguageTemplater;

public class RuleManagementServiceMockImpl implements RuleManagementService {
    
    
    // cache variable 
    // The LinkedHashMap is just so the values come back in a predictable order

    private Map<String, ReferenceObjectBinding> referenceObjectBindingMap = new LinkedHashMap<String, ReferenceObjectBinding>();
    private Map<String, ContextDefinition> contextMap = new LinkedHashMap<String, ContextDefinition>();
    private Map<String, AgendaDefinition> agendaMap = new LinkedHashMap<String, AgendaDefinition>();
    private Map<String, AgendaItemDefinition> agendaItemMap = new LinkedHashMap<String, AgendaItemDefinition>();
    private Map<String, RuleDefinition> ruleMap = new LinkedHashMap<String, RuleDefinition>();
    private Map<String, ActionDefinition> actionMap = new LinkedHashMap<String, ActionDefinition>();
    private Map<String, PropositionDefinition> propositionMap = new LinkedHashMap<String, PropositionDefinition>();
    private Map<String, NaturalLanguageUsage> naturalLanguageUsageMap = new LinkedHashMap<String, NaturalLanguageUsage>();
    private Map<String, NaturalLanguageTemplate> naturalLanguageTemplateMap = new LinkedHashMap<String, NaturalLanguageTemplate>();
    
    // supporting services used in this service impl
    private NaturalLanguageTemplaterContract templater = new SimpleNaturalLanguageTemplater();
    private TermRepositoryService termRepositoryService;
    
    public NaturalLanguageTemplaterContract getTemplater() {
        return templater;
    }

    public void setTemplater(NaturalLanguageTemplaterContract templater) {
        this.templater = templater;
    }

    public TermRepositoryService getTermRepositoryService() {
        return termRepositoryService;
    }

    public void setTermRepositoryService(TermRepositoryService termRepositoryService) {
        this.termRepositoryService = termRepositoryService;
    }

    
    
    public void clear() {
        this.referenceObjectBindingMap.clear();
        this.contextMap.clear();
        this.agendaMap.clear();
        this.agendaItemMap.clear();
        this.ruleMap.clear();
        this.actionMap.clear();
        this.propositionMap.clear();
        this.naturalLanguageUsageMap.clear();
        this.naturalLanguageTemplateMap.clear();
    }

    @Override
    public ReferenceObjectBinding createReferenceObjectBinding(ReferenceObjectBinding referenceObjectDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        ReferenceObjectBinding orig = this.getReferenceObjectBinding(referenceObjectDefinition.getId());
        if (orig != null) {
            throw new RiceIllegalArgumentException(referenceObjectDefinition.getId());
        }
        ReferenceObjectBinding.Builder copy = ReferenceObjectBinding.Builder.create(referenceObjectDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        referenceObjectDefinition = copy.build();
        referenceObjectBindingMap.put(referenceObjectDefinition.getId(), referenceObjectDefinition);
        return referenceObjectDefinition;
    }

    @Override
    public ReferenceObjectBinding getReferenceObjectBinding(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.referenceObjectBindingMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.referenceObjectBindingMap.get(id);
    }

    @Override
    public List<ReferenceObjectBinding> getReferenceObjectBindings(List<String> ids)
            throws RiceIllegalArgumentException {
        // GET_BY_IDS
        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (String id : ids) {
            list.add(this.getReferenceObjectBinding(id));
        }
        return list;
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceDiscriminatorType(String referenceObjectReferenceDiscriminatorType)
            throws RiceIllegalArgumentException {
        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (ReferenceObjectBinding info : this.referenceObjectBindingMap.values()) {
            if (info.getReferenceDiscriminatorType().equals(referenceObjectReferenceDiscriminatorType)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceObject(String referenceObjectReferenceDiscriminatorType, String referenceObjectId) throws RiceIllegalArgumentException {

        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (ReferenceObjectBinding info : this.referenceObjectBindingMap.values()) {
            if (info.getReferenceDiscriminatorType().equals(referenceObjectReferenceDiscriminatorType)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsDiscriminatorType(String referenceObjectKrmsDiscriminatorType)
            throws RiceIllegalArgumentException {
        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (ReferenceObjectBinding info : this.referenceObjectBindingMap.values()) {
            if (info.getKrmsDiscriminatorType().equals(referenceObjectKrmsDiscriminatorType)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsObject(String krmsObjectId)
            throws RiceIllegalArgumentException {
        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (ReferenceObjectBinding info : this.referenceObjectBindingMap.values()) {
            if (info.getKrmsObjectId().equals(krmsObjectId)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public void updateReferenceObjectBinding(ReferenceObjectBinding referenceObjectBindingDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        ReferenceObjectBinding.Builder copy = ReferenceObjectBinding.Builder.create(referenceObjectBindingDefinition);
        ReferenceObjectBinding old = this.getReferenceObjectBinding(referenceObjectBindingDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        referenceObjectBindingDefinition = copy.build();
        this.referenceObjectBindingMap.put(referenceObjectBindingDefinition.getId(), referenceObjectBindingDefinition);
        return;
    }

    @Override
    public void deleteReferenceObjectBinding(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.referenceObjectBindingMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public List<String> findReferenceObjectBindingIds(QueryByCriteria queryByCriteria)
            throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<ReferenceObjectBinding> instance = new CriteriaMatcherInMemory<ReferenceObjectBinding>();
        instance.setCriteria(queryByCriteria);
        Collection<ReferenceObjectBinding> selected = instance.findMatching(this.referenceObjectBindingMap.values());
        List<String> list = new ArrayList<String>();
        for (ReferenceObjectBinding sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }

    @Override
    public AgendaDefinition createAgenda(AgendaDefinition agendaDefinition)
            throws RiceIllegalArgumentException {
        if (agendaDefinition.getId() != null) {
            AgendaDefinition orig = this.getAgenda(agendaDefinition.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(agendaDefinition.getId() + "." + agendaDefinition.getName());
            }
        }
        AgendaDefinition existing = this.getAgendaByNameAndContextId (agendaDefinition.getName(), agendaDefinition.getContextId());
        if (existing == null) {
            throw new RiceIllegalArgumentException (agendaDefinition.getName() + " " + agendaDefinition.getContextId() + " already exists");
        }
        AgendaDefinition.Builder copy = AgendaDefinition.Builder.create(agendaDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        copy.setVersionNumber(0l);
        agendaDefinition = copy.build();
        agendaMap.put(agendaDefinition.getId(), agendaDefinition);
        return agendaDefinition;
    }

    @Override
    public AgendaDefinition findCreateAgenda(AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException {
        AgendaDefinition agenda = this.getAgendaByNameAndContextId(agendaDefinition.getName(), agendaDefinition.getContextId());
        if (agenda != null) {
            return agenda;
        }
        return this.createAgenda(agendaDefinition);        
    }

    @Override
    public AgendaDefinition getAgendaByNameAndContextId(String name, String contextId) {  
        for (AgendaDefinition info : this.agendaMap.values()) {
            if (info.getContextId().equals(contextId)) {
                if (info.getName().equals(name)) {
                    return info;
                }
            }
        }
        return null;
    }
    
    

    @Override
    public AgendaDefinition getAgenda(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.agendaMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.agendaMap.get(id);
    }

    @Override
    public List<AgendaDefinition> getAgendasByType(String typeId)
            throws RiceIllegalArgumentException {
        // GET_IDS_BY_TYPE
        List<AgendaDefinition> list = new ArrayList<AgendaDefinition>();
        for (AgendaDefinition info : agendaMap.values()) {
            if (typeId.equals(info.getTypeId())) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public List<AgendaDefinition> getAgendasByContext(String contextId)
            throws RiceIllegalArgumentException {
        List<AgendaDefinition> list = new ArrayList<AgendaDefinition>();
        for (AgendaDefinition info : this.agendaMap.values()) {
            if (info.getContextId().equals(contextId)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public List<AgendaDefinition> getAgendasByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException {
        List<AgendaDefinition> list = new ArrayList<AgendaDefinition>();
        for (AgendaDefinition info : this.agendaMap.values()) {
            if (info.getContextId().equals(contextId)) {
                if (info.getTypeId().equals(typeId)) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    @Override
    public void updateAgenda(AgendaDefinition agendaDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        AgendaDefinition.Builder copy = AgendaDefinition.Builder.create(agendaDefinition);
        AgendaDefinition old = this.getAgenda(agendaDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        agendaDefinition = copy.build();
        this.agendaMap.put(agendaDefinition.getId(), agendaDefinition);
        return;
    }

    @Override
    public void deleteAgenda(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.agendaMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public AgendaItemDefinition createAgendaItem(AgendaItemDefinition agendaItemDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        AgendaItemDefinition.Builder copy = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        agendaItemDefinition = copy.build();
        agendaItemMap.put(agendaItemDefinition.getId(), agendaItemDefinition);
        return agendaItemDefinition;
    }

    @Override
    public AgendaItemDefinition getAgendaItem(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.agendaItemMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.agendaItemMap.get(id);
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByType(String typeId)
            throws RiceIllegalArgumentException {
        // GET_IDS_BY_TYPE
        List<AgendaDefinition> agendas = this.getAgendasByType(typeId);
        List<AgendaItemDefinition> list = new ArrayList<AgendaItemDefinition>();
        for (AgendaDefinition agenda : agendas) {
            for (AgendaItemDefinition info : agendaItemMap.values()) {
                if (agenda.getId().equals(info.getAgendaId())) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByContext(String contextId)
            throws RiceIllegalArgumentException {
        List<AgendaDefinition> agendas = this.getAgendasByContext(contextId);
        List<AgendaItemDefinition> list = new ArrayList<AgendaItemDefinition>();
        for (AgendaDefinition agenda : agendas) {
            for (AgendaItemDefinition info : agendaItemMap.values()) {
                if (agenda.getId().equals(info.getAgendaId())) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException {
        List<AgendaDefinition> agendas = this.getAgendasByContext(contextId);
        List<AgendaItemDefinition> list = new ArrayList<AgendaItemDefinition>();
        for (AgendaDefinition agenda : agendas) {
            if (agenda.getTypeId().equals(typeId)) {
                for (AgendaItemDefinition info : agendaItemMap.values()) {
                    if (agenda.getId().equals(info.getAgendaId())) {
                        list.add(info);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void updateAgendaItem(AgendaItemDefinition agendaItemDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        AgendaItemDefinition.Builder copy = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        AgendaItemDefinition old = this.getAgendaItem(agendaItemDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        agendaItemDefinition = copy.build();
        this.agendaItemMap.put(agendaItemDefinition.getId(), agendaItemDefinition);
        return;
    }

    @Override
    public void deleteAgendaItem(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.agendaItemMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public RuleDefinition getRuleByNameAndNamespace(String name, String namespace) {
        for (RuleDefinition rule : this.ruleMap.values()) {
            if (rule.getName().equals(name)) {
                if (rule.getNamespace().equals(namespace)) {
                    return rule;
                }
            }
        }
        return null;
    }

    
    
    @Override
    public RuleDefinition createRule(RuleDefinition ruleDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        if (ruleDefinition.getId() != null) {
            RuleDefinition orig = this.getRule(ruleDefinition.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(ruleDefinition.getId());
            }
        }
        RuleDefinition.Builder copy = RuleDefinition.Builder.create(ruleDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        ruleDefinition = copy.build();
        ruleMap.put(ruleDefinition.getId(), ruleDefinition);
        return ruleDefinition;
    }

    @Override
    public RuleDefinition getRule(String ruleId) {
        // GET_BY_ID
        if (!this.ruleMap.containsKey(ruleId)) {
            throw new RiceIllegalArgumentException(ruleId);
        }
        return this.ruleMap.get(ruleId);
    }

    @Override
    public List<RuleDefinition> getRules(List<String> ruleIds) {
        List<RuleDefinition> list = new ArrayList<RuleDefinition>();
        for (String id : ruleIds) {
            list.add(this.getRule(id));
        }
        return list;
    }

    @Override
    public void updateRule(RuleDefinition ruleDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        RuleDefinition.Builder copy = RuleDefinition.Builder.create(ruleDefinition);
        RuleDefinition old = this.getRule(ruleDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        ruleDefinition = copy.build();
        this.ruleMap.put(ruleDefinition.getId(), ruleDefinition);
        return;
    }

    @Override
    public void deleteRule(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.ruleMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public ActionDefinition createAction(ActionDefinition actionDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        ActionDefinition orig = this.getAction(actionDefinition.getId());
        if (orig != null) {
            throw new RiceIllegalArgumentException(actionDefinition.getId());
        }
        ActionDefinition.Builder copy = ActionDefinition.Builder.create(actionDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        actionDefinition = copy.build();
        actionMap.put(actionDefinition.getId(), actionDefinition);
        return actionDefinition;
    }

    @Override
    public ActionDefinition getAction(String actionId) {
        // GET_BY_ID
        if (!this.actionMap.containsKey(actionId)) {
            throw new RiceIllegalArgumentException(actionId);
        }
        return this.actionMap.get(actionId);
    }

    @Override
    public List<ActionDefinition> getActions(List<String> actionIds) {
        List<ActionDefinition> list = new ArrayList<ActionDefinition>();
        for (String id : actionIds) {
            list.add(this.getAction(id));
        }
        return list;
    }

    @Override
    public void updateAction(ActionDefinition actionDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        ActionDefinition.Builder copy = ActionDefinition.Builder.create(actionDefinition);
        ActionDefinition old = this.getAction(actionDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        actionDefinition = copy.build();
        this.actionMap.put(actionDefinition.getId(), actionDefinition);
        return;
    }

    @Override
    public void deleteAction(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.actionMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public PropositionDefinition createProposition(PropositionDefinition propositionDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        if (propositionDefinition.getId() != null) {
            PropositionDefinition orig = this.getProposition(propositionDefinition.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(propositionDefinition.getId());
            }
        }
        PropositionDefinition.Builder copy = PropositionDefinition.Builder.create(propositionDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        for (PropositionParameter.Builder paramBldr : copy.getParameters()) {
            if (paramBldr.getId() == null) {
                paramBldr.setId(UUID.randomUUID().toString());
            }
            if (paramBldr.getPropId() == null) {
                paramBldr.setPropId(copy.getId());
            }
            if (paramBldr.getTermValue() != null) {
                TermDefinition termValue = paramBldr.getTermValue();
                // no id means it does not exist yet
                if (termValue.getId() == null) {
                    termValue = this.termRepositoryService.createTerm(termValue);
                    paramBldr.setTermValue(termValue);
                }
                if (paramBldr.getValue() == null) {
                    paramBldr.setValue(termValue.getId());
                }
            }
        }
        propositionDefinition = copy.build();
        propositionMap.put(propositionDefinition.getId(), propositionDefinition);
        return propositionDefinition;
    }

    @Override
    public PropositionDefinition getProposition(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.propositionMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.propositionMap.get(id);
    }

    @Override
    public Set<PropositionDefinition> getPropositionsByType(String typeId)
            throws RiceIllegalArgumentException {
        // GET_IDS_BY_TYPE
        Set<PropositionDefinition> set = new LinkedHashSet<PropositionDefinition>();
        for (PropositionDefinition info : propositionMap.values()) {
            if (typeId.equals(info.getTypeId())) {
                set.add(info);
            }
        }
        return set;
    }

    @Override
    public Set<PropositionDefinition> getPropositionsByRule(String ruleId)
            throws RiceIllegalArgumentException {
        Set<PropositionDefinition> set = new LinkedHashSet<PropositionDefinition>();
        for (PropositionDefinition info : this.propositionMap.values()) {
            if (info.getRuleId().equals(ruleId)) {
                set.add(info);
            }
        }
        return set;
    }

    @Override
    public void updateProposition(PropositionDefinition propositionDefinition)
            throws RiceIllegalArgumentException {
        if (this.propositionMap.containsKey(propositionDefinition.getId())) {
            throw new RiceIllegalArgumentException (propositionDefinition.getId() + "not found");
        }
        // UPDATE
        PropositionDefinition.Builder copy = PropositionDefinition.Builder.create(propositionDefinition);
        PropositionDefinition old = this.getProposition(propositionDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        for (PropositionParameter.Builder paramBldr : copy.getParameters()) {
            if (paramBldr.getId() == null) {
                paramBldr.setId(UUID.randomUUID().toString());
            }
            if (paramBldr.getPropId() == null) {
                paramBldr.setPropId(copy.getId());
            }
            if (paramBldr.getTermValue() != null) {
                TermDefinition termValue = paramBldr.getTermValue();
                // no id means it does not exist yet
                if (termValue.getId() == null) {
                    termValue = this.termRepositoryService.createTerm(termValue);
                    paramBldr.setTermValue(termValue);
                }
                if (paramBldr.getValue() == null) {
                    paramBldr.setValue(termValue.getId());
                }
            }
        }
        propositionDefinition = copy.build();
        this.propositionMap.put(propositionDefinition.getId(), propositionDefinition);
        return;
    }

    @Override
    public void deleteProposition(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.propositionMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public NaturalLanguageUsage createNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage)
            throws RiceIllegalArgumentException {
        // CREATE
        try {
            NaturalLanguageUsage orig = this.getNaturalLanguageUsage(naturalLanguageUsage.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(naturalLanguageUsage.getId());
            }
        } catch (RiceIllegalArgumentException ex) {
//            same as returning null
        }
        NaturalLanguageUsage.Builder copy = NaturalLanguageUsage.Builder.create(naturalLanguageUsage);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        naturalLanguageUsage = copy.build();
        naturalLanguageUsageMap.put(naturalLanguageUsage.getId(), naturalLanguageUsage);
        return naturalLanguageUsage;
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsage(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.naturalLanguageUsageMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.naturalLanguageUsageMap.get(id);
    }

    @Override
    public void updateNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage)
            throws RiceIllegalArgumentException {
        // UPDATE
        NaturalLanguageUsage.Builder copy = NaturalLanguageUsage.Builder.create(naturalLanguageUsage);
        NaturalLanguageUsage old = this.getNaturalLanguageUsage(naturalLanguageUsage.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        naturalLanguageUsage = copy.build();
        this.naturalLanguageUsageMap.put(naturalLanguageUsage.getId(), naturalLanguageUsage);
        return;
    }

    @Override
    public void deleteNaturalLanguageUsage(String naturalLanguageUsageId)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.naturalLanguageUsageMap.remove(naturalLanguageUsageId) == null) {
            throw new RiceIllegalArgumentException(naturalLanguageUsageId);
        }
        return;
    }

    ////
    //// natural language translations
    ////
    @Override
    public String translateNaturalLanguageForObject(String naturalLanguageUsageId,
            String typeId,
            String krmsObjectId,
            String languageCode)
            throws RiceIllegalArgumentException {
        TranslationUtility util = new TranslationUtility(this, this.termRepositoryService, this.templater);
        return util.translateNaturalLanguageForObject(naturalLanguageUsageId, typeId, krmsObjectId, languageCode);
    }

    @Override
    public String translateNaturalLanguageForProposition(String naturalLanguageUsageId,
            PropositionDefinition proposition, String languageCode)
            throws RiceIllegalArgumentException {
        TranslationUtility util = new TranslationUtility(this, this.termRepositoryService, this.templater);
        return util.translateNaturalLanguageForProposition(naturalLanguageUsageId, proposition, languageCode);
    }

    @Override
    public NaturalLanguageTree translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
            PropositionDefinition propositionDefinintion,
            String languageCode) throws RiceIllegalArgumentException {
        TranslationUtility util = new TranslationUtility(this, this.termRepositoryService, this.templater);
        return util.translateNaturalLanguageTreeForProposition(naturalLanguageUsageId, propositionDefinintion, languageCode);
    }

    @Override
    public List<NaturalLanguageUsage> getNaturalLanguageUsagesByNamespace(String namespace) throws RiceIllegalArgumentException {
        List<NaturalLanguageUsage> list = new ArrayList<NaturalLanguageUsage>();
        for (NaturalLanguageUsage info : naturalLanguageUsageMap.values()) {
            if (namespace.equals(info.getNamespace())) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsageByNameAndNamespace(String name, String namespace) throws RiceIllegalArgumentException {
        for (NaturalLanguageUsage info : naturalLanguageUsageMap.values()) {
            if (namespace.equals(info.getNamespace())) {
                if (name.equals(info.getName())) {
                    return info;
                }
            }
        }
        return null;
    }

    @Override
    public ContextDefinition selectContext(ContextSelectionCriteria contextSelectionCriteria) throws RiceIllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AgendaTreeDefinition getAgendaTree(String agendaId) throws RiceIllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AgendaTreeDefinition> getAgendaTrees(List<String> agendaIds) throws RiceIllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ContextDefinition createContext(ContextDefinition contextDefinition)
            throws RiceIllegalArgumentException {
        // CREATE
        ContextDefinition orig = this.getContextByNameAndNamespace(contextDefinition.getName(), contextDefinition.getNamespace());
        if (orig != null) {
            throw new RiceIllegalArgumentException(contextDefinition.getNamespace() + "." + contextDefinition.getName());
        }
        ContextDefinition.Builder copy = ContextDefinition.Builder.create(contextDefinition);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        contextDefinition = copy.build();
        contextMap.put(contextDefinition.getId(), contextDefinition);
        return contextDefinition;
    }

    @Override
    public ContextDefinition findCreateContext(ContextDefinition contextDefinition) throws RiceIllegalArgumentException {
        ContextDefinition orig = this.getContextByNameAndNamespace(contextDefinition.getName(), contextDefinition.getNamespace());
        if (orig != null) {
            return orig;
        }
        return this.createContext(contextDefinition);
    }

    @Override
    public void updateContext(ContextDefinition contextDefinition)
            throws RiceIllegalArgumentException {
        // UPDATE
        ContextDefinition.Builder copy = ContextDefinition.Builder.create(contextDefinition);
        ContextDefinition old = this.getContext(contextDefinition.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        contextDefinition = copy.build();
        this.contextMap.put(contextDefinition.getId(), contextDefinition);
        return;
    }

    @Override
    public void deleteContext(String id)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.contextMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id);
        }
        return;
    }

    @Override
    public ContextDefinition getContext(String id)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.contextMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.contextMap.get(id);
    }

    @Override
    public ContextDefinition getContextByNameAndNamespace(String name, String namespace)
            throws RiceIllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new RiceIllegalArgumentException("name is null or empty");
        }
        if (namespace == null || namespace.trim().isEmpty()) {
            throw new RiceIllegalArgumentException("name is null or empty");
        }
        // RICE_GET_BY_NAMESPACE_AND_NAME
        for (ContextDefinition info : contextMap.values()) {
            if (name.equals(info.getName())) {
                if (namespace.equals(info.getNamespace())) {
                    return ContextDefinition.Builder.create(info).build();
                }
            }
        }
        return null;
    }

    @Override
    public NaturalLanguageTemplate createNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate)
            throws RiceIllegalArgumentException {
        // CREATE
        try {
            NaturalLanguageTemplate orig = this.getNaturalLanguageTemplate(naturalLanguageTemplate.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(naturalLanguageTemplate.getId());
            }
        } catch (RiceIllegalArgumentException ex) {
            // same as getting a null
        }
        NaturalLanguageTemplate.Builder copy = NaturalLanguageTemplate.Builder.create(naturalLanguageTemplate);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        naturalLanguageTemplate = copy.build();
        naturalLanguageTemplateMap.put(naturalLanguageTemplate.getId(), naturalLanguageTemplate);
        return naturalLanguageTemplate;
    }

    @Override
    public NaturalLanguageTemplate getNaturalLanguageTemplate(String naturalLanguageTemplateId)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.naturalLanguageTemplateMap.containsKey(naturalLanguageTemplateId)) {
            throw new RiceIllegalArgumentException(naturalLanguageTemplateId);
        }
        return this.naturalLanguageTemplateMap.get(naturalLanguageTemplateId);
    }

    @Override
    public void updateNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate)
            throws RiceIllegalArgumentException {
        // UPDATE
        NaturalLanguageTemplate.Builder copy = NaturalLanguageTemplate.Builder.create(naturalLanguageTemplate);
        NaturalLanguageTemplate old = this.getNaturalLanguageTemplate(naturalLanguageTemplate.getId());
        if (!old.getVersionNumber().equals(copy.getVersionNumber())) {
            throw new RiceIllegalStateException("" + old.getVersionNumber());
        }
        copy.setVersionNumber(copy.getVersionNumber() + 1);
        naturalLanguageTemplate = copy.build();
        this.naturalLanguageTemplateMap.put(naturalLanguageTemplate.getId(), naturalLanguageTemplate);
        return;
    }

    @Override
    public void deleteNaturalLanguageTemplate(String naturalLanguageTemplateId)
            throws RiceIllegalArgumentException {
        // DELETE
        if (this.naturalLanguageTemplateMap.remove(naturalLanguageTemplateId) == null) {
            throw new RiceIllegalArgumentException(naturalLanguageTemplateId);
        }
        return;
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByLanguageCode(String languageCode)
            throws RiceIllegalArgumentException {
        List<NaturalLanguageTemplate> list = new ArrayList<NaturalLanguageTemplate>();
        for (NaturalLanguageTemplate nlt : this.naturalLanguageTemplateMap.values()) {
            if (nlt.getLanguageCode().equals(languageCode)) {
                list.add(nlt);
            }
        }
        return list;
    }

    @Override
    public NaturalLanguageTemplate findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(String languageCode, String typeId, String naturalLanguageUsageId)
            throws RiceIllegalArgumentException {
        for (NaturalLanguageTemplate nlt : this.naturalLanguageTemplateMap.values()) {
            if (nlt.getLanguageCode().equals(languageCode)) {
                if (nlt.getTypeId().equals(typeId)) {
                    if (nlt.getNaturalLanguageUsageId().equals(naturalLanguageUsageId)) {
                        return nlt;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByNaturalLanguageUsage(String naturalLanguageUsageId)
            throws RiceIllegalArgumentException {
        List<NaturalLanguageTemplate> list = new ArrayList<NaturalLanguageTemplate>();
        for (NaturalLanguageTemplate nlt : this.naturalLanguageTemplateMap.values()) {
            if (nlt.getNaturalLanguageUsageId().equals(naturalLanguageUsageId)) {
                list.add(nlt);
            }
        }
        return list;
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByType(String typeId)
            throws RiceIllegalArgumentException {
        List<NaturalLanguageTemplate> list = new ArrayList<NaturalLanguageTemplate>();
        for (NaturalLanguageTemplate nlt : this.naturalLanguageTemplateMap.values()) {
            if (nlt.getTypeId().equals(typeId)) {
                list.add(nlt);
            }
        }
        return list;
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByTemplate(String template)
            throws RiceIllegalArgumentException {
        List<NaturalLanguageTemplate> list = new ArrayList<NaturalLanguageTemplate>();
        for (NaturalLanguageTemplate nlt : this.naturalLanguageTemplateMap.values()) {
            if (nlt.getTemplate().equals(template)) {
                list.add(nlt);
            }
        }
        return list;
    }

    @Override
    public List<String> findContextIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<ContextDefinition> instance = new CriteriaMatcherInMemory<ContextDefinition>();
        instance.setCriteria(queryByCriteria);
        Collection<ContextDefinition> selected = instance.findMatching(this.contextMap.values());
        List<String> list = new ArrayList<String>();
        for (ContextDefinition sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }

    @Override
    public List<String> findAgendaIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<AgendaDefinition> instance = new CriteriaMatcherInMemory<AgendaDefinition>();
        instance.setCriteria(queryByCriteria);
        Collection<AgendaDefinition> selected = instance.findMatching(this.agendaMap.values());
        List<String> list = new ArrayList<String>();
        for (AgendaDefinition sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }

    @Override
    public List<String> findRuleIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<RuleDefinition> instance = new CriteriaMatcherInMemory<RuleDefinition>();
        instance.setCriteria(queryByCriteria);
        Collection<RuleDefinition> selected = instance.findMatching(this.ruleMap.values());
        List<String> list = new ArrayList<String>();
        for (RuleDefinition sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }

    @Override
    public List<String> findActionIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<ActionDefinition> instance = new CriteriaMatcherInMemory<ActionDefinition>();
        instance.setCriteria(queryByCriteria);
        Collection<ActionDefinition> selected = instance.findMatching(this.actionMap.values());
        List<String> list = new ArrayList<String>();
        for (ActionDefinition sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }

    @Override
    public List<String> findPropositionIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        CriteriaMatcherInMemory<PropositionDefinition> instance = new CriteriaMatcherInMemory<PropositionDefinition>();
        instance.setCriteria(queryByCriteria);
        Collection<PropositionDefinition> selected = instance.findMatching(this.propositionMap.values());
        List<String> list = new ArrayList<String>();
        for (PropositionDefinition sel : selected) {
            list.add(sel.getId());
        }
        return list;
    }
}
