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
package org.kuali.rice.kew.responsibility.dao.impl;

import org.apache.ojb.broker.PersistenceBroker;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.responsibility.dao.ResponsibilityIdDAO;
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

public class ResponsibilityIdDAOOjbImpl extends PersistenceBrokerDaoSupport implements ResponsibilityIdDAO {

	public String getNewResponsibilityId() {
        return (String)this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            public Object doInPersistenceBroker(PersistenceBroker broker) {
            	return String.valueOf(getPlatform().getNextValSQL("KREW_RSP_S", broker));
            }
        });
    }

	protected DatabasePlatform getPlatform() {
    	return (DatabasePlatform) GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
    }

}
