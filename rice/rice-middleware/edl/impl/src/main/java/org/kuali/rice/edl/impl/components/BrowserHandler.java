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
package org.kuali.rice.edl.impl.components;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.edl.impl.EDLXmlUtils;
import org.kuali.rice.edl.impl.RequestParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles setting the request browser type in the EDL XML.
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class BrowserHandler implements EDLModelComponent {

	private static final Log LOG = LogFactory.getLog(BrowserHandler.class);
	
	public static final String BROWSER_EL = "requestBrowser";
	
	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
	    String userAgent = browserString(edlContext.getRequestParser());
	    setBrowser(dom, uaConvert(userAgent));
	}
	
	/**
	 * get user-agent header
	 */
	private String browserString(RequestParser requestParser) {
	    return requestParser.getRequest().getHeader("User-Agent");
	}

	private void setBrowser(Document dom, String bStr) {
	    if (!StringUtils.isBlank(bStr)) {
	    	Element currentPageElement = EDLXmlUtils.getOrCreateChildElement(dom.getDocumentElement(), BROWSER_EL, true);
	    	currentPageElement.appendChild(dom.createTextNode(bStr));
	    	if (LOG.isDebugEnabled()) {
	    		LOG.debug("Appended" + bStr + " to XML field " + currentPageElement.getNodeName());
	    	}
	    }
	}
	
	private String uaConvert(String userAgent){
	    String res = userAgent;
	    int ie = userAgent.indexOf("MSIE");
	    int ff = userAgent.indexOf("Firefox/");
	    int saf = userAgent.indexOf("Safari/");
	    int chr = userAgent.indexOf("Chrome/");
	    if(ie>0){
		res = "InternetExplorer";
	    }else if(ff > 0){
		res = "Firefox";
	    }else if(saf > 0 && chr < 0){
		res = "Safari";
	    }else if(saf > 0 && chr > 0){
	    res = "Chrome";
	    }else{
		res = "Other";
	    }	    
	    return res;
	}
	

	    

}
