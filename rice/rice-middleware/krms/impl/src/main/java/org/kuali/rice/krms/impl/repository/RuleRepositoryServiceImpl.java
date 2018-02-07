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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krms.api.repository.RuleRepositoryService;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeRuleEntry;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeSubAgendaEntry;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.impl.util.KrmsImplConstants.PropertyNames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;

/**
 *
 */
public class RuleRepositoryServiceImpl implements RuleRepositoryService {
    protected BusinessObjectService businessObjectService;
    private CriteriaLookupService criteriaLookupService;
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.krms.api.repository.RuleRepositoryService#selectContext(org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria)
	 */
    @Override
    public ContextDefinition selectContext(
    		ContextSelectionCriteria contextSelectionCriteria) {
    	if (contextSelectionCriteria == null){
    		throw new RiceIllegalArgumentException("selection criteria is null");
    	}
    	if (StringUtils.isBlank(contextSelectionCriteria.getNamespaceCode())){
    		throw new RiceIllegalArgumentException("selection criteria namespaceCode is null or blank");
    	}
    	QueryByCriteria queryCriteria = buildQuery(contextSelectionCriteria);
        GenericQueryResults<ContextBo> results = getCriteriaLookupService().lookup(ContextBo.class, queryCriteria);

    	List<ContextBo> resultBos = results.getResults();

    	//assuming 1 ?
    	ContextDefinition result = null;
    	if (!CollectionUtils.isEmpty(resultBos)) {
    		if (resultBos.size() == 1) {
    			ContextBo bo = resultBos.iterator().next();
    			return ContextBo.to(bo);
    		}
    		else throw new RiceIllegalArgumentException("Ambiguous context qualifiers, can not select more than one context.");
    	}
    	return result;
    }

	@Override
	public AgendaTreeDefinition getAgendaTree(String agendaId) {
		if (StringUtils.isBlank(agendaId)){
    		throw new RiceIllegalArgumentException("agenda id is null or blank");
    	}
		// Get agenda items from db, then build up agenda tree structure
		AgendaBo agendaBo = getBusinessObjectService().findBySinglePrimaryKey(AgendaBo.class, agendaId);
        if (agendaBo == null) {
            return null;
        }

		String agendaItemId = agendaBo.getFirstItemId();
		
		// walk thru the agenda items, building an agenda tree definition Builder along the way
		AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create();
		myBuilder.setAgendaId( agendaId );
        if (agendaItemId != null) {
		    myBuilder = walkAgendaItemTree(agendaItemId, myBuilder);
        }
		
		// build the agenda tree and return it
		return myBuilder.build();
	}
    	
	@Override
	public List<AgendaTreeDefinition> getAgendaTrees(List<String> agendaIds) {
		List<AgendaTreeDefinition> agendaTrees = new ArrayList<AgendaTreeDefinition>();
        if (agendaIds == null) {
            return agendaTrees;
        }

		for (String agendaId : agendaIds){
            if (getAgendaTree(agendaId) != null) {
			   agendaTrees.add( getAgendaTree(agendaId) );
            }
		}
        return Collections.unmodifiableList(agendaTrees);		
	}
	
	@Override
	public RuleDefinition getRule(String ruleId) {
		if (StringUtils.isBlank(ruleId)){
			return null;			
		}
		RuleBo bo = getBusinessObjectService().findBySinglePrimaryKey(RuleBo.class, ruleId);
		return RuleBo.to(bo);
	}
	
	@Override
	public List<RuleDefinition> getRules(List<String> ruleIds) {
        if (ruleIds == null) throw new RiceIllegalArgumentException("ruleIds must not be null");

        // Fetch BOs
        List<RuleBo> bos = null;
        if (ruleIds.size() == 0) {
            bos = Collections.emptyList();
        } else {
            QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
            List<Predicate> pList = new ArrayList<Predicate>();
            qBuilder.setPredicates(in("id", ruleIds.toArray()));
            GenericQueryResults<RuleBo> results = getCriteriaLookupService().lookup(RuleBo.class, qBuilder.build());

    	    bos = results.getResults();
    	}

        // Translate BOs
        ArrayList<RuleDefinition> rules = new ArrayList<RuleDefinition>();
        for (RuleBo bo : bos) {
            RuleDefinition rule = RuleBo.to(bo);
            rules.add(rule);
        }
        return Collections.unmodifiableList(rules);
	}

