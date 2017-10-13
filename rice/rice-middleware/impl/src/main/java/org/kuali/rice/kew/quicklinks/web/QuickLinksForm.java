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
package org.kuali.rice.kew.quicklinks.web;

import org.kuali.rice.kns.web.struts.form.KualiForm;

import java.util.List;


/**
 * Struts ActionForm for the {@link QuickLinksAction}.
 *
 * @see QuickLinksAction
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class QuickLinksForm extends KualiForm {

	private static final long serialVersionUID = 7937908655502925150L;
	private List watchedDocuments;
    private List recentSearches;
    private List namedSearches;
    private List actionListStats;
    private List initiatedDocumentTypes;
    private String namedSearch = "";

    /**
     * @return Returns the actionListStats.
     */
    public List getActionListStats() {
        return actionListStats;
    }
    /**
     * @param actionListStats The actionListStats to set.
     */
    public void setActionListStats(List actionListStats) {
        this.actionListStats = actionListStats;
    }
    /**
     * @return Returns the initiatedDocumentTypes.
     */
    public List getInitiatedDocumentTypes() {
        return initiatedDocumentTypes;
    }
    /**
     * @param initiatedDocumentTypes The initiatedDocumentTypes to set.
     */
    public void setInitiatedDocumentTypes(List initiatedDocumentTypes) {
        this.initiatedDocumentTypes = initiatedDocumentTypes;
    }
    /**
     * @return Returns the namedSearches.
     */
    public List getNamedSearches() {
        return namedSearches;
    }
    /**
     * @param namedSearches The namedSearches to set.
     */
    public void setNamedSearches(List namedSearches) {
        this.namedSearches = namedSearches;
    }
    /**
     * @return Returns the recentSearches.
     */
    public List getRecentSearches() {
        return recentSearches;
    }
    /**
     * @param recentSearches The recentSearches to set.
     */
    public void setRecentSearches(List recentSearches) {
        this.recentSearches = recentSearches;
    }
    /**
     * @return Returns the watchedDocuments.
     */
    public List getWatchedDocuments() {
        return watchedDocuments;
    }
    /**
     * @param watchedDocuments The watchedDocuments to set.
     */
    public void setWatchedDocuments(List watchedDocuments) {
        this.watchedDocuments = watchedDocuments;
    }
	/**
	 * @return the namedSearch
	 */
	public String getNamedSearch() {
		return this.namedSearch;
	}
	/**
	 * @param namedSearch the namedSearch to set
	 */
	public void setNamedSearch(String namedSearch) {
		this.namedSearch = namedSearch;
	}
}
