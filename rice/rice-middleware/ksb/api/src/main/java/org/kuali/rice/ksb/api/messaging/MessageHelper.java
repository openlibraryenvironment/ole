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
package org.kuali.rice.ksb.api.messaging;

import java.io.Serializable;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * Interface for acquiring services asynchronously.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface MessageHelper {

	<T> T getServiceAsynchronously(QName qname);
    <T> T getServiceAsynchronously(QName qname, String applicationId);
	<T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback);
	<T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback, Serializable context);
	<T> T getServiceAsynchronously(QName qname, AsynchronousCallback callback, Serializable context, String value1, String value2);
    <T> T getServiceAsynchronously(QName qname, String applicationId, AsynchronousCallback callback, Serializable context, String value1, String value2);
	<T> T getServiceAsynchronously(QName qname, Serializable context, String value1, String value2, long delayMilliseconds);
    <T> T getServiceAsynchronously(QName qname, String applicationId, Serializable context, String value1, String value2, long delayMilliseconds);
    <T> List<T> getAllRemoteServicesAsynchronously(QName qname);
}
