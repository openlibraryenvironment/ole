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
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.edl.impl.EDLContext;
import org.kuali.rice.edl.impl.EDLModelComponent;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class PerformLookupComponent implements EDLModelComponent {

	private static final Logger LOG = Logger.getLogger(PerformLookupComponent.class);
	
	@Override
	public void updateDOM(Document dom, Element configElement, EDLContext edlContext) {
		String userAction = edlContext.getUserAction().getAction();
		if (userAction != null && userAction.startsWith("performLookup")) {
			edlContext.setRedirectUrl(constructRedirectUrl(dom, configElement, edlContext));
		}
	}
	
	protected String constructRedirectUrl(Document dom, Element configElement, EDLContext edlContext) {
		StringBuilder buf = new StringBuilder(30);
		buf.append(CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.APPLICATION_URL_KEY));
		buf.append("/kr/").append(KRADConstants.LOOKUP_ACTION);
		
		Properties parameters = new Properties();
		parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, getBusinessObjectClassName(dom, configElement, edlContext));
		parameters.put(KEWPropertyConstants.DOC_FORM_KEY, edlContext.getUserSession().addObjectWithGeneratedKey(dom));
		parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, constructReturnUrl(dom, configElement, edlContext));
		parameters.putAll(getLookupParameters(dom, configElement, edlContext));
		parameters.put(KRADConstants.CONVERSION_FIELDS_PARAMETER, getFieldConversions(dom, configElement, edlContext));
		
		String url = UrlFactory.parameterizeUrl(buf.toString(), parameters);
		
		return url;
	}

	protected String getBusinessObjectClassName(Document dom, Element configElement, EDLContext edlContext) {
		String userAction = edlContext.getUserAction().getAction();
		String lookupField = StringUtils.substringAfter(userAction, ".");
		if (StringUtils.isBlank(lookupField)) {
			LOG.error("Cannot find lookup field parameters definition for field " + lookupField);
			return null;
		}

        XPath xPath = edlContext.getXpath();
        try {
			String businessObjectClassName = xPath.evaluate("//fieldDef[@name='" + lookupField + "']/lookup/businessObjectClassName", dom);
			return businessObjectClassName;
		} catch (XPathExpressionException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	
	protected String getFieldConversions(Document dom, Element configElement, EDLContext edlContext) {
		String userAction = edlContext.getUserAction().getAction();
		String lookupField = StringUtils.substringAfter(userAction, ".");
		if (StringUtils.isBlank(lookupField)) {
			LOG.error("Cannot find lookup field parameters definition for field " + lookupField);
			return null;
		}

        XPath xPath = edlContext.getXpath();
        try {
			String lookupParameters = xPath.evaluate("//fieldDef[@name='" + lookupField + "']/lookup/fieldConversions", dom);
			return lookupParameters;
		} catch (XPathExpressionException e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	
	protected Map<String, String> getLookupParameters(Document dom, Element configElement, EDLContext edlContext) {
		String lookupParameterDefinition = retrieveLookupParametersString(dom, configElement, edlContext);
		if (StringUtils.isBlank(lookupParameterDefinition)) {
			return Collections.emptyMap();
		}
		StringTokenizer tok = new StringTokenizer(lookupParameterDefinition, ",");
		Map<String, String> lookupParameters = new HashMap<String, String>();
		
		// where all of the field values are stored
		Element currentVersion = VersioningPreprocessor.findCurrentVersion(dom);
		
		while (tok.hasMoreTokens()) {
			String parameterDefinition = tok.nextToken();
			int colonInd = parameterDefinition.indexOf(':');
			if (colonInd == -1) {
				throw new WorkflowRuntimeException("Lookup definition string improperly formatted " + lookupParameterDefinition);
			}
			
			String parameterName = parameterDefinition.substring(colonInd + 1);
			String parameterValuePropertyName = parameterDefinition.substring(0, colonInd);

            XPath xPath = edlContext.getXpath();
            try {
                String parameterValue = xPath.evaluate("//field[@name='" + parameterValuePropertyName + "']/value", currentVersion);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(XmlJotter.jotNode(currentVersion, true));
                }
				if (StringUtils.isNotBlank(parameterValue)) {
					lookupParameters.put(parameterName, parameterValue);
				}
			} catch (XPathExpressionException e) {
				throw new WorkflowRuntimeException(e);
			}
		}
		return lookupParameters;
	}
	
	protected String retrieveLookupParametersString(Document dom, Element configElement, EDLContext edlContext) {
		String userAction = edlContext.getUserAction().getAction();
		String lookupField = StringUtils.substringAfter(userAction, ".");
		if (StringUtils.isBlank(lookupField)) {
			LOG.error("Cannot find lookup field parameters definition for field " + lookupField);
			return null;
		}

        XPath xPath = edlContext.getXpath();
        try {
			String lookupParameters = xPath.evaluate("//fieldDef[@name='" + lookupField + "']/lookup/lookupParameters", dom);
			return lookupParameters;
		} catch (XPathExpressionException e) {
			throw new WorkflowRuntimeException(e);
		}
	}

	protected String constructReturnUrl(Document dom, Element configElement, EDLContext edlContext) {
		StringBuilder baseUrl = new StringBuilder(30);
		baseUrl.append(CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.APPLICATION_URL_KEY));
		baseUrl.append("/kew/EDocLite");
		
		Properties parameters = new Properties();
		
		String url = UrlFactory.parameterizeUrl(baseUrl.toString(), parameters);
		return url;
	}
}
