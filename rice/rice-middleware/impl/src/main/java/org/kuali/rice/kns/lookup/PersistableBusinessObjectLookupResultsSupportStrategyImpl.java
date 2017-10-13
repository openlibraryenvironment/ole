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
package org.kuali.rice.kns.lookup;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kuali.rice.kns.lookup.LookupResultsSupportStrategyService;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.KRADPropertyConstants;

/**
 * The LookupResultsSupportStrategyService implementation which supports PersistableBusinessObjects, simply enough 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersistableBusinessObjectLookupResultsSupportStrategyImpl
		implements LookupResultsSupportStrategyService {
	
	private BusinessObjectService businessObjectService;

	/**
	 * Returns the object id
	 * 
	 * @see org.kuali.rice.kns.lookup.LookupResultsSupportStrategyService#getLookupIdForBusinessObject(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public String getLookupIdForBusinessObject(BusinessObject businessObject) {
		PersistableBusinessObject pbo = (PersistableBusinessObject)businessObject;
		return pbo.getObjectId(); 
	}

	/**
	 * Uses the BusinessObjectService to retrieve a collection of PersistableBusinessObjects
	 * 
	 * @see org.kuali.rice.kns.lookup.LookupResultsSupportStrategyService#retrieveSelectedResultBOs(java.lang.String, java.lang.Class, java.lang.String)
	 */
	public <T extends BusinessObject> Collection<T> retrieveSelectedResultBOs(Class<T> boClass, Set<String> lookupIds)
			throws Exception {
		
        Map<String, Collection<String>> queryCriteria = new HashMap<String, Collection<String>>();
        queryCriteria.put(KRADPropertyConstants.OBJECT_ID, lookupIds);
        return getBusinessObjectService().findMatching(boClass, queryCriteria);
	}
	

	/**
	 * Sees if the class implements the PersistableBusinessObject interface; if so, then yes, the BO qualifies!
	 * @see org.kuali.rice.kns.lookup.LookupResultsSupportStrategyService#qualifiesForStrategy(java.lang.Class)
	 */
	public boolean qualifiesForStrategy(Class<? extends BusinessObject> boClass) {
		return PersistableBusinessObject.class.isAssignableFrom(boClass);
	}

	/**
	 * @return the businessObjectService
	 */
	public BusinessObjectService getBusinessObjectService() {
		return this.businessObjectService;
	}

	/**
	 * @param businessObjectService the businessObjectService to set
	 */
	public void setBusinessObjectService(BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}
}
