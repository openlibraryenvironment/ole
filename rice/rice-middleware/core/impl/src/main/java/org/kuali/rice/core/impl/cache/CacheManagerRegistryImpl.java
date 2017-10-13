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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.springframework.beans.factory.NamedBean;
import org.springframework.cache.CacheManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A simple class that holds a global registry to the cache managers.
 */
public final class CacheManagerRegistryImpl implements CacheManagerRegistry {
    private static final String GET_NAME = "getName";
    private static final Logger LOG = Logger.getLogger(CacheManagerRegistryImpl.class);
    private static final String GET_NAME_MSG = "unable to get the getName method on the cache manager";

    private static final List<CacheManager> CACHE_MANAGERS = new CopyOnWriteArrayList<CacheManager>();
    private static final Map<String, CacheManager> CACHE_MANAGER_MAP = new ConcurrentHashMap<String, CacheManager> ();

    public void setCacheManager(CacheManager c) {
        if (c == null) {
            throw new IllegalArgumentException("c is null");
        }

        CACHE_MANAGERS.add(c);

        //keep map as well
        for (String cacheName : c.getCacheNames()) {
            CACHE_MANAGER_MAP.put(cacheName, c);
        }
    }

    @Override
    public List<CacheManager> getCacheManagers() {
        return Collections.unmodifiableList(CACHE_MANAGERS);
    }

    @Override
    public CacheManager getCacheManager(String name) {
        for (CacheManager cm : getCacheManagers()) {
            if (name.equals(getCacheManagerName(cm))) {
                return cm;
            }
        }
        return null;
    }

    @Override
    public String getCacheManagerName(CacheManager cm) {
        if (cm instanceof NamedBean) {
            return ((NamedBean) cm).getBeanName();
        }

        String v = "Unnamed CacheManager " + cm.hashCode();
        try {
            final Method nameMethod = cm.getClass().getMethod(GET_NAME, new Class[] {});
            if (nameMethod != null && nameMethod.getReturnType() == String.class) {
                v = (String) nameMethod.invoke(cm, new Object[] {});
            }
        } catch (NoSuchMethodException e) {
            LOG.warn(GET_NAME_MSG, e);
        } catch (InvocationTargetException e) {
            LOG.warn(GET_NAME_MSG, e);
        } catch (IllegalAccessException e) {
            LOG.warn(GET_NAME_MSG, e);
        }

        return v;
    }

    @Override
    public CacheManager getCacheManagerByCacheName(String cacheName) {
        CacheManager cm = CACHE_MANAGER_MAP.get(cacheName);
        if (cm != null) {
            return cm;
        }
        throw new RiceIllegalArgumentException("Cache not found : " + cacheName);
    }
}
