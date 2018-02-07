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

public abstract class OjbCharBooleanConversionBase implements FieldConversion {

    protected abstract String getTrueValue();

    protected abstract String getFalseValue();


    /**
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        Object result = source;
        if (source instanceof Boolean) {
            if (source != null) {
                Boolean b = (Boolean) source;
                result = b.booleanValue() ? getTrueValue() : getFalseValue();
            }
        }
        return result;
    }

    /**
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        Object result = source;
        if (source instanceof String) {
            if (source != null) {
                String s = (String) source;
                result = getTrueValue().equals(s) ? Boolean.TRUE : getFalseValue().equals(s) ? Boolean.FALSE : null;
                if (result == null) {
                    throw new RuntimeException("Expected '" + getTrueValue() + "' or '" + getFalseValue() + "' but saw '" + source + "'");
                }
            }
        }
        return result;
    }

}
