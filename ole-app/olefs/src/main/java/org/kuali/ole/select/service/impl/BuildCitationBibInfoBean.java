/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.select.CitationParser;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class BuildCitationBibInfoBean {


    private Logger LOG = org.apache.log4j.Logger.getLogger(BuildCitationBibInfoBean.class);
    private DocumentBuilderFactory domFactory;
    private DocumentBuilder builder;
    private Document doc;
    private String responseXmlString;
    private CitationParser ciatationParser;
    private BibInfoBean bibInfoBean;
    private ArrayList<String> authorList;
    private String citation;


    public BibInfoBean getBean(String citationString, BibInfoBean bibInfoBean) throws Exception {
        this.bibInfoBean = bibInfoBean;
        if (null != citationString && !citationString.equals("")) {
            initiateDocument();
            citationResposeXML(citationString);
            iterateXML();
        }
        return this.bibInfoBean;
    }

    private void initiateDocument() throws Exception {
        domFactory = DocumentBuilderFactory.newInstance();
        builder = domFactory.newDocumentBuilder();
    }

    private void citationResposeXML(String citationString) throws Exception {
        ciatationParser = getCiatationParser();
        citation = citationString;
        String citationParamerter = "citation=";
        String citationStringToParse = citationParamerter + citationString;
        responseXmlString = ciatationParser.parse(citationStringToParse);
    }

    private void iterateXML() throws Exception {
        if (responseXmlString != null) {
            byte b[] = responseXmlString.getBytes();
            ByteArrayInputStream input = new ByteArrayInputStream(b);
            Document doc = builder.parse(input);
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//journal/*/text()");
            XPathExpression expr1 = xpath.compile("//journal/*");

            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            Object result1 = expr1.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            NodeList nodes1 = (NodeList) result1;

            authorList = new ArrayList();
            if (nodes.getLength() > 0) {
                //bibInfoBean = new BibInfoBean();
                for (int i = 0; i < nodes.getLength(); i++) {
                    String nodeName = nodes1.item(i).getNodeName();
                    String nodeValue = nodes.item(i).getNodeValue();
                    convertToBean(nodeName, nodeValue);
                }
            } else {
                String nodeName = "rft:atitle";
                String nodeValue = citation;
                convertToBean(nodeName, nodeValue);
                //throw new Exception("The Citation value is incorrect.");
            }
        } else {
            String nodeName = "rft:atitle";
            String nodeValue = citation;
            convertToBean(nodeName, nodeValue);
        }
    }

    private void convertToBean(String nodeName, String nodeValue) throws Exception {
        if (nodeName.equalsIgnoreCase("rft:atitle")) {
            //bibInfoBean.setMainTitle(nodeValue);
            bibInfoBean.setTitle(nodeValue);
        } else if (nodeName.equalsIgnoreCase("rft:stitle")) {
            bibInfoBean.setSubTitle(nodeValue);
        } else if (nodeName.equalsIgnoreCase("rft:au")) {
            authorList.add(nodeValue);
            //bibInfoBean.setAuthors(authorList);
            bibInfoBean.setAuthor(authorList.get(0));
        } else if (nodeName.equalsIgnoreCase("rft:spage")) {
            bibInfoBean.setStartPage(new Long(nodeValue));
        } else if (nodeName.equalsIgnoreCase("rft:epage")) {
            bibInfoBean.setEndPage(new Long(nodeValue));
        } else if (nodeName.equalsIgnoreCase("rft:genre")) {
            bibInfoBean.setCategory(nodeValue);
        } else if (nodeName.equalsIgnoreCase("rft:date")) {
            bibInfoBean.setYearOfPublication(nodeValue);
        }
    }

    public CitationParser getCiatationParser() {
        if (null == ciatationParser) {
            ciatationParser = new CitationParser();
        }
        return ciatationParser;
    }


    public void setCiatationParser(CitationParser ciatationParser) {
        this.ciatationParser = ciatationParser;
    }


}
