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
package org.kuali.rice.kew.service.impl;

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.framework.responsibility.ResponsibilityTypeService;
import org.kuali.rice.kns.kim.responsibility.KimResponsibilityTypeServiceBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypeResponsibilityTypeServiceImpl extends
        KimResponsibilityTypeServiceBase implements ResponsibilityTypeService {
	DocumentTypeService documentTypeService;
	protected String exactMatchStringAttributeName;

    @Override
    protected List<String> getRequiredAttributes() {
        final List<String> attrs = new ArrayList<String>(super.getRequiredAttributes());
        attrs.add(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME);
        return Collections.unmodifiableList(attrs);
    }

    @Override
    protected boolean isCheckRequiredAttributes() {
        return true;
    }
	
	@Override
	protected List<Responsibility> performResponsibilityMatches(
			Map<String, String> requestedDetails,
			List<Responsibility> responsibilitiesList) {
		Map<String, List<Responsibility>> potentialDocumentTypeMatches = new HashMap<String, List<Responsibility>>();
		for (Responsibility responsibility : responsibilitiesList) {
			if ((exactMatchStringAttributeName == null)
					|| responsibility
							.getAttributes()
							.get(exactMatchStringAttributeName)
							.equals(
									requestedDetails
											.get(exactMatchStringAttributeName))) {
				if (!potentialDocumentTypeMatches.containsKey(responsibility
						.getAttributes().get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME))) {
					potentialDocumentTypeMatches.put(
							responsibility.getAttributes().get(
									KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME),
							new ArrayList<Responsibility>());
				}
				potentialDocumentTypeMatches.get(
						responsibility.getAttributes().get(
								KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME)).add(
						responsibility);
			}
		}
		List<Responsibility> matchingResponsibilities = new ArrayList<Responsibility>();
		if (potentialDocumentTypeMatches.containsKey(requestedDetails
				.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME))) {
			matchingResponsibilities
					.addAll(potentialDocumentTypeMatches.get(requestedDetails
							.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME)));
		} else {
			String closestParentDocumentTypeName = getClosestParentDocumentTypeName(
					getDocumentTypeService().getDocumentTypeByName(
							requestedDetails
									.get(KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME)),
					potentialDocumentTypeMatches.keySet());
			if (closestParentDocumentTypeName != null) {
				matchingResponsibilities.addAll(potentialDocumentTypeMatches
						.get(closestParentDocumentTypeName));
			}
		}
		return matchingResponsibilities;
	}

	public DocumentTypeService getDocumentTypeService() {
		if (documentTypeService == null) {
			documentTypeService = KewApiServiceLocator.getDocumentTypeService();
		}
		return this.documentTypeService;
	}
}
