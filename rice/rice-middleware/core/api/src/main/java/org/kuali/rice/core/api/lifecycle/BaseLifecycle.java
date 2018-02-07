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
package org.kuali.rice.core.api.lifecycle;

/**
 * An abstract superclass to aid in implementing lifeycles.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseLifecycle implements Lifecycle {

	private boolean started = false;

	public boolean isStarted() {
		return this.started;
	}

	public void start() throws Exception {
		setStarted(true);
	}

	public void stop() throws Exception {
		setStarted(false);
	}

	protected void setStarted(boolean started) {
		this.started = started;
	}

}
