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
 * Responsible for automatic removal of expired message deliveries
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationMessageDeliveryAutoRemovalService {
    /**
     * This service method is responsible for retrieving all delivered notifications that have autoRemoveDateTimes 
     * that are not null and are less than or equal to the current time, and "completing" them so that they do not 
     * show up in the users list anymore. 
     * @see org.kuali.rice.ken.service.NotificationMessageDeliveryDispatchService#processAutoRemovalOfDeliveredNotificationMessageDeliveries()
     */
    public ProcessingResult processAutoRemovalOfDeliveredNotificationMessageDeliveries();
}
