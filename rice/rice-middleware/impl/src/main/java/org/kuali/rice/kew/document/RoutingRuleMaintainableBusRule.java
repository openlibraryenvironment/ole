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
package org.kuali.rice.kew.document;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.rule.RuleTemplate;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.rule.GroupRuleResponsibility;
import org.kuali.rice.kew.rule.PersonRuleResponsibility;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.WorkflowRuleAttributeRows;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.web.WebRuleUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - Garey don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RoutingRuleMaintainableBusRule extends MaintenanceDocumentRuleBase {

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase#processCustomSaveDocumentBusinessRules(org.kuali.rice.krad.maintenance.MaintenanceDocument)
	 */
	@Override
	protected boolean processCustomSaveDocumentBusinessRules(
			MaintenanceDocument document) {

		boolean isValid = true;

		RuleBaseValues ruleBaseValues = this.getRuleBaseValues(document);
		RuleBaseValues oldRuleBaseValues = this.getOldRuleBaseValues(document);
		
		isValid &= this.populateErrorMap(ruleBaseValues);


		return isValid;
	}

	protected RuleBaseValues getRuleBaseValues(MaintenanceDocument document){
		return (RuleBaseValues)document.getNewMaintainableObject().getBusinessObject();
	}
	
	protected RuleBaseValues getOldRuleBaseValues(MaintenanceDocument document){
		return (RuleBaseValues)document.getOldMaintainableObject().getBusinessObject();
	}
	

	protected void populateErrorMap(Map<String,String> errorMap){
		for(Map.Entry<String, String> entry : errorMap.entrySet()){
			this.putFieldError(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase#processCustomAddCollectionLineBusinessRules(org.kuali.rice.krad.maintenance.MaintenanceDocument, java.lang.String, org.kuali.rice.krad.bo.PersistableBusinessObject)
	 */
	@Override
	public boolean processCustomAddCollectionLineBusinessRules(
			MaintenanceDocument document, String collectionName,
			PersistableBusinessObject line) {

		boolean isValid = true;

		if(getPersonSectionName().equals(collectionName)){
			PersonRuleResponsibility pr = (PersonRuleResponsibility)line;
			String name = pr.getPrincipalName();

			if(!personExists(name)){
				isValid &= false;
				this.putFieldError(getPersonSectionName(), "error.document.personResponsibilities.principleDoesNotExist");
			}
		}else if(getGroupSectionName().equals(collectionName)){
			GroupRuleResponsibility gr = (GroupRuleResponsibility)line;
			if(!groupExists(gr.getNamespaceCode(), gr.getName())){
				isValid &= false;
				this.putFieldError(getGroupSectionName(), "error.document.personResponsibilities.groupDoesNotExist");
			}
		}

		return isValid;
	}

	protected String getPersonSectionName(){
		return KEWPropertyConstants.PERSON_RESP_SECTION;
	}
	protected String getGroupSectionName(){
		return KEWPropertyConstants.GROUP_RESP_SECTION;
	}

	protected boolean personExists(String principalName){
		boolean bRet = false;
		try{
			KEWServiceLocator.getIdentityHelperService().getIdForPrincipalName(principalName);
			bRet = true;
		}catch(Exception ex){
			bRet = false;
			//ex.printStackTrace();
		}

		return bRet;
	}

	protected boolean groupExists(String namespaceCode, String groupName){
		boolean bRet = false;
		try{
			KEWServiceLocator.getIdentityHelperService().getGroupByName(namespaceCode, groupName);
			bRet = true;
		}catch(Exception ex){
			bRet = false;
			//ex.printStackTrace();
		}
		return bRet;
	}

	protected boolean populateErrorMap(RuleBaseValues ruleBaseValues){

		boolean isValid = true;

		if (getDocumentTypeService().findByName(ruleBaseValues.getDocTypeName()) == null) {
            this.putFieldError("docTypeName", "doctype.documenttypeservice.doctypename.required");
            isValid &= false;
        }
        if(ruleBaseValues.getName() != null){
        	if(ruleExists(ruleBaseValues)){
        		this.putFieldError("name", "routetemplate.ruleservice.name.unique");
            	isValid &= false;
        	}
        }

        /*
         * Logic: If both from and to dates exist, make sure toDate is after fromDate
         */
        if(ruleBaseValues.getToDateValue() != null && ruleBaseValues.getFromDateValue() != null){
        	if (ruleBaseValues.getToDateValue().before(ruleBaseValues.getFromDateValue())) {
    			this.putFieldError("toDate", "error.document.maintainableItems.toDate");
    			isValid &= false;
            }
        }

		if(!setRuleAttributeErrors(ruleBaseValues)){
			isValid &= false;
		}

		// This doesn't map directly to a single field. It's either the person or the group tab
        if (ruleBaseValues.getRuleResponsibilities().isEmpty()) {
        	this.putFieldError("Responsibilities", "error.document.responsibility.required");
        	isValid &= false;
        } else {
            for (RuleResponsibilityBo responsibility : ruleBaseValues.getRuleResponsibilities()) {
                if (responsibility.getRuleResponsibilityName() != null && KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID.equals(responsibility.getRuleResponsibilityType())) {
                    if (getGroupService().getGroup(responsibility.getRuleResponsibilityName()) == null) {
                    	this.putFieldError("Groups", "routetemplate.ruleservice.workgroup.invalid");
                    	isValid &= false;
                    }
                } else if (responsibility.getPrincipal() == null && responsibility.getRole() == null) {
                	this.putFieldError("Persons", "routetemplate.ruleservice.user.invalid");
                	isValid &= false;
                }
            }
        }

        return isValid;
	}

	protected boolean ruleExists(RuleBaseValues rule){
		boolean bRet = false;

		RuleBaseValues tmp = KEWServiceLocator.getRuleService().getRuleByName(rule.getName());

		if(tmp != null) {
		    if ((rule.getPreviousRuleId() == null)
		         || (rule.getPreviousRuleId() != null
		            && !rule.getPreviousRuleId().equals(tmp.getId()))) {
			    bRet = true;
		    }
		}

		return bRet;
	}

	protected DocumentTypeService getDocumentTypeService() {
        return (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
    }


	protected boolean setRuleAttributeErrors(RuleBaseValues rule){

		boolean isValid = true;

		RuleTemplate ruleTemplate = KewApiServiceLocator.getRuleService().getRuleTemplate(rule.getRuleTemplateId());

		/** Populate rule extension values * */
		for (RuleTemplateAttribute ruleTemplateAttribute : ruleTemplate.getActiveRuleTemplateAttributes()) {
            if (!RuleAttribute.isWorkflowAttribute(ruleTemplateAttribute.getRuleAttribute().getType())) {
                continue;
            }
            Map<String, String> parameterMap = WebRuleUtils.getFieldMapForRuleTemplateAttribute(rule, ruleTemplateAttribute);
            WorkflowRuleAttributeRows rows =
                    KEWServiceLocator.getWorkflowRuleAttributeMediator().getRuleRows(parameterMap, ruleTemplateAttribute);

			// TODO hook validation of rule data into PreRules
            List<RemotableAttributeError> errors = rows.getValidationErrors();
			if (!errors.isEmpty()) {
                isValid = false;
				for(RemotableAttributeError error: errors){
				    this.putFieldError("RuleAttributes", RiceKeyConstants.ERROR_CUSTOM, error.getMessage());
				}
			}
		}
		return isValid;

	}

}
