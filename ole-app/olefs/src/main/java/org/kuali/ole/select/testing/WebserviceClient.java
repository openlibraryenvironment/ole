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
package org.kuali.ole.select.testing;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPBinding;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

public class WebserviceClient {

    private ConfigurationService kualiConfigurationService;
    static String preOrderUrlProperty = null;
    private final static Logger logger = Logger.getLogger(org.kuali.ole.select.testing.PreOrderService_Service.class.getName());

    public WebserviceClient() {
        kualiConfigurationService = SpringContext.getBean(ConfigurationService.class);
        String environment = kualiConfigurationService.getPropertyValueAsString(OLEConstants.ENVIRONMENT_KEY);
        preOrderUrlProperty = "PREORDER_URL";
    }

    /*
     * public String processCitationInput(String citationString,String routeRequestorReceipt, String requestorsNote,String
     * requestorsFirstName, String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity,
     * String requestorsState, String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String
     * requestorsEmail, String requestorsSMS, String requestorType) {
     */
    public String processCitationInput(String citationString, String routeRequestorReceipt, String requestorsNote,
                                       String requestorType, String requestorId) {
        String url = kualiConfigurationService.getPropertyValueAsString(preOrderUrlProperty);
        WebserviceInfo wsInfo = new WebserviceInfo();
        wsInfo.setUrl(url);
        wsInfo.setNameSpace(kualiConfigurationService.getPropertyValueAsString("PREORDER_NAMESPACE"));
        wsInfo.setServiceName(kualiConfigurationService.getPropertyValueAsString("PREORDER_SERVICENAME"));
        wsInfo.setPortName(kualiConfigurationService.getPropertyValueAsString("PREORDER_PORTNAME"));
        wsInfo.setOperationName(kualiConfigurationService.getPropertyValueAsString("PREORDER_CITATION_OPR"));

        String docNumber = null;
        URL preOrderUrl = null;

        try {
            URL baseUrl;
            baseUrl = org.kuali.ole.select.testing.PreOrderService_Service.class.getResource(".");
            preOrderUrl = new URL(wsInfo.getUrl() + "?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: " + preOrderUrl + ", retrying as a local file");
            logger.warning(e.getMessage());
        }
        QName serviceQName = new QName(wsInfo.getNameSpace(), wsInfo.getServiceName());
        PreOrderService_Service soapService = new PreOrderService_Service(preOrderUrl, serviceQName);
        soapService.addPort(new QName(wsInfo.getNameSpace(), wsInfo.getServiceName()), SOAPBinding.SOAP12HTTP_BINDING,
                wsInfo.getUrl());
        PreOrderService preOrderService = soapService.getPreOrderServicePort();

        try {
            /*
             * docNumber = preOrderService.createPreOrderForCitation(citationString,routeRequestorReceipt,
             * requestorsNote,requestorsFirstName, requestorsLastName, requestorsAddress1, requestorsAddress2, requestorsCity,
             * requestorsState, requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail, requestorsSMS,
             * requestorType);
             */
            docNumber = preOrderService.createPreOrderForCitation(citationString, routeRequestorReceipt,
                    requestorsNote, requestorType, requestorId);


            CreatePreOrderForCitationResponse createPreOrderForCitationResponse = new CreatePreOrderForCitationResponse();
            createPreOrderForCitationResponse.getReturn();

            if (isNumber(docNumber)) {
                docNumber = "Document Created successfully with Document ID : " + docNumber;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            docNumber = "ERROR: " + exception.getMessage();
        } finally {
            return docNumber;
        }
    }


    /*
     * public String processWebformInput(String title, String author, String edition, String series, String publisher, String
     * placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber, String
     * routeRequesterReceipt, String requestorsNote,String requestorsFirstName, String requestorsLastName, String
     * requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState, String requestorsZipCode,
     * String requestorsCountryCode, String requestorsPhone, String requestorsEmail, String requestorsSMS, String requestorType) {
     */
    public String processWebformInput(String title, String author, String edition, String series, String publisher,
                                      String placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber,
                                      String requestorsNote, String requestorType, String requestorId) {
        String url = kualiConfigurationService.getPropertyValueAsString(preOrderUrlProperty);
        WebserviceInfo wsInfo = new WebserviceInfo();
        wsInfo.setUrl(url);
        logger.info("wsInfo.getUrl() >>>>>>>>>>>" + wsInfo.getUrl());
        wsInfo.setNameSpace(kualiConfigurationService.getPropertyValueAsString("PREORDER_NAMESPACE"));
        wsInfo.setServiceName(kualiConfigurationService.getPropertyValueAsString("PREORDER_SERVICENAME"));
        wsInfo.setOperationName(kualiConfigurationService.getPropertyValueAsString("PREORDER_WFRM_OPR"));
        wsInfo.setPortName(kualiConfigurationService.getPropertyValueAsString("PREORDER_PORTNAME"));

        String docNumber = null;
        QName serviceQName = new QName(wsInfo.getNameSpace(), wsInfo.getServiceName());
        URL preOrderUrl = null;

        try {
            URL baseUrl;
            baseUrl = org.kuali.ole.select.testing.PreOrderService_Service.class.getResource(".");
            preOrderUrl = new URL(wsInfo.getUrl() + "?wsdl");
            logger.info("PreOrderService url : "+preOrderUrl);
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: " + preOrderUrl + ", retrying as a local file");
            logger.warning(e.getMessage());
        }

        PreOrderService_Service soapService = new PreOrderService_Service(preOrderUrl, serviceQName);
        soapService.addPort(new QName(wsInfo.getNameSpace(), wsInfo.getServiceName()), SOAPBinding.SOAP12HTTP_BINDING,
                wsInfo.getUrl());
        PreOrderService preOrderService = soapService.getPreOrderServicePort();

        try {
            /*
             * docNumber = preOrderService.createPreOrderForForm(title, author, edition, series, publisher, placeOfPublication,
             * yearOfPublication, standardNumber, typeOfStandardNumber, routeRequesterReceipt, requestorsNote,requestorsFirstName,
             * requestorsLastName, requestorsAddress1, requestorsAddress2, requestorsCity, requestorsState, requestorsZipCode,
             * requestorsCountryCode, requestorsPhone, requestorsEmail, requestorsSMS, requestorType);
             */
            docNumber = preOrderService.createPreOrderForForm(title, author, edition, series, publisher,
                    placeOfPublication, yearOfPublication, standardNumber, typeOfStandardNumber, requestorsNote,
                    requestorType, requestorId);
            CreatePreOrderForFormResponse createPreOrderForFormResponse = new CreatePreOrderForFormResponse();
            createPreOrderForFormResponse.getReturn();
            logger.info("PreOrderService Document Created successfully with Document ID : " +docNumber);

            if (isNumber(docNumber)) {
                docNumber = "Document Created successfully with Document ID : " + docNumber;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            docNumber = "ERROR: " + exception.getMessage();
        } finally {
            return docNumber;
        }

    }

    /*
     * public String processOpenUrlInput(String openUrlString, String routeRequesterReceipt, String requestorsNote,String
     * requestorsFirstName, String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity,
     * String requestorsState, String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String
     * requestorsEmail, String requestorsSMS, String requestorType) {
     */
    public String processOpenUrlInput(String openUrlString, String routeRequesterReceipt, String requestorsNote,
                                      String requestorType, String requestorId) {
        String url = kualiConfigurationService.getPropertyValueAsString(preOrderUrlProperty);

        WebserviceInfo wsInfo = new WebserviceInfo();
        wsInfo.setUrl(url);
        wsInfo.setNameSpace(kualiConfigurationService.getPropertyValueAsString("PREORDER_NAMESPACE"));
        wsInfo.setServiceName(kualiConfigurationService.getPropertyValueAsString("PREORDER_SERVICENAME"));
        wsInfo.setPortName(kualiConfigurationService.getPropertyValueAsString("PREORDER_PORTNAME"));
        wsInfo.setOperationName(kualiConfigurationService.getPropertyValueAsString("PREORDER_OPENURL_OPR"));

        String docNumber = null;
        QName serviceQName = new QName(wsInfo.getNameSpace(), wsInfo.getServiceName());
        URL preOrderUrl = null;

        try {
            URL baseUrl;
            baseUrl = org.kuali.ole.select.testing.PreOrderService_Service.class.getResource(".");
            preOrderUrl = new URL(wsInfo.getUrl() + "?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: " + preOrderUrl + ", retrying as a local file");
            logger.warning(e.getMessage());
        }

        PreOrderService_Service soapService = new PreOrderService_Service(preOrderUrl, serviceQName);
        soapService.addPort(new QName(wsInfo.getNameSpace(), wsInfo.getServiceName()), SOAPBinding.SOAP12HTTP_BINDING,
                wsInfo.getUrl());
        PreOrderService preOrderService = soapService.getPreOrderServicePort();


        try {
            /*
             * docNumber = preOrderService.createPreOrderForOpenURL(openUrlString,routeRequesterReceipt,
             * requestorsNote,requestorsFirstName, requestorsLastName, requestorsAddress1, requestorsAddress2, requestorsCity,
             * requestorsState, requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail, requestorsSMS,
             * requestorType);
             */
            docNumber = preOrderService.createPreOrderForOpenURL(openUrlString, routeRequesterReceipt, requestorsNote,
                    requestorType, requestorId);
            CreatePreOrderForOpenURLResponse createPreOrderForOpenURLResponse = new CreatePreOrderForOpenURLResponse();
            createPreOrderForOpenURLResponse.getReturn();
            if (isNumber(docNumber)) {
                docNumber = "Document Created successfully with Document ID : " + docNumber;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            docNumber = "ERROR: " + exception.getMessage();
        } finally {
            return docNumber;
        }
    }

    public boolean isNumber(String string) {
        char[] c = string.toCharArray();
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(c[i])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        WebserviceClient ws = new WebserviceClient();
        //   String[] input = new String[] { "http://resolver.example.edu/cgi?url_ver=Z39.88-2004&rft_val_fmt=info:ofi/fmt:kev:mtx:book&amp;rft.isbn=0836218310&rft.btitle=The+Far+Side+Gallery+3", "", "", "", "", "", "", "", "", "", "", "" };
        // ;

        /*
         * String response = ws.processOpenUrlInput(
         * "http://resolver.example.edu/cgi?url_ver=Z39.88-2004&rft_val_fmt=info:ofi/fmt:kev:mtx:book&amp;rft.isbn=0836218310&rft.btitle=The+Far+Side+Gallery+3"
         * , "","","","", "", "", "", "", "", "", "", "", "", "" );
         */
        String response = ws
                .processOpenUrlInput(
                        "http://resolver.example.edu/cgi?url_ver=Z39.88-2004&rft_val_fmt=info:ofi/fmt:kev:mtx:book&amp;rft.isbn=0836218310&rft.btitle=The+Far+Side+Gallery+3",
                        "", "", "", "");

    }

}
