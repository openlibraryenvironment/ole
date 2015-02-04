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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BuildVendorBibInfoBean {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BuildVendorBibInfoBean.class);

    private BibInfoBean bibInfoBean;
    StringBuilder stringBuilder = null;
    String purchaseOrderType;
    String requestorSourceType;
    private List<BibInfoBean> bibInfoBeanList = null;

    public List<BibInfoBean> getBibInfoList(String xmlString) {
        bibInfoBeanList = new ArrayList<BibInfoBean>();
        try {
            initiateDocument(xmlString);
        } catch (Exception ex) {
            LOG.error("parsing error in BuildVendorBibInfoBean " + ex);
            throw new RuntimeException(ex);
        }

        return bibInfoBeanList;
    }

    private void initiateDocument(String xmlString) throws Exception {
        
      /*  if (path == null || path.length() == 0){
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream("org/kuali/ole/select/service/impl/bibinfo.properties"));
                path = properties.getProperty("filename");
            } catch (IOException e) {
            }
        }*/

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {

            boolean leader = false;
            boolean controlField = false;

            boolean isbn = false;
            boolean author = false;
            boolean title = false;
            boolean placeOfPublication = false;
            boolean publisher = false;
            boolean yearOfPublication = false;
            boolean accountNumber = false;
            boolean location = false;
            boolean libraryNote = false;
            boolean listPrice = false;
            boolean quantity = false;
            boolean ybp = false;
            boolean subAccountNumber = false;
            boolean binding = false;
            boolean initials = false;
            boolean dateOrdered = false;
            boolean vendorCode = false;
            boolean ybpuid = false;
            boolean requisitionDescription = false;
            boolean financialYear = false;
            boolean fundingSource = false;
            boolean deliveryCampusCode = false;
            boolean deliverytoName = false;
            boolean deliveryBuildingCode = false;
            boolean deliveryBuildingLine1Address = false;
            boolean deliveryBuildingRoomNumber = false;
            boolean deliveryCityName = false;
            boolean deliveryStateCode = false;
            boolean deliveryPostalCode = false;
            boolean deliveryCountryCode = false;
            boolean vendorCustomerNumber = false;
            //boolean items = false;
            boolean purchaseOrderTransmissionMethodCode = false;
            boolean purchaseOrderCostSourceCode = false;
            boolean requestorPersonName = false;
            boolean requestorPersonEmailAddress = false;
            boolean requestorPersonPhoneNumber = false;
            boolean volumeNumber = false;

            boolean uom = false;
            boolean chart = false;
            boolean objectCode = false;
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
            boolean orderType = false;

            boolean billingName = false;
            boolean billingLine1Address = false;
            boolean billingCityName = false;
            boolean billingStateCode = false;
            boolean billingPostalCode = false;
            boolean billingCountryCode = false;
            boolean billingPhoneNumber = false;
            boolean acquisitionunitvendoraccount = false;
            // boolean requestorsource = false;

            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes) throws SAXException {
                if (LOG.isDebugEnabled())
                    LOG.debug("<-------------------qName----------------------->" + qName);

                if (qName.equalsIgnoreCase("REQUISITIONS")) {
                    purchaseOrderType = attributes.getValue("ordertype");
                    orderType = true;
                    requestorSourceType = attributes.getValue("requestorsource");
                }
                if (qName.equalsIgnoreCase("REQUISITION")) {
                    bibInfoBean = new BibInfoBean();
                    orderType = true;
                } else if (qName.equalsIgnoreCase("LEADER")) {
                    stringBuilder = new StringBuilder();
                    leader = true;
                } else if (qName.equalsIgnoreCase("CONTROLFIELD")) {
                    stringBuilder = new StringBuilder();
                    controlField = true;
                } else if (qName.equalsIgnoreCase("ISBN")) {
                    stringBuilder = new StringBuilder();
                    isbn = true;
                } else if (qName.equalsIgnoreCase("AUTHOR")) {
                    stringBuilder = new StringBuilder();
                    author = true;
                } else if (qName.equalsIgnoreCase("TITLE")) {
                    stringBuilder = new StringBuilder();
                    title = true;
                } else if (qName.equalsIgnoreCase("PLACEOFPUBLICATION")) {
                    stringBuilder = new StringBuilder();
                    placeOfPublication = true;
                } else if (qName.equalsIgnoreCase("PUBLISHER")) {
                    stringBuilder = new StringBuilder();
                    publisher = true;
                } else if (qName.equalsIgnoreCase("YEAROFPUBLICATION")) {
                    stringBuilder = new StringBuilder();
                    yearOfPublication = true;
                } else if (qName.equalsIgnoreCase("ACCOUNTNO")) {
                    stringBuilder = new StringBuilder();
                    accountNumber = true;
                } else if (qName.equalsIgnoreCase("LOCATION")) {
                    stringBuilder = new StringBuilder();
                    location = true;
                } else if (qName.equalsIgnoreCase("LIBRARYNOTE")) {
                    stringBuilder = new StringBuilder();
                    libraryNote = true;
                } else if (qName.equalsIgnoreCase("LISTPRICE")) {
                    stringBuilder = new StringBuilder();
                    listPrice = true;
                } else if (qName.equalsIgnoreCase("QUANTITY")) {
                    stringBuilder = new StringBuilder();
                    quantity = true;
                } else if (qName.equalsIgnoreCase("YBP")) {
                    stringBuilder = new StringBuilder();
                    ybp = true;
                } else if (qName.equalsIgnoreCase("SUBACCOUNTNUMBER")) {
                    stringBuilder = new StringBuilder();
                    subAccountNumber = true;
                } else if (qName.equalsIgnoreCase("BINDING")) {
                    stringBuilder = new StringBuilder();
                    binding = true;
                } else if (qName.equalsIgnoreCase("INITIALS")) {
                    stringBuilder = new StringBuilder();
                    initials = true;
                } else if (qName.equalsIgnoreCase("DATEORDERED")) {
                    stringBuilder = new StringBuilder();
                    dateOrdered = true;
                } else if (qName.equalsIgnoreCase("VENDORCODE")) {
                    stringBuilder = new StringBuilder();
                    vendorCode = true;
                } else if (qName.equalsIgnoreCase("YBPUID")) {
                    stringBuilder = new StringBuilder();
                    ybpuid = true;
                } else if (qName.equalsIgnoreCase("REQUISITIONDESCRIPTION")) {
                    stringBuilder = new StringBuilder();
                    requisitionDescription = true;
                } else if (qName.equalsIgnoreCase("FINANCIALYEAR")) {
                    stringBuilder = new StringBuilder();
                    financialYear = true;
                } else if (qName.equalsIgnoreCase("FUNDINGSOURCE")) {
                    stringBuilder = new StringBuilder();
                    fundingSource = true;
                } else if (qName.equalsIgnoreCase("DELIVERYCAMPUSCODE")) {
                    stringBuilder = new StringBuilder();
                    deliveryCampusCode = true;
                } else if (qName.equalsIgnoreCase("DELIVERYTONAME")) {
                    stringBuilder = new StringBuilder();
                    deliverytoName = true;
                } else if (qName.equalsIgnoreCase("DELIVERYBUILDINGCODE")) {
                    stringBuilder = new StringBuilder();
                    deliveryBuildingCode = true;
                } else if (qName.equalsIgnoreCase("DELIVERYBUILDINGLINE1ADDRESS")) {
                    stringBuilder = new StringBuilder();
                    deliveryBuildingLine1Address = true;
                } else if (qName.equalsIgnoreCase("DELIVERYBUILDINGROOMNUMBER")) {
                    stringBuilder = new StringBuilder();
                    deliveryBuildingRoomNumber = true;
                } else if (qName.equalsIgnoreCase("DELIVERYCITYNAME")) {
                    stringBuilder = new StringBuilder();
                    deliveryCityName = true;
                } else if (qName.equalsIgnoreCase("DELIVERYSTATECODE")) {
                    stringBuilder = new StringBuilder();
                    deliveryStateCode = true;
                } else if (qName.equalsIgnoreCase("DELIVERYPOSTALCODE")) {
                    stringBuilder = new StringBuilder();
                    deliveryPostalCode = true;
                } else if (qName.equalsIgnoreCase("DELIVERYCOUNTRYCODE")) {
                    stringBuilder = new StringBuilder();
                    deliveryCountryCode = true;
                } else if (qName.equalsIgnoreCase("VENDORCUSTOMERNUMBER")) {
                    stringBuilder = new StringBuilder();
                    vendorCustomerNumber = true;
                } else if (qName.equalsIgnoreCase("PURCHASEORDERTRANSMISSIONMETHODCODE")) {
                    stringBuilder = new StringBuilder();
                    purchaseOrderTransmissionMethodCode = true;
                } else if (qName.equalsIgnoreCase("PURCHASEORDERCOSTSOURCECODE")) {
                    stringBuilder = new StringBuilder();
                    purchaseOrderCostSourceCode = true;
                } else if (qName.equalsIgnoreCase("REQUESTORPERSONNAME")) {
                    stringBuilder = new StringBuilder();
                    requestorPersonName = true;
                } else if (qName.equalsIgnoreCase("REQUESTORPERSONEMAILADDRESS")) {
                    stringBuilder = new StringBuilder();
                    requestorPersonEmailAddress = true;
                } else if (qName.equalsIgnoreCase("REQUESTORPERSONPHONENUMBER")) {
                    stringBuilder = new StringBuilder();
                    requestorPersonPhoneNumber = true;
                } else if (qName.equalsIgnoreCase("VOLUMENUMBER")) {
                    stringBuilder = new StringBuilder();
                    volumeNumber = true;
                } else if (qName.equalsIgnoreCase("UOM")) {
                    stringBuilder = new StringBuilder();
                    uom = true;
                } else if (qName.equalsIgnoreCase("CHART")) {
                    stringBuilder = new StringBuilder();
                    chart = true;
                } else if (qName.equalsIgnoreCase("OBJECTCODE")) {
                    stringBuilder = new StringBuilder();
                    objectCode = true;
                } else if (qName.equalsIgnoreCase("PERCENT")) {
                    stringBuilder = new StringBuilder();
                    percent = true;
                } else if (qName.equalsIgnoreCase("CHARTOFACCOUNTSCODE")) {
                    stringBuilder = new StringBuilder();
                    chartOfAccountsCode = true;
                } else if (qName.equalsIgnoreCase("ORGANIZATIONCODE")) {
                    stringBuilder = new StringBuilder();
                    organizationCode = true;
                } else if (qName.equalsIgnoreCase("DOCUMENTFUNDINGSOURCECODE")) {
                    stringBuilder = new StringBuilder();
                    documentFundingSourceCode = true;
                } else if (qName.equalsIgnoreCase("USETAXINDICATOR")) {
                    stringBuilder = new StringBuilder();
                    useTaxIndicator = true;
                } else if (qName.equalsIgnoreCase("DELIVERYBUILDINGOTHERINDICATOR")) {
                    stringBuilder = new StringBuilder();
                    deliveryBuildingOtherIndicator = true;
                } else if (qName.equalsIgnoreCase("ORGANIZATIONAUTOMATICPURCHASEORDERLIMIT")) {
                    stringBuilder = new StringBuilder();
                    organizationAutomaticPurchaseOrderLimit = true;
                } else if (qName.equalsIgnoreCase("PURCHASEORDERAUTOMATICINDICATOR")) {
                    stringBuilder = new StringBuilder();
                    purchaseOrderAutomaticIndicator = true;
                } else if (qName.equalsIgnoreCase("RECEIVINGDOCUMENTREQUIREDINDICATOR")) {
                    stringBuilder = new StringBuilder();
                    receivingDocumentRequiredIndicator = true;
                } else if (qName.equalsIgnoreCase("PAYMENTREQUESTPOSITIVEAPPROVALINDICATOR")) {
                    stringBuilder = new StringBuilder();
                    paymentRequestPositiveApprovalIndicator = true;
                } else if (qName.equalsIgnoreCase("ITEMTYPECODE")) {
                    stringBuilder = new StringBuilder();
                    itemTypeCode = true;
                } else if (qName.equalsIgnoreCase("BILLINGNAME")) {
                    stringBuilder = new StringBuilder();
                    LOG.debug("----------Bill----Name----->true");
                    billingName = true;
                } else if (qName.equalsIgnoreCase("BILLINGLINE1ADDRESS")) {
                    stringBuilder = new StringBuilder();
                    billingLine1Address = true;
                } else if (qName.equalsIgnoreCase("BILLINGCITYNAME")) {
                    stringBuilder = new StringBuilder();
                    billingCityName = true;
                } else if (qName.equalsIgnoreCase("BILLINGSTATECODE")) {
                    stringBuilder = new StringBuilder();
                    billingStateCode = true;
                } else if (qName.equalsIgnoreCase("BILLINGPOSTALCODE")) {
                    stringBuilder = new StringBuilder();
                    billingPostalCode = true;
                } else if (qName.equalsIgnoreCase("BILLINGCOUNTRYCODE")) {
                    stringBuilder = new StringBuilder();
                    billingCountryCode = true;
                } else if (qName.equalsIgnoreCase("BILLINGPHONENUMBER")) {
                    stringBuilder = new StringBuilder();
                    billingPhoneNumber = true;
                } else if (qName.equalsIgnoreCase("ACQUISITIONUNITVENDORACCOUNT")) {
                    stringBuilder = new StringBuilder();
                    acquisitionunitvendoraccount = true;
                }


            }

            public void endElement(String uri, String localName,
                                   String qName) throws SAXException {

                if (leader) {
                    bibInfoBean.setLeader(stringBuilder.toString());
                    stringBuilder = null;
                    leader = false;
                } else if (controlField) {
                    bibInfoBean.setControlField(stringBuilder.toString());
                    stringBuilder = null;
                    controlField = false;
                } else if (isbn) {
                    bibInfoBean.setIsbn(stringBuilder.toString());
                    bibInfoBean.setStandardNumber(stringBuilder.toString());
                    stringBuilder = null;
                    isbn = false;
                } else if (author) {
                    bibInfoBean.setAuthor(stringBuilder.toString());
                    stringBuilder = null;
                    author = false;
                } else if (title) {
                    bibInfoBean.setTitle(stringBuilder.toString());
                    stringBuilder = null;
                    title = false;
                } else if (placeOfPublication) {
                    bibInfoBean.setPlaceOfPublication(stringBuilder.toString());
                    stringBuilder = null;
                    placeOfPublication = false;
                } else if (publisher) {
                    bibInfoBean.setPublisher(stringBuilder.toString());
                    stringBuilder = null;
                    publisher = false;
                } else if (yearOfPublication) {
                    bibInfoBean.setYearOfPublication(stringBuilder.toString());
                    stringBuilder = null;
                    yearOfPublication = false;
                } else if (accountNumber) {
                    bibInfoBean.setAccountNumber(stringBuilder.toString());
                    if (LOG.isDebugEnabled())
                        LOG.debug("Account No==>" + stringBuilder.toString());
                    stringBuilder = null;
                    accountNumber = false;
                } else if (location) {
                    bibInfoBean.setLocation(stringBuilder.toString());
                    stringBuilder = null;
                    location = false;
                } else if (libraryNote) {
                    bibInfoBean.setLibrarynote(stringBuilder.toString());
                    stringBuilder = null;
                    libraryNote = false;
                } else if (listPrice) {
                    if (stringBuilder.toString().length() > 0) {
                        bibInfoBean.setListprice(new Double(stringBuilder.toString()));
                        listPrice = false;
                    }
                    stringBuilder = null;
                } else if (quantity) {
                    if (stringBuilder.toString().length() > 0) {
                        bibInfoBean.setQuantity(new Long(stringBuilder.toString()));
                        quantity = false;
                    }
                    stringBuilder = null;
                } else if (ybp) {
                    bibInfoBean.setYbp(stringBuilder.toString());
                    stringBuilder = null;
                    ybp = false;
                } else if (subAccountNumber) {
                    bibInfoBean.setSubAccountNumber(stringBuilder.toString());
                    stringBuilder = null;
                    subAccountNumber = false;
                } else if (binding) {
                    bibInfoBean.setBinding(stringBuilder.toString());
                    stringBuilder = null;
                    binding = false;
                } else if (initials) {
                    bibInfoBean.setInitials(stringBuilder.toString());
                    stringBuilder = null;
                    initials = false;
                } else if (dateOrdered) {
                    bibInfoBean.setDateOrdered(stringBuilder.toString());
                    stringBuilder = null;
                    dateOrdered = false;
                } else if (vendorCode) {
                    bibInfoBean.setVendorCode(stringBuilder.toString());
                    stringBuilder = null;
                    vendorCode = false;
                } else if (ybpuid) {
                    bibInfoBean.setYbpuid(stringBuilder.toString());
                    stringBuilder = null;
                    ybpuid = false;
                } else if (requisitionDescription) {
                    bibInfoBean.setRequisitionDescription(stringBuilder.toString());
                    stringBuilder = null;
                    requisitionDescription = false;
                } else if (financialYear) {
                    bibInfoBean.setFinantialYear(stringBuilder.toString());
                    stringBuilder = null;
                    financialYear = false;
                } else if (fundingSource) {
                    bibInfoBean.setFundingSource(stringBuilder.toString());
                    stringBuilder = null;
                    fundingSource = false;
                } else if (deliveryCampusCode) {
                    bibInfoBean.setDeliveryCampusCode(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryCampusCode = false;
                } else if (deliverytoName) {
                    bibInfoBean.setDeliveryToName(stringBuilder.toString());
                    stringBuilder = null;
                    deliverytoName = false;
                } else if (deliveryBuildingCode) {
                    bibInfoBean.setDeliveryBuildingCode(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryBuildingCode = false;
                } else if (deliveryBuildingLine1Address) {
                    bibInfoBean.setDeliveryBuildingLine1Address(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryBuildingLine1Address = false;
                } else if (deliveryBuildingRoomNumber) {
                    bibInfoBean.setDeliveryBuildingRoomNumber(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryBuildingRoomNumber = false;
                } else if (deliveryCityName) {
                    bibInfoBean.setDeliveryCityName(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryCityName = false;
                } else if (deliveryStateCode) {
                    bibInfoBean.setDeliveryStateCode(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryStateCode = false;
                } else if (deliveryPostalCode) {
                    bibInfoBean.setDeliveryPostalCode(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryPostalCode = false;
                } else if (deliveryCountryCode) {
                    bibInfoBean.setDeliveryCountryCode(stringBuilder.toString());
                    stringBuilder = null;
                    deliveryCountryCode = false;
                } else if (vendorCustomerNumber) {
                    bibInfoBean.setVendorCustomerNumber(stringBuilder.toString());
                    stringBuilder = null;
                    vendorCustomerNumber = false;
                } else if (purchaseOrderTransmissionMethodCode) {
                    bibInfoBean.setPurchaseOrderTransmissionMethodCode(stringBuilder.toString());
                    stringBuilder = null;
                    purchaseOrderTransmissionMethodCode = false;
                } else if (purchaseOrderCostSourceCode) {
                    bibInfoBean.setPurchaseOrderCostSourceCode(stringBuilder.toString());
                    stringBuilder = null;
                    purchaseOrderCostSourceCode = false;
                } else if (requestorPersonName) {
                    bibInfoBean.setRequestorPersonName(stringBuilder.toString());
                    stringBuilder = null;
                    requestorPersonName = false;
                } else if (requestorPersonEmailAddress) {
                    bibInfoBean.setRequestorPersonEmailAddress(stringBuilder.toString());
                    stringBuilder = null;
                    requestorPersonEmailAddress = false;
                } else if (requestorPersonPhoneNumber) {
                    bibInfoBean.setRequestorPersonPhoneNumber(stringBuilder.toString());
                    stringBuilder = null;
                    requestorPersonPhoneNumber = false;
                } else if (volumeNumber) {
                    bibInfoBean.setVolumeNumber(stringBuilder.toString());
                    stringBuilder = null;
                    volumeNumber = false;
                } else if (uom) {
                    bibInfoBean.setUom(stringBuilder.toString());
                    stringBuilder = null;
                    uom = false;
                } else if (chart) {
                    bibInfoBean.setChart(stringBuilder.toString());
                    stringBuilder = null;
                    chart = false;
                } else if (objectCode) {
                    bibInfoBean.setObjectCode(stringBuilder.toString());
                    stringBuilder = null;
                    objectCode = false;
                } else if (percent) {
                    if (stringBuilder.toString().length() > 0) {
                        bibInfoBean.setPercent(new Long(stringBuilder.toString()));
                        percent = false;
                    }
                    stringBuilder = null;
                } else if (chartOfAccountsCode) {
                    bibInfoBean.setChartOfAccountsCode(stringBuilder.toString());
                    stringBuilder = null;
                    chartOfAccountsCode = false;
                } else if (organizationCode) {
                    bibInfoBean.setOrganizationCode(stringBuilder.toString());
                    stringBuilder = null;
                    organizationCode = false;
                } else if (documentFundingSourceCode) {
                    bibInfoBean.setDocumentFundingSourceCode(stringBuilder.toString());
                    stringBuilder = null;
                    documentFundingSourceCode = false;
                } else if (useTaxIndicator) {
                    bibInfoBean.setUseTaxIndicator(stringBuilder.toString().equalsIgnoreCase("TRUE") ? true : false);
                    stringBuilder = null;
                    useTaxIndicator = false;
                } else if (deliveryBuildingOtherIndicator) {
                    bibInfoBean.setDeliveryBuildingOtherIndicator(stringBuilder.toString().equalsIgnoreCase("TRUE") ? true : false);
                    stringBuilder = null;
                    deliveryBuildingOtherIndicator = false;
                } else if (organizationAutomaticPurchaseOrderLimit) {
                    bibInfoBean.setOrganizationAutomaticPurchaseOrderLimit(stringBuilder.toString());
                    stringBuilder = null;
                    organizationAutomaticPurchaseOrderLimit = false;
                } else if (purchaseOrderAutomaticIndicator) {
                    bibInfoBean.setPurchaseOrderAutomaticIndicator(stringBuilder.toString().equalsIgnoreCase("TRUE") ? true : false);
                    stringBuilder = null;
                    purchaseOrderAutomaticIndicator = false;
                } else if (receivingDocumentRequiredIndicator) {
                    bibInfoBean.setReceivingDocumentRequiredIndicator(stringBuilder.toString().equalsIgnoreCase("TRUE") ? true : false);
                    stringBuilder = null;
                    receivingDocumentRequiredIndicator = false;
                } else if (paymentRequestPositiveApprovalIndicator) {
                    bibInfoBean.setPaymentRequestPositiveApprovalIndicator(stringBuilder.toString().equalsIgnoreCase("TRUE") ? true : false);
                    stringBuilder = null;
                    paymentRequestPositiveApprovalIndicator = false;
                } else if (itemTypeCode) {
                    bibInfoBean.setItemTypeCode(stringBuilder.toString());
                    stringBuilder = null;
                    itemTypeCode = false;
                } else if (billingName) {
                    bibInfoBean.setBillingName(stringBuilder.toString());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("------------------Billing Name-----stringBuilder.toString()--------" + stringBuilder.toString());
                        LOG.debug("------------------Billing Name-------------" + bibInfoBean.getBillingName());
                    }
                    stringBuilder = null;
                    billingName = false;
                } else if (billingLine1Address) {
                    bibInfoBean.setBillingLine1Address(stringBuilder.toString());
                    stringBuilder = null;
                    billingLine1Address = false;
                } else if (billingCityName) {
                    bibInfoBean.setBillingCityName(stringBuilder.toString());
                    stringBuilder = null;
                    billingCityName = false;
                } else if (billingStateCode) {
                    bibInfoBean.setBillingStateCode(stringBuilder.toString());
                    stringBuilder = null;
                    billingStateCode = false;
                } else if (billingPostalCode) {
                    bibInfoBean.setBillingPostalCode(stringBuilder.toString());
                    stringBuilder = null;
                    billingPostalCode = false;
                } else if (billingCountryCode) {
                    bibInfoBean.setBillingCountryCode(stringBuilder.toString());
                    stringBuilder = null;
                    billingCountryCode = false;
                } else if (billingPhoneNumber) {
                    bibInfoBean.setBillingPhoneNumber(stringBuilder.toString());
                    stringBuilder = null;
                    billingPhoneNumber = false;
                } else if (acquisitionunitvendoraccount) {
                    bibInfoBean.setVendorCustomerNumber(stringBuilder.toString());
                    stringBuilder = null;
                    acquisitionunitvendoraccount = false;
                } else if (qName.equalsIgnoreCase("REQUISITION")) {
                    bibInfoBean.setPurchaseOrderType(purchaseOrderType);
                    bibInfoBean.setRequisitionSource(requestorSourceType);
                    bibInfoBeanList.add(bibInfoBean);
                    stringBuilder = null;
                }

            }

            public void characters(char ch[], int start, int length)
                    throws SAXException {
                if (stringBuilder != null)
                    stringBuilder.append(ch, start, length);
            }

        };
        InputStream is = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
        saxParser.parse(is, handler);
    }
}
   
