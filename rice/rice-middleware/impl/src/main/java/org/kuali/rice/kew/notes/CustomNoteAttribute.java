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
package org.kuali.rice.kew.notes;

import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.krad.UserSession;


/**
 * An attribute which allows for customization of the Notes interface for
 * a particular document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface CustomNoteAttribute {

    public boolean isAuthorizedToEditNote(Note note) throws Exception;
    public boolean isAuthorizedToAddNotes() throws Exception;
    public boolean isAuthorizedToRetrieveAttachments() throws Exception;
    public Document getRouteHeaderVO();
    public void setRouteHeaderVO(Document routeHeaderVO);
    public UserSession getUserSession();
    public void setUserSession(UserSession workflowUserSession);
    
}
