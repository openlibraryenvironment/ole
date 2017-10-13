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

import java.util.List;

/**
 * Defines the contract for a KRMS type definition.
 * <p>A KrmsTypeDefinition is a metadata wrapper around a KRMS type service. This contains a
 * collection of related attributes. This also provides the name of the service used to resolve these attributes.
 * </p>
 */
public interface KrmsTypeDefinitionContract extends Identifiable, Inactivatable, Versioned {

	/**
	 * Returns the name of the KrmsTypeDefinition.  All KrmsTypeDefinitions have a name, so this
     * value can not be null or blank. The combination of name and namespace must
     * be unique within the entire KRMS system.
	 *
	 * @return the name of this KrmsTypeDefinition
	 */
	public String getName();

	/**
	 * Returns the namespace to which the KrmsTypeDefinition belongs. All type definitions
     * exist within a namespace. This value can not be null or blank. The combination
     * of name and namespace must be unique within the entire KRMS system.
	 *
	 * @return the namespace of this KrmsTypeDefinition
	 */
	public String getNamespace();

	/**
	 * Returns the name of the service used to resolve attribute values. The service name
     * may be null or blank.
	 * 
	 * @return the service name of this KrmsTypeDefinition
	 */
	public String getServiceName();

	/**
	 * Returns a list of attributes associated with the KRMS type definition.
     * This can be empty, but will never be null. If no attribute definitions are associated with the KRMS type
     * then this will return an empty collection.
	 * 
	 * @return the list of KrmsTypeAttributeContract attribute definition contracts
	 */
	public List<? extends KrmsTypeAttributeContract> getAttributes();
}
