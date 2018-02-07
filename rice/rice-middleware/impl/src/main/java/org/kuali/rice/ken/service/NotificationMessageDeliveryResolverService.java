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
package org.kuali.rice.ken.service;


/**
 * This class is responsible for the job that will actually resolve which recipients receive a notification for specific endpoints.
 * This service will be responsible for creating NotificationDeliveryMessage object instances to be used by a later process that actually 
 * delivers the messages.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationMessageDeliveryResolverService {
    /**
     * This service method is responsible for retrieving all unprocessed notifications that have sendDateTimes either equal to or before the 
     * current time, and resolving their recipient lists so that proper NotificationMessageDelivery records can be created for them.  
     * This service is to be run periodically in a separate thread, as a daemon process.
     * @return int the number of resolved messages
     */
    public ProcessingResult resolveNotificationMessageDeliveries();
}
