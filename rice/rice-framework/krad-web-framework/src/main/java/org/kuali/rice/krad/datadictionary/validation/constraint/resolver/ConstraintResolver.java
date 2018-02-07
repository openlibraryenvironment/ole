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
package org.kuali.rice.krad.datadictionary.validation.constraint.resolver;

import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;

import java.util.List;

/**
 * ConstraintResolver provides a lookup of constraints for a specific constrainable attribute definition
 *
 * <p>Implemented by constraint
 * providers as a mechanism to store functional lookups in a map, keyed by constraint type, for example.</p>
 *
 * {@see AttributeDefinitionConstraintProvider} for a number of examples.
 *
 * @param <T>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public interface ConstraintResolver<T extends Constrainable> {

    /**
     * gets the list of constraints from the provided definition
     *
     * @param definition - a data dictionary definition
     * @param <C> - the java type of the constraint
     * @return - a list of constraints
     */
    public <C extends Constraint> List<C> resolve(T definition);

}