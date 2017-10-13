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
package org.kuali.rice.krms.framework.engine.expression;

import org.kuali.rice.krms.api.KrmsApiServiceLocator;
import org.kuali.rice.krms.api.engine.expression.ComparisonOperatorService;

/**
 * Contains utility methods for working with the ComparisonOperatorService
 */
public final class ComparisonOperatorServiceUtils {

    // don't allow instances
    private ComparisonOperatorServiceUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * <P>checks if the value needs to be coerced from a String to another type, and if so it looks for an
     * applicable StringCoercionExtension.</p>
     *
     * @param value the value of the argument that may or may not need coercion
     * @param expectedType the name of the type that the function is expecting for the argument
     * @param comparisonOperatorService the ComparisonOperatorService instance to use (if needed) for finding a StringCoercionExtension
     * @return the coerced value, or the unchanged value if (1) no coercion needs to be done or (2) an applicable
     * StringCoercionExtension can't be found.
     */
    public static Object coerceIfNeeded(Object value, String expectedType, ComparisonOperatorService comparisonOperatorService) {

        Object result = value;

        if (value instanceof String && !"java.lang.String".equals(expectedType)) {
            String valueString = value.toString();
            StringCoercionExtension coercionExtension =
                    comparisonOperatorService.findStringCoercionExtension(expectedType, valueString);
            if (coercionExtension != null) {
                result = coercionExtension.coerce(expectedType, valueString);
            }
        }

        return result;
    }
}
