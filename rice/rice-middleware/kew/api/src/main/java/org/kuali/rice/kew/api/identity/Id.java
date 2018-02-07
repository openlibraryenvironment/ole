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
package org.kuali.rice.kew.api.identity;

import java.io.Serializable;

/**
 * Superinterface of UserId and GroupId
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Id extends Serializable {
    /**
     * Returns true if this Id has an empty value. Empty Ids can't be used as keys in a Hash,
     * among other things.
     *
     * @return true if this instance doesn't have a value
     */
    public boolean isEmpty();
}
