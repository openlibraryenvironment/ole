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
package org.kuali.rice.core.web.format;


/**
 * Currently, in the {@link Formatter Formatter} class, array are formatted
 * by returning the first item in the array.  This is not acceptable in some cases.
 * 
 * <p>
 * One example is a struts multi-select dropdown.  In order for struts to correctly handle
 * field highlighting struts expects an array.
 * </p>
 * 
 * <p>
 * This formatter does not handle {@code null} arrays or arrays with {@code null} items.
 * This class may need to be modified to handle these conditions.
 * </p>
 */
public class ArrayFormatter extends Formatter {
    
    private static final long serialVersionUID = -2207993820610088716L;

    /*
    public Object convertFromPresentationFormat(Object value) {
        if (isEmptyValue(value))
            return getNullObjectValue();

        Class type = value.getClass();
        boolean isArray = true;
        boolean isCollection = propertyType != null && Collection.class.isAssignableFrom(propertyType);

        String[] strings = isArray ? (String[]) value : new String[] { (String) value };

        return isArray ? convertToArray(strings) : convertToCollection(strings);
    }
    */
    /**
     * Returns the value passed in.
     * 
     * {@inheritDoc}
     */
    @Override
    public Object formatObject(Object value) {
        return value;
    }
}
