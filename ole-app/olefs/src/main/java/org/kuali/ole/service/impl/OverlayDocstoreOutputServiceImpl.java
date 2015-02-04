package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.client.DocstoreClientLocator;
import org.kuali.ole.docstore.common.document.*;
import org.kuali.ole.docstore.common.document.content.instance.*;

import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.Items;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.service.OverlayOutputService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/23/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayDocstoreOutputServiceImpl implements OverlayOutputService {
    private static final Logger LOG = Logger.getLogger(OverlayDocstoreOutputServiceImpl.class);


    private DocstoreClientLocator docstoreClientLocator;
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor;
    private ItemOlemlRecordProcessor itemOlemlRecordProcessor;

    public DocstoreClientLocator getDocstoreClientLocator() {
        if (docstoreClientLocator == null) {
            docstoreClientLocator = SpringContext.getBean(DocstoreClientLocator.class);
        }
        return docstoreClientLocator;
    }
    @Override
    public void setOutPutValue(String field, String value,Object object) {
        if(object instanceof Instance){
            Instance instance = (Instance)object;
            OleHoldings oleHoldings = instance.getOleHoldings();
            Items items = instance.getItems();
            instance.setOleHoldings(oleHoldings);
            instance.setItems(items);
        }else if(object instanceof OleHoldings){
            OleHoldings oleHoldings = (OleHoldings)object;
            if(field.equalsIgnoreCase(OLEConstants.OVERLAY_CALLNUMBER)){
                CallNumber callNumber =oleHoldings.getCallNumber();
                if(callNumber==null){
                    callNumber = new CallNumber();
                }
                String callNumberValue = value;
                callNumber.setClassificationPart(callNumberValue);
                oleHoldings.setCallNumber(callNumber);
            }else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_LOCATION)){
                Location location = oleHoldings.getLocation();
                if(location == null){
                    location = new Location();
                }
                LocationLevel locationLevel = location.getLocationLevel();
                if(locationLevel == null){
                    locationLevel = new LocationLevel();
                }
                locationLevel.setName(value);
                location.setLocationLevel(locationLevel);
                oleHoldings.setLocation(location);
            }else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_NOTE)) {
                Note  note = new Note();
                note.setType(value);
                List<Note> noteList = new ArrayList<Note>();
                noteList.add(note);
                oleHoldings.setNote(noteList);
            }  else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_HOLDINGS_IDENTIFIER)) {
                oleHoldings.setHoldingsIdentifier(value);
            }
        }else if(object instanceof Item){
            Item item = (Item)object;
            if(field.equalsIgnoreCase(OLEConstants.OVERLAY_CALLNUMBER)){
                CallNumber callNumber =item.getCallNumber();
                if(callNumber==null){
                    callNumber = new CallNumber();
                }
                String callNumberValue = value;
                callNumber.setClassificationPart(callNumberValue);
                item.setCallNumber(callNumber);
            }else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_ITEMTYPE)){
                ItemType itemType = item.getItemType();
                if(itemType==null){
                    itemType = new ItemType();
                }
                itemType.setFullValue(value);
                item.setItemType(itemType);

            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_BARCODE)){
                item.setBarcodeARSL(value);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_STATISTICALSEARCHINGCODES)){
                StatisticalSearchingCode statisticalSearchingCode = new StatisticalSearchingCode();
                statisticalSearchingCode.setFullValue(value);
                List<StatisticalSearchingCode> statisticalSearchingCodeList = new ArrayList<StatisticalSearchingCode>();
                statisticalSearchingCodeList.add(statisticalSearchingCode);
                item.setStatisticalSearchingCode(statisticalSearchingCodeList);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_VENDOR_LINEITEM_IDENTIFIER)){
                item.setVendorLineItemIdentifier(value);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_STAFF_ONLY_FLAG)){
                Boolean staffOnlyFlag = Boolean.parseBoolean(value);
                item.setStaffOnlyFlag(staffOnlyFlag);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_FAST_ADD_FLAG)){
                Boolean fastAddFlag = Boolean.parseBoolean(value);
                item.setFastAddFlag(fastAddFlag);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_STATUS)){
                ItemStatus itemStatus = new ItemStatus();
                itemStatus.setCodeValue(value);
                itemStatus.setFullValue(value);
            }
            else if(field.equalsIgnoreCase(OLEConstants.OVERLAY_ITEM_LOCATION)){
                Location location = item.getLocation();
                if(location == null){
                    location = new Location();
                }
                LocationLevel locationLevel = location.getLocationLevel();
                if(locationLevel == null){
                    locationLevel = new LocationLevel();
                }
                locationLevel.setName(value);
                location.setLocationLevel(locationLevel);
                item.setLocation(location);
            }
        }
    }


    public void persist(Object object) throws Exception {

        if (object instanceof OleHoldings) {
            Holdings holdings = new PHoldings();
            holdings.setCategory(DocCategory.WORK.getCode());
            holdings.setType(DocType.HOLDINGS.getCode());
            holdings.setFormat(DocFormat.OLEML.getCode());
            OleHoldings oleHoldings = (OleHoldings) object;
            holdings.setContentObject(getHoldingOlemlRecordProcessor().toXML(oleHoldings));
            holdings.setId(oleHoldings.getHoldingsIdentifier());
            getDocstoreClientLocator().getDocstoreClient().updateHoldings(holdings);
        } else if (object instanceof Items) {
            Items items = (Items) object;
            List<Item> itemList = items.getItem();

            org.kuali.ole.docstore.common.document.Item itemOleml = new ItemOleml();
            itemOleml.setCategory(DocCategory.WORK.getCode());
            itemOleml.setType(DocType.ITEM.getCode());
            itemOleml.setFormat(DocFormat.OLEML.getCode());
            for (Item item : itemList) {
                itemOleml.setContent(getItemOlemlRecordProcessor().toXML(item));
                itemOleml.setId(item.getItemIdentifier());
                getDocstoreClientLocator().getDocstoreClient().updateItem(itemOleml);
            }
        }
    }

    public HoldingOlemlRecordProcessor getHoldingOlemlRecordProcessor() {
        if (holdingOlemlRecordProcessor == null) {
            holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
        }
        return holdingOlemlRecordProcessor;
    }

    public ItemOlemlRecordProcessor getItemOlemlRecordProcessor() {
        if (itemOlemlRecordProcessor == null) {
            itemOlemlRecordProcessor = new ItemOlemlRecordProcessor();
        }
        return itemOlemlRecordProcessor;
    }
}
