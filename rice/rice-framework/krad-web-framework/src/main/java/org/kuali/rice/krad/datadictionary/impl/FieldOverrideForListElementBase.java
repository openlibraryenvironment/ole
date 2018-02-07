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
package org.kuali.rice.krad.datadictionary.impl;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * The super class which implementations of the FieldOverride interface will extend.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldOverrideForListElementBase {

    private String propertyName;
    private Object element;
    protected String propertyNameForElementCompare;

    public String getPropertyNameForElementCompare() {
        return propertyNameForElementCompare;
    }

    public void setPropertyNameForElementCompare(String propertyNameForElementCompare) {
        this.propertyNameForElementCompare = propertyNameForElementCompare;
    }

    protected int getElementPositionInList(Object object, List theList) {
        Comparator comparator = this.getComparator();
        int pos = -1;

        if (object != null && theList != null) {
            for (int i = 0; i < theList.size(); ++i) {
                Object item = theList.get(i);
                boolean equalFlag = false;
                if (comparator != null) {
                    equalFlag = comparator.compare(object, item) == 0;
                } else {
                    equalFlag = item.equals(object);
                }
                if (equalFlag) {
                    pos = i;
                    break;
                }
            }
        }
        return pos;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object value) {
        this.element = value;
    }

    public FieldOverrideForListElementBase() {
        super();
    }

    protected Comparator getComparator() {
        Comparator comparator = null;
        if (StringUtils.isNotBlank(propertyNameForElementCompare)) {
            comparator = new BeanComparator(propertyNameForElementCompare);
        } else {
            throw new RuntimeException("Missing required comparator definitions.");
        }
        return comparator;
    }

}
