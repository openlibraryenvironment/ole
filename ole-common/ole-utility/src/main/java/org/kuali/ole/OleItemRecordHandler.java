package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.pojo.ProfileAttribute;

import org.kuali.ole.pojo.edi.LineItemOrder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//import org.kuali.ole.pojo.item.Extension;
//import org.kuali.ole.pojo.item.OleItem;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/9/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleItemRecordHandler {

    public static final String COPY_NUMBER = "1";
    public static final String COPY_NUMBER_LABEL = "c";
    public static final String CALL_NUMBER_TYPE = "LCC";
    public static final String ITEM_STATUS = "AVAILABLE";
    public static final String ITEM_TYPE = "NORMAL LOAN";

    public String generateXML(BibMarcRecord bibliographicRecord, List<ProfileAttribute> profileAttributes) {
        return toXML(getOleItem(bibliographicRecord, profileAttributes));
    }

    public Item getOleItem(BibMarcRecord bibMarcRecord, List<ProfileAttribute> profileAttributes) {


        Item oleItem = new Item();
//        oleItem.setItemType(getSubFieldValueFor(bibliographicRecord, dataField, "$5"));
        ItemType itemType = new ItemType();
        /*itemType.setCodeValue(getSubFieldValueFor(bibliographicRecord, dataField, "$5"));
        itemType.setFullValue(getSubFieldValueFor(bibliographicRecord, dataField, "$5"));*/
        /*String itmType = getSubFieldValueFor(bibliographicRecord, "985", "t");
        if(itmType != null){
            itemType.setCodeValue(itmType);
            itemType.setFullValue(itmType);
        }
        else {
            itemType.setCodeValue(ITEM_TYPE);
            itemType.setFullValue(ITEM_TYPE);
        }*/

        itemType.setCodeValue(getSubFieldValueFor(bibMarcRecord, "985", "t") == null ? ITEM_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "t"));
        itemType.setFullValue(getSubFieldValueFor(bibMarcRecord, "985", "t") == null ? ITEM_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "t"));
        oleItem.setItemType(itemType);
        // String copyNumber = getSubFieldValueFor(bibliographicRecord, "876", "t");
        /*String copyNumber = getSubFieldValueFor(bibliographicRecord, "985", "g");
        if(copyNumber != null) {
            oleItem.setCopyNumber(copyNumber);
        }
        else {
            oleItem.setCopyNumber(COPY_NUMBER);
        }*/

        oleItem.setCopyNumber(getSubFieldValueFor(bibMarcRecord, "985", "g") == null ? COPY_NUMBER : getSubFieldValueFor(bibMarcRecord, "985", "g"));
        oleItem.setCopyNumberLabel(COPY_NUMBER_LABEL);

        oleItem.setPurchaseOrderLineItemIdentifier("");
        ItemStatus itemStatus = new ItemStatus();
        String itemStatusValue = getSubFieldValueFor(bibMarcRecord, "985", "s") == null ? ITEM_STATUS : getSubFieldValueFor(bibMarcRecord, "985", "s");
        itemStatus.setCodeValue(itemStatusValue);
        itemStatus.setFullValue(itemStatusValue);
        oleItem.setItemStatus(itemStatus);

        /*String itemStatus = getSubFieldValueFor(bibliographicRecord, "985", "s");
        if(itemStatus != null){
            oleItem.setItemStatus(itemStatus);
        }
        else {
            oleItem.setItemStatus(ITEM_STATUS);
        }*/
        DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, "852");
        if (dataFieldForTag != null) {
            String dataField = dataFieldForTag.getTag();
            String valueFor = getSubFieldValueFor(bibMarcRecord, dataField, "$4");
            if (null != valueFor) {
//            oleItem.setStatisticalSearchingCodes(valueFor);
                List<StatisticalSearchingCode> statisticalSearchingCodes = new ArrayList<StatisticalSearchingCode>();
                StatisticalSearchingCode searchingCodes = new StatisticalSearchingCode();
                searchingCodes.setCodeValue(valueFor);
                searchingCodes.setFullValue(valueFor);
                statisticalSearchingCodes.add(searchingCodes);
                oleItem.setStatisticalSearchingCode(statisticalSearchingCodes);
            }
        }
        /*oleItem.setCopyNumber(COPY_NUMBER);
        oleItem.setCopyNumberLabel(COPY_NUMBER_LABEL);*/
//
//        PhysicalLocation location = new PhysicalLocation();
//        location.getLocationStatus().add(getAttributeValue(profileAttributes, "locationStatus"));

