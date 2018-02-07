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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.cache.CacheAdminService;
import org.kuali.rice.core.api.cache.CacheTarget;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of the cache administration service which handles requests to flush cache targets from local caches
 * managed by the injected cache manager.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CacheAdminServiceImpl implements CacheAdminService, InitializingBean {

    private static final Logger LOG = Logger.getLogger(CacheAdminServiceImpl.class);

    private CacheManager cacheManager;

    @Override
    public void flush(Collection<CacheTarget> cacheTargets) throws RiceIllegalArgumentException {
        if (CollectionUtils.isNotEmpty(cacheTargets)) {
            logCacheFlush(cacheTargets);
            for (CacheTarget cacheTarget : cacheTargets) {
                if (cacheTarget == null) {
                    throw new RiceIllegalArgumentException("cacheTarget is null");
                }
                final Cache c = getCache(cacheTarget.getCache());
                if (c != null) {
                    if (cacheTarget.containsKey()) {
                        c.evict(cacheTarget.getKey());
                    } else {
                        c.clear();
                    }
                }
            }
        }
    }

    protected void logCacheFlush(Collection<CacheTarget> cacheTargets) {
        if (LOG.isDebugEnabled()) {
            List<String> cacheTargetLog = new ArrayList<String>(cacheTargets.size());
            for (CacheTarget cacheTarget : cacheTargets) {
                cacheTargetLog.add(cacheTarget.toString());
            }
            LOG.debug("Performing local flush of cache targets [" + StringUtils.join(cacheTargetLog, ", ") + "]");
        }
    }

    private Cache getCache(String cache) {
        return cacheManager.getCache(cache);
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (cacheManager == null) {
            throw new IllegalStateException("the cacheManager must be set");
        }
    }
}
