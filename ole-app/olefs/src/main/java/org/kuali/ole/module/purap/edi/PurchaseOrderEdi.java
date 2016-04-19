/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.edi;

import com.lowagie.text.DocumentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.ole.docstore.OleException;
import org.kuali.ole.docstore.utility.ISBNUtil;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileConstantsBo;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.util.PurApDateFormatUtils;
import org.kuali.ole.select.businessobject.OleNotes;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.lookup.DocLookupSearch;
import org.kuali.ole.select.lookup.DocLookupServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Base class to handle Edi for purchase order documents.
 */
public class PurchaseOrderEdi {
    private static Log LOG = LogFactory.getLog(PurchaseOrderEdi.class);

    private DateTimeService dateTimeService;


    public PurchaseOrderEdi() {
    }


    /**
     * Invokes the createEdiFile method to create a EDI document and saves it into a file
     * which name and location are specified in the file.
     *
     * @param po           The PurchaseOrderDocument to be used to create the EDI.
     * @param file         The file containing some of the parameters information needed by the EDI for example, the EDI file name and EDI file location, purchasing director name, etc.
     * @param isRetransmit The boolean to indicate whether this is for a retransmit purchase order document.
     * @param environment  The current environment used (e.g. DEV if it is a development environment).
     */
    public boolean saveEdi(PurchaseOrderDocument po, PurApItem item, String file) {
        if (LOG.isInfoEnabled()) {
            LOG.info("saveEdi() started for po number " + po.getPurapDocumentIdentifier());
        }

        try {
            createEdiFile(po, item, file);
            if (LOG.isDebugEnabled()) {
                LOG.debug("saveEdi() completed for po number " + po.getPurapDocumentIdentifier());
            }
        } catch (DocumentException de) {
            LOG.error("saveEdi() DocumentException: " + de.getMessage(), de);
            throw new PurError("Document Exception when trying to save a Purchase Order EDI", de);
        } catch (FileNotFoundException f) {
            LOG.error("saveEdi() FileNotFoundException: " + f.getMessage(), f);
            throw new PurError("FileNotFound Exception when trying to save a Purchase Order EDI", f);
        } catch (IOException i) {
            LOG.error("saveEdi() IOException: " + i.getMessage(), i);
            throw new PurError("IO Exception when trying to save a Purchase Order EDI", i);
        } catch (Exception t) {
            LOG.error("saveEdi() EXCEPTION: " + t.getMessage(), t);
            throw new PurError("Exception when trying to save a Purchase Order EDI", t);
        }
        return true;
    }


    /**
     * This method is for generate a unique reference number for a PO
     *
     * @return String
     * @throws Exception
     */
    private String generateReferenceNumber() throws Exception {
        UUID referenceNumber = UUID.randomUUID();
        return referenceNumber.toString();
    }


