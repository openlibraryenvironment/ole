/*
 * Copyright 2013 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject.options;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

public class OlePurchaseOrderStatusFinder extends KeyValuesBase {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurchaseOrderStatusFinder.class);

    @Override
    public List getKeyValues() {
        LOG.debug("Inside getKeyValues of OlePurchaseOrderStatusFinder");
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByNameCaseInsensitive(
                OLEConstants.FinancialDocumentTypeCodes.PURCHASE_ORDER);
        List<ApplicationDocumentStatus> applicationDocumentStatus = documentType.getValidApplicationStatuses();
        List labels = new ArrayList();
        labels.add(new ConcreteKeyValue("", ""));
        for (ApplicationDocumentStatus status : applicationDocumentStatus) {
            labels.add(new ConcreteKeyValue(status.getStatusName(), status.getStatusName()));
        }
        return labels;
    }

}
