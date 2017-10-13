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

import java.util.Collection;

/**
 * The NotificationRecipientService class is responsible for housing user/group related services.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationRecipientService {
    /**
     * This method handles figuring out which recipient type that you are deling with and calls
     * the appropriate validation method.
     * @param recipientId
     * @param recipientType
     * @return boolean
     */
    public boolean isRecipientValid(String recipientId, String recipientType);

    /**
     * This service method checks to make sure that the user recipient is a valid user in the system.
     * @param userRecipientId
     * @return boolean
     */
    public boolean isUserRecipientValid(String userRecipientId);

    /**
     * This service method checks to make sure that the group recipient is a valid group in the system.
     * @param groupRecipientId
     * @return boolean
     */
    public boolean isGroupRecipientValid(String groupRecipientId);

    /**
     * This service method will retrieve all of the user recipients ids that belong to a group.
     * @param groupRecipientId
     * @return A String array of user recipient ids that belong to the specified recipient group id.
     */
    public String[] getGroupMembers(String groupRecipientId);


    /**
     * This method retrieves the display name for a user.
     * @param userId
     * @return String
     */
    public String getUserDisplayName(String userId);
}
