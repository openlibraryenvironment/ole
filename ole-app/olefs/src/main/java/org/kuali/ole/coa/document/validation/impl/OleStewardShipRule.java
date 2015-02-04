/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.coa.document.validation.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.ole.coa.businessobject.OleStewardship;
import org.kuali.ole.sys.OLEKeyConstants;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

public class OleStewardShipRule extends MaintenanceDocumentRuleBase{
    
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        isValid &= validateStewardShipTypeName(document);
        return isValid;
        
    }
    
    /**
     * This method  validates duplicate  stewardShipTypeName and return boolean value.
     * @param oleStewardship
     * @return boolean
     */
    private boolean validateStewardShipTypeName(MaintenanceDocument document) {
        OleStewardship oleStewardship = (OleStewardship) document.getNewMaintainableObject().getBusinessObject();
        if (!document.isEdit() && oleStewardship.getStewardshipTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEKeyConstants.STWRDSHIP_TYPE_NAME, oleStewardship.getStewardshipTypeName());
            List<OleStewardship> oleStewardshipList = (List<OleStewardship>) getBoService().findMatching(OleStewardship.class, criteria);

            if ((oleStewardshipList.size() > 0)) {
                for(OleStewardship oleStewardshipObj:oleStewardshipList){
                    String stewardShipTypeName=oleStewardshipObj.getStewardshipTypeName();
                    if(null==oleStewardship.getStewardshipTypeName()|| oleStewardship.getStewardshipTypeName().equalsIgnoreCase(stewardShipTypeName)) {
                        putFieldError(OLEKeyConstants.STWRDSHIP_TYPE_NAME, OLEKeyConstants.ERROR_DUP_FOUND_STWRD_TYP_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
