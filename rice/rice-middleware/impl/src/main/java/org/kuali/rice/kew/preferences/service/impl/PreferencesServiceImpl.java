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
package org.kuali.rice.kew.preferences.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.exception.WorkflowServiceErrorException;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.useroptions.UserOptions;
import org.kuali.rice.kew.useroptions.UserOptionsService;

/**
 * An implementation of the {@link PreferencesService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PreferencesServiceImpl implements PreferencesService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PreferencesServiceImpl.class);

    private static Map<String, String> USER_OPTION_KEY_DEFAULT_MAP;

    static {
        USER_OPTION_KEY_DEFAULT_MAP = new HashMap<String, String>();
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_APPROVED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_CANCELED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_DISAPPROVE_CANCEL, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_DISAPPROVED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_ENROUTE, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_EXCEPTION, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_FINAL, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_INITIATED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_PROCESSED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.COLOR_SAVED, "userOptions.default.color");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.EMAIL_NOTIFICATION, "userOptions.default.email");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_PRIMARY_DELEGATION, "userOptions.default.notifyPrimary");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_SECONDARY_DELEGATION, "userOptions.default.notifySecondary");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.OPEN_NEW_WINDOW, "userOptions.default.openNewWindow");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.PAGE_SIZE, "userOptions.default.actionListSize");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.REFRESH_RATE, "userOptions.default.refreshRate");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_ACTION_REQUESTED, "userOptions.default.showActionRequired");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DATE_CREATED, "userOptions.default.showDateCreated");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DOC_TYPE, "userOptions.default.showDocumentType");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DOCUMENT_STATUS, "userOptions.default.showDocumentStatus");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_INITIATOR, "showInitiator");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DELEGATOR, "userOptions.default.showDelegator");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DOC_TITLE, "userOptions.default.showTitle");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_GROUP_REQUEST, "userOptions.default.showWorkgroupRequest");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_CLEAR_FYI, "userOptions.default.showClearFYI");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.DELEGATOR_FILTER, "userOptions.default.delegatorFilterOnActionList");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.PRIMARY_DELEGATE_FILTER, "userOptions.default.primaryDelegatorFilterOnActionList");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_DATE_APPROVED, "userOptions.default.showLastApprovedDate");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.SHOW_CURRENT_NODE, "userOptions.default.showCurrentNode");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.USE_OUT_BOX, KewApiConstants.USER_OPTIONS_DEFAULT_USE_OUTBOX_PARAM);
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_ACKNOWLEDGE, "userOptions.default.notifyAcknowledge");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_APPROVE, "userOptions.default.notifyApprove");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_COMPLETE, "userOptions.default.notifyComplete");
        USER_OPTION_KEY_DEFAULT_MAP.put(Preferences.KEYS.NOTIFY_FYI, "userOptions.default.notifyFYI");
    }


    public Preferences getPreferences(String principalId) {
        if ( LOG.isDebugEnabled() ) {
        LOG.debug("start preferences fetch user " + principalId);
        }
        Collection<UserOptions> options = getUserOptionService().findByWorkflowUser(principalId);
        Map<String,UserOptions> optionMap = new HashMap<String, UserOptions>();
        Map<String,String> optionValueMap = new HashMap<String, String>();
        Map<String, String> documentTypeNotificationPreferences = new HashMap<String, String>();
        for ( UserOptions option : options ) {
            if(option.getOptionId().endsWith(KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_SUFFIX)) {
                String preferenceName = option.getOptionId();
                preferenceName = StringUtils.substringBeforeLast(preferenceName, KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_SUFFIX);
                documentTypeNotificationPreferences.put(preferenceName, option.getOptionVal());
            } else {
                optionMap.put(option.getOptionId(), option);
            }
        }
        
        ConfigurationService kcs = CoreApiServiceLocator.getKualiConfigurationService();

        boolean isSaveRequired = false;

        for (Map.Entry<String, String> entry : USER_OPTION_KEY_DEFAULT_MAP.entrySet()) {
            String optionKey = entry.getKey();
            String defaultValue = kcs.getPropertyValueAsString(entry.getValue());
            if (LOG.isDebugEnabled()) {
                LOG.debug("start fetch option " + optionKey + " user " + principalId);
            }

            UserOptions option = optionMap.get(optionKey);
            if (option == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("User option '"
                            + optionKey
                            + "' on user "
                            + principalId
                            + " has no stored value.  Preferences will require save.");
                }
                option = new UserOptions();
                option.setWorkflowId(principalId);
                option.setOptionId(optionKey);
                option.setOptionVal(defaultValue);
                optionMap.put(optionKey, option); // just in case referenced a second time

                if (!isSaveRequired) {
                    if (optionKey.equals(Preferences.KEYS.USE_OUT_BOX) && !ConfigContext.getCurrentContextConfig().getOutBoxOn()) {
                        // don't mark as needing save
                    } else {
                        isSaveRequired = true;
                    }
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("End fetch option " + optionKey + " user " + principalId);
            }

            optionValueMap.put(optionKey, option.getOptionVal());
        }

//  TODO: JLR - I'm not sure why this isSaveRequired logic is necessary -- couldn't we do something like the following?
//        if (isSaveRequired)
//            getUserOptionService().save(principalId, optionValueMap);

        return Preferences.Builder.create(optionValueMap, documentTypeNotificationPreferences, isSaveRequired).build();
    }

    public void savePreferences(String principalId, Preferences preferences) {
    	// NOTE: this previously displayed the principalName.  Now it's just the id
    	if ( LOG.isDebugEnabled() ) {
            LOG.debug("saving preferences user " + principalId);
    	}

        validate(preferences);
        Map<String,String> optionsMap = new HashMap<String,String>(50);
        
        optionsMap.put(Preferences.KEYS.COLOR_DISAPPROVE_CANCEL, preferences.getColorDisapproveCancel());
        optionsMap.put(Preferences.KEYS.COLOR_DISAPPROVED, preferences.getColorDisapproved());
        optionsMap.put(Preferences.KEYS.COLOR_APPROVED, preferences.getColorApproved());
        optionsMap.put(Preferences.KEYS.COLOR_CANCELED, preferences.getColorCanceled());
        optionsMap.put(Preferences.KEYS.COLOR_SAVED, preferences.getColorSaved());
        optionsMap.put(Preferences.KEYS.COLOR_ENROUTE, preferences.getColorEnroute());
        optionsMap.put(Preferences.KEYS.COLOR_PROCESSED, preferences.getColorProcessed());
        optionsMap.put(Preferences.KEYS.COLOR_INITIATED, preferences.getColorInitiated());
        optionsMap.put(Preferences.KEYS.COLOR_FINAL, preferences.getColorFinal());
        optionsMap.put(Preferences.KEYS.COLOR_EXCEPTION, preferences.getColorException());
        optionsMap.put(Preferences.KEYS.REFRESH_RATE, preferences.getRefreshRate().trim());
        optionsMap.put(Preferences.KEYS.OPEN_NEW_WINDOW, preferences.getOpenNewWindow());
        optionsMap.put(Preferences.KEYS.SHOW_DOC_TYPE, preferences.getShowDocType());
        optionsMap.put(Preferences.KEYS.SHOW_DOC_TITLE, preferences.getShowDocTitle());
        optionsMap.put(Preferences.KEYS.SHOW_ACTION_REQUESTED, preferences.getShowActionRequested());
        optionsMap.put(Preferences.KEYS.SHOW_INITIATOR, preferences.getShowInitiator());
        optionsMap.put(Preferences.KEYS.SHOW_DELEGATOR, preferences.getShowDelegator());
        optionsMap.put(Preferences.KEYS.SHOW_DATE_CREATED, preferences.getShowDateCreated());
        optionsMap.put(Preferences.KEYS.SHOW_DOCUMENT_STATUS, preferences.getShowDocumentStatus());
        optionsMap.put(Preferences.KEYS.SHOW_APP_DOC_STATUS, preferences.getShowAppDocStatus());
        optionsMap.put(Preferences.KEYS.SHOW_GROUP_REQUEST, preferences.getShowWorkgroupRequest());
        optionsMap.put(Preferences.KEYS.SHOW_CLEAR_FYI, preferences.getShowClearFyi());
        optionsMap.put(Preferences.KEYS.PAGE_SIZE, preferences.getPageSize().trim());
        optionsMap.put(Preferences.KEYS.EMAIL_NOTIFICATION, preferences.getEmailNotification());
        optionsMap.put(Preferences.KEYS.NOTIFY_PRIMARY_DELEGATION, preferences.getNotifyPrimaryDelegation());
        optionsMap.put(Preferences.KEYS.NOTIFY_SECONDARY_DELEGATION, preferences.getNotifySecondaryDelegation());
        optionsMap.put(Preferences.KEYS.DELEGATOR_FILTER, preferences.getDelegatorFilter());
        optionsMap.put(Preferences.KEYS.PRIMARY_DELEGATE_FILTER, preferences.getPrimaryDelegateFilter());
        optionsMap.put(Preferences.KEYS.SHOW_DATE_APPROVED, preferences.getShowDateApproved());
        optionsMap.put(Preferences.KEYS.SHOW_CURRENT_NODE, preferences.getShowCurrentNode());
        optionsMap.put(Preferences.KEYS.NOTIFY_ACKNOWLEDGE, preferences.getNotifyAcknowledge());
        optionsMap.put(Preferences.KEYS.NOTIFY_APPROVE, preferences.getNotifyApprove());
        optionsMap.put(Preferences.KEYS.NOTIFY_COMPLETE, preferences.getNotifyComplete());
        optionsMap.put(Preferences.KEYS.NOTIFY_FYI, preferences.getNotifyFYI());
        if (ConfigContext.getCurrentContextConfig().getOutBoxOn()) {
            optionsMap.put(Preferences.KEYS.USE_OUT_BOX, preferences.getUseOutbox());
        }
        for(Entry<String, String> documentTypePreference : preferences.getDocumentTypeNotificationPreferences().entrySet()) {
            optionsMap.put(documentTypePreference.getKey() + KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_SUFFIX, documentTypePreference.getValue());
        }
        getUserOptionService().save(principalId, optionsMap);
        
        // Find which document type notification preferences have been deleted
        // and remove them from the database
        Preferences storedPreferences = this.getPreferences(principalId);
        for(Entry<String, String> storedEntry : storedPreferences.getDocumentTypeNotificationPreferences().entrySet()) {
            if(preferences.getDocumentTypeNotificationPreference(storedEntry.getKey()) == null) {
                getUserOptionService().deleteUserOptions(getUserOptionService().findByOptionId(storedEntry.getKey() + KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_SUFFIX, principalId));
            }
        }
        if ( LOG.isDebugEnabled() ) {
        LOG.debug("saved preferences user " + principalId);
    }
    }

    private void validate(Preferences preferences) {
        LOG.debug("validating preferences");
        
        Collection errors = new ArrayList();
        try {
            new Integer(preferences.getRefreshRate().trim());
        } catch (NumberFormatException e) {
            errors.add(new WorkflowServiceErrorImpl("ActionList Refresh Rate must be in whole " +
                    "minutes", Preferences.KEYS.ERR_KEY_REFRESH_RATE_WHOLE_NUM));
        } catch (NullPointerException e1) {
            errors.add(new WorkflowServiceErrorImpl("ActionList Refresh Rate must be in whole " +
                    "minutes", Preferences.KEYS.ERR_KEY_REFRESH_RATE_WHOLE_NUM));
        }

        try {
            if(new Integer(preferences.getPageSize().trim()) == 0){
            	errors.add(new WorkflowServiceErrorImpl("ActionList Page Size must be non-zero ",
                        Preferences.KEYS.ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM));
            }            
        } catch (NumberFormatException e) {
            errors.add(new WorkflowServiceErrorImpl("ActionList Page Size must be in whole " +
                    "minutes", Preferences.KEYS.ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM));
        } catch (NullPointerException e1) {
            errors.add(new WorkflowServiceErrorImpl("ActionList Page Size must be in whole " +
                    "minutes", Preferences.KEYS.ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM));
        }
      
        LOG.debug("end validating preferences");
        if (! errors.isEmpty()) {
            throw new WorkflowServiceErrorException("Preference Validation Error", errors);
        }
    }

    public UserOptionsService getUserOptionService() {
        return (UserOptionsService) KEWServiceLocator.getService(
                KEWServiceLocator.USER_OPTIONS_SRV);
    }

    private final class UserOptionsWrapper {

        private final UserOptions userOptions;
        private final boolean isSaveRequired;

        public UserOptionsWrapper(UserOptions userOptions, boolean isSaveRequired) {
            this.userOptions = userOptions;
            this.isSaveRequired = isSaveRequired;
        }

        public UserOptions getUserOptions() {
            return userOptions;
        }

        public boolean isSaveRequired() {
            return isSaveRequired;
        }
    }
}


