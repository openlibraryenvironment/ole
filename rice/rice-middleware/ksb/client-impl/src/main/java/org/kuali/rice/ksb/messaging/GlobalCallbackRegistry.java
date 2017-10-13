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
package org.kuali.rice.ksb.messaging;

import org.kuali.rice.ksb.api.messaging.AsynchronousCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Can be used to register an {@link AsynchronousCallback} to recieve callback
 * notifications.
 * 
 * @see AsynchronousCallback
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GlobalCallbackRegistry {

	private static List<AsynchronousCallback> callbacks = new ArrayList<AsynchronousCallback>();

	public static List<AsynchronousCallback> getCallbacks() {
		return callbacks;
	}

	public static void setCallbacks(List<AsynchronousCallback> callbacks) {
		GlobalCallbackRegistry.callbacks = callbacks;
	}
}
