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
package org.kuali.rice.krad.demo.uif.form;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * For server paging test view purposes only
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ServerPagingTestObject implements Serializable {
    private static final long serialVersionUID = -7525378097732916411L;

    private BigDecimal doubleField;
    private Integer integerField = 0;
    private String   stringField;

    public ServerPagingTestObject() { }

    /**
     * @return the doubleField
     */
    public BigDecimal getDoubleField() {
        return doubleField;
    }

    /**
     * set the doubleField.
     *
     * @param doubleField the doubleField to set
     */
    public void setDoubleField(BigDecimal doubleField) {
        this.doubleField = doubleField;
    }

    /**
     * @return the integerField
     */
    public Integer getIntegerField() {
        return integerField;
    }

    /**
     * Set the integerField.
     *
     * @param integerField the integerField to set
     */
    public void setIntegerField(Integer integerField) {
        this.integerField = integerField;

    }

    /**
     * @return the tringField
     */
    public String getStringField() {
        return stringField;
    }

    /**
     * Set the stringField
     *
     * @param stringField the stringField to set
     */
    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

