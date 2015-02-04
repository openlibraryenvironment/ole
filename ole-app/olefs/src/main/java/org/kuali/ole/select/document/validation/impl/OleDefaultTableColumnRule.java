/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleDefaultTableColumn;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleDefaultTableColumnRule extends MaintenanceDocumentRuleBase {

    @Override
    protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {

        boolean valid = processValidation(document);
        return valid & super.processCustomRouteDocumentBusinessRules(document);
    }

    private boolean processValidation(MaintenanceDocument document) {
        boolean valid = true;
        valid &= processDocumentTypeValidation(document);
        return valid;
    }

    private boolean processDocumentTypeValidation(MaintenanceDocument document) {
        boolean valid = true;
        OleDefaultTableColumn oleDefaultTableColumn = (OleDefaultTableColumn) document.getNewMaintainableObject().getBusinessObject();
        String documentTypeId = oleDefaultTableColumn.getDocumentTypeId();
        Map<String, Object> documentTypes = new HashMap<String, Object>();
        documentTypes.put("documentTypeId", documentTypeId);
        List<OleDefaultTableColumn> documentTypeList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultTableColumn.class, documentTypes);
        if (documentTypeList.size() > 0) {
            for (int i = 0; i < documentTypeList.size(); i++) {
                if (documentTypeList.get(i).getDocumentTypeId().toString().equalsIgnoreCase(oleDefaultTableColumn.getDocumentTypeId().toString()) && documentTypeList.get(i).getDocumentColumn().equalsIgnoreCase(oleDefaultTableColumn.getDocumentColumn())) {
                    valid = false;
                    putFieldError(OleSelectConstant.DEFAULT_TABLE_COLUMN_DOCUMENTTYPE, OleSelectPropertyConstants.ERROR_DOCUMENTTYPE_EXIST);
                }
            }
        }
        return valid;
    }
}
