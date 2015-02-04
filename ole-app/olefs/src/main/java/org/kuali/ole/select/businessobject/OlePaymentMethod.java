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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class OlePaymentMethod extends PersistableBusinessObjectBase implements MutableInactivatable {
    private Integer paymentMethodId;
    private String paymentMethod;
    private String paymentMethodDescription;
    private boolean active;

    /**
     * Gets payment method id.
     *
     * @return paymentMethodId.
     */

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    /**
     * Sets payment method id.
     *
     * @param paymentMethodId to set.
     */
    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    /**
     * Gets payment method.
     *
     * @return paymentMethod.
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets payment method.
     *
     * @param payment method to set.
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets payment Method Description.
     *
     * @return paymentMethodDescription.
     */
    public String getPaymentMethodDescription() {
        return paymentMethodDescription;
    }


    /**
     * Sets payment method description.
     *
     * @param paymentMethodDescription to set.
     */
    public void setPaymentMethodDescription(String paymentMethodDescription) {
        this.paymentMethodDescription = paymentMethodDescription;
    }


    /**
     * Gets active indicator.
     *
     * @return active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets active indicator.
     *
     * @param active.
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * @see org.kuali.rice.krad.bo.BusinessObjectBase#toStringMapper()
     */

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap m = new LinkedHashMap();
        m.put("paymentMethodId", this.paymentMethodId);
        return m;
    }
}
