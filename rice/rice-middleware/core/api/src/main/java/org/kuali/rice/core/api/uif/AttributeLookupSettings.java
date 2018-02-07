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
package org.kuali.rice.core.api.uif;

/**
 * Defines configuration for an attribute which may be used as part of a lookups.  This includes indicating if the
 * field is displayed in the lookup criteria or lookup result set, as well as defining information about this field
 * if it used as part of a range-based lookup.
 *
 * <p>The range bounds allows the party executing the lookup against this attribute to enter a value for both ends
 * (lower and upper bounds) in order to determine if the attribute is "between" those two values.</p>
 *
 * <p>Note that an attribute range only makes sense if the {@code DataType} of the attribute is
 * a date or numerical data type.  The consumer of these settings is free to ignore the given
 * attribute range information if it does not believe it is possible to successfully present a range-based
 * lookup option based on the data type (or other settings) of the {@code AttributeField}.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface AttributeLookupSettings {

    /**
     * Returns true if this field should be included as part of the lookup criteria, false if not.
     *
     * @return true if this field should be included as part of the lookup criteria, false if not
     */
    boolean isInCriteria();

    /**
     * Returns true if this field should be included in the result set of the lookup, false if not.
     *
     * @return true if this field should be included in the result set of the lookup, false if not
     */
    boolean isInResults();

    /**
     * Returns true if lookups against this field should be handled as a lookup operation supporting a range-based
     * search of data against the field.
     *
     * @return true if lookups against this attribute should allow for ranged lookup fields, false otherwise
     */
    boolean isRanged();

    /**
     * Returns true if the lower bound should be treated as inclusive when executing a ranged
     * lookup against the attribute, false if it should be treated as exclusive.
     *
     * @return true if the lower bound is inclusive, false if it is exclusive
     */
    boolean isLowerBoundInclusive();

    /**
     * Returns true if the upper bound should be treated as inclusive when executing a ranged
     * lookup against the attribute, false if it should be treated as exclusive.
     *
     * @return true if the upper bound is inclusive, false if it is exclusive
     */
    boolean isUpperBoundInclusive();

/**
     * Indicates if lookups which use this attribute should execute the lookup against this attribute
     * in a case sensitive fashion.  If this method returns null, it means that the system-level
     * default for case sensitivity of attributes on lookups should be used.
     *
     * @return true if the attribute should be case sensitive on lookups, false if it should not, and
     * null if the system-level default should be used
     */
    Boolean isCaseSensitive();

    /**
     * @return an explicit label for the lower bound field
     */
    String getLowerLabel();

    /**
     * @return an explicit label for the upper bound field
     */
    String getUpperLabel();

    /**
     * @return whether to display a datepicker for the lower bound
     */
    Boolean isLowerDatePicker();

    /**
     * @return whether to display a datepicker for the upper bound
     */
    Boolean isUpperDatePicker();
}