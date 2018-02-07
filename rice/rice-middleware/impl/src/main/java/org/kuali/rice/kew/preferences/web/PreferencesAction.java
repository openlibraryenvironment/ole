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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;


/**
 * A Struts Action for interfaces with {@link Preferences}.
 *
 * @see PreferencesService
 * @see Preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PreferencesAction extends KewKualiAction {

    private static final String DOC_TYPE_NAME_PROPERTY = "documentTypePreferenceName";
    private static final String DOCUMENT_TYPE_ERROR = "docType.preference.name.required";
    private static final String DOCUMENT_TYPE_PREFERENCE_ADDED_MESSAGE = "docType.preference.added.message";
    private static final String DOCUMENT_TYPE_PREFERENCE_REMOVED_MESSAGE = "docType.preference.removed.message";
    private static final String DOC_TYPE_PARAM = "documentType";
    private static final String PREFERENCE_VALUE_PARAM = "preferenceValue";
    public static final String SAVE_REMINDER_ATTR = "saveReminder";
    
    private PreferencesService preferencesService;
    
    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        initForm(request, form);
        request.setAttribute("Constants", getServlet().getServletContext().getAttribute("KewApiConstants"));
        return super.execute(mapping, form, request, response);
    }

    @Override
	public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PreferencesForm preferencesForm = (PreferencesForm) form;
        org.kuali.rice.kew.api.preferences.Preferences preferences = getPreferencesService().getPreferences(
                getUserSession().getPrincipalId());
        preferencesForm.setPreferences(org.kuali.rice.kew.api.preferences.Preferences.Builder.create(preferences));
        return mapping.findForward("basic");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PreferencesForm prefForm = (PreferencesForm) form;

        prefForm.validatePreferences();
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            getPreferencesService().savePreferences(getUserSession().getPrincipalId(), prefForm.getPreferences().build());
        }
        
        GlobalVariables.getUserSession().addObject(KewApiConstants.UPDATE_ACTION_LIST_ATTR_NAME, Boolean.TRUE);
        GlobalVariables.getUserSession().removeObject(KewApiConstants.PREFERENCES);
        
        if (! StringUtils.isEmpty(prefForm.getReturnMapping())) {
            return mapping.findForward(prefForm.getReturnMapping());
        }
        return mapping.findForward("basic");
    }

    public ActionMessages initForm(HttpServletRequest request, ActionForm form) throws Exception {
        request.setAttribute("actionListContent", KewApiConstants.ACTION_LIST_CONTENT);
        getDelegatorFilterChoices(request);
        getPrimaryDelegateFilterChoices(request);
        PreferencesForm prefForm = (PreferencesForm)form;
        prefForm.setShowOutbox(ConfigContext.getCurrentContextConfig().getOutBoxOn());
        return null;
    }

    public void getDelegatorFilterChoices(HttpServletRequest request) {
        List<KeyValue> delegatorFilterChoices = new ArrayList<KeyValue>();
        delegatorFilterChoices.add(new ConcreteKeyValue(KewApiConstants.DELEGATORS_ON_FILTER_PAGE, KewApiConstants.DELEGATORS_ON_FILTER_PAGE));
        delegatorFilterChoices.add(new ConcreteKeyValue(KewApiConstants.DELEGATORS_ON_ACTION_LIST_PAGE, KewApiConstants.DELEGATORS_ON_ACTION_LIST_PAGE));
        request.setAttribute("delegatorFilter", delegatorFilterChoices);
    }
    
    public void getPrimaryDelegateFilterChoices(HttpServletRequest request) {
    	List<KeyValue> primaryDelegateFilterChoices = new ArrayList<KeyValue>();
    	primaryDelegateFilterChoices.add(new ConcreteKeyValue(KewApiConstants.PRIMARY_DELEGATES_ON_FILTER_PAGE, KewApiConstants.PRIMARY_DELEGATES_ON_FILTER_PAGE));
        primaryDelegateFilterChoices.add(new ConcreteKeyValue(KewApiConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE, KewApiConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE));
        request.setAttribute("primaryDelegateFilter", primaryDelegateFilterChoices);
    }

    private static UserSession getUserSession() {
        return GlobalVariables.getUserSession();
    }
    
    public ActionForward addNotificationPreference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PreferencesForm preferencesForm = (PreferencesForm) form;
        if(validateAddNotificationPreference(preferencesForm)) {
            preferencesForm.getPreferences().addDocumentTypeNotificationPreference(preferencesForm.getDocumentTypePreferenceName(), preferencesForm.getDocumentTypePreferenceValue());
            preferencesForm.setDocumentTypePreferenceName(null);
            preferencesForm.setDocumentTypePreferenceValue(null);
            GlobalVariables.getMessageMap().putInfo(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_PREFERENCE_ADDED_MESSAGE);
            request.setAttribute(SAVE_REMINDER_ATTR, "true");
        }
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteNotificationPreference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PreferencesForm preferencesForm = (PreferencesForm) form;
        String parameterName = (String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE);
        String documentType = StringUtils.substringAfter(StringUtils.substringBeforeLast(parameterName, "."), "deleteNotificationPreference.");
        preferencesForm.getPreferences().removeDocumentTypeNotificationPreference(documentType);
        GlobalVariables.getMessageMap().putInfo(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_PREFERENCE_REMOVED_MESSAGE);
        request.setAttribute(SAVE_REMINDER_ATTR, "true");
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    private boolean validateAddNotificationPreference(PreferencesForm form) {
        if (StringUtils.isEmpty(form.getDocumentTypePreferenceName()) || StringUtils.isEmpty(form.getDocumentTypePreferenceValue())) {
            GlobalVariables.getMessageMap().putError(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_ERROR);
        } else {
            DocumentType docType = KewApiServiceLocator.getDocumentTypeService().getDocumentTypeByName(form.getDocumentTypePreferenceName());
            if (docType == null) {
                GlobalVariables.getMessageMap().putError(DOC_TYPE_NAME_PROPERTY, DOCUMENT_TYPE_ERROR);
            }
        }
        return GlobalVariables.getMessageMap().getErrorMessages().size() == 0;
    }

    public ActionForward registerDocumentTypePreference(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PreferencesForm preferencesForm = (PreferencesForm) form;
        this.start(mapping, preferencesForm, request, response);
        preferencesForm.setDocumentTypePreferenceName(request.getParameter(DOC_TYPE_PARAM));
        preferencesForm.setDocumentTypePreferenceValue(request.getParameter(PREFERENCE_VALUE_PARAM));
        this.addNotificationPreference(mapping, preferencesForm, request, response);
        return this.save(mapping, preferencesForm, request, response);
    }

    /**
     * @return the preferencesService
     */
    public PreferencesService getPreferencesService() {
        if(this.preferencesService == null) {
            this.preferencesService = KewApiServiceLocator.getPreferencesService();
        }
        return this.preferencesService;
    }

    /**
     * @param preferencesService the preferencesService to set
     */
    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }
}
