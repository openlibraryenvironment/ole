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
package org.kuali.rice.kcb.web.spring;

import org.apache.log4j.Logger;
import org.kuali.rice.kcb.bo.RecipientDelivererConfig;
import org.kuali.rice.kcb.deliverer.MessageDeliverer;
import org.kuali.rice.kcb.exception.ErrorList;
import org.kuali.rice.kcb.service.KENIntegrationService;
import org.kuali.rice.kcb.service.MessageDelivererRegistryService;
import org.kuali.rice.kcb.service.RecipientPreferenceService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is the controller that handles management of various user preferences interfaces (deliver types, user subscriptions, etc).
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UserPreferencesController extends MultiActionController {
    /** Logger for this class and subclasses */
    private static final Logger LOG = Logger.getLogger(UserPreferencesController.class);

    private static final String VIEW = "DelivererPreferences";
    private static final String KEW_CHANNEL = "KEW"; 
    protected RecipientPreferenceService recipientPreferenceService;
    protected MessageDelivererRegistryService messageDelivererRegistryService;
    protected KENIntegrationService kenIntegrationService;

    /**
     * Set the RecipientPreferenceService
     * @param recipientPreferenceService
     */
    @Required
    public void setRecipientPreferenceService(RecipientPreferenceService userPreferenceService) {
        this.recipientPreferenceService = userPreferenceService;
    }

    /**
     * Set the MessageDelivererRegistryService
     * @param messageDelivererRegistryService
     */
    @Required
    public void setMessageDelivererRegistryService(MessageDelivererRegistryService messageDelivererRegistryService) {
        this.messageDelivererRegistryService = messageDelivererRegistryService;
    }

    /**
     * Sets the KENIntegrationService
     * @param kis the KENIntegrationService
     */
    @Required
    public void setKenIntegrationService(KENIntegrationService kis) {
        this.kenIntegrationService = kis;
    }

    /**
     * @return all channels for Rice, including the builtin KEW action list "channel"
     */
    protected Collection<String> getAllChannels() {
        // TODO: does not traverse bus yet
        Collection<String> allChannels = new ArrayList<String>();
        //allChannels.add(KEW_CHANNEL);
        allChannels.addAll(kenIntegrationService.getAllChannelNames());
        return allChannels;
    }

    /**
     * displayDelivererConfigurationForm - obtain information necessary
     * for displaying all possible Deliverer types and forward to the form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView displayDelivererConfigurationForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userid = request.getRemoteUser();
        LOG.debug("remoteUser: "+userid); 

        // Get DeliveryType classes
        Collection<MessageDeliverer> deliveryTypes = this.messageDelivererRegistryService.getAllDeliverers();

        // get all channels       
        Collection<String> channels = getAllChannels();

        //     get all user preferences in a HashMap
        HashMap<String, String> preferences  = this.recipientPreferenceService.getRecipientPreferences(userid);

        // get existing configured deliverers
        Collection<RecipientDelivererConfig> currentDeliverers = this.recipientPreferenceService.getDeliverersForRecipient(userid);
        // create a Map as an easy way for the JSP to determine whether a deliver is enabled for channels
        Map<String, Boolean> currentDeliverersMap = new HashMap<String, Boolean>();
        for (RecipientDelivererConfig udc: currentDeliverers) {
            String channelName = udc.getChannel();
            currentDeliverersMap.put(udc.getDelivererName() + "." + channelName, Boolean.TRUE);
        }

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("channels", channels);
        model.put("deliveryTypes", deliveryTypes);
        model.put("preferences", preferences);
        model.put("currentDeliverersMap", currentDeliverersMap);
        return new ModelAndView(VIEW, model);
    }

    /**
     * saveDelivererConfiguration - save deliverer configuration data
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ModelAndView saveDelivererConfiguration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userid = request.getRemoteUser();
        LOG.debug("remoteUser: "+userid);
        boolean error = false;

        Map<String, Object> model = new HashMap<String, Object>();

        // create preferences map here so that we can pass them all back to the view
        HashMap<String, String> preferences  = new HashMap<String, String>();

        // Get DeliveryType classes.  loop through each deliverer type to 
        // to obtain preferenceKeys.  Check to see if a matching request
        // parameter was provided, then save a record for the userID, channelID, and 
        // preferences setting
        Collection<MessageDeliverer> deliveryTypes = this.messageDelivererRegistryService.getAllDeliverers();

        // first remove all configured user delivers for this user
        this.recipientPreferenceService.removeRecipientDelivererConfigs(userid);        

        for (MessageDeliverer dt: deliveryTypes) {
            String deliveryTypeName = dt.getName();
            HashMap<String,String> prefMap = dt.getPreferenceKeys();
            LOG.debug("deliveryName: "+deliveryTypeName);
            HashMap<String, String> userprefs = new HashMap<String, String>();
            for (String prefKey:prefMap.keySet()) {
                LOG.debug("   key: "+prefKey+", value: "+request.getParameter(deliveryTypeName+"."+prefKey));
                userprefs.put(deliveryTypeName+"."+prefKey, request.getParameter(deliveryTypeName+"."+prefKey ));
                preferences.put(deliveryTypeName+"."+prefKey, request.getParameter(deliveryTypeName+"."+prefKey ));
            }
            try {
                this.recipientPreferenceService.saveRecipientPreferences(userid, userprefs, dt);
            } catch (ErrorList errorlist) {
                error = true;
                model.put("errorList", errorlist.getErrors()) ;
            }

            // get channelName.channels
            String[] channels = request.getParameterValues(deliveryTypeName+".channels");
            if (channels != null && channels.length > 0) {
                for (int j=0; j < channels.length; j++) {
                    LOG.debug(deliveryTypeName+".channels["+j+"] "+channels[j]);   
                }
            }
            //	 now save the userid, channel selection
            this.recipientPreferenceService.saveRecipientDelivererConfig(userid, deliveryTypeName, channels);
        }

        // get all channels       
        Collection<String> channels = getAllChannels();

        // get existing configured deliverers
        Collection<RecipientDelivererConfig> currentDeliverers = this.recipientPreferenceService.getDeliverersForRecipient(userid);
        Map<String, Object> currentDeliverersMap = new HashMap<String, Object>();
        for (RecipientDelivererConfig udc: currentDeliverers) {
            String channelId = udc.getChannel();
            currentDeliverersMap.put(udc.getDelivererName()+"."+channelId, Boolean.TRUE);
        }

        // use for debugging, uncomment for production
        //LOG.info("CurrentDeliverersMap");
        //Iterator iter = currentDeliverersMap.keySet().iterator();
        //while (iter.hasNext()) {
        //   Object o = iter.next();	   
        //   LOG.info("key: "+o.toString()+", value: "+ currentDeliverersMap.get(o) );
        //}

        model.put("channels", channels);
        model.put("deliveryTypes", deliveryTypes);
        model.put("preferences", preferences);
        model.put("currentDeliverersMap", currentDeliverersMap);
        model.put("message", "Update Successful");

        return new ModelAndView(VIEW, model);
    }
}
