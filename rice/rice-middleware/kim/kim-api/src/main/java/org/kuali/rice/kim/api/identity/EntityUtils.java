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
package org.kuali.rice.kim.api.identity;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.mo.common.Defaultable;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;

import java.util.Collection;

public class EntityUtils {

    private EntityUtils() {
        throw new UnsupportedOperationException("do not call.");
    }

    public static <T extends Defaultable & Inactivatable> T getDefaultItem( Collection<T> collection ) {
		// find the default entry
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
		for ( T item : collection ) {
			if ( item.isDefaultValue() && item.isActive() ) {
				return (T)item;
			}
		}
		// if no default, return the first
		for ( T item : collection ) {
		    return item;
		}
		// if neither, return null
		return null;
	}
}
