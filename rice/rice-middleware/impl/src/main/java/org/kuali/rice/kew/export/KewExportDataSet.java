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
package org.kuali.rice.kew.export;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.group.Group;

/**
 * Defines a set of data to export from Kuali Enterprise Workflow.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KewExportDataSet {

	public static final QName DOCUMENT_TYPES = new QName("KEW", "documentTypes");
	public static final QName GROUPS = new QName("KEW", "groups");
	public static final QName RULE_ATTRIBUTES = new QName("KEW", "ruleAttributes");
	public static final QName RULE_TEMPLATES = new QName("KEW", "ruleTemplates");
	public static final QName RULES = new QName("KEW", "rules");
	public static final QName RULE_DELEGATIONS = new QName("KEW", "ruleDelegations");
	public static final QName HELP = new QName("KEW", "help");
	public static final QName EDOCLITES = new QName("KEW", "eDocLites");
	
	private List<DocumentType> documentTypes = new ArrayList<DocumentType>();
	private List<Group> groups = new ArrayList<Group>();
	private List<RuleAttribute> ruleAttributes = new ArrayList<RuleAttribute>();
	private List<RuleTemplateBo> ruleTemplates = new ArrayList<RuleTemplateBo>();
	private List<RuleBaseValues> rules = new ArrayList<RuleBaseValues>();
	private List<RuleDelegationBo> ruleDelegations = new ArrayList<RuleDelegationBo>();

	public List<DocumentType> getDocumentTypes() {
		return documentTypes;
	}

	public List<RuleAttribute> getRuleAttributes() {
		return ruleAttributes;
	}

	public List<RuleBaseValues> getRules() {
		return rules;
	}

	public List<RuleTemplateBo> getRuleTemplates() {
		return ruleTemplates;
	}

	public List<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<RuleDelegationBo> getRuleDelegations() {
		return this.ruleDelegations;
	}
	
	public void populateExportDataSet(ExportDataSet exportDataSet) {
		if (documentTypes != null && !documentTypes.isEmpty()) {
            /* 
             * this is a terrible hack to fix a problem where not everything for document type is getting exported 
             * This is caused because the KEWModuleService creates an EBO from the api DocumentTypeService, which doesn't contain
             * all the data needed for exporting.
             * 
             * Sooo... we put this ugly code here until we can hope to remove EBOs from this project, or create a DocumentType dto class that 
             * contains the information needed
             */
            List<DocumentType> correctDocumentTypes = new ArrayList<DocumentType>();
            for (DocumentType docType : documentTypes) {
                correctDocumentTypes.add(KEWServiceLocator.getDocumentTypeService().findById(docType.getDocumentTypeId()));
            }
			exportDataSet.addDataSet(DOCUMENT_TYPES, correctDocumentTypes);
		}
		if (groups != null && !groups.isEmpty()) {
			exportDataSet.addDataSet(GROUPS, groups);
		}
		if (ruleAttributes != null && !ruleAttributes.isEmpty()) {
			exportDataSet.addDataSet(RULE_ATTRIBUTES, ruleAttributes);
		}
		if (ruleTemplates != null && !ruleTemplates.isEmpty()) {
			exportDataSet.addDataSet(RULE_TEMPLATES, ruleTemplates);
		}
		if (rules != null && !rules.isEmpty()) {
			exportDataSet.addDataSet(RULES, rules);
		}
		if (ruleDelegations != null && !ruleDelegations.isEmpty()) {
			exportDataSet.addDataSet(RULE_DELEGATIONS, ruleDelegations);
		}
	}
	
	public ExportDataSet createExportDataSet() {
		ExportDataSet exportDataSet = new ExportDataSet();
		populateExportDataSet(exportDataSet);
		return exportDataSet;
	}
	
	public static KewExportDataSet fromExportDataSet(ExportDataSet exportDataSet) {
		KewExportDataSet kewExportDataSet = new KewExportDataSet();
		
		List<DocumentType> documentTypes = (List<DocumentType>)exportDataSet.getDataSets().get(DOCUMENT_TYPES);
		if (documentTypes != null) {
			kewExportDataSet.getDocumentTypes().addAll(documentTypes);
		}
		List<Group> groups = (List<Group>)exportDataSet.getDataSets().get(GROUPS);
		if (groups != null) {
			kewExportDataSet.getGroups().addAll(groups);
		}
		List<RuleAttribute> ruleAttributes = (List<RuleAttribute>)exportDataSet.getDataSets().get(RULE_ATTRIBUTES);
		if (ruleAttributes != null) {
			kewExportDataSet.getRuleAttributes().addAll(ruleAttributes);
		}
		List<RuleTemplateBo> ruleTemplates = (List<RuleTemplateBo>)exportDataSet.getDataSets().get(RULE_TEMPLATES);
		if (ruleTemplates != null) {
			kewExportDataSet.getRuleTemplates().addAll(ruleTemplates);
		}
		List<RuleBaseValues> rules = (List<RuleBaseValues>)exportDataSet.getDataSets().get(RULES);
		if (rules != null) {
			kewExportDataSet.getRules().addAll(rules);
		}
		List<RuleDelegationBo> ruleDelegations = (List<RuleDelegationBo>)exportDataSet.getDataSets().get(RULE_DELEGATIONS);
		if (ruleDelegations != null) {
			kewExportDataSet.getRuleDelegations().addAll(ruleDelegations);
		}
		
		return kewExportDataSet;
	}
	
}
