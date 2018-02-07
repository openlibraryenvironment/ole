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
package org.kuali.rice.krms.api.repository.agenda;

import java.util.Map;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Agenda Definition Contract
 *
 * @see AgendaDefinition
 */
public interface AgendaDefinitionContract extends Identifiable, Inactivatable, Versioned {
	/**
	 * Returns the name of the Agenda.
	 *
	 * @return the name of the Agenda
	 */
	public String getName();

	/**
	 * Returns the KRMS type id of the Agenda.
	 *
	 * @return id for KRMS type related of the agenda
	 */
	public String getTypeId();
	
	/**
	 * Returns the context id of the Agenda.
	 *
	 * @return id for context relative to the agenda
	 */	
	public String getContextId();
	
	/**
	 * Returns the fist agenda item id to be executed in the Agenda.
     * (Also known as the root of the agenda item / rules tree.)
	 *
	 * @return id of the first agenda item id of the agenda.
	 */	
	public String getFirstItemId();
	
	/**
	 * This method returns a list of custom/remote attributes associated with the
	 * agenda.
	 * 
	 * @return a list of custom/remote attribute of the agenda.
	 */
	public Map<String, String> getAttributes();
	
}
