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
 * This class originates from a similar class in the Kuali Financial System and has been adapted from 
 * that original state which was originally authored by the Kuali Nervous System team.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class OjbCharBooleanConversion2 implements FieldConversion {
    /**
     * This handles checking the boolean value coming in and converts it to 
     * the appropriate Y or N value.
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof Boolean) {
            if (source != null) {
                Boolean b = (Boolean) source;
                return b.booleanValue() ? "Y" : "N";
            }
            else {
                return null;
            }
        }
        else if (source instanceof String) {
            if ("true".equals(source)) {
                return "Y";
            }
            else if ("false".equals(source)) {
                return "N";
            }
        }
        return source;
    }

    /**
     * This handles checking the sql coming back from the database and converting 
     * it to the appropriate boolean true or false value.
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        try {
            if (source instanceof String) {
                if (source != null) {
                    String s = (String) source;
                    return Boolean.valueOf("YT1".contains(s));
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
