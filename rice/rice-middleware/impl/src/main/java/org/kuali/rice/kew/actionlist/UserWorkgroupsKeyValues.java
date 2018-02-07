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
package org.kuali.rice.kew.actionlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.kew.actionlist.ActionListForm;

public class UserWorkgroupsKeyValues extends UifKeyValuesFinderBase {

    private boolean blankOption;


    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        ActionListForm actionListForm = (ActionListForm)model;
        List<String> userWorkgroups =
                KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(actionListForm.getUser());

        //note that userWorkgroups is unmodifiable so we need to create a new list that we can sort
        List<String> userGroupsToSort = new ArrayList<String>(userWorkgroups);

        List<KeyValue> sortedUserWorkgroups = new ArrayList<KeyValue>();
        KeyValue keyValue = null;
        keyValue = new ConcreteKeyValue(KewApiConstants.NO_FILTERING, KewApiConstants.NO_FILTERING);
        sortedUserWorkgroups.add(keyValue);

        if (userGroupsToSort != null && userGroupsToSort.size() > 0) {
            Collections.sort(userGroupsToSort);
            Group group;

            for (String groupId : userGroupsToSort)
            {
                group = KimApiServiceLocator.getGroupService().getGroup(groupId);
                keyValue = new ConcreteKeyValue(groupId, group.getName());
                sortedUserWorkgroups.add(keyValue);
            }
        }

        return sortedUserWorkgroups;
    }

    /**
     * @return the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * @param blankOption the blankOption to set
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

//    private List<? extends KeyValue> getUserWorkgroupsDropDownList(String principalId) {
//        List<String> userWorkgroups =
//                KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId);
//
//        //note that userWorkgroups is unmodifiable so we need to create a new list that we can sort
//        List<String> userGroupsToSort = new ArrayList<String>(userWorkgroups);
//
//        List<KeyValue> sortedUserWorkgroups = new ArrayList<KeyValue>();
//        KeyValue keyValue = null;
//        keyValue = new ConcreteKeyValue(KewApiConstants.NO_FILTERING, KewApiConstants.NO_FILTERING);
//        sortedUserWorkgroups.add(keyValue);
//        if (userGroupsToSort != null && userGroupsToSort.size() > 0) {
//            Collections.sort(userGroupsToSort);
//
//            Group group;
//            for (String groupId : userGroupsToSort)
//            {
//                group = KimApiServiceLocator.getGroupService().getGroup(groupId);
//                keyValue = new ConcreteKeyValue(groupId, group.getName());
//                sortedUserWorkgroups.add(keyValue);
//            }
//        }
//        return sortedUserWorkgroups;
//    }


}
