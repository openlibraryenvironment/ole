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
package org.kuali.rice.kim.client.acegi;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.cas.TicketResponse;
import org.acegisecurity.providers.cas.ticketvalidator.CasProxyTicketValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.kim.sesn.DistributedSession;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.yale.its.tp.cas.client.ProxyTicketValidator;


/**
 * Uses CAS' <code>ProxyTicketValidator</code> to validate a service ticket.  
 * Creates the distributed session.  Session principal is currently 
 * user@method.
 *  
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiCasProxyTicketValidator extends CasProxyTicketValidator {
    //~ Static fields/initializers =====================================================================================

    private static final Log logger = LogFactory.getLog(KualiCasProxyTicketValidator.class);

    private DistributedSession distributedSession;
    //~ Instance fields ================================================================================================


    /**
     * This overridden method gets the authentication source and 
     * Distributed Session Ticket from the response
     * 
     * @see org.acegisecurity.providers.cas.ticketvalidator.CasProxyTicketValidator#validateNow(edu.yale.its.tp.cas.client.ProxyTicketValidator)
     */
    protected TicketResponse validateNow(ProxyTicketValidator pv)
        throws AuthenticationServiceException, BadCredentialsException {
		String					sAuthenticationSource = null;
		String                  sDST = null;

        try {
            pv.validate();
        } catch (Exception internalProxyTicketValidatorProblem) {
            throw new AuthenticationServiceException(internalProxyTicketValidatorProblem.getMessage());
        }

        if (!pv.isAuthenticationSuccesful()) {
            throw new BadCredentialsException(pv.getErrorCode() + ": " + pv.getErrorMessage());
        }
        
        logger.debug("PROXY RESPONSE: " + pv.getResponse());
        
        if (logger.isDebugEnabled()) {
            logger.debug("DEBUG");
        }
                
        try {
			DocumentBuilderFactory	factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder			builder = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(pv.getResponse()));
			Document				doc     = builder.parse(inStream);
			Element 				head = doc.getDocumentElement();
			NodeList 				attrs = head.getElementsByTagName("cas:attribute");
			for (int i=0; i<attrs.getLength(); i++) {
				logger.debug(("Field name:" + ((Element)attrs.item(i)).getAttribute("name")) + "=" + ((Element)attrs.item(i)).getAttribute("value"));
				if ( ((Element)attrs.item(i)).getAttribute("name").equals("authenticationMethod") ) {
					sAuthenticationSource = ((Element)attrs.item(i)).getAttribute("value");
				} else if ( ((Element)attrs.item(i)).getAttribute("name").equals("DST") ) {
				    sDST = ((Element)attrs.item(i)).getAttribute("value");
				}
			}
			if (sAuthenticationSource != null && sDST != null) {
                String sPrincipal = pv.getUser() + "@" + sAuthenticationSource;

                if (logger.isDebugEnabled()) {
			        logger.debug("Updating session: " + sDST + " " + sPrincipal);
			    }
// Touching here may be overkill since it should happen in the filter
                distributedSession.touchSesn(sDST);
              //  distributedSession.addPrincipalToSesn(sDST, sPrincipal);
			} else {
			    if (logger.isDebugEnabled()) {
                    logger.debug("Incomplete data from CAS:" + sAuthenticationSource + ":" + sDST);
                }
			}
        } catch (Exception e) {
        	logger.error("Error parsing CAS Result", e);
        }
        
        logger.debug("Authentication Method:" + sAuthenticationSource);
        return new KualiTicketResponse(pv.getUser(), pv.getProxyList(), pv.getPgtIou(), sDST);
    }


    /**
     * @param distributedSession the distributedSession to set
     */
    public void setDistributedSession(DistributedSession distributedSession) {
        this.distributedSession = distributedSession;
    }
    

}
