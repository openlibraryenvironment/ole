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

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.uif.RemotableAttributeFieldContract;

/**
 * An dynamic attribute for kim.
 */
public interface KimAttributeFieldContract extends Identifiable {

    /**
     * Gets the attribute field definition.  Cannot be null.
     *
     * @return the field
     */
    RemotableAttributeFieldContract getAttributeField();

    /**
     * Whether the attribute is a "unique" attribute according to KIM
     * @return unique status
     */
    boolean isUnique();
}
