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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.web.WebRuleUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.web.ui.Section;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.kns.maintenance.Maintainable;

/**
 * This class is the maintainable implementation for Routing Rules 
 * in KEW (represented by the {@link RuleBaseValues} business object). 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RoutingRuleDelegationMaintainable extends KualiMaintainableImpl {
	
	/**
	 * Override the getSections method on this maintainable so that the Section Containing the various Rule Attributes
	 * can be dynamically generated based on the RuleTemplate which is selected.
	 */
	@Override
	public List getSections(MaintenanceDocument document, Maintainable oldMaintainable) {
		List<Section> sections = super.getSections(document, oldMaintainable);
		return WebRuleUtils.customizeSections(getThisRule(), sections, true);
	}
	
	/**
	 * On creation of a new rule document, we must validate that a rule template and document type are set. 
	 */
	@Override
	public void processAfterNew(MaintenanceDocument document,
			Map<String, String[]> parameters) {
		initializeBusinessObjects(document);
		WebRuleUtils.validateRuleAndResponsibility(getOldRuleDelegation(document), getNewRuleDelegation(document), parameters);
		WebRuleUtils.validateRuleTemplateAndDocumentType(getOldRule(document), getNewRule(document), parameters);
		WebRuleUtils.establishDefaultRuleValues(getNewRule(document));
		getNewRule(document).setDocumentId(document.getDocumentHeader().getDocumentNumber());
	}
		
	/**
	 * Creates the initial structure of the new business object so that it can be properly
	 * populated with non-null object references.
	 */
	private void initializeBusinessObjects(MaintenanceDocument document) {
		RuleDelegationBo oldRuleDelegation = getOldRuleDelegation(document);
		RuleDelegationBo newRuleDelegation = getNewRuleDelegation(document);
        
		if (oldRuleDelegation.getDelegationRule() == null) {
			oldRuleDelegation.setDelegationRule(new RuleBaseValues());
		}
		if (newRuleDelegation.getDelegationRule() == null) {
			newRuleDelegation.setDelegationRule(new RuleBaseValues());
		}
	}
	
	/**
	 * This is a hack to get around the fact that when a document is first created, this value is
 	 * true which causes issues if you want to be able to initialize fields on  the document using
 	 * request parameters.  See SectionBridge.toSection for the "if" block where it populates
 	 * Field.propertyValue to see why this causes problems
	 */
	@Override
	public void setGenerateDefaultValues(String docTypeName) {		
		
	}
	
	/**
     * A complete override of the implementation for saving a Rule
     */
    @Override
    public void saveBusinessObject() {
    	WebRuleUtils.clearKeysForSave(getThisRuleDelegation());
    	WebRuleUtils.translateResponsibilitiesForSave(getThisRule());
    	WebRuleUtils.translateFieldValuesForSave(getThisRule());
    	WebRuleUtils.processRuleForDelegationSave(getThisRuleDelegation());
    	KEWServiceLocator.getRuleService().makeCurrent(getThisRuleDelegation(), true);
    }

    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> parameters) {
    	WebRuleUtils.processRuleForCopy(document.getDocumentNumber(), getOldRule(document), getNewRule(document));
        super.processAfterCopy(document, parameters);
    }
    
	@Override
	public void processAfterEdit(MaintenanceDocument document,
			Map<String, String[]> parameters) {
		if (!getOldRule(document).getCurrentInd()) {
			throw new RiceRuntimeException("Cannot edit a non-current version of a rule.");
		}
		WebRuleUtils.populateForCopyOrEdit(getOldRule(document), getNewRule(document));
		getNewRule(document).setPreviousRuleId(getOldRule(document).getId());
		getNewRule(document).setDocumentId(document.getDocumentHeader().getDocumentNumber());
		super.processAfterEdit(document, parameters);
	}

    
	@Override
	public List<MaintenanceLock> generateMaintenanceLocks() {
		if (getThisRule().getId() == null) {
			return Collections.emptyList();
		}
		return super.generateMaintenanceLocks();
	}
    
    @Override
	public String getDocumentTitle(MaintenanceDocument document) {
		StringBuffer title = new StringBuffer();
        RuleBaseValues rule = getThisRule();
        if (rule.getPreviousRuleId() != null) {
            title.append("Editing Rule Delegation '").append(rule.getDescription()).append("'");
        } else {
            title.append("Adding Rule Delegation '").append(rule.getDescription()).append("'");
        }
        return title.toString();
	}
	
	protected RuleDelegationBo getNewRuleDelegation(MaintenanceDocument document) {
		return (RuleDelegationBo)document.getNewMaintainableObject().getDataObject();
	}
	
	protected RuleDelegationBo getOldRuleDelegation(MaintenanceDocument document) {
		return (RuleDelegationBo)document.getOldMaintainableObject().getDataObject();
	}

	protected RuleDelegationBo getThisRuleDelegation() {
		return (RuleDelegationBo)getDataObject();
	}

	protected RuleBaseValues getNewRule(MaintenanceDocument document) {
		return getNewRuleDelegation(document).getDelegationRule();
	}

	protected RuleBaseValues getOldRule(MaintenanceDocument document) {
		return getOldRuleDelegation(document).getDelegationRule();
	}

	protected RuleBaseValues getThisRule() {
		return getThisRuleDelegation().getDelegationRule();
	}
	
	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.maintenance.KualiMaintainableImpl#prepareForSave()
	 */
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		WebRuleUtils.translateResponsibilitiesForSave(getThisRule());
	}
	

	
	
}
