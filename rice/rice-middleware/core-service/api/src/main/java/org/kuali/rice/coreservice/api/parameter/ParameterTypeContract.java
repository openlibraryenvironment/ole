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
package org.kuali.rice.coreservice.api.parameter;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Defines the contract for a parameter type.  The parameter type is largely indicative of the way in which the
 * parameter will be used by clients.  Examples might include a parameter which is used for configuration purposes
 * or one which is used to define parameters used during validation. 
 */
public interface ParameterTypeContract extends Versioned, GloballyUnique, Inactivatable, Coded {
    
    /**
     * This the name for the ParameterType.  This can be null or a blank string.
     *
     * @return the name of the ParameterType
     */
    String getName();
}
