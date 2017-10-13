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
package org.kuali.rice.kew.actionlist.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.comparators.ComparableComparator;
import org.kuali.rice.kew.actionrequest.Recipient;
import org.kuali.rice.kew.util.WebFriendlyRecipient;

/**
 * Internal Utility class for Action Lists.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class ActionListUtil {
	
	private ActionListUtil() {
		throw new UnsupportedOperationException("do not call");
	}

    /**
     * Converts a collection of Recipients into a collection of WebFriendlyRecipients which can be displayed in the UI
     * @param recipients recipients to convert
     * @return a collection of WebFriendlyRecipients which can be displayed in the UI
     */
    public static List<WebFriendlyRecipient> getWebFriendlyRecipients(Collection<Recipient> recipients) {
        Collection<WebFriendlyRecipient> newRecipients = new ArrayList<WebFriendlyRecipient>(recipients.size());
        for (Recipient recipient : recipients) {
            newRecipients.add(new WebFriendlyRecipient(recipient));
        }
        List<WebFriendlyRecipient> recipientList = new ArrayList<WebFriendlyRecipient>(newRecipients);
        Collections.sort(recipientList, new Comparator<WebFriendlyRecipient>() {
            Comparator<String> comp = new ComparableComparator();

            @Override
			public int compare(WebFriendlyRecipient o1, WebFriendlyRecipient o2) {
                return comp.compare(o1.getDisplayName().trim().toLowerCase(), o2.getDisplayName().trim().toLowerCase());
            }
        });
        return recipientList;
    }
}
