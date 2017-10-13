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
import org.kuali.rice.ken.api.KenApiConstants;
import org.kuali.rice.ken.api.notification.Notification;
import org.kuali.rice.ken.api.notification.NotificationResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Service that enables publishing a KEN notification document.
 */
@WebService(name= KenApiConstants.ServiceNames.SEND_NOTIFICATION_SERVICE, targetNamespace = KenApiConstants.Namespaces.KEN_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SendNotificationService {
    /**
     * This method invokes a notification with the given message.  This particular service
     * accepts the actual business object.
     * @param message
     * @return NotificationResponse
     */
    @WebMethod(operationName = "invoke")
    @WebResult(name = "response")
    NotificationResponse invoke(@WebParam(name = "message") String message) throws RiceIllegalArgumentException;

    /**
     * This method allows consumers to send notification messages.
     * @since 2.0.1
     * @param notification
     * @return NotificationResponse
     */
    @WebMethod(operationName = "sendNotification")
    @WebResult(name = "response")
    NotificationResponse sendNotification(Notification notification);
}