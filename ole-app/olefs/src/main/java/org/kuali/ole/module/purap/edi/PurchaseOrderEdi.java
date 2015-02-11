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
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchaseOrderDocument;
import org.kuali.ole.module.purap.exception.PurError;
import org.kuali.ole.module.purap.util.PurApDateFormatUtils;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.lookup.DocLookupSearch;
import org.kuali.ole.select.lookup.DocLookupServiceImpl;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.core.api.util.RiceConstants;
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
    private void createEdiFile(PurchaseOrderDocument po, PurApItem item, String file) throws Exception {
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

}