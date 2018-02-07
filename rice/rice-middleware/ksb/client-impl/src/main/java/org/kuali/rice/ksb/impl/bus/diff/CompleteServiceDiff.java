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
package org.kuali.rice.ksb.impl.bus.diff;

/**
 * A complete difference between local and remote KSB services.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CompleteServiceDiff {

	private final LocalServicesDiff localServicesDiff;
	private final RemoteServicesDiff remoteServicesDiff;
	
	public CompleteServiceDiff(LocalServicesDiff localServicesDiff, RemoteServicesDiff remoteServicesDiff) {
		if (localServicesDiff == null) {
			throw new IllegalArgumentException("localServicesDiff cannot be null");
		}
		if (remoteServicesDiff == null) {
			throw new IllegalArgumentException("remoteServicesDiff cannot be null");
		}
		this.localServicesDiff = localServicesDiff;
		this.remoteServicesDiff = remoteServicesDiff;
	}

	public LocalServicesDiff getLocalServicesDiff() {
		return this.localServicesDiff;
	}

	public RemoteServicesDiff getRemoteServicesDiff() {
		return this.remoteServicesDiff;
	}
	
}
