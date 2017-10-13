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
package org.kuali.rice.edl.impl;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;

import org.kuali.rice.edl.framework.workflow.EDocLitePostProcessor;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * Used by {@link EDocLitePostProcessorTest} to test the {@link #postEvent(Long, Object, String)} method 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TestEDocLitePostProcessor extends EDocLitePostProcessor {

    public static final String CONTEXT_NAME = "/edl-test";

    public static String getURL(Document edlDoc) throws XPathExpressionException {
        return System.getProperty("basedir") + CONTEXT_NAME;
    }

    public static Document getEDLContent(DocumentRouteHeaderValue routeHeader) throws Exception {
        String content = "<content><data>yahoo</data></content>";
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(content)));
        return doc;
    }

}
