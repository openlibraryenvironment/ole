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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;

import javax.xml.namespace.QName;

public final class KimTypeUtils {

    private KimTypeUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    /**
     * Resolves the given kim type service name represented as a String to the appropriate QName.
     * If the value given is empty or null, then it will resolve to a qname representing the
     * {@link KimConstants#DEFAULT_KIM_TYPE_SERVICE}.
     *
     * @param kimTypeServiceName the name to resolve
     * @return a qname representing a resolved type service
     */
    public static QName resolveKimTypeServiceName(String kimTypeServiceName) {
        if (StringUtils.isBlank(kimTypeServiceName)) {
            return QName.valueOf(KimConstants.DEFAULT_KIM_TYPE_SERVICE);
        }
        return QName.valueOf(kimTypeServiceName);
    }
}
