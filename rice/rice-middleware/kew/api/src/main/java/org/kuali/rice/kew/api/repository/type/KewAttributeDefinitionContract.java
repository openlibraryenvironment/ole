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

public interface KewAttributeDefinitionContract extends Identifiable, Inactivatable, Versioned {

	/**
	 * This is the name for the KewAttributeDefinition
	 *
	 * <p>
	 * It is a name of a KewAttributeDefinition.
	 * </p>
	 * @return name for KewAttributeDefinition.
	 */
	public String getName();

	/**
	 * This is the namespace code. 
	 *
	 * <p>
	 * It provides scope of the KewAttributeDefinition.
	 * </p>
	 * @return the namespace code of the KewAttributeDefinition.
	 */
	public String getNamespace();

	/**
	 * This is the label of the KewAttributeDefinition
	 * 
	 * @return the label of the KewAttributeDefinition
	 */
	public String getLabel();

    /**
     * this is the optional description for the {@link KewAttributeDefinition}
     * @return the description text
     */
    public String getDescription();
	
	/**
	 * This is the component name of the KewAttributeDefinition
	 * 
	 * @return the component name of the KewAttributeDefinition
	 */
	public String getComponentName();
}
