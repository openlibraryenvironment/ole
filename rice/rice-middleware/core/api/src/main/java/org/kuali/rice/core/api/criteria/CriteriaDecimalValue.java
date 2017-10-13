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
package org.kuali.rice.core.api.criteria;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import java.math.BigDecimal;

/**
 * A CriteriaValue which stores date and time information in the form of a
 * {@link BigDecimal} value.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = CriteriaDecimalValue.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CriteriaDecimalValue.Constants.TYPE_NAME)
public final class CriteriaDecimalValue implements CriteriaValue<BigDecimal> {

    @XmlValue
    private final BigDecimal value;
    
    CriteriaDecimalValue() {
        this.value = null;
    }
    
    CriteriaDecimalValue(BigDecimal value) {
    	validateValue(value);
        this.value = safeInstance(value);
    }
    
    CriteriaDecimalValue(Number value) {
    	validateValue(value);
    	this.value = BigDecimal.valueOf(value.doubleValue());
    }
    
    private static void validateValue(Object value) {
    	if (value == null) {
    		throw new IllegalArgumentException("Value cannot be null.");
    	}
    }

    /**
     * Since BigDecimal is not technically immutable we defensively copy when needed.
     *
     * see Effective Java 2nd ed. page 79 for details.
     *
     * @param val the big decimal to check
     * @return the safe BigDecimal
     */
    private static BigDecimal safeInstance(BigDecimal val) {
        if (val.getClass() != BigDecimal.class) {
            return new BigDecimal(val.toPlainString());
        }
        return val;
    }
    
    @Override
    public BigDecimal getValue() {
        return value;
    }
    
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "decimalValue";
        final static String TYPE_NAME = "CriteriaDecimalValueType";
    }
    
}
