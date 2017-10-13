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
package org.kuali.rice.location.api.state;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * This is the contract for a State.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface StateContract extends Versioned, Inactivatable, Coded {

    /**
     * This the postal name for the State.  This cannot be null or a blank string.
     *
     * @return postal name
     */
    String getName();

    /**
     * This the postal country code for the State.  This cannot be null or a blank string.
     *
     * @return postal country code
     */
    String getCountryCode();
}
