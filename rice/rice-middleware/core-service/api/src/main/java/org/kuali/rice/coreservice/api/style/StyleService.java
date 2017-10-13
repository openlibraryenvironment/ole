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
package org.kuali.rice.coreservice.api.style;

import java.util.List;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Service for working with stylesheets.  This service provides pure data-oriented
 * operations as well as operations dealing with pre-compiled stylesheets.  It's
 * intended that most clients will interact with this service in lieu of the
 * lower-level {@link StyleRepositoryService}.
 * 
 * @see Style
 * @see StyleRepositoryService
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface StyleService {
		
	/**
	 * @see StyleRepositoryService#getStyle(String)
	 */
    public Style getStyle(String styleName);

    /**
     * @see StyleRepositoryService#getAllStyleNames()
     */
    public List<String> getAllStyleNames();

    /**
     * @see StyleRepositoryService#saveStyle(Style)
     */
    @CacheEvict(value={Style.Cache.NAME}, allEntries = true)
    public void saveStyle(Style data);
    
    /**
     * Gets a compiled version of the style with the given name.
     * 
     * @param styleName the name of the style for which to retrieve a compiled version
     * 
     * @return a compiled version of the stylesheet as a {@link Templates} instance
     * 
     * @throws TransformerConfigurationException if compilation of the stylesheet fails
     * @throws IllegalArgumentException if the given styleName is null or blank
     */
    @Cacheable(value= Style.Cache.NAME, key="'styleName=' + #p0")
    public Templates getStyleAsTranslet(String styleName) throws TransformerConfigurationException;

	
}
