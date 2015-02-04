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
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.UUID;

public class BibInfoBeanToBibXML {
    private DocumentBuilderFactory docFactory;
    private DocumentBuilder docBuilder;
    private Document doc;
    private Element rootElement;
    private BibInfoBean bibInfoBean;
    private String externalDirectory;

    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibInfoBeanToBibXML.class);

    protected static ConfigurationService configurationService;

    private void initiateDocument() throws Exception {
        docFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docFactory.newDocumentBuilder();
    }

    private void createRootElement() throws Exception {
        doc = docBuilder.newDocument();
        rootElement = doc.createElement("bib");
        doc.appendChild(rootElement);
    }

    private void creatChildElements() throws Exception {

        Element bibData = doc.createElement("bibData");

        Element titleID = doc.createElement("titleId");
        titleID.appendChild(doc.createTextNode(bibInfoBean.getTitleId()));
        bibData.appendChild(titleID);

        Element title = doc.createElement("title");
        title.appendChild(doc.createTextNode(bibInfoBean.getTitle() != null ? bibInfoBean.getTitle() : ""));
        bibData.appendChild(title);

        Element author = doc.createElement("author");
        author.appendChild(doc.createTextNode(bibInfoBean.getAuthor() != null ? bibInfoBean.getAuthor() : ""));
        bibData.appendChild(author);

        Element edition = doc.createElement("edition");
        edition.appendChild(doc.createTextNode(bibInfoBean.getEdition() != null ? bibInfoBean.getEdition() : ""));
        bibData.appendChild(edition);

        Element standardNumber = doc.createElement("standardNumber");
        standardNumber.appendChild(doc.createTextNode(bibInfoBean.getStandardNumber() != null ? bibInfoBean.getStandardNumber() : ""));
        bibData.appendChild(standardNumber);

        Element publisher = doc.createElement("publisher");
        publisher.appendChild(doc.createTextNode(bibInfoBean.getPublisher() != null ? bibInfoBean.getPublisher() : ""));
        bibData.appendChild(publisher);

        Element placeOfPublication = doc.createElement("placeOfPublication");
        placeOfPublication.appendChild(doc.createTextNode(bibInfoBean.getPlaceOfPublication() != null ? bibInfoBean.getPlaceOfPublication() : ""));
        bibData.appendChild(placeOfPublication);

        Element yearOfPublication = doc.createElement("yearOfPublication");
        yearOfPublication.appendChild(doc.createTextNode(bibInfoBean.getYearOfPublication() != null ? bibInfoBean.getYearOfPublication().toString() : ""));
        bibData.appendChild(yearOfPublication);

        Element physicalDescription = doc.createElement("physicalDescription");
        physicalDescription.appendChild(doc.createTextNode(bibInfoBean.getPhysicalDescription() != null ? bibInfoBean.getPhysicalDescription().toString() : ""));
        bibData.appendChild(physicalDescription);

        Element format = doc.createElement("format");
        format.appendChild(doc.createTextNode(bibInfoBean.getFormat() != null ? bibInfoBean.getFormat().toString() : ""));
        bibData.appendChild(format);

        Element series = doc.createElement("series");
        series.appendChild(doc.createTextNode(bibInfoBean.getSeries() != null ? bibInfoBean.getSeries() : ""));
        bibData.appendChild(series);

        Element subjects = doc.createElement("subjects");
        subjects.appendChild(doc.createTextNode(bibInfoBean.getSubjects() != null ? bibInfoBean.getSubjects() : ""));
        bibData.appendChild(subjects);

        Element price = doc.createElement("price");
        price.appendChild(doc.createTextNode(bibInfoBean.getPrice() != null ? bibInfoBean.getPrice() : ""));
        bibData.appendChild(price);

        Element requestorContact = doc.createElement("requestorContact");
        requestorContact.appendChild(doc.createTextNode(bibInfoBean.getRequestorContact() != null ? bibInfoBean.getRequestorContact() : ""));
        bibData.appendChild(requestorContact);

        Element requestersNotes = doc.createElement("requestersNotes");
        requestersNotes.appendChild(doc.createTextNode(bibInfoBean.getRequestersNotes() != null ? bibInfoBean.getRequestersNotes() : ""));
        bibData.appendChild(requestersNotes);

        Element noOfCopies = doc.createElement("noOfCopies");
        noOfCopies.appendChild(doc.createTextNode(bibInfoBean.getNoOfCopies() != null ? bibInfoBean.getNoOfCopies() : ""));
        bibData.appendChild(noOfCopies);

        Element category = doc.createElement("category");
        category.appendChild(doc.createTextNode(bibInfoBean.getCategory() != null ? bibInfoBean.getCategory() : ""));
        bibData.appendChild(category);

        Element requestSource = doc.createElement("requestSource");
        requestSource.appendChild(doc.createTextNode(bibInfoBean.getRequestSource() != null ? bibInfoBean.getRequestSource() : ""));
        bibData.appendChild(requestSource);

        Element selector = doc.createElement("selector");
        selector.appendChild(doc.createTextNode(bibInfoBean.getSelector() != null ? bibInfoBean.getSelector() : ""));
        bibData.appendChild(selector);

        Element selectorNotes = doc.createElement("selectorNotes");
        selectorNotes.appendChild(doc.createTextNode(bibInfoBean.getSelectorNotes() != null ? bibInfoBean.getSelectorNotes() : ""));
        bibData.appendChild(selectorNotes);

        Element startPage = doc.createElement("startPage");
        startPage.appendChild(doc.createTextNode(bibInfoBean.getStartPage() != null ? bibInfoBean.getStartPage().toString() : ""));
        bibData.appendChild(startPage);

        Element endPage = doc.createElement("endPage");
        endPage.appendChild(doc.createTextNode(bibInfoBean.getEndPage() != null ? bibInfoBean.getEndPage().toString() : ""));
        bibData.appendChild(endPage);

        doc.getDocumentElement().appendChild(bibData);
    }

    private void writeToXMLFile() throws Exception {
        LOG.debug("before writing file...........................");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        externalDirectory = getConfigurationService().getPropertyValueAsString(OLEConstants.STAGING_DIRECTORY_KEY);
        String fileName = getConfigurationService().getPropertyValueAsString(OLEConstants.DOCSTORE_FILE_KEY);
        String filePath = externalDirectory + fileName;
        if (LOG.isInfoEnabled()) {
            LOG.info("externalDirectory------------->" + externalDirectory);
            LOG.info("filePath--------------->" + filePath);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            LOG.debug("Creating bib xml file...............");
            initiateDocument();
            createRootElement();
            creatChildElements();
        } else {
            LOG.debug("Appending bib xml file...............");
            initiateDocument();
            doc = docBuilder.parse(filePath);
            creatChildElements();
        }
        StreamResult result = new StreamResult(file);


        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        LOG.debug("after writing file...........................");
    }

    private void setBibInfoBean(BibInfoBean bibInfoBean) throws Exception {
        this.bibInfoBean = bibInfoBean;
    }

    public void exportToXMLFile(BibInfoBean bibInfoBean) throws Exception {
        setBibInfoBean(bibInfoBean);
        generateTitleId();
        //initiateDocument();
        //createRootElement();
        //creatChildElements();
        writeToXMLFile();
    }

    private void generateTitleId() throws Exception {
        UUID uniqueTitleId = UUID.randomUUID();
        bibInfoBean.setTitleId(uniqueTitleId.toString());
    }


    protected ConfigurationService getConfigurationService() {
        if (configurationService == null) {
            configurationService = SpringContext.getBean(ConfigurationService.class);
        }
        return configurationService;
    }
}
