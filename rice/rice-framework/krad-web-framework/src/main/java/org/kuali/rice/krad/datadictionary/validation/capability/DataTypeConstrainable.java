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
package org.kuali.rice.krad.datadictionary.validation.capability;

import org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint;

/**
 * Validation capability to constrain a value to be of a specific data type.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DataTypeConstrainable extends Constrainable, DataTypeConstraint {

    // To match up with legacy code for length and range constraints, data type constraints are imposed
    // on the definition, making the capability a sub-interface of the constraint

}
