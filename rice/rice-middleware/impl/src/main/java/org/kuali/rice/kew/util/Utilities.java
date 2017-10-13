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
package org.kuali.rice.kew.util;

import org.apache.commons.lang.text.StrSubstitutor;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Various static utility methods.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class Utilities {
    /**
     * Commons-Lang StrSubstitor which substitutes variables specified like ${name} in strings,
     * using a lookup implementation that pulls variables from the core config
     */
    private static final StrSubstitutor SUBSTITUTOR = new StrSubstitutor(new ConfigStringLookup());
    
    private Utilities() {
    	throw new UnsupportedOperationException("do not call");
    }

    /**
     * Performs variable substitution on the specified string, replacing variables specified like ${name}
     * with the value of the corresponding config parameter obtained from the current context Config object.
     * This version of the method also takes an application id to qualify the parameter.
     * @param applicationId the application id to use for qualifying the parameter
     * @param string the string on which to perform variable substitution
     * @return a string with any variables substituted with configuration parameter values
     */
    public static String substituteConfigParameters(String applicationId, String string) {
    	StrSubstitutor sub = new StrSubstitutor(new ConfigStringLookup(applicationId));
        return sub.replace(string);
    }
        
    /**
     * Performs variable substitution on the specified string, replacing variables specified like ${name}
     * with the value of the corresponding config parameter obtained from the current context Config object
     * @param string the string on which to perform variable substitution
     * @return a string with any variables substituted with configuration parameter values
     */
    public static String substituteConfigParameters(String string) {
        return SUBSTITUTOR.replace(string);
    }

    public static String parseGroupNamespaceCode(String namespaceAndNameCombo) {
        if (namespaceAndNameCombo == null) {
            return null;
        }
        String[] groupData = namespaceAndNameCombo.split(KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER);
        if (groupData.length == 1) {
            return KimConstants.KIM_GROUP_WORKFLOW_NAMESPACE_CODE;
        } else if (groupData.length == 2) {
            return groupData[0].trim();
        } else {
            return null;
        }
    }

    public static String parseGroupName(String namespaceAndNameCombo) {
        if (namespaceAndNameCombo == null) {
            return null;
        }
        String[] groupData = namespaceAndNameCombo.split(KewApiConstants.KIM_GROUP_NAMESPACE_NAME_DELIMITER_CHARACTER);
        if (groupData.length == 1) {
            return groupData[0].trim();
        } else if (groupData.length == 2) {
            return groupData[1].trim();
        } else {
            return null;
        }
    }

    /**
     *
     *	Consider moving out of this class if this bugs
     */
    public static class PrioritySorter implements Comparator<ActionRequestValue> {
        @Override
		public int compare(ActionRequestValue ar1, ActionRequestValue ar2) {
            int value = ar1.getPriority().compareTo(ar2.getPriority());
            if (value == 0) {
                value = ActionRequestValue.compareActionCode(ar1.getActionRequested(), ar2.getActionRequested(), true);
                if (value == 0) {
                    if ( (ar1.getActionRequestId() != null) && (ar2.getActionRequestId() != null) ) {
                        value = ar1.getActionRequestId().compareTo(ar2.getActionRequestId());
                    } else {
                        // if even one action request id is null at this point return that the two are equal
                        value = 0;
                    }
                }
            }
            return value;
        }
    }

    /**
     *
     *	Consider moving out of this class if this bugs
     */
    public static class RouteLogActionRequestSorter extends PrioritySorter implements Comparator<ActionRequestValue> {
        @Override
		public int compare(ActionRequestValue ar1, ActionRequestValue ar2) {
            if (! ar1.getChildrenRequests().isEmpty()) {
                Collections.sort(ar1.getChildrenRequests(), this);
            }
            if (! ar2.getChildrenRequests().isEmpty()) {
                Collections.sort(ar2.getChildrenRequests(), this);
            }

            int routeLevelCompareVal = ar1.getRouteLevel().compareTo(ar2.getRouteLevel());
            if (routeLevelCompareVal != 0) {
                return routeLevelCompareVal;
            }

            if (ar1.isActive() && ar2.isPending()) {
                return -1;
            } else if (ar2.isActive() && ar1.isPending()) {
                return 1;
            }

            return super.compare(ar1, ar2);
        }
    }

    public static boolean checkDateRanges(String fromDate, String toDate) {
        try {
            Date parsedDate = CoreApiServiceLocator.getDateTimeService().convertToDate(fromDate.trim());
            Calendar fromCalendar = Calendar.getInstance();
            fromCalendar.setLenient(false);
            fromCalendar.setTime(parsedDate);
            fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
            fromCalendar.set(Calendar.MINUTE, 0);
            fromCalendar.set(Calendar.SECOND, 0);
            fromCalendar.set(Calendar.MILLISECOND, 0);
            parsedDate = CoreApiServiceLocator.getDateTimeService().convertToDate(toDate.trim());
            Calendar toCalendar = Calendar.getInstance();
            toCalendar.setLenient(false);
            toCalendar.setTime(parsedDate);
            toCalendar.set(Calendar.HOUR_OF_DAY, 0);
            toCalendar.set(Calendar.MINUTE, 0);
            toCalendar.set(Calendar.SECOND, 0);
            toCalendar.set(Calendar.MILLISECOND, 0);
            if (fromCalendar.after(toCalendar)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Helper method that takes a List of {@link KeyValue} and presents it as a Map
     * @param collection collection of {@link KeyValue}
     * @return a Map<String, String> representing the keys and values in the KeyValue collection
     */
    public static <T  extends KeyValue> Map<String, String> getKeyValueCollectionAsMap(List<T> collection) {
        Map<String, String> map = new HashMap<String, String>(collection.size());
        for (KeyValue kv: collection) {
            map.put(kv.getKey(), kv.getValue());
        }
        return map;
    }

    /**
     * Helper method that takes a List of {@link KeyValue} and presents it as a Map containing
     * KeyValue values
     * @param <T> the key type
     * @param collection collection of {@link KeyValue}
     * @return a Map<T,Z> where keys of the KeyValues in the collection are mapped to their respective KeyValue object
     */
    public static <T  extends KeyValue> Map<String, T> getKeyValueCollectionAsLookupTable(List<T> collection) {
        Map<String, T> map = new HashMap<String, T>(collection.size());
        for (T kv: collection) {
            map.put(kv.getKey(), kv);
        }
        return map;
    }
}
