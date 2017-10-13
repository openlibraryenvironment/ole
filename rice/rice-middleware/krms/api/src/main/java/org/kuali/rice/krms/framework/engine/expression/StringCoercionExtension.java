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
package org.kuali.rice.krms.framework.engine.expression;

/**
 * Interface to extend to implement custom coerce of {@link String}s to an instance of the given type, when when validating
 * {@link org.kuali.rice.krms.framework.engine.Proposition} {@link org.kuali.rice.krms.api.engine.Term}s
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface StringCoercionExtension {

    /**
     * Can this StringCoercionExtension coerce the given type and value into an Object?
     * @param type of Object to coerce to.
     * @param value value to use for coerced type
     * @return boolean true if this StringCoercionExtension can coerce this type and value
     */
    boolean canCoerce(String type, String value);

    /**
     * Returns an Object of the given type populated with the given value.
     * @param type to coerce Object to
     * @param value to coerce
     * @return Object of given type with given value
     */
    Object coerce(String type, String value);
}
