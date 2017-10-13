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
package org.kuali.rice.krad.demo.travel.authorization.dataobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.LinkedHashMap;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.jpa.type.HibernateKualiDecimalFieldType;

@Entity
@Table(name="TRVL_ADV_T")
public class TravelAdvance extends PersistableBusinessObjectBase {
    private Integer id;
    private String documentNumber;
    private KualiDecimal travelAdvanceRequested;
    private Date dueDate;
    private String paymentMethod;
    private String advancePaymentReasonCode;
    private Boolean travelAdvancePolicy = Boolean.FALSE;
    private String additionalJustification;


    /**
     * Returns the travel document identifier.
     *
     * <p>
     * Gets the travel document identifier.
     * </p>
     *
     * @return Integer - generated id of this Travel Advance
     */
    public Integer getId() {
        return id;
    }

    /**
     * Initializes the document identifier.
     *
     * <p>
     * Sets the document identifier.
     * </p>
     *
     * @param id - generated id of this Travel Advance
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the documentNumber attribute.
     *
     * <p>
     * Gets the documentNumber attribute.
     * </p>
     *
     * @return Integer - generated id of this Travel Advance
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Initializes the documentNumber attribute.
     *
     * <p>
     * Sets the the documentNumber attribute.
     * </p>
     *
     * @param documentNumber - document number attribute
     */
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * Returns travel advance requested amount.
     *
     * <p>
     * Gets the travel advance requested amount.
     * </p>
     *
     * @return KualiDecimal - travel advance requested amount
     */
    public KualiDecimal getTravelAdvanceRequested() {
        return travelAdvanceRequested;
    }

    /**
     * Initializes the amount requested attribute.
     *
     * <p>
     * Sets the the amount requested attribute.
     * </p>
     *
     * @param travelAdvanceRequested - amount requested
     */
    public void setTravelAdvanceRequested(KualiDecimal travelAdvanceRequested) {
        this.travelAdvanceRequested = travelAdvanceRequested;
    }

    /**
     * Returns travel advance due date.
     *
     * <p>
     * Gets the travel advance due date.
     * </p>
     *
     * @return Date - travel advance due date
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Initializes the due date.
     *
     * <p>
     * Sets the the payment advance due date.
     * </p>
     *
     * @param dueDate - due date
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Returns travel advance payment method.
     *
     * <p>
     * Gets the travel advance payment method.
     * </p>
     *
     * @return String - travel advance payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Initializes the payment method.
     *
     * <p>
     * Sets the the payment advance payment method.
     * </p>
     *
     * @param paymentMethod - payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Returns travel advance payment reason code.
     *
     * <p>
     * Gets the travel advance payment reason code.
     * </p>
     *
     * @return String - travel advance payment reason code
     */
    public String getAdvancePaymentReasonCode() {
        return advancePaymentReasonCode;
    }

    /**
     * Initializes the payment reason code.
     *
     * <p>
     * Sets the payment reason code.
     * </p>
     *
     * @param advancePaymentReasonCode - payment reason code
     */
    public void setAdvancePaymentReasonCode(String advancePaymentReasonCode) {
        this.advancePaymentReasonCode = advancePaymentReasonCode;
    }

    /**
     * Returns travel policy acceptance flag.
     *
     * <p>
     * Gets the travel policy acceptance flag.
     * </p>
     *
     * @return Boolean - travel policy acceptance flag
     */
    public boolean getTravelAdvancePolicy() {
        return travelAdvancePolicy;
    }

    /**
     * Initializes the travel policy acceptance flag.
     *
     * <p>
     * Sets the travel policy acceptance flag.
     * </p>
     *
     * @param travelAdvancePolicy - travel policy acceptance flag
     */
    public void setTravelAdvancePolicy(boolean travelAdvancePolicy) {
        this.travelAdvancePolicy = travelAdvancePolicy;
    }

    /**
     * Returns the additional justification text.
     *
     * <p>
     * Gets the additional justification text.
     * </p>
     *
     * @return String - additional justification
     */
    public String getAdditionalJustification() {
        return additionalJustification;
    }

    /**
     * Initializes the additional justification text.
     *
     * <p>
     * Sets the additional justification text.
     * </p>
     *
     * @param additionalJustification - the additional justification
     */
    public void setAdditionalJustification(String additionalJustification) {
        this.additionalJustification = additionalJustification;
    }

    protected LinkedHashMap toStringMapper() {
        LinkedHashMap map = new LinkedHashMap();
        map.put("id", this.id);
        map.put("documentNumber", this.documentNumber);
        map.put("travelAdvanceRequested", this.travelAdvanceRequested);
        map.put("dueDate", this.dueDate);
        map.put("paymentMethod", this.paymentMethod);
        map.put("advancePaymentReasonCode", this.advancePaymentReasonCode);
        map.put("travelAdvancePolicy", this.travelAdvancePolicy);
        map.put("additionalJustification", this.additionalJustification);
        return map;
    }

}
