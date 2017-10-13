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
package org.kuali.rice.krms.api.repository.type;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Defines the contract for a KRMS Attribute Definition. An attribute definition contract
 * describes an attribute and how to find the attribute for krad purposes.
 * It has a name, namespace, label, description and associated ui component.
 */
public interface KrmsAttributeDefinitionContract extends Identifiable, Inactivatable, Versioned {

	/**
	 * Returns the name of this attribute definition.
	 *
	 * <p>
	 * All attribute definitions have a name and namespace, so this value can
     * not be null or blank. The combination of name and namespace must be unique within the
     * entire KRMS system.
	 * </p>
	 * @return name for this attribute definition
	 */
	public String getName();

	/**
	 * Returns the namespace of this attribute definition.
	 *
	 * <p>
	 * The namespace provides scope of the the attribute definition. All attribute definition
     * have a name and namespace, so this field may not be null or blank. The combination
     * of name and namespace must be unique within the entire KRMS system.
	 * </p>
	 * @return the namespace of this attribute definition
	 */
	public String getNamespace();

	/**
	 * Returns the label of this attribute definition.
     * <p>This is an optional
     * field and may be null or blank.</p>
	 * 
	 * @return the label to be used when displaying the attribute
	 */
	public String getLabel();

    /**
     * Returns the description of the attribute.
     * <p>This is an option field and may be null or blank.</p>
     * @return a text description of the attribute
     */
    public String getDescription();
	
	/**
	 * Returns the name of the component used to display the attribute.
	 * 
	 * @return the component name of the of the attribute
	 */
	public String getComponentName();
}
