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
import org.kuali.rice.kim.api.common.attribute.KimAttributeContract;

/**
 * This is the contract for a KimTypeAttribute.  A KimTypeAttribute
 * associates a kim type with a kim attribute.
 */
public interface KimTypeAttributeContract extends Versioned, GloballyUnique, Identifiable, Inactivatable {

    /**
     * The alpha and/or numeric code used to choose an order for displaying KimTypeAttributes.  This can be null or blank.
     *
     * @return the sort code.
     */
    String getSortCode();

    /**
     * The kim attribute associated with the kim type attribute.  This can be null if no KimAttributes are associated.
     *
     * @return the kim attribute
     */
    KimAttributeContract getKimAttribute();

    /**
     * The kim type id associated with the kim type attribute.  This can be null if no KimType is associated.
     *
     * @return the kim type id
     */
    String getKimTypeId();
}