//        LocationLevel locationLevel = new LocationLevel();
//        CodeOrIdentifier levelName = new CodeOrIdentifier();

//        levelName.setValue(getAttributeValue(profileAttributes, "levelName"));
//
//        TypeOrSource typeOrSource1 = new TypeOrSource();
//        typeOrSource1.setPointer("Pointer 1");
//        typeOrSource1.setText("Text for Level Name 1");
//        levelName.setTypeOrSource(typeOrSource1);
//
//        locationLevel.setLevelName(levelName);
//        location.getLocationLevel().add(locationLevel);
//
//        location.getCallNumberPrefix().add(getSubFieldValueFor(bibliographicRecord, dataField, "$k"));
//        location.getCallNumberSuffix().add(getSubFieldValueFor(bibliographicRecord, dataField, "$,"));

//        CodeOrIdentifier classification = new CodeOrIdentifier();
//        TypeOrSource typeOrSource4 = new TypeOrSource();
//        typeOrSource4.setPointer("Classification Pointer -1");
//        typeOrSource4.setText("Classification Text -1");
//        classification.setTypeOrSource(typeOrSource4);
//        classification.setValue(getClassificationOrShelvingSchemeSource(bibliographicRecord));
//        location.setClassificationOrShelvingSchemeSource(classification);

//        location.setClassificationPart(getSubFieldValueFor(bibliographicRecord, dataField, "$h"));
//        location.getItemPart().add(getSubFieldValueFor(bibliographicRecord, dataField, "$i"));
//        location.setShelvingControlNumber(getSubFieldValueFor(bibliographicRecord, dataField, "$j"));
//
//        oleItem.getLocation().add(location);

        //Eill
