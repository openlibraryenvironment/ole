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
package org.kuali.rice.ken.deliverer;

import java.util.Collection;

import org.kuali.rice.ken.bo.NotificationMessageDelivery;
import org.kuali.rice.ken.exception.NotificationAutoRemoveException;
import org.kuali.rice.ken.exception.NotificationMessageDeliveryException;

/**
 * A NotificationMessageDeliverer interface specialization that should be implemented
 * by deliverers which can deliver messages in bulk.  This interface needs to exist
 * distinct from NotificationMessageDeliverer because processing in the two cases
 * will be different.  In the bulk case, the deliveries will be performed in a single
 * transaction.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BulkNotificationMessageDeliverer extends NotificationMessageDeliverer {
    /**
     * This method is responsible for delivering a series of messageDelivery records
     * @param messageDeliveries The messageDeliveries to process
     * @throws NotificationMessageDeliveryException
     */
    public void deliverMessage(Collection<NotificationMessageDelivery> messageDeliveries) throws NotificationMessageDeliveryException;
    
    /**
     * This method handles auto removing message deliveries
     * @param messageDelivery The messageDeliveries to auto remove
     * @throws NotificationAutoRemoveException
     */
    public void autoRemoveMessageDelivery(Collection<NotificationMessageDelivery> messageDeliveries) throws NotificationAutoRemoveException;
}
