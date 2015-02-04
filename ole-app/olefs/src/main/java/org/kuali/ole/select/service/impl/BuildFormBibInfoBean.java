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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

public class BuildFormBibInfoBean {


    private Logger LOG = org.apache.log4j.Logger.getLogger(BuildFormBibInfoBean.class);
    private DocumentBuilderFactory domFactory;
    private DocumentBuilder builder;
    private Document doc;
    private String responseXmlString;
    private CitationParser ciatationParser;
    //private BibInfoBean bibInfoBean;
    private ArrayList<String> authorList;
    

/*    public  BibInfoBean getBean(String title,String author,String edition,String series,
            String publisher,String placeOfPublication,String yearOfPublication,
            String standardNumber,String typeOfStandardNumber,String routeRequesterReceipt,
            String requestorsContactInfo,String requestorsNote)throws Exception{
*/
/*    public  BibInfoBean getBean(String title,String author,String edition,String series,
            String publisher,String placeOfPublication,String yearOfPublication,
            String standardNumber,String typeOfStandardNumber,
            String routeRequesterReceipt, String requestorsNote,String requestorsFirstName, 
            String requestorsLastName, String requestorsAddress1, String requestorsAddress2, String requestorsCity, String requestorsState, 
            String requestorsZipCode, String requestorsCountryCode, String requestorsPhone, String requestorsEmail,
            String requestorsSMS)throws Exception{
        BibInfoBean bibInfoBean = new BibInfoBean();
        bibInfoBean.setTitle(title);
        bibInfoBean.setAuthor(author);
        bibInfoBean.setEdition(edition);
        bibInfoBean.setSeries(series);
        bibInfoBean.setPublisher(publisher);
        bibInfoBean.setPlaceOfPublication(placeOfPublication);
        bibInfoBean.setYearOfPublication(yearOfPublication);
        bibInfoBean.setStandardNumber(standardNumber);
        bibInfoBean.setTypeOfStandardNumber(typeOfStandardNumber);
        bibInfoBean.setRequestorsFirstName(requestorsFirstName);
        bibInfoBean.setRequestorsLastName(requestorsLastName);
        bibInfoBean.setRequestorsAddress1(requestorsAddress1);
        bibInfoBean.setRequestorsAddress2(requestorsAddress2);
        bibInfoBean.setRequestorsCity(requestorsCity);
        bibInfoBean.setRequestorsState(requestorsState);
        bibInfoBean.setRequestorsZipCode(requestorsZipCode);
        bibInfoBean.setRequestorsCountryCode(requestorsCountryCode);
        bibInfoBean.setRequestorsPhone(requestorsPhone);
        bibInfoBean.setRequestorsEmail(requestorsEmail);
        bibInfoBean.setRequestorsSMS(requestorsSMS);
        return bibInfoBean;
    }*/

    public BibInfoBean getBean(BibInfoBean bibInfoBean, String title, String author, String edition, String series,
                               String publisher, String placeOfPublication, String yearOfPublication,
                               String standardNumber, String typeOfStandardNumber,
                               String routeRequesterReceipt) throws Exception {
        bibInfoBean.setTitle(title);
        bibInfoBean.setAuthor(author);
        bibInfoBean.setEdition(edition);
        bibInfoBean.setSeries(series);
        bibInfoBean.setPublisher(publisher);
        bibInfoBean.setPlaceOfPublication(placeOfPublication);
        bibInfoBean.setYearOfPublication(yearOfPublication);
        bibInfoBean.setStandardNumber(standardNumber);
        bibInfoBean.setTypeOfStandardNumber(typeOfStandardNumber);
        bibInfoBean.setIsbn(standardNumber);
/*        bibInfoBean.setRequestorsFirstName(requestorsFirstName);
        bibInfoBean.setRequestorsLastName(requestorsLastName);
        bibInfoBean.setRequestorsAddress1(requestorsAddress1);
        bibInfoBean.setRequestorsAddress2(requestorsAddress2);
        bibInfoBean.setRequestorsCity(requestorsCity);
        bibInfoBean.setRequestorsState(requestorsState);
        bibInfoBean.setRequestorsZipCode(requestorsZipCode);
        bibInfoBean.setRequestorsCountryCode(requestorsCountryCode);
        bibInfoBean.setRequestorsPhone(requestorsPhone);
        bibInfoBean.setRequestorsEmail(requestorsEmail);
        bibInfoBean.setRequestorsSMS(requestorsSMS);*/
        return bibInfoBean;
    }

}
