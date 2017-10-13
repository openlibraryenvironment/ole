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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.framework.responsibility.ResponsibilityTypeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ReviewResponsibilityTypeServiceImpl extends DocumentTypeResponsibilityTypeServiceImpl implements ResponsibilityTypeService {
	{
		exactMatchStringAttributeName = KimConstants.AttributeConstants.ROUTE_NODE_NAME;
	}

    @Override
    protected List<String> getRequiredAttributes() {
        final List<String> attrs = new ArrayList<String>(super.getRequiredAttributes());
        attrs.add(KimConstants.AttributeConstants.ROUTE_NODE_NAME);
        return Collections.unmodifiableList(attrs);
    }

	@Override
	protected List<Responsibility> performResponsibilityMatches(
			Map<String, String> requestedDetails,
			List<Responsibility> responsibilitiesList) {
		// get the base responsibility matches based on the route level and document type
		List<Responsibility> baseMatches = super.performResponsibilityMatches(requestedDetails,
				responsibilitiesList);
		// now, if any of the responsibilities have the "qualifierResolverProvidedIdentifier" detail property
		// perform an exact match on the property with the requested details
		// if the property does not match or does not exist in the requestedDetails, remove
		// the responsibility from the list
		Iterator<Responsibility> respIter = baseMatches.iterator();
		while ( respIter.hasNext() ) {
			Map<String, String> respDetails = respIter.next().getAttributes();
			if ( respDetails.containsKey( KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER ) && StringUtils.isNotBlank( respDetails.get(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER) ) ) {
				if ( !requestedDetails.containsKey( KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER )
						|| !StringUtils.equals( respDetails.get(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER)
								, requestedDetails.get(KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER))) {
					respIter.remove();
				}
			}
		}		
		return baseMatches;
	}
}
