/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.krms.test;

import org.kuali.rice.krms.framework.engine.expression.EngineComparatorExtension;

/**
 *
 * Used for testing the ComparisonOperatorService
 * 
 * @link ComparisonOperatorService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CustomComparisonOperator implements EngineComparatorExtension {
    @Override
    public int compare(Object lhs, Object rhs) {
        if (lhs.equals(rhs)) {
            return 0;
        }
        return lhs.toString().compareTo(rhs.toString());
    }

    @Override
    public boolean canCompare(Object lhs, Object rhs) {
        return (lhs instanceof CustomComparisonOperator && rhs instanceof CustomComparisonOperator);
    }
}
