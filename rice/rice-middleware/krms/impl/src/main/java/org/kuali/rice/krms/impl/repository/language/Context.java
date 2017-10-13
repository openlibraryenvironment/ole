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
package org.kuali.rice.krms.impl.repository.language;

import java.util.Map;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;

public interface Context {
    /**
     * Creates the template context map (template token and data) for 
     * a specific context.
     * 
     *
     * @param parameters Context to create the map from
     * @throws org.kuali.student.r2.common.exceptions.OperationFailedException If creating context data map fails
     */
     public Map<String, Object> createContextMap(Map<String, Object> parameters)
                throws RiceIllegalStateException;
}