    /**
     * Create an EDI file using the given input parameters.
     *
     * @param po   The PurchaseOrderDocument to be used to create the EDI.
     * @param file The EDI document whose margins have already been set.
     * @throws Exception
     */
   /* private void createEdiFile(PurchaseOrderDocument po, PurApItem item, String file) throws Exception {
        Writer writer = null;
        StringBuffer text = new StringBuffer();
        try {

            LOG.debug("EDI File creation started");
            List<OlePurchaseOrderItem> purchaseOrderItemList = new ArrayList<OlePurchaseOrderItem>();
            String itemIdentifier = item.getItemIdentifier().toString();
            Map map = new HashMap();
            map.put("itemIdentifier", itemIdentifier);
            DocLookupServiceImpl impl = SpringContext.getBean(DocLookupServiceImpl.class, "docLookupService");
            OlePurchaseOrderItem olePurchaseOrderItem = null;
            impl.setLookupDao(new DocLookupSearch());
            try {
                List<OlePurchaseOrderItem> searchResults = (List) impl.findCollectionBySearchHelper(OlePurchaseOrderItem.class, map, true);
                for (OlePurchaseOrderItem poItem : searchResults) {
                    purchaseOrderItemList.add(poItem);
                }
                olePurchaseOrderItem = purchaseOrderItemList.get(0);
            } catch (Exception exception) {
                LOG.error("*****Exception in createEdiFile()******: No matching data found in the docstore " + exception.getMessage());
            }

            int lineCount = 0;
            String uniqueRefNumber = generateReferenceNumber();
            text.append("UNH+" + uniqueRefNumber + "ORDERS:D:96A:UN:EAN008'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            String statusCode = "220";
            if (po.getApplicationDocumentStatus() != null && po.getApplicationDocumentStatus().equalsIgnoreCase(PurapConstants.PurchaseOrderStatuses.APPDOC_RETIRED_VERSION)) {
                statusCode = "230";
            }
            text.append("BGM+" + statusCode + "+" + po.getPurapDocumentIdentifier().toString() + "+9'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            String orderDate = "";

            SimpleDateFormat sdf = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE, Locale.getDefault());
            if (po.getPurchaseOrderInitialOpenTimestamp() != null) {
                orderDate = sdf.format(po.getPurchaseOrderInitialOpenTimestamp());
            } else {
                // This date isn't set until the first time this document is printed, so will be null the first time; use today's date.
                orderDate = sdf.format(getDateTimeService().getCurrentSqlDate());
            }

            SimpleDateFormat formatter = new SimpleDateFormat(RiceConstants.SIMPLE_DATE_FORMAT_FOR_DATE);
            Date date = formatter.parse(orderDate);
            String formattedDate = formatter.format(date);

            Date newDate = formatter.parse(formattedDate);
            formatter = new SimpleDateFormat("yyMMdd");
            formattedDate = formatter.format(newDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int century = (calendar.get(Calendar.YEAR) / 100) + 1;

            text.append("DTM+137:" + century + formattedDate + ":102'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("NAD+SU+++" + getSupplierDetails(po) + "'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("NAD+BY+++" + getBillingDetails(po) + "'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("NAD+DP+++" + getDeliveryDetails(po) + "'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            if (po.getVendorCustomerNumber() != null && po.getVendorCustomerNumber().length() > 0) {
                text.append("RFF+API:" + po.getVendorCustomerNumber() + "'");
                text.append(System.getProperty("line.separator"));
                lineCount++;
            }

            //text.append("CUX+2:"+"USD"+":9'");
            //text.append(System.getProperty("line.separator"));
            text.append("LIN+" + "1'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("PIA+5+" + "9781444334609" + ":IB'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            if (olePurchaseOrderItem != null && olePurchaseOrderItem.getDocData() != null) {
                if (olePurchaseOrderItem.getDocData().getTitle() != null && olePurchaseOrderItem.getDocData().getTitle().length() > 0) {
                    text.append("IMD+L+050+:::" + olePurchaseOrderItem.getDocData().getTitle() + "'");
                    text.append(System.getProperty("line.separator"));
                    lineCount++;
                }
                if (olePurchaseOrderItem.getDocData().getPlaceOfPublication() != null && olePurchaseOrderItem.getDocData().getPlaceOfPublication().length() > 0) {
                    text.append("IMD+L+110+:::" + olePurchaseOrderItem.getDocData().getPlaceOfPublication() + "'");
                    text.append(System.getProperty("line.separator"));
                    lineCount++;
                }
                if (olePurchaseOrderItem.getDocData().getAuthor() != null && olePurchaseOrderItem.getDocData().getAuthor().length() > 0) {
                    text.append("IMD+L+009+:::" + olePurchaseOrderItem.getDocData().getAuthor() + "'");
                    text.append(System.getProperty("line.separator"));
                    lineCount++;
                }
                if (olePurchaseOrderItem.getDocData().getPublicationDate() != null && olePurchaseOrderItem.getDocData().getPublicationDate().length() > 0) {
                    text.append("IMD+L+170+:::" + olePurchaseOrderItem.getDocData().getPublicationDate() + "'");
                    text.append(System.getProperty("line.separator"));
                    lineCount++;
                }
                if (olePurchaseOrderItem.getDocData().getPublisher() != null && olePurchaseOrderItem.getDocData().getPublisher().length() > 0) {
                    text.append("IMD+L+109+:::" + olePurchaseOrderItem.getDocData().getPublisher() + "'");
                    text.append(System.getProperty("line.separator"));
                    lineCount++;
                }
            }

            text.append("QTY+21:" + item.getItemQuantity() + "'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("PRI+AAE:" + "33" + ":SRP'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("UNS+S'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            text.append("CNT+2:1'");
            text.append(System.getProperty("line.separator"));
            lineCount++;
            lineCount++;
            text.append("UNT+" + lineCount + "+" + uniqueRefNumber + ":'");

            File filePath = new File(file);
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(text.toString());
            LOG.debug("EDI File created");
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
*/


