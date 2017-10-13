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

/**
 * <p>The contract for a {@link TermParameterDefinition} which defines a term parameter.  This is simply
 * a name and a value that is associated with a term by an identifier.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see TermParameterDefinition
 */
public interface TermParameterDefinitionContract extends Identifiable, Versioned {

    /**
     * Gets the identifier for the term that this parameter belongs to.  May be null, must not be empty.
     *
     * @return the term identifier
     */
	String getTermId();

    /**
     * Gets the name of this parameter.  Must not be null or empty.
     *
     * @return the name of this parameter
     */
	String getName();

    /**
     * Gets the value of this parameter.  May be null.
     *
     * @return the value of this parameter
     */
	String getValue();

}
