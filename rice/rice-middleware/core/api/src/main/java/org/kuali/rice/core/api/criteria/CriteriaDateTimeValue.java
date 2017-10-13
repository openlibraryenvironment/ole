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
import org.joda.time.DateTime;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Calendar;
import java.util.Date;

/**
 * A CriteriaValue which stores date and time information in the form of a {@link Calendar} value.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = CriteriaDateTimeValue.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = CriteriaDateTimeValue.Constants.TYPE_NAME)
public final class CriteriaDateTimeValue implements CriteriaValue<DateTime> {

    @XmlValue
    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    private final DateTime value;

    CriteriaDateTimeValue() {
        this.value = null;
    }

    CriteriaDateTimeValue(DateTime value) {
        validateValue(value);
        this.value = value;
    }
    
    CriteriaDateTimeValue(Calendar value) {
        validateValue(value);
        this.value = new DateTime(value.getTimeInMillis());
    }

    CriteriaDateTimeValue(Date value) {
        validateValue(value);
        this.value = new DateTime(value.getTime());
    }

    private static void validateValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null.");
        }
    }

    @Override
    public DateTime getValue() {
        //defensive copy outgoing value - keeps things immutable
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
        final static String ROOT_ELEMENT_NAME = "dateTimeValue";
        final static String TYPE_NAME = "CriteriaDateTimeValueType";
    }

}
