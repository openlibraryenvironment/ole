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

import org.kuali.ole.select.bo.OleAgreementStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleAgreementDocTypeKeyValues is the value finder class for OleAgreementDocType Maintenance Document
 */
public class OleAgreementStatusKeyValues extends KeyValuesBase {

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
        Collection<OleAgreementStatus> oleAgreementStatuses = KRADServiceLocator.getBusinessObjectService().findAll(OleAgreementStatus.class);
        for (OleAgreementStatus oleAgreementStatus : oleAgreementStatuses) {
            keyValues.add(new ConcreteKeyValue(oleAgreementStatus.getAgreementStatusId(), oleAgreementStatus.getAgreementStatusName()));
        }
        return keyValues;
    }

}
