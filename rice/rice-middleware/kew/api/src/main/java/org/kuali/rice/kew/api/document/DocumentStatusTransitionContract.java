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
package org.kuali.rice.kew.api.document;

import org.joda.time.DateTime;

public interface DocumentStatusTransitionContract {
    /**
     * The unique id of the DocumentStatusTransition.
     *
     * @return id
     */
    String getId();

    /**
     * The id parent document of the DocumentStatusTransition.
     *
     * @return documentId
     */
	String getDocumentId();

    /**
     * The previous status value of the DocumentStatusTransition.
     *
     * @return oldStatus
     */
	String getOldStatus();

    /**
     * The new status value of the DocumentStatusTransition.
     *
     * @return newStatus
     */
	String getNewStatus();

    /**
     * The date of the DocumentStatusTransition.
     *
     * @return statusTransitionDate
     */
	DateTime getStatusTransitionDate();
}
