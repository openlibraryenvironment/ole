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
package org.kuali.rice.krms.api.repository.function;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;

import java.util.List;

/**
 * Defines the contract for a function definition.  A function definition can be
 * defined by clients integrating with KRMS in order to implement custom logic
 * which they may require as part of rule execution and evaluation.  These can
 * then be used in simple propositions.
 * 
 * <p>The function definition itself defines various metadata about the function
 * including it's name, return type, and expected parameter types.  The actual
 * implementation of the function is retrieved through the type defined by
 * {@link #getTypeId()}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface FunctionDefinitionContract extends Versioned, Identifiable, Inactivatable {

	/**
	 * Returns the namespace code of this function definition.  All functions
	 * have a namespace and this value can never be null or blank.  The
	 * combination of namespace plus name must be unique within the entire
	 * repository of functions.
	 * 
	 * @return the namespace code of this function definition
	 */
	String getNamespace();
	
	/**
	 * Returns the name of this function definition.  All functions have a name
	 * and this value can never be null or blank.  The combination of namespace
	 * plus name must be unique within the entire repository of functions.
	 * 
	 * @return the name of this function definition
	 */
	String getName();
	
	/**
	 * Returns the description of this function definition.  The description is
	 * intended to provide more information about a function and it's
	 * appropriate usage.  The description is optional.
	 * 
	 * @return the description of this function definition
	 */
	String getDescription();
	
	/**
	 * Returns the type of the return value of the function defined by this
	 * function definition.  This can be one of a set of "built-in" data types
	 * or a custom data type represented as a fully qualified java class name.
	 * All functions must have a return type so this method should never return
	 * null or blank.
	 * 
	 * @return the return type of this function definition
	 */
	String getReturnType();
	
	/**
	 * Returns the id of the {@link KrmsTypeDefinition} which defines the
	 * actual implementation of this function such that it can be loaded into
	 * the engine and executed.
	 * 
	 * @return the type id of this function definition
	 */
	String getTypeId();

	/**
	 * Returns an ordered, immutable list of the parameters which this function
	 * definition requires.  This list can be empty (in the case of a function
	 * which has no arguments) but will never be null.
	 * 
	 * @return the list of parameters for this function definition
	 */
	List<? extends FunctionParameterDefinitionContract> getParameters();

    /**
     * Returns an ordered list of the categories which this function
     * definition requires.  This list can be empty (in the case of a function
     * which has no arguments) but will never be null.
     *
     * @return the list of categories for this function definition
     */
    List<? extends CategoryDefinitionContract> getCategories();

}
