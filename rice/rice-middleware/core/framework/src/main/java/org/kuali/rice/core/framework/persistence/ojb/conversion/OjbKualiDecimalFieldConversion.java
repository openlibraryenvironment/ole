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
 * This class converts NUMERIC fields from the database into KualiDecimal instances, and vice-versa.
 * 
 * 
 */

public class OjbKualiDecimalFieldConversion implements FieldConversion {
    private static final long serialVersionUID = 2450111778124335242L;

    /**
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        Object converted = source;

        if (source instanceof KualiDecimal) {
            converted = ((KualiDecimal) source).bigDecimalValue();
        }

        return converted;
    }

    /**
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        Object converted = source;

        if (source instanceof BigDecimal) {
            converted = new KualiDecimal((BigDecimal) source);
        }

        return converted;
    }
}
