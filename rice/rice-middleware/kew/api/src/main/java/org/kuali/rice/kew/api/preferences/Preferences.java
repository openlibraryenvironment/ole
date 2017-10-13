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
package org.kuali.rice.kew.api.preferences;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.core.api.util.jaxb.MultiValuedStringMapAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.w3c.dom.Element;

/**
 * An immutable data transfer object implementing {@link PreferencesContract}.
 *
 * p>When loaded, Preferences could be in a state where they require being saved to the database.
 * If this is the case then {{@link #requiresSave} will evaluate to true.
 *
 * @see PreferencesContract
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = Preferences.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Preferences.Constants.TYPE_NAME, propOrder = {
        Preferences.Elements.REQUIRES_SAVE,
        Preferences.Elements.EMAIL_NOTIFICATION,
        Preferences.Elements.NOTIFY_PRIMARY_DELEGATION,
        Preferences.Elements.NOTIFY_SECONDARY_DELEGATION,
        Preferences.Elements.OPEN_NEW_WINDOW,
        Preferences.Elements.SHOW_ACTION_REQUESTED,
        Preferences.Elements.SHOW_DATE_CREATED,
        Preferences.Elements.SHOW_DOCUMENT_STATUS,
        Preferences.Elements.SHOW_APP_DOC_STATUS,
        Preferences.Elements.SHOW_DOC_TYPE,
        Preferences.Elements.SHOW_INITIATOR,
        Preferences.Elements.SHOW_DOC_TITLE,
        Preferences.Elements.SHOW_WORKGROUP_REQUEST,
        Preferences.Elements.SHOW_DELEGATOR,
        Preferences.Elements.SHOW_CLEAR_FYI,
        Preferences.Elements.PAGE_SIZE,
        Preferences.Elements.REFRESH_RATE,
        Preferences.Elements.COLOR_SAVED,
        Preferences.Elements.COLOR_INITIATED,
        Preferences.Elements.COLOR_DISAPPROVED,
        Preferences.Elements.COLOR_ENROUTE,
        Preferences.Elements.COLOR_APPROVED,
        Preferences.Elements.COLOR_FINAL,
        Preferences.Elements.COLOR_DISAPPROVE_CANCEL,
        Preferences.Elements.COLOR_PROCESSED,
        Preferences.Elements.COLOR_EXCEPTION,
        Preferences.Elements.COLOR_CANCELED,
        Preferences.Elements.DELEGATOR_FILTER,
        Preferences.Elements.USE_OUTBOX,
        Preferences.Elements.SHOW_DATE_APPROVED,
        Preferences.Elements.SHOW_CURRENT_NODE,
        Preferences.Elements.PRIMARY_DELEGATE_FILTER,
        Preferences.Elements.NOTIFY_ACKNOWLEDGE,
        Preferences.Elements.NOTIFY_APPROVE,
        Preferences.Elements.NOTIFY_COMPLETE,
        Preferences.Elements.NOTIFY_FYI,
        Preferences.Elements.DOCUMENT_TYPE_NOTIFICATION_PREFERENCES,
        Preferences.Elements.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_MAP,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Preferences extends AbstractDataTransferObject implements PreferencesContract {

    private static final long serialVersionUID = 642820621349964439L;

    @XmlElement(name = Elements.REQUIRES_SAVE)
    private final boolean requiresSave;
    @XmlElement(name = Elements.EMAIL_NOTIFICATION)
    private final String emailNotification;
    @XmlElement(name = Elements.NOTIFY_PRIMARY_DELEGATION)
    private final String notifyPrimaryDelegation;
    @XmlElement(name = Elements.NOTIFY_SECONDARY_DELEGATION)
    private final String notifySecondaryDelegation;
    @XmlElement(name = Elements.OPEN_NEW_WINDOW)
    private final String openNewWindow;
    @XmlElement(name = Elements.SHOW_ACTION_REQUESTED)
    private final String showActionRequested;
    @XmlElement(name = Elements.SHOW_DATE_CREATED)
    private final String showDateCreated;
    @XmlElement(name = Elements.SHOW_DOCUMENT_STATUS)
    private final String showDocumentStatus;
    @XmlElement(name = Elements.SHOW_APP_DOC_STATUS)
    private final String showAppDocStatus;
    @XmlElement(name = Elements.SHOW_DOC_TYPE)
    private final String showDocType;
    @XmlElement(name = Elements.SHOW_INITIATOR)
    private final String showInitiator;
    @XmlElement(name = Elements.SHOW_DOC_TITLE)
    private final String showDocTitle;
    @XmlElement(name = Elements.SHOW_WORKGROUP_REQUEST)
    private final String showWorkgroupRequest;
    @XmlElement(name = Elements.SHOW_DELEGATOR)
    private final String showDelegator;
    @XmlElement(name = Elements.SHOW_CLEAR_FYI)
    private final String showClearFyi;
    @XmlElement(name = Elements.PAGE_SIZE)
    private final String pageSize;
    @XmlElement(name = Elements.REFRESH_RATE)
    private final String refreshRate;
    @XmlElement(name = Elements.COLOR_SAVED)
    private final String colorSaved;
    @XmlElement(name = Elements.COLOR_INITIATED)
    private final String colorInitiated;
    @XmlElement(name = Elements.COLOR_DISAPPROVED)
    private final String colorDisapproved;
    @XmlElement(name = Elements.COLOR_ENROUTE)
    private final String colorEnroute;
    @XmlElement(name = Elements.COLOR_APPROVED)
    private final String colorApproved;
    @XmlElement(name = Elements.COLOR_FINAL)
    private final String colorFinal;
    @XmlElement(name = Elements.COLOR_DISAPPROVE_CANCEL)
    private final String colorDisapproveCancel;
    @XmlElement(name = Elements.COLOR_PROCESSED)
    private final String colorProcessed;
    @XmlElement(name = Elements.COLOR_EXCEPTION)
    private final String colorException;
    @XmlElement(name = Elements.COLOR_CANCELED)
    private final String colorCanceled;
    @XmlElement(name = Elements.DELEGATOR_FILTER)
    private final String delegatorFilter;
    @XmlElement(name = Elements.USE_OUTBOX)
    private final String useOutbox;
    @XmlElement(name = Elements.SHOW_DATE_APPROVED)
    private final String showDateApproved;
    @XmlElement(name = Elements.SHOW_CURRENT_NODE)
    private final String showCurrentNode;
    @XmlElement(name = Elements.PRIMARY_DELEGATE_FILTER)
    private final String primaryDelegateFilter;
    @XmlElement(name = Elements.NOTIFY_ACKNOWLEDGE)
    private final String notifyAcknowledge;
    @XmlElement(name = Elements.NOTIFY_APPROVE)
    private final String notifyApprove;

    //TODO: fix this type in Rice 3.0
    @XmlElement(name = Elements.NOTIFY_COMPLETE)
    private final String notifyCompelte;
    @XmlElement(name = Elements.NOTIFY_FYI)
    private final String notifyFYI;

    /*
     * @Deprecated for 2.1.1.  Invalid @XmlJavaTypeAdapter.  Use documentTypeNotitificationPreferenceMap instead.
     */
    @XmlElement(name = Elements.DOCUMENT_TYPE_NOTIFICATION_PREFERENCES)
    @XmlJavaTypeAdapter(MultiValuedStringMapAdapter.class)
    @Deprecated
    private Map<String, String> documentTypeNotificationPreferences;

    @XmlElement(name = Elements.DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_MAP)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private Map<String, String> documentTypeNotificationPreferenceMap;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private Preferences() {
        this.emailNotification = null;
        this.notifyPrimaryDelegation = null;
        this.notifySecondaryDelegation = null;
        this.openNewWindow = null;
        this.showActionRequested = null;
        this.showDateCreated = null;
        this.showDocumentStatus = null;
        this.showAppDocStatus = null;
        this.showDocType = null;
        this.showInitiator = null;
        this.showDocTitle = null;
        this.showWorkgroupRequest = null;
        this.showDelegator = null;
        this.showClearFyi = null;
        this.pageSize = null;
        this.refreshRate = null;
        this.colorSaved = null;
        this.colorInitiated = null;
        this.colorDisapproved = null;
        this.colorEnroute = null;
        this.colorApproved = null;
        this.colorFinal = null;
        this.colorDisapproveCancel = null;
        this.colorProcessed = null;
        this.colorException = null;
        this.colorCanceled = null;
        this.delegatorFilter = null;
        this.useOutbox = null;
        this.showDateApproved = null;
        this.showCurrentNode = null;
        this.primaryDelegateFilter = null;
        this.notifyAcknowledge = null;
        this.notifyApprove =  null;
        this.notifyCompelte = null;
        this.notifyFYI = null;
        this.documentTypeNotificationPreferences = null;
        this.documentTypeNotificationPreferenceMap = Collections.emptyMap();

        this.requiresSave = false;
    }

    public Preferences(Builder builder) {
        this.emailNotification = builder.getEmailNotification();
        this.notifyPrimaryDelegation = builder.getNotifyPrimaryDelegation();
        this.notifySecondaryDelegation = builder.getNotifySecondaryDelegation();
        this.openNewWindow = builder.getOpenNewWindow();
        this.showActionRequested = builder.getShowActionRequested();
        this.showDateCreated = builder.getShowDateCreated();
        this.showDocumentStatus = builder.getShowDocumentStatus();
        this.showAppDocStatus = builder.getShowAppDocStatus();
        this.showDocType = builder.getShowDocType();
        this.showInitiator = builder.getShowInitiator();
        this.showDocTitle = builder.getShowDocTitle();
        this.showWorkgroupRequest = builder.getShowWorkgroupRequest();
        this.showDelegator = builder.getShowDelegator();
        this.showClearFyi = builder.getShowClearFyi();
        this.pageSize = builder.getPageSize();
        this.refreshRate = builder.getRefreshRate();
        this.colorSaved = builder.getColorSaved();
        this.colorInitiated = builder.getColorInitiated();
        this.colorDisapproved = builder.getColorDisapproved();
        this.colorEnroute = builder.getColorEnroute();
        this.colorApproved = builder.getColorApproved();
        this.colorFinal = builder.getColorFinal();
        this.colorDisapproveCancel = builder.getColorDisapproveCancel();
        this.colorProcessed = builder.getColorProcessed();
        this.colorException = builder.getColorException();
        this.colorCanceled = builder.getColorCanceled();
        this.delegatorFilter = builder.getDelegatorFilter();
        this.useOutbox = builder.getUseOutbox();
        this.showDateApproved = builder.getShowDateApproved();
        this.showCurrentNode = builder.getShowCurrentNode();
        this.primaryDelegateFilter = builder.getPrimaryDelegateFilter();
        this.requiresSave = builder.isRequiresSave();
        this.notifyAcknowledge = builder.getNotifyAcknowledge();
        this.notifyApprove = builder.getNotifyApprove();
        this.notifyCompelte = builder.getNotifyComplete();
        this.notifyFYI = builder.getNotifyFYI();
        this.documentTypeNotificationPreferences = null;
        this.documentTypeNotificationPreferenceMap = builder.getDocumentTypeNotificationPreferences();
    }

    @Override
    public boolean isRequiresSave() {
        return requiresSave;
    }

    @Override
    public String getEmailNotification() {
        return emailNotification;
    }

    @Override
    public String getNotifyPrimaryDelegation() {
        return notifyPrimaryDelegation;
    }

    @Override
    public String getNotifySecondaryDelegation() {
        return notifySecondaryDelegation;
    }

    @Override
    public String getOpenNewWindow() {
        return openNewWindow;
    }

    @Override
    public String getShowActionRequested() {
        return showActionRequested;
    }

    @Override
    public String getShowDateCreated() {
        return showDateCreated;
    }

    @Override
    public String getShowDocumentStatus() {
        return showDocumentStatus;
    }

    @Override
    public String getShowAppDocStatus() {
        return showAppDocStatus;
    }

    @Override
    public String getShowDocType() {
        return showDocType;
    }

    @Override
    public String getShowInitiator() {
        return showInitiator;
    }

    @Override
    public String getShowDocTitle() {
        return showDocTitle;
    }

    @Override
    public String getShowWorkgroupRequest() {
        return showWorkgroupRequest;
    }

    @Override
    public String getShowDelegator() {
        return showDelegator;
    }

    @Override
    public String getShowClearFyi() {
        return showClearFyi;
    }

    @Override
    public String getPageSize() {
        return pageSize;
    }

    @Override
    public String getRefreshRate() {
        return refreshRate;
    }

    @Override
    public String getColorSaved() {
        return colorSaved;
    }

    @Override
    public String getColorInitiated() {
        return colorInitiated;
    }

    @Override
    public String getColorDisapproved() {
        return colorDisapproved;
    }

    @Override
    public String getColorEnroute() {
        return colorEnroute;
    }

    @Override
    public String getColorApproved() {
        return colorApproved;
    }

    @Override
    public String getColorFinal() {
        return colorFinal;
    }

    @Override
    public String getColorDisapproveCancel() {
        return colorDisapproveCancel;
    }

    @Override
    public String getColorProcessed() {
        return colorProcessed;
    }

    @Override
    public String getColorException() {
        return colorException;
    }

    @Override
    public String getColorCanceled() {
        return colorCanceled;
    }

    @Override
    public String getDelegatorFilter() {
        return delegatorFilter;
    }

    @Override
    public String getUseOutbox() {
        return useOutbox;
    }

    @Override
    public String getShowDateApproved() {
        return showDateApproved;
    }

    @Override
    public String getShowCurrentNode() {
        return showCurrentNode;
    }

    @Override
    public String getPrimaryDelegateFilter() {
        return primaryDelegateFilter;
    }

    @Override
    public String getNotifyComplete() {
        return this.notifyCompelte;
    }

    @Override
    public String getNotifyApprove() {
        return this.notifyApprove;
    }

    @Override
    public String getNotifyAcknowledge() {
        return this.notifyAcknowledge;
    }

    @Override
    public String getNotifyFYI() {
        return this.notifyFYI;
    }
    
    public String getDocumentTypeNotificationPreference(String documentType) {
        String preferenceName = documentType.replace(KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_DELIMITER, ".");
        String preferenceValue = this.getDocumentTypeNotificationPreferences().get(preferenceName);
        if(StringUtils.isNotBlank(preferenceValue)) {
            return preferenceValue;
        }
        return null;
    }

    @Override
    public Map<String, String> getDocumentTypeNotificationPreferences() {
        return this.documentTypeNotificationPreferenceMap == null ? this.documentTypeNotificationPreferences : this.documentTypeNotificationPreferenceMap ;
    }

    public boolean isUsingOutbox() {
        if (this.getUseOutbox() != null && this.getUseOutbox().equals(Constants.PREFERENCES_YES_VAL)) {
            return true;
        }
        return false;
    }

    public final static class Builder
            implements Serializable, ModelBuilder, PreferencesContract
    {

        private boolean requiresSave = false;

        private String emailNotification;
        private String notifyPrimaryDelegation;
        private String notifySecondaryDelegation;
        private String openNewWindow;
        private String showActionRequested;
        private String showDateCreated;
        private String showDocumentStatus;
        private String showAppDocStatus;
        private String showDocType;
        private String showInitiator;
        private String showDocTitle;
        private String showWorkgroupRequest;
        private String showDelegator;
        private String showClearFyi;
        private String pageSize;
        private String refreshRate;
        private String colorSaved;
        private String colorInitiated;
        private String colorDisapproved;
        private String colorEnroute;
        private String colorApproved;
        private String colorFinal;
        private String colorDissapproveCancel;
        private String colorProcessed;
        private String colorException;
        private String colorCanceled;
        private String delegatorFilter;
        private String useOutbox;
        private String showDateApproved;
        private String showCurrentNode;
        private String primaryDelegateFilter;
        private String notifyAcknowledge;
        private String notifyApprove;
        private String notifyComplete;
        private String notifyFYI;
        private Map<String, String> documentTypeNotificationPreferences;

        private Builder() {
            this.documentTypeNotificationPreferences = new HashMap<String, String>();
        }

        private Builder(String emailNotification, String notifyPrimaryDelegation, String notifySecondaryDelegation,
                String openNewWindow, String showActionRequested, String showDateCreated, String showDocumentStatus,
                String showAppDocStatus, String showDocType, String showInitiator, String showDocTitle,
                String showWorkgroupRequest, String showDelegator, String showClearFyi, String pageSize, String refreshRate,
                String colorSaved, String colorInitiated, String colorDisapproved, String colorEnroute,
                String colorApproved, String colorFinal, String colorDissapproveCancel, String colorProcessed,
                String colorException, String colorCanceled, String delegatorFilter, String useOutbox,
                String showDateApproved, String showCurrentNode, String primaryDelegateFilter, String notifyAcknowledge,
                String notifyApprove, String notifyComplete, String notifyFYI, Map<String, String> documentTypeNotificationPreferences,
                boolean requiresSave) {
            this.emailNotification = emailNotification;
            this.notifyPrimaryDelegation = notifyPrimaryDelegation;
            this.notifySecondaryDelegation = notifySecondaryDelegation;
            this.openNewWindow = openNewWindow;
            this.showActionRequested = showActionRequested;
            this.showDateCreated = showDateCreated;
            this.showDocumentStatus = showDocumentStatus;
            this.showAppDocStatus = showAppDocStatus;
            this.showDocType = showDocType;
            this.showInitiator = showInitiator;
            this.showDocTitle = showDocTitle;
            this.showWorkgroupRequest = showWorkgroupRequest;
            this.showDelegator = showDelegator;
            this.showClearFyi = showClearFyi;
            this.pageSize = pageSize;
            this.refreshRate = refreshRate;
            this.colorSaved = colorSaved;
            this.colorInitiated = colorInitiated;
            this.colorDisapproved = colorDisapproved;
            this.colorEnroute = colorEnroute;
            this.colorApproved = colorApproved;
            this.colorFinal = colorFinal;
            this.colorDissapproveCancel = colorDissapproveCancel;
            this.colorProcessed = colorProcessed;
            this.colorException = colorException;
            this.colorCanceled = colorCanceled;
            this.delegatorFilter = delegatorFilter;
            this.useOutbox = useOutbox;
            this.showDateApproved = showDateApproved;
            this.showCurrentNode = showCurrentNode;
            this.primaryDelegateFilter = primaryDelegateFilter;
            this.requiresSave = requiresSave;
            this.notifyAcknowledge = notifyAcknowledge;
            this.notifyApprove = notifyApprove;
            this.notifyComplete = notifyComplete;
            this.notifyFYI = notifyFYI;
            this.documentTypeNotificationPreferences = documentTypeNotificationPreferences;
        }

        public Preferences build() {
            return new Preferences(this);
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String emailNotification, String notifyPrimaryDelegation, String notifySecondaryDelegation,
                String openNewWindow, String showActionRequested, String showDateCreated, String showDocumentStatus,
                String showAppDocStatus, String showDocType, String showInitiator, String showDocTitle,
                String showWorkgroupRequest, String showDelegator, String showClearFyi, String pageSize, String refreshRate,
                String colorSaved, String colorInitiated, String colorDisapproved, String colorEnroute,
                String colorApproved, String colorFinal, String colorDissapproveCancel, String colorProcessed,
                String colorException, String colorCanceled, String delegatorFilter, String useOutbox,
                String showDateApproved, String showCurrentNode, String primaryDelegateFilter, String notifyAcknowledge,
                String notifyApprove, String notifyComplete, String notifyFYI, Map<String, String> documentTypeNotificationPreferences,
                boolean requiresSave) {
            return new Builder(emailNotification, notifyPrimaryDelegation, notifySecondaryDelegation, openNewWindow, showActionRequested, showDateCreated,
                    showDocumentStatus, showAppDocStatus, showDocType, showInitiator, showDocTitle, showWorkgroupRequest,  showDelegator, showClearFyi,
                    pageSize, refreshRate, colorSaved, colorInitiated, colorDisapproved, colorEnroute, colorApproved, colorFinal, colorDissapproveCancel,
                    colorProcessed, colorException, colorCanceled, delegatorFilter, useOutbox, showDateApproved, showCurrentNode, primaryDelegateFilter,
                    notifyAcknowledge, notifyApprove, notifyComplete, notifyFYI, documentTypeNotificationPreferences, requiresSave);
        }

        public static Builder create(PreferencesContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create(contract.getEmailNotification(), contract.getNotifyPrimaryDelegation(), contract.getNotifySecondaryDelegation(), contract.getOpenNewWindow(),
                    contract.getShowActionRequested(), contract.getShowDateCreated(), contract.getShowDocumentStatus(), contract.getShowAppDocStatus(), contract.getShowDocType(),
                    contract.getShowInitiator(), contract.getShowDocTitle(), contract.getShowWorkgroupRequest(), contract.getShowDelegator(), contract.getShowClearFyi(),
                    contract.getPageSize(), contract.getRefreshRate(), contract.getColorSaved(), contract.getColorInitiated(), contract.getColorDisapproved(),
                    contract.getColorEnroute(), contract.getColorApproved(), contract.getColorFinal(), contract.getColorDisapproveCancel(), contract.getColorProcessed(),
                    contract.getColorException(), contract.getColorCanceled(), contract.getDelegatorFilter(), contract.getUseOutbox(), contract.getShowDateApproved(),
                    contract.getShowCurrentNode(), contract.getPrimaryDelegateFilter(), contract.getNotifyAcknowledge(), contract.getNotifyApprove(), contract.getNotifyComplete(),
                    contract.getNotifyFYI(), contract.getDocumentTypeNotificationPreferences(), contract.isRequiresSave());
            return builder;
        }

        public static Builder create(Map<String, String> map, Map<String, String> documentTypeNotificationPreferences, boolean requiresSave) {
            Builder builder = create(map.get(KEYS.EMAIL_NOTIFICATION), map.get(KEYS.NOTIFY_PRIMARY_DELEGATION), map.get(KEYS.NOTIFY_SECONDARY_DELEGATION), map.get(KEYS.OPEN_NEW_WINDOW),
                    map.get(KEYS.SHOW_ACTION_REQUESTED), map.get(KEYS.SHOW_DATE_CREATED), map.get(KEYS.SHOW_DOCUMENT_STATUS), map.get(KEYS.SHOW_APP_DOC_STATUS), map.get(KEYS.SHOW_DOC_TYPE),
                    map.get(KEYS.SHOW_INITIATOR), map.get(KEYS.SHOW_DOC_TITLE), map.get(KEYS.SHOW_GROUP_REQUEST), map.get(KEYS.SHOW_DELEGATOR), map.get(KEYS.SHOW_CLEAR_FYI),
                    map.get(KEYS.PAGE_SIZE), map.get(KEYS.REFRESH_RATE), map.get(KEYS.COLOR_SAVED), map.get(KEYS.COLOR_INITIATED), map.get(KEYS.COLOR_DISAPPROVED),
                    map.get(KEYS.COLOR_ENROUTE), map.get(KEYS.COLOR_APPROVED), map.get(KEYS.COLOR_FINAL), map.get(KEYS.COLOR_DISAPPROVE_CANCEL), map.get(KEYS.COLOR_PROCESSED),
                    map.get(KEYS.COLOR_EXCEPTION), map.get(KEYS.COLOR_CANCELED), map.get(KEYS.DELEGATOR_FILTER), map.get(KEYS.USE_OUT_BOX), map.get(KEYS.SHOW_DATE_APPROVED),
                    map.get(KEYS.SHOW_CURRENT_NODE), map.get(KEYS.PRIMARY_DELEGATE_FILTER), map.get(KEYS.NOTIFY_ACKNOWLEDGE), map.get(KEYS.NOTIFY_APPROVE), map.get(KEYS.NOTIFY_COMPLETE),
                    map.get(KEYS.NOTIFY_FYI), documentTypeNotificationPreferences, requiresSave);
            return builder;
        }

        public synchronized boolean isRequiresSave() {
            return requiresSave;
        }

        public synchronized void setRequiresSave(boolean requiresSave) {
            this.requiresSave = requiresSave;
        }

        public synchronized String getEmailNotification() {
            return emailNotification;
        }

        public synchronized void setEmailNotification(String emailNotification) {
            this.emailNotification = emailNotification;
        }

        public synchronized String getNotifyPrimaryDelegation() {
            return notifyPrimaryDelegation;
        }

        public synchronized void setNotifyPrimaryDelegation(String notifyPrimaryDelegation) {
            this.notifyPrimaryDelegation = notifyPrimaryDelegation;
        }

        public synchronized String getNotifySecondaryDelegation() {
            return notifySecondaryDelegation;
        }

        public synchronized void setNotifySecondaryDelegation(String notifySecondaryDelegation) {
            this.notifySecondaryDelegation = notifySecondaryDelegation;
        }

        public synchronized String getOpenNewWindow() {
            return openNewWindow;
        }

        public synchronized void setOpenNewWindow(String openNewWindow) {
            this.openNewWindow = openNewWindow;
        }

        public synchronized String getShowActionRequested() {
            return showActionRequested;
        }

        public synchronized void setShowActionRequested(String showActionRequested) {
            this.showActionRequested = showActionRequested;
        }

        public synchronized String getShowDateCreated() {
            return showDateCreated;
        }

        public synchronized void setShowDateCreated(String showDateCreated) {
            this.showDateCreated = showDateCreated;
        }

        public synchronized String getShowDocumentStatus() {
            return showDocumentStatus;
        }

        public synchronized void setShowDocumentStatus(String showDocumentStatus) {
            this.showDocumentStatus = showDocumentStatus;
        }

        public synchronized  String getShowAppDocStatus() {
            return showAppDocStatus;
        }

        public synchronized void setShowAppDocStatus(String showAppDocStatus) {
            this.showAppDocStatus = showAppDocStatus;
        }

        public synchronized String getShowDocType() {
            return showDocType;
        }

        public synchronized void setShowDocType(String showDocType) {
            this.showDocType = showDocType;
        }

        public synchronized String getShowInitiator() {
            return showInitiator;
        }

        public synchronized void setShowInitiator(String showInitiator) {
            this.showInitiator = showInitiator;
        }

        public synchronized String getShowDocTitle() {
            return showDocTitle;
        }

        public synchronized void setShowDocTitle(String showDocTitle) {
            this.showDocTitle = showDocTitle;
        }

        public synchronized String getShowWorkgroupRequest() {
            return showWorkgroupRequest;
        }

        public synchronized void setShowWorkgroupRequest(String showWorkgroupRequest) {
            this.showWorkgroupRequest = showWorkgroupRequest;
        }

        public synchronized String getShowDelegator() {
            return showDelegator;
        }

        public synchronized void setShowDelegator(String showDelegator) {
            this.showDelegator = showDelegator;
        }

        public synchronized String getShowClearFyi() {
            return showClearFyi;
        }

        public synchronized void setShowClearFyi(String showClearFyi) {
            this.showClearFyi = showClearFyi;
        }

        public synchronized String getPageSize() {
            return pageSize;
        }

        public synchronized void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public synchronized String getRefreshRate() {
            return refreshRate;
        }

        public synchronized void setRefreshRate(String refreshRate) {
            this.refreshRate = refreshRate;
        }

        public synchronized String getColorSaved() {
            return colorSaved;
        }

        public synchronized void setColorSaved(String colorSaved) {
            this.colorSaved = colorSaved;
        }

        public synchronized String getColorInitiated() {
            return colorInitiated;
        }

        public synchronized void setColorInitiated(String colorInitiated) {
            this.colorInitiated = colorInitiated;
        }

        public synchronized String getColorDisapproved() {
            return colorDisapproved;
        }

        public synchronized void setColorDisapproved(String colorDisapproved) {
            this.colorDisapproved = colorDisapproved;
        }

        public synchronized String getColorEnroute() {
            return colorEnroute;
        }

        public synchronized void setColorEnroute(String colorEnroute) {
            this.colorEnroute = colorEnroute;
        }

        public synchronized String getColorApproved() {
            return colorApproved;
        }

        public synchronized void setColorApproved(String colorApproved) {
            this.colorApproved = colorApproved;
        }

        public synchronized String getColorFinal() {
            return colorFinal;
        }

        public synchronized void setColorFinal(String colorFinal) {
            this.colorFinal = colorFinal;
        }

        public synchronized String getColorDisapproveCancel() {
            return colorDissapproveCancel;
        }

        public synchronized void setColorDissapproveCancel(String colorDissapproveCancel) {
            this.colorDissapproveCancel = colorDissapproveCancel;
        }

        public synchronized String getColorProcessed() {
            return colorProcessed;
        }

        public synchronized void setColorProcessed(String colorProcessed) {
            this.colorProcessed = colorProcessed;
        }

        public synchronized String getColorException() {
            return colorException;
        }

        public synchronized void setColorException(String colorException) {
            this.colorException = colorException;
        }

        public synchronized String getColorCanceled() {
            return colorCanceled;
        }

        public synchronized void setColorCanceled(String colorCanceled) {
            this.colorCanceled = colorCanceled;
        }

        public synchronized String getDelegatorFilter() {
            return delegatorFilter;
        }

        public synchronized void setDelegatorFilter(String delegatorFilter) {
            this.delegatorFilter = delegatorFilter;
        }

        public synchronized String getUseOutbox() {
            return useOutbox;
        }

        public synchronized void setUseOutbox(String useOutbox) {
            this.useOutbox = useOutbox;
        }

        public synchronized String getShowDateApproved() {
            return showDateApproved;
        }

        public synchronized void setShowDateApproved(String showDateApproved) {
            this.showDateApproved = showDateApproved;
        }

        public synchronized String getShowCurrentNode() {
            return showCurrentNode;
        }

        public synchronized void setShowCurrentNode(String showCurrentNode) {
            this.showCurrentNode = showCurrentNode;
        }

        public synchronized String getPrimaryDelegateFilter() {
            return primaryDelegateFilter;
        }

        public synchronized void setPrimaryDelegateFilter(String primaryDelegateFilter) {
            this.primaryDelegateFilter = primaryDelegateFilter;
        }

        public synchronized String getNotifyAcknowledge() {
            return this.notifyAcknowledge;
        }

        public synchronized void setNotifyAcknowledge(String notifyAcknowledge) {
            this.notifyAcknowledge = notifyAcknowledge;
        }

        public synchronized String getNotifyApprove() {
            return this.notifyApprove;
        }

        public synchronized void setNotifyApprove(String notifyApprove) {
            this.notifyApprove = notifyApprove;
        }

        public synchronized String getNotifyComplete() {
            return this.notifyComplete;
        }

        public synchronized void setNotifyComplete(String notifyComplete) {
            this.notifyComplete = notifyComplete;
        }

        public synchronized String getNotifyFYI() {
            return this.notifyFYI;
        }

        public synchronized void setNotifyFYI(String notifyFYI) {
            this.notifyFYI = notifyFYI;
        }
        
        public synchronized String getDocumentTypeNotificationPreference(String documentType) {
            String preferenceName = documentType.replace(KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_DELIMITER, ".");
            String preferenceValue = this.documentTypeNotificationPreferences.get(preferenceName);
            if(StringUtils.isNotBlank(preferenceValue)) {
                return preferenceValue;
            }
            return null;
        }
        
        public synchronized void setDocumentTypeNotificationPreference(String documentType, String preference) {   
            documentType = documentType.replace(KewApiConstants.DOCUMENT_TYPE_NOTIFICATION_DELIMITER, ".");
            this.documentTypeNotificationPreferences.put(documentType, preference);
        }

        public synchronized Map<String, String> getDocumentTypeNotificationPreferences() {
            if(this.documentTypeNotificationPreferences == null) {
                this.documentTypeNotificationPreferences = new HashMap<String, String>();
            }
            return this.documentTypeNotificationPreferences;
        }

        public synchronized void setDocumentTypeNotificationPreferences(Map<String, String> documentTypeNotificationPreferences) {
            this.documentTypeNotificationPreferences = documentTypeNotificationPreferences;
        }
        
        public synchronized void addDocumentTypeNotificationPreference(String documentType, String preference) {
            this.getDocumentTypeNotificationPreferences().put(documentType, preference);
        }
        
        public synchronized void removeDocumentTypeNotificationPreference(String documentType) {
            this.getDocumentTypeNotificationPreferences().remove(documentType);
        }
    }

    static class Constants {
        static final String ROOT_ELEMENT_NAME = "preferences";
        static final String TYPE_NAME = "PreferencesType";
        static final String PREFERENCES_YES_VAL = "yes";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {

        static final String REQUIRES_SAVE = "requiresSave";
        static final String EMAIL_NOTIFICATION = "emailNotification";
        static final String NOTIFY_PRIMARY_DELEGATION = "notifyPrimaryDelegation";
        static final String NOTIFY_SECONDARY_DELEGATION = "notifySecondaryDelegation";
        static final String OPEN_NEW_WINDOW = "openNewWindow";
        static final String SHOW_ACTION_REQUESTED = "showActionRequested";
        static final String SHOW_DATE_CREATED = "showDateCreated";
        static final String SHOW_DOCUMENT_STATUS = "showDocumentStatus";
        static final String SHOW_APP_DOC_STATUS = "showAppDocStatus";
        static final String SHOW_DOC_TYPE = "showDocType";
        static final String SHOW_INITIATOR = "showInitiator";
        static final String SHOW_DOC_TITLE = "showDocTitle";
        static final String SHOW_WORKGROUP_REQUEST = "showWorkgroupRequest";
        static final String SHOW_DELEGATOR = "showDelegator";
        static final String SHOW_CLEAR_FYI = "showClearFyi";
        static final String PAGE_SIZE = "pageSize";
        static final String REFRESH_RATE = "refreshRate";
        static final String COLOR_SAVED = "colorSaved";
        static final String COLOR_INITIATED = "colorInitiated";
        static final String COLOR_DISAPPROVED = "colorDisapproved";
        static final String COLOR_ENROUTE = "colorEnroute";
        static final String COLOR_APPROVED = "colorApproved";
        static final String COLOR_FINAL = "colorFinal";
        static final String COLOR_DISAPPROVE_CANCEL = "colorDisapproveCancel";
        static final String COLOR_PROCESSED = "colorProcessed";
        static final String COLOR_EXCEPTION = "colorException";
        static final String COLOR_CANCELED = "colorCanceled";
        static final String DELEGATOR_FILTER = "delegatorFilter";
        static final String USE_OUTBOX = "useOutbox";
        static final String SHOW_DATE_APPROVED = "showDateApproved";
        static final String SHOW_CURRENT_NODE = "showCurrentNode";
        static final String PRIMARY_DELEGATE_FILTER = "primaryDelegateFilter";
        static final String NOTIFY_ACKNOWLEDGE = "notifyAcknowledge";
        static final String NOTIFY_APPROVE = "notifyApprove";
        static final String NOTIFY_COMPLETE = "notifyCompelte";
        static final String NOTIFY_FYI = "notifyFYI";
        static final String DOCUMENT_TYPE_NOTIFICATION_PREFERENCES = "documentTypeNotificationPreferences";
        static final String DOCUMENT_TYPE_NOTIFICATION_PREFERENCE_MAP = "documentTypeNotificationPreferenceMap";
    }

    public static class KEYS {
        public static final String COLOR_DISAPPROVED = "DOCUMENT_STATUS_COLOR_D";
        public static final String COLOR_DISAPPROVE_CANCEL = "DOCUMENT_STATUS_COLOR_C";
        public static final String COLOR_APPROVED = "DOCUMENT_STATUS_COLOR_A";
        public static final String COLOR_CANCELED = "DOCUMENT_STATUS_COLOR_X";
        public static final String COLOR_SAVED = "DOCUMENT_STATUS_COLOR_S";
        public static final String COLOR_ENROUTE = "DOCUMENT_STATUS_COLOR_R";
        public static final String COLOR_PROCESSED = "DOCUMENT_STATUS_COLOR_P";
        public static final String COLOR_INITIATED = "DOCUMENT_STATUS_COLOR_I";
        public static final String COLOR_FINAL = "DOCUMENT_STATUS_COLOR_F";
        public static final String COLOR_EXCEPTION = "DOCUMENT_STATUS_COLOR_E";
        public static final String REFRESH_RATE = "REFRESH_RATE";
        public static final String OPEN_NEW_WINDOW = "OPEN_ITEMS_NEW_WINDOW";
        public static final String SHOW_DOC_TYPE = "DOC_TYPE_COL_SHOW_NEW";
        public static final String SHOW_DOC_TITLE = "TITLE_COL_SHOW_NEW";
        public static final String SHOW_ACTION_REQUESTED = "ACTION_REQUESTED_COL_SHOW_NEW";
        public static final String SHOW_INITIATOR = "INITIATOR_COL_SHOW_NEW";
        public static final String SHOW_DELEGATOR = "DELEGATOR_COL_SHOW_NEW";
        public static final String SHOW_DATE_CREATED = "DATE_CREATED_COL_SHOW_NEW";
        public static final String SHOW_DOCUMENT_STATUS = "DOCUMENT_STATUS_COL_SHOW_NEW";
        public static final String SHOW_APP_DOC_STATUS = "APP_DOC_STATUS_COL_SHOW_NEW";
        public static final String SHOW_GROUP_REQUEST = "WORKGROUP_REQUEST_COL_SHOW_NEW";
        public static final String SHOW_CLEAR_FYI = "CLEAR_FYI_COL_SHOW_NEW";
        public static final String PAGE_SIZE = "ACTION_LIST_SIZE_NEW";
        public static final String EMAIL_NOTIFICATION = "EMAIL_NOTIFICATION";
        public static final String NOTIFY_PRIMARY_DELEGATION = "EMAIL_NOTIFY_PRIMARY";
        public static final String NOTIFY_SECONDARY_DELEGATION = "EMAIL_NOTIFY_SECONDARY";
        public static final String DEFAULT_COLOR = "white";
        public static final String DEFAULT_ACTION_LIST_SIZE = "10";
        public static final String DEFAULT_REFRESH_RATE = "15";
        public static final String ERR_KEY_REFRESH_RATE_WHOLE_NUM = "preferences.refreshRate";
        public static final String ERR_KEY_ACTION_LIST_PAGE_SIZE_WHOLE_NUM = "preferences.pageSize";
        public static final String DELEGATOR_FILTER = "DELEGATOR_FILTER";
        public static final String PRIMARY_DELEGATE_FILTER = "PRIMARY_DELEGATE_FILTER";
        public static final String USE_OUT_BOX = "USE_OUT_BOX";
        public static final String SHOW_DATE_APPROVED = "LAST_APPROVED_DATE_COL_SHOW_NEW";
        public static final String SHOW_CURRENT_NODE = "CURRENT_NODE_COL_SHOW_NEW";
        public static final String NOTIFY_ACKNOWLEDGE = "NOTIFY_ACKNOWLEDGE";
        public static final String NOTIFY_APPROVE = "NOTIFY_APPROVE";
        public static final String NOTIFY_COMPLETE = "NOTIFY_COMPLETE";
        public static final String NOTIFY_FYI = "NOTIFY_FYI";
        public static final String DOCUMENT_TYPE_NOTIFICATION_PREFERENCES = "DOCUMENT_TYPE_NOTIFICATION_PREFERENCES";
    }

    public static class Cache {
        public static final String NAME = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0 + "/" + Preferences.Constants.TYPE_NAME;
    }

}
