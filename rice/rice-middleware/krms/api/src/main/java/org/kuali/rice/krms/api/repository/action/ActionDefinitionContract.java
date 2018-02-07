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
package org.kuali.rice.krms.api.repository.action;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Map;

/**
 * Defines the contract for an {@link ActionDefinition}
 *
 * @see ActionDefinition
 * @see org.kuali.rice.krms.framework.engine.Action
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ActionDefinitionContract extends Identifiable, Versioned {

	/**
	 * Returns the name of the Action
	 *
	 * <p>
	 * name - the name of the Action
	 * </p>
	 * @return the name of the Action
	 */
	public String getName();

	/**
	 * Returns the namespace of the Action
	 *
	 * <p>
	 * The namespace of the Action
	 * </p>
	 * @return the namespace of the Action
	 */
	public String getNamespace();

    /**
     * Returns the description for what the parameter is used for.  This can be null or a blank string.
     *
     * @return the description of the Action
     */
	public String getDescription();

	/**
	 * Returns the KrmsType of the Action
	 *
	 * @return id for KRMS type related of the Action
	 */
	public String getTypeId();
	
	/**
	 * Returns the id of the rule associated with the action
	 * 
	 * @return id for the Rule associated with the action.
	 */
	public String getRuleId();
	
	/**
	 * Returns the sequence number of the  action
	 * 
	 * @return sequence number of the action.
	 */
	public Integer getSequenceNumber();
	
	/**
	 * Returns a set of attributes associated with the
	 * Action.  The attributes are represented as name/value pairs.
	 * 
	 * @return a set of ActionAttribute objects.
	 */
	public Map<String, String> getAttributes();
	

}
