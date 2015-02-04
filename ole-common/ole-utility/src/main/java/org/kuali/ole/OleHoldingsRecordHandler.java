package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.Note;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.pojo.ProfileAttribute;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/10/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleHoldingsRecordHandler {
    Map<String, String> recordTypes = new HashMap<String, String>();
    Map<String, String> encodingLevels = new HashMap<String, String>();
    public static final String CALL_NUMBER_TYPE = "LC";

    public OleHoldingsRecordHandler() {
        recordTypes = new HashMap<String, String>();
        recordTypes.put("u", "Unknown");
        recordTypes.put("s", "Single-part item holdings");
        recordTypes.put("y", "Serial item holdings");

        encodingLevels.put("1", "Holdings level 1");
        encodingLevels.put("2", "Holdings level 2");
        encodingLevels.put("3", "Holdings level 3");
        encodingLevels.put("4", "Holdings level 4");
        encodingLevels.put("5", "Holdings level 5");
        encodingLevels.put("m", "Mixed level");
        encodingLevels.put("u", "Unknown");
        encodingLevels.put("z", "Other level");
    }

    public String generateXML(List<ProfileAttribute> profileAttributes) {
        OleHoldings oleHolding = getOleHoldings(profileAttributes);
        return toXML(oleHolding);
    }

    public OleHoldings getOleHoldings(List<ProfileAttribute> profileAttributes) {
        OleHoldings oleHolding = new OleHoldings();
        /*oleHolding.setRecordType(resolveRecordType(getAttributeValue(profileAttributes, "recordType")));
        oleHolding.setEncodingLevel(resolveEncodingLevel(getAttributeValue(profileAttributes, "encodingLevel")));
        oleHolding.setReceiptStatus(getAttributeValue(profileAttributes, "recieptStatus"));
        oleHolding.setAcquisitionMethod(getAttributeValue(profileAttributes, "acquisitionMethod"));
        oleHolding.setReproductionPolicy(getAttributeValue(profileAttributes, "generalRetentionPolicy"));
        SpecificRetentionPolicy specificRetentionPolicy = new SpecificRetentionPolicy();
        specificRetentionPolicy.setPolicyType(getAttributeValue(profileAttributes, "policyType"));
        oleHolding.setSpecificRetentionPolicy(specificRetentionPolicy);
        oleHolding.setGeneralRetentionPolicy(getAttributeValue(profileAttributes, "generalRetentionPolicy"));*/

        /*oleHolding.setReceiptStatus(getAttributeValue(profileAttributes, "receiptStatus"));

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
        oleHolding.setExtension(extension);*/
        return oleHolding;
    }

    public OleHoldings getOleHoldings(BibMarcRecord bibMarcRecord, List<ProfileAttribute> profileAttributes) {
        OleHoldings oleHolding = new OleHoldings();
        /*oleHolding.setRecordType(resolveRecordType(getAttributeValue(profileAttributes, "recordType")));
        oleHolding.setEncodingLevel(resolveEncodingLevel(getAttributeValue(profileAttributes, "encodingLevel")));
        oleHolding.setReceiptStatus(getAttributeValue(profileAttributes, "recieptStatus"));
        oleHolding.setAcquisitionMethod(getAttributeValue(profileAttributes, "acquisitionMethod"));
        oleHolding.setReproductionPolicy(getAttributeValue(profileAttributes, "generalRetentionPolicy"));
        SpecificRetentionPolicy specificRetentionPolicy = new SpecificRetentionPolicy();
        specificRetentionPolicy.setPolicyType(getAttributeValue(profileAttributes, "policyType"));
        oleHolding.setSpecificRetentionPolicy(specificRetentionPolicy);
        oleHolding.setGeneralRetentionPolicy(getAttributeValue(profileAttributes, "generalRetentionPolicy"));*/

        oleHolding.setReceiptStatus(getAttributeValue(profileAttributes, "receiptStatus"));

        CallNumber callNumber = new CallNumber();
        callNumber.setNumber(getSubFieldValueFor(bibMarcRecord, "050", "a"));
        callNumber.setPrefix(getSubFieldValueFor(bibMarcRecord, "050", "b"));
        callNumber.setType(getSubFieldValueFor(bibMarcRecord, "985", "q") == null ? CALL_NUMBER_TYPE : getSubFieldValueFor(bibMarcRecord, "985", "q"));

        oleHolding.setCallNumber(callNumber);

        Note note = new Note();
        note.setValue(getSubFieldValueFor(bibMarcRecord, "856", "x"));
        List<Note> notes = new ArrayList<Note>();
        notes.add(note);
        oleHolding.setNote(notes);

        //oleHolding.setReceiptStatus(getSubFieldValueFor(bibliographicRecord, "856", "i"));
/*
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
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
        oleHolding.setExtension(extension);*/
        return oleHolding;
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

    private String resolveEncodingLevel(String encodingLevel) {
        return encodingLevels.get(encodingLevel);
    }

    private String resolveRecordType(String recordType) {
        return recordTypes.get(recordType);
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

    public String toXML(OleHoldings oleHolding) {
        XStream xs = new XStream();
        xs.autodetectAnnotations(true);
        xs.processAnnotations(OleHoldings.class);
        String xml = xs.toXML(oleHolding);
        return xml;
    }

}
