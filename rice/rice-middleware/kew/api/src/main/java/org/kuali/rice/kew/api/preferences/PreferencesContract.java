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

import java.util.Map;

/**
 * A contract defining the method for a {@link Preferences} model object and its data transfer object equivalent.
 *
 * @see Preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PreferencesContract {

    boolean isRequiresSave();

    String getEmailNotification();

    String getNotifyPrimaryDelegation();

    String getNotifySecondaryDelegation();

    String getOpenNewWindow();

    String getShowActionRequested();

    String getShowDateCreated();

    String getShowDocumentStatus();

    String getShowAppDocStatus();

    String getShowDocType();

    String getShowInitiator();

    String getShowDocTitle();

    String getShowWorkgroupRequest();

    String getShowDelegator();

    String getShowClearFyi();

    String getPageSize();

    String getRefreshRate();

    String getColorSaved();

    String getColorInitiated();

    String getColorDisapproved();

    String getColorEnroute();

    String getColorApproved();

    String getColorFinal();

    String getColorDisapproveCancel();

    String getColorProcessed();

    String getColorException();

    String getColorCanceled();

    String getDelegatorFilter();

    String getUseOutbox();

    String getShowDateApproved();

    String getShowCurrentNode();

    String getPrimaryDelegateFilter();
    
    String getNotifyAcknowledge();
    
    String getNotifyApprove();
    
    String getNotifyComplete();
    
    String getNotifyFYI();
    
    Map<String, String> getDocumentTypeNotificationPreferences();

}
