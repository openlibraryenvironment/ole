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

import org.junit.Test;
import org.kuali.rice.krms.framework.engine.expression.ComparisonOperator;
import org.kuali.rice.test.BaselineTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class ComparisonOperatorIntegrationTest extends AbstractBoTest {

    @Test
    public void testRegularComparisonOperationEquals() {
        ComparisonOperator equalsOp = ComparisonOperator.fromCode(ComparisonOperator.EQUALS.toString());
        assertTrue(equalsOp.compare("StringOne", "StringOne"));
    }

    @Test
    public void testCustomComparisonOperator() {
        CustomComparisonOperator one = new CustomComparisonOperator();
        CustomComparisonOperator two = new CustomComparisonOperator();
        ComparisonOperator equalsOp = ComparisonOperator.fromCode(ComparisonOperator.EQUALS.toString());
        assertTrue(equalsOp.compare(one, one));
        assertFalse(equalsOp.compare(one, two));
    }
}
