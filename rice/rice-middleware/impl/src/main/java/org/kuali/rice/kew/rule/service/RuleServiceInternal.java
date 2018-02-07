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
package org.kuali.rice.kew.rule.service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.api.rule.Rule;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.springframework.cache.annotation.CacheEvict;

/**
 * A service which provides data access and functions for the KEW Rules engine.
 *
 * @see RuleBaseValues
 * @see org.kuali.rice.kew.rule.RuleResponsibilityBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleServiceInternal extends XmlLoader, XmlExporter {

    /**
     * Returns a Rule based on unique name.  Returns null if name is null.
     * @param name the rule name
     * @return the Rule if found, null if not found or null name
     */
    public RuleBaseValues getRuleByName(String name);

    public String routeRuleWithDelegate(String documentId, RuleBaseValues parentRule, RuleBaseValues delegateRule, PrincipalContract principal, String annotation, boolean blanketApprove) throws Exception;
    //public void save(RuleBaseValues ruleBaseValues) throws Exception;
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public void save2(RuleBaseValues ruleBaseValues) throws Exception;
    public void validate2(RuleBaseValues ruleBaseValues, RuleDelegationBo ruleDelegation, List errors) throws Exception;
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public void delete(String ruleBaseValuesId);
    public RuleBaseValues findRuleBaseValuesById(String ruleBaseValuesId);
    public List<RuleBaseValues> search(String docTypeName, String ruleId, String ruleTemplateId, String ruleDescription, String groupId, String principalId, Boolean delegateRule, Boolean activeInd, Map extensionValues, String workflowIdDirective);
    public List<RuleBaseValues> searchByTemplate(String docTypeName, String ruleTemplateName, String ruleDescription, String groupId, String principalId, Boolean workgroupMember, Boolean delegateRule, Boolean activeInd, Map extensionValues, Collection<String> actionRequestCodes);
    public RuleResponsibilityBo findRuleResponsibility(String responsibilityId);
    public void deleteRuleResponsibilityById(String ruleResponsibilityId);
    public RuleResponsibilityBo findByRuleResponsibilityId(String ruleResponsibilityId);
    public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateName, String documentType);
    public List<RuleBaseValues> fetchAllCurrentRulesForTemplateDocCombination(String ruleTemplateName, String documentType, Timestamp effectiveDate);
    public List<RuleBaseValues> findByDocumentId(String documentId);
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public void makeCurrent(String documentId);
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public void makeCurrent(RuleBaseValues rule, boolean isRetroactiveUpdatePermitted);
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public void makeCurrent(RuleDelegationBo ruleDelegation, boolean isRetroactiveUpdatePermitted);
    public List<RuleBaseValues> findRuleBaseValuesByResponsibilityReviewer(String reviewerName, String type);
    public List<RuleBaseValues> findRuleBaseValuesByResponsibilityReviewerTemplateDoc(String ruleTemplateName, String documentType, String reviewerName, String type);
    public String isLockedForRouting(String currentRuleBaseValuesId);
    public List<RuleBaseValues> fetchAllRules(boolean currentRules);
    public RuleBaseValues findDefaultRuleByRuleTemplateId(String ruleTemplateId);
    public RuleBaseValues getParentRule(String ruleBaseValuesId);

    /**
     * Returns the name of the document type definition that should be used to route the given List of rules.  This method will never
     * return a null value, as it will default to the default Rule document type name if not custom document type is configured for
     * the given rules.
     */
    public String getRuleDocumentTypeName(List<RuleBaseValues> rules);

    /**
     * Checks if the Rule with the given value is a duplicate of an existing rule in the system.
     * 
     * @return the id of the duplicate rule if one exists, null otherwise
     */
    public String getDuplicateRuleId(RuleBaseValues rule);
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public RuleBaseValues saveRule(RuleBaseValues rule, boolean isRetroactiveUpdatePermitted);
    @CacheEvict(value={Rule.Cache.NAME}, allEntries = true)
    public List<RuleBaseValues> saveRules(List<RuleBaseValues> rulesToSave, boolean isRetroactiveUpdatePermitted);

    public RuleDelegationBo saveRuleDelegation(RuleDelegationBo ruleDelegation, boolean isRetroactiveUpdatePermitted);

    public List<RuleDelegationBo> saveRuleDelegations(List<RuleDelegationBo> ruleDelegationsToSave, boolean isRetroactiveUpdatePermitted);
    
    public String findResponsibilityIdForRule(String ruleName, String ruleResponsibilityName, String ruleResponsibilityType);
}
