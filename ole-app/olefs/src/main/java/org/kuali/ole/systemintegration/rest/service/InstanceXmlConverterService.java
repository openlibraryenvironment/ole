package org.kuali.ole.systemintegration.rest.service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import org.kuali.ole.docstore.common.document.content.instance.*;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.systemintegration.rest.bo.*;


/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 7:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceXmlConverterService {


    /**
     * @param instanceCollection
     * @return the required format of the instance xml
     */
    public String generateInstanceCollectionsXml(InstanceCollection instanceCollection) {
        XStream stream = new XStream();
        stream = generateInstanceCollectionXml(stream);
        String xml = stream.toXML(instanceCollection);
        xml = xml.replace("<string>", "");
        xml = xml.replace("</string>", "");
        String output = xml.replace("<ole:instanceCollection>", "<ole:instanceCollection xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "  xsi:schemaLocation=\"http://ole.kuali.org/standards/ole-instance instance9.1.1-circulation.xsd\"\n" +
                "  xmlns:circ=\"http://ole.kuali.org/standards/ole-instance-circulation\"\n" +
                "  xmlns:ole=\"http://ole.kuali.org/standards/ole-instance\">");
        return output;
    }

    /**
     * @param instanceCollection
     * @return the json content of the instance collection object
     */
    public String generateInstanceCollectionsJSON(InstanceCollection instanceCollection) {
        XStream stream = new XStream(new JettisonMappedXmlDriver());
        stream.autodetectAnnotations(true);
        stream.processAnnotations(InstanceCollection.class);
        stream.setMode(XStream.NO_REFERENCES);
        stream = generateInstanceCollectionXml(stream);

        String jsonContent = stream.toXML(instanceCollection);
        return jsonContent;

    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Location object to xml  conversion
     */
    private XStream generateLocationXml(XStream xstream) {
        xstream.alias("ole:location", Location.class);
        xstream.aliasField("ole:primary", Location.class, "primary");
        xstream.aliasField("ole:status", Location.class, "status");
        xstream.aliasField("circ:locationLevel", Location.class, "locationLevel");
        xstream = generateLocationLevelXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the LocationLevel object to xml  conversion
     */
    private XStream generateLocationLevelXml(XStream xstream) {
        xstream.alias("circ:locationLevel", LocationLevel.class);
        xstream.aliasField("ole:name", LocationLevel.class, "name");
        xstream.aliasField("ole:level", LocationLevel.class, "level");
        xstream.aliasField("circ:locationLevel", LocationLevel.class, "locationLevel");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Note object to xml  conversion
     */
    private XStream generateNoteXml(XStream xstream) {
        xstream.alias("ole:note", Note.class);
        xstream.aliasField("ole:value", Note.class, "value");
        //  xstream.aliasField("ole:type",Note.class,"type");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Uri object to xml  conversion
     */
    private XStream generateUriXml(XStream xstream) {
        xstream.alias("ole:uri", Uri.class);
        xstream.aliasField("ole:value", Uri.class, "value");
        xstream.aliasField("ole:resolvable", Uri.class, "resolvable");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the ItemType object to xml  conversion
     */
    private XStream generateItemTypeXml(XStream xstream) {
        xstream.alias("ole:itemType", ItemType.class);
        xstream.aliasField("ole:codeValue", ItemType.class, "codeValue");
        xstream.omitField(ItemType.class, "fullValue");
        xstream.aliasField("ole:fullValue", ItemType.class, "fullValue");
        xstream.aliasField("ole:typeOrSource", ItemType.class, "typeOrSource");
        xstream = generateTypeOrSourceXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the TypeOrSource object to xml  conversion
     */
    private XStream generateTypeOrSourceXml(XStream xstream) {
        xstream.alias("typeOrSource", TypeOrSource.class);
        xstream.aliasField("ole:pointer", TypeOrSource.class, "pointer");
        xstream.aliasField("ole:text", TypeOrSource.class, "text");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the StatisticalSearchingCode object to xml  conversion
     */
    private XStream generateStatisticalSearchingCodeXml(XStream xstream) {
        xstream.alias("ole:statisticalSearchingCode", StatisticalSearchingCode.class);
        xstream.aliasField("ole:codeValue", StatisticalSearchingCode.class, "codeValue");
        xstream.aliasField("ole:fullValue", StatisticalSearchingCode.class, "fullValue");
        xstream.aliasField("ole:typeOrSource", StatisticalSearchingCode.class, "typeOrSource");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Identifier object to xml  conversion
     */
    private XStream generateIdentifierXml(XStream xstream) {
        xstream.alias("ole:identifier", Identifier.class);
        xstream.aliasField("ole:identifierValue", Identifier.class, "identifierValue");
        xstream.aliasField("ole:source", Identifier.class, "source");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the AccessInformation object to xml  conversion
     */
    private XStream generateAccessInformationXml(XStream xstream) {
        xstream.alias("ole:accessInformation", AccessInformation.class);
        xstream.aliasField("ole:barcode", AccessInformation.class, "barcode");
        xstream.aliasField("ole:uri", AccessInformation.class, "uri");
        xstream = generateUriXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the SourceHoldings object to xml  conversion
     */
    private XStream generateSourceHoldingsXml(XStream xstream) {
        xstream.alias("ole:sourceHoldings", SourceHoldings.class);
        xstream.aliasField("ole:holdingsIdentifier", SourceHoldings.class, "holdingsIdentifier");
        xstream.aliasField("ole:name", SourceHoldings.class, "name");
        // xstream.omitField(SourceHoldings.class,"primary");
        xstream.aliasField("ole:primary", SourceHoldings.class, "primary");
        xstream.aliasField("ole:extension", SourceHoldings.class, "extension");
        xstream.aliasField("ole:holdings", SourceHoldings.class, "holdings");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the ShelvingOrder object to xml  conversion
     */
    private XStream generateShelvingOrderXml(XStream xstream) {
        xstream.alias("ole:shelvingOrder", ShelvingOrder.class);
        xstream.aliasField("ole:codeValue", ShelvingOrder.class, "codeValue");
        xstream.aliasField("ole:fullValue", ShelvingOrder.class, "fullValue");
        xstream.aliasField("ole:typeOrSource", ShelvingOrder.class, "typeOrSource");
        xstream.omitField(ShelvingOrder.class, "fullValue");
        xstream.omitField(ShelvingOrder.class, "typeOrSource");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the ShelvingScheme object to xml  conversion
     */
    private XStream generateShelvingSchemeXml(XStream xstream) {
        xstream.alias("ole:shelvingScheme", ShelvingScheme.class);
        xstream.aliasField("ole:codeValue", ShelvingScheme.class, "codeValue");
        xstream.aliasField("ole:fullValue", ShelvingScheme.class, "fullValue");
        xstream.aliasField("ole:typeOrSource", ShelvingScheme.class, "typeOrSource");
        xstream.omitField(ShelvingScheme.class, "fullValue");
        xstream.omitField(ShelvingScheme.class, "typeOrSource");
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the CallNumber object to xml  conversion
     */
    private XStream generateCallNumberXml(XStream xstream) {
        xstream.alias("ole:callNumber", CallNumber.class);
        xstream.aliasField("ole:type", CallNumber.class, "type");
        xstream.aliasField("ole:prefix", CallNumber.class, "prefix");
        xstream.aliasField("ole:number", CallNumber.class, "number");
        xstream.aliasField("ole:classificationPart", CallNumber.class, "classificationPart");
        xstream.aliasField("ole:itemPart", CallNumber.class, "itemPart");
        xstream.omitField(CallNumber.class, "classificationPart");
        xstream.omitField(CallNumber.class, "itemPart");
        xstream.aliasField("ole:shelvingScheme", CallNumber.class, "shelvingScheme");
        xstream.aliasField("ole:shelvingOrder", CallNumber.class, "shelvingOrder");
        xstream = generateShelvingOrderXml(xstream);
        xstream = generateShelvingSchemeXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the AdditionalAttributes object to xml  conversion
     */
    private XStream generateAdditionalAttributesXml(XStream xstream) {
        xstream.alias("ole:additionalAttributes", AdditionalAttributes.class);
        xstream.aliasField("ole:dateEntered", AdditionalAttributes.class, "dateEntered");
        xstream.aliasField("ole:lastUpdated", AdditionalAttributes.class, "lastUpdated");
        xstream.aliasField("ole:fastAddFlag", AdditionalAttributes.class, "fastAddFlag");
        xstream.aliasField("ole:supressFromPublic", AdditionalAttributes.class, "supressFromPublic");
        xstream.aliasField("ole:harvestable", AdditionalAttributes.class, "harvestable");
        xstream.aliasField("ole:fastAddFlag", AdditionalAttributes.class, "status");
        xstream.aliasField("ole:supressFromPublic", AdditionalAttributes.class, "createdBy");
        xstream.aliasField("ole:harvestable", AdditionalAttributes.class, "updatedBy");
        //map attribute map
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Extension object to xml  conversion
     */
    private XStream generateExtensionXml(XStream xstream) {
        xstream.alias("ole:extension", Extension.class);
        xstream.aliasField("ole:displayLabel", Extension.class, "displayLabel");
        xstream.omitField(Extension.class, "displayLabel");
        xstream.aliasField("ole:additionalAttributes", Extension.class, "content");
        xstream.addImplicitCollection(Extension.class, "content", AdditionalAttributes.class);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the ExtentOfOwnership object to xml  conversion
     */
    private XStream generateExtentOfOwnershipXml(XStream xstream) {
        xstream.alias("ole:extentOfOwnership", ExtentOfOwnership.class);
        xstream.aliasField("ole:textualHoldings", ExtentOfOwnership.class, "textualHoldings");
        xstream.aliasField("ole:type", ExtentOfOwnership.class, "type");
        xstream.aliasField("ole:notes", ExtentOfOwnership.class, "note");
        xstream.aliasAttribute(Note.class, "type", "type");
        xstream.addImplicitCollection(ExtentOfOwnership.class, "note", Note.class);
        xstream = generateNoteXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the FormerIdentifier object to xml  conversion
     */
    private XStream generateFormerIdentifierXml(XStream xstream) {
        xstream.alias("ole:formerIdentifier", FormerIdentifier.class);
        xstream.aliasField("ole:identifierType", FormerIdentifier.class, "identifierType");
        xstream.aliasField("ole:identifier", FormerIdentifier.class, "identifier");
        xstream = generateIdentifierXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the OleHoldings object to xml  conversion
     */
    private XStream generateOleHoldingsXml(XStream xstream) {
        xstream.alias("ole:oleHoldings", OleHoldings.class);
        xstream.aliasField("ole:holdingsIdentifier", OleHoldings.class, "holdingsIdentifier");
        xstream.aliasField("ole:receiptStatus", OleHoldings.class, "receiptStatus");
        //  xstream.omitField(OleHoldings.class,"primary");
        xstream.addImplicitCollection(OleHoldings.class, "extentOfOwnership", ExtentOfOwnership.class);
        xstream.addImplicitCollection(OleHoldings.class, "uri", Uri.class);
        xstream.addImplicitCollection(OleHoldings.class, "note", Note.class);
        xstream.aliasField("ole:extension", OleHoldings.class, "extension");
        xstream.aliasField("ole:callNumber", OleHoldings.class, "callNumber");
        xstream.aliasField("ole:location", OleHoldings.class, "location");
        xstream = generateLocationXml(xstream);
        xstream = generateExtentOfOwnershipXml(xstream);
        xstream = generateHoldingsSerialReceivingXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Item object to xml  conversion
     */
    private XStream generateItemXml(XStream xstream) {
        xstream.alias("ole:item", Item.class);
        xstream.aliasField("ole:itemIdentifier", Item.class, "itemIdentifier");
        xstream.aliasField("ole:purchaseOrderLineItemIdentifier", Item.class, "purchaseOrderLineItemIdentifier");
        xstream.aliasField("ole:vendorLineItemIdentifier", Item.class, "vendorLineItemIdentifier");
        xstream.aliasField("ole:accessInformation", Item.class, "accessInformation");
       // xstream.omitField(Item.class, "location");
        xstream = generateAccessInformationXml(xstream);
        xstream.aliasField("circ:itemType", Item.class, "itemType");
        xstream = generateItemTypeXml(xstream);
        xstream.aliasField("ole:location", Item.class, "location");
        xstream = generateLocationXml(xstream);
        xstream.aliasField("ole:note", Item.class, "note");
        xstream = generateNoteXml(xstream);
        xstream.aliasField("circ:temporaryItemType", Item.class, "temporaryItemType");
        xstream = generateItemTypeXml(xstream);
        xstream.aliasField("ole:callNumber", Item.class, "callNumber");
        xstream = generateCallNumberXml(xstream);
        xstream.aliasField("ole:extension", Item.class, "extension");
        xstream = generateExtensionXml(xstream);
        xstream = generateStatisticalSearchingCodeXml(xstream);
        xstream = generateAdditionalAttributesXml(xstream);
        xstream.aliasField("ole:additionalAttributes", Item.class, "content");
        xstream.aliasField("ole:barcodeARSL", Item.class, "barcodeARSL");
        xstream.aliasField("ole:copyNumber", Item.class, "copyNumber");
        xstream.aliasField("ole:copyNumberLabel", Item.class, "copyNumberLabel");
        xstream.aliasField("ole:volumeNumber", Item.class, "volumeNumber");
        xstream.aliasField("ole:volumeNumberLabel", Item.class, "volumeNumberLabel");
        xstream.aliasField("ole:enumeration", Item.class, "enumeration");
        xstream.aliasField("ole:chronology", Item.class, "chronology");
        xstream.aliasField("ole:fund", Item.class, "fund");
        xstream.aliasField("ole:donorPublicDisplay", Item.class, "donorPublicDisplay");
        xstream.aliasField("ole:donorNote", Item.class, "donorNote");
        xstream.aliasField("ole:price", Item.class, "price");
        xstream.aliasField("ole:numberOfPieces", Item.class, "numberOfPieces");
        xstream.aliasField("circ:itemStatus", Item.class, "itemStatus");
        xstream.aliasField("ole:itemStatusEffectiveDate", Item.class, "itemStatusEffectiveDate");
        xstream.aliasField("ole:checkinNote", Item.class, "checkinNote");
        xstream.aliasField("ole:staffOnlyFlag", Item.class, "staffOnlyFlag");
        xstream.aliasField("ole:fastAddFlag", Item.class, "fastAddFlag");
        xstream.omitField(Item.class, "analytic");
        xstream.omitField(Item.class, "resourceIdentifier");
        xstream.aliasField("ole:analytic", Item.class, "analytic");
        xstream.aliasField("ole:resourceIdentifier", Item.class, "resourceIdentifier");
        xstream.aliasField("ole:formerIdentifiers", Item.class, "formerIdentifier");
        xstream.aliasField("ole:statisticalSearchingCodes", Item.class, "statisticalSearchingCode");
        xstream.addImplicitCollection(Item.class, "formerIdentifier", FormerIdentifier.class);
        xstream.addImplicitCollection(Item.class, "statisticalSearchingCode", StatisticalSearchingCode.class);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Items object to xml  conversion
     */
    public XStream generateItemsXml(XStream xstream) {
        xstream.alias("ole:items", Items.class);
        xstream.aliasField("ole:items", Items.class, "item");
        xstream = generateItemXml(xstream);
        xstream.addImplicitCollection(Items.class, "item", Item.class);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the Instance object to xml  conversion
     */
    public XStream generateInstanceXml(XStream xstream) {
        xstream.alias("ole:instance", Instance.class);
        xstream.aliasField("ole:instanceIdentifier", Instance.class, "instanceIdentifier");
        xstream.aliasField("ole:resourceIdentifier", Instance.class, "resourceIdentifier");
        xstream.aliasField("ole:oleHoldings", Instance.class, "oleHoldings");
        xstream.aliasAttribute(OleHoldings.class, "primary", "primary");
        xstream.aliasField("ole:sourceHoldings", Instance.class, "sourceHoldings");
        xstream.aliasField("ole:items", Instance.class, "items");
        xstream.aliasField("ole:extension", Instance.class, "extension");
        xstream.omitField(Instance.class, "extension");
        xstream.omitField(Instance.class, "formerResourceIdentifier");
        xstream.aliasField("ole:formerResourceIdentifiers", Instance.class, "formerResourceIdentifier");
        xstream = generateItemsXml(xstream);
        xstream = generateFormerIdentifierXml(xstream);
        xstream = generateOleHoldingsXml(xstream);
        xstream = generateSourceHoldingsXml(xstream);
        xstream.addImplicitCollection(Instance.class, "formerResourceIdentifier", FormerIdentifier.class);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the InstanceCollection object to xml  conversion
     */
    public XStream generateInstanceCollectionXml(XStream xstream) {
        xstream.alias("ole:instanceCollection", InstanceCollection.class);
        xstream.aliasField("ole:instances", Instance.class, "instance");
        xstream.addImplicitCollection(InstanceCollection.class, "instance", Instance.class);
        xstream = generateInstanceXml(xstream);
        return xstream;
    }

    /**
     * @param xstream
     * @return the xstream object with the information for  the ExtentOfOwnership object to xml  conversion
     */
    private XStream generateHoldingsSerialReceivingXml(XStream xstream) {

        xstream.alias("ole:HoldingsSerialReceiving", HoldingsSerialReceiving.class);
        xstream.aliasField("ole:serialReceiving", HoldingsSerialReceiving.class, "oleSerialReceiving");
        xstream.aliasField("ole:callNumber", SerialReceiving.class, "callNumber");
        xstream.aliasField("ole:unboundLocation", SerialReceiving.class, "unboundLocation");
        xstream.aliasField("ole:mains", HoldingsSerialHistory.class, "oleSerialReceivingMainList");
        xstream.aliasField("ole:indexs", HoldingsSerialHistory.class, "oleSerialReceivingIndexList");
        xstream.aliasField("ole:supplementaries", HoldingsSerialHistory.class, "oleSerialReceivingSupplementList");
        xstream.aliasField("ole:serialReceivingHistory", SerialReceiving.class, "oleHoldingsSerialHistory");
        xstream.alias("ole:main", SerialReceivingMain.class);
        xstream.alias("ole:index", SerialReceivingIndex.class);
        xstream.alias("ole:supplementary",SerialReceivingSupplement.class);
        xstream.alias("ole:serialReceivingHistory",HoldingsSerialHistory.class);
        xstream.aliasField("ole:enumerationCaption",SerialReceivingMain.class, "enumerationCaption");
        xstream.aliasField("ole:chronologyCaption", SerialReceivingMain.class, "chronologyCaption");
        xstream.aliasField("ole:publicReceiptNote", SerialReceivingMain.class, "publicReceiptNote");
        xstream.aliasField("ole:enumerationCaption",SerialReceivingIndex.class, "enumerationCaption");
        xstream.aliasField("ole:chronologyCaption", SerialReceivingIndex.class, "chronologyCaption");
        xstream.aliasField("ole:publicReceiptNote", SerialReceivingIndex.class, "publicReceiptNote");
        xstream.aliasField("ole:enumerationCaption",SerialReceivingSupplement.class, "enumerationCaption");
        xstream.aliasField("ole:chronologyCaption", SerialReceivingSupplement.class, "chronologyCaption");
        xstream.aliasField("ole:publicReceiptNote", SerialReceivingSupplement.class, "publicReceiptNote");
        /*xstream.aliasField("ole:boundLocation", SerialReceiving.class, "boundLocation");
        xstream.aliasField("ole:receivingRecordType", SerialReceiving.class, "receivingRecordType");
        xstream.aliasField("ole:claim", SerialReceiving.class, "claim");
        xstream.aliasField("ole:serialReceivingRecordId", SerialReceiving.class, "serialReceivingRecordId");
        xstream.aliasField("ole:claimIntervalInformation", SerialReceiving.class, "claimIntervalInformation");
        xstream.aliasField("ole:createItem", SerialReceiving.class, "createItem");
        xstream.aliasField("ole:generalReceivingNote", SerialReceiving.class, "generalReceivingNote");
        xstream.aliasField("ole:poId", SerialReceiving.class, "poId");
        xstream.aliasField("ole:printLabel", SerialReceiving.class, "printLabel");
        xstream.aliasField("ole:publicDisplay", SerialReceiving.class, "publicDisplay");
        xstream.aliasField("ole:subscriptionStatus", SerialReceiving.class, "subscriptionStatus");
        xstream.aliasField("ole:serialReceiptLocation", SerialReceiving.class, "serialReceiptLocation");
        xstream.aliasField("ole:serialReceivingRecord", SerialReceiving.class, "serialReceivingRecord");
        xstream.aliasField("ole:treatmentInstructionNote", SerialReceiving.class, "treatmentInstructionNote");
        xstream.aliasField("ole:urgentNote", SerialReceiving.class, "urgentNote");
        xstream.aliasField("ole:vendorId", SerialReceiving.class, "vendorId");
        xstream.aliasField("ole:createDate", SerialReceiving.class, "createDate");
        xstream.aliasField("ole:urgentNote", SerialReceiving.class, "operatorId");
        xstream.aliasField("ole:vendorId", SerialReceiving.class, "machineId");
        xstream.aliasField("ole:createDate", SerialReceiving.class, "subscriptionStatusDate");
        xstream.addImplicitCollection(OLESerialReceiving.class, "oleSerialReceivingMainList");
        xstream.addImplicitCollection(OLESerialReceiving.class, "oleSerialReceivingIndexList");
        xstream.addImplicitCollection(OLESerialReceiving.class, "oleSerialReceivingSupplementList");*/

        return xstream;
    }


}
