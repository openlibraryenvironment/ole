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
package org.kuali.rice.krad.dao.impl;

import org.apache.ojb.broker.Identity;
import org.apache.ojb.broker.core.IdentityFactoryImpl;
import org.apache.ojb.broker.core.proxy.IndirectionHandlerCGLIBImpl;
import org.apache.ojb.broker.core.proxy.ProxyHelper;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.dao.PersistenceDao;

public class PersistenceDaoOjb extends PlatformAwareDaoBaseOjb implements PersistenceDao {

    /**
     * @see org.kuali.rice.krad.dao.PersistenceDao#clearCache()
     */
    public void clearCache() {
        getPersistenceBroker(true).clearCache();
    }

    /**
     * @see org.kuali.rice.krad.dao.PersistenceDao#resolveProxy(java.lang.Object)
     */
    public Object resolveProxy(Object o) {
        Identity ident = new IdentityFactoryImpl(getPersistenceBroker(true)).buildIdentity(o);
        IndirectionHandlerCGLIBImpl ih = new IndirectionHandlerCGLIBImpl(getPersistenceBroker(true).getPBKey(), ident);
        return ih.getRealSubject();
    }

    /**
     * @see org.kuali.rice.krad.dao.PersistenceDao#retrieveAllReferences(java.lang.Object)
     */
    public void retrieveAllReferences(Object o) {
        getPersistenceBroker(true).retrieveAllReferences(o);
    }

    /**
     * @see org.kuali.rice.krad.dao.PersistenceDao#retrieveReference(java.lang.Object, java.lang.String)
     */
    public void retrieveReference(Object o, String referenceName) {
        getPersistenceBroker(true).retrieveReference(o, referenceName);
    }

	/**
	 * Asks ProxyHelper if the object is proxied
	 * 
	 * @see org.kuali.rice.krad.dao.PersistenceDao#isProxied(java.lang.Object)
	 */
	public boolean isProxied(Object object) {
		return ProxyHelper.isProxy(object);
	}
 
}
