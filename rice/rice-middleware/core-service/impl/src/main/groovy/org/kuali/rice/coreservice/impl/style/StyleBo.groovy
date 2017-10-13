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
package org.kuali.rice.coreservice.impl.style

import org.kuali.rice.coreservice.api.style.Style
import org.kuali.rice.coreservice.api.style.StyleContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

/**
 * A BusinessObject implementation of the StyleContract which is mapped to the
 * database for persistence.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class StyleBo extends PersistableBusinessObjectBase implements StyleContract {

	private static final long serialVersionUID = 2020611019976731725L

	String id
	String name
	String xmlContent
	boolean active = true
    
    /**
     * Converts the given StyleBo to a Style object.
     * 
     * @param styleBo the StyleBo to convert
     * @return the resulting Style object, or null if the given styleBo was null
     */
    static Style to(StyleBo styleBo) {
    	if (styleBo == null) {
    		return null
    	}
    	return Style.Builder.create(styleBo).build()
    }
    
    /**
     * Constructs a StyleBo from the given Style.
     * 
     * @param style the Style to convert
     * @return the resulting StyleBo object, or null if the given style was null
     */
    static StyleBo from(Style style) {
    	if (style == null) {
    		return null
    	}
    	StyleBo styleBo = new StyleBo()
    	styleBo.setId(style.getId())
    	styleBo.setName(style.getName())
    	styleBo.setXmlContent(style.getXmlContent())
    	styleBo.setActive(style.isActive())
    	styleBo.setVersionNumber(style.getVersionNumber())
    	styleBo.setObjectId(style.getObjectId())
    	return styleBo
    }

}
