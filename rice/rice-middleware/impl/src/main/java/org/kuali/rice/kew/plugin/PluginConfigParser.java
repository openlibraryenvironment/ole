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
package org.kuali.rice.kew.plugin;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.util.xml.XmlException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Parses a {@link PluginConfig} configuration from an XML file.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PluginConfigParser {

    private static final String PARAMETER_TAG = "parameter";
    private static final String LISTENER_TAG = "listener";
    private static final String LISTENER_CLASS_TAG = "listener-class";
    private static final String RESOURCE_LOADER_TAG = "resourceLoader";

    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String CLASS_ATTRIBUTE = "class";

    public PluginConfig parse(File configFile, Config parentConfig) throws IOException, XmlException {
    	return parse(configFile.toURI().toURL(), parentConfig);
    }

	public PluginConfig parse(URL url, Config parentConfig) throws IOException, XmlException {
		SAXBuilder builder = new SAXBuilder(false);
		try {
            // NOTE: need to be wary of whether streams are closed properly
            // by builder
			Document doc = builder.build(url);
			Element root = doc.getRootElement();
			PluginConfig pluginConfig = new PluginConfig(url, parentConfig);
			parseResourceLoader(root, pluginConfig);
			parseListeners(root, pluginConfig);
			return pluginConfig;
		} catch (JDOMException e) {
		    throw new PluginException("Error when parsing the plugin config file.", e);
		}
	}

	public void parseResourceLoader(Element element, PluginConfig pluginConfig) throws XmlException {
		List loaderElements = element.getChildren(RESOURCE_LOADER_TAG);
		if (loaderElements.size() > 1) {
			throw new XmlException("Only one <resourceLoader> tag may be defined.");
		} else if (!loaderElements.isEmpty()) {
			Element loaderElement = (Element)loaderElements.get(0);
			String attributeClass = loaderElement.getAttributeValue(CLASS_ATTRIBUTE);
			if (StringUtils.isEmpty(attributeClass)) {
				throw new XmlException("<resourceLoader> element must define a 'class' attribute.");
			}
			pluginConfig.setResourceLoaderClassname(attributeClass);
		}
	}

	public void parseListeners(Element element, PluginConfig pluginConfig) throws XmlException {
		List listeners = element.getChildren(LISTENER_TAG);
		for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
		    pluginConfig.addListener(parseListenerProperties((Element)iterator.next()));
		}
	}

	private String parseListenerProperties(Element element) throws XmlException {
		String listenerClass = element.getChildText(LISTENER_CLASS_TAG);
		if (org.apache.commons.lang.StringUtils.isEmpty(listenerClass)) {
			throw new XmlException("Listener Class tag must have a class property defined");
		}
		return listenerClass;
	}

	public Map parseParameters(Element element) throws XmlException {
        Map parsedParms = new HashMap();
	    List parameters = element.getChildren(PARAMETER_TAG);
		for (Iterator iterator = parameters.iterator(); iterator.hasNext();) {
		    String [] parm = parseParameter((Element)iterator.next());
		    parsedParms.put(parm[0], parm[1]);
		}
		return parsedParms;
	}

	private String [] parseParameter(Element element) throws XmlException {
		String name = element.getAttributeValue(NAME_ATTRIBUTE);
		String value = element.getAttributeValue(VALUE_ATTRIBUTE);
		if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
			throw new XmlException("Parameter tag must have a name attribute defined");
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(value)) {
			throw new XmlException("Parameter tag must have a value attribute defined");
		}
		return new String [] {name, value};
	}


}
