/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.ole.select.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OleLicenseRequestWorkflowType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleLicenseRequestWorkflowTypeKeyValues is the value finder class for OleLicenseRequestWorkflowType
 */
public class OleLicenseRequestWorkflowTypeKeyValues extends KeyValuesBase {

    private boolean blankOption;

    /**
     * Gets the blankOption attribute.
     *
     * @return Returns the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * Sets the blankOption attribute value.
     *
     * @param blankOption The blankOption to set.
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    /**
     * Gets the keyValues attribute.
     *
     * @return Returns the keyValues
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleLicenseRequestWorkflowType> licenseRequestWorkflowTypes =
                KRADServiceLocator.getBusinessObjectService().findAll(OleLicenseRequestWorkflowType.class);
        PermissionService service = KimApiServiceLocator.getPermissionService();
        boolean initialFilter = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(),
                OLEConstants.OleLicenseRequest.LICENSE_NMSPACE, OLEConstants.OleLicenseRequest.WORKFLOW_INITIAL_FILTER);
        boolean secFilter = service.hasPermission(GlobalVariables.getUserSession().getPrincipalId(),
                OLEConstants.OleLicenseRequest.LICENSE_NMSPACE, OLEConstants.OleLicenseRequest.WORKFLOW_SEC_FILTER);
        keyValues.add(new ConcreteKeyValue("", ""));
        if (initialFilter) {
            for (OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType : licenseRequestWorkflowTypes) {
                if (!OLEConstants.OleLicenseRequest.INITIAL_FILTER_WORKFLOW_CODE.containsKey(oleLicenseRequestWorkflowType.getCode())) {
                    keyValues.add(new ConcreteKeyValue(oleLicenseRequestWorkflowType.getCode(), oleLicenseRequestWorkflowType.getName()));
                }
            }
            return keyValues;
        } else if (secFilter) {
            for (OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType : licenseRequestWorkflowTypes) {
                if (!OLEConstants.OleLicenseRequest.SEC_FILTER_WORKFLOW_CODE.containsKey(oleLicenseRequestWorkflowType.getCode())) {
                    keyValues.add(new ConcreteKeyValue(oleLicenseRequestWorkflowType.getCode(), oleLicenseRequestWorkflowType.getName()));
                }
            }
            return keyValues;
        } else {
            for (OleLicenseRequestWorkflowType oleLicenseRequestWorkflowType : licenseRequestWorkflowTypes) {
                keyValues.add(new ConcreteKeyValue(oleLicenseRequestWorkflowType.getCode(), oleLicenseRequestWorkflowType.getName()));
            }
            return keyValues;
        }
    }
}
