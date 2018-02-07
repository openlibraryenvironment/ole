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

/**
 * This class converts the "Y" or "N" value from the database into a true or false in Java.
 * 
 * 
 * @deprecated Use OjbCharBooleanConversion2 instead
 */
public final class OjbCharBooleanConversion4 extends OjbCharBooleanConversionBase {
    private static final long serialVersionUID = 5192588414458129183L;
    private static String S_TRUE = "Y";
    private static String S_FALSE = "N";

    /**
     * no args constructor
     */
    public OjbCharBooleanConversion4() {
        super();
    }

    protected String getTrueValue() {
        return "Y";
    }

    protected String getFalseValue() {
        return "N";
    }
}
