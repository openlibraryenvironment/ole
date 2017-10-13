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
package org.kuali.rice.krms.api.repository.category;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Defines the category definition.
 */
public interface CategoryDefinitionContract extends Identifiable, Versioned {

    /**
     * Returns the name of the category definition.  The combination of name and namespaceCode
     * represent a unique business key for the category definition.  The name should never be
     * null or blank.
     *
     * @return the name of the category definition, should never be null or blank
     */
    String getName();

    /**
     * Returns the namespace of the category definition.  The combination of
     * namespace and name represent a unique business key for the category
     * definition.  The namespace should never be null or blank.
     *
     * @return the namespace of the category definition, should never be null or blank
     */
    String getNamespace();

}
