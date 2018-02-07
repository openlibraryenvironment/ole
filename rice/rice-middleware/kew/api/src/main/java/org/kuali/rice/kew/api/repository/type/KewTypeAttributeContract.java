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
package org.kuali.rice.kew.api.repository.type;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;


public interface KewTypeAttributeContract extends Identifiable, Inactivatable {

	/**
	 * This is the KewType to which the attribute applies 
	 *
	 * <p>
	 * It is a id of a KEW type related to the attribute.
	 * </p>
	 * @return id for KEW type related to the attribute.
	 */
	public String getTypeId();

	/**
	 * This is the id of the definition of the attribute. 
	 *
	 * <p>
	 * It identifies the attribute definition
	 * </p>
	 * @return the attribute definition id.
	 */
	public String getAttributeDefinitionId();

	/**
	 * This is the sequence number of the attribute
	 * 
	 * @return the service name of the KewTypeAttribute
	 */
	public Integer getSequenceNumber();

	/**
	 * This is the definition of the attribute
	 */
	public KewAttributeDefinitionContract getAttributeDefinition();
}
