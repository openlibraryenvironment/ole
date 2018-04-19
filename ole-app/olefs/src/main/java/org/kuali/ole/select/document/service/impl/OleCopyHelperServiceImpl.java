package org.kuali.ole.select.document.service.impl;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.Holdings;
import org.kuali.ole.docstore.common.document.content.instance.DonorInfo;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.ids.BibId;
import org.kuali.ole.docstore.common.document.ids.HoldingsId;
import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.select.bo.OLELinkPurapDonor;
import org.kuali.ole.select.businessobject.*;
import org.kuali.ole.select.document.service.OleCopyHelperService;
import org.kuali.ole.select.document.service.OleDocstoreHelperService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.api.util.type.KualiInteger;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 11/13/13
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCopyHelperServiceImpl implements OleCopyHelperService {

   private DocstoreClientLocator docstoreClientLocator;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    private DocstoreUtil docstoreUtil=new DocstoreUtil();
    private OleDocstoreHelperService oleDocstoreHelperService = SpringContext
            .getBean(OleDocstoreHelperService.class);
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
            .getLogger(OleDocstoreHelperServiceImpl.class);

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (null == docstoreClientLocator) {
            return SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }

    @Override
    public HashMap<String, List<OleCopy>> getCopyListBasedOnLocation(List<OleCopy> copyList, String bibId) {
        LinkedHashMap<String, List<OleCopy>> copyMap = new LinkedHashMap<>();
        for (OleCopy oleCopy : copyList) {
            Map map = new HashMap();
            map.put(OLEConstants.OleCopy.BIB_ID, bibId);
            map.put(OLEConstants.OleCopy.LOC, oleCopy.getLocation());
            if (oleCopy.getInstanceId() != null)
                map.put(OLEConstants.OleCopy.INSTANCE_ID, oleCopy.getInstanceId());
            if (oleCopy.getReqItemId() != null)
                map.put(OLEConstants.OleCopy.REQ_ITM_ID, oleCopy.getReqItemId());
            if (oleCopy.getPoDocNum() != null && oleCopy.getPoItemId() != null) {
                map.put(OLEConstants.OleCopy.PO_DOC_NUM, oleCopy.getPoDocNum());
                map.put(OLEConstants.OleCopy.PO_ITM_ID, oleCopy.getPoItemId());
            }
            List<OleCopy> oleCopyList = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class, map);
            List<OleCopy>  modCopyList = new ArrayList<>();
            for(OleCopy copy : copyList){
                for(OleCopy modCopy : oleCopyList){
                    if(modCopy.getCopyId().equals(copy.getCopyId())){
                        if(!modCopyList.contains(modCopy)){
                            modCopyList.add(modCopy);
                        }

                    }
                }
            }
            if (copyMap.containsKey(oleCopy.getLocation())) {
                List<OleCopy> oleCopiesList = copyMap.get(oleCopy.getLocation());
                for (int modCopies=0;modCopies<modCopyList.size();modCopies++) {
                    if (!oleCopiesList.contains(modCopyList.get(modCopies))) {
                        copyMap.get(oleCopy.getLocation()).add(modCopyList.get(modCopies));
                    }
                }
            } else {
                copyMap.put(oleCopy.getLocation(), modCopyList);
            }
        }
        return copyMap;
    }

    @Override
    public HashMap<String, List<OleCopy>> getCopyListBasedOnCopyNumber(List<OleCopy> copyList,Integer noOfParts) {
        HashMap<String, List<OleCopy>> copyMap = new HashMap<>();
        for (OleCopy oleCopy : copyList) {
            Map map = new HashMap();
            if(oleCopy.getCopyNumber()!=null)
                map.put(OLEConstants.OleCopy.COPY_NUM, oleCopy.getCopyNumber());
            map.put(OLEConstants.OleCopy.LOC, oleCopy.getLocation());
            map.put(OLEConstants.OleCopy.INSTANCE_ID, oleCopy.getInstanceId());
            if (oleCopy.getReqItemId() != null)
                map.put(OLEConstants.OleCopy.REQ_ITM_ID, oleCopy.getReqItemId());
            if (oleCopy.getPoDocNum() != null && oleCopy.getPoItemId() != null) {
                map.put(OLEConstants.OleCopy.PO_DOC_NUM, oleCopy.getPoDocNum());
                map.put(OLEConstants.OleCopy.PO_ITM_ID, oleCopy.getPoItemId());
            }
            List<OleCopy> oleCopyList = (List<OleCopy>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCopy.class, map);
            String location = oleCopy.getLocation()!=null?oleCopy.getLocation():"";
            String copyNum = oleCopy.getCopyNumber()!=null?"-"+oleCopy.getCopyNumber():"";
            if(copyNum.isEmpty()){
                int i=0;
                List<OleCopy> copies = new ArrayList<>();
                for(OleCopy copy : oleCopyList){
                    copies.add(copy);
                    if(noOfParts!=null &&( noOfParts==1 || copy.getPartNumber().equalsIgnoreCase(noOfParts.toString()))){
                        copyMap.put(location+i++, copies);
                        copies = new ArrayList<>();
                    }
                }
            }else{
                copyMap.put(location+copyNum, oleCopyList);
            }
        }
        return copyMap;
    }

    public String getStartingNumber(String content){
        StringBuilder sb = new StringBuilder();
        String number = "";
        if (content != null) {
            for (int i = 0; i < content.length(); i++) {
                final char c = content.charAt(i);
                if (c > 47 && c < 58) {
                    sb.append(c);
                }
            }
            number = sb.toString();
        }
        return number;
    }

    /**
     * This method will set copies into list of copies for LineItem.
     *
     * @param copyList
     * @param noOfParts
     * @return
     */
    @Override
    public List<OleCopies> setCopiesToLineItem(List<OleCopy> copyList, KualiInteger noOfParts, String bibId) {
        List<OleCopies> copies = new ArrayList<OleCopies>();
        HashMap<String, List<OleCopy>> copyListBasedOnLocation = getCopyListBasedOnLocation(copyList, bibId);
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnLocation.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> oleCopyList = entry.getValue();
            String caption = "";
            LinkedHashMap<String, String> volumeNumbers = new LinkedHashMap<>();
            String location = "";
            int i = 0;
            KualiInteger staringCopyNumber = oleCopyList != null && oleCopyList.size() > 0 && oleCopyList.get(0).getCopyNumber() != null ?
                    new KualiInteger(getStartingNumber(oleCopyList.get(0).getCopyNumber())) : null;
            for (OleCopy copy : oleCopyList) {
                i++;
                location = copy.getLocation();
                String[] enums = copy.getEnumeration() != null ? copy.getEnumeration().split("\\s") : new String[0];
                if (enums != null && enums.length > 0) {
                    caption = enums[0];
                    if (caption.equals(OLEConstants.OlePersonRequestorLookupable.NULLSTRING)){
                        caption="";
                    }
                    if( enums.length > 1) {
                        volumeNumbers.put(enums[1], enums[1]);
                    }
                    if(enums.length==1 && isNumber(enums[0])){
                        volumeNumbers.put(enums[0], enums[0]);
                        caption="";
                    }
                }
            }
            if (i > 0) {
                OleRequisitionCopies oleRequisitionCopies = new OleRequisitionCopies();
                StringBuffer volumeNumber = new StringBuffer();
                Iterator<Map.Entry<String, String>> volumeEntries = volumeNumbers.entrySet().iterator();
                int j = 0;
                while (volumeEntries.hasNext()) {
                    Map.Entry<String, String> volumeEntry = volumeEntries.next();
                    if (j > 0)
                        volumeNumber.append(",");
                    j++;
                    volumeNumber.append(volumeEntry.getValue());
                }
                KualiDecimal copyCount = noOfParts.isGreaterThan(KualiInteger.ZERO) ? new KualiDecimal(new KualiInteger(i).divide(noOfParts)) : new KualiDecimal(oleCopyList.size());
                oleRequisitionCopies.setItemCopies(copyCount);
                oleRequisitionCopies.setParts(noOfParts);
                oleRequisitionCopies.setLocationCopies(location);
                oleRequisitionCopies.setCaption(caption);
                oleRequisitionCopies.setVolumeNumber(volumeNumber.toString());
                oleRequisitionCopies.setStartingCopyNumber(staringCopyNumber);
                copies.add(oleRequisitionCopies);
            }
        }
        return copies;
    }

    public static boolean isNumber(String value) {
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * This method takes RequisitionItem as parameter, it will calculate and set copyList
     * lineItem
     *
     * @param itemCopy
     * @return OleRequisitionCopies
     */
    public List<OleCopy> setCopyValues(OleRequisitionCopies itemCopy, String bibId, List<String> volChar) {
        KualiInteger noOfItems = KualiInteger.ZERO;
        KualiInteger noOfCopies = new KualiInteger(itemCopy.getItemCopies().intValue());
        if (noOfCopies.isGreaterThan(KualiInteger.ZERO)) {
            if (itemCopy.getParts().isGreaterThan(KualiInteger.ZERO)) {
                noOfItems = itemCopy.getParts().multiply(noOfCopies);
            } else {
                noOfItems = new KualiInteger(itemCopy.getItemCopies().intValue());
            }
        }
        int itemCount = volChar.size();
        if(itemCopy.getParts().equals(new KualiInteger(1)) && volChar.size()==0){
            itemCount = 1;
        }
        List<OleCopy> copyList = new ArrayList<>();
        Integer copyNum = itemCopy.getStartingCopyNumber() != null ? itemCopy.getStartingCopyNumber().intValue() : null;
        if (itemCount == itemCopy.getParts().intValue()) {
            String volNum = "";
            for (int i = 0, partNumCount = 1, enumCount = 0; i < noOfItems.intValue(); i++) {
                OleCopy oleCopy = new OleCopy();
                oleCopy.setLocation(itemCopy.getLocationCopies());
                volNum = volChar.size()>enumCount ? volChar.get(enumCount):"";
                if(StringUtils.isNotBlank(itemCopy.getCaption())) {
                    oleCopy.setEnumeration(itemCopy.getCaption() + " " + volNum);
                }
                oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                oleCopy.setCopyNumber(copyNum!=null?copyNum.toString():(i+""));
                oleCopy.setPartNumber(partNumCount + "");
                oleCopy.setBibId(bibId);
                if (partNumCount == itemCopy.getParts().intValue()) {
                    partNumCount = 1;
                    if(copyNum != null){
                        copyNum++;
                    }
                    enumCount = 0;
                } else {
                    enumCount++;
                    partNumCount++;
                }
                copyList.add(oleCopy);
            }
        }
        return copyList;
    }


    /**
     * This method takes RequisitionItem as parameter, it will calculate and set copyList
     * lineItem
     *
     * @param itemCopies
     * @return OleRequisitionCopies
     */
    public List<OleCopy> setCopyValuesForList(List<OleCopies> itemCopies, String bibId, BibId bibTree, String oleERSIdentifier) {
        List<OleCopy> copyList = new ArrayList<>();
        for (OleCopies itemCopy : itemCopies) {
            KualiInteger noOfItems = KualiInteger.ZERO;
            KualiInteger noOfCopies = new KualiInteger(itemCopy.getItemCopies().intValue());
            if (noOfCopies.isGreaterThan(KualiInteger.ZERO)) {
                if (itemCopy.getParts().isGreaterThan(KualiInteger.ZERO)) {
                    noOfItems = itemCopy.getParts().multiply(noOfCopies);
                } else {
                    noOfItems = new KualiInteger(itemCopy.getItemCopies().intValue());
                }
            }
            List<String> volChar = new ArrayList<>();
            String[] volNumbers = itemCopy.getVolumeNumber() != null ? itemCopy.getVolumeNumber().split(",") : new String[0];
            for (String volStr : volNumbers) {
                volChar.add(volStr);
            }
            int itemCount = volChar.size();
            if(itemCopy.getParts().equals(new KualiInteger(1)) && volChar.size()==0){
                itemCount = 1;
            }
            Integer copyNum = itemCopy.getStartingCopyNumber() != null ? itemCopy.getStartingCopyNumber().intValue() : null;
            if (itemCount == itemCopy.getParts().intValue()) {
                String volNum="";
                for (int i = 0, partNumCount = 1, enumCount = 0; i < noOfItems.intValue(); i++) {
                    OleCopy oleCopy = new OleCopy();
                    String caption = itemCopy.getCaption()!=null?itemCopy.getCaption():"";
                    oleCopy.setLocation(itemCopy.getLocationCopies());
                    volNum = volChar.size()>enumCount ? volChar.get(enumCount):"";
                    oleCopy.setEnumeration(caption + " " + volNum);
                    oleCopy.setReceiptStatus(OLEConstants.OleLineItemReceiving.NOT_RECEIVED_STATUS);
                    if(itemCopy.getSingleCopyNumber()!=null){
                        oleCopy.setCopyNumber(itemCopy.getSingleCopyNumber().toString());
                    }else {
                        oleCopy.setCopyNumber(copyNum != null ? copyNum.toString() : null);
                    }
                    oleCopy.setPartNumber(partNumCount + "");
                    oleCopy.setBibId(bibId);
                    if (oleERSIdentifier != null) {
                        oleCopy.setOleERSIdentifier(oleERSIdentifier);
                    }
                    if (partNumCount == itemCopy.getParts().intValue()) {
                        partNumCount = 1;
                        if(copyNum != null){
                            copyNum++;
                        }
                        enumCount = 0;
                    } else {
                        enumCount++;
                        partNumCount++;
                    }
                    copyList.add(oleCopy);
                }
            }
        }
        if (bibTree != null) {
            for (int size = 0; size < copyList.size(); size++) {
                for (HoldingsId holdingsId : bibTree.getHoldingsIds()) {
                    if (holdingsId.getItems() != null && holdingsId.getItems().size() > 0) {
                        for (String itemId : holdingsId.getItems()) {
                            if (copyList.size() > size) {
                                copyList.get(size).setInstanceId(holdingsId.getId());
                                setLocationToCopy(copyList.get(size));
                                copyList.get(size).setItemUUID(itemId);
                                size++;
                            }
                        }
                    } else {
                        if (copyList.size() > size) {
                            copyList.get(size).setInstanceId(holdingsId.getId());
                            setLocationToCopy(copyList.get(size));
                            size++;
                        }
                    }
                }
            }
        }
        return copyList;
    }

    private void setLocationToCopy(OleCopy oleCopy){
        try{
            if (oleCopy.getInstanceId()!=null){
                Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
                org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                StringBuffer locationName=new StringBuffer("");
                String location=null;
                location=docstoreUtil.getLocation(oleHoldings.getLocation(),locationName);
                if (org.apache.commons.lang3.StringUtils.isNotBlank(location) && oleDocstoreHelperService.isValidLocation(location)){
                    oleCopy.setLocation(location);
                }
            }
        }catch (Exception e){
            LOG.error("Exception "+e);
        }
    }

    public boolean checkCopyEntry(KualiDecimal noOfCopies, String location, Integer itemCount, KualiDecimal noOfCopiesOrdered,
                                  KualiInteger noOfPartsOrdered, List<OleCopies> copiesList, String volumeNumber, boolean isRoute) {
        boolean isValid = true;
        isValid &= checkForCopiesAndLocation(noOfCopies, location);
        isValid &= checkForItemCopiesGreaterThanQuantity(noOfCopies, noOfCopiesOrdered);
        if (!isRoute)
            isValid &= checkForTotalCopiesGreaterThanQuantity(copiesList, noOfCopies, noOfCopiesOrdered);
        isValid &= volumeNumberValidation(itemCount, noOfPartsOrdered, volumeNumber, copiesList, isRoute);
        return isValid;
    }

    public boolean checkForCopiesAndLocation(KualiDecimal noOfCopies, String location) {
        boolean isValid = true;
        if (null == noOfCopies || null == location) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_ITEMCOPIES_OR_LOCATIONCOPIES_SHOULDNOT_BE_NULL, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean checkForItemCopiesGreaterThanQuantity(KualiDecimal noOfCopies, KualiDecimal noOfCopiesOrdered) {
        boolean isValid = true;
        if (noOfCopies != null && noOfCopiesOrdered != null && noOfCopies.isGreaterThan(noOfCopiesOrdered)) {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
            isValid = false;
        }
        return isValid;
    }


    public boolean checkForTotalCopiesGreaterThanQuantity(List<OleCopies> copyList, KualiDecimal noOfCopies, KualiDecimal noOfCopiesOrdered) {
        boolean isValid = true;
        int copies = 0;
        if (copyList.size() > 0) {
            for (int itemCopies = 0; itemCopies < copyList.size(); itemCopies++) {
                copies = copies + copyList.get(itemCopies).getItemCopies().intValue();
            }
            if (noOfCopies != null && noOfCopiesOrdered != null && noOfCopiesOrdered.isLessThan(noOfCopies.add(new KualiDecimal(copies)))) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }

    public boolean checkForTotalCopiesGreaterThanQuantityAtSubmit(List<OleCopies> copyList, KualiDecimal noOfCopiesOrdered) {
        boolean isValid = true;
        int copies = 0;
        if (copyList.size() > 0) {
            for (int itemCopies = 0; itemCopies < copyList.size(); itemCopies++) {
                copies = copies + copyList.get(itemCopies).getItemCopies().intValue();
            }
            if (noOfCopiesOrdered != null && !noOfCopiesOrdered.equals(new KualiDecimal(copies))) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
                isValid = false;
            }
        } else {
            GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                    OLEConstants.TOTAL_OF_ITEM_COPIES_ITEMCOPIES_GREATERTHAN_ITEMCOPIESORDERED, new String[]{});
            isValid = false;
        }
        return isValid;
    }

    public boolean volumeNumberValidation(Integer itemCount, KualiInteger noOfPartsOrdered, String volumeNumber, List<OleCopies> copiesList, boolean isRoute) {
        boolean isValid = true;
        int noOfParts = noOfPartsOrdered != null ? noOfPartsOrdered.intValue() : 0;
        if(noOfParts > 1){
            if (isRoute) {
                for (OleCopies oleCopies : copiesList) {
                    if (!isValidVolumeNumber(oleCopies.getVolumeNumber(), OLEConstants.VOLUME_NUMBER_PATTERN)) {
                        GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                                OLEConstants.VOLUME_NUMBER_REGEX_VALIDATIONS, new String[]{});
                        isValid = false;
                    }
                }
            } else {
                if (!isValidVolumeNumber(volumeNumber, OLEConstants.VOLUME_NUMBER_PATTERN)) {
                    GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                            OLEConstants.VOLUME_NUMBER_REGEX_VALIDATIONS, new String[]{});
                    isValid = false;
                }
            }
            if (itemCount != noOfParts) {
                GlobalVariables.getMessageMap().putError(PurapConstants.ITEM_TAB_ERROR_PROPERTY,
                        OLEConstants.VOLUME_NUMBER_VALIDATIONS, new String[]{});
                isValid = false;
            }
        }
        return isValid;
    }

    public void updateRequisitionAndPOItems(OlePurchaseOrderItem olePurchaseOrderItem,
                                            OleLineItemReceivingItem oleLineItemReceivingItem, OleCorrectionReceivingItem oleCorrectionReceivingItem, boolean isReceiving) {

        List<OleCopy> copyList = null;
        List<OLELinkPurapDonor> oleLinkPurapDonors = new ArrayList<>();
        try{
            if (!isReceiving && oleCorrectionReceivingItem != null) {
                copyList = oleCorrectionReceivingItem.getCopyList() != null ? oleCorrectionReceivingItem.getCopyList() : new ArrayList<OleCopy>();
                oleLinkPurapDonors = oleCorrectionReceivingItem.getOleDonors();
                olePurchaseOrderItem.setOleDonors(oleLinkPurapDonors);
            } else {
                copyList = oleLineItemReceivingItem.getCopyList() != null ? oleLineItemReceivingItem.getCopyList() : new ArrayList<OleCopy>();
                oleLinkPurapDonors = oleLineItemReceivingItem.getOleDonors();
                olePurchaseOrderItem.setOleDonors(oleLinkPurapDonors);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        Integer receivedCount = 0;
        for (OleCopy oleCopy : copyList) {
            if (!isReceiving && oleCorrectionReceivingItem != null)
                oleCopy.setCorrectionItemId(oleCorrectionReceivingItem.getReceivingItemIdentifier());
            if (oleCopy.getReceiptStatus().equalsIgnoreCase(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS)) {
                receivedCount++;
            }
        }
        if (receivedCount == 0) {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_NOT_RECEIVED));
        } else if (receivedCount == copyList.size()) {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_FULLY_RECEIVED));
        } else {
            oleLineItemReceivingItem
                    .setReceiptStatusId(getReceiptStatusDetails(OLEConstants.PO_RECEIPT_STATUS_PARTIALLY_RECEIVED));
        }
        olePurchaseOrderItem.setReceiptStatusId(oleLineItemReceivingItem.getReceiptStatusId());
        if (olePurchaseOrderItem.getItemQuantity().equals(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived(OLEConstants.OleLineItemReceiving.NOT_APPLICABLE);
            olePurchaseOrderItem.setNoOfPartsReceived(receivedCount.toString());
        } else if (olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().equals(new KualiInteger(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived(receivedCount.toString());
            olePurchaseOrderItem.setNoOfPartsReceived(OLEConstants.OleLineItemReceiving.NOT_APPLICABLE);
        } else if (olePurchaseOrderItem.getItemQuantity().isGreaterThan(new KualiDecimal(1)) && olePurchaseOrderItem.getItemNoOfParts().isGreaterThan(new KualiInteger(1))) {
            olePurchaseOrderItem.setNoOfCopiesReceived(OLEConstants.OleLineItemReceiving.SEE_COPIES_SECTION);
            olePurchaseOrderItem.setNoOfPartsReceived(OLEConstants.OleLineItemReceiving.SEE_COPIES_SECTION);
        } else {
            olePurchaseOrderItem.setNoOfCopiesReceived(receivedCount.toString());
            olePurchaseOrderItem.setNoOfPartsReceived(receivedCount.toString());
        }

        Integer reqsItemId = copyList.size() > 0 ? copyList.get(0).getReqItemId() : null;
        Map reqItemMap = new HashMap();
        reqItemMap.put(OLEConstants.LN_ITM_IDN, reqsItemId);
        OleRequisitionItem oleRequisitionItem = KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(OleRequisitionItem.class, reqItemMap);
        if (oleRequisitionItem != null) {
            oleRequisitionItem.setNoOfCopiesReceived(olePurchaseOrderItem.getNoOfCopiesReceived());
            oleRequisitionItem.setNoOfPartsReceived(olePurchaseOrderItem.getNoOfPartsReceived());
            oleRequisitionItem.setOleDonors(olePurchaseOrderItem.getOleDonors());
            oleRequisitionItem.setReceiptStatusId(olePurchaseOrderItem.getReceiptStatusId());
            KRADServiceLocator.getBusinessObjectService().save(oleRequisitionItem);
        }
        if (isReceiving) {
            HashMap<Integer, String> receiptStatusMap = new HashMap<Integer, String>();
            for (OleCopy oleCopy : oleLineItemReceivingItem.getCopyList()) {
                receiptStatusMap.put(oleCopy.getCopyId(), oleCopy.getReceiptStatus());
            }
            updateReceivedCountFromRCVL(oleLineItemReceivingItem, receiptStatusMap);
            for (OleCopy purCopy : olePurchaseOrderItem.getCopyList()) {
                if (oleLineItemReceivingItem.getItemReceivedTotalQuantity().compareTo(KualiDecimal.ZERO) > 0) {
                    purCopy.setReceiptStatus(receiptStatusMap.get(purCopy.getCopyId()));
                    purCopy.setReceivingItemId(oleLineItemReceivingItem.getReceivingItemIdentifier());
                }
            }
        } else {
            if (oleCorrectionReceivingItem != null) {
                HashMap<Integer, String> receiptStatusMap = new HashMap<Integer, String>();
                for (OleCopy oleCopy : oleCorrectionReceivingItem.getCopyList()) {
                    receiptStatusMap.put(oleCopy.getCopyId(), oleCopy.getReceiptStatus());
                }
                updateReceivedCountFromRCVC(oleLineItemReceivingItem, receiptStatusMap);
                oleCorrectionReceivingItem.setItemReceivedTotalQuantity(oleLineItemReceivingItem.getItemReceivedTotalQuantity());
                oleCorrectionReceivingItem.setItemReceivedTotalParts(oleLineItemReceivingItem.getItemReceivedTotalParts());
                oleCorrectionReceivingItem.setItemReturnedTotalQuantity(oleLineItemReceivingItem.getItemReturnedTotalQuantity());
                oleCorrectionReceivingItem.setItemReturnedTotalParts(oleLineItemReceivingItem.getItemReturnedTotalParts());
                for (OleCopy purCopy : olePurchaseOrderItem.getCopyList()) {
                    purCopy.setReceiptStatus(receiptStatusMap.get(purCopy.getCopyId()));
                    purCopy.setCorrectionItemId(oleCorrectionReceivingItem.getReceivingItemIdentifier());
                }
            }
        }

        KRADServiceLocator.getBusinessObjectService().save(olePurchaseOrderItem);

        updateItemOrEHoldings(olePurchaseOrderItem.getLinkToOrderOption(), copyList, oleLinkPurapDonors);
    }

    private void updateItemOrEHoldings(String linkToOrderOption, List<OleCopy> copyList, List<OLELinkPurapDonor> oleLinkPurapDonors) {
        try {
            for (OleCopy oleCopy : copyList) {
                if ((linkToOrderOption.equalsIgnoreCase("ORDER_RECORD_IMPORT_MARC_ONLY_PRINT") || linkToOrderOption.equalsIgnoreCase("NB_PRINT") || linkToOrderOption.equalsIgnoreCase("EB_PRINT")) && StringUtils.isNotBlank(oleCopy.getItemUUID())) {
                    org.kuali.ole.docstore.common.document.Item item = getDocstoreClientLocator().getDocstoreClient().retrieveItem(oleCopy.getItemUUID());
                    Item itemContent = new ItemOlemlRecordProcessor().fromXML(item.getContent());
                    List<DonorInfo> donorInfoList = getDonorInfoList(oleLinkPurapDonors, itemContent.getDonorInfo());
                    itemContent.setDonorInfo(donorInfoList);
                    item.setContent(new ItemOlemlRecordProcessor().toXML(itemContent));
                    item.setId(itemContent.getItemIdentifier());
                    getDocstoreClientLocator().getDocstoreClient().updateItem(item);
                } else if ((linkToOrderOption.equalsIgnoreCase("ORDER_RECORD_IMPORT_MARC_EDI_ELECTRONIC") || linkToOrderOption.equalsIgnoreCase("NB_ELECTRONIC") || linkToOrderOption.equalsIgnoreCase("EB_ELECTRONIC")) && StringUtils.isNotBlank(oleCopy.getInstanceId())) {
                    Holdings holdings = getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleCopy.getInstanceId());
                    org.kuali.ole.docstore.common.document.content.instance.OleHoldings oleHoldings = holdingOlemlRecordProcessor.fromXML(holdings.getContent());
                    List<DonorInfo> donorInfoList = getDonorInfoList(oleLinkPurapDonors, oleHoldings.getDonorInfo());
                    oleHoldings.setDonorInfo(donorInfoList);
                    holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
                    getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception " + e);
        }
    }

    private List<DonorInfo> getDonorInfoList(List<OLELinkPurapDonor> oleLinkPurapDonors, List<DonorInfo> donorInfos) {
        List<OLELinkPurapDonor> oleLinkPurapDonorList = new ArrayList<>();
        for (OLELinkPurapDonor oleLinkPurapDonor : oleLinkPurapDonors) {
            boolean flag = true;
            if (donorInfos != null && donorInfos.size() > 0) {
                for (DonorInfo itemDonorInfo : donorInfos) {
                    if (oleLinkPurapDonor.getDonorCode().equals(itemDonorInfo.getDonorCode())) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    oleLinkPurapDonorList.add(oleLinkPurapDonor);
                }
            }
        }
        List<DonorInfo> donorInfoList = new ArrayList<>();
        if (donorInfos != null && donorInfos.size() > 0) {
            donorInfoList = oleDocstoreHelperService.setDonorInfoToItem(oleLinkPurapDonorList, donorInfos);
        } else {
            donorInfoList = oleDocstoreHelperService.setDonorInfoToItem(oleLinkPurapDonors, donorInfos);
        }
        return donorInfoList;
    }

    public int getReceiptStatusDetails(String receiptStatusCd) {
        int receiptStatusId = 0;
        Map<String, String> receiptStatusCdMap = new HashMap<String, String>();
        receiptStatusCdMap.put(OLEConstants.RCPT_STATUS_CD, receiptStatusCd);
        List<OleReceiptStatus> oleReceiptStatusList = (List) KRADServiceLocator.getBusinessObjectService().findMatching(
                OleReceiptStatus.class, receiptStatusCdMap);
        for (OleReceiptStatus oleReceiptStatus : oleReceiptStatusList) {
            receiptStatusId = oleReceiptStatus.getReceiptStatusId().intValue();
        }
        return receiptStatusId;
    }

    private void updateReceivedCountFromRCVL(OleLineItemReceivingItem oleLineItemReceivingItem, HashMap<Integer, String> receiptStatusMap) {
        HashMap<String, List<OleCopy>> copyListBasedOnCopyNumber = getCopyListBasedOnCopyNumber(oleLineItemReceivingItem.getCopyList(),oleLineItemReceivingItem.getItemOrderedParts().intValue());
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnCopyNumber.entrySet().iterator();
        Integer receivedCopyCount = 0;
        Integer receivedParts=0;
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> copyMap = entry.getValue();
            Integer copyCount = 0;
            for (OleCopy copy : copyMap) {
                if (receiptStatusMap.get(copy.getCopyId()) != null &&
                        receiptStatusMap.get(copy.getCopyId()).equalsIgnoreCase(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS)) {
                    copyCount++;
                    receivedParts++;
                }
            }
            if (copyCount != 0 &&copyCount <=oleLineItemReceivingItem.getItemOrderedParts().intValue()) {
                receivedCopyCount++;
            }
        }
        if (receivedCopyCount > 0) {
            oleLineItemReceivingItem.setItemReceivedTotalQuantity(new KualiDecimal(receivedCopyCount));
            oleLineItemReceivingItem.setItemReceivedTotalParts(new KualiDecimal(receivedParts));
        } else {
            oleLineItemReceivingItem.setItemReceivedTotalQuantity(KualiDecimal.ZERO);
            oleLineItemReceivingItem.setItemReceivedTotalParts(KualiDecimal.ZERO);
        }
    }

    private void updateReceivedCountFromRCVC(OleLineItemReceivingItem oleLineItemReceivingItem, HashMap<Integer, String> receiptStatusMap) {
        HashMap<String, List<OleCopy>> copyListBasedOnCopyNumber = getCopyListBasedOnCopyNumber(oleLineItemReceivingItem.getCopyList(),oleLineItemReceivingItem.getItemOrderedParts().intValue());
        Iterator<Map.Entry<String, List<OleCopy>>> entries = copyListBasedOnCopyNumber.entrySet().iterator();
        Integer receivedCopyCount = 0;
        Integer unReceivedCopyCount = 0;
        while (entries.hasNext()) {
            Map.Entry<String, List<OleCopy>> entry = entries.next();
            List<OleCopy> copyMap = entry.getValue();
            Integer copyCount = 0;
            for (OleCopy copy : copyMap) {
                if (receiptStatusMap.get(copy.getCopyId()) != null &&
                        receiptStatusMap.get(copy.getCopyId()).equalsIgnoreCase(OLEConstants.OleLineItemReceiving.RECEIVED_STATUS)) {
                    copyCount++;
                }
            }
            if (copyCount == oleLineItemReceivingItem.getItemOrderedParts().intValue()) {
                receivedCopyCount++;
            } else {
                unReceivedCopyCount++;
            }
        }
        if (receivedCopyCount > 0) {
            oleLineItemReceivingItem.setItemReceivedTotalQuantity(new KualiDecimal(receivedCopyCount));
            oleLineItemReceivingItem.setItemReceivedTotalParts(new KualiDecimal(oleLineItemReceivingItem.getItemOrderedParts().intValue()));
        } else {
            oleLineItemReceivingItem.setItemReceivedTotalQuantity(KualiDecimal.ZERO);
            oleLineItemReceivingItem.setItemReceivedTotalParts(KualiDecimal.ZERO);
        }
        if (unReceivedCopyCount > 0) {
            oleLineItemReceivingItem.setItemReturnedTotalQuantity(new KualiDecimal(unReceivedCopyCount));
            oleLineItemReceivingItem.setItemReturnedTotalParts(new KualiDecimal(oleLineItemReceivingItem.getItemOrderedParts().intValue()));
        } else {
            oleLineItemReceivingItem.setItemReturnedTotalQuantity(KualiDecimal.ZERO);
            oleLineItemReceivingItem.setItemReturnedTotalParts(KualiDecimal.ZERO);
        }
    }

    public boolean isValidVolumeNumber(String volumeNumber, String pattern) {
        //  pattern = "^([0-9][,][0-9]*)$";
        boolean valid = false;
        //"^[0-9]{1}(([0-9]*-[0-9]*)*|([0-9]* [0-9]*)*|[0-9]*)[0-9]{1}$"
        if (pattern != null && volumeNumber != null) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(volumeNumber);
            StringBuffer sb = new StringBuffer();
            boolean result = m.matches();

            if (result) {
                valid = true;
            }
        }
        return valid;
    }

}
