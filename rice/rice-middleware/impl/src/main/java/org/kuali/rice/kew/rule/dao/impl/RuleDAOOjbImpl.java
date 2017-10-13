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
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.dao.RuleDAO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class RuleDAOOjbImpl extends PersistenceBrokerDaoSupport implements RuleDAO {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleDAOOjbImpl.class);

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
		this.getPersistenceBrokerTemplate().store(ruleBaseValues);
	}

	public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateId, List documentTypes) {
		Criteria crit = new Criteria();
		crit.addIn("docTypeName", documentTypes);
		crit.addEqualTo("ruleTemplateId", ruleTemplateId);
		crit.addEqualTo("currentInd", Boolean.TRUE);
		crit.addEqualTo("active", Boolean.TRUE);
		crit.addEqualTo("delegateRule", Boolean.FALSE);
		crit.addEqualTo("templateRuleInd", Boolean.FALSE);

		crit.addAndCriteria(generateFromToDateCriteria(new Date()));
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
	}

	public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateId, List documentTypes, Timestamp effectiveDate) {
		Criteria crit = new Criteria();
		crit.addIn("docTypeName", documentTypes);
		crit.addEqualTo("ruleTemplateId", ruleTemplateId);
		crit.addEqualTo("active", Boolean.TRUE);
		crit.addEqualTo("delegateRule", Boolean.FALSE);
		crit.addEqualTo("templateRuleInd", Boolean.FALSE);
		if (effectiveDate != null) {
			crit.addLessOrEqualThan("activationDate", effectiveDate);
			crit.addGreaterThan("deactivationDate", effectiveDate);
		}

		crit.addAndCriteria(generateFromToDateCriteria(new Date()));
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
	}

	public Criteria generateFromToDateCriteria(Date date) {
		Criteria crit = new Criteria();

		Criteria fromCrit = new Criteria();
		Criteria fromNullCrit = new Criteria();
		fromNullCrit.addIsNull("fromDateValue");
		Criteria fromLessOrEqualCrit = new Criteria();
		fromLessOrEqualCrit.addLessOrEqualThan("fromDateValue", new Timestamp(date.getTime()));
		fromCrit.addOrCriteria(fromNullCrit);
		fromCrit.addOrCriteria(fromLessOrEqualCrit);

		Criteria toCrit = new Criteria();
		Criteria toNullCrit = new Criteria();
		toNullCrit.addIsNull("toDateValue");
		Criteria toGreaterOrEqualCrit = new Criteria();
		toGreaterOrEqualCrit.addGreaterOrEqualThan("toDateValue", new Timestamp(date.getTime()));
		toCrit.addOrCriteria(toNullCrit);
		toCrit.addOrCriteria(toGreaterOrEqualCrit);

		crit.addAndCriteria(fromCrit);
		crit.addAndCriteria(toCrit);

		return crit;
	}

	public List<RuleBaseValues> fetchAllRules(boolean currentRules) {
		Criteria crit = new Criteria();
		crit.addEqualTo("currentInd", new Boolean(currentRules));
		crit.addEqualTo("templateRuleInd", Boolean.FALSE);
		// crit.addEqualTo("delegateRule", Boolean.FALSE);

		QueryByCriteria query = new QueryByCriteria(RuleBaseValues.class, crit);
		query.addOrderByDescending("activationDate");

		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
	}

	public void delete(String ruleBaseValuesId) {
		this.getPersistenceBrokerTemplate().delete(findRuleBaseValuesById(ruleBaseValuesId));
	}

	public List<RuleBaseValues> findByDocumentId(String documentId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("documentId", documentId);
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
	}

    public RuleBaseValues findRuleBaseValuesByName(String name) {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", name);
        crit.addEqualTo("currentInd", Boolean.TRUE);
        return (RuleBaseValues) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
    }

	public RuleBaseValues findRuleBaseValuesById(String ruleBaseValuesId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("id", ruleBaseValuesId);
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("id", ruleBaseValuesId);
		// crit.addEqualTo("currentInd", new Boolean(true));
        return KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(RuleBaseValues.class, criteria);
		//return (RuleBaseValues) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
	}

	public List<RuleBaseValues> findRuleBaseValuesByResponsibilityReviewer(String reviewerName, String type) {
		Criteria crit = new Criteria();
		crit.addEqualTo("ruleResponsibilityName", reviewerName);
		crit.addEqualTo("ruleResponsibilityType", type);

		List responsibilities = (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleResponsibilityBo.class, crit));
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
	    Criteria crit = new Criteria();
		crit.addEqualTo("ruleResponsibilityName", reviewerName);
		crit.addEqualTo("ruleResponsibilityType", type);
		crit.addEqualTo("ruleBaseValues.currentInd", Boolean.TRUE);
		if (!StringUtils.isBlank(ruleTemplateName)) {
		    crit.addLike("ruleBaseValues.ruleTemplate.name", ruleTemplateName.replace("*", "%").concat("%"));
		}
		if (!StringUtils.isBlank(documentType)) {
		    crit.addLike("ruleBaseValues.docTypeName", documentType.replace("*", "%").concat("%"));
		}

		List responsibilities = (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleResponsibilityBo.class, crit));
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

	public List findRuleBaseValuesByObjectGraph(RuleBaseValues ruleBaseValues) {
		ruleBaseValues.setCurrentInd(Boolean.TRUE);
		ruleBaseValues.setTemplateRuleInd(Boolean.FALSE);
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(ruleBaseValues));
	}

	public RuleResponsibilityBo findRuleResponsibility(String responsibilityId) {
				
		ReportQueryByCriteria subQuery;
		Criteria subCrit = new Criteria();
		Criteria crit2 = new Criteria();
		subCrit.addLike("responsibilityId", responsibilityId);
		subQuery = QueryFactory.newReportQuery(RuleResponsibilityBo.class, subCrit);
		subQuery.setAttributes(new String[] {"RULE_ID"});
		crit2.addIn("id", subQuery);
		crit2.addEqualTo("currentInd", 1);
		QueryByCriteria outerQuery = QueryFactory.newQuery(RuleBaseValues.class, crit2);
		RuleBaseValues rbv = (RuleBaseValues)this.getPersistenceBrokerTemplate().getObjectByQuery(outerQuery);
		Criteria finalCrit = new Criteria();
		finalCrit.addEqualTo("responsibilityId", responsibilityId);
		finalCrit.addEqualTo("ruleBaseValuesId", rbv.getId());
		RuleResponsibilityBo
                rResp = (RuleResponsibilityBo)this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(RuleResponsibilityBo.class, finalCrit));
		
		if(rResp != null){
			if(rResp.getRuleBaseValuesId().equals(rbv.getId())){
				return rResp;
			}
		}	
	
		return null;
	}

	public List<RuleBaseValues> search(String docTypeName, String ruleId, String ruleTemplateId, String ruleDescription, String workgroupId, String principalId, Boolean delegateRule, Boolean activeInd, Map extensionValues, String workflowIdDirective) {
        Criteria crit = getSearchCriteria(docTypeName, ruleTemplateId, ruleDescription, delegateRule, activeInd, extensionValues);
        if (ruleId != null) {
            crit.addEqualTo("id", ruleId);
        }
        if (workgroupId != null) {
            crit.addIn("id", getResponsibilitySubQuery(workgroupId));
        }
        List<String> workgroupIds = new ArrayList<String>();
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
        
        if (!org.apache.commons.lang.StringUtils.isEmpty(principalId) && searchUserInWorkgroups)
        {
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalId);

            if (principal == null)
            {
            	throw new RiceRuntimeException("Failed to locate user for the given workflow id: " + principalId);
            }
            workgroupIds = KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
        }
        ReportQueryByCriteria query = getResponsibilitySubQuery(workgroupIds, principalId, searchUser, searchUserInWorkgroups);
        if (query != null) {
        	crit.addIn("id", query);
        }

		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit, true));
	}

    public List<RuleBaseValues> search(String docTypeName, String ruleTemplateId, String ruleDescription, Collection<String> workgroupIds, String workflowId, Boolean delegateRule, Boolean activeInd, Map extensionValues, Collection actionRequestCodes) {
    	List results = null;
        Criteria crit = getSearchCriteria(docTypeName, ruleTemplateId, ruleDescription, delegateRule, activeInd, extensionValues);
        ReportQueryByCriteria query = getResponsibilitySubQuery(workgroupIds, workflowId, actionRequestCodes, (workflowId != null), ((workgroupIds != null) && !workgroupIds.isEmpty()));
        if (query != null) {
        	crit.addIn("ruleResponsibilities.ruleBaseValuesId", query);
        }
        results = (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit, true));
        return results;
    }

    private ReportQueryByCriteria getResponsibilitySubQuery(List<String> workgroupIds, String workflowId, Boolean searchUser, Boolean searchUserInWorkgroups) {
        Collection<String> workgroupIdStrings = new ArrayList<String>();
        for (String workgroupId : workgroupIds) {
            workgroupIdStrings.add(workgroupId);
        }
        return getResponsibilitySubQuery(workgroupIdStrings,workflowId,new ArrayList<String>(), searchUser, searchUserInWorkgroups);
    }
    
    private ReportQueryByCriteria getResponsibilitySubQuery(Collection<String> workgroupIds, String workflowId, Collection actionRequestCodes, Boolean searchUser, Boolean searchUserInWorkgroups) {
        Criteria responsibilityCrit = null;
        if ( (actionRequestCodes != null) && (!actionRequestCodes.isEmpty()) ) {
        	responsibilityCrit = new Criteria();
            responsibilityCrit.addIn("actionRequestedCd", actionRequestCodes);
        }

        ReportQueryByCriteria query = null;
        Criteria ruleResponsibilityNameCrit = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(workflowId)) {
            // workflow user id exists
            if (searchUser != null && searchUser) {
                // searching user wishes to search for rules specific to user
                ruleResponsibilityNameCrit = new Criteria();
                ruleResponsibilityNameCrit.addLike("ruleResponsibilityName", workflowId);
                ruleResponsibilityNameCrit.addEqualTo("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID);
            }
            if ( (searchUserInWorkgroups != null && searchUserInWorkgroups) && (workgroupIds != null) && (!workgroupIds.isEmpty()) ) {
                // at least one workgroup id exists and user wishes to search on workgroups
                if (ruleResponsibilityNameCrit == null) {
                    ruleResponsibilityNameCrit = new Criteria();
                }
                Criteria workgroupCrit = new Criteria();
                workgroupCrit.addIn("ruleResponsibilityName", workgroupIds);
                workgroupCrit.addEqualTo("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
                ruleResponsibilityNameCrit.addOrCriteria(workgroupCrit);
            }
        } else if ( (workgroupIds != null) && (workgroupIds.size() == 1) ) {
            // no user and one workgroup id
            ruleResponsibilityNameCrit = new Criteria();
            ruleResponsibilityNameCrit.addLike("ruleResponsibilityName", workgroupIds.iterator().next());
            ruleResponsibilityNameCrit.addEqualTo("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        } else if ( (workgroupIds != null) && (workgroupIds.size() > 1) ) {
            // no user and more than one workgroup id
            ruleResponsibilityNameCrit = new Criteria();
            ruleResponsibilityNameCrit.addIn("ruleResponsibilityName", workgroupIds);
            ruleResponsibilityNameCrit.addEqualTo("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
        }
        if (ruleResponsibilityNameCrit != null) {
        	if (responsibilityCrit == null) {
        		responsibilityCrit = new Criteria();
        	}
            responsibilityCrit.addAndCriteria(ruleResponsibilityNameCrit);
        }
        if (responsibilityCrit != null) {
        	query = QueryFactory.newReportQuery(RuleResponsibilityBo.class, responsibilityCrit);
        	query.setAttributes(new String[] { "ruleBaseValuesId" });
        }
        return query;
    }

    private Criteria getSearchCriteria(String docTypeName, String ruleTemplateId, String ruleDescription, Boolean delegateRule, Boolean activeInd, Map extensionValues) {
        Criteria crit = new Criteria();
        crit.addEqualTo("currentInd", Boolean.TRUE);
        crit.addEqualTo("templateRuleInd", Boolean.FALSE);
        if (activeInd != null) {
            crit.addEqualTo("active", activeInd);
        }
        if (docTypeName != null) {
            crit.addLike("UPPER(docTypeName)", docTypeName.toUpperCase());
        }
        if (ruleDescription != null && !ruleDescription.trim().equals("")) {
            crit.addLike("UPPER(description)", ruleDescription.toUpperCase());
        }
        if (ruleTemplateId != null) {
            crit.addEqualTo("ruleTemplateId", ruleTemplateId);
        }
        if (delegateRule != null) {
            crit.addEqualTo("delegateRule", delegateRule);
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

                    Criteria extensionCrit2 = new Criteria();
                    extensionCrit2.addEqualTo("extensionValues.key", entry.getKey());
                    extensionCrit2.addLike("UPPER(extensionValues.value)", ("%" + (String) entry.getValue() + "%").toUpperCase());

                    // Criteria extensionCrit3 = new Criteria();
                    // extensionCrit3.addEqualTo("extensionValues.key",
                    // entry.getKey());
                    // extensionCrit3.addLike("extensionValues.value",
                    // ("%"+(String) entry.getValue()+"%").toLowerCase());

                    // extensionCrit.addOrCriteria(extensionCrit2);
                    // extensionCrit.addOrCriteria(extensionCrit3);
                    ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleExtensionBo.class, extensionCrit2);
                    query.setAttributes(new String[] { "ruleBaseValuesId" });
                    crit.addIn("ruleExtensions.ruleBaseValuesId", query);
                }
            }
        }
        return crit;
    }

    private Criteria getWorkgroupOrCriteria(Collection workgroupIds) {
        Criteria responsibilityCrit = new Criteria();
        for (Iterator iter = workgroupIds.iterator(); iter.hasNext();) {
            String workgroupIdFromList = (String) iter.next();
            Criteria orCriteria = new Criteria();
            orCriteria.addLike("ruleResponsibilityName", workgroupIdFromList);
            responsibilityCrit.addOrCriteria(orCriteria);
        }
        ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleResponsibilityBo.class, responsibilityCrit);
        query.setAttributes(new String[] { "ruleBaseValuesId" });
        Criteria crit = new Criteria();
        crit.addIn("responsibilities.ruleBaseValuesId", query);
        return crit;
    }

	private ReportQueryByCriteria getResponsibilitySubQuery(String ruleResponsibilityName) {
		Criteria responsibilityCrit = new Criteria();
		responsibilityCrit.addLike("ruleResponsibilityName", ruleResponsibilityName);
		ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleResponsibilityBo.class, responsibilityCrit);
		query.setAttributes(new String[] { "ruleBaseValuesId" });
		return query;
	}

	private ReportQueryByCriteria getWorkgroupResponsibilitySubQuery(Set<Long> workgroupIds) {
	    	Set<String> workgroupIdStrings = new HashSet<String>();
	    	for (Long workgroupId : workgroupIds) {
	    	    workgroupIdStrings.add(workgroupId.toString());
	    	}
		Criteria responsibilityCrit = new Criteria();
		responsibilityCrit.addIn("ruleResponsibilityName", workgroupIds);
		responsibilityCrit.addEqualTo("ruleResponsibilityType", KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID);
		ReportQueryByCriteria query = QueryFactory.newReportQuery(RuleResponsibilityBo.class, responsibilityCrit);
		query.setAttributes(new String[] { "ruleBaseValuesId" });
		return query;
	}

	public List<RuleBaseValues> findByPreviousRuleId(String previousRuleId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("previousRuleId", previousRuleId);
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
	}

	public RuleBaseValues findDefaultRuleByRuleTemplateId(String ruleTemplateId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("ruleTemplateId", ruleTemplateId);
		crit.addEqualTo("templateRuleInd", Boolean.TRUE);
		List rules = (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, crit));
		if (rules != null && !rules.isEmpty()) {
			return (RuleBaseValues) rules.get(0);
		}
		return null;
	}

	public void retrieveAllReferences(RuleBaseValues rule) {
		// getPersistenceBroker().retrieveAllReferences(rule);
	}

	public RuleBaseValues getParentRule(String ruleBaseValuesId) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("currentInd", Boolean.TRUE);
		criteria.addEqualTo("responsibilities.delegationRules.delegateRuleId", ruleBaseValuesId);
		Collection rules = this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleBaseValues.class, criteria));
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
		return (List)this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
			public Object doInPersistenceBroker(PersistenceBroker pb) {
				List oldDelegations = new ArrayList();
				PreparedStatement ps = null;
				ResultSet rs = null;
				try {
					ps = pb.serviceConnectionManager().getConnection().prepareStatement(OLD_DELEGATIONS_SQL);
					ps.setString(1, oldRule.getId());
					ps.setString(2, newRule.getId());
					rs = ps.executeQuery();
					while (rs.next()) {
						oldDelegations.add(findRuleBaseValuesById(rs.getString(1)));
					}
				} catch (Exception e) {
					throw new WorkflowRuntimeException("error saving deactivation date", e);
				} finally {
					if (rs != null) {
						try {
							rs.close();
						} catch (Exception e) {
							LOG.error("error closing result set", e);
						}
					}
					if (ps != null) {
						try {
							ps.close();
						} catch (Exception e) {
							LOG.error("error closing preparedstatement", e);
						}
					}
				}
				return oldDelegations;
			}
		});
	}
	
	public String findResponsibilityIdForRule(String ruleName, String ruleResponsibilityName, String ruleResponsibilityType) {
		Criteria crit = new Criteria();
		crit.addEqualTo("ruleResponsibilityName", ruleResponsibilityName);
		crit.addEqualTo("ruleResponsibilityType", ruleResponsibilityType);
		crit.addEqualTo("ruleBaseValues.currentInd", Boolean.TRUE);
		crit.addEqualTo("ruleBaseValues.name", ruleName);
		List responsibilities = (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(RuleResponsibilityBo.class, crit));
		if (responsibilities != null) {
			for (Iterator iter = responsibilities.iterator(); iter.hasNext();) {
				RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iter.next();
				return responsibility.getResponsibilityId();
			}
		}
		return null;
	}

}
