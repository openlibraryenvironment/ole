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

import org.apache.commons.lang.StringEscapeUtils;
import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.PopulateBibInfoService;
import org.kuali.ole.select.testing.PreOrderService;
import org.kuali.ole.select.testing.WSException_Exception;

public class PreOrderServiceImpl implements PreOrderService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(PreOrderServiceImpl.class);
    private PopulateBibInfoService populateBibInfoService;
    private BibInfoBean bibInfoBean;

    @Override
    /*
     * public String createPreOrderForCitation(String citationString, String routeRequesterReceipt, String requestorsNote, String
     * requestorsFirstName, String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity,
     * String requestorsState, String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String
     * requestorsEmail, String requestorsSMS, String requestorType) throws WSException_Exception {
     */
    public String createPreOrderForCitation(String citationString, String routeRequesterReceipt, String requestorsNote,
                                            String requestorType, String requestorId) throws WSException_Exception {
        String docNumber = null;
        try {
            PopulateBibInfoService populateBibInfoService = getPopulateBibInfoService();

            BibInfoBean bibInfoBean = getBibInfoBean();
            /*
             * setRequestorDetail(requestorsNote, requestorsFirstName, requestorsLastName, requestorsAddress1, requestorsAddress2,
             * requestorsCity, requestorsState, requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail,
             * requestorsSMS, requestorType);
             */
            setRequestorDetail(requestorsNote, requestorType, requestorId);
            docNumber = populateBibInfoService.processBibInfoForCitation(citationString, bibInfoBean);
        } catch (Exception E) {

        }
        return docNumber;
    }

    private BibInfoBean getBibInfoBean() {
        bibInfoBean = new BibInfoBean();
        return bibInfoBean;
    }

    private PopulateBibInfoService getPopulateBibInfoService() {
        if (null == populateBibInfoService) {
            populateBibInfoService = new PopulateBibInfoServiceImpl();
        }
        return populateBibInfoService;
    }

    public void setPopulateBibInfoService(PopulateBibInfoService populateBibInfoService) {
        this.populateBibInfoService = populateBibInfoService;
    }

    @Override
    /*
     * public String createPreOrderForOpenURL(String openUrlString,String routeRequestorReceipt, String requestorsNote,String
     * requestorsFirstName, String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity,
     * String requestorsState, String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String
     * requestorsEmail, String requestorsSMS, String requestorType) throws WSException_Exception {
     */
    public String createPreOrderForOpenURL(String openUrlString, String routeRequestorReceipt, String requestorsNote,
                                           String requestorType, String requestorId) throws WSException_Exception {
        PopulateBibInfoService populateBibInfoService = new PopulateBibInfoServiceImpl();
        BibInfoBean bibInfoBean = getBibInfoBean();
        /*
         * setRequestorDetail(requestorsNote,requestorsFirstName, requestorsLastName, requestorsAddress1, requestorsAddress2,
         * requestorsCity, requestorsState, requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail,
         * requestorsSMS, requestorType);
         */
        setRequestorDetail(requestorsNote, requestorType, requestorId);
        String docNumber = null;
        try {
            docNumber = populateBibInfoService.processBibInfoForOperURL(openUrlString, bibInfoBean);
        } catch (Exception E) {

        }
        return docNumber;
    }

    @Override
    /*
     * public String createPreOrderForForm(String title, String author, String edition, String series, String publisher, String
     * placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber, String
     * routeRequestorReceipt, String requestorsNote, String requestorsFirstName, String requestorsLastName, String
     * requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState, String requestorsZipCode,
     * String requestorsCountryCode, String requestorsPhone, String requestorsEmail, String requestorsSMS, String requestorType)
     * throws WSException_Exception {
     */
    public String createPreOrderForForm(String title, String author, String edition, String series, String publisher,
                                        String placeOfPublication, String yearOfPublication, String standardNumber, String typeOfStandardNumber,
                                        String requestorsNote, String requestorType, String requestorId) throws WSException_Exception {
        PopulateBibInfoService populateBibInfoService = getPopulateBibInfoService();
        BibInfoBean bibInfoBean = getBibInfoBean();
        StringEscapeUtils stringEscapeUtils = new StringEscapeUtils();
        title = stringEscapeUtils.escapeHtml(title);
        author = stringEscapeUtils.escapeHtml(author);
        edition = stringEscapeUtils.escapeHtml(edition);
        publisher = stringEscapeUtils.escapeHtml(publisher);
        placeOfPublication = stringEscapeUtils.escapeHtml(placeOfPublication);
        requestorId = stringEscapeUtils.escapeHtml(requestorId);
        /*
         * setRequestorDetail(requestorsNote, requestorsFirstName, requestorsLastName, requestorsAddress1, requestorsAddress2,
         * requestorsCity, requestorsState, requestorsZipCode, requestorsCountryCode, requestorsPhone, requestorsEmail,
         * requestorsSMS, requestorType);
         */
        setRequestorDetail(requestorsNote, requestorType, requestorId);
        String docNumber = null;
        try {
            /*
             * docNumber = populateBibInfoService.processBibInfoForForm(bibInfoBean, title, author, edition, series, publisher,
             * placeOfPublication, yearOfPublication, standardNumber, typeOfStandardNumber, routeRequestorReceipt);
             */
            docNumber = populateBibInfoService
                    .processBibInfoForForm(bibInfoBean, title, author, edition, series, publisher, placeOfPublication,
                            yearOfPublication, standardNumber, typeOfStandardNumber, requestorId);

            LOG.info("docNumber in createPreOrderForForm>>>>>>>>>>>>>>" + docNumber);
        } catch (Exception E) {

        }
        return docNumber;
    }

    /*@Override
    public String createPreOrderForForm(String input) throws Exception {
        String[] inputArr = input.split("||");
        for (int i = 0; i < inputArr.length; i++) {

        }

        PopulateBibInfoService populateBibInfoService = getPopulateBibInfoService();
        BibInfoBean bibInfoBean = getBibInfoBean();
        StringEscapeUtils stringEscapeUtils=new StringEscapeUtils();
        String title = stringEscapeUtils.escapeHtml(inputArr[0]);
        String author = stringEscapeUtils.escapeHtml(inputArr[1]);
        String edition = stringEscapeUtils.escapeHtml(inputArr[2]);
        String publisher = stringEscapeUtils.escapeHtml(inputArr[4]);
        String placeOfPublication = stringEscapeUtils.escapeHtml(inputArr[5]);
        setRequestorDetail(inputArr[10], inputArr[11],
                inputArr[12], inputArr[13], inputArr[14], inputArr[15], inputArr[16],
                inputArr[17], inputArr[18], inputArr[19], inputArr[20],
                inputArr[21], inputArr[22]);
        String docNumber = populateBibInfoService.processBibInfoForForm(bibInfoBean,
                title, author, edition, inputArr[3], publisher, placeOfPublication,
                inputArr[6], inputArr[7], inputArr[8],
                inputArr[9]);
        return docNumber;
    }*/

    /*
     * private void setRequestorDetail(String requestorsNote, String requestorsFirstName, String requestorsLastName, String
     * requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState, String requestorsZipCode,
     * String requestorsCountryCode, String requestorsPhone, String requestorsEmail, String requestorsSMS, String requestorType)
     * throws WSException_Exception { bibInfoBean.setRequestersNotes(requestorsNote);
     * bibInfoBean.setRequestorsFirstName(requestorsFirstName); bibInfoBean.setRequestorsLastName(requestorsLastName);
     * bibInfoBean.setRequestorsAddress1(requestorsAddress1); bibInfoBean.setRequestorsAddress2(requestorsAddress2);
     * bibInfoBean.setRequestorsCity(requestorsCity); bibInfoBean.setRequestorsState(requestorsState);
     * bibInfoBean.setRequestorsZipCode(requestorsZipCode); bibInfoBean.setRequestorsCountryCode(requestorsCountryCode);
     * bibInfoBean.setRequestorsPhone(requestorsPhone); bibInfoBean.setRequestorsEmail(requestorsEmail);
     * bibInfoBean.setRequestorsSMS(requestorsSMS); bibInfoBean.setRequestorType(requestorType); }
     */
    private void setRequestorDetail(String requestorsNote, String requestorType, String requestorId)
            throws WSException_Exception {
        bibInfoBean.setRequestersNotes(requestorsNote);
        bibInfoBean.setRequestorType(requestorType);
        bibInfoBean.setRequestorId(requestorId);
    }

}
