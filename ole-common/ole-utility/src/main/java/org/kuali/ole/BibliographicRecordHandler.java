package org.kuali.ole;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.ControlField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.DataField;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.LeaderTag;
import org.kuali.ole.docstore.model.xmlpojo.work.bib.marc.SubField;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.ControlFieldConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.DataFieldConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.LeaderConverter;
import org.kuali.ole.docstore.model.xstream.work.bib.marc.SubFieldConverter;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.bib.Collection;


import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibliographicRecordHandler {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(BibliographicRecordHandler.class);
    private Map<String, String> valuesMap = new HashMap<String, String>();
    private StringBuilder errBuilder;
    private static final String ERR_BIB = "Error BIB:: ";
    private static final String TIME_STAMP = " ::TIME STAMP:: ";
    private static final String ERR_CAUSE = " ::Error Caused:: ";
    private static final String lineSeparator = System.getProperty("line.separator");
    private int errCnt;
    private int successCnt;
    private final static String RECORD = "record";
    private final static String CONTROLFIELD = "controlfield";
    private final static String DATAFIELD = "datafield";
    private final static String SUBFIELD = "subfield";
    private final static String CONTROLFIELDS = "controlfields";
    private final static String DATAFIELDS = "datafields";
    private final static String SUBFIELDS = "subFields";
    private final static String LEADER = "leader";

    public BibliographicRecordHandler() {
    }// default Constructor

    public BibliographicRecordHandler(StringBuilder errBuilder) {
        this.errBuilder = errBuilder;
    }

    /**
     * Converts given bib record to xml returns null if any error occured
     * @param bibliographicRecord
     * @return
     */
    public String generateXML(BibliographicRecord bibliographicRecord) {
        StringBuffer stringBuffer = new StringBuffer();
        try{
            stringBuffer.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
            stringBuffer.append("\n");

            XStream xStream = new XStream();
            xStream.alias(LEADER, LeaderTag.class);
            xStream.alias(RECORD, BibliographicRecord.class);
            xStream.alias(CONTROLFIELD, ControlField.class);
            xStream.alias(DATAFIELD, DataField.class);
            xStream.alias(SUBFIELD, SubField.class);
            xStream.addImplicitCollection(BibliographicRecord.class, DATAFIELDS, DataField.class);
            xStream.addImplicitCollection(DataField.class, SUBFIELDS,SubField.class);
            xStream.addImplicitCollection(BibliographicRecord.class, CONTROLFIELDS,ControlField.class);
            xStream.registerConverter(new DataFieldConverter());
            xStream.registerConverter(new ControlFieldConverter());
            xStream.registerConverter(new LeaderConverter());
            String xml = xStream.toXML(bibliographicRecord);
            stringBuffer.append(xml);
            stringBuffer.append("\n");
            stringBuffer.append("</collection>");
            successCnt++;
        } catch (Exception ex) {
            LOG.error("Error while converting BibliographicRecord to string for Bib Record id:: " + bibliographicRecord.getRecordId(), ex);
            errBuilder.append(ERR_BIB).append(bibliographicRecord.getRecordId()).append(TIME_STAMP)
                    .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateXML() for mrc").append(lineSeparator);
            errCnt++;
            return null;
        }
        return stringBuffer.toString();
    }

    /**
     * generates the xml string with all the records under one collection tag
     * @param bibliographicRecord
     * @return
     */
    public String generateXML(List<BibliographicRecord> bibliographicRecord) {
        StringBuffer stringBuffer = new StringBuffer();
        //stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        stringBuffer.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        for (BibliographicRecord record : bibliographicRecord) {
            try {
                if (record != null) {
                    stringBuffer.append(lineSeparator);

                    XStream xStream = new XStream();
                    xStream.alias(LEADER, LeaderTag.class);
                    xStream.alias(RECORD, BibliographicRecord.class);
                    xStream.alias(CONTROLFIELD, ControlField.class);
                    xStream.alias(DATAFIELD, DataField.class);
                    xStream.alias(SUBFIELD, SubField.class);
                    xStream.addImplicitCollection(BibliographicRecord.class, DATAFIELDS, DataField.class);
                    xStream.addImplicitCollection(DataField.class, SUBFIELDS, SubField.class);
                    xStream.addImplicitCollection(BibliographicRecord.class, CONTROLFIELDS, ControlField.class);
                    xStream.registerConverter(new DataFieldConverter());
                    xStream.registerConverter(new ControlFieldConverter());
                    xStream.registerConverter(new LeaderConverter());
                    String xml = xStream.toXML(record);
                    stringBuffer.append(xml);
                    successCnt++;
                }
            } catch (Exception ex) {
                LOG.error("Error while converting BibliographicRecord to string for Bib Record id:: " + record.getRecordId(), ex);
                errBuilder.append(ERR_BIB).append(record.getRecordId()).append(TIME_STAMP)
                        .append(new Date()).append(ERR_CAUSE).append(ex.getMessage()).append(" ::At:: ").append("generateXML()").append(lineSeparator);
                errCnt++;
            }
        }
        stringBuffer.append(lineSeparator);
        stringBuffer.append("</collection>");
        return stringBuffer.toString();
    }

    public BibliographicRecord getModifiedBibWithout8xxFields(BibliographicRecord bibliographicRecord) {
        List<DataField> dataFields = new ArrayList<DataField>();
        BibliographicRecord newBibliographicRecord = new BibliographicRecord();
        newBibliographicRecord.setLeader(bibliographicRecord.getLeader());
        newBibliographicRecord.setControlfields(bibliographicRecord.getControlfields());
        List<DataField> datafields = bibliographicRecord.getDatafields();
        for (Iterator<DataField> iterator1 = datafields.iterator(); iterator1.hasNext(); ) {
            DataField dataField = iterator1.next();
            if (!dataField.getTag().contains("8")) {
                dataFields.add(dataField);
            }
        }
        newBibliographicRecord.setDatafields(dataFields);

        return newBibliographicRecord;
    }

    public Collection fromXML(String marcXMLContent) {
        XStream xStream = new XStream();
        xStream.alias("collection", Collection.class);
        xStream.alias(LEADER, LeaderTag.class);
        xStream.alias("record", BibliographicRecord.class);
        xStream.alias("controlfield", ControlField.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.addImplicitCollection(BibliographicRecord.class, "datafields", DataField.class);
        xStream.addImplicitCollection(BibliographicRecord.class, "controlfields", ControlField.class);
        xStream.addImplicitCollection(Collection.class, "collection");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new SubFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        xStream.registerConverter(new LeaderConverter());
        return (Collection) xStream.fromXML(marcXMLContent);
    }

   /* public Map<String, String> valuesMapFromXML(String marcXMLContent) {
        Collection bibliographicRecordCollection = fromXML(marcXMLContent);
        List<BibliographicRecord> records = bibliographicRecordCollection.getRecords();
        for (Iterator<BibliographicRecord> iterator = records.iterator(); iterator.hasNext(); ) {
            BibliographicRecord record = iterator.next();
            valuesMap.put("leader", record.getLeader());
            List<ControlField> controlFields = record.getControlfields();
            for (Iterator<ControlField> controlFieldIterator = controlFields.iterator(); controlFieldIterator.hasNext(); ) {
                ControlField controlField = controlFieldIterator.next();
                valuesMap.put(controlField.getTag(), controlField.getValue());
            }
            List<DataField> datafields = record.getDatafields();
            for (Iterator<DataField> dataFieldIterator = datafields.iterator(); dataFieldIterator.hasNext(); ) {
                DataField dataField = dataFieldIterator.next();
                String tag = dataField.getTag();
                List<SubField> subfields = dataField.getSubFields();
                for (Iterator<SubField> marcSubFieldIterator = subfields.iterator(); marcSubFieldIterator.hasNext(); ) {
                    SubField subField = marcSubFieldIterator.next();
                    valuesMap.put(tag + subField.getCode(), subField.getValue());
                }
            }
        }
        return valuesMap;
    }*/


    public String generateXML(Collection bibliographicRecords) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stringBuffer.append("\n");

        XStream xStream = new XStream();
        xStream.alias("collection", Collection.class);
        xStream.alias(LEADER, LeaderTag.class);
        xStream.alias("record", BibliographicRecord.class);
        xStream.alias("controlfield", ControlField.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.addImplicitCollection(BibliographicRecord.class, "datafields", DataField.class);
        xStream.addImplicitCollection(BibliographicRecord.class, "controlfields", ControlField.class);
        xStream.addImplicitCollection(Collection.class, "collection");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new SubFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        xStream.registerConverter(new LeaderConverter());
        String xml = xStream.toXML(bibliographicRecords);
        stringBuffer.append(xml);
        stringBuffer.append("\n");
        return stringBuffer.toString();
    }

    public String generateXMLWithoutCollectionTag(BibliographicRecord bibliographicRecord) {
        StringBuffer stringBuffer = new StringBuffer();
        //stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        //stringBuffer.append("<collection xmlns=\"http://www.loc.gov/MARC21/slim\">");
        //stringBuffer.append("\n");

        XStream xStream = new XStream();
        xStream.alias(RECORD, BibliographicRecord.class);
        xStream.alias(CONTROLFIELD, ControlField.class);
        xStream.alias(DATAFIELD, DataField.class);
        xStream.alias(SUBFIELD, SubField.class);
        xStream.alias(LEADER, LeaderTag.class);
        xStream.addImplicitCollection(BibliographicRecord.class, DATAFIELDS,DataField.class);
        xStream.addImplicitCollection(DataField.class, SUBFIELDS,SubField.class);
        xStream.addImplicitCollection(BibliographicRecord.class, CONTROLFIELDS,ControlField.class);
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        xStream.registerConverter(new LeaderConverter());
        String xml = xStream.toXML(bibliographicRecord);
        stringBuffer.append(xml);
        stringBuffer.append("\n");
       // stringBuffer.append("</collection>");
        return stringBuffer.toString();
    }

    /**
     * gets the errBuilder value
     *
     * @return
     */
    public String getErrBuilder() {
        return errBuilder.toString();
    }

    public int getErrCnt() {
        return errCnt;
    }

    public int getSuccessCnt() {
        return successCnt;
    }


}
