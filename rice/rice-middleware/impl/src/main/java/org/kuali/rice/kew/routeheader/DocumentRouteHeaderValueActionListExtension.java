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
package org.kuali.rice.kew.routeheader;

import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


/**
 * An extension of {@link DocumentRouteHeaderValue} which is mapped to OJB to help
 * with optimization of the loading of a user's Action List.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
//@Entity
//@Table(name="KREW_DOC_HDR_T")
@MappedSuperclass
public class DocumentRouteHeaderValueActionListExtension extends DocumentRouteHeaderValue {

	private static final long serialVersionUID = 8458532812557846684L;

    @Transient
	private String initiatorName = "";
    @Transient
    private boolean isInitiatorNameInitialized = false;

    public void initialize(Preferences preferences) {
        if (KewApiConstants.PREFERENCES_YES_VAL.equals(preferences.getShowInitiator())) {
            initializeInitiatorName();
        }
    }

    public String getInitiatorName() {
        initializeInitiatorName();
        return initiatorName;
    }

    /**
     * Fetches the initiator name, masked appropriately if restricted.
     */
    private void initializeInitiatorName() {
        if (!isInitiatorNameInitialized) {
            EntityNamePrincipalName name = KimApiServiceLocator.getIdentityService().getDefaultNamesForPrincipalId(getInitiatorPrincipalId());
            if (name != null) {
                this.initiatorName = name.getDefaultName().getCompositeName();
            }
            isInitiatorNameInitialized = true;
        }
    }

}

