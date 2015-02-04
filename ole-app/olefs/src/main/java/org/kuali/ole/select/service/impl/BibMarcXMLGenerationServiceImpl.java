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
import org.kuali.ole.select.service.BibMarcXMLGenerationService;

import java.util.HashMap;

public class BibMarcXMLGenerationServiceImpl implements BibMarcXMLGenerationService {
    private static BibMarcXMLGenerationServiceImpl bibMarcXMLGenerationServiceImpl;
    protected static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibMarcXMLGenerationServiceImpl.class);


    private String buildXmlString(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        //StringBuffer xmlString = new StringBuffer();
        StringBuilder xmlString = new StringBuilder();
        //xmlString.append("<?xml version=\"1.0\"?>");
        xmlString.append("<collection>");
        xmlString.append("<record>");
        xmlString.append("<leader>" + bibInfoBean.getLeader() + "</leader>");
        xmlString.append("<controlfield tag=\"001\"></controlfield>");
        xmlString.append("<controlfield tag=\"008\">" + bibInfoBean.getControlField() + "</controlfield>");
        if (bibInfoBean.getStandardNumber() != null && !bibInfoBean.getStandardNumber().isEmpty()) {
            xmlString.append("<datafield tag=\"020\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getStandardNumber()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getAuthor() != null && !bibInfoBean.getAuthor().isEmpty()) {
            xmlString.append("<datafield tag=\"100\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getAuthor()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getTitle() != null && !bibInfoBean.getTitle().isEmpty()) {
            xmlString.append("<datafield tag=\"245\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getTitle()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getEdition() != null && !bibInfoBean.getEdition().isEmpty()) {
            xmlString.append("<datafield tag=\"250\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getEdition()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if ((bibInfoBean.getPlaceOfPublication() != null && !bibInfoBean.getPlaceOfPublication().isEmpty()) || (bibInfoBean.getPublisher() != null && !bibInfoBean.getPublisher().isEmpty()) || (bibInfoBean.getYearOfPublication() != null && !bibInfoBean.getYearOfPublication().isEmpty())) {
            xmlString.append("<datafield tag=\"260\" >");
            //if (bibInfoBean.getPlaceOfPublication() != null)
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getPlaceOfPublication()).append("</subfield>");
            //if (bibInfoBean.getPublisher() != null)
            xmlString.append("<subfield code=\"b\">").append(bibInfoBean.getPublisher()).append("</subfield>");
            //if (bibInfoBean.getYearOfPublication() != null)
            xmlString.append("<subfield code=\"c\">").append(bibInfoBean.getYearOfPublication()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getSeriesOfStatement() != null && !bibInfoBean.getSeriesOfStatement().isEmpty()) {
            xmlString.append("<datafield tag=\"490\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getSeriesOfStatement()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        /*if ((bibInfoBean.getFundCode() != null && !bibInfoBean.getFundCode().isEmpty()) || ((bibInfoBean.getLocation() != null)&& !bibInfoBean.getLocation().isEmpty())) {
            xmlString.append("<datafield tag=\"960\" >");
            //if (bibInfoBean.getFundCode() != null)
                xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getFundCode()).append("</subfield>");
            //if (bibInfoBean.getLocation() != null)
                xmlString.append("<subfield code=\"h\">").append(bibInfoBean.getLocation()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getRequestersNotes() != null && !bibInfoBean.getRequestersNotes().isEmpty() ) {
            xmlString.append("<datafield tag=\"961\" >");
            xmlString.append("<subfield code=\"d\">").append(bibInfoBean.getRequestersNotes()).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (dataMap.containsKey("unitPrice") || dataMap.containsKey("itemQuantity")) {
            xmlString.append("<datafield tag=\"980\" >");
            //if (dataMap.containsKey("unitPrice"))
                xmlString.append("<subfield code=\"b\">").append(dataMap.get("unitPrice")).append("</subfield>");
            //if (dataMap.containsKey("itemQuantity"))
                xmlString.append("<subfield code=\"q\">").append(dataMap.get("itemQuantity")).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (dataMap.containsKey("ybpOrderKey") || dataMap.containsKey("subAccountNo") || dataMap.containsKey("binding") || dataMap.containsKey("initials")) {
            xmlString.append("<datafield tag=\"982\" >");
            //if (dataMap.containsKey("ybpOrderKey"))
                xmlString.append("<subfield code=\"a\">").append(dataMap.get("ybpOrderKey")).append("</subfield>");
            //if (dataMap.containsKey("subAccountNo"))
                xmlString.append("<subfield code=\"b\">").append(dataMap.get("subAccountNo")).append("</subfield>");
            //if (dataMap.containsKey("binding"))
                xmlString.append("<subfield code=\"d\">").append(dataMap.get("binding")).append("</subfield>");
            //if (dataMap.containsKey("initials"))
                xmlString.append("<subfield code=\"f\">").append(dataMap.get("initials")).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (dataMap.containsKey("dateOrdered") || dataMap.containsKey("vendorCode")) {
            xmlString.append("<datafield tag=\"984\" >");
            //if (dataMap.containsKey("dateOrdered"))
            if ((bibInfoBean.getFundCode() != null && !bibInfoBean.getFundCode().isEmpty()) || ((bibInfoBean.getLocation() != null)&& !bibInfoBean.getLocation().isEmpty())) {
                xmlString.append("<datafield tag=\"960\" >");
                //if (bibInfoBean.getFundCode() != null)
                    xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getFundCode()).append("</subfield>");
                //if (bibInfoBean.getLocation() != null)
                    xmlString.append("<subfield code=\"h\">").append(bibInfoBean.getLocation()).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (bibInfoBean.getRequestersNotes() != null && !bibInfoBean.getRequestersNotes().isEmpty() ) {
                xmlString.append("<datafield tag=\"961\" >");
                xmlString.append("<subfield code=\"d\">").append(bibInfoBean.getRequestersNotes()).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (dataMap.containsKey("unitPrice") || dataMap.containsKey("itemQuantity")) {
                xmlString.append("<datafield tag=\"980\" >");
                //if (dataMap.containsKey("unitPrice"))
                    xmlString.append("<subfield code=\"b\">").append(dataMap.get("unitPrice")).append("</subfield>");
                //if (dataMap.containsKey("itemQuantity"))
                    xmlString.append("<subfield code=\"q\">").append(dataMap.get("itemQuantity")).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (dataMap.containsKey("ybpOrderKey") || dataMap.containsKey("subAccountNo") || dataMap.containsKey("binding") || dataMap.containsKey("initials")) {
                xmlString.append("<datafield tag=\"982\" >");
                //if (dataMap.containsKey("ybpOrderKey"))
                    xmlString.append("<subfield code=\"a\">").append(dataMap.get("ybpOrderKey")).append("</subfield>");
                //if (dataMap.containsKey("subAccountNo"))
                    xmlString.append("<subfield code=\"b\">").append(dataMap.get("subAccountNo")).append("</subfield>");
                //if (dataMap.containsKey("binding"))
                    xmlString.append("<subfield code=\"d\">").append(dataMap.get("binding")).append("</subfield>");
                //if (dataMap.containsKey("initials"))
                    xmlString.append("<subfield code=\"f\">").append(dataMap.get("initials")).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (dataMap.containsKey("dateOrdered") || dataMap.containsKey("vendorCode")) {
                xmlString.append("<datafield tag=\"984\" >");
                //if (dataMap.containsKey("dateOrdered"))
                    xmlString.append("<subfield code=\"a\">").append(dataMap.get("dateOrdered")).append("</subfield>");
                //if (dataMap.containsKey("vendorCode"))
                    xmlString.append("<subfield code=\"c\">").append(dataMap.get("vendorCode")).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (dataMap.containsKey("ybpUID")) {
                xmlString.append("<datafield tag=\"987\" >");
                xmlString.append("<subfield code=\"a\">").append(dataMap.get("ybpUID")).append("</subfield>");
                xmlString.append("</datafield>");
            }
            if (bibInfoBean.getSeries() != null && !bibInfoBean.getSeries().isEmpty()) {
                xmlString.append("<datafield tag=\"993\" >");
                xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getSeries()).append("</subfield>");
                xmlString.append("</datafield>");
            }                xmlString.append("<subfield code=\"a\">").append(dataMap.get("dateOrdered")).append("</subfield>");
            //if (dataMap.containsKey("vendorCode"))
                xmlString.append("<subfield code=\"c\">").append(dataMap.get("vendorCode")).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (dataMap.containsKey("ybpUID")) {
            xmlString.append("<datafield tag=\"987\" >");
            xmlString.append("<subfield code=\"a\">").append(dataMap.get("ybpUID")).append("</subfield>");
            xmlString.append("</datafield>");
        }
        if (bibInfoBean.getSeries() != null && !bibInfoBean.getSeries().isEmpty()) {
            xmlString.append("<datafield tag=\"993\" >");
            xmlString.append("<subfield code=\"a\">").append(bibInfoBean.getSeries()).append("</subfield>");
            xmlString.append("</datafield>");
        }*/
        xmlString.append("</record>");
        xmlString.append("</collection>");
        if (LOG.isDebugEnabled())
            LOG.debug("xmlString----------->" + xmlString.toString());
        return xmlString.toString();
    }

    public String getMarcXML(BibInfoBean bibInfoBean, HashMap dataMap) throws Exception {
        return buildXmlString(bibInfoBean, dataMap);
    }
    
/*    public static void main(String args[]){
        try{
            writeToXMLFile();
        }catch(Exception e){
            e.printStackTrace();
        }
    }*/
}
