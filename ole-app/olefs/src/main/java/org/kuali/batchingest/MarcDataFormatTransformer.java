/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.batchingest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.kuali.ole.select.businessobject.BibInfoBean;
import org.kuali.ole.select.service.impl.OleBatchIngestServiceImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MarcDataFormatTransformer {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MarcDataFormatTransformer.class);
    
    private BibInfoBean bibInfoBean;
    StringBuilder defaultValues = null; 
    String requestorSourceType = "";
  //  Map<String,String> vendorCodeMap = new HashMap<String,String>();
    Map<String,String> deliverToName = new HashMap<String,String>();
    Map<String, Map<String, String>> fundCodesForMarcXMLTags = new HashMap<String, Map<String, String>>();
   
    public MarcDataFormatTransformer() {
        
    //    vendorCodeMap.put("YBP", "12106-0");
        //vendorCodeMap.put("L&H", "12106-0");
        
        deliverToName.put("JLL", "ole-jolevinson");
        deliverToName.put("CM", "ole-mschramm");
        deliverToName.put("AK", "ole-gkrau");
        deliverToName.put("RB", "ole-mbelton");
        
        HashMap<String, String> map1 = new HashMap<String, String>();
        map1.put("2947430", "7112");
        fundCodesForMarcXMLTags.put("43", map1);
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("2947485", "7112");
        fundCodesForMarcXMLTags.put("13", map2);
        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("2947483", "7112");
        fundCodesForMarcXMLTags.put("74", map3);
        HashMap<String, String> map4 = new HashMap<String, String>();
        map4.put("2947499", "7112");
        fundCodesForMarcXMLTags.put("98", map4);
        HashMap<String, String> map5 = new HashMap<String, String>();
        map5.put("2947494", "7112");
        fundCodesForMarcXMLTags.put("65", map5);
        HashMap<String, String> map6 = new HashMap<String, String>();
        map6.put("2947486", "7112");
        fundCodesForMarcXMLTags.put("19", map6);
        HashMap<String, String> map7 = new HashMap<String, String>();
        map7.put("2947493", "7112");
        fundCodesForMarcXMLTags.put("64", map7);
        HashMap<String, String> map8 = new HashMap<String, String>();
        map8.put("2947496", "7112");
        fundCodesForMarcXMLTags.put("93", map8);
        HashMap<String, String> map9 = new HashMap<String, String>();
        map9.put("2947497", "7112");
        fundCodesForMarcXMLTags.put("94", map9);
        HashMap<String, String> map10 = new HashMap<String, String>();
        map10.put("2947488", "7112");
        fundCodesForMarcXMLTags.put("35", map10);
        HashMap<String, String> map11 = new HashMap<String, String>();
        map11.put("2947498", "7112");
        fundCodesForMarcXMLTags.put("95", map11);
        HashMap<String, String> map12 = new HashMap<String, String>();
        map12.put("2947487", "7112");
        fundCodesForMarcXMLTags.put("21", map12);
        HashMap<String, String> map13 = new HashMap<String, String>();
        map13.put("2947489", "7112");
        fundCodesForMarcXMLTags.put("37", map13);
        HashMap<String, String> map14 = new HashMap<String, String>();
        map14.put("2947482", "7112");
        fundCodesForMarcXMLTags.put("27", map14);
        HashMap<String, String> map15 = new HashMap<String, String>();
        map15.put("2947490", "7112");
        fundCodesForMarcXMLTags.put("50", map15);
        HashMap<String, String> map16 = new HashMap<String, String>();
        map16.put("2947492", "7112");
        fundCodesForMarcXMLTags.put("53", map16);
        HashMap<String, String> map17 = new HashMap<String, String>();
        map17.put("2947491", "7112");
        fundCodesForMarcXMLTags.put("51", map17);
        HashMap<String, String> map18 = new HashMap<String, String>();
        map18.put("2947495", "7112");
        fundCodesForMarcXMLTags.put("86", map18);
    }
    
    
    public void parseDefaultValues() throws Exception{
        
      
        
        String path = getClass().getClassLoader().getResource("KualiETLConfig.xml").toString();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        DefaultHandler handler = new DefaultHandler() { 
            
        boolean deliveryCampusCode = false;
       // boolean deliverytoName = false;
        boolean deliveryBuildingCode = false;
        boolean deliveryBuildingLine1Address = false;
        boolean deliveryBuildingRoomNumber = false;
        boolean deliveryCityName = false;
        boolean deliveryStateCode = false;
        boolean deliveryPostalCode = false;
        boolean deliveryCountryCode = false;    
        boolean purchaseOrderTransmissionMethodCode = false;
        boolean purchaseOrderCostSourceCode = false;
        boolean requestorPersonName = false;
        boolean requestorPersonEmailAddress = false;
        boolean requestorPersonPhoneNumber = false;
        boolean requisitionDescription = false;
        boolean financialYear = false;
        boolean fundingSource = false;
        boolean uom = false;
        boolean chart = false;
      //  boolean objectCode = false;
        boolean percent = false;
        boolean chartOfAccountsCode = false;
        boolean organizationCode = false;
        boolean documentFundingSourceCode = false; 
        boolean useTaxIndicator = false;
        boolean deliveryBuildingOtherIndicator = false;
        boolean organizationAutomaticPurchaseOrderLimit = false;
        boolean purchaseOrderAutomaticIndicator = false;
        boolean receivingDocumentRequiredIndicator = false;
        boolean paymentRequestPositiveApprovalIndicator = false;
        boolean itemTypeCode = false;
        boolean billingName = false;
        boolean billingLine1Address = false;
        boolean billingCityName = false;
        boolean billingStateCode = false;
        boolean billingPostalCode = false;
        boolean billingCountryCode = false;
        boolean billingPhoneNumber = false;
        boolean vendorcustomernumber = false;
        boolean requestorsource = false;
       // boolean ybp = false;

        //Event Handlers
        public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
            //reset
                
            if (qName.equalsIgnoreCase("CONFIG")) {
                bibInfoBean = new BibInfoBean();
               defaultValues = new StringBuilder();
           }
            if (qName.equalsIgnoreCase("DELIVERYCAMPUSCODE")) {
                    bibInfoBean = new BibInfoBean();
                   defaultValues = new StringBuilder();
                   deliveryCampusCode = true;
               }/*else if (qName.equalsIgnoreCase("DELIVERYTONAME")) {
                   defaultValues = new StringBuilder();
                   deliverytoName = true;
               }*/else if (qName.equalsIgnoreCase("DELIVERYBUILDINGCODE")) {
                   defaultValues = new StringBuilder();
                   deliveryBuildingCode = true;
               }else if (qName.equalsIgnoreCase("DELIVERYBUILDINGLINE1ADDRESS")) {
                   defaultValues = new StringBuilder();
                   deliveryBuildingLine1Address = true;
               }else if (qName.equalsIgnoreCase("DELIVERYBUILDINGROOMNUMBER")) {
                   defaultValues = new StringBuilder();
                   deliveryBuildingRoomNumber = true;
               }else if (qName.equalsIgnoreCase("DELIVERYCITYNAME")) {
                   defaultValues = new StringBuilder();
                   deliveryCityName = true;
               }else if (qName.equalsIgnoreCase("DELIVERYSTATECODE")) {
                   defaultValues = new StringBuilder();
                   deliveryStateCode = true;
               }else if (qName.equalsIgnoreCase("DELIVERYPOSTALCODE")) {
                   defaultValues = new StringBuilder();
                   deliveryPostalCode = true;
               }else if (qName.equalsIgnoreCase("DELIVERYCOUNTRYCODE")) {
                   defaultValues = new StringBuilder();
                   deliveryCountryCode = true;
               }else if (qName.equalsIgnoreCase("REQUISITIONDESCRIPTION")) {
                   defaultValues = new StringBuilder();
                   requisitionDescription = true;
               }else if (qName.equalsIgnoreCase("FINANCIALYEAR")) {
                   defaultValues = new StringBuilder();
                   financialYear = true;
               }else if (qName.equalsIgnoreCase("FUNDINGSOURCE")) {
                   defaultValues = new StringBuilder();
                   fundingSource = true;
               }else if (qName.equalsIgnoreCase("PURCHASEORDERTRANSMISSIONMETHODCODE")) {
                   defaultValues = new StringBuilder();
                   purchaseOrderTransmissionMethodCode = true;
               }else if (qName.equalsIgnoreCase("PURCHASEORDERCOSTSOURCECODE")) {
                   defaultValues = new StringBuilder();
                   purchaseOrderCostSourceCode = true;
               }else if (qName.equalsIgnoreCase("REQUESTORPERSONNAME")) {
                   defaultValues = new StringBuilder();
                   requestorPersonName = true;
               }else if (qName.equalsIgnoreCase("REQUESTORPERSONEMAILADDRESS")) {
                   defaultValues = new StringBuilder();
                   requestorPersonEmailAddress = true;
               }else if (qName.equalsIgnoreCase("REQUESTORPERSONPHONENUMBER")) {
                   defaultValues = new StringBuilder();
                   requestorPersonPhoneNumber = true;
               }else if (qName.equalsIgnoreCase("UOM")) {
                   defaultValues = new StringBuilder();
                   uom = true;
               }else if (qName.equalsIgnoreCase("CHART")) {
                   defaultValues = new StringBuilder();
                   chart = true;
               }/*else if (qName.equalsIgnoreCase("OBJECTCODE")) {
                   defaultValues = new StringBuilder();
                   objectCode = true;
               }*/else if (qName.equalsIgnoreCase("PERCENT")) {
                   defaultValues = new StringBuilder();
                   percent = true;
               }else if (qName.equalsIgnoreCase("CHARTOFACCOUNTSCODE")) {
                   defaultValues = new StringBuilder();
                   chartOfAccountsCode = true;
               }else if (qName.equalsIgnoreCase("ORGANIZATIONCODE")) {
                   defaultValues = new StringBuilder();
                   organizationCode = true;
               }else if (qName.equalsIgnoreCase("DOCUMENTFUNDINGSOURCECODE")) {
                   defaultValues = new StringBuilder();
                   documentFundingSourceCode = true;
               }else if (qName.equalsIgnoreCase("USETAXINDICATOR")) {
                   defaultValues = new StringBuilder();
                   useTaxIndicator = true;
               }else if (qName.equalsIgnoreCase("DELIVERYBUILDINGOTHERINDICATOR")) {
                   defaultValues = new StringBuilder();
                   deliveryBuildingOtherIndicator = true;
               }else if (qName.equalsIgnoreCase("ORGANIZATIONAUTOMATICPURCHASEORDERLIMIT")) {
                   defaultValues = new StringBuilder();
                   organizationAutomaticPurchaseOrderLimit = true;
               }else if (qName.equalsIgnoreCase("PURCHASEORDERAUTOMATICINDICATOR")) {
                   defaultValues = new StringBuilder();
                   purchaseOrderAutomaticIndicator = true;
               }else if (qName.equalsIgnoreCase("RECEIVINGDOCUMENTREQUIREDINDICATOR")) {
                   defaultValues = new StringBuilder();
                   receivingDocumentRequiredIndicator = true;
               }else if (qName.equalsIgnoreCase("PAYMENTREQUESTPOSITIVEAPPROVALINDICATOR")) {
                   defaultValues = new StringBuilder();
                   paymentRequestPositiveApprovalIndicator = true;
               }else if (qName.equalsIgnoreCase("ITEMTYPECODE")) {
                   defaultValues = new StringBuilder();
                   itemTypeCode = true;
               }else if(qName.equalsIgnoreCase("BILLINGNAME")){ 
                   defaultValues = new StringBuilder();
                   billingName = true;
               }else if(qName.equalsIgnoreCase("BILLINGLINE1ADDRESS")){
                   defaultValues = new StringBuilder();
                   billingLine1Address = true;
               } else if(qName.equalsIgnoreCase("BILLINGCITYNAME")){
                   defaultValues = new StringBuilder();
                   billingCityName = true;
               }else if(qName.equalsIgnoreCase("BILLINGSTATECODE")){
                   defaultValues = new StringBuilder();
                   billingStateCode = true;
               }else if(qName.equalsIgnoreCase("BILLINGPOSTALCODE")){
                   defaultValues = new StringBuilder();
                   billingPostalCode = true;
               }else if(qName.equalsIgnoreCase("BILLINGCOUNTRYCODE")){
                   defaultValues = new StringBuilder();
                   billingCountryCode = true;
               }else if(qName.equalsIgnoreCase("BILLINGPHONENUMBER")){
                   defaultValues = new StringBuilder();
                   billingPhoneNumber = true;
               }else if(qName.equalsIgnoreCase("VENDORCUSTOMERNUMBER")){
                   defaultValues = new StringBuilder();
                   vendorcustomernumber = true;
               }else if(qName.equalsIgnoreCase("REQUESTORSOURCE")){
                   defaultValues = new StringBuilder();
                   requestorsource = true;
               }
             
        }

        
        public void endElement(String uri, String localName,
                String qName) throws SAXException {
           
            
              if (deliveryCampusCode) {
                    bibInfoBean.setDeliveryCampusCode(defaultValues.toString());
                    defaultValues = null;
                    deliveryCampusCode = false;
                }/*else if (deliverytoName) {
                    bibInfoBean.setDeliveryToName(defaultValues.toString());
                    defaultValues = null;
                       deliverytoName = false;
                }*/else if (deliveryBuildingCode) {
                    bibInfoBean.setDeliveryBuildingCode(defaultValues.toString());
                    defaultValues = null;
                     deliveryBuildingCode = false;
                }else if (deliveryBuildingLine1Address) {
                    bibInfoBean.setDeliveryBuildingLine1Address(defaultValues.toString());
                    defaultValues = null;
                     deliveryBuildingLine1Address = false;
                }else if (deliveryBuildingRoomNumber) {
                    bibInfoBean.setDeliveryBuildingRoomNumber(defaultValues.toString());
                    defaultValues = null;
                     deliveryBuildingRoomNumber = false;
                }else if (deliveryCityName) {
                    bibInfoBean.setDeliveryCityName(defaultValues.toString());
                    defaultValues = null;
                     deliveryCityName = false;
                }else if (deliveryStateCode) {
                    bibInfoBean.setDeliveryStateCode(defaultValues.toString());
                    defaultValues = null;
                     deliveryStateCode = false;
                }else if (deliveryPostalCode) {
                    bibInfoBean.setDeliveryPostalCode(defaultValues.toString());
                    defaultValues = null;
                     deliveryPostalCode = false;
                }else if (deliveryCountryCode) {
                    bibInfoBean.setDeliveryCountryCode(defaultValues.toString());
                    defaultValues = null;
                     deliveryCountryCode = false;
                }else if (purchaseOrderTransmissionMethodCode) {
                       bibInfoBean.setPurchaseOrderTransmissionMethodCode(defaultValues.toString());
                       defaultValues = null;
                       purchaseOrderTransmissionMethodCode = false;
                   }else if (purchaseOrderCostSourceCode) {
                       bibInfoBean.setPurchaseOrderCostSourceCode(defaultValues.toString());
                       defaultValues = null;
                       purchaseOrderCostSourceCode = false;
                   }else if (requestorPersonName) {
                       bibInfoBean.setRequestorPersonName(defaultValues.toString());
                       defaultValues = null;
                       requestorPersonName = false;
                   }else if (requestorPersonEmailAddress) {
                       bibInfoBean.setRequestorPersonEmailAddress(defaultValues.toString());
                       defaultValues = null;
                       requestorPersonEmailAddress = false;
                   }else if (requestorPersonPhoneNumber) {
                       bibInfoBean.setRequestorPersonPhoneNumber(defaultValues.toString());
                       defaultValues = null;
                       requestorPersonPhoneNumber = false;
                   }else if (requisitionDescription) {
                       bibInfoBean.setRequisitionDescription(defaultValues.toString());
                       defaultValues = null;
                       requisitionDescription = false;
                   }else if (financialYear) {
                       bibInfoBean.setFinantialYear(defaultValues.toString());
                       defaultValues = null;
                       financialYear = false;
                   }else if (fundingSource) {
                       bibInfoBean.setFundingSource(defaultValues.toString());
                       defaultValues = null;
                       fundingSource = false;
                   } else if (uom) {
                       bibInfoBean.setUom(defaultValues.toString());
                       defaultValues = null;
                       uom = false;
                   }else if (chart) {
                       bibInfoBean.setChart(defaultValues.toString());
                       defaultValues = null;
                       chart = false;
                   }/*else if (objectCode) {
                       bibInfoBean.setObjectCode(defaultValues.toString());
                       defaultValues = null;
                       objectCode = false;
                   }*/else if (percent) {
                       if(defaultValues.toString().length()>0){
                           bibInfoBean.setPercent(new Long(defaultValues.toString()));
                           percent = false;
                       }
                       defaultValues = null;
                   }else if (chartOfAccountsCode) {
                       bibInfoBean.setChartOfAccountsCode(defaultValues.toString());
                       defaultValues = null;
                       chartOfAccountsCode = false;
                   }else if (organizationCode) {
                       bibInfoBean.setOrganizationCode(defaultValues.toString());
                       defaultValues = null;
                       organizationCode = false;
                   }else if (documentFundingSourceCode) {
                       bibInfoBean.setDocumentFundingSourceCode(defaultValues.toString());
                       defaultValues = null;
                       documentFundingSourceCode = false;
                   }else if (useTaxIndicator) {
                       bibInfoBean.setUseTaxIndicator(defaultValues.toString().equalsIgnoreCase("TRUE") ? true : false);
                       defaultValues = null;
                       useTaxIndicator = false;
                   }else if (deliveryBuildingOtherIndicator) {
                       bibInfoBean.setDeliveryBuildingOtherIndicator(defaultValues.toString().equalsIgnoreCase("TRUE") ? true : false);
                       defaultValues = null;
                       deliveryBuildingOtherIndicator = false;
                   }else if (organizationAutomaticPurchaseOrderLimit) {
                       bibInfoBean.setOrganizationAutomaticPurchaseOrderLimit(defaultValues.toString());
                       defaultValues = null;
                       organizationAutomaticPurchaseOrderLimit = false;
                   }else if (purchaseOrderAutomaticIndicator) {
                       bibInfoBean.setPurchaseOrderAutomaticIndicator(defaultValues.toString().equalsIgnoreCase("TRUE") ? true : false);
                       defaultValues = null;
                       purchaseOrderAutomaticIndicator = false;
                   }else if (receivingDocumentRequiredIndicator) {
                       bibInfoBean.setReceivingDocumentRequiredIndicator(defaultValues.toString().equalsIgnoreCase("TRUE") ? true : false);
                       defaultValues = null;
                       receivingDocumentRequiredIndicator = false;
                   }else if (paymentRequestPositiveApprovalIndicator) {
                       bibInfoBean.setPaymentRequestPositiveApprovalIndicator(defaultValues.toString().equalsIgnoreCase("TRUE") ? true : false);
                       defaultValues = null;
                       paymentRequestPositiveApprovalIndicator = false;
                   }else if (itemTypeCode) {
                       bibInfoBean.setItemTypeCode(defaultValues.toString());
                       defaultValues = null;
                       itemTypeCode = false;
                   }else if (billingName) {
                       bibInfoBean.setBillingName(defaultValues.toString());
                       defaultValues = null;
                       billingName = false;
                   }else if (billingLine1Address) {
                       bibInfoBean.setBillingLine1Address(defaultValues.toString());
                       defaultValues = null;
                       billingLine1Address = false;
                   }else if (billingCityName) {
                       bibInfoBean.setBillingCityName(defaultValues.toString());
                       defaultValues = null;
                       billingCityName = false;
                   }else if (billingStateCode) {
                       bibInfoBean.setBillingStateCode(defaultValues.toString());
                       defaultValues = null;
                       billingStateCode = false;
                   }else if (billingPostalCode) {
                       bibInfoBean.setBillingPostalCode(defaultValues.toString());
                       defaultValues = null;
                       billingPostalCode = false;
                   }else if (billingCountryCode) {
                       bibInfoBean.setBillingCountryCode(defaultValues.toString());
                       defaultValues = null;
                       billingCountryCode = false;
                   }else if (billingPhoneNumber) {
                       bibInfoBean.setBillingPhoneNumber(defaultValues.toString());
                       defaultValues = null;
                       billingPhoneNumber = false;
                   }else if(vendorcustomernumber){
                       bibInfoBean.setVendorCustomerNumber(defaultValues.toString());
                       defaultValues = null;
                       vendorcustomernumber = false;
                   }else if(requestorsource){
                       requestorSourceType = defaultValues.toString();
                       defaultValues = null;
                       requestorsource = false;
                   }

            }
        
          public void characters(char ch[], int start, int length)
           throws SAXException {
           if(defaultValues != null)
               defaultValues.append(ch, start, length);
          }
        
        };saxParser.parse(path, handler);
    }
    
    
    
    public String transformRawDataToXml(String rawData) throws Exception{
        StringBuilder xmlString = new StringBuilder();
        parseDefaultValues();
        boolean rootFlag = false;
        String urlxsi = "\"http://www.w3.org/2001/XMLSchema-instance\"";
        String ns = "xsi:noNamespaceSchemaLocation=";
        String orderType = "firm_fixed_ybp"; // Need to take it from file name.
        //String requestorSource = requestorSourceType;
        if ( LOG.isDebugEnabled() ) { 
            LOG.debug("Start Checking the Data");
            LOG.debug(rawData);
        }
        xmlString.append("<requisitions  ordertype='" + orderType + "' requestorsource='"+requestorSourceType+"' xmlns:xsi=" + urlxsi + "  " + ns + "\"requisition.xsd\"" + ">\n");
        boolean emptyFlag = false;
        StringTokenizer field = new StringTokenizer(rawData, "=");
        while (field.hasMoreTokens()) {

            String fieldLine = field.nextToken();
            if((fieldLine.trim().length()>0)&&(fieldLine.trim().length()>=4) ){
            String fieldCode = fieldLine.substring(0, 4);
          
            if (fieldCode.contains("LDR")) {
                if (rootFlag) {
                    try{
                        if(emptyFlag){
                            parseDefaultValues();
                            xmlString = getDefaultValues(xmlString);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if(emptyFlag){
                    xmlString.append("</requisition>\n");
                    emptyFlag = false;
                    }
                }
                xmlString.append("<requisition>\n");
                String leader = fieldLine.substring(fieldCode.length(), fieldLine.length());
                xmlString.append("<leader><![CDATA["+leader.trim()+"]]></leader>\n");
                emptyFlag = true;
                rootFlag = true;
            }else if(fieldCode.contains("008")){
            String controlField = fieldLine.substring(fieldCode.length(), fieldLine.length());
            xmlString.append("<controlfield><![CDATA["+controlField.trim()+"]]></controlfield>\n");
            }else if (fieldCode.contains("020")) {
                String[] subField = fieldLine.split("\\$");
                String isbn = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        isbn = subField[1].substring(1, subField[1].length());
                        xmlString.append("<ISBN><![CDATA["+isbn.trim()+"]]></ISBN>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("100")) {
                String[] subField = fieldLine.split("\\$");
                String author = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        author = subField[1].substring(1, subField[1].length());
                        xmlString.append("<author><![CDATA["+author.trim()+"]]></author>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("245")) {
                String[] subField = fieldLine.split("\\$");
                String title = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        title = subField[1].substring(1, subField[1].length());
                        xmlString.append("<title><![CDATA["+title.trim()+"]]></title>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("250")) {
                String[] subField = fieldLine.split("\\$");
                String edition = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        edition = subField[1].substring(1, subField[1].length());
                        xmlString.append("<edition><![CDATA["+edition.trim()+"]]></edition>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("260")) {
                String[] subField = fieldLine.split("\\$");
                String placeOfPublication = "";
                String publisher = "";
                String yearOfPublication = "";
                xmlString.append("<publicationdetails>\n");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        placeOfPublication = subField[i].substring(1, subField[i].length());
                        xmlString.append("<placeofpublication><![CDATA["+placeOfPublication.trim()+"]]></placeofpublication>\n");
                        emptyFlag = true;
                    }

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("b")) {
                        publisher = subField[i].substring(1, subField[i].length());
                        xmlString.append("<publisher><![CDATA["+publisher.trim()+"]]></publisher>\n");
                        emptyFlag = true;
                    }

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("c")) {
                        yearOfPublication = subField[i].substring(1, subField[i].length());
                        xmlString.append("<yearofpublication><![CDATA["+yearOfPublication.trim()+"]]></yearofpublication>\n");
                        emptyFlag = true;
                    }
                }
                xmlString.append("</publicationdetails>\n");
            } else if (fieldCode.contains("490")) {
                String[] subField = fieldLine.split("\\$");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        String series = subField[1].substring(1, subField[1].length());
                        xmlString.append("<seriesstatement><![CDATA["+series.replaceAll("&", "&amp;").trim()+"]]></seriesstatement>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("960")) {
                String[] subField = fieldLine.split("\\$");
                String accountNumber = "";
                String location = "";
                xmlString.append("<funddetails>\n");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        accountNumber = subField[i].substring(1, subField[i].length());
                        HashMap<String, String> map = fundCodesForMarcXMLTags.get(accountNumber.trim()) != null?(HashMap<String, String>)fundCodesForMarcXMLTags.get(accountNumber.trim()):null;
                        
                        String account="";
                        String fundCode="";
                        if(map != null){
                            for (Iterator key = map.keySet().iterator(); key.hasNext();) {
                                 account = (String)key.next();
                                 fundCode = (String)map.get(account);
                            }
                        }
                      
                        
                        xmlString.append("<accountno>"+account+"</accountno>\n");
                        xmlString.append("<objectcode>"+fundCode+"</objectcode>\n");
                        emptyFlag = true;
                    }

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("h")) {
                        location = subField[i].substring(1, subField[i].length());
                        xmlString.append("<location><![CDATA["+location.trim()+"]]></location>\n");
                        emptyFlag = true;
                    }
                }
                xmlString.append("</funddetails>\n");
            } else if (fieldCode.contains("961")) {
                String[] subField = fieldLine.split("\\$");
                String libraryNote = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("d")) {
                        libraryNote = subField[1].substring(1, subField[1].length());
                        xmlString.append("<librarynote><![CDATA["+libraryNote.trim()+"]]></librarynote>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("980")) {
                String[] subField = fieldLine.split("\\$");
                String listPrice = "";
                String quantity = "";
                xmlString.append("<quantitydetails>\n");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("b")) {
                        listPrice = subField[i].substring(1, subField[i].length());
                        xmlString.append("<listprice><![CDATA["+listPrice.trim()+"]]></listprice>\n");
                        emptyFlag = true;
                    }
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("q")) {
                        quantity = subField[i].substring(1, subField[i].length());
                        xmlString.append("<quantity><![CDATA["+quantity.trim()+"]]></quantity>\n");
                        emptyFlag = true;
                    }
                }
                xmlString.append("</quantitydetails>\n");

            } else if (fieldCode.contains("982")) {
                String[] subField = fieldLine.split("\\$");
                String ybp = "";
                String binding = "";
                String initials = "";
                xmlString.append("<transactiondata1>\n");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        ybp = subField[i].substring(1, subField[i].length());
                        xmlString.append("<YBP><![CDATA["+ybp.trim()+"]]></YBP>\n");
                        emptyFlag = true;
                    }
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("b")) {
                        xmlString.append("<subAccountNumber></subAccountNumber>\n");
                        emptyFlag = true;
                    }
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("d")) {
                        binding = subField[i].substring(1, subField[i].length());
                        xmlString.append("<binding><![CDATA["+binding.trim()+"]]></binding>\n");
                        emptyFlag = true;
                    }
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("f")) {
                        initials = subField[i].substring(1, subField[i].length());
                        xmlString.append("<initials><![CDATA["+initials.trim()+"]]></initials>\n");
                        xmlString.append("<deliverytoname><![CDATA["+deliverToName.get(initials.trim())+"]]></deliverytoname>\n");
                        emptyFlag = true;
                    }
                }
                xmlString.append("</transactiondata1>\n");

            } else if (fieldCode.contains("984")) {

                String[] subField = fieldLine.split("\\$");
                String orderDate = "";
                String vendorCode = "";

                xmlString.append("<vendor>\n");
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        orderDate = subField[i].substring(1, subField[i].length());
                        xmlString.append("<dateOrdered><![CDATA["+orderDate.trim()+"]]></dateOrdered>\n");
                        emptyFlag = true;
                    }

                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("c")) {
                        vendorCode = subField[i].substring(1, subField[i].length());
                        xmlString.append("<vendorcode>12106-0</vendorcode>\n");
                        emptyFlag = true;
                    }
                }
                xmlString.append("</vendor>\n");
            } else if (fieldCode.contains("987")) {
                String[] subField = fieldLine.split("\\$");
                String ybpuid = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        ybpuid = subField[1].substring(1, subField[1].length());
                        xmlString.append("<ybpuid><![CDATA["+ybpuid.trim()+"]]></ybpuid>\n");
                        emptyFlag = true;
                    }
                }
            } else if (fieldCode.contains("993")) {
                String[] subField = fieldLine.split("\\$");
                String volumeNumber = "";
                for (int i = 0; i < subField.length; i++) {
                    String subFieldCode = String.valueOf(subField[i].charAt(0));
                    if (subFieldCode != null && subFieldCode != "" && subFieldCode.equalsIgnoreCase("a")) {
                        volumeNumber = subField[1].substring(1, subField[1].length());
                        xmlString.append("<volumenumber><![CDATA["+volumeNumber.trim()+"]]></volumenumber>\n");
                        emptyFlag = true;
                    }
                }
            }

        }
    }
        try{
        if(emptyFlag){    
            parseDefaultValues();
            xmlString = getDefaultValues(xmlString);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(emptyFlag){
         xmlString.append("</requisition>\n");
         emptyFlag = false;
        }
        xmlString.append("</requisitions>");
        return xmlString.toString();
    }
    
    
    private StringBuilder getDefaultValues(StringBuilder xmlString){
        xmlString.append("<deliverycampuscode>"+bibInfoBean.getDeliveryCampusCode()+"</deliverycampuscode>\n");
   //   xmlString.append("<deliverytoname>"+bibInfoBean.getDeliveryToName()+"</deliverytoname>\n");
        xmlString.append("<deliverybuildingcode>"+bibInfoBean.getDeliveryBuildingCode()+"</deliverybuildingcode>\n");  
        xmlString.append("<deliverybuildingline1address>"+bibInfoBean.getDeliveryBuildingLine1Address()+"</deliverybuildingline1address>\n");
        xmlString.append("<deliverybuildingroomnumber>"+bibInfoBean.getDeliveryBuildingRoomNumber()+"</deliverybuildingroomnumber>\n");
        xmlString.append("<deliverycityname>"+bibInfoBean.getDeliveryCityName()+"</deliverycityname>\n");
        xmlString.append("<deliverystatecode>"+bibInfoBean.getDeliveryStateCode()+"</deliverystatecode>\n");
        xmlString.append("<deliverypostalcode>"+bibInfoBean.getDeliveryPostalCode()+"</deliverypostalcode>\n");
        xmlString.append("<deliverycountrycode>"+bibInfoBean.getDeliveryCountryCode()+"</deliverycountrycode>\n");
        xmlString.append("<purchaseordertransmissionmethodcode>"+bibInfoBean.getPurchaseOrderTransmissionMethodCode()+"</purchaseordertransmissionmethodcode>\n");
        xmlString.append("<purchaseordercostsourcecode>"+bibInfoBean.getPurchaseOrderCostSourceCode()+"</purchaseordercostsourcecode>\n");
        xmlString.append("<requestorpersonname>"+bibInfoBean.getRequestorPersonName()+"</requestorpersonname>\n");
        xmlString.append("<requestorpersonphonenumber>"+bibInfoBean.getRequestorPersonPhoneNumber()+"</requestorpersonphonenumber>\n");
        xmlString.append("<requestorpersonemailaddress>"+bibInfoBean.getRequestorPersonEmailAddress()+"</requestorpersonemailaddress>\n");
        xmlString.append("<requisitiondescription>"+bibInfoBean.getRequisitionDescription()+"</requisitiondescription>\n");
        xmlString.append("<fundingsource>"+bibInfoBean.getFundingSource()+"</fundingsource>\n");
        xmlString.append("<financialyear>"+bibInfoBean.getFinancialYear()+"</financialyear>\n");
        xmlString.append("<uom>"+bibInfoBean.getUom()+"</uom>\n");
        xmlString.append("<chart>"+bibInfoBean.getChart()+"</chart>\n");
   //   xmlString.append("<objectcode>"+bibInfoBean.getObjectCode()+"</objectcode>\n");
        xmlString.append("<percent>"+bibInfoBean.getPercent()+"</percent>\n");
        xmlString.append("<chartofaccountscode>"+bibInfoBean.getChartOfAccountsCode()+"</chartofaccountscode>\n");
        xmlString.append("<organizationcode>"+bibInfoBean.getOrganizationCode()+"</organizationcode>\n");
        xmlString.append("<documentfundingsourcecode>"+bibInfoBean.getDocumentFundingSourceCode()+"</documentfundingsourcecode>\n");
        xmlString.append("<usetaxindicator>"+bibInfoBean.isUseTaxIndicator()+"</usetaxindicator>\n");
        xmlString.append("<deliverybuildingotherindicator>"+bibInfoBean.isDeliveryBuildingOtherIndicator()+"</deliverybuildingotherindicator>\n");
        xmlString.append("<organizationautomaticpurchaseorderlimit>"+bibInfoBean.getOrganizationAutomaticPurchaseOrderLimit()+"</organizationautomaticpurchaseorderlimit>\n");
        xmlString.append("<purchaseorderautomaticindicator>"+bibInfoBean.isPurchaseOrderAutomaticIndicator()+"</purchaseorderautomaticindicator>\n");
        xmlString.append("<receivingdocumentrequiredindicator>"+bibInfoBean.isReceivingDocumentRequiredIndicator()+"</receivingdocumentrequiredindicator>\n");
        xmlString.append("<paymentrequestpositiveapprovalindicator>"+bibInfoBean.isPaymentRequestPositiveApprovalIndicator()+"</paymentrequestpositiveapprovalindicator>\n");
        xmlString.append("<itemtypecode>"+bibInfoBean.getItemTypeCode()+"</itemtypecode>\n");  
        xmlString.append("<billingname>"+bibInfoBean.getBillingName()+"</billingname>\n");  
        xmlString.append("<billingline1address>"+bibInfoBean.getBillingLine1Address()+"</billingline1address>\n");  
        xmlString.append("<billingcityname>"+bibInfoBean.getBillingCityName()+"</billingcityname>\n");  
        xmlString.append("<billingstatecode>"+bibInfoBean.getBillingStateCode()+"</billingstatecode>\n");  
        xmlString.append("<billingpostalcode>"+bibInfoBean.getBillingPostalCode()+"</billingpostalcode>\n");  
        xmlString.append("<billingcountrycode>"+bibInfoBean.getBillingCountryCode()+"</billingcountrycode>\n");  
        xmlString.append("<billingphonenumber>"+bibInfoBean.getBillingPhoneNumber()+"</billingphonenumber>\n");
        xmlString.append("<acquisitionunitvendoraccount>"+bibInfoBean.getVendorCustomerNumber()+"</acquisitionunitvendoraccount>\n");
        return xmlString;
    }

    
    public String getFailureRawData(String rawData,List<List> bibinfoFailure) throws Exception{
        StringBuilder failureBuilder = new StringBuilder();
         for(int failure=0;failure<bibinfoFailure.size();failure++){
            BibInfoBean bibBean = (BibInfoBean)bibinfoFailure.get(failure);
            String leader = bibBean.getLeader();
            String ybpUid=bibBean.getYbpuid();
            StringTokenizer field = new StringTokenizer(rawData, "=");
            int fromIndex = 0;
            int toIndex =0;
            String rawdata = "";
            while (field.hasMoreTokens()) {
            
                String fieldLine = field.nextToken();
                if(!fieldLine.equalsIgnoreCase("\n")){
                String fieldCode = fieldLine.substring(0, 4);
                if (fieldCode.contains("LDR")) {
                    if(fieldLine.contains(leader)){
                      fromIndex = rawData.indexOf(fieldLine);
                    }
                }
                if (fieldCode.contains("987")) {
                    if(fieldLine.contains(ybpUid)){
                    toIndex = rawData.indexOf(fieldLine);
                    rawdata = rawData.substring(fromIndex,toIndex);
                    failureBuilder.append(rawdata+fieldLine+"\nFailure Information :::"+bibBean.getFailure()+"\n\n");
                }
                }
            
           }
            }
        }
        
        return failureBuilder.toString();
    }
    
    
}
