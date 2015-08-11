package org.kuali.ole.describe.service.impl;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.common.document.content.instance.ExtentOfOwnership;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.describe.form.InstanceEditorForm;
import org.kuali.ole.describe.service.InstanceEditorTreeService;

import java.util.*;

/**
 * InstanceEditorTreeServiceImpl is the service implementation class for Instance Editor  tree structure in left pane.
 */
public class InstanceEditorTreeServiceImpl implements InstanceEditorTreeService {
    private static final Logger LOG = Logger.getLogger(InstanceEditorTreeServiceImpl.class);

    /**
     * This method creates TreeStructure (InnerHtML as a String and make treeStructure with jQuery) for instance with Holdings as parent node
     * and items as child nodes grouped by formerShelvingLocation.
     *
     * @param instanceEditorForm
     * @return String
     */
    @Override
    public String createTreeStructure(InstanceEditorForm instanceEditorForm) {
        String textualHolding = null;
        List<ExtentOfOwnership> extentOfOwnership = instanceEditorForm.getInstance().getOleHoldings().getExtentOfOwnership();
        if (extentOfOwnership != null && extentOfOwnership.size() > 0) {
            textualHolding = extentOfOwnership.get(0).getTextualHoldings();
            LOG.info("textualHolding-->" + textualHolding);
        }
        textualHolding = (textualHolding != null && !textualHolding.trim().equals("")) ?
                textualHolding : OLEConstants.NON_SERIAL_HOLDINGS_TEXT;
        String holdingUuid = instanceEditorForm.getInstance().getOleHoldings().getHoldingsIdentifier();
        String treeData = null;
        String formerShelvingLocation = null;
        if (instanceEditorForm != null) {
            List<Item> oleItems = new ArrayList<Item>();
            oleItems = instanceEditorForm.getInstance().getItems().getItem();
            List<String> itemIdentifiersList = new ArrayList<String>();
            for (int i = 0; i < oleItems.size(); i++) {
                itemIdentifiersList.add(oleItems.get(i).getItemIdentifier());
                oleItems.get(i).setItemIdentifier(oleItems.get(i).getItemIdentifier() + "_" + i);
            }
            Map<String, List<Item>> oleItemMap = getItemMap(instanceEditorForm);
            treeData = "<ul id=\"navigation\">" +
                    "<li><div id=\"rightClick\"><b>" + textualHolding + "</b></div>" +
                    "<div class=\"vmenu\">\n" +
                    "<div class=\"add_li\"><span>Add Item</span></div>\n" +
                    "<div class=\"delete_li\"><span>Delete Holdings</span></div></div>" +
                    "<div id=\"itmMenu\" style=\"border: 1px solid #aaa; border-left:1px solid #ccc; padding: 10px; display: none; position: absolute; top: 663px; left: 539px; background:#fff;\">" +  //background-color: rgb(238, 238, 238);
                    "<table>" +
                    "<tr><td class=\"delete_itm\"width=\"100%\" >Delete Item</td></tr> " +   //style="border: 1px solid rgb(0, 0, 0);
                    "</table>" +
                    "</div>";

            Set set = oleItemMap.entrySet();
            Iterator itr = set.iterator();
            if (itr.hasNext()) {
                int itemSize = 1;
                while (itr.hasNext()) {

                    boolean isElectronic = false;
                    boolean isItem = false;
                    boolean isNoLocation = false;
                    Map.Entry itemEntry = (Map.Entry) itr.next();
                    String location = (String) itemEntry.getKey();
                    List<Item> itemsEntry = (List<Item>) itemEntry.getValue();
                    if (location.equalsIgnoreCase(OLEConstants.ITEM_ELECTRONIC)) {
                        isElectronic = true;
                    } else if (location.equalsIgnoreCase(OLEConstants.NO_LOCATION_ITEM)) {
                        isNoLocation = true;
                    } else if (location.equalsIgnoreCase(OLEConstants.ITEM_LEVEL_TEXT)) {
                        isItem = true;
                    } else if (!isElectronic && !isItem && !isNoLocation) {
                        treeData = treeData +
                                "<ul>" +
                                "<li><label>" + location + "</label>" +
                                "<ul>";
                    }
                    for (int i = 0; i < itemsEntry.size(); i++) {
                        String itemIdentifier = itemsEntry.get(i).getItemIdentifier();
                        if (!isElectronic && !isItem && !isNoLocation) {
                            OleHoldings oleHoldings = instanceEditorForm.getInstance().getOleHoldings();
                            String itemLevelContent = buildNonElectronicItemContent(itemsEntry.get(i), oleHoldings);
                            treeData = treeData + "<li><div class=\"itemIdentifierClass\" id=\"" + itemIdentifier + "\">" + itemLevelContent + "</div>" +
                                    "</li>";
                        } else if (isElectronic) {
                            String itemLevelContent = itemsEntry.get(i).getAccessInformation().getUri().getValue();
                            treeData = treeData +
                                    "<ul>" +
                                    "<li><div class=\"itemIdentifierClass\" id=\"" + itemIdentifier + "\">" + itemLevelContent + "</div>" +
                                    "</li>" +
                                    "</ul>";
                        } else {
                            treeData = treeData +
                                    "<ul>" +
                                    "<li><div class=\"itemIdentifierClass\" id=\"" + itemIdentifier + "\">" + OLEConstants.ITEM_LEVEL_TEXT + itemSize + "</div>" +
                                    "</li>" +
                                    "</ul>";
                            itemSize++;
                        }

                    }
                    if (!isElectronic && !isItem && !isNoLocation) {
                        treeData = treeData +
                                "</ul>" +
                                "</li>" +
                                "</ul>";
                    }
                }
            } else {
                for (int j = 0; j < oleItems.size(); j++) {
                    String itemIdentifier = oleItems.get(j).getItemIdentifier();
                    int itemSize = j + 1;
                    treeData = treeData +
                            "<ul>" +
                            "<li><div class=\"itemIdentifierClass\" id=\"" + itemIdentifier + "\">" + OLEConstants.ITEM_LEVEL_TEXT + itemSize + "</div>" +
                            "</li>" +
                            "</ul>";
                }
            }
            treeData = treeData + "</li>" +
                    "</ul>";

            for (int j = 0; j < oleItems.size(); j++) {
                oleItems.get(j).setItemIdentifier(itemIdentifiersList.get(j));
            }
        }

        LOG.info("tree data in createTreeStructure() method-->" + treeData);
        return treeData;
    }

