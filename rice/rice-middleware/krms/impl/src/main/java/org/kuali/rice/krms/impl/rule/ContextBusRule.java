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
package org.kuali.rice.krms.impl.rule;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.impl.repository.ContextBo;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.util.KRMSPropertyConstants;

public class ContextBusRule extends MaintenanceDocumentRuleBase {
    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;

        ContextBo newContext = (ContextBo) document.getNewMaintainableObject().getDataObject();
        ContextBo oldContext = (ContextBo) document.getOldMaintainableObject().getDataObject();
        boolean isEditAction = KRADConstants.MAINTENANCE_EDIT_ACTION.equals(document.getNewMaintainableObject().getMaintenanceAction());

        isValid &= validateId(oldContext, newContext, isEditAction);
        isValid &= validateNameNamespace(newContext, isEditAction);

        return isValid;
    }

    private boolean validateId(ContextBo oldContext, ContextBo newContext, boolean isEditAction) {
        if (StringUtils.isBlank(newContext.getId())) {
            this.putFieldError(KRMSPropertyConstants.Context.CONTEXT_ID, "error.context.blankId");
            return false;
        }
        if (isEditAction) {
            if (!oldContext.getId().equals(newContext.getId())) {
                throw new IllegalStateException("The ID of a Context being edited must not change.");
            }
        } else {
            ContextDefinition contextInDatabase = getContextBoService().getContextByContextId(newContext.getId());
            if (contextInDatabase  != null) {
                this.putFieldError(KRMSPropertyConstants.Context.CONTEXT_ID, "error.context.duplicateId");
                return false;
            }
        }

        return true;
    }

    /**
     * Check if the name-namespace pair already exist.
     * @param newContext
     * @return true if the name-namespace pair is unique, false otherwise
     */
    private boolean validateNameNamespace(ContextBo newContext, boolean isEditAction) {
        if (isEditAction) {
            ContextDefinition contextInDatabase = getContextBoService().getContextByNameAndNamespace(newContext.getName(), newContext
                    .getNamespace());
            if ((contextInDatabase != null) && !contextInDatabase.getId().equals(newContext.getId())) { // if ID is the same, it's not a duplicate
                this.putFieldError(KRMSPropertyConstants.Context.NAME, "error.context.duplicateNameNamespace");
                return false;
            }
        } else if (StringUtils.isNotBlank(newContext.getName()) && StringUtils.isNotBlank(newContext.getNamespace())) {
            ContextDefinition contextInDatabase = getContextBoService().getContextByNameAndNamespace(newContext.getName(), newContext
                    .getNamespace());
            if(contextInDatabase != null) {
                this.putFieldError(KRMSPropertyConstants.Context.NAME, "error.context.duplicateNameNamespace");
                return false;
            }
        } else {
            if (StringUtils.isBlank(newContext.getName())) {
                this.putFieldError(KRMSPropertyConstants.Context.NAME, "error.context.blankName");
            }
            if (StringUtils.isBlank(newContext.getNamespace())) {
                this.putFieldError(KRMSPropertyConstants.Context.NAME, "error.context.blankNamespace");
            }
            return false;
        }

        return true;
    }

    public ContextBoService getContextBoService() {
        return KrmsRepositoryServiceLocator.getContextBoService();
    }
}
