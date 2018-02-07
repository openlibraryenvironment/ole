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
package org.kuali.rice.kew.impl.actionlist;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actionitem.ActionItemActionListExtension;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.actionlist.ActionListService;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Reference implementation of the {@link org.kuali.rice.kew.api.actionlist.ActionListService} api.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ActionListServiceNewImpl implements ActionListService {

    @Override
    public Integer getUserActionItemCount(String principalId) {
        incomingParamCheck(principalId, "principalId");
        return Integer.valueOf(KEWServiceLocator.getActionListService().getCount(principalId));
    }

    @Override
    public List<ActionItem> getActionItemsForPrincipal(String principalId) {
        incomingParamCheck(principalId, "principalId");
        Collection<ActionItemActionListExtension> actionItems
                = KEWServiceLocator.getActionListService().getActionList(principalId, null);
        List<ActionItem> actionItemVOs = new ArrayList<ActionItem>(actionItems.size());
        for (org.kuali.rice.kew.actionitem.ActionItem actionItem : actionItems) {
            actionItemVOs.add(org.kuali.rice.kew.actionitem.ActionItem.to(actionItem));
        }
        return actionItemVOs;
    }

    @Override
    public List<ActionItem> getAllActionItems(String documentId) {
        incomingParamCheck(documentId, "documentId");
        Collection<ActionItemActionListExtension> actionItems
                = KEWServiceLocator.getActionListService().getActionListForSingleDocument(documentId);
        List<ActionItem> actionItemVOs = new ArrayList<ActionItem>(actionItems.size());
        for (org.kuali.rice.kew.actionitem.ActionItem actionItem : actionItems) {
            actionItemVOs.add(org.kuali.rice.kew.actionitem.ActionItem.to(actionItem));
        }
        return actionItemVOs;
    }

    @Override
    public List<ActionItem> getActionItems(String documentId, List<String> actionRequestedCodes) {
        incomingParamCheck(documentId, "documentId");
        List<ActionItem> actionItems = getAllActionItems(documentId);
        List<ActionItem> matchingActionitems = new ArrayList<ActionItem>();
        for (ActionItem actionItemVO : actionItems) {
            if (actionRequestedCodes.contains(actionItemVO.getActionRequestCd())) {
                matchingActionitems.add(actionItemVO);
            }
        }
        return matchingActionitems;
    }

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }
}
