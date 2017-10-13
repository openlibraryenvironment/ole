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
package org.kuali.rice.coreservice.api.namespace;

import org.kuali.rice.core.api.mo.common.Coded;
import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Defines the contract for a Namespace.  A namespace is a mechanism for partitioning of data into
 * areas of responsibility.  Since much of the Kuali Rice middleware is shared across multiple integrating
 * applications, this notion of a namespace is a critical element in keeping related data elements
 * grouped together and isolated from those to which they should have no relation or access.
 */
public interface NamespaceContract extends Versioned, GloballyUnique, Inactivatable, Coded {

    /**
     * This the id of the application which owns this Namespace.  If this namespace has no application owner,
     * then this method will return null.
     *
     * <p>
     * It is a way of assigning the Namespace to a specific rice application or rice ecosystem.
     * </p>
     *
     * @return application id
     */
    String getApplicationId();

    /**
     * This the name for the Namespace.  This can be null or a blank string.
     *
     * @return name
     */
    String getName();
}
