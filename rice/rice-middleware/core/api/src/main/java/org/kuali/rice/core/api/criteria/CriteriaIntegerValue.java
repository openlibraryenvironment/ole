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
import java.math.BigInteger;

/**
 * A CriteriaValue which stores date and time information in the form of a
 * {@link BigInteger} value.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = CriteriaIntegerValue.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CriteriaIntegerValue.Constants.TYPE_NAME)
public final class CriteriaIntegerValue implements CriteriaValue<BigInteger> {

    @XmlValue
    private BigInteger value;
    
    CriteriaIntegerValue() {
        this.value = null;
    }
    
    CriteriaIntegerValue(BigInteger value) {
    	validateValue(value);
        this.value = safeInstance(value);
    }
    
    CriteriaIntegerValue(Number value) {
    	validateValue(value);
    	this.value = BigInteger.valueOf(value.longValue());
    }
    
    private static void validateValue(Object value) {
    	if (value == null) {
    		throw new IllegalArgumentException("Value cannot be null.");
    	}
    }

    /**
     * Since BigInteger is not technically immutable we defensively copy when needed.
     *
     * see Effective Java 2nd ed. page 79 for details.
     *
     * @param val the big integer to check
     * @return the safe BigInteger
     */
    private static BigInteger safeInstance(BigInteger val) {
        if (val.getClass() != BigInteger.class) {
            return new BigInteger(val.toByteArray());
        }
        return val;
    }
    
    @Override
    public BigInteger getValue() {
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
        final static String ROOT_ELEMENT_NAME = "integerValue";
        final static String TYPE_NAME = "CriteriaIntegerValueType";
    }
    
}
