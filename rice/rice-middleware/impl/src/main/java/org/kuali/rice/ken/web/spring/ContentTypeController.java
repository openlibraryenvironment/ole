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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.kuali.rice.ken.bo.NotificationContentTypeBo;
import org.kuali.rice.ken.service.NotificationAuthorizationService;
import org.kuali.rice.ken.service.NotificationContentTypeService;
import org.kuali.rice.ken.service.NotificationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * Controller that manages ContentTypes (add/update)
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContentTypeController extends MultiActionController {
   
   private static String view = "";
   protected NotificationService notificationService;
   protected NotificationContentTypeService notificationContentTypeService;
   protected NotificationAuthorizationService notificationAuthzService;
   
   /**
    * Set the Notification Services
    * @param notificationService
    */   
   public void setNotificationService(NotificationService notificationService) {
      this.notificationService = notificationService;
   }
   
   /**
    * Set the Notification Services
    * @param notificationService
    */   
   public void setNotificationContentTypeService(NotificationContentTypeService notificationContentTypeService) {
      this.notificationContentTypeService = notificationContentTypeService;
   }
   
   /**
    * Set the Notification Authorization Services
    * @param notificationAuthzService
    */   
   public void setNotificationAuthorizationService(NotificationAuthorizationService notificationAuthzService) {
      this.notificationAuthzService = notificationAuthzService;
   }

   /** Logger for this class and subclasses */
   private static final Logger LOG = Logger.getLogger(NotificationController.class);
   
   @Override
   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
       // only allow admins access to this controller
       // yuck, we should implement ACEGI down the road
       String user = request.getRemoteUser();
       if (!notificationAuthzService.isUserAdministrator(user)) {
           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
           //response.sendError(HttpServletResponse.SC_FORBIDDEN, "User " + user + " is not a Notification System administrator");
           throw new SecurityException("User " + user + " is not a Notification System administrator");
       }
       return super.handleRequestInternal(request, response);
   }

   /**
    * display ContentTypes
    * @param request : a servlet request
    * @param response : a servlet response
    * @throws ServletException : an exception
    * @throws IOException : an exception
    * @return a ModelAndView object
    */   
   public ModelAndView displayContentTypes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      view = "ContentTypeManager";
      
      Collection<NotificationContentTypeBo> contentTypes = this.notificationContentTypeService.getAllCurrentContentTypes();
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("contentTypes", contentTypes);
      return new ModelAndView(view, model);
   }

/**
    * display ContentTypeForm
    * @param request : a servlet request
    * @param response : a servlet response
    * @throws ServletException : an exception
    * @throws IOException : an exception
    * @return a ModelAndView object
    */   
   public ModelAndView displayContentTypeForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      view = "ContentTypeForm";
      NotificationContentTypeBo notificationContentType;
      String actionrequested; 
      String name = request.getParameter("name");
      LOG.debug("name param: "+name);
 
      if (name == null) {
	 actionrequested = new String("add");
         notificationContentType = new NotificationContentTypeBo();
      } else {
	  actionrequested = new String("update"); 
	 notificationContentType = this.notificationContentTypeService.getNotificationContentType(name);
      }

      Map<String, Object> model = new HashMap<String, Object>();
      model.put("notificationContentType",notificationContentType);
      model.put("actionrequested", actionrequested);
      return new ModelAndView(view, model);
   }
   
   /**
    * addContentType
    * @param request : a servlet request
    * @param response : a servlet response
    * @throws ServletException : an exception
    * @throws IOException : an exception
    * @return a ModelAndView object
    */   
   public ModelAndView addContentType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       view = "ContentTypeManager";
       String id = request.getParameter("id");
       String name = request.getParameter("name");
       String description = request.getParameter("description");
       String namespace = request.getParameter("namespace");
       String xsd = request.getParameter("xsd");
       String xsl = request.getParameter("xsl");
       
       LOG.debug("id: "+id);
       LOG.debug("name: "+name);
       LOG.debug("description: "+description);
       LOG.debug("namespace: "+namespace);
       LOG.debug("xsd: "+xsd);
       LOG.debug("xsl: "+xsl);
       
       NotificationContentTypeBo notificationContentType = new NotificationContentTypeBo();
       notificationContentType.setName(name);
       notificationContentType.setDescription(description);
       notificationContentType.setNamespace(namespace);
       notificationContentType.setXsd(xsd);
       notificationContentType.setXsl(xsl);
       
       this.notificationContentTypeService.saveNotificationContentType(notificationContentType);
       
       Collection<NotificationContentTypeBo> contentTypes = this.notificationContentTypeService.getAllCurrentContentTypes();
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("contentTypes", contentTypes);            
       return new ModelAndView(view, model);     
   }
   
   /**
    * updateContentType
    * @param request : a servlet request
    * @param response : a servlet response
    * @throws ServletException : an exception
    * @throws IOException : an exception
    * @return a ModelAndView object
    */   
   public ModelAndView updateContentType(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       view = "ContentTypeManager";
       String id = request.getParameter("id");
       String name = request.getParameter("name");
       String description = request.getParameter("description");
       String namespace = request.getParameter("namespace");
       String xsd = request.getParameter("xsd");
       String xsl = request.getParameter("xsl");
       
       LOG.debug("id: "+id);
       LOG.debug("name: "+name);
       LOG.debug("description: "+description);
       LOG.debug("namespace: "+namespace);
       LOG.debug("xsd: "+xsd);
       LOG.debug("xsl: "+xsl);
       
       NotificationContentTypeBo notificationContentType = this.notificationContentTypeService.getNotificationContentType(name);
        
       notificationContentType.setName(name);
       notificationContentType.setDescription(description);
       notificationContentType.setNamespace(namespace);
       notificationContentType.setXsd(xsd);
       notificationContentType.setXsl(xsl);
       
       this.notificationContentTypeService.saveNotificationContentType(notificationContentType);
       
       
       // get updated content type collection
       Collection<NotificationContentTypeBo> contentTypes = this.notificationContentTypeService.getAllCurrentContentTypes();
       Map<String, Object> model = new HashMap<String, Object>();
       model.put("contentTypes", contentTypes);
       return new ModelAndView(view, model);     
   }
}
