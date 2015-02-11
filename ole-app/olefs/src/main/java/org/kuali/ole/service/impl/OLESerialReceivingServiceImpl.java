package org.kuali.ole.service.impl;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEKeyConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.ItemOleml;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.ItemType;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.select.bo.OLESerialReceivingDocument;
import org.kuali.ole.select.bo.OLESerialReceivingHistory;
import org.kuali.ole.select.bo.OLESerialReceivingType;
import org.kuali.ole.select.bo.OLESerialRelatedPODocument;
import org.kuali.ole.select.businessobject.OleCopy;
import org.kuali.ole.select.document.OlePurchaseOrderDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.vnd.businessobject.VendorAlias;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/9/13
 * Time: 7:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingServiceImpl implements OLESerialReceivingService {



    private DocstoreClientLocator docstoreClientLocator;

    public DocstoreClientLocator getDocstoreClientLocator() {

        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);

        }
        return docstoreClientLocator;
    }

    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;

    BusinessObjectService   businessObject = SpringContext.getBean(BusinessObjectService.class);
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLESerialReceivingServiceImpl.class);



    private ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        }
        return itemOlemlRecordProcessor;
    }


    public void receiveRecord(OLESerialReceivingDocument oleSerialReceivingDocument,String receiptStatus){
        List<OLESerialReceivingHistory> oleSerialReceivingHistoryList = oleSerialReceivingDocument.getOleSerialReceivingHistoryList();
        OLESerialReceivingHistory oleSerialReceivingHistory = new OLESerialReceivingHistory();
        oleSerialReceivingHistory.setReceivingRecordType(oleSerialReceivingDocument.getReceivingRecordType());
        oleSerialReceivingHistory.setReceiptStatus(receiptStatus);
        oleSerialReceivingHistory.setPublicDisplay(oleSerialReceivingDocument.isPublicDisplay());
        oleSerialReceivingHistory.setOperatorId(oleSerialReceivingDocument.getOperatorId());
        if(oleSerialReceivingDocument.isPublicDisplay()){
            oleSerialReceivingHistory.setPublicDisplayHistory(OLEConstants.DISPLAY_YES);
        }else{
            oleSerialReceivingHistory.setPublicDisplayHistory(OLEConstants.DISPLAY_NO);
        }
        oleSerialReceivingHistory.setSerialReceivingRecordId(oleSerialReceivingDocument.getSerialReceivingRecordId());
        oleSerialReceivingHistory.setDocumentNumber(oleSerialReceivingDocument.getDocumentNumber());
        Date date = new Date(System.currentTimeMillis());
        if(receiptStatus.equalsIgnoreCase(OLEConstants.RECEIVED)){
            oleSerialReceivingHistory.setReceiptDate(new Timestamp(date.getTime()));
        }else  if(receiptStatus.equalsIgnoreCase(OLEConstants.CLAIMED)){
            oleSerialReceivingHistory.setClaimDate(new Timestamp(date.getTime()));
            oleSerialReceivingHistory.setClaimNote(oleSerialReceivingDocument.getClaimIntervalInformation());
            oleSerialReceivingHistory.setClaimType(oleSerialReceivingDocument.getClaimType());
            if(oleSerialReceivingHistory.getClaimCount()!=null && !oleSerialReceivingHistory.getClaimCount().isEmpty()){
                Integer count = Integer.parseInt(oleSerialReceivingHistory.getClaimCount());
                count++;
                oleSerialReceivingHistory.setClaimCount(count.toString());
            }else {
                oleSerialReceivingHistory.setClaimCount("1");
            }
        }
        if(oleSerialReceivingDocument.isSpecialIssueFlag()){
            oleSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumerationCaptionLevel1());
        }else{
            updateEnumCaptionValues(oleSerialReceivingDocument,oleSerialReceivingHistory);
        }
        if(oleSerialReceivingHistoryList != null){
            oleSerialReceivingHistoryList.add(oleSerialReceivingHistory);
        }else{
            oleSerialReceivingHistoryList = new ArrayList<OLESerialReceivingHistory>();
            oleSerialReceivingHistoryList.add(oleSerialReceivingHistory);
        }
        setEnumerationAndChronologyValues(oleSerialReceivingHistory);
        if (oleSerialReceivingDocument.isCreateItem()) {
            Item item = setItemDetails(oleSerialReceivingHistory.getEnumerationCaption(),oleSerialReceivingHistory.getChronologyCaption());
            String oleItemXMLString = getItemOlemlRecordProcessor().toXML(item);
            org.kuali.ole.docstore.common.document.Item itemc = new ItemOleml();

            itemc.setContent(oleItemXMLString);
            itemc.setCategory(DocCategory.WORK.getCode());
            itemc.setFormat(OLEConstants.OLEML_FORMAT);
            itemc.setType(OLEConstants.ITEM_DOC_TYPE);
            try{
                itemc.setHolding(getDocstoreClientLocator().getDocstoreClient().retrieveHoldings(oleSerialReceivingDocument.getInstanceId()));
                getDocstoreClientLocator().getDocstoreClient().createItem(itemc);//holdingsId)

            }catch (Exception ex){
                ex.printStackTrace();
            }

            oleSerialReceivingDocument.setItemUUID(itemc.getId());
        }
        oleSerialReceivingDocument.setOleSerialReceivingHistoryList(oleSerialReceivingHistoryList);

    }
    public List<OLESerialReceivingHistory> sortById(List<OLESerialReceivingHistory> oleSerialReceivingHistoryList){
        if(oleSerialReceivingHistoryList!=null && oleSerialReceivingHistoryList.size()>0){
            Collections.sort(oleSerialReceivingHistoryList, new Comparator<OLESerialReceivingHistory>() {
                public int compare(OLESerialReceivingHistory obj1, OLESerialReceivingHistory obj2) {
                    if(obj1.getSerialReceivingRecordHistoryId()!=null && !obj1.getSerialReceivingRecordHistoryId().isEmpty()
                            && obj2.getSerialReceivingRecordHistoryId()!=null && !obj2.getSerialReceivingRecordHistoryId().isEmpty()){
                        return Integer.parseInt(obj1.getSerialReceivingRecordHistoryId()) > Integer.parseInt(obj2.getSerialReceivingRecordHistoryId()) ? 1 : -1;
                    }
                    return 0;
                }

            });
            Collections.reverse(oleSerialReceivingHistoryList);
        } else {
            return null;
        }
        return oleSerialReceivingHistoryList;
    }
    public List<OLESerialReceivingHistory> reSortById(List<OLESerialReceivingHistory> oleSerialReceivingHistoryList){
        if(oleSerialReceivingHistoryList!=null && oleSerialReceivingHistoryList.size()>0){
            Collections.sort(oleSerialReceivingHistoryList, new Comparator<OLESerialReceivingHistory>() {
                public int compare(OLESerialReceivingHistory obj1, OLESerialReceivingHistory obj2) {
                    if(obj1.getSerialReceivingRecordHistoryId()!=null && !obj1.getSerialReceivingRecordHistoryId().isEmpty()
                            && obj2.getSerialReceivingRecordHistoryId()!=null && !obj2.getSerialReceivingRecordHistoryId().isEmpty()){
                        return Integer.parseInt(obj1.getSerialReceivingRecordHistoryId()) < Integer.parseInt(obj2.getSerialReceivingRecordHistoryId()) ? 1 : -1;
                    }
                    return 0;
                }

            });
            Collections.reverse(oleSerialReceivingHistoryList);
        } else {
            return null;
        }
        return oleSerialReceivingHistoryList;
    }


    /**
     * This method will set values to Item Object and returns it to update or create Item at Docstore.
     *
     * @param enumeration
     * @param chronology
     * @return Item
     */
    private Item setItemDetails(String enumeration, String chronology){
        Item item = new Item();
        ItemType docstoreItemType = new ItemType();
        docstoreItemType.setCodeValue("");
        docstoreItemType.setFullValue("");
        item.setItemType(docstoreItemType);
        item.setEnumeration(enumeration);
        item.setChronology(chronology);
        return item;
    }

    public void validateVendorDetailsForSave(OLESerialReceivingDocument oleSerialReceivingDocument) {
        Map vendorAliasMap = new HashMap();
        vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_ALIAS_NAME, oleSerialReceivingDocument.getVendorAliasName());
        List<VendorAlias> vendorAliasList = (List<VendorAlias>)getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
        if(vendorAliasList != null && vendorAliasList.size() >0) {
            Map vendorNameMap = new HashMap();
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier());
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorAliasList.get(0).getVendorDetailAssignedIdentifier());
            List<VendorDetail> vendorDetailList = (List) businessObject.findMatching(VendorDetail.class, vendorNameMap);
            if(vendorDetailList != null && vendorDetailList.size() >0){
                if((vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier().toString() + "-" + vendorAliasList.get(0).getVendorDetailAssignedIdentifier().toString()).equalsIgnoreCase(oleSerialReceivingDocument.getVendorId())) {
                    LOG.debug("Allowed to save the Vendor Details");
                }
                else {
                    GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_SAME,OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_SAME);
                }
            }
        }
        else {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND,OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND);
        }
    }

    public void validateVendorDetailsForSelect(OLESerialReceivingDocument oleSerialReceivingDocument) {
        Map vendorAliasMap = new HashMap();
        vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_ALIAS_NAME, oleSerialReceivingDocument.getVendorAliasName());
        List<VendorAlias> vendorAliasList = (List<VendorAlias>)getLookupService().findCollectionBySearchHelper(VendorAlias.class, vendorAliasMap, true);
        if(vendorAliasList != null && vendorAliasList.size() >0) {
            Integer vendorHeaderGeneratedIdentifier = vendorAliasList.get(0).getVendorHeaderGeneratedIdentifier();
            Integer vendorDetailAssignedIdentifier = vendorAliasList.get(0).getVendorDetailAssignedIdentifier();
            Map vendorNameMap = new HashMap();
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
            List<VendorDetail> vendorDetailList = (List) businessObject.findMatching(VendorDetail.class, vendorNameMap);
            if(vendorDetailList != null && vendorDetailList.size() >0) {
                oleSerialReceivingDocument.setVendorName(vendorDetailList.get(0).getVendorName());
                oleSerialReceivingDocument.setVendorId(vendorDetailList.get(0).getVendorHeaderGeneratedIdentifier().toString()
                        + "-" + vendorDetailList.get(0).getVendorDetailAssignedIdentifier().toString());
            }
        }
        else {
            oleSerialReceivingDocument.setVendorId(null);
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND,OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ALIAS_NOT_FOUND);
        }
    }

    public void populateVendorNameFromVendorId(String vendorId,OLESerialReceivingDocument oleSerialReceivingDocument) {
        String[] vendorDetails = vendorId.split("-");
        if(vendorDetails.length>1){
            String vendorHeaderGeneratedIdentifier = vendorDetails[0];
            String vendorDetailAssignedIdentifier = vendorDetails[1];
            Map vendorNameMap = new HashMap();
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
            vendorNameMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
            List<VendorDetail> vendorDetailList = (List) businessObject.findMatching(VendorDetail.class, vendorNameMap);
            List<VendorAlias> vendorAliasList = (List) businessObject.findMatching(VendorAlias.class, vendorNameMap);
            if(vendorDetailList != null && vendorDetailList.size() >0) {
                oleSerialReceivingDocument.setVendorName(vendorDetailList.get(0).getVendorName());
                if(oleSerialReceivingDocument.getActionInterval()==null || oleSerialReceivingDocument.getActionInterval().isEmpty()){
                    oleSerialReceivingDocument.setActionInterval(vendorDetailList.get(0).getClaimInterval());
                }
            }
            if(vendorAliasList != null && vendorAliasList.size() >0) {
                oleSerialReceivingDocument.setVendorAliasName(vendorAliasList.get(0).getVendorAliasName());
            }
        }
    }

    public void populateVendorAliasNameFromVendorName(OLESerialReceivingDocument oleSerialReceivingDocument) {
        String vendorId = oleSerialReceivingDocument.getVendorId();
        if(vendorId != null && !vendorId.isEmpty()) {
            String[] vendorIds = vendorId.split("-");
            String vendorHeaderGeneratedIdentifier = vendorIds[0];
            String vendorDetailAssignedIdentifier = vendorIds[1];
            Map vendorAliasMap = new HashMap();
            vendorAliasMap.put(OLEConstants.VENDOR_HEADER_GENERATED_ID,vendorHeaderGeneratedIdentifier);
            vendorAliasMap.put(OLEConstants.VENDOR_DETAILED_ASSIGNED_ID,vendorDetailAssignedIdentifier);
            List<VendorAlias> vendorAliasList = (List) businessObject.findMatching(VendorAlias.class, vendorAliasMap);
            if(vendorAliasList != null && vendorAliasList.size() >0) {
                oleSerialReceivingDocument.setVendorAliasName(vendorAliasList.get(0).getVendorAliasName());
            }
            else {
                oleSerialReceivingDocument.setVendorAliasName(null);
            }
        }
        else {
            oleSerialReceivingDocument.setVendorAliasName(null);
        }
    }

    public void updateEnumValues(OLESerialReceivingDocument oleSerialReceivingDocument){
        String enumCaptionSeparator  = getParameter(OLEConstants.ENUMERATION_CAPTION);

        String enumCaption1= oleSerialReceivingDocument.getEnumCaption1()!=null ?
                oleSerialReceivingDocument.getEnumCaption1(): "" ;
        if(enumCaption1.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption1Length())){
            enumCaption1 = enumCaption1+" ";
        }
        String enumLevel1 = oleSerialReceivingDocument.getEnumLevel1()!=null?
                oleSerialReceivingDocument.getEnumLevel1(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel1(!enumCaption1.isEmpty() || !enumLevel1.isEmpty() ?enumCaption1 +enumCaptionSeparator+ enumLevel1:"");
        String enumCaption2= oleSerialReceivingDocument.getEnumCaption2()!=null ?
                oleSerialReceivingDocument.getEnumCaption2(): "" ;
        if(enumCaption2.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption2Length())){
            enumCaption2 = enumCaption2+" ";
        }
        String enumLevel2 = oleSerialReceivingDocument.getEnumLevel2()!=null?
                oleSerialReceivingDocument.getEnumLevel2(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel2(!enumCaption2.isEmpty() || !enumLevel2.isEmpty() ?enumCaption2 +enumCaptionSeparator+ enumLevel2:"");
        String enumCaption3= oleSerialReceivingDocument.getEnumCaption3()!=null ?
                oleSerialReceivingDocument.getEnumCaption3(): "" ;
        if(enumCaption3.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption3Length())){
            enumCaption3 = enumCaption3+" ";
        }
        String enumLevel3 = oleSerialReceivingDocument.getEnumLevel3()!=null?
                oleSerialReceivingDocument.getEnumLevel3(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel3(!enumCaption3.isEmpty() || !enumLevel3.isEmpty() ?enumCaption3 +enumCaptionSeparator+ enumLevel3:"");
        String enumCaption4= oleSerialReceivingDocument.getEnumCaption4()!=null ?
                oleSerialReceivingDocument.getEnumCaption4(): "" ;
        if(enumCaption4.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption4Length())){
            enumCaption4 = enumCaption4+" ";
        }
        String enumLevel4 = oleSerialReceivingDocument.getEnumLevel4()!=null?
                oleSerialReceivingDocument.getEnumLevel4(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel4(!enumCaption4.isEmpty() || !enumLevel4.isEmpty() ?enumCaption4 +enumCaptionSeparator+ enumLevel4:"");
        String enumCaption5= oleSerialReceivingDocument.getEnumCaption5()!=null ?
                oleSerialReceivingDocument.getEnumCaption5(): "" ;
        if(enumCaption5.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption5Length())){
            enumCaption5 = enumCaption5+" ";
        }
        String enumLevel5 = oleSerialReceivingDocument.getEnumLevel5()!=null?
                oleSerialReceivingDocument.getEnumLevel5(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel5(!enumCaption5.isEmpty() || !enumLevel5.isEmpty() ?enumCaption5 +enumCaptionSeparator+ enumLevel5:"");
        String enumCaption6= oleSerialReceivingDocument.getEnumCaption6()!=null ?
                oleSerialReceivingDocument.getEnumCaption6(): "" ;
        if(enumCaption6.length() < Integer.parseInt(oleSerialReceivingDocument.getEnumCaption6Length())){
            enumCaption6 = enumCaption6+" ";
        }
        String enumLevel6 = oleSerialReceivingDocument.getEnumLevel6()!=null?
                oleSerialReceivingDocument.getEnumLevel6(): "";
        oleSerialReceivingDocument.setEnumerationCaptionLevel6(!enumCaption6.isEmpty() || !enumLevel6.isEmpty() ?enumCaption6 +enumCaptionSeparator+ enumLevel6:"");

        String chronCaptionSeparator  = getParameter(OLEConstants.CHRONOLOGY_CAPTION);

        String chronCaption1= oleSerialReceivingDocument.getChronCaption1()!=null ?
                oleSerialReceivingDocument.getChronCaption1(): "" ;
        if(chronCaption1.length() < Integer.parseInt(oleSerialReceivingDocument.getChronCaption1Length())){
            chronCaption1 = chronCaption1+" ";
        }
        String chronLevel1 = oleSerialReceivingDocument.getChronLevel1()!=null?
                oleSerialReceivingDocument.getChronLevel1(): "";
        oleSerialReceivingDocument.setChronologyCaptionLevel1(!chronCaption1.isEmpty() || !chronLevel1.isEmpty() ?chronCaption1 +chronCaptionSeparator+ chronLevel1:"");
        String chronCaption2= oleSerialReceivingDocument.getChronCaption2()!=null ?
                oleSerialReceivingDocument.getChronCaption2(): "" ;
        if(chronCaption2.length() < Integer.parseInt(oleSerialReceivingDocument.getChronCaption2Length())){
            chronCaption2 = chronCaption2+" ";
        }
        String chronLevel2 = oleSerialReceivingDocument.getChronLevel2()!=null?
                oleSerialReceivingDocument.getChronLevel2(): "";
        oleSerialReceivingDocument.setChronologyCaptionLevel2(!chronCaption2.isEmpty() || !chronLevel2.isEmpty() ?chronCaption2 +chronCaptionSeparator+ chronLevel2:"");
        String chronCaption3= oleSerialReceivingDocument.getChronCaption3()!=null ?
                oleSerialReceivingDocument.getChronCaption3(): "" ;
        if(chronCaption3.length() < Integer.parseInt(oleSerialReceivingDocument.getChronCaption3Length())){
            chronCaption3 = chronCaption3+" ";
        }
        String chronLevel3 = oleSerialReceivingDocument.getChronLevel3()!=null?
                oleSerialReceivingDocument.getChronLevel3(): "";
        oleSerialReceivingDocument.setChronologyCaptionLevel3(!chronCaption3.isEmpty() || !chronLevel3.isEmpty() ?chronCaption3 +chronCaptionSeparator+ chronLevel3:"");
        String chronCaption4= oleSerialReceivingDocument.getChronCaption4()!=null ?
                oleSerialReceivingDocument.getChronCaption4(): "" ;
        if(chronCaption4.length() < Integer.parseInt(oleSerialReceivingDocument.getChronCaption4Length())){
            chronCaption4 = chronCaption4+" ";
        }
        String chronLevel4 = oleSerialReceivingDocument.getChronLevel4()!=null?
                oleSerialReceivingDocument.getChronLevel4(): "";
        oleSerialReceivingDocument.setChronologyCaptionLevel4(!chronCaption4.isEmpty() || !chronLevel4.isEmpty() ?chronCaption4 +chronCaptionSeparator+ chronLevel4:"");
    }
    public void updateEnumCaptionValues(OLESerialReceivingDocument oleSerialReceivingDocument,OLESerialReceivingHistory oleSerialReceivingHistory){

        String enumCaptionSeparator  = getParameter(OLEConstants.ENUMERATION_CAPTION);

        String [] enumCaptions;
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel1()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel1().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption1(enumCaptions.length>0 && !enumCaptions[0].isEmpty()?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION1));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel1(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel1(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel1().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption1().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption1().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumLevel1());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumCaption1() + oleSerialReceivingDocument.getEnumLevel1());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption1(getParameter(OLEConstants.ENUM_CAPTION1));
            oleSerialReceivingDocument.setEnumLevel1("");
        }
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel2()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel2().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption2(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION2));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel2(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel2(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel2().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption2().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption2().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel2(oleSerialReceivingDocument.getEnumLevel2());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel2(oleSerialReceivingDocument.getEnumCaption2() + oleSerialReceivingDocument.getEnumLevel2());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption2(getParameter(OLEConstants.ENUM_CAPTION2));
            oleSerialReceivingDocument.setEnumLevel2("");
        }
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel3()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel3().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption3(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION3));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel3(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel3(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel3().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption3().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption3().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel3(oleSerialReceivingDocument.getEnumLevel3());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel3(oleSerialReceivingDocument.getEnumCaption3() + oleSerialReceivingDocument.getEnumLevel3());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption3(getParameter(OLEConstants.ENUM_CAPTION3));
            oleSerialReceivingDocument.setEnumLevel3("");
        }
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel4()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel4().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption4(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION4));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel4(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel4(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel4().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption4().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption4().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel4(oleSerialReceivingDocument.getEnumLevel4());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel4(oleSerialReceivingDocument.getEnumCaption4() + oleSerialReceivingDocument.getEnumLevel4());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption4(getParameter(OLEConstants.ENUM_CAPTION4));
            oleSerialReceivingDocument.setEnumLevel4("");
        }
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel5()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel5().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption5(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION5));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel5(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel5(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel5().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption5().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption5().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel5(oleSerialReceivingDocument.getEnumLevel5());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel5(oleSerialReceivingDocument.getEnumCaption5() + oleSerialReceivingDocument.getEnumLevel5());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption5(getParameter(OLEConstants.ENUM_CAPTION5));
            oleSerialReceivingDocument.setEnumLevel5("");
        }
        if(oleSerialReceivingDocument.getEnumerationCaptionLevel6()!=null){
            enumCaptions = oleSerialReceivingDocument.getEnumerationCaptionLevel6().split(enumCaptionSeparator);
            oleSerialReceivingDocument.setEnumCaption6(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.ENUM_CAPTION6));
            if(enumCaptions.length>2){
                String enumLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? enumCaptionSeparator : "";
                    enumLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setEnumLevel6(enumLevel);
            }else{
                oleSerialReceivingDocument.setEnumLevel6(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getEnumLevel6().isEmpty()){
                if(oleSerialReceivingDocument.getEnumCaption6().trim().startsWith("[") && oleSerialReceivingDocument.getEnumCaption6().trim().endsWith("]")) {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel6(oleSerialReceivingDocument.getEnumLevel6());
                }
                else {
                    oleSerialReceivingHistory.setEnumerationCaptionLevel6(oleSerialReceivingDocument.getEnumCaption6() + oleSerialReceivingDocument.getEnumLevel6());
                }
            }
        }else {
            oleSerialReceivingDocument.setEnumCaption6(getParameter(OLEConstants.ENUM_CAPTION6));
            oleSerialReceivingDocument.setEnumLevel6("");
        }
        String chronCaptionSeparator  = getParameter(OLEConstants.CHRONOLOGY_CAPTION);

        if(oleSerialReceivingDocument.getChronologyCaptionLevel1()!=null){
            enumCaptions = oleSerialReceivingDocument.getChronologyCaptionLevel1().split(chronCaptionSeparator);
            oleSerialReceivingDocument.setChronCaption1(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.CHRON_CAPTION1));
            if(enumCaptions.length>2){
                String chronLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? chronCaptionSeparator : "";
                    chronLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setChronLevel1(chronLevel);
            }else{
                oleSerialReceivingDocument.setChronLevel1(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getChronLevel1().isEmpty()){
                if(oleSerialReceivingDocument.getChronCaption1().trim().startsWith("[") && oleSerialReceivingDocument.getChronCaption1().trim().endsWith("]")){
                    oleSerialReceivingHistory.setChronologyCaptionLevel1(oleSerialReceivingDocument.getChronLevel1());
                }
                else {
                    oleSerialReceivingHistory.setChronologyCaptionLevel1(oleSerialReceivingDocument.getChronCaption1() + oleSerialReceivingDocument.getChronLevel1());
                }
            }
        } else {
            oleSerialReceivingDocument.setChronCaption1(getParameter(OLEConstants.CHRON_CAPTION1));
            oleSerialReceivingDocument.setChronLevel1("");
        }
        if(oleSerialReceivingDocument.getChronologyCaptionLevel2()!=null){
            enumCaptions = oleSerialReceivingDocument.getChronologyCaptionLevel2().split(chronCaptionSeparator);
            oleSerialReceivingDocument.setChronCaption2(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.CHRON_CAPTION2));
            if(enumCaptions.length>2){
                String chronLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? chronCaptionSeparator : "";
                    chronLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setChronLevel2(chronLevel);
            }else{
                oleSerialReceivingDocument.setChronLevel2(enumCaptions.length>1?enumCaptions[1]:"");
            }

            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getChronLevel2().isEmpty()){
                if(oleSerialReceivingDocument.getChronCaption2().trim().startsWith("[") && oleSerialReceivingDocument.getChronCaption2().trim().endsWith("]")){
                    oleSerialReceivingHistory.setChronologyCaptionLevel2(oleSerialReceivingDocument.getChronLevel2());
                }
                else {
                    oleSerialReceivingHistory.setChronologyCaptionLevel2(oleSerialReceivingDocument.getChronCaption2() + oleSerialReceivingDocument.getChronLevel2());
                }
            }
        }else {
            oleSerialReceivingDocument.setChronCaption2(getParameter(OLEConstants.CHRON_CAPTION2));
            oleSerialReceivingDocument.setChronLevel2("");
        }
        if(oleSerialReceivingDocument.getChronologyCaptionLevel3()!=null){
            enumCaptions = oleSerialReceivingDocument.getChronologyCaptionLevel3().split(chronCaptionSeparator);
            oleSerialReceivingDocument.setChronCaption3(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.CHRON_CAPTION3));
            if(enumCaptions.length>2){
                String chronLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? chronCaptionSeparator : "";
                    chronLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setChronLevel3(chronLevel);
            }else{
                oleSerialReceivingDocument.setChronLevel3(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getChronLevel3().isEmpty()){
                if(oleSerialReceivingDocument.getChronCaption3().trim().startsWith("[") && oleSerialReceivingDocument.getChronCaption3().trim().endsWith("]")){
                    oleSerialReceivingHistory.setChronologyCaptionLevel3(oleSerialReceivingDocument.getChronLevel3());
                }
                else {
                    oleSerialReceivingHistory.setChronologyCaptionLevel3(oleSerialReceivingDocument.getChronCaption3() + oleSerialReceivingDocument.getChronLevel3());
                }
            }
        }else {
            oleSerialReceivingDocument.setChronCaption3(getParameter(OLEConstants.CHRON_CAPTION3));
            oleSerialReceivingDocument.setChronLevel3("");
        }
        if(oleSerialReceivingDocument.getChronologyCaptionLevel4()!=null){
            enumCaptions = oleSerialReceivingDocument.getChronologyCaptionLevel4().split(chronCaptionSeparator);
            oleSerialReceivingDocument.setChronCaption4(enumCaptions.length>0 && !enumCaptions[0].isEmpty() ?enumCaptions[0]:getParameter(OLEConstants.CHRON_CAPTION4));
            if(enumCaptions.length>2){
                String chronLevel = "";
                for(int i=1;i<enumCaptions.length;i++){
                    String separator = i<enumCaptions.length-1 ? chronCaptionSeparator : "";
                    chronLevel += enumCaptions[i] + separator;
                }
                oleSerialReceivingDocument.setChronLevel4(chronLevel);
            }else{
                oleSerialReceivingDocument.setChronLevel4(enumCaptions.length>1?enumCaptions[1]:"");
            }
            if(oleSerialReceivingHistory!=null && !oleSerialReceivingDocument.getChronLevel4().isEmpty()){
                if(oleSerialReceivingDocument.getChronCaption4().trim().startsWith("[") && oleSerialReceivingDocument.getChronCaption4().trim().endsWith("]")){
                    oleSerialReceivingHistory.setChronologyCaptionLevel4(oleSerialReceivingDocument.getChronLevel4());
                }
                else {
                    oleSerialReceivingHistory.setChronologyCaptionLevel4(oleSerialReceivingDocument.getChronCaption4() + oleSerialReceivingDocument.getChronLevel4());
                }
            }
        }else {
            oleSerialReceivingDocument.setChronCaption4(getParameter(OLEConstants.CHRON_CAPTION4));
            oleSerialReceivingDocument.setChronLevel4("");
        }
    }
    public String getParameter(String name){
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.SELECT_NMSPC, OLEConstants.SELECT_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        return parameter!=null?parameter.getValue():null;
    }

    @Override
    public void createOrUpdateReceivingRecordType(OLESerialReceivingDocument oleSerialReceivingDocument) {
        List<OLESerialReceivingType> oleSerialReceivingTypeList = oleSerialReceivingDocument.getOleSerialReceivingTypes()!=null?oleSerialReceivingDocument.getOleSerialReceivingTypes():
                new ArrayList<OLESerialReceivingType>();
        boolean isReceivingTypeExists = false;
        for(OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingTypeList){
            if(oleSerialReceivingType.getReceivingRecordType().equalsIgnoreCase(oleSerialReceivingDocument.getReceivingRecordType())){
                isReceivingTypeExists = true;
                oleSerialReceivingType.setReceivingRecordType(oleSerialReceivingDocument.getReceivingRecordType());
                oleSerialReceivingType.setActionDate(oleSerialReceivingDocument.getActionDate());
                oleSerialReceivingType.setActionInterval(oleSerialReceivingDocument.getActionInterval());
                oleSerialReceivingType.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumerationCaptionLevel1());
                oleSerialReceivingType.setEnumerationCaptionLevel2(oleSerialReceivingDocument.getEnumerationCaptionLevel2());
                oleSerialReceivingType.setEnumerationCaptionLevel3(oleSerialReceivingDocument.getEnumerationCaptionLevel3());
                oleSerialReceivingType.setEnumerationCaptionLevel4(oleSerialReceivingDocument.getEnumerationCaptionLevel4());
                oleSerialReceivingType.setEnumerationCaptionLevel5(oleSerialReceivingDocument.getEnumerationCaptionLevel5());
                oleSerialReceivingType.setEnumerationCaptionLevel6(oleSerialReceivingDocument.getEnumerationCaptionLevel6());
                oleSerialReceivingType.setChronologyCaptionLevel1(oleSerialReceivingDocument.getChronologyCaptionLevel1());
                oleSerialReceivingType.setChronologyCaptionLevel2(oleSerialReceivingDocument.getChronologyCaptionLevel2());
                oleSerialReceivingType.setChronologyCaptionLevel3(oleSerialReceivingDocument.getChronologyCaptionLevel3());
                oleSerialReceivingType.setChronologyCaptionLevel4(oleSerialReceivingDocument.getChronologyCaptionLevel4());
            }
        }
        if(oleSerialReceivingTypeList.size()==0 || !isReceivingTypeExists) {
            OLESerialReceivingType oleSerialReceivingType =  new OLESerialReceivingType();
            oleSerialReceivingType.setReceivingRecordType(oleSerialReceivingDocument.getReceivingRecordType());
            oleSerialReceivingType.setActionDate(oleSerialReceivingDocument.getActionDate());
            oleSerialReceivingType.setActionInterval(oleSerialReceivingDocument.getActionInterval());
            oleSerialReceivingType.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumerationCaptionLevel1());
            oleSerialReceivingType.setEnumerationCaptionLevel2(oleSerialReceivingDocument.getEnumerationCaptionLevel2());
            oleSerialReceivingType.setEnumerationCaptionLevel3(oleSerialReceivingDocument.getEnumerationCaptionLevel3());
            oleSerialReceivingType.setEnumerationCaptionLevel4(oleSerialReceivingDocument.getEnumerationCaptionLevel4());
            oleSerialReceivingType.setEnumerationCaptionLevel5(oleSerialReceivingDocument.getEnumerationCaptionLevel5());
            oleSerialReceivingType.setEnumerationCaptionLevel6(oleSerialReceivingDocument.getEnumerationCaptionLevel6());
            oleSerialReceivingType.setChronologyCaptionLevel1(oleSerialReceivingDocument.getChronologyCaptionLevel1());
            oleSerialReceivingType.setChronologyCaptionLevel2(oleSerialReceivingDocument.getChronologyCaptionLevel2());
            oleSerialReceivingType.setChronologyCaptionLevel3(oleSerialReceivingDocument.getChronologyCaptionLevel3());
            oleSerialReceivingType.setChronologyCaptionLevel4(oleSerialReceivingDocument.getChronologyCaptionLevel4());
            if(oleSerialReceivingDocument.getOleSerialReceivingTypes()==null){
                oleSerialReceivingDocument.setOleSerialReceivingTypes(new ArrayList<OLESerialReceivingType>());
            }
            oleSerialReceivingDocument.getOleSerialReceivingTypes().add(oleSerialReceivingType);
        }
    }

    @Override
    public void readReceivingRecordType(OLESerialReceivingDocument oleSerialReceivingDocument) {
        List<OLESerialReceivingType> oleSerialReceivingTypeList = oleSerialReceivingDocument.getOleSerialReceivingTypes()!=null?oleSerialReceivingDocument.getOleSerialReceivingTypes():
                new ArrayList<OLESerialReceivingType>();
        boolean isReceivingTypeExists = false;
        for(OLESerialReceivingType oleSerialReceivingType : oleSerialReceivingTypeList){
            if(oleSerialReceivingType.getReceivingRecordType().equalsIgnoreCase(oleSerialReceivingDocument.getReceivingRecordType())){
                isReceivingTypeExists = true;
                oleSerialReceivingDocument.setActionDate(oleSerialReceivingType.getActionDate());
                oleSerialReceivingDocument.setActionInterval(oleSerialReceivingType.getActionInterval());
                oleSerialReceivingDocument.setEnumerationCaptionLevel1(oleSerialReceivingType.getEnumerationCaptionLevel1());
                oleSerialReceivingDocument.setEnumerationCaptionLevel2(oleSerialReceivingType.getEnumerationCaptionLevel2());
                oleSerialReceivingDocument.setEnumerationCaptionLevel3(oleSerialReceivingType.getEnumerationCaptionLevel3());
                oleSerialReceivingDocument.setEnumerationCaptionLevel4(oleSerialReceivingType.getEnumerationCaptionLevel4());
                oleSerialReceivingDocument.setEnumerationCaptionLevel5(oleSerialReceivingType.getEnumerationCaptionLevel5());
                oleSerialReceivingDocument.setEnumerationCaptionLevel6(oleSerialReceivingType.getEnumerationCaptionLevel6());
                oleSerialReceivingDocument.setChronologyCaptionLevel1(oleSerialReceivingType.getChronologyCaptionLevel1());
                oleSerialReceivingDocument.setChronologyCaptionLevel2(oleSerialReceivingType.getChronologyCaptionLevel2());
                oleSerialReceivingDocument.setChronologyCaptionLevel3(oleSerialReceivingType.getChronologyCaptionLevel3());
                oleSerialReceivingDocument.setChronologyCaptionLevel4(oleSerialReceivingType.getChronologyCaptionLevel4());
            }
        }
        if(!isReceivingTypeExists){
            oleSerialReceivingDocument.setActionDate(null);
            oleSerialReceivingDocument.setActionInterval("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel1("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel2("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel3("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel4("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel5("");
            oleSerialReceivingDocument.setEnumerationCaptionLevel6("");
            oleSerialReceivingDocument.setChronologyCaptionLevel1("");
            oleSerialReceivingDocument.setChronologyCaptionLevel2("");
            oleSerialReceivingDocument.setChronologyCaptionLevel3("");
            oleSerialReceivingDocument.setChronologyCaptionLevel4("");
        }
    }

    @Override
    public void validateSerialReceivingDocument(OLESerialReceivingDocument oleSerialReceivingDocument) {
        if((oleSerialReceivingDocument.getEnumLevel1()==null || oleSerialReceivingDocument.getEnumLevel1().isEmpty()) &&
                (oleSerialReceivingDocument.getChronLevel1()==null || oleSerialReceivingDocument.getChronLevel1().isEmpty()) ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM1_OR_CHRON1_REQUIRED,OLEKeyConstants.SERIAL_RECEIVE_ENUM1_OR_CHRON1_REQUIRED);
        }
        if((oleSerialReceivingDocument.getEnumCaption1()==null || oleSerialReceivingDocument.getEnumCaption1().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel1().isEmpty()) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L1"});
        }
        if((oleSerialReceivingDocument.getEnumCaption2()==null || oleSerialReceivingDocument.getEnumCaption2().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel2().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L2"});
        }
        if((oleSerialReceivingDocument.getEnumCaption3()==null || oleSerialReceivingDocument.getEnumCaption3().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel3().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L3"});
        }
        if((oleSerialReceivingDocument.getEnumCaption4()==null || oleSerialReceivingDocument.getEnumCaption4().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel4().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L4"});
        }
        if((oleSerialReceivingDocument.getEnumCaption5()==null || oleSerialReceivingDocument.getEnumCaption5().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel5().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L5"});
        }
        if((oleSerialReceivingDocument.getEnumCaption6()==null || oleSerialReceivingDocument.getEnumCaption6().isEmpty()) &&
                !oleSerialReceivingDocument.getEnumLevel6().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_ENUM_WITHOUT_CAPTION,new String[]{"L6"});
        }
        if((oleSerialReceivingDocument.getChronCaption1()==null || oleSerialReceivingDocument.getChronCaption1().isEmpty()) &&
                !oleSerialReceivingDocument.getChronLevel1().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,new String[]{"L1"});
        }
        if((oleSerialReceivingDocument.getChronCaption2()==null || oleSerialReceivingDocument.getChronCaption2().isEmpty()) &&
                !oleSerialReceivingDocument.getChronLevel2().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,new String[]{"L2"});
        }
        if((oleSerialReceivingDocument.getChronCaption3()==null || oleSerialReceivingDocument.getChronCaption3().isEmpty()) &&
                !oleSerialReceivingDocument.getChronLevel3().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,new String[]{"L3"});
        }
        if((oleSerialReceivingDocument.getChronCaption4()==null || oleSerialReceivingDocument.getChronCaption4().isEmpty()) &&
                !oleSerialReceivingDocument.getChronLevel4().isEmpty() ) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,OLEKeyConstants.SERIAL_RECEIVE_CHRON_WITHOUT_CAPTION,new String[]{"L4"});
        }
    }

    public void updatePOVendorDetail(OLESerialReceivingDocument oleSerialReceivingDocument){
        BusinessObjectService businessObjectService= KRADServiceLocator.getBusinessObjectService();
        Map parentCriterial = new HashMap();
        parentCriterial.put(OLEConstants.BIB_ID,oleSerialReceivingDocument.getBibId());
        parentCriterial.put(OLEConstants.INSTANCE_ID,oleSerialReceivingDocument.getInstanceId());
        List<OleCopy> copyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,parentCriterial);
        Map<String,String> poMap =  new ConcurrentHashMap<String,String>();
        for(OleCopy oleCopy : copyList){
            poMap.put(oleCopy.getPoDocNum(),oleCopy.getPoDocNum());
        }
        Iterator<Map.Entry<String,String>> entries = poMap.entrySet().iterator();
        List<OLESerialRelatedPODocument> oleSerialRelatedPODocuments = new ArrayList<>();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String poDocNum = entry.getValue();
            parentCriterial.clear();
            parentCriterial.put(OLEConstants.DOC_NUM,poDocNum);
            OlePurchaseOrderDocument olePurchaseOrderDocument=  businessObjectService.findByPrimaryKey(OlePurchaseOrderDocument.class,parentCriterial);
            if(!olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching()){
                poMap.remove(olePurchaseOrderDocument.getDocumentNumber());
            }
            if(olePurchaseOrderDocument!=null && olePurchaseOrderDocument.getPurchaseOrderCurrentIndicatorForSearching())    {
                String vendorHeaderGeneratedIdentifier = olePurchaseOrderDocument.getVendorHeaderGeneratedIdentifier()!=null ? olePurchaseOrderDocument.getVendorHeaderGeneratedIdentifier().toString() : "";
                String vendorDetailAssignedIdentifier = olePurchaseOrderDocument.getVendorDetailAssignedIdentifier()!=null ? olePurchaseOrderDocument.getVendorDetailAssignedIdentifier().toString() : "";
                Map vendorAliasMap = new HashMap();
                vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_HEADER_IDENTIFIER,vendorHeaderGeneratedIdentifier);
                vendorAliasMap.put(org.kuali.ole.sys.OLEConstants.VENDOR_DETAIL_IDENTIFIER,vendorDetailAssignedIdentifier);
                List<VendorAlias> vendorAliasList = (List) businessObject.findMatching(VendorAlias.class, vendorAliasMap);
                List<VendorDetail> vendorDetailList = (List) businessObject.findMatching(VendorDetail.class, vendorAliasMap);
                if(poMap.size()==1){
                    oleSerialReceivingDocument.setPoId(olePurchaseOrderDocument.getPurapDocumentIdentifier()!=null ? olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : "");
                    oleSerialReceivingDocument.setPoIdLink(oleSerialReceivingDocument.formPoIdLinkUsingPoDocumentNumber(olePurchaseOrderDocument.getDocumentNumber()));
                    oleSerialReceivingDocument.setVendorId(vendorHeaderGeneratedIdentifier + "-" + vendorDetailAssignedIdentifier);
                    if(vendorAliasList != null && vendorAliasList.size() >0) {
                        oleSerialReceivingDocument.setVendorAliasName(vendorAliasList.get(0).getVendorAliasName());
                    }
                    if(vendorDetailList != null && vendorDetailList.size() >0) {
                        oleSerialReceivingDocument.setVendorName(vendorDetailList.get(0).getVendorName());
                        if(oleSerialReceivingDocument.getActionInterval()==null || oleSerialReceivingDocument.getActionInterval().isEmpty()){
                            oleSerialReceivingDocument.setActionInterval(vendorDetailList.get(0).getClaimInterval());
                        }
                    }
                } else {
                    oleSerialReceivingDocument.setLinkPO(true);
                    OLESerialRelatedPODocument oleSerialRelatedPODocument = new OLESerialRelatedPODocument();
                    oleSerialRelatedPODocument.setPoId(olePurchaseOrderDocument.getPurapDocumentIdentifier() != null ? olePurchaseOrderDocument.getPurapDocumentIdentifier().toString() : "");
                    oleSerialRelatedPODocument.setPoIdLink(olePurchaseOrderDocument.getDocumentNumber());
                    oleSerialRelatedPODocument.setVendorId(vendorHeaderGeneratedIdentifier + "-" + vendorDetailAssignedIdentifier);
                    if(vendorAliasList != null && vendorAliasList.size() >0) {
                        oleSerialRelatedPODocument.setVendorAliasName(vendorAliasList.get(0).getVendorAliasName());
                    }
                    if(vendorDetailList != null && vendorDetailList.size() >0) {
                        oleSerialRelatedPODocument.setVendorName(vendorDetailList.get(0).getVendorName());
                        if(oleSerialReceivingDocument.getActionInterval()==null || oleSerialReceivingDocument.getActionInterval().isEmpty()){
                            oleSerialRelatedPODocument.setActionInterval(vendorDetailList.get(0).getClaimInterval());
                        }
                    }
                    oleSerialRelatedPODocuments.add(oleSerialRelatedPODocument) ;
                }
            }
        }
        if(oleSerialRelatedPODocuments.size()>1){
            oleSerialReceivingDocument.setOleSerialRelatedPODocuments(oleSerialRelatedPODocuments);
        }else if (oleSerialRelatedPODocuments.size()==1){
            oleSerialReceivingDocument.setLinkPO(false);
            OLESerialRelatedPODocument oleSerialRelatedPODocument = oleSerialRelatedPODocuments.get(0);
            oleSerialReceivingDocument.setPoId(oleSerialRelatedPODocument.getPoId());
            oleSerialReceivingDocument.setPoIdLink(oleSerialRelatedPODocument.getPoIdLink());
            oleSerialReceivingDocument.setVendorId(oleSerialRelatedPODocument.getVendorId());
            oleSerialReceivingDocument.setVendorAliasName(oleSerialRelatedPODocument.getVendorAliasName());
            oleSerialReceivingDocument.setVendorName(oleSerialRelatedPODocument.getVendorName());
            oleSerialReceivingDocument.setActionInterval(oleSerialRelatedPODocument.getActionInterval());
        }
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
    public void updateSerialIdInCopy(OLESerialReceivingDocument oleSerialReceivingDocument){
        BusinessObjectService businessObjectService= KRADServiceLocator.getBusinessObjectService();
        Map parentCriterial = new HashMap();
        parentCriterial.put(OLEConstants.BIB_ID,oleSerialReceivingDocument.getBibId());
        parentCriterial.put(OLEConstants.INSTANCE_ID,oleSerialReceivingDocument.getInstanceId());

        Map oldInstanceMap = new HashMap();
        oldInstanceMap.put(OLEConstants.BIB_ID,oleSerialReceivingDocument.getBibId());
        oldInstanceMap.put(OLEConstants.INSTANCE_ID,oleSerialReceivingDocument.getTempInstanceId());


        if(oleSerialReceivingDocument.getTempInstanceId().equalsIgnoreCase(oleSerialReceivingDocument.getInstanceId())){
            List<OleCopy> copyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,parentCriterial);
            for(OleCopy oleCopy :copyList){
                oleCopy.setSerialReceivingIdentifier(oleSerialReceivingDocument.getSerialReceivingRecordId());
                businessObjectService.save(oleCopy);
            }
        } else {
            List<OleCopy> oldCopyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,oldInstanceMap);
            for(OleCopy oleCopy :oldCopyList){
                oleCopy.setSerialReceivingIdentifier(null);
                businessObjectService.save(oleCopy);
            }
            List<OleCopy> newCopyList = (List<OleCopy>) businessObjectService.findMatching(OleCopy.class,parentCriterial);
            for(OleCopy oleCopy :newCopyList){
                oleCopy.setSerialReceivingIdentifier(oleSerialReceivingDocument.getSerialReceivingRecordId());
                businessObjectService.save(oleCopy);
            }
        }
    }

    public void listOutHistoryBasedOnReceivingRecord(OLESerialReceivingDocument oleSerialReceivingDocument){
        List<OLESerialReceivingHistory> oleSerialReceivingHistoryList =  oleSerialReceivingDocument.getOleSerialReceivingHistoryList()!=null?
                oleSerialReceivingDocument.getOleSerialReceivingHistoryList():new ArrayList<OLESerialReceivingHistory>();
        List<OLESerialReceivingHistory> mainSerialReceivingHistoryList = new ArrayList<>();
        List<OLESerialReceivingHistory> supplementSerialReceivingHistoryList = new ArrayList<>();
        List<OLESerialReceivingHistory> indexSerialReceivingHistoryList = new ArrayList<>();
        for(OLESerialReceivingHistory oleSerialReceivingHistory : oleSerialReceivingHistoryList){
            if(oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase(OLEConstants.MAIN_RCV_REC_TYP)){
                mainSerialReceivingHistoryList.add(oleSerialReceivingHistory);
            }else if(oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase(OLEConstants.SUPPLEMENT_RCV_REC_TYP)){
                supplementSerialReceivingHistoryList.add(oleSerialReceivingHistory);
            }else if(oleSerialReceivingHistory.getReceivingRecordType().equalsIgnoreCase(OLEConstants.INDEX_RCV_REC_TYP)){
                indexSerialReceivingHistoryList.add(oleSerialReceivingHistory);
            }
        }
        String searchLimit;
        if((mainSerialReceivingHistoryList.size()>0 && supplementSerialReceivingHistoryList.size()>0) ||
                (mainSerialReceivingHistoryList.size()>0 && indexSerialReceivingHistoryList.size()>0) ||
                (supplementSerialReceivingHistoryList.size()>0 && indexSerialReceivingHistoryList.size()>0)){
            searchLimit = getParameter(OLEConstants.SERIAL_MULTI_SEC_LIMIT);
            oleSerialReceivingDocument.setSearchLimit(searchLimit!=null?searchLimit:"0");
        }else {
            searchLimit = getParameter(OLEConstants.SERIAL_SINGLE_SEC_LIMIT);
            oleSerialReceivingDocument.setSearchLimit(searchLimit!=null?searchLimit:"0");
        }
        oleSerialReceivingDocument.setMainSerialReceivingHistoryList(mainSerialReceivingHistoryList);
        oleSerialReceivingDocument.setSupplementSerialReceivingHistoryList(supplementSerialReceivingHistoryList);
        oleSerialReceivingDocument.setIndexSerialReceivingHistoryList(indexSerialReceivingHistoryList);
    }

    public void disapproveCreateNewWithExisting(OLESerialReceivingDocument newSerialReceivingDocument,OLESerialReceivingDocument oldSerialReceivingDocument){
        newSerialReceivingDocument.setTitle(oldSerialReceivingDocument.getTitle());
        newSerialReceivingDocument.setActionDate(oldSerialReceivingDocument.getActionDate());
        newSerialReceivingDocument.setActionInterval(oldSerialReceivingDocument.getActionInterval());
        newSerialReceivingDocument.setBibId(oldSerialReceivingDocument.getBibId());
        newSerialReceivingDocument.setBoundLocation(oldSerialReceivingDocument.getBoundLocation());
        newSerialReceivingDocument.setCallNumber(oldSerialReceivingDocument.getCallNumber());
        List<OLESerialReceivingType> oleSerialReceivingTypeList = new ArrayList<OLESerialReceivingType>();
        for(OLESerialReceivingType oleSerialReceivingType : oldSerialReceivingDocument.getOleSerialReceivingTypes()){
            OLESerialReceivingType newSerialReceivingType = new OLESerialReceivingType();
            newSerialReceivingType.setSerialReceivingRecordId(oldSerialReceivingDocument.getSerialReceivingRecordId());
            newSerialReceivingType.setReceivingRecordType(oleSerialReceivingType.getReceivingRecordType());
            newSerialReceivingType.setActionDate(oleSerialReceivingType.getActionDate());
            newSerialReceivingType.setActionInterval(oleSerialReceivingType.getActionInterval());
            newSerialReceivingType.setChronologyCaptionLevel1(oleSerialReceivingType.getChronologyCaptionLevel1());
            newSerialReceivingType.setChronologyCaptionLevel2(oleSerialReceivingType.getChronologyCaptionLevel2());
            newSerialReceivingType.setChronologyCaptionLevel3(oleSerialReceivingType.getChronologyCaptionLevel3());
            newSerialReceivingType.setChronologyCaptionLevel4(oleSerialReceivingType.getChronologyCaptionLevel4());
            newSerialReceivingType.setEnumerationCaptionLevel1(oleSerialReceivingType.getEnumerationCaptionLevel1());
            newSerialReceivingType.setEnumerationCaptionLevel2(oleSerialReceivingType.getEnumerationCaptionLevel2());
            newSerialReceivingType.setEnumerationCaptionLevel3(oleSerialReceivingType.getEnumerationCaptionLevel3());
            newSerialReceivingType.setEnumerationCaptionLevel4(oleSerialReceivingType.getEnumerationCaptionLevel4());
            newSerialReceivingType.setEnumerationCaptionLevel5(oleSerialReceivingType.getEnumerationCaptionLevel5());
            newSerialReceivingType.setEnumerationCaptionLevel6(oleSerialReceivingType.getEnumerationCaptionLevel6());
            oleSerialReceivingTypeList.add(newSerialReceivingType);

        }
        newSerialReceivingDocument.setOleSerialReceivingTypes(oleSerialReceivingTypeList);
        newSerialReceivingDocument.setReceivingRecordType(oldSerialReceivingDocument.getReceivingRecordType());
        newSerialReceivingDocument.setChronologyCaptionLevel1(oldSerialReceivingDocument.getChronologyCaptionLevel1());
        newSerialReceivingDocument.setChronologyCaptionLevel2(oldSerialReceivingDocument.getChronologyCaptionLevel2());
        newSerialReceivingDocument.setChronologyCaptionLevel3(oldSerialReceivingDocument.getChronologyCaptionLevel3());
        newSerialReceivingDocument.setChronologyCaptionLevel4(oldSerialReceivingDocument.getChronologyCaptionLevel4());
        newSerialReceivingDocument.setClaim(oldSerialReceivingDocument.isClaim());
        newSerialReceivingDocument.setClaimIntervalInformation(oldSerialReceivingDocument.getClaimIntervalInformation());
        newSerialReceivingDocument.setCopyNumber(oldSerialReceivingDocument.getCopyNumber());
        newSerialReceivingDocument.setCorporateAuthor(oldSerialReceivingDocument.getCorporateAuthor());
        newSerialReceivingDocument.setCreateItem(oldSerialReceivingDocument.isCreateItem());
        newSerialReceivingDocument.setEnumerationCaptionLevel1(oldSerialReceivingDocument.getEnumerationCaptionLevel1());
        newSerialReceivingDocument.setEnumerationCaptionLevel2(oldSerialReceivingDocument.getEnumerationCaptionLevel2());
        newSerialReceivingDocument.setEnumerationCaptionLevel3(oldSerialReceivingDocument.getEnumerationCaptionLevel3());
        newSerialReceivingDocument.setEnumerationCaptionLevel4(oldSerialReceivingDocument.getEnumerationCaptionLevel4());
        newSerialReceivingDocument.setEnumerationCaptionLevel5(oldSerialReceivingDocument.getEnumerationCaptionLevel5());
        newSerialReceivingDocument.setEnumerationCaptionLevel6(oldSerialReceivingDocument.getEnumerationCaptionLevel6());
        newSerialReceivingDocument.setGeneralReceivingNote(oldSerialReceivingDocument.getGeneralReceivingNote());
        newSerialReceivingDocument.setInstanceId(oldSerialReceivingDocument.getInstanceId());
        newSerialReceivingDocument.setIssn(oldSerialReceivingDocument.getIssn());
        newSerialReceivingDocument.setPoId(oldSerialReceivingDocument.getPoId());
        newSerialReceivingDocument.setPrintLabel(oldSerialReceivingDocument.isPrintLabel());
        newSerialReceivingDocument.setPublicDisplay(oldSerialReceivingDocument.isPublicDisplay());
        newSerialReceivingDocument.setPublisher(oldSerialReceivingDocument.getPublisher());
        newSerialReceivingDocument.setSerialReceiptLocation(oldSerialReceivingDocument.getSerialReceiptLocation());
        newSerialReceivingDocument.setSerialReceivingRecord(oldSerialReceivingDocument.getSerialReceivingRecord());
        newSerialReceivingDocument.setSubscriptionStatus(oldSerialReceivingDocument.getSubscriptionStatus());
        newSerialReceivingDocument.setTreatmentInstructionNote(oldSerialReceivingDocument.getTreatmentInstructionNote());
        newSerialReceivingDocument.setUnboundLocation(oldSerialReceivingDocument.getUnboundLocation());
        newSerialReceivingDocument.setUrgentNote(oldSerialReceivingDocument.getUrgentNote());
        newSerialReceivingDocument.setVendorId(oldSerialReceivingDocument.getVendorId());
        newSerialReceivingDocument.setCreateDate(oldSerialReceivingDocument.getCreateDate());
        newSerialReceivingDocument.setOperatorId(oldSerialReceivingDocument.getOperatorId());
        newSerialReceivingDocument.setMachineId(oldSerialReceivingDocument.getMachineId());
        newSerialReceivingDocument.setSubscriptionStatusDate(oldSerialReceivingDocument.getSubscriptionStatusDate());
        newSerialReceivingDocument.setActive(true);
        List<OLESerialReceivingHistory> newSerialReceivingHistoryList = new ArrayList<>();
        reSortById(oldSerialReceivingDocument.getOleSerialReceivingHistoryList());
        for(OLESerialReceivingHistory oleSerialReceivingHistory : oldSerialReceivingDocument.getOleSerialReceivingHistoryList()){
            OLESerialReceivingHistory newSerialReceivingHistory = new OLESerialReceivingHistory();
            newSerialReceivingHistory.setReceivingRecordType(oleSerialReceivingHistory.getReceivingRecordType());
            newSerialReceivingHistory.setChronologyCaptionLevel1(oleSerialReceivingHistory.getChronologyCaptionLevel1());
            newSerialReceivingHistory.setChronologyCaptionLevel2(oleSerialReceivingHistory.getChronologyCaptionLevel2());
            newSerialReceivingHistory.setChronologyCaptionLevel3(oleSerialReceivingHistory.getChronologyCaptionLevel3());
            newSerialReceivingHistory.setChronologyCaptionLevel4(oleSerialReceivingHistory.getChronologyCaptionLevel4());
            newSerialReceivingHistory.setClaimCount(oleSerialReceivingHistory.getClaimCount());
            newSerialReceivingHistory.setClaimDate(oleSerialReceivingHistory.getClaimDate());
            newSerialReceivingHistory.setClaimNote(oleSerialReceivingHistory.getClaimNote());
            newSerialReceivingHistory.setClaimResponse(oleSerialReceivingHistory.getClaimResponse());
            newSerialReceivingHistory.setClaimType(oleSerialReceivingHistory.getClaimType());
            newSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingHistory.getEnumerationCaptionLevel1());
            newSerialReceivingHistory.setEnumerationCaptionLevel2(oleSerialReceivingHistory.getEnumerationCaptionLevel2());
            newSerialReceivingHistory.setEnumerationCaptionLevel3(oleSerialReceivingHistory.getEnumerationCaptionLevel3());
            newSerialReceivingHistory.setEnumerationCaptionLevel4(oleSerialReceivingHistory.getEnumerationCaptionLevel4());
            newSerialReceivingHistory.setEnumerationCaptionLevel5(oleSerialReceivingHistory.getEnumerationCaptionLevel5());
            newSerialReceivingHistory.setEnumerationCaptionLevel6(oleSerialReceivingHistory.getEnumerationCaptionLevel6());
            newSerialReceivingHistory.setPublicDisplay(oleSerialReceivingHistory.isPublicDisplay());
            newSerialReceivingHistory.setSerialReceiptNote(oleSerialReceivingHistory.getSerialReceiptNote());
            newSerialReceivingHistory.setOperatorId(oleSerialReceivingHistory.getOperatorId());
            newSerialReceivingHistory.setMachineId(oleSerialReceivingHistory.getMachineId());
            newSerialReceivingHistory.setReceiptStatus(oleSerialReceivingHistory.getReceiptStatus());
            newSerialReceivingHistory.setReceiptDate(oleSerialReceivingHistory.getReceiptDate());
            newSerialReceivingHistory.setPublicReceipt(oleSerialReceivingHistory.getPublicReceipt());
            newSerialReceivingHistory.setStaffOnlyReceipt(oleSerialReceivingHistory.getStaffOnlyReceipt());
            newSerialReceivingHistoryList.add(newSerialReceivingHistory);
        }
        newSerialReceivingDocument.setOleSerialReceivingHistoryList(newSerialReceivingHistoryList);
    }


    public void createNewWithExisting(OLESerialReceivingDocument newSerialReceivingDocument,OLESerialReceivingDocument oldSerialReceivingDocument){
        newSerialReceivingDocument.setTitle(oldSerialReceivingDocument.getTitle());
        newSerialReceivingDocument.setActionDate(oldSerialReceivingDocument.getActionDate());
        newSerialReceivingDocument.setActionInterval(oldSerialReceivingDocument.getActionInterval());
        newSerialReceivingDocument.setBibId(oldSerialReceivingDocument.getBibId());
        newSerialReceivingDocument.setBoundLocation(oldSerialReceivingDocument.getBoundLocation());
        newSerialReceivingDocument.setCallNumber(oldSerialReceivingDocument.getCallNumber());
        List<OLESerialReceivingType> oleSerialReceivingTypeList = new ArrayList<OLESerialReceivingType>();
        if (oldSerialReceivingDocument.getOleSerialReceivingTypes() != null) {
            for (OLESerialReceivingType oleSerialReceivingType : oldSerialReceivingDocument.getOleSerialReceivingTypes()) {
                OLESerialReceivingType newSerialReceivingType = new OLESerialReceivingType();
                newSerialReceivingType.setReceivingRecordType(oleSerialReceivingType.getReceivingRecordType());
                newSerialReceivingType.setActionDate(oleSerialReceivingType.getActionDate());
                newSerialReceivingType.setActionInterval(oleSerialReceivingType.getActionInterval());
                newSerialReceivingType.setChronologyCaptionLevel1(oleSerialReceivingType.getChronologyCaptionLevel1());
                newSerialReceivingType.setChronologyCaptionLevel2(oleSerialReceivingType.getChronologyCaptionLevel2());
                newSerialReceivingType.setChronologyCaptionLevel3(oleSerialReceivingType.getChronologyCaptionLevel3());
                newSerialReceivingType.setChronologyCaptionLevel4(oleSerialReceivingType.getChronologyCaptionLevel4());
                newSerialReceivingType.setEnumerationCaptionLevel1(oleSerialReceivingType.getEnumerationCaptionLevel1());
                newSerialReceivingType.setEnumerationCaptionLevel2(oleSerialReceivingType.getEnumerationCaptionLevel2());
                newSerialReceivingType.setEnumerationCaptionLevel3(oleSerialReceivingType.getEnumerationCaptionLevel3());
                newSerialReceivingType.setEnumerationCaptionLevel4(oleSerialReceivingType.getEnumerationCaptionLevel4());
                newSerialReceivingType.setEnumerationCaptionLevel5(oleSerialReceivingType.getEnumerationCaptionLevel5());
                newSerialReceivingType.setEnumerationCaptionLevel6(oleSerialReceivingType.getEnumerationCaptionLevel6());
            oleSerialReceivingTypeList.add(newSerialReceivingType);

            }
        }
        newSerialReceivingDocument.setOleSerialReceivingTypes(oleSerialReceivingTypeList);
        newSerialReceivingDocument.setReceivingRecordType(oldSerialReceivingDocument.getReceivingRecordType());
        newSerialReceivingDocument.setChronologyCaptionLevel1(oldSerialReceivingDocument.getChronologyCaptionLevel1());
        newSerialReceivingDocument.setChronologyCaptionLevel2(oldSerialReceivingDocument.getChronologyCaptionLevel2());
        newSerialReceivingDocument.setChronologyCaptionLevel3(oldSerialReceivingDocument.getChronologyCaptionLevel3());
        newSerialReceivingDocument.setChronologyCaptionLevel4(oldSerialReceivingDocument.getChronologyCaptionLevel4());
        newSerialReceivingDocument.setClaim(oldSerialReceivingDocument.isClaim());
        newSerialReceivingDocument.setClaimIntervalInformation(oldSerialReceivingDocument.getClaimIntervalInformation());
        newSerialReceivingDocument.setCopyNumber(oldSerialReceivingDocument.getCopyNumber());
        newSerialReceivingDocument.setCorporateAuthor(oldSerialReceivingDocument.getCorporateAuthor());
        newSerialReceivingDocument.setCreateItem(oldSerialReceivingDocument.isCreateItem());
        newSerialReceivingDocument.setEnumerationCaptionLevel1(oldSerialReceivingDocument.getEnumerationCaptionLevel1());
        newSerialReceivingDocument.setEnumerationCaptionLevel2(oldSerialReceivingDocument.getEnumerationCaptionLevel2());
        newSerialReceivingDocument.setEnumerationCaptionLevel3(oldSerialReceivingDocument.getEnumerationCaptionLevel3());
        newSerialReceivingDocument.setEnumerationCaptionLevel4(oldSerialReceivingDocument.getEnumerationCaptionLevel4());
        newSerialReceivingDocument.setEnumerationCaptionLevel5(oldSerialReceivingDocument.getEnumerationCaptionLevel5());
        newSerialReceivingDocument.setEnumerationCaptionLevel6(oldSerialReceivingDocument.getEnumerationCaptionLevel6());
        newSerialReceivingDocument.setGeneralReceivingNote(oldSerialReceivingDocument.getGeneralReceivingNote());
        newSerialReceivingDocument.setInstanceId(oldSerialReceivingDocument.getInstanceId());
        newSerialReceivingDocument.setIssn(oldSerialReceivingDocument.getIssn());
        newSerialReceivingDocument.setPoId(oldSerialReceivingDocument.getPoId());
        newSerialReceivingDocument.setPrintLabel(oldSerialReceivingDocument.isPrintLabel());
        newSerialReceivingDocument.setPublicDisplay(oldSerialReceivingDocument.isPublicDisplay());
        newSerialReceivingDocument.setPublisher(oldSerialReceivingDocument.getPublisher());
        newSerialReceivingDocument.setSerialReceiptLocation(oldSerialReceivingDocument.getSerialReceiptLocation());
        newSerialReceivingDocument.setSerialReceivingRecord(oldSerialReceivingDocument.getSerialReceivingRecord());
        newSerialReceivingDocument.setSubscriptionStatus(oldSerialReceivingDocument.getSubscriptionStatus());
        newSerialReceivingDocument.setTreatmentInstructionNote(oldSerialReceivingDocument.getTreatmentInstructionNote());
        newSerialReceivingDocument.setUnboundLocation(oldSerialReceivingDocument.getUnboundLocation());
        newSerialReceivingDocument.setUrgentNote(oldSerialReceivingDocument.getUrgentNote());
        newSerialReceivingDocument.setVendorId(oldSerialReceivingDocument.getVendorId());
        newSerialReceivingDocument.setCreateDate(oldSerialReceivingDocument.getCreateDate());
        newSerialReceivingDocument.setOperatorId(oldSerialReceivingDocument.getOperatorId());
        newSerialReceivingDocument.setMachineId(oldSerialReceivingDocument.getMachineId());
        newSerialReceivingDocument.setSubscriptionStatusDate(oldSerialReceivingDocument.getSubscriptionStatusDate());
        newSerialReceivingDocument.setActive(true);
        List<OLESerialReceivingHistory> newSerialReceivingHistoryList = new ArrayList<>();
        if (oldSerialReceivingDocument.getOleSerialReceivingHistoryList() != null) {
            for (OLESerialReceivingHistory oleSerialReceivingHistory : oldSerialReceivingDocument.getOleSerialReceivingHistoryList()) {
                OLESerialReceivingHistory newSerialReceivingHistory = new OLESerialReceivingHistory();
                newSerialReceivingHistory.setReceivingRecordType(oleSerialReceivingHistory.getReceivingRecordType());
                newSerialReceivingHistory.setChronologyCaptionLevel1(oleSerialReceivingHistory.getChronologyCaptionLevel1());
                newSerialReceivingHistory.setChronologyCaptionLevel2(oleSerialReceivingHistory.getChronologyCaptionLevel2());
                newSerialReceivingHistory.setChronologyCaptionLevel3(oleSerialReceivingHistory.getChronologyCaptionLevel3());
                newSerialReceivingHistory.setChronologyCaptionLevel4(oleSerialReceivingHistory.getChronologyCaptionLevel4());
                newSerialReceivingHistory.setClaimCount(oleSerialReceivingHistory.getClaimCount());
                newSerialReceivingHistory.setClaimDate(oleSerialReceivingHistory.getClaimDate());
                newSerialReceivingHistory.setClaimNote(oleSerialReceivingHistory.getClaimNote());
                newSerialReceivingHistory.setClaimResponse(oleSerialReceivingHistory.getClaimResponse());
                newSerialReceivingHistory.setClaimType(oleSerialReceivingHistory.getClaimType());
                newSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingHistory.getEnumerationCaptionLevel1());
                newSerialReceivingHistory.setEnumerationCaptionLevel2(oleSerialReceivingHistory.getEnumerationCaptionLevel2());
                newSerialReceivingHistory.setEnumerationCaptionLevel3(oleSerialReceivingHistory.getEnumerationCaptionLevel3());
                newSerialReceivingHistory.setEnumerationCaptionLevel4(oleSerialReceivingHistory.getEnumerationCaptionLevel4());
                newSerialReceivingHistory.setEnumerationCaptionLevel5(oleSerialReceivingHistory.getEnumerationCaptionLevel5());
                newSerialReceivingHistory.setEnumerationCaptionLevel6(oleSerialReceivingHistory.getEnumerationCaptionLevel6());
                newSerialReceivingHistory.setPublicDisplay(oleSerialReceivingHistory.isPublicDisplay());
                newSerialReceivingHistory.setSerialReceiptNote(oleSerialReceivingHistory.getSerialReceiptNote());
                newSerialReceivingHistory.setOperatorId(oleSerialReceivingHistory.getOperatorId());
                newSerialReceivingHistory.setMachineId(oleSerialReceivingHistory.getMachineId());
                newSerialReceivingHistory.setReceiptStatus(oleSerialReceivingHistory.getReceiptStatus());
                newSerialReceivingHistory.setReceiptDate(oleSerialReceivingHistory.getReceiptDate());
                newSerialReceivingHistory.setPublicReceipt(oleSerialReceivingHistory.getPublicReceipt());
                newSerialReceivingHistory.setStaffOnlyReceipt(oleSerialReceivingHistory.getStaffOnlyReceipt());
                newSerialReceivingHistoryList.add(newSerialReceivingHistory);
            }
        }
        newSerialReceivingDocument.setOleSerialReceivingHistoryList(newSerialReceivingHistoryList);
    }

    public void setEnumerationAndChronologyValues(OLESerialReceivingHistory oleSerialReceivingHistory){
        String enumCaptionParameter = OLEConstants.ENUMERATION_CAPTION;
        StringBuffer enumerationCaption =  new StringBuffer();
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel1())?"":oleSerialReceivingHistory.getEnumerationCaptionLevel1());
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel2())?"":getParameter(enumCaptionParameter) + oleSerialReceivingHistory.getEnumerationCaptionLevel2());
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel3())?"":getParameter(enumCaptionParameter) + oleSerialReceivingHistory.getEnumerationCaptionLevel3());
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel4())?"":getParameter(enumCaptionParameter) + oleSerialReceivingHistory.getEnumerationCaptionLevel4());
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel5())?"":getParameter(enumCaptionParameter) + oleSerialReceivingHistory.getEnumerationCaptionLevel5());
        enumerationCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getEnumerationCaptionLevel6())?"":getParameter(enumCaptionParameter) + oleSerialReceivingHistory.getEnumerationCaptionLevel6());
        if(!StringUtils.isEmpty(enumerationCaption.toString())) {
            if(enumerationCaption.subSequence(0,1).equals(getParameter(enumCaptionParameter))) {
                enumerationCaption.deleteCharAt(0);
            }
        }
        oleSerialReceivingHistory.setEnumerationCaption(enumerationCaption.toString());
        String chronologyCaptionParameter = OLEConstants.CHRONOLOGY_CAPTION;
        StringBuffer chronologyCaption =  new StringBuffer();
        chronologyCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getChronologyCaptionLevel1())?"":oleSerialReceivingHistory.getChronologyCaptionLevel1());
        chronologyCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getChronologyCaptionLevel2())?"":getParameter(chronologyCaptionParameter) + oleSerialReceivingHistory.getChronologyCaptionLevel2());
        chronologyCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getChronologyCaptionLevel3())?"":getParameter(chronologyCaptionParameter) + oleSerialReceivingHistory.getChronologyCaptionLevel3());
        chronologyCaption.append(StringUtils.isEmpty(oleSerialReceivingHistory.getChronologyCaptionLevel4())?"":getParameter(chronologyCaptionParameter) + oleSerialReceivingHistory.getChronologyCaptionLevel4());
        if(!StringUtils.isEmpty(chronologyCaption.toString())) {
            if(chronologyCaption.subSequence(0,1).equals(getParameter(chronologyCaptionParameter))) {
                chronologyCaption.deleteCharAt(0);
            }
        }
        oleSerialReceivingHistory.setChronologyCaption(chronologyCaption.toString());
    }

    public void populateEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingType oleSerialReceivingType) {
        oleSerialReceivingDocument.setSerialReceivingRecordId(oleSerialReceivingType.getSerialReceivingRecordId());
        oleSerialReceivingDocument.setReceivingRecordType(oleSerialReceivingType.getReceivingRecordType());
        oleSerialReceivingDocument.setActionDate(oleSerialReceivingType.getActionDate());
        oleSerialReceivingDocument.setActionInterval(oleSerialReceivingType.getActionInterval());
        oleSerialReceivingDocument.setChronologyCaptionLevel1(oleSerialReceivingType.getChronologyCaptionLevel1());
        oleSerialReceivingDocument.setChronologyCaptionLevel2(oleSerialReceivingType.getChronologyCaptionLevel2());
        oleSerialReceivingDocument.setChronologyCaptionLevel3(oleSerialReceivingType.getChronologyCaptionLevel3());
        oleSerialReceivingDocument.setChronologyCaptionLevel4(oleSerialReceivingType.getChronologyCaptionLevel4());
        oleSerialReceivingDocument.setEnumerationCaptionLevel1(oleSerialReceivingType.getEnumerationCaptionLevel1());
        oleSerialReceivingDocument.setEnumerationCaptionLevel2(oleSerialReceivingType.getEnumerationCaptionLevel2());
        oleSerialReceivingDocument.setEnumerationCaptionLevel3(oleSerialReceivingType.getEnumerationCaptionLevel3());
        oleSerialReceivingDocument.setEnumerationCaptionLevel4(oleSerialReceivingType.getEnumerationCaptionLevel4());
        oleSerialReceivingDocument.setEnumerationCaptionLevel5(oleSerialReceivingType.getEnumerationCaptionLevel5());
        oleSerialReceivingDocument.setEnumerationCaptionLevel6(oleSerialReceivingType.getEnumerationCaptionLevel6());
    }
    public void validateClaim(OLESerialReceivingDocument oleSerialReceivingDocument){
        if (StringUtils.isBlank(oleSerialReceivingDocument.getActionInterval())) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL, OLEKeyConstants.SERIAL_RECEIVE_ACTIONINTERVAL);
        }
        if (oleSerialReceivingDocument.getActionDate() == null) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE, OLEKeyConstants.SERIAL_RECEIVE_ACTIONDATE);
        }
        if (StringUtils.isBlank(oleSerialReceivingDocument.getVendorId())) {
            GlobalVariables.getMessageMap().putError(OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID, OLEKeyConstants.SERIAL_RECEIVE_VENDOR_ID);
        }
    }

    @Override
    public void updateEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingHistory oleSerialReceivingHistory) {
        oleSerialReceivingHistory.setEnumerationCaptionLevel1(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel1());
        oleSerialReceivingHistory.setEnumerationCaptionLevel2(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel2());
        oleSerialReceivingHistory.setEnumerationCaptionLevel3(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel3());
        oleSerialReceivingHistory.setEnumerationCaptionLevel4(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel4());
        oleSerialReceivingHistory.setEnumerationCaptionLevel5(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel5());
        oleSerialReceivingHistory.setEnumerationCaptionLevel6(oleSerialReceivingDocument.getEnumerationHistoryCaptionLevel6());

        oleSerialReceivingHistory.setChronologyCaptionLevel1(oleSerialReceivingDocument.getChronologyHistoryCaptionLevel1());
        oleSerialReceivingHistory.setChronologyCaptionLevel2(oleSerialReceivingDocument.getChronologyHistoryCaptionLevel2());
        oleSerialReceivingHistory.setChronologyCaptionLevel3(oleSerialReceivingDocument.getChronologyHistoryCaptionLevel3());
        oleSerialReceivingHistory.setChronologyCaptionLevel4(oleSerialReceivingDocument.getChronologyHistoryCaptionLevel4());
    }

    @Override
    public void setEnumerationAndChronologyValues(OLESerialReceivingDocument oleSerialReceivingDocument, OLESerialReceivingHistory oleSerialReceivingHistory) {
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel1(oleSerialReceivingHistory.getEnumerationCaptionLevel1());
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel2(oleSerialReceivingHistory.getEnumerationCaptionLevel2());
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel3(oleSerialReceivingHistory.getEnumerationCaptionLevel3());
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel4(oleSerialReceivingHistory.getEnumerationCaptionLevel4());
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel5(oleSerialReceivingHistory.getEnumerationCaptionLevel5());
        oleSerialReceivingDocument.setEnumerationHistoryCaptionLevel6(oleSerialReceivingHistory.getEnumerationCaptionLevel6());

        oleSerialReceivingDocument.setChronologyHistoryCaptionLevel1(oleSerialReceivingHistory.getChronologyCaptionLevel1());
        oleSerialReceivingDocument.setChronologyHistoryCaptionLevel2(oleSerialReceivingHistory.getChronologyCaptionLevel2());
        oleSerialReceivingDocument.setChronologyHistoryCaptionLevel3(oleSerialReceivingHistory.getChronologyCaptionLevel3());
        oleSerialReceivingDocument.setChronologyHistoryCaptionLevel4(oleSerialReceivingHistory.getChronologyCaptionLevel4());
    }

}
