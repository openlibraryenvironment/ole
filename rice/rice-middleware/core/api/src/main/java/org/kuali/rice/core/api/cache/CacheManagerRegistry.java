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
package org.kuali.rice.core.api.cache;

import org.springframework.cache.CacheManager;

import java.util.List;

/**
 * Allows access to a registry of {@link CacheManager} instances that are identified by name.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
public interface CacheManagerRegistry {
    
    /**
     * Will return a list of registered cache managers.  Will not return null but may return an empty list.
     * 
     * @return a list of cache managers
     */
    List<CacheManager> getCacheManagers();

    /**
     * Gets a cache manager for a given name.  Name cannot be null or blank.
     *
     * @param name the cache manager name
     * @return the CacheManager
     * @throws IllegalArgumentException if the name is null or blank
     */
    CacheManager getCacheManager(String name);

    /**
     * Gets the name of a cache manager.  The cm cannot be null.  Will not return null or blank string.
     *
     * @param cm the cache manager
     * @return the name
     * @throws IllegalArgumentException if the cm is null
     */
    String getCacheManagerName(CacheManager cm);

    /**
     * Gets a cache manager for a given cache name.  Name cannot be null or blank.
     *
     * @param cacheName the  name of a Cache in a CacheManager.
     * @return the CacheManager
     * @throws IllegalArgumentException if the name is null or blank
     */
    CacheManager getCacheManagerByCacheName(String cacheName);
}
