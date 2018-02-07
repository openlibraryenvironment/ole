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
package org.kuali.rice.ken.web.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationChannelBo;
import org.kuali.rice.ken.bo.UserChannelSubscriptionBo;
import org.kuali.rice.ken.service.NotificationChannelService;
import org.kuali.rice.ken.service.UserPreferenceService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * This class is the controller that handles management of various user preferences interfaces (deliver types, user subscriptions, etc).
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UserPreferencesController extends MultiActionController {
    
   private static String view = "";
   
   /** Logger for this class and subclasses */
   private static final Logger LOG = Logger.getLogger(UserPreferencesController.class);
   
   protected NotificationChannelService notificationChannelService;
   protected UserPreferenceService userPreferenceService;
   protected Object notificationMessageDelivererRegistryService;

   
   /**
    * Set the NotificationChannelService
    * @param notificationChannelService
    */   
   public void setNotificationChannelService(NotificationChannelService notificationChannelService) {
      this.notificationChannelService = notificationChannelService;
   }
   
   /**
    * Set the UserPreferenceService
    * @param userPreferenceService
    */   
   public void setUserPreferenceService(UserPreferenceService userPreferenceService) {
      this.userPreferenceService = userPreferenceService;
   }
   
   /**
    * Set the NotificationMessageDelivererRegistryService
    * @param notificationMessageDelivererRegistryService
    */   
   public void setNotificationMessageDelivererRegistryService(Object notificationMessageDelivererRegistryService) {
      this.notificationMessageDelivererRegistryService = notificationMessageDelivererRegistryService;
   }
   
   /**
    * This method displays the actionList preferences screen.
    * @param request
    * @param response
    * @return
    * @throws ServletException
    * @throws IOException
    */
   public ModelAndView displayActionListPreferences(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       view = "ActionListPreferences";
       LOG.debug("remoteUser: "+request.getRemoteUser());
       Map<String, Object> model = new HashMap<String, Object>(); 
       return new ModelAndView(view, model);
   }
    
   /**
    * This method handles displaying the user preferences UI.
    * @param request
    * @param response
    * @return
    * @throws ServletException
    * @throws IOException
    */
   public ModelAndView displayUserPreferences(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      view = "UserPreferencesForm";
      String userid = request.getRemoteUser();
      LOG.debug("remoteUser: "+userid);
      // get subscribable channels
      Collection<NotificationChannelBo> channels = this.notificationChannelService.getSubscribableChannels();
      // get current subscriptions for this user
      Collection<UserChannelSubscriptionBo> subscriptions = this.userPreferenceService.getCurrentSubscriptions(userid);
      Map<String, Object> currentsubs = new HashMap<String, Object>();
      Iterator<UserChannelSubscriptionBo> i = subscriptions.iterator();
      while (i.hasNext()) {
	  UserChannelSubscriptionBo sub = i.next();
	  String subid = Long.toString(sub.getChannel().getId());
 	  currentsubs.put(subid, subid);
	  LOG.debug("currently subscribed to: "+sub.getChannel().getId());
      }
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("channels", channels);
      model.put("currentsubs", currentsubs);
      
      return new ModelAndView(view, model);
   }

   /**
    * Subscribe To a Channel
    * @param request
    * @param response
    * @return
    * @throws ServletException
    * @throws IOException
   */
   public ModelAndView subscribeToChannel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       view = "UserPreferencesForm";
       String userid = request.getRemoteUser();
       LOG.debug("remoteUser: "+userid);
       String channelid = request.getParameter("channelid");
       NotificationChannelBo newChannel = this.notificationChannelService.getNotificationChannel(channelid);
       LOG.debug("newChannel name:"+newChannel.getName());
       UserChannelSubscriptionBo newSub = new UserChannelSubscriptionBo();
       newSub.setUserId(userid);
       newSub.setChannel(newChannel);
       LOG.debug("Calling service to subscribe to channel: "+newChannel.getName());
       this.userPreferenceService.subscribeToChannel(newSub);
       
       // get current subscription channel ids
       Collection<UserChannelSubscriptionBo> subscriptions = this.userPreferenceService.getCurrentSubscriptions(userid);
       Map<String, Object> currentsubs = new HashMap<String, Object>();;
       Iterator<UserChannelSubscriptionBo> i = subscriptions.iterator();
       while (i.hasNext()) {
 	  UserChannelSubscriptionBo sub = i.next();
 	  String subid = Long.toString(sub.getChannel().getId());
 	  currentsubs.put(subid, subid);
 	  LOG.debug("currently subscribed to: "+sub.getChannel().getId());
       }
       
       // get all subscribable channels       
       Collection<NotificationChannelBo> channels = this.notificationChannelService.getSubscribableChannels();
       
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("channels", channels);
       model.put("currentsubs", currentsubs);
       return new ModelAndView(view, model);       
   }
   
   /**
    * Unsubscribe from Channel
    * @param request
    * @param response
    * @return
    * @throws ServletException
    * @throws IOException
    */
   public ModelAndView unsubscribeFromChannel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       view = "UserPreferencesForm";
       String userid = request.getRemoteUser();
       LOG.debug("remoteUser: "+userid);
       String channelid = request.getParameter("channelid");
       
       NotificationChannelBo newChannel = this.notificationChannelService.getNotificationChannel(channelid);
       LOG.debug("getting channel (id, user): "+channelid+","+userid); 
       UserChannelSubscriptionBo oldsub = this.userPreferenceService.getSubscription(channelid, userid);
       oldsub.setChannel(newChannel);
       
       LOG.debug("Calling service to unsubscribe: "+newChannel.getName());
       this.userPreferenceService.unsubscribeFromChannel(oldsub);
       LOG.debug("Finished unsubscribe service: "+newChannel.getName());
       
       // get current subscription channel ids
       Collection<UserChannelSubscriptionBo> subscriptions = this.userPreferenceService.getCurrentSubscriptions(userid);
       Map<String, Object> currentsubs = new HashMap<String, Object>();
       Iterator<UserChannelSubscriptionBo> i = subscriptions.iterator();
       while (i.hasNext()) {
 	  UserChannelSubscriptionBo sub = i.next();
 	  String subid = Long.toString(sub.getChannel().getId());
	  currentsubs.put(subid, subid);
 	  LOG.debug("currently subscribed to: "+sub.getChannel().getId());
       }
       
       // get all subscribable channels       
       Collection<NotificationChannelBo> channels = this.notificationChannelService.getSubscribableChannels();
       
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("channels", channels);
       model.put("currentsubs", currentsubs);
       return new ModelAndView(view, model);    
        
   }
}
