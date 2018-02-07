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
package org.kuali.rice.kew.preferences.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.preferences.web.PreferencesConstants;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;


/**
 * Struts ActionForm for {@link PreferencesAction}.
 *
 * @see PreferencesAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PreferencesForm extends KualiForm {

    private static final long serialVersionUID = 4536869031291955777L;
    private static final String ERR_KEY_REFRESH_RATE_WHOLE_NUM = "preferences.refreshRate";
    private static final String ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM = "preferences.pageSize";
	private Preferences.Builder preferences;
    private String methodToCall = "";
    private String returnMapping;
    private boolean showOutbox = true;
    private String documentTypePreferenceName;
    private String documentTypePreferenceValue;

    // KULRICE-3137: Added a backLocation parameter similar to the one from lookups.
    private String backLocation;
    
	public String getReturnMapping() {
        return returnMapping;
    }
    public void setReturnMapping(String returnMapping) {
        this.returnMapping = returnMapping;
    }
    public PreferencesForm() {
        preferences = Preferences.Builder.create();
    }
    public String getMethodToCall() {
        return methodToCall;
    }
    public void setMethodToCall(String methodToCall) {
        Pattern p = Pattern.compile("\\w");
        if (!StringUtils.isBlank(methodToCall)) {
            Matcher m = p.matcher(methodToCall);
            if (m.find()) {
                this.methodToCall = methodToCall;
            } else {
                throw new RiceRuntimeException("invalid characters found in the parameter methodToCall");
            }
        } else {
            this.methodToCall = methodToCall;
        }
    }
    public Preferences.Builder getPreferences() {
        return preferences;
    }
    public void setPreferences(Preferences.Builder preferences) {
        this.preferences = preferences;
    }
    public boolean isShowOutbox() {
        return this.showOutbox;
    }
    public void setShowOutbox(boolean showOutbox) {
        this.showOutbox = showOutbox;
    }
    
	public String getBackLocation() {
		return this.backLocation;
	}
	public void setBackLocation(String backLocation) {
		this.backLocation = backLocation;
	}
	
	public String getDocumentTypePreferenceName() {
        return documentTypePreferenceName;
    }
    
    public void setDocumentTypePreferenceName(String documentTypePreferenceName) {
        this.documentTypePreferenceName = documentTypePreferenceName;
    }
    
    public String getDocumentTypePreferenceValue() {
        return documentTypePreferenceValue;
    }
    
    public void setDocumentTypePreferenceValue(String documentTypePreferenceValue) {
        this.documentTypePreferenceValue = documentTypePreferenceValue;
    }
    
    public Object getDocumentTypeNotificationPreference(String documentType) {
        return preferences.getDocumentTypeNotificationPreference(documentType);
    }
    
    public void setDocumentTypeNotificationPreference(String documentType, String preferenceValue) {
        preferences.addDocumentTypeNotificationPreference(documentType, preferenceValue);
    }
	
	/**
	 * Retrieves the "returnLocation" parameter after calling "populate" on the superclass.
	 * 
	 * @see org.kuali.rice.krad.web.struts.form.KualiForm#populate(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void populate(HttpServletRequest request) {
		super.populate(request);
		
        if (getParameter(request, KRADConstants.RETURN_LOCATION_PARAMETER) != null) {
            String returnLocation = getParameter(request, KRADConstants.RETURN_LOCATION_PARAMETER);
            if(returnLocation.contains(">") || returnLocation.contains("<") || returnLocation.contains("\"")) {
                returnLocation = returnLocation.replaceAll("\"", "%22");
                returnLocation = returnLocation.replaceAll("<", "%3C");
                returnLocation = returnLocation.replaceAll(">","%3E");
                
            }
            setBackLocation(returnLocation);
        }
	}

    public void validatePreferences() {
        if((!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorSaved()))  ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorInitiated())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorDisapproved())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorEnroute())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorApproved())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorFinal())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorProcessed())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorException())) ||
                (!PreferencesConstants.PreferencesDocumentRouteStatusColors.getPreferencesDocumentRouteStatusColors().contains(preferences.getColorCanceled()))
                ){
            throw new RiceRuntimeException("Preferences cannot be saved since they have been tampered with. Please refresh the page and try again");
        }

        if(!PreferencesConstants.EmailNotificationPreferences.getEmailNotificationPreferences().contains(preferences.getEmailNotification())) {
            throw new RiceRuntimeException("Email notifications cannot be saved since they have been tampered with. Please refresh the page and try again");
        }

        if(!PreferencesConstants.DelegatorFilterValues.getDelegatorFilterValues().contains(preferences.getDelegatorFilter())) {
            throw new RiceRuntimeException("Delegator filter values cannot be saved since they have been tampered with. Please refresh the page and try again");

        }

        if(!PreferencesConstants.PrimaryDelegateFilterValues.getPrimaryDelegateFilterValues().contains(preferences.getPrimaryDelegateFilter())) {
            throw new RiceRuntimeException("Primary delegator filter values cannot be saved since they have been tampered with. Please refresh the page and try again");
        }

        if((!StringUtils.isBlank(preferences.getNotifyPrimaryDelegation())) &&
           (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getNotifyPrimaryDelegation()))) {
            throw new RiceRuntimeException("Invalid value found for checkbox \"Recieve Primary Delegate Email\"");
        }

        if((!StringUtils.isBlank(preferences.getNotifySecondaryDelegation())) &&
           (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getNotifySecondaryDelegation()))) {
            throw new RiceRuntimeException("Invalid value found for checkbox \"Recieve Secondary Delegate Email\"");
        }

        if((!StringUtils.isBlank(preferences.getShowDocType())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDocType())) ||
                (!StringUtils.isBlank(preferences.getShowDocTitle())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDocTitle())) ||
                (!StringUtils.isBlank(preferences.getShowActionRequested())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowActionRequested())) ||
                (!StringUtils.isBlank(preferences.getShowInitiator())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowInitiator())) ||
                (!StringUtils.isBlank(preferences.getShowDelegator())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDelegator())) ||
                (!StringUtils.isBlank(preferences.getShowDateCreated())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDateCreated())) ||
                (!StringUtils.isBlank(preferences.getShowDateApproved())) &&(!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDateApproved())) ||
                (!StringUtils.isBlank(preferences.getShowCurrentNode())) &&	(!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowCurrentNode())) ||
                (!StringUtils.isBlank(preferences.getShowWorkgroupRequest())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowWorkgroupRequest())) ||
                (!StringUtils.isBlank(preferences.getShowDocumentStatus())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowDocumentStatus())) ||
                (!StringUtils.isBlank(preferences.getShowClearFyi())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getShowClearFyi())) ||
                (!StringUtils.isBlank(preferences.getUseOutbox())) && (!PreferencesConstants.CheckBoxValues.getCheckBoxValues().contains(preferences.getUseOutbox()))) {
            throw new RiceRuntimeException("Preferences for fields displayed in action list cannot be saved since they have in tampered with. Please refresh the page and try again");
        }

        try {
            new Integer(preferences.getRefreshRate().trim());
        } catch (NumberFormatException e) {
            GlobalVariables.getMessageMap().putError(ERR_KEY_REFRESH_RATE_WHOLE_NUM, "general.message", "ActionList Refresh Rate must be in whole minutes");
        } catch (NullPointerException e1) {
            GlobalVariables.getMessageMap().putError(ERR_KEY_REFRESH_RATE_WHOLE_NUM, "general.message", "ActionList Refresh Rate must be in whole minutes");
        }

        try {
            new Integer(preferences.getPageSize().trim());
            if((new Integer(preferences.getPageSize().trim()) <= 0) || (new Integer(preferences.getPageSize().trim()) > 500)) {
                GlobalVariables.getMessageMap().putError(ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM, "general.message", "ActionList Page Size must be between 1 and 500");
            }    
        } catch (NumberFormatException e) {
            GlobalVariables.getMessageMap().putError(ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM, "general.message", "ActionList Page Size must be in whole minutes");
        } catch (NullPointerException e1) {
            GlobalVariables.getMessageMap().putError(ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM, "general.message", "ActionList Page Size must be in whole minutes");
        }
      
        if (GlobalVariables.getMessageMap().hasErrors()) {
            throw new ValidationException("errors in preferences");
        }
    }
}
