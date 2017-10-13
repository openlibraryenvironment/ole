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
package org.kuali.rice.core.api.reflect;

/**
 * Constructor param on an {@link ObjectDefinition}.  Represents an object and it's type.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDefinition implements java.io.Serializable {

	private static final long serialVersionUID = 2171793503482942282L;
	private final Object value;
    private final Class type;
    
    public DataDefinition(Object value) {
    	if (value == null) {
    		throw new IllegalArgumentException("Can't determine data type from null value.");
    	}
    	this.value = value;
    	this.type = value.getClass();
    }
    
    public DataDefinition(Object value, Class type) {
        this.value = value;
        this.type = type;
    }
        
    public Object getValue() {
        return this.value;
    }
    
    public Class getType() {
        return this.type;
    }
    
}
