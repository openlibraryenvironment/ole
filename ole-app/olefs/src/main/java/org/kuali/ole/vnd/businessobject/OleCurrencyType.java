/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.vnd.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

public class OleCurrencyType extends PersistableBusinessObjectBase implements MutableInactivatable {


    private Long currencyTypeId;

    private String currencyType;

    private String currencyCountry;

    private String currencyAlphaCode;

    private Integer currencyNumericCode;

    private String currencyMinorUnit;

    //private BigDecimal currencyRate;

    private boolean active;


    @Override
    public boolean isActive() {
        // TODO Auto-generated method stub
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active=active;

    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencyCountry() {
        return currencyCountry;
    }

    public void setCurrencyCountry(String currencyCountry) {
        this.currencyCountry = currencyCountry;
    }

    public String getCurrencyAlphaCode() {
        return currencyAlphaCode;
    }

    public void setCurrencyAlphaCode(String currencyAlphaCode) {
        this.currencyAlphaCode = currencyAlphaCode;
    }




    public String getCurrencyMinorUnit() {
        return currencyMinorUnit;
    }

    public void setCurrencyMinorUnit(String currencyMinorUnit) {
        this.currencyMinorUnit = currencyMinorUnit;
    }

    public Long getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(Long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public Integer getCurrencyNumericCode() {
        return currencyNumericCode;
    }

    public void setCurrencyNumericCode(Integer currencyNumericCode) {
        this.currencyNumericCode = currencyNumericCode;
    }

    /*public BigDecimal getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(BigDecimal currencyRate) {
        this.currencyRate = currencyRate;
    } */

}