    /**
     * This method is to get the Supplier details
     *
     * @param po
     * @return String
     */
    public String getSupplierDetails(PurchaseOrderDocument po) {
        String str = (po.getVendorName() != null && po.getVendorName().length() > 0 ? po.getVendorName() + "+" : "") +
                (po.getVendorAttentionName() != null && po.getVendorAttentionName().length() > 0 ? "ATTN: " + po.getVendorAttentionName() + "+" : "") +
                (po.getVendorLine1Address() != null && po.getVendorLine1Address().length() > 0 ? po.getVendorLine1Address() + "+" : "") +
                (po.getVendorCityName() != null && po.getVendorCityName().length() > 0 ? po.getVendorCityName() + "+" : "") +
                (po.getVendorStateCode() != null && po.getVendorStateCode().length() > 0 ? po.getVendorStateCode() + "+" : "") +
                (po.getVendorPostalCode() != null && po.getVendorPostalCode().length() > 0 ? po.getVendorPostalCode() + "+" : "") +
                (po.getVendorCountryCode() != null && po.getVendorCountryCode().length() > 0 ? po.getVendorCountryCode() : "");
        return str;

    }


    /**
     * This method is to get the billing details
     *
     * @param po
     * @return String
     */
    public String getBillingDetails(PurchaseOrderDocument po) {
        String str = (po.getBillingName() != null && po.getBillingName().length() > 0 ? po.getBillingName() + "+" : "") +
                (po.getBillingLine1Address() != null && po.getBillingLine1Address().length() > 0 ? po.getBillingLine1Address() + "+" : "") +
                (po.getBillingLine2Address() != null && po.getBillingLine2Address().length() > 0 ? po.getBillingLine2Address() + "+" : "") +
                (po.getBillingCityName() != null && po.getBillingCityName().length() > 0 ? po.getBillingCityName() + "+" : "") +
                (po.getBillingStateCode() != null && po.getBillingStateCode().length() > 0 ? po.getBillingStateCode() + "+" : "") +
                (po.getBillingPhoneNumber() != null && po.getBillingPhoneNumber().length() > 0 ? po.getBillingPhoneNumber() + "+" : "") +
                (po.getBillingPostalCode() != null && po.getBillingPostalCode().length() > 0 ? po.getBillingPostalCode() + "+" : "") +
                (po.getBillingCountryCode() != null && po.getBillingCountryCode().length() > 0 ? po.getBillingCountryCode() : "");
        return str;

    }


    /**
     * This method is to get the delivery details
     *
     * @param po
     * @return String
     */
    public String getDeliveryDetails(PurchaseOrderDocument po) {
        String str = (po.getDeliveryToName() != null && po.getDeliveryToName().length() > 0 ? po.getDeliveryToName() + "+" : "") +
                (po.getDeliveryBuildingRoomNumber() != null && po.getDeliveryBuildingRoomNumber().length() > 0 ? "Room # " + po.getDeliveryBuildingRoomNumber() + "+" : "") +
                (po.getDeliveryBuildingLine1Address() != null && po.getDeliveryBuildingLine1Address().length() > 0 ? po.getDeliveryBuildingLine1Address() + "+" : "") +
                (po.getDeliveryCityName() != null && po.getDeliveryCityName().length() > 0 ? po.getDeliveryCityName() + "+" : "") +
                (po.getDeliveryStateCode() != null && po.getDeliveryStateCode().length() > 0 ? po.getDeliveryStateCode() + "+" : "") +
                (po.getDeliveryPostalCode() != null && po.getDeliveryPostalCode().length() > 0 ? po.getDeliveryPostalCode() + "+" : "") +
                (po.getDeliveryCountryCode() != null && po.getDeliveryCountryCode().length() > 0 ? po.getDeliveryCountryCode() : "");
        return str;

    }


