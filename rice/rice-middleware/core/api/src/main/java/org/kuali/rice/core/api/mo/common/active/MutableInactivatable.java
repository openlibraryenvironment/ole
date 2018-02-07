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
package org.kuali.rice.core.api.mo.common.active;

/**
 * This interface is used to tag business objects as inactivateable, so that the framework will automatically handle special
 * processing related to active indicator, e.g. default active indicator to active on new or copy, have a show/hide inactive and
 * hide inactive by default for collections in maintenance documents, display active indicator in the search criteria and result set
 * for lookups and default the search criteria field to active
 */
public interface MutableInactivatable extends org.kuali.rice.core.api.mo.common.active.Inactivatable {

    /**
     * Sets the record to active or inactive.
     */
    void setActive(boolean active);
}