//        oleItem.getVendorLineItemIdentifier().add("Vendoer Line Item Identifier-1");

        //CallNumber

        CallNumber callNumber = new CallNumber();
        /*callNumber.setPrefix(getSubFieldValueFor(bibliographicRecord, dataField, "k"));
        callNumber.setClassificationPart(getSubFieldValueFor(bibliographicRecord, dataField, "h"));
        callNumber.setItemPart(getSubFieldValueFor(bibliographicRecord, dataField, "i"));
        callNumber.setType(getSubFieldValueFor(bibliographicRecord, dataField, "2"));*/
        callNumber.setNumber(getSubFieldValueFor(bibMarcRecord, "050", "a"));
        callNumber.setPrefix(getSubFieldValueFor(bibMarcRecord, "050", "b"));
        //String callNumberType = getSubFieldValueFor(bibliographicRecord, "985", "q") == null?CALL_NUMBER_TYPE:getSubFieldValueFor(bibliographicRecord, "985", "q");
        /*if(callNumberType != null){
            callNumber.setType(callNumberType);
        }
        else {
            callNumber.setType(CALL_NUMBER_TYPE);
        }*/

        callNumber.setType(getSubFieldValueFor(bibMarcRecord, "985", "q") == null ? CALL_NUMBER_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "q"));
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue(StringUtils.isBlank(getSubFieldValueFor(bibMarcRecord, "985", "q")) ? CALL_NUMBER_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "q"));
        callNumber.setShelvingScheme(shelvingScheme);

        oleItem.setCallNumber(callNumber);

        String locationLevelInstitution = getSubFieldValueFor(bibMarcRecord, "985", "a");
        String locationLevelLibrary = getSubFieldValueFor(bibMarcRecord, "985", "c") == null ? "Jrl" : getSubFieldValueFor(bibMarcRecord, "985", "c");
        String locationLevelShelving = getSubFieldValueFor(bibMarcRecord, "985", "d") == null ? "Gen" : getSubFieldValueFor(bibMarcRecord, "985", "d");

        /*Map<String,String> locationLevelMap = new HashMap<String,String>();
        locationLevelMap.put(locationLevelInstitution, "Institution");
        locationLevelMap.put(locationLevelLibrary, "Library");
        locationLevelMap.put(locationLevelShelving, "Shelving Location");*/

        String locationLevelName = locationLevelInstitution + "/" + locationLevelLibrary + "/" + locationLevelShelving;
        String locationLevelCode = "Institution" + "/" + "Library" + "/" + "Shelving Location";
        LocationLevel locationLevel = new LocationLevel();
        Location itemLocation = new Location();
        itemLocation.setLocationLevel(setLocationLevels(locationLevel, locationLevelCode, locationLevelName));
        itemLocation.setPrimary("true");
        itemLocation.setStatus("temporary");
        oleItem.setLocation(itemLocation);

        // Extension
       /* DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered(String.valueOf(df.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(df.format(new Date())));
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setFastAddFlag("false");
        additionalAttributes.setHarvestable("true");
        additionalAttributes.setStatus("n"); // new Record
        additionalAttributes.setAttribute("dateEntered", additionalAttributes.getDateEntered());
        additionalAttributes.setAttribute("lastUpdated", additionalAttributes.getLastUpdated());
        additionalAttributes.setAttribute("fastAddFlag", additionalAttributes.getFastAddFlag());
        additionalAttributes.setAttribute("supressFromPublic", additionalAttributes.getSupressFromPublic());
        additionalAttributes.setAttribute("harvestable", additionalAttributes.getHarvestable());
        additionalAttributes.setAttribute("status", additionalAttributes.getStatus());
        extension.getContent().add(additionalAttributes);
        oleItem.setExtension(extension);*/

        return oleItem;
    }

    public Item getOleItem(LineItemOrder lineItemOrder, BibMarcRecord bibMarcRecord, List<ProfileAttribute> profileAttributes) {

        DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, "852");
        String dataField = dataFieldForTag.getTag();

        Item oleItem = new Item();
        ItemType itemType = new ItemType();

        itemType.setCodeValue(getSubFieldValueFor(bibMarcRecord, "985", "t") == null ? ITEM_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "t"));
        itemType.setFullValue(getSubFieldValueFor(bibMarcRecord, "985", "t") == null ? ITEM_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "t"));
        oleItem.setItemType(itemType);
        if (null != lineItemOrder) {
            oleItem.setVendorLineItemIdentifier(lineItemOrder.getBuyerReferenceInformation().get(0).getBuyerLineItemReference().get(0).getOrderLineNumber());
        } else {
            oleItem.setVendorLineItemIdentifier(null);
        }

        oleItem.setCopyNumber(getSubFieldValueFor(bibMarcRecord, "985", "g") == null ? COPY_NUMBER : getSubFieldValueFor(bibMarcRecord, "985", "g"));
        oleItem.setCopyNumberLabel(COPY_NUMBER_LABEL);

        oleItem.setPurchaseOrderLineItemIdentifier("");

        ItemStatus itemStatus = new ItemStatus();
        String itemStatusValue = getSubFieldValueFor(bibMarcRecord, "985", "s") == null ? ITEM_STATUS : getSubFieldValueFor(bibMarcRecord, "985", "s");
        itemStatus.setCodeValue(itemStatusValue);
        itemStatus.setFullValue(itemStatusValue);
        oleItem.setItemStatus(itemStatus);

        String valueFor = getSubFieldValueFor(bibMarcRecord, dataField, "$4");
        if (null != valueFor) {
            List<StatisticalSearchingCode> statisticalSearchingCodes = new ArrayList<StatisticalSearchingCode>();
            StatisticalSearchingCode searchingCodes = new StatisticalSearchingCode();
            searchingCodes.setCodeValue(valueFor);
            searchingCodes.setFullValue(valueFor);
            statisticalSearchingCodes.add(searchingCodes);
            oleItem.setStatisticalSearchingCode(statisticalSearchingCodes);
        }

        CallNumber callNumber = new CallNumber();
        callNumber.setNumber(getSubFieldValueFor(bibMarcRecord, "050", "a"));
        callNumber.setPrefix(getSubFieldValueFor(bibMarcRecord, "050", "b"));

        callNumber.setType(getSubFieldValueFor(bibMarcRecord, "985", "q") == null ? CALL_NUMBER_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "q"));

        oleItem.setCallNumber(callNumber);

        String locationLevelInstitution = getSubFieldValueFor(bibMarcRecord, "985", "a");
        String locationLevelLibrary = getSubFieldValueFor(bibMarcRecord, "985", "c") == null ? "Jrl" : getSubFieldValueFor(bibMarcRecord, "985", "c");
        String locationLevelShelving = getSubFieldValueFor(bibMarcRecord, "985", "d") == null ? "Gen" : getSubFieldValueFor(bibMarcRecord, "985", "d");

        String locationLevelName = locationLevelInstitution + "/" + locationLevelLibrary + "/" + locationLevelShelving;
        String locationLevelCode = "Institution" + "/" + "Library" + "/" + "Shelving Location";
        LocationLevel locationLevel = new LocationLevel();
        Location itemLocation = new Location();
        itemLocation.setLocationLevel(setLocationLevels(locationLevel, locationLevelCode, locationLevelName));
        itemLocation.setPrimary("true");
        itemLocation.setStatus("temporary");
        oleItem.setLocation(itemLocation);

        // Extension
       /* DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Extension extension = new Extension();
        AdditionalAttributes additionalAttributes = new AdditionalAttributes();
        additionalAttributes.setDateEntered(String.valueOf(df.format(new Date())));
        additionalAttributes.setLastUpdated(String.valueOf(df.format(new Date())));
        additionalAttributes.setSupressFromPublic("false");
        additionalAttributes.setFastAddFlag("false");
        additionalAttributes.setHarvestable("true");
        additionalAttributes.setStatus("n"); // new Record
        additionalAttributes.setAttribute("dateEntered", additionalAttributes.getDateEntered());
        additionalAttributes.setAttribute("lastUpdated", additionalAttributes.getLastUpdated());
        additionalAttributes.setAttribute("fastAddFlag", additionalAttributes.getFastAddFlag());
        additionalAttributes.setAttribute("supressFromPublic", additionalAttributes.getSupressFromPublic());
        additionalAttributes.setAttribute("harvestable", additionalAttributes.getHarvestable());
        additionalAttributes.setAttribute("status", additionalAttributes.getStatus());
        extension.getContent().add(additionalAttributes);
        oleItem.setExtension(extension);*/

        return oleItem;
    }

    public LocationLevel setLocationLevels(LocationLevel locationLevel, String locationLevelCode, String locationLevelName) {

        String[] levelNames = locationLevelName.split("/");
        String[] levels = locationLevelCode.split("/");
        locationLevel.setName(levelNames[0]);
        locationLevel.setLevel(levels[0]);
        String levlName = "";
        String levl = "";
        if (locationLevelName.contains("/") && locationLevelCode.contains("/")) {
            levlName = locationLevelName.replace(levelNames[0] + "/", "");
            levl = locationLevelCode.replace(levels[0] + "/", "");
        } else {
            levlName = locationLevelName.replace(levelNames[0], "");
            levl = locationLevelCode.replace(levels[0], "");
        }
        if ((levlName != null && !levlName.equals("")) && (levl != null && !levl.equals(""))) {
            LocationLevel newLocationLevel = new LocationLevel();
            locationLevel.setLocationLevel(setLocationLevels(newLocationLevel, levl, levlName));
        }
        return locationLevel;
    }

    private String getAttributeValue(List<ProfileAttribute> profileAttributes, String attributeName) {
        for (Iterator<ProfileAttribute> iterator = profileAttributes.iterator(); iterator.hasNext(); ) {
            ProfileAttribute attribute = iterator.next();
            if (attribute.getAttributeName().equals(attributeName)) {
                return attribute.getAttributeValue();
            }
        }
        return null;
    }

    private String getSubFieldValueFor(BibMarcRecord bibMarcRecord, String dataField, String tag) {
        String subFieldValue = null;
        DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, dataField);
        if (null != dataFieldForTag) {
            List<SubField> subfields = dataFieldForTag.getSubFields();
            for (Iterator<SubField> iterator = subfields.iterator(); iterator.hasNext(); ) {
                SubField marcSubField = iterator.next();
                if (marcSubField.getCode().equals(tag)) {
                    return subFieldValue = marcSubField.getValue();
                }
            }
        }
        return subFieldValue;
    }


    public DataField getDataFieldForTag(BibMarcRecord bibMarcRecord, String tag) {
        for (Iterator<DataField> iterator = bibMarcRecord.getDataFields().iterator(); iterator.hasNext(); ) {
            DataField marcDataField = iterator.next();
            if (marcDataField.getTag().equalsIgnoreCase(tag)) {
                return marcDataField;
            }
        }
        return null;
    }


    public String toXML(Item oleItem) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(Item.class);
        String xml = xs.toXML(oleItem);
        return xml;
    }

    public String getClassificationOrShelvingSchemeSource(BibMarcRecord bibMarcRecord) {
        DataField dataFieldForTag = getDataFieldForTag(bibMarcRecord, "852");
        if (dataFieldForTag.getInd1().equals("7")) {
            List<SubField> subfields = dataFieldForTag.getSubFields();
            for (Iterator<SubField> iterator = subfields.iterator(); iterator.hasNext(); ) {
                SubField subField = iterator.next();
                if (subField.getCode().equals("2")) {
                    return subField.getValue();
                }
            }
        }

        return "";
    }
}
