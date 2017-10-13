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
package org.kuali.rice.kew.notes.web;

import org.apache.log4j.Logger;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.notes.Attachment;
import org.kuali.rice.kew.notes.service.NoteService;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;




/**
 * A servlet which can be used to retrieve attachments from Notes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AttachmentServlet extends HttpServlet {
	
	private static final long serialVersionUID = -1918858512573502697L;
	public static final String ATTACHMENT_ID_KEY = "attachmentId";

	// TODO This should probably be put into KewApiConstants when contributed back
	// to Rice 1.0.3
	private static final Logger LOG = Logger.getLogger(AttachmentServlet.class);
			
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String attachmentId = request.getParameter(ATTACHMENT_ID_KEY);
		if (attachmentId == null) {
			throw new ServletException("No 'attachmentId' was specified.");
		}
		
		boolean secureChecks = false;
		String secureAttachmentsParam = null;
		try {
			secureAttachmentsParam = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, "All", KewApiConstants.SECURE_ATTACHMENTS_PARAM);
		} catch (Exception e) {
			LOG.info("Attempted to retrieve parameter value, but could not. Defaulting to unsecured attachment retrieval. " + e.getMessage());
		}
		if (secureAttachmentsParam != null && secureAttachmentsParam.equals("Y")) {
			secureChecks = true;
		}
		try {
			UserSession userSession = (UserSession) request.getSession().getAttribute(KRADConstants.USER_SESSION_KEY);
			if (userSession != null) {// If we can get a valid userSession object off the Http request...
				
				NoteService noteService = KEWServiceLocator.getNoteService(); 
				Attachment attachment = noteService.findAttachment(attachmentId);
				File file = noteService.findAttachmentFile(attachment);
				
				DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(noteService.getNoteByNoteId(attachment.getNoteId()).getDocumentId());
				
				if(!secureChecks || routeHeader != null){// If we can get a valid routeHeader based on the requested attachment ID
					boolean authorized = KEWServiceLocator.getDocumentSecurityService().routeLogAuthorized(userSession.getPrincipalId(), routeHeader, new SecuritySession(userSession.getPrincipalId()));
                    boolean customAttributeAuthorized = false;
                    if(routeHeader.getCustomNoteAttribute() != null){
                        routeHeader.getCustomNoteAttribute().setUserSession(userSession);
                        customAttributeAuthorized = routeHeader.getCustomNoteAttribute().isAuthorizedToRetrieveAttachments();
                    }                    
                    if(!secureChecks || (authorized && customAttributeAuthorized)){//If this user can see this document, they can get the attachment(s)						
                    	response.setContentLength((int)file.length());
						response.setContentType(attachment.getMimeType());
						response.setHeader("Content-disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
						FileInputStream attachmentFile = new FileInputStream(file);
						BufferedInputStream inputStream = new BufferedInputStream(attachmentFile);
						OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());

						try {
							int c;
							while ((c = inputStream.read()) != -1) {
								outputStream.write(c);
							}
						} finally {
							inputStream.close();
						}
						outputStream.close();
					} else {// Throw a forbidden page back, they were not approved by DocumentSecurityService
						LOG.error("Attempt to access attachmentId:"+ attachmentId + " from documentId:" + routeHeader.getDocumentId() + " from unauthorized user: " + userSession.getPrincipalId());
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return;
					}
				} else {// Throw a not found, couldn't get a valid routeHeader
					LOG.error("Caught Null Pointer trying to determine routeHeader for requested attachmentId:" + attachmentId);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
					return;
				}
			} else {// Throw a bad request, we couldn't find a valid user session
				LOG.error("Attempt to access attachmentId:" + attachmentId + " with invalid UserSession");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		} catch (Exception e) {// Catch any error, log it. Send a not found, and throw up the exception.
			LOG.error("Problem retrieving requested attachmentId:" + attachmentId, e);
			throw new WorkflowRuntimeException(e);
		}
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}	
	
}
