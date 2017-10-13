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
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.List;

public interface KewTypeDefinitionContract extends Identifiable, Inactivatable,
		Versioned {

	/**
	 * This is the name for the KEWType
	 * 
	 * <p>
	 * It is a name of a KEW type.
	 * </p>
	 * 
	 * @return name for KEW type.
	 */
	public String getName();

	/**
	 * This is the namespace code.
	 * 
	 * <p>
	 * It provides scope of the KEW type.
	 * </p>
	 * 
	 * @return the namespace code of the KEW type.
	 */
	public String getNamespace();

	/**
	 * This is the name of the KEW KewType service
	 * 
	 * @return the service name of the KEW type
	 */
	public String getServiceName();

	/**
	 * This method returns a list of attributes associated with the KewType
	 * 
	 * @return a list of KewTypeAttribute objects.
	 */
	public List<? extends KewTypeAttributeContract> getAttributes();
}
