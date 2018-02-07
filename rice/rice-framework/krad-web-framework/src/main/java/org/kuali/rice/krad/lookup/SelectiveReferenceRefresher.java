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
package org.kuali.rice.krad.lookup;

import org.kuali.rice.krad.bo.BusinessObject;

import java.util.Collection;

/**
 * Classes that implement this interface will refresh
 */
public interface SelectiveReferenceRefresher {
    /**
     * Returns a list of references that must be refreshed after a lookup performed on an attribute is performed.
     *
     * A lookup on an attribute may cause many attribute values to be updated upon return from lookup.  Generally,
     * the returned attributes are the PK of the BO being looked up.  For example,
     * a lookup on an account number attribute will cause the chart of accounts code and account code to be returned.
     *
     * These returned attributes may cause other references on the page to be returned.  For example, an account number lookup
     * may cause the chart code to change, and if there is also an ObjectCode reference in the BO, then any change in the chart code will cause the
     * referenced ObjectCode BO to be changed.
     *
     * @param attributeName the name of the attribute with a quickfinder of the maintained BO.
     *
     * @return a list of reference names that could be affected by lookup return values
     */
    public Collection<String> getAffectedReferencesFromLookup(BusinessObject baseBO, String attributeName, String collectionPrefix);
}
