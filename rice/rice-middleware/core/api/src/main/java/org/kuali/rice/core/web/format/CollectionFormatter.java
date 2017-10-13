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

import java.util.Collection;
import java.util.Iterator;

/**
 * Formats a collection into a string that looks like an array.  To print out each element,
 * the toString method of each element is called. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CollectionFormatter extends Formatter {

    private static final long serialVersionUID = 1074862354812893254L;

	/**
     * Formats a collection into a string that looks like an array.
     * 
     * @see org.kuali.rice.core.web.format.Formatter#format(java.lang.Object)
     */
    @Override
    public Object format(Object value) {
	StringBuilder buf = new StringBuilder();
	buf.append("[");
	
	if (!(value instanceof Collection)) {
		throw new IllegalArgumentException("value was not an instance of Collection, instead was: " + (value == null ? null : value.getClass()));
	}
	
	@SuppressWarnings("unchecked")
	Collection<Object> collection = (Collection<Object>)value;
	Iterator<Object> i = collection.iterator();
	boolean hasNext = i.hasNext();
	while (hasNext) {
	    Object elem = i.next();
	    buf.append(elem);
	    
	    hasNext = i.hasNext();
	    if (hasNext) {
		buf.append(", ");
	    }
	}
	buf.append("]");
	return buf.toString();
    }

}
