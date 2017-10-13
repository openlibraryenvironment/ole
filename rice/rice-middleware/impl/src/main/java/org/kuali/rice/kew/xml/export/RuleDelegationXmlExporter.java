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
package org.kuali.rice.kew.xml.export;

import org.jdom.Element;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.util.xml.XmlRenderer;
import org.kuali.rice.core.framework.impex.xml.XmlExporter;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;

import java.util.Iterator;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;

/**
 * Exports rules to XML.
 *
 * @see RuleBaseValues
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleDelegationXmlExporter implements XmlExporter {

    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(getClass());

    private XmlRenderer renderer = new XmlRenderer(RULE_NAMESPACE);
    private RuleXmlExporter ruleExporter = new RuleXmlExporter(RULE_NAMESPACE);

	@Override
	public boolean supportPrettyPrint() {
		return true;
	}

    public Element export(ExportDataSet exportDataSet) {
    	KewExportDataSet dataSet = KewExportDataSet.fromExportDataSet(exportDataSet);
        if (!dataSet.getRuleDelegations().isEmpty()) {
            Element rootElement = renderer.renderElement(null, RULE_DELEGATIONS);
            rootElement.setAttribute(SCHEMA_LOCATION_ATTR, RULE_SCHEMA_LOCATION, SCHEMA_NAMESPACE);
            for (Iterator iterator = dataSet.getRuleDelegations().iterator(); iterator.hasNext();) {
                RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
                exportRuleDelegation(rootElement, ruleDelegation);
            }
            return rootElement;
        }
        return null;
    }

    private void exportRuleDelegation(Element parent, RuleDelegationBo ruleDelegation) {
    	Element ruleDelegationElement = renderer.renderElement(parent, RULE_DELEGATION);
    	exportParentResponsibility(ruleDelegationElement, ruleDelegation);
    	renderer.renderTextElement(ruleDelegationElement, DELEGATION_TYPE, ruleDelegation.getDelegationType().getCode());
    	ruleExporter.exportRule(ruleDelegationElement, ruleDelegation.getDelegationRule());
    }
    
    private void exportParentResponsibility(Element parent, RuleDelegationBo delegation) {
        Element parentResponsibilityElement = renderer.renderElement(parent, PARENT_RESPONSIBILITY);
        RuleResponsibilityBo ruleResponsibility = KEWServiceLocator.getRuleService().findRuleResponsibility(delegation.getResponsibilityId());
        renderer.renderTextElement(parentResponsibilityElement, PARENT_RULE_NAME, ruleResponsibility.getRuleBaseValues().getName());
        if (ruleResponsibility.isUsingPrincipal()) {
        	Principal principal = ruleResponsibility.getPrincipal();
        	renderer.renderTextElement(parentResponsibilityElement, PRINCIPAL_NAME, principal.getPrincipalName());
        } else if (ruleResponsibility.isUsingGroup()) {
        	Group group = ruleResponsibility.getGroup();
        	Element groupElement = renderer.renderElement(parentResponsibilityElement, GROUP_NAME);
        	groupElement.setText(group.getName());
        	groupElement.setAttribute(NAMESPACE, group.getNamespaceCode());
        } else if (ruleResponsibility.isUsingRole()) {
        	renderer.renderTextElement(parentResponsibilityElement, ROLE, ruleResponsibility.getRuleResponsibilityName());
        } else {
        	throw new RiceRuntimeException("Encountered a rule responsibility when exporting with an invalid type of '" + ruleResponsibility.getRuleResponsibilityType());
        }
    }

}
