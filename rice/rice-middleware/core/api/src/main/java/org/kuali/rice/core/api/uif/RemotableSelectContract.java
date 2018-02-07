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
package org.kuali.rice.core.api.uif;

import java.util.List;

/** A select control. */
public interface RemotableSelectContract extends Sized, KeyLabeled {

    /**
     * Gets an immutable list of Grouped keyLabel pairs.  When this list is non-empty,
     * {@link #getKeyLabels()} must be empty.  Cannot be null.
     *
     * @return the list of groups.
     */
    List<? extends RemotableSelectGroupContract> getGroups();

    /**
     * Whether the select control allows selection of multiple values.  defaults to false.
     *
     * @return allows multiple selections
     */
    boolean isMultiple();

    /**
     * If true, indicates that the page needs to be refreshed whenever the drop-down value is changed.
     *
     * @return whether or not to refresh the page when the select value is changed
     */
    boolean isRefreshOnChange();

}
