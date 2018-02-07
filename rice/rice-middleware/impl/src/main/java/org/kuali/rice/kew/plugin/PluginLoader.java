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
 * Loads a plugin from some source (i.e. filesytem, classpath, network...)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PluginLoader extends Modifiable {

    public String getPluginName();
    
	/**
	 * Loads the plugin, this method should NOT invoke any of the Lifecycle methods of the plugin
	 * before returning it to the calling code.
	 */
	public Plugin load() throws Exception;
	
	/**
	 * Returns true if the Plugin loaded by this loader has been removed (for example, it was
	 * deleted from the file system which should result in the plugin being undeployed).
	 */
	public boolean isRemoved();
	
	public void setPluginConfigPath(String pluginConfigPath);
	
}
