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
package org.kuali.rice.kew.role;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.rule.XmlConfiguredAttribute;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves qualifiers based on XPath configuration in the resolver's attribute.
 * 
 * <p>An example of the xml processed by this attribute follows:
 * 
 * <p><pre>
 * <resolverConfig>
 *   <baseXPathExpression>/xmlData/chartOrg</baseXPathExpression>
 *   <attributes name="chart">
 *     <xPathExpression>./chart</xPathExpression>
 *   </attributes>
 *   <attributes name="org">
 *     <xPathExpression>./org</xPathExpression>
 *   </attributes>
 * </resolverConfig>
 * </pre>
 * 
 * <p>There are 2 different types of qualifier resolvers, those that resolve compound
 * attribute sets and those that resolve simple attribute sets.  A simple attribute
 * set is one which includes only a single "qualifier" specification.  The example above
 * is compound because it includes both chart and org.
 * 
 * <p>When dealing with compound attribute sets, the baseXPathExpression is used to
 * define grouping for these compound sets.  It is therefore required that inside each
 * resulting element retrieved from the baseXPathExpression, there is only a single instance
 * of each qualifier.  If this is not the case, an error will be thrown.  For the example
 * above, the following XML would be evaluated successfully:
 * 
 * <p><pre>
 * <xmlData>
 *   <chartOrg>
 *     <chart>BL</chart>
 *     <org>BUS</org>
 *   </chartOrg>
 *   <chartOrg>
 *     <chart>IN</chart>
 *     <org>MED</org>
 *   </chartOrg>
 * </xmlData>
 * </pre>
 * 
 * <p>This would return 2 attributes sets, each with a chart and org in it.  The following
 * XML would cause the XPathQualifierResolver to throw an exception during processing.
 * 
 * <p><pre>
 * <xmlData>
 *   <chartOrg>
 *     <chart>BL</chart>
 *     <org>BUS</org>
 *     <chart>IN</chart>
 *     <org>MED</org>
 *   </chartOrg>
 * </xmlData>
 * </pre>
 * 
 * <p>In this case the resolver has no knowledge of how to group chart and org together.
 * What follows is an example of a resolver using a simple attribute set:
 * 
 * <p><pre>
 * <resolverConfig>
 *   <baseXPathExpression>/xmlData/accountNumbers</baseXPathExpression>
 *   <attributes name="accountNumber">
 *     <xPathExpression>./accountNumber</xPathExpression>
 *   </attributes>
 * </resolverConfig>
 * </pre>
 * 
 * <p>In this example, the following XML would return a List containing an Map<String, String>
 * for each account number when resolved.
 * 
 * <p><pre>
 * <xmlData>
 *   <accountNumbers>
 *     <accountNumber>12345</accountNumber>
 *     <accountNumber>54321</accountNumber>
 *     <accountNumber>102030</accountNumber>
 *     <accountNumber>302010</accountNumber>
 *   </accountNumbers>
 * </xmlData>
 * 
 * <p>The baseXPathExpression is optional and defaults to the root of the document if not specified.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XPathQualifierResolver implements QualifierResolver, XmlConfiguredAttribute {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XPathQualifierResolver.class);

	private ExtensionDefinition extensionDefinition;

	public List<Map<String, String>> resolve(RouteContext context) {
			ResolverConfig config = parseResolverConfig();
			Document xmlContent = context.getDocumentContent().getDocument();
			XPath xPath = XPathHelper.newXPath();
			boolean isCompoundMap = config.getExpressionMap().size() > 1;
			try {
				List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
				NodeList baseElements = (NodeList)xPath.evaluate(config.getBaseXPathExpression(), xmlContent, XPathConstants.NODESET);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found " + baseElements.getLength() + " baseElements to parse for Map<String, String>s using document XML:" + XmlJotter.jotDocument(xmlContent));
				}
				for (int index = 0; index < baseElements.getLength(); index++) {
					Node baseNode = baseElements.item(index);
					if (isCompoundMap) {
						handleCompoundMap(baseNode, maps, config, xPath);
					} else {
						handleSimpleMap(baseNode, maps, config, xPath);
					}
				}
				return maps;
			} catch (XPathExpressionException e) {
				throw new RiceRuntimeException("Encountered an issue executing XPath.", e);
			}
	}
	
	protected void handleCompoundMap(Node baseNode, List<Map<String, String>> maps, ResolverConfig config, XPath xPath) throws XPathExpressionException {
		Map<String, String> map = new HashMap<String, String>();
		for (String attributeName : config.getExpressionMap().keySet()) {
			String xPathExpression = config.getExpressionMap().get(attributeName);
			NodeList attributes = (NodeList)xPath.evaluate(xPathExpression, baseNode, XPathConstants.NODESET);
			if (attributes.getLength() > 1) {
				throw new RiceRuntimeException("Found more than more XPath result for an attribute in a compound attribute set for attribute: " + attributeName + " with expression " + xPathExpression);
			} else if (attributes.getLength() != 0) {
				String attributeValue = ((Element)attributes.item(0)).getTextContent();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Adding values to compound Map<String, String>: " + attributeName + "::" + attributeValue);
				}
				map.put(attributeName, attributeValue);
			}
		}
		maps.add(map);
	}
	
	protected void handleSimpleMap(Node baseNode, List<Map<String, String>> maps, ResolverConfig config, XPath xPath) throws XPathExpressionException {
		String attributeName = config.getExpressionMap().keySet().iterator().next();
		String xPathExpression = config.getExpressionMap().get(attributeName);
		NodeList attributes = (NodeList)xPath.evaluate(xPathExpression, baseNode, XPathConstants.NODESET);
		for (int index = 0; index < attributes.getLength(); index++) {
			Element attributeElement = (Element)attributes.item(index);
			Map<String, String> map = new HashMap<String, String>();
			String attributeValue = attributeElement.getTextContent();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Adding values to simple Map<String, String>: " + attributeName + "::" + attributeValue);
			}
			map.put(attributeName, attributeValue);
			maps.add(map);
		}
	}
	
	protected ResolverConfig parseResolverConfig() {
		if (extensionDefinition.getConfiguration() != null
               && extensionDefinition.getConfiguration().get(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA) == null) {
			throw new RiceRuntimeException("Failed to locate a RuleAttribute for the given XPathQualifierResolver");
		}
		try {
			ResolverConfig resolverConfig = new ResolverConfig();
			String xmlConfig = extensionDefinition.getConfiguration().get(KewApiConstants.ATTRIBUTE_XML_CONFIG_DATA);
			XPath xPath = XPathHelper.newXPath();
			String baseExpression = xPath.evaluate("//resolverConfig/baseXPathExpression", new InputSource(new StringReader(xmlConfig)));
			if (!StringUtils.isEmpty(baseExpression)) {
				resolverConfig.setBaseXPathExpression(baseExpression);
			}
            //We need to check for two possible xml configurations
            //1 - 'attributes'
            //2- 'qualifier' (legacy)
			NodeList qualifiers = (NodeList)xPath.evaluate("//resolverConfig/attributes", new InputSource(new StringReader(xmlConfig)), XPathConstants.NODESET);
            NodeList qualifiersLegacy = (NodeList)xPath.evaluate("//resolverConfig/qualifier", new InputSource(new StringReader(xmlConfig)), XPathConstants.NODESET);

            if ((qualifiers == null || qualifiers.getLength() == 0) && (qualifiersLegacy == null || qualifiersLegacy.getLength() == 0)) {
				throw new RiceRuntimeException("Invalid qualifier resolver configuration.  Must contain at least one qualifier!");
			}
            //check for standard qualifiers (those using 'attributes' xml elements) and add if they exist
			for (int index = 0; index < qualifiers.getLength(); index++) {
				Element qualifierElement = (Element)qualifiers.item(index);
				String name = qualifierElement.getAttribute("name");
				NodeList expressions = qualifierElement.getElementsByTagName("xPathExpression");
				if (expressions.getLength() != 1) {
					throw new RiceRuntimeException("There should only be a single xPathExpression per qualifier");
				}
				Element expressionElement = (Element)expressions.item(0);
				resolverConfig.getExpressionMap().put(name, expressionElement.getTextContent());
			}

            //check for legacy qualifiers (those using 'qualifier' xml elements) and add if they exist
			for (int index = 0; index < qualifiersLegacy.getLength(); index++) {
				Element qualifierElement = (Element)qualifiersLegacy.item(index);
				String name = qualifierElement.getAttribute("name");
				NodeList expressions = qualifierElement.getElementsByTagName("xPathExpression");
				if (expressions.getLength() != 1) {
					throw new RiceRuntimeException("There should only be a single xPathExpression per qualifier");
				}
				Element expressionElement = (Element)expressions.item(0);
				resolverConfig.getExpressionMap().put(name, expressionElement.getTextContent());
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Using Resolver Config Settings: " + resolverConfig.toString());
			}
			return resolverConfig;
		} catch (XPathExpressionException e) {
			throw new RiceRuntimeException("Encountered an error parsing resolver config.", e);
		}
	}

    @Override
    public void setExtensionDefinition(ExtensionDefinition ruleAttribute) {
        extensionDefinition = ruleAttribute;
    }

    class ResolverConfig {
		private String baseXPathExpression = "/";
		private Map<String, String> expressionMap = new HashMap<String, String>();
		public String getBaseXPathExpression() {
			return this.baseXPathExpression;
		}
		public void setBaseXPathExpression(String baseXPathExpression) {
			this.baseXPathExpression = baseXPathExpression;
		}
		public Map<String, String> getExpressionMap() {
			return this.expressionMap;
		}
		public void setExpressionMap(Map<String, String> expressionMap) {
			this.expressionMap = expressionMap;
		}
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(  '\n' );
			sb.append("ResolverConfig Parameters\n");
			sb.append( "      baseXPathExpression: " + baseXPathExpression + "\n" );
			sb.append( "      expressionMap:\n" );
			for (Map.Entry<String, String> entry : expressionMap.entrySet()) {
				sb.append( "            " + entry.getKey() + ": " + entry.getValue() + "\n" );
			}
			return sb.toString();
		}
	}

}
