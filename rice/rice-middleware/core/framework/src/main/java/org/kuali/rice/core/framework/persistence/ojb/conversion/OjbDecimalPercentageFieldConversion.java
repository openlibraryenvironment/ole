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
package org.kuali.rice.core.framework.persistence.ojb.conversion;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.math.BigDecimal;

/**
 * 
 * 
 */
public class OjbDecimalPercentageFieldConversion extends OjbKualiDecimalFieldConversion implements FieldConversion {

    private static BigDecimal oneHundred = new BigDecimal(100.0000);

    /**
     * Convert percentages to decimals for proper storage.
     * 
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {

        // Convert to BigDecimal using existing conversion.
        source = super.javaToSql(source);

        // Check for null, and verify object type.
        // Do conversion if our type is correct (BigDecimal).
        if (source != null && source instanceof BigDecimal) {
            BigDecimal converted = (BigDecimal) source;
            return converted.divide(oneHundred, 4, KualiDecimal.ROUND_BEHAVIOR);
        }
        else {
            return null;
        }
    }

    /**
     * Convert database decimals to 'visual' percentages for use in our business objects.
     * 
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {

        // Check for null, and verify object type.
        // Do conversion if our type is correct (BigDecimal).
        if (source != null && source instanceof BigDecimal) {
            BigDecimal converted = (BigDecimal) source;

            // Once we have converted, we need to do the super conversion to KualiDecimal.
            return super.sqlToJava(converted.multiply(oneHundred));
        }
        else {
            return null;
        }
    }
}
