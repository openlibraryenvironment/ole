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
package org.kuali.rice.kew.attribute;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableQuickFinder;
import org.kuali.rice.kns.lookup.LookupUtils;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provides some common shared utility methods shared by the XML-based
 * attribute implementations. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class XMLAttributeUtils {

    private static final Logger LOG = Logger.getLogger(XMLAttributeUtils.class);

	private XMLAttributeUtils() {
		throw new UnsupportedOperationException("do not call");
	}

	public static void establishFieldLookup(Field field, Node lookupNode) {
		NamedNodeMap quickfinderAttributes = lookupNode.getAttributes();
		String businessObjectClass = quickfinderAttributes.getNamedItem("businessObjectClass").getNodeValue();
		field.setQuickFinderClassNameImpl(businessObjectClass);
        Map<String, String> fieldConversionsMap = new HashMap<String, String>();
		for (int lcIndex = 0; lcIndex < lookupNode.getChildNodes().getLength(); lcIndex++) {
			Node fieldConversionsChildNode = lookupNode.getChildNodes().item(lcIndex);
			if ("fieldConversions".equals(fieldConversionsChildNode.getNodeName())) {
				for (int fcIndex = 0; fcIndex < fieldConversionsChildNode.getChildNodes().getLength(); fcIndex++) {
					Node fieldConversionChildNode = fieldConversionsChildNode.getChildNodes().item(fcIndex);
					if ("fieldConversion".equals(fieldConversionChildNode.getNodeName())) {
						NamedNodeMap fieldConversionAttributes = fieldConversionChildNode.getAttributes();
						String lookupFieldName = fieldConversionAttributes.getNamedItem("lookupFieldName").getNodeValue();
						String localFieldName = fieldConversionAttributes.getNamedItem("localFieldName").getNodeValue();
						fieldConversionsMap.put(lookupFieldName, localFieldName);
					}
				}
			}
		}
        field.setFieldConversions(fieldConversionsMap);
	}

    public static void establishFieldLookup(RemotableAttributeField.Builder fieldBuilder, Node lookupNode) {
		NamedNodeMap quickfinderAttributes = lookupNode.getAttributes();
        Node dataObjectNode = quickfinderAttributes.getNamedItem("dataObjectClass");
        if (dataObjectNode == null) {
            // for legacy compatibility, though businessObjectClass is deprecated
            dataObjectNode = quickfinderAttributes.getNamedItem("businessObjectClass");
            if (dataObjectNode != null) {
                LOG.warn("Field is using deprecated 'businessObjectClass' instead of 'dataObjectClass' for lookup definition, field name is: " + fieldBuilder.getName());
            } else {
                throw new ConfigurationException("Failed to locate 'dataObjectClass' for lookup definition.");
            }
        }
        String dataObjectClass = dataObjectNode.getNodeValue();
        String baseLookupUrl = LookupUtils.getBaseLookupUrl(false);
        RemotableQuickFinder.Builder quickFinderBuilder = RemotableQuickFinder.Builder.create(baseLookupUrl, dataObjectClass);
		for (int lcIndex = 0; lcIndex < lookupNode.getChildNodes().getLength(); lcIndex++) {
			Map<String, String> fieldConversionsMap = new HashMap<String, String>();
			Node fieldConversionsChildNode = lookupNode.getChildNodes().item(lcIndex);
			if ("fieldConversions".equals(fieldConversionsChildNode)) {
				for (int fcIndex = 0; fcIndex < fieldConversionsChildNode.getChildNodes().getLength(); fcIndex++) {
					Node fieldConversionChildNode = fieldConversionsChildNode.getChildNodes().item(fcIndex);
					if ("fieldConversion".equals(fieldConversionChildNode)) {
						NamedNodeMap fieldConversionAttributes = fieldConversionChildNode.getAttributes();
						String lookupFieldName = fieldConversionAttributes.getNamedItem("lookupFieldName").getNodeValue();
						String localFieldName = fieldConversionAttributes.getNamedItem("localFieldName").getNodeValue();
						fieldConversionsMap.put(lookupFieldName, localFieldName);
					}
				}
			}
            quickFinderBuilder.setFieldConversions(fieldConversionsMap);
			fieldBuilder.getWidgets().add(quickFinderBuilder);
		}
	}
	
}
