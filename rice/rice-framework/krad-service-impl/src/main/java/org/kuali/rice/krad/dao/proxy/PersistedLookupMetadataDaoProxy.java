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
package org.kuali.rice.krad.dao.proxy;

import java.sql.Timestamp;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kns.lookup.LookupResults;
import org.kuali.rice.kns.lookup.SelectedObjectIds;
import org.kuali.rice.krad.dao.PersistedLookupMetadataDao;

public class PersistedLookupMetadataDaoProxy implements PersistedLookupMetadataDao {

    private PersistedLookupMetadataDao persistedLookupMetadataDaoJpa;
    private PersistedLookupMetadataDao persistedLookupMetadataDaoOjb;
	
    private PersistedLookupMetadataDao getDao(Class clazz) {
    	final String TMP_NM = clazz.getName();
		final int START_INDEX = TMP_NM.indexOf('.', TMP_NM.indexOf('.') + 1) + 1;
    	return (OrmUtils.isJpaAnnotated(clazz) && (OrmUtils.isJpaEnabled() || OrmUtils.isJpaEnabled("rice.krad"))) ?
						persistedLookupMetadataDaoJpa : persistedLookupMetadataDaoOjb; 
    }

    public void deleteOldLookupResults(Timestamp expirationDate) {
    	getDao(LookupResults.class).deleteOldLookupResults(expirationDate);
    }

    public void deleteOldSelectedObjectIds(Timestamp expirationDate) {
    	getDao(SelectedObjectIds.class).deleteOldSelectedObjectIds(expirationDate);
    }

	public void setPersistedLookupMetadataDaoJpa(PersistedLookupMetadataDao persistedLookupMetadataDaoJpa) {
		this.persistedLookupMetadataDaoJpa = persistedLookupMetadataDaoJpa;
	}

	public void setPersistedLookupMetadataDaoOjb(PersistedLookupMetadataDao persistedLookupMetadataDaoOjb) {
		this.persistedLookupMetadataDaoOjb = persistedLookupMetadataDaoOjb;
	}
    
}
