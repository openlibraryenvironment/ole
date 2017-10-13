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
package org.kuali.rice.coreservice.web.namespace;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.impl.cache.DistributedCacheManagerDecorator;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;

public class NamespaceMaintainableImpl extends KualiMaintainableImpl {
    @Override
    public void saveDataObject() {
        super.saveDataObject();

        //flush cache
        DistributedCacheManagerDecorator distributedCacheManagerDecorator =
                GlobalResourceLoader.getService(CoreConstants.CORE_SERVICE_DISTRIBUTED_CACHE);
        distributedCacheManagerDecorator.getCache(Namespace.Cache.NAME).clear();
    }

}
