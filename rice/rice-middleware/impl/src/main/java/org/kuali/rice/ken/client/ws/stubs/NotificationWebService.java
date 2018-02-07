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
/**
 * NotificationWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.kuali.rice.ken.client.ws.stubs;

/**
 * This class is a facade service wrapper interface for JAX-RPC compliance within 
 * Apache Axis.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationWebService extends java.rmi.Remote {
    public java.lang.String sendNotification(java.lang.String notificationMessageAsXml) throws java.rmi.RemoteException;
}
