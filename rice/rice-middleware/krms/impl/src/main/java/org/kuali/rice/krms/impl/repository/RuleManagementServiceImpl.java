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
package org.kuali.rice.krms.impl.repository;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.NaturalLanguageTree;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.TranslateBusinessMethods;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.impl.repository.language.SimpleNaturalLanguageTemplater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

/**
 * The implementation of {@link RuleManagementService} operations facilitate management of rules and
 * associated information.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleManagementServiceImpl extends RuleRepositoryServiceImpl implements RuleManagementService {

    private ReferenceObjectBindingBoService referenceObjectBindingBoService = new ReferenceObjectBindingBoServiceImpl();
    private AgendaBoService agendaBoService = new AgendaBoServiceImpl();
    private RuleBoService ruleBoService = new RuleBoServiceImpl();
    private ActionBoService actionBoService = new ActionBoServiceImpl();
    private PropositionBoService propositionBoService = new PropositionBoServiceImpl();
    private NaturalLanguageUsageBoService naturalLanguageUsageBoService = new NaturalLanguageUsageBoServiceImpl();
    private NaturalLanguageTemplateBoService naturalLanguageTemplateBoService = new NaturalLanguageTemplateBoServiceImpl();
    private ContextBoService contextBoService = new ContextBoServiceImpl();
    private NaturalLanguageTemplaterContract templater = new SimpleNaturalLanguageTemplater();
    private TermRepositoryService termRepositoryService = new TermBoServiceImpl();
    private SequenceAccessorService sequenceAccessorService = null;
    private TranslateBusinessMethods translationBusinessMethods = null;

    /**
     * Fields to ignore when calling reflection-based equals in
     * {@link #isSame(org.kuali.rice.krms.api.repository.agenda.AgendaDefinition, org.kuali.rice.krms.api.repository.agenda.AgendaDefinition)}
     */
    private static final List<String> isSameIgnoreFields = Arrays.asList(
            AgendaDefinition.Elements.AGENDA_ID,
            CoreConstants.CommonElements.VERSION_NUMBER,
            CoreConstants.CommonElements.FUTURE_ELEMENTS
    );

    /**
     * get the {@link ReferenceObjectBindingBoService}
     * @return the {@link ReferenceObjectBindingBoService}
     */
    public ReferenceObjectBindingBoService getReferenceObjectBindingBoService() {
        return referenceObjectBindingBoService;
    }

    /**
     * set the {@link ReferenceObjectBindingBoService}
     * @param referenceObjectBindingBoService the {@link ReferenceObjectBindingBoService} to set
     */
    public void setReferenceObjectBindingBoService(ReferenceObjectBindingBoService referenceObjectBindingBoService) {
        this.referenceObjectBindingBoService = referenceObjectBindingBoService;
    }

    /**
     * get the {@link AgendaBoService}
     * @return the {@link AgendaBoService}
     */
    public AgendaBoService getAgendaBoService() {
        return agendaBoService;
    }

    /**
     * set the {@link AgendaBoService}
     * @param agendaBoService the {@link AgendaBoService} to set
     */
    public void setAgendaBoService(AgendaBoService agendaBoService) {
        this.agendaBoService = agendaBoService;
    }

    /**
     * get the {@link RuleBoService}
     * @return the {@link RuleBoService}
     */
    public RuleBoService getRuleBoService() {
        return ruleBoService;
    }

    /**
     * set the {@link RuleBoService}
     * @param ruleBoService the {@link RuleBoService} to set
     */
    public void setRuleBoService(RuleBoService ruleBoService) {
        this.ruleBoService = ruleBoService;
    }

    /**
     * get the {@link PropositionBoService}
     * @return the {@link PropositionBoService}
     */
    public PropositionBoService getPropositionBoService() {
        return propositionBoService;
    }

    /**
     * set the {@link PropositionBoService}
     * @param propositionBoService the {@link PropositionBoService} to set
     */
    public void setPropositionBoService(PropositionBoService propositionBoService) {
        this.propositionBoService = propositionBoService;
    }

    /**
     * set the {@link NaturalLanguageUsageBoService}
     * @return the {@link NaturalLanguageUsageBoService}
     */
    public NaturalLanguageUsageBoService getNaturalLanguageUsageBoService() {
        return naturalLanguageUsageBoService;
    }

    /**
     * set the {@link NaturalLanguageUsageBoService}
     * @param naturalLanguageUsageBoService the {@link NaturalLanguageUsageBoService} to set
     */
    public void setNaturalLanguageUsageBoService(NaturalLanguageUsageBoService naturalLanguageUsageBoService) {
        this.naturalLanguageUsageBoService = naturalLanguageUsageBoService;
    }

    /**
     * get the {@link NaturalLanguageTemplateBoService}
     * @return the {@link NaturalLanguageTemplateBoService}
     */
    public NaturalLanguageTemplateBoService getNaturalLanguageTemplateBoService() {
        return naturalLanguageTemplateBoService;
    }

    /**
     * set the {@link NaturalLanguageTemplateBoService}
     * @param naturalLanguageTemplateBoService the {@link NaturalLanguageTemplateBoService} to set
     */
    public void setNaturalLanguageTemplateBoService(NaturalLanguageTemplateBoService naturalLanguageTemplateBoService) {
        this.naturalLanguageTemplateBoService = naturalLanguageTemplateBoService;
    }

    /**
     * get the {@link ContextBoService}
     * @return the {@link ContextBoService}
     */
    public ContextBoService getContextBoService() {
        return contextBoService;
    }

    /**
     * set the {@link ContextBoService}
     * @param contextBoService the {@link ContextBoService} to set
     */
    public void setContextBoService(ContextBoService contextBoService) {
        this.contextBoService = contextBoService;
    }

    /**
     * get the {@link ActionBoService}
     * @return the {@link ActionBoService}
     */
    public ActionBoService getActionBoService() {
        return actionBoService;
    }

    /**
     * Set the {@link ActionBoService}
     * @param actionBoService the {@link ActionBoService} to set
     */
    public void setActionBoService(ActionBoService actionBoService) {
        this.actionBoService = actionBoService;
    }

    /**
     * get the {@link NaturalLanguageTemplaterContract}
     * @return the {@link NaturalLanguageTemplaterContract}
     */
    public NaturalLanguageTemplaterContract getTemplater() {
        return templater;
    }

    /**
     * set the {@link NaturalLanguageTemplaterContract}
     * @param templater the {@link NaturalLanguageTemplaterContract} to set
     */
    public void setTemplater(NaturalLanguageTemplaterContract templater) {
        this.templater = templater;
    }

    /**
     * get the {@link TermRepositoryService}
     * @return the {@link TermRepositoryService}
     */
    public TermRepositoryService getTermRepositoryService() {
        return termRepositoryService;
    }

    /**
     * Set the {@link TermRepositoryService}
     * @param termRepositoryService the {@link TermRepositoryService} to set
     */
    public void setTermRepositoryService(TermRepositoryService termRepositoryService) {
        this.termRepositoryService = termRepositoryService;
    }

    /**
     * get the {@link SequenceAccessorService}
     * @return the {@link SequenceAccessorService}
     */
    public SequenceAccessorService getSequenceAccessorService() {
        if (this.sequenceAccessorService == null) {
            this.sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        return sequenceAccessorService;
    }

    /**
     * set the {@link SequenceAccessorService}
     * @param sequenceAccessorService the {@link SequenceAccessorService} to set
     */
    public void setSequenceAccessorService(SequenceAccessorService sequenceAccessorService) {
        this.sequenceAccessorService = sequenceAccessorService;
    }

    /**
     * get the {@link TranslateBusinessMethods}
     * @return the current {@link TranslateBusinessMethods}
     */
    public TranslateBusinessMethods getTranslateBusinessMethods() {
        if(translationBusinessMethods  == null) {
            this.translationBusinessMethods  = new TranslationUtility(this, getTermRepositoryService(), this.templater);
        }
        return this.translationBusinessMethods ;
    }

    /**
     * set the {@link TranslateBusinessMethods}
     * @param translationBusinessMethods the {@link TranslateBusinessMethods} to set
     */
    public void setTranslationBusinessMethods (TranslateBusinessMethods  translationBusinessMethods) {
        this.translationBusinessMethods  = translationBusinessMethods;
    }

    ////
    //// reference object binding methods
    ////

    @Override
    public ReferenceObjectBinding createReferenceObjectBinding(ReferenceObjectBinding referenceObjectDefinition)
            throws RiceIllegalArgumentException {
        //Set the id if it doesn't exist.
        if (referenceObjectDefinition.getId() == null) {
            String referenceObjectBindingId = getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_REF_OBJ_KRMS_OBJ_S", ReferenceObjectBindingBo.class).toString();
            ReferenceObjectBinding.Builder refBldr = ReferenceObjectBinding.Builder.create(referenceObjectDefinition);
            refBldr.setId(referenceObjectBindingId);
            referenceObjectDefinition = refBldr.build();
        }

        return referenceObjectBindingBoService.createReferenceObjectBinding(referenceObjectDefinition);
    }

    @Override
    public ReferenceObjectBinding getReferenceObjectBinding(String id) throws RiceIllegalArgumentException {
        return referenceObjectBindingBoService.getReferenceObjectBinding(id);
    }

    @Override
    public List<ReferenceObjectBinding> getReferenceObjectBindings(List<String> ids) throws RiceIllegalArgumentException {
        if (ids == null) {
            throw new IllegalArgumentException("reference binding object ids must not be null");
        }

        // Fetch BOs
        List<ReferenceObjectBindingBo> bos = null;

        if (ids.size() == 0) {
            bos = Collections.emptyList();
        } else {
            QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
            List<Predicate> pList = new ArrayList<Predicate>();
            qBuilder.setPredicates(in("id", ids.toArray()));
            GenericQueryResults<ReferenceObjectBindingBo> results = getCriteriaLookupService().lookup(ReferenceObjectBindingBo.class, qBuilder.build());

            bos = results.getResults();
        }

        // Translate BOs
        List<ReferenceObjectBinding> bindings = new LinkedList<ReferenceObjectBinding>();
        for (ReferenceObjectBindingBo bo : bos) {
            ReferenceObjectBinding binding = ReferenceObjectBindingBo.to(bo);
            bindings.add(binding);
        }

        return Collections.unmodifiableList(bindings);
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceObject(String referenceObjectReferenceDiscriminatorType,
            String referenceObjectId)
            throws RiceIllegalArgumentException {
        if (referenceObjectReferenceDiscriminatorType == null) {
            throw new RiceIllegalArgumentException("reference binding object discriminator type must not be null");
        }

        if (referenceObjectId == null) {
            throw new RiceIllegalArgumentException("reference object id must not be null");
        }

        List<ReferenceObjectBinding> list = new ArrayList<ReferenceObjectBinding>();
        for (ReferenceObjectBinding binding : this.referenceObjectBindingBoService.findReferenceObjectBindingsByReferenceObject(referenceObjectId)) {
            if (binding.getReferenceDiscriminatorType().equals(referenceObjectReferenceDiscriminatorType)) {
                list.add(binding);
            }
        }

        return list;
    }
    
    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceDiscriminatorType(String referenceObjectReferenceDiscriminatorType) throws RiceIllegalArgumentException {
        return referenceObjectBindingBoService.findReferenceObjectBindingsByReferenceDiscriminatorType(referenceObjectReferenceDiscriminatorType);
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsDiscriminatorType(String referenceObjectKrmsDiscriminatorType) throws RiceIllegalArgumentException {
        return referenceObjectBindingBoService.findReferenceObjectBindingsByKrmsDiscriminatorType(referenceObjectKrmsDiscriminatorType);
    }

    @Override
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsObject(String krmsObjectId) throws RiceIllegalArgumentException {
        return referenceObjectBindingBoService.findReferenceObjectBindingsByKrmsObject(krmsObjectId);
    }

    @Override
    public void updateReferenceObjectBinding(ReferenceObjectBinding referenceObjectBindingDefinition)
            throws RiceIllegalArgumentException {
        referenceObjectBindingBoService.updateReferenceObjectBinding(referenceObjectBindingDefinition);
    }

    @Override
    public void deleteReferenceObjectBinding(String id) throws RiceIllegalArgumentException {
        referenceObjectBindingBoService.deleteReferenceObjectBinding(id);
    }

    @Override
    public List<String> findReferenceObjectBindingIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        return referenceObjectBindingBoService.findReferenceObjectBindingIds(queryByCriteria);
    }

    ////
    //// agenda
    //// 
    @Override
    public AgendaDefinition createAgenda(AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException {
        return agendaBoService.createAgenda(agendaDefinition);
    }

    @Override
    public AgendaDefinition getAgendaByNameAndContextId(String name, String contextId) {
        return this.agendaBoService.getAgendaByNameAndContextId(name, contextId);
    }

    @Override
    public AgendaDefinition findCreateAgenda(AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException {
        AgendaDefinition existing = this.getAgendaByNameAndContextId(agendaDefinition.getName(), agendaDefinition.getContextId());

        if (existing != null) {
            existing = this.updateAgendaIfNeeded(agendaDefinition, existing);
            return existing;
        }

        return this.createAgenda(agendaDefinition);
    }

    private AgendaDefinition updateAgendaIfNeeded(AgendaDefinition agenda, AgendaDefinition existing) {
        if (this.isSame(agenda, existing)) {
            return existing;
        }

        AgendaDefinition.Builder bldr = AgendaDefinition.Builder.create(existing);
        bldr.setActive(agenda.isActive());
        bldr.setAttributes(agenda.getAttributes());
        bldr.setContextId(agenda.getContextId());

        if (agenda.getFirstItemId() != null) {
            bldr.setFirstItemId(agenda.getFirstItemId());
        }

        bldr.setTypeId(agenda.getTypeId());
        this.updateAgenda(bldr.build());

        return this.getAgenda(existing.getId());
    }

    /**
     * Compares two {@link AgendaDefinition}s for equality, ignoring certain fields defined by
     * {@link #isSameIgnoreFields}.
     *
     * @param agenda one agenda
     * @param existing another agenda
     * @return true if the two agendas are considered to be the same
     */
    private boolean isSame(AgendaDefinition agenda, AgendaDefinition existing) {
        return EqualsBuilder.reflectionEquals(agenda, existing, isSameIgnoreFields);
    }

    @Override
    public AgendaDefinition getAgenda(String id) throws RiceIllegalArgumentException {
        return agendaBoService.getAgendaByAgendaId(id);
    }

    @Override
    public List<AgendaDefinition> getAgendasByContext(String contextId) throws RiceIllegalArgumentException {
        return agendaBoService.getAgendasByContextId(contextId);
    }

    @Override
    public void updateAgenda(AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException {
        agendaBoService.updateAgenda(agendaDefinition);
    }

    @Override
    public void deleteAgenda(String id) throws RiceIllegalArgumentException {
        agendaBoService.deleteAgenda(id);
    }

    @Override
    public List<AgendaDefinition> getAgendasByType(String typeId) throws RiceIllegalArgumentException {
        return agendaBoService.getAgendasByType(typeId);
    }

    @Override
    public List<AgendaDefinition> getAgendasByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException {
        return agendaBoService.getAgendasByTypeAndContext(typeId, contextId);
    }

    ////
    //// agenda item methods
    ////
    @Override
    public AgendaItemDefinition createAgendaItem(AgendaItemDefinition agendaItemDefinition) throws RiceIllegalArgumentException {
        agendaItemDefinition = createUpdateAgendaItemIfNeeded(agendaItemDefinition);
        return agendaBoService.createAgendaItem(agendaItemDefinition);
    }

    private AgendaItemDefinition createUpdateAgendaItemIfNeeded(AgendaItemDefinition agendaItemDefinition) {
        this.crossCheckRuleId(agendaItemDefinition);
        this.crossCheckWhenTrueId(agendaItemDefinition);
        this.crossCheckWhenFalseId(agendaItemDefinition);
        this.crossCheckAlwaysId(agendaItemDefinition);
        this.crossCheckSubAgendaId(agendaItemDefinition);
        agendaItemDefinition = createUpdateRuleIfNeeded(agendaItemDefinition);
        agendaItemDefinition = createWhenTrueAgendaItemIfNeeded(agendaItemDefinition);
        agendaItemDefinition = createWhenFalseAgendaItemIfNeeded(agendaItemDefinition);
        agendaItemDefinition = createAlwaysAgendaItemIfNeeded(agendaItemDefinition);
        return createSubAgendaIfNeeded(agendaItemDefinition);
    }

    private void crossCheckRuleId(AgendaItemDefinition agendItemDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (agendItemDefinition.getRuleId() != null && agendItemDefinition.getRule() != null) {
            if (!agendItemDefinition.getRuleId().equals(agendItemDefinition.getRule().getId())) {
                throw new RiceIllegalArgumentException("ruleId does not rule.getId" + agendItemDefinition.getRuleId() + " " + agendItemDefinition.getRule().getId());
            }
        }
    }

    private void crossCheckWhenTrueId(AgendaItemDefinition agendaItemDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (agendaItemDefinition.getWhenTrueId()!= null && agendaItemDefinition.getWhenTrue() != null) {
            if (!agendaItemDefinition.getWhenTrueId().equals(agendaItemDefinition.getWhenTrue().getId())) {
                throw new RiceIllegalArgumentException("when true id does not match " + agendaItemDefinition.getWhenTrueId() + " " + agendaItemDefinition.getWhenTrue().getId());
            }
        }
    }

    private void crossCheckWhenFalseId(AgendaItemDefinition agendItemDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (agendItemDefinition.getWhenFalseId()!= null && agendItemDefinition.getWhenFalse() != null) {
            if (!agendItemDefinition.getWhenFalseId().equals(agendItemDefinition.getWhenFalse().getId())) {
                throw new RiceIllegalArgumentException("when false id does not match " + agendItemDefinition.getWhenFalseId() + " " + agendItemDefinition.getWhenFalse().getId());
            }
        }
    }

    private void crossCheckAlwaysId(AgendaItemDefinition agendItemDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (agendItemDefinition.getAlwaysId()!= null && agendItemDefinition.getAlways() != null) {
            if (!agendItemDefinition.getAlwaysId().equals(agendItemDefinition.getAlways().getId())) {
                throw new RiceIllegalArgumentException("Always id does not match " + agendItemDefinition.getAlwaysId() + " " + agendItemDefinition.getAlways().getId());
            }
        }
    }

    private void crossCheckSubAgendaId(AgendaItemDefinition agendItemDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (agendItemDefinition.getSubAgendaId()!= null && agendItemDefinition.getSubAgenda() != null) {
            if (!agendItemDefinition.getSubAgendaId().equals(agendItemDefinition.getSubAgenda().getId())) {
                throw new RiceIllegalArgumentException("SubAgenda id does not match " + agendItemDefinition.getSubAgendaId() + " " + agendItemDefinition.getSubAgenda().getId());
            }
        }
    }

    private AgendaItemDefinition createUpdateRuleIfNeeded(AgendaItemDefinition agendaItemDefinition)
            throws RiceIllegalArgumentException {
        // no rule to create
        if (agendaItemDefinition.getRule() == null) {
            return agendaItemDefinition;
        }

        // update
        if (agendaItemDefinition.getRule().getId() != null) {
            this.updateRule(agendaItemDefinition.getRule());
            RuleDefinition rule = this.getRule(agendaItemDefinition.getRule ().getId());
            AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
            agendaItemBuilder.setRule(RuleDefinition.Builder.create(rule));
            agendaItemBuilder.setRuleId(rule.getId());
            
            return agendaItemBuilder.build();
        }

        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        RuleDefinition ruleDefinition = this.createRule(agendaItemDefinition.getRule());
        RuleDefinition.Builder ruleBuilder = RuleDefinition.Builder.create(ruleDefinition);
        agendaItemBuilder.setRule(ruleBuilder);
        agendaItemBuilder.setRuleId(ruleBuilder.getId());

        return agendaItemBuilder.build();
    }

    private AgendaItemDefinition createWhenTrueAgendaItemIfNeeded(AgendaItemDefinition agendaItemDefinition) {
        // nothing to create
        if (agendaItemDefinition.getWhenTrue() == null) {
            return agendaItemDefinition;
        }

        // ojb will take care of it if it has already been created
        if (agendaItemDefinition.getWhenTrue().getId() != null) {
            return agendaItemDefinition;
        }

        // ojb does not take care of terms and termparameters, recursively loop thru agendaitems to update terms
        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        AgendaItemDefinition subAgendaItem = this.createUpdateAgendaItemIfNeeded(agendaItemDefinition.getWhenTrue());
        agendaItemBuilder.setWhenTrue(AgendaItemDefinition.Builder.create(subAgendaItem));
        agendaItemBuilder.setWhenTrueId(subAgendaItem.getId());

        return agendaItemBuilder.build();
    }


    private AgendaItemDefinition createWhenFalseAgendaItemIfNeeded(AgendaItemDefinition agendaItemDefinition) {
        // nothing to create
        if (agendaItemDefinition.getWhenFalse() == null) {
            return agendaItemDefinition;
        }

        // ojb will take care of it if it has already been created
        if (agendaItemDefinition.getWhenFalse().getId() != null) {
            return agendaItemDefinition;
        }

        // ojb does not take care of terms and termparameters, recursively loop thru agendaitems to update terms
        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        AgendaItemDefinition subAgendaItem = this.createUpdateAgendaItemIfNeeded(agendaItemDefinition.getWhenFalse());
        agendaItemBuilder.setWhenFalse(AgendaItemDefinition.Builder.create(subAgendaItem));
        agendaItemBuilder.setWhenFalseId(subAgendaItem.getId());
        return agendaItemBuilder.build();
    }
    

    private AgendaItemDefinition createAlwaysAgendaItemIfNeeded(AgendaItemDefinition agendaItemDefinition) {
        // nothing to create
        if (agendaItemDefinition.getAlways()== null) {
            return agendaItemDefinition;
        } 
        // ojb will take care of it if it has already been created
        if (agendaItemDefinition.getAlways().getId() != null) {
            return agendaItemDefinition;
        }
        
        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        AgendaItemDefinition subAgendaItem = this.createAgendaItem(agendaItemDefinition.getAlways());
        agendaItemBuilder.setAlways(AgendaItemDefinition.Builder.create(subAgendaItem));
        agendaItemBuilder.setAlwaysId(subAgendaItem.getId());

        return agendaItemBuilder.build();
    }

    private AgendaItemDefinition createSubAgendaIfNeeded(AgendaItemDefinition agendaItemDefinition) {
        // nothing to create
        if (agendaItemDefinition.getSubAgenda() == null) {
            return agendaItemDefinition;
        }

        // ojb will take care of it if it has already been created
        if (agendaItemDefinition.getSubAgenda().getId() != null) {
            return agendaItemDefinition;
        }

        AgendaItemDefinition.Builder agendaItemBuilder = AgendaItemDefinition.Builder.create(agendaItemDefinition);
        AgendaDefinition subAgenda = this.createAgenda(agendaItemDefinition.getSubAgenda());
        agendaItemBuilder.setSubAgenda(AgendaDefinition.Builder.create(subAgenda));
        agendaItemBuilder.setSubAgendaId(subAgenda.getId());

        return agendaItemBuilder.build();
    }

    @Override
    public AgendaItemDefinition getAgendaItem(String id) throws RiceIllegalArgumentException {
        AgendaItemDefinition agendaItem = agendaBoService.getAgendaItemById(id);

        if (agendaItem != null) {
            return setTermValuesForAgendaItem(agendaItem).build();
        }

        return agendaItem;
    }

    private AgendaItemDefinition.Builder setTermValuesForAgendaItem(AgendaItemDefinition agendaItem) {
        if (agendaItem == null){
            return null;
        }

        AgendaItemDefinition.Builder itemBuiler = AgendaItemDefinition.Builder.create(agendaItem);
        if (itemBuiler.getRule() != null) {
            // Set the termValues on the agenda item.
            PropositionDefinition proposition = agendaItem.getRule().getProposition();
            if (proposition != null) {
                proposition = this.orderCompoundPropositionsIfNeeded(proposition);
                itemBuiler.getRule().setProposition(this.replaceTermValues(proposition));
            }
        }

        // Recursively set the term values on child agenda items.
        itemBuiler.setWhenTrue(setTermValuesForAgendaItem(agendaItem.getWhenTrue()));
        itemBuiler.setWhenFalse(setTermValuesForAgendaItem(agendaItem.getWhenFalse()));
        itemBuiler.setAlways(setTermValuesForAgendaItem(agendaItem.getAlways()));

        return itemBuiler;
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByType(String typeId)
            throws RiceIllegalArgumentException {
        return agendaBoService.getAgendaItemsByType(typeId);
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByContext(String contextId) throws RiceIllegalArgumentException {
        return agendaBoService.getAgendaItemsByContext(contextId);
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByTypeAndContext(String typeId, String contextId)
            throws RiceIllegalArgumentException {
        return agendaBoService.getAgendaItemsByTypeAndContext(typeId, contextId);
    }

    @Override
    public void deleteAgendaItem(String id) throws RiceIllegalArgumentException {
        agendaBoService.deleteAgendaItem(id);
    }

    @Override
    public void updateAgendaItem(AgendaItemDefinition agendaItemDefinition) throws RiceIllegalArgumentException {
        agendaItemDefinition = createUpdateAgendaItemIfNeeded(agendaItemDefinition);
        agendaBoService.updateAgendaItem(agendaItemDefinition);
    }

    private void crossCheckPropId(RuleDefinition ruleDefinition)
            throws RiceIllegalArgumentException {
        // if both are set they better match
        if (ruleDefinition.getPropId() != null && ruleDefinition.getProposition() != null) {
            if (!ruleDefinition.getPropId().equals(ruleDefinition.getProposition().getId())) {
                throw new RiceIllegalArgumentException("propId does not match proposition.getId" + ruleDefinition.getPropId() + " " + ruleDefinition.getProposition().getId());
            }
        }
    }

    ////
    //// rule methods
    ////
    @Override
    public RuleDefinition getRuleByNameAndNamespace(String name, String namespace) {
        return this.ruleBoService.getRuleByNameAndNamespace(name, namespace);
    }


    @Override
    public RuleDefinition createRule(RuleDefinition ruleDefinition) throws RiceIllegalArgumentException {
        if (ruleDefinition.getId() != null) {
            RuleDefinition orig = this.getRule(ruleDefinition.getId());

            if (orig != null) {
                throw new RiceIllegalArgumentException(ruleDefinition.getId());
            }
        } else {
            // if no id then set it because it is needed to store propositions connected to this rule
            String ruleId = getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_RULE_S", RuleBo.class).toString();
            RuleDefinition.Builder ruleBldr = RuleDefinition.Builder.create(ruleDefinition);
            ruleBldr.setId(ruleId);
            ruleDefinition = ruleBldr.build();
        }

        // if both are set they better match
        crossCheckPropId(ruleDefinition);
        ruleDefinition = this.createUpdatePropositionIfNeeded(ruleDefinition);
        ruleDefinition = ruleBoService.createRule(ruleDefinition);

        return ruleDefinition;
    }

    private RuleDefinition createRuleIfNeeded(RuleDefinition ruleDefinition) throws RiceIllegalArgumentException {

        // no rule to create
        if (ruleDefinition == null) {
            return null;
        }

        // if no id then set it because it is needed to store propositions connected to this rule
        if (ruleDefinition.getId() == null) {
            String ruleId = getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_RULE_S", RuleBo.class).toString();
            RuleDefinition.Builder ruleBldr = RuleDefinition.Builder.create(ruleDefinition);
            ruleBldr.setId(ruleId);
            ruleDefinition = ruleBldr.build();
        }

        // if both are set they better match
        crossCheckPropId(ruleDefinition);
        ruleDefinition = this.createUpdatePropositionIfNeeded(ruleDefinition);

        return ruleDefinition;
    }

    private RuleDefinition createUpdatePropositionIfNeeded(RuleDefinition rule) {
        // no prop to create
        if (rule.getProposition() == null) {
            return rule;
        }

        // update 
        if (rule.getProposition().getId() != null) {
            this.updateProposition(rule.getProposition());
            PropositionDefinition prop = this.getProposition(rule.getProposition().getId());
            RuleDefinition.Builder ruleBldr = RuleDefinition.Builder.create(rule);
            ruleBldr.setProposition(PropositionDefinition.Builder.create(prop));
            ruleBldr.setPropId(prop.getId());
            return ruleBldr.build();
        }

        // create the proposition
        RuleDefinition.Builder ruleBldr = RuleDefinition.Builder.create(rule);
        PropositionDefinition propositionDefinition = null;
        // ojb will take care of props that have already been created, but we still need to take care of the terms.
        PropositionDefinition.Builder propBldr = ruleBldr.getProposition();

        if (rule.getProposition().getId() != null) {
            this.crossCheckPropositionParameters(rule.getProposition());
            propBldr = setSequenceOnCompoundPropositionsIfNeeded(propBldr);
            propBldr = maintainTermValuesAndChildPropositions(propBldr);
        } else {
            // create the proposition
            propBldr.setRule(ruleBldr);
            propositionDefinition = this.createProposition(propBldr.build());
            propBldr = PropositionDefinition.Builder.create(propositionDefinition);
        }

        // now update the rule so it holds the proposition id
        ruleBldr.setProposition(propBldr);

        return ruleBldr.build();
    }

    @Override
    public void updateRule(RuleDefinition ruleDefinition) throws RiceIllegalArgumentException {
        crossCheckPropId(ruleDefinition);
        ruleDefinition = this.createUpdatePropositionIfNeeded(ruleDefinition);

        ruleBoService.updateRule(ruleDefinition);
    }

    @Override
    public void deleteRule(String id) throws RiceIllegalArgumentException {
        ruleBoService.deleteRule(id);
    }

    ////
    //// action methods
    ////
    @Override
    public ActionDefinition createAction(ActionDefinition actionDefinition) throws RiceIllegalArgumentException {
        return actionBoService.createAction(actionDefinition);
    }

    @Override
    public void updateAction(ActionDefinition actionDefinition) throws RiceIllegalArgumentException {
        actionBoService.updateAction(actionDefinition);
    }

    @Override
    public void deleteAction(String id) throws RiceIllegalArgumentException {
        throw new RiceIllegalArgumentException("not implemented yet because not supported by the bo service");
//        actionBoService.deleteAction(id);
    }

    @Override
    public ActionDefinition getAction(String actionId) {
        return actionBoService.getActionByActionId(actionId);
    }

    @Override
    public List<ActionDefinition> getActions(List<String> actionIds) {
        // TODO: implement this more efficiently by adding the bulk op to the bo service and calling it
        List<ActionDefinition> list = new ArrayList<ActionDefinition>();

        for (String id : actionIds) {
            list.add(this.getAction(id));
        }

        return list;
    }

    private void crossCheckPropositionParameters(PropositionDefinition propositionDefinition) {
        // check that if the value and termValue are both supplied that they match
        for (PropositionParameter param : propositionDefinition.getParameters()) {
            if (param.getValue() != null && param.getTermValue() != null) {
                if (!param.getValue().equals(param.getTermValue().getId())) {
                    throw new RiceIllegalArgumentException("value does not match termValue.id on param "
                            + param.getSequenceNumber() + " " + param.getValue() + " " + param.getTermValue().getId());
                }
            }
        }
    }


    ////
    //// proposition methods
    ////
    @Override
    public PropositionDefinition createProposition(PropositionDefinition propositionDefinition) throws RiceIllegalArgumentException {
        // CREATE
        if (propositionDefinition.getId() != null) {
            PropositionDefinition orig = this.getProposition(propositionDefinition.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(propositionDefinition.getId());
            }
        }

        crossCheckPropositionParameters(propositionDefinition);
        PropositionDefinition.Builder propBldr = PropositionDefinition.Builder.create(propositionDefinition);
        propBldr = setSequenceOnCompoundPropositionsIfNeeded(propBldr);
        propBldr = maintainTermValuesAndChildPropositions(propBldr);
        PropositionDefinition prop = propositionBoService.createProposition(propBldr.build());

        return prop;
    }

    private PropositionDefinition.Builder maintainTermValuesAndChildPropositions(PropositionDefinition.Builder propBldr) {
        if (PropositionType.SIMPLE.getCode().equalsIgnoreCase(propBldr.getPropositionTypeCode())) {
            return maintainTermValues(propBldr);
        } else {
            return createChildPropsIfNeeded(propBldr);
        }
    }

    private PropositionDefinition.Builder maintainTermValues(PropositionDefinition.Builder propBldr) {
        if (propBldr.getParameters() == null) {
            return propBldr;
        }

        if (propBldr.getParameters().isEmpty()) {
            return propBldr;
        }

        boolean updated = false;
        List<PropositionParameter.Builder> paramBldrs = new ArrayList<PropositionParameter.Builder> ();

        for (PropositionParameter.Builder paramBldr : propBldr.getParameters()) {
            paramBldrs.add(paramBldr);

            // link the param the proposition's id
            // not sure we need to do this but...
            if (paramBldr.getPropId() == null) {
                paramBldr.setPropId(propBldr.getId());
                updated = true;
            }

            // create the termvalue if it was specified
            if (paramBldr.getTermValue() != null) {
                TermDefinition termValue = paramBldr.getTermValue();

                // no id means it does not exist yet
                if (termValue.getId() == null) {
                    termValue = this.termRepositoryService.createTerm(termValue);
                    paramBldr.setTermValue(termValue);
                    updated = true;
                } else {
                    this.termRepositoryService.updateTerm(termValue);
                }
                
                if ((paramBldr.getValue() == null) || (!paramBldr.getValue().equals(termValue.getId()))) {
                    paramBldr.setValue(termValue.getId());
                    updated = true;
                }
            }
        }

        if (!updated) {
            return propBldr;
        }

        propBldr.setParameters(paramBldrs);
        return propBldr;
    }

    private PropositionDefinition.Builder createChildPropsIfNeeded(PropositionDefinition.Builder propBldr) {
        if (propBldr.getCompoundComponents() == null) {
            return propBldr;
        }

        if (propBldr.getCompoundComponents().isEmpty()) {
            return propBldr;
        }

        List<PropositionDefinition.Builder> childPropBldrs = new ArrayList<PropositionDefinition.Builder>();

        for (PropositionDefinition.Builder compPropBldr : propBldr.getCompoundComponents()) {
            compPropBldr.setRuleId(propBldr.getRuleId());
            propBldr = setSequenceOnCompoundPropositionsIfNeeded(propBldr);
            compPropBldr = maintainTermValuesAndChildPropositions(compPropBldr);
            childPropBldrs.add(compPropBldr);
        }

        propBldr.setCompoundComponents(childPropBldrs);

        return propBldr;
    }

    @Override
    public PropositionDefinition getProposition(String id) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(id)) {
            throw new RiceIllegalArgumentException("Proposition id must not be null or blank");
        }

        PropositionDefinition proposition = propositionBoService.getPropositionById(id);

        if (proposition == null) {
            return null;
        }

        proposition = this.replaceTermValues(proposition).build();
        proposition = this.orderCompoundPropositionsIfNeeded(proposition);

        return proposition;
    }

    private PropositionDefinition orderCompoundPropositionsIfNeeded(PropositionDefinition prop) {
        if (!prop.getPropositionTypeCode().equals(PropositionType.COMPOUND.getCode())) {
            return prop;
        }

        if (prop.getCompoundComponents() == null) {
            return prop;
        }

        if (prop.getCompoundComponents().size() <= 1) {
            return prop;
        }

        PropositionDefinition.Builder propBldr = PropositionDefinition.Builder.create(prop);        
        List<PropositionDefinition> childProps = new ArrayList<PropositionDefinition> (prop.getCompoundComponents());
        Collections.sort(childProps, new CompoundPropositionComparator());
        List<PropositionDefinition.Builder> childPropBldrs = new ArrayList<PropositionDefinition.Builder> (childProps.size());

        for (PropositionDefinition chidProp : childProps) {
            PropositionDefinition.Builder childPropBlder = PropositionDefinition.Builder.create(chidProp);
            childPropBldrs.add(childPropBlder);
        }

        propBldr.setCompoundComponents(childPropBldrs);

        return propBldr.build();
    }

    private PropositionDefinition.Builder replaceTermValues(PropositionDefinition proposition) {

        PropositionDefinition.Builder bldr = PropositionDefinition.Builder.create(proposition);

        // recursively add termValues to child propositions.
        if (!PropositionType.SIMPLE.getCode().equalsIgnoreCase(proposition.getPropositionTypeCode())) {
            List<PropositionDefinition.Builder> cmpdProps = new ArrayList<PropositionDefinition.Builder>();
            for (PropositionDefinition cmpdProp : proposition.getCompoundComponents()) {
                cmpdProps.add(replaceTermValues(cmpdProp));
            }
            bldr.setCompoundComponents(cmpdProps);
            return bldr;
        }

        // that have parameters
        if (proposition.getParameters() == null) {
            return bldr;
        }

        if (proposition.getParameters().isEmpty()) {
            return bldr;
        }

        boolean found = false;
        List<PropositionParameter.Builder> params = new ArrayList<PropositionParameter.Builder>(proposition.getParameters().size());

        for (PropositionParameter param : proposition.getParameters()) {
            if (!PropositionParameterType.TERM.getCode().equalsIgnoreCase(param.getParameterType())) {
                params.add(PropositionParameter.Builder.create(param));
                continue;
            }

            // inflate the termValue
            found = true;
            TermDefinition termValue = this.termRepositoryService.getTerm(param.getValue());
            PropositionParameter.Builder parmbldr = PropositionParameter.Builder.create(param);
            parmbldr.setTermValue(termValue);
            params.add(parmbldr);
        }

        if (!found) {
            return bldr;
        }

        bldr.setParameters(params);

        return bldr;
    }


    private Set<PropositionDefinition> replaceTermValuesInSet(Set<PropositionDefinition> propositions) {
        if (propositions == null) {
            return null;
        }

        if (propositions.isEmpty()) {
            return propositions;
        }

        Set<PropositionDefinition> set = new LinkedHashSet<PropositionDefinition>(propositions.size());
        for (PropositionDefinition proposition : propositions) {
            proposition = this.replaceTermValues(proposition).build();
            set.add(proposition);
        }

        return set;
    }

    @Override
    public Set<PropositionDefinition> getPropositionsByType(String typeId) throws RiceIllegalArgumentException {
        return replaceTermValuesInSet(propositionBoService.getPropositionsByType(typeId));
    }

    @Override
    public Set<PropositionDefinition> getPropositionsByRule(String ruleId) throws RiceIllegalArgumentException {
        return replaceTermValuesInSet(propositionBoService.getPropositionsByRule(ruleId));
    }

    @Override
    public void updateProposition(PropositionDefinition propositionDefinition) throws RiceIllegalArgumentException {
        this.crossCheckPropositionParameters(propositionDefinition);
        PropositionDefinition.Builder propBldr = PropositionDefinition.Builder.create(propositionDefinition);
        propBldr = setSequenceOnCompoundPropositionsIfNeeded(propBldr);
        propBldr = maintainTermValuesAndChildPropositions(propBldr);

        propositionBoService.updateProposition(propBldr.build());
    }

    private PropositionDefinition.Builder setSequenceOnCompoundPropositionsIfNeeded(PropositionDefinition.Builder propBldr) {
        if (propBldr.getCompoundComponents() == null) {
            return propBldr;
        }

        if (propBldr.getCompoundComponents().size() <= 1) {
            return propBldr;
        }

        List<PropositionDefinition.Builder> childList = new ArrayList<PropositionDefinition.Builder>(propBldr.getCompoundComponents().size());
        int i = 1; // start at 1 because rice does that

        for (PropositionDefinition.Builder childPropBldr : propBldr.getCompoundComponents()) {
            childPropBldr.setCompoundSequenceNumber(i);
            i++;
            childList.add(childPropBldr);
        }
        propBldr.setCompoundComponents(childList);
        return propBldr;
    }

    @Override
    public void deleteProposition(String id) throws RiceIllegalArgumentException {
        propositionBoService.deleteProposition(id);
    }

    ////
    //// natural language usage methods
    ////
    @Override
    public NaturalLanguageUsage createNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage) throws RiceIllegalArgumentException {
        return naturalLanguageUsageBoService.createNaturalLanguageUsage(naturalLanguageUsage);
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsage(String id) throws RiceIllegalArgumentException {
        return naturalLanguageUsageBoService.getNaturalLanguageUsage(id);
    }

    @Override
    public void updateNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage) throws RiceIllegalArgumentException {
        naturalLanguageUsageBoService.updateNaturalLanguageUsage(naturalLanguageUsage);
    }

    @Override
    public void deleteNaturalLanguageUsage(String naturalLanguageUsageId) throws RiceIllegalArgumentException {
        naturalLanguageUsageBoService.deleteNaturalLanguageUsage(naturalLanguageUsageId);
    }

    @Override
    public List<NaturalLanguageUsage> getNaturalLanguageUsagesByNamespace(String namespace)
            throws RiceIllegalArgumentException {
        return this.naturalLanguageUsageBoService.findNaturalLanguageUsagesByNamespace(namespace);
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsageByNameAndNamespace(String name, String namespace)
            throws RiceIllegalArgumentException {
        return this.naturalLanguageUsageBoService.getNaturalLanguageUsageByName(namespace, name);
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

        return this.getTranslateBusinessMethods().translateNaturalLanguageForObject(naturalLanguageUsageId, typeId, krmsObjectId, languageCode);
    }

    @Override
    public String translateNaturalLanguageForProposition(String naturalLanguageUsageId,
                                                         PropositionDefinition proposition, String languageCode)
            throws RiceIllegalArgumentException {

        return this.getTranslateBusinessMethods().translateNaturalLanguageForProposition(naturalLanguageUsageId, proposition, languageCode);
    }

    @Override
    public NaturalLanguageTree translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId, 
            PropositionDefinition propositionDefinintion, String languageCode) throws RiceIllegalArgumentException {

        return getTranslateBusinessMethods().translateNaturalLanguageTreeForProposition(naturalLanguageUsageId,
                propositionDefinintion, languageCode);
    }

    ////
    //// context methods
    ////
    @Override
    public ContextDefinition createContext(ContextDefinition contextDefinition) throws RiceIllegalArgumentException {
        return this.contextBoService.createContext(contextDefinition);
    }


    @Override
    public ContextDefinition findCreateContext(ContextDefinition contextDefinition) throws RiceIllegalArgumentException {
        ContextDefinition orig = this.contextBoService.getContextByNameAndNamespace(contextDefinition.getName(), contextDefinition.getNamespace());

        if (orig != null) {
            return orig;
        }

        return this.contextBoService.createContext(contextDefinition);
    }

    @Override
    public void updateContext(ContextDefinition contextDefinition) throws RiceIllegalArgumentException {
        this.contextBoService.updateContext(contextDefinition);
    }

    @Override
    public void deleteContext(String id) throws RiceIllegalArgumentException {
        throw new RiceIllegalArgumentException("not implemented yet");
    }

    @Override
    public ContextDefinition getContext(String id) throws RiceIllegalArgumentException {
        return this.contextBoService.getContextByContextId(id);
    }

    @Override
    public ContextDefinition getContextByNameAndNamespace(String name, String namespace) throws RiceIllegalArgumentException {
        return this.contextBoService.getContextByNameAndNamespace(name, namespace);
    }

    ////
    //// natural language templates
    ////
    @Override
    public NaturalLanguageTemplate createNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.createNaturalLanguageTemplate(naturalLanguageTemplate);
    }

    @Override
    public NaturalLanguageTemplate getNaturalLanguageTemplate(String naturalLanguageTemplateId) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.getNaturalLanguageTemplate(naturalLanguageTemplateId);
    }

    @Override
    public void updateNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate) throws RiceIllegalArgumentException {
        this.naturalLanguageTemplateBoService.updateNaturalLanguageTemplate(naturalLanguageTemplate);
    }

    @Override
    public void deleteNaturalLanguageTemplate(String naturalLanguageTemplateId) throws RiceIllegalArgumentException {
        this.naturalLanguageTemplateBoService.deleteNaturalLanguageTemplate(naturalLanguageTemplateId);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByLanguageCode(String languageCode) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.findNaturalLanguageTemplatesByLanguageCode(languageCode);
    }

    @Override
    public NaturalLanguageTemplate findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(String languageCode, String typeId, String naturalLanguageUsageId) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(languageCode, typeId, naturalLanguageUsageId);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByNaturalLanguageUsage(String naturalLanguageUsageId) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.findNaturalLanguageTemplatesByNaturalLanguageUsage(naturalLanguageUsageId);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByType(String typeId) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.findNaturalLanguageTemplatesByType(typeId);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByTemplate(String template) throws RiceIllegalArgumentException {
        return this.naturalLanguageTemplateBoService.findNaturalLanguageTemplatesByTemplate(template);
    }

    @Override
    public List<String> findContextIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        GenericQueryResults<ContextBo> results = getCriteriaLookupService().lookup(ContextBo.class, queryByCriteria);

        List<String> list = new ArrayList<String> ();
        for (ContextBo bo : results.getResults()) {
            list.add(bo.getId());
        }

        return list;
    }

    @Override
    public List<String> findAgendaIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        GenericQueryResults<AgendaBo> results = getCriteriaLookupService().lookup(AgendaBo.class, queryByCriteria);
        List<String> list = new ArrayList<String> ();

        for (AgendaBo bo : results.getResults()) {
            list.add(bo.getId());
        }

        return list;
    }

    @Override
    public List<String> findRuleIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        GenericQueryResults<RuleBo> results = getCriteriaLookupService().lookup(RuleBo.class, queryByCriteria);
        List<String> list = new ArrayList<String> ();

        for (RuleBo bo : results.getResults()) {
            list.add(bo.getId());
        }

        return list;
    }

    @Override
    public List<String> findPropositionIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        GenericQueryResults<PropositionBo> results = getCriteriaLookupService().lookup(PropositionBo.class, queryByCriteria);

        List<String> list = new ArrayList<String> ();
        for (PropositionBo bo : results.getResults()) {
            list.add(bo.getId());
        }

        return list;
    }

    @Override
    public List<String> findActionIds(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        GenericQueryResults<ActionBo> results = getCriteriaLookupService().lookup(ActionBo.class, queryByCriteria);

        List<String> list = new ArrayList<String> ();
        for (ActionBo bo : results.getResults()) {
            list.add(bo.getId());
        }

        return list;
    }
    
    /**
     * Sets the businessObjectService property.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    @Override
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        super.setBusinessObjectService(businessObjectService);
        if (referenceObjectBindingBoService instanceof ReferenceObjectBindingBoServiceImpl) {
            ((ReferenceObjectBindingBoServiceImpl) referenceObjectBindingBoService).setBusinessObjectService(businessObjectService);
        }

        if (agendaBoService instanceof AgendaBoServiceImpl) {
            ((AgendaBoServiceImpl) agendaBoService).setBusinessObjectService(businessObjectService);
        }

        if (ruleBoService instanceof RuleBoServiceImpl) {
            ((RuleBoServiceImpl) ruleBoService).setBusinessObjectService(businessObjectService);
        }

        if (actionBoService instanceof ActionBoServiceImpl) {
            ((ActionBoServiceImpl) actionBoService).setBusinessObjectService(businessObjectService);
        }

        if (propositionBoService instanceof PropositionBoServiceImpl) {
            ((PropositionBoServiceImpl) propositionBoService).setBusinessObjectService(businessObjectService);
        }

        if (naturalLanguageUsageBoService instanceof NaturalLanguageUsageBoServiceImpl) {
            ((NaturalLanguageUsageBoServiceImpl) naturalLanguageUsageBoService).setBusinessObjectService(businessObjectService);
        }

        if (naturalLanguageTemplateBoService instanceof NaturalLanguageTemplateBoServiceImpl) {
            ((NaturalLanguageTemplateBoServiceImpl) naturalLanguageTemplateBoService).setBusinessObjectService(businessObjectService);
        }

        if (contextBoService instanceof ContextBoServiceImpl) {
            ((ContextBoServiceImpl) contextBoService).setBusinessObjectService(businessObjectService);
        }

        if (termRepositoryService instanceof TermBoServiceImpl) {
            ((TermBoServiceImpl) termRepositoryService).setBusinessObjectService(businessObjectService);
        }
    }
}
