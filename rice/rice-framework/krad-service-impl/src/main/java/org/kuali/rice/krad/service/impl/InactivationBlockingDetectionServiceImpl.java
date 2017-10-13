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
package org.kuali.rice.krad.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.InactivationBlockingDetectionService;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Performs checking of inactivation blocking 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Transactional
public class InactivationBlockingDetectionServiceImpl implements InactivationBlockingDetectionService {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InactivationBlockingDetectionServiceImpl.class);

    protected DataObjectMetaDataService dataObjectMetaDataService;
    protected BusinessObjectService businessObjectService;
    
    /**
     * Note we are checking the active getting after retrieving potential blocking records instead of setting criteria on the
	 * active field. This is because some implementations of {@link org.kuali.rice.core.api.mo.common.active.MutableInactivatable} might not have the active field, for example
	 * instances of {@link org.kuali.rice.krad.bo.InactivatableFromTo}
	 * 
     * @see org.kuali.rice.krad.service.InactivationBlockingDetectionService#listAllBlockerRecords(org.kuali.rice.krad.datadictionary.InactivationBlockingDefinition)
     * @see org.kuali.rice.core.api.mo.common.active.MutableInactivatable
     */
    @SuppressWarnings("unchecked")
	public Collection<BusinessObject> listAllBlockerRecords(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata) {
		Collection<BusinessObject> blockingRecords = new ArrayList<BusinessObject>();

		Map<String, String> queryMap = buildInactivationBlockerQueryMap(blockedBo, inactivationBlockingMetadata);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Checking for blocker records for object: " + blockedBo);
			LOG.debug("    With Metadata: " + inactivationBlockingMetadata);
			LOG.debug("    Resulting Query Map: " + queryMap);
		}

		if (queryMap != null) {
			Collection potentialBlockingRecords = businessObjectService.findMatching(
					inactivationBlockingMetadata.getBlockingReferenceBusinessObjectClass(), queryMap);
			for (Iterator iterator = potentialBlockingRecords.iterator(); iterator.hasNext();) {
				MutableInactivatable businessObject = (MutableInactivatable) iterator.next();
				if (businessObject.isActive()) {
					blockingRecords.add((BusinessObject) businessObject);
				}
			}
		}

		return blockingRecords;
	}

	/**
	 * Note we are checking the active getting after retrieving potential blocking records instead of setting criteria on the
	 * active field. This is because some implementations of {@link org.kuali.rice.core.api.mo.common.active.MutableInactivatable} might not have the active field, for example
	 * instances of {@link org.kuali.rice.krad.bo.InactivatableFromTo}
	 * 
	 * @see org.kuali.rice.krad.service.InactivationBlockingDetectionService#hasABlockingRecord(org.kuali.rice.krad.bo.BusinessObject,
	 *      org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata)
	 * @see org.kuali.rice.core.api.mo.common.active.MutableInactivatable
	 */
	public boolean hasABlockingRecord(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata) {
		boolean hasBlockingRecord = false;

		Map<String, String> queryMap = buildInactivationBlockerQueryMap(blockedBo, inactivationBlockingMetadata);
		if (queryMap != null) {
			Collection potentialBlockingRecords = businessObjectService.findMatching(
					inactivationBlockingMetadata.getBlockingReferenceBusinessObjectClass(), queryMap);
			for (Iterator iterator = potentialBlockingRecords.iterator(); iterator.hasNext();) {
				MutableInactivatable businessObject = (MutableInactivatable) iterator.next();
				if (businessObject.isActive()) {
					hasBlockingRecord = true;
					break;
				}
			}
		}

		// if queryMap were null, means that we couldn't perform a query, and hence, need to return false
		return hasBlockingRecord;
	}

	protected Map<String, String> buildInactivationBlockerQueryMap(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata) {
		BusinessObject blockingBo = (BusinessObject) ObjectUtils.createNewObjectFromClass(inactivationBlockingMetadata
				.getBlockingReferenceBusinessObjectClass());

		DataObjectRelationship dataObjectRelationship = dataObjectMetaDataService
				.getDataObjectRelationship(blockingBo, blockedBo.getClass(),
                        inactivationBlockingMetadata.getBlockedReferencePropertyName(), "", true, false, false);

		// note, this method assumes that all PK fields of the blockedBo have a non-null and, for strings, non-blank values
		if (dataObjectRelationship != null) {
			Map<String, String> parentToChildReferences = dataObjectRelationship.getParentToChildReferences();
			Map<String, String> queryMap = new HashMap<String, String>();
			for (Map.Entry<String, String> parentToChildReference : parentToChildReferences.entrySet()) {
				String fieldName = parentToChildReference.getKey();
				Object fieldValue = ObjectUtils.getPropertyValue(blockedBo, parentToChildReference.getValue());
				if (fieldValue != null && StringUtils.isNotBlank(fieldValue.toString())) {
					queryMap.put(fieldName, fieldValue.toString());
				} else {
					LOG.error("Found null value for foreign key field " + fieldName
							+ " while building inactivation blocking query map.");
					throw new RuntimeException("Found null value for foreign key field '" + fieldName
							+ "' while building inactivation blocking query map.");
				}
			}

			return queryMap;
		}

		return null;
	}
    
    public void setDataObjectMetaDataService(DataObjectMetaDataService dataObjectMetaDataService) {
        this.dataObjectMetaDataService = dataObjectMetaDataService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

}
