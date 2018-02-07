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
package org.kuali.rice.kew.impl.actionlist;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.action.ActionItemCustomization;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationHandlerService;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationMediator;
import org.kuali.rice.kew.rule.bo.RuleAttribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Internal utility class that partitions ActionItems by application id, and calls the appropriate
 * {@link ActionListCustomizationHandlerService} for each parition to retrieve any customizations.
 */
public class ActionListCustomizationMediatorImpl implements ActionListCustomizationMediator {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ActionListCustomizationMediatorImpl.class);

    private DocumentTypeService documentTypeService;
    private ActionListCustomizationHandlerServiceChooser actionListCustomizationHandlerServiceChooser =
            new ActionListCustomizationHandlerServiceChooser();

    /**
     * <p>partitions ActionItems by application id, and calls the appropriate
     * {@link ActionListCustomizationHandlerService} for each parition, merging the results.</p>
     *
     * <dl><dt><b>inherited docs:</b></dt><dd>{@inheritDoc}</dd></dl>
     */
    @Override
    public Map<String, ActionItemCustomization> getActionListCustomizations(String principalId,
            List<ActionItem> actionItems) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("invalid principalId: " + principalId);
        }
        if (actionItems == null) {
            actionItems = Collections.emptyList();
        }

        // map from action item ID to ActionItemCustomization
        Map<String, ActionItemCustomization> results = new HashMap<String, ActionItemCustomization>();

        // group each action item by application id that needs to be called for action list customizations (note that
        // the application id comes from the extension/rule attribute record, most action lists will have doc types
        // with no custom action list attribute, though the default still needs to be run in this case)

        ListMultimap<String, ActionItem> itemsByApplicationId = ArrayListMultimap.create();

        for (ActionItem actionItem : actionItems) {
            //DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(actionItem.getDocName());
            DocumentType docType = getDocumentTypeService().findByName(actionItem.getDocName());
            if (docType == null) {
                LOG.error(String.format("Action item %s has an invalid document type name of %s",
                        actionItem.getId(), actionItem.getDocName()));
                // OK to have a null key, this represents the default app id
                itemsByApplicationId.put(null, actionItem);
            } else {
                // OK to have a null key, this represents the default app id
                itemsByApplicationId.put(getActionListCustomizationApplicationId(docType), actionItem);
            }
        }

        // For each application id, pass all action items which might need to be customized (because they have a
        // document type, which declares an action list attribute, which has an application id declared) to the
        // appropriate ActionListCustomizationHandlerService endpoint

        for (String applicationId : itemsByApplicationId.keySet()) {
            ActionListCustomizationHandlerService actionListCustomizationHandler =
                    getActionListCustomizationHandlerServiceChooser().getByApplicationId(applicationId);

            if (actionListCustomizationHandler == null) {
                // get the local ActionListCustomizationHandlerService as a fallback
                actionListCustomizationHandler =
                        getActionListCustomizationHandlerServiceChooser().getByApplicationId(null);
            }

            List<ActionItemCustomization> customizations =
                    actionListCustomizationHandler.customizeActionList(principalId, itemsByApplicationId.get(
                            applicationId));


            // Get back the customized results and reassemble with customized results from all different application
            // customizations (as well as default customizations)
            if (customizations != null) for (ActionItemCustomization customization : customizations) {
                results.put(customization.getActionItemId(), customization);
            }
        }

        return results;
    }

    // CustomActionListAttributes are configured in RuleAttributes, so that is the
    // applicationId we need to use
    private String getActionListCustomizationApplicationId(DocumentType docType) {
        String applicationId = null;
        RuleAttribute ruleAttribute = docType.getCustomActionListRuleAttribute();
        if (ruleAttribute != null) {
            applicationId = ruleAttribute.getApplicationId();
        }
        // we may return null
        return applicationId;
    }

    public DocumentTypeService getDocumentTypeService() {
        return documentTypeService;
    }

    public void setDocumentTypeService(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    /**
     * Need this to make our class testable without having to wire the universe up through spring.
     */
    public static class ActionListCustomizationHandlerServiceChooser {
        public ActionListCustomizationHandlerService getByApplicationId(String applicationId) {
            return KewFrameworkServiceLocator.getActionListCustomizationHandlerService(applicationId);
        }
    }

    public ActionListCustomizationHandlerServiceChooser getActionListCustomizationHandlerServiceChooser() {
        return actionListCustomizationHandlerServiceChooser;
    }

    public void setActionListCustomizationHandlerServiceChooser(
            ActionListCustomizationHandlerServiceChooser actionListCustomizationHandlerServiceChooser) {
        this.actionListCustomizationHandlerServiceChooser = actionListCustomizationHandlerServiceChooser;
    }
}
