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
package org.kuali.rice.krad.datadictionary.parse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation flag that the connected method is represented by the declared name in Spring Beans created using the
 * custom schema.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanTagAttribute {
    /*
     * The different types the property could be.
     * SingleValue - Property is a single standard value (attribute). (DEFAULT)
     * SingleBean - Property is a single bean object.
     * ListBean - Property is a list consisting of beans.
     * ListValue - Property is a list consisting of standard values (string, int, char, etc).
     * MapValue - The property is a map that consists of String keys and String values.
     * MapBean - The property is a map that consists of either String or bean keys and bean values.
     * SetValue - The property is a set consisting of standard values.
     * SetBean - The property is a set consisting of beans.
     */
    public enum AttributeType {
        SINGLEVALUE, SINGLEBEAN, LISTBEAN, LISTVALUE, MAPVALUE, MAPBEAN, MAP2BEAN, SETVALUE, SETBEAN
    }

    String name();

    // The type of the property defining how it should be parsed
    AttributeType type() default AttributeType.SINGLEVALUE;
}
