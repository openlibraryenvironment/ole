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
package org.kuali.rice.kew.api.action;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.delegation.DelegationType;

public interface ActionItemContract {
    String getId();
    DateTime getDateTimeAssigned();
    String getActionRequestCd();
    String getActionRequestId();
    String getDocumentId();
    String getDocTitle();
    String getDocLabel();
    String getDocHandlerURL();
    String getDocName();
    String getResponsibilityId();
    String getRoleName();
    DelegationType getDelegationType();
    String getGroupId();
    String getPrincipalId();
    String getDelegatorGroupId();
    String getDelegatorPrincipalId();

    /**
     * This method should never be called, has been deprecated, and will always return null. It was mistakenly added to
     * this interface when it was created and has been left here for compatibility purposes.
     *
     * @return always returns null
     * @deprecated dateAssignedString is never used
     */
    @Deprecated
    String getDateAssignedString();

    /**
     * This method should never be called, has been deprecated, and will always return null. It was mistakenly added to
     * this interface when it was created and has been left here for compatibility purposes.
     *
     * @return always returns null
     * @deprecated actionToTake is never used
     */
    @Deprecated
    String getActionToTake();

    /**
     * This method should never be called, has been deprecated, and will always return null. It was mistakenly added to
     * this interface when it was created and has been left here for compatibility purposes.
     *
     * @return always returns null
     * @deprecated actionItemIndex is never used
     */
    Integer getActionItemIndex();


}
