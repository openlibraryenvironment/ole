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
package org.kuali.rice.core.impl.cache;

import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GlobalCacheManager implements CacheManager {


    private CacheManagerRegistry cacheManagerRegistry;

    @Override
    public Cache getCache(String name) {
        return cacheManagerRegistry.getCacheManagerByCacheName(name).getCache(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        List<String> cacheNames = new ArrayList<String>();
        for (CacheManager cm : cacheManagerRegistry.getCacheManagers()) {
            cacheNames.addAll(cm.getCacheNames());
        }
        return Collections.unmodifiableCollection(cacheNames);
    }

    public void setCacheManagerRegistry(CacheManagerRegistry cacheManagerRegistry) {
        this.cacheManagerRegistry = cacheManagerRegistry;
    }
}
