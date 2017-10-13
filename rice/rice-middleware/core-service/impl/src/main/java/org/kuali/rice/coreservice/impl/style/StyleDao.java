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
package org.kuali.rice.coreservice.impl.style;

import org.kuali.rice.coreservice.impl.style.StyleBo;

import java.util.List;

/**
 * Defines data access operations related to {@link StyleBo}.
 * 
 * @see StyleBo
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface StyleDao {
	
	/**
	 * Updates or creates the given style in the data store. 
	 * 
	 * @param style the style to save, if null then this method will do nothing
	 */
    void saveStyle(StyleBo style);

    /**
     * Returns the style with the given name from the data store.
     * 
     * @param styleName the name of the style to retrieve
     * @return the style with the given name, or null if it does not exist
     */
    StyleBo getStyle(String styleName);
    
    /**
     * Return a list of the names of all styles that exist in the data store.
     * This method ...
     * 
     * @return
     */
    List<String> getAllStyleNames();

}
