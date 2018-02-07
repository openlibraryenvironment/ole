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
package org.kuali.rice.core.impl.config.property;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * ConfigParser implementation that supports a hierarchy of configs, in which 
 * configs can include other configs.  Variable tokens are resolved at parse time,
 * in the order in which they are encountered.  This class relies on Spring for resource
 * loading and Apache Commons Lang for variable replacement.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @deprecated This is an old hand-rolled Rice configuration parser which has since been replaced by the superior JAXBConfigImpl.
 *             This old implementation only exists to support PluginConfig which supports ancient KEW plugin configs
 */
@Deprecated
public class ConfigParserImpl implements ConfigParser {
    // keep the same random
    private static final Random RANDOM = new Random();

    private static final Logger LOG = Logger.getLogger(ConfigParserImpl.class);
    private static final String IMPORT_NAME = "config.location";
    private static final String PARAM_NAME= "param";
    private static final String NAME_ATTR = "name";
    private static final String OVERRIDE_ATTR = "override";
    private static final String RANDOM_ATTR = "random";
    private static final String INDENT = "  ";
    
    public static final String ALTERNATE_BUILD_LOCATION_KEY = "alt.build.location";

    /**
     * A StrLookup implementation that delegates to System properties if the key is not
     * found in the supplied map.
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    private static class SystemPropertiesDelegatingStrLookup extends StrLookup {
        private final Map map;
        private SystemPropertiesDelegatingStrLookup(Map map) {
            this.map = map;
        }
        @Override
        public String lookup(String key) {
            Object o = map.get(key);
            if (o != null) {
                return String.valueOf(o);
            } else {
                String s = System.getProperty(key);
                if (s != null) {
                    return s;
                } else {
                    // implement behavior for missing property here...e.g. return ""
                    // returning null will result in the substitutor not substituting
                    return "";
                }
            }
        }
    }

    /**
     * @see org.kuali.rice.core.api.config.ConfigParser#parse(java.lang.String[])
     */
    public void parse(Map props, String[] locations) throws IOException {
        LinkedHashMap params = new LinkedHashMap();
        params.putAll(props);
        parse(params, locations);
        props.putAll(params);
    }

    /**
     * Parses a list of locations
     * @param params the current parameter map
     * @param locations a list of locations to parse
     * @throws IOException
     */
    protected void parse(LinkedHashMap<String, Object> params, String[] locations) throws IOException {
        StrSubstitutor subs = new StrSubstitutor(new SystemPropertiesDelegatingStrLookup(params));
        for (String location: locations) {
            parse(params, location, subs, 0);
        }
    }

