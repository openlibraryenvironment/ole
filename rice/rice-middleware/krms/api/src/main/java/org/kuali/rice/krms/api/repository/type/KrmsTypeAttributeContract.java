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
 * This is the contract for a KrmsTypeAttribute.
 * <p>A KrmsTypeAttribute associates a {@link KrmsTypeDefinition} with an individual attribute
 * {@link KrmsAttributeDefinition} associated with a the KrmsTypeDefinition.</p>
 * @see KrmsTypeAttributeContract
 */
public interface KrmsTypeAttributeContract extends Identifiable, Inactivatable, Versioned {

	/**
	 * Returns the id of the KrmsTypeDefinition to which the attribute applies
	 *
	 * <p>
	 * A KRMS type definition has zero or more attributes associated with it.
     * The id field indicates which type definition this attribute is associated
     * with. It is the id of a KrmsTypeDefinition related to the attribute. This required
     * field may not be null or blank.
	 * </p>
	 * @return id for KrmsTypeDefinition related to the attribute.
	 */
	public String getTypeId();

	/**
	 * Returns the id of the KrmsAttributeDefinition of the attribute.
	 *
	 * <p>
	 * The attribute definition contains metadata about the attribute. This
     * is a required field and may not be null or blank. Many
     * attributes may share the same attribute definition.
	 * </p>
	 * @return the attribute definition id
	 */
	public String getAttributeDefinitionId();

	/**
	 * Returns the sequence number of the attribute within the KrmsTypeDefinition collection.
     * <p>The list of attributes is an ordered list. This value represents the position in the list
     * and cannot be null.</p>
	 * 
	 * @return the sequence number of the attribute
	 */
	public Integer getSequenceNumber();

}
