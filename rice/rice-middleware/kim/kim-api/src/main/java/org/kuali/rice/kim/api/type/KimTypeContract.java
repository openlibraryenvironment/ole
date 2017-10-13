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
package org.kuali.rice.kim.api.type;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.List;

/**
 * This is the contract for a KimType.  A KimType is a metadata wrapper around a
 * kim type service which aides in attribute resolution.
 */
public interface KimTypeContract extends Versioned, GloballyUnique, Identifiable, Inactivatable {

    /**
     * The service name used to resolve attribute values.
     *
     * @return the service name
     */
    String getServiceName();

    /**
     * The namespace code that this kim type belongs too.
     *
     * @return namespace code
     */
    String getNamespaceCode();

    /**
     * The name of the kim type.
     *
     * @return the name
     */
    String getName();

    /**
     * The list of attribute definitions associated with the kim type. This cannot be null.  If no
     * attribute definitions are associated with the kim type then this will return an empty collection.
     *
     * @return the list of attribute definitions
     */
    List<? extends KimTypeAttributeContract> getAttributeDefinitions();
}
