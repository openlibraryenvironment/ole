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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import java.io.Serializable;

/**
 * This is the marker interface for constraints. Constraints are a central concept in the Rice data dictionary
 * validation, and are the
 * primary mechanism by which the validation of an object or one of its attributes takes place. For example, by
 * imposing
 * a length constraint
 * on an attribute of a business object, it's possible to indicate that only values shorter (or longer) than a specific
 * number of characters
 * are valid for that attribute.
 *
 * Any interface that extends Constraint is by definition a constraint, and may have one of the following defined:
 *
 * - A sub-interface for {@see Constrainable} that advises on how a constraint maps to data dictionary metadata
 * - A {@see ConstraintProvider} that looks up constraints for a specific constrainable definition
 * - A {@see ConstraintProcessor} that processes the constraint against some object value to determine if it is valid
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 1.1
 */
public interface Constraint extends Serializable {

    // Empty - since this is a marker interface, all of the interesting stuff is in interfaces or classes that extend this interface

}
