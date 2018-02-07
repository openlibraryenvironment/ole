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

/**
 * Performs conversion of java boolean fields to and from the database
 */
public class OjbCharBooleanConversion implements FieldConversion {
	public static final String DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION = "Y";
	public static final String DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION = "N";

    /**
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Boolean) {
            if (source != null) {
                Boolean b = (Boolean) source;
                return b.booleanValue() ? DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION : DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION;
            }
            else {
                return null;
            }
        }
        else if (source instanceof String) {
            if ("true".equalsIgnoreCase((String)source) || "yes".equalsIgnoreCase((String)source) || "y".equalsIgnoreCase((String)source)) {
                return DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION;
            }
            else if ("false".equalsIgnoreCase((String)source) || "no".equalsIgnoreCase((String)source) || "n".equalsIgnoreCase((String)source)) {
                return DATABASE_BOOLEAN_FALSE_STRING_REPRESENTATION;
            }
        }
        return source;
    }

    /**
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        try {
            if (source instanceof String) {
                if (source != null) {
                    String s = (String) source;
                    String trueValues = DATABASE_BOOLEAN_TRUE_STRING_REPRESENTATION + "T1";
                    return trueValues.contains(s);
                }
                else {
                    return null;
                }
            }
            return source;
        }
        catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("I have exploded converting types", t);
        }
    }

}
