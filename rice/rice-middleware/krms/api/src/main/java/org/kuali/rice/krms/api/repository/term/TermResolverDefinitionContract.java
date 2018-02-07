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
package org.kuali.rice.krms.api.repository.term;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.Map;
import java.util.Set;

/**
 * <p>The contract for a {@link TermResolverDefinition} which defines a term resolver.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see TermResolverDefinition
 * @see org.kuali.rice.krms.api.engine.TermResolver
 */
public interface TermResolverDefinitionContract extends Identifiable, Inactivatable, Versioned {

    /**
     * Gets the namespace for the defined term resolver. Will not be null or empty.
     *
     * @return the namespace
     */
	String getNamespace();

    /**
     * Gets the name for the defined term resolver.  The namespace and name should uniquely identify a
     * term resolver definition.  Will not be null or empty.
     *
     * @return the name
     */
	String getName();

    /**
     * Gets the type id for the defined term resolver.  This id refers to a
     * type which configures how the term resolver may be obtained.  May be null, but never empty.
     *
     * @return the type id.
     */
	String getTypeId();

    /**
     * Gets the term specification for the output of the defined term resolver.  Will never be null.
     *
     * @return the output term's specification.
     */
	TermSpecificationDefinitionContract getOutput();

    /**
     * Gets the term specifications for any prerequisite terms of the defined term resolver.  May be empty, but will
     * never be null.
     *
     * @return any prerequisite terms.
     */
	Set<? extends TermSpecificationDefinitionContract> getPrerequisites();

    /**
     * Gets any attributes specified on the term resolver definition. May be empty, but never null.
     *
     * @return the attribute map for the term resolver definition.
     */
	public Map<String, String> getAttributes();

    /**
     * Gets the names of any parameters that the defined term resolver requires.  May be empty, but never null.
     *
     * @return the parameter names.
     */
	Set<String> getParameterNames();
	
}
