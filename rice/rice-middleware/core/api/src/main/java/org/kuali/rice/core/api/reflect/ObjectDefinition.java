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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A marker interface for object definitions.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ObjectDefinition implements Serializable {

	private static final long serialVersionUID = 835423601196288352L;

	private String className;
	private String applicationId;
	private boolean atRemotingLayer;
	private final List<DataDefinition> constructorParameters = new ArrayList<DataDefinition>();
	private final Map<String, PropertyDefinition> properties = new HashMap<String, PropertyDefinition>();

	public ObjectDefinition(Class<?> objectClass) {
		this(objectClass.getName());
	}
	
	public ObjectDefinition(String className, String applicationId) {
		this.className = className;
		this.applicationId = applicationId;
	}
	
	public ObjectDefinition(String className) {
		if (className == null) {
			throw new IllegalArgumentException("Extension class name cannot be null");
		}
		this.className = className;
	}

	public String getClassName() {
		return this.className;
	}

	public void addConstructorParameter(DataDefinition parameter) {
	    this.constructorParameters.add(parameter);
	}

	public void removeConstructorParameter(DataDefinition parameter) {
	    this.constructorParameters.remove(parameter);
	}

	public void setConstructorParameters(List<DataDefinition> parameters) {
	    this.constructorParameters.clear();
	    this.constructorParameters.addAll(parameters);
	}

	public List<DataDefinition> getConstructorParameters() {
		return this.constructorParameters;
	}

	public void addProperty(PropertyDefinition property) {
		if (property == null) {
			return;
		}
		if (property.getName() == null) {
			throw new IllegalArgumentException("PropertyDefinition cannot have a null name.");
		}
		this.properties.put(property.getName(), property);
	}

	public PropertyDefinition getProperty(String name) {
		return (PropertyDefinition) this.properties.get(name);
	}

	public Collection<PropertyDefinition> getProperties() {
		return this.properties.values();
	}

	public void setProperties(Collection<PropertyDefinition> properties) {
		this.properties.clear();
		if (properties == null) {
			return;
		}
		for (PropertyDefinition prop: properties) {
			addProperty(prop);
		}
	}

    public String toString() {
        return "[ObjectDefinition: className: " + getClassName()
               + ", applicationId: " + getApplicationId()
               + "]";
    }

	public boolean isAtRemotingLayer() {
		return this.atRemotingLayer;
	}

	public void setAtRemotingLayer(boolean atRemotingLayer) {
		this.atRemotingLayer = atRemotingLayer;
	}

	public String getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
}
