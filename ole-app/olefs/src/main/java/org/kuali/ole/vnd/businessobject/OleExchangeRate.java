/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.vnd.businessobject;

import java.math.BigDecimal;
import java.util.Date;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * CommodityCode Business Object
 */
public class OleExchangeRate extends PersistableBusinessObjectBase implements MutableInactivatable  {

    private Long exchangeRateCode;
    private BigDecimal exchangeRate;
    private Long currencyTypeId;
    private String exchangeRateSource;
    private Date exchangeRateDate;
    private boolean active;

    private OleCurrencyType currencyType;



    public Long getExchangeRateCode() {
        return exchangeRateCode;
    }

    public void setExchangeRateCode(Long exchangeRateCode) {
        this.exchangeRateCode = exchangeRateCode;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getCurrencyTypeId() {
        return currencyTypeId;
    }

    public void setCurrencyTypeId(Long currencyTypeId) {
        this.currencyTypeId = currencyTypeId;
    }

    public String getExchangeRateSource() {
        return exchangeRateSource;
    }

    public void setExchangeRateSource(String exchangeRateSource) {
        this.exchangeRateSource = exchangeRateSource;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }

    public OleCurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(OleCurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

}
