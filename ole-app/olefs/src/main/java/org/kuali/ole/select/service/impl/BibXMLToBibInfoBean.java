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

import org.kuali.ole.select.businessobject.BibInfoBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class BibXMLToBibInfoBean {

    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element rootElement;
    private BibInfoBean bibInfoBean;
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibInfoBeanToBibXML.class);

    public BibInfoBean getBibInfoBean(String path) throws Exception {
        initiateDocument(path);
        setBeanValuess();
        return bibInfoBean;
    }

    private void initiateDocument(String path) throws Exception {
        //File file = new File("\\olemarcfiles\\b2c3a5d9-0049-435a-9ac8-dbda01f2eb58.xml");
        File file = new File(path);
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(file);
        doc.getDocumentElement().normalize();
    }

    private void setBeanValuess() throws Exception {
        bibInfoBean = new BibInfoBean();
        NodeList nodeLst = doc.getElementsByTagName("bibData");
        for (int s = 0; s < nodeLst.getLength(); s++) {
            Node fstNode = nodeLst.item(s);
            if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                Element fstElmnt = (Element) fstNode;

                NodeList titleIdLst = fstElmnt.getElementsByTagName("titleId");
                Element titleIdElmnt = (Element) titleIdLst.item(0);
                NodeList titleId = titleIdElmnt.getChildNodes();
                if (titleId.item(0) != null) {
                    bibInfoBean.setTitleId(((Node) titleId.item(0)).getNodeValue());
                }

                NodeList titleLst = fstElmnt.getElementsByTagName("title");
                Element titleElmnt = (Element) titleLst.item(0);
                NodeList title = titleElmnt.getChildNodes();
                if (title.item(0) != null) {
                    bibInfoBean.setTitle(((Node) title.item(0)).getNodeValue());
                }

                NodeList authorLst = fstElmnt.getElementsByTagName("author");
                Element authorElmnt = (Element) authorLst.item(0);
                NodeList author = authorElmnt.getChildNodes();
                if (author.item(0) != null) {
                    bibInfoBean.setAuthor(((Node) author.item(0)).getNodeValue());
                }

                NodeList editionLst = fstElmnt.getElementsByTagName("edition");
                Element editionElmnt = (Element) editionLst.item(0);
                NodeList edition = editionElmnt.getChildNodes();
                if (edition.item(0) != null) {
                    bibInfoBean.setEdition(((Node) edition.item(0)).getNodeValue());
                }

                NodeList standardNumberLst = fstElmnt.getElementsByTagName("standardNumber");
                Element standardNumberElmnt = (Element) standardNumberLst.item(0);
                NodeList standardNumber = standardNumberElmnt.getChildNodes();
                if (standardNumber.item(0) != null) {
                    bibInfoBean.setStandardNumber(((Node) standardNumber.item(0)).getNodeValue());
                }

                NodeList publisherLst = fstElmnt.getElementsByTagName("publisher");
                Element publisherElmnt = (Element) publisherLst.item(0);
                NodeList publisher = publisherElmnt.getChildNodes();
                if (publisher.item(0) != null) {
                    bibInfoBean.setPublisher(((Node) publisher.item(0)).getNodeValue());
                }

                NodeList placeOfPublicationLst = fstElmnt.getElementsByTagName("placeOfPublication");
                Element placeOfPublicationElmnt = (Element) placeOfPublicationLst.item(0);
                NodeList placeOfPublication = placeOfPublicationElmnt.getChildNodes();
                if (placeOfPublication.item(0) != null) {
                    bibInfoBean.setPlaceOfPublication(((Node) placeOfPublication.item(0)).getNodeValue());
                }

                NodeList yearOfPublicationLst = fstElmnt.getElementsByTagName("yearOfPublication");
                Element yearOfPublicationElmnt = (Element) yearOfPublicationLst.item(0);
                NodeList yearOfPublication = yearOfPublicationElmnt.getChildNodes();
                if (yearOfPublication.item(0) != null) {
                    bibInfoBean.setYearOfPublication(((Node) yearOfPublication.item(0)).getNodeValue());
                }


                NodeList physicalDescriptionLst = fstElmnt.getElementsByTagName("physicalDescription");
                Element physicalDescriptionElmnt = (Element) physicalDescriptionLst.item(0);
                NodeList physicalDescription = physicalDescriptionElmnt.getChildNodes();
                if (physicalDescription.item(0) != null) {
                    bibInfoBean.setYearOfPublication(((Node) physicalDescription.item(0)).getNodeValue());
                }

                NodeList formatLst = fstElmnt.getElementsByTagName("format");
                Element formatElmnt = (Element) formatLst.item(0);
                NodeList format = formatElmnt.getChildNodes();
                if (format.item(0) != null) {
                    bibInfoBean.setFormat(((Node) format.item(0)).getNodeValue());
                }

                NodeList seriesLst = fstElmnt.getElementsByTagName("series");
                Element seriesElmnt = (Element) seriesLst.item(0);
                NodeList series = seriesElmnt.getChildNodes();
                if (series.item(0) != null) {
                    bibInfoBean.setSeries(((Node) series.item(0)).getNodeValue());
                }

                NodeList subjectsLst = fstElmnt.getElementsByTagName("subjects");
                Element subjectsElmnt = (Element) subjectsLst.item(0);
                NodeList subjects = subjectsElmnt.getChildNodes();
                if (subjects.item(0) != null) {
                    bibInfoBean.setSubjects(((Node) subjects.item(0)).getNodeValue());
                }

                NodeList priceLst = fstElmnt.getElementsByTagName("price");
                Element priceElmnt = (Element) priceLst.item(0);
                NodeList price = priceElmnt.getChildNodes();
                if (price.item(0) != null) {
                    bibInfoBean.setPrice(((Node) price.item(0)).getNodeValue());
                }

                NodeList requestorContactLst = fstElmnt.getElementsByTagName("requestorContact");
                Element requestorContactElmnt = (Element) requestorContactLst.item(0);
                NodeList requestorContact = requestorContactElmnt.getChildNodes();
                if (requestorContact.item(0) != null) {
                    bibInfoBean.setRequestorContact(((Node) requestorContact.item(0)).getNodeValue());
                }

                NodeList requestersNotesLst = fstElmnt.getElementsByTagName("requestersNotes");
                Element requestersNotesElmnt = (Element) requestersNotesLst.item(0);
                NodeList requestersNotes = requestersNotesElmnt.getChildNodes();
                if (requestersNotes.item(0) != null) {
                    bibInfoBean.setRequestersNotes(((Node) requestersNotes.item(0)).getNodeValue());
                }

                NodeList noOfCopiesLst = fstElmnt.getElementsByTagName("noOfCopies");
                Element noOfCopiesElmnt = (Element) noOfCopiesLst.item(0);
                NodeList noOfCopies = noOfCopiesElmnt.getChildNodes();
                if (noOfCopies.item(0) != null) {
                    bibInfoBean.setNoOfCopies(((Node) noOfCopies.item(0)).getNodeValue());
                }

                NodeList categoryLst = fstElmnt.getElementsByTagName("category");
                Element categoryElmnt = (Element) categoryLst.item(0);
                NodeList category = categoryElmnt.getChildNodes();
                if (category.item(0) != null) {
                    bibInfoBean.setCategory(((Node) category.item(0)).getNodeValue());
                }

                NodeList requestSourceLst = fstElmnt.getElementsByTagName("requestSource");
                Element requestSourceElmnt = (Element) requestSourceLst.item(0);
                NodeList requestSource = requestSourceElmnt.getChildNodes();
                if (requestSource.item(0) != null) {
                    bibInfoBean.setRequestSource(((Node) requestSource.item(0)).getNodeValue());
                }

                NodeList selectorLst = fstElmnt.getElementsByTagName("selector");
                Element selectorElmnt = (Element) selectorLst.item(0);
                NodeList selector = selectorElmnt.getChildNodes();
                if (selector.item(0) != null) {
                    bibInfoBean.setSelector(((Node) selector.item(0)).getNodeValue());
                }

                NodeList selectorNotesLst = fstElmnt.getElementsByTagName("selectorNotes");
                Element selectorNotesElmnt = (Element) selectorNotesLst.item(0);
                NodeList selectorNotes = selectorNotesElmnt.getChildNodes();
                if (selectorNotes.item(0) != null) {
                    bibInfoBean.setSelectorNotes(((Node) selectorNotes.item(0)).getNodeValue());
                }
            
/*            NodeList startPageLst = fstElmnt.getElementsByTagName("startPage");
            Element startPageElmnt = (Element) startPageLst.item(0);
            NodeList startPage = startPageElmnt.getChildNodes();
            System.out.println("startPage : "  + ((Node) startPage.item(0)).getNodeValue());
            
            NodeList endPageLst = fstElmnt.getElementsByTagName("endPage");
            Element endPageElmnt = (Element) endPageLst.item(0);
            NodeList endPage = endPageElmnt.getChildNodes();
            System.out.println("endPage : " + ((Node) endPage.item(0)).getNodeValue());*/

            }
        }
    }

}
