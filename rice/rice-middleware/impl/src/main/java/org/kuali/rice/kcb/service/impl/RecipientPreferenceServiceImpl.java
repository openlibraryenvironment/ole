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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kcb.bo.RecipientDelivererConfig;
import org.kuali.rice.kcb.bo.RecipientPreference;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.exception.ErrorList;
import org.kuali.rice.kcb.service.RecipientPreferenceService;

/**
 * RecipientPreferenceService implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RecipientPreferenceServiceImpl extends BusinessObjectServiceImpl implements RecipientPreferenceService {
    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#getRecipientPreference(java.lang.String, java.lang.String)
     */
    public RecipientPreference getRecipientPreference(String recipientId, String key) {
        Map<String, String> fields = new HashMap<String, String>(2);
        fields.put(RecipientPreference.RECIPIENT_FIELD, recipientId);
        fields.put(RecipientPreference.PROPERTY_FIELD, key);

        Collection<RecipientPreference> prefs = dao.findMatching(RecipientPreference.class, fields);
        assert(prefs.size() <= 1);
        
        if (prefs.size() > 0) {
            return prefs.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#deleteRecipientPreference(org.kuali.rice.kcb.bo.RecipientPreference)
     */
    public void deleteRecipientPreference(RecipientPreference pref) {
        dao.delete(pref);
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#getRecipientPreferences(java.lang.String)
     */
    public HashMap<String, String> getRecipientPreferences(String recipientId) {
        Map<String, String> fields = new HashMap<String, String>(1);
        fields.put(RecipientPreference.RECIPIENT_FIELD, recipientId);
        HashMap<String, String> prefs = new HashMap<String,String>();
        Collection<RecipientPreference> userPrefs =  dao.findMatching(RecipientPreference.class, fields);
        for (RecipientPreference p: userPrefs) {
            prefs.put(p.getProperty(), p.getValue());
        }

        return prefs;
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#saveRecipientPreference(org.kuali.rice.kcb.bo.RecipientPreference)
     */
    public void saveRecipientPreference(RecipientPreference pref) {
        dao.save(pref);
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#saveRecipientPreferences(java.lang.String, java.util.HashMap, org.kuali.rice.kcb.deliverer.MessageDeliverer)
     */
    public void saveRecipientPreferences(String recipientId, HashMap<String, String> prefs, MessageDeliverer deliverer) throws ErrorList {
        deliverer.validatePreferenceValues(prefs);         
        
        for (Map.Entry<String, String> entry: prefs.entrySet()) {
           String prop = entry.getKey();
           String value = entry.getValue();
               
           // We need to check if this property is already set
           // for the user by checking doing a unique key query...if
           // it already exists, update, otherwise add it 
           RecipientPreference currentPreference = getRecipientPreference(recipientId, prop);
           if (currentPreference != null) {
              currentPreference.setRecipientId(recipientId);
              currentPreference.setProperty(prop);
              currentPreference.setValue(value);
              dao.save(currentPreference);
           } else {
              RecipientPreference recipientPreference = new RecipientPreference();
              recipientPreference.setRecipientId(recipientId);
              recipientPreference.setProperty(prop);
              recipientPreference.setValue(value);
              dao.save(recipientPreference);
           }
        }
    }

    // deliverer config
    
    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#removeRecipientDelivererConfigs(java.lang.String)
     */
    public void removeRecipientDelivererConfigs(String recipientId) {
        Map<String, String> fields = new HashMap<String, String>(1);
        fields.put(RecipientDelivererConfig.RECIPIENT_ID, recipientId);
        dao.deleteMatching(RecipientDelivererConfig.class, fields);
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#saveRecipientDelivererConfig(java.lang.String, java.lang.String, java.lang.String[])
     */
    public void saveRecipientDelivererConfig(String recipientId, String delivererName, String[] channels) {
        if (channels == null || channels.length == 0) return;
    
        // if selected[0] is 0 we want to remove this deliverer
        // for all channels.  We already did that above.
        for (String channel: channels) {
            RecipientDelivererConfig config = new RecipientDelivererConfig();

            config.setRecipientId(recipientId);
            config.setDelivererName(delivererName);
            config.setChannel(channel);
            
            // first, verify that we aren't trying to insert a duplicate
            Collection<RecipientDelivererConfig> deliverers = getDeliverersForRecipientAndChannel(recipientId, channel);
            if (deliverers != null) {
            	for (RecipientDelivererConfig deliverer : deliverers) {
            		if (deliverer.getDelivererName().equals(delivererName)) {
            			throw new RiceRuntimeException("Attempting to save a duplicate Recipient Deliverer Config.");
            		}
            	}
            }
            dao.save(config);
        }
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#getDeliverersForRecipient(java.lang.String)
     */
    public Collection<RecipientDelivererConfig> getDeliverersForRecipient(String recipientId) {
        Map<String, String> fields = new HashMap<String, String>(1);
        fields.put(RecipientDelivererConfig.RECIPIENT_ID, recipientId);
        return dao.findMatching(RecipientDelivererConfig.class, fields);
    }

    /**
     * @see org.kuali.rice.kcb.service.RecipientPreferenceService#getDeliverersForRecipientAndChannel(java.lang.String, java.lang.String)
     */
    public Collection<RecipientDelivererConfig> getDeliverersForRecipientAndChannel(String recipientId, String channel) {
        Map<String, String> fields = new HashMap<String, String>(1);
        fields.put(RecipientDelivererConfig.RECIPIENT_ID, recipientId);
        fields.put(RecipientDelivererConfig.CHANNEL, channel);

        return dao.findMatching(RecipientDelivererConfig.class, fields);
    }
}
