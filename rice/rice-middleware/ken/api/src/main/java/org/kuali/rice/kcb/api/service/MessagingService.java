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
package org.kuali.rice.kcb.api.service;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kcb.api.message.MessageDTO;
import org.kuali.rice.kcb.api.exception.MessageDeliveryException;
import org.kuali.rice.kcb.api.exception.MessageDismissalException;

/**
 * The KCB MessagingService provides an API to deliver messages
 * to arbitrary multiple endpoints. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessagingService {
    /**
     * Delivers a message
     * 
     * @param message message to deliver
     * @return identifier for the message
     * @throws MessageDeliveryException if unable to deliver message.
     * @throws RiceIllegalArgumentException if the message is null.
     */
    public Long deliver(MessageDTO message) throws MessageDeliveryException, RiceIllegalArgumentException;
    /**
     * Removes a specific message and all deliveries
     * 
     * @param messageId id of the message to remove
     * @param user the user under which the action was taken
     * @param cause the cause or action taken to remove the message
     * @throws MessageDismissalException if no message for the given messageId is found.
     * @throws RiceIllegalArgumentException if user, cause is null.
     */
    public void remove(long messageId, String user, String cause) throws MessageDismissalException, RiceIllegalArgumentException;
    
    /**
     * Removes a specific message and all deliveries.  Does not throw an exception if no message with the origin
     * id is found.
     * 
     * @param originId origin id of the message to remove
     * @param user the user under which the action was taken
     * @param cause the cause or action taken to remove the message
     * @return Long the message id of the message removed, if any
     * @throws MessageDismissalException if no message for the given messageId is found.
     * @throws RiceIllegalArgumentException if originId is null
     */
    public Long removeByOriginId(String originId, String user, String cause) throws MessageDismissalException, RiceIllegalArgumentException;
}
