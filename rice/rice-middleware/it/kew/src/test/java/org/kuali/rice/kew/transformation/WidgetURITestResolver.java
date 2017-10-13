/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.transformation;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

public class WidgetURITestResolver implements URIResolver {
	
	private static final Logger LOG = Logger.getLogger(WidgetURITestResolver.class);
	
	public Source resolve(String href, String base) {
		   
	    try {
		    	//EDocLiteService eDocSvc=SpringServiceLocator.getEDocLiteService();
		    	//EDocLiteStyle widgetStyle = eDocSvc.getEDocLiteStyle(href);
		    	//LOG.debug("Widgets: \n" + widgetStyle.getXmlContent());
		    	//return new StreamSource(new StringReader(widgetStyle.getXmlContent()));
	    		return new SAXSource(new InputSource(WidgetURITestResolver.class.getResourceAsStream("widgets_pipeTest.xml")));
	    		
	    }
	    catch (Exception e) {
	    	LOG.error("Error ocurred getting style " + href, e);
	      //e.printStackTrace();
	    }
	    return null;
	  }

}