    /**
     * Parses a single config location
     * @param params the current parameter map
     * @param location the location to parse
     * @param subs a StrSubstitutor used to substitute variable tokens
     * @throws IOException
     */
    protected void parse(LinkedHashMap<String, Object> params, String location, StrSubstitutor subs, int depth) throws IOException {
        InputStream configStream = RiceUtilities.getResourceAsStream(location);
        if (configStream == null) {
            LOG.warn("###############################");
            LOG.warn("#");
            LOG.warn("# Configuration file '" + location + "' not found!");
            LOG.warn("#");
            LOG.warn("###############################");
            return;
        }
        
        final String prefix = StringUtils.repeat(INDENT, depth); 
        LOG.info(prefix + "+ Parsing config: " + location);

        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configStream);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Contents of config " + location + ": \n" + XmlJotter.jotNode(doc, true));
            }
        } catch (SAXException se) {
            IOException ioe = new IOException("Error parsing config resource: " + location);
            ioe.initCause(se);
            throw ioe;
        } catch (ParserConfigurationException pce) {
            IOException ioe = new IOException("Unable to obtain document builder");
            ioe.initCause(pce);
            throw ioe;
        } finally {
            configStream.close();
        }

        Element root = doc.getDocumentElement();
        // ignore the actual type of the document element for now
        // so that plugin descriptors can be parsed
        NodeList list = root.getChildNodes();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (!PARAM_NAME.equals(node.getNodeName())) {
                LOG.warn("Encountered non-param config node: " + node.getNodeName());
                continue;
            }
            Element param = (Element) node;
            String name = param.getAttribute(NAME_ATTR);
            if (name == null) {
                LOG.error("Unnamed parameter in config resource '" + location + "': " + XmlJotter.jotNode(param));
                continue;
            }
            Boolean override = Boolean.TRUE;
            String overrideVal = param.getAttribute(OVERRIDE_ATTR);
            if (!StringUtils.isEmpty(overrideVal)) {
                override = Boolean.valueOf(overrideVal);
            }

            content.setLength(0);
            // accumulate all content (preserving any XML content)
            getNodeValue(name, location, param, content);
            String value = subs.replace(content);
            if (LOG.isDebugEnabled()) {
                LOG.debug(prefix + INDENT + "* " + name + "=[" + ConfigLogger.getDisplaySafeValue(name, value) + "]");
            }

            if (IMPORT_NAME.equals(name)) {
                // what is this...we don't follow a string with this substring in it? i.e. if the value does not
                // resolve then don't try to follow it (it won't find it anyway; this is the case for any path
                // with unresolved params...)?
                if (!value.contains(ALTERNATE_BUILD_LOCATION_KEY)) {
                    parse(params, value, subs, depth + 1);
                }
            } else {
                if (Boolean.valueOf(param.getAttribute(RANDOM_ATTR))) {
                    // this is a special type of property whose value is a randomly generated number in the range specified
                    value = String.valueOf(generateRandomInteger(value));   
                }
                setParam(params, override, name, value, prefix + INDENT);
            }
        }
        LOG.info(prefix + "- Parsed config: " + location);
    }
    
    /**
     * Generates a random integer in the range specified by the specifier, in the format: min-max
     * @param rangeSpec a range specification, 'min-max'
     * @return a random integer in the range specified by the specifier, in the format: min-max
     */
    protected int generateRandomInteger(String rangeSpec) {
        String[] range = rangeSpec.split("-");
        if (range.length != 2) {
            throw new RuntimeException("Invalid range specifier: " + rangeSpec);
        }
        int from = Integer.parseInt(range[0].trim());
        int to = Integer.parseInt(range[1].trim());
        if (from > to) {
            int tmp = from;
            from = to;
            to = tmp;
        }
        int num;
        // not very random huh...
        if (from == to) {
            num = from;
        } else {
            num = from + RANDOM.nextInt((to - from) + 1);
        }
        return num;
    }

    /**
     * @param name name of the node
     * @param location config file location
     * @param n the node
     * @param sb a StringBuilder into which to set contents of the node, preserving any XML content
     * @throws IOException
     */
    protected void getNodeValue(String name, String location, Node n, StringBuilder sb) throws IOException {
        NodeList children = n.getChildNodes();
        // accumulate all content (preserving any XML content)
        try {
            sb.setLength(0);
            for (int j = 0; j < children.getLength(); j++) {
                sb.append(XmlJotter.jotNode(children.item(j), true));
            }
        } catch (XmlException te) {
            IOException ioe = new IOException("Error obtaining parameter '" + name + "' from config resource: " + location);
            ioe.initCause(te);
            throw ioe;
        }
    }

    /**
     * Sets a parameter in the parameter map, based on the override setting and whether a parameter of the same
     * name is already present 
     * @param params the current parameter map
     * @param override whether to override a previous parameter definition
     * @param name the parameter name
     * @param value the parameter value
     */
    private void setParam(Map params, Boolean override, String name, String value, String indent) {
        if (value == null || "null".equals(value)) {
            LOG.warn("Not adding property [" + name + "] because it is null - most likely no token could be found for substituion.");
            return;
        }
        if (override) {
            final String message;
            Object existingValue = params.get(name);
            if (existingValue != null) {
                //if (!existingValue.equals(value)) {
                    message = indent + "Overriding property " + name + "=[" + existingValue + "] with " + name + "=[" + value + "]"; 
                //}
                params.remove(name);
            } else {
                message = indent + "Defining property " + name + "=[" + value + "]";
            }
            LOG.debug(message);
            params.put(name, value);
        } else if (!params.containsKey(name)) {
            LOG.debug(indent + "Defining property " + name + "=[" + value + "]");
            params.put(name, value);
        } else {
            LOG.debug(indent + "Not overriding existing parameter: " + name + " '" + params.get(name) + "'");
        }
    }
}
