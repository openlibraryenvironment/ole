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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;

public class DocumentTypeMaintainableBusRule extends MaintenanceDocumentRuleBase {

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean result = super.processCustomSaveDocumentBusinessRules(document);

        result &= checkDoctypeName(document);
        result &= checkDoctypeLabel(document);

        return result;
    }

    @Override
    protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
        boolean result = super.processCustomRouteDocumentBusinessRules(document);

        result &= checkDoctypeName(document);
        result &= checkDoctypeLabel(document);

        return result;
    }

    /**
     * Checks if the doctype name already exist.
     *
     * @param document
     * @return false, if doctype name already exists otherwise true.
     */
    public boolean checkDoctypeName(MaintenanceDocument document) {
        boolean result = true;
        DocumentType bo = (DocumentType) document.getNewMaintainableObject().getDataObject();
        if (null != bo.getName()) {
            DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(bo.getName());
            if (null != documentType && document.isNew()) {
                result = false;
                putFieldError("name", "documenttype.name.duplicate");
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Checks that the doctype label is specified.
     *
     * @param document
     * @return false if doctype label is blank, otherwise true.
     */
    public boolean checkDoctypeLabel(MaintenanceDocument document) {
        boolean isValid = true;
        DocumentType bo = (DocumentType) document.getNewMaintainableObject().getDataObject();

        isValid = !StringUtils.isBlank(bo.getLabel());

        return isValid;
    }
}
