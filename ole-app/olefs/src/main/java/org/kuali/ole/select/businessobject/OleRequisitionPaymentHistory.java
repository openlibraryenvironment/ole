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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OleRequisitionPaymentHistory extends PersistableBusinessObjectBase implements OlePaymentHistory {

    private String paymentHistory;


    /**
     * Gets the paymentHistory attribute.
     *
     * @return Returns the paymentHistory.
     */
    public String getPaymentHistory() {
        return paymentHistory;
    }


    /**
     * Sets the paymentHistory attribute value.
     *
     * @param paymentHistory The paymentHistory to set.
     */
    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }


    @SuppressWarnings("rawtypes")
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("paymentHistory", paymentHistory);
        return map;
    }

}
