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

 
public class PreferencesConstants {

    public static final class EmailNotificationPreferences {
        public static final String NONE = "no";
        public static final String DAILY= "daily";
        public static final String WEEKLY= "weekly";
        public static final String IMMEDIATE = "immediate";

        public static List<String> getEmailNotificationPreferences(){
            List<String> emailNotifications = new ArrayList<String>();
            emailNotifications.add(NONE);
            emailNotifications.add(DAILY);
            emailNotifications.add(WEEKLY);
            emailNotifications.add(IMMEDIATE);
            return emailNotifications;
        }
    }

    public static class DelegatorFilterValues {
	    public static final String SECONDARY_DELEGATORS_ONLY_ON_FILTER_PAGE = "Secondary Delegators only on Filter Page";
	    public static final String SECONDARY_DELEGATORS_ON_ACTION_LIST_PAGE = "Secondary Delegators on Action List Page";
	
	    public static List<String> getDelegatorFilterValues() {
		    List<String> delegatorFilterValues = new ArrayList<String>();
		    delegatorFilterValues.add(SECONDARY_DELEGATORS_ONLY_ON_FILTER_PAGE);
		    delegatorFilterValues.add(SECONDARY_DELEGATORS_ON_ACTION_LIST_PAGE);
		    return delegatorFilterValues;
	    }
    }

    public static class PrimaryDelegateFilterValues {
	    public static final String PRIMARY_DELEGATES_ONLY_ON_FILTER_VALUES = "Primary Delegates only on Filter Page";
	    public static final String PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE = "Primary Delegates on Action List Page";

	    public static List<String> getPrimaryDelegateFilterValues() {
		    List<String> primaryDelegateFilterValues = new ArrayList<String>();
		    primaryDelegateFilterValues.add(PRIMARY_DELEGATES_ONLY_ON_FILTER_VALUES);
		    primaryDelegateFilterValues.add(PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE);
		    return primaryDelegateFilterValues;
	    }
    }

    public static class CheckBoxValues {
    	public static final String YES = "yes";
	    public static final String NO = "no";
	
	    public static List<String> getCheckBoxValues() {
		    List<String> checkboxValues = new ArrayList<String>();
    		checkboxValues.add(YES);
	    	checkboxValues.add(NO);
		    return checkboxValues;
	    }
    }

    public static class PreferencesDocumentRouteStatusColors {
	    public static final String ORANGE = "orange";
	    public static final String RED = "red";
	    public static final String PURPLE = "purple";
	    public static final String BLUE = "blue";
	    public static final String GREEN = "green";
	    public static final String SLATE = "slate";
	    public static final String WHITE = "white";
	    public static final String PINK = "pink";
	    public static final String YELLOW = "yellow";
	    public static final String AQUA = "aqua";
	    public static final String TAN = "tan";
	
    	public static List<String> getPreferencesDocumentRouteStatusColors() {
	    	List<String> colors = new ArrayList<String>();
		    colors.add(ORANGE);
		    colors.add(RED);
		    colors.add(PURPLE);
		    colors.add(BLUE);
		    colors.add(GREEN);
		    colors.add(SLATE);
		    colors.add(WHITE);
		    colors.add(PINK);
		    colors.add(YELLOW);
		    colors.add(AQUA);
		    colors.add(TAN);
		    return colors;
	    }
    }
}
