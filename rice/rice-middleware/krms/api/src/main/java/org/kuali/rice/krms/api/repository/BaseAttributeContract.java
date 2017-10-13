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
package org.kuali.rice.krms.api.repository;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinitionContract;

/**
 * Base interface intended for extension by other AttributeContract interfaces 
 * <p>This contains the common fields shared by all KRMS attributes.</p>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface BaseAttributeContract extends Identifiable {

    /**
     * Returns the id of the attribute definition.
     *
     * <p>
     * The attribute definition contains metadata about the attribute. This
     * is a required field and may not be null or blank. Many
     * attribute instances may share the same attribute definition.
     * </p>
     * @return the attribute definition id
     */
	public String getAttributeDefinitionId();

	/**
	 * Returns the value of the attribute
	 * 
	 * @return the value of the attribute
	 */
	public String getValue();

	/**
	 * Returns a {@link KrmsAttributeDefinitionContract.}
     * <p>This is the object referred to by the attributeDefinitionId.
     * The full object is also kept here for performance purposes. This is because the name of the attribute
     * is referenced often for resolving name / value attribute pairs.</p>
     *
     * @return the attribute definition
	 */
	public KrmsAttributeDefinitionContract getAttributeDefinition();

}
