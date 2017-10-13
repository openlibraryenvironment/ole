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
package org.kuali.rice.krms.api.repository.context;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinitionContract;

import java.rmi.activation.Activatable;
import java.util.List;
import java.util.Map;

/**
 * An interface which defines the contract for context definition objects.
 * <p>A context is a set of related krms entities. A context definition
 * defines information about a context which can be loaded into the rules
 * engine for evaluation.
 *
 * A context definition includes a list of agendas which are valid within the
 * context.  Typically, during rule engine execution, one or more of these
 * agendas is selected for execution based on a given set of selection criteria. All KRMS components
 * (agendas, rules, actions, terms, etc.) must be of the same context to
 * work together. It is up to the client implementor to choose how broadly or
 * finely grained the scope of the context is to be.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface ContextDefinitionContract extends Versioned, Identifiable, Inactivatable {

	/**
	 * Returns the namespace of the context definition.  The combination of
	 * namespace and name represent a unique business key for the context
	 * definition.  The namespace should never be null or blank.
	 * 
	 * @return the namespace of the context definition, should never be null or blank
	 */
	String getNamespace();
	
	/**
	 * Returns the name of the context definition.  The combination of name and namespaceCode
	 * represent a unique business key for the context definition.  The name should never be
	 * null or blank.
	 * 
	 * @return the name of the context definition, should never be null or blank
	 */
	String getName();
	
	/**
	 * Returns the type id for the context definition.  If the type id is null, that means
	 * this context definition is of the default type.
	 * 
	 * @return the type id for the context definition, or null if this context definition is of the default type
	 */
	String getTypeId();
	
	
    /**
     * Returns the description of the context definition.
     *
     * @return the description of the context definition. May be null.
     */
    String getDescription();

	/**
	 * Returns the list of agendas {@link AgendaDefinitionContract} contained in the context definition.
	 * This method should never return null. An empty list is returned
     * if no agendas are associated with this context.
	 * 
	 * @return the list of agendas contained in this context definition
	 */
	List<? extends AgendaDefinitionContract> getAgendas();

	/**
	 * Returns a map of name/value pairs representing the
     * attributes associated with this context.
     * <p>This method should never
     * return null. An empty map is returned if no attributes are associated
     * with the context.</p>
	 * 
	 * @return a list of Map of name/value String pairs.
	 */
	public Map<String, String> getAttributes();
	
}