    private Map<String, List<Item>> getItemMap(InstanceEditorForm instanceEditorForm) {
        List<Item> listItems = null;
        String itemLocation = null;
        Map<String, List<Item>> oleItemMap = new HashMap<String, List<Item>>();
        List<Item> oleItems;
        oleItems = instanceEditorForm.getInstance().getItems().getItem();
        String holdingsLocations = instanceEditorForm.getInstance().getOleHoldings().getLocation().getLocationLevel().getName();
        for (Item oleItem : oleItems) {
            if (oleItem.getLocation() != null) {
                itemLocation = oleItem.getLocation().getLocationLevel().getName();
                LOG.info("item location =" + itemLocation);
                addItemsToMap(itemLocation, oleItemMap, oleItem);
                LOG.info("map size for item location = " + oleItemMap.size());
            } else if (!StringUtils.isBlank(holdingsLocations)) {
                addItemsToMap(holdingsLocations, oleItemMap, oleItem);
                LOG.info("map size for holdings location = " + oleItemMap.size());
            }
        }
        return oleItemMap;
    }

    private void addItemsToMap(String itemLocation, Map<String, List<Item>> oleItemMap, Item oleItem) {
        List<Item> listItems;
        if (oleItemMap.containsKey(itemLocation)) {
            List<Item> itemList = oleItemMap.get(itemLocation);
            itemList.add(oleItem);
            oleItemMap.put(itemLocation, itemList);
            LOG.info("item list size in if = " + itemList.size());
        } else {
            listItems = new ArrayList<Item>();
            listItems.add(oleItem);
            LOG.info("item list size in else = " + listItems.size());
            oleItemMap.put(itemLocation, listItems);
        }
    }

