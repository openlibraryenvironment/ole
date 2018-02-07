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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.document.authorization.FieldRestriction;
import org.kuali.rice.kns.service.BusinessObjectAuthorizationService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata;
import org.kuali.rice.krad.datadictionary.mask.MaskFormatter;
import org.kuali.rice.krad.service.*;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * This is a description of what this class does - wliang don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class InactivationBlockingDisplayServiceImpl implements InactivationBlockingDisplayService {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(InactivationBlockingDetectionServiceImpl.class);
	
	private PersistenceService persistenceService;
	private DataDictionaryService dataDictionaryService;
	private PersistenceStructureService persistenceStructureService;
	private BusinessObjectAuthorizationService businessObjectAuthorizationService;
	
	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.service.InactivationBlockingDisplayService#listAllBlockerRecords(org.kuali.rice.krad.bo.BusinessObject, org.kuali.rice.krad.datadictionary.InactivationBlockingMetadata)
	 */
	public List<String> listAllBlockerRecords(BusinessObject blockedBo, InactivationBlockingMetadata inactivationBlockingMetadata) {
        String inactivationBlockingDetectionServiceBeanName = inactivationBlockingMetadata.getInactivationBlockingDetectionServiceBeanName();
        if (StringUtils.isBlank(inactivationBlockingDetectionServiceBeanName)) {
            inactivationBlockingDetectionServiceBeanName = KRADServiceLocatorWeb.DEFAULT_INACTIVATION_BLOCKING_DETECTION_SERVICE;
        }
        InactivationBlockingDetectionService inactivationBlockingDetectionService = KRADServiceLocatorWeb
                .getInactivationBlockingDetectionService(inactivationBlockingDetectionServiceBeanName);

        Collection<BusinessObject> collection = inactivationBlockingDetectionService.listAllBlockerRecords(blockedBo, inactivationBlockingMetadata);

        Map<String, Formatter> formatters = getFormattersForPrimaryKeyFields(inactivationBlockingMetadata.getBlockingReferenceBusinessObjectClass());

        List<String> displayValues = new ArrayList<String>();
        List<String> pkFieldNames = persistenceStructureService.listPrimaryKeyFieldNames(inactivationBlockingMetadata.getBlockingReferenceBusinessObjectClass());
        Person user = GlobalVariables.getUserSession().getPerson();
        
        for (BusinessObject element : collection) {
        	StringBuilder buf = new StringBuilder();

        	// the following method will return a restriction for all DD-defined attributes
        	BusinessObjectRestrictions businessObjectRestrictions = getBusinessObjectAuthorizationService().getLookupResultRestrictions(element, user);
        	for (int i = 0; i < pkFieldNames.size(); i++) {
        		String pkFieldName = pkFieldNames.get(i);
        		Object value = ObjectUtils.getPropertyValue(element, pkFieldName);

        		String displayValue = null;
        		if (!businessObjectRestrictions.hasRestriction(pkFieldName)) {
        			Formatter formatter = formatters.get(pkFieldName);
        			if (formatter != null) {
        				displayValue = (String) formatter.format(value);
        			}
        			else {
        				displayValue = String.valueOf(value);
        			}
        		}
        		else {
        			FieldRestriction fieldRestriction = businessObjectRestrictions.getFieldRestriction(pkFieldName);
        			if (fieldRestriction.isMasked() || fieldRestriction.isPartiallyMasked()) {
		    			MaskFormatter maskFormatter = fieldRestriction.getMaskFormatter();
						displayValue = maskFormatter.maskValue(value);
        			}
        			else {
        				// there was a restriction, but we did not know how to obey it.
        				LOG.warn("Restriction was defined for class: " + element.getClass() + " field name: " + pkFieldName + ", but it was not honored by the inactivation blocking display framework");
        			}
        		}

        		buf.append(displayValue);
        		if (i < pkFieldNames.size() - 1) {
        			buf.append(" - ");
        		}
        	}

        	displayValues.add(buf.toString());
        }
		return displayValues;
	}

	protected Map<String, Formatter> getFormattersForPrimaryKeyFields(Class boClass) {
		List<String> keyNames = persistenceStructureService.listPrimaryKeyFieldNames(boClass);
		Map<String, Formatter> formattersForPrimaryKeyFields = new HashMap<String, Formatter>();

		for (String pkFieldName : keyNames) {
			Formatter formatter = null;

			Class<? extends Formatter> formatterClass = dataDictionaryService.getAttributeFormatter(boClass, pkFieldName);
			if (formatterClass != null) {
				try {
					formatter = formatterClass.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        }
		return formattersForPrimaryKeyFields;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setPersistenceStructureService(
			PersistenceStructureService persistenceStructureService) {
		this.persistenceStructureService = persistenceStructureService;
	}

	public void setDataDictionaryService(DataDictionaryService dataDictionaryService) {
		this.dataDictionaryService = dataDictionaryService;
	}
	
	protected BusinessObjectAuthorizationService getBusinessObjectAuthorizationService() {
		if (businessObjectAuthorizationService == null) {
			businessObjectAuthorizationService = KNSServiceLocator.getBusinessObjectAuthorizationService();
		}
		return businessObjectAuthorizationService;
	}
}

