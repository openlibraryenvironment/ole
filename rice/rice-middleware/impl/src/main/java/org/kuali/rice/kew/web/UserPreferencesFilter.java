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
package org.kuali.rice.kew.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.preferences.Preferences;
import org.kuali.rice.kew.api.preferences.PreferencesService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.KRADUtils;

/**
 * This class establishes and initializes the KEW Preferences after a user logs in.
 * 
 * <p>
 * This filter assumes that a UserSession is already established.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UserPreferencesFilter implements Filter {
	
	private static final Log LOG = LogFactory.getLog(UserPreferencesFilter.class);

	private FilterConfig filterConfig;
	private PreferencesService preferencesService;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.filterConfig = config;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}
	
	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		final UserSession session = KRADUtils.getUserSessionFromRequest(request);
		
		if (session == null) {
			throw new IllegalStateException("A user session has not been established");
		}
		
		final String principalId = session.getPrincipalId();
		
		if (session.retrieveObject(KewApiConstants.PREFERENCES) == null) {
			final Preferences preferences = retrievePreferences(principalId);
			session.addObject(KewApiConstants.PREFERENCES, preferences);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		filterConfig = null;
	}

    private Preferences retrievePreferences(String principalId) {
    	Preferences preferences = this.getPreferenceService().getPreferences(principalId);
        if (preferences.isRequiresSave()) {
            LOG.info("Detected that user preferences require saving.");
            this.getPreferenceService().savePreferences(principalId, preferences);
            preferences = this.getPreferenceService().getPreferences(principalId);
        }
        
        return preferences;
    }
    
    
    private PreferencesService getPreferenceService() {
    	if (this.preferencesService == null) {
    		this.preferencesService = KewApiServiceLocator.getPreferencesService();
    	}
    	
    	return this.preferencesService;
    }
    
}
