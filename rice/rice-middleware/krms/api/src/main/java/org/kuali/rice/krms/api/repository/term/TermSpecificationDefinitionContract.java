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
import org.kuali.rice.krms.api.repository.category.CategoryDefinitionContract;

import java.util.List;

/**
 * <p>The contract for a {@link TermSpecificationDefinition} which defines important information about a term (see
 * {@link org.kuali.rice.krms.api.repository.term.TermDefinitionContract}).  A term specification should be uniquely
 * identifiable by its name and namespace. This key is important for determining how the fact value for a term can be
 * resolved.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface TermSpecificationDefinitionContract extends Identifiable, Inactivatable, Versioned {

    /**
     * Gets the name for this {@link TermSpecificationDefinitionContract}.  This is an important key
     * that must be unique within a namespace, and is used to determine how to resolve any terms
     * having this specification. Will not be null or empty.
     *
     * @return the name
     */
	String getName();

    /**
	 * Gets the namespace of this {@link TermSpecificationDefinitionContract}.  Will not be null or empty.
	 *
	 * @return the namespace of the TermSpecificationDefinitionContract
	 */
	public String getNamespace();

    /**
     * Gets the fully qualified class name for the values of any term having this specification.  E.g. if the
     * type of the fact values for the "total dollar amount of a grant" term was {@link java.math.BigDecimal},
     * then the term specification's type would be the String "java.math.BigDecimal".  Will never return null or
     * the empty string.
     *
     * @return the fully qualified name of the java type of values for this term.
     */
	String getType();

    /**
     * Gets the description for this term specification, which will typically be a suitable description for
     * any term having this specification as well.  May return null if no description is specified for the term
     * specification.
     *
     * @return the description for this term specification.
     */
    String getDescription();

    /**
     * Gets an ordered list of the categories which this term specification
     * definition belongs to.  This list can be empty but will never be null.
     *
     * @return the list of categories for this term specification definition.
     */
    List<? extends CategoryDefinitionContract> getCategories();

    // TODO: ensure that @since is accurate when this sandbox branch is merged back into Rice proper
    /**
     * Gets a list of the IDs for the contexts this TermSpecification can be used in.
     *
     * @return the list of contexts for this term specification definition
     * @since 2.2
     */
    List<String> getContextIds();

}
