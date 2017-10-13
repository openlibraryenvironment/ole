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
package org.kuali.rice.kcb.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kcb.bo.MessageDelivery;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.deliverer.impl.AOLInstantMessageDeliverer;
import org.kuali.rice.kcb.deliverer.impl.EmailMessageDeliverer;
import org.kuali.rice.kcb.deliverer.impl.MockMessageDeliverer;
import org.kuali.rice.kcb.deliverer.impl.SMSMessageDeliverer;
import org.kuali.rice.kcb.service.MessageDelivererRegistryService;

/**
 * MessageDelivererRegistryService implementation - for now we use a HashMap to do this registration, in the future we'll use resource loading.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MessageDelivererRegistryServiceImpl implements MessageDelivererRegistryService {
    private static Logger LOG = Logger.getLogger(MessageDelivererRegistryServiceImpl.class);

    // holds information about the registered deliverer types
    private HashMap<String, Class<? extends MessageDeliverer>> messageDelivererTypes;

    /**
     * Constructs an instance of the MessageDelivererRegistryServiceImpl class and sets up the 
     * registered MessageDeliverers in the system. These are the hardcoded message deliverers 
     * that we support out of the box.
     * 
     * TODO: we'll need to implement a plugin registry discovery mechanism long term.
     */
    public MessageDelivererRegistryServiceImpl() {
        //KEWActionListMessageDeliverer kewActionList = new KEWActionListMessageDeliverer();
        EmailMessageDeliverer email = new EmailMessageDeliverer();
        SMSMessageDeliverer sms = new SMSMessageDeliverer();
        AOLInstantMessageDeliverer aim = new AOLInstantMessageDeliverer();
        MockMessageDeliverer mock = new MockMessageDeliverer();

        messageDelivererTypes = new HashMap<String, Class<? extends MessageDeliverer>>(4);
        //messageDelivererTypes.put(kewActionList.getName(), kewActionList.getClass());
        messageDelivererTypes.put(email.getName().toLowerCase(), email.getClass());
        messageDelivererTypes.put(sms.getName().toLowerCase(), sms.getClass());
        messageDelivererTypes.put(aim.getName().toLowerCase(), aim.getClass());
        messageDelivererTypes.put(mock.getName().toLowerCase(), mock.getClass());
    }

    /**
     * @see org.kuali.rice.kcb.service.MessageDelivererRegistryService#getAllDelivererTypes()
     */
    public Collection<String> getAllDelivererTypes() {
        Collection<MessageDeliverer> deliverers = getAllDeliverers();
        Set<String> types = new HashSet<String>(deliverers.size());
        for (MessageDeliverer deliverer: deliverers) {
            types.add(deliverer.getName().toLowerCase());
        }
        return types;
    }

    /**
     * Implements by constructing instances of each registered class and adding to an ArrayList that
     * gets passed back to the calling method.
     * @see MessageDelivererRegistryService#getAllDelivererTypes()
     */
    public Collection<MessageDeliverer> getAllDeliverers() {
        ArrayList<MessageDeliverer>  delivererTypes = new ArrayList<MessageDeliverer>();

        Set<Entry<String, Class<? extends MessageDeliverer>>> registeredTypes = messageDelivererTypes.entrySet();

        // iterate over each type and add an instance of each to the returning ArrayList
        for(Entry<String, Class<? extends MessageDeliverer>> entry: registeredTypes ) {
            try {
                delivererTypes.add(entry.getValue().newInstance());
            } catch (InstantiationException e) {
                LOG.error(e.getStackTrace());
            } catch (IllegalAccessException e) {
                LOG.error(e.getStackTrace());
            }
        }

        return delivererTypes;
    }

    /**
     * Implements by calling getDelivererByName for the delivery type name within the messageDelivery object.
     * @see MessageDelivererRegistryService#getDeliverer(MessageDelivery)
     */
    public MessageDeliverer getDeliverer(MessageDelivery messageDelivery) {
        if (messageDelivery == null) {
            throw new RiceIllegalArgumentException("messageDelivery is null");
        }

        MessageDeliverer nmd = getDelivererByName(messageDelivery.getDelivererTypeName());
        if (nmd == null) {
            LOG.error("The message deliverer type ('" + messageDelivery.getDelivererTypeName() + "') " +
                      "associated with message delivery id='" + messageDelivery.getId() + "' was not found in the message deliverer registry.  This deliverer " +
                      "plugin is not in the system.");
        }
        return nmd;
    }

    /**
     * Implements by doing a key lookup in the hashmap that acts as the deliverer plugin registry.  The deliverer name is the key in the hashmap for 
     * all registered deliverers.
     * @see MessageDelivererRegistryService#getDelivererByName(String)
     */
    public MessageDeliverer getDelivererByName(String messageDelivererName) {
        if (StringUtils.isBlank(messageDelivererName)) {
            throw new RiceIllegalArgumentException("messageDelivererName is null or blank");
        }

        Class<? extends MessageDeliverer> clazz = messageDelivererTypes.get(messageDelivererName.toLowerCase());

        if(clazz == null) {
            LOG.error("The message deliverer type ('" + messageDelivererName + "') " +
                      " was not found in the message deliverer registry.  This deliverer " +
                      "plugin is not in the system.");
            return null;
        }

        MessageDeliverer messageDeliverer = null;
        try {
            messageDeliverer = clazz.newInstance();
        } catch (InstantiationException e) {
            LOG.error(e.getStackTrace());
        } catch (IllegalAccessException e) {
            LOG.error(e.getStackTrace());
        }

        return messageDeliverer;
    }
}
