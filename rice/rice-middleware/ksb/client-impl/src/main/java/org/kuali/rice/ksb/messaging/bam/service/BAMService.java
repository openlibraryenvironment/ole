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
package org.kuali.rice.ksb.messaging.bam.service;

import java.lang.reflect.Method;
import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;
import org.kuali.rice.ksb.api.bus.ServiceDefinition;
import org.kuali.rice.ksb.messaging.bam.BAMTargetEntry;


/**
 * Service for recording and accessing entries in the BAM.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface BAMService {

	public List<BAMTargetEntry> getCallsForService(QName serviceName);
	public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef);
	public BAMTargetEntry recordClientInvocation(ServiceConfiguration serviceConfiguration, Object target, Method method, Object[] params);
	public BAMTargetEntry recordClientInvocationError(Throwable throwable, BAMTargetEntry bamTargetEntry);
	public BAMTargetEntry recordServerInvocation(Object target, ServiceDefinition serviceDefinition, Method method, Object[] params);
	public BAMTargetEntry recordServerInvocationError(Throwable throwable, BAMTargetEntry bamTargetEntry);
	public void clearBAMTables();
	public List<BAMTargetEntry> getCallsForService(QName serviceName, String methodName);
	public List<BAMTargetEntry> getCallsForRemotedClasses(ObjectDefinition objDef, String methodName);
}