    /**
     * This method returns a Map with formerShelvingLocation as key and a List of Items belongs to this formerShelvingLocation as value.
     *
     * @param oleItems
     * @return Map<String,List<OleItem>>
     */
    private Map<String, List<Item>> getItemMap(List<Item> oleItems) {

        Map<String, List<Item>> oleItemMap = new HashMap<String, List<Item>>();

        if (oleItems != null) {
            for (Item item : oleItems) {
                String formerShelvingLocation = null;
                String uri = null;
                //boolean isElectronic = false;
                List<Item> listItems = new ArrayList<Item>();
                //List<PhysicalLocation> physicalLocations =  item.getLocation();
                /*if ((physicalLocations != null && physicalLocations.size() > 0)
                        && physicalLocations.get(0).getFormerShelvingLocation() != null &&
                        physicalLocations.get(0).getFormerShelvingLocation().size() > 0) {
                    formerShelvingLocation = physicalLocations.get(0).getFormerShelvingLocation().get(0);
                }*/
                if (item.getLocation() != null) {
                    formerShelvingLocation = item.getLocation().getLocationLevel().getName();
                }
                if (item.getAccessInformation() != null && item.getAccessInformation().getUri() != null) {
                    uri = item.getAccessInformation().getUri().getValue();
                }
                /*if (item.getAccessInformation() != null) {
                    isElectronic = (item.getAccessInformation().getUri() != null ? true:false);
                }*/
                if (formerShelvingLocation != null) {
                    for (Item items : oleItems) {
                        //if(items.getLocation().size()>0 && items.getLocation().get(0).getFormerShelvingLocation().size()>0){
                        String itemLocation = null;
                        if (items.getLocation() != null) {
                            itemLocation = items.getLocation().getLocationLevel().getName();
                        }
                        if ((formerShelvingLocation != null && !formerShelvingLocation.trim().equals("")) && itemLocation != null) {
                            if (formerShelvingLocation.equalsIgnoreCase(items.getLocation().getLocationLevel().getName())) {
                                listItems.add(items);
                            }
                        }
                        /*else if(isElectronic && formerShelvingLocation == null) {
                            formerShelvingLocation = OLEConstants.ITEM_ELECTRONIC;
                            listItems.add(items);
                        }*/
                        else if (formerShelvingLocation.trim().equals("")) {
                            //if(formerShelvingLocation.equalsIgnoreCase(items.getLocation())){
                            listItems.add(items);
                            //}
                        }
                        //}
                    }
                    if (formerShelvingLocation == null || formerShelvingLocation.trim().equals("")) {
                        formerShelvingLocation = OLEConstants.ITEM_LEVEL_TEXT;
                    }

                    oleItemMap.put(formerShelvingLocation, listItems);
                }
/*                else if(formerShelvingLocation == null && uri != null && !uri.equalsIgnoreCase("")) {
                    for(Item items : oleItems){
                        String uriVal = null;
                        String itemLocation = null;
                        if(items.getLocation()!=null){
                            itemLocation = items.getLocation().getLocationLevel().getName();
                        }
                        if(items.getAccessInformation()!= null && items.getAccessInformation().getUri()!= null){
                            uriVal = items.getAccessInformation().getUri().getValue();
                        }
                        if(itemLocation == null && uriVal != null && !uriVal.trim().equals("")){
                            formerShelvingLocation = OLEConstants.ITEM_ELECTRONIC;
                            listItems.add(items);
                        }
                        *//*else if(uriVal.trim().equals("")){
                            //if(formerShelvingLocation.equalsIgnoreCase(items.getLocation())){
                            listItems.add(items);
                            //}
                        }*//*
                    }
                    if(formerShelvingLocation == null || formerShelvingLocation.trim().equals("")) {
                        formerShelvingLocation = OLEConstants.ITEM_LEVEL_TEXT;
                    }
                    oleItemMap.put(formerShelvingLocation,listItems);
                }*/
                /*else {
                    for(Item items : oleItems){
                        if((formerShelvingLocation == null && uri == null) || uri.trim().equals("")) {
                            *//*String uriVal = null;
                            String itemLocation = null;
                            if(items.getLocation()!=null){
                                itemLocation = items.getLocation().getLocationLevel().getName();
                            }
                            if(items.getAccessInformation()!= null && items.getAccessInformation().getUri()!= null){
                                uriVal = items.getAccessInformation().getUri().getValue();
                            }*//*
                            if(formerShelvingLocation == null && uri==null){
                                listItems.add(items);
                            }
                        }
                    }
                    if(listItems.size()>0){
                        String emptyFormerShelvingLocation = OLEConstants.NO_LOCATION_ITEM;
                        oleItemMap.put(emptyFormerShelvingLocation,listItems);
                    }
                }*/
/*                else {
                    for(Item items : oleItems){
                        if(formerShelvingLocation == null) {
                            listItems.add(items);
                        }
                    }
                    if(listItems.size()>0){
                        String emptyFormerShelvingLocation = OLEConstants.NO_LOCATION_ITEM;
                        oleItemMap.put(emptyFormerShelvingLocation,listItems);
                    }
                }*/

            }
        }
        return oleItemMap;
    }


    /**
     * Method to build Non_Electronic Item record node content
     *
     * @param oleItem
     * @return nonElectronicItemContent
     */
    private String buildNonElectronicItemContent(Item oleItem, OleHoldings oleHoldings) {

        String callNumberPrefix = null;
        String copyNumber = null;
        String copyNumberLabel = null;
        String barcode = null;
        String status = null;
        if (oleItem.getCallNumber() != null) {
            callNumberPrefix = (oleItem.getCallNumber().getPrefix());
        } else if (oleHoldings.getCallNumber() != null) {
            callNumberPrefix = (oleHoldings.getCallNumber().getPrefix());
        }
        String enumeration = oleItem.getEnumeration();
        String chronology = oleItem.getChronology();
        String textToPrefix = OLEConstants.NON_ELECTRONIC_PREFIX_TEXT;
        copyNumber = oleItem.getCopyNumber();
        copyNumberLabel = oleItem.getCopyNumberLabel();
        if (oleItem.getAccessInformation() != null) {
            barcode = oleItem.getAccessInformation().getBarcode();
        }
        status = oleItem.getItemStatus().getCodeValue();
        String nonElectronicItemContent = new StringBuffer().append(null == copyNumberLabel ? "Empty" : copyNumberLabel).append(" ")
                .append(null == copyNumber ? "Empty" : copyNumber).append(" ")
                .append(" ").append(null == callNumberPrefix ? "Empty" : callNumberPrefix).append(" ")
                .append(" ").append(null == enumeration ? "Empty" : enumeration).append(" ")
                .append(null == chronology ? "Empty" : chronology).append(" ")
                .append(null == barcode ? "Empty" : barcode).append("-").append(null == status ? "Empty" : status).toString();

        return nonElectronicItemContent;
    }
}