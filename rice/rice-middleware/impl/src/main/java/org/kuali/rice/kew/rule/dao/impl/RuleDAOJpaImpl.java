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
package org.kuali.rice.kew.rule.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.dao.RuleDAO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RuleDAOJpaImpl implements RuleDAO {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleDAOJpaImpl.class);

	@PersistenceContext(unitName="kew-unit")
	private EntityManager entityManager;

	private static final String OLD_DELEGATIONS_SQL =
		"select oldDel.dlgn_rule_id "+
		"from krew_rule_rsp_t oldRsp, krew_dlgn_rsp_t oldDel "+
		"where oldRsp.rule_id=? and "+
		"oldRsp.rule_rsp_id=oldDel.rule_rsp_id and "+
		"oldDel.dlgn_rule_base_val_id not in "+
		"(select newDel.dlgn_rule_base_val_id from krew_rule_rsp_t newRsp, krew_dlgn_rsp_t newDel "+
		"where newRsp.rule_id=? and "+
		"newRsp.rule_rsp_id=newDel.rule_rsp_id)";

	public void save(RuleBaseValues ruleBaseValues) {
		if(ruleBaseValues.getId()==null){
			entityManager.persist(ruleBaseValues);
		}else{
			OrmUtils.merge(entityManager, ruleBaseValues);
		}
	}

	public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateId, List documentTypes) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.in("docTypeName", documentTypes);
		crit.eq("ruleTemplateId", ruleTemplateId);
		crit.eq("currentInd", Boolean.TRUE);
		crit.eq("active", Boolean.TRUE);
		crit.eq("delegateRule", Boolean.FALSE);
		crit.eq("templateRuleInd", Boolean.FALSE);

		crit.and(generateFromToDateCriteria(new Date()));
		return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

	public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateId, List documentTypes, Timestamp effectiveDate) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.in("docTypeName", documentTypes);
		crit.eq("ruleTemplateId", ruleTemplateId);
		crit.eq("active", Boolean.TRUE);
		crit.eq("delegateRule", Boolean.FALSE);
		crit.eq("templateRuleInd", Boolean.FALSE);
		if (effectiveDate != null) {
			crit.lte("activationDate", effectiveDate);
			crit.gte("deactivationDate", effectiveDate);
		}

		crit.and(generateFromToDateCriteria(new Date()));
		return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

	public Criteria generateFromToDateCriteria(Date date) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());

		Criteria fromCrit = new Criteria(RuleBaseValues.class.getName());
		Criteria fromNullCrit = new Criteria(RuleBaseValues.class.getName());
		fromNullCrit.isNull("fromDateValue");
		Criteria fromLessOrEqualCrit = new Criteria(RuleBaseValues.class.getName());
		fromLessOrEqualCrit.lte("fromDateValue", new Timestamp(date.getTime()));
		fromCrit.or(fromNullCrit);
		fromCrit.or(fromLessOrEqualCrit);

		Criteria toCrit = new Criteria(RuleBaseValues.class.getName());
		Criteria toNullCrit = new Criteria(RuleBaseValues.class.getName());
		toNullCrit.isNull("toDateValue");
		Criteria toGreaterOrEqualCrit = new Criteria(RuleBaseValues.class.getName());
		toGreaterOrEqualCrit.gte("toDateValue", new Timestamp(date.getTime()));
		toCrit.or(toNullCrit);
		toCrit.or(toGreaterOrEqualCrit);

		crit.and(fromCrit);
		crit.and(toCrit);

		return crit;
	}

	public List<RuleBaseValues> fetchAllRules(boolean currentRules) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.eq("currentInd", new Boolean(currentRules));
		crit.eq("templateRuleInd", Boolean.FALSE);
		crit.orderBy("activationDate", false);

		QueryByCriteria query = new QueryByCriteria(entityManager, crit);

		return (List) query.toQuery().getResultList();
	}

	public void delete(String ruleBaseValuesId) {
		entityManager.remove(entityManager.find(RuleBaseValues.class, ruleBaseValuesId));
	}

	public List<RuleBaseValues> findByDocumentId(String documentId) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.eq("documentId", documentId);
		return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

    public RuleBaseValues findRuleBaseValuesByName(String name) {
        Criteria crit = new Criteria(RuleBaseValues.class.getName());
        if (name == null) {
        	return null;
        }
        crit.eq("name", name);
        crit.eq("currentInd", Boolean.TRUE);
       	return (RuleBaseValues) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

	public RuleBaseValues findRuleBaseValuesById(String ruleBaseValuesId) {
		if (ruleBaseValuesId == null) {
			return null;
		}
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.eq("id", ruleBaseValuesId);
		return (RuleBaseValues) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
	}

	public List<RuleBaseValues> findRuleBaseValuesByResponsibilityReviewer(String reviewerName, String type) {
		Criteria crit = new Criteria(RuleResponsibilityBo.class.getName());
		crit.eq("ruleResponsibilityName", reviewerName);
		crit.eq("ruleResponsibilityType", type);

		List responsibilities = (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
		List rules = new ArrayList();

		for (Iterator iter = responsibilities.iterator(); iter.hasNext();) {
			RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iter.next();
			RuleBaseValues rule = responsibility.getRuleBaseValues();
			if (rule != null && rule.getCurrentInd() != null && rule.getCurrentInd().booleanValue()) {
				rules.add(rule);
			}
		}
		return rules;
	}

	public List<RuleBaseValues> findRuleBaseValuesByResponsibilityReviewerTemplateDoc(String ruleTemplateName, String documentType, String reviewerName, String type) {
	    Criteria crit = new Criteria(RuleResponsibilityBo.class.getName());
		crit.eq("ruleResponsibilityName", reviewerName);
		crit.eq("ruleResponsibilityType", type);
		crit.eq("ruleBaseValues.currentInd", Boolean.TRUE);
		if (!StringUtils.isBlank(ruleTemplateName)) {
		    crit.like("ruleBaseValues.ruleTemplate.name", ruleTemplateName.replace("*", "%").concat("%"));
		}
		if (!StringUtils.isBlank(documentType)) {
		    crit.like("ruleBaseValues.docTypeName", documentType.replace("*", "%").concat("%"));
		}

		List responsibilities = (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
		List rules = new ArrayList();

		for (Iterator iter = responsibilities.iterator(); iter.hasNext();) {
			RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iter.next();
			RuleBaseValues rule = responsibility.getRuleBaseValues();
			if (rule != null && rule.getCurrentInd() != null && rule.getCurrentInd().booleanValue()) {
				rules.add(rule);
			}
		}
		return rules;
	}

	//FIXME nothing uses this, it's not in ruleDAO interface
//	public List findRuleBaseValuesByObjectGraph(RuleBaseValues ruleBaseValues) {
//		ruleBaseValues.setCurrentInd(Boolean.TRUE);
//		ruleBaseValues.setTemplateRuleInd(Boolean.FALSE);
//		return (List) new QueryByObject(entityManager,ruleBaseValues).toQuery().getResultList();
//	}

	public RuleResponsibilityBo findRuleResponsibility(String responsibilityId) {
		Criteria crit = new Criteria(RuleResponsibilityBo.class.getName());
		crit.eq("responsibilityId", responsibilityId);
		Collection responsibilities = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
		for (Iterator iterator = responsibilities.iterator(); iterator.hasNext();) {
			RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iterator.next();
			if (responsibility.getRuleBaseValues().getCurrentInd().booleanValue()) {
				return responsibility;
			}
		}
		return null;
	}

	public List<RuleBaseValues> search(String docTypeName, String ruleId, String ruleTemplateId, String ruleDescription, String groupId, String principalId, Boolean delegateRule, Boolean activeInd, Map extensionValues, String workflowIdDirective) {
        Criteria crit = getSearchCriteria(docTypeName, ruleTemplateId, ruleDescription, delegateRule, activeInd, extensionValues);
        if (ruleId != null) {
            crit.eq("id", ruleId);
        }
        if (groupId != null) {
            //crit.in("responsibilities.ruleBaseValuesId", getResponsibilitySubQuery(groupId), "ruleBaseValuesId");
        	addResponsibilityCriteria(crit, groupId);
        }
        Collection<String> kimGroupIds = new HashSet<String>();
        Boolean searchUser = Boolean.FALSE;
        Boolean searchUserInWorkgroups = Boolean.FALSE;
        
        if ("group".equals(workflowIdDirective)) {
            searchUserInWorkgroups = Boolean.TRUE;
        } else if (StringUtils.isBlank(workflowIdDirective)) {
            searchUser = Boolean.TRUE;
            searchUserInWorkgroups = Boolean.TRUE;
        } else {
            searchUser = Boolean.TRUE;
        }
        
        if (!org.apache.commons.lang.StringUtils.isEmpty(principalId) && searchUserInWorkgroups) {
            Principal principal = null;

            principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);

            if (principal == null)
            {
            	throw new RiceRuntimeException("Failed to locate user for the given principal id: " + principalId);
            }
            kimGroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
        }
        addResponsibilityCriteria(crit, kimGroupIds, principalId, searchUser, searchUserInWorkgroups);
        //if (responsibilityCrit != null) {
        	//crit.in("responsibilities.ruleBaseValuesId", responsibilityCrit,"ruleBaseValuesId");
        //}
        crit.distinct(true);
		return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

    public List<RuleBaseValues> search(String docTypeName, String ruleTemplateId, String ruleDescription, Collection<String> workgroupIds, String workflowId, Boolean delegateRule, Boolean activeInd, Map extensionValues, Collection actionRequestCodes) {
        Criteria crit = getSearchCriteria(docTypeName, ruleTemplateId, ruleDescription, delegateRule, activeInd, extensionValues);
        addResponsibilityCriteria(crit, workgroupIds, workflowId, actionRequestCodes, (workflowId != null), ((workgroupIds != null) && !workgroupIds.isEmpty()));
        //if (responsibilityCrit != null) {
        	//crit.in("responsibilities.ruleBaseValuesId", responsibilityCrit, "ruleBaseValuesId");
        //}
        return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    private void addResponsibilityCriteria(Criteria parentCrit, Collection<String> kimGroupIds, String principalId, Boolean searchUser, Boolean searchUserInWorkgroups) {
        Collection<String> workgroupIdStrings = new ArrayList<String>();
        for (String workgroupId : kimGroupIds) {
            workgroupIdStrings.add(workgroupId.toString());
        }
        addResponsibilityCriteria(parentCrit, workgroupIdStrings,principalId,new ArrayList<String>(), searchUser, searchUserInWorkgroups);
    }
    
    private void addResponsibilityCriteria(Criteria parentCrit, Collection<String> workgroupIds, String workflowId, Collection actionRequestCodes, Boolean searchUser, Boolean searchUserInWorkgroups) {
        Criteria responsibilityCrit = null;

        Criteria ruleResponsibilityNameCrit = null;
        
        if ( (actionRequestCodes != null) && (!actionRequestCodes.isEmpty()) ) {
        	responsibilityCrit = new Criteria(RuleBaseValues.class.getName(), false);
            responsibilityCrit.in("__JPA_ALIAS[['rr']]__.actionRequestedCd", new ArrayList(actionRequestCodes));
        }
        
        if (!org.apache.commons.lang.StringUtils.isEmpty(workflowId)) {
            // workflow user id exists
            if (searchUser != null && searchUser) {
                // searching user wishes to search for rules specific to user
                ruleResponsibilityNameCrit = new Criteria(RuleBaseValues.class.getName(), false);
                ruleResponsibilityNameCrit.like("__JPA_ALIAS[['rr']]__.ruleResponsibilityName", workflowId);
                ruleResponsibilityNameCrit.eq("__JPA_ALIAS[['rr']]__.ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
            }
            if ( (searchUserInWorkgroups != null && searchUserInWorkgroups) && (workgroupIds != null) && (!workgroupIds.isEmpty()) ) {
                // at least one workgroup id exists and user wishes to search on workgroups
                if (ruleResponsibilityNameCrit == null) {
                    ruleResponsibilityNameCrit = new Criteria(RuleBaseValues.class.getName(), false);
                }
                Criteria workgroupCrit = new Criteria(RuleBaseValues.class.getName(), false);
                workgroupCrit.in("__JPA_ALIAS[['rr']]__.ruleResponsibilityName", new ArrayList<String>(workgroupIds));
                workgroupCrit.eq("__JPA_ALIAS[['rr']]__.ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
                ruleResponsibilityNameCrit.or(workgroupCrit);
            }
        } else if ( (workgroupIds != null) && (workgroupIds.size() == 1) ) {
            // no user and one workgroup id
            ruleResponsibilityNameCrit = new Criteria(RuleBaseValues.class.getName(), false);
            ruleResponsibilityNameCrit.like("__JPA_ALIAS[['rr']]__.ruleResponsibilityName", workgroupIds.iterator().next());
            ruleResponsibilityNameCrit.eq("__JPA_ALIAS[['rr']]__.ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        } else if ( (workgroupIds != null) && (workgroupIds.size() > 1) ) {
            // no user and more than one workgroup id
            ruleResponsibilityNameCrit = new Criteria(RuleBaseValues.class.getName(), false);
            ruleResponsibilityNameCrit.in("__JPA_ALIAS[['rr']]__.ruleResponsibilityName",  new ArrayList<String>(workgroupIds));
            ruleResponsibilityNameCrit.eq("__JPA_ALIAS[['rr']]__.ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        }

        if (ruleResponsibilityNameCrit != null) {
        	if (responsibilityCrit == null) {
        		responsibilityCrit = new Criteria(RuleBaseValues.class.getName(), false);
        	}
            responsibilityCrit.and(ruleResponsibilityNameCrit);
        }

        if (responsibilityCrit != null) {
        	if (parentCrit.getAliasIndex("rr") == -1) {
    			parentCrit.join("responsibilities", "rr", false, true);
    		}
        	parentCrit.and(responsibilityCrit);
        }
        //return responsibilityCrit;
    }

    private Criteria getSearchCriteria(String docTypeName, String ruleTemplateId, String ruleDescription, Boolean delegateRule, Boolean activeInd, Map extensionValues) {
        Criteria crit = new Criteria(RuleBaseValues.class.getName());
        crit.eq("currentInd", Boolean.TRUE);
        crit.eq("templateRuleInd", Boolean.FALSE);
        if (activeInd != null) {
            crit.eq("active", activeInd);
        }
        if (docTypeName != null) {
            crit.like("UPPER(__JPA_ALIAS[[0]]__.docTypeName)", docTypeName.toUpperCase());
        }
        if (ruleDescription != null && !ruleDescription.trim().equals("")) {
            crit.like("UPPER(__JPA_ALIAS[[0]]__.description)", ruleDescription.toUpperCase());
        }
        if (ruleTemplateId != null) {
            crit.eq("ruleTemplateId", ruleTemplateId);
        }
        if (delegateRule != null) {
            crit.eq("delegateRule", delegateRule);
        }
        if (extensionValues != null && !extensionValues.isEmpty()) {
            for (Iterator iter2 = extensionValues.entrySet().iterator(); iter2.hasNext();) {
                Map.Entry entry = (Map.Entry) iter2.next();
                if (!org.apache.commons.lang.StringUtils.isEmpty((String) entry.getValue())) {
                    // Criteria extensionCrit = new Criteria();
                    // extensionCrit.addEqualTo("extensionValues.key",
                    // entry.getKey());
                    // extensionCrit.addLike("extensionValues.value",
                    // "%"+(String) entry.getValue()+"%");

                    Criteria extensionCrit2 = new Criteria(RuleExtensionBo.class.getName());
                    extensionCrit2.distinct(true);
                    extensionCrit2.join("extensionValues", "extval", false, true);
                    extensionCrit2.eq("__JPA_ALIAS[['extval']]__.key", entry.getKey());
                    extensionCrit2.like("UPPER(__JPA_ALIAS[['extval']]__.value)", ("%" + (String) entry.getValue() + "%").toUpperCase());

                    // Criteria extensionCrit3 = new Criteria();
                    // extensionCrit3.addEqualTo("extensionValues.key",
                    // entry.getKey());
                    // extensionCrit3.addLike("extensionValues.value",
                    // ("%"+(String) entry.getValue()+"%").toLowerCase());

                    // extensionCrit.addOrCriteria(extensionCrit2);
                    // extensionCrit.addOrCriteria(extensionCrit3);
                    extensionCrit2.memberOf("__JPA_ALIAS[[0]]__", "__JPA_ALIAS[[-1]]__.ruleExtensions");
                    crit.exists(extensionCrit2);
                }
            }
        }
        return crit;
    }

//    private Criteria getWorkgroupOrCriteria(Collection workgroupIds) {
//        Criteria responsibilityCrit = new Criteria(RuleResponsibility.class.getName());
//        for (Iterator iter = workgroupIds.iterator(); iter.hasNext();) {
//            String workgroupIdFromList = (String) iter.next();
//            Criteria orCriteria = new Criteria(RuleResponsibility.class.getName());
//            orCriteria.like("ruleResponsibilityName", workgroupIdFromList);
//            responsibilityCrit.or(orCriteria);
//        }
//
//        Criteria crit = new Criteria();
//        crit.in("responsibilities.ruleBaseValuesId", responsibilityCrit,"ruleBaseValuesId");
//        return crit;
//    }

	private void addResponsibilityCriteria(Criteria parentCrit, String ruleResponsibilityName) {
		//Criteria responsibilityCrit = new Criteria(RuleResponsibility.class.getName());
		if (parentCrit.getAliasIndex("rr") == -1) {
			parentCrit.join("responsibilities", "rr", false, true);
		}
		parentCrit.like("__JPA_ALIAS[['rr']]__.ruleResponsibilityName", ruleResponsibilityName);
		//ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleResponsibility.class, responsibilityCrit);
		//query.setAttributes(new String[] { "ruleBaseValuesId" });
		//return responsibilityCrit;
	}

//	private Criteria getWorkgroupResponsibilitySubQuery(Set<Long> workgroupIds) {
//	    	Set<String> workgroupIdStrings = new HashSet<String>();
//	    	for (Long workgroupId : workgroupIds) {
//	    	    workgroupIdStrings.add(workgroupId.toString());
//	    	}
//		Criteria responsibilityCrit = new Criteria(RuleResponsibility.class.getName());
//		responsibilityCrit.in("ruleResponsibilityName", new ArrayList(workgroupIds));
//		responsibilityCrit.eq("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
////		ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleResponsibility.class, responsibilityCrit);
////		query.setAttributes(new String[] { "ruleBaseValuesId" });
//		return responsibilityCrit;
//	}

	public List<RuleBaseValues> findByPreviousRuleId(String previousRuleId) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.eq("previousRuleId", previousRuleId);
		return (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
	}

	public RuleBaseValues findDefaultRuleByRuleTemplateId(String ruleTemplateId) {
		Criteria crit = new Criteria(RuleBaseValues.class.getName());
		crit.eq("ruleTemplateId", ruleTemplateId);
		crit.eq("templateRuleInd", Boolean.TRUE);
		List rules = (List) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
		if (rules != null && !rules.isEmpty()) {
			return (RuleBaseValues) rules.get(0);
		}
		return null;
	}

	public void retrieveAllReferences(RuleBaseValues rule) {
		// getPersistenceBroker().retrieveAllReferences(rule);
	}

	public RuleBaseValues getParentRule(String ruleBaseValuesId) {
		Criteria criteria = new Criteria(RuleBaseValues.class.getName());
		criteria.eq("currentInd", Boolean.TRUE);
		criteria.eq("responsibilities.delegationRules.delegateRuleId", ruleBaseValuesId);
		Collection rules = new QueryByCriteria(entityManager, criteria).toQuery().getResultList();
		RuleBaseValues rule = null;
		for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
			RuleBaseValues currentRule = (RuleBaseValues) iterator.next();
			if (rule == null || currentRule.getVersionNbr().intValue() > rule.getVersionNbr().intValue()) {
				rule = currentRule;
			}
		}
		return rule;
	}

	public List findOldDelegations(final RuleBaseValues oldRule, final RuleBaseValues newRule) {

		Query q = entityManager.createNativeQuery(OLD_DELEGATIONS_SQL);
		q.setParameter(1, oldRule.getId());
		q.setParameter(2, newRule.getId());
		List oldDelegations = new ArrayList();
		for(Object l:q.getResultList()){
			// FIXME: KULRICE-5201 - This used to be a cast by new Long(l) -- assuming that the Object here in result list is actually a string or is castable to string by .toString()
			oldDelegations.add(findRuleBaseValuesById(String.valueOf(l)));
		}
		return oldDelegations;

	}
	
	public String findResponsibilityIdForRule(String ruleName, String ruleResponsibilityName, String ruleResponsibilityType) {
		Criteria crit = new Criteria(RuleResponsibilityBo.class.getName());
		crit.eq("ruleResponsibilityName", ruleResponsibilityName);
		crit.eq("ruleResponsibilityType", ruleResponsibilityType);
		crit.eq("ruleBaseValues.currentInd", Boolean.TRUE);
		crit.eq("ruleBaseValues.name", ruleName);
		Collection responsibilities = new QueryByCriteria(entityManager, crit).toQuery().getResultList();
		if (responsibilities != null) {
			for (Iterator iter = responsibilities.iterator(); iter.hasNext();) {
				RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iter.next();
				return responsibility.getResponsibilityId();
			}
		}
		return null;
	}

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
