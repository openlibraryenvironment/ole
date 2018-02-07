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
package org.kuali.rice.ken.service;

import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.ken.bo.NotificationBo;
import org.kuali.rice.ken.bo.NotificationResponseBo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Notification Message Content service - handles parsing the notification XML message and also marshalling out BOs for the response.
 * @see <a href="http://wiki.library.cornell.edu/wiki/display/notsys/Hi-Level+Service+Interface+Definitions#Hi-LevelServiceInterfaceDefinitions-NotificationMessageContentService">NotificationMessageContentService</a>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface NotificationMessageContentService {
    /**
     * Parses a Notification request message into business objects.  Performs syntactic and semantic validation.  
     * This method takes an InputStream.
     * @param stream request message stream
     * @return Notification business object
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws XmlException
     */
    public NotificationBo parseNotificationRequestMessage(InputStream stream) throws IOException, XmlException;
    
    /**
     * Parses a Notification request message into business objects.  Performs syntactic and semantic validation.  
     * This method takes a String of XML.
     * @param notificationMessageAsXml
     * @return
     * @throws IOException
     * @throws XmlException
     */
    public NotificationBo parseNotificationRequestMessage(String notificationMessageAsXml) throws IOException, XmlException;
    
    /**
     * Generates a Notification response message
     * @param response
     * @return String XML representation of a Notification response object
     */
    public String generateNotificationResponseMessage(NotificationResponseBo response);

    /**
     * This method is responsible for marshalling out the passed in Notification object in and XML representation. 
     * @param notification
     * @return String of XML.
     */
    public String generateNotificationMessage(NotificationBo notification);
    
    /**
     * This method is responsible for marshalling out the passed in Notification object in and XML representation, with 
     * the addition of adding the specific recipient to the recipients list and removing the others. 
     * @param notification
     * @param userRecipientId
     * @return String of XML.
     */
    public String generateNotificationMessage(NotificationBo notification, String userRecipientId);
    
    /**
     * This method parses out the serialized XML version of Notification BO and populates a Notification BO with it.
     * @param xmlAsBytes
     * @return Notification
     * @throws Exception
     */
    public NotificationBo parseSerializedNotificationXml(byte[] xmlAsBytes) throws Exception;
}
