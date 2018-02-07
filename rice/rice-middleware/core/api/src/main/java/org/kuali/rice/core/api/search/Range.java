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
package org.kuali.rice.core.api.search;

import org.apache.commons.lang.StringUtils;

/**
 * Represents a search range
 */
public class Range {
    private String lowerBoundValue;
    private String upperBoundValue;
    private boolean lowerBoundInclusive = true;
    private boolean upperBoundInclusive = true;

    public String getLowerBoundValue() {
        return lowerBoundValue;
    }

    public void setLowerBoundValue(String lowerBoundValue) {
        this.lowerBoundValue = lowerBoundValue;
    }

    public String getUpperBoundValue() {
        return upperBoundValue;
    }

    public void setUpperBoundValue(String upperBoundValue) {
        this.upperBoundValue = upperBoundValue;
    }

    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public String toString() {
        if (StringUtils.isNotEmpty(lowerBoundValue) && StringUtils.isNotEmpty(upperBoundValue)) {
            SearchOperator op;
            if (lowerBoundInclusive && upperBoundInclusive) {
                op = SearchOperator.BETWEEN;
            } else if (lowerBoundInclusive && !upperBoundInclusive) {
                op = SearchOperator.BETWEEN_EXCLUSIVE_UPPER2;
            } else if (!lowerBoundInclusive && upperBoundInclusive) {
                op = SearchOperator.BETWEEN_EXCLUSIVE_LOWER;
            } else {
                op = SearchOperator.BETWEEN_EXCLUSIVE;
            }
            return lowerBoundValue + op.op() + upperBoundValue;
        } else if (StringUtils.isNotEmpty(lowerBoundValue) && StringUtils.isEmpty(upperBoundValue)) {
            SearchOperator op = lowerBoundInclusive ? SearchOperator.GREATER_THAN_EQUAL : SearchOperator.GREATER_THAN;
            return op.op() + lowerBoundValue;
        } else if (StringUtils.isNotEmpty(upperBoundValue) && StringUtils.isEmpty(lowerBoundValue)) {
            SearchOperator op = upperBoundInclusive ? SearchOperator.LESS_THAN_EQUAL : SearchOperator.LESS_THAN;
            return op.op() + upperBoundValue;
        } else { // both are empty
            return "";
        }
    }
}
