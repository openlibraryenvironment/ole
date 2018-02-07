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

import java.util.List;

/**
 * <p>The contract for a {@link TermDefinition} which defines a term.  Conceptually,
 * a term describes a piece of data used in a proposition, e.g. the total dollar amount of a grant.  It is a place
 * holder, not a specific fact value as the amount will vary between grants.
 * </p>
 *
 * <p> In KRMS' model, a term contains a term specification which specifies some import details about the term.
 * </p>
 *
 * <p>A term may have parameters associated with it.  The parameters are intended to be used during term resolution to
 * reify the fact value for the term.  Parameters allow multiple terms to exist for a single specification.
 * </p>
 *
 * @see TermDefinition
 * @see org.kuali.rice.krms.api.engine.Term
 * @see TermSpecificationDefinitionContract
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface TermDefinitionContract extends Identifiable, Versioned {
	
	/**
     * Get the associated {@link TermSpecificationDefinitionContract} which specifies some important details about
     * the term.  Will not be null.
     *
	 * @return the term specification
	 */
	TermSpecificationDefinitionContract getSpecification();

    /**
     * Get the description for this {@link TermDefinitionContract}.  May be null.
     *
     * @return the description
     */
    String getDescription();
	
	/**
	 * Get any parameters specified on this {@link TermDefinitionContract}.  May be empty, but never null.
     *
     * @return the term's parameters
     */
	List<? extends TermParameterDefinitionContract> getParameters();
	
}
