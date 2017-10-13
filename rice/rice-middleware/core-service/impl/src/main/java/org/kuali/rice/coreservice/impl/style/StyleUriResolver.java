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
package org.kuali.rice.coreservice.impl.style;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.kuali.rice.coreservice.api.style.Style;
import org.kuali.rice.coreservice.api.style.StyleService;

/**
 * A URIResolver that knows how to resolve href's based on style names.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
class StyleUriResolver implements URIResolver {

	private static final Logger LOG = Logger.getLogger(StyleUriResolver.class);
	
	private final StyleService styleService;
	
	StyleUriResolver(StyleService styleService) {
		if (styleService == null) {
			throw new IllegalArgumentException("styleService cannot be null");
		}
		this.styleService = styleService;
	}

	public Source resolve(String href, String base) {

		try {
			Style style = styleService.getStyle(href);
			return new StreamSource(new StringReader(style.getXmlContent()));

		} catch (Exception e) {
			LOG.error("Error ocurred getting style " + href, e);
		}
		return null;
	}

}
