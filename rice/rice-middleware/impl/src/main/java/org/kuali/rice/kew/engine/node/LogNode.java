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
package org.kuali.rice.kew.engine.node;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.RouteHelper;
import org.kuali.rice.kew.engine.node.var.PropertyScheme;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * A node which Logs messages to Log4j.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LogNode implements SimpleNode {
    private static final Logger LOG = Logger.getLogger(LogNode.class);

    public SimpleResult process(RouteContext context, RouteHelper helper) throws Exception {
        LOG.error("processing");
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String contentFragment = context.getNodeInstance().getRouteNode().getContentFragment();
        LOG.error("contentFragment=" + contentFragment);
        Document d = db.parse(new InputSource(new StringReader(contentFragment)));
        Element e = d.getDocumentElement();
        String name = null;
        NodeList list = e.getElementsByTagName("log");
        if (list != null && list.getLength() > 0) {
            name = list.item(0).getTextContent();
        }
        list = e.getElementsByTagName("level");
        String level = "info";
        if (list != null && list.getLength() > 0) {
            level = list.item(0).getTextContent().toLowerCase();
        }
        LOG.error("doc content: "+ context.getDocument().getDocContent());
        String valueRef = e.getElementsByTagName("message").item(0).getTextContent();
        Object retrievedVal = PropertiesUtil.retrieveProperty(valueRef, PropertyScheme.LITERAL_SCHEME, context);
        LOG.error("retrieved value '" + retrievedVal + " for message '" + valueRef);
        Logger l;
        if (name == null) {
            l = LOG;
        } else {
            l = Logger.getLogger(name);
        }
        l.log(Level.toLevel(level), retrievedVal);
        return new SimpleResult(true);
    }

}
