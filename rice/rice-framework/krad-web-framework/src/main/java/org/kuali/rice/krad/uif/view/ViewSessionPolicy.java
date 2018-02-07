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
package org.kuali.rice.krad.uif.view;

import org.kuali.rice.krad.datadictionary.DictionaryBeanBase;

import java.io.Serializable;

/**
 * Holds configuration related to session handling of a view (and its related form)
 *
 * <p>
 * The framework will keep track of the session for which a view is rendered in. When a request such as a
 * post is made, the session id for the view will be compared against the current session. If different, or no
 * session exists, a timeout will be assumed and the framework will take the action configured on this
 * policy
 * </p>
 *
 * <p>
 * If none of the options are set here, the framework will allow a request after a timeout to go uninterrupted
 * </p>
 *
 * <p>
 * Notes carrying out the configured view session policy requires the filter
 * {@link org.kuali.rice.krad.web.filter.UifSessionTimeoutFilter} to be configured first in the list of filters
 * for the servlet
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ViewSessionPolicy extends DictionaryBeanBase implements Serializable {
    private static final long serialVersionUID = -5187545712142535662L;

    private boolean redirectToHome;
    private String redirectUrl;
    private boolean renderTimeoutView;
    private boolean enableTimeoutWarning;
    private int timeoutWarningSeconds;

    public ViewSessionPolicy() {
        super();

        timeoutWarningSeconds = 120;
    }

    /**
     * Indicates when a session timeout occurs the user should be redirect to the application home url
     * (determined by the configuration parameter 'application.url')
     *
     * @return true if the user should be redirected to the home URL
     */
    public boolean isRedirectToHome() {
        return redirectToHome;
    }

    /**
     * Setter for indicating whether the user should be redirected to the home URL on session timeout
     *
     * @param redirectToHome
     */
    public void setRedirectToHome(boolean redirectToHome) {
        this.redirectToHome = redirectToHome;
    }

    /**
     * URL the user should be redirected to when a session timeout occurs
     *
     * @return url to redirect user to
     */
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Setter for the URL to redirect the user to when a session timeout occurs
     *
     * @param redirectUrl
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * Indicates the user should be shown the timeout message view when a session timeout occurs
     *
     * @return true if the timeout view should be shown on session timeout
     */
    public boolean isRenderTimeoutView() {
        return renderTimeoutView;
    }

    /**
     * Setter to indicate the timeout view should be shown on session timeout
     *
     * @param renderTimeoutView
     */
    public void setRenderTimeoutView(boolean renderTimeoutView) {
        this.renderTimeoutView = renderTimeoutView;
    }

    /**
     * Enables the session timeout warning dialog and timeout dialog for the view
     *
     * <p>
     * When enabled, a timer will be kept on the client to warning the user when their session is about
     * to timeout, and if the timeout actually occurs. The amount of time before a timeout to warn is specified
     * by {@link #getTimeoutWarningSeconds()}
     * </p>
     *
     * <p>
     * The dialogs shown for the warning and timeout are configured by the dialog groups with ids
     * {@link org.kuali.rice.krad.uif.util.ComponentFactory#SESSION_TIMEOUT_WARNING_DIALOG} and
     * {@link org.kuali.rice.krad.uif.util.ComponentFactory#SESSION_TIMEOUT_DIALOG}
     * </p>
     *
     * @return true if the timeout warning dialog should be enabled
     */
    public boolean isEnableTimeoutWarning() {
        return enableTimeoutWarning;
    }

    /**
     * Setter for enabling the session timeout warning dialog
     *
     * @param enableTimeoutWarning
     */
    public void setEnableTimeoutWarning(boolean enableTimeoutWarning) {
        this.enableTimeoutWarning = enableTimeoutWarning;
    }

    /**
     * When {@link #isEnableTimeoutWarning()} is true, the number of seconds before a timeout occurs to give a
     * warning (default is 120 (2 minutes))
     *
     * @return number of seconds before timeout to give warning dialog
     */
    public int getTimeoutWarningSeconds() {
        return timeoutWarningSeconds;
    }

    /**
     * Setter for the number of seconds before timeout to give a warning dialog
     *
     * @param timeoutWarningSeconds
     */
    public void setTimeoutWarningSeconds(int timeoutWarningSeconds) {
        this.timeoutWarningSeconds = timeoutWarningSeconds;
    }
}
