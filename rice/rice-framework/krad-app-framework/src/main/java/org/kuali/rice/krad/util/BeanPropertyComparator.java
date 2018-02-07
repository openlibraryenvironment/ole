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
package org.kuali.rice.krad.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.kuali.rice.core.api.exception.KualiException;
import org.kuali.rice.core.api.util.type.TypeUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * BeanPropertyComparator compares the two beans using multiple property names
 * 
 * 
 */
public class BeanPropertyComparator implements Comparator, Serializable {
    private static final long serialVersionUID = -2675700473766186018L;
    boolean ignoreCase;
    private List propertyNames;
    private Comparator stringComparator;
    private Comparator booleanComparator;
    private Comparator genericComparator;

    /**
     * Constructs a PropertyComparator for comparing beans using the properties named in the given List
     *
     * <p>if the List is null, the beans will be compared directly
     * by Properties will be compared in the order in which they are listed. Case will be ignored
     * in String comparisons.</p>
     * 
     * @param propertyNames List of property names (as Strings) used to compare beans
     */
    public BeanPropertyComparator(List propertyNames) {
        this(propertyNames, true);
    }

    /**
     * Constructs a PropertyComparator for comparing beans using the properties named in the given List.
     *
     * <p>Properties will be compared
     * in the order in which they are listed. Case will be ignored if ignoreCase is true.</p>
     * 
     * @param propertyNames List of property names (as Strings) used to compare beans
     * @param ignoreCase if true, case will be ignored during String comparisons
     */
    public BeanPropertyComparator(List propertyNames, boolean ignoreCase) {
        if (propertyNames == null) {
            throw new IllegalArgumentException("invalid (null) propertyNames list");
        }
        if (propertyNames.size() == 0) {
            throw new IllegalArgumentException("invalid (empty) propertyNames list");
        }
        this.propertyNames = Collections.unmodifiableList(propertyNames);
        this.ignoreCase = ignoreCase;

        if (ignoreCase) {
            this.stringComparator = String.CASE_INSENSITIVE_ORDER;
        }
        else {
            this.stringComparator = ComparableComparator.getInstance();
        }
        this.booleanComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                int compared = 0;

                Boolean b1 = (Boolean) o1;
                Boolean b2 = (Boolean) o2;

                if (!b1.equals(b2)) {
                    if (b1.equals(Boolean.FALSE)) {
                        compared = -1;
                    }
                    else {
                        compared = 1;
                    }
                }

                return compared;
            }

        };
        this.genericComparator = ComparableComparator.getInstance();
    }


    /**
     * Compare two JavaBeans by the properties given to the constructor.
     * 
     * @param o1 Object The first bean to get data from to compare against
     * @param o2 Object The second bean to get data from to compare
     * @return int negative or positive based on order
     */
    public int compare(Object o1, Object o2) {
        int compared = 0;

        try {
            for (Iterator i = propertyNames.iterator(); (compared == 0) && i.hasNext();) {
                String currentProperty = i.next().toString();

                // choose appropriate comparator
                Comparator currentComparator = null;
                try {
                    PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(o1, currentProperty);
                    Class propertyClass = propertyDescriptor.getPropertyType();
                    if (propertyClass.equals(String.class)) {
                        currentComparator = this.stringComparator;
                    }
                    else if (TypeUtils.isBooleanClass(propertyClass)) {
                        currentComparator = this.booleanComparator;
                    }
                    else {
                        currentComparator = this.genericComparator;
                    }
                }
                catch (NullPointerException e) {
                    throw new BeanComparisonException("unable to find property '" + o1.getClass().getName() + "." + currentProperty + "'", e);
                }

                // compare the values
                Object value1 = PropertyUtils.getProperty(o1, currentProperty);
                Object value2 = PropertyUtils.getProperty(o2, currentProperty);
                /* Fix for KULRICE-5170 : BeanPropertyComparator throws exception when a null value is found in sortable non-string data type column */
                if ( value1 == null && value2 == null)
                    return 0;
                else if ( value1 == null)
                    return -1;
                else if ( value2 == null )
                    return 1;
                /* End KULRICE-5170 Fix*/
                compared = currentComparator.compare(value1, value2);
            }
        }
        catch (IllegalAccessException e) {
            throw new BeanComparisonException("unable to compare property values", e);
        }
        catch (NoSuchMethodException e) {
            throw new BeanComparisonException("unable to compare property values", e);
        }
        catch (InvocationTargetException e) {
            throw new BeanComparisonException("unable to compare property values", e);
        }

        return compared;
    }
    
    public static class BeanComparisonException extends KualiException {
        private static final long serialVersionUID = 2622379680100640029L;

        /**
         * @param message
         * @param t
         */
        public BeanComparisonException(String message, Throwable t) {
            super(message, t);
        }
    }
}
