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
package org.kuali.rice.krad.bo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BusinessObjectBase implements BusinessObject {

    /**
     * Default constructor. Required to do some of the voodoo involved in letting the DataDictionary validate attributeNames for a
     * given BusinessObject subclass.
     */
    public BusinessObjectBase() {
    }

    @Override
	public String toString() {
        class BusinessObjectToStringBuilder extends ReflectionToStringBuilder {
            private BusinessObjectToStringBuilder(Object object) {
                super(object);
            }

            public boolean accept(Field field) {
                if (BusinessObject.class.isAssignableFrom(field.getType())) {
                    return false;
                }
                return super.accept(field);
            }
        };
        ReflectionToStringBuilder toStringBuilder = new BusinessObjectToStringBuilder(this);
        return toStringBuilder.toString();
    }

}
