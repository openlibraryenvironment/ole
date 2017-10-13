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
package org.kuali.rice.ken.bo;

import org.kuali.rice.ken.api.notification.NotificationResponse;
import org.kuali.rice.ken.api.notification.NotificationResponseContract;
import org.kuali.rice.ken.util.NotificationConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo;

/**
 * This class represents the data structure that will house information for
 * a Notification Response
 *
 * TODO: Really this class should just be replaced by NotificationResponse...
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationResponseBo implements NotificationResponseContract {
    
    private String status;
    
    private String message;

    private Long notificationId;
    
    /**
     * Constructs a NotificationResponse.java instance.
     */
    public NotificationResponseBo() {
	status = NotificationConstants.RESPONSE_STATUSES.SUCCESS;
    }
    
    /**
     * Gets the status attribute. 
     * @return Returns the response status.
     */
    public String getStatus() {
	return status;
    }

    /**
     * Sets the status attribute value.
     * @param status The status to set.
     */
    public void setStatus(String status) {
	this.status = status;
    }
    
    /**
     * Gets the message attribute. 
     * @return Returns the response message.
     */
    
    public String getMessage() {
	return message;
    }

    /**
     * Sets the message attribute value.
     * @param message The message to set.
     */
    public void setMessage(String message) {
	this.message = message;
    }

    /**
     * Gets the id of the sent notification
     * @return the id of the sent notification
     */
    public Long getNotificationId() {
        return notificationId;
    }

    /**
     * Sets the id of the sent notification
     * @param notificationId the id of the sent notification
     */
    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    public static NotificationResponse to(NotificationResponseBo bo) {
        if (bo == null) {
            return null;
        }

        return NotificationResponse.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    public static NotificationResponseBo from(NotificationResponse im) {
        if (im == null) {
            return null;
        }

        NotificationResponseBo bo = new NotificationResponseBo();
        bo.setMessage(im.getMessage());
        bo.setNotificationId(im.getNotificationId());
        bo.setStatus(im.getStatus());

        return bo;
    }
}
