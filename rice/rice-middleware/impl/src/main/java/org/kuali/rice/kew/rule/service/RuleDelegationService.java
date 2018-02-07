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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;
import org.kuali.rice.kew.rule.RuleDelegationBo;

/**
 * A service providing data access for {@link org.kuali.rice.kew.rule.RuleDelegationBo}s.
 *
 * @see org.kuali.rice.kew.rule.RuleDelegationBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleDelegationService extends XmlLoader, XmlExporter {

    public List<RuleDelegationBo> findByDelegateRuleId(String ruleId);
    public void save(RuleDelegationBo ruleDelegation);
    public void delete(String ruleDelegationId);
    public List<RuleDelegationBo> findAllCurrentRuleDelegations();
    public RuleDelegationBo findByRuleDelegationId(String ruleDelegationId);
    public List<RuleDelegationBo> search(String parentRuleBaseVaueId, String parentResponsibilityId,  String docTypeName, String ruleId, String ruleTemplateId, String ruleDescription, String groupId, String principalId, String delegationType, Boolean activeInd, Map extensionValues, String workflowIdDirective);
    public List<RuleDelegationBo> searchByTemplate(String parentRuleBaseVaueId, String parentResponsibilityId,  String docTypeName, String ruleTemplateName, String ruleDescription, String groupId, String principalId, Boolean workgroupMember, String delegationType, Boolean activeInd, Map extensionValues, Collection<String> actionRequestCodes);
    /**
     * Returns a List of all RuleDelegations with "current" Rules for the given
     * responsibility id.
     */
    public List<RuleDelegationBo> findByResponsibilityId(String responsibilityId);
}
