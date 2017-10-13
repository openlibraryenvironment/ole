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

import org.apache.commons.codec.binary.Base64;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.dao.PersistedLookupMetadataDao;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LookupResultsServiceImpl implements LookupResultsService {
    private BusinessObjectService businessObjectService;
    private PersistedLookupMetadataDao persistedLookupMetadataDao;
    private LookupResultsSupportStrategyService persistableBusinessObjectSupportStrategy;
    private LookupResultsSupportStrategyService dataDictionarySupportStrategy;
    
    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#persistResultsTable(java.lang.String, java.util.List, java.lang.String)
     */
    public void persistResultsTable(String lookupResultsSequenceNumber, List<ResultRow> resultTable, String personId) throws Exception {
        String resultTableString = new String(Base64.encodeBase64(ObjectUtils.toByteArray(resultTable)));
        
        Timestamp now = CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp();
        
        LookupResults lookupResults = retrieveLookupResults(lookupResultsSequenceNumber);
        if (lookupResults == null) {
            lookupResults = new LookupResults();
            lookupResults.setLookupResultsSequenceNumber(lookupResultsSequenceNumber);
        }
        lookupResults.setLookupResultsSequenceNumber(lookupResultsSequenceNumber);
        lookupResults.setLookupPersonId(personId);
        lookupResults.setSerializedLookupResults(resultTableString);
        lookupResults.setLookupDate(now);
        businessObjectService.save(lookupResults);
    }

    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#persistSelectedObjectIds(java.lang.String, java.util.Set, java.lang.String)
     */
    public void persistSelectedObjectIds(String lookupResultsSequenceNumber, Set<String> selectedObjectIds, String personId) throws Exception {
        SelectedObjectIds selectedObjectIdsBO = retrieveSelectedObjectIds(lookupResultsSequenceNumber);
        if (selectedObjectIdsBO == null) {
            selectedObjectIdsBO = new SelectedObjectIds();
            selectedObjectIdsBO.setLookupResultsSequenceNumber(lookupResultsSequenceNumber);
        }
        selectedObjectIdsBO.setLookupResultsSequenceNumber(lookupResultsSequenceNumber);
        selectedObjectIdsBO.setLookupPersonId(personId);
        selectedObjectIdsBO.setSelectedObjectIds(
                LookupUtils.convertSetOfObjectIdsToString(selectedObjectIds));
        selectedObjectIdsBO.setLookupDate(CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        businessObjectService.save(selectedObjectIdsBO);
    }

    /**
     * Retrieves the LookupResults BO with the given sequence number.  Does not check authentication.
     * @param lookupResultsSequenceNumber
     * @return
     * @throws Exception
     */
    protected LookupResults retrieveLookupResults(String lookupResultsSequenceNumber) throws Exception {
        Map<String, String> queryCriteria = new HashMap<String, String>();
        queryCriteria.put(KRADConstants.LOOKUP_RESULTS_SEQUENCE_NUMBER, lookupResultsSequenceNumber);
        LookupResults lookupResults = (LookupResults) businessObjectService.findByPrimaryKey(LookupResults.class, queryCriteria);
        
        return lookupResults;
    }

    /**
     * Retrieves the SelectedObjectIds BO with the given sequence number.  Does not check authentication.
     * @param lookupResultsSequenceNumber
     * @return
     * @throws Exception
     */
    protected SelectedObjectIds retrieveSelectedObjectIds(String lookupResultsSequenceNumber) throws Exception {
        Map<String, String> queryCriteria = new HashMap<String, String>();
        queryCriteria.put(KRADConstants.LOOKUP_RESULTS_SEQUENCE_NUMBER, lookupResultsSequenceNumber);
        SelectedObjectIds selectedObjectIds = (SelectedObjectIds) businessObjectService.findByPrimaryKey(SelectedObjectIds.class, queryCriteria);
        
        return selectedObjectIds;
    }

    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#isAuthorizedToAccessLookupResults(java.lang.String, java.lang.String)
     */
    public boolean isAuthorizedToAccessLookupResults(String lookupResultsSequenceNumber, String personId) {
        try {
            LookupResults lookupResults = retrieveLookupResults(lookupResultsSequenceNumber);
            return isAuthorizedToAccessLookupResults(lookupResults, personId);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns whether the user ID parameter is allowed to view the results.
     * 
     * @param lookupResults
     * @param personId
     * @return
     */
    protected boolean isAuthorizedToAccessLookupResults(LookupResults lookupResults, String personId) {
        return isAuthorizedToAccessMultipleValueLookupMetadata(lookupResults, personId);
    }

    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#isAuthorizedToAccessSelectedObjectIds(java.lang.String, java.lang.String)
     */
    public boolean isAuthorizedToAccessSelectedObjectIds(String lookupResultsSequenceNumber, String personId) {
        try {
            SelectedObjectIds selectedObjectIds = retrieveSelectedObjectIds(lookupResultsSequenceNumber);
            return isAuthorizedToAccessSelectedObjectIds(selectedObjectIds, personId);
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns whether the user ID parameter is allowed to view the selected object IDs
     * 
     * @param selectedObjectIds
     * @param personId
     * @return
     */
    protected boolean isAuthorizedToAccessSelectedObjectIds(SelectedObjectIds selectedObjectIds, String personId) {
        return isAuthorizedToAccessMultipleValueLookupMetadata(selectedObjectIds, personId);
    }
    

    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#retrieveResultsTable(java.lang.String, java.lang.String)
     */
    public List<ResultRow> retrieveResultsTable(String lookupResultsSequenceNumber, String personId) throws Exception {
        LookupResults lookupResults = retrieveLookupResults(lookupResultsSequenceNumber);
        if (!isAuthorizedToAccessLookupResults(lookupResults, personId)) {
            // TODO: use the other identifier
            throw new AuthorizationException(personId, "retrieve lookup results", "lookup sequence number " + lookupResultsSequenceNumber);
        }
        List<ResultRow> resultTable = (List<ResultRow>) ObjectUtils.fromByteArray(Base64.decodeBase64(lookupResults.getSerializedLookupResults().getBytes()));
        return resultTable;
    }

    /**
     * Figures out which support strategy to defer to and uses that service to retrieve the results; if the bo class doesn't qualify with any support strategy, an exception is thrown.  A nasty one, too.
     * 
     * @see org.kuali.rice.krad.lookup.LookupResultsService#retrieveSelectedResultBOs(java.lang.String, java.lang.Class, java.lang.String)
     */
    public <T extends BusinessObject> Collection<T> retrieveSelectedResultBOs(String lookupResultsSequenceNumber, Class<T> boClass, String personId) throws Exception {
    	final LookupResultsSupportStrategyService supportService = getQualifingSupportStrategy(boClass);
    	if (supportService == null) {
    		throw new RuntimeException("BusinessObject class "+boClass.getName()+" cannot be used within a multiple value lookup; it either needs to be a PersistableBusinessObject or have both its primary keys and a lookupable defined in its data dictionary entry");
    	}
    	
    	SelectedObjectIds selectedObjectIds = retrieveSelectedObjectIds(lookupResultsSequenceNumber);
        
        if (!isAuthorizedToAccessSelectedObjectIds(selectedObjectIds, personId)) {
            // TODO: use the other identifier
            throw new AuthorizationException(personId, "retrieve lookup results", "lookup sequence number " + lookupResultsSequenceNumber);
        }
        
        Set<String> setOfSelectedObjIds = LookupUtils
                .convertStringOfObjectIdsToSet(selectedObjectIds.getSelectedObjectIds());
        
        if (setOfSelectedObjIds.isEmpty()) {
            // OJB throws exception if querying on empty set
            return new ArrayList<T>();
        }
    	
    	return supportService.retrieveSelectedResultBOs(boClass, setOfSelectedObjIds);
    }
    
    /**
     * Given the business object class, determines the best qualifying LookupResultsSupportStrategyService to use
     * 
     * @param boClass a business object class
     * @return an LookupResultsSupportStrategyService implementation, or null if no qualifying strategies could be found
     */
    protected LookupResultsSupportStrategyService getQualifingSupportStrategy(Class boClass) {
    	if (getPersistableBusinessObjectSupportStrategy().qualifiesForStrategy(boClass)) {
    		return getPersistableBusinessObjectSupportStrategy();
    	} else if (getDataDictionarySupportStrategy().qualifiesForStrategy(boClass)) {
    		return getDataDictionarySupportStrategy();
    	}
    	return null;
    }
    
    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#clearPersistedLookupResults(java.lang.String)
     */
    public void clearPersistedLookupResults(String lookupResultsSequenceNumber) throws Exception {
        LookupResults lookupResults = retrieveLookupResults(lookupResultsSequenceNumber);
        if (lookupResults != null) {
            businessObjectService.delete(lookupResults);
        }
    }
    
    /**
     * @see org.kuali.rice.krad.lookup.LookupResultsService#clearPersistedSelectedObjectIds(java.lang.String)
     */
    public void clearPersistedSelectedObjectIds(String lookupResultsSequenceNumber) throws Exception {
        SelectedObjectIds selectedObjectIds = retrieveSelectedObjectIds(lookupResultsSequenceNumber);
        if (selectedObjectIds != null) {
            businessObjectService.delete(selectedObjectIds);
        }
    }
    
    /**
	 * Figures out which LookupResultsServiceSupportStrategy to defer to, and uses that to get the lookup id
	 * @see org.kuali.rice.krad.lookup.LookupResultsService#getLookupId(org.kuali.rice.krad.bo.BusinessObject)
	 */
	public String getLookupId(BusinessObject businessObject) {
		final LookupResultsSupportStrategyService supportService = getQualifingSupportStrategy(businessObject.getClass());
		if (supportService == null) {
			return null; // this may happen quite often, so let's just return null - no exception here
		}
		return supportService.getLookupIdForBusinessObject(businessObject);
	}

	public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
    
    /**
     * Determines whether the passed in user ID is allowed to view the lookup metadata (object IDs or results table)
     * @param mvlm
     * @param personId
     * @return
     */
    protected boolean isAuthorizedToAccessMultipleValueLookupMetadata(MultipleValueLookupMetadata mvlm, String personId) {
        return personId.equals(mvlm.getLookupPersonId());
    }

    
    public void deleteOldLookupResults(Timestamp expirationDate) {
        persistedLookupMetadataDao.deleteOldLookupResults(expirationDate);
        
    }

    public void deleteOldSelectedObjectIds(Timestamp expirationDate) {
        persistedLookupMetadataDao.deleteOldSelectedObjectIds(expirationDate);
    }

    public PersistedLookupMetadataDao getPersistedLookupMetadataDao() {
        return persistedLookupMetadataDao;
    }

    public void setPersistedLookupMetadataDao(PersistedLookupMetadataDao persistedLookupMetadataDao) {
        this.persistedLookupMetadataDao = persistedLookupMetadataDao;
    }

	/**
	 * @return the persistableBusinessObjectSupportStrategy
	 */
	public LookupResultsSupportStrategyService getPersistableBusinessObjectSupportStrategy() {
		return this.persistableBusinessObjectSupportStrategy;
	}

	/**
	 * @return the dataDictionarySupportStrategy
	 */
	public LookupResultsSupportStrategyService getDataDictionarySupportStrategy() {
		return this.dataDictionarySupportStrategy;
	}

	/**
	 * @param persistableBusinessObjectSupportStrategy the persistableBusinessObjectSupportStrategy to set
	 */
	public void setPersistableBusinessObjectSupportStrategy(
			LookupResultsSupportStrategyService persistableBusinessObjectSupportStrategy) {
		this.persistableBusinessObjectSupportStrategy = persistableBusinessObjectSupportStrategy;
	}

	/**
	 * @param dataDictionarySupportStrategy the dataDictionarySupportStrategy to set
	 */
	public void setDataDictionarySupportStrategy(
			LookupResultsSupportStrategyService dataDictionarySupportStrategy) {
		this.dataDictionarySupportStrategy = dataDictionarySupportStrategy;
	}
    
}

