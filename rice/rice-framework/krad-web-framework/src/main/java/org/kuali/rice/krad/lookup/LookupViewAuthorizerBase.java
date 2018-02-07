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
package org.kuali.rice.krad.lookup;

import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewAuthorizerBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.Map;

/**
 * Implementation of {@link org.kuali.rice.krad.uif.view.ViewAuthorizer} for
 * {@link org.kuali.rice.krad.uif.view.LookupView} instances
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupViewAuthorizerBase extends ViewAuthorizerBase {
    private static final long serialVersionUID = 3755133641536256283L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupViewAuthorizerBase.class);

    /**
     * Override to check the for permissions of type 'Look Up Records' in addition to the open view check
     * done in super
     */
    @Override
    public boolean canOpenView(View view, ViewModel model, Person user) {
        boolean canOpen = super.canOpenView(view, model, user);

        if (canOpen) {
            LookupForm lookupForm = (LookupForm) model;

            Map<String, String> additionalPermissionDetails;
            try {
                additionalPermissionDetails = KRADUtils.getNamespaceAndComponentSimpleName(Class.forName(
                        lookupForm.getDataObjectClassName()));
            } catch (ClassNotFoundException e) {
                throw new RiceRuntimeException(
                        "Unable to create class for lookup class name: " + lookupForm.getDataObjectClassName());
            }

            if (permissionExistsByTemplate(model, KRADConstants.KNS_NAMESPACE,
                    KimConstants.PermissionTemplateNames.LOOK_UP_RECORDS, additionalPermissionDetails)) {
                canOpen = isAuthorizedByTemplate(model, KRADConstants.KNS_NAMESPACE,
                        KimConstants.PermissionTemplateNames.LOOK_UP_RECORDS, user.getPrincipalId(),
                        additionalPermissionDetails, null);
            }
        }

        return canOpen;
    }

    /**
     * Check if user is allowed to initiate the document
     *
     * @param lookupForm - The lookup form of the document
     * @param user - user we are authorizing the actions for
     * @return true if user is authorized to initiate the document, false otherwise
     */
    public boolean canInitiateDocument(LookupForm lookupForm, Person user) {
        boolean canInitiateDocument = false;

        try {
            Class<?> dataObjectClass = Class.forName(lookupForm.getDataObjectClassName());
            // check if creating documents is allowed
            String documentTypeName = KRADServiceLocatorWeb.getDocumentDictionaryService()
                    .getMaintenanceDocumentTypeName(dataObjectClass);
            if ((documentTypeName != null) &&
                    KRADServiceLocatorWeb.getDocumentDictionaryService().getDocumentAuthorizer(documentTypeName)
                            .canInitiate(documentTypeName, user)) {
                canInitiateDocument = true;
            }
        } catch (ClassNotFoundException e) {
            LOG.warn("Unable to load Data Object Class: " + lookupForm.getDataObjectClassName(), e);
        }

        return canInitiateDocument;
    }
}
