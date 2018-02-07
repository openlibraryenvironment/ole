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

import java.util.HashMap;
import java.util.Map;

/**
 * This is an abstract class for creating a map (containing token/data) used
 * in template translations.
 */
public abstract class AbstractContext implements Context {
	/**
	 * <p>These common shared tokens are needed since velocity doesn't 
	 * allow periods in tokens.</p>
	 * <p>E.g. kuali.reqComponent.field.type.totalCredits must either be convert to 
	 * totalCredits or reqCompFieldType_totalCredits so a template would look 
	 * like:</p>
	 * <p>'Student must take $totalCredits of MATH 100'</p>
	 * or
	 * <p>'Student must take $reqCompFieldType_totalCredits of MATH 100'</p>
	 */
	protected final static String FIELDS_TOKEN = "fields";

	/**
     * Creates the context map (template data) for the requirement component.
     * Also, adds the field token map to the context map.
     *
     * @param parameters
     * @throws org.kuali.student.r2.common.exceptions.DoesNotExistException If CLU, CluSet or relation does not exist
     */
    @Override
    public Map<String, Object> createContextMap(Map<String, Object> parameters)  {
        Map<String, Object> contextMap = new HashMap<String, Object>();
        contextMap.put(FIELDS_TOKEN, parameters);
        return contextMap;
    }
}
