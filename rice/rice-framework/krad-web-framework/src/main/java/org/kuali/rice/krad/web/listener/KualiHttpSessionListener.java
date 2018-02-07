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
package org.kuali.rice.krad.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.document.authorization.PessimisticLock;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

/**
 * Used to handle session timeouts where {@link PessimisticLock} objects should
 * be removed from a document
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiHttpSessionListener implements HttpSessionListener {

    /**
     * HttpSession hook for additional setup method when sessions are created
     *
     * @param se - the HttpSessionEvent containing the session
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent se) {
        // no operation required at this time
    }

    /**
     * HttpSession hook for additional cleanup when sessions are destroyed
     *
     * @param se - the HttpSessionEvent containing the session
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        releaseLocks();
    }

    /**
     * Remove any locks that the user has for this session
     */
    private void releaseLocks() {
        UserSession userSession = GlobalVariables.getUserSession();

        if (userSession == null) return;

        String sessionId = userSession.getKualiSessionId();
        List<PessimisticLock> locks = KRADServiceLocatorWeb.getPessimisticLockService().getPessimisticLocksForSession(
                sessionId);
        Person user = userSession.getPerson();

        KRADServiceLocatorWeb.getPessimisticLockService().releaseAllLocksForUser(locks, user);
    }

}

