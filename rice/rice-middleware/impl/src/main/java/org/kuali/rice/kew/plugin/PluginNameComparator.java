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

import java.util.Comparator;

/**
 * A comparator which sorts a collection of plugins names alphabeticaly.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginNameComparator implements Comparator<String> {
	
    public int compare(String pluginName1, String pluginName2) {
        int compareValue = 0;
        if (pluginName1.equals(pluginName2)) {
            compareValue = 0;
        } else {
            compareValue = pluginName1.compareTo(pluginName2);
        }
        return compareValue;
    }

}