    public DateTimeService getDateTimeService() {
        if (ObjectUtils.isNull(dateTimeService)) {
            this.dateTimeService = SpringContext.getBean(DateTimeService.class);
        }
        return this.dateTimeService;
    }

    /* Created for SOAS Imp-- Created by Giri S & Gayathri A */
    private void createEdiFile(PurchaseOrderDocument po, PurApItem item, String file) throws Exception {

        Writer writer = null;
        try {
            LOG.debug("EDI File creation started");

            StringBuffer text = new StringBuffer();
            List<OlePurchaseOrderItem> purchaseOrderItemList = new ArrayList<>();

            /* Create edi doc for each line item  */
            Map map = new HashMap();
            map.put(OLEConstants.ITEM_IDENTIFIER, item.getItemIdentifier().toString());
            purchaseOrderItemList = getPOItemList(map);


             /* Create single edi doc for all line item  */

            /*List<PurApItem> items = po.getItems();

            for(PurApItem purApItem : items){
                if(purApItem != null && purApItem.getItemLineNumber() != null)
                    purchaseOrderItemList.addAll(getPurchaseOrderItemList(purApItem.getItemIdentifier().toString()));
            }*/

            //Get ProfileBO using vendor name
            OLEBatchProcessProfileBo profileBo = getOLEBatchProcessProfileBo(po.getVendorName());

            //Else get "Default_EDI_Format" ProfileBO
            if (profileBo == null) {
                profileBo = getOLEBatchProcessProfileBo(OLEConstants.EDIBatchProfile.DEFAULT_EDI_FORMAT);
            }

            if (profileBo != null) {
                text = createEDIText(profileBo, po, purchaseOrderItemList);
            }

            File filePath = new File(file);
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(text.toString());
            LOG.debug("EDI File created");

        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                LOG.error(e, e);
            }
        }

    }

    private List<OlePurchaseOrderItem> getPOItemList(Map map) {

        List<OlePurchaseOrderItem> purchaseOrderItemList = new ArrayList<OlePurchaseOrderItem>();

        DocLookupServiceImpl impl = SpringContext.getBean(DocLookupServiceImpl.class, OLEConstants.EDIBatchProfile.DOC_LOOKUP_SERVICE);
        impl.setLookupDao(new DocLookupSearch());

        try {

            purchaseOrderItemList = (List) impl.findCollectionBySearchHelper(OlePurchaseOrderItem.class, map, true);

        } catch (Exception exception) {
            LOG.error("*****Exception in createEdiFile()******: No matching data found in the docstore " + exception.getMessage());
        }

        return purchaseOrderItemList;
    }

    private KualiDecimal getTotalItemQuantity(PurchaseOrderDocument purchaseOrderDocument) {

        KualiDecimal totalQuantity = new KualiDecimal(0.0);

        Map map = new HashMap();
        map.put("documentNumber", purchaseOrderDocument.getDocumentNumber());
        List<OlePurchaseOrderItem> purchaseOrderItemList = getPOItemList(map);

        for (OlePurchaseOrderItem poItem : purchaseOrderItemList) {
            if (poItem.getItemLineNumber() != null)
                totalQuantity = totalQuantity.add(poItem.getItemQuantity());
        }

        return totalQuantity;

    }

    private OLEBatchProcessProfileBo getOLEBatchProcessProfileBo(String batchProcessProfileName) {

        List<OLEBatchProcessProfileBo> profileBoList = null;
        OLEBatchProcessProfileBo batchProcessProfileBo = null;

        try {
            Map profilemap = new HashMap();
            profilemap.put(OLEConstants.BATCH_PROFILE_NM, batchProcessProfileName);

            profileBoList = (List) KRADServiceLocator.getBusinessObjectService().findMatching(OLEBatchProcessProfileBo.class, profilemap);

            if (profileBoList != null && profileBoList.size() > 0) {
                Collections.sort(profileBoList);
                batchProcessProfileBo = profileBoList.get(profileBoList.size() - 1);
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }


        return batchProcessProfileBo;
    }

    private StringBuffer createEDIText(OLEBatchProcessProfileBo oleBatchProcessProfileBo, PurchaseOrderDocument purchaseOrderDocument, List<OlePurchaseOrderItem> purchaseOrderItemList) {

        List<OLEBatchProcessProfileConstantsBo> oleBatchProcessProfileConstantsList = oleBatchProcessProfileBo.getOleBatchProcessProfileConstantsList();
        int lineCount = 0;

        SimpleDateFormat dateFormatter = null;
        String randomNumber = "" + System.currentTimeMillis();
        Map<String, String> ediValues = new HashMap<String, String>();
        ISBNUtil isbnUtil = new ISBNUtil();
        StringBuffer text = new StringBuffer();
        String userName = purchaseOrderDocument.getVendorDetail().getVendorTransmissionFormat().get(0).getVendorEDIConnectionUserName();


        for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsList) {

            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.DEFAULT_SEGMENT)) {
                ediValues.put(OLEConstants.EDIBatchProfile.DEFAULT_SEGMENT, oleBatchProcessProfileConstantsBo.getAttributeValue());

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.INTERCHANGE_DETAILS)) {
                String[] splitString = oleBatchProcessProfileConstantsBo.getAttributeValue().split("\\*");
                dateFormatter = new SimpleDateFormat(OLEConstants.EDIBatchProfile.DATE_TIME_FORMAT_YYMMDDHHMM);
                ediValues.put(OLEConstants.EDIBatchProfile.INTERCHANGE_DETAILS, splitString[0] + userName + splitString[1] + purchaseOrderDocument.getVendorDetail().getVendorRemitName() + splitString[2] + dateFormatter.format(new Date()) + splitString[3] + randomNumber + splitString[4]);

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.UNIQUE_REF_NUMBER)) {
                ediValues.put(OLEConstants.EDIBatchProfile.UNIQUE_REF_NUMBER, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, randomNumber + OLEConstants.EDIBatchProfile.NUM_0001));

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.PURAP_DOCUMENT_IDENTIFIER)) {
                ediValues.put(OLEConstants.EDIBatchProfile.PURAP_DOCUMENT_IDENTIFIER, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, randomNumber + OLEConstants.EDIBatchProfile.NUM_0001));

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ORDER_DATE)) {
                dateFormatter = new SimpleDateFormat(OLEConstants.OleDeliverRequest.DATE_FORMAT);
                ediValues.put(OLEConstants.EDIBatchProfile.ORDER_DATE, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK,
                        purchaseOrderDocument.getPurchaseOrderInitialOpenTimestamp() != null ?
                                dateFormatter.format(purchaseOrderDocument.getPurchaseOrderInitialOpenTimestamp()) : dateFormatter.format(getDateTimeService().getCurrentSqlDate())));
                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.VENDOR_USERNAME)) {
                ediValues.put(OLEConstants.EDIBatchProfile.VENDOR_USERNAME, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, userName));

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.VENDOR_SAN)) {
                if(purchaseOrderDocument.getVendorDetail().getVendorRemitName()!=null){
                    ediValues.put(OLEConstants.EDIBatchProfile.VENDOR_SAN, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderDocument.getVendorDetail().getVendorRemitName()));
                    lineCount++;
                }
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.CURRENCY_CODE)) {
                ediValues.put(OLEConstants.EDIBatchProfile.CURRENCY_CODE, oleBatchProcessProfileConstantsBo.getAttributeValue());

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.SECTION_IDENTIFICATION)) {
                ediValues.put(OLEConstants.EDIBatchProfile.SECTION_IDENTIFICATION, oleBatchProcessProfileConstantsBo.getAttributeValue());

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_1)) {
                String totalQuantity = getTotalItemQuantity(purchaseOrderDocument).toString();
                ediValues.put(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_1, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, totalQuantity.substring(0,totalQuantity.toString().indexOf("."))));

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_2)) {
                ediValues.put(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_2, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, "" + purchaseOrderItemList.size()));

                lineCount++;
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.NUMBER_OF_SEGMENTS)) {
                String[] segmentCount = oleBatchProcessProfileConstantsBo.getAttributeValue().split("\\*");
                ediValues.put(OLEConstants.EDIBatchProfile.NUMBER_OF_SEGMENTS, segmentCount[0] + OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK + segmentCount[1] + randomNumber + OLEConstants.EDIBatchProfile.NUM_0001 + segmentCount[2]);
            }
            if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.INTERCHANGE_CONTROL_COUNT)) {
                ediValues.put(OLEConstants.EDIBatchProfile.INTERCHANGE_CONTROL_COUNT, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, randomNumber));

            }
        }

        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.DEFAULT_SEGMENT))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.DEFAULT_SEGMENT) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.INTERCHANGE_DETAILS))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.INTERCHANGE_DETAILS) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.UNIQUE_REF_NUMBER))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.UNIQUE_REF_NUMBER) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.PURAP_DOCUMENT_IDENTIFIER))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.PURAP_DOCUMENT_IDENTIFIER) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.ORDER_DATE))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.ORDER_DATE) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.VENDOR_USERNAME))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.VENDOR_USERNAME) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.VENDOR_SAN))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.VENDOR_SAN) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.CURRENCY_CODE))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.CURRENCY_CODE) + OLEConstants.OLEBatchProcess.lineSeparator);


        for (OlePurchaseOrderItem purchaseOrderItem : purchaseOrderItemList) {

            Map<String, String> ediItemValues = new HashMap<String, String>();
            String isbn = purchaseOrderItem.getDocData().getIsbn();
            if(isbn!=null){
                isbn = isbn.replaceAll("[-:\\s]", "");
                char[] digitCheck = isbn.toCharArray();
                for(int ch =0; ch<digitCheck.length;ch++){
                    if(!Character.isDigit(digitCheck[ch])){
                        isbn = isbn.substring(0,ch);
                        break;
                    }
                }
            }

            for (OLEBatchProcessProfileConstantsBo oleBatchProcessProfileConstantsBo : oleBatchProcessProfileConstantsList) {
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.LINE_ITEM)) {
                        try{
                            ediItemValues.put(OLEConstants.EDIBatchProfile.LINE_ITEM, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, isbn!=null && !isbn.equalsIgnoreCase("") ? isbnUtil.normalizeISBN(isbn) : ""));
                        }catch(OleException oe){
                            LOG.error("SaveEDi - Invalid ISBN" +oe.getMessage(), oe);
                        }
                    }
                    lineCount++;
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.PURCHASE_LINE_ITEM)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.PURCHASE_LINE_ITEM, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, isbn!=null ? isbn : ""));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ITEM_PRICE)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.ITEM_PRICE, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getItemListPrice().toString()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.ITM_AUT)) {
                    ediItemValues.put(OLEConstants.ITM_AUT, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getDocData().getAuthor()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.ITM_TIT)) {
                    ediItemValues.put(OLEConstants.ITM_TIT, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getDocData().getTitle()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getDocData().getPublisher()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER_DATE)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER_DATE, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getDocData().getPublicationDate()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ITEM_QUANTITY)) {
                    if(!purchaseOrderItem.getItemQuantity().toString().isEmpty()){
                        ediItemValues.put(OLEConstants.EDIBatchProfile.ITEM_QUANTITY, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getItemQuantity().toString().substring(0,purchaseOrderItem.getItemQuantity().toString().indexOf("."))));
                        lineCount++;
                    }
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_1)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_1, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getSourceAccountingLines().get(0).getAccountNumber().toLowerCase()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_2)) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_2, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderDocument.getPurapDocumentIdentifier().toString()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.ITEM_LOCATION) && purchaseOrderItem.getItemLocation()!=null) {
                    ediItemValues.put(OLEConstants.EDIBatchProfile.ITEM_LOCATION, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, purchaseOrderItem.getItemLocation()));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.OlePatronBill.LABEL_NOTE)) {
                    List<OleNotes> notesList = purchaseOrderItem.getNotes();
                    if (notesList.size() > 0) {
                        ediItemValues.put(OLEConstants.OlePatronBill.LABEL_NOTE, oleBatchProcessProfileConstantsBo.getAttributeValue().replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, notesList.get(0).getNote()));
                    }
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.DELIVERY_ADDRESS)) {
                    String[] address = oleBatchProcessProfileConstantsBo.getAttributeValue().split("\\*");
                    ediItemValues.put(OLEConstants.EDIBatchProfile.DELIVERY_ADDRESS, (address[0] + userName + address[1] + getDeliveryDetails(purchaseOrderDocument) + address[2]));
                    lineCount++;
                }
                if (oleBatchProcessProfileConstantsBo.getAttributeName().equalsIgnoreCase(OLEConstants.EDIBatchProfile.OTHER_ADDRESS)) {
                    String[] address = oleBatchProcessProfileConstantsBo.getAttributeValue().split("\\*");
                    ediItemValues.put(OLEConstants.EDIBatchProfile.OTHER_ADDRESS, (address[0] + userName + address[1] + getDeliveryDetails(purchaseOrderDocument) + address[2]));
                    lineCount++;
                }

            }

            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.LINE_ITEM))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.LINE_ITEM) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.PURCHASE_LINE_ITEM))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.PURCHASE_LINE_ITEM) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.ITEM_PRICE))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.ITEM_PRICE) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.ITM_AUT))
                text.append(ediItemValues.get(OLEConstants.ITM_AUT) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.ITM_TIT))
                text.append(ediItemValues.get(OLEConstants.ITM_TIT) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER_DATE))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.ITEM_PUBLISHER_DATE) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.ITEM_QUANTITY))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.ITEM_QUANTITY) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_1))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_1) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_2))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.REFERENCE_QUALIFIER_2) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.ITEM_LOCATION))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.ITEM_LOCATION) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.OlePatronBill.LABEL_NOTE))
                text.append(ediItemValues.get(OLEConstants.OlePatronBill.LABEL_NOTE) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.DELIVERY_ADDRESS))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.DELIVERY_ADDRESS) + OLEConstants.OLEBatchProcess.lineSeparator);
            if (ediItemValues.containsKey(OLEConstants.EDIBatchProfile.OTHER_ADDRESS))
                text.append(ediItemValues.get(OLEConstants.EDIBatchProfile.OTHER_ADDRESS) + OLEConstants.OLEBatchProcess.lineSeparator);


        }

        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.SECTION_IDENTIFICATION))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.SECTION_IDENTIFICATION) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_1))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_1) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_2))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.CONTROL_QUALIFIER_2) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.NUMBER_OF_SEGMENTS))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.NUMBER_OF_SEGMENTS).replace(OLEConstants.EDIBatchProfile.SYMBOL_ASTERISK, "" + lineCount--) + OLEConstants.OLEBatchProcess.lineSeparator);
        if (ediValues.containsKey(OLEConstants.EDIBatchProfile.INTERCHANGE_CONTROL_COUNT))
            text.append(ediValues.get(OLEConstants.EDIBatchProfile.INTERCHANGE_CONTROL_COUNT) + OLEConstants.OLEBatchProcess.lineSeparator);

        return text;
    }

}