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
package org.kuali.rice.kew.notification.service.impl;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kcb.api.message.MessageDTO;
import org.kuali.rice.kcb.api.service.KCBServiceNames;
import org.kuali.rice.kcb.api.service.MessagingService;
import org.kuali.rice.kcb.util.KCBConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionItem;
import org.kuali.rice.kew.api.KewApiConstants;


/**
 * NotificationService implementation that delegates to KCB
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KCBNotificationService extends DefaultNotificationService {
    @Override
    protected void sendNotification(ActionItem actionItem) {
        super.sendNotification(actionItem);

        String enableKENNotificationValue = ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.ENABLE_KEN_NOTIFICATION);
        boolean enableKENNotification = Boolean.parseBoolean(enableKENNotificationValue);
        // we only send per-user messages to KCB
        if (!enableKENNotification || actionItem.getPrincipalId() != null)
            return;


        // send it off to KCB if available
        MessagingService ms = (MessagingService) GlobalResourceLoader.getService(new QName(KCBConstants.SERVICE_NAMESPACE, KCBServiceNames.KCB_MESSAGING));
        if (ms == null) {
        	LOG.info("Could not locate KCB MessagingService.  Message will not be forwarded to the KCB.");
        	return;
        }
        MessageDTO mvo = new MessageDTO();
        mvo.setChannel("KEW");
        mvo.setContent("i'm a kew notification");
        mvo.setContentType("KEW notification");
        mvo.setDeliveryType(actionItem.getActionRequestCd());
        mvo.setProducer("kew@localhost");
        mvo.setTitle(actionItem.getDocLabel() + " - " + actionItem.getDocName() + " - " + actionItem.getDocTitle());
        if (StringUtils.isNotBlank(actionItem.getDocHandlerURL())) {
        	mvo.setUrl(actionItem.getDocHandlerURL() + "?docId=" + actionItem.getDocumentId());
        }
        mvo.setOriginId(String.valueOf(actionItem.getId()));
        try {
            // just assume it's a user at this point...
            mvo.setRecipient(actionItem.getPrincipalId());
            LOG.debug("Sending message to KCB: " + mvo);
            ms.deliver(mvo);
        } catch (Exception e) {
            throw new WorkflowRuntimeException("could not deliver message to KCB", e);
        }
    }

    @Override
    public void removeNotification(List<ActionItem> actionItems) {
    	String enableKENNotificationValue = ConfigContext.getCurrentContextConfig().getProperty(KewApiConstants.ENABLE_KEN_NOTIFICATION);
        boolean enableKENNotification = Boolean.parseBoolean(enableKENNotificationValue);
        if (!enableKENNotification) {
        	return;
        }
        MessagingService ms = (MessagingService) GlobalResourceLoader.getService(new QName(KCBConstants.SERVICE_NAMESPACE, KCBServiceNames.KCB_MESSAGING));

        for (ActionItem actionItem: actionItems) {
        	LOG.debug("Removing KCB messages for action item: " + actionItem.getId() + " " + actionItem.getActionRequestCd() + " " + actionItem.getPrincipalId());
            try {
                // we don't have the action takens at this point...? :(
                ms.removeByOriginId(String.valueOf(actionItem.getId()), null, null);
            } catch (Exception e) {
                throw new RuntimeException("could not remove message from KCB", e);
            }
        }
    }
}