	/**
	 * Recursive method to create AgendaTreeDefinition builder
	 * 	
	 *  
	 */
	private AgendaTreeDefinition.Builder walkAgendaItemTree(String agendaItemId, AgendaTreeDefinition.Builder builder){
		//TODO: prevent circular, endless looping
		if (StringUtils.isBlank(agendaItemId)){
			return null;
		}
		// Get AgendaItemDefinition Business Object from database
		// NOTE: if we read agendaItem one at a time from db.   Could consider linking in OJB and getting all at once
		AgendaItemBo agendaItemBo = getBusinessObjectService().findBySinglePrimaryKey(AgendaItemBo.class, agendaItemId);
		
		// If Rule  
		// TODO: validate that only either rule or subagenda, not both
		if (!StringUtils.isBlank( agendaItemBo.getRuleId() )){
			// setup new rule entry builder
			AgendaTreeRuleEntry.Builder ruleEntryBuilder = AgendaTreeRuleEntry.Builder
					.create(agendaItemBo.getId(), agendaItemBo.getRuleId());
			ruleEntryBuilder.setRuleId( agendaItemBo.getRuleId() );
			ruleEntryBuilder.setAgendaItemId( agendaItemBo.getId() );
			if (agendaItemBo.getWhenTrueId() != null){
				// Go follow the true branch, creating AgendaTreeDefinintion Builder for the
				// true branch level
				AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create();
				myBuilder.setAgendaId( agendaItemBo.getAgendaId() );
				ruleEntryBuilder.setIfTrue( walkAgendaItemTree(agendaItemBo.getWhenTrueId(),myBuilder));
			}
			if (agendaItemBo.getWhenFalseId() != null){
				// Go follow the false branch, creating AgendaTreeDefinintion Builder 
				AgendaTreeDefinition.Builder myBuilder = AgendaTreeDefinition.Builder.create();
				myBuilder.setAgendaId( agendaItemBo.getAgendaId() );
				ruleEntryBuilder.setIfFalse( walkAgendaItemTree(agendaItemBo.getWhenFalseId(), myBuilder));
			}
			// Build the Rule Entry and add it to the AgendaTreeDefinition builder
			builder.addRuleEntry( ruleEntryBuilder.build() );
		}
		// if SubAgenda and a sub agenda tree entry
		if (!StringUtils.isBlank(agendaItemBo.getSubAgendaId())) {
			AgendaTreeSubAgendaEntry.Builder subAgendaEntryBuilder = 
				AgendaTreeSubAgendaEntry.Builder.create(agendaItemBo.getId(), agendaItemBo.getSubAgendaId());
			builder.addSubAgendaEntry( subAgendaEntryBuilder.build() );
			}

		// if this agenda item has an "After Id", follow that branch
		if (!StringUtils.isBlank( agendaItemBo.getAlwaysId() )){
			builder = walkAgendaItemTree( agendaItemBo.getAlwaysId(), builder);
			
		}
		return builder;
	}
	
	/**
	 * 
	 * This method converts a {@link ContextSelectionCriteria} object into a
	 * {@link QueryByCriteria} object with the proper predicates for AttributeBo properties.
	 * 
	 * @param selectionCriteria
	 * @return 
	 */
	private QueryByCriteria buildQuery( ContextSelectionCriteria selectionCriteria ){
		Predicate p;
		QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
    	List<Predicate> pList = new ArrayList<Predicate>();
    	if (selectionCriteria.getNamespaceCode() != null){
    		p = equal(PropertyNames.Context.NAMESPACE, selectionCriteria.getNamespaceCode());
    		pList.add(p);
    	}
    	if (selectionCriteria.getName() != null){
    		p = equal(PropertyNames.Context.NAME, selectionCriteria.getName());
    		pList.add(p);
    	}
    	if (selectionCriteria.getContextQualifiers() != null){
    		for (Map.Entry<String, String> entry : selectionCriteria.getContextQualifiers().entrySet()){
    			p = and(equal(PropertyNames.Context.ATTRIBUTE_BOS
    					+ "." + PropertyNames.BaseAttribute.ATTRIBUTE_DEFINITION
    					+ "." + PropertyNames.KrmsAttributeDefinition.NAME, entry.getKey()),
    				equal(PropertyNames.Context.ATTRIBUTE_BOS
    					+ "." + PropertyNames.BaseAttribute.VALUE, entry.getValue()));
    			pList.add(p);
    		}
    	}
     	Predicate[] preds = new Predicate[pList.size()];
     	pList.toArray(preds);
    	qBuilder.setPredicates(and(preds)); 
		return qBuilder.build();
	}

	/**
     * Sets the businessObjectService property.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    protected BusinessObjectService getBusinessObjectService() {
		if ( businessObjectService == null ) {
            // TODO: inject this instead
			businessObjectService = KRADServiceLocator.getBusinessObjectService();
		}
		return businessObjectService;
	}
    
    /**
     * Sets the criteriaLookupService attribute value.
     *
     * @param criteriaLookupService The criteriaLookupService to set.
     */
    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    protected CriteriaLookupService getCriteriaLookupService() {
        if ( criteriaLookupService == null ) {
            criteriaLookupService = KrmsRepositoryServiceLocator.getCriteriaLookupService();
        }
        return criteriaLookupService;
    }
    
}
