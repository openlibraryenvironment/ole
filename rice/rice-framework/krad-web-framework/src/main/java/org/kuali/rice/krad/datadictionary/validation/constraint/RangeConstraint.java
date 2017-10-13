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
package org.kuali.rice.krad.datadictionary.validation.constraint;

/**
 * A range constraint that restricts a value to a specified range
 *
 * <p>
 * This range can be numeric or it can be a date.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
public interface RangeConstraint extends DataTypeConstraint {

    /**
     * Determines the maximum value of the field
     *
     * <p>
     * The inclusiveMax element determines the maximum allowable value for data entry editing purposes.
     * Value can be an integer or decimal value such as -.001 or 99.
     * </p>
     *
     * @return String specifying the maximum value
     */
    String getInclusiveMax();

    /**
     * Determines the minimum value of the field
     *
     * <p>
     * The exclusiveMin element determines the minimum allowable value for data entry editing purposes.
     * Value can be an integer or decimal value such as -.001 or 99.
     * </p>
     *
     * @return String specifying the minimum value
     */
    String getExclusiveMin();

}
