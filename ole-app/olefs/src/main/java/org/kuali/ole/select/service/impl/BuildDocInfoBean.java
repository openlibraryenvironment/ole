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

import org.kuali.ole.select.businessobject.DocInfoBean;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BuildDocInfoBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BuildDocInfoBean.class);

    private DocInfoBean docInfoBean;
    private List<DocInfoBean> docInfoBeanList = null;
    private StringBuilder stringBuilder = null;
    private String noOfRecords = null;
    private String attribute = null;


    public List<DocInfoBean> getDocInfoBeanList(String query) {
        docInfoBeanList = new ArrayList<DocInfoBean>();
        try {
            initiateDocument(query);
        } catch (Exception ex) {
            LOG.error("Exception while getting DocInfoBeanList"+ex);
        }
        return docInfoBeanList;
    }

    private void initiateDocument(String query) throws Exception {

        if (query == null || query.length() == 0) {
            Properties properties = new Properties();
            try {
                //properties.load(new FileInputStream("org/kuali/ole/select/service/impl/bibinfo.properties"));
                query = properties.getProperty("filename");
            } catch (Exception e) {
                LOG.error("Exception while initiating the document"+e);
            }
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            //boolean author = false;
            boolean author_display = false;

            boolean dateOfPublication = false;

            boolean date_d = false;

            //boolean description = false;
            boolean description_display = false;

            boolean dimentions = false;

            boolean docType = false;

            boolean extent = false;

            boolean generalNote = false;

            //boolean isbn = false;
            boolean isbn_display = false;

            boolean issn_display = false;

            boolean mainEntryPersonalNameComposite = false;

            boolean mainEntryPersonalNameComposite_suggest = false;

            boolean modifyingAgency = false;

            boolean modifyingAgency_suggest = false;

            //boolean nameOfPublisher = false;
            boolean publisher_display = false;

            //boolean placeOfPublication = false;
            boolean publicationPlace_search = false;

            boolean placeOfPublication_suggest = false;

            boolean price_f = false;

            boolean remainderOfTitle = false;

            boolean statementOfResponsibility = false;

            //boolean subject = false;
            boolean subject_display = false;

            //boolean title = false;
            boolean title_display = false;

            boolean titleStatementComposite = false;

            boolean title_suggest = false;

            boolean topicalTermorgeographicnameElement = false;

            boolean yearOfPublication = false;

            boolean all_controlFields = false;

            boolean all_subfields = false;

            boolean all_text = false;

            boolean itemLinks = false;

            boolean uniqueId = false;

            boolean instanceId = false;

            boolean bibIdentifier = false;

            boolean publisher_search = false;

            boolean localIdentifier_search = false;

            boolean publicationDate_search = false;


            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes)
                    throws SAXException {


                if (qName.equalsIgnoreCase("doc")) {
                    docInfoBean = new DocInfoBean();
                }
                if (qName.equalsIgnoreCase("result")) {
                    noOfRecords = attributes.getValue("numFound");
                }


                if (qName.equalsIgnoreCase("str")) {
                    String attribute = attributes.getValue("name");
                    if (attributes.getValue("name") != null) {
                        if ("Description_display".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            description_display = true;
                        } else if ("ISBN_display".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            isbn_display = true;
                        } else if ("Subject_display ".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            subject_display = true;
                        } else if ("all_controlFields".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            all_controlFields = true;
                        } else if ("all_subfields".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            all_subfields = true;
                        } else if ("all_text".equalsIgnoreCase(attribute)) {
                            stringBuilder = new StringBuilder();
                            all_text = true;
                        }
                    }
                }


                if (qName.equalsIgnoreCase("arr")) {

                    attribute = attributes.getValue("name");

                    if ("Author_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        author_display = true;
                    } else if ("Description_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        description_display = true;
                    } else if ("Title_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        title_display = true;
                    } else if ("Publisher_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        //nameOfPublisher= true;
                        publisher_display = true;
                    } else if ("Publisher_search".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        //nameOfPublisher= true;
                        publisher_search = true;
                    } else if ("LocalId_search".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        localIdentifier_search = true;
                    } else if ("LocalId_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        localIdentifier_search = true;
                    } else if ("PublicationDate_search".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        //nameOfPublisher= true;
                        publicationDate_search = true;
                    } else if ("ISBN_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        isbn_display = true;
                    } else if ("Subject_display".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        subject_display = true;
                    } else if ("all_controlFields".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        all_controlFields = true;
                    } else if ("all_subfields".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        all_subfields = true;
                    } else if ("all_text".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        all_text = true;
                    } else if ("ItemLinks".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        itemLinks = true;
                    } else if ("instanceIdentifier".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        instanceId = true;
                    } else if ("bibIdentifier".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        bibIdentifier = true;
                    }
                }


                if (qName.equalsIgnoreCase("str") || qName.equalsIgnoreCase("date") || qName.equalsIgnoreCase("float")) {

                    if ("DateOfPublication".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        dateOfPublication = true;
                    } else if ("Date_d".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        date_d = true;
                    } else if ("Dimentions".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        dimentions = true;
                    } else if ("DocType".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        docType = true;
                    } else if ("Extent".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        extent = true;
                    } else if ("GeneralNote".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        generalNote = true;
                    } else if ("MainEntryPersonalNameComposite".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        mainEntryPersonalNameComposite = true;
                    } else if ("MainEntryPersonalNameComposite_suggest".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        mainEntryPersonalNameComposite_suggest = true;
                    } else if ("ModifyingAgency".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        modifyingAgency = true;
                    } else if ("ModifyingAgency_suggest".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        modifyingAgency_suggest = true;
                    } else if ("PublicationPlace_search".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        publicationPlace_search = true;
                    } else if ("PlaceOfPublication_suggest".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        placeOfPublication_suggest = true;
                    } else if ("Price_f".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        price_f = true;
                    } else if ("RemainderOfTitle".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        remainderOfTitle = true;
                    } else if ("StatementOfResponsibility".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        statementOfResponsibility = true;
                    } else if ("TitleStatementComposite".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        titleStatementComposite = true;
                    } else if ("Title_suggest".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        title_suggest = true;
                    } else if ("TopicalTermorgeographicnameElement".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        topicalTermorgeographicnameElement = true;
                    } else if ("YearOfPublication".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        yearOfPublication = true;
                    }
            /* else if ("id".equalsIgnoreCase(attributes.getValue("name"))) {
                 id= true;
              }*/
                    else if ("uniqueId".equalsIgnoreCase(attributes.getValue("name"))) {
                        stringBuilder = new StringBuilder();
                        uniqueId = true;
                    }
                }
            }

            public void endElement(String uri, String localName,
                                   String qName)
                    throws SAXException {
                String value = null;
                if (stringBuilder != null)
                    if (!"".equals(stringBuilder.toString())) {
                        value = stringBuilder.toString();
                    }
                if (author_display) {
                    docInfoBean.setAuthor_display(value);
                    stringBuilder = null;
                    author_display = false;
                } else if (dateOfPublication) {
                    docInfoBean.setDateOfPublication(value);
                    stringBuilder = null;
                    dateOfPublication = false;
                } else if (date_d) {
                    docInfoBean.setDate_d(value);
                    stringBuilder = null;
                    date_d = false;
                } else if (description_display) {
                    docInfoBean.setDescription_display(value);
                    stringBuilder = null;
                    //System.out.println("description===    "+new String(ch, start, length));
                    description_display = false;
                } else if (dimentions) {
                    docInfoBean.setDimentions(value);
                    stringBuilder = null;
                    dimentions = false;
                } else if (docType) {
                    docInfoBean.setDocType(value);
                    stringBuilder = null;
                    docType = false;
                } else if (extent) {
                    docInfoBean.setExtent(value);
                    stringBuilder = null;
                    extent = false;
                } else if (generalNote) {
                    docInfoBean.setGeneralNote(value);
                    stringBuilder = null;
                    generalNote = false;
                } else if (isbn_display) {
                    docInfoBean.setIsbn_display(value);
                    stringBuilder = null;
                    // System.out.println(new String(ch, start, length));
                    isbn_display = false;
                } else if (mainEntryPersonalNameComposite) {
                    docInfoBean.setMainEntryPersonalNameComposite(value);
                    stringBuilder = null;
                    mainEntryPersonalNameComposite = false;
                } else if (mainEntryPersonalNameComposite_suggest) {
                    docInfoBean.setMainEntryPersonalNameComposite_suggest(value);
                    stringBuilder = null;
                    mainEntryPersonalNameComposite_suggest = false;
                } else if (modifyingAgency) {
                    docInfoBean.setModifyingAgency(value);
                    stringBuilder = null;
                    modifyingAgency = false;
                } else if (modifyingAgency_suggest) {
                    docInfoBean.setModifyingAgency_suggest(value);
                    stringBuilder = null;
                    modifyingAgency_suggest = false;
                } else if (publisher_display) {
                    docInfoBean.setPublisher_display(value);
                    stringBuilder = null;
                    publisher_display = false;
                } else if (publisher_search) {
                    docInfoBean.setPublisher_search(value);
                    stringBuilder = null;
                    publisher_search = false;
                } else if (localIdentifier_search) {
                    docInfoBean.setLocalIdentifier_search(value);
                    stringBuilder = null;
                    localIdentifier_search = false;
                } else if (publicationDate_search) {
                    if ("Date could not be determined".equalsIgnoreCase(value)) {
                        docInfoBean.setPublicationDate_search("");
                    } else {
                        docInfoBean.setPublicationDate_search(value);
                    }
                    stringBuilder = null;
                    publicationDate_search = false;
                } else if (publicationPlace_search) {
                    docInfoBean.setPublicationPlace_search(value);
                    stringBuilder = null;
                    publicationPlace_search = false;
                } else if (placeOfPublication_suggest) {
                    docInfoBean.setPlaceOfPublication_suggest(value);
                    stringBuilder = null;
                    placeOfPublication_suggest = false;
                } else if (price_f) {
                    docInfoBean.setPrice_f(value);
                    stringBuilder = null;
                    price_f = false;
                } else if (remainderOfTitle) {
                    docInfoBean.setRemainderOfTitle(value);
                    stringBuilder = null;
                    remainderOfTitle = false;
                } else if (statementOfResponsibility) {
                    docInfoBean.setStatementOfResponsibility(value);
                    stringBuilder = null;
                    statementOfResponsibility = false;
                } else if (subject_display) {
                    docInfoBean.setSubject_display(value);
                    stringBuilder = null;
                    subject_display = false;
                } else if (title_display) {
                    docInfoBean.setTitle_display(value);
                    stringBuilder = null;
                    title_display = false;
                } else if (titleStatementComposite) {
                    docInfoBean.setTitleStatementComposite(value);
                    stringBuilder = null;
                    titleStatementComposite = false;
                } else if (title_suggest) {
                    docInfoBean.setTitle_suggest(value);
                    stringBuilder = null;
                    title_suggest = false;
                } else if (topicalTermorgeographicnameElement) {
                    docInfoBean.setTopicalTermorgeographicnameElement(value);
                    stringBuilder = null;
                    topicalTermorgeographicnameElement = false;
                } else if (yearOfPublication) {
                    docInfoBean.setYearOfPublication(value);
                    stringBuilder = null;
                    yearOfPublication = false;
                } else if (all_controlFields) {
                    docInfoBean.setAll_controlFields(value);
                    stringBuilder = null;
                    // System.out.println(value);
                    all_controlFields = false;
                } else if (all_subfields) {
                    docInfoBean.setAll_subfields(value);
                    stringBuilder = null;
                    // System.out.println(value);
                    all_subfields = false;
                } else if (all_text) {
                    docInfoBean.setAll_text(value);
                    stringBuilder = null;
                    // System.out.println(value);
                    all_text = false;
                } else if (uniqueId) {
                    docInfoBean.setUniqueId(value);
                    stringBuilder = null;
                    uniqueId = false;
                } else if (bibIdentifier) {
                    docInfoBean.setBibIdentifier(value);
                    stringBuilder = null;
                    bibIdentifier = false;
                } else if (itemLinks) {
                    docInfoBean.setTitleId(value);
                    stringBuilder = null;
                    itemLinks = false;
                } else if (qName.equalsIgnoreCase("doc")) {
                    docInfoBean.setNoOfRecords(noOfRecords);
                    docInfoBeanList.add(docInfoBean);
                } else if (instanceId) {
                    docInfoBean.setTitleId(value);
                    stringBuilder = null;
                    instanceId = false;
                }
            /* if(id){
            //docInfoBean.setId(stringBuilder.toString());
            docInfoBean.setTitleId(stringBuilder.toString());
            id=false;
        }*/

            }

            public void characters(char ch[], int start, int length)
                    throws SAXException {

                if (stringBuilder != null)
                    stringBuilder.append(ch, start, length);
            }

        };

        WebClientServiceImpl webclientService = new WebClientServiceImpl();
        String url = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSEARCH_URL_KEY);
        String contentType = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(OLEConstants.DOCSTORE_APP_CONTENT_TYPE_KEY);
        String response = webclientService.sendRequest(url, contentType, query);
        InputStream is = new ByteArrayInputStream(response.getBytes("UTF-8"));
        saxParser.parse(is, handler);
    }

}

   
