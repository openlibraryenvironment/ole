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
package org.kuali.rice.edl.impl;

import javax.xml.transform.Transformer;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.krad.UserSession;


/**
 * Convenience object to hang valuable objects in edl off of.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class EDLContext {
	
	private EDocLiteAssociation edocLiteAssociation;
	private EDLControllerChain	edlControllerChain;
	private UserSession userSession;
	private Transformer transformer;
	private RequestParser requestParser;
	private boolean inError;
	private UserAction userAction;
	private String redirectUrl;
    private XPath xpath;

    public EDLContext() {
		redirectUrl = null;
        xpath = XPathFactory.newInstance().newXPath();
    }

    public XPath getXpath() {
        return xpath;
    }

    public void setXpath(XPath xpath) {
        this.xpath = xpath;
    }

	public UserSession getUserSession() {
		return userSession;
	}
	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}
	public EDLControllerChain getEdlControllerChain() {
		return edlControllerChain;
	}
	public void setEdlControllerChain(EDLControllerChain edlControllerChain) {
		this.edlControllerChain = edlControllerChain;
	}
	public EDocLiteAssociation getEdocLiteAssociation() {
		return edocLiteAssociation;
	}
	public void setEdocLiteAssociation(EDocLiteAssociation edocLiteAssociation) {
		this.edocLiteAssociation = edocLiteAssociation;
	}
	public Transformer getTransformer() {
		return transformer;
	}
	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}
	public boolean isInError() {
		return inError;
	}
	public void setInError(boolean inError) {
		this.inError = inError;
	}
	public RequestParser getRequestParser() {
		return requestParser;
	}
	public void setRequestParser(RequestParser requestParser) {
		this.requestParser = requestParser;
	}
	public UserAction getUserAction() {
	    return this.userAction;
	}
	public void setUserAction(UserAction userAction) {
	    this.userAction = userAction;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}	
	
}
