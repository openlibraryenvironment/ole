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
package org.kuali.rice.ken.api.service;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;

import java.util.Collection;

/**
 * KEN API service internal to Rice, for use by KCB 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface KENAPIService {
    /**
     * Returns names of all channels defined in KEN
     * @return names of all channels defined in KEN
     */
    Collection<String> getAllChannelNames();
    
    /**
     * Returns the names of all deliverers the recipient has configured for a given channel
     * @return the names of all deliverers the recipient has configured for a given channel
     * @throws RiceIllegalArgumentException if the recipient,  channel is null or blank.
     */
    Collection<String> getDeliverersForRecipientAndChannel(String recipient, String channel) throws RiceIllegalArgumentException;

    /**
     * Returns the specified recipient preference for the user
     * @param recipient the recipient
     * @param prefKey the preference key
     * @return the specified recipient preference for the user
     * @throws RiceIllegalArgumentException if the recipient,  prefKey is null or blank
     */
    String getRecipientPreference(String recipient, String prefKey) throws RiceIllegalArgumentException;
}
