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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * A configuration parser that can get properties already parsed passed in and
 * override them. Also, can do token replace based on values already parsed
 * using ${ } to denote tokens.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class HierarchicalConfigParser {
    private static final Logger LOG = Logger.getLogger(HierarchicalConfigParser.class);

    private static final String VAR_START_TOKEN = "${";

    private static final String VAR_END_TOKEN = "}";

    public static final String ALTERNATE_BUILD_LOCATION_KEY = "alt.build.location";


    // this Map is for token replacement this represents the set of tokens that
    // has been made by a parent config file of the one this is parsing
    Map currentProperties;

    HierarchicalConfigParser(final Map currentProperties) {
        if (currentProperties == null) {
            this.currentProperties = new LinkedHashMap();
        } else {
            this.currentProperties = currentProperties;
        }
    }

    public Map<String, Object> parse(String fileLoc) throws IOException {
        Map<String, Object> fileProperties = new LinkedHashMap<String, Object>();
        parse(fileLoc, fileProperties, true);
        return fileProperties;
    }

    private void parse(String fileLoc, Map<String, Object> fileProperties, boolean baseFile) throws IOException {
        InputStream configStream = getConfigAsStream(fileLoc);
        if (configStream == null) {
            LOG.warn("###############################");
            LOG.warn("#");
            LOG.warn("# Configuration file " + fileLoc + " not found!");
            LOG.warn("#");
            LOG.warn("###############################");
            return;
        }

        LOG.info("Parsing config: " + fileLoc);

        if (!baseFile) {
            fileProperties.put(fileLoc, new Properties());
        }
        Properties props = (Properties) fileProperties.get(fileLoc);
        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configStream);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Contents of config " + fileLoc + ": \n" + XmlJotter.jotNode(doc, true));
            }
        } catch (SAXException se) {
            IOException ioe = new IOException("Error parsing config resource: " + fileLoc);
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
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (!"param".equals(node.getNodeName())) {
                LOG.warn("Encountered non-param config node: " + node.getNodeName());
                continue;
            }
            Element param = (Element) node;
            String name = param.getAttribute("name");
            Boolean override = Boolean.TRUE;
            if (param.getAttribute("override") != null && param.getAttribute("override").trim().length() > 0) {
                override = new Boolean(param.getAttribute("override"));
            }
            if (name == null) {
                LOG.error("Unnamed parameter in config resource '" + fileLoc + "': " + XmlJotter.jotNode(param));
                continue;
            }
            NodeList contents = param.getChildNodes();
            // accumulate all content (preserving any XML content)
            try {
                content.setLength(0);
                for (int j = 0; j < contents.getLength(); j++) {
                    content.append(XmlJotter.jotNode(contents.item(j), true));
                }
                String contentValue;
                try {
                    contentValue = resolvePropertyTokens(content.toString(), fileProperties);
                } catch (Exception e) {
                    LOG.error("Exception caught parsing " + content, e);
                    throw new RuntimeException(e);
                }
                if (name.equals("config.location")) {
                    if (!contentValue.contains(ALTERNATE_BUILD_LOCATION_KEY)) {
                        parse(contentValue, fileProperties, false);
                    }
                } else {
                    if (props == null) {
                        updateProperties(fileProperties, override, name, contentValue, fileProperties);
                    } else {
                        updateProperties(props, override, name, contentValue, fileProperties);
                    }
                }
            } catch (XmlException te) {
                IOException ioe = new IOException("Error obtaining parameter '" + name + "' from config resource: " + fileLoc);
                ioe.initCause(te);
                throw ioe;
            }
        }
        LOG.info("Parsed config: " + fileLoc);
    }

    private void updateProperties(Map props, Boolean override, String name, String value, Map<String, Object> fileProperties) {
        if (value == null || "null".equals(value)) {
            LOG.warn("Not adding property [" + name + "] because it is null - most likely no token could be found for substituion.");
            return;
        }
        if (override) {
            setProperty(props, name, value);
        } else {
            if (!override && !fileProperties.containsKey(name)) {
                setProperty(props, name, value);
            }
        }
    }

    private void setProperty(Map map, String name, String value) {
        if (map.containsKey(name)) {
            map.remove(name);
        }
        map.put(name, value);
    }

    public static InputStream getConfigAsStream(String fileLoc) throws MalformedURLException, IOException {
        if (fileLoc.lastIndexOf("classpath:") > -1) {
            String configName = fileLoc.split("classpath:")[1];
            /*ClassPathResource cpr = new  ClassPathResource(configName, Thread.currentThread().getContextClassLoader());
			if (cpr.exists()) {
			    return cpr.getInputStream();
			} else {
			    return null;
			}*/
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(configName);
        } else if (fileLoc.lastIndexOf("http://") > -1 || fileLoc.lastIndexOf("file:/") > -1) {
            return new URL(fileLoc).openStream();
        } else {
            try {
                return new FileInputStream(fileLoc);
            } catch (FileNotFoundException e) {
                return null; // logged by caller
            }
        }
    }

    private String resolvePropertyTokens(String content, Map<String, Object> properties) {
        // TODO: consider implementing with iteration instead of recursion, and using the jakarta commons support for
        // parameter expansion
        if (content.contains(VAR_START_TOKEN)) {
            int tokenStart = content.indexOf(VAR_START_TOKEN);
            int tokenEnd = content.indexOf(VAR_END_TOKEN, tokenStart + VAR_START_TOKEN.length());
            if (tokenEnd == -1) {
                throw new RuntimeException("No ending bracket on token in value " + content);
            }
            String token = content.substring(tokenStart + VAR_START_TOKEN.length(), tokenEnd);
            String tokenValue = null;

            // get all the properties from all the potentially nested configs in
            // the master set
            // of propertiesUsed. Do it now so that all the values are available
            // for token replacement
            // next iteration
            //
            // The properties map is sorted with the top of the hierarchy as the
            // first element in the iteration, however
            // we want to include starting with the bottom of the hierarchy, so
            // we will iterate over the Map in reverse
            // order (this reverse iteration fixes the bug referenced by EN-68.
            LinkedList<Map.Entry<String, Object>> propertiesList = new LinkedList<Map.Entry<String, Object>>(properties.entrySet());
            Collections.reverse(propertiesList);
            for (Map.Entry<String, Object> config : propertiesList) {
                if (!(config.getValue() instanceof Properties)) {
                    if (token.equals(config.getKey())) {
                        tokenValue = (String) config.getValue();
                        break;
                    } else {
                        continue;
                    }
                }
                Properties configProps = (Properties) config.getValue();
                tokenValue = (String) configProps.get(token);
                if (tokenValue != null) {
                    break;
                }

                LOG.debug("Found token " + token + " in included config file " + config.getKey());
            }

            if (tokenValue == null) {
                if (token.contains(ALTERNATE_BUILD_LOCATION_KEY)) {
                    return token;
                }
                LOG.debug("Did not find token " + token + " in local properties.  Looking in parent.");
                tokenValue = (String) this.currentProperties.get(token);
                if (tokenValue == null) {
                    LOG.debug("Did not find token " + token + " in parent properties.  Looking in system properties.");
                    tokenValue = System.getProperty(token);
                    if (tokenValue == null) {
                        LOG.warn("Did not find token " + token + " in all available configuration properties!");
                    } else {
                        LOG.debug("Found token " + token + " in sytem properties");
                    }
                } else {
                    LOG.debug("Found token " + token + "=" + tokenValue + " in parent.");
                }
            } else {
                LOG.debug("Found token in local properties");
            }

            // NOTE: this will result in any variables that are not found being replaced with the string 'null'
            // this is the place to change that behavior (e.g. if (tokenvalue == null) do something else)
            String tokenizedContent = content.substring(0, tokenStart) + tokenValue + content.substring(tokenEnd + VAR_END_TOKEN.length(), content.length());
            // give it back to this method so we can have multiple tokens per
            // config entry.
            return resolvePropertyTokens(tokenizedContent, properties);
        }

        return content;
    }
}
