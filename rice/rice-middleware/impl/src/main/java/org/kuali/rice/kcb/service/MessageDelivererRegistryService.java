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
package org.kuali.rice.kcb.service;

import java.util.Collection;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kcb.api.service.MessageDelivererRegistryAPI;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;

/**
 * This class is responsible for providing services for Message Deliverers (delivery types)
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MessageDelivererRegistryService extends MessageDelivererRegistryAPI {
    /**
     * This service method is responsible for retrieving all MessageDeliverer Types.  
     * @return Collection of MessageDeliverer objects
     */
    Collection<MessageDeliverer> getAllDeliverers();

    /**
     * This service method is responsible for retrieving all MessageDeliverer Types names.  
     * @return Collection of deliverer type names
     */
    Collection<String> getAllDelivererTypes();

    /**
     * This method returns the associated deliverer class instance for the given MessageDelivery instance.
     * @param messageDelivery
     * @return MessageDeliverer or null if not found
     * @throws RiceIllegalArgumentException if messageDelivery is null
     */
    MessageDeliverer getDeliverer(MessageDelivery messageDelivery) throws RiceIllegalArgumentException;
    
    /**
     * This method returns the associated deliverer class instance for the given deliverer name.
     * @param messageDelivererName
     * @return MessageDeliverer or null if not found
     * @throws RiceIllegalArgumentException if messageDelivererName is null
     */
    MessageDeliverer getDelivererByName(String messageDelivererName) throws RiceIllegalArgumentException;
}
