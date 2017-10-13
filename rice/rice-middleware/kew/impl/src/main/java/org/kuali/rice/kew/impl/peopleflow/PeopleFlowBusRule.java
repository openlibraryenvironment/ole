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
package org.kuali.rice.kew.impl.peopleflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.impl.KewImplConstants;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PeopleFlowBusRule extends MaintenanceDocumentRuleBase {
    private BusinessObjectService businessObjectService;

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean result = super.processCustomSaveDocumentBusinessRules(document);
        PeopleFlowBo peopleFlowDoc = (PeopleFlowBo)document.getNewMaintainableObject().getDataObject();
        if (StringUtils.isBlank(peopleFlowDoc.getId())) {
            result &= checkIfDuplicatePeopleFlow(peopleFlowDoc);
        }
        return result;
    }

    protected boolean checkIfDuplicatePeopleFlow(PeopleFlowBo peopleFlowBo){
        boolean rulePassed = true;
        if (!(StringUtils.isBlank(peopleFlowBo.getName()) ||
              StringUtils.isBlank(peopleFlowBo.getNamespaceCode()))) {
                Map<String,String> criteria = new HashMap<String,String>();
                criteria.put(KewImplConstants.PropertyConstants.NAMESPACE_CODE, peopleFlowBo.getNamespaceCode());
                criteria.put(KewImplConstants.PropertyConstants.NAME, peopleFlowBo.getName());
                Collection<PeopleFlowBo> peopleFlows = getBusinessObjectService().findMatching(PeopleFlowBo.class,criteria);
                if (CollectionUtils.isEmpty(peopleFlows)) {
                    rulePassed = true;
                } else {
                    GlobalVariables.getMessageMap().putError("document.peopleFlow.duplicate",
                            RiceKeyConstants.PEOPLEFLOW_DUPLICATE, peopleFlowBo.getName().toString(), peopleFlowBo.getNamespaceCode().toString());
                    rulePassed = false;
                }
        }
        return rulePassed;
    }

    public BusinessObjectService getBusinessObjectService() {
        if(businessObjectService == null){
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
