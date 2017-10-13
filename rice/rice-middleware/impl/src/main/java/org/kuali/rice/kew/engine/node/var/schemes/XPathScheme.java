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
package org.kuali.rice.kew.engine.node.var.schemes;

import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathVariableResolver;

import org.apache.log4j.Logger;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.BranchState;
import org.kuali.rice.kew.engine.node.service.BranchService;
import org.kuali.rice.kew.engine.node.var.Property;
import org.kuali.rice.kew.engine.node.var.PropertyScheme;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.xml.sax.InputSource;


/**
 * A PropertyScheme that resolves the Property by evaluating it as an XPath expression.
 * DocumentRouteHeaderValue variables are set on the XPath instance so they are accessible.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XPathScheme implements PropertyScheme {
    private static final Logger LOG = Logger.getLogger(XPathScheme.class);

    public String getName() {
        return "xpath";
    }

    public String getShortName() {
        return "xpath";
    }

    public Object load(Property property, final RouteContext context) {
        XPath xpath = XPathHelper.newXPath();
        final BranchService branchService = KEWServiceLocator.getBranchService();
        xpath.setXPathVariableResolver(new XPathVariableResolver() {
            public Object resolveVariable(QName name) {
                LOG.debug("Resolving XPath variable: " + name);
                String value = branchService.getScopedVariableValue(context.getNodeInstance().getBranch(), BranchState.VARIABLE_PREFIX + name.getLocalPart());
                LOG.debug("Resolved XPath variable " + name + " to " + value);
                return value;
            }
        });
        try {
            String docContent = context.getDocument().getDocContent();
            LOG.debug("Executing xpath expression '" + property.locator + "' in doc '" + docContent + "'");
            return xpath.evaluate(property.locator, new InputSource(new StringReader(docContent)), XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            throw new RuntimeException("Error evaluating xpath expression '" + property.locator + "'", xpee);
        }
    }
}
