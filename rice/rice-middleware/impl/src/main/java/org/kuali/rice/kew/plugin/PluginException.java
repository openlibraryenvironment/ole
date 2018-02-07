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
package org.kuali.rice.kew.plugin;

/**
 * A RuntimeException thrown from the plugin system when a problem is encountered.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginException extends RuntimeException {

	private static final long serialVersionUID = 4383784848245103557L;
	
	public PluginException() {
        super();
    }
    public PluginException(String message) {
        super(message);
    }
    public PluginException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public PluginException(Throwable throwable) {
        super(throwable);
    }
    
}
